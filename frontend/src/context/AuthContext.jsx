import React, { createContext, useState, useContext, useCallback, useEffect } from 'react';
import api from '../api/axiosInstance';

const AuthContext = createContext(null);

const loadInitialAuth = () => {
  try {
    const storedAuth = localStorage.getItem('auth');
    if (storedAuth) return JSON.parse(storedAuth);

    const storedToken = localStorage.getItem('accessToken');
    const storedUser = localStorage.getItem('user');
    if (storedToken && storedUser) {
      return {
        accessToken: storedToken,
        refreshToken: localStorage.getItem('refreshToken') || null,
        user: JSON.parse(storedUser),
      };
    }
  } catch (e) {
    console.error('Failed to parse auth state from localStorage', e);
  }
  return { accessToken: null, refreshToken: null, user: null };
};

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState(loadInitialAuth);

  // ── Role Detection Logic ──────────────────────────────────────────────────
  const userRoles = auth.user?.roles || [];

  const hasRole = useCallback(
    (role) => {
      if (!userRoles) return false;
      if (Array.isArray(userRoles)) return userRoles.includes(role);
      if (userRoles instanceof Set) return userRoles.has(role);
      if (typeof userRoles === 'string') return userRoles === role;
      return false;
    },
    [userRoles]
  );

  const isAdmin = hasRole('ROLE_ADMIN');
  const isNurseryOwner = hasRole('ROLE_NURSERY_OWNER');
  const isCustomer = hasRole('ROLE_CUSTOMER');

  // Single source of truth for role-based dashboard path
  const getDashboardPath = useCallback(() => {
    if (isAdmin) return '/admin/dashboard';
    if (isNurseryOwner) return '/nursery/dashboard';
    if (isCustomer) return '/customer/dashboard';
    return '/customer/dashboard';
  }, [isAdmin, isNurseryOwner, isCustomer]);

  // ── Login Action ─────────────────────────────────────────────────────────
  const login = useCallback((data, rememberMe = true) => {
    // data structure: { accessToken, refreshToken, tokenType, expiresIn, user }
    const newAuth = {
      accessToken: data.accessToken,
      refreshToken: data.refreshToken || null,
      user: data.user || null,
    };

    setAuth(newAuth);

    if (rememberMe) {
      localStorage.setItem('accessToken', data.accessToken || '');
      if (data.refreshToken) localStorage.setItem('refreshToken', data.refreshToken);
      if (data.user) localStorage.setItem('user', JSON.stringify(data.user));
      localStorage.setItem('auth', JSON.stringify(newAuth));
    } else {
      sessionStorage.setItem('accessToken', data.accessToken || '');
      if (data.refreshToken) sessionStorage.setItem('refreshToken', data.refreshToken);
      if (data.user) sessionStorage.setItem('user', JSON.stringify(data.user));
      sessionStorage.setItem('auth', JSON.stringify(newAuth));
      // Ensure local storage is cleared
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('user');
      localStorage.removeItem('auth');
    }
  }, []);

  // ── Logout Action ────────────────────────────────────────────────────────
  const logout = useCallback(async () => {
    try {
      // Backend invalidates the refresh token record if available
      await api.post('/auth/logout');
    } catch (_) {
      // Ignore network / API failure on logout
    } finally {
      setAuth({ accessToken: null, refreshToken: null, user: null });
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('user');
      localStorage.removeItem('auth');
      sessionStorage.removeItem('accessToken');
      sessionStorage.removeItem('refreshToken');
      sessionStorage.removeItem('user');
      sessionStorage.removeItem('auth');
    }
  }, []);

  // Helper used by axios interceptor to update tokens silently
  const updateTokens = useCallback((newAccessToken, newRefreshToken) => {
    setAuth((prev) => {
      const updated = {
        ...prev,
        accessToken: newAccessToken,
        refreshToken: newRefreshToken ?? prev.refreshToken,
      };
      localStorage.setItem('auth', JSON.stringify(updated));
      return updated;
    });
    localStorage.setItem('accessToken', newAccessToken);
    if (newRefreshToken) localStorage.setItem('refreshToken', newRefreshToken);
  }, []);

  const isAuthenticated = Boolean(auth.accessToken);

  return (
    <AuthContext.Provider
      value={{
        ...auth,
        login,
        logout,
        isAuthenticated,
        isAdmin,
        isNurseryOwner,
        isCustomer,
        userRoles,
        getDashboardPath,
        updateTokens,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

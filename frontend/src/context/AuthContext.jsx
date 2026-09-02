import React, { createContext, useState, useContext, useCallback } from 'react';
import api from '../api/axiosInstance';

const AuthContext = createContext(null);

const loadInitialAuth = () => {
  try {
    const storedAuth = localStorage.getItem('auth');
    if (storedAuth) return JSON.parse(storedAuth);

    const storedToken = localStorage.getItem('accessToken');
    const storedUser = localStorage.getItem('user');
    if (storedToken && storedUser) {
      return { accessToken: storedToken, user: JSON.parse(storedUser), refreshToken: localStorage.getItem('refreshToken') };
    }
  } catch (e) {
    console.error('Failed to parse auth state from localStorage', e);
  }
  return { accessToken: null, user: null, refreshToken: null };
};

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState(loadInitialAuth);

  // ── Login: persist tokens and user ────────────────────────────────────────
  const login = useCallback((data) => {
    // data: { accessToken, refreshToken, tokenType, expiresIn, user }
    const newAuth = {
      accessToken: data.accessToken,
      refreshToken: data.refreshToken,
      user: data.user,
    };

    setAuth(newAuth);
    localStorage.setItem('accessToken', data.accessToken || '');
    if (data.refreshToken) localStorage.setItem('refreshToken', data.refreshToken);
    if (data.user) localStorage.setItem('user', JSON.stringify(data.user));
    localStorage.setItem('auth', JSON.stringify(newAuth));
  }, []);

  // ── Logout: tell the backend first, then clear local state ────────────────
  const logout = useCallback(async () => {
    try {
      // Backend invalidates the refresh token record
      await api.post('/auth/logout');
    } catch (_) {
      // Best-effort — always clear local state even if the call fails
    } finally {
      setAuth({ accessToken: null, refreshToken: null, user: null });
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('user');
      localStorage.removeItem('auth');
    }
  }, []);

  // ── Derived state ─────────────────────────────────────────────────────────
  const isAuthenticated = Boolean(auth.accessToken);
  const userRoles = auth.user?.roles || [];

  const hasRole = (role) =>
    Array.isArray(userRoles)
      ? userRoles.includes(role)
      : userRoles instanceof Set
      ? userRoles.has(role)
      : false;

  const isAdmin = hasRole('ROLE_ADMIN');
  const isNurseryOwner = hasRole('ROLE_NURSERY_OWNER');
  const isCustomer = hasRole('ROLE_CUSTOMER');

  // Single source of truth for role-based dashboard path
  const getDashboardPath = useCallback(() => {
    if (isAdmin) return '/admin/dashboard';
    if (isNurseryOwner) return '/nursery/dashboard';
    if (isCustomer) return '/customer/dashboard';
    return '/';
  }, [isAdmin, isNurseryOwner, isCustomer]);

  // Helper used by the axios interceptor (avoids circular import)
  const updateTokens = useCallback((newAccessToken, newRefreshToken) => {
    setAuth((prev) => {
      const updated = { ...prev, accessToken: newAccessToken, refreshToken: newRefreshToken ?? prev.refreshToken };
      localStorage.setItem('auth', JSON.stringify(updated));
      return updated;
    });
    localStorage.setItem('accessToken', newAccessToken);
    if (newRefreshToken) localStorage.setItem('refreshToken', newRefreshToken);
  }, []);

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
        getDashboardPath,
        updateTokens,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

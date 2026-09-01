import React, { createContext, useState, useEffect, useContext } from 'react';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState(() => {
    try {
      const storedToken = localStorage.getItem('accessToken');
      const storedUser = localStorage.getItem('user');
      const storedAuth = localStorage.getItem('auth');
      
      if (storedAuth) {
        return JSON.parse(storedAuth);
      } else if (storedToken && storedUser) {
        return { accessToken: storedToken, user: JSON.parse(storedUser) };
      }
    } catch (e) {
      console.error('Failed to parse auth state from localStorage', e);
    }
    return { accessToken: null, user: null, refreshToken: null };
  });

  const login = (data) => {
    // data: { accessToken, refreshToken, tokenType, expiresIn, user }
    const newAuth = {
      accessToken: data.accessToken,
      refreshToken: data.refreshToken,
      user: data.user,
    };

    setAuth(newAuth);
    if (data.accessToken) {
      localStorage.setItem('accessToken', data.accessToken);
    }
    if (data.refreshToken) {
      localStorage.setItem('refreshToken', data.refreshToken);
    }
    if (data.user) {
      localStorage.setItem('user', JSON.stringify(data.user));
    }
    localStorage.setItem('auth', JSON.stringify(newAuth));
  };

  const logout = () => {
    setAuth({ accessToken: null, refreshToken: null, user: null });
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
    localStorage.removeItem('auth');
  };

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

  const getDashboardPath = () => {
    if (isAdmin) return '/admin/dashboard';
    if (isNurseryOwner) return '/nursery/dashboard';
    return '/customer/dashboard';
  };

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
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);


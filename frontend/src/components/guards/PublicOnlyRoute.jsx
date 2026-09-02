import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

/**
 * PublicOnlyRoute ensures that already logged-in users cannot access
 * public authentication pages (Login, Register).
 * Logged-in users are automatically redirected to their role-specific dashboard.
 */
const PublicOnlyRoute = ({ children }) => {
  const { isAuthenticated, getDashboardPath } = useAuth();

  if (isAuthenticated) {
    return <Navigate to={getDashboardPath()} replace />;
  }

  return children;
};

export default PublicOnlyRoute;

import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const CustomerRoute = ({ children }) => {
  const { isAuthenticated, isCustomer, getDashboardPath } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  if (!isCustomer) {
    return <Navigate to={getDashboardPath()} replace />;
  }
  return children;
};

export default CustomerRoute;

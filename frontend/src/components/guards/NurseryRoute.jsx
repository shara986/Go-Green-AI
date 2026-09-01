import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const NurseryRoute = ({ children }) => {
  const { isAuthenticated, isNurseryOwner, getDashboardPath } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  if (!isNurseryOwner) {
    return <Navigate to={getDashboardPath()} replace />;
  }
  return children;
};

export default NurseryRoute;

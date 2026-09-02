import React, { useState } from 'react';
import { useNavigate, useLocation, Outlet } from 'react-router-dom';
import {
  FiGrid, FiShoppingBag, FiPackage, FiArchive,
  FiShoppingCart, FiUsers, FiBarChart2, FiUser
} from 'react-icons/fi';
import { useAuth } from '../../context/AuthContext';
import DashboardSidebar from '../dashboard/DashboardSidebar';
import DashboardTopNav from '../dashboard/DashboardTopNav';
import '../dashboard/shared.css';

// Nursery Links Configuration
const NURSERY_LINKS = [
  { label: 'Dashboard', to: '/nursery/dashboard', icon: <FiGrid size={16} /> },
  { label: 'My Nursery', to: '/nursery/my-nursery', icon: <FiShoppingBag size={16} /> },
  { label: 'Plants', to: '/nursery/plants', icon: <FiPackage size={16} /> },
  { label: 'Inventory', to: '/nursery/inventory', icon: <FiArchive size={16} /> },
  { label: 'Orders', to: '/nursery/orders', icon: <FiShoppingCart size={16} /> },
  { label: 'Customers', to: '/nursery/customers', icon: <FiUsers size={16} /> },
  { label: 'Reports', to: '/nursery/reports', icon: <FiBarChart2 size={16} /> },
  { label: 'Profile', to: '/nursery/profile', icon: <FiUser size={16} /> },
];

const NurseryLayout = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const location = useLocation();

  const userName = user?.name || user?.username || 'Nursery Owner';

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <div className="dashboard-layout">
      <DashboardSidebar
        links={NURSERY_LINKS}
        isOpen={sidebarOpen}
        onClose={() => setSidebarOpen(false)}
        onLogout={handleLogout}
        activePath={location.pathname}
      />

      <div className="dashboard-main">
        <DashboardTopNav
          userName={userName}
          onLogout={handleLogout}
          onToggleSidebar={() => setSidebarOpen((p) => !p)}
        />

        {/* Nursery Sub-Pages Render Here */}
        <div className="dashboard-content">
          <Outlet />
        </div>
      </div>
    </div>
  );
};

export default NurseryLayout;

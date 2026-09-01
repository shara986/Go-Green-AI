import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  FiGrid, FiShoppingBag, FiPackage, FiArchive,
  FiShoppingCart, FiUsers, FiBarChart2, FiUser
} from 'react-icons/fi';
import { useAuth } from '../../context/AuthContext';
import DashboardSidebar from '../../components/dashboard/DashboardSidebar';
import DashboardTopNav from '../../components/dashboard/DashboardTopNav';
import StatCard from '../../components/dashboard/StatCard';
import '../../components/dashboard/shared.css';
import './NurseryDashboard.css';
import api from '../../api/axiosInstance';

// Sidebar links for Nursery Owner
const NURSERY_LINKS = [
  { label: 'Dashboard', to: '/nursery/dashboard', icon: <FiGrid size={16} /> },
  { label: 'My Nursery', to: '/nursery', icon: <FiShoppingBag size={16} /> },
  { label: 'Plants', to: '/nursery/plants', icon: <FiPackage size={16} /> },
  { label: 'Inventory', to: '/nursery/inventory', icon: <FiArchive size={16} /> },
  { label: 'Orders', to: '/nursery/orders', icon: <FiShoppingCart size={16} /> },
  { label: 'Customers', to: '/nursery/customers', icon: <FiUsers size={16} /> },
  { label: 'Reports', to: '/nursery/reports', icon: <FiBarChart2 size={16} /> },
  { label: 'Profile', to: '/profile', icon: <FiUser size={16} /> },
];

const NurseryDashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [nurseryInfo, setNurseryInfo] = useState(null);
  const [plantCount, setPlantCount] = useState(null);
  const [orderCount, setOrderCount] = useState(null);
  const [loading, setLoading] = useState(true);

  const userName = user?.name || user?.username || 'Nursery Owner';

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);

      // Try to fetch nursery info for this owner
      try {
        const res = await api.get('/nursery/my');
        const data = res.data?.data || res.data;
        setNurseryInfo(data);
      } catch {
        try {
          // Alternate endpoint
          const res2 = await api.get('/nurseries/my');
          const data2 = res2.data?.data || res2.data;
          setNurseryInfo(data2);
        } catch {
          setNurseryInfo(null);
        }
      }

      // Try to fetch nursery plants
      try {
        const res = await api.get('/nursery/plants');
        const data = res.data?.data || res.data;
        const list = Array.isArray(data) ? data : data?.content;
        if (list) setPlantCount(list.length);
      } catch {
        setPlantCount(null);
      }

      // Try to fetch nursery orders
      try {
        const res = await api.get('/nursery/orders');
        const data = res.data?.data || res.data;
        const list = Array.isArray(data) ? data : data?.content;
        if (list) setOrderCount(list.length);
      } catch {
        setOrderCount(null);
      }

      setLoading(false);
    };

    fetchData();
  }, []);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  // Sections that dont have APIs yet
  const naPlaceholders = [
    { icon: '📊', title: 'Sales Summary', desc: 'Sales and revenue data — API not available yet.' },
    { icon: '📦', title: 'Stock Information', desc: 'Real-time stock levels — API not available yet.' },
    { icon: '👥', title: 'Customer Directory', desc: 'Customer management — API not available yet.' },
  ];

  return (
    <div className="dashboard-layout">
      <DashboardSidebar
        links={NURSERY_LINKS}
        isOpen={sidebarOpen}
        onClose={() => setSidebarOpen(false)}
        onLogout={handleLogout}
      />

      <div className="dashboard-main">
        <DashboardTopNav
          userName={userName}
          onLogout={handleLogout}
          onToggleSidebar={() => setSidebarOpen((p) => !p)}
        />

        <div className="dashboard-content">
          {/* Header */}
          <div className="nursery-db-header">
            <h1>Nursery Dashboard</h1>
            <p className="nursery-db-subtitle">Manage your nursery, plants and orders</p>
          </div>

          {/* Welcome Banner */}
          <div className="db-welcome-banner">
            <div>
              <h2>Welcome back, {userName}! 🌳</h2>
              <p>
                {nurseryInfo?.name
                  ? `Managing: ${nurseryInfo.name}`
                  : 'Your nursery management hub.'}
              </p>
            </div>
            <div className="db-welcome-emoji">🏡</div>
          </div>

          {loading ? (
            <div className="db-loading">
              <div className="db-spinner" />
              Loading nursery data…
            </div>
          ) : (
            <>
              {/* Nursery Overview Stats */}
              <h2 className="db-section-title">
                <FiShoppingBag size={16} /> Nursery Overview
              </h2>
              <div className="stat-cards-grid" style={{ marginBottom: 28 }}>
                {nurseryInfo ? (
                  <>
                    <StatCard
                      icon={<FiShoppingBag />}
                      title="Nursery Name"
                      value={nurseryInfo.name || 'N/A'}
                      color="#0f766e"
                      bgColor="#ccfbf1"
                    />
                    <StatCard
                      icon={<span>📍</span>}
                      title="Location"
                      value={nurseryInfo.city || nurseryInfo.location || 'N/A'}
                      color="#0284c7"
                      bgColor="#e0f2fe"
                    />
                    <StatCard
                      icon={<span>⭐</span>}
                      title="Rating"
                      value={nurseryInfo.rating ?? 'N/A'}
                      color="#d97706"
                      bgColor="#fef3c7"
                    />
                  </>
                ) : (
                  <div style={{ gridColumn: '1/-1' }}>
                    <p className="db-na">Nursery overview not available — API not connected yet.</p>
                  </div>
                )}
                <StatCard
                  icon={<FiPackage />}
                  title="Plant Inventory"
                  value={plantCount}
                  color="#16a34a"
                  bgColor="#dcfce7"
                />
                <StatCard
                  icon={<FiShoppingCart />}
                  title="Total Orders"
                  value={orderCount}
                  color="#7c3aed"
                  bgColor="#ede9fe"
                />
              </div>

              {/* Recent Orders */}
              <h2 className="db-section-title">
                <FiShoppingCart size={16} /> Recent Orders
              </h2>
              <div className="db-table-card" style={{ marginBottom: 28 }}>
                <div className="db-table-header">
                  <h3>Orders</h3>
                </div>
                <div style={{ padding: '20px 24px' }}>
                  <p className="db-na">Recent order details — API not available yet.</p>
                </div>
              </div>

              {/* N/A placeholder sections */}
              <h2 className="db-section-title">Additional Sections</h2>
              <div className="nursery-two-col">
                {naPlaceholders.map((item) => (
                  <div key={item.title} className="nursery-info-card">
                    <div className="nursery-info-icon">{item.icon}</div>
                    <h3>{item.title}</h3>
                    <p>{item.desc}</p>
                  </div>
                ))}
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default NurseryDashboard;

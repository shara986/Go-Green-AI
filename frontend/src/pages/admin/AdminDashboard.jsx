import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  FiGrid, FiUsers, FiShoppingBag, FiTag, FiPackage,
  FiFileText, FiSettings, FiBell, FiBarChart2
} from 'react-icons/fi';
import { useAuth } from '../../context/AuthContext';
import DashboardSidebar from '../../components/dashboard/DashboardSidebar';
import DashboardTopNav from '../../components/dashboard/DashboardTopNav';
import StatCard from '../../components/dashboard/StatCard';
import '../../components/dashboard/shared.css';
import './AdminDashboard.css';
import api from '../../api/axiosInstance';

// Sidebar links for Admin
const ADMIN_LINKS = [
  { label: 'Dashboard', to: '/admin/dashboard', icon: <FiGrid size={16} /> },
  { label: 'Users', to: '/admin/users', icon: <FiUsers size={16} /> },
  { label: 'Nurseries', to: '/admin/nurseries', icon: <FiShoppingBag size={16} /> },
  { label: 'Categories', to: '/admin/categories', icon: <FiTag size={16} /> },
  { label: 'Plants', to: '/admin/plants', icon: <FiPackage size={16} /> },
  { label: 'Orders', to: '/admin/orders', icon: <FiFileText size={16} /> },
  { label: 'Announcements', to: '/admin/announcements', icon: <FiBell size={16} /> },
  { label: 'Reports', to: '/admin/reports', icon: <FiBarChart2 size={16} /> },
  { label: 'Settings', to: '/admin/settings', icon: <FiSettings size={16} /> },
];

const AdminDashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  // Statistics state
  const [plantStats, setPlantStats] = useState(null);
  const [userCount, setUserCount] = useState(null);
  const [nurseryCount, setNurseryCount] = useState(null);
  const [categoryCount, setCategoryCount] = useState(null);
  const [loading, setLoading] = useState(true);
  const [recentUsers, setRecentUsers] = useState([]);

  const userName = user?.name || user?.username || 'Admin';

  useEffect(() => {
    const fetchAdminData = async () => {
      setLoading(true);

      // Fetch plant statistics
      try {
        const res = await api.get('/admin/plants/statistics');
        const data = res.data?.data || res.data;
        setPlantStats(data);
      } catch {
        setPlantStats(null);
      }

      // Fetch users list
      try {
        const res = await api.get('/admin/users');
        const data = res.data?.data || res.data;
        if (Array.isArray(data)) {
          setUserCount(data.length);
          setRecentUsers(data.slice(0, 5));
        } else if (data?.content) {
          setUserCount(data.totalElements ?? data.content.length);
          setRecentUsers(data.content.slice(0, 5));
        }
      } catch {
        setUserCount(null);
      }

      // Fetch nurseries
      try {
        const res = await api.get('/admin/nurseries');
        const data = res.data?.data || res.data;
        const list = Array.isArray(data) ? data : data?.content;
        if (list) setNurseryCount(list.length);
      } catch {
        setNurseryCount(null);
      }

      // Fetch categories
      try {
        const res = await api.get('/admin/categories');
        const data = res.data?.data || res.data;
        const list = Array.isArray(data) ? data : data?.content;
        if (list) setCategoryCount(list.length);
      } catch {
        setCategoryCount(null);
      }

      setLoading(false);
    };

    fetchAdminData();
  }, []);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const totalPlants = plantStats?.totalPlants ?? plantStats?.total ?? null;
  const activePlants = plantStats?.activePlants ?? plantStats?.active ?? null;
  const inactivePlants = plantStats?.inactivePlants ?? plantStats?.inactive ?? null;
  const plantsByCategory = plantStats?.plantsByCategory ?? null;

  return (
    <div className="dashboard-layout">
      <DashboardSidebar
        links={ADMIN_LINKS}
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
          {/* Page header */}
          <div>
            <h1 className="admin-db-page-title">Admin Dashboard</h1>
            <p className="admin-db-subtitle">
              Welcome back, <strong>{userName}</strong> — here's your system overview.
            </p>
          </div>

          {loading ? (
            <div className="db-loading">
              <div className="db-spinner" />
              Loading dashboard data…
            </div>
          ) : (
            <>
              {/* Plant Statistics */}
              <h2 className="db-section-title">
                <FiPackage size={16} /> Plant Statistics
              </h2>
              <div className="stat-cards-grid">
                <StatCard
                  icon={<FiPackage />}
                  title="Total Plants"
                  value={totalPlants}
                  color="#16a34a"
                  bgColor="#dcfce7"
                />
                <StatCard
                  icon={<span>✅</span>}
                  title="Active Plants"
                  value={activePlants}
                  color="#0284c7"
                  bgColor="#e0f2fe"
                />
                <StatCard
                  icon={<span>⛔</span>}
                  title="Inactive Plants"
                  value={inactivePlants}
                  color="#dc2626"
                  bgColor="#fee2e2"
                />
                <StatCard
                  icon={<FiTag />}
                  title="Categories"
                  value={categoryCount}
                  color="#7c3aed"
                  bgColor="#ede9fe"
                />
                <StatCard
                  icon={<FiUsers />}
                  title="Total Users"
                  value={userCount}
                  color="#d97706"
                  bgColor="#fef3c7"
                />
                <StatCard
                  icon={<FiShoppingBag />}
                  title="Nurseries"
                  value={nurseryCount}
                  color="#0f766e"
                  bgColor="#ccfbf1"
                />
              </div>

              {/* Plants by Category */}
              {plantsByCategory && Object.keys(plantsByCategory).length > 0 && (
                <>
                  <h2 className="db-section-title">
                    <FiTag size={16} /> Plants by Category
                  </h2>
                  <div className="admin-category-grid" style={{ marginBottom: 28 }}>
                    {Object.entries(plantsByCategory).map(([cat, count]) => (
                      <div key={cat} className="admin-category-row">
                        <span className="admin-category-name">{cat}</span>
                        <span className="admin-category-count">{count}</span>
                      </div>
                    ))}
                  </div>
                </>
              )}

              {/* Recent Users */}
              {recentUsers.length > 0 ? (
                <>
                  <h2 className="db-section-title">
                    <FiUsers size={16} /> Recent Users
                  </h2>
                  <div className="db-table-card" style={{ marginBottom: 24 }}>
                    <div className="db-table-wrap">
                      <table className="db-table">
                        <thead>
                          <tr>
                            <th>Name</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Status</th>
                          </tr>
                        </thead>
                        <tbody>
                          {recentUsers.map((u) => (
                            <tr key={u.id || u.userId || u.username}>
                              <td>{u.name || u.fullName || '—'}</td>
                              <td>{u.username || '—'}</td>
                              <td>{u.email || '—'}</td>
                              <td>
                                <span className="status-badge processing">
                                  {(Array.isArray(u.roles) ? u.roles[0] : u.role) || '—'}
                                </span>
                              </td>
                              <td>
                                <span className={`status-badge ${u.active !== false ? 'active' : 'inactive'}`}>
                                  {u.active !== false ? 'Active' : 'Inactive'}
                                </span>
                              </td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  </div>
                </>
              ) : (
                // Show N/A placeholder when no user API data
                <>
                  <h2 className="db-section-title">
                    <FiUsers size={16} /> Recent Users
                  </h2>
                  <p className="db-na">N/A — user data not available</p>
                </>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;

import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import {
  FiShoppingBag, FiPackage, FiShoppingCart, FiAlertCircle,
  FiPlus, FiRefreshCw, FiEye, FiClock, FiActivity, FiArchive
} from 'react-icons/fi';
import { useAuth } from '../../context/AuthContext';
import StatCard from '../../components/dashboard/StatCard';
import { EmptyState, LoadingSkeleton } from '../../components/common/UIState';
import api from '../../api/axiosInstance';
import './NurseryDashboardOverview.css';

const NurseryDashboardOverview = () => {
  const { user } = useAuth();
  const userName = user?.name || user?.username || 'Nursery Manager';
  
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    nurseryName: null,
    totalPlants: 0,
    totalInventory: 0,
    totalOrders: 0,
    pendingOrders: 0,
    lowStock: 0
  });

  const [recentOrders, setRecentOrders] = useState([]);
  const [recentActivity, setRecentActivity] = useState([]);

  useEffect(() => {
    // Mocking the data fetching since APIs might not have aggregate dashboard metrics
    const fetchDashboardData = async () => {
      setLoading(true);
      try {
        // Try getting nursery profile
        let nurseryName = 'My Nursery';
        try {
          const res = await api.get('/nursery/my');
          nurseryName = res.data?.data?.name || nurseryName;
        } catch (e) {
          // ignore
        }

        // Simulating metrics fetching
        setTimeout(() => {
          setStats({
            nurseryName,
            totalPlants: 42,
            totalInventory: 1250,
            totalOrders: 156,
            pendingOrders: 12,
            lowStock: 5
          });

          setRecentOrders([
            { id: 'ORD-8921', customer: 'Alice Smith', date: '2 hrs ago', status: 'Pending', total: 120.00 },
            { id: 'ORD-8920', customer: 'Bob Jones', date: '4 hrs ago', status: 'Processing', total: 45.50 },
            { id: 'ORD-8919', customer: 'Charlie Day', date: '1 day ago', status: 'Completed', total: 210.00 },
          ]);

          setRecentActivity([
            { id: 1, text: 'Added new plant "Monstera Deliciosa"', time: '2 hours ago', icon: <FiPackage /> },
            { id: 2, text: 'Updated inventory for "Snake Plant"', time: '4 hours ago', icon: <FiRefreshCw /> },
            { id: 3, text: 'Order #ORD-8919 marked as Completed', time: '1 day ago', icon: <FiShoppingCart /> },
          ]);

          setLoading(false);
        }, 800);

      } catch (err) {
        console.error("Error fetching dashboard data", err);
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  if (loading) {
    return (
      <div className="nursery-page">
        <h1 className="nursery-page-title">Dashboard</h1>
        <LoadingSkeleton type="card-grid" count={4} />
      </div>
    );
  }

  const getStatusBadge = (status) => {
    switch (status.toLowerCase()) {
      case 'pending': return 'badge-pending';
      case 'processing': return 'badge-processing';
      case 'completed': return 'badge-completed';
      case 'cancelled': return 'badge-cancelled';
      default: return 'badge-default';
    }
  };

  return (
    <div className="nursery-page nursery-overview">
      <div className="nursery-welcome-banner">
        <div className="welcome-content">
          <h2>Welcome back, {userName}! 👋</h2>
          <p>Here's what's happening at {stats.nurseryName} today.</p>
        </div>
        <div className="quick-actions-bar">
          <Link to="/nursery/plants" className="btn-primary btn-sm">
            <FiPlus /> Add Plant
          </Link>
          <Link to="/nursery/inventory" className="btn-secondary btn-sm">
            <FiRefreshCw /> Update Stock
          </Link>
          <Link to="/nursery/orders" className="btn-secondary btn-sm">
            <FiEye /> View Orders
          </Link>
        </div>
      </div>

      <div className="overview-stats-grid">
        <StatCard
          icon={<FiPackage />}
          title="Total Plants"
          value={stats.totalPlants}
          color="#0f766e"
          bgColor="#ccfbf1"
        />
        <StatCard
          icon={<FiArchive />} 
          title="Total Inventory"
          value={stats.totalInventory}
          color="#0284c7"
          bgColor="#e0f2fe"
        />
        <StatCard
          icon={<FiShoppingCart />}
          title="Total Orders"
          value={stats.totalOrders}
          color="#7c3aed"
          bgColor="#ede9fe"
        />
        <StatCard
          icon={<FiAlertCircle />}
          title="Pending Orders"
          value={stats.pendingOrders}
          color="#d97706"
          bgColor="#fef3c7"
        />
        <StatCard
          icon={<FiActivity />}
          title="Low Stock Items"
          value={stats.lowStock}
          color="#e11d48"
          bgColor="#ffe4e6"
        />
      </div>

      <div className="overview-content-grid">
        {/* Recent Orders Table */}
        <div className="overview-card">
          <div className="overview-card-header">
            <h3>Recent Orders</h3>
            <Link to="/nursery/orders" className="overview-link">View All</Link>
          </div>
          <div className="overview-card-body">
            {recentOrders.length > 0 ? (
              <div className="table-responsive">
                <table className="nursery-table">
                  <thead>
                    <tr>
                      <th>Order ID</th>
                      <th>Customer</th>
                      <th>Amount</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    {recentOrders.map(order => (
                      <tr key={order.id}>
                        <td className="fw-500">{order.id}</td>
                        <td>{order.customer}</td>
                        <td>${order.total.toFixed(2)}</td>
                        <td>
                          <span className={`status-badge ${getStatusBadge(order.status)}`}>
                            {order.status}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            ) : (
              <EmptyState title="No recent orders" message="You don't have any orders yet." />
            )}
          </div>
        </div>

        {/* Recent Activity Feed */}
        <div className="overview-card">
          <div className="overview-card-header">
            <h3>Recent Activity</h3>
          </div>
          <div className="overview-card-body">
            {recentActivity.length > 0 ? (
              <ul className="activity-feed">
                {recentActivity.map(activity => (
                  <li key={activity.id} className="activity-item">
                    <div className="activity-icon-wrap">
                      {activity.icon}
                    </div>
                    <div className="activity-details">
                      <p className="activity-text">{activity.text}</p>
                      <span className="activity-time"><FiClock size={12}/> {activity.time}</span>
                    </div>
                  </li>
                ))}
              </ul>
            ) : (
              <EmptyState title="No recent activity" message="Your activity feed is empty." />
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default NurseryDashboardOverview;

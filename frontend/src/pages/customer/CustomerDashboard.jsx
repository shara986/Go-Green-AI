import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  FiGrid, FiPackage, FiTag, FiShoppingCart,
  FiHeart, FiBookOpen, FiUser, FiArrowRight
} from 'react-icons/fi';
import { useAuth } from '../../context/AuthContext';
import DashboardSidebar from '../../components/dashboard/DashboardSidebar';
import DashboardTopNav from '../../components/dashboard/DashboardTopNav';
import StatCard from '../../components/dashboard/StatCard';
import '../../components/dashboard/shared.css';
import './CustomerDashboard.css';
import api from '../../api/axiosInstance';

// Sidebar links for Customer
const CUSTOMER_LINKS = [
  { label: 'Dashboard', to: '/customer/dashboard', icon: <FiGrid size={16} /> },
  { label: 'Plants', to: '/plants', icon: <FiPackage size={16} /> },
  { label: 'Categories', to: '/categories', icon: <FiTag size={16} /> },
  { label: 'Orders', to: '/orders', icon: <FiShoppingCart size={16} /> },
  { label: 'Cart', to: '/cart', icon: <FiShoppingCart size={16} /> },
  { label: 'Wishlist', to: '/wishlist', icon: <FiHeart size={16} /> },
  { label: 'Plant Diary', to: '/plant-diary', icon: <FiBookOpen size={16} /> },
  { label: 'Profile', to: '/profile', icon: <FiUser size={16} /> },
];

const CustomerDashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [plantCount, setPlantCount] = useState(null);
  const [categoryCount, setCategoryCount] = useState(null);
  const [orderCount, setOrderCount] = useState(null);
  const [wishlistCount, setWishlistCount] = useState(null);
  const [cartCount, setCartCount] = useState(null);
  const [loading, setLoading] = useState(true);

  const userName = user?.name || user?.username || 'Customer';

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);

      // Fetch available plants
      try {
        const res = await api.get('/plants');
        const data = res.data?.data || res.data;
        const list = Array.isArray(data) ? data : data?.content;
        if (list) setPlantCount(list.length);
      } catch {
        setPlantCount(null);
      }

      // Fetch categories
      try {
        const res = await api.get('/categories');
        const data = res.data?.data || res.data;
        const list = Array.isArray(data) ? data : data?.content;
        if (list) setCategoryCount(list.length);
      } catch {
        setCategoryCount(null);
      }

      // Fetch orders (may not exist yet)
      try {
        const res = await api.get('/orders');
        const data = res.data?.data || res.data;
        const list = Array.isArray(data) ? data : data?.content;
        if (list) setOrderCount(list.length);
      } catch {
        setOrderCount(null);
      }

      // Fetch wishlist
      try {
        const res = await api.get('/wishlist');
        const data = res.data?.data || res.data;
        const list = Array.isArray(data) ? data : data?.content;
        if (list) setWishlistCount(list.length);
      } catch {
        setWishlistCount(null);
      }

      // Fetch cart
      try {
        const res = await api.get('/cart');
        const data = res.data?.data || res.data;
        const items = data?.items || (Array.isArray(data) ? data : null);
        if (items) setCartCount(items.length);
      } catch {
        setCartCount(null);
      }

      setLoading(false);
    };

    fetchData();
  }, []);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  // Feature cards leading to real pages
  const featureCards = [
    {
      to: '/plants',
      icon: '🌿',
      iconBg: '#dcfce7',
      title: 'Browse Plants',
      desc: 'Explore our full plant catalogue and find your next green companion.',
    },
    {
      to: '/categories',
      icon: '🏷️',
      iconBg: '#e0f2fe',
      title: 'Categories',
      desc: 'Browse plants by type, climate, care level and more.',
    },
    {
      to: '/orders',
      icon: '📦',
      iconBg: '#fef3c7',
      title: 'My Orders',
      desc: 'Track and manage all your plant orders in one place.',
    },
    {
      to: '/wishlist',
      icon: '❤️',
      iconBg: '#fee2e2',
      title: 'Wishlist',
      desc: 'Save plants you love and come back to them anytime.',
    },
    {
      to: '/cart',
      icon: '🛒',
      iconBg: '#ede9fe',
      title: 'Cart',
      desc: 'Review items in your cart and proceed to checkout.',
    },
    {
      to: '/plant-diary',
      icon: '📓',
      iconBg: '#fdf2f8',
      title: 'Plant Diary',
      desc: 'Log care routines, notes and growth progress for your plants.',
    },
  ];

  return (
    <div className="dashboard-layout">
      <DashboardSidebar
        links={CUSTOMER_LINKS}
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
          {/* Welcome Banner */}
          <div className="db-welcome-banner">
            <div>
              <h2>Welcome back, {userName}! 🌱</h2>
              <p>Here's what's growing in your account today.</p>
            </div>
            <div className="db-welcome-emoji">🌺</div>
          </div>

          {/* Stats */}
          {loading ? (
            <div className="db-loading">
              <div className="db-spinner" />
              Loading your dashboard…
            </div>
          ) : (
            <>
              <div className="stat-cards-grid">
                <StatCard icon={<FiPackage />} title="Available Plants" value={plantCount} color="#16a34a" bgColor="#dcfce7" />
                <StatCard icon={<FiTag />} title="Categories" value={categoryCount} color="#0284c7" bgColor="#e0f2fe" />
                <StatCard icon={<FiShoppingCart />} title="My Orders" value={orderCount} color="#d97706" bgColor="#fef3c7" />
                <StatCard icon={<FiHeart />} title="Wishlist" value={wishlistCount} color="#dc2626" bgColor="#fee2e2" />
                <StatCard icon={<FiShoppingCart />} title="Cart Items" value={cartCount} color="#7c3aed" bgColor="#ede9fe" />
              </div>

              {/* Feature Cards */}
              <h2 className="db-section-title">Quick Access</h2>
              <div className="customer-feature-grid">
                {featureCards.map((card) => (
                  <Link key={card.to} to={card.to} className="customer-feature-card">
                    <div className="customer-feature-icon" style={{ background: card.iconBg }}>
                      {card.icon}
                    </div>
                    <div className="customer-feature-title">{card.title}</div>
                    <div className="customer-feature-desc">{card.desc}</div>
                    <span className="customer-feature-link">
                      Go to {card.title} <FiArrowRight size={12} />
                    </span>
                  </Link>
                ))}
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default CustomerDashboard;

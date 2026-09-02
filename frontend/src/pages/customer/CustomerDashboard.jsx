import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import {
  FiPackage, FiTag, FiShoppingCart,
  FiHeart, FiArrowRight
} from 'react-icons/fi';
import { useAuth } from '../../context/AuthContext';
import CustomerLayout from '../../components/customer/CustomerLayout';
import StatCard from '../../components/dashboard/StatCard';
import { fetchPlants, fetchCategories, fetchMyOrders, fetchCart } from '../../api/customerApi';
import '../../components/dashboard/shared.css';
import './CustomerDashboard.css';

const CustomerDashboard = () => {
  const { user } = useAuth();
  const [plantCount, setPlantCount] = useState(null);
  const [categoryCount, setCategoryCount] = useState(null);
  const [orderCount, setOrderCount] = useState(null);
  const [cartCount, setCartCount] = useState(null);
  const [loading, setLoading] = useState(true);

  const userName = user?.name || user?.username || 'Customer';

  useEffect(() => {
    let isMounted = true;

    const fetchData = async () => {
      setLoading(true);

      try {
        const plantsRes = await fetchPlants();
        const list = Array.isArray(plantsRes) ? plantsRes : plantsRes?.content || [];
        if (isMounted) setPlantCount(list.length);
      } catch (_) {
        if (isMounted) setPlantCount(0);
      }

      try {
        const categoriesRes = await fetchCategories();
        const list = Array.isArray(categoriesRes) ? categoriesRes : categoriesRes?.content || [];
        if (isMounted) setCategoryCount(list.length);
      } catch (_) {
        if (isMounted) setCategoryCount(0);
      }

      try {
        const ordersRes = await fetchMyOrders();
        const list = Array.isArray(ordersRes) ? ordersRes : ordersRes?.content || [];
        if (isMounted) setOrderCount(list.length);
      } catch (_) {
        if (isMounted) setOrderCount(0);
      }

      try {
        const cartRes = await fetchCart();
        const items = cartRes?.items || (Array.isArray(cartRes) ? cartRes : []);
        const count = cartRes?.totalItems !== undefined
          ? cartRes.totalItems
          : items.reduce((acc, item) => acc + (item.quantity || 1), 0);
        if (isMounted) setCartCount(count);
      } catch (_) {
        if (isMounted) setCartCount(0);
      }

      if (isMounted) setLoading(false);
    };

    fetchData();

    return () => {
      isMounted = false;
    };
  }, []);

  const featureCards = [
    {
      to: '/customer/plants',
      icon: '🌿',
      iconBg: '#dcfce7',
      title: 'Browse Plants',
      desc: 'Explore our full plant catalogue and find your next green companion.',
    },
    {
      to: '/customer/categories',
      icon: '🏷️',
      iconBg: '#e0f2fe',
      title: 'Categories',
      desc: 'Browse plants by type, climate, care level and more.',
    },
    {
      to: '/customer/orders',
      icon: '📦',
      iconBg: '#fef3c7',
      title: 'My Orders',
      desc: 'Track and manage all your plant orders in one place.',
    },
    {
      to: '/customer/wishlist',
      icon: '❤️',
      iconBg: '#fee2e2',
      title: 'Wishlist',
      desc: 'Save plants you love and come back to them anytime.',
    },
    {
      to: '/customer/cart',
      icon: '🛒',
      iconBg: '#ede9fe',
      title: 'Cart',
      desc: 'Review items in your cart and proceed to checkout.',
    },
    {
      to: '/customer/plant-diary',
      icon: '📓',
      iconBg: '#fdf2f8',
      title: 'Plant Diary',
      desc: 'Log care routines, notes and growth progress for your plants.',
    },
  ];

  return (
    <CustomerLayout>
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
            <StatCard icon={<FiHeart />} title="Wishlist" value="--" color="#dc2626" bgColor="#fee2e2" />
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
    </CustomerLayout>
  );
};

export default CustomerDashboard;

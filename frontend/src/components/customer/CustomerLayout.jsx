import React, { useState, useEffect, createContext, useContext, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  FiGrid, FiPackage, FiTag, FiShoppingCart,
  FiHeart, FiBookOpen, FiUser
} from 'react-icons/fi';
import { useAuth } from '../../context/AuthContext';
import DashboardSidebar from '../dashboard/DashboardSidebar';
import DashboardTopNav from '../dashboard/DashboardTopNav';
import { fetchCart } from '../../api/customerApi';
import '../dashboard/shared.css';

const CustomerContext = createContext({
  cartCount: 0,
  refreshCartCount: () => {},
});

export const useCustomer = () => useContext(CustomerContext);

const CustomerLayout = ({ children }) => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [cartCount, setCartCount] = useState(0);

  const userName = user?.name || user?.username || 'Customer';

  const refreshCartCount = useCallback(async () => {
    try {
      const cartData = await fetchCart();
      const items = cartData?.items || (Array.isArray(cartData) ? cartData : []);
      const count = cartData?.totalItems !== undefined
        ? cartData.totalItems
        : items.reduce((acc, item) => acc + (item.quantity || 1), 0);
      setCartCount(count);
    } catch (_) {
      setCartCount(0);
    }
  }, []);

  useEffect(() => {
    refreshCartCount();
  }, [refreshCartCount]);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const customerLinks = [
    { label: 'Dashboard', to: '/customer/dashboard', icon: <FiGrid size={16} /> },
    { label: 'Plants', to: '/customer/plants', icon: <FiPackage size={16} /> },
    { label: 'Categories', to: '/customer/categories', icon: <FiTag size={16} /> },
    { label: 'Orders', to: '/customer/orders', icon: <FiShoppingCart size={16} /> },
    { label: 'Cart', to: '/customer/cart', icon: <FiShoppingCart size={16} />, badge: cartCount },
    { label: 'Wishlist', to: '/customer/wishlist', icon: <FiHeart size={16} /> },
    { label: 'Plant Diary', to: '/customer/plant-diary', icon: <FiBookOpen size={16} /> },
    { label: 'Profile', to: '/customer/profile', icon: <FiUser size={16} /> },
  ];

  return (
    <CustomerContext.Provider value={{ cartCount, refreshCartCount }}>
      <div className="dashboard-layout">
        <DashboardSidebar
          links={customerLinks}
          isOpen={sidebarOpen}
          onClose={() => setSidebarOpen(false)}
          onLogout={handleLogout}
        />

        <div className="dashboard-main">
          <DashboardTopNav
            userName={userName}
            onLogout={handleLogout}
            onToggleSidebar={() => setSidebarOpen((prev) => !prev)}
          />

          <div className="dashboard-content">{children}</div>
        </div>
      </div>
    </CustomerContext.Provider>
  );
};

export default CustomerLayout;

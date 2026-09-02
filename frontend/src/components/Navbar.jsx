import React, { useState, useEffect } from 'react';
import { Link, NavLink, useLocation, useNavigate } from 'react-router-dom';
import { FiMenu, FiX, FiUser, FiLogOut, FiLayout } from 'react-icons/fi';
import { useAuth } from '../context/AuthContext';
import './Navbar.css';

const LeafIcon = ({ size = 20, className = '' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round" className={className}>
    <path d="M11 20A9 9 0 0 1 2.3 9A9 9 0 0 1 11 2c4.97 0 9 4.03 9 9 0 2.12-.74 4.07-1.97 5.61L21 20h-4l-1.39-1.39A8.93 8.93 0 0 1 11 20z" />
    <path d="M7 17L17 7" />
  </svg>
);

const Navbar = () => {
  const [menuOpen, setMenuOpen] = useState(false);
  const [scrolled, setScrolled] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();
  const { isAuthenticated, user, logout, isAdmin, isNurseryOwner, isCustomer, getDashboardPath } = useAuth();

  useEffect(() => { setMenuOpen(false); }, [location.pathname]);

  useEffect(() => {
    const handleScroll = () => setScrolled(window.scrollY > 20);
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const handleLogout = () => { 
    logout(); 
    navigate('/'); 
  };

  // Generate dynamic links based on role
  const getNavLinks = () => {
    if (!isAuthenticated) {
      return [
        { name: 'Home', to: '/' },
        { name: 'Plants', to: '/plants' },
        { name: 'Categories', to: '/categories' },
        { name: 'About', to: '/about' },
        { name: 'Contact', to: '/contact' }
      ];
    } else if (isCustomer) {
      return [
        { name: 'Home', to: '/' },
        { name: 'Plants', to: '/plants' },
        { name: 'Categories', to: '/categories' },
        { name: 'About', to: '/about' },
        { name: 'Contact', to: '/contact' },
        { name: 'Dashboard', to: '/customer/dashboard' },
        { name: 'Profile', to: '/customer/profile' }
      ];
    } else if (isNurseryOwner) {
      return [
        { name: 'Home', to: '/' },
        { name: 'Plants', to: '/plants' },
        { name: 'Categories', to: '/categories' },
        { name: 'About', to: '/about' },
        { name: 'Contact', to: '/contact' },
        { name: 'Nursery Dashboard', to: '/nursery/dashboard' },
        { name: 'Profile', to: '/nursery/profile' } // Ensure profile endpoint exists or fallback handled
      ];
    } else if (isAdmin) {
      return [
        { name: 'Admin Dashboard', to: '/admin/dashboard' },
        { name: 'Users', to: '/admin/users' },
        { name: 'Nurseries', to: '/admin/nurseries' },
        { name: 'Categories', to: '/admin/categories' },
        { name: 'Plants', to: '/admin/plants' },
        { name: 'Orders', to: '/admin/orders' },
        { name: 'Statistics', to: '/admin/statistics' }
      ];
    }
    return [];
  };

  const navLinks = getNavLinks();
  const dashboardPath = getDashboardPath();

  return (
    <header className={`navbar ${scrolled ? 'scrolled' : ''}`}>
      <div className="navbar-container container">
        <Link to="/" className="logo-brand">
          <div className="logo-icon-wrapper">
            <LeafIcon className="logo-icon" />
          </div>
          <span className="logo-text">GoGreen <span className="logo-highlight">AI</span></span>
        </Link>

        {/* Desktop Navigation */}
        <nav className="nav-links desktop">
          {navLinks.map((link) => (
            <NavLink
              key={link.name}
              to={link.to}
              className={({ isActive }) => (isActive ? 'nav-item active' : 'nav-item')}
            >
              {link.name}
            </NavLink>
          ))}
        </nav>

        {/* Right Auth Section */}
        <div className="auth-section desktop">
          {isAuthenticated ? (
            <div className="user-profile-menu">
              {isAdmin && (
                <Link to={dashboardPath} className="admin-badge">
                  <FiLayout /> {document.title.includes('Admin') ? 'Admin Panel' : 'Dashboard'}
                </Link>
              )}
              <span className="user-greeting">
                <FiUser className="user-icon" /> {user?.name || user?.username || 'User'}
              </span>
              <button onClick={handleLogout} className="btn-logout" title="Sign Out">
                <FiLogOut /> Logout
              </button>
            </div>
          ) : (
            <div className="auth-buttons">
              <Link to="/login" className="nav-login-btn">Login</Link>
              <Link to="/register" className="btn-primary nav-register-btn">Get Started</Link>
            </div>
          )}
        </div>

        {/* Mobile Hamburger Toggle */}
        <button
          className="menu-toggle mobile"
          onClick={() => setMenuOpen((prev) => !prev)}
          aria-label="Toggle navigation menu"
        >
          {menuOpen ? <FiX size={24} /> : <FiMenu size={24} />}
        </button>
      </div>

      {/* Mobile Menu Drawer */}
      <div className={`mobile-drawer ${menuOpen ? 'open' : ''}`}>
        <nav className="mobile-nav-links">
          {navLinks.map((link) => (
            <NavLink
              key={link.name}
              to={link.to}
              className={({ isActive }) => (isActive ? 'mobile-nav-item active' : 'mobile-nav-item')}
            >
              {link.name}
            </NavLink>
          ))}
        </nav>

        <div className="mobile-auth-section">
          {isAuthenticated ? (
            <div className="mobile-user-box">
              <div className="user-info">
                <FiUser /> {user?.name || user?.username || 'User'}
                {isAdmin && <span className="mobile-admin-tag">Admin</span>}
              </div>
              <Link
                to={dashboardPath}
                className="btn-primary full-width"
                style={{ marginBottom: 8, display: 'flex', justifyContent: 'center', gap: 6 }}
              >
                <FiLayout /> Dashboard
              </Link>
              <button onClick={handleLogout} className="btn-logout mobile-logout">
                <FiLogOut /> Logout
              </button>
            </div>
          ) : (
            <div className="mobile-auth-btns">
              <Link to="/login" className="btn-secondary full-width">Login</Link>
              <Link to="/register" className="btn-primary full-width">Get Started</Link>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};

export default Navbar;

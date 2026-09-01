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
  const { isAuthenticated, user, logout, isAdmin, getDashboardPath } = useAuth();

  useEffect(() => { setMenuOpen(false); }, [location.pathname]);

  useEffect(() => {
    const handleScroll = () => setScrolled(window.scrollY > 20);
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  // Always-visible public links
  const publicNavLinks = [
    { name: 'Home', to: '/' },
    { name: 'About', to: '/about' },
    { name: 'Contact', to: '/contact' },
  ];

  // Visible in navbar but require auth — redirect to /login if not authenticated
  const protectedNavLinks = [
    { name: 'Plants', to: '/plants' },
    { name: 'Categories', to: '/categories' },
  ];

  const handleLogout = () => { logout(); navigate('/'); };

  const handleProtectedClick = (e, to) => {
    if (!isAuthenticated) {
      e.preventDefault();
      navigate(`/login?redirect=${encodeURIComponent(to)}`);
    }
  };

  const dashboardPath = isAuthenticated && getDashboardPath ? getDashboardPath() : '/login';

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
          {publicNavLinks.map((link) => (
            <NavLink
              key={link.name}
              to={link.to}
              className={({ isActive }) => (isActive ? 'nav-item active' : 'nav-item')}
            >
              {link.name}
            </NavLink>
          ))}
          {protectedNavLinks.map((link) => (
            <NavLink
              key={link.name}
              to={link.to}
              onClick={(e) => handleProtectedClick(e, link.to)}
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
              <Link to={dashboardPath} className="admin-badge">
                <FiLayout /> Dashboard
              </Link>
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
              <Link to="/register" className="btn-primary nav-register-btn">Register</Link>
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
          {publicNavLinks.map((link) => (
            <NavLink
              key={link.name}
              to={link.to}
              className={({ isActive }) => (isActive ? 'mobile-nav-item active' : 'mobile-nav-item')}
            >
              {link.name}
            </NavLink>
          ))}
          {protectedNavLinks.map((link) => (
            <NavLink
              key={link.name}
              to={link.to}
              onClick={(e) => handleProtectedClick(e, link.to)}
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
              <Link to="/register" className="btn-primary full-width">Register</Link>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};

export default Navbar;

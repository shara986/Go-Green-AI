import React from 'react';
import { NavLink, useLocation } from 'react-router-dom';
import { FiLogOut } from 'react-icons/fi';
import '../dashboard/shared.css';

const LeafIcon = () => (
  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M11 20A9 9 0 0 1 2.3 9A9 9 0 0 1 11 2c4.97 0 9 4.03 9 9 0 2.12-.74 4.07-1.97 5.61L21 20h-4l-1.39-1.39A8.93 8.93 0 0 1 11 20z" />
    <path d="M7 17L17 7" />
  </svg>
);

/**
 * Shared sidebar component for all three dashboards.
 * @param {Object[]} links - Array of { label, to, icon }
 * @param {boolean} isOpen - Mobile open state
 * @param {function} onClose - Callback to close on mobile
 * @param {function} onLogout - Logout handler
 */
const DashboardSidebar = ({ links = [], isOpen, onClose, onLogout }) => {
  const location = useLocation();

  return (
    <>
      {/* Backdrop for mobile */}
      <div
        className={`db-sidebar-backdrop ${isOpen ? 'visible' : ''}`}
        onClick={onClose}
      />

      <aside className={`db-sidebar ${isOpen ? 'open' : ''}`}>
        {/* Brand */}
        <div className="db-sidebar-brand">
          <div className="brand-icon">
            <LeafIcon />
          </div>
          <span>
            GoGreen&nbsp;<span className="brand-highlight">AI</span>
          </span>
        </div>

        {/* Navigation links */}
        <nav className="db-sidebar-nav">
          {links.map(({ label, to, icon }) => (
            <NavLink
              key={to}
              to={to}
              className={({ isActive }) =>
                `db-nav-item${isActive || location.pathname === to ? ' active' : ''}`
              }
              onClick={onClose}
            >
              {icon && <span className="nav-icon">{icon}</span>}
              {label}
            </NavLink>
          ))}
        </nav>

        {/* Logout */}
        <div className="db-sidebar-footer">
          <button className="db-logout-btn" onClick={onLogout}>
            <FiLogOut size={16} />
            Logout
          </button>
        </div>
      </aside>
    </>
  );
};

export default DashboardSidebar;

import React from 'react';
import { FiSearch, FiBell, FiLogOut, FiMenu } from 'react-icons/fi';
import '../dashboard/shared.css';

/**
 * Shared top navigation bar for all three dashboards.
 */
const DashboardTopNav = ({ userName, onLogout, onToggleSidebar }) => {
  const initials = userName
    ? userName.split(' ').map((n) => n[0]).slice(0, 2).join('').toUpperCase()
    : 'U';

  return (
    <header className="db-topnav">
      <div className="db-topnav-left">
        <button className="db-sidebar-toggle" onClick={onToggleSidebar} aria-label="Toggle Sidebar">
          <FiMenu size={22} />
        </button>
        <div className="db-topnav-search">
          <FiSearch size={15} />
          <input type="text" placeholder="Search…" aria-label="Search" />
        </div>
      </div>

      <div className="db-topnav-right">
        <button className="db-topnav-icon-btn" aria-label="Notifications">
          <FiBell size={20} />
          <span className="db-badge" />
        </button>

        <div className="db-user-pill">
          <div className="db-avatar">{initials}</div>
          <span className="db-username">{userName || 'User'}</span>
        </div>

        <button className="db-topnav-logout" onClick={onLogout}>
          <FiLogOut size={14} />
          Logout
        </button>
      </div>
    </header>
  );
};

export default DashboardTopNav;

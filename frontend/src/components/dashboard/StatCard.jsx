import React from 'react';

/**
 * Reusable stat card for all three dashboards.
 * Shows "N/A" when value is null/undefined.
 */
const StatCard = ({ icon, title, value, sub, color = '#16a34a', bgColor = '#dcfce7' }) => {
  const displayValue = (value === null || value === undefined) ? 'N/A' : value;

  return (
    <div className="stat-card">
      <div className="stat-card-icon" style={{ background: bgColor, color }}>
        {icon}
      </div>
      <div className="stat-card-value">{displayValue}</div>
      <div className="stat-card-title">{title}</div>
      {sub && <div className="stat-card-sub">{sub}</div>}
    </div>
  );
};

export default StatCard;

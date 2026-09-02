import React from 'react';
import { Link } from 'react-router-dom';
import { FiAlertCircle, FiInbox, FiRefreshCw, FiLock } from 'react-icons/fi';
import './UIState.css';

export const LoadingSkeleton = ({ type = 'cards', count = 4 }) => {
  if (type === 'table') {
    return (
      <div className="skeleton-wrapper">
        {Array.from({ length: count }).map((_, i) => (
          <div key={i} className="skeleton-row" />
        ))}
      </div>
    );
  }

  return (
    <div className="skeleton-grid">
      {Array.from({ length: count }).map((_, i) => (
        <div key={i} className="skeleton-card">
          <div className="skeleton-image" />
          <div className="skeleton-title" />
          <div className="skeleton-line short" />
          <div className="skeleton-line" />
        </div>
      ))}
    </div>
  );
};

export const EmptyState = ({
  icon: Icon = FiInbox,
  title = 'No Data Available',
  message = 'There are currently no items to display.',
  actionText,
  actionLink,
  onAction,
}) => {
  return (
    <div className="ui-empty-state">
      <div className="empty-icon-box">
        <Icon size={36} />
      </div>
      <h3 className="empty-title">{title}</h3>
      <p className="empty-message">{message}</p>
      {actionText && actionLink && (
        <Link to={actionLink} className="empty-action-btn">
          {actionText}
        </Link>
      )}
      {actionText && onAction && !actionLink && (
        <button onClick={onAction} className="empty-action-btn">
          {actionText}
        </button>
      )}
    </div>
  );
};

export const ErrorState = ({ error, onRetry }) => {
  const status = error?.response?.status;

  let title = 'Something went wrong';
  let message = 'Something went wrong. Please try again.';
  let showLogin = false;

  if (status === 401) {
    title = 'Session Expired';
    message = 'Your session has expired. Please login again.';
    showLogin = true;
  } else if (status === 403) {
    title = 'Access Denied';
    message = "You don't have permission to access this page.";
  } else if (status === 404) {
    title = 'Not Found';
    message = 'The requested item was not found.';
  } else if (!error?.response && error?.message === 'Network Error') {
    title = 'Connection Error';
    message = 'Unable to connect to the server. Please check your internet connection.';
  } else if (error?.response?.data?.message) {
    message = error.response.data.message;
  }

  return (
    <div className="ui-error-state">
      <div className="error-icon-box">
        {status === 401 || status === 403 ? <FiLock size={32} /> : <FiAlertCircle size={32} />}
      </div>
      <h3 className="error-title">{title}</h3>
      <p className="error-message">{message}</p>
      <div className="error-actions">
        {showLogin && (
          <Link to="/login" className="error-btn primary-btn">
            Go to Login
          </Link>
        )}
        {onRetry && (
          <button onClick={onRetry} className="error-btn secondary-btn">
            <FiRefreshCw size={14} /> Try Again
          </button>
        )}
      </div>
    </div>
  );
};

export const OrderStatusBadge = ({ status }) => {
  const normalized = (status || '').toUpperCase();

  let className = 'status-badge default';

  switch (normalized) {
    case 'PENDING':
      className = 'status-badge pending';
      break;
    case 'CONFIRMED':
    case 'PROCESSING':
      className = 'status-badge processing';
      break;
    case 'SHIPPED':
      className = 'status-badge shipped';
      break;
    case 'DELIVERED':
      className = 'status-badge delivered';
      break;
    case 'CANCELLED':
      className = 'status-badge cancelled';
      break;
    default:
      break;
  }

  return <span className={className}>{normalized || 'UNKNOWN'}</span>;
};

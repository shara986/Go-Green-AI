import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import {
  FiArrowLeft, FiPackage, FiCalendar,
  FiMapPin, FiPhone, FiCreditCard, FiXCircle
} from 'react-icons/fi';
import CustomerLayout from '../../components/customer/CustomerLayout';
import { LoadingSkeleton, EmptyState, ErrorState, OrderStatusBadge } from '../../components/common/UIState';
import { fetchOrderById, cancelOrder } from '../../api/customerApi';
import './CustomerOrderDetailPage.css';

const CustomerOrderDetailPage = () => {
  const { orderId } = useParams();
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [cancelling, setCancelling] = useState(false);
  const [toastMessage, setToastMessage] = useState(null);

  const showToast = (msg) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3000);
  };

  const loadOrder = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchOrderById(orderId);
      setOrder(data);
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadOrder();
  }, [orderId]);

  const handleCancelOrder = async () => {
    if (!window.confirm('Are you sure you want to cancel this order?')) return;
    setCancelling(true);
    try {
      const updated = await cancelOrder(orderId);
      setOrder(updated);
      showToast('Order cancelled successfully.');
    } catch (err) {
      showToast(err?.response?.data?.message || 'Failed to cancel order.');
    } finally {
      setCancelling(false);
    }
  };

  if (loading) {
    return (
      <CustomerLayout>
        <div className="order-detail-container">
          <LoadingSkeleton type="table" count={5} />
        </div>
      </CustomerLayout>
    );
  }

  if (error) {
    return (
      <CustomerLayout>
        <div className="order-detail-container">
          <ErrorState error={error} onRetry={loadOrder} />
        </div>
      </CustomerLayout>
    );
  }

  if (!order) {
    return (
      <CustomerLayout>
        <div className="order-detail-container">
          <EmptyState
            title="Order Not Found"
            message="The requested order was not found."
            actionText="Back to Orders"
            actionLink="/customer/orders"
          />
        </div>
      </CustomerLayout>
    );
  }

  const items = order.items || [];
  const formattedDate = order.createdAt
    ? new Date(order.createdAt).toLocaleString(undefined, {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      })
    : 'N/A';

  const isCancelable = order.status === 'PENDING';

  return (
    <CustomerLayout>
      <div className="order-detail-container">
        {/* Navigation top */}
        <div className="order-top-bar">
          <Link to="/customer/orders" className="back-link-btn">
            <FiArrowLeft size={16} /> Back to Orders
          </Link>

          {isCancelable && (
            <button
              className="cancel-order-btn"
              disabled={cancelling}
              onClick={handleCancelOrder}
            >
              <FiXCircle size={15} />
              {cancelling ? 'Cancelling…' : 'Cancel Order'}
            </button>
          )}
        </div>

        {/* Toast */}
        {toastMessage && <div className="toast-notification">{toastMessage}</div>}

        {/* Order Header Card */}
        <div className="order-summary-header-card">
          <div>
            <span className="order-id-label">Order Reference</span>
            <h1 className="order-number-title">#{order.orderNumber || order.id}</h1>
            <p className="order-meta-info">
              <FiCalendar size={14} /> Placed on {formattedDate}
            </p>
          </div>
          <div className="order-header-status">
            <OrderStatusBadge status={order.status} />
          </div>
        </div>

        <div className="order-detail-grid">
          {/* Left Column: Items */}
          <div className="order-items-card">
            <h3 className="section-title">
              <FiPackage size={18} /> Ordered Items ({items.length})
            </h3>
            <div className="items-list">
              {items.map((item, idx) => (
                <div key={item.id || idx} className="order-item-row">
                  <img
                    src={item.plantImageUrl || 'https://images.unsplash.com/photo-1545241047-6083a3684587?w=150&auto=format&fit=crop&q=60'}
                    alt={item.plantName}
                    className="item-thumbnail"
                    onError={(e) => {
                      e.target.onerror = null;
                      e.target.src = 'https://images.unsplash.com/photo-1545241047-6083a3684587?w=150&auto=format&fit=crop&q=60';
                    }}
                  />
                  <div className="item-details">
                    <h4 className="item-name">{item.plantName || 'Plant'}</h4>
                    <p className="item-price">
                      ${Number(item.unitPrice || 0).toFixed(2)} x {item.quantity || 1}
                    </p>
                  </div>
                  <div className="item-subtotal">
                    ${Number(item.subtotal || (item.unitPrice * item.quantity) || 0).toFixed(2)}
                  </div>
                </div>
              ))}
            </div>

            {/* Financial summary breakdown */}
            <div className="order-totals-box">
              <div className="total-row">
                <span>Subtotal</span>
                <span>${Number(order.subtotal || order.totalAmount || 0).toFixed(2)}</span>
              </div>
              {order.shippingFee !== undefined && order.shippingFee > 0 && (
                <div className="total-row">
                  <span>Shipping Fee</span>
                  <span>${Number(order.shippingFee).toFixed(2)}</span>
                </div>
              )}
              <div className="total-row final">
                <span>Total Amount</span>
                <span>${Number(order.totalAmount || 0).toFixed(2)}</span>
              </div>
            </div>
          </div>

          {/* Right Column: Delivery & Payment info */}
          <div className="order-sidebar-col">
            {/* Delivery Info */}
            <div className="info-card">
              <h3 className="section-title">
                <FiMapPin size={18} /> Delivery Details
              </h3>
              <div className="info-content">
                <div className="info-item">
                  <strong>Shipping Address:</strong>
                  <p>{order.shippingAddress || 'No address provided.'}</p>
                </div>
                {order.contactPhone && (
                  <div className="info-item">
                    <FiPhone size={14} /> <span>{order.contactPhone}</span>
                  </div>
                )}
                {order.nurseryName && (
                  <div className="info-item">
                    <strong>Nursery:</strong> <span>{order.nurseryName}</span>
                  </div>
                )}
                {order.notes && (
                  <div className="info-item">
                    <strong>Notes:</strong> <p>{order.notes}</p>
                  </div>
                )}
              </div>
            </div>

            {/* Payment Info */}
            <div className="info-card">
              <h3 className="section-title">
                <FiCreditCard size={18} /> Payment Details
              </h3>
              <div className="info-content">
                <div className="info-item flex-between">
                  <span>Method:</span>
                  <strong>{order.paymentMethod || 'Credit / Debit Card'}</strong>
                </div>
                <div className="info-item flex-between">
                  <span>Payment Status:</span>
                  <span className="payment-status-tag">{order.paymentStatus || 'UNPAID'}</span>
                </div>
                {order.transactionId && (
                  <div className="info-item flex-between">
                    <span>Transaction ID:</span>
                    <span className="font-mono">{order.transactionId}</span>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </CustomerLayout>
  );
};

export default CustomerOrderDetailPage;

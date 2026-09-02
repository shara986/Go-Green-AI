import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FiEye, FiPackage, FiCalendar, FiDollarSign } from 'react-icons/fi';
import CustomerLayout from '../../components/customer/CustomerLayout';
import { LoadingSkeleton, EmptyState, ErrorState, OrderStatusBadge } from '../../components/common/UIState';
import { fetchMyOrders } from '../../api/customerApi';
import './CustomerOrdersPage.css';

const CustomerOrdersPage = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadOrders = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchMyOrders();
      const list = Array.isArray(data) ? data : data?.content || [];
      setOrders(list);
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadOrders();
  }, []);

  return (
    <CustomerLayout>
      <div className="orders-page-container">
        <div className="orders-header">
          <h1 className="orders-title">My Orders</h1>
          <p className="orders-subtitle">Track and review all your plant purchases and order history.</p>
        </div>

        {loading ? (
          <LoadingSkeleton type="table" count={5} />
        ) : error ? (
          <ErrorState error={error} onRetry={loadOrders} />
        ) : orders.length === 0 ? (
          <EmptyState
            icon={FiPackage}
            title="You haven't placed any orders yet."
            message="Explore our plant catalog and add your favorite green companions to your home!"
            actionText="Explore Plants"
            actionLink="/customer/plants"
          />
        ) : (
          <div className="orders-card">
            <div className="orders-table-wrap">
              <table className="orders-table">
                <thead>
                  <tr>
                    <th>Order #</th>
                    <th>Date</th>
                    <th>Items</th>
                    <th>Total</th>
                    <th>Payment</th>
                    <th>Status</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {orders.map((order) => {
                    const itemCount = order.items
                      ? order.items.reduce((sum, item) => sum + (item.quantity || 1), 0)
                      : 0;

                    const formattedDate = order.createdAt
                      ? new Date(order.createdAt).toLocaleDateString(undefined, {
                          year: 'numeric',
                          month: 'short',
                          day: 'numeric',
                        })
                      : 'N/A';

                    return (
                      <tr key={order.id}>
                        <td>
                          <span className="order-number font-mono">
                            {order.orderNumber || order.id?.substring(0, 8)}
                          </span>
                        </td>
                        <td>
                          <span className="order-date-text">
                            <FiCalendar size={13} style={{ marginRight: 4 }} />
                            {formattedDate}
                          </span>
                        </td>
                        <td>
                          <span className="order-items-summary">
                            {itemCount} {itemCount === 1 ? 'item' : 'items'}
                          </span>
                        </td>
                        <td>
                          <span className="order-total-amount">
                            ${Number(order.totalAmount || 0).toFixed(2)}
                          </span>
                        </td>
                        <td>
                          <span className="payment-status-tag">
                            {order.paymentStatus || 'UNPAID'}
                          </span>
                        </td>
                        <td>
                          <OrderStatusBadge status={order.status} />
                        </td>
                        <td>
                          <Link
                            to={`/customer/orders/${order.id}`}
                            className="order-view-btn"
                          >
                            <FiEye size={14} /> Details
                          </Link>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        )}
      </div>
    </CustomerLayout>
  );
};

export default CustomerOrdersPage;

import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { FiArrowLeft, FiMapPin, FiPhone, FiMail, FiUser, FiPackage, FiCheck, FiPrinter } from 'react-icons/fi';
import api from '../../api/axiosInstance';
import { LoadingSkeleton } from '../../components/common/UIState';
import './NurseryOrderDetailPage.css';

const NurseryOrderDetailPage = () => {
  const { orderId } = useParams();
  const [loading, setLoading] = useState(true);
  const [order, setOrder] = useState(null);
  const [updating, setUpdating] = useState(false);

  useEffect(() => {
    const fetchOrderDetails = async () => {
      setLoading(true);
      try {
        const res = await api.get(`/nursery/orders/${orderId}`);
        setOrder(res.data?.data || res.data);
      } catch (err) {
        // Mock data logic for UI building based on params
        setOrder({
          id: orderId || 'ORD-8921',
          date: '2023-11-21 14:30',
          status: 'Pending',
          paymentMethod: 'Credit Card',
          paymentStatus: 'Paid',
          customer: {
            name: 'Alice Smith',
            email: 'alice.smith@example.com',
            phone: '+1 (555) 012-3456',
            shippingAddress: '123 Garden Lane, Roseville, CA 90210'
          },
          items: [
            { id: 1, name: 'Monstera Deliciosa', price: 45.00, qty: 1, image: '' },
            { id: 2, name: 'Snake Plant', price: 25.00, qty: 2, image: '' }
          ],
          subtotal: 95.00,
          shipping: 10.00,
          tax: 15.00,
          total: 120.00
        });
      } finally {
        setLoading(false);
      }
    };
    fetchOrderDetails();
  }, [orderId]);

  const handleStatusChange = async (newStatus) => {
    setUpdating(true);
    try {
      // Simulate API call for now (or make real call if endpoint handles proper DTO)
      await new Promise(resolve => setTimeout(resolve, 800));
      setOrder(prev => ({ ...prev, status: newStatus }));
    } catch (err) {
      alert("Failed to update status.");
    } finally {
      setUpdating(false);
    }
  };

  const getStatusBadge = (status) => {
    switch (status.toLowerCase()) {
      case 'pending': return 'badge-pending';
      case 'processing': return 'badge-processing';
      case 'completed': return 'badge-completed';
      case 'cancelled': return 'badge-cancelled';
      default: return 'badge-default';
    }
  };

  if (loading) {
    return (
      <div className="nursery-page">
        <h1 className="nursery-page-title">Order Details</h1>
        <LoadingSkeleton type="table" count={3} />
      </div>
    );
  }

  if (!order) return <div className="nursery-page">Order not found</div>;

  return (
    <div className="nursery-page nursery-order-detail">
      <div className="page-header-flex">
        <div>
          <Link to="/nursery/orders" className="back-link">
            <FiArrowLeft /> Back to Orders
          </Link>
          <h1 className="nursery-page-title detail-title">
            Order #{order.id}
            <span className={`status-badge detail-badge ${getStatusBadge(order.status)}`}>
              {order.status}
            </span>
          </h1>
          <p className="order-date"><FiClock style={{display: 'none'}} /> Placed on {order.date}</p>
        </div>
        
        <div className="order-actions">
          <button className="btn-secondary btn-sm" onClick={() => window.print()}>
            <FiPrinter /> Print Label
          </button>
          
          <div className="status-dropdown">
            <select 
              value={order.status}
              onChange={(e) => handleStatusChange(e.target.value)}
              disabled={updating}
              className={`select-status ${order.status.toLowerCase()}`}
            >
              <option value="Pending">Mark as Pending</option>
              <option value="Processing">Mark as Processing</option>
              <option value="Completed">Mark as Completed</option>
              <option value="Cancelled">Mark as Cancelled</option>
            </select>
          </div>
        </div>
      </div>

      <div className="order-grid">
        <div className="order-main-col">
          <div className="detail-card">
            <div className="detail-card-header">
              <h3><FiPackage /> Ordered Items ({order.items.reduce((s, i) => s + i.qty, 0)})</h3>
            </div>
            <div className="detail-card-body p-0">
              <table className="nursery-table order-items-table">
                <thead>
                  <tr>
                    <th>Item</th>
                    <th>Price</th>
                    <th align="center">Qty</th>
                    <th align="right">Total</th>
                  </tr>
                </thead>
                <tbody>
                  {order.items.map(item => (
                    <tr key={item.id}>
                      <td>
                        <div className="item-cell">
                          <div className="table-img-box mini">
                            {item.image ? <img src={item.image} alt={item.name} /> : <div className="placeholder" />}
                          </div>
                          <span className="fw-500">{item.name}</span>
                        </div>
                      </td>
                      <td>${item.price.toFixed(2)}</td>
                      <td align="center">{item.qty}</td>
                      <td align="right" className="fw-500">${(item.price * item.qty).toFixed(2)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
          
          <div className="detail-card">
            <div className="detail-card-header">
              <h3>Payment Summary</h3>
              <span className="payment-status">{order.paymentStatus} · {order.paymentMethod}</span>
            </div>
            <div className="detail-card-body summary-body">
              <div className="summary-row">
                <span>Subtotal</span>
                <span>${order.subtotal.toFixed(2)}</span>
              </div>
              <div className="summary-row">
                <span>Shipping</span>
                <span>${order.shipping.toFixed(2)}</span>
              </div>
              <div className="summary-row">
                <span>Tax</span>
                <span>${order.tax.toFixed(2)}</span>
              </div>
              <div className="summary-row total-row">
                <span>Total</span>
                <span>${order.total.toFixed(2)}</span>
              </div>
            </div>
          </div>
        </div>

        <div className="order-side-col">
          <div className="detail-card">
            <div className="detail-card-header">
              <h3>Customer Information</h3>
            </div>
            <div className="detail-card-body customer-card">
              <div className="customer-info-row">
                <FiUser className="c-icon" />
                <div>
                  <strong>{order.customer.name}</strong>
                  <p>Customer since 2023</p>
                </div>
              </div>
              <hr />
              <div className="customer-contact">
                <p><FiMail /> <a href={`mailto:${order.customer.email}`}>{order.customer.email}</a></p>
                <p><FiPhone /> <a href={`tel:${order.customer.phone}`}>{order.customer.phone}</a></p>
              </div>
            </div>
          </div>

          <div className="detail-card">
            <div className="detail-card-header">
              <h3>Shipping Address</h3>
            </div>
            <div className="detail-card-body">
              <div className="shipping-address-wrap">
                <FiMapPin className="c-icon" />
                <p>{order.customer.shippingAddress}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NurseryOrderDetailPage;

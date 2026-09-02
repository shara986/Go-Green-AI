import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FiSearch, FiFilter, FiExternalLink, FiClock, FiCheckCircle, FiPackage, FiXCircle } from 'react-icons/fi';
import api from '../../api/axiosInstance';
import { LoadingSkeleton, EmptyState } from '../../components/common/UIState';
import StatCard from '../../components/dashboard/StatCard';
import './NurseryOrdersPage.css';

const NurseryOrdersPage = () => {
  const [loading, setLoading] = useState(true);
  const [orders, setOrders] = useState([]);
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  
  const [stats, setStats] = useState({ pending: 0, processing: 0, completed: 0, cancelled: 0 });

  useEffect(() => {
    const fetchOrders = async () => {
      setLoading(true);
      try {
        const res = await api.get('/nursery/orders');
        const data = res.data?.data || res.data;
        const list = Array.isArray(data) ? data : data?.content || [];
        
        if (list.length === 0) throw new Error("empty data");
        
        setOrders(list);
      } catch (err) {
        // Mock data logic for UI building since APIs might not be connected yet
        setOrders([
          { id: 'ORD-8921', customer: 'Alice Smith', items: 3, date: '2023-11-21', total: 120.00, status: 'Pending' },
          { id: 'ORD-8920', customer: 'Bob Jones', items: 1, date: '2023-11-21', total: 45.50, status: 'Processing' },
          { id: 'ORD-8919', customer: 'Charlie Day', items: 5, date: '2023-11-20', total: 210.00, status: 'Completed' },
          { id: 'ORD-8918', customer: 'Diana Rust', items: 2, date: '2023-11-19', total: 60.00, status: 'Cancelled' },
          { id: 'ORD-8917', customer: 'Evan Wright', items: 1, date: '2023-11-19', total: 35.00, status: 'Completed' }
        ]);
      } finally {
        setLoading(false);
      }
    };
    fetchOrders();
  }, []);

  useEffect(() => {
    let pending = 0, processing = 0, completed = 0, cancelled = 0;
    orders.forEach(o => {
      const status = o.status.toLowerCase();
      if (status === 'pending') pending++;
      else if (status === 'processing') processing++;
      else if (status === 'completed') completed++;
      else if (status === 'cancelled') cancelled++;
    });
    setStats({ pending, processing, completed, cancelled });
  }, [orders]);

  const filteredOrders = orders.filter(o => {
    const matchesSearch = o.id.toLowerCase().includes(search.toLowerCase()) || 
                          o.customer.toLowerCase().includes(search.toLowerCase());
    const matchesStatus = statusFilter ? o.status === statusFilter : true;
    return matchesSearch && matchesStatus;
  });

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
        <h1 className="nursery-page-title">Orders Management</h1>
        <div className="controls-bar"><LoadingSkeleton type="table" count={1}/></div>
      </div>
    );
  }

  return (
    <div className="nursery-page nursery-orders">
      <div className="page-header-flex">
        <h1 className="nursery-page-title">Orders Management</h1>
      </div>

      <div className="order-stats-grid">
        <StatCard icon={<FiClock />} title="Pending orders" value={stats.pending} color="#d97706" bgColor="#fef3c7" />
        <StatCard icon={<FiPackage />} title="Processing" value={stats.processing} color="#0284c7" bgColor="#e0f2fe" />
        <StatCard icon={<FiCheckCircle />} title="Completed" value={stats.completed} color="#16a34a" bgColor="#dcfce7" />
        <StatCard icon={<FiXCircle />} title="Cancelled" value={stats.cancelled} color="#dc2626" bgColor="#fee2e2" />
      </div>

      <div className="controls-wrapper">
        <div className="search-box">
          <FiSearch className="search-icon" />
          <input 
            type="text" 
            placeholder="Search by Order ID or Customer Name..." 
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        <div className="filter-box">
          <FiFilter className="filter-icon" />
          <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
            <option value="">All Statuses</option>
            <option value="Pending">Pending</option>
            <option value="Processing">Processing</option>
            <option value="Completed">Completed</option>
            <option value="Cancelled">Cancelled</option>
          </select>
        </div>
      </div>

      <div className="table-card">
        {filteredOrders.length > 0 ? (
          <div className="table-responsive">
            <table className="nursery-table text-left">
              <thead>
                <tr>
                  <th>Order ID</th>
                  <th>Customer</th>
                  <th>Total Items</th>
                  <th>Order Date</th>
                  <th>Amount</th>
                  <th>Status</th>
                  <th align="right">Action</th>
                </tr>
              </thead>
              <tbody>
                {filteredOrders.map(order => (
                  <tr key={order.id}>
                    <td className="fw-500">{order.id}</td>
                    <td>{order.customer}</td>
                    <td>{order.items} Items</td>
                    <td>{order.date}</td>
                    <td className="fw-500">${Number(order.total).toFixed(2)}</td>
                    <td>
                      <span className={`status-badge ${getStatusBadge(order.status)}`}>
                        {order.status}
                      </span>
                    </td>
                    <td align="right">
                      <Link to={`/nursery/orders/${order.id}`} className="btn-secondary btn-sm action-link">
                        <FiExternalLink /> Details
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <EmptyState title="No orders found" message="Try adjusting your filters or wait for new orders to arrive." />
        )}
      </div>
    </div>
  );
};

export default NurseryOrdersPage;

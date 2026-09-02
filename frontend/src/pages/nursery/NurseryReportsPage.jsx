import React, { useState, useEffect } from 'react';
import { FiTrendingUp, FiDollarSign, FiFilter, FiDownload } from 'react-icons/fi';
import { LoadingSkeleton } from '../../components/common/UIState';
import StatCard from '../../components/dashboard/StatCard';
import './NurseryReportsPage.css';

const NurseryReportsPage = () => {
  const [loading, setLoading] = useState(true);
  const [timeRange, setTimeRange] = useState('Last 7 Days');

  // Static mock reporting data based on GoGreen theme
  const weeklyLabels = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
  
  // Percentages for CSS height bars (0 to 100%)
  const revenueData = [35, 50, 20, 80, 65, 95, 40]; 
  const ordersData = [20, 45, 30, 60, 50, 85, 30]; 

  const topPlants = [
    { id: 1, name: 'Monstera Deliciosa', sales: 42, revenue: 1890, bar: '100%' },
    { id: 2, name: 'Snake Plant', sales: 38, revenue: 950, bar: '85%' },
    { id: 3, name: 'Fiddle Leaf Fig', sales: 25, revenue: 1500, bar: '55%' },
    { id: 4, name: 'Aloe Vera', sales: 18, revenue: 270, bar: '35%' },
  ];

  useEffect(() => {
    // Simulate API fetch delay for reports
    const timer = setTimeout(() => setLoading(false), 600);
    return () => clearTimeout(timer);
  }, [timeRange]);

  if (loading) {
    return (
      <div className="nursery-page">
        <h1 className="nursery-page-title">Analytics & Reports</h1>
        <LoadingSkeleton type="card-grid" count={4} />
      </div>
    );
  }

  return (
    <div className="nursery-page nursery-reports">
      <div className="page-header-flex">
        <h1 className="nursery-page-title">Analytics & Reports</h1>
        <div className="reports-actions">
          <div className="time-filter">
            <FiFilter className="r-icon"/>
            <select value={timeRange} onChange={(e) => {setLoading(true); setTimeRange(e.target.value)}}>
              <option value="Last 7 Days">Last 7 Days</option>
              <option value="Last 30 Days">Last 30 Days</option>
              <option value="This Year">This Year</option>
            </select>
          </div>
          <button className="btn-secondary btn-sm" onClick={() => alert("Report Export feature coming soon!")}>
            <FiDownload /> Export CSV
          </button>
        </div>
      </div>

      <div className="reports-stats-grid">
        <StatCard icon={<FiDollarSign />} title="Total Revenue" value="$4,280.00" sub="+12% from last period" color="#16a34a" bgColor="#dcfce7" />
        <StatCard icon={<FiTrendingUp />} title="Avg. Order Value" value="$65.50" sub="-2% from last period" color="#0284c7" bgColor="#e0f2fe" />
        <StatCard icon={<span>📦</span>} title="Total Orders" value="65" sub="+8% from last period" color="#7c3aed" bgColor="#ede9fe" />
        <StatCard icon={<span>🌱</span>} title="Items Sold" value="142" sub="+15% from last period" color="#d97706" bgColor="#fef3c7" />
      </div>

      <div className="charts-grid">
        {/* Revenue Chart */}
        <div className="chart-card">
          <div className="chart-header">
            <h3>Revenue vs Orders</h3>
            <div className="chart-legend">
              <span className="legend-item"><span className="legend-p rev"></span> Revenue</span>
              <span className="legend-item"><span className="legend-p ord"></span> Orders</span>
            </div>
          </div>
          <div className="chart-body">
            <div className="css-bar-chart">
              {weeklyLabels.map((lbl, idx) => (
                <div key={lbl} className="chart-col">
                  <div className="bars-wrap">
                    <div className="bar rev-bar" style={{height: `${revenueData[idx]}%`}} title={`Revenue: ${revenueData[idx]}%`}></div>
                    <div className="bar ord-bar" style={{height: `${ordersData[idx]}%`}} title={`Orders: ${ordersData[idx]}%`}></div>
                  </div>
                  <div className="chart-label">{lbl}</div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Top Selling Chart */}
        <div className="chart-card">
          <div className="chart-header">
            <h3>Top Selling Plants</h3>
          </div>
          <div className="chart-body">
            <div className="horizontal-bars">
              {topPlants.map(plant => (
                <div key={plant.id} className="hbar-row">
                  <div className="hbar-label">
                    <span className="p-name">{plant.name}</span>
                    <span className="p-sales">{plant.sales} units (${plant.revenue})</span>
                  </div>
                  <div className="hbar-track">
                    <div className="hbar-fill" style={{width: plant.bar}}></div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NurseryReportsPage;

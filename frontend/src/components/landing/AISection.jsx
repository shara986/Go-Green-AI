import React from 'react';
import { FiCpu, FiTrendingUp, FiShield, FiZap, FiActivity } from 'react-icons/fi';
import './AISection.css';

const aiFeatures = [
  {
    icon: <FiZap />,
    title: 'Plant Recommendations',
    description: 'Intelligent species matching tailored to your home light, humidity, and lifestyle.',
  },
  {
    icon: <FiTrendingUp />,
    title: 'Smart Care Guidance',
    description: 'Dynamic watering schedules and seasonal care adjustments generated automatically.',
  },
  {
    icon: <FiActivity />,
    title: 'Plant Health Insights',
    description: 'Analyze leaf condition, soil moisture metrics, and early symptom detection.',
  },
];

const AISection = () => {
  return (
    <section className="ai-section">
      <div className="container">
        <div className="ai-wrapper">
          <div className="ai-content">
            <div className="ai-tag">
              <FiCpu /> Powered by GoGreen Intelligence
            </div>
            
            <h2 className="ai-title">Smart Plant Care, Powered by AI</h2>
            
            <p className="ai-subtitle">
              GoGreen AI combines plant knowledge with intelligent recommendations to help you understand and care for your plants better.
            </p>

            <div className="ai-features-list">
              {aiFeatures.map((item, idx) => (
                <div key={idx} className="ai-feature-item">
                  <div className="ai-feature-icon">{item.icon}</div>
                  <div className="ai-feature-text">
                    <h4>{item.title}</h4>
                    <p>{item.description}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>

          <div className="ai-graphic-card">
            <div className="ai-dashboard-mock">
              <div className="ai-mock-header">
                <div className="dot red"></div>
                <div className="dot yellow"></div>
                <div className="dot green"></div>
                <span className="mock-title">GoGreen AI Care Monitor v2.4</span>
              </div>
              
              <div className="ai-mock-body">
                <div className="stat-pill green-pill">
                  <span className="pill-label">Vitality Index</span>
                  <span className="pill-val">98% Optimum</span>
                </div>

                <div className="insight-box">
                  <h5>🤖 Care Advice</h5>
                  <p>Increase soil hydration by 15% during summer afternoons. Move 2ft closer to east window.</p>
                </div>

                <div className="metrics-grid">
                  <div className="metric-card">
                    <span className="metric-name">Sunlight</span>
                    <div className="bar-bg"><div className="bar-fill" style={{ width: '85%' }}></div></div>
                  </div>
                  <div className="metric-card">
                    <span className="metric-name">Moisture</span>
                    <div className="bar-bg"><div className="bar-fill" style={{ width: '70%' }}></div></div>
                  </div>
                  <div className="metric-card">
                    <span className="metric-name">Temperature</span>
                    <div className="bar-bg"><div className="bar-fill" style={{ width: '92%' }}></div></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default AISection;

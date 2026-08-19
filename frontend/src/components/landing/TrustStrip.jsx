import React from 'react';
import { FiSmile, FiCpu, FiHome, FiGlobe } from 'react-icons/fi';
import './TrustStrip.css';

const trustItems = [
  {
    icon: <FiSmile className="trust-icon" />,
    title: 'Healthy Plants',
    subtitle: '100% verified nursery quality',
  },
  {
    icon: <FiCpu className="trust-icon" />,
    title: 'AI-Powered Insights',
    subtitle: 'Tailored care recommendations',
  },
  {
    icon: <FiHome className="trust-icon" />,
    title: 'Easy Plant Management',
    subtitle: 'Track schedule & foliage health',
  },
  {
    icon: <FiGlobe className="trust-icon" />,
    title: 'Sustainable Living',
    subtitle: 'Eco-conscious green decisions',
  },
];

const TrustStrip = () => {
  return (
    <section className="trust-strip-section">
      <div className="container trust-container">
        {trustItems.map((item, index) => (
          <div key={index} className="trust-item">
            <div className="trust-icon-box">{item.icon}</div>
            <div className="trust-text">
              <h4 className="trust-title">{item.title}</h4>
              <p className="trust-subtitle">{item.subtitle}</p>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
};

export default TrustStrip;

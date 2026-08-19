import React from 'react';
import { FiSearch, FiSun, FiCheckSquare, FiFeather } from 'react-icons/fi';
import './WhySection.css';

const whyFeatures = [
  {
    icon: <FiSearch />,
    title: 'Discover Plants',
    description: 'Find plants based on your needs and preferences.',
  },
  {
    icon: <FiSun />,
    title: 'Smart Plant Care',
    description: 'Get useful plant-care information and recommendations.',
  },
  {
    icon: <FiCheckSquare />,
    title: 'Track Your Plants',
    description: 'Keep your plant information and progress organized.',
  },
  {
    icon: <FiFeather />,
    title: 'Sustainable Choices',
    description: 'Make greener and more informed decisions.',
  },
];

const WhySection = () => {
  return (
    <section className="why-section">
      <div className="container">
        <div className="why-header">
          <div className="section-tag">Why GoGreen AI</div>
          <h2 className="section-title">Everything You Need to Grow Better</h2>
          <p className="section-subtitle">
            Designed for urban gardeners, plant enthusiasts, and beginners alike.
          </p>
        </div>

        <div className="why-grid">
          {whyFeatures.map((feature, idx) => (
            <div key={idx} className="why-card">
              <div className="why-card-icon">{feature.icon}</div>
              <h3 className="why-card-title">{feature.title}</h3>
              <p className="why-card-desc">{feature.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default WhySection;

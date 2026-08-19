import React from 'react';
import './HowItWorks.css';

const steps = [
  {
    number: '01',
    title: 'Discover',
    description: 'Find plants that match your needs and indoor environment.',
  },
  {
    number: '02',
    title: 'Choose',
    description: 'Select the right plants and explore their care requirements.',
  },
  {
    number: '03',
    title: 'Grow',
    description: 'Use smart AI guidance to care for and track your plants.',
  },
];

const HowItWorks = () => {
  return (
    <section className="how-section">
      <div className="container">
        <div className="how-header">
          <div className="section-tag">Simple Process</div>
          <h2 className="section-title">How GoGreen AI Works</h2>
          <p className="section-subtitle">
            Three easy steps to start your green journey with AI assistance.
          </p>
        </div>

        <div className="how-steps-grid">
          {steps.map((step, idx) => (
            <div key={idx} className="how-step-card">
              <div className="step-num">{step.number}</div>
              <h3 className="step-title">{step.title}</h3>
              <p className="step-desc">{step.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default HowItWorks;

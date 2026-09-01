import React from 'react';
import { useNavigate } from 'react-router-dom';
import { FiArrowRight } from 'react-icons/fi';
import './CTASection.css';
import { useAuth } from '../../context/AuthContext';

const CTASection = () => {
  const { isAuthenticated, getDashboardPath } = useAuth();
  const navigate = useNavigate();

  const handleGetStarted = (e) => {
    e.preventDefault();
    if (isAuthenticated && getDashboardPath) {
      navigate(getDashboardPath());
    } else {
      navigate('/login');
    }
  };

  return (
    <section className="cta-section">
      <div className="container">
        <div className="cta-card">
          <h2 className="cta-title">Ready to Grow Smarter?</h2>
          <p className="cta-text">
            Join GoGreen AI and start your greener journey today.
          </p>
          <div className="cta-actions">
            <button onClick={handleGetStarted} className="btn-cta-main" style={{ cursor: 'pointer' }}>
              Get Started <FiArrowRight />
            </button>
          </div>
        </div>
      </div>
    </section>
  );
};

export default CTASection;

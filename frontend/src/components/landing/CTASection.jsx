import React from 'react';
import { Link } from 'react-router-dom';
import { FiArrowRight } from 'react-icons/fi';
import './CTASection.css';

const CTASection = () => {
  return (
    <section className="cta-section">
      <div className="container">
        <div className="cta-card">
          <h2 className="cta-title">Ready to Grow Smarter?</h2>
          <p className="cta-text">
            Join GoGreen AI and start your greener journey today.
          </p>
          <div className="cta-actions">
            <Link to="/login" className="btn-cta-main">
              Get Started <FiArrowRight />
            </Link>
          </div>
        </div>
      </div>
    </section>
  );
};

export default CTASection;

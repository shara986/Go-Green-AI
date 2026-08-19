import React from 'react';
import { Link } from 'react-router-dom';
import './Footer.css';

const LeafIcon = ({ size = 20, className = '' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round" className={className}>
    <path d="M11 20A9 9 0 0 1 2.3 9A9 9 0 0 1 11 2c4.97 0 9 4.03 9 9 0 2.12-.74 4.07-1.97 5.61L21 20h-4l-1.39-1.39A8.93 8.93 0 0 1 11 20z" />
    <path d="M7 17L17 7" />
  </svg>
);

const Footer = () => (
  <footer className="footer-section">
    <div className="container footer-container">
      <div className="footer-top">
        <div className="footer-brand">
          <Link to="/" className="footer-logo">
            <div className="logo-icon-wrapper small">
              <LeafIcon className="logo-icon" />
            </div>
            <span className="logo-text">GoGreen <span className="logo-highlight">AI</span></span>
          </Link>
          <p className="footer-desc">
            Plant-management and AI-powered commerce platform. Discover greenery, automate care schedules, and live sustainably.
          </p>
        </div>

        <div className="footer-columns">
          <div className="footer-col">
            <h4>Platform</h4>
            <ul>
              <li><Link to="/">Home</Link></li>
              <li><Link to="/plants">Plants</Link></li>
              <li><Link to="/categories">Categories</Link></li>
              <li><Link to="/about">About</Link></li>
            </ul>
          </div>

          <div className="footer-col">
            <h4>Account</h4>
            <ul>
              <li><Link to="/login">Login</Link></li>
              <li><Link to="/register">Register</Link></li>
              <li><Link to="/profile">Profile</Link></li>
            </ul>
          </div>

          <div className="footer-col">
            <h4>Support</h4>
            <ul>
              <li><Link to="/contact">Contact</Link></li>
              <li><Link to="/help">Help</Link></li>
              <li><Link to="/privacy">Privacy Policy</Link></li>
            </ul>
          </div>
        </div>
      </div>

      <div className="footer-bottom">
        <p>© 2026 GoGreen AI. All rights reserved.</p>
      </div>
    </div>
  </footer>
);

export default Footer;

import React from 'react';
import { Link } from 'react-router-dom';
import { FiArrowRight, FiCpu, FiCheckCircle } from 'react-icons/fi';
import './HeroSection.css';
import hero3dImg from '../../assets/hero_3d_plant.png';

const HeroSection = () => {
  return (
    <section className="hero-section">
      <div className="container hero-container">
        <div className="hero-content">
          <div className="section-tag">
            <FiCpu className="tag-icon" /> AI-Powered Plant Platform
          </div>
          <h1 className="hero-title">
            Grow Smarter.<br />
            <span className="text-highlight">Live Greener.</span>
          </h1>
          <p className="hero-description">
            Discover plants, manage your greenery, and make smarter plant-care decisions with GoGreen AI.
          </p>
          
          <div className="hero-actions">
            <a href="#categories" className="btn-primary hero-btn-main">
              Explore Plants <FiArrowRight />
            </a>
            <Link to="/login" className="btn-secondary hero-btn-sub">
              Get Started
            </Link>
          </div>

          <div className="hero-trust-bullets">
            <div className="bullet-item">
              <FiCheckCircle className="bullet-icon" />
              <span>Smart Recommendations</span>
            </div>
            <div className="bullet-item">
              <FiCheckCircle className="bullet-icon" />
              <span>Verified Nurseries</span>
            </div>
            <div className="bullet-item">
              <FiCheckCircle className="bullet-icon" />
              <span>Eco-Care Guide</span>
            </div>
          </div>
        </div>

        <div className="hero-visual">
          <div className="visual-card-wrapper">
            <img 
              src={hero3dImg} 
              alt="GoGreen AI 3D Plant Care" 
              className="hero-main-img" 
            />
            
            {/* AI Floating Card */}
            <div className="floating-ai-card">
              <div className="ai-badge-header">
                <span className="pulse-dot"></span>
                <span className="ai-card-title">AI Plant Care Assistant</span>
              </div>
              <p className="ai-card-text">"Your Monstera is receiving optimal light today. Next watering in 2 days."</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default HeroSection;

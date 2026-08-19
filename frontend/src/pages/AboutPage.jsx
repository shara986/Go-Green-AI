import React from 'react';
import { Link } from 'react-router-dom';
import { FiGlobe, FiCpu, FiSmile, FiHome, FiSearch, FiCheckSquare, FiSun, FiArrowRight } from 'react-icons/fi';
import './AboutPage.css';

const LeafIcon = ({ size = 20, className = '' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round" className={className}>
    <path d="M11 20A9 9 0 0 1 2.3 9A9 9 0 0 1 11 2c4.97 0 9 4.03 9 9 0 2.12-.74 4.07-1.97 5.61L21 20h-4l-1.39-1.39A8.93 8.93 0 0 1 11 20z" />
    <path d="M7 17L17 7" />
  </svg>
);

const AboutPage = () => {
  return (
    <main className="about-page">
      {/* Hero Section */}
      <section className="about-hero">
        <div className="container">
          <div className="about-hero-content">
            <div className="section-tag">
              <LeafIcon /> About GoGreen AI
            </div>
            <h1 className="about-hero-title">Technology that helps people grow greener.</h1>
            <p className="about-hero-subtitle">
              We empower plant lovers, home gardeners, and urban plant parents with intelligent AI guidance and verified nursery choices.
            </p>
          </div>
        </div>
      </section>

      {/* Mission & What Is GoGreen AI */}
      <section className="about-mission-section">
        <div className="container">
          <div className="mission-grid">
            <div className="mission-card">
              <div className="card-badge-icon"><LeafIcon /></div>
              <h2 className="about-section-heading">Our Mission</h2>
              <p className="mission-text">
                GoGreen AI aims to make plant discovery, plant care, and plant management easier and more enjoyable for everyone. By marrying environmental technology with artificial intelligence, we simplify plant health monitoring and help urban environments bloom.
              </p>
            </div>

            <div className="mission-card highlight-card">
              <div className="card-badge-icon"><FiCpu /></div>
              <h2 className="about-section-heading">What is GoGreen AI?</h2>
              <p className="mission-text">
                GoGreen AI is a modern plant-management and eco-commerce ecosystem that seamlessly integrates:
              </p>
              <ul className="platform-features-list">
                <li><span className="bullet-dot"></span> <strong>Diverse Plant Collections</strong> & categories</li>
                <li><span className="bullet-dot"></span> <strong>Verified Local Nurseries</strong> & ethical growers</li>
                <li><span className="bullet-dot"></span> <strong>Comprehensive Botanical Care Info</strong></li>
                <li><span className="bullet-dot"></span> <strong>AI-Powered Diagnostic & Care Guidance</strong></li>
                <li><span className="bullet-dot"></span> <strong>Personalized Household Plant Tracking</strong></li>
              </ul>
            </div>
          </div>
        </div>
      </section>

      {/* Why GoGreen AI - 4 Cards */}
      <section className="about-why-section">
        <div className="container">
          <div className="section-header-center">
            <div className="section-tag">Core Values</div>
            <h2 className="section-title">Why Choose GoGreen AI?</h2>
            <p className="section-subtitle">
              Built on four core pillars designed to make plant parentage effortless.
            </p>
          </div>

          <div className="why-four-grid">
            <div className="why-pillar-card">
              <div className="pillar-icon-box"><FiGlobe /></div>
              <h3>Sustainable Living</h3>
              <p>Encourage greener everyday choices by making eco-friendly plants accessible to all homes.</p>
            </div>

            <div className="why-pillar-card">
              <div className="pillar-icon-box"><FiCpu /></div>
              <h3>Smart Technology</h3>
              <p>Use intelligent technology and AI insights to make plant care intuitive and proactive.</p>
            </div>

            <div className="why-pillar-card">
              <div className="pillar-icon-box"><FiSmile /></div>
              <h3>Better Plant Care</h3>
              <p>Help users understand their plants, light needs, watering schedules, and soil requirements.</p>
            </div>

            <div className="why-pillar-card">
              <div className="pillar-icon-box"><FiHome /></div>
              <h3>Grow With Confidence</h3>
              <p>Make plant discovery, nursery ordering, and greenery management completely hassle-free.</p>
            </div>
          </div>
        </div>
      </section>

      {/* Our Vision */}
      <section className="about-vision-section">
        <div className="container">
          <div className="vision-banner">
            <div className="vision-content">
              <span className="vision-tag">Our Vision</span>
              <h2 className="vision-heading">A Greener Future, Powered by Technology</h2>
              <p className="vision-paragraph">
                We envision a world where technology brings nature closer to our daily lives. By enabling individuals, families, and organizations to nurture plants with intelligent AI guidance, GoGreen AI contributes to cleaner urban air, happier homes, and a healthier planet.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* How GoGreen AI Works (Visual Flow) */}
      <section className="about-flow-section">
        <div className="container">
          <div className="section-header-center">
            <div className="section-tag">The Process</div>
            <h2 className="section-title">How GoGreen AI Works</h2>
            <p className="section-subtitle">A simple 4-step path to thriving household greenery.</p>
          </div>

          <div className="flow-steps-wrapper">
            <div className="flow-step">
              <div className="flow-circle">
                <FiSearch />
                <span className="flow-num">1</span>
              </div>
              <h4>Discover</h4>
              <p>Find plants matching your light & living space.</p>
            </div>

            <div className="flow-line"></div>

            <div className="flow-step">
              <div className="flow-circle">
                <LeafIcon />
                <span className="flow-num">2</span>
              </div>
              <h4>Choose</h4>
              <p>Select healthy plants from trusted nurseries.</p>
            </div>

            <div className="flow-line"></div>

            <div className="flow-step">
              <div className="flow-circle">
                <FiSun />
                <span className="flow-num">3</span>
              </div>
              <h4>Care</h4>
              <p>Receive smart AI alerts for light & watering.</p>
            </div>

            <div className="flow-line"></div>

            <div className="flow-step">
              <div className="flow-circle">
                <FiCheckSquare />
                <span className="flow-num">4</span>
              </div>
              <h4>Grow</h4>
              <p>Watch your plant sanctuary flourish day by day.</p>
            </div>
          </div>
        </div>
      </section>

      {/* Call to Action */}
      <section className="about-cta-section">
        <div className="container">
          <div className="about-cta-card">
            <h2>Ready to Grow With Us?</h2>
            <p>Create your GoGreen AI account today and start your green journey with intelligent plant care.</p>
            <Link to="/register" className="btn-cta-white">
              Get Started <FiArrowRight />
            </Link>
          </div>
        </div>
      </section>
    </main>
  );
};

export default AboutPage;

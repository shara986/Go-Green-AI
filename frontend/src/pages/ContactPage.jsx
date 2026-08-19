import React, { useState } from 'react';
import { FiMail, FiPhone, FiMapPin, FiSend, FiCheckCircle, FiInfo } from 'react-icons/fi';
import './ContactPage.css';

const LeafIcon = ({ size = 20, className = '' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round" className={className}>
    <path d="M11 20A9 9 0 0 1 2.3 9A9 9 0 0 1 11 2c4.97 0 9 4.03 9 9 0 2.12-.74 4.07-1.97 5.61L21 20h-4l-1.39-1.39A8.93 8.93 0 0 1 11 20z" />
    <path d="M7 17L17 7" />
  </svg>
);

const ContactPage = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    subject: '',
    message: '',
  });

  const [validationErrors, setValidationErrors] = useState({});
  const [submitted, setSubmitted] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (validationErrors[name]) {
      setValidationErrors((prev) => ({ ...prev, [name]: '' }));
    }
    setSubmitted(false);
  };

  const validateForm = () => {
    const errors = {};
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!formData.name.trim()) {
      errors.name = 'Please enter your name.';
    }

    if (!formData.email.trim()) {
      errors.email = 'Please enter your email.';
    } else if (!emailRegex.test(formData.email.trim())) {
      errors.email = 'Please enter a valid email address.';
    }

    if (!formData.subject.trim()) {
      errors.subject = 'Please enter a subject.';
    }

    if (!formData.message.trim()) {
      errors.message = 'Please enter your message.';
    }

    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (loading) return;

    if (!validateForm()) {
      return;
    }

    setLoading(true);

    // Backend contact API is not present; show notice as per prompt instructions
    setTimeout(() => {
      setLoading(false);
      setSubmitted(true);
      setFormData({ name: '', email: '', subject: '', message: '' });
    }, 600);
  };

  return (
    <main className="contact-page">
      {/* Hero Section */}
      <section className="contact-hero">
        <div className="container">
          <div className="contact-hero-content">
            <div className="section-tag">
              <LeafIcon /> Get In Touch
            </div>
            <h1 className="contact-hero-title">Let's Grow Together</h1>
            <p className="contact-hero-subtitle">
              Have a question, suggestion, or need help? We'd love to hear from you.
            </p>
          </div>
        </div>
      </section>

      {/* Contact Content Grid */}
      <section className="contact-body-section">
        <div className="container">
          <div className="contact-grid">
            {/* Left Info Column */}
            <div className="contact-info-panel">
              <h2 className="info-heading">Reach Out to Us</h2>
              <p className="info-desc">
                Our plant support team and gardening experts are ready to assist you with inquiries, partnerships, and technical support.
              </p>

              <div className="contact-cards-list">
                <div className="contact-info-card">
                  <div className="info-icon-box">
                    <FiMail />
                  </div>
                  <div className="info-details">
                    <span className="info-label">Email Support</span>
                    <a href="mailto:support@gogreen.ai" className="info-value">support@gogreen.ai</a>
                  </div>
                </div>

                <div className="contact-info-card">
                  <div className="info-icon-box">
                    <FiPhone />
                  </div>
                  <div className="info-details">
                    <span className="info-label">Phone Hotline</span>
                    <a href="tel:+910000000000" className="info-value">+91 00000 00000</a>
                  </div>
                </div>

                <div className="contact-info-card">
                  <div className="info-icon-box">
                    <FiMapPin />
                  </div>
                  <div className="info-details">
                    <span className="info-label">Headquarters</span>
                    <span className="info-value">India</span>
                  </div>
                </div>
              </div>

              <div className="contact-hours-box">
                <h4>Support Hours</h4>
                <p>Monday — Friday: 9:00 AM – 6:00 PM IST</p>
                <p>Saturday: 10:00 AM – 2:00 PM IST</p>
              </div>
            </div>

            {/* Right Form Column */}
            <div className="contact-form-panel">
              <div className="contact-card">
                <h3 className="form-title">Send Us a Message</h3>
                <p className="form-subtitle">Fill out the form below and our team will respond shortly.</p>

                {submitted && (
                  <div className="contact-notice-banner">
                    <FiInfo className="notice-icon" />
                    <span>Contact form submission will be available soon. Thank you for reaching out!</span>
                  </div>
                )}

                <form onSubmit={handleSubmit} className="contact-form" noValidate>
                  <div className="form-group">
                    <label htmlFor="name" className="form-label">
                      Your Name <span className="required-star">*</span>
                    </label>
                    <input
                      type="text"
                      id="name"
                      name="name"
                      className={`form-input ${validationErrors.name ? 'input-error' : ''}`}
                      placeholder="Enter your full name"
                      value={formData.name}
                      onChange={handleChange}
                      disabled={loading}
                    />
                    {validationErrors.name && <span className="field-error">{validationErrors.name}</span>}
                  </div>

                  <div className="form-group">
                    <label htmlFor="email" className="form-label">
                      Email Address <span className="required-star">*</span>
                    </label>
                    <input
                      type="email"
                      id="email"
                      name="email"
                      className={`form-input ${validationErrors.email ? 'input-error' : ''}`}
                      placeholder="name@example.com"
                      value={formData.email}
                      onChange={handleChange}
                      disabled={loading}
                    />
                    {validationErrors.email && <span className="field-error">{validationErrors.email}</span>}
                  </div>

                  <div className="form-group">
                    <label htmlFor="subject" className="form-label">
                      Subject <span className="required-star">*</span>
                    </label>
                    <input
                      type="text"
                      id="subject"
                      name="subject"
                      className={`form-input ${validationErrors.subject ? 'input-error' : ''}`}
                      placeholder="What is this regarding?"
                      value={formData.subject}
                      onChange={handleChange}
                      disabled={loading}
                    />
                    {validationErrors.subject && <span className="field-error">{validationErrors.subject}</span>}
                  </div>

                  <div className="form-group">
                    <label htmlFor="message" className="form-label">
                      Message <span className="required-star">*</span>
                    </label>
                    <textarea
                      id="message"
                      name="message"
                      rows="4"
                      className={`form-textarea ${validationErrors.message ? 'input-error' : ''}`}
                      placeholder="Write your query or feedback..."
                      value={formData.message}
                      onChange={handleChange}
                      disabled={loading}
                    ></textarea>
                    {validationErrors.message && <span className="field-error">{validationErrors.message}</span>}
                  </div>

                  <button
                    type="submit"
                    className="btn-primary contact-submit-btn"
                    disabled={loading}
                  >
                    {loading ? 'Sending Message...' : <><FiSend /> Send Message</>}
                  </button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
  );
};

export default ContactPage;

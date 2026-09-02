import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FiUser, FiMail, FiLock, FiPhone, FiEye, FiEyeOff, FiAlertCircle, FiCheckCircle } from 'react-icons/fi';
import api from '../api/axiosInstance';
import { useAuth } from '../context/AuthContext';
import './RegisterPage.css';

const LeafIcon = ({ size = 20, className = '' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round" className={className}>
    <path d="M11 20A9 9 0 0 1 2.3 9A9 9 0 0 1 11 2c4.97 0 9 4.03 9 9 0 2.12-.74 4.07-1.97 5.61L21 20h-4l-1.39-1.39A8.93 8.93 0 0 1 11 20z" />
    <path d="M7 17L17 7" />
  </svg>
);

const RegisterPage = () => {
  const [formData, setFormData] = useState({
    name: '',
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    phoneNumber: '',
  });

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [validationErrors, setValidationErrors] = useState({});

  const { login, getDashboardPath } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (validationErrors[name]) {
      setValidationErrors((prev) => ({ ...prev, [name]: '' }));
    }
    setErrorMessage('');
    setSuccessMessage('');
  };

  const validateForm = () => {
    const errors = {};
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!formData.name.trim()) {
      errors.name = 'Name is required.';
    }

    if (!formData.username.trim()) {
      errors.username = 'Username is required.';
    } else if (formData.username.trim().length < 3 || formData.username.trim().length > 50) {
      errors.username = 'Username must be between 3 and 50 characters.';
    }

    if (!formData.email.trim()) {
      errors.email = 'Email is required.';
    } else if (!emailRegex.test(formData.email.trim())) {
      errors.email = 'Please enter a valid email address.';
    }

    if (!formData.password) {
      errors.password = 'Password is required.';
    } else if (formData.password.length < 8) {
      errors.password = 'Password must be at least 8 characters.';
    }

    if (!formData.confirmPassword) {
      errors.confirmPassword = 'Confirm Password is required.';
    } else if (formData.password !== formData.confirmPassword) {
      errors.confirmPassword = 'Passwords do not match.';
    }

    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (loading) return;

    if (!validateForm()) {
      setErrorMessage('Please fill all required fields correctly.');
      return;
    }

    setLoading(true);
    setErrorMessage('');
    setSuccessMessage('');

    try {
      const response = await api.post('/auth/register/customer', {
        name: formData.name.trim(),
        username: formData.username.trim(),
        email: formData.email.trim(),
        password: formData.password,
        confirmPassword: formData.confirmPassword,
        phoneNumber: formData.phoneNumber.trim() || null,
      });

      const resData = response.data;
      if (resData && (resData.success || resData.data)) {
        const authData = resData.data || resData;
        
        setSuccessMessage('Account created successfully!');

        // Save auth data if token returned, then redirect to role dashboard
        if (authData.accessToken) {
          login(authData);
          // Derive path from raw API response (context won't have updated yet)
          const roles = authData.user?.roles || [];
          const hasRoleIn = (r) => Array.isArray(roles) ? roles.includes(r) : false;
          let rolePath = '/customer/dashboard';
          if (hasRoleIn('ROLE_ADMIN')) rolePath = '/admin/dashboard';
          else if (hasRoleIn('ROLE_NURSERY_OWNER')) rolePath = '/nursery/dashboard';
          setTimeout(() => navigate(rolePath), 1200);
        } else {
          // No token returned (e.g. nursery/expert pending approval) → go to login
          setTimeout(() => navigate('/login'), 1500);
        }
      } else {
        setErrorMessage('Failed to complete registration.');
      }
    } catch (err) {
      console.error('Registration error:', err);
      if (err.response) {
        const backendMsg = err.response.data?.message || err.response.data?.data;
        if (backendMsg) {
          if (backendMsg.toLowerCase().includes('username')) {
            setErrorMessage('Username is already taken.');
          } else if (backendMsg.toLowerCase().includes('email')) {
            setErrorMessage('Email is already registered.');
          } else if (backendMsg.toLowerCase().includes('match')) {
            setErrorMessage('Passwords do not match.');
          } else {
            setErrorMessage(backendMsg);
          }
        } else {
          setErrorMessage('Registration failed. Please check your details and try again.');
        }
      } else {
        setErrorMessage('Unable to connect to the server. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="register-page">
      <div className="register-split-container">
        {/* Left Side Visual Panel */}
        <div className="register-visual-panel">
          <div className="register-visual-overlay"></div>
          <div className="register-visual-content">
            <div className="brand-badge">
              <LeafIcon /> GoGreen Platform
            </div>
            <h1 className="visual-heading">Start Your Greener Journey</h1>
            <p className="visual-subheading">
              Create your GoGreen AI account and discover a smarter way to care for plants.
            </p>
          </div>
        </div>

        {/* Right Side Form Panel */}
        <div className="register-form-panel">
          <div className="register-card">
            <div className="register-card-header">
              <Link to="/" className="register-logo-brand">
                <div className="logo-icon-wrapper">
                  <LeafIcon className="logo-icon" />
                </div>
                <span className="logo-text">GoGreen <span className="logo-highlight">AI</span></span>
              </Link>
              <h2 className="register-heading">Create Your Account</h2>
              <p className="register-subheading">Join GoGreen AI today</p>
            </div>

            {/* Success Banner */}
            {successMessage && (
              <div className="register-success-banner">
                <FiCheckCircle className="success-icon" />
                <span>{successMessage} Redirecting...</span>
              </div>
            )}

            {/* Error Banner */}
            {errorMessage && (
              <div className="register-error-banner">
                <FiAlertCircle className="error-icon" />
                <span>{errorMessage}</span>
              </div>
            )}

            <form onSubmit={handleSubmit} className="register-form" noValidate>
              <div className="form-group">
                <label htmlFor="name" className="form-label">
                  Full Name <span className="required-star">*</span>
                </label>
                <div className="input-input-wrapper">
                  <FiUser className="input-icon" />
                  <input
                    type="text"
                    id="name"
                    name="name"
                    className={`form-input ${validationErrors.name ? 'input-error' : ''}`}
                    placeholder="Enter your full name"
                    value={formData.name}
                    onChange={handleChange}
                    disabled={loading}
                    autoComplete="name"
                  />
                </div>
                {validationErrors.name && <span className="field-error">{validationErrors.name}</span>}
              </div>

              <div className="form-group">
                <label htmlFor="username" className="form-label">
                  Username <span className="required-star">*</span>
                </label>
                <div className="input-input-wrapper">
                  <FiUser className="input-icon" />
                  <input
                    type="text"
                    id="username"
                    name="username"
                    className={`form-input ${validationErrors.username ? 'input-error' : ''}`}
                    placeholder="Choose a username (3-50 chars)"
                    value={formData.username}
                    onChange={handleChange}
                    disabled={loading}
                    autoComplete="username"
                  />
                </div>
                {validationErrors.username && <span className="field-error">{validationErrors.username}</span>}
              </div>

              <div className="form-group">
                <label htmlFor="email" className="form-label">
                  Email Address <span className="required-star">*</span>
                </label>
                <div className="input-input-wrapper">
                  <FiMail className="input-icon" />
                  <input
                    type="email"
                    id="email"
                    name="email"
                    className={`form-input ${validationErrors.email ? 'input-error' : ''}`}
                    placeholder="name@example.com"
                    value={formData.email}
                    onChange={handleChange}
                    disabled={loading}
                    autoComplete="email"
                  />
                </div>
                {validationErrors.email && <span className="field-error">{validationErrors.email}</span>}
              </div>

              <div className="form-group">
                <label htmlFor="phoneNumber" className="form-label">
                  Phone Number <span className="optional-tag">(Optional)</span>
                </label>
                <div className="input-input-wrapper">
                  <FiPhone className="input-icon" />
                  <input
                    type="tel"
                    id="phoneNumber"
                    name="phoneNumber"
                    className="form-input"
                    placeholder="+91 00000 00000"
                    value={formData.phoneNumber}
                    onChange={handleChange}
                    disabled={loading}
                    autoComplete="tel"
                  />
                </div>
              </div>

              <div className="form-row-2col">
                <div className="form-group">
                  <label htmlFor="password" className="form-label">
                    Password <span className="required-star">*</span>
                  </label>
                  <div className="input-input-wrapper">
                    <FiLock className="input-icon" />
                    <input
                      type={showPassword ? 'text' : 'password'}
                      id="password"
                      name="password"
                      className={`form-input password-input ${validationErrors.password ? 'input-error' : ''}`}
                      placeholder="Min 8 chars"
                      value={formData.password}
                      onChange={handleChange}
                      disabled={loading}
                      autoComplete="new-password"
                    />
                    <button
                      type="button"
                      className="password-toggle-btn"
                      onClick={() => setShowPassword((prev) => !prev)}
                      tabIndex={-1}
                      aria-label={showPassword ? 'Hide password' : 'Show password'}
                    >
                      {showPassword ? <FiEyeOff /> : <FiEye />}
                    </button>
                  </div>
                  {validationErrors.password && <span className="field-error">{validationErrors.password}</span>}
                </div>

                <div className="form-group">
                  <label htmlFor="confirmPassword" className="form-label">
                    Confirm Password <span className="required-star">*</span>
                  </label>
                  <div className="input-input-wrapper">
                    <FiLock className="input-icon" />
                    <input
                      type={showConfirmPassword ? 'text' : 'password'}
                      id="confirmPassword"
                      name="confirmPassword"
                      className={`form-input password-input ${validationErrors.confirmPassword ? 'input-error' : ''}`}
                      placeholder="Re-enter password"
                      value={formData.confirmPassword}
                      onChange={handleChange}
                      disabled={loading}
                      autoComplete="new-password"
                    />
                    <button
                      type="button"
                      className="password-toggle-btn"
                      onClick={() => setShowConfirmPassword((prev) => !prev)}
                      tabIndex={-1}
                      aria-label={showConfirmPassword ? 'Hide password' : 'Show password'}
                    >
                      {showConfirmPassword ? <FiEyeOff /> : <FiEye />}
                    </button>
                  </div>
                  {validationErrors.confirmPassword && <span className="field-error">{validationErrors.confirmPassword}</span>}
                </div>
              </div>

              <button
                type="submit"
                className="btn-primary register-submit-btn"
                disabled={loading}
              >
                {loading ? 'Creating Account...' : 'Create Account'}
              </button>
            </form>

            <div className="register-card-footer">
              <p>
                Already have an account?{' '}
                <Link to="/login" className="login-link">
                  Login
                </Link>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RegisterPage;

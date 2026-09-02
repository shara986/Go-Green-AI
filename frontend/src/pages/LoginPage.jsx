import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FiUser, FiLock, FiEye, FiEyeOff, FiAlertCircle } from 'react-icons/fi';
import api from '../api/axiosInstance';
import { useAuth } from '../context/AuthContext';
import './LoginPage.css';

const LeafIcon = ({ size = 20, className = '' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round" className={className}>
    <path d="M11 20A9 9 0 0 1 2.3 9A9 9 0 0 1 11 2c4.97 0 9 4.03 9 9 0 2.12-.74 4.07-1.97 5.61L21 20h-4l-1.39-1.39A8.93 8.93 0 0 1 11 20z" />
    <path d="M7 17L17 7" />
  </svg>
);

const LoginPage = () => {
  const [formData, setFormData] = useState({
    usernameOrEmail: '',
    password: '',
  });
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [validationError, setValidationError] = useState('');

  const { login, getDashboardPath } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    setValidationError('');
    setErrorMessage('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (loading) return; // Prevent duplicate submission

    // Form Validation
    if (!formData.usernameOrEmail.trim()) {
      setValidationError('Please enter your username/email.');
      return;
    }
    if (!formData.password) {
      setValidationError('Please enter your password.');
      return;
    }

    setLoading(true);
    setErrorMessage('');
    setValidationError('');

    try {
      const response = await api.post('/auth/login', {
        usernameOrEmail: formData.usernameOrEmail.trim(),
        password: formData.password,
      });

      // API returns ApiResponse<AuthResponseDto> -> { success, message, data: { accessToken, refreshToken, tokenType, expiresIn, user } }
      const resData = response.data;
      if (resData && (resData.success || resData.data)) {
        const authData = resData.data || resData;
        
        // Save auth data
        login(authData);

        // Check if there was a target redirect path (deep-link protection)
        const searchParams = new URLSearchParams(window.location.search);
        const redirectPath = searchParams.get('redirect');

        // Derive dashboard path from the API response directly (context hasn't re-rendered yet)
        const roles = authData.user?.roles || [];
        const hasRoleIn = (role) => Array.isArray(roles) ? roles.includes(role) : false;
        let rolePath = '/';
        if (hasRoleIn('ROLE_ADMIN')) rolePath = '/admin/dashboard';
        else if (hasRoleIn('ROLE_NURSERY_OWNER')) rolePath = '/nursery/dashboard';
        else if (hasRoleIn('ROLE_CUSTOMER')) rolePath = '/customer/dashboard';

        navigate(redirectPath || rolePath);
      } else {
        setErrorMessage('Invalid response format from server.');
      }
    } catch (err) {
      console.error('Login error:', err);
      if (err.response) {
        const status = err.response.status;
        if (status === 401) {
          setErrorMessage('Invalid username/email or password.');
        } else if (status === 403) {
          setErrorMessage('You do not have permission to access this account.');
        } else {
          setErrorMessage(err.response.data?.message || 'Invalid username/email or password.');
        }
      } else {
        setErrorMessage('Unable to connect to the server. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-split-container">
        {/* Left Side Visual (Desktop / Tablet) */}
        <div className="login-visual-panel">
          <div className="login-visual-overlay"></div>
          <div className="login-visual-content">
            <div className="brand-badge">
              <LeafIcon /> GoGreen Platform
            </div>
            <h1 className="visual-heading">Welcome back to GoGreen AI</h1>
            <p className="visual-subheading">Your smarter way to grow and manage plants.</p>
          </div>
        </div>

        {/* Right Side Form Panel */}
        <div className="login-form-panel">
          <div className="login-card">
            <div className="login-card-header">
              <Link to="/" className="login-logo-brand">
                <div className="logo-icon-wrapper">
                  <LeafIcon className="logo-icon" />
                </div>
                <span className="logo-text">GoGreen <span className="logo-highlight">AI</span></span>
              </Link>
              <h2 className="login-heading">Welcome Back</h2>
              <p className="login-subheading">Sign in to continue to GoGreen AI</p>
            </div>

            {/* Error Message Box */}
            {(errorMessage || validationError) && (
              <div className="login-error-banner">
                <FiAlertCircle className="error-icon" />
                <span>{validationError || errorMessage}</span>
              </div>
            )}

            <form onSubmit={handleSubmit} className="login-form" noValidate>
              <div className="form-group">
                <label htmlFor="usernameOrEmail" className="form-label">
                  Email / Username
                </label>
                <div className="input-input-wrapper">
                  <FiUser className="input-icon" />
                  <input
                    type="text"
                    id="usernameOrEmail"
                    name="usernameOrEmail"
                    className="form-input"
                    placeholder="Enter your username or email"
                    value={formData.usernameOrEmail}
                    onChange={handleChange}
                    disabled={loading}
                    autoComplete="username"
                  />
                </div>
              </div>

              <div className="form-group">
                <div className="label-row">
                  <label htmlFor="password" className="form-label">
                    Password
                  </label>
                  <a href="#forgot-password" onClick={(e) => { e.preventDefault(); alert("Password reset instructions available via admin or /api/auth/forgot-password"); }} className="forgot-link">
                    Forgot Password?
                  </a>
                </div>
                <div className="input-input-wrapper">
                  <FiLock className="input-icon" />
                  <input
                    type={showPassword ? 'text' : 'password'}
                    id="password"
                    name="password"
                    className="form-input password-input"
                    placeholder="Enter your password"
                    value={formData.password}
                    onChange={handleChange}
                    disabled={loading}
                    autoComplete="current-password"
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
              </div>

              <button
                type="submit"
                className="btn-primary login-submit-btn"
                disabled={loading}
              >
                {loading ? 'Signing in...' : 'Login'}
              </button>
            </form>

            <div className="login-card-footer">
              <p>
                Don't have an account?{' '}
                <Link to="/register" className="register-link">
                  Create Account
                </Link>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;

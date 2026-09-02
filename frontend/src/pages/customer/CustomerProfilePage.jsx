import React, { useState, useEffect } from 'react';
import { FiUser, FiMail, FiPhone, FiMapPin, FiShield, FiSave, FiCheckCircle } from 'react-icons/fi';
import CustomerLayout from '../../components/customer/CustomerLayout';
import { LoadingSkeleton, ErrorState } from '../../components/common/UIState';
import { fetchUserProfile, updateUserProfile } from '../../api/customerApi';
import { useAuth } from '../../context/AuthContext';
import './CustomerProfilePage.css';

const CustomerProfilePage = () => {
  const { user: authUser } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [saving, setSaving] = useState(false);
  const [toastMessage, setToastMessage] = useState(null);

  // Edit form state
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    address: '',
  });

  const showToast = (msg) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3000);
  };

  const loadProfile = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchUserProfile();
      setProfile(data);
      setFormData({
        name: data.name || data.username || '',
        email: data.email || '',
        phone: data.phone || '',
        address: data.address || '',
      });
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProfile();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      const updated = await updateUserProfile(formData);
      setProfile(updated);
      showToast('Profile updated successfully! ✨');
    } catch (err) {
      showToast(err?.response?.data?.message || 'Failed to update profile.');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <CustomerLayout>
        <div className="profile-page-container">
          <LoadingSkeleton type="table" count={5} />
        </div>
      </CustomerLayout>
    );
  }

  if (error) {
    return (
      <CustomerLayout>
        <div className="profile-page-container">
          <ErrorState error={error} onRetry={loadProfile} />
        </div>
      </CustomerLayout>
    );
  }

  const initialLetter = (formData.name || profile?.username || 'C').charAt(0).toUpperCase();

  return (
    <CustomerLayout>
      <div className="profile-page-container">
        <div className="profile-header">
          <h1 className="profile-title">My Profile</h1>
          <p className="profile-subtitle">Manage your personal details, contact information, and shipping address.</p>
        </div>

        {/* Toast Notification */}
        {toastMessage && <div className="toast-notification">{toastMessage}</div>}

        <div className="profile-grid">
          {/* Left Column: Avatar & Account Summary Card */}
          <div className="profile-summary-card">
            <div className="profile-avatar-circle">{initialLetter}</div>
            <h2 className="profile-name-text">{formData.name || profile?.username}</h2>
            <p className="profile-role-badge">
              <FiShield size={13} /> {profile?.role || 'ROLE_CUSTOMER'}
            </p>

            <div className="profile-summary-divider" />

            <div className="summary-info-list">
              <div className="summary-info-item">
                <FiUser className="summary-icon" />
                <div>
                  <span className="info-label">Username</span>
                  <p className="info-val">{profile?.username || authUser?.username || 'N/A'}</p>
                </div>
              </div>
              <div className="summary-info-item">
                <FiMail className="summary-icon" />
                <div>
                  <span className="info-label">Email Address</span>
                  <p className="info-val">{profile?.email || 'No email provided'}</p>
                </div>
              </div>
              <div className="summary-info-item">
                <FiCheckCircle className="summary-icon" />
                <div>
                  <span className="info-label">Account Status</span>
                  <p className="info-val text-green">Active Customer</p>
                </div>
              </div>
            </div>
          </div>

          {/* Right Column: Edit Profile Form */}
          <div className="profile-edit-card">
            <h2 className="edit-card-title">Account Information</h2>
            <form onSubmit={handleSubmit} className="profile-form">
              <div className="form-group-row">
                <div className="form-group">
                  <label htmlFor="name">Full Name</label>
                  <div className="input-with-icon">
                    <FiUser className="input-icon" />
                    <input
                      id="name"
                      name="name"
                      type="text"
                      placeholder="Your Full Name"
                      value={formData.name}
                      onChange={handleChange}
                    />
                  </div>
                </div>

                <div className="form-group">
                  <label htmlFor="email">Email Address</label>
                  <div className="input-with-icon">
                    <FiMail className="input-icon" />
                    <input
                      id="email"
                      name="email"
                      type="email"
                      placeholder="your.email@example.com"
                      value={formData.email}
                      onChange={handleChange}
                    />
                  </div>
                </div>
              </div>

              <div className="form-group">
                <label htmlFor="phone">Phone Number</label>
                <div className="input-with-icon">
                  <FiPhone className="input-icon" />
                  <input
                    id="phone"
                    name="phone"
                    type="tel"
                    placeholder="e.g. +1 (555) 123-4567"
                    value={formData.phone}
                    onChange={handleChange}
                  />
                </div>
              </div>

              <div className="form-group">
                <label htmlFor="address">Default Shipping Address</label>
                <div className="input-with-icon text-area-wrap">
                  <FiMapPin className="input-icon text-area-icon" />
                  <textarea
                    id="address"
                    name="address"
                    rows={4}
                    placeholder="Enter your street, apartment, city, state, zip code"
                    value={formData.address}
                    onChange={handleChange}
                  />
                </div>
              </div>

              <div className="form-actions">
                <button type="submit" className="save-profile-btn" disabled={saving}>
                  <FiSave size={16} />
                  {saving ? 'Saving Changes…' : 'Save Profile Changes'}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </CustomerLayout>
  );
};

export default CustomerProfilePage;

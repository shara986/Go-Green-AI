import React, { useState } from 'react';
import { FiUser, FiMail, FiLock, FiBell, FiSave } from 'react-icons/fi';
import { useAuth } from '../../context/AuthContext';
import './NurseryUserSettingsPage.css';

const NurseryUserSettingsPage = () => {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('profile');
  const [saving, setSaving] = useState(false);
  const [notification, setNotification] = useState('');

  const [profileForm, setProfileForm] = useState({
    name: user?.name || user?.username || 'Nursery Manager',
    email: user?.email || 'manager@nursery.com',
    phone: '',
  });

  const [passwordForm, setPasswordForm] = useState({
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  });

  const handleProfileChange = (e) => {
    setProfileForm({ ...profileForm, [e.target.name]: e.target.value });
  };

  const handlePasswordChange = (e) => {
    setPasswordForm({ ...passwordForm, [e.target.name]: e.target.value });
  };

  const handleProfileSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setTimeout(() => {
      setSaving(false);
      setNotification('Profile details updated successfully!');
      setTimeout(() => setNotification(''), 3000);
    }, 800);
  };

  const handlePasswordSubmit = async (e) => {
    e.preventDefault();
    if (passwordForm.newPassword !== passwordForm.confirmPassword) {
      alert("New passwords do not match!");
      return;
    }
    setSaving(true);
    setTimeout(() => {
      setSaving(false);
      setNotification('Password changed successfully!');
      setPasswordForm({ currentPassword: '', newPassword: '', confirmPassword: '' });
      setTimeout(() => setNotification(''), 3000);
    }, 800);
  };

  return (
    <div className="nursery-page nursery-settings">
      <h1 className="nursery-page-title">Account Settings</h1>

      {notification && (
        <div className="notification-banner alert-success">
          {notification}
        </div>
      )}

      <div className="settings-layout">
        <div className="settings-sidebar">
          <ul className="settings-nav">
            <li>
              <button 
                className={`settings-tab ${activeTab === 'profile' ? 'active' : ''}`}
                onClick={() => setActiveTab('profile')}
              >
                <FiUser /> Personal Information
              </button>
            </li>
            <li>
              <button 
                className={`settings-tab ${activeTab === 'security' ? 'active' : ''}`}
                onClick={() => setActiveTab('security')}
              >
                <FiLock /> Security & Password
              </button>
            </li>
            <li>
              <button 
                className={`settings-tab ${activeTab === 'notifications' ? 'active' : ''}`}
                onClick={() => setActiveTab('notifications')}
              >
                <FiBell /> Notification Preferences
              </button>
            </li>
          </ul>
        </div>

        <div className="settings-content">
          {activeTab === 'profile' && (
            <div className="settings-card">
              <div className="settings-header">
                <h2>Personal Information</h2>
                <p>Update your personal details and contact information.</p>
              </div>
              <form className="settings-form" onSubmit={handleProfileSubmit}>
                <div className="form-group">
                  <label>Full Name</label>
                  <div className="input-group">
                    <FiUser className="input-icon" />
                    <input type="text" name="name" value={profileForm.name} onChange={handleProfileChange} required />
                  </div>
                </div>
                <div className="form-group">
                  <label>Email Address</label>
                  <div className="input-group">
                    <FiMail className="input-icon" />
                    <input type="email" name="email" value={profileForm.email} onChange={handleProfileChange} required />
                  </div>
                </div>
                <div className="form-group">
                  <label>Phone Number (Optional)</label>
                  <input type="tel" className="basic-input" name="phone" placeholder="+1..." value={profileForm.phone} onChange={handleProfileChange} />
                </div>
                
                <div className="form-actions border-top pt-3 mt-3">
                  <button type="submit" className="btn-primary" disabled={saving}>
                    {saving ? 'Saving...' : <><FiSave /> Save Changes</>}
                  </button>
                </div>
              </form>
            </div>
          )}

          {activeTab === 'security' && (
            <div className="settings-card">
              <div className="settings-header">
                <h2>Change Password</h2>
                <p>Ensure your account is using a long, random password to stay secure.</p>
              </div>
              <form className="settings-form" onSubmit={handlePasswordSubmit}>
                <div className="form-group">
                  <label>Current Password</label>
                  <input type="password" name="currentPassword" minLength="6" className="basic-input" value={passwordForm.currentPassword} onChange={handlePasswordChange} required />
                </div>
                <div className="form-group">
                  <label>New Password</label>
                  <input type="password" name="newPassword" minLength="6" className="basic-input" value={passwordForm.newPassword} onChange={handlePasswordChange} required />
                </div>
                <div className="form-group">
                  <label>Confirm New Password</label>
                  <input type="password" name="confirmPassword" minLength="6" className="basic-input" value={passwordForm.confirmPassword} onChange={handlePasswordChange} required />
                </div>
                
                <div className="form-actions border-top pt-3 mt-3">
                  <button type="submit" className="btn-primary" disabled={saving}>
                    {saving ? 'Updating...' : <><FiSave /> Update Password</>}
                  </button>
                </div>
              </form>
            </div>
          )}

          {activeTab === 'notifications' && (
            <div className="settings-card">
              <div className="settings-header">
                <h2>Notification Preferences</h2>
                <p>Control what emails and alerts you receive.</p>
              </div>
              <div className="settings-form">
                <div className="toggle-row">
                  <div>
                    <strong>New Order Alerts</strong>
                    <p>Receive an email when a new order is placed.</p>
                  </div>
                  <label className="toggle-switch">
                    <input type="checkbox" defaultChecked />
                    <span className="slider round"></span>
                  </label>
                </div>
                <hr className="divider" style={{margin: '1rem 0'}} />
                <div className="toggle-row">
                  <div>
                    <strong>Low Stock Warnings</strong>
                    <p>Get notified when plant inventory falls below minimum required.</p>
                  </div>
                  <label className="toggle-switch">
                    <input type="checkbox" defaultChecked />
                    <span className="slider round"></span>
                  </label>
                </div>
                <hr className="divider" style={{margin: '1rem 0'}} />
                <div className="toggle-row">
                  <div>
                    <strong>Marketing Emails</strong>
                    <p>Receive weekly insights and GoGreen AI updates.</p>
                  </div>
                  <label className="toggle-switch">
                    <input type="checkbox" />
                    <span className="slider round"></span>
                  </label>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default NurseryUserSettingsPage;

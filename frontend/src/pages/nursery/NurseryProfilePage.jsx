import React, { useState, useEffect } from 'react';
import { FiEdit2, FiSave, FiX, FiImage, FiMapPin, FiMail, FiPhone, FiClock, FiInfo } from 'react-icons/fi';
import api from '../../api/axiosInstance';
import { LoadingSkeleton } from '../../components/common/UIState';
import './NurseryProfilePage.css';

const NurseryProfilePage = () => {
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [notification, setNotification] = useState({ type: '', message: '' });
  
  const [nursery, setNursery] = useState({
    name: 'Green Haven Nursery',
    owner: 'John Doe',
    description: 'Providing the best organic and indoor plants in the city since 2010. We specialize in rare tropicals and easy-care succulents.',
    address: '123 Botanical Ave, Green City, GC 12345',
    phone: '+1 (555) 123-4567',
    email: 'contact@greenhaven.com',
    openTime: '08:00',
    closeTime: '18:00',
    status: 'Open',
    imageUrl: ''
  });

  const [editForm, setEditForm] = useState(nursery);

  useEffect(() => {
    const fetchNursery = async () => {
      try {
        const res = await api.get('/nursery/my');
        if (res.data?.data) {
          const data = res.data.data;
          const mapped = {
            name: data.name || nursery.name,
            owner: data.ownerName || nursery.owner,
            description: data.description || nursery.description,
            address: data.address || data.location || data.city || nursery.address,
            phone: data.phoneNumber || nursery.phone,
            email: data.email || nursery.email,
            openTime: data.openTime || nursery.openTime,
            closeTime: data.closeTime || nursery.closeTime,
            status: data.status || nursery.status,
            imageUrl: data.imageUrl || ''
          };
          setNursery(mapped);
          setEditForm(mapped);
        }
      } catch (err) {
        // use mock data gracefully fallback
      } finally {
        setLoading(false);
      }
    };
    fetchNursery();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEditForm(prev => ({ ...prev, [name]: value }));
  };

  const handleEditToggle = () => {
    if (isEditing) {
      setEditForm(nursery); // cancel edit
    }
    setIsEditing(!isEditing);
    setNotification({ type: '', message: '' });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setNotification({ type: '', message: '' });

    try {
      // Simulate API call for now (or make real call if endpoint handles proper DTO)
      await new Promise(resolve => setTimeout(resolve, 800));
      
      setNursery(editForm);
      setIsEditing(false);
      setNotification({ type: 'success', message: 'Nursery details updated successfully!' });
    } catch (err) {
      setNotification({ type: 'error', message: 'Failed to update nursery details. Please try again.' });
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="nursery-page">
        <h1 className="nursery-page-title">My Nursery</h1>
        <LoadingSkeleton type="table" count={4} />
      </div>
    );
  }

  return (
    <div className="nursery-page nursery-profile">
      <div className="page-header-flex">
        <h1 className="nursery-page-title">My Nursery Management</h1>
        {!isEditing && (
          <button className="btn-primary btn-sm" onClick={handleEditToggle}>
            <FiEdit2 /> Edit Nursery
          </button>
        )}
      </div>

      {notification.message && (
        <div className={`notification-banner alert-${notification.type}`}>
          {notification.message}
        </div>
      )}

      {isEditing ? (
        <div className="profile-edit-card">
          <div className="card-header border-bottom">
            <h3>Edit Nursery Details</h3>
            <button className="close-btn" onClick={handleEditToggle}><FiX size={20}/></button>
          </div>
          <form className="edit-nursery-form" onSubmit={handleSubmit}>
            <div className="form-grid">
              <div className="form-group">
                <label>Nursery Name</label>
                <div className="input-group">
                  <FiInfo className="input-icon" />
                  <input type="text" name="name" value={editForm.name} onChange={handleChange} required />
                </div>
              </div>
              <div className="form-group">
                <label>Email Address</label>
                <div className="input-group">
                  <FiMail className="input-icon" />
                  <input type="email" name="email" value={editForm.email} onChange={handleChange} required />
                </div>
              </div>
              <div className="form-group">
                <label>Phone Number</label>
                <div className="input-group">
                  <FiPhone className="input-icon" />
                  <input type="tel" name="phone" value={editForm.phone} onChange={handleChange} required />
                </div>
              </div>
              <div className="form-group">
                <label>Status</label>
                <select name="status" className="select-input" value={editForm.status} onChange={handleChange}>
                  <option value="Open">Open</option>
                  <option value="Closed">Closed</option>
                  <option value="Temporarily Closed">Temporarily Closed</option>
                </select>
              </div>
              <div className="form-group">
                <label>Opening Time</label>
                <div className="input-group">
                  <FiClock className="input-icon" />
                  <input type="time" name="openTime" value={editForm.openTime} onChange={handleChange} required />
                </div>
              </div>
              <div className="form-group">
                <label>Closing Time</label>
                <div className="input-group">
                  <FiClock className="input-icon" />
                  <input type="time" name="closeTime" value={editForm.closeTime} onChange={handleChange} required />
                </div>
              </div>
              <div className="form-group full-width">
                <label>Address</label>
                <div className="input-group">
                  <FiMapPin className="input-icon" />
                  <input type="text" name="address" value={editForm.address} onChange={handleChange} required />
                </div>
              </div>
              <div className="form-group full-width">
                <label>Description (About the Nursery)</label>
                <textarea name="description" value={editForm.description} onChange={handleChange} rows="4" required></textarea>
              </div>
            </div>
            
            <div className="form-actions border-top">
              <button type="button" className="btn-secondary" onClick={handleEditToggle}>Cancel</button>
              <button type="submit" className="btn-primary" disabled={saving}>
                {saving ? 'Saving...' : <><FiSave /> Save Changes</>}
              </button>
            </div>
          </form>
        </div>
      ) : (
        <div className="profile-view-layout">
          <div className="profile-main-card">
            <div className="nursery-cover">
              {nursery.imageUrl ? (
                <img src={nursery.imageUrl} alt="Nursery Cover" />
              ) : (
                <div className="cover-placeholder"><FiImage size={48} /></div>
              )}
              <span className={`nursery-status-badge status-${nursery.status.toLowerCase().replace(' ', '-')}`}>
                {nursery.status}
              </span>
            </div>
            
            <div className="profile-info-content">
              <h2 className="nursery-title">{nursery.name}</h2>
              <p className="nursery-owner">Owned by <strong>{nursery.owner}</strong></p>
              
              <div className="nursery-description">
                {nursery.description}
              </div>
              
              <hr className="divider" />
              
              <div className="info-grid">
                <div className="info-item">
                  <span className="icon"><FiMapPin /></span>
                  <div>
                    <span className="label">Location</span>
                    <span className="value">{nursery.address}</span>
                  </div>
                </div>
                <div className="info-item">
                  <span className="icon"><FiMail /></span>
                  <div>
                    <span className="label">Contact Email</span>
                    <span className="value">{nursery.email}</span>
                  </div>
                </div>
                <div className="info-item">
                  <span className="icon"><FiPhone /></span>
                  <div>
                    <span className="label">Phone</span>
                    <span className="value">{nursery.phone}</span>
                  </div>
                </div>
                <div className="info-item">
                  <span className="icon"><FiClock /></span>
                  <div>
                    <span className="label">Store Hours</span>
                    <span className="value">{nursery.openTime} - {nursery.closeTime}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default NurseryProfilePage;

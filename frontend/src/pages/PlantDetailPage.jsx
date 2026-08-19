import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { FiArrowLeft, FiSun, FiDroplet, FiShield, FiAlertCircle, FiCheckCircle, FiPackage, FiGrid } from 'react-icons/fi';
import api from '../api/axiosInstance';
import './PlantDetailPage.css';

const LeafIcon = ({ size = 20, className = '' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round" className={className}>
    <path d="M11 20A9 9 0 0 1 2.3 9A9 9 0 0 1 11 2c4.97 0 9 4.03 9 9 0 2.12-.74 4.07-1.97 5.61L21 20h-4l-1.39-1.39A8.93 8.93 0 0 1 11 20z" />
    <path d="M7 17L17 7" />
  </svg>
);

const PlantDetailPage = () => {
  const { plantId } = useParams();
  const navigate = useNavigate();

  const [plant, setPlant] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    setLoading(true);
    setError('');

    api.get(`/v1/plants/${plantId}`)
      .then((res) => {
        if (res.data && res.data.data) {
          setPlant(res.data.data);
        } else {
          setError('Plant details not found.');
        }
      })
      .catch((err) => {
        console.error('Error fetching plant detail:', err);
        if (err.response && err.response.status === 404) {
          setError('Plant details not found.');
        } else {
          setError('Unable to load plant details. Please try again.');
        }
      })
      .finally(() => setLoading(false));
  }, [plantId]);

  if (loading) {
    return (
      <div className="plant-detail-loading container">
        <div className="spinner"></div>
        <p>Loading plant details...</p>
      </div>
    );
  }

  if (error || !plant) {
    return (
      <div className="plant-detail-error container">
        <FiAlertCircle className="error-icon" />
        <h2>{error || 'Plant not found'}</h2>
        <p>The plant listing you requested could not be retrieved.</p>
        <Link to="/plants" className="btn-primary">
          <FiArrowLeft /> Back to Plants Catalog
        </Link>
      </div>
    );
  }

  return (
    <main className="plant-detail-page">
      <div className="container">
        {/* Back Link */}
        <div className="back-link-bar">
          <button onClick={() => navigate('/plants')} className="btn-back">
            <FiArrowLeft /> Back to Catalog
          </button>
        </div>

        {/* Main Details Grid */}
        <div className="plant-detail-grid">
          {/* Left Column: Large Image */}
          <div className="detail-image-panel">
            <div className="detail-image-card">
              <img
                src={
                  plant.imageUrl ||
                  'https://images.unsplash.com/photo-1545241047-6083a3684587?w=800&auto=format&fit=crop&q=80'
                }
                alt={plant.name}
                className="detail-main-img"
              />
              <span className={`detail-availability-badge ${plant.active !== false && plant.stock > 0 ? 'in-stock' : 'out-of-stock'}`}>
                {plant.active !== false && plant.stock > 0 ? 'In Stock' : 'Out of Stock'}
              </span>
            </div>
          </div>

          {/* Right Column: Info */}
          <div className="detail-info-panel">
            <div className="detail-category-tag">
              <LeafIcon /> {plant.categoryName || plant.plantType || 'Indoor Plant'}
            </div>

            <h1 className="detail-title">{plant.name}</h1>
            <p className="detail-scientific">{plant.scientificName || 'Botanical Species'}</p>

            <div className="detail-price-box">
              <span className="detail-price">₹{plant.price ? plant.price.toFixed(2) : '499.00'}</span>
              <span className="detail-sku">SKU: {plant.sku || 'N/A'}</span>
            </div>

            {/* Quick Meta Stats */}
            <div className="detail-meta-grid">
              <div className="meta-item">
                <FiGrid className="meta-icon" />
                <div className="meta-text">
                  <span className="meta-label">Category</span>
                  <span className="meta-val">{plant.categoryName || 'General'}</span>
                </div>
              </div>

              <div className="meta-item">
                <FiPackage className="meta-icon" />
                <div className="meta-text">
                  <span className="meta-label">Nursery Partner</span>
                  <span className="meta-val">{plant.nurseryName || 'Verified Nursery'}</span>
                </div>
              </div>

              <div className="meta-item">
                <FiSun className="meta-icon" />
                <div className="meta-text">
                  <span className="meta-label">Plant Type</span>
                  <span className="meta-val">{plant.plantType || 'Indoor'}</span>
                </div>
              </div>

              <div className="meta-item">
                <FiDroplet className="meta-icon" />
                <div className="meta-text">
                  <span className="meta-label">Available Units</span>
                  <span className="meta-val">{plant.stock ?? 10} units</span>
                </div>
              </div>
            </div>

            {/* Description */}
            <div className="detail-section-block">
              <h3>Description</h3>
              <p className="detail-description">
                {plant.description ||
                  'This healthy nursery-grown plant is specially curated for urban home spaces. Air-purifying, resilient, and easy to maintain under natural indoor light conditions.'}
              </p>
            </div>

            {/* Care Instructions */}
            <div className="detail-section-block care-instructions-box">
              <h3>🌱 Care Instructions & Tips</h3>
              <p className="care-text">
                {plant.careInstructions ||
                  'Water thoroughly when top 2 inches of soil feel dry. Provide bright indirect sunlight. Keep away from harsh air conditioning drafts.'}
              </p>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
};

export default PlantDetailPage;

import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FiLock, FiEye, FiShoppingBag, FiCheckCircle } from 'react-icons/fi';
import api from '../../api/axiosInstance';
import { useAuth } from '../../context/AuthContext';
import PlantImage from '../common/PlantImage';
import formatCurrency from '../../utils/formatCurrency';
import './FeaturedPlants.css';

const FeaturedPlants = () => {
  const { isAuthenticated } = useAuth();
  const [plants, setPlants] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (isAuthenticated) {
      setLoading(true);
      api.get('/v1/plants?pageSize=6')
        .then((res) => {
          if (res.data && res.data.data) {
            // Check if page response object or list
            const plantList = res.data.data.content || res.data.data;
            if (Array.isArray(plantList)) {
              setPlants(plantList);
            }
          }
        })
        .catch((err) => {
          console.warn('Unable to load plants catalog:', err);
          setError('Unable to load catalog from server.');
        })
        .finally(() => setLoading(false));
    }
  }, [isAuthenticated]);

  return (
    <section className="featured-plants-section" id="featured">
      <div className="container">
        <div className="featured-header">
          <div className="section-tag">Catalog Preview</div>
          <h2 className="section-title">Featured Plants</h2>
          <p className="section-subtitle">
            Explore healthy nursery greenery carefully selected for smart home care.
          </p>
        </div>

        {!isAuthenticated ? (
          /* Unauthenticated Empty State as specified by prompt */
          <div className="auth-locked-container">
            <div className="locked-card">
              <div className="lock-icon-box">
                <FiLock className="lock-icon" />
              </div>
              <h3 className="locked-title">Explore our plants after signing in.</h3>
              <p className="locked-subtitle">
                Log in to your account to view live inventory, plant prices, nursery availability, and AI care recommendations.
              </p>
              <div className="locked-actions">
                <Link to="/login" className="btn-primary">
                  Sign In to Browse Catalog
                </Link>
                <Link to="/register" className="btn-secondary">
                  Create Account
                </Link>
              </div>
            </div>
          </div>
        ) : loading ? (
          <div className="plants-loading-state">
            <div className="spinner"></div>
            <p>Loading plant catalog...</p>
          </div>
        ) : plants.length > 0 ? (
          <div className="plants-grid">
            {plants.map((plant) => (
              <div key={plant.id} className="plant-card">
                <div className="plant-img-wrapper">
                  <PlantImage
                    src={plant.imageUrl}
                    alt={plant.name}
                    aspectRatio="4/3"
                  />
                  <span className={`availability-tag ${plant.available !== false && plant.stock > 0 ? 'in-stock' : 'out-of-stock'}`}>
                    {plant.available !== false && plant.stock > 0 ? 'In Stock' : 'Unavailable'}
                  </span>
                </div>

                <div className="plant-info">
                  <div className="plant-type-badge">{plant.categoryName || plant.type || plant.plantType || 'Indoor'}</div>
                  <h3 className="plant-name">{plant.name}</h3>
                  <p className="plant-scientific">{plant.scientificName || 'Botanical Species'}</p>
                  
                  <div className="plant-price-row">
                    <span className="plant-price">{formatCurrency(plant.price || 499)}</span>
                    <Link to={`/plants/${plant.id}`} className="btn-view-plant">
                      <FiEye /> View Plant
                    </Link>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ) : (
          /* Authenticated but no products in database state */
          <div className="empty-catalog-box">
            <FiShoppingBag className="empty-icon" />
            <h3>No plants listed yet</h3>
            <p>Our nursery catalog is currently being updated. Check back soon for fresh arrivals!</p>
          </div>
        )}
      </div>
    </section>
  );
};

export default FeaturedPlants;

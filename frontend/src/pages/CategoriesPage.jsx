import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FiHome, FiSun, FiHeart, FiShield, FiArrowRight, FiAlertCircle, FiGrid } from 'react-icons/fi';
import api from '../api/axiosInstance';
import './CategoriesPage.css';

const LeafIcon = ({ size = 20, className = '' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round" className={className}>
    <path d="M11 20A9 9 0 0 1 2.3 9A9 9 0 0 1 11 2c4.97 0 9 4.03 9 9 0 2.12-.74 4.07-1.97 5.61L21 20h-4l-1.39-1.39A8.93 8.93 0 0 1 11 20z" />
    <path d="M7 17L17 7" />
  </svg>
);

// Fallback presentation categories if backend array is empty
const defaultCategories = [
  {
    id: 'indoor-plants',
    name: 'Indoor Plants',
    slug: 'indoor-plants',
    description: 'Purify indoor air and add lush green aesthetic to your home and office space.',
    icon: <FiHome />,
  },
  {
    id: 'outdoor-plants',
    name: 'Outdoor Plants',
    slug: 'outdoor-plants',
    description: 'Vibrant trees, shrubs, and climbers built to thrive under natural sun & weather.',
    icon: <FiSun />,
  },
  {
    id: 'flowering-plants',
    name: 'Flowering Plants',
    slug: 'flowering-plants',
    description: 'Bring pleasant fragrance and colorful blossoms to your garden and balcony.',
    icon: <FiHeart />,
  },
  {
    id: 'medicinal-plants',
    name: 'Medicinal Plants',
    slug: 'medicinal-plants',
    description: 'Natural wellness, aromatic herbs, and traditional therapeutic plants.',
    icon: <FiShield />,
  },
];

const CategoriesPage = () => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const navigate = useNavigate();

  const fetchCategories = () => {
    setLoading(true);
    setError('');

    api.get('/v1/categories')
      .then((res) => {
        if (res.data && res.data.data && Array.isArray(res.data.data) && res.data.data.length > 0) {
          setCategories(res.data.data);
        } else {
          setCategories(defaultCategories);
        }
      })
      .catch((err) => {
        console.error('Error loading categories:', err);
        if (err.response) {
          if (err.response.status === 401) {
            setError('Your session has expired. Please login again.');
          } else if (err.response.status === 403) {
            setError('You do not have permission to access this page.');
          } else {
            setError('Unable to load categories. Please try again.');
          }
        } else {
          setError('Unable to load categories. Please try again.');
        }
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  const handleExploreCategory = (catId) => {
    navigate(`/plants?category=${catId}`);
  };

  return (
    <main className="categories-page">
      {/* Header Banner */}
      <section className="categories-hero">
        <div className="container">
          <div className="categories-hero-content">
            <div className="section-tag">
              <LeafIcon /> Plant Taxonomies
            </div>
            <h1 className="categories-hero-title">Explore Categories</h1>
            <p className="categories-hero-subtitle">
              Find the right plants for your space and lifestyle.
            </p>
          </div>
        </div>
      </section>

      {/* Main Categories Grid */}
      <section className="categories-body">
        <div className="container">
          {error && (
            <div className="categories-error-banner">
              <FiAlertCircle className="error-icon" />
              <span>{error}</span>
              <button onClick={fetchCategories} className="btn-retry">Retry</button>
            </div>
          )}

          {loading ? (
            <div className="categories-grid">
              {[1, 2, 3, 4].map((i) => (
                <div key={i} className="category-card skeleton-card">
                  <div className="skeleton-icon"></div>
                  <div className="skeleton-line short"></div>
                  <div className="skeleton-line long"></div>
                  <div className="skeleton-line medium"></div>
                </div>
              ))}
            </div>
          ) : categories.length > 0 ? (
            <div className="categories-grid">
              {categories.map((cat, idx) => (
                <div key={cat.id || idx} className="category-card">
                  <div className="cat-card-header">
                    <div className="cat-icon-wrapper">
                      {cat.icon || <LeafIcon />}
                    </div>
                  </div>
                  <h3 className="cat-title">{cat.name}</h3>
                  <p className="cat-desc">
                    {cat.description || 'Quality selection of plants suited for indoor and outdoor living.'}
                  </p>

                  <div className="cat-footer">
                    <button
                      onClick={() => handleExploreCategory(cat.id || cat.slug)}
                      className="btn-explore-cat"
                    >
                      Explore Plants <FiArrowRight className="arrow-icon" />
                    </button>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="categories-empty-state">
              <FiGrid className="empty-icon" />
              <h3>No categories available.</h3>
              <p>Check back later as new plant categories are added by our nurseries.</p>
            </div>
          )}
        </div>
      </section>
    </main>
  );
};

export default CategoriesPage;

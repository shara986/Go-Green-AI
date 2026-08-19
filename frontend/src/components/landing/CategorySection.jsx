import React, { useState, useEffect } from 'react';
import { FiHome, FiSun, FiHeart, FiShield, FiArrowRight } from 'react-icons/fi';
import api from '../../api/axiosInstance';
import { useAuth } from '../../context/AuthContext';
import './CategorySection.css';

const defaultCategories = [
  {
    name: 'Indoor Plants',
    slug: 'indoor-plants',
    description: 'Purify indoor air and add lush green aesthetic to your home and office space.',
    icon: <FiHome />,
    badge: 'Popular',
  },
  {
    name: 'Outdoor Plants',
    slug: 'outdoor-plants',
    description: 'Vibrant trees, shrubs, and climbers built to thrive under natural sun & weather.',
    icon: <FiSun />,
    badge: 'Resilient',
  },
  {
    name: 'Flowering Plants',
    slug: 'flowering-plants',
    description: 'Bring pleasant fragrance and colorful blossoms to your garden and balcony.',
    icon: <FiHeart />,
    badge: 'Seasonal',
  },
  {
    name: 'Medicinal Plants',
    slug: 'medicinal-plants',
    description: 'Natural wellness, aromatic herbs, and traditional therapeutic plants.',
    icon: <FiShield />,
    badge: 'Herbal',
  },
];

const CategorySection = () => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(false);
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    // Only attempt backend category fetch if authenticated and appropriate endpoint exists
    if (isAuthenticated) {
      setLoading(true);
      api.get('/v1/categories')
        .then((res) => {
          if (res.data && res.data.data && Array.isArray(res.data.data) && res.data.data.length > 0) {
            setCategories(res.data.data);
          }
        })
        .catch(() => {
          // If unauthorized or endpoint not found, preserve presentation structure
        })
        .finally(() => setLoading(false));
    }
  }, [isAuthenticated]);

  const displayList = categories.length > 0 ? categories : defaultCategories;

  return (
    <section className="category-section" id="categories">
      <div className="container">
        <div className="category-header">
          <div className="section-tag">Categories</div>
          <h2 className="section-title">Explore Plant Categories</h2>
          <p className="section-subtitle">
            Browse our curated collections designed for every space and care level.
          </p>
        </div>

        <div className="category-grid">
          {displayList.map((cat, idx) => (
            <div key={cat.id || cat.slug || idx} className="category-card">
              <div className="cat-card-header">
                <div className="cat-icon-wrapper">
                  {cat.icon || <FiHome />}
                </div>
                {cat.badge && <span className="cat-badge">{cat.badge}</span>}
              </div>
              <h3 className="cat-title">{cat.name}</h3>
              <p className="cat-desc">{cat.description || 'Quality selection of plants for your home and garden.'}</p>
              
              <div className="cat-footer">
                <span className="cat-action">
                  Explore Collection <FiArrowRight className="arrow-icon" />
                </span>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default CategorySection;

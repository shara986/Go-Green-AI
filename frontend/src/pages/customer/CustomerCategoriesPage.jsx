import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { FiArrowRight, FiTag } from 'react-icons/fi';
import CustomerLayout from '../../components/customer/CustomerLayout';
import { LoadingSkeleton, EmptyState, ErrorState } from '../../components/common/UIState';
import { fetchCategories } from '../../api/customerApi';
import './CustomerCategoriesPage.css';

const DEFAULT_CATEGORY_IMAGES = [
  'https://images.unsplash.com/photo-1512428559087-560fa5ceab42?w=600&auto=format&fit=crop&q=80',
  'https://images.unsplash.com/photo-1463936575829-25148e1db1b8?w=600&auto=format&fit=crop&q=80',
  'https://images.unsplash.com/photo-1545241047-6083a3684587?w=600&auto=format&fit=crop&q=80',
  'https://images.unsplash.com/photo-1509423350716-97f9360b4e09?w=600&auto=format&fit=crop&q=80',
  'https://images.unsplash.com/photo-1485955900006-10f4d324d411?w=600&auto=format&fit=crop&q=80',
];

const CustomerCategoriesPage = () => {
  const navigate = useNavigate();
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadCategories = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchCategories();
      const list = Array.isArray(data) ? data : data?.content || [];
      setCategories(list);
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCategories();
  }, []);

  const handleExploreCategory = (cat) => {
    navigate(`/customer/plants?category=${cat.id}`);
  };

  return (
    <CustomerLayout>
      <div className="categories-page-container">
        <div className="categories-header">
          <h1 className="categories-title">Plant Categories</h1>
          <p className="categories-subtitle">Browse plants organized by environment, species, and care requirements.</p>
        </div>

        {loading ? (
          <LoadingSkeleton type="cards" count={6} />
        ) : error ? (
          <ErrorState error={error} onRetry={loadCategories} />
        ) : categories.length === 0 ? (
          <EmptyState
            title="No Categories Available"
            message="There are currently no plant categories set up in the store."
            actionText="Explore Plants"
            actionLink="/customer/plants"
          />
        ) : (
          <div className="categories-grid">
            {categories.map((cat, idx) => {
              const fallbackImg = DEFAULT_CATEGORY_IMAGES[idx % DEFAULT_CATEGORY_IMAGES.length];
              return (
                <div key={cat.id} className="category-card">
                  <div className="category-card-image-wrap">
                    <img
                      src={cat.imageUrl || fallbackImg}
                      alt={cat.name}
                      className="category-card-image"
                      onError={(e) => {
                        e.target.onerror = null;
                        e.target.src = fallbackImg;
                      }}
                    />
                    <div className="category-card-overlay" />
                    <div className="category-card-icon">
                      <FiTag size={20} />
                    </div>
                  </div>

                  <div className="category-card-body">
                    <h3 className="category-card-name">{cat.name}</h3>
                    <p className="category-card-desc">
                      {cat.description || 'Explore our selection of plants curated for this category.'}
                    </p>

                    <div className="category-card-footer">
                      {cat.plantCount !== undefined && cat.plantCount !== null ? (
                        <span className="category-plant-count">{cat.plantCount} Plants</span>
                      ) : (
                        <span className="category-active-tag">Active Category</span>
                      )}

                      <button
                        className="category-explore-btn"
                        onClick={() => handleExploreCategory(cat)}
                      >
                        Explore <FiArrowRight size={14} />
                      </button>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>
    </CustomerLayout>
  );
};

export default CustomerCategoriesPage;

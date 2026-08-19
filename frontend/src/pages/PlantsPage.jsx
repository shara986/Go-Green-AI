import React, { useState, useEffect } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { FiSearch, FiFilter, FiEye, FiShoppingBag, FiAlertCircle, FiRefreshCw, FiCheckCircle, FiXCircle } from 'react-icons/fi';
import api from '../api/axiosInstance';
import './PlantsPage.css';

const LeafIcon = ({ size = 20, className = '' }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round" className={className}>
    <path d="M11 20A9 9 0 0 1 2.3 9A9 9 0 0 1 11 2c4.97 0 9 4.03 9 9 0 2.12-.74 4.07-1.97 5.61L21 20h-4l-1.39-1.39A8.93 8.93 0 0 1 11 20z" />
    <path d="M7 17L17 7" />
  </svg>
);

const plantTypes = [
  'ALL',
  'INDOOR',
  'OUTDOOR',
  'SUCCULENT',
  'FLOWERING',
  'HERB',
  'VEGETABLE',
  'FRUIT',
  'BONSAI',
  'MEDICINAL',
];

const PlantsPage = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const initialCategory = searchParams.get('category') || '';
  const initialSearch = searchParams.get('search') || '';

  const [plants, setPlants] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [errorStatus, setErrorStatus] = useState(null);

  const [searchQuery, setSearchQuery] = useState(initialSearch);
  const [selectedCategory, setSelectedCategory] = useState(initialCategory);
  const [selectedType, setSelectedType] = useState('ALL');
  const [activeOnly, setActiveOnly] = useState(false);

  // Load categories for filter dropdown
  useEffect(() => {
    api.get('/v1/categories')
      .then((res) => {
        if (res.data && res.data.data) {
          setCategories(res.data.data);
        }
      })
      .catch(() => {
        // Ignore category filter load error
      });
  }, []);

  // Fetch plants based on search and filters
  const fetchPlants = () => {
    setLoading(true);
    setError('');
    setErrorStatus(null);

    let endpoint = '/v1/plants?pageSize=20';

    if (selectedCategory) {
      endpoint = `/v1/plants/category/${selectedCategory}`;
    }

    api.get(endpoint)
      .then((res) => {
        if (res.data && res.data.data) {
          let list = res.data.data.content || res.data.data;
          if (!Array.isArray(list)) {
            list = [];
          }

          // Apply client-side filter for search query and type if present
          if (searchQuery.trim()) {
            const q = searchQuery.toLowerCase().trim();
            list = list.filter(
              (p) =>
                (p.name && p.name.toLowerCase().includes(q)) ||
                (p.scientificName && p.scientificName.toLowerCase().includes(q)) ||
                (p.description && p.description.toLowerCase().includes(q))
            );
          }

          if (selectedType !== 'ALL') {
            list = list.filter((p) => p.plantType === selectedType || p.type === selectedType);
          }

          if (activeOnly) {
            list = list.filter((p) => p.active || p.available);
          }

          setPlants(list);
        } else {
          setPlants([]);
        }
      })
      .catch((err) => {
        console.error('Error loading plants:', err);
        if (err.response) {
          setErrorStatus(err.response.status);
          if (err.response.status === 401) {
            setError('Your session has expired. Please login again.');
          } else if (err.response.status === 403) {
            setError('You do not have permission to access this page.');
          } else {
            setError('Unable to load plants. Please try again.');
          }
        } else {
          setError('Unable to load plants. Please try again.');
        }
        setPlants([]);
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchPlants();
  }, [selectedCategory, selectedType, activeOnly]);

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    fetchPlants();
  };

  const handleResetFilters = () => {
    setSearchQuery('');
    setSelectedCategory('');
    setSelectedType('ALL');
    setActiveOnly(false);
    setSearchParams({});
  };

  return (
    <main className="plants-page">
      {/* Header Banner */}
      <section className="plants-hero">
        <div className="container">
          <div className="plants-hero-content">
            <div className="section-tag">
              <LeafIcon /> Catalog Explorer
            </div>
            <h1 className="plants-hero-title">Explore Plants</h1>
            <p className="plants-hero-subtitle">
              Discover plants that fit your space, lifestyle, and growing needs.
            </p>
          </div>
        </div>
      </section>

      {/* Main Content Section */}
      <section className="plants-body">
        <div className="container">
          {/* Search & Filter Bar */}
          <div className="search-filter-bar">
            <form onSubmit={handleSearchSubmit} className="search-form">
              <div className="search-input-wrapper">
                <FiSearch className="search-icon" />
                <input
                  type="text"
                  className="search-input"
                  placeholder="Search plants by name, species..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                />
              </div>
              <button type="submit" className="btn-primary search-btn">
                Search
              </button>
            </form>

            <div className="filters-group">
              {/* Category Filter */}
              <div className="filter-select-wrapper">
                <select
                  className="filter-select"
                  value={selectedCategory}
                  onChange={(e) => {
                    setSelectedCategory(e.target.value);
                    if (e.target.value) {
                      setSearchParams({ category: e.target.value });
                    } else {
                      setSearchParams({});
                    }
                  }}
                >
                  <option value="">All Categories</option>
                  {categories.map((cat) => (
                    <option key={cat.id} value={cat.id}>
                      {cat.name}
                    </option>
                  ))}
                </select>
              </div>

              {/* Plant Type Filter */}
              <div className="filter-select-wrapper">
                <select
                  className="filter-select"
                  value={selectedType}
                  onChange={(e) => setSelectedType(e.target.value)}
                >
                  {plantTypes.map((type) => (
                    <option key={type} value={type}>
                      {type === 'ALL' ? 'All Types' : type}
                    </option>
                  ))}
                </select>
              </div>

              {/* Active / In Stock Toggle */}
              <label className="checkbox-filter">
                <input
                  type="checkbox"
                  checked={activeOnly}
                  onChange={(e) => setActiveOnly(e.target.checked)}
                />
                <span>In Stock Only</span>
              </label>

              {(searchQuery || selectedCategory || selectedType !== 'ALL' || activeOnly) && (
                <button onClick={handleResetFilters} className="btn-reset-filters">
                  <FiRefreshCw /> Reset
                </button>
              )}
            </div>
          </div>

          {/* Error Banner */}
          {error && (
            <div className="plants-error-banner">
              <FiAlertCircle className="error-icon" />
              <span>{error}</span>
              <button onClick={fetchPlants} className="btn-retry">Retry</button>
            </div>
          )}

          {/* Loading Skeleton Grid */}
          {loading ? (
            <div className="plants-grid">
              {[1, 2, 3, 4, 5, 6, 7, 8].map((i) => (
                <div key={i} className="plant-card skeleton-card">
                  <div className="skeleton-img"></div>
                  <div className="skeleton-body">
                    <div className="skeleton-line short"></div>
                    <div className="skeleton-line long"></div>
                    <div className="skeleton-line medium"></div>
                    <div className="skeleton-footer"></div>
                  </div>
                </div>
              ))}
            </div>
          ) : plants.length > 0 ? (
            <div className="plants-grid">
              {plants.map((plant) => (
                <div key={plant.id} className="plant-card">
                  <div className="plant-img-wrapper">
                    <img
                      src={
                        plant.imageUrl ||
                        'https://images.unsplash.com/photo-1545241047-6083a3684587?w=500&auto=format&fit=crop&q=60'
                      }
                      alt={plant.name}
                      className="plant-img"
                    />
                    <span
                      className={`availability-tag ${
                        plant.active !== false && plant.stock > 0 ? 'in-stock' : 'out-of-stock'
                      }`}
                    >
                      {plant.active !== false && plant.stock > 0 ? 'In Stock' : 'Unavailable'}
                    </span>
                  </div>

                  <div className="plant-info">
                    <div className="plant-type-badge">
                      {plant.categoryName || plant.plantType || 'General'}
                    </div>
                    <h3 className="plant-name">{plant.name}</h3>
                    <p className="plant-scientific">{plant.scientificName || 'Botanical Species'}</p>

                    <div className="plant-price-row">
                      <span className="plant-price">₹{plant.price ? plant.price.toFixed(2) : '499.00'}</span>
                      <Link to={`/plants/${plant.id}`} className="btn-view-plant">
                        <FiEye /> View Details
                      </Link>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            /* Empty State */
            <div className="plants-empty-state">
              <FiShoppingBag className="empty-icon" />
              <h3>No plants found.</h3>
              <p>We couldn't find any plants matching your current search or filter options.</p>
              <button onClick={handleResetFilters} className="btn-secondary">
                Clear Filters & View All
              </button>
            </div>
          )}
        </div>
      </section>
    </main>
  );
};

export default PlantsPage;

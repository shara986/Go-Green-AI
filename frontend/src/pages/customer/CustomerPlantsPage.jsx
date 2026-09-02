import React, { useState, useEffect, useMemo } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { FiSearch, FiShoppingCart, FiHeart, FiEye, FiFilter } from 'react-icons/fi';
import CustomerLayout, { useCustomer } from '../../components/customer/CustomerLayout';
import { LoadingSkeleton, EmptyState, ErrorState } from '../../components/common/UIState';
import { fetchPlants, fetchCategories, addToCart } from '../../api/customerApi';
import './CustomerPlantsPage.css';

const CustomerPlantsPage = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const initialCategory = searchParams.get('category') || '';

  const { refreshCartCount } = useCustomer();
  const [plants, setPlants] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Filters state
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState(initialCategory);
  const [selectedType, setSelectedType] = useState('');
  const [sortBy, setSortBy] = useState('newest');
  const [addingId, setAddingId] = useState(null);
  const [toastMessage, setToastMessage] = useState(null);

  const showToast = (msg) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3000);
  };

  const loadData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [plantsData, categoriesData] = await Promise.all([
        fetchPlants({ pageSize: 100 }),
        fetchCategories(),
      ]);

      const plantList = Array.isArray(plantsData)
        ? plantsData
        : plantsData?.content || [];
      const categoryList = Array.isArray(categoriesData)
        ? categoriesData
        : categoriesData?.content || [];

      setPlants(plantList);
      setCategories(categoryList);
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  // Update selectedCategory if URL search param changes
  useEffect(() => {
    if (searchParams.get('category')) {
      setSelectedCategory(searchParams.get('category'));
    }
  }, [searchParams]);

  // Extract unique plant types from dataset
  const plantTypes = useMemo(() => {
    const types = new Set();
    plants.forEach((p) => {
      if (p.plantType) types.add(p.plantType);
    });
    return Array.from(types);
  }, [plants]);

  // Filter & Sort plants
  const filteredPlants = useMemo(() => {
    return plants
      .filter((plant) => {
        // Search filter
        const matchesSearch =
          !searchTerm ||
          plant.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
          plant.scientificName?.toLowerCase().includes(searchTerm.toLowerCase());

        // Category filter
        const matchesCategory =
          !selectedCategory ||
          plant.categoryId === selectedCategory ||
          plant.categoryName?.toLowerCase() === selectedCategory.toLowerCase();

        // Plant type filter
        const matchesType =
          !selectedType ||
          plant.plantType?.toLowerCase() === selectedType.toLowerCase();

        return matchesSearch && matchesCategory && matchesType;
      })
      .sort((a, b) => {
        if (sortBy === 'price-low') return (a.price || 0) - (b.price || 0);
        if (sortBy === 'price-high') return (b.price || 0) - (a.price || 0);
        if (sortBy === 'name-asc') return (a.name || '').localeCompare(b.name || '');
        return 0; // default newest / server order
      });
  }, [plants, searchTerm, selectedCategory, selectedType, sortBy]);

  const handleAddToCart = async (plant) => {
    setAddingId(plant.id);
    try {
      await addToCart(plant.id, 1);
      await refreshCartCount();
      showToast(`Added "${plant.name}" to cart! 🛒`);
    } catch (err) {
      showToast(err?.response?.data?.message || 'Failed to add to cart.');
    } finally {
      setAddingId(null);
    }
  };

  const handleWishlistClick = () => {
    showToast('Wishlist feature is currently unavailable.');
  };

  return (
    <CustomerLayout>
      <div className="plants-page-container">
        {/* Header */}
        <div className="plants-header">
          <div>
            <h1 className="plants-title">Explore Plants</h1>
            <p className="plants-subtitle">Find high-quality indoor & outdoor plants sourced directly from top nurseries.</p>
          </div>
        </div>

        {/* Toast Notification */}
        {toastMessage && <div className="toast-notification">{toastMessage}</div>}

        {/* Controls Bar */}
        <div className="plants-controls-card">
          <div className="search-input-wrapper">
            <FiSearch className="search-icon" size={18} />
            <input
              type="text"
              placeholder="Search plants by name or scientific name…"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>

          <div className="filters-group">
            <div className="filter-select-wrapper">
              <FiFilter className="filter-icon" size={14} />
              <select
                value={selectedCategory}
                onChange={(e) => {
                  setSelectedCategory(e.target.value);
                  setSearchParams(e.target.value ? { category: e.target.value } : {});
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

            {plantTypes.length > 0 && (
              <div className="filter-select-wrapper">
                <select
                  value={selectedType}
                  onChange={(e) => setSelectedType(e.target.value)}
                >
                  <option value="">All Plant Types</option>
                  {plantTypes.map((type) => (
                    <option key={type} value={type}>
                      {type}
                    </option>
                  ))}
                </select>
              </div>
            )}

            <div className="filter-select-wrapper">
              <select value={sortBy} onChange={(e) => setSortBy(e.target.value)}>
                <option value="newest">Sort by: Newest</option>
                <option value="price-low">Price: Low to High</option>
                <option value="price-high">Price: High to Low</option>
                <option value="name-asc">Name: A to Z</option>
              </select>
            </div>
          </div>
        </div>

        {/* Main Content State */}
        {loading ? (
          <LoadingSkeleton type="cards" count={8} />
        ) : error ? (
          <ErrorState error={error} onRetry={loadData} />
        ) : filteredPlants.length === 0 ? (
          <EmptyState
            title="Currently no plants are available."
            message={
              searchTerm || selectedCategory || selectedType
                ? 'No plants match your search or filter criteria. Try clearing your filters.'
                : 'There are no active plants in the catalogue right now.'
            }
            actionText={searchTerm || selectedCategory ? 'Clear Filters' : undefined}
            onAction={() => {
              setSearchTerm('');
              setSelectedCategory('');
              setSelectedType('');
              setSearchParams({});
            }}
          />
        ) : (
          <div className="plants-grid">
            {filteredPlants.map((plant) => {
              const inStock = plant.stockQuantity > 0;
              return (
                <div key={plant.id} className="plant-card">
                  <div className="plant-card-image-wrap">
                    <img
                      src={plant.imageUrl || 'https://images.unsplash.com/photo-1545241047-6083a3684587?w=500&auto=format&fit=crop&q=60'}
                      alt={plant.name}
                      className="plant-card-image"
                      onError={(e) => {
                        e.target.onerror = null;
                        e.target.src = 'https://images.unsplash.com/photo-1545241047-6083a3684587?w=500&auto=format&fit=crop&q=60';
                      }}
                    />
                    <span className={`stock-badge ${inStock ? 'in-stock' : 'out-of-stock'}`}>
                      {inStock ? `In Stock (${plant.stockQuantity})` : 'Out of Stock'}
                    </span>
                    <button
                      className="wishlist-btn-overlay"
                      title="Add to Wishlist"
                      onClick={handleWishlistClick}
                    >
                      <FiHeart size={16} />
                    </button>
                  </div>

                  <div className="plant-card-body">
                    <div className="plant-category-badge">{plant.categoryName || plant.plantType || 'Plant'}</div>
                    <h3 className="plant-card-name">{plant.name}</h3>
                    {plant.scientificName && (
                      <p className="plant-scientific-name"><em>{plant.scientificName}</em></p>
                    )}
                    {plant.nurseryName && (
                      <p className="plant-nursery-tag">Nursery: {plant.nurseryName}</p>
                    )}

                    <div className="plant-card-footer">
                      <div className="plant-price">${Number(plant.price || 0).toFixed(2)}</div>
                      <div className="plant-actions">
                        <Link
                          to={`/customer/plants/${plant.id}`}
                          className="plant-action-btn view-btn"
                          title="View Details"
                        >
                          <FiEye size={16} />
                        </Link>
                        <button
                          className="plant-action-btn cart-btn"
                          disabled={!inStock || addingId === plant.id}
                          onClick={() => handleAddToCart(plant)}
                          title={inStock ? 'Add to Cart' : 'Out of Stock'}
                        >
                          <FiShoppingCart size={16} />
                          {addingId === plant.id ? 'Adding…' : 'Add'}
                        </button>
                      </div>
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

export default CustomerPlantsPage;

import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import {
  FiArrowLeft, FiShoppingCart, FiHeart,
  FiCheckCircle, FiXCircle, FiTruck, FiShield, FiSun, FiDroplet
} from 'react-icons/fi';
import CustomerLayout, { useCustomer } from '../../components/customer/CustomerLayout';
import { LoadingSkeleton, EmptyState, ErrorState } from '../../components/common/UIState';
import { fetchPlantById, addToCart } from '../../api/customerApi';
import './CustomerPlantDetailPage.css';

const CustomerPlantDetailPage = () => {
  const { plantId } = useParams();
  const navigate = useNavigate();
  const { refreshCartCount } = useCustomer();

  const [plant, setPlant] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [adding, setAdding] = useState(false);
  const [toastMessage, setToastMessage] = useState(null);

  const showToast = (msg) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3000);
  };

  const loadPlant = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchPlantById(plantId);
      setPlant(data);
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPlant();
  }, [plantId]);

  const handleAddToCart = async () => {
    if (!plant) return;
    setAdding(true);
    try {
      await addToCart(plant.id, quantity);
      await refreshCartCount();
      showToast(`Added ${quantity} x "${plant.name}" to cart! 🛒`);
    } catch (err) {
      showToast(err?.response?.data?.message || 'Failed to add item to cart.');
    } finally {
      setAdding(false);
    }
  };

  const handleWishlistClick = () => {
    showToast('Wishlist feature is currently unavailable.');
  };

  if (loading) {
    return (
      <CustomerLayout>
        <div className="plant-detail-container">
          <LoadingSkeleton type="table" count={6} />
        </div>
      </CustomerLayout>
    );
  }

  if (error) {
    const is404 = error?.response?.status === 404;
    return (
      <CustomerLayout>
        <div className="plant-detail-container">
          {is404 ? (
            <EmptyState
              title="Plant Not Found"
              message="The plant you are looking for does not exist or has been removed."
              actionText="Back to Plants"
              actionLink="/customer/plants"
            />
          ) : (
            <ErrorState error={error} onRetry={loadPlant} />
          )}
        </div>
      </CustomerLayout>
    );
  }

  if (!plant) {
    return (
      <CustomerLayout>
        <div className="plant-detail-container">
          <EmptyState
            title="Plant Not Found"
            message="No plant details returned from the server."
            actionText="Back to Plants"
            actionLink="/customer/plants"
          />
        </div>
      </CustomerLayout>
    );
  }

  const inStock = plant.stockQuantity > 0;

  return (
    <CustomerLayout>
      <div className="plant-detail-container">
        {/* Navigation back */}
        <div className="detail-top-bar">
          <Link to="/customer/plants" className="back-link-btn">
            <FiArrowLeft size={16} /> Back to Plants
          </Link>
        </div>

        {/* Toast Notification */}
        {toastMessage && <div className="toast-notification">{toastMessage}</div>}

        {/* Main Detail Grid */}
        <div className="plant-detail-card">
          {/* Left: Image */}
          <div className="detail-image-section">
            <img
              src={plant.imageUrl || 'https://images.unsplash.com/photo-1545241047-6083a3684587?w=800&auto=format&fit=crop&q=80'}
              alt={plant.name}
              className="detail-main-image"
              onError={(e) => {
                e.target.onerror = null;
                e.target.src = 'https://images.unsplash.com/photo-1545241047-6083a3684587?w=800&auto=format&fit=crop&q=80';
              }}
            />
          </div>

          {/* Right: Info */}
          <div className="detail-info-section">
            <div className="detail-tags">
              <span className="category-tag">{plant.categoryName || 'General Category'}</span>
              {plant.plantType && <span className="type-tag">{plant.plantType}</span>}
            </div>

            <h1 className="detail-title">{plant.name}</h1>
            {plant.scientificName && (
              <p className="detail-scientific"><em>{plant.scientificName}</em></p>
            )}

            {plant.nurseryName && (
              <div className="nursery-info-box">
                Provided by: <strong>{plant.nurseryName}</strong>
              </div>
            )}

            <div className="detail-price-row">
              <div className="detail-price">${Number(plant.price || 0).toFixed(2)}</div>
              <div className={`stock-status ${inStock ? 'in-stock' : 'out-of-stock'}`}>
                {inStock ? <FiCheckCircle size={14} /> : <FiXCircle size={14} />}
                {inStock ? `In Stock (${plant.stockQuantity} available)` : 'Out of Stock'}
              </div>
            </div>

            <div className="detail-divider" />

            {/* Description */}
            <div className="detail-description">
              <h3>About this Plant</h3>
              <p>{plant.description || 'No description provided for this plant.'}</p>
            </div>

            {/* Care instructions */}
            {plant.careInstructions && (
              <div className="care-instructions-card">
                <h3>Care Instructions</h3>
                <div className="care-grid">
                  <div className="care-item">
                    <FiSun className="care-icon" />
                    <div>
                      <strong>Care Details</strong>
                      <p>{plant.careInstructions}</p>
                    </div>
                  </div>
                </div>
              </div>
            )}

            {/* Purchase Controls */}
            <div className="detail-purchase-row">
              <div className="qty-picker">
                <button
                  type="button"
                  onClick={() => setQuantity((q) => Math.max(1, q - 1))}
                  disabled={quantity <= 1 || !inStock}
                >
                  -
                </button>
                <span>{quantity}</span>
                <button
                  type="button"
                  onClick={() => setQuantity((q) => Math.min(plant.stockQuantity || 99, q + 1))}
                  disabled={quantity >= (plant.stockQuantity || 1) || !inStock}
                >
                  +
                </button>
              </div>

              <button
                className="add-cart-primary-btn"
                disabled={!inStock || adding}
                onClick={handleAddToCart}
              >
                <FiShoppingCart size={18} />
                {adding ? 'Adding to Cart…' : 'Add to Cart'}
              </button>

              <button
                className="wishlist-secondary-btn"
                onClick={handleWishlistClick}
                title="Add to Wishlist"
              >
                <FiHeart size={18} />
              </button>
            </div>

            {/* Value Props */}
            <div className="value-props">
              <div className="prop-item">
                <FiTruck size={16} /> Eco-friendly nursery delivery
              </div>
              <div className="prop-item">
                <FiShield size={16} /> Guaranteed healthy plant on arrival
              </div>
            </div>
          </div>
        </div>
      </div>
    </CustomerLayout>
  );
};

export default CustomerPlantDetailPage;

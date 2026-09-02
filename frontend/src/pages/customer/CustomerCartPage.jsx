import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
  FiShoppingCart, FiTrash2, FiArrowRight,
  FiShield, FiTruck, FiX
} from 'react-icons/fi';
import CustomerLayout, { useCustomer } from '../../components/customer/CustomerLayout';
import { LoadingSkeleton, EmptyState, ErrorState } from '../../components/common/UIState';
import { fetchCart, updateCartItemQuantity, removeCartItem, checkoutCart } from '../../api/customerApi';
import './CustomerCartPage.css';

const CustomerCartPage = () => {
  const navigate = useNavigate();
  const { refreshCartCount } = useCustomer();

  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [updatingItemId, setUpdatingItemId] = useState(null);
  const [toastMessage, setToastMessage] = useState(null);

  // Checkout Modal State
  const [isCheckoutOpen, setIsCheckoutOpen] = useState(false);
  const [shippingAddress, setShippingAddress] = useState('');
  const [contactPhone, setContactPhone] = useState('');
  const [notes, setNotes] = useState('');
  const [checkingOut, setCheckingOut] = useState(false);

  const showToast = (msg) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3000);
  };

  const loadCart = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchCart();
      setCart(data);
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCart();
  }, []);

  const handleUpdateQuantity = async (item, newQty) => {
    if (newQty < 1) return;
    if (item.stockQuantity && newQty > item.stockQuantity) {
      showToast(`Only ${item.stockQuantity} items available in stock.`);
      return;
    }

    setUpdatingItemId(item.id);
    try {
      const updatedCart = await updateCartItemQuantity(item.id, newQty);
      setCart(updatedCart);
      await refreshCartCount();
    } catch (err) {
      showToast(err?.response?.data?.message || 'Failed to update item quantity.');
    } finally {
      setUpdatingItemId(null);
    }
  };

  const handleRemoveItem = async (itemId) => {
    setUpdatingItemId(itemId);
    try {
      const updatedCart = await removeCartItem(itemId);
      setCart(updatedCart);
      await refreshCartCount();
      showToast('Item removed from cart.');
    } catch (err) {
      showToast(err?.response?.data?.message || 'Failed to remove item.');
    } finally {
      setUpdatingItemId(null);
    }
  };

  const handleProceedToCheckout = async (e) => {
    e.preventDefault();
    if (!shippingAddress.trim()) {
      showToast('Please enter your shipping address.');
      return;
    }

    setCheckingOut(true);
    try {
      const order = await checkoutCart({
        shippingAddress,
        contactPhone,
        notes,
        paymentMethod: 'CREDIT_CARD',
      });
      await refreshCartCount();
      setIsCheckoutOpen(false);
      showToast('Order placed successfully! 🎉');
      navigate(`/customer/orders/${order.id}`);
    } catch (err) {
      showToast(err?.response?.data?.message || 'Failed to place order. Please try again.');
    } finally {
      setCheckingOut(false);
    }
  };

  if (loading) {
    return (
      <CustomerLayout>
        <div className="cart-page-container">
          <LoadingSkeleton type="table" count={4} />
        </div>
      </CustomerLayout>
    );
  }

  if (error) {
    return (
      <CustomerLayout>
        <div className="cart-page-container">
          <ErrorState error={error} onRetry={loadCart} />
        </div>
      </CustomerLayout>
    );
  }

  const items = cart?.items || (Array.isArray(cart) ? cart : []);
  const isEmpty = items.length === 0;

  const subtotal = cart?.totalAmount !== undefined
    ? cart.totalAmount
    : items.reduce((acc, item) => acc + (item.subtotal || item.unitPrice * item.quantity), 0);

  return (
    <CustomerLayout>
      <div className="cart-page-container">
        <div className="cart-header">
          <h1 className="cart-title">Shopping Cart</h1>
          <p className="cart-subtitle">Review items in your cart before placing your order.</p>
        </div>

        {/* Toast */}
        {toastMessage && <div className="toast-notification">{toastMessage}</div>}

        {isEmpty ? (
          <EmptyState
            icon={FiShoppingCart}
            title="Your cart is empty."
            message="Looks like you haven't added any plants to your cart yet."
            actionText="Explore Plants"
            actionLink="/customer/plants"
          />
        ) : (
          <div className="cart-layout-grid">
            {/* Left: Cart Items List */}
            <div className="cart-items-section">
              <div className="cart-items-card">
                <div className="cart-table-wrap">
                  <table className="cart-table">
                    <thead>
                      <tr>
                        <th>Plant</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Subtotal</th>
                        <th>Remove</th>
                      </tr>
                    </thead>
                    <tbody>
                      {items.map((item) => {
                        const maxStock = item.stockQuantity || 99;
                        const isUpdating = updatingItemId === item.id;

                        return (
                          <tr key={item.id}>
                            <td>
                              <div className="cart-product-cell">
                                <img
                                  src={item.plantImageUrl || 'https://images.unsplash.com/photo-1545241047-6083a3684587?w=150&auto=format&fit=crop&q=60'}
                                  alt={item.plantName}
                                  className="cart-product-image"
                                  onError={(e) => {
                                    e.target.onerror = null;
                                    e.target.src = 'https://images.unsplash.com/photo-1545241047-6083a3684587?w=150&auto=format&fit=crop&q=60';
                                  }}
                                />
                                <div>
                                  <h4 className="cart-product-title">{item.plantName || 'Plant'}</h4>
                                  {item.stockQuantity && (
                                    <span className="cart-stock-hint">
                                      {item.stockQuantity} in stock
                                    </span>
                                  )}
                                </div>
                              </div>
                            </td>
                            <td>
                              <span className="cart-price-text">
                                ${Number(item.unitPrice || 0).toFixed(2)}
                              </span>
                            </td>
                            <td>
                              <div className="cart-qty-picker">
                                <button
                                  type="button"
                                  disabled={item.quantity <= 1 || isUpdating}
                                  onClick={() => handleUpdateQuantity(item, item.quantity - 1)}
                                >
                                  -
                                </button>
                                <span>{item.quantity}</span>
                                <button
                                  type="button"
                                  disabled={item.quantity >= maxStock || isUpdating}
                                  onClick={() => handleUpdateQuantity(item, item.quantity + 1)}
                                >
                                  +
                                </button>
                              </div>
                            </td>
                            <td>
                              <span className="cart-subtotal-text">
                                ${Number(item.subtotal || (item.unitPrice * item.quantity) || 0).toFixed(2)}
                              </span>
                            </td>
                            <td>
                              <button
                                type="button"
                                className="cart-remove-btn"
                                disabled={isUpdating}
                                onClick={() => handleRemoveItem(item.id)}
                                title="Remove Item"
                              >
                                <FiTrash2 size={16} />
                              </button>
                            </td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>

            {/* Right: Order Summary */}
            <div className="cart-summary-section">
              <div className="cart-summary-card">
                <h3 className="summary-title">Order Summary</h3>

                <div className="summary-row">
                  <span>Subtotal</span>
                  <span>${Number(subtotal).toFixed(2)}</span>
                </div>
                <div className="summary-row">
                  <span>Estimated Shipping</span>
                  <span className="free-shipping-tag">FREE</span>
                </div>

                <div className="summary-divider" />

                <div className="summary-row total">
                  <span>Total Amount</span>
                  <span>${Number(subtotal).toFixed(2)}</span>
                </div>

                <button
                  className="checkout-btn"
                  onClick={() => setIsCheckoutOpen(true)}
                >
                  Proceed to Checkout <FiArrowRight size={16} />
                </button>

                <div className="summary-perks">
                  <div className="perk-item">
                    <FiTruck size={14} /> Free delivery on all orders
                  </div>
                  <div className="perk-item">
                    <FiShield size={14} /> 100% secure payment gateway
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Checkout Modal */}
        {isCheckoutOpen && (
          <div className="checkout-modal-backdrop">
            <div className="checkout-modal-card">
              <div className="modal-header">
                <h3>Complete Your Order</h3>
                <button
                  className="modal-close-btn"
                  onClick={() => setIsCheckoutOpen(false)}
                >
                  <FiX size={18} />
                </button>
              </div>

              <form onSubmit={handleProceedToCheckout} className="checkout-form">
                <div className="form-group">
                  <label>Shipping Address *</label>
                  <textarea
                    required
                    rows={3}
                    placeholder="Enter your full street address, city, state and zip code"
                    value={shippingAddress}
                    onChange={(e) => setShippingAddress(e.target.value)}
                  />
                </div>

                <div className="form-group">
                  <label>Contact Phone Number</label>
                  <input
                    type="tel"
                    placeholder="e.g. +1 (555) 000-0000"
                    value={contactPhone}
                    onChange={(e) => setContactPhone(e.target.value)}
                  />
                </div>

                <div className="form-group">
                  <label>Order Notes / Delivery Instructions</label>
                  <input
                    type="text"
                    placeholder="e.g. Leave near the front porch"
                    value={notes}
                    onChange={(e) => setNotes(e.target.value)}
                  />
                </div>

                <div className="checkout-order-total-preview">
                  <span>Total Payment:</span>
                  <strong>${Number(subtotal).toFixed(2)}</strong>
                </div>

                <div className="modal-actions">
                  <button
                    type="button"
                    className="modal-btn cancel"
                    onClick={() => setIsCheckoutOpen(false)}
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="modal-btn submit"
                    disabled={checkingOut}
                  >
                    {checkingOut ? 'Placing Order…' : 'Place Order'}
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </CustomerLayout>
  );
};

export default CustomerCartPage;

import React from 'react';
import { FiHeart } from 'react-icons/fi';
import CustomerLayout from '../../components/customer/CustomerLayout';
import { EmptyState } from '../../components/common/UIState';

const CustomerWishlistPage = () => {
  return (
    <CustomerLayout>
      <div className="wishlist-page-container">
        <div style={{ marginBottom: '1rem' }}>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 800, color: '#111827', margin: '0 0 0.25rem 0' }}>
            My Wishlist
          </h1>
          <p style={{ fontSize: '0.95rem', color: '#4b5563', margin: 0 }}>
            Save and monitor plants you plan to add to your garden.
          </p>
        </div>

        <EmptyState
          icon={FiHeart}
          title="Wishlist Feature Currently Unavailable"
          message="The wishlist service endpoint is not currently enabled in the backend system. Please browse our plant catalog directly to add items to your cart."
          actionText="Explore Plants"
          actionLink="/customer/plants"
        />
      </div>
    </CustomerLayout>
  );
};

export default CustomerWishlistPage;

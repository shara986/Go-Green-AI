import React from 'react';
import { FiBookOpen } from 'react-icons/fi';
import CustomerLayout from '../../components/customer/CustomerLayout';
import { EmptyState } from '../../components/common/UIState';

const CustomerPlantDiaryPage = () => {
  return (
    <CustomerLayout>
      <div className="plant-diary-page-container">
        <div style={{ marginBottom: '1rem' }}>
          <h1 style={{ fontSize: '1.75rem', fontWeight: 800, color: '#111827', margin: '0 0 0.25rem 0' }}>
            Plant Care Diary
          </h1>
          <p style={{ fontSize: '0.95rem', color: '#4b5563', margin: 0 }}>
            Track watering schedules, fertilizer logs, and growth milestones for your personal plants.
          </p>
        </div>

        <EmptyState
          icon={FiBookOpen}
          title="Plant Diary Feature Currently Unavailable"
          message="The plant diary service endpoint is not currently enabled in the backend system. Check back soon for plant care logs and journal updates!"
          actionText="Explore Plants"
          actionLink="/customer/plants"
        />
      </div>
    </CustomerLayout>
  );
};

export default CustomerPlantDiaryPage;

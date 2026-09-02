import React from 'react';
import './ConfirmModal.css';

/**
 * Reusable confirmation modal.
 *
 * Usage:
 *   <ConfirmModal
 *     open={showModal}
 *     title="Delete Plant?"
 *     message="This action cannot be undone."
 *     confirmText="Delete"
 *     confirmVariant="danger"
 *     onConfirm={handleDelete}
 *     onCancel={() => setShowModal(false)}
 *   />
 */
const ConfirmModal = ({
  open,
  title = 'Are you sure?',
  message = '',
  confirmText = 'Confirm',
  cancelText = 'Cancel',
  confirmVariant = 'danger', // 'danger' | 'primary'
  onConfirm,
  onCancel,
}) => {
  if (!open) return null;

  return (
    <div className="confirm-modal-backdrop" onClick={onCancel}>
      <div
        className="confirm-modal-card"
        onClick={(e) => e.stopPropagation()}
        role="dialog"
        aria-modal="true"
        aria-labelledby="confirm-modal-title"
      >
        <h3 className="confirm-modal-title" id="confirm-modal-title">
          {title}
        </h3>
        {message && <p className="confirm-modal-message">{message}</p>}
        <div className="confirm-modal-actions">
          <button
            className="confirm-modal-btn cancel"
            onClick={onCancel}
          >
            {cancelText}
          </button>
          <button
            className={`confirm-modal-btn ${confirmVariant}`}
            onClick={onConfirm}
          >
            {confirmText}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ConfirmModal;

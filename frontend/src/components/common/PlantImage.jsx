import React, { useState } from 'react';
import './PlantImage.css';

/**
 * Reusable plant image component with graceful fallback.
 *
 * Usage:
 *   <PlantImage src={plant.imageUrl} alt={plant.name} />
 */
const PlantImage = ({
  src,
  alt = 'Plant',
  className = '',
  aspectRatio = '4/3',
  hover = true,
}) => {
  const [failed, setFailed] = useState(false);

  const showFallback = !src || failed;

  return (
    <div
      className={`plant-image-wrapper ${hover ? 'plant-image-hover' : ''} ${className}`}
      style={{ aspectRatio }}
    >
      {showFallback ? (
        <div className="plant-image-fallback" aria-label={alt}>
          <svg
            viewBox="0 0 80 80"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
            className="plant-fallback-svg"
          >
            <rect width="80" height="80" rx="16" fill="#E8F5E9" />
            <path
              d="M40 58V38"
              stroke="#2D6A4F"
              strokeWidth="2.5"
              strokeLinecap="round"
            />
            <path
              d="M40 38C40 30 34 24 26 24C26 32 32 38 40 38Z"
              fill="#52B788"
              stroke="#2D6A4F"
              strokeWidth="1.5"
            />
            <path
              d="M40 44C40 36 46 30 54 30C54 38 48 44 40 44Z"
              fill="#95D5B2"
              stroke="#2D6A4F"
              strokeWidth="1.5"
            />
            <ellipse cx="40" cy="60" rx="8" ry="3" fill="#B7E4C7" />
          </svg>
          <span className="plant-fallback-text">GoGreen AI</span>
        </div>
      ) : (
        <img
          src={src}
          alt={alt}
          className="plant-image-img"
          loading="lazy"
          onError={() => setFailed(true)}
        />
      )}
    </div>
  );
};

export default PlantImage;

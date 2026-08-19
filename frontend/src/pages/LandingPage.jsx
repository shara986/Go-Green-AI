import React from 'react';
import HeroSection from '../components/landing/HeroSection';
import TrustStrip from '../components/landing/TrustStrip';
import WhySection from '../components/landing/WhySection';
import CategorySection from '../components/landing/CategorySection';
import FeaturedPlants from '../components/landing/FeaturedPlants';
import AISection from '../components/landing/AISection';
import HowItWorks from '../components/landing/HowItWorks';
import CTASection from '../components/landing/CTASection';

const LandingPage = () => {
  return (
    <main className="landing-page">
      <HeroSection />
      <TrustStrip />
      <WhySection />
      <CategorySection />
      <FeaturedPlants />
      <AISection />
      <HowItWorks />
      <CTASection />
    </main>
  );
};

export default LandingPage;

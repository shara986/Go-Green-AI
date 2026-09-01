import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from 'react-router-dom';
import LandingPage from './pages/LandingPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import AboutPage from './pages/AboutPage';
import ContactPage from './pages/ContactPage';
import PlantsPage from './pages/PlantsPage';
import PlantDetailPage from './pages/PlantDetailPage';
import CategoriesPage from './pages/CategoriesPage';
import ProtectedRoute from './components/ProtectedRoute';
import AdminRoute from './components/guards/AdminRoute';
import CustomerRoute from './components/guards/CustomerRoute';
import NurseryRoute from './components/guards/NurseryRoute';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import { AuthProvider } from './context/AuthContext';

// Dashboard pages
import AdminDashboard from './pages/admin/AdminDashboard';
import CustomerDashboard from './pages/customer/CustomerDashboard';
import NurseryDashboard from './pages/nursery/NurseryDashboard';

// Dashboard pages have their own sidebar/topnav — hide global Navbar & Footer for them
const DASHBOARD_PATHS = [
  '/admin',
  '/customer',
  '/nursery',
];

const AppLayout = ({ children }) => {
  const location = useLocation();
  const isDashboard = DASHBOARD_PATHS.some((p) => location.pathname.startsWith(p));

  return (
    <>
      {!isDashboard && <Navbar />}
      {children}
      {!isDashboard && <Footer />}
    </>
  );
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <AppLayout>
          <Routes>
            {/* =================== PUBLIC ROUTES =================== */}
            <Route path="/" element={<LandingPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/about" element={<AboutPage />} />
            <Route path="/contact" element={<ContactPage />} />

            {/* =================== SHARED PROTECTED ROUTES (Customer + Nursery) =================== */}
            <Route
              path="/plants"
              element={
                <ProtectedRoute>
                  <PlantsPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/plants/:plantId"
              element={
                <ProtectedRoute>
                  <PlantDetailPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/categories"
              element={
                <ProtectedRoute>
                  <CategoriesPage />
                </ProtectedRoute>
              }
            />

            {/* =================== ADMIN DASHBOARD =================== */}
            <Route
              path="/admin/dashboard"
              element={
                <AdminRoute>
                  <AdminDashboard />
                </AdminRoute>
              }
            />
            {/* Catch-all admin sub-pages → redirect to dashboard for now */}
            <Route
              path="/admin/*"
              element={
                <AdminRoute>
                  <AdminDashboard />
                </AdminRoute>
              }
            />

            {/* =================== CUSTOMER DASHBOARD =================== */}
            <Route
              path="/customer/dashboard"
              element={
                <CustomerRoute>
                  <CustomerDashboard />
                </CustomerRoute>
              }
            />
            <Route
              path="/customer/*"
              element={
                <CustomerRoute>
                  <CustomerDashboard />
                </CustomerRoute>
              }
            />

            {/* =================== NURSERY DASHBOARD =================== */}
            <Route
              path="/nursery/dashboard"
              element={
                <NurseryRoute>
                  <NurseryDashboard />
                </NurseryRoute>
              }
            />
            <Route
              path="/nursery/*"
              element={
                <NurseryRoute>
                  <NurseryDashboard />
                </NurseryRoute>
              }
            />

            {/* =================== FALLBACK =================== */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </AppLayout>
      </Router>
    </AuthProvider>
  );
}

export default App;

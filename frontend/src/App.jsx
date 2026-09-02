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
import PublicOnlyRoute from './components/guards/PublicOnlyRoute';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import { AuthProvider } from './context/AuthContext';
import { ToastProvider } from './components/common/Toast';

// Dashboard pages
import AdminDashboard from './pages/admin/AdminDashboard';
// Customer pages
import CustomerDashboard from './pages/customer/CustomerDashboard';
import CustomerPlantsPage from './pages/customer/CustomerPlantsPage';
import CustomerPlantDetailPage from './pages/customer/CustomerPlantDetailPage';
import CustomerCategoriesPage from './pages/customer/CustomerCategoriesPage';
import CustomerOrdersPage from './pages/customer/CustomerOrdersPage';
import CustomerOrderDetailPage from './pages/customer/CustomerOrderDetailPage';
import CustomerCartPage from './pages/customer/CustomerCartPage';
import CustomerWishlistPage from './pages/customer/CustomerWishlistPage';
import CustomerPlantDiaryPage from './pages/customer/CustomerPlantDiaryPage';
import CustomerProfilePage from './pages/customer/CustomerProfilePage';
import NurseryLayout from './components/nursery/NurseryLayout';
import NurseryDashboardOverview from './pages/nursery/NurseryDashboardOverview';
import NurseryProfilePage from './pages/nursery/NurseryProfilePage';
import NurseryPlantsPage from './pages/nursery/NurseryPlantsPage';
import NurseryInventoryPage from './pages/nursery/NurseryInventoryPage';
import NurseryOrdersPage from './pages/nursery/NurseryOrdersPage';
import NurseryOrderDetailPage from './pages/nursery/NurseryOrderDetailPage';
import NurseryCustomersPage from './pages/nursery/NurseryCustomersPage';
import NurseryReportsPage from './pages/nursery/NurseryReportsPage';
import NurseryUserSettingsPage from './pages/nursery/NurseryUserSettingsPage';
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
      <ToastProvider>
      <Router>
        <AppLayout>
          <Routes>
            {/* =================== PUBLIC ROUTES =================== */}
            <Route path="/" element={<LandingPage />} />
            <Route 
              path="/login" 
              element={
                <PublicOnlyRoute>
                  <LoginPage />
                </PublicOnlyRoute>
              } 
            />
            <Route 
              path="/register" 
              element={
                <PublicOnlyRoute>
                  <RegisterPage />
                </PublicOnlyRoute>
              } 
            />
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

            {/* =================== CUSTOMER DASHBOARD & SUB-PAGES =================== */}
            <Route
              path="/customer/dashboard"
              element={
                <CustomerRoute>
                  <CustomerDashboard />
                </CustomerRoute>
              }
            />
            <Route
              path="/customer/plants"
              element={
                <CustomerRoute>
                  <CustomerPlantsPage />
                </CustomerRoute>
              }
            />
            <Route
              path="/customer/plants/:plantId"
              element={
                <CustomerRoute>
                  <CustomerPlantDetailPage />
                </CustomerRoute>
              }
            />
            <Route
              path="/customer/categories"
              element={
                <CustomerRoute>
                  <CustomerCategoriesPage />
                </CustomerRoute>
              }
            />
            <Route
              path="/customer/orders"
              element={
                <CustomerRoute>
                  <CustomerOrdersPage />
                </CustomerRoute>
              }
            />
            <Route
              path="/customer/orders/:orderId"
              element={
                <CustomerRoute>
                  <CustomerOrderDetailPage />
                </CustomerRoute>
              }
            />
            <Route
              path="/customer/cart"
              element={
                <CustomerRoute>
                  <CustomerCartPage />
                </CustomerRoute>
              }
            />
            <Route
              path="/customer/wishlist"
              element={
                <CustomerRoute>
                  <CustomerWishlistPage />
                </CustomerRoute>
              }
            />
            <Route
              path="/customer/plant-diary"
              element={
                <CustomerRoute>
                  <CustomerPlantDiaryPage />
                </CustomerRoute>
              }
            />
            <Route
              path="/customer/profile"
              element={
                <CustomerRoute>
                  <CustomerProfilePage />
                </CustomerRoute>
              }
            />
            <Route
              path="/customer/*"
              element={
                <CustomerRoute>
                  <Navigate to="/customer/dashboard" replace />
                </CustomerRoute>
              }
            />

            {/* =================== NURSERY DASHBOARD =================== */}
            <Route
              path="/nursery"
              element={
                <NurseryRoute>
                  <NurseryLayout />
                </NurseryRoute>
              }
            >
              <Route path="dashboard" element={<NurseryDashboardOverview />} />
              <Route path="my-nursery" element={<NurseryProfilePage />} />
              <Route path="plants" element={<NurseryPlantsPage />} />
              <Route path="inventory" element={<NurseryInventoryPage />} />
              <Route path="orders" element={<NurseryOrdersPage />} />
              <Route path="orders/:orderId" element={<NurseryOrderDetailPage />} />
              <Route path="customers" element={<NurseryCustomersPage />} />
              <Route path="reports" element={<NurseryReportsPage />} />
              <Route path="profile" element={<NurseryUserSettingsPage />} />
              <Route path="" element={<Navigate to="dashboard" replace />} />
              <Route path="*" element={<Navigate to="dashboard" replace />} />
            </Route>

            {/* =================== FALLBACK =================== */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </AppLayout>
      </Router>
      </ToastProvider>
    </AuthProvider>
  );
}

export default App;

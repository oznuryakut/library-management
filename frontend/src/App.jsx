import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Navbar from './components/Navbar';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import BooksPage from './pages/BooksPage';
import AdminPage from './pages/AdminPage';
import MyBorrowsPage from './pages/MyBorrowsPage';
import DashboardPage from './pages/DashboardPage';
import BookDetailPage from './pages/BookDetailPage';
import ForgotPasswordPage from './pages/ForgotPasswordPage';
import ResetPasswordPage from './pages/ResetPasswordPage';

function PrivateRoute({ children, adminOnly }) {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" />;
  if (adminOnly && user.role !== 'ROLE_ADMIN') return <Navigate to="/books" />;
  return children;
}

function AppRoutes() {
  return (
    <>
      <Navbar />
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/forgot-password" element={<ForgotPasswordPage />} />
        <Route path="/reset-password" element={<ResetPasswordPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/books" element={<BooksPage />} />
        <Route path="/books/:id" element={<BookDetailPage />} />
        <Route path="/my-borrows" element={
          <PrivateRoute>
            <MyBorrowsPage />
          </PrivateRoute>
        } />
        <Route path="/dashboard" element={
          <PrivateRoute adminOnly>
            <DashboardPage />
          </PrivateRoute>
        } />
        <Route path="/admin" element={
          <PrivateRoute adminOnly>
            <AdminPage />
          </PrivateRoute>
        } />
        <Route path="*" element={<Navigate to="/books" />} />
      </Routes>
    </>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <AppRoutes />
      </BrowserRouter>
    </AuthProvider>
  );
}
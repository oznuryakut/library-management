import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { user, logoutUser } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logoutUser();
    navigate('/login');
  };

  return (
    <nav style={{
      background: '#1e293b', padding: '0 24px',
      display: 'flex', alignItems: 'center',
      justifyContent: 'space-between', height: 56
    }}>
      <Link to="/books" style={{ color: '#fff', fontWeight: 700, fontSize: 18, textDecoration: 'none' }}>
        LibraryApp
      </Link>
      <div style={{ display: 'flex', gap: 16, alignItems: 'center' }}>
        <Link to="/books" style={{ color: '#94a3b8', textDecoration: 'none' }}>Kitaplar</Link>
        {user && (
  <Link to="/my-borrows" style={{ color: '#94a3b8', textDecoration: 'none' }}>Ödünçlerim</Link>
)}
        {user?.role === 'ROLE_ADMIN' && (
          <Link to="/admin" style={{ color: '#94a3b8', textDecoration: 'none' }}>Admin</Link>

        )}
        {user?.role === 'ROLE_ADMIN' && (
  <Link to="/dashboard" style={{ color: '#94a3b8', textDecoration: 'none' }}>Dashboard</Link>
)}
        {user ? (
          <>
            <span style={{ color: '#64748b', fontSize: 13 }}>{user.username}</span>
            <button onClick={handleLogout} style={{
              background: '#ef4444', color: '#fff', border: 'none',
              borderRadius: 6, padding: '6px 14px', cursor: 'pointer'
            }}>Çıkış</button>
          </>
        ) : (
          <Link to="/login" style={{ color: '#94a3b8', textDecoration: 'none' }}>Giriş</Link>
        )}
      </div>
    </nav>
  );
}
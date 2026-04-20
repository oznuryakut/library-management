import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { login } from '../api/authApi';
import { useAuth } from '../context/AuthContext';

export default function LoginPage() {
  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const { loginUser } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await login(form);
      loginUser(res.data);
      navigate('/books');
    } catch {
      setError('Kullanıcı adı veya şifre hatalı');
    }
  };

  return (
    <div style={{ minHeight: '100vh', background: '#0f172a', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
      <div style={{ background: '#1e293b', padding: 32, borderRadius: 16, width: 360 }}>
        <h2 style={{ color: '#f1f5f9', marginBottom: 24, textAlign: 'center' }}>Giriş Yap</h2>
        {error && <p style={{ color: '#ef4444', marginBottom: 16, fontSize: 14 }}>{error}</p>}
        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <input
            placeholder="Kullanıcı adı"
            value={form.username}
            onChange={e => setForm({ ...form, username: e.target.value })}
            style={{ padding: '10px 14px', borderRadius: 8, border: '1px solid #334155', background: '#0f172a', color: '#f1f5f9' }}
          />
          <input
            type="password"
            placeholder="Şifre"
            value={form.password}
            onChange={e => setForm({ ...form, password: e.target.value })}
            style={{ padding: '10px 14px', borderRadius: 8, border: '1px solid #334155', background: '#0f172a', color: '#f1f5f9' }}
          />
          <p style={{ color: '#64748b', textAlign: 'right', fontSize: 13, margin: 0 }}>
           <Link to="/forgot-password" style={{ color: '#6366f1' }}>Şifremi unuttum</Link>
           </p>
          <button type="submit" style={{
            background: '#6366f1', color: '#fff', border: 'none',
            borderRadius: 8, padding: '12px 0', cursor: 'pointer', fontWeight: 600
          }}>Giriş</button>
        </form>
        <p style={{ color: '#64748b', textAlign: 'center', marginTop: 16, fontSize: 14 }}>
          Hesabın yok mu? <Link to="/register" style={{ color: '#6366f1' }}>Kayıt ol</Link>
        </p>
      </div>
    </div>
  );
}
import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';

export default function ResetPasswordPage() {
  const [form, setForm] = useState({ email: '', code: '', newPassword: '' });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
     await axios.post('http://localhost:8080/api/password/reset-password', form);
      setSuccess(true);
      setTimeout(() => navigate('/login'), 2000);
    } catch (err) {
      setError(err.response?.data?.error || 'Hata oluştu');
    }
  };

  return (
    <div style={{ minHeight: '100vh', background: '#0f172a', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
      <div style={{ background: '#1e293b', padding: 32, borderRadius: 16, width: 360 }}>
        <h2 style={{ color: '#f1f5f9', marginBottom: 24, textAlign: 'center' }}>Şifremi Sıfırla</h2>

        {success ? (
          <p style={{ color: '#22c55e', textAlign: 'center' }}>Şifreniz güncellendi! Yönlendiriliyorsunuz...</p>
        ) : (
          <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
            {error && <p style={{ color: '#ef4444', fontSize: 14 }}>{error}</p>}
            <input
              type="email"
              placeholder="Email adresiniz"
              value={form.email}
              onChange={e => setForm({ ...form, email: e.target.value })}
              style={{ padding: '10px 14px', borderRadius: 8, border: '1px solid #334155', background: '#0f172a', color: '#f1f5f9' }}
            />
            <input
              placeholder="6 haneli kod"
              value={form.code}
              onChange={e => setForm({ ...form, code: e.target.value })}
              style={{ padding: '10px 14px', borderRadius: 8, border: '1px solid #334155', background: '#0f172a', color: '#f1f5f9' }}
            />
            <input
              type="password"
              placeholder="Yeni şifre"
              value={form.newPassword}
              onChange={e => setForm({ ...form, newPassword: e.target.value })}
              style={{ padding: '10px 14px', borderRadius: 8, border: '1px solid #334155', background: '#0f172a', color: '#f1f5f9' }}
            />
            <button type="submit" style={{
              background: '#6366f1', color: '#fff', border: 'none',
              borderRadius: 8, padding: '12px 0', cursor: 'pointer', fontWeight: 600
            }}>Şifremi Güncelle</button>
          </form>
        )}

        <p style={{ color: '#64748b', textAlign: 'center', marginTop: 16, fontSize: 14 }}>
          <Link to="/login" style={{ color: '#6366f1' }}>Giriş sayfasına dön</Link>
        </p>
      </div>
    </div>
  );
}
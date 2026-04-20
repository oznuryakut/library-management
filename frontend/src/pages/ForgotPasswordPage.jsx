import { useState } from 'react';
import { Link } from 'react-router-dom';

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState('');
  const [sent, setSent] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const res = await fetch('http://localhost:8080/api/password/forgot-password', {
        method: 'POST',
        headers: { 
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        body: JSON.stringify({ email }),
        credentials: 'omit'
      });
      if (res.ok) {
        setSent(true);
      } else {
        const data = await res.json().catch(() => ({}));
        setError(data.error || 'Hata oluştu: ' + res.status);
      }
    } catch (err) {
      setError('Bağlantı hatası: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ minHeight: '100vh', background: '#0f172a', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
      <div style={{ background: '#1e293b', padding: 32, borderRadius: 16, width: 360 }}>
        <h2 style={{ color: '#f1f5f9', marginBottom: 8, textAlign: 'center' }}>Şifremi Unuttum</h2>
        <p style={{ color: '#64748b', textAlign: 'center', marginBottom: 24, fontSize: 14 }}>
          Email adresinize doğrulama kodu göndereceğiz
        </p>

        {sent ? (
          <div style={{ textAlign: 'center' }}>
            <p style={{ color: '#22c55e', marginBottom: 16 }}>Kod gönderildi! Email'inizi kontrol edin.</p>
            <Link to="/reset-password" style={{
              background: '#6366f1', color: '#fff', padding: '12px 24px',
              borderRadius: 8, textDecoration: 'none', display: 'inline-block'
            }}>Kodu Gir</Link>
          </div>
        ) : (
          <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
            {error && <p style={{ color: '#ef4444', fontSize: 14 }}>{error}</p>}
            <input
              type="email"
              placeholder="Email adresiniz"
              value={email}
              onChange={e => setEmail(e.target.value)}
              style={{ padding: '10px 14px', borderRadius: 8, border: '1px solid #334155', background: '#0f172a', color: '#f1f5f9' }}
            />
            <button type="submit" disabled={loading} style={{
              background: '#6366f1', color: '#fff', border: 'none',
              borderRadius: 8, padding: '12px 0', cursor: 'pointer', fontWeight: 600
            }}>{loading ? 'Gönderiliyor...' : 'Kod Gönder'}</button>
          </form>
        )}

        <p style={{ color: '#64748b', textAlign: 'center', marginTop: 16, fontSize: 14 }}>
          <Link to="/login" style={{ color: '#6366f1' }}>Giriş sayfasına dön</Link>
        </p>
      </div>
    </div>
  );
}
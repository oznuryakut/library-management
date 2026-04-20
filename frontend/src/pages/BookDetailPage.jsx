import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getBook } from '../api/bookApi';
import { borrowBook } from '../api/borrowApi';
import { getReviews, getRating, addReview, deleteReview } from '../api/reviewApi';
import { useAuth } from '../context/AuthContext';

export default function BookDetailPage() {
  const { id } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [book, setBook] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [avgRating, setAvgRating] = useState(0);
  const [form, setForm] = useState({ rating: 5, comment: '' });
  const [msg, setMsg] = useState('');
  const [borrowMsg, setBorrowMsg] = useState('');

  useEffect(() => {
    getBook(id).then(res => setBook(res.data));
    fetchReviews();
  }, [id]);

  const fetchReviews = async () => {
    const [reviewRes, ratingRes] = await Promise.all([
      getReviews(id), getRating(id)
    ]);
    setReviews(reviewRes.data);
    setAvgRating(ratingRes.data.rating);
  };

  const handleBorrow = async () => {
    try {
      await borrowBook(id);
      setBorrowMsg('Kitap ödünç alındı!');
      getBook(id).then(res => setBook(res.data));
    } catch (err) {
      setBorrowMsg(err.response?.data?.error || 'Hata oluştu');
    }
  };

  const handleReview = async (e) => {
    e.preventDefault();
    try {
      await addReview(id, form);
      setMsg('Yorum eklendi!');
      setForm({ rating: 5, comment: '' });
      fetchReviews();
    } catch (err) {
      setMsg(err.response?.data?.error || 'Hata oluştu');
    }
  };

  const handleDelete = async (reviewId) => {
    try {
      await deleteReview(id, reviewId);
      fetchReviews();
    } catch (err) {
      setMsg(err.response?.data?.error || 'Silinemedi');
    }
  };

  const renderStars = (rating) => '★'.repeat(rating) + '☆'.repeat(5 - rating);

  if (!book) return <div style={{ color: '#94a3b8', padding: 24 }}>Yükleniyor...</div>;

  return (
    <div style={{ minHeight: '100vh', background: '#0f172a', padding: 24 }}>
      <div style={{ maxWidth: 800, margin: '0 auto' }}>

        <button onClick={() => navigate('/books')} style={{
          background: 'none', border: 'none', color: '#6366f1',
          cursor: 'pointer', marginBottom: 16, fontSize: 14
        }}>← Geri</button>

        <div style={{ background: '#1e293b', borderRadius: 16, padding: 32, marginBottom: 24 }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
            <div>
              <h1 style={{ color: '#f1f5f9', margin: '0 0 8px', fontSize: 24 }}>{book.title}</h1>
              <p style={{ color: '#94a3b8', margin: '0 0 8px', fontSize: 16 }}>{book.author}</p>
              {book.category && (
                <span style={{
                  background: '#6366f120', color: '#6366f1', fontSize: 12,
                  padding: '4px 12px', borderRadius: 20, fontWeight: 700
                }}>{book.category}</span>
              )}
            </div>
            <div style={{ textAlign: 'right' }}>
              <div style={{ color: '#f59e0b', fontSize: 24, marginBottom: 4 }}>
                {renderStars(Math.round(avgRating))}
              </div>
              <p style={{ color: '#64748b', fontSize: 13, margin: 0 }}>
                {avgRating > 0 ? `${avgRating} / 5` : 'Henüz puan yok'}
              </p>
            </div>
          </div>

          <div style={{ display: 'flex', gap: 24, marginTop: 20, paddingTop: 20, borderTop: '1px solid #334155' }}>
            <div>
              <p style={{ color: '#64748b', fontSize: 12, margin: '0 0 4px' }}>ISBN</p>
              <p style={{ color: '#f1f5f9', fontSize: 14, margin: 0 }}>{book.isbn}</p>
            </div>
            {book.price && (
              <div>
                <p style={{ color: '#64748b', fontSize: 12, margin: '0 0 4px' }}>Fiyat</p>
                <p style={{ color: '#f1f5f9', fontSize: 14, margin: 0 }}>₺{book.price}</p>
              </div>
            )}
            <div>
              <p style={{ color: '#64748b', fontSize: 12, margin: '0 0 4px' }}>Stok</p>
              <p style={{ color: book.stock === 0 ? '#ef4444' : '#22c55e', fontSize: 14, margin: 0 }}>
                {book.stock === 0 ? 'Stok yok' : `${book.stock} adet`}
              </p>
            </div>
          </div>

          {user && (
            <div style={{ marginTop: 20 }}>
              {borrowMsg && (
                <p style={{ color: borrowMsg.includes('alındı') ? '#22c55e' : '#ef4444', fontSize: 14, marginBottom: 8 }}>
                  {borrowMsg}
                </p>
              )}
              {book.stock > 0 && (
                <button onClick={handleBorrow} style={{
                  background: '#6366f1', color: '#fff', border: 'none',
                  borderRadius: 8, padding: '12px 24px', cursor: 'pointer', fontWeight: 600
                }}>Ödünç Al</button>
              )}
            </div>
          )}
        </div>

        <div style={{ background: '#1e293b', borderRadius: 16, padding: 24, marginBottom: 24 }}>
          <h3 style={{ color: '#f1f5f9', marginBottom: 16 }}>Yorumlar ({reviews.length})</h3>

          {reviews.length === 0 && (
            <p style={{ color: '#64748b' }}>Henüz yorum yok. İlk yorumu sen yap!</p>
          )}

          <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
            {reviews.map(r => (
              <div key={r.id} style={{
                background: '#0f172a', borderRadius: 10, padding: 16,
                display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start'
              }}>
                <div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 6 }}>
                    <span style={{ color: '#6366f1', fontWeight: 600, fontSize: 14 }}>{r.username}</span>
                    <span style={{ color: '#f59e0b' }}>{renderStars(r.rating)}</span>
                  </div>
                  <p style={{ color: '#94a3b8', margin: 0, fontSize: 14 }}>{r.comment}</p>
                </div>
                {user?.username === r.username && (
                  <button onClick={() => handleDelete(r.id)} style={{
                    background: 'none', border: 'none', color: '#ef4444',
                    cursor: 'pointer', fontSize: 18
                  }}>×</button>
                )}
              </div>
            ))}
          </div>
        </div>

        {user && (
          <div style={{ background: '#1e293b', borderRadius: 16, padding: 24 }}>
            <h3 style={{ color: '#f1f5f9', marginBottom: 16 }}>Yorum Yap</h3>
            {msg && (
              <p style={{ color: msg.includes('eklendi') ? '#22c55e' : '#ef4444', marginBottom: 12 }}>{msg}</p>
            )}
            <form onSubmit={handleReview} style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
              <div style={{ display: 'flex', gap: 8 }}>
                {[1, 2, 3, 4, 5].map(star => (
                  <button key={star} type="button" onClick={() => setForm({ ...form, rating: star })}
                    style={{
                      background: 'none', border: 'none', cursor: 'pointer',
                      fontSize: 28, color: star <= form.rating ? '#f59e0b' : '#334155'
                    }}>★</button>
                ))}
              </div>
              <textarea
                placeholder="Yorumunuzu yazın..."
                value={form.comment}
                onChange={e => setForm({ ...form, comment: e.target.value })}
                rows={3}
                style={{
                  padding: '10px 14px', borderRadius: 8, border: '1px solid #334155',
                  background: '#0f172a', color: '#f1f5f9', resize: 'vertical'
                }}
              />
              <button type="submit" style={{
                background: '#6366f1', color: '#fff', border: 'none',
                borderRadius: 8, padding: '12px 0', cursor: 'pointer', fontWeight: 600
              }}>Gönder</button>
            </form>
          </div>
        )}
      </div>
    </div>
  );
}
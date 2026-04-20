import { useEffect, useState } from 'react';
import { getMyBorrows, returnBook } from '../api/borrowApi';

export default function MyBorrowsPage() {
  const [borrows, setBorrows] = useState([]);
  const [error, setError] = useState('');

  const fetchBorrows = async () => {
    try {
      const res = await getMyBorrows();
      setBorrows(res.data);
    } catch {
      setError('Ödünçler yüklenemedi');
    }
  };

  useEffect(() => { fetchBorrows(); }, []);

  const handleReturn = async (borrowId) => {
    try {
      await returnBook(borrowId);
      fetchBorrows();
    } catch (err) {
      setError(err.response?.data?.error || 'İade başarısız');
    }
  };

  return (
    <div style={{ minHeight: '100vh', background: '#0f172a', padding: 24 }}>
      <div style={{ maxWidth: 800, margin: '0 auto' }}>
        <h2 style={{ color: '#f1f5f9', marginBottom: 24 }}>Ödünç Aldıklarım</h2>
        {error && <p style={{ color: '#ef4444' }}>{error}</p>}
        {borrows.length === 0 && (
          <p style={{ color: '#64748b' }}>Henüz ödünç aldığın kitap yok.</p>
        )}
        <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
          {borrows.map(b => (
            <div key={b.id} style={{
              background: '#1e293b', borderRadius: 12, padding: 20,
              display: 'flex', justifyContent: 'space-between', alignItems: 'center',
              borderLeft: `4px solid ${b.overdue ? '#ef4444' : b.status === 'RETURNED' ? '#22c55e' : '#6366f1'}`
            }}>
              <div>
                <h3 style={{ color: '#f1f5f9', margin: '0 0 4px' }}>{b.bookTitle}</h3>
                <p style={{ color: '#94a3b8', margin: '0 0 4px', fontSize: 13 }}>{b.bookAuthor}</p>
                <p style={{ color: '#64748b', margin: 0, fontSize: 12 }}>
                  Alındı: {b.borrowDate} · Son teslim: {b.dueDate}
                  {b.returnDate && ` · İade: ${b.returnDate}`}
                </p>
                {b.overdue && (
                  <p style={{ color: '#ef4444', margin: '4px 0 0', fontSize: 12, fontWeight: 600 }}>
                    {b.penaltyDays} gün gecikme!
                  </p>
                )}
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: 8 }}>
                <span style={{
                  fontSize: 11, fontWeight: 700, padding: '3px 10px', borderRadius: 20,
                  background: b.overdue ? '#7f1d1d' : b.status === 'RETURNED' ? '#14532d' : '#1e1b4b',
                  color: b.overdue ? '#fca5a5' : b.status === 'RETURNED' ? '#86efac' : '#a5b4fc'
                }}>
                  {b.overdue ? 'GECİKMİŞ' : b.status === 'RETURNED' ? 'İADE EDİLDİ' : 'ÖDÜNÇTE'}
                </span>
                {b.status === 'BORROWED' && (
                  <button onClick={() => handleReturn(b.id)} style={{
                    background: '#6366f1', color: '#fff', border: 'none',
                    borderRadius: 6, padding: '6px 14px', cursor: 'pointer', fontSize: 13
                  }}>İade Et</button>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
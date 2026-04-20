
import { useAuth } from '../context/AuthContext';
import { borrowBook } from '../api/borrowApi';
import { useState } from 'react';

const catColors = {
  Programming: '#6366f1', Fiction: '#ec4899',
  Science: '#14b8a6', History: '#f59e0b', Default: '#64748b'
};

export default function BookCard({ book, isAdmin, onEdit, onDelete, onBorrowed, onClick }) {
  const { user } = useAuth();  const catColor = catColors[book.category] || catColors.Default;
  const [msg, setMsg] = useState('');

const handleBorrow = async (e) => {
  e.stopPropagation();
  try {
    await borrowBook(book.id);
    setMsg('Ödünç alındı!');
    if (onBorrowed) onBorrowed();
  } catch (err) {
    setMsg(err.response?.data?.error || 'Hata oluştu');
  }
};

 return (
   <div onClick={() => () => onClick && onClick() } style={{
  background: '#1e293b', borderRadius: 12, padding: 20,
  display: 'flex', flexDirection: 'column', gap: 8,
  cursor: 'pointer'
}}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div style={{ flex: 1, minWidth: 0 }}>
          <h3 style={{ margin: '0 0 4px', fontSize: 15, color: '#f1f5f9' }}>{book.title}</h3>
          <p style={{ margin: 0, fontSize: 13, color: '#94a3b8' }}>{book.author}</p>
        </div>
        {book.category && (
          <span style={{
            fontSize: 11, fontWeight: 700, padding: '3px 8px',
            background: `${catColor}20`, color: catColor,
            borderRadius: 20, whiteSpace: 'nowrap', flexShrink: 0
          }}>{book.category}</span>
        )}
      </div>
      <div style={{ display: 'flex', gap: 8, fontSize: 11, color: '#475569' }}>
        <span>{book.isbn}</span>
        {book.price != null && <span>₺{book.price}</span>}
        {book.stock != null && (
          <span style={{ color: book.stock === 0 ? '#ef4444' : '#475569' }}>
            Stok: {book.stock}
          </span>
        )}
      </div>
      {msg && <p style={{ fontSize: 12, color: msg.includes('alındı') ? '#22c55e' : '#ef4444', margin: 0 }}>{msg}</p>}
      {isAdmin ? (
        <div style={{ display: 'flex', gap: 8, marginTop: 4 }}>
          <button onClick={() => onEdit(book)} style={{
            flex: 1, background: '#334155', color: '#e2e8f0',
            border: 'none', borderRadius: 6, padding: '6px 0', cursor: 'pointer'
          }}>Düzenle</button>
          <button onClick={() => onDelete(book.id)} style={{
            flex: 1, background: '#7f1d1d', color: '#fca5a5',
            border: 'none', borderRadius: 6, padding: '6px 0', cursor: 'pointer'
          }}>Sil</button>
        </div>
      ) : user && book.stock > 0 && (
        <button onClick={handleBorrow} style={{
          background: '#6366f1', color: '#fff', border: 'none',
          borderRadius: 6, padding: '8px 0', cursor: 'pointer', marginTop: 4
        }}>Ödünç Al</button>
      )}
    </div>
  );
}
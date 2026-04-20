import { useEffect, useState } from 'react';
import { getBooks, createBook, updateBook, deleteBook } from '../api/bookApi';
import BookCard from '../components/BookCard';

const empty = { title: '', author: '', isbn: '', category: '', price: '', stock: '' };

export default function AdminPage() {
  const [books, setBooks] = useState([]);
  const [form, setForm] = useState(empty);
  const [editId, setEditId] = useState(null);
  const [error, setError] = useState('');

  const fetchBooks = async () => {
    const res = await getBooks('', 0);
    setBooks(res.data.content);
  };

  useEffect(() => { fetchBooks(); }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = { ...form, price: parseFloat(form.price), stock: parseInt(form.stock) };
      if (editId) { await updateBook(editId, data); }
      else { await createBook(data); }
      setForm(empty); setEditId(null); fetchBooks();
    } catch (err) {
      setError(err.response?.data?.error || 'Hata oluştu');
    }
  };

  const handleEdit = (book) => {
    setEditId(book.id);
    setForm({ title: book.title, author: book.author, isbn: book.isbn,
      category: book.category || '', price: book.price || '', stock: book.stock || '' });
  };

  const handleDelete = async (id) => {
    if (window.confirm('Silmek istediğine emin misin?')) {
      await deleteBook(id); fetchBooks();
    }
  };

  const inputStyle = { padding: '10px 14px', borderRadius: 8, border: '1px solid #334155', background: '#0f172a', color: '#f1f5f9' };

  return (
    <div style={{ minHeight: '100vh', background: '#0f172a', padding: 24 }}>
      <div style={{ maxWidth: 900, margin: '0 auto' }}>
        <h2 style={{ color: '#f1f5f9', marginBottom: 24 }}>Admin Panel</h2>
        <div style={{ background: '#1e293b', borderRadius: 12, padding: 24, marginBottom: 32 }}>
          <h3 style={{ color: '#94a3b8', marginBottom: 16 }}>{editId ? 'Kitabı Düzenle' : 'Yeni Kitap Ekle'}</h3>
          {error && <p style={{ color: '#ef4444', marginBottom: 12 }}>{error}</p>}
          <form onSubmit={handleSubmit} style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
            <input placeholder="Başlık" value={form.title} onChange={e => setForm({ ...form, title: e.target.value })} style={inputStyle} />
            <input placeholder="Yazar" value={form.author} onChange={e => setForm({ ...form, author: e.target.value })} style={inputStyle} />
            <input placeholder="ISBN" value={form.isbn} onChange={e => setForm({ ...form, isbn: e.target.value })} style={inputStyle} />
            <input placeholder="Kategori" value={form.category} onChange={e => setForm({ ...form, category: e.target.value })} style={inputStyle} />
            <input placeholder="Fiyat" type="number" value={form.price} onChange={e => setForm({ ...form, price: e.target.value })} style={inputStyle} />
            <input placeholder="Stok" type="number" value={form.stock} onChange={e => setForm({ ...form, stock: e.target.value })} style={inputStyle} />
            <button type="submit" style={{
              gridColumn: '1 / -1', background: '#6366f1', color: '#fff',
              border: 'none', borderRadius: 8, padding: '12px 0', cursor: 'pointer', fontWeight: 600
            }}>{editId ? 'Güncelle' : 'Ekle'}</button>
            {editId && (
              <button type="button" onClick={() => { setEditId(null); setForm(empty); }} style={{
                gridColumn: '1 / -1', background: '#334155', color: '#e2e8f0',
                border: 'none', borderRadius: 8, padding: '10px 0', cursor: 'pointer'
              }}>İptal</button>
            )}
          </form>
        </div>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))', gap: 16 }}>
          {books.map(book => <BookCard key={book.id} book={book} isAdmin={true} onEdit={handleEdit} onDelete={handleDelete} />)}
        </div>
      </div>
    </div>
  );
}
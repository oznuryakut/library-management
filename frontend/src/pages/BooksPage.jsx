import { useEffect, useState } from 'react';
import { getBooks } from '../api/bookApi';
import BookCard from '../components/BookCard';
import RecommendationSection from '../components/RecommendationSection';

export default function BooksPage() {
  const [books, setBooks] = useState([]);
  const [search, setSearch] = useState('');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const fetchBooks = async () => {
    try {
      const res = await getBooks(search, page);
      setBooks(res.data.content);
      setTotalPages(res.data.totalPages);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => { fetchBooks(); }, [page]);

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(0);
    fetchBooks();
  };

  return (
    <div style={{ minHeight: '100vh', background: '#0f172a', padding: 24 }}>
      <div style={{ maxWidth: 900, margin: '0 auto' }}>
        <form onSubmit={handleSearch} style={{ display: 'flex', gap: 12, marginBottom: 24 }}>
          <input placeholder="Kitap veya yazar ara..." value={search}
            onChange={e => setSearch(e.target.value)}
            style={{ flex: 1, padding: '10px 16px', borderRadius: 8, border: '1px solid #334155', background: '#1e293b', color: '#f1f5f9' }} />
          <button type="submit" style={{
            background: '#6366f1', color: '#fff', border: 'none',
            borderRadius: 8, padding: '10px 20px', cursor: 'pointer'
          }}>Ara</button>
        </form>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))', gap: 16 }}>
          {books.map(book => (
            <a key={book.id} href={`/books/${book.id}`} style={{ textDecoration: 'none' }}>
              <BookCard book={book} isAdmin={false} />
            </a>
          ))}
        </div>
        {totalPages > 1 && (
          <div style={{ display: 'flex', justifyContent: 'center', gap: 8, marginTop: 24 }}>
            {Array.from({ length: totalPages }, (_, i) => (
              <button key={i} onClick={() => setPage(i)} style={{
                background: page === i ? '#6366f1' : '#1e293b',
                color: '#fff', border: 'none', borderRadius: 6,
                padding: '6px 14px', cursor: 'pointer'
              }}>{i + 1}</button>
            ))}
          </div>
        )}
        <RecommendationSection />
      </div>
    </div>
  );
}
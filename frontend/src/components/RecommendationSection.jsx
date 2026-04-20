import { useEffect, useState } from 'react';
import { getRecommendations } from '../api/recommendationApi';

export default function RecommendationSection() {
  const [books, setBooks] = useState([]);

  useEffect(() => {
    getRecommendations()
      .then(res => setBooks(res.data))
      .catch(() => {});
  }, []);

  if (books.length === 0) return null;

  return (
    <div style={{ marginTop: 40 }}>
      <h3 style={{ color: '#94a3b8', marginBottom: 16, fontSize: 14, textTransform: 'uppercase', letterSpacing: 1 }}>
        Sana Özel Öneriler
      </h3>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 12 }}>
        {books.map(book => (
          <a key={book.id} href={`/books/${book.id}`} style={{ textDecoration: 'none' }}>
            <div style={{
              background: '#1e293b', borderRadius: 10, padding: 16,
              borderLeft: '3px solid #6366f1', cursor: 'pointer'
            }}>
              <h4 style={{ color: '#f1f5f9', margin: '0 0 4px', fontSize: 13 }}>{book.title}</h4>
              <p style={{ color: '#64748b', margin: 0, fontSize: 12 }}>{book.author}</p>
              {book.category && (
                <span style={{ color: '#6366f1', fontSize: 11, marginTop: 6, display: 'block' }}>
                  {book.category}
                </span>
              )}
            </div>
          </a>
        ))}
      </div>
    </div>
  );
}

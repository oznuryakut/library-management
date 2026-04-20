import { useEffect, useState } from 'react';
import { Bar, Pie, Line } from 'react-chartjs-2';
import {
  Chart as ChartJS, CategoryScale, LinearScale, BarElement,
  Title, Tooltip, Legend, ArcElement, PointElement, LineElement, Filler
} from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement, PointElement, LineElement, Filler);
import axios from 'axios';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement, PointElement, LineElement);

const getHeaders = () => ({
  headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
});

export default function DashboardPage() {

  const [data, setData] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    axios.get('http://localhost:8080/api/admin/dashboard', getHeaders())
      .then(res => setData(res.data))
      .catch(() => setError('Dashboard yüklenemedi'));
  }, []);

  if (error) return <div style={{ color: '#ef4444', padding: 24 }}>{error}</div>;
  if (!data) return <div style={{ color: '#94a3b8', padding: 24 }}>Yükleniyor...</div>;

  const statCards = [
    { label: 'Toplam Kitap', value: data.totalBooks, color: '#6366f1' },
    { label: 'Toplam Kullanıcı', value: data.totalUsers, color: '#14b8a6' },
    { label: 'Aktif Ödünç', value: data.activeBorrows, color: '#f59e0b' },
    { label: 'Geciken', value: data.overdueBorrows, color: '#ef4444' },
    { label: 'Toplam Ödünç', value: data.totalBorrows, color: '#8b5cf6' },
  ];

  const barData = {
    labels: data.topBooks.map(b => b.title.length > 20 ? b.title.substring(0, 20) + '...' : b.title),
    datasets: [{
      label: 'Ödünç Sayısı',
      data: data.topBooks.map(b => b.count),
      backgroundColor: '#6366f1',
      borderRadius: 6,
    }]
  };

  const pieData = {
    labels: data.categoryStats.map(c => c.category),
    datasets: [{
      data: data.categoryStats.map(c => c.count),
      backgroundColor: ['#6366f1', '#14b8a6', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899'],
    }]
  };

  const lineData = {
    labels: data.monthlyBorrows.map(m => m.month),
    datasets: [{
      label: 'Aylık Ödünç',
      data: data.monthlyBorrows.map(m => m.count),
      borderColor: '#6366f1',
      backgroundColor: '#6366f120',
      tension: 0.4,
      fill: true,
    }]
  };

  const chartOptions = {
    responsive: true,
    plugins: { legend: { labels: { color: '#94a3b8' } } },
    scales: {
      x: { ticks: { color: '#64748b' }, grid: { color: '#1e293b' } },
      y: { ticks: { color: '#64748b' }, grid: { color: '#1e293b' } }
    }
  };

  const pieOptions = {
    responsive: true,
    plugins: { legend: { labels: { color: '#94a3b8' } } }
  };

  return (
    <div style={{ minHeight: '100vh', background: '#0f172a', padding: 24 }}>
      <div style={{ maxWidth: 1100, margin: '0 auto' }}>
        <h2 style={{ color: '#f1f5f9', marginBottom: 24 }}>Dashboard</h2>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(160px, 1fr))', gap: 16, marginBottom: 32 }}>
          {statCards.map(card => (
            <div key={card.label} style={{
              background: '#1e293b', borderRadius: 12, padding: 20,
              borderTop: `3px solid ${card.color}`
            }}>
              <p style={{ color: '#64748b', fontSize: 13, margin: '0 0 8px' }}>{card.label}</p>
              <p style={{ color: '#f1f5f9', fontSize: 28, fontWeight: 700, margin: 0 }}>{card.value}</p>
            </div>
          ))}
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24, marginBottom: 24 }}>
          <div style={{ background: '#1e293b', borderRadius: 12, padding: 20 }}>
            <h3 style={{ color: '#94a3b8', marginBottom: 16, fontSize: 14 }}>En Çok Ödünç Alınan Kitaplar</h3>
            <Bar data={barData} options={chartOptions} />
          </div>
          <div style={{ background: '#1e293b', borderRadius: 12, padding: 20 }}>
            <h3 style={{ color: '#94a3b8', marginBottom: 16, fontSize: 14 }}>Kategori Dağılımı</h3>
            <Pie data={pieData} options={pieOptions} />
          </div>
        </div>

        <div style={{ background: '#1e293b', borderRadius: 12, padding: 20 }}>
          <h3 style={{ color: '#94a3b8', marginBottom: 16, fontSize: 14 }}>Son 6 Ay Ödünç Trendi</h3>
          <Line data={lineData} options={chartOptions} />
        </div>
      </div>
    </div>
  );
}
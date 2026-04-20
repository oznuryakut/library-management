import axios from 'axios';

const getHeaders = () => ({
  headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
});

export const getRecommendations = () => {
  const token = localStorage.getItem('token');
  return token
    ? axios.get('http://localhost:8080/api/recommendations', getHeaders())
    : axios.get('http://localhost:8080/api/recommendations/popular');
};

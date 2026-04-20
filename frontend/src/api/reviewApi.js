import axios from 'axios';

const getHeaders = () => ({
  headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
});

export const getReviews = (bookId) =>
  axios.get(`http://localhost:8080/api/books/${bookId}/reviews`);

export const getRating = (bookId) =>
  axios.get(`http://localhost:8080/api/books/${bookId}/reviews/rating`);

export const addReview = (bookId, data) =>
  axios.post(`http://localhost:8080/api/books/${bookId}/reviews`, data, getHeaders());

export const deleteReview = (bookId, reviewId) =>
  axios.delete(`http://localhost:8080/api/books/${bookId}/reviews/${reviewId}`, getHeaders());
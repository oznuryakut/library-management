import axios from 'axios';

const API = 'http://localhost:8080/api/borrows';

const getHeaders = () => ({
  headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
});

export const borrowBook = (bookId) => axios.post(`${API}/${bookId}`, {}, getHeaders());
export const returnBook = (borrowId) => axios.put(`${API}/${borrowId}/return`, {}, getHeaders());
export const getMyBorrows = () => axios.get(`${API}/my`, getHeaders());
export const getAllBorrows = () => axios.get(`${API}/admin/all`, getHeaders());
export const getOverdueBorrows = () => axios.get(`${API}/admin/overdue`, getHeaders());
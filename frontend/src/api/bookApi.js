import axios from 'axios';

const API = 'http://localhost:8080/api/books';

const getHeaders = () => ({
  headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
});

export const getBooks = (search = '', page = 0) =>
  axios.get(`${API}?search=${search}&page=${page}&size=10`);

export const getBook = (id) => axios.get(`${API}/${id}`);
export const createBook = (data) => axios.post(API, data, getHeaders());
export const updateBook = (id, data) => axios.put(`${API}/${id}`, data, getHeaders());
export const deleteBook = (id) => axios.delete(`${API}/${id}`, getHeaders());
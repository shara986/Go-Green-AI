import api from './axiosInstance';

// =================== PLANTS API ===================
export const fetchPlants = async ({ search = '', active, pageNo = 0, pageSize = 20, sortBy = 'createdAt', sortDir = 'desc' } = {}) => {
  const params = { pageNo, pageSize, sortBy, sortDir };
  if (search) params.search = search;
  if (active !== undefined) params.active = active;

  const res = await api.get('/v1/plants', { params });
  return res.data?.data || res.data;
};

export const fetchPlantById = async (plantId) => {
  const res = await api.get(`/v1/plants/${plantId}`);
  return res.data?.data || res.data;
};

export const fetchPlantsByCategory = async (categoryId) => {
  const res = await api.get(`/v1/plants/category/${categoryId}`);
  return res.data?.data || res.data;
};

// =================== CATEGORIES API ===================
export const fetchCategories = async () => {
  const res = await api.get('/v1/categories');
  return res.data?.data || res.data;
};

export const fetchCategoryById = async (categoryId) => {
  const res = await api.get(`/v1/categories/${categoryId}`);
  return res.data?.data || res.data;
};

// =================== ORDERS API ===================
export const fetchMyOrders = async () => {
  const res = await api.get('/v1/orders/my-orders');
  return res.data?.data || res.data;
};

export const fetchOrderById = async (orderId) => {
  const res = await api.get(`/v1/orders/${orderId}`);
  return res.data?.data || res.data;
};

export const checkoutCart = async (orderRequestDto) => {
  const res = await api.post('/v1/orders/checkout', orderRequestDto);
  return res.data?.data || res.data;
};

export const cancelOrder = async (orderId) => {
  const res = await api.post(`/v1/orders/${orderId}/cancel`);
  return res.data?.data || res.data;
};

// =================== CART API ===================
export const fetchCart = async () => {
  const res = await api.get('/v1/cart');
  return res.data?.data || res.data;
};

export const addToCart = async (plantId, quantity = 1) => {
  const res = await api.post('/v1/cart/items', { plantId, quantity });
  return res.data?.data || res.data;
};

export const updateCartItemQuantity = async (itemId, quantity) => {
  const res = await api.put(`/v1/cart/items/${itemId}?quantity=${quantity}`);
  return res.data?.data || res.data;
};

export const removeCartItem = async (itemId) => {
  const res = await api.delete(`/v1/cart/items/${itemId}`);
  return res.data?.data || res.data;
};

export const clearCart = async () => {
  const res = await api.delete('/v1/cart');
  return res.data?.data || res.data;
};

// =================== PROFILE / USER API ===================
export const fetchUserProfile = async () => {
  const res = await api.get('/users/me');
  return res.data?.data || res.data;
};

export const updateUserProfile = async (updateDto) => {
  const res = await api.put('/users/me', updateDto);
  return res.data?.data || res.data;
};

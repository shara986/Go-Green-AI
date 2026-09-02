import React, { useState, useEffect } from 'react';
import { FiSearch, FiFilter, FiPlus, FiEdit2, FiTrash2, FiImage, FiX } from 'react-icons/fi';
import api from '../../api/axiosInstance';
import { LoadingSkeleton, EmptyState } from '../../components/common/UIState';
import './NurseryPlantsPage.css';

const NurseryPlantsPage = () => {
  const [loading, setLoading] = useState(true);
  const [plants, setPlants] = useState([]);
  const [categories, setCategories] = useState([]);
  const [search, setSearch] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('');
  
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  
  const initialFormState = {
    id: null,
    name: '',
    categoryId: '',
    description: '',
    price: '',
    stockQuantity: '',
    sunlightRequirement: 'Medium',
    waterRequirement: 'Medium',
    imageUrl: ''
  };
  const [formData, setFormData] = useState(initialFormState);
  
  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        // Fetch Categories
        let fetchedCats = [];
        try {
          const catRes = await api.get('/categories');
          fetchedCats = catRes.data?.data || [];
        } catch {
          fetchedCats = [
            { id: '1', name: 'Indoor Plants' },
            { id: '2', name: 'Outdoor Plants' },
            { id: '3', name: 'Succulents' }
          ];
        }
        setCategories(fetchedCats);

        // Fetch Plants
        try {
          const plantsRes = await api.get('/nursery/plants');
          const data = plantsRes.data?.data || plantsRes.data;
          setPlants(Array.isArray(data) ? data : data?.content || []);
        } catch {
          // Mock data for UI 
          setPlants([
            { id: '101', name: 'Monstera Deliciosa', category: { id: '1', name: 'Indoor Plants' }, price: 45.00, stockQuantity: 24, status: 'In Stock', imageUrl: '' },
            { id: '102', name: 'Snake Plant', category: { id: '1', name: 'Indoor Plants' }, price: 25.00, stockQuantity: 5, status: 'Low Stock', imageUrl: '' },
            { id: '103', name: 'Aloe Vera', category: { id: '3', name: 'Succulents' }, price: 15.00, stockQuantity: 0, status: 'Out of Stock', imageUrl: '' }
          ]);
        }
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const handleOpenModal = (plant = null) => {
    if (plant) {
      setFormData({
        id: plant.id,
        name: plant.name,
        categoryId: plant.category?.id || '',
        description: plant.description || '',
        price: plant.price,
        stockQuantity: plant.stockQuantity,
        sunlightRequirement: plant.sunlightRequirement || 'Medium',
        waterRequirement: plant.waterRequirement || 'Medium',
        imageUrl: plant.imageUrl || ''
      });
      setIsEditing(true);
    } else {
      setFormData(initialFormState);
      setIsEditing(false);
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setFormData(initialFormState);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Simulate Save
    const selectedCat = categories.find(c => String(c.id) === String(formData.categoryId));
    
    if (isEditing) {
      setPlants(prev => prev.map(p => p.id === formData.id ? { 
        ...p, 
        ...formData, 
        category: selectedCat || p.category,
        price: parseFloat(formData.price),
        stockQuantity: parseInt(formData.stockQuantity)
      } : p));
    } else {
      setPlants(prev => [{
        ...formData,
        id: Math.floor(Math.random() * 1000).toString(),
        category: selectedCat || { id: '0', name: 'Uncategorized' },
        price: parseFloat(formData.price),
        stockQuantity: parseInt(formData.stockQuantity),
        status: parseInt(formData.stockQuantity) > 10 ? 'In Stock' : (parseInt(formData.stockQuantity) > 0 ? 'Low Stock' : 'Out of Stock')
      }, ...prev]);
    }
    handleCloseModal();
  };

  const handleDelete = (id) => {
    if (window.confirm('Are you sure you want to delete this plant?')) {
      setPlants(prev => prev.filter(p => p.id !== id));
    }
  };

  const filteredPlants = plants.filter(p => {
    const matchesSearch = p.name.toLowerCase().includes(search.toLowerCase());
    const matchesCategory = categoryFilter ? String(p.category?.id) === String(categoryFilter) : true;
    return matchesSearch && matchesCategory;
  });

  const getStatusBadge = (qty) => {
    if (qty <= 0) return 'status-out';
    if (qty < 10) return 'status-low';
    return 'status-in';
  };
  
  const getStatusLabel = (qty) => {
    if (qty <= 0) return 'Out of Stock';
    if (qty < 10) return 'Low Stock';
    return 'In Stock';
  };

  if (loading) {
    return (
      <div className="nursery-page">
        <h1 className="nursery-page-title">Manage Plants</h1>
        <div className="controls-bar"><LoadingSkeleton type="table" count={1}/></div>
      </div>
    );
  }

  return (
    <div className="nursery-page nursery-plants">
      <div className="page-header-flex">
        <h1 className="nursery-page-title">Manage Plants</h1>
        <button className="btn-primary btn-sm" onClick={() => handleOpenModal()}>
          <FiPlus /> Add Plant
        </button>
      </div>

      <div className="controls-wrapper">
        <div className="search-box">
          <FiSearch className="search-icon" />
          <input 
            type="text" 
            placeholder="Search plants by name..." 
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        <div className="filter-box">
          <FiFilter className="filter-icon" />
          <select value={categoryFilter} onChange={(e) => setCategoryFilter(e.target.value)}>
            <option value="">All Categories</option>
            {categories.map(c => (
              <option key={c.id} value={c.id}>{c.name}</option>
            ))}
          </select>
        </div>
      </div>

      <div className="table-card">
        {filteredPlants.length > 0 ? (
          <div className="table-responsive">
            <table className="nursery-table">
              <thead>
                <tr>
                  <th width="60">Image</th>
                  <th>Plant Name</th>
                  <th>Category</th>
                  <th>Price</th>
                  <th>Quantity</th>
                  <th>Status</th>
                  <th align="right">Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredPlants.map(plant => (
                  <tr key={plant.id}>
                    <td>
                      <div className="table-img-box">
                        {plant.imageUrl ? <img src={plant.imageUrl} alt={plant.name} /> : <FiImage className="placeholder-img" />}
                      </div>
                    </td>
                    <td className="fw-500">{plant.name}</td>
                    <td>{plant.category?.name || '---'}</td>
                    <td>${Number(plant.price).toFixed(2)}</td>
                    <td>{plant.stockQuantity} units</td>
                    <td>
                      <span className={`nursery-badge ${getStatusBadge(plant.stockQuantity)}`}>
                        {getStatusLabel(plant.stockQuantity)}
                      </span>
                    </td>
                    <td align="right">
                      <div className="action-btns">
                        <button className="icon-btn edit" onClick={() => handleOpenModal(plant)} title="Edit"><FiEdit2 /></button>
                        <button className="icon-btn delete" onClick={() => handleDelete(plant.id)} title="Delete"><FiTrash2 /></button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <EmptyState title="No plants found" message="Try adjusting your search or add a new plant." />
        )}
      </div>

      {isModalOpen && (
        <div className="modal-overlay">
          <div className="nursery-modal">
            <div className="modal-header">
              <h3>{isEditing ? 'Edit Plant' : 'Add New Plant'}</h3>
              <button className="close-btn" onClick={handleCloseModal}><FiX size={20}/></button>
            </div>
            <form onSubmit={handleSubmit} className="modal-form">
              <div className="form-grid">
                <div className="form-group full-width">
                  <label>Plant Name *</label>
                  <input type="text" name="name" className="basic-input" value={formData.name} onChange={handleChange} required />
                </div>
                
                <div className="form-group">
                  <label>Category *</label>
                  <select name="categoryId" className="basic-input" value={formData.categoryId} onChange={handleChange} required>
                    <option value="" disabled>Select Category</option>
                    {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                  </select>
                </div>
                
                <div className="form-group">
                  <label>Image URL</label>
                  <input type="text" name="imageUrl" className="basic-input" value={formData.imageUrl} onChange={handleChange} placeholder="https://..." />
                </div>

                <div className="form-group">
                  <label>Price ($) *</label>
                  <input type="number" step="0.01" min="0" name="price" className="basic-input" value={formData.price} onChange={handleChange} required />
                </div>

                <div className="form-group">
                  <label>Stock Quantity *</label>
                  <input type="number" min="0" name="stockQuantity" className="basic-input" value={formData.stockQuantity} onChange={handleChange} required />
                </div>

                <div className="form-group">
                  <label>Sunlight</label>
                  <select name="sunlightRequirement" className="basic-input" value={formData.sunlightRequirement} onChange={handleChange}>
                    <option value="Low">Low Light</option>
                    <option value="Medium">Medium Light</option>
                    <option value="High">High Light</option>
                    <option value="Direct">Direct Sunlight</option>
                  </select>
                </div>
                
                <div className="form-group">
                  <label>Water</label>
                  <select name="waterRequirement" className="basic-input" value={formData.waterRequirement} onChange={handleChange}>
                    <option value="Low">Low Water</option>
                    <option value="Medium">Medium Water</option>
                    <option value="High">High Water</option>
                  </select>
                </div>

                <div className="form-group full-width">
                  <label>Description</label>
                  <textarea name="description" className="basic-input" rows="3" value={formData.description} onChange={handleChange} />
                </div>
              </div>
              <div className="modal-actions">
                <button type="button" className="btn-secondary" onClick={handleCloseModal}>Cancel</button>
                <button type="submit" className="btn-primary">{isEditing ? 'Save Changes' : 'Add Plant'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default NurseryPlantsPage;

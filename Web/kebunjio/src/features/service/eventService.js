import axios from 'axios';

const API_URL = 'http://localhost:8080/api/events';

const eventService = {
  // 获取所有事件
  getAllEvents: async (page = 0, size = 10) => {
    const response = await axios.get(`${API_URL}?page=${page}&size=${size}`);
    return response.data;
  },

  // 获取单个事件详情
  getEventById: async (id) => {
    const response = await axios.get(`${API_URL}/${id}`);
    return response.data;
  },

  // 创建新事件
  createEvent: async (eventData) => {
    const response = await axios.post(API_URL, eventData);
    return response.data;
  },

  // 更新事件
  updateEvent: async (id, eventData) => {
    const response = await axios.put(`${API_URL}/${id}`, eventData);
    return response.data;
  },

  // 删除事件
  deleteEvent: async (id) => {
    await axios.delete(`${API_URL}/${id}`);
  },

  // 上传图片
  uploadImage: async (file) => {
    const formData = new FormData();
    formData.append('file', file);
    try {
      console.log('Uploading file:', file.name, 'size:', file.size);
      const response = await axios.post(`${API_URL}/upload`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Accept': 'application/json'
        },
        transformRequest: [(data, headers) => {
          // 删除 Content-Type 头，让浏览器自动设置正确的 boundary
          delete headers['Content-Type'];
          return data;
        }]
      });
      console.log('Upload response:', response);
      return response.data;
    } catch (error) {
      console.error('Upload error:', error.response || error);
      throw error;
    }
  }
};

export default eventService; 
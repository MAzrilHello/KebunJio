import axios from 'axios';

const BASE_URL = 'http://localhost:8080/events';

// Add error handling wrapper
const handleApiError = async (apiCall) => {
    try {
        const response = await apiCall();
        return response;
    } catch (error) {
        console.error('API Error:', error);
        throw error.response?.data || error.message;
    }
};

export const getAllEvents = () => {
    return handleApiError(() => axios.get(BASE_URL));
};

export const getEventById = (id) => {
    return handleApiError(() => axios.get(`${BASE_URL}/${id}`));
};

export const createEvent = (eventData) => {
    return handleApiError(() => axios.post(BASE_URL, eventData));
};

export const updateEvent = (id, eventData) => {
    return handleApiError(() => axios.put(`${BASE_URL}/${id}`, eventData));
};

export const deleteEvent = (id) => {
    return handleApiError(() => axios.delete(`${BASE_URL}/${id}`));
};
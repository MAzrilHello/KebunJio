import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

// Create an axios instance for making requests to the backend
const axiosInstance = axios.create({
    baseURL: API_BASE_URL,
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
});

const statisticsService = {
    // Fetch the latest statistics for the dashboard
    getLatestStatistics: async () => {
        console.log('Requesting Dashboard data...'); // For debugging
        try {
            // Making GET request to fetch statistics from the backend
            const response = await axiosInstance.get('/api/statistics');
            
            // Log response details for debugging purposes
            console.log('Received response:', response);
            console.log('Response data:', response.data);
            
            return response.data;  // Return the data to be used in the frontend
        } catch (error) {
            console.error('Request failed:', error); // Log the error in case the request fails
            
            // Logging detailed error information for easier debugging
            console.error('Error details:', {
                message: error.message,
                status: error.response?.status,
                statusText: error.response?.statusText,
                data: error.response?.data,
                config: {
                    url: error.config?.url,
                    method: error.config?.method,
                    headers: error.config?.headers
                }
            });

            // Throw the error to be handled by the calling code (in `index.js`)
            throw new Error('Failed to fetch statistics data.');
        }
    }
};

export default statisticsService;

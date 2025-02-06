import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

// 创建一个axios实例
const axiosInstance = axios.create({
    baseURL: API_BASE_URL,
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
});

const statisticsService = {
    // 获取最新的统计数据
    getLatestStatistics: async () => {
        console.log('statisticsService: 开始请求Dashboard数据...');
        console.log('请求URL:', `${API_BASE_URL}/api/statistics`);
        
        try {
            const response = await axiosInstance.get('/api/statistics');
            
            console.log('statisticsService: 收到响应');
            console.log('响应状态:', response.status);
            console.log('响应头:', response.headers);
            console.log('响应数据:', response.data);
            
            return response.data;
        } catch (error) {
            console.error('statisticsService: 请求失败');
            console.error('错误信息:', {
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
            throw error;
        }
    }
};

export default statisticsService; 
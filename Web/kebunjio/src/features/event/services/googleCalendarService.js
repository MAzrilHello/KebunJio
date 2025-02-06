const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';

class GoogleCalendarService {
    constructor() {
        this.baseUrl = `${API_BASE_URL}/google/calendar`;
    }

    async fetchWithErrorHandling(url, options = {}) {
        try {
            const response = await fetch(url, {
                ...options,
                headers: {
                    'Content-Type': 'application/json',
                    ...options.headers,
                },
                credentials: 'include',
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.error || data.message || 'API request failed');
            }

            return data;
        } catch (error) {
            console.error('Google Calendar API Error:', error);
            throw error;
        }
    }

    async getAuthUrl() {
        return this.fetchWithErrorHandling(`${this.baseUrl}/auth-url`);
    }

    async handleAuthCallback(code, eventData) {
        const formattedEventData = {
            id: eventData.id,
            eventId: eventData.eventId,
            name: eventData.title,
            description: eventData.description,
            location: eventData.location,
            startDateTime: new Date(eventData.startTime).toISOString(),
            endDateTime: new Date(eventData.endTime).toISOString(),
            picture: eventData.picture
        };

        return this.fetchWithErrorHandling(`${this.baseUrl}/callback`, {
            method: 'POST',
            body: JSON.stringify({
                code,
                eventData: formattedEventData
            })
        });
    }
}

export const googleCalendarService = new GoogleCalendarService();
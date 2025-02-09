import React, { useState } from 'react';

const AddToGoogleCalendar = ({ event }) => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleAddToCalendar = async () => {
        setLoading(true);
        setError(null);

        try {
            const response = await fetch('/api/google/calendar/auth-url');
            const data = await response.json();

            if (data.error) {
                throw new Error(data.error);
            }

            // Store event data in localStorage for callback
            localStorage.setItem('pendingCalendarEvent', JSON.stringify(event));

            // Redirect to Google OAuth
            window.location.href = data.authUrl;
        } catch (err) {
            setError('Failed to connect to Google Calendar. Please try again.');
            console.error('Error:', err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <button
            onClick={handleAddToCalendar}
            disabled={loading}
            className="bg-blue-500 text-white px-6 py-3 rounded-md hover:bg-blue-600
                       transition-colors duration-200 focus:outline-none focus:ring-2
                       focus:ring-blue-500 focus:ring-offset-2 w-full max-w-md
                       disabled:bg-blue-300 disabled:cursor-not-allowed"
        >
            {loading ? 'Connecting...' : 'Add to my Google Calendar'}
            {error && (
                <p className="text-sm text-red-100 mt-2">{error}</p>
            )}
        </button>
    );
};

export default AddToGoogleCalendar;
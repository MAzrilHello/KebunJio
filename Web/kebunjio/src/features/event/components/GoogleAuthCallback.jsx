import React, { useEffect, useState } from 'react';

const GoogleAuthCallback = () => {
    const [status, setStatus] = useState('Processing...');

    useEffect(() => {
        const handleCallback = async () => {
            // Get URL parameters without router
            const urlParams = new URLSearchParams(window.location.search);
            const code = urlParams.get('code');
            const error = urlParams.get('error');

            if (error || !code) {
                setStatus('Authorization failed');
                setTimeout(() => window.location.href = '/events', 2000);
                return;
            }

            try {
                const eventData = JSON.parse(localStorage.getItem('pendingCalendarEvent'));
                if (!eventData) {
                    throw new Error('No event data found');
                }

                const response = await fetch('/api/google/calendar/callback', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        code,
                        eventData,
                    }),
                });

                const data = await response.json();

                if (!response.ok) {
                    throw new Error(data.error || 'Failed to add event to calendar');
                }

                localStorage.removeItem('pendingCalendarEvent');
                setStatus('Successfully added to Google Calendar!');
                setTimeout(() => window.location.href = '/events', 2000);
            } catch (error) {
                console.error('Error:', error);
                setStatus('Failed to add event, please try again');
                setTimeout(() => window.location.href = '/events', 2000);
            }
        };

        handleCallback();
    }, []); // Empty dependency array since we only want to run this once

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            <div className="bg-white p-8 rounded-lg shadow-md">
                <div className="flex flex-col items-center">
                    {status === 'Processing...' && (
                        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500 mb-4" />
                    )}
                    <h2 className="text-xl font-semibold text-gray-800">{status}</h2>
                </div>
            </div>
        </div>
    );
};

export default GoogleAuthCallback;
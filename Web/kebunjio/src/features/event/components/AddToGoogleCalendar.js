import React, { useState, useEffect } from 'react';
import { updateEvent } from '../services/eventService';

const AddToGoogleCalendar = ({ event }) => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [showSuccessMessage, setShowSuccessMessage] = useState(false);
    const [showAlreadyAddedMessage, setShowAlreadyAddedMessage] = useState(false); // 新增状态管理

    const handleAddToCalendar = async () => {
        setLoading(true);
        setError(null);

        // if the event.description include "The Google account has been successfully added"
        if (event.description && event.description.includes("The Google account has been successfully added")) {
            // if it has, displays popup
            setShowAlreadyAddedMessage(true);
            setLoading(false); // 结束加载状态
            return;
        }

        try {
            // const response = await fetch('/api/google/calendar/auth-url');
            // const data = await response.json();

            // if (data.error) {
            //     throw new Error(data.error);
            // }

            localStorage.setItem('pendingCalendarEvent', JSON.stringify(event));

            window.open('https://calendar.google.com/calendar/u/0/r?pli=1', '_blank');

            setTimeout(() => {
                localStorage.setItem('calendarEventSuccess', 'true');
                setShowSuccessMessage(true);
            }, 2000);
        } catch (err) {
            setError('Failed to connect to Google Calendar. Please try again.');
            console.error('Error:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleCloseSuccessMessage = () => {
        setShowSuccessMessage(false);
        // update event.description
        const fetchEvent = async () => {
            try {
                const newEvent = { ...event };
                newEvent.description = "The Google account has been successfully added " + new Date().toISOString().slice(0, 19).replace("T", " ") + "." + event.description;
                await updateEvent(event.id, newEvent);
            } catch (error) {
                console.error('Error fetching event:', error);
            } finally {
                setLoading(false);
            }
        };
        fetchEvent();
    };

    const handleCloseAlreadyAddedMessage = () => {
        setShowAlreadyAddedMessage(false)
    };

    return (
        <div>
            <button
                onClick={handleAddToCalendar}
                disabled={loading}
                className="bg-blue-500 hover:bg-blue-600 text-white font-semibold py-3 px-6 rounded-md transition duration-200 w-full disabled:bg-blue-300 disabled:cursor-not-allowed"
            >
                {loading ? 'Connecting...' : 'Add to my Google Calendar'}
                {error && (
                    <p className="text-sm text-red-100 mt-2">{error}</p>
                )}
            </button>

            {/* show the message added */}
            {showAlreadyAddedMessage && (
                <div className="fixed top-0 left-0 right-0 bottom-0 flex items-center justify-center bg-black bg-opacity-50">
                    <div className="bg-white p-4 rounded-md">
                        <p className="text-green-500 mb-4">Google account has already been added. Please do not add it again.</p>
                        <button
                            onClick={handleCloseAlreadyAddedMessage}
                            className="bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded-md"
                        >
                            Confirm
                        </button>
                    </div>
                </div>
            )}

            {/* show success messgae */}
            {showSuccessMessage && (
                <div className="fixed top-0 left-0 right-0 bottom-0 flex items-center justify-center bg-black bg-opacity-50">
                    <div className="bg-white p-4 rounded-md">
                        <p className="text-green-500 mb-4">The event has been successfully saved</p>
                        <button
                            onClick={handleCloseSuccessMessage}
                            className="bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded-md"
                        >
                            Confirm
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AddToGoogleCalendar;

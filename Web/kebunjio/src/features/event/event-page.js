import React, { useState, useEffect } from 'react';
import { Routes, Route } from 'react-router-dom';
import EventCard from './components/EventCard';
import EventDetail from './components/EventDetail';
import GoogleAuthCallback from './components/GoogleAuthCallback';
import { getAllEvents } from './services/eventService';

const EventList = () => {
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchEvents();
    }, []);

    const fetchEvents = async () => {
        try {
            setLoading(true);
            setError(null);
            const response = await getAllEvents();
            setEvents(Array.isArray(response.data) ? response.data : []);
        } catch (error) {
            console.error('Error fetching events:', error);
            setError('Failed to load events. Please try again later.');
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-green-600" />
            </div>
        );
    }

    if (error) {
        return (
            <div className="container mx-auto px-4 py-8 text-center">
                <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
                    {error}
                </div>
            </div>
        );
    }

    return (
        <div className="container mx-auto px-4 py-8">
            <h1 className="text-3xl font-bold text-gray-800">Upcoming Events</h1>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {events.map((event) => (
                    <EventCard key={event.id || event.eventId} event={event} />
                ))}
            </div>
        </div>
    );
};

const EventPage = () => {
    return (
        <Routes>
            <Route path="/" element={<EventList />} />
            <Route path="/:id" element={<EventDetail />} />
            <Route path="/oauth2/callback" element={<GoogleAuthCallback />} />
        </Routes>
    );
};

export default EventPage;
import React, { useState, useEffect } from 'react';
import { Routes, Route } from 'react-router-dom';
import EventCard from './components/EventCard';
import EventDetail from './components/EventDetail';
import GoogleAuthCallback from './components/GoogleAuthCallback';
import { getAllEvents } from './services/eventService';

const EventList = () => {
    // State management for events and UI states
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [resultsPerPage, setResultsPerPage] = useState(50);
    const [selectedDate, setSelectedDate] = useState('');

    // Fetch events when component mounts
    useEffect(() => {
        fetchEvents();
    }, []);

    // Function to fetch events from the API
    const fetchEvents = async () => {
        try {
            setLoading(true);
            setError(null);
            const response = await getAllEvents();
            // Ensure we have an array of events, otherwise use empty array
            setEvents(Array.isArray(response.data) ? response.data : []);
        } catch (error) {
            console.error('Error fetching events:', error);
            setError('Failed to load events. Please try again later.');
        } finally {
            setLoading(false);
        }
    };

    // Loading state display
    if (loading) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-green-600" />
            </div>
        );
    }

    // Error state display
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
            {/* Header section with filters */}
            <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-8">
                <h1 className="text-3xl font-bold text-gray-800">Upcoming Events</h1>

                {/* Filter controls */}
                <div className="flex flex-col md:flex-row gap-4 mt-4 md:mt-0">
                    {/* Date filter */}
                    <div className="flex flex-col">
                        <label className="text-sm text-gray-600 mb-1">Date</label>
                        <input
                            type="date"
                            value={selectedDate}
                            onChange={(e) => setSelectedDate(e.target.value)}
                            className="border rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500"
                        />
                    </div>

                    {/* Results per page selector */}
                    <div className="flex flex-col">
                        <label className="text-sm text-gray-600 mb-1">Result per page</label>
                        <select
                            value={resultsPerPage}
                            onChange={(e) => setResultsPerPage(Number(e.target.value))}
                            className="border rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-500"
                        >
                            <option value={10}>10</option>
                            <option value={20}>20</option>
                            <option value={50}>50</option>
                        </select>
                    </div>
                </div>
            </div>

            {/* Event cards grid */}
            {events.length === 0 ? (
                <div className="bg-gray-100 border border-gray-300 text-gray-700 px-4 py-3 rounded text-center">
                    No events found.
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    {events.map((event) => (
                        <EventCard key={event.id || event.eventId} event={event} />
                    ))}
                </div>
            )}

            {/* Pagination placeholder */}
            <div className="mt-8 flex justify-center gap-2">
                {/* Pagination components can be added here */}
            </div>
        </div>
    );
};

// Main component with route configuration
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
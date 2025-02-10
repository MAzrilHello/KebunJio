import React, { useState, useEffect } from 'react';
import EventCard from './components/EventCard';
import { getAllEvents } from './services/eventService';
import Appbar from '../../components/Appbar';

export const EventList = () => {
    const [events, setEvents] = useState([]);
          
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const eventsPerPage = 4;
    const [searchName, setSearchName] = useState('');
    const [searchDate, setSearchDate] = useState('');

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
    

    const filteredEvents = events.filter((event) => {
        const matchesName = event.name.toLowerCase().includes(searchName.toLowerCase());
        const matchesDate = event.startDateTime
            ? event.startDateTime.includes(searchDate)
            : true;
        return matchesName && matchesDate;
    });

    
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
    

    const indexOfLastEvent = currentPage * eventsPerPage;
    const indexOfFirstEvent = indexOfLastEvent - eventsPerPage;
    const currentEvents = filteredEvents.slice(indexOfFirstEvent, indexOfLastEvent);
    const totalPages = Math.ceil(filteredEvents.length / eventsPerPage);

    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    };

    return (
        <div className="min-h-screen bg-gray-50">
            <Appbar/>
            <div className="container mx-auto px-4 py-8">
                        <h1 className="text-3xl font-bold text-gray-800">Upcoming Events</h1>

                        <div className="flex space-x-4 mb-4">
                            <input
                                type="text"
                                placeholder="Search by event name"
                                className="px-4 py-2 border border-gray-300 rounded"
                                value={searchName}
                                onChange={(e) => setSearchName(e.target.value)}
                            />
                            <input
                                type="text"
                                placeholder="Search by event date"
                                className="px-4 py-2 border border-gray-300 rounded"
                                value={searchDate}
                                onChange={(e) => setSearchDate(e.target.value)}
                            />
                        </div>

                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-2 gap-6">
                            {currentEvents.map((event) => (
                                <div key={event.id || event.eventId} className="event-card">
                                    <EventCard event={event} />
                                </div>
                            ))}
                        </div>

                        <div className="mt-8 flex justify-between items-center">
                            <button
                                onClick={() => handlePageChange(currentPage - 1)}
                                disabled={currentPage === 1}
                                className="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400"
                            >
                                Previous
                            </button>

                            <div className="flex space-x-2">
                                {Array.from({ length: totalPages }, (_, index) => (
                                    <button
                                        key={index + 1}
                                        onClick={() => handlePageChange(index + 1)}
                                        className={`px-4 py-2 rounded ${currentPage === index + 1 ? 'bg-blue-500 text-white' : 'bg-gray-200'}`}
                                    >
                                        {index + 1}
                                    </button>
                                ))}
                            </div>

                            <button
                                onClick={() => handlePageChange(currentPage + 1)}
                                disabled={currentPage === totalPages}
                                className="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400"
                            >
                                Next
                            </button>
                        </div>
                    </div>
        </div>
    );
};

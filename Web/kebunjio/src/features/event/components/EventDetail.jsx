import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getEventById } from '../services/eventService';
import AddToGoogleCalendar from './googleCalendar/AddToGoogleCalendar';

const EventDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [event, setEvent] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchEventDetails();
    }, [id]);

    const fetchEventDetails = async () => {
        try {
            const response = await getEventById(id);
            setEvent(response.data);
        } catch (error) {
            console.error('Error fetching event details:', error);
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500" />
            </div>
        );
    }

    if (!event) return <div>Event not found</div>;

    const formatDateTime = (dateString) => {
        return new Date(dateString).toLocaleString('en-US', {
            weekday: 'short',
            day: 'numeric',
            month: 'short',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    return (
        <div className="max-w-6xl mx-auto p-6">
            <button
                onClick={() => navigate('/events')}
                className="flex items-center text-gray-600 hover:text-gray-900 mb-8"
            >
                <span className="mr-2">←</span> Back to event search
            </button>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                <div className="bg-gray-200 aspect-square flex items-center justify-center">
                    {event.picture ? (
                        <img
                            src={event.picture}
                            alt={event.name}
                            className="w-full h-full object-cover"
                        />
                    ) : (
                        <div className="text-gray-500 text-xl">IMAGE PLACEHOLDER</div>
                    )}
                </div>

                <div className="space-y-6">
                    <h1 className="text-4xl font-bold text-gray-900">Event number {event.eventId}</h1>

                    <div className="flex items-center space-x-8">
                        <div className="flex items-center space-x-2">
                            <span className="text-gray-500">🕒</span>
                            <span>{formatDateTime(event.startDateTime)}</span>
                        </div>
                        <div className="flex items-center space-x-2">
                            <span className="text-gray-500">📍</span>
                            <span>{event.location}</span>
                        </div>
                    </div>

                    <p className="text-gray-700 whitespace-pre-wrap">{event.description}</p>

                    <AddToGoogleCalendar
                        event={{
                            eventId: event.eventId,
                            title: event.name,
                            description: event.description,
                            location: event.location,
                            startTime: event.startDateTime,
                            endTime: event.endDateTime,
                            picture: event.picture
                        }}
                    />
                </div>
            </div>
        </div>
    );
};

export default EventDetail;
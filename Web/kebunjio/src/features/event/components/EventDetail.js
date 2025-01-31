import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getEventById } from '../services/eventService';
import AddToGoogleCalendar from './AddToGoogleCalendar';

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

    if (loading) {
        return React.createElement(
            'div',
            { className: "flex items-center justify-center min-h-screen" },
            React.createElement(
                'div',
                { className: "animate-spin rounded-full h-12 w-12 border-b-2 border-green-500" }
            )
        );
    }

    if (!event) return React.createElement('div', null, 'Event not found');

    return React.createElement(
        'div',
        { className: "max-w-6xl mx-auto p-6" },
        [
            React.createElement(
                'button',
                {
                    onClick: () => navigate('/events'),
                    className: "flex items-center text-gray-600 hover:text-gray-900 mb-8",
                    key: "back-button"
                },
                [
                    React.createElement('span', { className: "mr-2", key: "arrow" }, "←"),
                    "Back to event search"
                ]
            ),
            React.createElement(
                'div',
                { className: "grid grid-cols-1 md:grid-cols-2 gap-8", key: "content" },
                [
                    React.createElement(
                        'div',
                        { className: "bg-gray-200 aspect-square flex items-center justify-center", key: "image-container" },
                        event.picture
                            ? React.createElement('img', {
                                src: event.picture,
                                alt: event.name,
                                className: "w-full h-full object-cover"
                            })
                            : React.createElement(
                                'div',
                                { className: "text-gray-500 text-xl" },
                                "IMAGE PLACEHOLDER"
                            )
                    ),
                    React.createElement(
                        'div',
                        { className: "space-y-6", key: "details" },
                        [
                            React.createElement(
                                'h1',
                                { className: "text-4xl font-bold text-gray-900", key: "title" },
                                `Event number ${event.eventId}`
                            ),
                            React.createElement(
                                'div',
                                { className: "flex items-center space-x-8", key: "meta" },
                                [
                                    React.createElement(
                                        'div',
                                        { className: "flex items-center space-x-2", key: "time" },
                                        [
                                            React.createElement('span', { className: "text-gray-500", key: "time-icon" }, "🕒"),
                                            React.createElement('span', { key: "time-text" }, formatDateTime(event.startDateTime))
                                        ]
                                    ),
                                    React.createElement(
                                        'div',
                                        { className: "flex items-center space-x-2", key: "location" },
                                        [
                                            React.createElement('span', { className: "text-gray-500", key: "location-icon" }, "📍"),
                                            React.createElement('span', { key: "location-text" }, event.location)
                                        ]
                                    )
                                ]
                            ),
                            React.createElement(
                                'p',
                                { className: "text-gray-700 whitespace-pre-wrap", key: "description" },
                                event.description
                            ),
                            React.createElement(
                                AddToGoogleCalendar,
                                {
                                    event: {
                                        eventId: event.eventId,
                                        title: event.name,
                                        description: event.description,
                                        location: event.location,
                                        startTime: event.startDateTime,
                                        endTime: event.endDateTime,
                                        picture: event.picture
                                    },
                                    key: "calendar-button"
                                }
                            )
                        ]
                    )
                ]
            )
        ]
    );
};

export default EventDetail;
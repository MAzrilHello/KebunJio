import React from 'react';
import { useNavigate } from 'react-router-dom';

const EventCard = ({ event }) => {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(`/events/${event.eventId}`);
    };

    const getMonthAbbr = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleString('en-US', { month: 'short' }).toUpperCase();
    };

    const getDay = (dateString) => {
        return new Date(dateString).getDate();
    };

    return React.createElement(
        'div',
        {
            className: "bg-green-100 rounded-lg p-6 hover:shadow-lg transition-shadow cursor-pointer",
            onClick: handleClick
        },
        React.createElement(
            'div',
            { className: "flex flex-col" },
            [
                React.createElement(
                    'div',
                    { className: "flex items-start space-x-4 mb-3", key: "header" },
                    [
                        React.createElement(
                            'div',
                            { className: "flex flex-col items-center", key: "date" },
                            [
                                React.createElement(
                                    'span',
                                    { className: "text-sm font-semibold text-gray-600", key: "month" },
                                    getMonthAbbr(event.startDateTime)
                                ),
                                React.createElement(
                                    'span',
                                    { className: "text-2xl font-bold", key: "day" },
                                    getDay(event.startDateTime)
                                )
                            ]
                        ),
                        React.createElement(
                            'div',
                            { className: "flex-1", key: "info" },
                            [
                                React.createElement(
                                    'h3',
                                    { className: "text-xl font-bold text-gray-900 mb-2", key: "title" },
                                    event.name
                                ),
                                React.createElement(
                                    'p',
                                    { className: "text-gray-600 text-sm mb-2", key: "location" },
                                    event.location
                                )
                            ]
                        )
                    ]
                ),
                React.createElement(
                    'p',
                    { className: "text-gray-600 text-sm line-clamp-2", key: "description" },
                    event.description
                ),
                React.createElement(
                    'button',
                    { className: "text-blue-600 text-sm mt-4 hover:underline self-start", key: "button" },
                    "View more"
                )
            ]
        )
    );
};

export default EventCard;

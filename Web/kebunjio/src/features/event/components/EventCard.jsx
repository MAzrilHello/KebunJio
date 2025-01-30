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

    return (
        <div className="bg-green-100 rounded-lg p-6 hover:shadow-lg transition-shadow cursor-pointer"
             onClick={handleClick}>
            <div className="flex flex-col">
                <div className="flex items-start space-x-4 mb-3">
                    <div className="flex flex-col items-center">
                        <span className="text-sm font-semibold text-gray-600">
                            {getMonthAbbr(event.startDateTime)}
                        </span>
                        <span className="text-2xl font-bold">
                            {getDay(event.startDateTime)}
                        </span>
                    </div>
                    <div className="flex-1">
                        <h3 className="text-xl font-bold text-gray-900 mb-2">{event.name}</h3>
                        <p className="text-gray-600 text-sm mb-2">{event.location}</p>
                    </div>
                </div>
                <p className="text-gray-600 text-sm line-clamp-2">{event.description}</p>
                <button className="text-blue-600 text-sm mt-4 hover:underline self-start">
                    View more
                </button>
            </div>
        </div>
    );
};

export default EventCard;
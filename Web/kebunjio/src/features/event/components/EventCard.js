import React from 'react';
import { useNavigate } from 'react-router-dom';

const EventCard = ({ event }) => {
    const navigate = useNavigate();

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return {
            month: date.toLocaleString('default', { month: 'short' }).toUpperCase(),
            day: date.getDate()
        };
    };

    return (
        <div
            className="bg-green-100 rounded-lg overflow-hidden shadow-md hover:shadow-lg transition-all duration-300 cursor-pointer"
            onClick={() => navigate(`/events/${event.id}`)}
        >
            <div className="p-4">
                <div className="flex items-start space-x-4">
                    {/* date */}
                    <div className="flex flex-col items-center min-w-[60px]">
                        <span className="text-purple-600 text-sm font-bold">
                            {formatDate(event.startDateTime).month}
                        </span>
                        <span className="text-2xl font-bold">
                            {formatDate(event.startDateTime).day}
                        </span>
                    </div>

                    {/* event content */}
                    <div className="flex-1">
                        <h3 className="text-xl font-semibold mb-2">{event.name}</h3>
                        <p className="text-gray-600 mb-2">{event.location}</p>
                        <p className="text-gray-600 line-clamp-2">{event.description}</p>
                        <div className="mt-4">
                            <span className="text-blue-600 hover:underline">View more</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default EventCard;
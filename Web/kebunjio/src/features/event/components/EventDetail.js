import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getEventById } from "../services/eventService";
import AddToGoogleCalendar from "./AddToGoogleCalendar";

const EventDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [event, setEvent] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchEvent = async () => {
      try {
        const response = await getEventById(id);
        setEvent(response.data);
      } catch (error) {
        console.error("Error fetching event:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchEvent();
  }, [id]);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500" />
      </div>
    );
  }

  if (!event) return <div>Event not found</div>;

  const formatDateTime = (dateTime) => {
    return new Date(dateTime).toLocaleDateString("en-US", {
      weekday: "short",
      day: "numeric",
      month: "short",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };
  const description = event.description || "";

  // the string needed to find
  const targetString = "The Google account has been successfully added";

  const containsTargetString = description.includes(targetString);

  let targetStringPart = "";
  let remainingDescription = description;

  if (containsTargetString) {

    const targetIndex = description.indexOf(targetString);

    const endIndex = description.indexOf(".", targetIndex) + 1; // 包括句号
    targetStringPart = description.slice(targetIndex, endIndex);
    remainingDescription = description.slice(endIndex).trim();
  }
  return (
    <div className="max-w-7xl mx-auto p-6">
      <button
        onClick={() => navigate("/events")}
        className="flex items-center text-gray-600 mb-6 hover:text-gray-900"
      >
        <span className="mr-2">←</span> Back to event search
      </button>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        {/* left side picture */}
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

        {/* right side content */}
        <div className="space-y-6">
          <h1 className="text-3xl font-bold">Event number {event.id}</h1>
          <div className="flex flex-col space-y-2">
            <div className="flex items-center space-x-2">
              <span className="text-gray-500">🕒</span>
              <span>{formatDateTime(event.startDateTime)}</span>
            </div>
            <div className="flex items-center space-x-2">
              <span className="text-gray-500">📍</span>
              <span>{event.location}</span>
            </div>
          </div>
          {/* <p className="text-gray-700 whitespace-pre-wrap">{event.description}</p> */}
          <div>
            {remainingDescription && (
              <p className="text-gray-700 whitespace-pre-wrap">
                {remainingDescription}
              </p>
            )}
            <AddToGoogleCalendar
              event={{
                id: event.id,
                title: event.name,
                description: event.description,
                location: event.location,
                startTime: event.startDateTime,
                endTime: event.endDateTime,
                picture: event.picture,
              }}
            />
            {targetStringPart && (
                
              <div style={{ color: "green" }}><br></br>{targetStringPart}</div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default EventDetail;

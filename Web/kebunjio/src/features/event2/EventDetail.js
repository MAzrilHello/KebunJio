import React, { useState, useEffect } from 'react';
import { Layout, Button, message } from 'antd';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeftOutlined, ClockCircleOutlined, EnvironmentOutlined } from '@ant-design/icons';
import eventService from '../../service/eventService';
import "./EventDetail.css";
import moment from 'moment';

const { Content } = Layout;

const EventDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [event, setEvent] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchEventDetail = async () => {
      try {
        const data = await eventService.getEventById(id);
        setEvent(data);
      } catch (error) {
        message.error('Failed to fetch event details');
        navigate('/events');
      } finally {
        setLoading(false);
      }
    };

    fetchEventDetail();
  }, [id, navigate]);

  const handleBack = () => {
    navigate('/events');
  };

  const handleAddToCalendar = () => {
    console.log("Add to Google Calendar clicked");
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <Content className="event-detail-page">
      <div className="back-link" onClick={handleBack}>
        <ArrowLeftOutlined /> Back to event search
      </div>
      <div className="event-detail-container">
        {/* 左侧图片 */}
        <div className="event-detail-image">
          {event?.picture ? (
            <img
              src={`http://localhost:8080/api/events/images/${event.picture}`}
              alt={event.name}
              className="event-image"
            />
          ) : (
            <div className="no-image">No image available</div>
          )}
        </div>

        {/* 右侧文字内容 */}
        <div className="event-detail-content">
          <h1 className="event-detail-title">{event?.name}</h1>
          <div className="event-detail-meta">
            <div className="detail-meta-item">
              <ClockCircleOutlined />
              {moment(event?.startDateTime).format('ddd, Do MMM YYYY hh:mm a')}
            </div>
            <div className="detail-meta-item">
              <EnvironmentOutlined />
              {event?.location}
            </div>
          </div>
          <p className="event-detail-description">
            {event?.description}
          </p>
          <div className="event-detail-actions">
            <Button className="google-calendar-btn" onClick={handleAddToCalendar}>
              Add to my Google Calendar
            </Button>
          </div>
        </div>
      </div>
    </Content>
  );
};

export default EventDetail;
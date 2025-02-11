import React, { useState, useEffect } from 'react';
import { Layout, Button, message } from 'antd';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { ArrowLeftOutlined, ClockCircleOutlined, EnvironmentOutlined } from '@ant-design/icons';
import eventService from '../service/eventService';
import "./EventDetail.css";
import moment from 'moment';
import placeholderImage from '../../media/event-placeholder.jpeg'
import Appbar from '../../components/Appbar';

const { Content } = Layout;

const AdminEventDetail = () => {
  const location = useLocation()

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

  }, [id, location.state, navigate]);

  const handleBack = () => {
    navigate('/admin/events');
  }

  /*
  const handleAddToCalendar = () => {
    console.log("Add to Google Calendar clicked");
  };*/

  
  if (loading) {
    return (
      <div>
        <Appbar />
        Loading...
      </div>
    );
  }

  return (
    <div>
      <Appbar/>
          <Content className="event-detail-page">
            <div className="back-link" onClick={handleBack}>
              <ArrowLeftOutlined /> Back to event search
            </div>
            <div className="event-detail-container">
              {/* 左侧图片 */}
              <div className="event-detail-image">
                <img src={placeholderImage}/>
                {
                event?.picture ? (
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
                {//chingnam's code, do not delete
                /*
                <div className="event-detail-actions">
                  <Button className="google-calendar-btn" onClick={handleAddToCalendar}>
                    Add to my Google Calendar
                  </Button>
                </div>*/
                }
              </div>
            </div>
          </Content>
    </div>
    
  );
};

export default AdminEventDetail;
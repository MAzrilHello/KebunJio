import React, { useState, useEffect } from 'react';
import { Layout, Input, Button, TimePicker, DatePicker, message } from 'antd';
import { CalendarOutlined, EnvironmentOutlined } from '@ant-design/icons';
import { useLocation, useParams, useNavigate } from 'react-router-dom';
import moment from 'moment';
import eventService from '../../service/eventService';
import './EditEvent.css';
import axios from 'axios';

const { Content } = Layout;

const EditEvent = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { id } = useParams();
  const isNewEvent = id === 'new';
  const [loading, setLoading] = useState(false);

  const [eventData, setEventData] = useState({
    name: '',
    location: '',
    startDateTime: null,
    endDateTime: null,
    description: '',
    picture: null
  });

  useEffect(() => {
    if (!isNewEvent && location.state?.event) {
      const event = location.state.event;
      setEventData({
        ...event,
        startDateTime: event.startDateTime ? moment(event.startDateTime, 'YYYY-MM-DDTHH:mm:ss') : null,
        endDateTime: event.endDateTime ? moment(event.endDateTime, 'YYYY-MM-DDTHH:mm:ss') : null
      });
    }
  }, [isNewEvent, location.state]);

  const handleImageUpload = async (e) => {
    const file = e.target.files[0];
    if (file) {
      try {
        const response = await eventService.uploadImage(file);
        setEventData(prev => ({
          ...prev,
          picture: response.filename
        }));
        message.success('Image uploaded successfully');
      } catch (error) {
        console.error('Upload error:', error);
        message.error('Failed to upload image');
      }
    }
  };

  const handleSubmit = async () => {
    try {
      setLoading(true);
      const formData = {
        ...eventData,
        startDateTime: eventData.startDateTime ? eventData.startDateTime.format('YYYY-MM-DDTHH:mm:ss') : null,
        endDateTime: eventData.endDateTime ? eventData.endDateTime.format('YYYY-MM-DDTHH:mm:ss') : null
      };
      
      console.log('Submitting event data:', formData);
      
      if (isNewEvent) {
        await eventService.createEvent(formData);
        message.success('Event created successfully');
      } else {
        await eventService.updateEvent(id, formData);
        message.success('Event updated successfully');
      }
      navigate('/events');
    } catch (error) {
      message.error(`Failed to ${isNewEvent ? 'create' : 'update'} event`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Content className="edit-event-page">
      <h1>{isNewEvent ? 'New Event' : 'Edit Event'}</h1>
      
      <div className="edit-form">
        <div className="form-group">
          <Input 
            placeholder="Event Title"
            value={eventData.name}
            onChange={e => setEventData(prev => ({ ...prev, name: e.target.value }))}
          />
        </div>

        <div className="form-row">
          <div className="form-group">
            <DatePicker
              showTime
              placeholder="Event Date & Time"
              value={eventData.startDateTime}
              onChange={(date) => setEventData(prev => ({ ...prev, startDateTime: date }))}
              format="YYYY-MM-DD HH:mm:ss"
            />
          </div>
          <div className="form-group">
            <DatePicker
              showTime
              placeholder="End Date & Time"
              value={eventData.endDateTime}
              onChange={(date) => setEventData(prev => ({ ...prev, endDateTime: date }))}
              format="YYYY-MM-DD HH:mm:ss"
            />
          </div>
        </div>

        <div className="form-group">
          <Input 
            placeholder="Location"
            suffix={<EnvironmentOutlined />}
            value={eventData.location}
            onChange={e => setEventData(prev => ({ ...prev, location: e.target.value }))}
          />
        </div>

        <div className="form-group">
          <Input.TextArea 
            placeholder="Type your question"
            rows={6}
            value={eventData.description}
            onChange={e => setEventData(prev => ({ ...prev, description: e.target.value }))}
          />
        </div>

        <div className="form-group">
          <div className="image-upload-container">
            <input
              type="file"
              id="imageUpload"
              accept="image/*"
              onChange={handleImageUpload}
              style={{ display: 'none' }}
            />
            <label htmlFor="imageUpload" className="upload-button">
              + Add image
            </label>
            {eventData.picture && (
              <div className="image-preview">
                <img
                  src={`http://localhost:8080/api/events/images/${eventData.picture}`}
                  alt="Event preview"
                  className="preview-image"
                />
                <span className="image-name">{eventData.picture}</span>
              </div>
            )}
          </div>
        </div>

        <div className="form-actions">
          <Button 
            type="primary" 
            onClick={handleSubmit} 
            loading={loading}
          >
            {isNewEvent ? 'Create' : 'Update'}
          </Button>
        </div>
      </div>
    </Content>
  );
};

export default EditEvent; 
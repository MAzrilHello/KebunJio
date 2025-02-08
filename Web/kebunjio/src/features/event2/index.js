import React, { useState, useEffect } from 'react';
import { Layout, Card, Button, Input, Select, Dropdown, message, Pagination, Spin } from 'antd';
import { EllipsisOutlined, CloseCircleOutlined, CalendarOutlined, LeftOutlined, RightOutlined, EditOutlined, DeleteOutlined, ClockCircleOutlined, EnvironmentOutlined } from '@ant-design/icons';
import './style.css';
import { useNavigate } from 'react-router-dom';
import eventService from '../service/eventService';
import moment from 'moment';

const { Content } = Layout;

const Events = () => {
  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [totalElements, setTotalElements] = useState(0);
  const [currentImageIndexes, setCurrentImageIndexes] = useState({});
  const navigate = useNavigate();

  // 获取事件列表
  const fetchEvents = async (page = currentPage) => {
    try {
      setLoading(true);
      const response = await eventService.getAllEvents(page, pageSize);
      setEvents(response.content);
      setTotalElements(response.totalElements);
      setCurrentPage(response.number);
    } catch (error) {
      message.error('Failed to fetch events');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchEvents();
  }, [pageSize]);

  const handlePageChange = (page) => {
    fetchEvents(page - 1);
  };

  const handlePrevImage = (eventId) => {
    setCurrentImageIndexes(prev => ({
      ...prev,
      [eventId]: prev[eventId] > 0 ? prev[eventId] - 1 : events.find(e => e.id === eventId).images.length - 1
    }));
  };

  const handleNextImage = (eventId) => {
    const event = events.find(e => e.id === eventId);
    setCurrentImageIndexes(prev => ({
      ...prev,
      [eventId]: prev[eventId] < event.images.length - 1 ? prev[eventId] + 1 : 0
    }));
  };

  const getDropdownItems = (event) => [
    {
      key: 'edit',
      label: (
        <div className="dropdown-item">
          <EditOutlined />
          <span>Edit event</span>
        </div>
      ),
      onClick: () => handleEditEvent(event),
    },
    {
      key: 'delete',
      label: (
        <div className="dropdown-item">
          <DeleteOutlined />
          <span>Delete event</span>
        </div>
      ),
      onClick: () => handleDeleteEvent(event.id),
    },
  ];

  // 处理删除事件
  const handleDeleteEvent = async (id) => {
    try {
      await eventService.deleteEvent(id);
      message.success('Event deleted successfully');
      fetchEvents(); // 重新获取列表
    } catch (error) {
      message.error('Failed to delete event');
    }
  };

  // 处理编辑事件
  const handleEditEvent = (event) => {
    navigate(`/events/edit/${event.id}`, { state: { event } });
  };

  // 处理查看详情
  const handleViewMore = (event) => {
    navigate(`/events/${event.id}`, { state: { event } });
  };

  // 处理添加新事件
  const handleAddNewEvent = () => {
    navigate('/events/edit/new');
  };

  const formatDateTime = (dateTime) => {
    return moment(dateTime).format('MMM D, YYYY h:mm A');
  };

  return (
    <Content className="events-page">
      <div className="page-header">
        <div className="header-left">
          <h1>Upcoming Events</h1>
          <Button 
            type="primary" 
            className="add-event-btn"
            onClick={handleAddNewEvent}
          >
            + Add new event
          </Button>
        </div>
        <div className="header-right">
          <div className="filter-item">
            <div className="filter-label">Label</div>
            <div className="input-wrapper">
              <Input placeholder="Event name" suffix={<CloseCircleOutlined />} />
            </div>
          </div>
          <div className="filter-item">
            <div className="filter-label">Date</div>
            <div className="input-wrapper">
              <Input 
                placeholder="MM/DD/YYYY" 
                suffix={<CalendarOutlined />}
              />
            </div>
          </div>
          <div className="filter-item">
            <div className="filter-label">Result per page</div>
            <Select
              defaultValue={10}
              value={pageSize}
              onChange={(value) => {
                setPageSize(value);
                setCurrentPage(0);
                fetchEvents(0);
              }}
              options={[
                { value: 10, label: '10' },
                { value: 20, label: '20' },
                { value: 50, label: '50' },
              ]}
            />
          </div>
        </div>
      </div>

      <div className="events-grid">
        {events.map(event => (
          <Card key={event.id} className="event-card">
            <div className="event-image-container">
              {event.picture ? (
                <img 
                  src={`http://localhost:8080/api/events/images/${event.picture}`} 
                  alt={event.name} 
                  className="event-image"
                  onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjQjRFQUFGIi8+';
                  }}
                />
              ) : (
                <div className="event-image placeholder-image">
                  <div className="placeholder-text">No Image</div>
                </div>
              )}
              <Dropdown
                menu={{ items: getDropdownItems(event) }}
                trigger={['click']}
                placement="bottomRight"
                overlayClassName="event-dropdown"
              >
                <Button 
                  type="text" 
                  icon={<EllipsisOutlined />} 
                  className="more-btn"
                />
              </Dropdown>
            </div>
            <div className="event-info">
              <div className="event-date">
                <div className="month">
                  {moment(event.startDateTime || new Date()).format('MMM').toUpperCase()}
                </div>
                <div className="day">
                  {moment(event.startDateTime || new Date()).format('D')}
                </div>
              </div>
              <div className="event-content">
                <h3>{event.name}</h3>
                <p>{event.description}</p>
                <button 
                  className="view-more-btn"
                  onClick={() => handleViewMore(event)}
                >
                  View more
                </button>
              </div>
            </div>
          </Card>
        ))}
      </div>

      <div className="events-pagination">
        <Pagination
          current={currentPage + 1}
          total={totalElements}
          pageSize={pageSize}
          onChange={handlePageChange}
          showSizeChanger={false}
        />
      </div>
    </Content>
  );
};

export default Events; 
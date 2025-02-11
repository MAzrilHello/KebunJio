import React, { useState, useEffect } from 'react';
<<<<<<< Updated upstream
import { Layout, Card, Button, Input, Select, Dropdown, message, Pagination, Spin } from 'antd';
=======
import { Layout, Card, Button, Input, Select, Dropdown, message, Pagination, DatePicker } from 'antd';
>>>>>>> Stashed changes
import { EllipsisOutlined, CloseCircleOutlined, CalendarOutlined, LeftOutlined, RightOutlined, EditOutlined, DeleteOutlined, ClockCircleOutlined, EnvironmentOutlined } from '@ant-design/icons';
import './style.css';
import { useNavigate } from 'react-router-dom';
import eventService from '../service/eventService';
import moment from 'moment';
import Appbar from '../../components/Appbar';
import placeholderImage from '../../media/event-placeholder.jpeg';

const { Content } = Layout;

const Events = () => {
  const [events, setEvents] = useState([]);
  const [filteredEvents, setFilteredEvents] = useState([]);
  const [filterSearch, setFilterSearch] = useState(""); // 用于存储搜索的 EventName
  const [filterDate, setFilterDate] = useState(null); // 用于存储搜索的 Date
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [totalElements, setTotalElements] = useState(0);
  const [currentImageIndexes, setCurrentImageIndexes] = useState({});
  const navigate = useNavigate();

  // 获取事件列表
  const fetchEvents = async (page = currentPage, searchParams = {}) => {
    try {
      setLoading(true);
<<<<<<< Updated upstream
      const response = await eventService.getAllEvents();
      console.log("Fetched data")
      console.log(response)
      setEvents(response);
      setFilteredEvents(response);
      //setTotalElements(response.totalElements);
      //setCurrentPage(response.number);
=======
      const response = await eventService.getAllEvents(page, pageSize, searchParams);
      console.log(response);
      setEvents(response.content);
      setFilteredEvents(response.content);
      setTotalElements(response.totalElements);
      setCurrentPage(response.number);
>>>>>>> Stashed changes
    } catch (error) {
      message.error('Failed to fetch events');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchEvents();
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
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

  const handleDeleteEvent = async (id) => {
    try {
      await eventService.deleteEvent(id);
      message.success('Event deleted successfully');
      fetchEvents(); // 重新获取列表
    } catch (error) {
      message.error('Failed to delete event');
    }
  };

  const handleEditEvent = (event) => {
    navigate(`/admin/events/edit/${event.id}`, { state: { event } });
  };

  const handleViewMore = (event) => {
    navigate(`/admin/events/${event.id}`, { state: { event } });
  };

  const handleAddNewEvent = () => {
    navigate(`/admin/events/edit/new`);
  };

  const formatDateTime = (dateTime) => {
    return moment(dateTime).format('MMM D, YYYY h:mm A');
  };

  const onSearchChange = (event) => {
    setFilterSearch(event.target.value);
  };

  const handleDateChange = (value) => {
    console.log("Selected date:", value); // 打印选中的日期
    setFilterDate(value);
  };

  const handleClearDate = () => {
    setFilterDate(null);
  };

  const handleClearSearch = () => {
    setFilterSearch('');
  };

  const onClickSearch = () => {
    console.log("filterSearch", filterSearch);
    console.log("filterDate", filterDate);

    const searchParams = {};
    if (filterSearch.trim() !== '') {
      searchParams.name = filterSearch;
    }
    if (filterDate) {
      try {
        // 确保日期格式为YYYY-MM-DD
        const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
      const formattedDate = new Intl.DateTimeFormat('en-US', options).format(filterDate);
        console.log("Formatted date:", formattedDate);
        searchParams.date = formattedDate;
      } catch (error) {
        console.error("Failed to format date:", error);
        message.error("Invalid date format");
        return; // 阻止搜索
      }
    }
    fetchEvents(0, searchParams); // 从第一页开始搜索
    setCurrentPage(0); // 重置当前页码
  };

  return (
    <div>
      <Appbar />
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
              <div className="filter-label">Event Name</div>
              <div className="input-wrapper">
                <Input
                  placeholder="Event name"
                  value={filterSearch}
                  onChange={onSearchChange}
                  suffix={<CloseCircleOutlined onClick={handleClearSearch} />}
                />
              </div>
            </div>
<<<<<<< Updated upstream

            <div className="events-grid">
              {filteredEvents.map(event => (
                <Card key={event.id} className="event-card">
                  <div className="event-image-container">
                    <img src={placeholderImage}></img>
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
                    <div className="event-date">
                      <div className="month">
                        {moment(event.endDateTime || new Date()).format('MMM').toUpperCase()}
                      </div>
                      <div className="day">
                        {moment(event.endDateTime || new Date()).format('D')}
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
=======
            <div className="filter-item">
              <div className="filter-label">Date</div>
              <div className="small-input" > 
                <DatePicker
                  value={filterDate}
                  onChange={handleDateChange}
                  suffixIcon={<CalendarOutlined />}
                />
              </div>
>>>>>>> Stashed changes
            </div>
            <div className="filter-item">
              <Button style={{ backgroundColor: "#002E14", color: "white", height: "60px" }} onClick={onClickSearch}>Search</Button>
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
          {filteredEvents.map(event => (
            <Card key={event.id} className="event-card">
              <div className="event-image-container">
                <img src={placeholderImage} alt={event.name} className="event-image" />
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
                <div className="event-date">
                  <div className="month">
                    {moment(event.endDateTime || new Date()).format('MMM').toUpperCase()}
                  </div>
                  <div className="day">
                    {moment(event.endDateTime || new Date()).format('D')}
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
    </div>
  );
};

export default Events;
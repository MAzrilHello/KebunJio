import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Spin, Alert } from 'antd';
import ReactECharts from 'echarts-for-react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { 
  faUser, 
  faSeedling,
  faChartLine, 
  faVirusCovid
} from '@fortawesome/free-solid-svg-icons';
import statisticsService from '../service/statisticsService';
import './style.css';

const Dashboard = () => {
  const [statistics, setStatistics] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchStatistics();
  }, []);

  const fetchStatistics = async () => {
    try {
      const data = await statisticsService.getLatestStatistics();
      console.log('Fetched data:', data);
      
      if (!data) {
        setError('No data available');
        return;
      }

      setStatistics(data);
      setError(null);
    } catch (err) {
      console.error('Error fetching data:', err);
      setError('Failed to fetch statistics data');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <Spin size="large" />
      </div>
    );
  }

  if (error) {
    return <Alert message={error} type="error" style={{ margin: '24px' }} />;
  }

  const plantTypeOption = {
    title: {
      text: 'Most popular plant types',
      left: 'left'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {d}%'
    },
    series: [{
      type: 'pie',
      radius: '70%',
      data: statistics?.popularPlantTypes ? 
        Object.entries(statistics.popularPlantTypes).map(([name, value]) => ({
          name,
          value
        })) : [],
      label: {
        show: true,
        formatter: '{b}\n{d}%',
        position: 'outside'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '16',
          fontWeight: 'bold'
        }
      }
    }]
  };

  const diseaseOption = {
    title: {
      text: 'Most reported diseases',
      left: 'left'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {d}%'
    },
    series: [{
      type: 'pie',
      radius: '70%',
      data: statistics?.reportedDiseases ? 
        Object.entries(statistics.reportedDiseases).map(([name, value]) => ({
          name,
          value
        })) : [],
      label: {
        show: true,
        formatter: '{b}\n{d}%',
        position: 'outside'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '16',
          fontWeight: 'bold'
        }
      }
    }]
  };

  return (
    <div className="dashboard">
      <h1>Dashboard</h1>
      
      <Row gutter={[16, 16]}>
        <Col span={6}>
          <Card className="stat-card">
            <FontAwesomeIcon icon={faUser} className="stat-icon" />
            <h2>Total Users</h2>
            <div className="stat-number">{statistics?.totalUsers || 0}</div>
          </Card>
        </Col>
        
        <Col span={6}>
          <Card className="stat-card">
            <FontAwesomeIcon icon={faSeedling} className="stat-icon" />
            <h2>Total Plants Planted</h2>
            <div className="stat-number">{statistics?.totalPlantsPlanted || 0}</div>
          </Card>
        </Col>
        
        <Col span={6}>
          <Card className="stat-card">
            <FontAwesomeIcon icon={faChartLine} className="stat-icon" />
            <h2>Total Plants Harvested</h2>
            <div className="stat-number">{statistics?.totalPlantsHarvested || 0}</div>
          </Card>
        </Col>
        
        <Col span={6}>
          <Card className="stat-card">
            <FontAwesomeIcon icon={faVirusCovid} className="stat-icon" />
            <h2>Total Reported Diseases</h2>
            <div className="stat-number">{statistics?.totalDiseasesReported || 0}</div>
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: '20px' }}>
        <Col span={12}>
          <Card>
            <ReactECharts option={plantTypeOption} />
          </Card>
        </Col>
        <Col span={12}>
          <Card>
            <ReactECharts option={diseaseOption} />
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Dashboard;

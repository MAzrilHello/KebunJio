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
  console.log('Dashboard组件被加载');
  const [statistics, setStatistics] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    console.log('Dashboard useEffect被触发');
    fetchStatistics();
  }, []);

  const fetchStatistics = async () => {
    console.log('开始获取统计数据...');
    try {
      const data = await statisticsService.getLatestStatistics();
      console.log('获取到的原始数据:', data);
      
      if (!data) {
        console.log('警告: 获取到的数据为空');
        setError('数据为空');
        return;
      }
      
      console.log('Dashboard数据:', {
        日期: data.date,
        总用户数: data.totalUsers,
        已种植植物: data.totalPlantsPlanted,
        已收获植物: data.totalPlantsHarvested,
        病害报告数: data.totalDiseasesReported,
        植物类型分布: data.popularPlantTypes,
        病害类型分布: data.reportedDiseases
      });
      
      setStatistics(data);
      setError(null);
    } catch (err) {
      console.error('获取Dashboard数据错误:', err);
      console.error('错误详情:', {
        message: err.message,
        response: err.response,
        stack: err.stack
      });
      setError('获取统计数据失败');
    } finally {
      setLoading(false);
      console.log('数据获取流程结束');
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
      text: 'Most reported disease',
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
            <h2>Total User</h2>
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
            <h2>Total Reported Disease</h2>
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
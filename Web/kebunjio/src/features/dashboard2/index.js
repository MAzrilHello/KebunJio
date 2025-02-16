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
import Appbar from '../../components/Appbar';
import eventService from '../service/eventService';

const Dashboard = () => {
  //console.log('Dashboard组件被加载');
  const [statistics, setStatistics] = useState(null);

  useEffect(() => {
    async function fetchData() {
      const statisticsRes = await statisticsService.getStatistics();
      setStatistics(statisticsRes)
    }

    fetchData()

  }, []);

    const plantTypeOption = {
      title: {
        text: 'Most Popular Plant Types',
        left: 'left'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {d}%'
      },
      series: [{
        type: 'pie',
        radius: '70%',
        data: statistics?.plantTypeCount 
          ? Object.entries(statistics.plantTypeCount).map(([plantId, value]) => ({
              name: statistics.speciesIdToName?.[plantId] || plantId,
              value
            })) 
          : [],
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

  /*const diseaseOption = {
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
  };*/

  return (
    <div>
      <Appbar/>
      <div className="dashboard">
            <h1 style={{fontSize:"1.5rem"}}>Dashboard</h1>
            
            <Row gutter={[16, 16]} style={{marginTop:"16px"}}>
              <Col span={6}>
                <Card className="stat-card">
                  <FontAwesomeIcon icon={faUser} className="stat-icon" />
                  <h2 style={{fontSize:"1.1rem"}}>Total User</h2>
                  <div className="stat-number">{statistics?.totalUser || 0}</div>
                </Card>
              </Col>
              
              <Col span={6}>
                <Card className="stat-card">
                  <FontAwesomeIcon icon={faSeedling} className="stat-icon" />
                  <h2 style={{fontSize:"1.1rem"}}>Total Plants Planted</h2>
                  <div className="stat-number">{statistics?.totalPlanted || 0}</div>
                </Card>
              </Col>
              
              <Col span={6}>
                <Card className="stat-card">
                  <FontAwesomeIcon icon={faChartLine} className="stat-icon" />
                  <h2 style={{fontSize:"1.1rem"}}>Total Plants Harvested</h2>
                  <div className="stat-number">{statistics?.totalHarvested || 0}</div>
                </Card>
              </Col>
              
              <Col span={6}>
                <Card className="stat-card">
                  <FontAwesomeIcon icon={faVirusCovid} className="stat-icon" />
                  <h2 style={{fontSize:"1.1rem"}}>Total Reported Disease</h2>
                  <div className="stat-number">{statistics?.totalDisease || 0}</div>
                </Card>
              </Col>
            </Row>

            <Row style={{ marginTop: '20px', justifyContent: 'center' }}>
              <Col span={16}>
                <Card>
                  <ReactECharts option={plantTypeOption} />
                </Card>
              </Col>
            </Row>
          </div>
    </div>
  );
};

export default Dashboard; 
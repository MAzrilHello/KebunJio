import logo from './logo.svg';
import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
//import DashboardPage from './features/dashboard/dashboard-page';
//<Route path="/dashboard" element={<DashboardPage/>}/>
import EventPage from './features/event/event-page';
// import ForumPage from './features/qna-forum/forum-page';

import ForumTopPage from './features/qna-forum/pages/forum-top-page';
import ForumMyPage from './features/qna-forum/pages/forum-my-post';
import ForumNewPost from './features/qna-forum/pages/forum-new-post';
import ForumSearchPage from './features/qna-forum/pages/forum-search-page';
import UserProfilePage from './features/user-profile/user-profile-page';
import ViewPost from './features/qna-forum/pages/forum-view-post'
import LoginPage from './features/login-signup/login';
import SignUpPage from './features/login-signup/signup';
// src/index.js 或 src/App.js
import './index.css';
import React, { useEffect } from 'react';
import { GoogleOAuthProvider } from '@react-oauth/google';
function App() {
  useEffect(() => {
    // 确保 OAuth 客户端库已加载
    const script = document.createElement('script');
    script.src = 'https://accounts.google.com/gsi/client';
    script.async = true;
    script.onload = () => console.log('Google OAuth client loaded');
    document.body.appendChild(script);
  }, []);
  return (
    
      <BrowserRouter>
        
      
    
        <div className="min-h-screen bg-gray-50">
          <Routes>
            <Route path="/" element={<LoginPage/>}/>
            <Route path="/signup" element={<SignUpPage/>}/>
            
            <Route path="/events/*" element={<EventPage/>}/> {/* 使用/*来匹配子路由 */}
            <Route path="/forum" element={<ForumTopPage/>}/>
            <Route path="/forum/search" element={<ForumSearchPage/>}/>
            <Route path="/forum/new" element={<ForumNewPost/>}/>
            <Route path="/forum/my" element={<ForumMyPage/>}/>
            <Route path="/forum/post" element={<ViewPost/>}/>
            <Route path="/user-profile" element={<UserProfilePage/>}/>
          </Routes>
        </div>
      </BrowserRouter>
  );
}

export default App;
import logo from './logo.svg';
import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Dashboard from './features/dashboard2/index';
import ForumTopPage from './features/qna-forum/pages/forum-top-page';
import ForumMyPage from './features/qna-forum/pages/forum-my-post';
import ForumNewPost from './features/qna-forum/pages/forum-new-post';
import ForumSearchPage from './features/qna-forum/pages/forum-search-page';
import UserProfilePage from './features/user-profile/user-profile-page';
import ViewPost from './features/qna-forum/pages/forum-view-post';
import LoginPage from './features/login-signup/login';
import SignUpPage from './features/login-signup/signup';
// 导入事件相关的组件
import { EventList } from './features/event/event-page';
import EventDetail from './features/event/components/EventDetail';
import GoogleAuthCallback from './features/event/components/GoogleAuthCallback';

import './index.css';
import React, { useEffect } from 'react';

function App() {
  useEffect(() => {
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
            <Route path="/dashboard2" element={<Dashboard />} />
            {/* 事件相关路由 */}
            <Route path="/events" element={<EventList />} />
            <Route path="/events/:id" element={<EventDetail />} />
            <Route path="/events/oauth2/callback" element={<GoogleAuthCallback />} />
            {/* 其他路由 */}
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
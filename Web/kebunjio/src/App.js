import logo from './logo.svg';
import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import React, { useEffect } from 'react';


import ProtectedRoute from './components/ProtectedRoute';
import { AuthProvider } from './context/AuthContext';

import Dashboard from './features/dashboard2/index';  

import Events from './features/event2/index'
import EditEvent from './features/event2/EditEvent';
import AdminEventDetail from './features/event2/EventDetail';

import ForumTopPage from './features/qna-forum/pages/forum-top-page';
import ForumMyPage from './features/qna-forum/pages/forum-my-post';
import ForumNewPost from './features/qna-forum/pages/forum-new-post';
import ForumSearchPage from './features/qna-forum/pages/forum-search-page';
import ForumEditPost from './features/qna-forum/pages/forum-edit-post';

import UserProfilePage from './features/user-profile/user-profile-page';

import ViewPost from './features/qna-forum/pages/forum-view-post';
import LoginPage from './features/login-signup/login';
import SignUpPage from './features/login-signup/signup';

import { EventList } from './features/event/event-page';
import EventDetail from './features/event/components/EventDetail';
import GoogleAuthCallback from './features/event/components/GoogleAuthCallback';

//import './index.css';

function App() {
  useEffect(() => {
    const script = document.createElement('script');
    script.src = 'https://accounts.google.com/gsi/client';
    script.async = true;
    script.onload = () => console.log('Google OAuth client loaded');
    document.body.appendChild(script);
  }, []);

  return (

    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignUpPage />} />

          <Route path="/forum" element={<ProtectedRoute element={<ForumTopPage />} />} />
          <Route path="/forum/search" element={<ProtectedRoute element={<ForumSearchPage />} />} />
          <Route path="/forum/new" element={<ProtectedRoute element={<ForumNewPost />} />} />
          <Route path="/forum/my" element={<ProtectedRoute element={<ForumMyPage />} />} />
          <Route path="/forum/post" element={<ProtectedRoute element={<ViewPost />} />} />

          {<Route path="/user-profile" element={<ProtectedRoute element={<UserProfilePage />} />} />}
          {/*<Route path="/user-profile/edit" element={<ProtectedRoute element={<UserProfileEditPage />} />} />*/}
          <Route path="/forum/:id/edit" element={<ProtectedRoute element={<ForumEditPost />} />} />

          <Route path="/admin/dashboard" element={<ProtectedRoute element={<Dashboard />} />} />
          <Route path="/admin/events" element={<ProtectedRoute element={<Events />} />} />
          <Route path="/admin/events/:id" element={<ProtectedRoute element={<AdminEventDetail />} />} />
          <Route path="/admin/events/edit/*" element={<EditEvent/>}/>

          <Route path="/events" element={<EventList />} />
          <Route path="/events/:id" element={<EventDetail />} />
          <Route path="/events/oauth2/callback" element={<GoogleAuthCallback />} />

        </Routes>
      </BrowserRouter>
    </AuthProvider>
);
}

export default App;

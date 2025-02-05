import logo from './logo.svg';
import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import DashboardPage from './features/dashboard/index';
import EventPage from './features/event/EventPage';
import EventDetail from './features/event/components/EventDetail';
import ForumTopPage from './features/qna-forum/pages/forum-top-page';
import ForumMyPage from './features/qna-forum/pages/forum-my-post';
import ForumNewPost from './features/qna-forum/pages/forum-new-post';
import ForumSearchPage from './features/qna-forum/pages/forum-search-page';
import ViewPost from './features/qna-forum/pages/forum-view-post'
import LoginPage from './features/login-signup/login';
import SignUpPage from './features/login-signup/signup';
import ForumEditPost from './features/qna-forum/pages/forum-edit-post';
import Dashboard from './features/dashboard/index';
import UserProfilePage from './features/user-profile/user-profile-page';
import UserProfileEditPage from './features/user-profile/user-profile-edit-page';

import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignUpPage />} />

          <Route path="/dashboard" element={<ProtectedRoute element={<DashboardPage />} />} />
          <Route path="/events" element={<ProtectedRoute element={<EventPage />} />} />
          <Route path="/events/:id" element={<ProtectedRoute element={<EventDetail />} />} />
          <Route path="/forum" element={<ProtectedRoute element={<ForumTopPage />} />} />
          <Route path="/forum/search" element={<ProtectedRoute element={<ForumSearchPage />} />} />
          <Route path="/forum/new" element={<ProtectedRoute element={<ForumNewPost />} />} />
          <Route path="/forum/my" element={<ProtectedRoute element={<ForumMyPage />} />} />
          <Route path="/forum/post" element={<ProtectedRoute element={<ViewPost />} />} />
          <Route path="/user-profile" element={<ProtectedRoute element={<UserProfilePage />} />} />
          <Route path="/user-profile/update" element={<ProtectedRoute element={<UserProfileEditPage />} />} />
          <Route path="/forum/:id/edit" element={<ProtectedRoute element={<ForumEditPost />} />} />
          <Route path="/admin/dashboard" element={<ProtectedRoute element={<Dashboard />} />} />
          <Route path="/user/profile" element={<ProtectedRoute element={<UserProfilePage />} />} />
          <Route path="/user/profile/edit" element={<ProtectedRoute element={<UserProfileEditPage />} />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
);
}

export default App;
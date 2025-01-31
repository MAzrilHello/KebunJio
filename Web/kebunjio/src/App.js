import logo from './logo.svg';
import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import DashboardPage from './features/dashboard/dashboard-page';
import EventPage from './features/event/event-page';
import ForumPage from './features/qna-forum/forum-page';
import ForumTopPage from './features/qna-forum/pages/forum-top-page';
import ForumMyPage from './features/qna-forum/pages/forum-my-post';
import ForumNewPost from './features/qna-forum/pages/forum-new-post';
import ForumSearchPage from './features/qna-forum/pages/forum-search-page';
import UserProfilePage from './features/user-profile/user-profile-page';
import ViewPost from './features/qna-forum/pages/forum-view-post'
import LoginPage from './features/login-signup/login';
import SignUpPage from './features/login-signup/signup';

function App() {
  return (
      <BrowserRouter>
        <div className="min-h-screen bg-gray-50">
          <Routes>
            <Route path="/" element={<LoginPage/>}/>
            <Route path="/signup" element={<SignUpPage/>}/>
            <Route path="/dashboard" element={<DashboardPage/>}/>
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
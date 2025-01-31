import logo from './logo.svg';
import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import DashboardPage from './features/dashboard/dashboard-page';
import { EventPage, EventDetail } from './features/event';
import ForumPage from './features/qna-forum/forum-page';
import EventPage from './features/event/event-page';
import ForumTopPage from './features/qna-forum/pages/forum-top-page';
import ForumMyPage from './features/qna-forum/pages/forum-my-post';
import ForumNewPost from './features/qna-forum/pages/forum-new-post';
import ForumSearchPage from './features/qna-forum/pages/forum-search-page';
import UserProfilePage from './features/user-profile/user-profile-page';
import ViewPost from './features/qna-forum/pages/forum-view-post'
import LoginPage from './features/login-signup/login';
import SignUpPage from './features/login-signup/signup';
import ForumEditPost from './features/qna-forum/pages/forum-edit-post';

function App() {
  return (
      <BrowserRouter>
<<<<<<< HEAD
        <div className="min-h-screen bg-gray-50">
          <Appbar/>
          <Routes>
            <Route path="/" element={<LoginPage/>}/>
            <Route path="/signup" element={<SignUpPage/>}/>
            <Route path="/dashboard" element={<DashboardPage/>}/>
            <Route path="/events" element={<EventPage/>}/>
            <Route path="/events/:id" element={<EventDetail/>}/>
            <Route path="/forum" element={<ForumPage/>}/>
            <Route path="/event" element={<EventPage/>}/>
            <Route path="/forum" element={<ForumTopPage/>}/>
            <Route path="/forum/search" element={<ForumSearchPage/>}/>
            <Route path="/forum/new" element={<ForumNewPost/>}/>
            <Route path="/forum/my" element={<ForumMyPage/>}/>
            <Route path="/forum/post" element={<ViewPost/>}/>
            <Route path="/user-profile" element={<UserProfilePage/>}/>
          </Routes>
        </div>
=======
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/signup" element={<SignUpPage />} />
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="/events" element={<EventPage />} />
          <Route path="/events/:id" element={<EventDetail />} />
          <Route path="/event" element={<EventPage />} />
          <Route path="/forum" element={<ForumTopPage/>} />
          <Route path="/forum/search" element={<ForumSearchPage/>} />
          <Route path="/forum/new" element={<ForumNewPost/>} /> 
          <Route path="/forum/my" element={<ForumMyPage/>} /> 
          <Route path="/forum/post" element={<ViewPost/>} /> 
          <Route path="/user-profile" element={<UserProfilePage/>} />
          <Route path="/forum/:id/edit" element={<ForumEditPost />} />
        </Routes>
>>>>>>> origin/feature/gardening_event_listing
      </BrowserRouter>
);
}

export default App;
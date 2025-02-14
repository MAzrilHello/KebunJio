import React, { useEffect, useState}  from 'react';
import Appbar from '../../../components/Appbar'
import MenuSidebar from '../components/menu-sidebar'
import PostSneakPeak from '../components/post-sneak-peek';
import '../styling/forum-page.css'
import axios from 'axios';

function ForumTopPage() {
  const [posts, setPosts] = useState([])

  const API_BASE_URL = process.env.REACT_APP_API_LIVE_URL;

  const getPostEndpoint = `${API_BASE_URL}/Forum`;
  
  useEffect(() => {
    async function fetchData() {
      axios.get(getPostEndpoint)
      .then(response => {
        console.log(getPostEndpoint)
        console.log(response.data)
        setPosts(response.data)
      })
      .catch(error => {
        console.error("Error fetching data:", error)
      });
    }

    fetchData()
}, []);

  return (
    <div>
      <Appbar/>
      <div className="page-container">
        <div className="menu-sidebar">
            <MenuSidebar/>
        </div>
        <div className="main-content">
        <p className="page-header">Top post</p>
        {posts.length !== 0 ? (posts.map((post, index) => (
          <PostSneakPeak key={index} post={post}/>
        ))
        ) : (<p>No result</p>)}
        </div>
      </div>
    </div>
  );
}

export default ForumTopPage;
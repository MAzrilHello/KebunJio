import React, { useEffect, useState}  from 'react';
import Appbar from '../../../components/Appbar'
import MenuSidebar from '../components/menu-sidebar'
import PostSneakPeak from '../components/post-sneak-peek';
import '../styling/forum-page.css'
import axios from 'axios';

function ForumTopPage() {
  const [posts, setPosts] = useState([])
  const [hasLiked, setHasLiked] = useState([])

  const API_BASE_URL = process.env.REACT_APP_API_LIVE_URL;

  const getPostEndpoint = `${API_BASE_URL}/Forum`;

  const getUpvotesByUser = `${API_BASE_URL}/Forum/Upvote`;

  async function fetchData() {
    axios.get(getPostEndpoint,{withCredentials:true})
    .then(response => {
      console.log(getPostEndpoint)
      console.log(response.data)
      setPosts(response.data.sort((a, b) => b.upvoteCount - a.upvoteCount).slice(0, 10));
    })
    .catch(error => {
      console.error("Error fetching data:", error)
    });

    axios.get(getUpvotesByUser,{withCredentials:true})
    .then(response => {
      console.log(getUpvotesByUser)
      console.log(response.data)
      setHasLiked(response.data)
    })
    .catch(error => {
      console.error("Error fetching data:", error)
    });
  }
  
  useEffect(() => {
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
        {posts.length !== 0 ? (posts.map(({post,upvoteCount,commentCount},index)=>(
              <PostSneakPeak key={index} post={post} upvoteCount={upvoteCount} commentCount={commentCount} hasLiked={hasLiked[index]} fetchData={fetchData}/>
        ))
        ) : (<p>No result</p>)}
        </div>
      </div>
    </div>
  );
}

export default ForumTopPage;

import React, {useState, useEffect} from 'react';
import Appbar from '../../../components/Appbar'
import MenuSidebar from '../components/menu-sidebar'
import '../styling/forum-page.css'
import PostSneakPeak from '../components/post-sneak-peek';
import axios from 'axios';

function ForumMyPage() {
  const [posts, setPosts] = useState([])

  const API_BASE_URL = process.env.REACT_APP_API_LIVE_URL;

  const getUserPostEndpoint = `${API_BASE_URL}/Forum/User/Posts`;    

    useEffect(() => {
      //still error from BE function
      async function fetchData() {
          axios.get(getUserPostEndpoint)
          .then(response=>{
            setPosts(response.data)
          })
          .catch(err=>{
            console.log(err)
          })
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
        <p className="page-header">My post</p>
            {posts.length!==0?(posts.map((post,index)=>(
                <PostSneakPeak key={index} post={post}/>
            ))):(<p>No result</p>)}
        </div>
      </div>
    </div>
  );
}

export default ForumMyPage;
import React, {useState, useEffect} from 'react';
import Appbar from '../../../components/Appbar'
import MenuSidebar from '../components/menu-sidebar'
import '../styling/forum-page.css'
import PostSneakPeak from '../components/post-sneak-peek';
import axios from 'axios';

function ForumMyPage() {
  const [posts, setPosts] = useState([])
  const [hasLiked, setHasLiked] = useState([])

  const API_BASE_URL = process.env.REACT_APP_API_LIVE_URL;

  const getUserPostEndpoint = `${API_BASE_URL}/Forum/User/Posts`;    
  const getUpvotesByUser = `${API_BASE_URL}/Forum/Upvote`;

  const handleDeletePost = (postId) => {
    setPosts((prevPosts) => prevPosts.filter((postObj) => postObj.post.id !== postId));
  };

    useEffect(() => {
      async function fetchData() {
        try {
          const [postsResponse, upvotesResponse] = await Promise.all([
            axios.get(getUserPostEndpoint, { withCredentials: true }),
            axios.get(getUpvotesByUser, { withCredentials: true }),
          ])
      
          console.log("Posts response data:", postsResponse.data)
          setPosts(postsResponse.data)
      
          console.log("Upvotes response data:", upvotesResponse.data)
          setHasLiked(upvotesResponse.data)
        } catch (error) {
          console.error("Error fetching data:", error)
        }
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
          {posts.length !== 0 ? (
            posts.map(({ post, upvoteCount, commentCount }, index) => (
              <PostSneakPeak 
                key={index} 
                post={post} 
                upvoteCount={upvoteCount} 
                commentCount={commentCount} 
                hasLiked={hasLiked.some(upvote => upvote.postId === post.id && upvote.hasUpvoted)} 
                onDelete={handleDeletePost} 
              />
            ))
          ) : (
            <p>No result</p>
          )}
        </div>
      </div>
    </div>
  );
}

export default ForumMyPage;

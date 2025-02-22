import React, { useEffect, useState}  from 'react';
import Appbar from '../../../components/Appbar'
import MenuSidebar from '../components/menu-sidebar'
import PostSneakPeak from '../components/post-sneak-peek';
import '../styling/forum-page.css'
import axios from 'axios';
import { useAuth } from '../../../context/AuthContext';

function ForumTopPage() {
  const [posts, setPosts] = useState([])
  const [hasLiked, setHasLiked] = useState([])

  const API_BASE_URL = process.env.REACT_APP_API_LIVE_URL

  const getPostEndpoint = `${API_BASE_URL}/Forum`

  const getUpvotesByUser = `${API_BASE_URL}/Forum/Upvote`

  const handleDeletePost = (postId) => {
    setPosts((prevPosts) => prevPosts.filter((postObj) => postObj.post.id !== postId))
  };

  async function fetchData() {
    axios.get(getPostEndpoint,{withCredentials:true})
    .then(response => {
      setPosts(response.data.sort((a, b) => b.upvoteCount - a.upvoteCount).slice(0, 10))
    })
    .catch(error => {
      console.error("Error fetching data:", error)
    });

    axios.get(getUpvotesByUser,{withCredentials:true})
    .then(response => {
      console.log(getUpvotesByUser)
      console.log(response.data)
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
        {posts.length !== 0 ? (
            posts.map(({ post, upvoteCount, commentCount }, index) => {
              console.log("Checking post:", post);  // Log the current post
              console.log("Checking hasLiked for index:", index, " - ", hasLiked); // Log hasLiked array

              // Ensure that the hasLiked array is not empty and contains valid data
              if (hasLiked && hasLiked.length > 0) {
                const hasUserLiked = hasLiked.some(upvote => {
                  console.log("Checking upvote:", upvote);  // Log the upvote object
                  const postId = String(post.id).trim();  // Convert and trim the post.id
                  const upvotePostId = String(upvote.postId).trim();  // Convert and trim the upvote.postId
                  console.log(`Comparing post.id: ${postId} vs upvote.postId: ${upvotePostId}`);
                  console.log(`Comparison result: ${postId === upvotePostId}`);
                  return postId === upvotePostId && upvote.hasUpvoted;
                });

                console.log(`Post ID: ${post.id}, hasLiked: ${hasUserLiked}, hasLiked Array: `, hasLiked);
                
                return (
                  <PostSneakPeak 
                    key={post.id} 
                    post={post} 
                    upvoteCount={upvoteCount} 
                    commentCount={commentCount} 
                    hasLiked={hasUserLiked}
                    onDelete={handleDeletePost} 
                  />
                );
              } else {
                console.log("hasLiked is empty or undefined");
                return null; // Return null if hasLiked is empty or undefined
              }
            })
          ) : (
            <p>No result</p>
          )}


        </div>
      </div>
    </div>
  );
}

export default ForumTopPage;

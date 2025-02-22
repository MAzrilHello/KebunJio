import React, {useState, useEffect} from 'react';
import Appbar from '../../../components/Appbar'
import MenuSidebar from '../components/menu-sidebar'
import '../styling/forum-page.css'
import PostSneakPeak from '../components/post-sneak-peek';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import axios from 'axios';

function ForumSearchPage() {

  const API_BASE_URL = process.env.REACT_APP_API_LIVE_URL;

  const [search_results, setSearchResults] = useState([])

  const [searchInput, setSearchInput] = useState('')

  const [hasLiked, setHasLiked] = useState([])
  
  const getUpvotesByUser = `${API_BASE_URL}/Forum/Upvote`;

  const handleSearchInputChange = (event) => {
      setSearchInput(event.target.value);
  };

  const getSearchData = async () => {
    const getSearchEndpoint = `${API_BASE_URL}/Forum/Search?query=${searchInput}`;
    try {
      const [postsResponse, upvotesResponse] = await Promise.all([
        axios.get(getSearchEndpoint, { withCredentials: true }),
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

  const handleDeletePost = (postId) => {
    setSearchResults((prevPosts) => prevPosts.filter((postObj) => postObj.post.id !== postId));
  };

  return (
    <div>
      <Appbar/>
      <div className="page-container">
        <div className="menu-sidebar">
            <MenuSidebar/>
        </div>
        <div className="main-content">
          <div>
            <Form className="d-flex">
              <Form.Control
                type="search"
                placeholder="Search"
                className="me-2"
                aria-label="Search"
                onChange={handleSearchInputChange}
              />
              <Button variant="primary" onClick={getSearchData}>Search</Button>
            </Form>
          </div>
          <div>
            <p style={{marginTop:"10px", marginLeft:"8px"}} className="page-header">Search result:</p>
            {search_results.length !== 0 ? (
              search_results.map(({ post, upvoteCount, commentCount }, index) => (
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
    </div>
  );
}

export default ForumSearchPage;

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

  const getUpvotesByUser = `${API_BASE_URL}/Forum/Upvote`;

  const handleSearchInputChange = (event) => {
      setSearchInput(event.target.value);
  };

  const getSearchData = () => {
    const getSearchEndpoint = `${API_BASE_URL}/Forum/Search?query=${searchInput}`;
    axios.get(getSearchEndpoint,{withCredentials:true})
    .then(response=>{
      console.log(response.data)
      setSearchResults(response.data)
    })
    .catch(err=>{
      console.log(err)
    })
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

  const handleSearchSubmit = () => {
    getSearchData()
  }

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
              <Button variant="primary" onClick={handleSearchSubmit}>Search</Button>
            </Form>
          </div>
          <div>
            <p style={{marginTop:"10px", marginLeft:"8px"}} className="page-header">Search result:</p>
            {search_results.length!==0?(search_results.map(({post,upvoteCount,commentCount},index)=>(
              <PostSneakPeak key={index} post={post} upvoteCount={upvoteCount} commentCount={commentCount} hasLiked={hasLiked}/>
            ))):(<p style={{marginTop:"10px", marginLeft:"8px"}}>No result</p>)}
          </div>
        </div>
      </div>
    </div>
  );
}

export default ForumSearchPage;

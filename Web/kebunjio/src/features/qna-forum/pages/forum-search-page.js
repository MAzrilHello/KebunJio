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

  const handleSearchInputChange = (event) => {
      setSearchInput(event.target.value);
  };

  const handleSearchSubmit = () => {
    const getSearchEndpoint = `${API_BASE_URL}/Forum/Search?query=${searchInput}`;
    axios.get(getSearchEndpoint)
    .then(response=>{
      setSearchResults(response.data)
    })
    .catch(err=>{
      console.log("No search result")
    })
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
            {search_results.length!==0?(search_results.map((post,index)=>(
                <PostSneakPeak key={index} post={post} upvoteCount={0}/>
            ))):(<p style={{marginTop:"10px", marginLeft:"8px"}}>No result</p>)}
          </div>
        </div>
      </div>
    </div>
  );
}

export default ForumSearchPage;
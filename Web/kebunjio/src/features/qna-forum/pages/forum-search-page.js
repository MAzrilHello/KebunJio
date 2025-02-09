import React, {useState} from 'react';
import Appbar from '../../../components/Appbar'
import MenuSidebar from '../components/menu-sidebar'
import '../styling/forum-page.css'
import PostSneakPeak from '../components/post-sneak-peek';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { useNavigate } from "react-router-dom";
import data from '../dummy-data/post.json'

function ForumSearchPage() {
  let navigate = useNavigate();
 
  const routeChange = (post) =>{ 
     navigate(`/forum/post/?id=${post.id}`, { state: { post } });
   }

  const [search_results, setSearchResults] = useState(data)

  const [searchInput, setSearchInput] = useState('');

  const handleSearchInputChange = (event) => {
      setSearchInput(event.target.value);
  };

  /*TODO: implement with call to API*/
  const handleSearchSubmit = (event) => {
    console.log(searchInput)

    /*For call to API function */
    /* const strLength = getLength(searchInput)
    if (strLength >= 2) {
      try {
        const response = await fetch(api);
        const data = await response.json();
        setPosts(data.books);
      } catch (error) {
        console.error('Error searching books:', error);
      }
    }
    else{
      alert("Please input more than two characters");
    }
    
    */
  }

  function getCleanLength(str) {
    var cleanStr = str.replace(/[^\w]/gi, '');
    return cleanStr.length;
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
                <PostSneakPeak key={index} post={post} onClick={() => routeChange(post)}/>
            ))):(<p style={{marginTop:"10px", marginLeft:"8px"}}>No result</p>)}
          </div>
        </div>
      </div>
    </div>
  );
}

export default ForumSearchPage;
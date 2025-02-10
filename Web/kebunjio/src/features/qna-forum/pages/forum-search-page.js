import React, {useState, useEffect} from 'react';
import Appbar from '../../../components/Appbar'
import MenuSidebar from '../components/menu-sidebar'
import '../styling/forum-page.css'
import PostSneakPeak from '../components/post-sneak-peek';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { useNavigate } from "react-router-dom";

function ForumSearchPage() {
  let navigate = useNavigate();
 
  const routeChange = (post) =>{ 
     navigate(`/forum/post/?id=${post.Id}`, {state: {post : post}});
   }

  const [posts, setPosts] = useState([])
  const [search_results, setSearchResults] = useState([])

    useEffect(() => {
      async function fetchData() {
          const postsRes = await fetch("/dummy-data/post.json");
          const upvotesRes = await fetch("/dummy-data/upvote.json");
          const usersRes = await fetch("/dummy-data/user.json");
          const commentRes = await fetch("/dummy-data/reply.json");
  
          const posts = await postsRes.json();
          const upvotes = await upvotesRes.json();
          const users = await usersRes.json();
          const comment = await commentRes.json();
  
          // Count upvotes per post
          const upvoteCount = upvotes.reduce((acc, { postId }) => {
              acc[postId] = (acc[postId] || 0) + 1;
              return acc;
          }, {});
  
          // Count replies per post
          const commentCount = comment.reduce((acc, { postId }) => {
              acc[postId] = (acc[postId] || 0) + 1;
              return acc;
          }, {});
  
          // Merge data
          const mergedPosts = posts.map(post => ({
              ...post,
              username: users.find(user => user.id === post.UserId)?.username || "Unknown",
              upvote: upvoteCount[post.Id] || 0,
              comment: commentCount[post.Id] || 0
          }));
  
          console.log(mergedPosts)
          setPosts(mergedPosts)
          setSearchResults(mergedPosts)
      }
  
      fetchData();
  }, []);

  const [searchInput, setSearchInput] = useState('')

  const handleSearchInputChange = (event) => {
      setSearchInput(event.target.value);
  };

  const handleSearchSubmit = () => {
    console.log(searchInput)
    const filteredPosts = posts.filter(post => post.Title.includes(searchInput.toLowerCase()))
    setSearchResults(filteredPosts)

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
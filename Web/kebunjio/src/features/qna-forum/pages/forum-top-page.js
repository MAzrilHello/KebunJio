import React, { useState}  from 'react';
import Appbar from '../../../components/Appbar'
import MenuSidebar from '../components/menu-sidebar'
import PostSneakPeak from '../components/post-sneak-peek';
import '../styling/forum-page.css'
import data from '../dummy-data/post.json'

function ForumTopPage() {
  /*TODO: implement call to API using the below useEffect */
  /* const [posts, setPosts] = useState([]) 
    useEffect(() => {
        const fetchTopTenPost = async () => {
            try{
              const response = await fetch(api);
              const data = await response.json();
              setPosts(data.posts)
            } catch (error) {
              console.error("Error fetching top post", error)
            }
          }
      })

  */

  const [posts, setPosts] = useState(data) 

  return (
    <div>
      <Appbar/>
      <div className="page-container">
        <div className="menu-sidebar">
            <MenuSidebar/>
        </div>
        <div className="main-content">
        <p className="page-header">Top post</p>
            {posts.length!==0?(posts.map((post,index)=>(
                <PostSneakPeak key={index} post={post}/>
            ))):(<p>No result</p>)}
            {
              
            }
        </div>
      </div>
    </div>
  );
}

export default ForumTopPage;
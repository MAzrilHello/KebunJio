import React, { useState, useEffect } from 'react';
import Appbar from '../../../components/Appbar'
import MenuSidebar from '../components/menu-sidebar'
import '../styling/forum-page.css'
import PostSneakPeak from '../components/post-sneak-peek';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { useNavigate } from "react-router-dom";

function ForumSearchPage() {
    let navigate = useNavigate();

    const routeChange = (post) => {
        navigate(`/forum/post/?id=${post.Id}`, { state: { post: post } });
    }

    const [posts, setPosts] = useState([]);
    const [searchResults, setSearchResults] = useState([]);
    const [searchInput, setSearchInput] = useState('');

    useEffect(() => {
        async function fetchData() {
            try {
                const postsRes = await fetch("/dummy-data/post.json");
                const upvotesRes = await fetch("/dummy-data/upvote.json");
                const usersRes = await fetch("/dummy-data/user.json");
                const commentRes = await fetch("/dummy-data/reply.json");

                const posts = await postsRes.json();
                const upvotes = await upvotesRes.json();
                const users = await usersRes.json();
                const comment = await commentRes.json();

                const upvoteCount = upvotes.reduce((acc, { postId }) => {
                    acc[postId] = (acc[postId] || 0) + 1;
                    return acc;
                }, {});

                const commentCount = comment.reduce((acc, { postId }) => {
                    acc[postId] = (acc[postId] || 0) + 1;
                    return acc;
                }, {});

                const mergedPosts = posts.map(post => {
                    const user = users.find(user => user.id === post.UserId) || {};
                    return {
                        ...post,
                        username: user.username || "Unknown",
                        avatarUrl: user.avatarUrl || "/default-avatar.png", 
                        upvote: upvoteCount[post.Id] || 0,
                        comment: commentCount[post.Id] || 0
                    };
                });

                console.log("Fetched posts:", mergedPosts);
                setPosts(mergedPosts);
                setSearchResults(mergedPosts);
            } catch (error) {
                console.error("Error fetching data:", error);
            }
        }

        fetchData();
    }, []);

    const handleSearchInputChange = (event) => {
        setSearchInput(event.target.value);
    };

    const handleSearchSubmit = () => {
        console.log("Search input:", searchInput);
        const filteredPosts = posts.filter(post =>
            post.Title.toLowerCase().includes(searchInput.toLowerCase())
        );
        setSearchResults(filteredPosts);
    };

    return (
        <div>
            <Appbar />
            <div className="page-container">
                <div className="menu-sidebar">
                    <MenuSidebar />
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
                        <p style={{ marginTop: "10px", marginLeft: "8px" }} className="page-header">Search result:</p>
                        {searchResults.length !== 0 ? (
                            searchResults.map((post, index) => (
                                <PostSneakPeak key={index} post={post} onClick={() => routeChange(post)} />
                            ))
                        ) : (
                            <p style={{ marginTop: "10px", marginLeft: "8px" }}>No result</p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ForumSearchPage;

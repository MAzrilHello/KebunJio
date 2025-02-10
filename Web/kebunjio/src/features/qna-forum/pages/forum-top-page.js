import React, { useEffect, useState } from 'react';
import Appbar from '../../../components/Appbar';
import MenuSidebar from '../components/menu-sidebar';
import PostSneakPeak from '../components/post-sneak-peek';
import '../styling/forum-page.css';
import { useNavigate } from "react-router-dom";

function ForumTopPage() {
    const [posts, setPosts] = useState([]);

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

            const sortedPosts = mergedPosts.sort((a, b) => {
                if (b.upvote === a.upvote) {
                    return b.comment - a.comment;
                }
                return b.upvote - a.upvote; 
            });

            console.log(sortedPosts);
            setPosts(sortedPosts);
        }

        fetchData();
    }, []);

    let navigate = useNavigate();
    const routeChange = (post) => {
        console.log(post);
        navigate(`/forum/post/?id=${post.Id}`, { state: { post: post } });
    };

    return (
        <div>
            <Appbar />
            <div className="page-container">
                <div className="menu-sidebar">
                    <MenuSidebar />
                </div>
                <div className="main-content">
                    <p className="page-header">Top post</p>
                    {posts.length !== 0 ? (
                        posts.map((post, index) => (
                            <PostSneakPeak key={index} post={post} onClick={() => routeChange(post)} />
                        ))
                    ) : (
                        <p>No result</p>
                    )}
                </div>
            </div>
        </div>
    );
}

export default ForumTopPage;

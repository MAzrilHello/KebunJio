import React, {useState, useEffect} from 'react';
import Appbar from '../../../components/Appbar';
import MenuSidebar from '../components/menu-sidebar';
import '../styling/forum-page.css';
import PostSneakPeak from '../components/post-sneak-peek';
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../../context/AuthContext";

function ForumMyPage() {
    const { authUser,setAuthUser } = useAuth();
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        console.log("Current User:", authUser);
        console.log("AuthUser ID Type:", typeof authUser?.Id);
        if (!authUser) return;

        async function fetchData() {
            const postsRes = await fetch("/dummy-data/post.json");
            const upvotesRes = await fetch("/dummy-data/upvote.json");
            const usersRes = await fetch("/dummy-data/user.json");
            const commentRes = await fetch("/dummy-data/reply.json");

            const posts = await postsRes.json();
            const upvotes = await upvotesRes.json();
            const users = await usersRes.json();
            const comment = await commentRes.json();

            console.log("Fetched Posts:", posts);
            console.log("Fetched Users:", users);

            const filteredPosts = posts.filter(post => Number(post.UserId) === Number(authUser?.Id));

            console.log("Filtered Posts:", filteredPosts);

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

            const mergedPosts = filteredPosts.map(post => {
                const user = users.find(user => user.id === post.UserId);
                return {
                    ...post,
                    username: user?.username || "Unknown",
                    avatarUrl: user?.avatarUrl || "/default-avatar.png",
                    upvote: upvoteCount[post.Id] || 0,
                    comment: commentCount[post.Id] || 0
                };
            });

            console.log("Merged Posts:", mergedPosts);
            setPosts(mergedPosts);
        }

        fetchData();
    }, [authUser]);

    let navigate = useNavigate();

    const routeChange = (post) => {
        navigate(`/forum/post/?id=${post.Id}`, { state: { post } });
    };

    const storedUser = JSON.parse(localStorage.getItem("authUser"));
    if (storedUser) {
        setAuthUser(storedUser);
    }


    return (
        <div>
            <Appbar avatarUrl={authUser?.avatarUrl} />
            <div className="page-container">
                <div className="menu-sidebar">
                    <MenuSidebar />
                </div>
                <div className="main-content">
                    <p className="page-header">My posts</p>
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

export default ForumMyPage;

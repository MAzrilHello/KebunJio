import React, { useEffect, useState } from "react";
import FullPost from "../components/full-post";
import Reply from "../components/reply";
import Form from "react-bootstrap/Form";
import { Button } from "react-bootstrap";
import { useLocation } from "react-router-dom";

import Appbar from "../../../components/Appbar";
import MenuSidebar from "../components/menu-sidebar";
import "../styling/forum-page.css";
import axios from "axios";
import { useAuth } from "../../../context/AuthContext";

const Post = () => {
    const location = useLocation();
    const [post, setPost] = useState(null);
    const [comments, setComments] = useState([]);
    const { authUser, setAuthUser } = useAuth();

    useEffect(() => {
        axios.get("/userProfile")
            .then(res => {
                console.log("Fetched user profile:", res.data);
                setAuthUser(res.data);
            })
            .catch(err => console.error("Failed to fetch user profile:", err));
    }, []);

    useEffect(() => {
        const fetchData = async () => {
            try {
                // 从 `location.state` 获取帖子信息
                const fetchedPost = location.state.post;

                const usersRes = await fetch("/dummy-data/user.json");
                const commentsRes = await fetch("/dummy-data/reply.json");
                const replyLikeRes = await fetch("/dummy-data/reply_like.json");
                const replyDislikeRes = await fetch("/dummy-data/reply_dislike.json");

                const usersData = await usersRes.json();
                const commentsData = await commentsRes.json();
                const replyLikes = await replyLikeRes.json();
                const replyDislikes = await replyDislikeRes.json();

                const postUser = usersData.find(user => user.id === fetchedPost.UserId);
                fetchedPost.avatarUrl = postUser?.avatarUrl || "/default-avatar.png";

                setPost(fetchedPost);

                const filteredComments = commentsData.filter(comment => comment.postId === fetchedPost.Id) || [];

                const likeCount = replyLikes.reduce((like, { replyId }) => {
                    like[replyId] = (like[replyId] || 0) + 1;
                    return like;
                }, {});

                const dislikeCount = replyDislikes.reduce((dislike, { replyId }) => {
                    dislike[replyId] = (dislike[replyId] || 0) + 1;
                    return dislike;
                }, {});

                const mergedComments = (filteredComments || []).map((comment) => {
                    const commentUser = usersData.find(user => user.id === comment.userId);
                    return {
                        ...comment,
                        username: commentUser?.username || "Unknown",
                        avatarUrl: commentUser?.avatarUrl || "/default-avatar.png",
                        like: likeCount[comment.replyId] || 0,
                        dislike: dislikeCount[comment.replyId] || 0,
                    };
                });

                setComments(mergedComments);

            } catch (error) {
                console.error("Error fetching data", error);
            }
        };
        fetchData();
    }, []);

    const [replyInput, setReplyInput] = useState("");

    const handleReplyInputChange = (event) => {
        setReplyInput(event.target.value);
    };

    const handleSubmitReply = () => {
        const requestData = {
            reply: replyInput
        };
        console.log(JSON.stringify(requestData));
        setReplyInput("");
        alert("Reply sent!");
    };

    const handleClear = () => {
        setReplyInput("");
    };

    return (
        <div>
            <Appbar />
            <div className="page-container">
                <div className="menu-sidebar">
                    <MenuSidebar />
                </div>
                <div className="main-content">
                    {post ? <FullPost post={post} /> : <p>Loading...</p>}
                    <div>
                        <Form>
                            <Form.Group controlId="replyForm">
                                <Form.Control
                                    className="text-area"
                                    onChange={handleReplyInputChange}
                                    value={replyInput}
                                    as="textarea"
                                    placeholder="Write your reply here"
                                />
                            </Form.Group>
                            <div style={{ marginTop: "16px" }}>
                                <Button style={{ fontSize: "12px" }} variant="secondary" type="reset" onClick={handleClear}>
                                    Cancel
                                </Button>
                                <Button style={{ fontSize: "12px", marginLeft: "8px" }} variant="primary" onClick={handleSubmitReply}>
                                    Reply
                                </Button>
                            </div>
                        </Form>
                    </div>
                    <div style={{ marginTop: "16px" }}>
                        {comments.length !== 0 ? (
                            comments.map((comment, index) => <Reply key={index} userReply={comment} />)
                        ) : (
                            <p>No replies yet</p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Post;

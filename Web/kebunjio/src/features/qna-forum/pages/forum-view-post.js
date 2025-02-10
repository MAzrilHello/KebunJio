import React, { useEffect, useState } from "react";
import FullPost from "../components/full-post";
import Reply from "../components/reply";
import Form from "react-bootstrap/Form";
import { Button } from "react-bootstrap";
import { useLocation } from "react-router-dom";

import Appbar from "../../../components/Appbar";
import MenuSidebar from "../components/menu-sidebar";
import "../styling/forum-page.css";

const Post = () => {
    const location = useLocation()
    const [post, setPost] = useState(null);
    const [comments, setComments] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                //fetch post
                const fetchedPost = location.state.post
                setPost(fetchedPost)

                const usersRes = await fetch("/dummy-data/user.json")
                const commentsRes = await fetch("/dummy-data/reply.json")
                const replyLikeRes = await fetch("/dummy-data/reply_like.json")
                const replyDislikeRes = await fetch("/dummy-data/reply_dislike.json")
    
                const usersData = await usersRes.json()
                const commentsData = await commentsRes.json()
                const replyLikes = await replyLikeRes.json()
                const replyDislikes = await replyDislikeRes.json()


                //filtered comment
                const filteredComments = commentsData.filter((comment) => comment.postId === fetchedPost.Id) || [];

                // Count likes per reply
                const likeCount = replyLikes.reduce((like, { replyId }) => {
                    like[replyId] = (like[replyId] || 0) + 1
                    return like;
                }, {})
    
                // Count dislikes per reply
                const dislikeCount = replyDislikes.reduce((dislike, { replyId }) => {
                    dislike[replyId] = (dislike[replyId] || 0) + 1
                    return dislike;
                }, {})
    
                const mergedComments = (filteredComments || []).map((comment) => ({
                    ...comment,
                    username: usersData.find(user => user.id === comment.userId)?.username || "Unknown",
                    like: likeCount[comment.replyId] || 0,
                    dislike: dislikeCount[comment.replyId] || 0,
                }));
    
                setComments(mergedComments)
    
            } catch (error) {
                console.error("Error fetching data", error)
            }
        };
        fetchData();
    }, []);

    /* Check if post is received
    useEffect(() => {
        console.log("Fetched Post: ", post);
    }, [post]);*/

    const [replyInput, setReplyInput] = useState("");

    const handleReplyInputChange = (event) => {
        setReplyInput(event.target.value);
    };

    const handleSubmitReply = () => {
        const requestData = {
            reply: replyInput
        }
        console.log(JSON.stringify(requestData))
        setReplyInput("")
        alert("Reply sent!")

        //
        /**
         * 
        API implementation
        fetch('https://', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestData),
        })
        .then(response => response.json())  // Parse the response to JSON
        .then(data => {
            console.log('Success:', data)
        })
        .catch((error) => {
            console.error('Error:', error)
        })
        */
    };

    const handleClear = () => {
        setReplyInput("")
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

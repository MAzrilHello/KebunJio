import React, { useEffect, useState } from "react";
import FullPost from "../components/full-post";
import Reply from "../components/reply";
import Form from "react-bootstrap/Form";
import { Button } from "react-bootstrap";
import { useParams } from "react-router-dom";
import axios from "axios";

import Appbar from "../../../components/Appbar";
import MenuSidebar from "../components/menu-sidebar";
import "../styling/forum-page.css";
import { sanitizeInput } from "../../../service/sanitizeService";

const Post = () => {
    const { id } = useParams();
    const [post, setPost] = useState(null);
    const [comments, setComments] = useState([]);

    const API_BASE_URL = process.env.REACT_APP_API_LIVE_URL;

    const getPostEndpoint = `${API_BASE_URL}/Forum/Post/${id}`;

    const createReplyEndpoint = `${API_BASE_URL}/Forum/Post/${id}/CreateComment`;

    useEffect(() => {
        const fetchData = async () => {
            try {
                axios.get(getPostEndpoint)
                .then(response=>{
                    console.log(response.data);
                    setPost(response.data.post);
                    setComments(response.data.commentList);
                })
                .catch(error => {
                    console.error("Error fetching data:", error)
                })
    
            } catch (error) {
                console.error("Error fetching data", error)
            }
        };
        fetchData();
    }, []);

    const [replyInput, setReplyInput] = useState("");

    const handleReplyInputChange = (event) => {
        setReplyInput(event.target.value);
    };

    const handleSubmitReply = async () => {
        if (!replyInput.trim()) return
    
        try {
            const response = await axios.post(createReplyEndpoint, {
                commentContent: sanitizeInput(replyInput)
            });
    
            if (response.status === 201) { 
                const newComment = response.data
                setComments(prevComments => [newComment, ...prevComments])
                setReplyInput("")
            } else {
                console.error("Failed to submit reply")
            }
        } catch (error) {
            console.error("Error submitting reply:", error)
        }
    }

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

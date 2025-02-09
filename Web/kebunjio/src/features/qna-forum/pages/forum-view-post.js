import React, {useState} from "react";

import FullPost from '../components/full-post'
import Reply from "../components/reply";
import Form from 'react-bootstrap/Form';
import { Button } from "react-bootstrap";
import { useLocation } from "react-router-dom";

import Appbar from '../../../components/Appbar'
import MenuSidebar from '../components/menu-sidebar'
import '../styling/forum-page.css'

const Post = () =>{
    const location = useLocation();
    const initialPost = location.state?.post;
    const [post, setPost] = useState(initialPost || null); 

    const replies = [{
        username: "Yasmine",
        time: new Date("2025-01-30").toDateString(),
        content: "OK, cool",
        like: 12,
        dislike: 3,
        hasLiked: false,
        hasDisliked: true
    },
    {
        username: "Yasmine",
        time: new Date("2025-01-30").toDateString(),
        content: "OK, cool",
        like: 12,
        dislike: 3,
        hasLiked: true,
        hasDisliked: false
    }]

    const [replyInput, setReplyInput] = useState('')

    const handleReplyInputChange = (event) => {
        setReplyInput(event.target.value)
    }

    const handleSubmitReply = (event) => {
        alert(replyInput)
        /*Implement API call here */
    }

    const handleClear = (event) => {
        setReplyInput("")
    }

    if (!post) {
        return <p>No post data available</p>;
    }

    return(
        <div>
            <Appbar/>
            <div className="page-container">
                <div className="menu-sidebar">
                    <MenuSidebar/>
                </div>
                <div className="main-content">
                <div>
                    <FullPost post={post}/>
                </div>
                <div>
                    <Form>
                        <Form.Group controlId="exampleForm.ControlInput1">
                            <Form.Control className="text-area" onChange={handleReplyInputChange} value={replyInput} type="textarea" placeholder="Write your reply here"/>
                        </Form.Group>
                        <div style={{marginTop:"16px"}}>
                            <Button style={{fontSize:"12px"}} variant="secondary" type="reset" onClick={handleClear}>Cancel</Button>
                            <Button style={{fontSize:"12px", marginLeft:"8px"}} variant="primary" onClick={handleSubmitReply}>Reply</Button>
                        </div>
                    </Form>
                </div>
                <div style={{marginTop:"16px"}}>
                {replies.length !== 0 ? (
                    replies.map((reply, index) => (
                        <Reply key={index} reply={reply} />
                    ))
                ) : (
                    <p>No replies yet</p>
                )}
                </div>

                </div>
            </div>
        </div>

    )}

export default Post
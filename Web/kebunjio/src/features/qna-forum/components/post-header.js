import React from "react";
import Image from 'react-bootstrap/Image';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from "react-bootstrap/Col";
import Dropdown from 'react-bootstrap/Dropdown';

import '../styling/forum-page.css'
import placeholderImage from '../../../media/placeholder.jpg';
import { useNavigate } from "react-router-dom";

const PostHeader = ({post}) =>{
    const navigate = useNavigate()

    const deletePost = () => {
        alert("Delete post")
        const requestData = {
            Id: post.Id
        }
        /*API Implementation
            fetch('https://', {
                method: 'DELETE',
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
    }
    return(
        <Container className="post-header">
            <Row className="align-items-center">
                <Col xs="auto">
                    <Image
                        src={placeholderImage}
                        roundedCircle
                        className="post-header-avatar"
                    />
                </Col>
                <Col>
                    <div className="post-header-info">
                        <div className="post-header-username">{post.username}</div>
                        <div className="post-header-time">{post.PublishedDateTime}</div>
                    </div>
                </Col>
                <Col xs="auto">
                
                {
                    /*For further implementation, now show all dropdown 
                    post.username==="Kelly"?(<Dropdown>
                        <Dropdown.Toggle className="three-dot">
                        </Dropdown.Toggle>
                            <Dropdown.Menu>
                                <Dropdown.Item onClick={()=>{navigate(`/forum/${post.Id}/edit`, {state:{post}})}}>Edit Post</Dropdown.Item>
                                <Dropdown.Item onClick={()=>{alert("Delete post")}}>Delete Post</Dropdown.Item>
                            </Dropdown.Menu>
                        </Dropdown>):(<div></div>)
                */}
                <Dropdown>
                        <Dropdown.Toggle className="three-dot">
                        </Dropdown.Toggle>
                            <Dropdown.Menu>
                                <Dropdown.Item onClick={()=>{navigate(`/forum/${post.Id}/edit`, {state:{post}})}}>Edit Post</Dropdown.Item>
                                <Dropdown.Item onClick={deletePost}>Delete Post</Dropdown.Item>
                            </Dropdown.Menu>
                </Dropdown>
                </Col>
            </Row>
        </Container>
    )
}

export default PostHeader
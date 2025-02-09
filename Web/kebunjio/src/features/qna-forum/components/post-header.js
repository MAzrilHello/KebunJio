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
                        <div className="post-header-time">{post.time}</div>
                    </div>
                </Col>
                <Col xs="auto">
                {
                    post.username==="Kelly"?(<Dropdown>
                        <Dropdown.Toggle className="three-dot">
                        </Dropdown.Toggle>
                            <Dropdown.Menu>
                                <Dropdown.Item onClick={()=>{navigate(`/forum/${post.id}/edit`, {state:{post}})}}>Edit Post</Dropdown.Item>
                                <Dropdown.Item onClick={()=>{alert("Delete post")}}>Delete Post</Dropdown.Item>
                            </Dropdown.Menu>
                        </Dropdown>):(<div></div>)
                }
                </Col>
            </Row>
        </Container>
    )
}

export default PostHeader
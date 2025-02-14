import React from "react";
import Image from 'react-bootstrap/Image';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from "react-bootstrap/Col";
import Dropdown from 'react-bootstrap/Dropdown';
import axios from 'axios';

import '../styling/forum-page.css'
import placeholderImage from '../../../media/placeholder.jpg';
import { useNavigate } from "react-router-dom";

import { useAuth } from "../../../context/AuthContext";

const PostHeader = ({post}) =>{
    const navigate = useNavigate()

    const API_BASE_URL = process.env.REACT_APP_API_LIVE_URL;

    const getDeletePostEndpoint = `${API_BASE_URL}/Forum/User/Post/${post.id}`;    
    
    const {authUser,isAdmin} = useAuth()

    const deletePost = () => {
        axios.delete(getDeletePostEndpoint)
        .then(response=>{
            console.log(response)
        })
        .catch(err=>{
            console.log(err)
        })
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
                        <div className="post-header-username">{post.userId}</div>
                        <div className="post-header-time">{post.publishedDateTime}</div>
                    </div>
                </Col>
                <Col xs="auto">
                
                {
                    post.username===authUser.Username?(<Dropdown>
                        <Dropdown.Toggle className="three-dot">
                        </Dropdown.Toggle>
                            <Dropdown.Menu>
                                <Dropdown.Item onClick={()=>{navigate(`/forum/post/edit/${post.id}`)}}>Edit Post</Dropdown.Item>
                                <Dropdown.Item onClick={deletePost}>Delete Post</Dropdown.Item>
                            </Dropdown.Menu>
                        </Dropdown>):(<div></div>)
                }
                {
                    isAdmin?(<Dropdown>
                        <Dropdown.Toggle className="three-dot">
                        </Dropdown.Toggle>
                            <Dropdown.Menu>
                                <Dropdown.Item onClick={deletePost}>Delete Post</Dropdown.Item>
                            </Dropdown.Menu>
                        </Dropdown>):(<div></div>)
                }
                </Col>
            </Row>
        </Container>
    )
}

export default PostHeader
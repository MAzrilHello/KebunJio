import React, { useState } from "react";
import Image from 'react-bootstrap/Image';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from "react-bootstrap/Col";
import Dropdown from 'react-bootstrap/Dropdown';
import { useNavigate } from "react-router-dom";

import '../styling/forum-page.css'
import placeholderImage from '../../../media/placeholder.jpg';

const ReplyHeader = ({username, time}) =>{

    const [isEditing, setIsEditing] = useState(false)

    const onClickEdit = () =>{
        setIsEditing = true
    }

    const onClickDelete = () => {}

    return(
        <Container className="post-header">
            <Row className="align-items-center">
                <Col xs="auto">
                    <Image
                        src={placeholderImage}
                        roundedCircle
                        className="reply-header-avatar"
                    />
                </Col>
                <Col>
                    <div className="post-header-info">
                        <div className="reply-header-username">{username}</div>
                        <div className="reply-header-time">{time}</div>
                    </div>
                </Col>
                <Col xs="auto">

                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-three-dots-vertical" viewBox="0 0 16 16">
                        <path d="M9.5 13a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0m0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0"/>
                    </svg>
                </Col>
            </Row>
        </Container>
    )
}

export default ReplyHeader
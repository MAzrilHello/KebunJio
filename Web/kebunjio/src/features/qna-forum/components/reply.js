import React from "react";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import ReplyHeader from "./reply-header";
import ReplyInsight from "./reply-insight";

const Reply = ({reply}) => {
    return(
        <div>
            <Container>
                <Row><ReplyHeader username={reply.username} time={reply.time}/></Row>
                <Row>
                    <p style={{fontSize:"0.9rem"}}>{reply.content}</p>
                </Row>
                <Row><ReplyInsight cur_like={reply.like} cur_dislike={reply.dislike} has_liked={reply.hasLiked} has_disliked={reply.hasDisliked}/></Row>
            </Container>
        </div>
    )
}

export default Reply;
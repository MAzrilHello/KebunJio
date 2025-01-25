import React from "react";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import PostHeader from "./post-header";
import PostInsight from "./post-insight";

function PostSneakPeak(){
    return(
        <div>
            <Container>
                <Row><PostHeader/></Row>
                <Row>
                    <b>Title</b>
                    <p>Content</p>
                </Row>
                <Row><PostInsight/></Row>
            </Container>
        </div>
    )
}

export default PostSneakPeak;
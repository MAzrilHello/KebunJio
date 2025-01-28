import React from "react";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import PostHeader from "./post-header";
import PostInsight from "./post-insight";
import Col from 'react-bootstrap/Col';
import Image from 'react-bootstrap/Image';
import placeholderPostImage from '../../../media/plant.jpg';


const FullPost = ({post}) => {
    return(
        <div>
            <div>
                <Container>
                    <Row><PostHeader username={post.username} time={post.time}/></Row>
                    <Row>
                        <b>{post.title}</b>
                        <div>
                            <span className="tag-class">{post.tag}</span>
                        </div>
                        {post.hasImage?(
                            <Image src={placeholderPostImage} className="post-image"></Image>
                        ):(
                            <div></div>
                        )}
                        <p>{post.content}</p>
                    </Row>
                    <Row>
                        <Col>
                            <PostInsight upvote={post.upvote} comment={post.comment} hasLiked={post.hasLiked}/>
                        </Col>
                    </Row>

                </Container>
            </div>
            <div>

            </div>
        </div>
    )
}

export default FullPost;
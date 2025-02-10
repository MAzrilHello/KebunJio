import React from "react";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import PostHeader from "./post-header";
import PostInsight from "./post-insight";
import { Link } from "react-router-dom";


function trimContent(content) {
    return content.length > 100 ? content.slice(0, 100) + " See more..." : content;
}

const PostSneakPeak = ({ post }) => {
    return (
        <div className="post-sneak-peak-class">
            <Container>
                <Row>
                    <PostHeader post={post} />
                </Row>

                <Row>
                    <Link to={`/forum/post/?id=${post.Id}`} state={{ post: post }}>
                        <b>{post.Title}</b>
                    </Link>
                    <div>
                        <span className="tag-class">{post.PostCategory}</span>
                    </div>
                    <div>
                        <p>{trimContent(post.Content)}</p>
                    </div>
                </Row>
                <Row>
                    <PostInsight upvote={post.upvote} comment={post.comment} />
                </Row>
            </Container>
        </div>
    );
};

export default PostSneakPeak;

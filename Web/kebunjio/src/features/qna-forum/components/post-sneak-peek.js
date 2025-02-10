import React from "react";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import PostHeader from "./post-header";
import PostInsight from "./post-insight";
import { Link } from "react-router-dom";

//need to trim down the post length
function trimContent(content) {
    if(content.length>100){
        var trimmed_content = content.slice(0,100) + " See more..."
        return trimmed_content
    }
    else{
        return content
    }
}

const PostSneakPeak = ({post}) => {    
    return(
        <div className="post-sneak-peak-class">
            <Container>
                <Row><PostHeader post={post}/></Row>
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
                <Row><PostInsight upvote={post.upvote} comment={post.comment}/></Row>
            </Container>
        </div>
    )
}

export default PostSneakPeak;
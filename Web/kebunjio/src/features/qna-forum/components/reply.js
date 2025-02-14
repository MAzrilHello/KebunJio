import React, {useState} from "react";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import ReplyHeader from "./reply-header";
import ReplyInsight from "./reply-insight";
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import { sanitizeInput } from '../../../service/sanitizeService';
import axios from "axios";

const Reply = ({userReply}) => {

    const [reply, setReply] = useState(userReply)

    const [isEditing, setIsEditing] = useState(false)

    const [editedContent, setEditedContent] = useState(reply.content);

    const API_BASE_URL = process.env.REACT_APP_API_LIVE_URL;

    const getDeleteReplyEndpoint = `${API_BASE_URL}/Forum/Post/Comment/${userReply.id}`;   

    const getEditReplyEndpoint = `${API_BASE_URL}/Forum/Post/Comment/${userReply.id}/Edit`;   

    const onClickEdit = () =>{
        setIsEditing(true)
    }

    const onClickDelete = () => {
        axios.delete(getDeleteReplyEndpoint)
        .then(response=>{
            console.log(response)
        })
        .catch(err=>{
            console.log(err)
        })
    }

    const onSubmitEdit = () => {
        setReply({ ...reply, content: editedContent });
        setIsEditing(false);

        axios.put(getEditReplyEndpoint,{
            commentContent:sanitizeInput(reply)
        })        
        .then(response=>{
            console.log(response)
        })
        .catch(err=>{
            console.log(err)
        })
    }

    const onChangeEdit = (e) => {
        setEditedContent(e.target.value)
    }

    return(
        <div>
            <Container>
                <Row><ReplyHeader username={reply.userId} time={reply.publishedDateTime} onDelete={onClickDelete} onEdit={onClickEdit}/></Row>
                <Row>
                    {isEditing?(
                        <Form>
                            <Form.Control
                                as="textarea"
                                value={editedContent}
                                onChange={onChangeEdit}/>
                            <Button onClick={onSubmitEdit}>Edit</Button>
                        </Form>
                    ):
                    (
                        <p style={{fontSize:"0.9rem"}}>{reply.commentContent}</p>

                    )}                
                    </Row>
                <Row><ReplyInsight cur_like={reply.likeCount} cur_dislike={reply.dislikeCount} has_liked={false} has_disliked={false}/></Row>
            </Container>
        </div>
    )
}

export default Reply;
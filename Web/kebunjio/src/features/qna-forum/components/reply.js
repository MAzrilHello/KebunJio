import React, {useState} from "react";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import ReplyHeader from "./reply-header";
import ReplyInsight from "./reply-insight";
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import { sanitizeInput } from '../../../service/sanitizeService';

const Reply = ({userReply}) => {

    const [reply, setReply] = useState(userReply)

    const [isEditing, setIsEditing] = useState(false)

    const [editedContent, setEditedContent] = useState(reply.content);
    
    const onClickEdit = () =>{
        setIsEditing(true)
    }

    const onClickDelete = () => {
        alert("Delete reply")
        const requestData = {
            Id: reply.id
        }
        console.log(JSON.stringify(requestData))
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

    const onSubmitEdit = () => {
        setReply({ ...reply, content: editedContent });
        setIsEditing(false);

        const requestData = {
            Id: reply.id,
            Content: sanitizeInput(reply.content),
            PublishedDateTime: new Date(),
          }
          console.log(JSON.stringify(requestData))
          alert("Updated reply!")
          /*API Implementation
          fetch('https://', {
                method: 'PUT',
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

    const onChangeEdit = (e) => {
        setEditedContent(e.target.value)
    }

    return(
        <div>
            <Container>
                <Row><ReplyHeader username={reply.username} time={reply.time} onDelete={onClickDelete} onEdit={onClickEdit}/></Row>
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
                        <p style={{fontSize:"0.9rem"}}>{reply.content}</p>

                    )}                
                    </Row>
                <Row><ReplyInsight cur_like={reply.like} cur_dislike={reply.dislike} has_liked={reply.hasLiked} has_disliked={reply.hasDisliked}/></Row>
            </Container>
        </div>
    )
}

export default Reply;
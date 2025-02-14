import React, { useState, useEffect } from 'react';
import Appbar from '../../../components/Appbar'
import MenuSidebar from '../components/menu-sidebar'
import '../styling/forum-page.css'
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import { sanitizeInput } from '../../../service/sanitizeService';
import { useParams } from 'react-router-dom';
import axios from 'axios';

function ForumEditPost() {

  const API_BASE_URL = process.env.REACT_APP_API_LIVE_URL;

  const { id } = useParams();

  const editPostEndpoint = `${API_BASE_URL}/Forum/Post/${id}`

  const [formData, setFormData] = useState({
    category: "",
    title: "",
    question: "",
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
          axios.get(editPostEndpoint)
          .then(response=>{
              console.log(response.data);
              setFormData({
                category: response.data.post.postCategory || "",
                title: response.data.post.title || "",
                question: response.data.post.content || "",
              });
          })
          .catch(error => {
              console.error("Error fetching data:", error)
          })

      } catch (error) {
          console.error("Error fetching data", error)
      }
  };
  fetchData();
}, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleFileChange = (e) => {
    setFormData({ ...formData, image: e.target.files[0] });
  };

  const handleSubmit = (e) => {
    e.preventDefault()
    axios.put((editPostEndpoint),{
      title: sanitizeInput(formData.title),
      content: sanitizeInput(formData.question),
      postCategory: formData.category})
    .then(response=>{
      if(response==201){
        console.log("Edit post successfully")
      }
      else{
        console.log("Failed to create post")
      }
    })
    .catch(err=>{
      console.log(err)
    })
  }

  const resetPost = (e) => {
    setFormData({ category: '', title: '', question: '', image: null })
  }

  return (
    <div>
      <Appbar/>
      <div className="page-container">
        <div className="menu-sidebar">
            <MenuSidebar/>
        </div>
        <div className="main-content">
          <p className="page-header">Edit post</p>
          <Form onSubmit={handleSubmit}>
            <Form.Group controlId="category">
              <Form.Label>Select category</Form.Label>
              <Form.Select
                name="category"
                value={formData.category}
                onChange={handleInputChange}
                required
                className="text-area"
              >
                <option value="">Select category</option>
                <option value="herb">Herb</option>
                <option value="leafy-vegetable">Leafy vegetables</option>
                <option value="fruit-vegetable">Fruit vegetables</option>
                <option value="fruit">Fruit</option>
                <option value="tuber-roots">Tuber and Roots</option>
                <option value="vines-climbers">Vines and Climbers</option>
                <option value="others">Others</option>
              </Form.Select>
            </Form.Group>

            <Form.Group controlId="title">
              <Form.Label>Title</Form.Label>
              <Form.Control
                as="textarea"
                placeholder="Insert title here"
                name="title"
                value={formData.title}
                onChange={handleInputChange}
                required
                className="text-area"
              />
            </Form.Group>

            <Form.Group controlId="question">
              <Form.Label>Question</Form.Label>
              <Form.Control
                as="textarea"
                placeholder="Type your question here"
                name="question"
                value={formData.question}
                onChange={handleInputChange}
                required
                className="text-area"
              />

            </Form.Group>

            <Form.Group controlId="image">
              <Form.Label>Upload image</Form.Label>
              <Form.Control
                type="file"
                name="image"
                onChange={handleFileChange}
                className="text-area"
              />
            </Form.Group>

            <div style={{marginTop:"32px"}}>
              <Button variant="secondary" type="reset" onClick={resetPost}>
                Cancel
              </Button>
              <Button style={{marginLeft:"8px"}} variant="primary" type="submit">
                Post
              </Button>
            </div>
          </Form>
        </div>
      </div>
    </div>
  );
}

export default ForumEditPost;
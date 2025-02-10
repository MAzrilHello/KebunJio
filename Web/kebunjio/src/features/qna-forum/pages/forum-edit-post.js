import React, { useState, useEffect } from 'react';
import Appbar from '../../../components/Appbar';
import MenuSidebar from '../components/menu-sidebar';
import '../styling/forum-page.css';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import { useLocation } from "react-router-dom";
import { useAuth } from "../../../context/AuthContext";

function ForumEditPost() {
  const { authUser } = useAuth();
  const location = useLocation();
  const post = location.state?.post;

  const [formData, setFormData] = useState({
    category: "",
    title: "",
    question: "",
    avatarUrl: authUser?.avatarUrl || "/default-avatar.png",
  });

  useEffect(() => {
    if (post) {
      setFormData({
        category: post.PostCategory || "",
        title: post.Title || "",
        question: post.Content || "",
        avatarUrl: post.avatarUrl || authUser?.avatarUrl || "/default-avatar.png",
      });
    }
  }, [post, authUser]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleFileChange = (e) => {
    setFormData({ ...formData, image: e.target.files[0] });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (formData.category !== '' && formData.title !== '' && formData.question !== '') {
      const requestData = {
        Id: post.Id,
        Title: formData.title,
        Content: formData.question,
        PostCategory: formData.category,
        PublishedDateTime: new Date(),
        UserId: post.UserId,
      };
      console.log(requestData);
      alert("Updated post!");
    }
  };

  const resetPost = () => {
    setFormData({ category: '', title: '', question: '', image: null });
  };

  return (
      <div>
        <Appbar avatarUrl={formData.avatarUrl} />
        <div className="page-container">
          <div className="menu-sidebar">
            <MenuSidebar />
          </div>
          <div className="main-content">
            <p className="page-header">Edit post</p>
            <div className="edit-post-avatar-container">
              <span>{authUser?.username || "Unknown User"}</span>
            </div>
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

              <div style={{ marginTop: "32px" }}>
                <Button variant="secondary" type="reset" onClick={resetPost}>
                  Cancel
                </Button>
                <Button style={{ marginLeft: "8px" }} variant="primary" type="submit">
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

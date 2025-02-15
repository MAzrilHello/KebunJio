import React, { useState } from 'react';
import axios from 'axios';
import './login-signup.css';
import { useNavigate } from "react-router-dom";
import { sanitizeInput } from '../../service/sanitizeService';

const SignUpPage = () => {
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [contactPhone, setContactPhone] = useState('');
    const [error, setError] = useState('');

    const API_BASE_URL = process.env.REACT_APP_API_LIVE_URL;

    const getSignupEndpoint = `${API_BASE_URL}/users/signup`;

    const navigate = useNavigate()

    const handleSubmit = (e) => {
        e.preventDefault();

        if (password !== confirmPassword) {
            setError('Password Do Not Match!');
            return;
        }


        axios.post(getSignupEndpoint, {
            email: sanitizeInput(email),
            username: sanitizeInput(username),
            password: sanitizeInput(password),
            confirmPassword: sanitizeInput(confirmPassword),
            contactPhone: sanitizeInput(contactPhone)

        })
            .then(response => {
                if (response.status === 201) {
                    navigate(`/login`)
                }
            })
            .catch(err => {
                setError('Registration Failed');
            });
    };

    return (
        <div className="signup-container">
            <div className="signup-form">
                <div className="login-logo">
                    <img src="./logo_appbar.png"/>
                </div>
                <h2>Sign up</h2>
                <form onSubmit={handleSubmit}>
                    <input
                        type="email"
                        placeholder="Enter email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                    <input
                        type="text"
                        placeholder="Enter username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                    <input
                        type="text"
                        placeholder="Enter contact number"
                        value={contactPhone}
                        onChange={(e) => setContactPhone(e.target.value)}
                        required
                    />
                    <input
                        type="password"
                        placeholder="Enter password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    <input
                        type="password"
                        placeholder="Confirm password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                    />
                    <button type="submit" className="signup-btn">Sign up</button>
                </form>
                {error && <p className="error-message">{error}</p>}
                <p className="login-link">
                    Have an account? <a href="/login">Login</a>
                </p>
            </div>
        </div>
    );
};

export default SignUpPage;

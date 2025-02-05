import React, { useState } from 'react';
import axios from 'axios';
import './login-signup.css';

const LoginPage = () => {
    const [emailOrUsername, setEmailOrUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();

        axios.post('/login', {
            emailOrUsername,
            password
        })
            .then(response => {
                if (response.status === 200) {
                    window.location.href = '/userProfile';
                }
            })
            .catch(err => {
                setError('Invalid username or password');
            });
    };

    return (
        <div className="login-container">
            <div className="login-logo">
                <img src="/logo.jpg" alt="Logo" />
            </div>
            <div className="login-form">
                <h2>Log in</h2>
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        placeholder="Enter email or user name"
                        value={emailOrUsername}
                        onChange={(e) => setEmailOrUsername(e.target.value)}
                        required
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    <button type="submit" className="login-btn">Log in</button>
                </form>
                {error && <p className="error-message">{error}</p>}
                <p className="sign-up-link">
                    Don't have an account? <a href="/signup">Sign up</a>
                </p>
            </div>
        </div>
    );
};

export default LoginPage;

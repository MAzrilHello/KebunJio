import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './login-signup.css';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from "react-router-dom";
import { sanitizeInput } from '../../service/sanitizeService';

const LoginPage = () => {
    const [emailOrUsername, setEmailOrUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const {setAuthUser, setIsLoggedIn, setIsAdmin} = useAuth();

    const API_BASE_URL = process.env.REACT_APP_API_LIVE_URL;

    const getLoginEndpoint = `${API_BASE_URL}/users/login`;

    const navigate = useNavigate()

    const handleSubmit = (e) => {
        e.preventDefault();

        axios.post(getLoginEndpoint, {
            emailOrUsername: emailOrUsername,
            password: password
        },{withCredentials:true})

            .then(response => {
                console.log(response.status)
                if (response.status === 200) {
                    const user = response.data;
                    setAuthUser(user)
                    setIsLoggedIn(true)
                    if(user.admin===true){
                        setIsAdmin(true)
                    }
                    else{
                        setIsAdmin(false)
                    }
                    navigate(`/forum`)

                }
            })
            .catch(err => {
                setError('Invalid username or password');
            });
    };

    return (
        <div className="login-container">
            <div className="login-form">
                <div className="login-logo">
                    <img src="./logo_appbar.png"/>
                </div>
                <h2>Log in</h2>
                <form onSubmit={handleSubmit}>
                    <input
                            id="username"
                            type="text"
                            placeholder="Enter email or user name"
                            value={emailOrUsername}
                            onChange={(e) => setEmailOrUsername(e.target.value)}
                            required
                        />
                    <input
                        id="password"
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

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './login-signup.css';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from "react-router-dom";

const LoginPage = () => {
    const [emailOrUsername, setEmailOrUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [users, setUsers] = useState([]);
    const {setAuthUser, setIsLoggedIn, setIsAdmin} = useAuth();

    const navigate = useNavigate()

    //temp function, will delete once backend is connected
    useEffect(()=>{
        async function fetchData(){
            const usersRes = await fetch("/dummy-data/user-login-data.json")
            const users = await usersRes.json()

            setUsers(users)
        }
        fetchData()

    },[])

    const handleSubmit = (e) => {
        e.preventDefault();

        const user = users.find(user =>
            (user.Username === emailOrUsername || user.Username === emailOrUsername) &&
            user.Password === password
        );

        if(user){
            setAuthUser(user)
            setIsLoggedIn(true)
            if(user.Username==="Admin"){
                setIsAdmin(true)
            }
            else{
                setIsAdmin(false)
            }
            navigate(`/forum`)
        }
        else{
            alert("Incorrect password or username")
        }

        //Ruihan's code, do not delete
        /*
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
            });*/
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

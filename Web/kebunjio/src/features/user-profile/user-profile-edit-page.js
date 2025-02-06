import React, { useState } from 'react';
import axios from 'axios';
import { useAuth } from '../../context/AuthContext';
import Appbar from '../../components/Appbar';

const UserProfileEditPage = () => {
    const {authUser} = useAuth()
    const [username, setUsername] = useState(authUser.Username);
    const [email, setEmail] = useState(authUser.Email);
    const [phoneNumber, setPhoneNumber] = useState(authUser.PhoneNumber);

    const handleSubmit = (e) => {
        e.preventDefault();

        alert("Edit success")

        /*Ruihan's code, do not delete
        axios.post('/userProfile/update', { username, email, phoneNumber })
            .then(response => {
                alert('Profile updated!');
            })
            .catch(error => {
                alert('Error updating profile');
            });
        */
    }

    return (
        <div>
            <Appbar/>
                <form onSubmit={handleSubmit}>
                <label>
                    Username:
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </label>
                <label>
                    Email:
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </label>
                <label>
                    Phone Number:
                    <input
                        type="text"
                        value={phoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)}
                    />
                </label>
                <button type="submit">Update Profile</button>
            </form>
        </div>
    );
}

export default UserProfileEditPage;

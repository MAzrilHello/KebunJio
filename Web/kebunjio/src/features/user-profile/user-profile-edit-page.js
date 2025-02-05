import React, { useState } from 'react';
import axios from 'axios';

const UserProfileEditPage = ({ user }) => {
    const [username, setUsername] = useState(user.username);
    const [email, setEmail] = useState(user.email);
    const [phoneNumber, setPhoneNumber] = useState(user.phoneNumber);

    const handleSubmit = (e) => {
        e.preventDefault();

        axios.post('/userProfile/update', { username, email, phoneNumber })
            .then(response => {
                alert('Profile updated!');
            })
            .catch(error => {
                alert('Error updating profile');
            });
    }

    return (
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
    );
}

export default UserProfileEditPage;

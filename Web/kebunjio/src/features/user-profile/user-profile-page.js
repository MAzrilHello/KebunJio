import React, { useEffect, useState } from 'react';
import axios from 'axios';


const UserProfilePage = () => {
    const [userProfile, setUserProfile] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        axios.get('/userProfile')
            .then(response => {
                setUserProfile(response.data);
            })
            .catch(err => {
                setError("Error fetching user profile.");
            });
    }, []);

    if (error) return <div>{error}</div>;

    if (!userProfile) {
        return <div>Loading...</div>;
    }

    return (
        <div className="user-profile">
            <div className="profile-header">
                <h1>{userProfile.user.username}'s Profile</h1>
                <button>Edit Profile</button>
            </div>

        </div>
    );
}

export default UserProfilePage;

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth } from '../../context/AuthContext';
import Appbar from '../../components/Appbar';
import { Link } from 'react-router-dom';

const UserProfilePage = () => {
    const [userProfile, setUserProfile] = useState(null);
    const [error, setError] = useState(null);
    const {authUser} = useAuth()

    useEffect(() => {
        /*Ruihan's code, do not delete
        axios.get('/userProfile')
            .then(response => {
                setUserProfile(response.data);
            })
            .catch(err => {
                setError("Error fetching user profile.");
            });*/
    }, []);

    /*
    if (error) return <div>{error}</div>;

    if (!userProfile) {
        return <div>Loading...</div>;
    }*/

    return (
        <div className="user-profile">
            <Appbar/>
            <div className="profile-header">
                {/*Ruihan's original code, do not delete
                    <h1>{userProfile.user.username}'s Profile</h1>
                */}
                <h1>{authUser.Username}'s Profile</h1>
                <Link to={"/user/profile/edit"}><button>Edit Profile</button></Link>
            </div>

        </div>
    );
}

export default UserProfilePage;

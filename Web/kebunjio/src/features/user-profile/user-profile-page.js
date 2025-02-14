import React, { useEffect, useState , useRef} from 'react';
import axios from 'axios';
import { useAuth } from '../../context/AuthContext';
import Button from 'react-bootstrap/Button'
import Table  from "react-bootstrap/Table";
import Appbar from '../../components/Appbar';
import "./user-profile-style.css"
import Card from 'react-bootstrap/Card';
import { useNavigate } from 'react-router-dom';


const UserProfilePage = () => {
    // set the auth user
    const { authUser, setAuthUser } = useAuth();

    // set the user profile info
    const [username, setUsername] = useState(authUser?.username || "");
    const [email, setEmail] = useState(authUser?.email || "");
    const [phone, setPhoneNumber] = useState(authUser?.phoneNumber || "");

    // set if edit
    const [isEdit, setIsEdit] = useState(false);
    const [plants, setPlants] = useState([]);

    const hasFetched = useRef(false);
    const [loading, setLoading] = useState(true);

    const [userInfo,setUserInfo] = useState({
        totalPlant:authUser?.totalPlant || 0,
        totalHarvested:authUser?.totalHarvested || 0,
        totalType:authUser?.totalType || 0
    });

    useEffect(() => {
        async function fetchUserProfile() {
            try {
                console.log("Fetching user profile...");
                // need change to 34.124.209.141
                const response = await axios.get("http://localhost:8080/api/userProfile", { withCredentials: true });

                if (response.data) {
                    console.log("User Profile Fetched:", response.data);
                    // to store the user data, make the structure more clear.
                    const userData = {
                        id: response.data.user.id,
                        username: response.data.user.username,
                        email: response.data.user.email,
                        phoneNumber: response.data.user.phoneNumber,
                        totalPlant: response.data.totalPlant || 0,
                        totalHarvested: response.data.totalHarvested || 0,
                        totalType: response.data.totalType || 0
                    };

                    localStorage.setItem("authUser", JSON.stringify(userData));

                    setAuthUser(userData);

                    setUserInfo({
                        totalPlant: response.data.totalPlant || 0,
                        totalHarvested: response.data.totalHarvested || 0,
                        totalType: response.data.totalType || 0
                    });

                    setUsername(response.data.user.username);
                    setEmail(response.data.user.email);
                    setPhoneNumber(response.data.user.phoneNumber);
                }
            } catch (error) {
                console.error("Error fetching user profile:", error);
            } finally {
                setLoading(false);
            }
        }

        fetchUserProfile();
    }, [setAuthUser]);

    const onEdit = async () => {
        if (isEdit) {
            try {
                // need change to 34.124.209.141
                const response = await axios.put(
                    "http://localhost:8080/api/userProfile/update", // need change to remote ip
                    { username, email, phoneNumber: phone },
                    {
                        withCredentials: true,
                        headers: { "Content-Type": "application/json" }
                    }
                );


                if (response.status === 200) {
                    const updatedUser = response.data;
                    // need confirm the response.data.structure
                    setAuthUser(prevUser => ({
                        ...prevUser,
                        username: updatedUser.username,
                        email: updatedUser.email,
                        phoneNumber: updatedUser.phoneNumber
                    }));
                    localStorage.setItem("authUser",authUser);
                    alert("Profile updated successfully!");
                }
            } catch (error) {
                console.error("Failed to update profile:", error);
                alert("Profile update failed.");
            }
        }
        setIsEdit(prevState => !prevState);
    };

    const handleInputChange = (event) => {
        const { id, value } = event.target;
        if (id === "username") {
            setUsername(value);
        } else if (id === "email") {
            setEmail(value);
        } else if (id === "phone") {
            setPhoneNumber(value);
        }
    };

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <div className="user-profile-page">
            {console.log("Rendering - AuthUser State:", authUser)}
            <Appbar/>
            <div className="user-profile">
                <div className="page-header">
                    <p>User profile</p>
                </div>
                <div className="user-profile-container">
                    <Card className="user-profile-card">
                        <div>
                            {isEdit?(
                                <div>
                                    <label htmlFor='username'>Username:</label>
                                    <input
                                        type="text"
                                        id="username"
                                        value={username}
                                        required
                                        onChange={handleInputChange}
                                    />
                                </div>
                            ):(
                                <p>Username: {authUser?.username || "N/A"}</p>
                            )}
                        </div>
                        <div>
                            {isEdit?(
                                <div>
                                    <label htmlFor='email'>Email:</label>
                                    <input
                                        type="email"
                                        id="email"
                                        value={email}
                                        required
                                        onChange={handleInputChange}

                                    />
                                </div>
                            ):(
                                <p>Email:{authUser?.email || "N/A"}</p>
                            )}
                        </div>
                        <div>
                            {isEdit?(
                                <div>
                                    <div>
                                        <label htmlFor='phone'>Phone:</label>
                                        <input
                                            type="tel"
                                            id="phone"
                                            value={phone}
                                            required
                                            onChange={handleInputChange}

                                        />
                                    </div>
                                </div>
                            ):(
                                <p>Phone: {authUser?.phoneNumber || "N/A"}</p>
                            )}
                        </div>
                        <Button onClick={onEdit} style={{backgroundColor:"white",color:"#002E14"}}>{isEdit ? "Save" : "Edit"}</Button>
                    </Card>
                </div>
            </div>
            <div className="user-summary">
                <div className="page-header">
                    User Summary
                </div>
                <div className="summary-group">
                    <Card className="summary-card">
                        <div className="icon-holder">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" class="bi bi-flower1" viewBox="0 0 16 16">
                                <path d="M6.174 1.184a2 2 0 0 1 3.652 0A2 2 0 0 1 12.99 3.01a2 2 0 0 1 1.826 3.164 2 2 0 0 1 0 3.652 2 2 0 0 1-1.826 3.164 2 2 0 0 1-3.164 1.826 2 2 0 0 1-3.652 0A2 2 0 0 1 3.01 12.99a2 2 0 0 1-1.826-3.164 2 2 0 0 1 0-3.652A2 2 0 0 1 3.01 3.01a2 2 0 0 1 3.164-1.826M8 1a1 1 0 0 0-.998 1.03l.01.091q.017.116.054.296c.049.241.122.542.213.887.182.688.428 1.513.676 2.314L8 5.762l.045-.144c.248-.8.494-1.626.676-2.314.091-.345.164-.646.213-.887a5 5 0 0 0 .064-.386L9 2a1 1 0 0 0-1-1M2 9l.03-.002.091-.01a5 5 0 0 0 .296-.054c.241-.049.542-.122.887-.213a61 61 0 0 0 2.314-.676L5.762 8l-.144-.045a61 61 0 0 0-2.314-.676 17 17 0 0 0-.887-.213 5 5 0 0 0-.386-.064L2 7a1 1 0 1 0 0 2m7 5-.002-.03a5 5 0 0 0-.064-.386 16 16 0 0 0-.213-.888 61 61 0 0 0-.676-2.314L8 10.238l-.045.144c-.248.8-.494 1.626-.676 2.314-.091.345-.164.646-.213.887a5 5 0 0 0-.064.386L7 14a1 1 0 1 0 2 0m-5.696-2.134.025-.017a5 5 0 0 0 .303-.248c.184-.164.408-.377.661-.629A61 61 0 0 0 5.96 9.23l.103-.111-.147.033a61 61 0 0 0-2.343.572c-.344.093-.64.18-.874.258a5 5 0 0 0-.367.138l-.027.014a1 1 0 1 0 1 1.732zM4.5 14.062a1 1 0 0 0 1.366-.366l.014-.027q.014-.03.036-.084a5 5 0 0 0 .102-.283c.078-.233.165-.53.258-.874a61 61 0 0 0 .572-2.343l.033-.147-.11.102a61 61 0 0 0-1.743 1.667 17 17 0 0 0-.629.66 5 5 0 0 0-.248.304l-.017.025a1 1 0 0 0 .366 1.366m9.196-8.196a1 1 0 0 0-1-1.732l-.025.017a5 5 0 0 0-.303.248 17 17 0 0 0-.661.629A61 61 0 0 0 10.04 6.77l-.102.111.147-.033a61 61 0 0 0 2.342-.572c.345-.093.642-.18.875-.258a5 5 0 0 0 .367-.138zM11.5 1.938a1 1 0 0 0-1.366.366l-.014.027q-.014.03-.036.084a5 5 0 0 0-.102.283c-.078.233-.165.53-.258.875a61 61 0 0 0-.572 2.342l-.033.147.11-.102a61 61 0 0 0 1.743-1.667c.252-.253.465-.477.629-.66a5 5 0 0 0 .248-.304l.017-.025a1 1 0 0 0-.366-1.366M14 9a1 1 0 0 0 0-2l-.03.002a5 5 0 0 0-.386.064c-.242.049-.543.122-.888.213-.688.182-1.513.428-2.314.676L10.238 8l.144.045c.8.248 1.626.494 2.314.676.345.091.646.164.887.213a5 5 0 0 0 .386.064zM1.938 4.5a1 1 0 0 0 .393 1.38l.084.035q.108.045.283.103c.233.078.53.165.874.258a61 61 0 0 0 2.343.572l.147.033-.103-.111a61 61 0 0 0-1.666-1.742 17 17 0 0 0-.66-.629 5 5 0 0 0-.304-.248l-.025-.017a1 1 0 0 0-1.366.366m2.196-1.196.017.025a5 5 0 0 0 .248.303c.164.184.377.408.629.661A61 61 0 0 0 6.77 5.96l.111.102-.033-.147a61 61 0 0 0-.572-2.342c-.093-.345-.18-.642-.258-.875a5 5 0 0 0-.138-.367l-.014-.027a1 1 0 1 0-1.732 1m9.928 8.196a1 1 0 0 0-.366-1.366l-.027-.014a5 5 0 0 0-.367-.138c-.233-.078-.53-.165-.875-.258a61 61 0 0 0-2.342-.572l-.147-.033.102.111a61 61 0 0 0 1.667 1.742c.253.252.477.465.66.629a5 5 0 0 0 .304.248l.025.017a1 1 0 0 0 1.366-.366m-3.928 2.196a1 1 0 0 0 1.732-1l-.017-.025a5 5 0 0 0-.248-.303 17 17 0 0 0-.629-.661A61 61 0 0 0 9.23 10.04l-.111-.102.033.147a61 61 0 0 0 .572 2.342c.093.345.18.642.258.875a5 5 0 0 0 .138.367zM8 9.5a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3"/>
                            </svg>
                        </div>
                        <div>
                            <p className="summary-title">Total number of plants planted</p>
                            <p className="summary-number">{userInfo.totalPlant}</p>
                        </div>
                    </Card>
                    <Card className="summary-card">
                        <div className="icon-holder">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" class="bi bi-bucket-fill" viewBox="0 0 16 16">
                                <path d="M2.522 5H2a.5.5 0 0 0-.494.574l1.372 9.149A1.5 1.5 0 0 0 4.36 16h7.278a1.5 1.5 0 0 0 1.483-1.277l1.373-9.149A.5.5 0 0 0 14 5h-.522A5.5 5.5 0 0 0 2.522 5m1.005 0a4.5 4.5 0 0 1 8.945 0z"/>
                            </svg>
                        </div>
                        <div>
                            <p className="summary-title">Total number of plants harvested</p>
                            <p className="summary-number">{userInfo.totalHarvested}</p>
                        </div>
                    </Card>
                    <Card className="summary-card">
                        <div className="icon-holder">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" class="bi bi-123" viewBox="0 0 16 16">
                                <path d="M2.873 11.297V4.142H1.699L0 5.379v1.137l1.64-1.18h.06v5.961zm3.213-5.09v-.063c0-.618.44-1.169 1.196-1.169.676 0 1.174.44 1.174 1.106 0 .624-.42 1.101-.807 1.526L4.99 10.553v.744h4.78v-.99H6.643v-.069L8.41 8.252c.65-.724 1.237-1.332 1.237-2.27C9.646 4.849 8.723 4 7.308 4c-1.573 0-2.36 1.064-2.36 2.15v.057zm6.559 1.883h.786c.823 0 1.374.481 1.379 1.179.01.707-.55 1.216-1.421 1.21-.77-.005-1.326-.419-1.379-.953h-1.095c.042 1.053.938 1.918 2.464 1.918 1.478 0 2.642-.839 2.62-2.144-.02-1.143-.922-1.651-1.551-1.714v-.063c.535-.09 1.347-.66 1.326-1.678-.026-1.053-.933-1.855-2.359-1.845-1.5.005-2.317.88-2.348 1.898h1.116c.032-.498.498-.944 1.206-.944.703 0 1.206.435 1.206 1.07.005.64-.504 1.106-1.2 1.106h-.75z"/>
                            </svg>
                        </div>
                        <div>
                            <p className="summary-title">Total number of plants harvested</p>
                            <p className="summary-number">{userInfo.totalHarvested}</p>
                        </div>
                    </Card>
                </div>
            </div>
            <div className="user-history">
                <div className="page-header">
                    User History
                </div>
                <div>
                    <Table striped bordered hover>
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>Plant species</th>
                            <th>Plant type</th>
                            <th>Date planted</th>
                            <th>Status</th>
                            <th>Disease</th>
                            <th>Harvested</th>
                        </tr>
                        </thead>
                        <tbody>
                        {plants.map((plant) => (
                            <tr key={plant.Id}>
                                <td>{plant.Id}</td>
                                <td>{plant.Name}</td>
                                <td>{plant.EdiblePlantSpecies}</td>
                                <td>{plant.PlantedDate}</td>
                                <td>{plant.PlantHealth}</td>
                                <td>{plant.Disease || "None"}</td>
                                <td>{plant.Harvested ? "Yes" : "No"}</td>
                            </tr>
                        ))}
                        </tbody>
                    </Table>
                </div>
            </div>
            {/*<div className="user-profile">
            <Appbar/>
            <div className="profile-header">
                    <h1>{userProfile.user.username}'s Profile</h1>
                                    <h1>{authUser.Username}'s Profile</h1>
                <Link to={"/user/profile/edit"}><button>Edit Profile</button></Link>
            </div>

        </div>*/}
        </div>


    );
}

export default UserProfilePage;

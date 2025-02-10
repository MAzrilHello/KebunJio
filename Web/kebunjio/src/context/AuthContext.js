import React, { useState, useEffect, useContext } from 'react';

const AuthContext = React.createContext();

export function useAuth() {
    return useContext(AuthContext);
}

export function AuthProvider({ children }) {
    const [authUser, setAuthUser] = useState(() => {
        const storedUser = localStorage.getItem("authUser");
        return storedUser ? JSON.parse(storedUser) : null;
    });

    const [isLoggedIn, setIsLoggedIn] = useState(!!authUser);
    const [isAdmin, setIsAdmin] = useState(authUser?.isAdmin || false);

    useEffect(() => {
        if (authUser) {
            localStorage.setItem("authUser", JSON.stringify(authUser));
            setIsLoggedIn(true);
        } else {
            localStorage.removeItem("authUser");
            setIsLoggedIn(false);
        }
    }, [authUser]);

    const value = {
        authUser,
        setAuthUser,
        isLoggedIn,
        setIsLoggedIn,
        isAdmin,
        setIsAdmin
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

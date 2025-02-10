import { Navigate } from "react-router-dom"
import { useAuth } from "../context/AuthContext";

const ProtectedRoute = ({ element }) => {
    const { isLoggedIn } = useAuth()

    return isLoggedIn ? element : <Navigate to="/login" />
};

export default ProtectedRoute;
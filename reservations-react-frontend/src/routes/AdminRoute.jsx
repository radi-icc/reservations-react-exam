import { Navigate } from "react-router-dom";
import useAuth from "../hooks/useAuth";
import Loader from "../components/common/Loader";

const AdminRoute = ({ children }) => {
  const { user, isAdmin, authLoading } = useAuth();

  if (authLoading) return <Loader />;
  if (!user) return <Navigate to="/login" replace />;

  return isAdmin ? children : <Navigate to="/" replace />;
};

export default AdminRoute;

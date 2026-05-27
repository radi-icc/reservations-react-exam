import { Navigate } from "react-router-dom";
import useAuth from "../hooks/useAuth";
import Loader from "../components/common/Loader";

const AdminRoute = ({ children }) => {
  const { user, isAdmin, isProducer, authLoading } = useAuth();

  if (authLoading) return <Loader />;
  if (!user) return <Navigate to="/login" replace />;

  return isAdmin || isProducer ? children : <Navigate to="/" replace />;
};

export default AdminRoute;

import { Navigate, useLocation } from "react-router-dom";
import useAuth from "../hooks/useAuth";
import Loader from "../components/common/Loader";

const PrivateRoute = ({ children }) => {
  const { user, authLoading } = useAuth();
  const location = useLocation();

  if (authLoading) return <Loader />;
  return user ? children : <Navigate to="/login" replace state={{ from: location }} />;
};

export default PrivateRoute;

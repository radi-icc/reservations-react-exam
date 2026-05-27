import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import Button from "../../components/common/Button";
import Input from "../../components/common/Input";
import useAuth from "../../hooks/useAuth";
import { getErrorMessage } from "../../utils/errorUtils";

const Login = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useAuth();
  const [form, setForm] = useState({ usernameOrEmail: "", password: "" });
  const [loading, setLoading] = useState(false);

  const handleChange = (event) => setForm((prev) => ({ ...prev, [event.target.name]: event.target.value }));

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      setLoading(true);
      await login(form);
      toast.success("Login successful");
      navigate(location.state?.from?.pathname || "/", { replace: true });
    } catch (error) {
      toast.error(getErrorMessage(error, "Invalid username/email or password"));
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <span className="eyebrow">Welcome back</span>
      <h1>Login</h1>
      <form onSubmit={handleSubmit} className="auth-form">
        <Input label="Username or Email" name="usernameOrEmail" value={form.usernameOrEmail} onChange={handleChange} required />
        <Input label="Password" name="password" type="password" value={form.password} onChange={handleChange} required />
        <Button type="submit" disabled={loading} className="btn-full">{loading ? "Logging in..." : "Login"}</Button>
      </form>
      <p className="auth-link">No account? <Link to="/register">Create one</Link></p>
    </>
  );
};

export default Login;

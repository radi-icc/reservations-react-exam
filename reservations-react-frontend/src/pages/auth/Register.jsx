import { useMemo, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import Button from "../../components/common/Button";
import Input from "../../components/common/Input";
import useAuth from "../../hooks/useAuth";
import { getErrorMessage } from "../../utils/errorUtils";
import { isEmail, isStrongPassword } from "../../utils/validators";

const Register = () => {
  const navigate = useNavigate();
  const { register } = useAuth();
  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
    firstname: "",
    lastname: "",
    language: "en",
  });
  const [loading, setLoading] = useState(false);

  const errors = useMemo(() => ({
    email: form.email && !isEmail(form.email) ? "Enter a valid email address" : "",
    password: form.password && !isStrongPassword(form.password) ? "Min 6 chars, one uppercase and one special character" : "",
    confirmPassword: form.confirmPassword && form.password !== form.confirmPassword ? "Passwords do not match" : "",
  }), [form]);

  const isInvalid = Object.values(errors).some(Boolean) || !form.username || !form.email || !form.password || !form.confirmPassword;

  const handleChange = (event) => setForm((prev) => ({ ...prev, [event.target.name]: event.target.value }));

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (isInvalid) {
      toast.error("Please fix the form errors first");
      return;
    }

    try {
      setLoading(true);
      await register(form);
      toast.success("Account created. Please login.");
      navigate("/login");
    } catch (error) {
      toast.error(getErrorMessage(error, "Registration failed"));
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <span className="eyebrow">Create member account</span>
      <h1>Register</h1>
      <form onSubmit={handleSubmit} className="auth-form two-column-form">
        <Input label="Username" name="username" value={form.username} onChange={handleChange} required />
        <Input label="Email" name="email" type="email" value={form.email} onChange={handleChange} error={errors.email} required />
        <Input label="First Name" name="firstname" value={form.firstname} onChange={handleChange} required />
        <Input label="Last Name" name="lastname" value={form.lastname} onChange={handleChange} required />
        <div className="form-group">
          <label className="form-label">Language</label>
          <select className="form-input" name="language" value={form.language} onChange={handleChange} required>
            <option value="en">English</option>
            <option value="fr">French</option>
            <option value="nl">Dutch</option>
          </select>
        </div>
        <Input label="Password" name="password" type="password" value={form.password} onChange={handleChange} error={errors.password} required />
        <Input label="Confirm Password" name="confirmPassword" type="password" value={form.confirmPassword} onChange={handleChange} error={errors.confirmPassword} required />
        <Button type="submit" disabled={loading || isInvalid} className="btn-full form-wide">{loading ? "Creating..." : "Create account"}</Button>
      </form>
      <p className="auth-link">Already have an account? <Link to="/login">Login</Link></p>
    </>
  );
};

export default Register;

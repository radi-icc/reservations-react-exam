import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import Button from "../../components/common/Button";
import Input from "../../components/common/Input";
import useAuth from "../../hooks/useAuth";
import { updateUser } from "../../api/usersApi";
import { getErrorMessage } from "../../utils/errorUtils";

const Profile = () => {
  const { user, setUser, loadProfile } = useAuth();
  const [form, setForm] = useState({ username: "", email: "", firstname: "", lastname: "", language: "en", enabled: true, roleId: "" });
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (user) {
      setForm({
        username: user.username || "",
        email: user.email || "",
        firstname: user.firstname || "",
        lastname: user.lastname || "",
        language: user.language || "en",
        enabled: user.enabled ?? true,
        roleId: user.roleId || 1,
      });
    }
  }, [user]);

  const handleChange = (event) => setForm((prev) => ({ ...prev, [event.target.name]: event.target.value }));

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      setSaving(true);
      const response = await updateUser(user.userId, { ...form, roleId: Number(form.roleId), enabled: Boolean(form.enabled) });
      setUser((prev) => ({ ...prev, ...response.data, userId: response.data.id ?? prev.userId }));
      await loadProfile();
      toast.success("Profile updated");
    } catch (error) {
      toast.error(getErrorMessage(error, "Profile update failed"));
    } finally {
      setSaving(false);
    }
  };

  return (
    <section className="page narrow-page">
      <div className="page-header">
        <div>
          <span className="eyebrow">Member area</span>
          <h1>My Profile</h1>
          <p>Update your personal information and language preference.</p>
        </div>
      </div>

      <form onSubmit={handleSubmit} className="card profile-form two-column-form">
        <Input label="Username" name="username" value={form.username} onChange={handleChange} required />
        <Input label="Email" name="email" type="email" value={form.email} onChange={handleChange} required />
        <Input label="First Name" name="firstname" value={form.firstname} onChange={handleChange} />
        <Input label="Last Name" name="lastname" value={form.lastname} onChange={handleChange} />
        <div className="form-group">
          <label className="form-label">Language</label>
          <select className="form-input" name="language" value={form.language} onChange={handleChange}>
            <option value="en">English</option>
            <option value="fr">French</option>
            <option value="nl">Dutch</option>
          </select>
        </div>
        <Button type="submit" disabled={saving} className="form-wide">{saving ? "Saving..." : "Update profile"}</Button>
      </form>
    </section>
  );
};

export default Profile;

import { Link, Outlet } from "react-router-dom";
import { APP_NAME } from "../../utils/constants";

const AuthLayout = () => (
  <main className="auth-layout">
    <section className="auth-brand-panel">
      <Link to="/" className="navbar-logo">
        <span className="logo-mark">R</span>
        <span>{APP_NAME}</span>
      </Link>
      <h1>Manage theatre reservations with a clean, secure web interface.</h1>
      <p>Members can reserve seats and review shows. Administrators can manage the full catalogue and tools.</p>
    </section>

    <section className="auth-card">
      <Outlet />
    </section>
  </main>
);

export default AuthLayout;

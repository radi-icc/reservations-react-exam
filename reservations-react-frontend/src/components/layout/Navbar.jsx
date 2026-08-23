import { Link, NavLink } from "react-router-dom";
import useAuth from "../../hooks/useAuth";
import { APP_NAME } from "../../utils/constants";

const Navbar = () => {
  const { user, logout, isAdmin, isProducer, isCritic, isAffiliate } = useAuth();

  return (
    <header className="navbar">
      <Link to="/" className="navbar-logo" aria-label="Home">
        <span className="logo-mark">R</span>
        <span>{APP_NAME}</span>
      </Link>

      <nav className="navbar-links" aria-label="Primary navigation">
        <NavLink to="/" end>Home</NavLink>
        <NavLink to="/shows">Shows</NavLink>
        <NavLink to="/affiliate-catalogue">Affiliate API</NavLink>
        {user && <NavLink to="/my-reservations">My Reservations</NavLink>}
        {user && <NavLink to="/my-reviews">My Reviews</NavLink>}
        {isProducer && <NavLink to="/producer">Producer</NavLink>}
        {isCritic && <NavLink to="/critic">Critic</NavLink>}
        {isAffiliate && <NavLink to="/affiliate">Affiliate</NavLink>}
        {isAdmin && <NavLink to="/admin">Back Office</NavLink>}
      </nav>

      <div className="navbar-actions">
        {!user ? (
          <>
            <Link to="/login" className="btn btn-outline btn-sm">Login</Link>
            <Link to="/register" className="btn btn-primary btn-sm">Register</Link>
          </>
        ) : (
          <>
            <Link to="/profile" className="user-chip" title="Open profile">
              <span className="avatar-dot">{(user.firstname || user.username || user.email || "U").charAt(0).toUpperCase()}</span>
              <span>{user.firstname || user.username || user.email}</span>
            </Link>
            <button onClick={logout} className="btn btn-danger btn-sm">Logout</button>
          </>
        )}
      </div>
    </header>
  );
};

export default Navbar;

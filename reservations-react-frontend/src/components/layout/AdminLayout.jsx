import { NavLink, Outlet } from "react-router-dom";
import Navbar from "./Navbar";

const links = [
  { to: "/admin", label: "Dashboard" },
  { to: "/admin/shows", label: "Shows" },
  { to: "/admin/representations", label: "Representations" },
  { to: "/admin/reservations", label: "Reservations" },
  { to: "/admin/reviews", label: "Reviews" },
  { to: "/admin/users", label: "Users" },
  { to: "/admin/roles", label: "Roles" },
  { to: "/admin/locations", label: "Locations" },
  { to: "/admin/localities", label: "Localities" },
  { to: "/admin/prices", label: "Prices" },
  { to: "/admin/artists", label: "Artists" },
  { to: "/admin/artist-types", label: "Artist Types" },
  { to: "/admin/artist-type-assignments", label: "Assignments" },
  { to: "/admin/collaborations", label: "Collaborations" },
  { to: "/admin/affiliate-plans", label: "Affiliate Plans" },
  { to: "/admin/api-keys", label: "API Keys" },
  { to: "/admin/tools", label: "Tools" },
];

const AdminLayout = () => (
  <div className="admin-layout">
    <Navbar />
    <div className="admin-body">
      <aside className="admin-sidebar">
        <div className="sidebar-title">Back Office</div>
        {links.map((item) => (
          <NavLink key={item.to} to={item.to} end={item.to === "/admin"}>
            {item.label}
          </NavLink>
        ))}
      </aside>
      <main className="admin-content">
        <Outlet />
      </main>
    </div>
  </div>
);

export default AdminLayout;

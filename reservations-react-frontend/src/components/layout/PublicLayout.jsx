import { Outlet } from "react-router-dom";
import Navbar from "./Navbar";
import Footer from "./Footer";

const PublicLayout = () => (
  <div className="app-layout">
    <a className="skip-link" href="#main-content">Skip to content</a>
    <Navbar />
    <main id="main-content" className="main-content">
      <Outlet />
    </main>
    <Footer />
  </div>
);

export default PublicLayout;

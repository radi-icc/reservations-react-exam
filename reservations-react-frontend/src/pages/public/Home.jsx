import { Link } from "react-router-dom";

const Home = () => (
  <section className="hero">
    <div className="hero-copy">
      <span className="eyebrow">Production company reservation platform</span>
      <h1>Discover theatre shows and reserve seats online.</h1>
      <p>
        Browse a paginated catalogue, view venues and performance dates, reserve seats as a member,
        and use the secure back-office for catalogue management.
      </p>
      <div className="hero-actions">
        <Link to="/shows" className="btn btn-primary">Browse shows</Link>
        <Link to="/affiliate-catalogue" className="btn btn-outline">Affiliate API</Link>
      </div>
    </div>

    <div className="hero-panel">
      <div className="hero-panel-item"><strong>Search</strong><span>Catalogue filtering, sorting and pagination</span></div>
      <div className="hero-panel-item"><strong>Reserve</strong><span>Authenticated reservation with price type and quantity</span></div>
      <div className="hero-panel-item"><strong>Manage</strong><span>Admin CRUD, CSV import/export and external import</span></div>
      <div className="hero-panel-item"><strong>Publish</strong><span>RSS feed and affiliate catalogue API</span></div>
    </div>
  </section>
);

export default Home;

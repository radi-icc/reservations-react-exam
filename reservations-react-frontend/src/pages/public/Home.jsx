import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import CatalogueTable from "../../components/shows/CatalogueTable";
import Loader from "../../components/common/Loader";
import { getShows } from "../../api/showsApi";

const Home = () => {
  const [shows, setShows] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getShows({ page: 0, size: 3, sortBy: "title", direction: "asc" })
      .then((response) => setShows(response.data.content || []))
      .catch(() => setShows([]))
      .finally(() => setLoading(false));
  }, []);

  return <>
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
  <section className="home-catalogue" aria-labelledby="home-catalogue-title">
    <div className="section-title-row"><div><span className="eyebrow">Programme</span><h2 id="home-catalogue-title">Shows catalogue</h2></div><Link className="btn btn-outline btn-sm" to="/shows">View all shows</Link></div>
    {loading ? <Loader /> : <CatalogueTable shows={shows} />}
  </section>
  </>;
};

export default Home;

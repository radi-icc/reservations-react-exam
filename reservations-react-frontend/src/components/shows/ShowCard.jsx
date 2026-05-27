import { Link } from "react-router-dom";
import { formatPrice } from "../../utils/formatPrice";

const ShowCard = ({ show }) => (
  <article className="show-card">
    <Link to={`/shows/${show.id}`} className="show-image" aria-label={`Open ${show.title}`}>
      {show.posterUrl ? <img src={show.posterUrl} alt={show.title} /> : <div className="image-placeholder">No Poster</div>}
    </Link>

    <div className="show-card-body">
      <div className="card-meta-row">
        <span className={`status-pill ${show.bookable ? "success" : "muted"}`}>{show.bookable ? "Bookable" : "Not bookable"}</span>
        <strong>{formatPrice(show.price)}</strong>
      </div>
      <h3><Link to={`/shows/${show.id}`}>{show.title}</Link></h3>
      <p>{show.description || "No description available."}</p>
      <div className="card-footer-row">
        <span>{show.locationDesignation || "Venue not set"}</span>
        <Link className="text-link" to={`/shows/${show.id}`}>Details →</Link>
      </div>
    </div>
  </article>
);

export default ShowCard;

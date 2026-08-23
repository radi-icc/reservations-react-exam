import { Link } from "react-router-dom";
import { formatPrice } from "../../utils/formatPrice";

const CatalogueTable = ({ shows = [] }) => {
  if (!shows.length) return <p className="catalogue-empty">No shows match your search.</p>;
  return <div className="catalogue-table-wrap" tabIndex="0" aria-label="Show catalogue table">
    <table className="catalogue-table">
      <thead><tr><th scope="col">Title</th><th scope="col">Venue</th><th scope="col">Price</th><th scope="col">Availability</th><th scope="col"><span className="sr-only">Open show</span></th></tr></thead>
      <tbody>{shows.map((show) => <tr key={show.id}>
        <th scope="row"><Link to={`/shows/${show.id}`}>{show.title}</Link></th>
        <td>{show.locationDesignation || "Venue to be announced"}</td>
        <td>{formatPrice(show.price)}</td>
        <td><span className={`catalogue-status ${show.bookable ? "is-bookable" : "is-closed"}`}>{show.bookable ? "Bookable" : "Sold out"}</span></td>
        <td><Link className="catalogue-details" to={`/shows/${show.id}`} aria-label={`View ${show.title}`}>View</Link></td>
      </tr>)}</tbody>
    </table>
  </div>;
};

export default CatalogueTable;

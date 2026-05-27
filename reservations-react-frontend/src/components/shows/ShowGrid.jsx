import EmptyState from "../common/EmptyState";
import ShowCard from "./ShowCard";

const ShowGrid = ({ shows = [] }) => {
  if (!shows.length) {
    return <EmptyState title="No shows found" message="Try changing the search, filters, or pagination." />;
  }

  return (
    <div className="show-grid">
      {shows.map((show) => <ShowCard key={show.id} show={show} />)}
    </div>
  );
};

export default ShowGrid;

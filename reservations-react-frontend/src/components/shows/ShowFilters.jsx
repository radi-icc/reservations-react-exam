import Button from "../common/Button";

const ShowFilters = ({ filters, locations = [], onChange, onReset }) => (
  <form className="filters-card" onSubmit={(e) => e.preventDefault()} aria-label="Catalogue filters">
    <div className="form-group no-margin">
      <label className="form-label">Search</label>
      <input
        className="form-input"
        placeholder="Search by show title"
        value={filters.search}
        onChange={(e) => onChange("search", e.target.value)}
      />
    </div>

    <div className="form-group no-margin">
      <label className="form-label">Venue</label>
      <select className="form-input" value={filters.locationId} onChange={(e) => onChange("locationId", e.target.value)}>
        <option value="">All venues</option>
        {locations.map((location) => (
          <option key={location.id} value={location.id}>{location.designation}</option>
        ))}
      </select>
    </div>

    <div className="form-group no-margin">
      <label className="form-label">Availability</label>
      <select className="form-input" value={filters.bookable} onChange={(e) => onChange("bookable", e.target.value)}>
        <option value="">All</option>
        <option value="true">Bookable</option>
        <option value="false">Not bookable</option>
      </select>
    </div>

    <div className="form-group no-margin">
      <label className="form-label">Sort</label>
      <select className="form-input" value={`${filters.sortBy}:${filters.direction}`} onChange={(e) => {
        const [sortBy, direction] = e.target.value.split(":");
        onChange("sortBy", sortBy);
        onChange("direction", direction);
      }}>
        <option value="title:asc">Title A-Z</option>
        <option value="title:desc">Title Z-A</option>
        <option value="price:asc">Price low-high</option>
        <option value="price:desc">Price high-low</option>
        <option value="createdAt:desc">Newest first</option>
      </select>
    </div>

    <Button type="button" variant="outline" onClick={onReset}>Reset</Button>
  </form>
);

export default ShowFilters;

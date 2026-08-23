import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { getResource } from "../../api/adminApi";
import { getShows } from "../../api/showsApi";
import Loader from "../../components/common/Loader";
import Pagination from "../../components/common/Pagination";
import ShowFilters from "../../components/shows/ShowFilters";
import CatalogueTable from "../../components/shows/CatalogueTable";
import useDebounce from "../../hooks/useDebounce";
import { DEFAULT_PAGE_SIZE } from "../../utils/constants";
import { getErrorMessage } from "../../utils/errorUtils";

const defaultFilters = {
  search: "",
  locationId: "",
  bookable: "",
  sortBy: "title",
  direction: "asc",
};

const Shows = () => {
  const [shows, setShows] = useState([]);
  const [locations, setLocations] = useState([]);
  const [filters, setFilters] = useState(defaultFilters);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(true);
  const debouncedSearch = useDebounce(filters.search, 450);

  const loadLocations = async () => {
    try {
      const response = await getResource("locations");
      setLocations(Array.isArray(response.data) ? response.data : response.data.content || []);
    } catch {
      setLocations([]);
    }
  };

  const loadShows = async () => {
    try {
      setLoading(true);
      const params = {
        page,
        size: DEFAULT_PAGE_SIZE,
        sortBy: filters.sortBy,
        direction: filters.direction,
      };

      if (debouncedSearch) params.search = debouncedSearch;
      if (filters.locationId) params.locationId = filters.locationId;
      if (filters.bookable !== "") params.bookable = filters.bookable;

      const response = await getShows(params);
      setShows(response.data.content || []);
      setTotalPages(response.data.totalPages || 0);
      setTotalElements(response.data.totalElements || 0);
    } catch (error) {
      toast.error(getErrorMessage(error, "Failed to load shows"));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadLocations(); }, []);
  useEffect(() => { loadShows(); }, [page, filters.locationId, filters.bookable, filters.sortBy, filters.direction, debouncedSearch]);

  const handleChange = (key, value) => {
    setPage(0);
    setFilters((prev) => ({ ...prev, [key]: value }));
  };

  return (
    <section className="catalogue-page">
      <aside className="catalogue-sidebar" aria-label="Catalogue navigation">
        <h2>Menu</h2>
        <a href="#catalogue">Shows</a>
        <a href="#filters">Search & filters</a>
        <a href="#upcoming">Upcoming performances</a>
        <a href="#catalogue">My reservations</a>
      </aside>
      <div className="catalogue-content">
      <div className="page-header catalogue-heading">
        <div>
          <span className="eyebrow">Catalogue</span>
          <h1>Shows catalogue</h1>
          <p>Browse productions, compare venues and reserve an upcoming performance.</p>
        </div>
      </div>

      <section id="upcoming" className="catalogue-stage" aria-labelledby="upcoming-title">
        <div className="stage-curtain" aria-hidden="true" />
        <div className="stage-copy"><span>Next dates</span><h2 id="upcoming-title">Upcoming performances</h2><p>{shows.slice(0, 3).map((show) => show.title).join(" · ") || "The new programme is being prepared."}</p></div>
        <div className="stage-curtain stage-curtain-right" aria-hidden="true" />
      </section>

      <div id="filters"><ShowFilters
        filters={filters}
        locations={locations}
        onChange={handleChange}
        onReset={() => { setPage(0); setFilters(defaultFilters); }}
      /></div>

      <section id="catalogue" className="catalogue-list" aria-labelledby="catalogue-title">
        <div className="section-title-row"><h2 id="catalogue-title">Show list</h2><span className="muted-text">{totalElements} productions</span></div>
        {loading ? <Loader /> : <CatalogueTable shows={shows} />}
      </section>

      <Pagination
        currentPage={page + 1}
        totalPages={totalPages}
        totalElements={totalElements}
        onPageChange={(newPage) => setPage(newPage - 1)}
      />
      </div>
    </section>
  );
};

export default Shows;

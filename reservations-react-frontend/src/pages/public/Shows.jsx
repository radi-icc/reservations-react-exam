import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { getResource } from "../../api/adminApi";
import { getShows } from "../../api/showsApi";
import Loader from "../../components/common/Loader";
import Pagination from "../../components/common/Pagination";
import ShowFilters from "../../components/shows/ShowFilters";
import ShowGrid from "../../components/shows/ShowGrid";
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
    <section className="page">
      <div className="page-header">
        <div>
          <span className="eyebrow">Catalogue</span>
          <h1>Shows</h1>
          <p>Search, filter, sort and paginate the theatre catalogue.</p>
        </div>
      </div>

      <ShowFilters
        filters={filters}
        locations={locations}
        onChange={handleChange}
        onReset={() => { setPage(0); setFilters(defaultFilters); }}
      />

      {loading ? <Loader /> : <ShowGrid shows={shows} />}

      <Pagination
        currentPage={page + 1}
        totalPages={totalPages}
        totalElements={totalElements}
        onPageChange={(newPage) => setPage(newPage - 1)}
      />
    </section>
  );
};

export default Shows;

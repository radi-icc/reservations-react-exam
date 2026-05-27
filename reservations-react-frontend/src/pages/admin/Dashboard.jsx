import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { getResource, getShowSales, getStatistics, normaliseCollection } from "../../api/adminApi";
import DashboardStats from "../../components/admin/DashboardStats";
import Loader from "../../components/common/Loader";
import { getErrorMessage } from "../../utils/errorUtils";
import { formatPrice } from "../../utils/formatPrice";

const Dashboard = () => {
  const [stats, setStats] = useState(null);
  const [shows, setShows] = useState([]);
  const [selectedShowId, setSelectedShowId] = useState("");
  const [sales, setSales] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadDashboard = async () => {
    try {
      setLoading(true);
      const [statsRes, showsRes] = await Promise.all([getStatistics(), getResource("shows", { size: 100, sortBy: "title" })]);
      setStats(statsRes.data);
      setShows(normaliseCollection(showsRes.data));
    } catch (error) {
      toast.error(getErrorMessage(error, "Failed to load dashboard"));
    } finally {
      setLoading(false);
    }
  };

  const loadSales = async (showId) => {
    setSelectedShowId(showId);
    if (!showId) {
      setSales(null);
      return;
    }

    try {
      const response = await getShowSales(showId);
      setSales(response.data);
    } catch (error) {
      toast.error(getErrorMessage(error, "Failed to load sales statistics"));
    }
  };

  useEffect(() => { loadDashboard(); }, []);

  if (loading || !stats) return <Loader />;

  const dashboardStats = [
    { label: "Shows", value: stats.totalShows ?? 0 },
    { label: "Representations", value: stats.totalRepresentations ?? 0 },
    { label: "Reservations", value: stats.totalReservations ?? 0 },
    { label: "Users", value: stats.totalUsers ?? 0 },
    { label: "Revenue", value: formatPrice(stats.totalRevenue) },
  ];

  return (
    <section className="admin-page">
      <div className="page-header">
        <div>
          <span className="eyebrow">Administration</span>
          <h1>Dashboard</h1>
          <p>Global statistics and producer-style show sales overview.</p>
        </div>
      </div>

      <DashboardStats stats={dashboardStats} />

      <div className="card sales-card">
        <div className="section-title-row">
          <div>
            <h2>Show sales statistics</h2>
            <p className="muted-text">Uses the sales endpoint for a selected show.</p>
          </div>
          <select className="form-input compact-select" value={selectedShowId} onChange={(e) => loadSales(e.target.value)}>
            <option value="">Select show</option>
            {shows.map((show) => <option key={show.id} value={show.id}>{show.title}</option>)}
          </select>
        </div>

        {sales && (
          <DashboardStats stats={[
            { label: "Show", value: sales.showTitle || sales.showId },
            { label: "Reservations", value: sales.totalReservations ?? 0 },
            { label: "Seats sold", value: sales.totalSeatsSold ?? 0 },
            { label: "Revenue", value: formatPrice(sales.totalRevenue) },
          ]} />
        )}
      </div>
    </section>
  );
};

export default Dashboard;

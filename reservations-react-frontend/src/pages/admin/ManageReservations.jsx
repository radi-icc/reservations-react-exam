import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { cancelReservation, getAllReservations } from "../../api/reservationsApi";
import AdminTable from "../../components/admin/AdminTable";
import Loader from "../../components/common/Loader";
import { formatDate, formatTime } from "../../utils/formatDate";
import { getErrorMessage } from "../../utils/errorUtils";
import { formatPrice } from "../../utils/formatPrice";

const ManageReservations = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadReservations = async () => {
    try {
      setLoading(true);
      const response = await getAllReservations();
      setData(response.data || []);
    } catch (error) {
      toast.error(getErrorMessage(error, "Failed to load reservations"));
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = async (id) => {
    if (!window.confirm("Cancel this reservation?")) return;

    try {
      await cancelReservation(id);
      toast.success("Reservation cancelled");
      loadReservations();
    } catch (error) {
      toast.error(getErrorMessage(error, "Cancel failed"));
    }
  };

  useEffect(() => { loadReservations(); }, []);

  if (loading) return <Loader />;

  return (
    <section className="admin-page">
      <div className="page-header">
        <div>
          <span className="eyebrow">Administration</span>
          <h1>Reservations</h1>
        </div>
      </div>

      <AdminTable
        data={data}
        columns={[
          { key: "reservationId", label: "ID" },
          { key: "username", label: "User" },
          { key: "showTitle", label: "Show" },
          { key: "performanceDate", label: "Performance", render: (r) => `${formatDate(r.performanceDate)} ${formatTime(r.performanceTime)}` },
          { key: "priceLabel", label: "Price" },
          { key: "quantity", label: "Quantity" },
          { key: "totalPrice", label: "Total", render: (r) => formatPrice(r.totalPrice) },
          { key: "status", label: "Status" },
        ]}
        extraActions={[{ name: "cancel", label: "Cancel", variant: "danger" }]}
        onAction={handleCancel}
      />
    </section>
  );
};

export default ManageReservations;

import toast from "react-hot-toast";
import { cancelReservation, getMyReservations } from "../../api/reservationsApi";
import EmptyState from "../../components/common/EmptyState";
import Loader from "../../components/common/Loader";
import ReservationCard from "../../components/reservations/ReservationCard";
import useFetch from "../../hooks/useFetch";
import { getErrorMessage } from "../../utils/errorUtils";

const MyReservations = () => {
  const { data, loading, error, refetch } = useFetch(getMyReservations, []);

  const handleCancel = async (id) => {
    if (!window.confirm("Cancel this reservation?")) return;

    try {
      await cancelReservation(id);
      toast.success("Reservation cancelled");
      refetch();
    } catch (error) {
      toast.error(getErrorMessage(error, "Failed to cancel reservation"));
    }
  };

  if (loading) return <Loader />;

  return (
    <section className="page">
      <div className="page-header">
        <div>
          <span className="eyebrow">Member area</span>
          <h1>My Reservations</h1>
          <p>View your reservations and cancel active bookings.</p>
        </div>
      </div>

      {error && <p className="error-banner">{error}</p>}
      {!data.length ? <EmptyState title="No reservations" message="You have not booked a performance yet." /> : (
        <div className="list">
          {data.map((reservation) => <ReservationCard key={reservation.reservationId} reservation={reservation} onCancel={handleCancel} />)}
        </div>
      )}
    </section>
  );
};

export default MyReservations;

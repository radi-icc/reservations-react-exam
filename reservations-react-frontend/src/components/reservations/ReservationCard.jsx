import Button from "../common/Button";
import { formatDate, formatTime } from "../../utils/formatDate";
import { formatPrice } from "../../utils/formatPrice";

const ReservationCard = ({ reservation, onCancel }) => {
  const cancelled = String(reservation.status || "").toLowerCase().includes("cancel");

  return (
    <article className="reservation-card">
      <div>
        <div className="card-meta-row left">
          <span className={`status-pill ${cancelled ? "danger" : "success"}`}>{reservation.status || "Confirmed"}</span>
          <span className="muted-text">#{reservation.reservationId}</span>
        </div>
        <h3>{reservation.showTitle}</h3>
        <p>{formatDate(reservation.performanceDate)} · {formatTime(reservation.performanceTime)}</p>
        <p className="muted-text">{reservation.priceLabel} · {reservation.quantity} seat(s)</p>
      </div>
      <div className="reservation-card-side">
        <strong>{formatPrice(reservation.totalPrice)}</strong>
        {!cancelled && <Button size="sm" variant="danger" onClick={() => onCancel(reservation.reservationId)}>Cancel</Button>}
      </div>
    </article>
  );
};

export default ReservationCard;

import Button from "../common/Button";
import { formatTime } from "../../utils/formatDate";

const RepresentationList = ({ representations = [], availabilityById = {}, onReserve }) => {
  if (!representations.length) {
    return <p className="muted-text">No representations available for this show.</p>;
  }

  return (
    <div className="representation-list">
      {representations.map((item) => {
        const availability = availabilityById[item.id];
        const availableSeats = availability?.availableSeats ?? (Number(item.capacity || 0) - Number(item.bookedSeats || 0));
        const isFull = availability?.full ?? item.full ?? availableSeats <= 0;

        return (
          <article className="representation-card" key={item.id}>
            <div>
              <h4>{item.performanceDate} · {formatTime(item.performanceTime)}</h4>
              <p>{item.locationDesignation || "Location not set"}</p>
              <p className="muted-text">Capacity {item.capacity || 0} · Booked {item.bookedSeats || 0}</p>
            </div>
            <div className="representation-actions">
              <span className={`status-pill ${isFull ? "danger" : "success"}`}>{availableSeats} seats left</span>
              <Button disabled={isFull} onClick={() => onReserve(item)}>{isFull ? "Full" : "Reserve"}</Button>
            </div>
          </article>
        );
      })}
    </div>
  );
};

export default RepresentationList;

import { useEffect, useMemo, useState } from "react";
import Button from "../common/Button";
import { formatPrice } from "../../utils/formatPrice";
import { formatTime } from "../../utils/formatDate";

const ReservationForm = ({ representation, prices = [], availability, onSubmit, loading, pricesLoading }) => {
  const [priceId, setPriceId] = useState("");
  const [quantity, setQuantity] = useState(1);
  const [ticketDeliveryMethod, setTicketDeliveryMethod] = useState("EMAIL");
  const [paymentMethod, setPaymentMethod] = useState("CARD");

  useEffect(() => {
    if (prices.length > 0 && !priceId) {
      setPriceId(String(prices[0].id));
    }
  }, [prices, priceId]);

  const selectedPrice = useMemo(
    () => prices.find((price) => Number(price.id) === Number(priceId)),
    [prices, priceId]
  );

  const availableSeats = availability?.availableSeats ?? (Number(representation.capacity || 0) - Number(representation.bookedSeats || 0));
  const total = Number(selectedPrice?.amount || 0) * Number(quantity || 0);

  const handleSubmit = (event) => {
    event.preventDefault();
    onSubmit({
      representationId: Number(representation.id),
      priceId: Number(priceId),
      quantity: Number(quantity),
      ticketDeliveryMethod,
      paymentMethod,
    });
  };

  return (
    <form onSubmit={handleSubmit} className="reservation-box">
      <div className="reservation-summary">
        <div>
          <span className="muted-text">Performance</span>
          <strong>{representation.performanceDate} · {formatTime(representation.performanceTime)}</strong>
        </div>
        <div>
          <span className="muted-text">Available</span>
          <strong>{availableSeats} seats</strong>
        </div>
      </div>

      <div className="form-group">
        <label className="form-label">Price type</label>
        <select className="form-input" value={priceId} onChange={(e) => setPriceId(e.target.value)} required disabled={pricesLoading}>
          <option value="">{pricesLoading ? "Loading prices..." : "Select price"}</option>
          {prices.map((price) => (
            <option key={price.id} value={price.id}>{price.label} · {formatPrice(price.amount)}</option>
          ))}
        </select>
      </div>

      <div className="form-group">
        <label className="form-label">Ticket delivery</label>
        <select className="form-input" value={ticketDeliveryMethod} onChange={(e) => setTicketDeliveryMethod(e.target.value)} required>
          <option value="EMAIL">Email ticket</option>
          <option value="PICKUP">Collect at the venue</option>
        </select>
      </div>

      <div className="form-group">
        <label className="form-label">Payment method</label>
        <select className="form-input" value={paymentMethod} onChange={(e) => setPaymentMethod(e.target.value)} required>
          <option value="CARD">Card payment (demo)</option>
          <option value="ONSITE">Pay at the venue</option>
        </select>
        <small className="form-help">Card payment is recorded as confirmed in this demo; no payment provider is connected yet.</small>
      </div>

      <div className="form-group">
        <label className="form-label">Quantity</label>
        <input
          className="form-input"
          type="number"
          min="1"
          max={Math.max(availableSeats, 1)}
          value={quantity}
          onChange={(e) => setQuantity(e.target.value)}
          required
        />
      </div>

      <div className="total-row">
        <span>Total</span>
        <strong>{formatPrice(total)}</strong>
      </div>

      <Button type="submit" disabled={loading || pricesLoading || !priceId || Number(quantity) < 1 || Number(quantity) > availableSeats} className="btn-full">
        {loading ? "Reserving..." : "Confirm reservation"}
      </Button>
    </form>
  );
};

export default ReservationForm;

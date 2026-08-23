import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import toast from "react-hot-toast";

import { normaliseCollection } from "../../api/adminApi";
import { createReservation } from "../../api/reservationsApi";
import { createReview, getReviewsByShow } from "../../api/reviewsApi";
import { getAvailability, getPricesByRepresentation, getRepresentationsByShow, getShowById } from "../../api/showsApi";
import useAuth from "../../hooks/useAuth";
import { getErrorMessage } from "../../utils/errorUtils";
import { formatPrice } from "../../utils/formatPrice";
import { formatDate, formatTime } from "../../utils/formatDate";

import Button from "../../components/common/Button";
import EmptyState from "../../components/common/EmptyState";
import Input from "../../components/common/Input";
import Loader from "../../components/common/Loader";
import Modal from "../../components/common/Modal";
import ReservationForm from "../../components/reservations/ReservationForm";
import RepresentationList from "../../components/shows/RepresentationList";

const ShowDetails = () => {
  const { id } = useParams();
  const { user } = useAuth();

  const [show, setShow] = useState(null);
  const [representations, setRepresentations] = useState([]);
  const [availabilityById, setAvailabilityById] = useState({});
  const [prices, setPrices] = useState([]);
  const [pricesLoading, setPricesLoading] = useState(false);
  const [reviews, setReviews] = useState([]);
  const [selectedRepresentation, setSelectedRepresentation] = useState(null);
  const [confirmation, setConfirmation] = useState(null);
  const [loading, setLoading] = useState(true);
  const [reserving, setReserving] = useState(false);
  const [reviewing, setReviewing] = useState(false);
  const [reviewForm, setReviewForm] = useState({ rating: 5, comment: "" });

  const loadData = async () => {
    try {
      setLoading(true);
      const [showRes, repsRes, reviewsRes] = await Promise.all([
        getShowById(id),
        getRepresentationsByShow(id),
        getReviewsByShow(id),
      ]);

      const reps = repsRes.data || [];
      setShow(showRes.data);
      setRepresentations(reps);
      setReviews(reviewsRes.data || []);

      const availabilityResults = await Promise.allSettled(reps.map((rep) => getAvailability(rep.id)));
      const nextAvailability = {};
      availabilityResults.forEach((result, index) => {
        if (result.status === "fulfilled") nextAvailability[reps[index].id] = result.value.data;
      });
      setAvailabilityById(nextAvailability);
    } catch (error) {
      toast.error(getErrorMessage(error, "Failed to load show details"));
    } finally {
      setLoading(false);
    }
  };

  const handleRepresentationSelect = async (representation) => {
    setSelectedRepresentation(representation);
    setConfirmation(null);
    setPrices([]);

    try {
      setPricesLoading(true);
      const response = await getPricesByRepresentation(representation.id);
      setPrices(normaliseCollection(response.data));
    } catch (error) {
      toast.error(getErrorMessage(error, "Failed to load prices for this performance"));
    } finally {
      setPricesLoading(false);
    }
  };

  useEffect(() => { loadData(); }, [id]);

  const handleReservationSubmit = async (payload) => {
    if (!user) {
      toast.error("Please login before reserving seats.");
      return;
    }

    if (!payload.priceId || !payload.quantity) {
      toast.error("Please select price and quantity.");
      return;
    }

    try {
      setReserving(true);
      const response = await createReservation(payload);
      toast.success("Reservation created successfully");
      setSelectedRepresentation(null);
      setConfirmation(response.data);
      await loadData();
    } catch (error) {
      toast.error(getErrorMessage(error, "Reservation failed"));
    } finally {
      setReserving(false);
    }
  };

  const handleReviewSubmit = async (event) => {
    event.preventDefault();

    if (!user) {
      toast.error("Please login before leaving a review.");
      return;
    }

    try {
      setReviewing(true);
      await createReview({
        showId: Number(id),
        rating: Number(reviewForm.rating),
        comment: reviewForm.comment.trim(),
      });
      toast.success("Review submitted");
      setReviewForm({ rating: 5, comment: "" });
      await loadData();
    } catch (error) {
      toast.error(getErrorMessage(error, "Review failed"));
    } finally {
      setReviewing(false);
    }
  };

  if (loading) return <Loader />;
  if (!show) return <EmptyState title="Show not found" message="The requested show does not exist." action={<Link className="btn btn-primary" to="/shows">Back to shows</Link>} />;

  return (
    <section className="show-details-page">
      <div className="show-details-card">
        <div className="show-poster-box">
          {show.posterUrl ? <img src={show.posterUrl} alt={show.title} /> : <div className="image-placeholder large">No Poster</div>}
        </div>

        <div className="show-details-copy">
          <span className={`status-pill ${show.bookable ? "success" : "muted"}`}>{show.bookable ? "Bookable" : "Not bookable"}</span>
          <h1>{show.title}</h1>
          <p className="show-description-large">{show.description || "No description available."}</p>

          <div className="details-grid">
            <div><strong>Venue</strong><span>{show.locationDesignation || "Not assigned"}</span></div>
            <div><strong>Base price</strong><span>{formatPrice(show.price)}</span></div>
            <div><strong>Representations</strong><span>{representations.length}</span></div>
            <div><strong>Reviews</strong><span>{reviews.length}</span></div>
          </div>
        </div>
      </div>

      <section className="section-block">
        <div className="section-title-row">
          <div>
            <span className="eyebrow">Booking</span>
            <h2>Representations</h2>
          </div>
          {!user && <Link className="btn btn-outline btn-sm" to="/login">Login to reserve</Link>}
        </div>
        <RepresentationList representations={representations} availabilityById={availabilityById} onReserve={handleRepresentationSelect} />
      </section>

      <section className="section-block split-section">
        <div>
          <span className="eyebrow">Member feedback</span>
          <h2>Leave a review</h2>
          <p className="muted-text">Members can comment on shows. Published reviews appear below.</p>
        </div>
        <form onSubmit={handleReviewSubmit} className="professional-form">
          <Input label="Rating" type="number" min="1" max="5" value={reviewForm.rating} onChange={(e) => setReviewForm((prev) => ({ ...prev, rating: e.target.value }))} required />
          <div className="form-group">
            <label className="form-label">Comment</label>
            <textarea className="form-input textarea" value={reviewForm.comment} onChange={(e) => setReviewForm((prev) => ({ ...prev, comment: e.target.value }))} required />
          </div>
          <Button type="submit" disabled={reviewing}>{reviewing ? "Submitting..." : "Submit review"}</Button>
        </form>
      </section>

      <section className="section-block">
        <h2>Published reviews</h2>
        <div className="review-grid">
          {reviews.length === 0 ? <EmptyState title="No reviews yet" message="Be the first member to review this show." /> : reviews.map((review) => (
            <article className="review-card" key={review.id}>
              <div className="card-meta-row left"><strong>{review.username || "Member"}</strong><span>{review.rating}/5</span></div>
              <p>{review.comment}</p>
            </article>
          ))}
        </div>
      </section>

      <Modal isOpen={!!selectedRepresentation || !!confirmation} title={confirmation ? "Reservation confirmed" : "Reserve seats"} onClose={() => { setSelectedRepresentation(null); setConfirmation(null); }}>
        {selectedRepresentation && (
          <ReservationForm
            representation={selectedRepresentation}
            prices={prices}
            availability={availabilityById[selectedRepresentation.id]}
            loading={reserving}
            pricesLoading={pricesLoading}
            onSubmit={handleReservationSubmit}
          />
        )}
        {confirmation && (
          <div className="reservation-confirmation">
            <span className="status-pill success">Confirmed</span>
            <h3>{confirmation.showTitle}</h3>
            <p>{formatDate(confirmation.performanceDate)} · {formatTime(confirmation.performanceTime)}</p>
            <p>{confirmation.quantity} seat(s) · {confirmation.priceLabel}</p>
            <p className="muted-text">Tickets: {confirmation.ticketDeliveryMethod === "EMAIL" ? "sent by email" : "collect at the venue"} · Payment: {confirmation.paymentMethod === "CARD" ? "card" : "at the venue"}</p>
            <div className="total-row"><span>Total</span><strong>{formatPrice(confirmation.totalPrice)}</strong></div>
            <Button className="btn-full" onClick={() => setConfirmation(null)}>Done</Button>
          </div>
        )}
      </Modal>
    </section>
  );
};

export default ShowDetails;

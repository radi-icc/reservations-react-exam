import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import Button from "../../components/common/Button";
import EmptyState from "../../components/common/EmptyState";
import Loader from "../../components/common/Loader";
import { deleteReview, getMyReviews } from "../../api/reviewsApi";
import { getErrorMessage } from "../../utils/errorUtils";

const MyReviews = () => {
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadReviews = async () => {
    try {
      setLoading(true);
      const response = await getMyReviews();
      setReviews(response.data || []);
    } catch (error) {
      toast.error(getErrorMessage(error, "Failed to load reviews"));
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this review?")) return;

    try {
      await deleteReview(id);
      toast.success("Review deleted");
      loadReviews();
    } catch (error) {
      toast.error(getErrorMessage(error, "Delete failed"));
    }
  };

  useEffect(() => { loadReviews(); }, []);

  if (loading) return <Loader />;

  return (
    <section className="page">
      <div className="page-header">
        <div>
          <span className="eyebrow">Member area</span>
          <h1>My Reviews</h1>
          <p>Track your comments and whether they have been published.</p>
        </div>
      </div>

      {!reviews.length ? <EmptyState title="No reviews" message="You have not reviewed any show yet." /> : (
        <div className="review-grid">
          {reviews.map((review) => (
            <article className="review-card" key={review.id}>
              <div className="card-meta-row left"><strong>{review.showTitle}</strong><span>{review.rating}/5</span></div>
              <p>{review.comment}</p>
              <div className="card-footer-row">
                <span className={`status-pill ${review.published ? "success" : "muted"}`}>{review.published ? "Published" : "Pending"}</span>
                <Button variant="danger" size="sm" onClick={() => handleDelete(review.id)}>Delete</Button>
              </div>
            </article>
          ))}
        </div>
      )}
    </section>
  );
};

export default MyReviews;

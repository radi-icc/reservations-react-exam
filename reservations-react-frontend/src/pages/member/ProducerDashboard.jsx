import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { getProducerReviews, getProducerSales, getProducerShows, moderateProducerReview } from "../../api/roleApi";
import { formatPrice } from "../../utils/formatPrice";

const ProducerDashboard = () => {
  const [shows, setShows] = useState([]); const [reviews, setReviews] = useState([]); const [sales, setSales] = useState({});
  const load = async () => { try { const [s, r] = await Promise.all([getProducerShows(), getProducerReviews()]); setShows(s.data); setReviews(r.data); const stats = await Promise.all(s.data.map(async (show) => [show.id, (await getProducerSales(show.id)).data])); setSales(Object.fromEntries(stats)); } catch { toast.error("Unable to load producer workspace"); } };
  useEffect(() => { load(); }, []);
  const moderate = async (id, publish) => { try { await moderateProducerReview(id, publish); await load(); } catch { toast.error("Moderation failed"); } };
  return <section className="page"><div className="page-header"><div><span className="eyebrow">Producer workspace</span><h1>My productions</h1></div></div>
    <div className="card"><h2>Sales</h2>{shows.map((show) => <p key={show.id}><strong>{show.title}</strong> — {sales[show.id]?.totalSeatsSold ?? 0} tickets, {formatPrice(sales[show.id]?.totalRevenue ?? 0)}</p>)}</div>
    <div className="card"><h2>Reviews and press critiques</h2>{reviews.map((review) => <p key={review.id}><strong>{review.showTitle}</strong> · {review.reviewType || "COMMENT"}: {review.comment} <button className="btn btn-sm" onClick={() => moderate(review.id, !review.published)}>{review.published ? "Unpublish" : "Publish"}</button></p>)}</div>
  </section>;
};
export default ProducerDashboard;

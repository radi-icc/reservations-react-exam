import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import { getResource, normaliseCollection } from "../../api";
import { submitCritique } from "../../api/roleApi";

const CriticWorkspace = () => { const [shows, setShows] = useState([]); const [form, setForm] = useState({ showId: "", rating: 5, comment: "", sourceUrl: "" });
  useEffect(() => { getResource("shows", { size: 100 }).then((r) => setShows(normaliseCollection(r.data))).catch(() => toast.error("Unable to load shows")); }, []);
  const submit = async (e) => { e.preventDefault(); try { await submitCritique({ showId: Number(form.showId), rating: Number(form.rating), comment: form.comment }, form.sourceUrl); toast.success("Critique submitted for moderation"); setForm({ showId: "", rating: 5, comment: "", sourceUrl: "" }); } catch { toast.error("Submission failed"); } };
  return <section className="page"><div className="page-header"><div><span className="eyebrow">Press</span><h1>Submit a critique</h1><p>Your article stays unpublished until the producer moderates it.</p></div></div><form className="card form-grid" onSubmit={submit}><select className="form-input" required value={form.showId} onChange={(e) => setForm({...form, showId:e.target.value})}><option value="">Choose a show</option>{shows.map((s) => <option key={s.id} value={s.id}>{s.title}</option>)}</select><input className="form-input" type="number" min="1" max="5" value={form.rating} onChange={(e) => setForm({...form, rating:e.target.value})}/><input className="form-input" placeholder="Article URL (optional)" value={form.sourceUrl} onChange={(e) => setForm({...form, sourceUrl:e.target.value})}/><textarea className="form-input" required placeholder="Your critique" value={form.comment} onChange={(e) => setForm({...form, comment:e.target.value})}/><button className="btn btn-primary">Submit critique</button></form></section>; };
export default CriticWorkspace;

import React, { createContext, useContext, useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Link, Navigate, Route, Routes, useNavigate, useParams, useSearchParams } from 'react-router-dom';
import { Calendar, Search, Ticket, User, Shield, LogOut, Menu, Plus, Trash2, Edit, Download, Upload } from 'lucide-react';
import './styles.css';

const API_BASE = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8085';

function buildQuery(params = {}) {
  const q = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== null && v !== '') q.set(k, v);
  });
  const s = q.toString();
  return s ? `?${s}` : '';
}

async function apiFetch(path, options = {}) {
  const token = localStorage.getItem('token');
  const isFormData = options.body instanceof FormData;
  const headers = {
    ...(isFormData ? {} : { 'Content-Type': 'application/json' }),
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...(options.headers || {}),
  };

  const res = await fetch(`${API_BASE}${path}`, { ...options, headers });
  if (res.status === 204) return null;
  const contentType = res.headers.get('content-type') || '';
  const data = contentType.includes('application/json') ? await res.json() : await res.text();

  if (!res.ok) {
    const message = typeof data === 'string' ? data : data.message || data.error || 'Request failed';
    throw new Error(message);
  }
  return data;
}

const AuthContext = createContext(null);

function AuthProvider({ children }) {
  const [auth, setAuth] = useState(() => {
    const raw = localStorage.getItem('auth');
    return raw ? JSON.parse(raw) : null;
  });
  const [me, setMe] = useState(null);

  useEffect(() => {
    if (!auth?.token) return;
    apiFetch('/api/auth/me').then(setMe).catch(() => logout());
  }, [auth?.token]);

  function saveAuth(data) {
    localStorage.setItem('token', data.token);
    localStorage.setItem('auth', JSON.stringify(data));
    setAuth(data);
  }

  function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('auth');
    setAuth(null);
    setMe(null);
  }

  const value = useMemo(() => ({ auth, me, saveAuth, logout, isAdmin: (me?.roleName || auth?.roleName) === 'ADMIN' }), [auth, me]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

function useAuth() {
  return useContext(AuthContext);
}

function RequireAuth({ children }) {
  const { auth } = useAuth();
  return auth ? children : <Navigate to="/login" replace />;
}

function RequireAdmin({ children }) {
  const { auth, isAdmin } = useAuth();
  if (!auth) return <Navigate to="/login" replace />;
  return isAdmin ? children : <Navigate to="/" replace />;
}

function Layout() {
  const { auth, me, logout, isAdmin } = useAuth();
  return (
    <>
      <nav className="navbar">
        <Link className="brand" to="/"><Ticket size={24} /> ReserShows</Link>
        <div className="navlinks">
          <Link to="/shows">Shows</Link>
          {auth && <Link to="/reservations/me">My reservations</Link>}
          {auth && <Link to="/profile">Profile</Link>}
          {isAdmin && <Link to="/admin">Admin</Link>}
          {!auth ? <Link className="btn small" to="/login">Login</Link> : (
            <button className="btn small ghost" onClick={logout}><LogOut size={16}/> {me?.username || 'Logout'}</button>
          )}
        </div>
      </nav>
      <main className="container"><RoutesView /></main>
      <footer>PID Reservations Project · React frontend · API: {API_BASE}</footer>
    </>
  );
}

function RoutesView() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/shows" element={<ShowsPage />} />
      <Route path="/shows/:id" element={<ShowDetails />} />
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/profile" element={<RequireAuth><Profile /></RequireAuth>} />
      <Route path="/reservations/me" element={<RequireAuth><MyReservations /></RequireAuth>} />
      <Route path="/admin" element={<RequireAdmin><AdminHome /></RequireAdmin>} />
      <Route path="/admin/:resource" element={<RequireAdmin><ResourceAdmin /></RequireAdmin>} />
      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );
}

function Home() {
  return (
    <section className="hero">
      <div>
        <span className="eyebrow">Theatre reservations</span>
        <h1>Browse shows, book performances, and manage the catalogue.</h1>
        <p>Public visitors can search shows. Members can reserve seats and review shows. Admins can manage the full back-office.</p>
        <div className="actions">
          <Link className="btn" to="/shows">Explore shows</Link>
          <Link className="btn secondary" to="/signup">Create account</Link>
        </div>
      </div>
      <div className="heroCard">
        <Calendar size={44}/>
        <h3>Upcoming performances</h3>
        <p>Connects to your backend on port 8085 using JWT Bearer authentication.</p>
      </div>
    </section>
  );
}

function useAsync(fn, deps = []) {
  const [state, setState] = useState({ loading: true, error: '', data: null });
  useEffect(() => {
    let active = true;
    setState(s => ({ ...s, loading: true, error: '' }));
    fn()
      .then(data => active && setState({ loading: false, error: '', data }))
      .catch(e => active && setState({ loading: false, error: e.message, data: null }));
    return () => { active = false; };
  }, deps);
  return state;
}

function ShowsPage() {
  const [params, setParams] = useSearchParams();
  const query = {
    search: params.get('search') || '',
    bookable: params.get('bookable') || '',
    page: Number(params.get('page') || 0),
    size: Number(params.get('size') || 10),
    sortBy: params.get('sortBy') || 'title',
    direction: params.get('direction') || 'asc',
  };
  const { data, loading, error } = useAsync(() => apiFetch('/api/shows' + buildQuery(query)), [params.toString()]);

  function update(next) {
    setParams({ ...query, ...next, page: next.page ?? 0 });
  }

  return (
    <>
      <div className="pageHead">
        <div><h1>Shows</h1><p>Search the catalogue and reserve available performances.</p></div>
      </div>
      <div className="toolbar">
        <label className="search"><Search size={18}/><input placeholder="Search title..." value={query.search} onChange={e => update({ search: e.target.value })}/></label>
        <select value={query.bookable} onChange={e => update({ bookable: e.target.value })}>
          <option value="">All</option><option value="true">Bookable</option><option value="false">Not bookable</option>
        </select>
        <select value={query.sortBy} onChange={e => update({ sortBy: e.target.value })}>
          <option value="title">Title</option><option value="price">Price</option><option value="createdAt">Newest</option>
        </select>
        <select value={query.direction} onChange={e => update({ direction: e.target.value })}>
          <option value="asc">Asc</option><option value="desc">Desc</option>
        </select>
      </div>
      <AsyncState loading={loading} error={error}>
        <div className="grid">
          {(data?.content || []).map(show => <ShowCard key={show.id} show={show} />)}
        </div>
        <Pagination page={data?.number || query.page} totalPages={data?.totalPages || 1} onPage={page => update({ page })}/>
      </AsyncState>
    </>
  );
}

function ShowCard({ show }) {
  return (
    <Link className="card showCard" to={`/shows/${show.id}`}>
      <img src={show.posterUrl || 'https://placehold.co/600x360?text=Show'} alt={show.title} onError={e => { e.currentTarget.src = 'https://placehold.co/600x360?text=Show'; }} />
      <div className="cardBody">
        <h3>{show.title}</h3>
        <p>{show.locationDesignation || 'Venue to be announced'}</p>
        <div className="row between"><span className="badge">{show.bookable ? 'Bookable' : 'Closed'}</span><b>€{Number(show.price || 0).toFixed(2)}</b></div>
      </div>
    </Link>
  );
}

function ShowDetails() {
  const { id } = useParams();
  const { auth } = useAuth();
  const show = useAsync(() => apiFetch(`/api/shows/${id}`), [id]);
  const reps = useAsync(() => apiFetch('/api/representations' + buildQuery({ showId: id, page: 0, size: 50 })), [id]);
  const reviews = useAsync(() => apiFetch('/api/reviews' + buildQuery({ showId: id })), [id]);
  const [reservation, setReservation] = useState({ representationId: '', seats: 1 });
  const [review, setReview] = useState({ rating: 5, comment: '' });
  const [message, setMessage] = useState('');

  async function reserve(e) {
    e.preventDefault();
    setMessage('');
    await apiFetch('/api/reservations', { method: 'POST', body: JSON.stringify({ ...reservation, seats: Number(reservation.seats) }) });
    setMessage('Reservation created successfully.');
  }

  async function submitReview(e) {
    e.preventDefault();
    await apiFetch('/api/reviews', { method: 'POST', body: JSON.stringify({ showId: Number(id), rating: Number(review.rating), comment: review.comment }) });
    setMessage('Review submitted.');
    setReview({ rating: 5, comment: '' });
  }

  return <AsyncState loading={show.loading} error={show.error}>
    <div className="details">
      <img className="poster" src={show.data?.posterUrl || 'https://placehold.co/600x700?text=Show'} />
      <div>
        <span className="badge">{show.data?.bookable ? 'Bookable' : 'Not bookable'}</span>
        <h1>{show.data?.title}</h1>
        <p>{show.data?.description}</p>
        <p><b>Venue:</b> {show.data?.locationDesignation}</p>
        <p><b>Price:</b> €{Number(show.data?.price || 0).toFixed(2)}</p>

        <h2>Representations</h2>
        <AsyncState loading={reps.loading} error={reps.error}>
          <div className="list">
            {(reps.data?.content || reps.data || []).map(r => (
              <label key={r.id} className="listItem">
                <input type="radio" name="representation" value={r.id} onChange={e => setReservation({ ...reservation, representationId: Number(e.target.value) })}/>
                <span>{r.performanceDate} · {r.performanceTime} · {r.locationDesignation || show.data?.locationDesignation}</span>
                <Availability id={r.id}/>
              </label>
            ))}
          </div>
        </AsyncState>

        {auth ? (
          <form className="inlineForm" onSubmit={reserve}>
            <input type="number" min="1" value={reservation.seats} onChange={e => setReservation({ ...reservation, seats: e.target.value })}/>
            <button className="btn" disabled={!reservation.representationId}>Reserve</button>
          </form>
        ) : <Link className="btn" to="/login">Login to reserve</Link>}

        {message && <p className="success">{message}</p>}

        {auth && <form className="panel" onSubmit={submitReview}>
          <h3>Leave a review</h3>
          <select value={review.rating} onChange={e => setReview({ ...review, rating: e.target.value })}>
            {[5,4,3,2,1].map(n => <option key={n} value={n}>{n} stars</option>)}
          </select>
          <textarea placeholder="Your comment" value={review.comment} onChange={e => setReview({ ...review, comment: e.target.value })}/>
          <button className="btn small">Submit review</button>
        </form>}

        <h2>Reviews</h2>
        <AsyncState loading={reviews.loading} error={reviews.error}>
          <div className="list">{(reviews.data?.content || reviews.data || []).map(r => <div className="listItem" key={r.id}><b>{r.rating}/5</b> {r.comment}</div>)}</div>
        </AsyncState>
      </div>
    </div>
  </AsyncState>;
}

function Availability({ id }) {
  const { data } = useAsync(() => apiFetch(`/api/representations/${id}/availability`), [id]);
  if (!data) return null;
  return <span className="muted">{data.availableSeats ?? data.availability ?? ''} seats left</span>;
}

function Login() {
  const { saveAuth } = useAuth();
  const nav = useNavigate();
  const [form, setForm] = useState({ usernameOrEmail: '', password: '' });
  const [error, setError] = useState('');

  async function submit(e) {
    e.preventDefault();
    setError('');
    try {
      const data = await apiFetch('/api/auth/login', { method: 'POST', body: JSON.stringify(form) });
      saveAuth(data);
      nav('/shows');
    } catch (e) { setError(e.message); }
  }

  return <AuthForm title="Login" error={error} onSubmit={submit}>
    <input placeholder="Username or email" value={form.usernameOrEmail} onChange={e => setForm({ ...form, usernameOrEmail: e.target.value })}/>
    <input placeholder="Password" type="password" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })}/>
    <button className="btn full">Login</button>
    <p>No account? <Link to="/signup">Sign up</Link></p>
  </AuthForm>;
}

function Signup() {
  const { saveAuth } = useAuth();
  const nav = useNavigate();
  const [form, setForm] = useState({ username: '', email: '', password: '', confirmPassword: '', firstname: '', lastname: '', language: 'en' });
  const [error, setError] = useState('');
  async function submit(e) {
    e.preventDefault();
    try {
      const data = await apiFetch('/api/auth/signup', { method: 'POST', body: JSON.stringify(form) });
      saveAuth(data);
      nav('/shows');
    } catch (e) { setError(e.message); }
  }
  return <AuthForm title="Create account" error={error} onSubmit={submit}>
    {['username','email','firstname','lastname'].map(k => <input key={k} placeholder={k} value={form[k]} onChange={e => setForm({ ...form, [k]: e.target.value })}/>)}
    <input placeholder="Password" type="password" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })}/>
    <input placeholder="Confirm password" type="password" value={form.confirmPassword} onChange={e => setForm({ ...form, confirmPassword: e.target.value })}/>
    <button className="btn full">Sign up</button>
  </AuthForm>;
}

function AuthForm({ title, error, onSubmit, children }) {
  return <form className="authBox" onSubmit={onSubmit}><h1>{title}</h1>{error && <p className="error">{error}</p>}{children}</form>;
}

function Profile() {
  const { me } = useAuth();
  const [form, setForm] = useState(null);
  useEffect(() => { if (me) setForm(me); }, [me]);
  if (!form) return <p>Loading...</p>;
  async function submit(e) {
    e.preventDefault();
    const data = await apiFetch(`/api/users/${form.userId || form.id}`, { method: 'PUT', body: JSON.stringify(form) });
    setForm(data);
    alert('Profile updated');
  }
  return <form className="panel" onSubmit={submit}><h1>Profile</h1>
    {['username','email','firstname','lastname','language'].map(k => <input key={k} placeholder={k} value={form[k] || ''} onChange={e => setForm({ ...form, [k]: e.target.value })}/>)}
    <button className="btn">Save</button>
  </form>;
}

function MyReservations() {
  const { data, loading, error } = useAsync(() => apiFetch('/api/reservations/me'), []);
  return <><h1>My reservations</h1><AsyncState loading={loading} error={error}>
    <div className="list">{(data?.content || data || []).map(r => <div className="listItem" key={r.id}><b>{r.showTitle}</b><span>{r.performanceDate} {r.performanceTime}</span><span>{r.seats || r.quantity || 1} seat(s)</span></div>)}</div>
  </AsyncState></>;
}

const resources = {
  shows: { label: 'Shows', path: '/api/shows', fields: ['locationId:number','title','posterUrl','bookable:boolean','price:number','description:textarea'] },
  users: { label: 'Users', path: '/api/users', fields: ['username','email','password:password','firstname','lastname','language','enabled:boolean','roleId:number'] },
  roles: { label: 'Roles', path: '/api/roles', fields: ['roleName'] },
  localities: { label: 'Localities', path: '/api/localities', fields: ['postalCode','locality'] },
  locations: { label: 'Locations', path: '/api/locations', fields: ['localityId:number','designation','address','website','phone'] },
  artists: { label: 'Artists', path: '/api/artists', fields: ['firstname','lastname'] },
  'artist-types': { label: 'Artist types', path: '/api/artist-types', fields: ['typeName'] },
  'artist-type-assignments': { label: 'Artist assignments', path: '/api/artist-type-assignments', fields: ['artistId:number','artistTypeId:number'] },
  prices: { label: 'Prices', path: '/api/prices', fields: ['label','amount:number'] },
  representations: { label: 'Representations', path: '/api/representations', fields: ['showId:number','locationId:number','performanceDate:date','performanceTime:time','capacity:number','bookedSeats:number'] },
  collaborations: { label: 'Collaborations', path: '/api/collaborations', fields: ['artistTypeAssignmentId:number','showId:number'] },
  'affiliate-plans': { label: 'Affiliate plans', path: '/api/affiliate-plans', fields: ['name','description','monthlyQuota:number','price:number'] },
};

function AdminHome() {
  async function externalImport() {
    await apiFetch('/api/admin/external-shows/import', { method: 'POST' });
    alert('External import finished');
  }
  async function exportCsv() {
    const token = localStorage.getItem('token');
    const res = await fetch(`${API_BASE}/api/admin/csv/shows/export`, { headers: { Authorization: `Bearer ${token}` } });
    const blob = await res.blob();
    const url = URL.createObjectURL(blob);
    Object.assign(document.createElement('a'), { href: url, download: 'shows.csv' }).click();
  }
  return <>
    <div className="pageHead"><div><h1>Admin dashboard</h1><p>Manage catalogue, users, imports and exports.</p></div></div>
    <div className="actions"><button className="btn" onClick={externalImport}><Upload size={16}/> Import external shows</button><button className="btn secondary" onClick={exportCsv}><Download size={16}/> Export shows CSV</button></div>
    <div className="adminGrid">{Object.entries(resources).map(([key, r]) => <Link className="card adminCard" key={key} to={`/admin/${key}`}><Menu/><h3>{r.label}</h3><p>{r.path}</p></Link>)}</div>
  </>;
}

function ResourceAdmin() {
  const { resource } = useParams();
  const cfg = resources[resource];
  const [reload, setReload] = useState(0);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({});
  const { data, loading, error } = useAsync(() => apiFetch(cfg.path + buildQuery(resource === 'shows' ? { page: 0, size: 100 } : {})), [resource, reload]);

  if (!cfg) return <Navigate to="/admin" />;

  const rows = data?.content || data || [];
  const columns = Array.from(new Set(rows.flatMap(row => Object.keys(row)))).slice(0, 8);

  function reset() { setEditing(null); setForm({}); }
  async function submit(e) {
    e.preventDefault();
    const body = {};
    cfg.fields.forEach(def => {
      const [name, type] = def.split(':');
      let val = form[name];
      if (type === 'number') val = val === '' || val == null ? null : Number(val);
      if (type === 'boolean') val = Boolean(val);
      if (val !== undefined) body[name] = val;
    });
    await apiFetch(editing ? `${cfg.path}/${editing.id}` : cfg.path, { method: editing ? 'PUT' : 'POST', body: JSON.stringify(body) });
    reset();
    setReload(x => x + 1);
  }
  async function remove(id) {
    if (!confirm('Delete this item?')) return;
    await apiFetch(`${cfg.path}/${id}`, { method: 'DELETE' });
    setReload(x => x + 1);
  }

  return <>
    <div className="pageHead"><div><h1>{cfg.label}</h1><p>{cfg.path}</p></div><Link className="btn secondary" to="/admin">Back</Link></div>
    <form className="panel" onSubmit={submit}>
      <h2>{editing ? 'Edit' : 'Create'} {cfg.label}</h2>
      <div className="formGrid">
        {cfg.fields.map(def => <Field key={def} def={def} form={form} setForm={setForm}/>)}
      </div>
      <div className="actions"><button className="btn"><Plus size={16}/>{editing ? 'Update' : 'Create'}</button>{editing && <button type="button" className="btn ghost" onClick={reset}>Cancel</button>}</div>
    </form>
    <AsyncState loading={loading} error={error}>
      <div className="tableWrap"><table><thead><tr>{columns.map(c => <th key={c}>{c}</th>)}<th>Actions</th></tr></thead>
        <tbody>{rows.map(row => <tr key={row.id}>{columns.map(c => <td key={c}>{String(row[c] ?? '')}</td>)}<td><button onClick={() => { setEditing(row); setForm(row); }}><Edit size={16}/></button><button onClick={() => remove(row.id)}><Trash2 size={16}/></button></td></tr>)}</tbody>
      </table></div>
    </AsyncState>
  </>;
}

function Field({ def, form, setForm }) {
  const [name, type = 'text'] = def.split(':');
  if (type === 'textarea') return <textarea placeholder={name} value={form[name] || ''} onChange={e => setForm({ ...form, [name]: e.target.value })}/>;
  if (type === 'boolean') return <label className="check"><input type="checkbox" checked={Boolean(form[name])} onChange={e => setForm({ ...form, [name]: e.target.checked })}/>{name}</label>;
  return <input type={type} placeholder={name} value={form[name] ?? ''} onChange={e => setForm({ ...form, [name]: e.target.value })}/>;
}

function Pagination({ page, totalPages, onPage }) {
  return <div className="pagination"><button disabled={page <= 0} onClick={() => onPage(page - 1)}>Previous</button><span>Page {page + 1} / {totalPages}</span><button disabled={page + 1 >= totalPages} onClick={() => onPage(page + 1)}>Next</button></div>;
}

function AsyncState({ loading, error, children }) {
  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">Error: {error}</div>;
  return children;
}

function App() {
  return <BrowserRouter><AuthProvider><Layout /></AuthProvider></BrowserRouter>;
}

createRoot(document.getElementById('root')).render(<App />);

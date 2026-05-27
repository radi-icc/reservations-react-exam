import { Link } from "react-router-dom";

const NotFound = () => (
  <section className="page center">
    <h1>404</h1>
    <p>The page you are looking for does not exist.</p>
    <Link to="/" className="btn btn-primary">Go home</Link>
  </section>
);

export default NotFound;

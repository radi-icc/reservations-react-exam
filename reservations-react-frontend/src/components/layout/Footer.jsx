import { RSS_FEED_URL } from "../../api/toolsApi";
import { APP_NAME } from "../../utils/constants";

const Footer = () => (
  <footer className="footer">
    <p>{APP_NAME} · Theatre reservations, affiliate catalogue, RSS feed and secure back-office.</p>
    <a href={RSS_FEED_URL} target="_blank" rel="noreferrer">Upcoming performances RSS</a>
  </footer>
);

export default Footer;

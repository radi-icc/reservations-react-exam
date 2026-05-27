const DashboardStats = ({ stats = [] }) => (
  <div className="stats-grid">
    {stats.map((item) => (
      <article className="stat-card" key={item.label}>
        <span>{item.label}</span>
        <strong>{item.value}</strong>
        {item.helper && <small>{item.helper}</small>}
      </article>
    ))}
  </div>
);

export default DashboardStats;

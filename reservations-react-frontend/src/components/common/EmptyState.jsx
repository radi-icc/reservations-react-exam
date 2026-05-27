const EmptyState = ({
  title = "No data found",
  message = "There is nothing to display right now.",
  action,
}) => (
  <div className="empty-state">
    <div className="empty-state-icon">◇</div>
    <h3>{title}</h3>
    <p>{message}</p>
    {action}
  </div>
);

export default EmptyState;

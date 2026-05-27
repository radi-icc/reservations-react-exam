const Input = ({ label, error, helpText, className = "", ...props }) => (
  <div className="form-group">
    {label && <label className="form-label">{label}</label>}
    <input className={`form-input ${error ? "input-error" : ""} ${className}`.trim()} {...props} />
    {helpText && <small className="form-help">{helpText}</small>}
    {error && <p className="form-error">{error}</p>}
  </div>
);

export default Input;

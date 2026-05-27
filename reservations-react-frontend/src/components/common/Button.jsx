const Button = ({
  children,
  type = "button",
  variant = "primary",
  size = "md",
  disabled = false,
  className = "",
  ...props
}) => (
  <button
    type={type}
    disabled={disabled}
    className={`btn btn-${variant} btn-${size} ${className}`.trim()}
    {...props}
  >
    {children}
  </button>
);

export default Button;

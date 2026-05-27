export const isEmail = (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
export const isRequired = (value) => value !== undefined && value !== null && value.toString().trim() !== "";

export const isStrongPassword = (password) =>
  /^(?=.*[A-Z])(?=.*[^A-Za-z0-9]).{6,}$/.test(password || "");

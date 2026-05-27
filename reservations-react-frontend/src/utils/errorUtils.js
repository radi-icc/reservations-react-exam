export const getErrorMessage = (error, fallback = "Something went wrong") => {
  const data = error?.response?.data;

  if (!data) return error?.message || fallback;
  if (typeof data === "string") return data;
  if (data.message) return data.message;

  if (data.validationErrors && typeof data.validationErrors === "object") {
    return Object.values(data.validationErrors).join(". ");
  }

  if (data.error) return data.error;
  return fallback;
};

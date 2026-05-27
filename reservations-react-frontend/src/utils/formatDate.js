export const formatDate = (date) => {
  if (!date) return "-";

  return new Date(date).toLocaleDateString("en-GB", {
    year: "numeric",
    month: "short",
    day: "numeric",
  });
};

export const formatDateTime = (date) => {
  if (!date) return "-";
  return new Date(date).toLocaleString("en-GB");
};

export const formatTime = (time) => {
  if (!time) return "-";
  if (typeof time === "string") return time.slice(0, 5);

  const hour = String(time.hour ?? 0).padStart(2, "0");
  const minute = String(time.minute ?? 0).padStart(2, "0");
  return `${hour}:${minute}`;
};

export const timeObjectToInput = (time) => {
  if (!time) return "";
  if (typeof time === "string") return time.slice(0, 5);
  return `${String(time.hour ?? 0).padStart(2, "0")}:${String(time.minute ?? 0).padStart(2, "0")}`;
};

export const inputToTimeObject = (value) => {
  const [hour = "0", minute = "0"] = String(value || "00:00").split(":");
  return {
    hour: Number(hour),
    minute: Number(minute),
    second: 0,
    nano: 0,
  };
};

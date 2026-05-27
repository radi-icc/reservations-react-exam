export const formatPrice = (price) => {
  if (price === undefined || price === null) return "-";

  return new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
  }).format(price);
};
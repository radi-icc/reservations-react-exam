import axiosClient from "./axiosClient";

export const getAllReservations = () => axiosClient.get("/reservations");
export const getMyReservations = () => axiosClient.get("/reservations/me");
export const getReservationById = (id) => axiosClient.get(`/reservations/${id}`);

export const createReservation = (data) => {
  const payload = {
    representationId: Number(data.representationId),
    priceId: Number(data.priceId),
    quantity: Number(data.quantity),
    ticketDeliveryMethod: data.ticketDeliveryMethod,
    paymentMethod: data.paymentMethod,
  };

  return axiosClient.post("/reservations", payload);
};

export const cancelReservation = (id) => axiosClient.patch(`/reservations/${id}/cancel`);

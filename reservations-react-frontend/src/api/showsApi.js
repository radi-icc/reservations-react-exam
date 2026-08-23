import axiosClient from "./axiosClient";

export const getShows = (params = {}) => axiosClient.get("/shows", { params });
export const getShowById = (id) => axiosClient.get(`/shows/${id}`);
export const getRepresentations = () => axiosClient.get("/representations");
export const getRepresentationById = (id) => axiosClient.get(`/representations/${id}`);
export const getAvailability = (id) => axiosClient.get(`/representations/${id}/availability`);

export const getRepresentationsByShow = (showId) =>
  axiosClient.get("/representations", { params: { showId } });

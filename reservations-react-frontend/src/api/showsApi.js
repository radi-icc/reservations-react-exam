import axiosClient from "./axiosClient";
import { normaliseCollection } from "./adminApi";

export const getShows = (params = {}) => axiosClient.get("/shows", { params });
export const getShowById = (id) => axiosClient.get(`/shows/${id}`);
export const getRepresentations = () => axiosClient.get("/representations");
export const getRepresentationById = (id) => axiosClient.get(`/representations/${id}`);
export const getAvailability = (id) => axiosClient.get(`/representations/${id}/availability`);

export const getRepresentationsByShow = async (showId) => {
  const response = await getRepresentations();
  const list = normaliseCollection(response.data).filter(
    (item) => Number(item.showId) === Number(showId)
  );

  return { ...response, data: list };
};

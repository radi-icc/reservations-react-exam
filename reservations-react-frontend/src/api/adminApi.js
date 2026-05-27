import axiosClient from "./axiosClient";

export const normaliseCollection = (data) => {
  if (Array.isArray(data)) return data;
  if (Array.isArray(data?.content)) return data.content;
  return [];
};

export const getResource = (resource, params) => axiosClient.get(`/${resource}`, { params });
export const getResourceById = (resource, id) => axiosClient.get(`/${resource}/${id}`);
export const createResource = (resource, data) => axiosClient.post(`/${resource}`, data);
export const updateResource = (resource, id, data) => axiosClient.put(`/${resource}/${id}`, data);
export const deleteResource = (resource, id) => axiosClient.delete(`/${resource}/${id}`);
export const patchResource = (resource, id, action) => axiosClient.patch(`/${resource}/${id}/${action}`);

export const getStatistics = () => axiosClient.get("/statistics");
export const getShowSales = (showId) => axiosClient.get(`/statistics/shows/${showId}/sales`);

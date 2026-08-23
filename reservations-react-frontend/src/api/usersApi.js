import axiosClient from "./axiosClient";

export const getUsers = () => axiosClient.get("/users");
export const getUserById = (id) => axiosClient.get(`/users/${id}`);
export const getCurrentUserProfile = () => axiosClient.get("/users/me");
export const createUser = (data) => axiosClient.post("/users", data);
export const updateUser = (id, data) => axiosClient.put(`/users/${id}`, data);
export const updateCurrentUserProfile = (data) => axiosClient.put("/users/me", data);
export const deleteUser = (id) => axiosClient.delete(`/users/${id}`);

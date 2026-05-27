import axiosClient from "./axiosClient";

export const loginUser = (data) => axiosClient.post("/auth/login", data);
export const registerUser = (data) => axiosClient.post("/auth/signup", data);
export const getProfile = () => axiosClient.get("/auth/me");

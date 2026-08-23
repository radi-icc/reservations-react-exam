import axiosClient from "./axiosClient";

export const getReviews = () => axiosClient.get("/reviews");
export const getMyReviews = () => axiosClient.get("/reviews/me");
export const getReviewById = (id) => axiosClient.get(`/reviews/${id}`);
export const createReview = (data) => axiosClient.post("/reviews", data);
export const deleteReview = (id) => axiosClient.delete(`/reviews/${id}`);
export const publishReview = (id) => axiosClient.patch(`/reviews/${id}/publish`);
export const unpublishReview = (id) => axiosClient.patch(`/reviews/${id}/unpublish`);

export const getReviewsByShow = (showId) => axiosClient.get(`/reviews/shows/${showId}`);

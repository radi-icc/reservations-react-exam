import axiosClient from "./axiosClient";

export const getProducerShows = () => axiosClient.get("/producer/shows");
export const getProducerReviews = () => axiosClient.get("/producer/reviews");
export const getProducerSales = (showId) => axiosClient.get(`/producer/shows/${showId}/sales`);
export const moderateProducerReview = (id, publish) => axiosClient.patch(`/producer/reviews/${id}/${publish ? "publish" : "unpublish"}`);
export const submitCritique = (payload, sourceUrl) => axiosClient.post("/critic/reviews", payload, { params: sourceUrl ? { sourceUrl } : {} });

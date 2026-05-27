import axiosClient from "./axiosClient";
import { normaliseCollection } from "./adminApi";

export const getReviews = () => axiosClient.get("/reviews");
export const getReviewById = (id) => axiosClient.get(`/reviews/${id}`);
export const createReview = (data) => axiosClient.post("/reviews", data);
export const deleteReview = (id) => axiosClient.delete(`/reviews/${id}`);
export const publishReview = (id) => axiosClient.patch(`/reviews/${id}/publish`);
export const unpublishReview = (id) => axiosClient.patch(`/reviews/${id}/unpublish`);

export const getReviewsByShow = async (showId, publishedOnly = false) => {
  const response = await getReviews();
  const data = normaliseCollection(response.data).filter((review) => {
    const sameShow = Number(review.showId) === Number(showId);
    return publishedOnly ? sameShow && review.published : sameShow;
  });

  return { ...response, data };
};

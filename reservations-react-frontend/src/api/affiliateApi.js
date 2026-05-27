import axiosClient from "./axiosClient";

export const getAffiliateShows = (apiKey) =>
  axiosClient.get("/affiliate/shows", {
    headers: {
      "X-API-KEY": apiKey,
    },
  });

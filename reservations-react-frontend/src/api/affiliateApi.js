import axiosClient from "./axiosClient";

export const getAffiliateShows = (apiKey) =>
  axiosClient.get("/affiliate/shows", {
    headers: {
      "X-API-KEY": apiKey,
    },
  });

export const getAffiliatePlans = () => axiosClient.get("/affiliate/plans");
export const getMyAffiliateKeys = () => axiosClient.get("/affiliate/me/keys");
export const createMyAffiliateKey = (affiliatePlanId) => axiosClient.post("/affiliate/me/keys", null, { params: { affiliatePlanId } });

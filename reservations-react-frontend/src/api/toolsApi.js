import axiosClient, { API_BASE_URL } from "./axiosClient";

export const importExternalShows = (defaultLocationId) =>
  axiosClient.post("/admin/external-shows/import", null, {
    params: { defaultLocationId: Number(defaultLocationId) },
  });

export const exportShowsCsv = () => axiosClient.get("/admin/csv/shows/export");
export const importShowsCsv = (file) => axiosClient.post("/admin/csv/shows/import", { file });

export const RSS_FEED_URL = `${API_BASE_URL}/rss/upcoming-representations`;

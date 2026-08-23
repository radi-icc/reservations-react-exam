import axiosClient, { API_BASE_URL } from "./axiosClient";

export const importExternalShows = (defaultLocationId) =>
  axiosClient.post("/admin/external-shows/import", null, {
    params: { defaultLocationId: Number(defaultLocationId) },
  });

export const exportShowsCsv = () => axiosClient.get("/admin/csv/shows/export");
export const importShowsCsv = (file) => {
  const formData = new FormData();
  formData.append("file", file);

  return axiosClient.post("/admin/csv/shows/import", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
};

export const RSS_FEED_URL = `${API_BASE_URL}/rss/upcoming-representations`;

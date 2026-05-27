import { useEffect, useState } from "react";
import { getErrorMessage } from "../utils/errorUtils";

const useFetch = (requestFn, dependencies = [], initialValue = []) => {
  const [data, setData] = useState(initialValue);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const fetchData = async () => {
    try {
      setLoading(true);
      const response = await requestFn();
      setData(response.data ?? initialValue);
      setError("");
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, dependencies);

  return { data, setData, loading, error, refetch: fetchData };
};

export default useFetch;

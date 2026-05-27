import { useState } from "react";
import toast from "react-hot-toast";
import { getAffiliateShows } from "../../api/affiliateApi";
import Button from "../../components/common/Button";
import Input from "../../components/common/Input";
import ShowGrid from "../../components/shows/ShowGrid";
import { getErrorMessage } from "../../utils/errorUtils";

const AffiliateCatalogue = () => {
  const [apiKey, setApiKey] = useState("");
  const [shows, setShows] = useState([]);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!apiKey.trim()) {
      toast.error("X-API-KEY is required");
      return;
    }

    try {
      setLoading(true);
      const response = await getAffiliateShows(apiKey.trim());
      setShows(Array.isArray(response.data) ? response.data : []);
      toast.success("Affiliate catalogue loaded");
    } catch (error) {
      toast.error(getErrorMessage(error, "Could not load affiliate catalogue"));
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="page">
      <div className="page-header">
        <div>
          <span className="eyebrow">Affiliate web service</span>
          <h1>Affiliate catalogue API</h1>
          <p>Use an enabled API key to consume the public show catalogue through the authenticated affiliate endpoint.</p>
        </div>
      </div>

      <form className="card api-key-form" onSubmit={handleSubmit}>
        <Input
          label="X-API-KEY"
          value={apiKey}
          onChange={(e) => setApiKey(e.target.value)}
          placeholder="Paste affiliate API key"
          required
        />
        <Button type="submit" disabled={loading}>{loading ? "Loading..." : "Load affiliate shows"}</Button>
      </form>

      <ShowGrid shows={shows} />
    </section>
  );
};

export default AffiliateCatalogue;

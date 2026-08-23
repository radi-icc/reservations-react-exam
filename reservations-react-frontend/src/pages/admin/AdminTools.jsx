import { useState } from "react";
import toast from "react-hot-toast";
import Button from "../../components/common/Button";
import Input from "../../components/common/Input";
import { exportShowsCsv, importExternalShows, importShowsCsv, RSS_FEED_URL } from "../../api/toolsApi";
import { getErrorMessage } from "../../utils/errorUtils";

const AdminTools = () => {
  const [defaultLocationId, setDefaultLocationId] = useState("");
  const [csvFile, setCsvFile] = useState(null);
  const [loading, setLoading] = useState("");

  const handleExternalImport = async () => {
    if (!defaultLocationId) {
      toast.error("Default location is required");
      return;
    }

    try {
      setLoading("external");
      await importExternalShows(defaultLocationId);
      toast.success("External shows imported");
    } catch (error) {
      toast.error(getErrorMessage(error, "External import failed"));
    } finally {
      setLoading("");
    }
  };

  const handleCsvExport = async () => {
    try {
      setLoading("export");
      const response = await exportShowsCsv();
      const content = Array.isArray(response.data) ? response.data.join("\n") : String(response.data || "");
      const blob = new Blob([content], { type: "text/csv;charset=utf-8" });
      const url = URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = "shows.csv";
      document.body.appendChild(link);
      link.click();
      link.remove();
      URL.revokeObjectURL(url);
      toast.success("CSV exported");
    } catch (error) {
      toast.error(getErrorMessage(error, "CSV export failed"));
    } finally {
      setLoading("");
    }
  };

  const handleCsvImport = async () => {
    if (!csvFile) {
      toast.error("Select a CSV file first");
      return;
    }

    try {
      setLoading("import");
      await importShowsCsv(csvFile);
      toast.success("CSV imported");
      setCsvFile(null);
    } catch (error) {
      toast.error(getErrorMessage(error, "CSV import failed"));
    } finally {
      setLoading("");
    }
  };

  return (
    <section className="admin-page">
      <div className="page-header">
        <div>
          <span className="eyebrow">Administration</span>
          <h1>Tools</h1>
          <p>External import, CSV import/export and RSS feed access.</p>
        </div>
      </div>

      <div className="tools-grid">
        <article className="card admin-tool-card">
          <h2>Import external shows</h2>
          <p className="muted-text">Calls the third-party import endpoint with a fallback default location.</p>
          <Input label="Default Location ID" type="number" min="1" value={defaultLocationId} onChange={(e) => setDefaultLocationId(e.target.value)} />
          <Button onClick={handleExternalImport} disabled={loading === "external"}>{loading === "external" ? "Importing..." : "Import External Shows"}</Button>
        </article>

        <article className="card admin-tool-card">
          <h2>CSV Export</h2>
          <p className="muted-text">Export the show catalogue as CSV content.</p>
          <Button onClick={handleCsvExport} disabled={loading === "export"}>{loading === "export" ? "Exporting..." : "Export Shows CSV"}</Button>
        </article>

        <article className="card admin-tool-card wide-card">
          <h2>CSV Import</h2>
          <p className="muted-text">Select a CSV file using the required catalogue format.</p>
          <input className="form-input" type="file" accept=".csv,text/csv" onChange={(e) => setCsvFile(e.target.files?.[0] || null)} />
          {csvFile && <p className="muted-text">Selected file: {csvFile.name}</p>}
          <Button onClick={handleCsvImport} disabled={loading === "import"}>{loading === "import" ? "Importing..." : "Import CSV"}</Button>
        </article>

        <article className="card admin-tool-card">
          <h2>RSS Feed</h2>
          <p className="muted-text">Upcoming representations are exposed as XML through the RSS endpoint.</p>
          <a className="btn btn-outline" href={RSS_FEED_URL} target="_blank" rel="noreferrer">Open RSS feed</a>
        </article>
      </div>
    </section>
  );
};

export default AdminTools;

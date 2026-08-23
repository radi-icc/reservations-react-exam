import CrudPage from "../../components/admin/CrudPage";
import { formatPrice } from "../../utils/formatPrice";

const ManageShows = () => (
  <CrudPage
    title="Shows"
    resource="shows"
    columns={[
      { key: "id", label: "ID" },
      { key: "title", label: "Title" },
      { key: "locationDesignation", label: "Location" },
      { key: "producerName", label: "Producer", render: (r) => r.producerName || "Unassigned" },
      { key: "price", label: "Price", render: (r) => formatPrice(r.price) },
      { key: "bookable", label: "Bookable", render: (r) => (r.bookable ? "Yes" : "No") },
    ]}
    fields={[
      { name: "locationId", label: "Location", type: "select", optionsResource: "locations", optionLabel: "designation", valueType: "number", required: true },
      { name: "producerId", label: "Producer", type: "select", optionsResource: "users", optionLabel: "username", valueType: "number" },
      { name: "title", label: "Title", required: true },
      { name: "posterUrl", label: "Poster URL" },
      { name: "bookable", label: "Bookable", type: "checkbox", defaultValue: true },
      { name: "price", label: "Base Price", type: "number", required: true, min: 0, step: "0.01" },
      { name: "description", label: "Description", type: "textarea" },
    ]}
  />
);

export default ManageShows;

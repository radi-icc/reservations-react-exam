import CrudPage from "../../components/admin/CrudPage";
import { formatTime, inputToTimeObject, timeObjectToInput } from "../../utils/formatDate";

const ManageRepresentations = () => (
  <CrudPage
    title="Representations"
    resource="representations"
    columns={[
      { key: "id", label: "ID" },
      { key: "showTitle", label: "Show" },
      { key: "locationDesignation", label: "Location" },
      { key: "performanceDate", label: "Date" },
      { key: "performanceTime", label: "Time", render: (r) => formatTime(r.performanceTime) },
      { key: "capacity", label: "Capacity" },
      { key: "bookedSeats", label: "Booked" },
      { key: "full", label: "Full", render: (r) => (r.full ? "Yes" : "No") },
    ]}
    fields={[
      { name: "showId", label: "Show", type: "select", optionsResource: "shows", optionLabel: "title", valueType: "number", required: true },
      { name: "locationId", label: "Location", type: "select", optionsResource: "locations", optionLabel: "designation", valueType: "number", required: true },
      { name: "performanceDate", label: "Performance Date", type: "date", required: true },
      { name: "performanceTimeInput", label: "Performance Time", type: "time", required: true, payload: "performanceTime", transform: inputToTimeObject, fromItem: (item) => timeObjectToInput(item.performanceTime) },
      { name: "capacity", label: "Capacity", type: "number", required: true, min: 1 },
      { name: "bookedSeats", label: "Booked Seats", type: "number", defaultValue: 0, min: 0 },
    ]}
  />
);

export default ManageRepresentations;

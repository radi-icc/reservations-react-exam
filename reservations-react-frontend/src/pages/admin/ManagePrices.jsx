import CrudPage from "../../components/admin/CrudPage";
import { formatPrice } from "../../utils/formatPrice";

const ManagePrices = () => (
  <CrudPage
    title="Prices"
    resource="prices"
    columns={[{ key: "id", label: "ID" }, { key: "showTitle", label: "Show" }, { key: "performanceDate", label: "Performance date" }, { key: "label", label: "Label" }, { key: "amount", label: "Amount", render: (r) => formatPrice(r.amount) }]}
    fields={[
      { name: "representationId", label: "Representation", type: "select", optionsResource: "representations", optionLabel: (r) => `${r.showTitle} · ${r.performanceDate} ${String(r.performanceTime || "").slice(0, 5)}`, valueType: "number", required: true },
      { name: "label", label: "Label", required: true },
      { name: "amount", label: "Amount", type: "number", required: true, min: 1, step: "0.01" },
    ]}
  />
);

export default ManagePrices;

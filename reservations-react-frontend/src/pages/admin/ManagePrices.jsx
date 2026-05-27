import CrudPage from "../../components/admin/CrudPage";
import { formatPrice } from "../../utils/formatPrice";

const ManagePrices = () => (
  <CrudPage
    title="Prices"
    resource="prices"
    columns={[{ key: "id", label: "ID" }, { key: "label", label: "Label" }, { key: "amount", label: "Amount", render: (r) => formatPrice(r.amount) }]}
    fields={[{ name: "label", label: "Label", required: true }, { name: "amount", label: "Amount", type: "number", required: true, min: 1, step: "0.01" }]}
  />
);

export default ManagePrices;

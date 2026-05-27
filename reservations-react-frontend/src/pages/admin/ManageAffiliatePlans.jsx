import CrudPage from "../../components/admin/CrudPage";
import { formatPrice } from "../../utils/formatPrice";

const ManageAffiliatePlans = () => (
  <CrudPage
    title="Affiliate Plans"
    resource="affiliate-plans"
    columns={[
      { key: "id", label: "ID" },
      { key: "planName", label: "Plan" },
      { key: "apiLimit", label: "API Limit" },
      { key: "monthlyPrice", label: "Monthly Price", render: (r) => formatPrice(r.monthlyPrice) },
    ]}
    fields={[
      { name: "planName", label: "Plan Name", required: true },
      { name: "apiLimit", label: "API Limit", type: "number", required: true, min: 0 },
      { name: "monthlyPrice", label: "Monthly Price", type: "number", required: true, min: 0, step: "0.01" },
    ]}
  />
);

export default ManageAffiliatePlans;

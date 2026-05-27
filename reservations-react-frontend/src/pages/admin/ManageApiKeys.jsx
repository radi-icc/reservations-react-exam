import CrudPage from "../../components/admin/CrudPage";

const ManageApiKeys = () => (
  <CrudPage
    title="API Keys"
    resource="api-keys"
    canEdit={false}
    columns={[
      { key: "id", label: "ID" },
      { key: "username", label: "User" },
      { key: "email", label: "Email" },
      { key: "affiliatePlanName", label: "Plan" },
      { key: "apiKey", label: "API Key" },
      { key: "enabled", label: "Enabled", render: (r) => (r.enabled ? "Yes" : "No") },
    ]}
    fields={[
      { name: "userId", label: "User", type: "select", optionsResource: "users", optionLabel: (u) => `${u.username} (${u.email})`, valueType: "number", required: true },
      { name: "affiliatePlanId", label: "Affiliate Plan", type: "select", optionsResource: "affiliate-plans", optionLabel: "planName", valueType: "number", required: true },
    ]}
    actions={[
      { name: "enable", label: "Enable" },
      { name: "disable", label: "Disable", variant: "danger" },
    ]}
  />
);

export default ManageApiKeys;

import CrudPage from "../../components/admin/CrudPage";

const ManageUsers = () => (
  <CrudPage
    title="Users"
    resource="users"
    columns={[
      { key: "id", label: "ID" },
      { key: "username", label: "Username" },
      { key: "email", label: "Email" },
      { key: "firstname", label: "First Name" },
      { key: "lastname", label: "Last Name" },
      { key: "roleName", label: "Role" },
      { key: "enabled", label: "Enabled", render: (r) => (r.enabled ? "Yes" : "No") },
    ]}
    fields={[
      { name: "username", label: "Username", required: true },
      { name: "email", label: "Email", type: "email", required: true },
      { name: "password", label: "Password", type: "password", createOnly: true, omitWhenEmpty: true },
      { name: "firstname", label: "First Name" },
      { name: "lastname", label: "Last Name" },
      { name: "language", label: "Language", type: "select", options: [{ id: "en", label: "English" }, { id: "fr", label: "French" }, { id: "nl", label: "Dutch" }], optionLabel: "label", defaultValue: "en", required: true },
      { name: "enabled", label: "Enabled", type: "checkbox", defaultValue: true },
      { name: "roleId", label: "Role", type: "select", optionsResource: "roles", optionLabel: "roleName", valueType: "number", required: true },
    ]}
  />
);

export default ManageUsers;

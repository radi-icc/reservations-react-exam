import CrudPage from "../../components/admin/CrudPage";

const ManageRoles = () => (
  <CrudPage
    title="Roles"
    resource="roles"
    columns={[{ key: "id", label: "ID" }, { key: "roleName", label: "Role Name" }]}
    fields={[{ name: "roleName", label: "Role Name", required: true }]}
  />
);

export default ManageRoles;

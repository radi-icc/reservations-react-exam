import CrudPage from "../../components/admin/CrudPage";

const ManageLocalities = () => (
  <CrudPage
    title="Localities"
    resource="localities"
    columns={[{ key: "id", label: "ID" }, { key: "postalCode", label: "Postal Code" }, { key: "locality", label: "Locality" }]}
    fields={[{ name: "postalCode", label: "Postal Code", required: true }, { name: "locality", label: "Locality", required: true }]}
  />
);

export default ManageLocalities;

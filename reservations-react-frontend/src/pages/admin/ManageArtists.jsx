import CrudPage from "../../components/admin/CrudPage";

const ManageArtists = () => (
  <CrudPage
    title="Artists"
    resource="artists"
    columns={[{ key: "id", label: "ID" }, { key: "firstname", label: "First Name" }, { key: "lastname", label: "Last Name" }]}
    fields={[{ name: "firstname", label: "First Name", required: true }, { name: "lastname", label: "Last Name", required: true }]}
  />
);

export default ManageArtists;

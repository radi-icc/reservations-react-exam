import CrudPage from "../../components/admin/CrudPage";

const ManageArtistTypes = () => (
  <CrudPage
    title="Artist Types"
    resource="artist-types"
    columns={[{ key: "id", label: "ID" }, { key: "typeName", label: "Type Name" }]}
    fields={[{ name: "typeName", label: "Type Name", required: true }]}
  />
);

export default ManageArtistTypes;

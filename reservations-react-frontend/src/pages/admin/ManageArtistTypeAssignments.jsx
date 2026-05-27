import CrudPage from "../../components/admin/CrudPage";

const ManageArtistTypeAssignments = () => (
  <CrudPage
    title="Artist Type Assignments"
    resource="artist-type-assignments"
    columns={[
      { key: "id", label: "ID" },
      { key: "artist", label: "Artist", render: (r) => `${r.artistFirstname || ""} ${r.artistLastname || ""}`.trim() },
      { key: "artistTypeName", label: "Artist Type" },
    ]}
    fields={[
      { name: "artistId", label: "Artist", type: "select", optionsResource: "artists", optionLabel: (a) => `${a.firstname} ${a.lastname}`, valueType: "number", required: true },
      { name: "artistTypeId", label: "Artist Type", type: "select", optionsResource: "artist-types", optionLabel: "typeName", valueType: "number", required: true },
    ]}
  />
);

export default ManageArtistTypeAssignments;

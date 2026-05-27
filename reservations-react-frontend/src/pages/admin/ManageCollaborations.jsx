import CrudPage from "../../components/admin/CrudPage";

const ManageCollaborations = () => (
  <CrudPage
    title="Collaborations"
    resource="collaborations"
    columns={[
      { key: "id", label: "ID" },
      { key: "showTitle", label: "Show" },
      { key: "artist", label: "Artist", render: (r) => `${r.artistFirstname || ""} ${r.artistLastname || ""}`.trim() },
      { key: "artistTypeName", label: "Type" },
    ]}
    fields={[
      { name: "artistTypeAssignmentId", label: "Artist Assignment", type: "select", optionsResource: "artist-type-assignments", optionLabel: (a) => `${a.artistFirstname || ""} ${a.artistLastname || ""} · ${a.artistTypeName || ""}`, valueType: "number", required: true },
      { name: "showId", label: "Show", type: "select", optionsResource: "shows", optionLabel: "title", valueType: "number", required: true },
    ]}
  />
);

export default ManageCollaborations;

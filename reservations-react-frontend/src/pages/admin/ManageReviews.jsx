import CrudPage from "../../components/admin/CrudPage";

const ManageReviews = () => (
  <CrudPage
    title="Reviews"
    resource="reviews"
    canEdit={false}
    columns={[
      { key: "id", label: "ID" },
      { key: "username", label: "User" },
      { key: "showTitle", label: "Show" },
      { key: "rating", label: "Rating" },
      { key: "comment", label: "Comment" },
      { key: "published", label: "Published", render: (r) => (r.published ? "Yes" : "No") },
    ]}
    fields={[
      { name: "showId", label: "Show", type: "select", optionsResource: "shows", optionLabel: "title", valueType: "number", required: true },
      { name: "rating", label: "Rating", type: "number", required: true, min: 1, max: 5 },
      { name: "comment", label: "Comment", type: "textarea", required: true },
    ]}
    actions={[{ name: "publish", label: "Publish" }, { name: "unpublish", label: "Unpublish", variant: "danger" }]}
  />
);

export default ManageReviews;

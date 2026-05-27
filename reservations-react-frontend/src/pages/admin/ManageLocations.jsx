import CrudPage from "../../components/admin/CrudPage";

const ManageLocations = () => (
  <CrudPage
    title="Locations"
    resource="locations"
    columns={[
      { key: "id", label: "ID" },
      { key: "designation", label: "Designation" },
      { key: "localityName", label: "Locality" },
      { key: "address", label: "Address" },
      { key: "phone", label: "Phone" },
    ]}
    fields={[
      { name: "localityId", label: "Locality", type: "select", optionsResource: "localities", optionLabel: (l) => `${l.postalCode} ${l.locality}`, valueType: "number", required: true },
      { name: "designation", label: "Designation", required: true },
      { name: "address", label: "Address" },
      { name: "website", label: "Website" },
      { name: "phone", label: "Phone" },
    ]}
  />
);

export default ManageLocations;

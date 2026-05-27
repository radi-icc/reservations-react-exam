import { useEffect, useMemo, useState } from "react";
import toast from "react-hot-toast";

import {
  createResource,
  deleteResource,
  getResource,
  normaliseCollection,
  patchResource,
  updateResource,
} from "../../api/adminApi";
import { getErrorMessage } from "../../utils/errorUtils";

import AdminFormModal from "./AdminFormModal";
import AdminTable from "./AdminTable";
import Button from "../common/Button";
import Input from "../common/Input";
import Loader from "../common/Loader";

const buildEmptyForm = (fields) => {
  const obj = {};
  fields.forEach((field) => {
    obj[field.name] = field.defaultValue ?? (field.type === "checkbox" ? false : "");
  });
  return obj;
};

const preparePayload = (form, fields, editingItem) => {
  const payload = {};

  fields.forEach((field) => {
    if (field.readOnly) return;
    if (editingItem && field.createOnly) return;

    const rawValue = form[field.name];
    if (field.omitWhenEmpty && (rawValue === "" || rawValue === null || rawValue === undefined)) return;

    const target = field.payload || field.name;
    let value = field.transform ? field.transform(rawValue, form) : rawValue;

    if (field.type === "number" && value !== "" && value !== null && value !== undefined) value = Number(value);
    if (field.type === "checkbox") value = Boolean(value);

    payload[target] = value;
  });

  return payload;
};

const renderOptionLabel = (option, field) => {
  if (typeof field.optionLabel === "function") return field.optionLabel(option);
  if (field.optionLabel) return option[field.optionLabel];
  return option.name || option.title || option.designation || option.roleName || option.label || option.typeName || option.id;
};

const CrudPage = ({
  title,
  resource,
  columns,
  fields,
  actions = [],
  canCreate = true,
  canEdit = true,
  canDelete = true,
  idAccessor = (item) => item.id,
}) => {
  const [data, setData] = useState([]);
  const [form, setForm] = useState(buildEmptyForm(fields));
  const [editingItem, setEditingItem] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [options, setOptions] = useState({});

  const optionResources = useMemo(
    () => [...new Set(fields.filter((field) => field.optionsResource).map((field) => field.optionsResource))],
    [fields]
  );

  const loadData = async () => {
    try {
      setLoading(true);
      const params = resource === "shows" ? { size: 100, sortBy: "title", direction: "asc" } : undefined;
      const response = await getResource(resource, params);
      setData(normaliseCollection(response.data));
    } catch (error) {
      toast.error(getErrorMessage(error, `Failed to load ${title}`));
    } finally {
      setLoading(false);
    }
  };

  const loadOptions = async () => {
    if (!optionResources.length) return;

    const results = await Promise.allSettled(
      optionResources.map((item) => getResource(item, item === "shows" ? { size: 100, sortBy: "title", direction: "asc" } : undefined))
    );
    const nextOptions = {};

    results.forEach((result, index) => {
      if (result.status === "fulfilled") {
        nextOptions[optionResources[index]] = normaliseCollection(result.value.data);
      }
    });

    setOptions(nextOptions);
  };

  useEffect(() => {
    loadData();
    loadOptions();
  }, [resource]);

  const openCreate = () => {
    setEditingItem(null);
    setForm(buildEmptyForm(fields));
    setModalOpen(true);
  };

  const openEdit = (item) => {
    setEditingItem(item);
    const nextForm = {};

    fields.forEach((field) => {
      nextForm[field.name] = field.fromItem ? field.fromItem(item) : item[field.name] ?? field.defaultValue ?? (field.type === "checkbox" ? false : "");
    });

    setForm(nextForm);
    setModalOpen(true);
  };

  const handleChange = (field, value, type) => {
    let nextValue = value;
    if (type === "number") nextValue = value === "" ? "" : Number(value);
    if (type === "checkbox") nextValue = Boolean(value);
    setForm((prev) => ({ ...prev, [field]: nextValue }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      setSaving(true);
      const payload = preparePayload(form, fields, editingItem);

      if (editingItem) {
        await updateResource(resource, idAccessor(editingItem), payload);
        toast.success("Record updated");
      } else {
        await createResource(resource, payload);
        toast.success("Record created");
      }

      setModalOpen(false);
      await loadData();
    } catch (error) {
      toast.error(getErrorMessage(error, "Save failed"));
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this record?")) return;

    try {
      await deleteResource(resource, id);
      toast.success("Record deleted");
      await loadData();
    } catch (error) {
      toast.error(getErrorMessage(error, "Delete failed"));
    }
  };

  const handleAction = async (id, action) => {
    try {
      await patchResource(resource, id, action);
      toast.success("Action completed");
      await loadData();
    } catch (error) {
      toast.error(getErrorMessage(error, "Action failed"));
    }
  };

  if (loading) return <Loader />;

  return (
    <section className="admin-page">
      <div className="page-header">
        <div>
          <span className="eyebrow">Administration</span>
          <h1>{title}</h1>
        </div>
        {canCreate && <Button onClick={openCreate}>Add New</Button>}
      </div>

      <AdminTable
        columns={columns}
        data={data}
        onEdit={canEdit ? openEdit : undefined}
        onDelete={canDelete ? handleDelete : undefined}
        extraActions={actions}
        onAction={handleAction}
      />

      <AdminFormModal
        isOpen={modalOpen}
        title={editingItem ? `Edit ${title}` : `Add ${title}`}
        onClose={() => setModalOpen(false)}
        onSubmit={handleSubmit}
        loading={saving}
      >
        {fields.map((field) => {
          if (editingItem && field.createOnly) return null;

          if (field.type === "checkbox") {
            return (
              <label key={field.name} className="checkbox-field">
                <input type="checkbox" checked={!!form[field.name]} onChange={(e) => handleChange(field.name, e.target.checked, "checkbox")} />
                <span>{field.label}</span>
              </label>
            );
          }

          if (field.type === "textarea") {
            return (
              <div className="form-group" key={field.name}>
                <label className="form-label">{field.label}</label>
                <textarea
                  className="form-input textarea"
                  value={form[field.name] ?? ""}
                  onChange={(e) => handleChange(field.name, e.target.value)}
                  required={field.required}
                  disabled={field.readOnly}
                />
              </div>
            );
          }

          if (field.type === "select") {
            const list = field.options || options[field.optionsResource] || [];
            return (
              <div className="form-group" key={field.name}>
                <label className="form-label">{field.label}</label>
                <select
                  className="form-input"
                  value={form[field.name] ?? ""}
                  onChange={(e) => handleChange(field.name, e.target.value, field.valueType)}
                  required={field.required}
                >
                  <option value="">Select {field.label}</option>
                  {list.map((option) => (
                    <option key={option[field.optionValue || "id"]} value={option[field.optionValue || "id"]}>
                      {renderOptionLabel(option, field)}
                    </option>
                  ))}
                </select>
              </div>
            );
          }

          return (
            <Input
              key={field.name}
              label={field.label}
              type={field.type || "text"}
              value={form[field.name] ?? ""}
              onChange={(e) => handleChange(field.name, e.target.value, field.type)}
              required={field.required}
              disabled={field.readOnly}
              min={field.min}
              max={field.max}
              step={field.step}
            />
          );
        })}
      </AdminFormModal>
    </section>
  );
};

export default CrudPage;

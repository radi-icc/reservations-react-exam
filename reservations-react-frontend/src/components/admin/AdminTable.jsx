import Button from "../common/Button";
import EmptyState from "../common/EmptyState";

const getRowId = (row) => row.id ?? row.reservationId ?? row.userId ?? row.showId;

const AdminTable = ({ columns = [], data = [], onEdit, onDelete, extraActions = [], onAction }) => {
  if (!data.length) {
    return <EmptyState title="No records found" message="Add a new record or adjust the current filters." />;
  }

  return (
    <div className="admin-table-wrapper">
      <table className="admin-table">
        <thead>
          <tr>
            {columns.map((column) => <th key={column.key}>{column.label}</th>)}
            {(onEdit || onDelete || extraActions.length > 0) && <th>Actions</th>}
          </tr>
        </thead>
        <tbody>
          {data.map((row, index) => {
            const id = getRowId(row);
            return (
              <tr key={id ?? index}>
                {columns.map((column) => (
                  <td key={column.key}>{column.render ? column.render(row) : row[column.key] ?? "-"}</td>
                ))}
                {(onEdit || onDelete || extraActions.length > 0) && (
                  <td className="table-actions">
                    {onEdit && <Button size="sm" variant="outline" onClick={() => onEdit(row)}>Edit</Button>}
                    {onDelete && <Button size="sm" variant="danger" onClick={() => onDelete(id)}>Delete</Button>}
                    {extraActions.map((action) => (
                      <Button key={action.name} size="sm" variant={action.variant || "outline"} onClick={() => onAction(id, action.name, row)}>
                        {action.label}
                      </Button>
                    ))}
                  </td>
                )}
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default AdminTable;

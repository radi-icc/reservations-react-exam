import Modal from "../common/Modal";
import Button from "../common/Button";

const AdminFormModal = ({ isOpen, title, children, onClose, onSubmit, loading = false }) => (
  <Modal isOpen={isOpen} title={title} onClose={onClose}>
    <form onSubmit={onSubmit} className="admin-form">
      {children}
      <div className="form-actions">
        <Button type="button" variant="outline" onClick={onClose}>Cancel</Button>
        <Button type="submit" disabled={loading}>{loading ? "Saving..." : "Save"}</Button>
      </div>
    </form>
  </Modal>
);

export default AdminFormModal;

const Pagination = ({ currentPage, totalPages, totalElements, onPageChange }) => {
  if (!totalPages || totalPages <= 1) return null;

  return (
    <nav className="pagination" aria-label="Catalogue pagination">
      <button className="btn btn-outline btn-sm" disabled={currentPage <= 1} onClick={() => onPageChange(currentPage - 1)}>
        Previous
      </button>
      <span aria-live="polite">
        Page {currentPage} of {totalPages}
        {typeof totalElements === "number" ? ` · ${totalElements} records` : ""}
      </span>
      <button className="btn btn-outline btn-sm" disabled={currentPage >= totalPages} onClick={() => onPageChange(currentPage + 1)}>
        Next
      </button>
    </nav>
  );
};

export default Pagination;

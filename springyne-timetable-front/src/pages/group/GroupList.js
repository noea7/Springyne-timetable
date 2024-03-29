import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import {
  Select,
  MenuItem,
  Pagination,
  TextField,
  Collapse,
  Alert,
} from "@mui/material";
import EditTwoToneIcon from "@mui/icons-material/EditTwoTone";
import DeleteTwoToneIcon from "@mui/icons-material/DeleteTwoTone";
import RestoreTwoToneIcon from "@mui/icons-material/RestoreTwoTone";
import VisibilityTwoToneIcon from "@mui/icons-material/VisibilityTwoTone";
import { apiUrl } from "../../App";

function GroupListPage() {
  const [groups, setGroups] = useState({});
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(25);
  const [searchName, setSearchName] = useState("");
  const [searchProgramName, setSearchProgramName] = useState("");
  const [searchYear, setSearchYear] = useState("");
  const [page, setPage] = useState(1);
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);

  const JSON_HEADERS = {
    "Content-Type": "application/json",
  };

  const fetchGroups = () => {
    fetch(
      `${apiUrl}/api/v1/groups/search?name=${searchName}&programName=${searchProgramName}&groupYear=${searchYear}&page=${pageNumber}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setGroups(jsonResponse));
  };

  useEffect(fetchGroups, []);

  const handlePageChange = (e, value) => {
    setPage(value);
    setPageNumber(value - 1);
    fetch(
      `${apiUrl}/api/v1/groups/search?name=${searchName}&programName=${searchProgramName}&groupYear=${searchYear}&page=${
        value - 1
      }&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setGroups(jsonResponse));
  };

  const handlePageSizeChange = (e) => {
    setPageSize(e.target.value);
    setPage(1);
    setPageNumber(0);
    fetch(
      `${apiUrl}/api/v1/groups/search?name=${searchName}&programName=${searchProgramName}&groupYear=${searchYear}&page=${0}&pageSize=${
        e.target.value
      }`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setGroups(jsonResponse));
  };

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(1);
    setPageNumber(0);
    fetch(
      `${apiUrl}/api/v1/groups/search?name=${searchName}&programName=${searchProgramName}&groupYear=${searchYear}&page=${0}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setGroups(jsonResponse));
  };

  const deleteGroup = (id) => {
    fetch(`${apiUrl}/api/v1/groups/delete/` + id, {
      method: "PATCH",
      headers: JSON_HEADERS,
    }).then(fetchGroups);
    setDeleted(true);
    setRestored(false);
    setTimeout(() => {
      setDeleted(false);
    }, 5000);
  };

  const restoreGroup = (id) => {
    fetch(`${apiUrl}/api/v1/groups/restore/` + id, {
      method: "PATCH",
      headers: JSON_HEADERS,
    }).then(fetchGroups);
    setDeleted(false);
    setRestored(true);
    setTimeout(() => {
      setRestored(false);
    }, 5000);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Grupės</h2>
      <Collapse in={deleted}>
        <Alert
          onClose={() => {
            setDeleted(false);
          }}
          severity="info"
          className="mb-3"
        >
          Įrašas sėkmingai ištrintas
        </Alert>
      </Collapse>
      <Collapse in={restored}>
        <Alert
          onClose={() => {
            setRestored(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai atstatytas
        </Alert>
      </Collapse>

      <div className="d-flex justify-content-end">
        <div className="me-auto d-flex">
          <button className="btn btn-primary mb-5 me-2">
            <Link to="/groups/create" className="nav-link">
              Pridėti naują grupę
            </Link>
          </button>
        </div>
        <div className="mb-4">
          <form className="d-flex" role="search">
            <TextField
              onChange={(e) => {
                setSearchName(e.target.value);
                setPageNumber(0);
                setPage(1);
              }}
              value={searchName}
              id="search-name-input"
              label="Ieškoti pagal pavadinimą"
              className="form-control me-2"
              size="small"
            />
            <TextField
              onChange={(e) => {
                setSearchProgramName(e.target.value);
                setPageNumber(0);
                setPage(1);
              }}
              value={searchProgramName}
              id="search-program-input"
              label="Ieškoti pagal programą"
              className="form-control me-2"
              size="small"
            />
            <TextField
              onChange={(e) => {
                setSearchYear(e.target.value);
                setPageNumber(0);
                setPage(1);
              }}
              value={searchYear}
              id="search-year-input"
              label="Ieškoti pagal metus"
              className="form-control me-2"
              size="small"
            />
            <button
              className="btn btn-outline-primary"
              type="submit"
              onClick={(e) => handleSearch(e)}
            >
              Ieškoti
            </button>
          </form>
        </div>
      </div>

      <table className="table table-hover shadow p-3 mb-5 bg-body rounded align-middle">
        <thead className="table-light">
          <tr>
            <th>Pavadinimas</th>
            <th>Programa</th>
            <th>Mokslo metai</th>
            <th>Studentų skaičius</th>
            <th>Būsena</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {groups.content?.map((group) => (
            <tr
              key={group.id}
              id={group.id}
              className={group.deleted ? "text-black-50" : ""}
            >
              <td>{group.name}</td>
              <td>{group.program?.name}</td>
              <td>{group.groupYear}</td>
              <td>{group.students}</td>
              <td>{group.deleted ? "Ištrintas" : ""}</td>
              <td className="text-end">
                <button
                  className="btn btn-outline-primary me-1 my-1 btn-link"
                  title="Žiūrėti"
                >
                  <Link className="nav-link" to={"/groups/view/" + group.id}>
                    <VisibilityTwoToneIcon />
                  </Link>
                </button>

                <button
                  className="btn btn-outline-primary me-1 my-1 btn-link"
                  title="Redaguoti"
                  disabled={group.deleted}
                >
                  <Link className="nav-link" to={"/groups/edit/" + group.id}>
                    <EditTwoToneIcon />
                  </Link>
                </button>

                {group.deleted ? (
                  <button
                    className="btn btn-outline-secondary me-1 my-1 btn-link"
                    title="Atstatyti"
                    onClick={() => restoreGroup(group.id)}
                  >
                    <RestoreTwoToneIcon />
                  </button>
                ) : (
                  <button
                    className="btn btn-danger me-2 my-1 btn-link"
                    title="Ištrinti"
                    onClick={() => deleteGroup(group.id)}
                  >
                    <DeleteTwoToneIcon className="red-icon" />
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
        <tfoot className="table-light">
          <tr>
            <td colSpan={6}>
              {groups.totalElements == "0"
                ? "Įrašų nerasta"
                : `Rasta įrašų: ${groups.totalElements}`}
            </td>
          </tr>
        </tfoot>
      </table>
      <div className="d-flex justify-content-end">
        <div className="mb-4">
          <form className="d-flex" role="search">
            <label htmlFor="page-size-select" className="me-2">
              Puslapyje:
            </label>
            <Select
              id="page-size-select"
              value={pageSize}
              size="small"
              className="me-2"
              onChange={handlePageSizeChange}
            >
              <MenuItem value={10}>10</MenuItem>
              <MenuItem value={25}>25</MenuItem>
              <MenuItem value={50}>50</MenuItem>
              <MenuItem value={100}>100</MenuItem>
            </Select>
          </form>
        </div>
        <div>
          <Pagination
            count={groups.totalPages}
            defaultPage={1}
            siblingCount={0}
            onChange={handlePageChange}
            value={page}
          />
        </div>
      </div>
    </div>
  );
}

export default GroupListPage;

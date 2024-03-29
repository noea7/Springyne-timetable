
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import {
  TextField,
  FormControl,
  Select,
  MenuItem,
  InputLabel,
  Collapse, Alert
} from "@mui/material";
import ClearIcon from "@mui/icons-material/Clear";
import { apiUrl } from "../../App";

function EditTeacherPage() {
  const [teacher, setTeacher] = useState({});
  const [nameError, setNameError] = useState(false);
  const [hoursError, setHoursError] = useState(false);
  const [teamsError, setTeamsError] = useState(false);
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [shifts, setShifts] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [selectedShift, setSelectedShift] = useState("");
  const [selectedSubject, setSelectedSubject] = useState("");
  const params = useParams();

  const fetchTeacher = () => {
    fetch(`${apiUrl}/api/v1/teachers/` + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setTeacher(jsonResponse));
  };

  useEffect(fetchTeacher, [params.id]);

  useEffect(() => {
    fetch(`${apiUrl}/api/v1/subjects/`)
      .then((response) => response.json())
      .then(setSubjects);
  }, []);

  useEffect(() => {
    fetch(`${apiUrl}/api/v1/shifts/`)
      .then((response) => response.json())
      .then(setShifts);
  }, []);

  const deleteSubject = (e) => {
    fetch(`${apiUrl}/api/v1/teachers/${params.id}/removeSubject/${e}`, {
      method: "PATCH",
    }).then(fetchTeacher);
  };

  const editTeacher = (e) => {
    e.preventDefault();
    setNameError(false);
    setTeamsError(false);
    setHoursError(false);
    if (
      teacher.name === "" ||
      teacher.teamsEmail === "" ||
      teacher.hours === ""
    ) {
      if (teacher.name === "") {
        setNameError(true);
      }
      if (teacher.teamsEmail === "") {
        setTeamsError(true);
      }
      if (teacher.hours === "") {
        setHoursError(true);
      }
    } else {
      fetch(
        `${apiUrl}/api/v1/teachers/update/${params.id}?shiftId=${selectedShift}&subjectId=${selectedSubject}`,
        {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(teacher),
        }
      ).then((result) => {
        if (result.ok) {
          setSuccess(true);
          setFailure(false);
          fetchTeacher();
        } else {
          setFailure(true);
          setSuccess(false);
        }
      });
    }
  };
  const updateProperty = (property, event) => {
    setTeacher({
      ...teacher,
      [property]: event.target.value,
    });
  };

  const handleDelete = () => {
    fetch(`${apiUrl}/api/v1/teachers/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setTeacher(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
  };
  const handleRestore = () => {
    fetch(`${apiUrl}/api/v1/teachers/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setTeacher(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Redaguoti mokytoją</h2>

      <Collapse in={success}>
        <Alert
          onClose={() => {
            setSuccess(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai atnaujintas
        </Alert>
      </Collapse>
      <Collapse in={failure}>
        <Alert
          onClose={() => {
            setFailure(false);
          }}
          severity="error"
          className="mb-3"
        >
          Įrašo nepavyko atnaujinti
        </Alert>
      </Collapse>
      <div className="container-fluid shadow p-3 mb-4 mb-md-5 bg-body rounded">
        <form noValidate>
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="edit-module-number-with-error">Vardas ir Pavardė *</label>
            </div>
            <div className="col-md-8 mb-2 mb-md-0">
              <TextField
                error={!!nameError}
                onChange={(e) => updateProperty("name", e)}
                value={teacher.name}
                id="teacher-name-with-error"
                label=""
                helperText="Vardas ir Pavardė privalomi"
                className="form-control mb-3"
                size="small"
                disabled={teacher.deleted}
                InputLabelProps={{ shrink: true }}
                required
              />
            </div>
          </div>
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="edit-module-number-with-error">Teams vartotojo vardas *</label>
            </div>
            <div className="col-md-8 mb-2 mb-md-0">
              <TextField
                error={!!teamsError}
                onChange={(e) => updateProperty("teamsEmail", e)}
                value={teacher.teamsEmail}
                id=""
                label=""
                helperText="Teams vardas privalomas"
                className="form-control mb-3"
                size="small"
                disabled={teacher.deleted}
                InputLabelProps={{ shrink: true }}
                required
              />
            </div>
          </div>
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="edit-module-number-with-error">Kontaktinis el. paštas</label>
            </div>
            <div className="col-md-8 mb-2 mb-md-0">
              <TextField
                onChange={(e) => updateProperty("email", e)}
                value={teacher.email}
                id="create-teacher-email"
                label=""
                className="form-control mb-3"
                size="small"
                disabled={teacher.deleted}
                InputLabelProps={{ shrink: true }}
              />
            </div>
          </div>
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="edit-module-number-with-error">Kontaktinis telefonas</label>
            </div>
            <div className="col-md-8 mb-2 mb-md-0">
              <TextField
                onChange={(e) => updateProperty("phone", e)}
                value={teacher.phone}
                id="create-teacher-phone"
                label=""
                className="form-control mb-3"
                size="small"
                disabled={teacher.deleted}
                InputLabelProps={{ shrink: true }}
              />
            </div>
          </div>
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="edit-module-number-with-error">Valandų skaičius per savaitę*</label>
            </div>
            <div className="col-md-8 mb-2 mb-md-0">
              <TextField
                error={!!hoursError}
                onChange={(e) => updateProperty("hours", e)}
                value={teacher.hours}
                id="create-teacher-hours"
                label=""
                helperText="Valandų skaičius privalomas"
                className="form-control mb-2"
                size="small"
                disabled={teacher.deleted}
                InputLabelProps={{ shrink: true }}
                required
              />
            </div>
          </div>
        </form>
        <div className="row">
          <div className="col-md-4 mb-2 mb-md-0 fw-bold">
            {teacher.subjects?.length === 0 ? "" : <div>Pašalinti dalyką</div>}
          </div>
          <div className="col-md-8 mb-2">
            {teacher.subjects?.map((subject) => (
              <button
                type="submit"
                className="btn btn-light me-2 mb-2"
                value={subject.id}
                onClick={(e) => deleteSubject(e.target.value)}
                key={subject.id}
                id={subject.id}
                disabled={teacher.deleted}
              >
                {subject.name}{" "}
                <ClearIcon
                  color="disabled"
                  sx={{ fontSize: 12 }}
                  value={subject.id}
                  onClick={(e) => deleteSubject(subject.id)}
                  key={`clearIcon-${subject.id}`}
                  id={`clearIcon-${subject.id}`}
                />
              </button>
            ))}
          </div>
        </div>


        <div className="row">
          <div className="col-md-4 mb-2 mb-md-0 fw-bold">
            <label htmlFor="edit-module-number-with-error">Pridėti dalyką *</label>
          </div>
          <div className="col-md-8 mb-2 mb-md-0">
            <FormControl fullWidth size="small" className="my-3">
              <InputLabel id="select-subject-label">Pridėti dalyką</InputLabel>
              <Select
                disabled={teacher.deleted}
                labelId="select-subject-label"
                InputLabelProps={{ shrink: true }}
                id="add-select-subject"
                label="Pridėti dalyką"
                fullWidth
                value={selectedSubject}
                onChange={(e) => setSelectedSubject(e.target.value)}
              >
                {subjects?.map((subject) => (
                  <MenuItem
                    value={subject.id}
                    key={subject.id}
                    disabled={subject.deleted}
                  >
                    {subject.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </div>
        </div>

        <div className="row">
          <div className="col-md-4 mb-2 mb-md-0 fw-bold">
            <label htmlFor="edit-module-number-with-error">Pamaina *</label>
          </div>
          <div className="col-md-8 mb-2 mb-md-0">
            <FormControl fullWidth size="small" className="mb-3">
              <InputLabel id="select-module-label">
                {teacher.shift?.name}  </InputLabel>
              <Select
                disabled={teacher.deleted}
                labelId="select-shift-label"
                InputLabelProps={{ shrink: true }}
                id="select-shift"
                label="Pamaina"

                fullWidth
                value={selectedShift}
                onChange={(e) => setSelectedShift(e.target.value)}
              >
                {shifts?.map((subject) => (
                  <MenuItem value={subject.id} key={subject.id}>
                    {subject.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </div>
        </div>
        <div className="row mb-md-4">
          <div className="col-md-4 mb-2 mb-md-0 fw-bold">Būsena</div>
          <div className="col-md-8 mb-2 mb-md-0">
            {teacher.deleted ? "Ištrintas" : "Aktyvus"}
          </div>
        </div>
        <div className="row mb-md-4">
          <div className="col-md-4 mb-2 mb-md-0 fw-bold">
            Paskutinį kartą modifikuotas
          </div>
          <div className="col-md-8 mb-2 mb-md-0">{teacher.modifiedDate}</div>
        </div>


      </div>
      <div>

        <button
          type="submit"
          className="btn btn-primary me-2 mt-2 mb-5"
          onClick={editTeacher}
          disabled={teacher.deleted}
        >
          Redaguoti
        </button>
        {teacher.deleted ? (
          <button
            className="btn btn-secondary me-2 mt-2 mb-5"
            onClick={handleRestore}
          >
            Atstatyti
          </button>
        ) : (
          <button
            className="btn btn-danger me-2 mt-2 mb-5"
            onClick={handleDelete}
          >
            Ištrinti
          </button>
        )}
      </div>
    </div>
  );
}

export default EditTeacherPage;


import React from "react";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import { FormControl, InputLabel, TextField, Select, MenuItem } from "@mui/material";
import { DatePicker } from "@mui/x-date-pickers";
import { useState, useEffect } from "react";
import dayjs from "dayjs";
import { apiUrl } from "../App";


function TeacherLessonToPdf(props) {
    const { open, handleClose, title } = props;
    const [startDateValue, setStartDateValue] = useState(null);
    const [endDateValue, setEndDateValue] = useState(null);
    const [startDateError, setStartDateError] = useState(false);
    const [endDateError, setEndDateError] = useState(false);
    const [teacherError, setTeacherError] = useState("");
    const [selectedTeacher, setSelectedTeacher] = useState("");
    const [teachers, setTeachers] = useState([]);

    const fetchTeachers = () => {

        fetch(
            `${apiUrl}/api/v1/teachers/`
        )
            .then((response) => response.json())
            .then((jsonResponse) => setTeachers(jsonResponse));
    };

    useEffect(fetchTeachers, []);

    const lessonstoPdf = (e) => {
        e.preventDefault();
        setStartDateError(false);
        setEndDateError(false);
        setTeacherError(false);
        const starts = dayjs(startDateValue).format("YYYY-MM-DD");
        const ends = dayjs(endDateValue).format("YYYY-MM-DD");
        if (selectedTeacher === null ||
            selectedTeacher === "" ||
            startDateValue === null ||
            endDateValue === null ||
            startDateValue > endDateValue) {
            if (selectedTeacher === "" || selectedTeacher === null) {
                setTeacherError(true);
            }
            if (
                startDateValue === null ||
                (startDateValue > endDateValue && endDateValue !== null)
            ) {
                setStartDateError(true);
            }
            if (
                endDateValue === null ||
                endDateValue < startDateValue
            ) {
                setEndDateError(true);
            }
        } else {

            fetch(`${apiUrl}/api/v1/lessons/teachers/export/pdf?teacherId=${selectedTeacher}&startDate=${starts}&endDate=${ends}`)
                .then(response => {
                    if (response.ok) {
                        setStartDateValue(null);
                        setEndDateValue(null);
                        setSelectedTeacher("");
                        setStartDateError(false);
                        setEndDateError(false);
                        setTeacherError(false);
                        handleClose();
                        return response.blob();
                    }
                    throw new Error('Network response was not ok.');
                })
                .then(blob => {
                    const url = window.URL.createObjectURL(blob);
                    window.open(url);
                    let link = document.createElement('a');
                    link.href = url;
                    link.download = `TeacherPDF`;
                    link.click();
                })
                .catch(error => {
                    console.error('There was a problem with the fetch operation:', error);
                });
        }
    };

    return (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>{title}</DialogTitle>
            <DialogContent>
                <form noValidate>
                    <DatePicker
                        className="mb-3 mt-2"
                        label="Pradžios data"
                        inputFormat="yyyy-MM-dd"
                        value={startDateValue}
                        onChange={(newValue) => {
                            setStartDateValue(newValue);
                        }}
                        renderInput={(params) => (
                            <TextField
                                fullWidth
                                size="small"
                                required
                                {...params}
                                error={!!startDateError}
                            />
                        )}
                    />
                    <DatePicker
                        className="mb-3"
                        label="Pabaigos data"
                        inputFormat="yyyy-MM-dd"
                        value={endDateValue}
                        minDate={
                            startDateValue !== null ? startDateValue : ""
                        }
                        onChange={(newValue) => {
                            setEndDateValue(newValue);
                        }}
                        onError={() => setEndDateError(true)}
                        renderInput={(params) => (
                            <TextField
                                fullWidth
                                size="small"
                                required
                                {...params}
                                error={!!endDateError}
                            />
                        )}
                    />
                    <FormControl fullWidth size="small" className="mb-3" required>
                        <InputLabel
                            id="select-teacher-label"
                            error={!!teacherError}
                            required
                        >
                            Pasirinkite mokytoją
                        </InputLabel>
                        <Select
                            error={!!teacherError}
                            labelId="select-teacher-label"
                            id="select-teacher"
                            label="Pasirinkite mokytoją"
                            fullWidth
                            value={selectedTeacher}
                            onChange={(e) => setSelectedTeacher(e.target.value)}
                            required
                        >
                            {teachers?.map((teacher) => (
                                <MenuItem
                                    value={teacher.id}
                                    key={teacher.id}
                                    disabled={teacher.deleted}
                                >
                                    {teacher.name}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                </form>
            </DialogContent>
            <DialogActions>
                <button
                    className="btn btn-primary me-3 mb-2"
                    onClick={lessonstoPdf}
                >
                    Spausdinti
                </button>
                <button
                    className="btn btn-danger me-3 mb-2"
                    onClick={() => {
                        handleClose();
                        setStartDateValue(null);
                        setEndDateValue(null);
                        setSelectedTeacher("");
                        setStartDateError(false);
                        setEndDateError(false);
                        setTeacherError(false);
                    }}
                >
                    Atšaukti
                </button>
            </DialogActions>

        </Dialog>
    );
}
export default TeacherLessonToPdf;
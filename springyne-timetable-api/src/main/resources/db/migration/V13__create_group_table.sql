CREATE TABLE IF NOT EXISTS GROUP_TABLE (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    GROUP_YEAR VARCHAR(255),
    STUDENTS INTEGER,
    PROGRAM_ID BIGINT,
    SHIFT_ID BIGINT,
    DELETED BOOLEAN DEFAULT FALSE,
    MODIFIED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

    CREATE TABLE IF NOT EXISTS TEACHERS_AND_SUBJECTS (
    	TEACHER_ID BIGINT,
    	SUBJECT_ID BIGINT
    );
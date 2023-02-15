CREATE TABLE SHIFT (
	ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
	NAME CHARACTER VARYING(255),
	STARTS INTEGER,
	ENDS INTEGER,
	LAST_UPDATED TIMESTAMP,
	VISIBLE INTEGER,

	CONSTRAINT SHIFT_PK PRIMARY KEY (ID)
)
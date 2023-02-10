CREATE TABLE IF NOT EXISTS MODULE_TABLE (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
    NUMBER VARCHAR(255),
    NAME VARCHAR(255),
    DELETED BOOLEAN,
    MODIFIED_DATE TIMESTAMP,

    CONSTRAINT BLOG_ENTRY_PK PRIMARY KEY (ID)
);
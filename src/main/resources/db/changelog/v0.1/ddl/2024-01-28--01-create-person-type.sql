--liquibase formatted sql

--changeset pdrozd:create-person-type
DO
'
DECLARE
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = ''person_type'') THEN
        CREATE TYPE person_type AS ENUM (''TENANT'', ''OWNER'');
    END IF;
END;
' LANGUAGE PLPGSQL;
--rollback drop type person_type;
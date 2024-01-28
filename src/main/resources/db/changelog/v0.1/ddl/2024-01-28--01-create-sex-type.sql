--liquibase formatted sql

--changeset pdrozd:create-sex-type
DO
'
DECLARE
BEGIN
    IF NOT EXISTS (SELECT * FROM pg_type WHERE typname = ''sex_type'') THEN
		CREATE TYPE sex_type AS ENUM (''MALE'', ''FEMALE'');
    END IF;
END;
' LANGUAGE PLPGSQL;
--rollback drop type sex_type;
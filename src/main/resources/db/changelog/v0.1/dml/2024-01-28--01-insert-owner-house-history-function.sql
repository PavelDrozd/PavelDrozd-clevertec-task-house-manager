--liquibase formatted sql

--changeset pdrozd:insert-person-house-history-function
CREATE OR REPLACE FUNCTION insert_owner_house_history_function()
RETURNS TRIGGER AS '
BEGIN
    IF TG_OP = ''INSERT'' THEN
        INSERT INTO house_histories (house_id, person_id, date, person_type)
        VALUES (NEW.house_id, NEW.person_id, (SELECT p.create_date FROM persons p WHERE id = NEW.person_id), ''OWNER'');
    END IF;
    RETURN NEW;
END;
' LANGUAGE plpgsql;
--rollback drop function insert_owner_house_history_function;
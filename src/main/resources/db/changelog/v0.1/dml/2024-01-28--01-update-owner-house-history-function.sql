--liquibase formatted sql

--changeset pdrozd:update-owner-house-history-function
CREATE OR REPLACE FUNCTION update_owner_house_history_function()
RETURNS TRIGGER AS '
BEGIN
    IF TG_OP = ''UPDATE'' THEN
        INSERT INTO house_histories (house_id, person_id, date, person_type)
        VALUES (NEW.house_id, NEW.person_id, (SELECT p.update_date FROM persons p WHERE p.id = NEW.person_id), ''OWNER'');
    END IF;
    RETURN NEW;
END;
' LANGUAGE plpgsql;
--rollback drop function update_owner_house_history_function;
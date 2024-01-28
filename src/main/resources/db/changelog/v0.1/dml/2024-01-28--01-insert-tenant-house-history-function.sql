--liquibase formatted sql

--changeset pdrozd:insert-person-house-history-function
CREATE OR REPLACE FUNCTION insert_tenant_house_history_function()
RETURNS TRIGGER AS '
BEGIN
    IF TG_OP = ''INSERT'' THEN
        INSERT INTO house_histories (house_id, person_id, date, person_type)
        VALUES (NEW.house_id, NEW.id, NEW.create_date, ''TENANT'');
    END IF;
    RETURN NEW;
END;
' LANGUAGE plpgsql;
--rollback drop function insert_tenant_house_history_function;
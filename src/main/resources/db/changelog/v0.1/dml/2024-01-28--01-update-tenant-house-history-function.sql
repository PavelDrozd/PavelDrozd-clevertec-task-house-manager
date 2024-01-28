--liquibase formatted sql

--changeset pdrozd:update-tenant-house-history-function
CREATE OR REPLACE FUNCTION update_tenant_house_history_function()
RETURNS TRIGGER AS '
BEGIN
    IF TG_OP = ''UPDATE'' THEN
        INSERT INTO house_histories (house_id, person_id, date, person_type)
        VALUES (NEW.house_id, OLD.id, NEW.update_date, ''TENANT'');
    END IF;
    RETURN NEW;
END;
' LANGUAGE plpgsql;
--rollback drop function update_tenant_house_history_function;
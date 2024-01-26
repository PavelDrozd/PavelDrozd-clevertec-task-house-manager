CREATE OR REPLACE FUNCTION update_tenant_house_history_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'UPDATE' THEN
        INSERT INTO house_histories (house_id, person_id, "date", type)
        VALUES (NEW.house_id, OLD.id, NEW.update_date, 'TENANT');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION update_owner_house_history_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'UPDATE' THEN
        INSERT INTO house_histories (house_id, person_id, "date", type_id)
        VALUES (NEW.house_id, NEW.person_id, (SELECT p.update_date FROM persons p WHERE p.id = NEW.person_id), (SELECT id FROM types WHERE name = 'OWNER'));
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
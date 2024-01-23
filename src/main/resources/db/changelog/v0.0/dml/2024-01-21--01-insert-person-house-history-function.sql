CREATE OR REPLACE FUNCTION insert_person_house_history_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO house_histories (house_id, person_id, "date", type_id)
        VALUES (NEW.house_id, NEW.id, NEW.create_date, (SELECT id FROM types WHERE name = 'TENANT'));
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
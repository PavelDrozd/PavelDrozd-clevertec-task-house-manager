CREATE OR REPLACE FUNCTION insert_person_house_history_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO house_histories (house_id, person_id, "date", type)
        VALUES (NEW.house_id, NEW.id, NEW.create_date, 'TENANT');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
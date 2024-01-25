CREATE TRIGGER insert_person_house_history_trigger
AFTER INSERT ON persons
FOR EACH ROW
EXECUTE FUNCTION insert_person_house_history_function();
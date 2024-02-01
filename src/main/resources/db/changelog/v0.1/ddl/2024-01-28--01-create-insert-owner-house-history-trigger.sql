--liquibase formatted sql

--changeset pdrozd:create-insert-person-house-history-trigger
CREATE TRIGGER insert_owner_house_history_trigger
AFTER INSERT ON owner_house
FOR EACH ROW
EXECUTE FUNCTION insert_owner_house_history_function();
--rollback drop trigger insert_owner_house_history_trigger on persons;
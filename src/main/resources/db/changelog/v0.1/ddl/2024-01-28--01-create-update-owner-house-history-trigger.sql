--liquibase formatted sql

--changeset pdrozd:create-update-owner-house-history-trigger
CREATE TRIGGER update_owner_house_history_trigger
AFTER UPDATE OF house_id, person_id ON owner_house
FOR EACH ROW
EXECUTE FUNCTION update_owner_house_history_function();
--rollback drop trigger update_owner_house_history_trigger on persons;
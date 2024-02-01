--liquibase formatted sql

--changeset pdrozd:create-insert-person-house-history-trigger
CREATE TRIGGER insert_tenant_house_history_trigger
AFTER INSERT ON persons
FOR EACH ROW
EXECUTE FUNCTION insert_tenant_house_history_function();
--rollback drop trigger insert_tenant_house_history_trigger on persons;
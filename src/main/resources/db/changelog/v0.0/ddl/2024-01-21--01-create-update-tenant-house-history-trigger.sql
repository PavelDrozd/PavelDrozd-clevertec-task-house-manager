CREATE TRIGGER update_tenant_house_history_trigger
AFTER UPDATE OF house_id ON persons
FOR EACH ROW
EXECUTE FUNCTION update_tenant_house_history_function();
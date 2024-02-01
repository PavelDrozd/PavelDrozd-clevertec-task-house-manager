--liquibase formatted sql

--changeset pdrozd:create-house-histories-table
CREATE TABLE IF NOT EXISTS house_histories (
	"id" BIGSERIAL PRIMARY KEY,
	house_id BIGINT NOT NULL REFERENCES houses,
	person_id BIGINT NOT NULL REFERENCES persons,
    "date" TIMESTAMP NOT NULL,
    person_type person_type NOT NULL
);
--rollback drop table house_histories;
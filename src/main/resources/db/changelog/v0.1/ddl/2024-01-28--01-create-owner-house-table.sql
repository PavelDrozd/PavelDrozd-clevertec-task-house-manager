--liquibase formatted sql

--changeset pdrozd:create-owner-house-table
CREATE TABLE IF NOT EXISTS  owner_house (
    person_id BIGINT NOT NULL REFERENCES persons,
    house_id BIGINT NOT NULL REFERENCES houses
);
--rollback drop table owner_house;
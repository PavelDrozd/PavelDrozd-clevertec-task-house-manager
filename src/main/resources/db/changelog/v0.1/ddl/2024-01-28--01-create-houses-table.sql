--liquibase formatted sql

--changeset pdrozd:create-houses-table
CREATE TABLE IF NOT EXISTS  houses (
	"id" BIGSERIAL PRIMARY KEY,
    "uuid" UUID UNIQUE NOT NULL,
	country VARCHAR(30) NOT NULL,
	area VARCHAR(30) NOT NULL,
	city VARCHAR(30) NOT NULL,
	street VARCHAR(50) NOT NULL,
	"number" VARCHAR(10) NOT NULL,
    create_date TIMESTAMP NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);
--rollback drop table houses;
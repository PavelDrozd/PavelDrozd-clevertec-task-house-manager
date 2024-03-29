/*
CREATE DATABASE "house-manager";
*/

CREATE TABLE IF NOT EXISTS  houses (
	"id" BIGSERIAL PRIMARY KEY,
    "uuid" UUID UNIQUE NOT NULL,
	country VARCHAR(30) NOT NULL,
	area VARCHAR(30) NOT NULL,
	city VARCHAR(30) NOT NULL,
	street VARCHAR(50) NOT NULL,
	"number" VARCHAR(10) NOT NULL,
    create_date TIMESTAMP NOT NULL,
	deleted boolean NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS  sex (
	id INT4 PRIMARY KEY,
	name varchar(10) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS  persons (
    "id" BIGSERIAL PRIMARY KEY,
    "uuid" UUID UNIQUE NOT NULL,
    "name" VARCHAR(30) NOT NULL,
    surname VARCHAR(30) NOT NULL,
    sex_id INT4 NOT NULL REFERENCES sex,
    passport_series varchar(4) NOT NULL,
    passport_number varchar(8) NOT NULL,
    create_date TIMESTAMP NOT NULL,
    update_date TIMESTAMP NOT NULL,
    house_id BIGINT NOT NULL REFERENCES houses,
    deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS  owner_house (
    person_id BIGINT NOT NULL REFERENCES persons,
    house_id BIGINT NOT NULL REFERENCES houses
);

ALTER TABLE persons ADD CONSTRAINT unique_passport_series_number UNIQUE (passport_series, passport_number);

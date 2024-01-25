/*
DROP DATABASE "house-manager";
*/

ALTER TABLE IF EXISTS persons DROP CONSTRAINT unique_passport_series_number;
DROP TABLE IF EXISTS owner_house;
DROP TABLE IF EXISTS persons;
DROP TABLE IF EXISTS sex;
DROP TABLE IF EXISTS houses;
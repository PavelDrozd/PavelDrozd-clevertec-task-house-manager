CREATE TABLE IF NOT EXISTS  house_histories (
	"id" BIGSERIAL PRIMARY KEY,
	house_id BIGINT NOT NULL REFERENCES houses,
	person_id BIGINT NOT NULL REFERENCES persons,
    "date" TIMESTAMP NOT NULL,
    type person_type NOT NULL
);
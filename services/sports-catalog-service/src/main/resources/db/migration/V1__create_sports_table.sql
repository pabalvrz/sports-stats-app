CREATE TABLE sports (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    active BOOLEAN NOT NULL
);

CREATE UNIQUE INDEX uk_sports_name_lower
    ON sports (lower(name));
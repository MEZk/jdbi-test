CREATE TABLE persons(
  person_id UUID PRIMARY KEY,
  name VARCHAR,
  age INTEGER
);

CREATE TABLE documents(
  document_id UUID PRIMARY KEY,
  person_id UUID REFERENCES persons (person_id),
  code VARCHAR
);
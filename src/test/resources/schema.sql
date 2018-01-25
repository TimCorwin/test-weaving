
DROP TABLE mappings;
DROP TABLE books;
  DROP TABLE authors;
  DROP TABLE genres;

  CREATE TABLE authors (

  id        BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  firstname VARCHAR,
  lastname  VARCHAR
);

CREATE TABLE books (

  id     BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  title  VARCHAR,
  description VARCHAR,
  author BIGINT REFERENCES authors (id)
);

CREATE TABLE genres (

  id          BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  name        VARCHAR,
  description VARCHAR
);

CREATE TABLE mappings (

  id    BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  genre BIGINT REFERENCES genres (id),
  book  BIGINT REFERENCES books (id)
);
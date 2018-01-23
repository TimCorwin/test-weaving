DROP TABLE books;
DROP TABLE authors;

  CREATE TABLE authors (

    id        BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    firstname VARCHAR,
    lastname VARCHAR
  );

  CREATE TABLE books (

    id        BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title VARCHAR,
    author BIGINT REFERENCES authors (id)
  );

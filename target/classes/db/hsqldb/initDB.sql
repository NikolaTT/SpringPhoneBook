 DROP TABLE customers IF EXISTS;

 CREATE TABLE customers(
    id SERIAL,
    username VARCHAR(255),
    password VARCHAR(255)
    );
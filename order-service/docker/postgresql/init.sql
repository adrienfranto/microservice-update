-- docker/postgresql/init.sql

CREATE ROLE postgres WITH LOGIN PASSWORD 'postgres';


CREATE DATABASE order_service OWNER postgres;

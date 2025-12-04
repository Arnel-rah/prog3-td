--  create database
CREATE DATABASE product_management_db;

--  create user
CREATE USER product_manager_user WITH PASSWORD '123456';

--  grant privileges on the database
GRANT ALL PRIVILEGES ON DATABASE product_management_db TO product_manager_user;

--  allow user to create tables
GRANT CREATE ON DATABASE product_management_db TO product_manager_user;

--  give CRUD permissions on all future tables
ALTER DEFAULT PRIVILEGES
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO product_manager_user;


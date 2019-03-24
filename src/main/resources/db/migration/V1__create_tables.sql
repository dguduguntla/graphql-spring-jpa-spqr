--create tables
BEGIN;

CREATE TABLE region
(
  region_id   SERIAL primary key,
  region_name VARCHAR(25)
);

CREATE TABLE country
(
  country_id   SERIAL PRIMARY KEY,
  country_code VARCHAR(2) not null,
  country_name VARCHAR(40),
  region_id    INTEGER REFERENCES region (region_id)
);

CREATE TABLE location
(
  location_id    SERIAL PRIMARY KEY
  ,
  street_address VARCHAR(40)
  ,
  postal_code    VARCHAR(12)
  ,
  city           VARCHAR(30) NOT NULL
  ,
  state_province VARCHAR(25)
  ,
  country_id     INTEGER REFERENCES country (country_id)
);

CREATE TABLE department
(
  department_id   SERIAL PRIMARY KEY
  ,
  department_name VARCHAR(30) NOT NULL
  ,
  location_id     INTEGER references location (location_id)
);

CREATE TABLE employee
(
  employee_id   SERIAL PRIMARY KEY
  ,
  first_name    VARCHAR(20)
  ,
  last_name     VARCHAR(25) NOT NULL
  ,
  email         VARCHAR(25) NOT NULL
  ,
  department_id INTEGER REFERENCES department (department_id)
);

--create indexes
CREATE INDEX emp_department_ix
  ON employee (department_id);

CREATE INDEX emp_name_ix
  ON employee (last_name, first_name);

CREATE INDEX dept_location_ix
  ON department (location_id);

CREATE INDEX loc_city_ix
  ON location (city);

CREATE INDEX loc_state_province_ix
  ON location (state_province);

CREATE INDEX loc_country_ix
  ON location (country_id);

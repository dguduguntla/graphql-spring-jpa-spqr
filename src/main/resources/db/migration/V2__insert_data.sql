--insert values into region table
INSERT INTO region (region_name) VALUES ('Europe');
INSERT INTO region (region_name) VALUES ('Americas');
INSERT INTO region (region_name) VALUES ('Asia');
INSERT INTO region (region_name) VALUES ('Middle East and Africa');

--insert values into country table
INSERT INTO country (country_code, country_name, region_id)
  SELECT
    'IT',
    'Italy',
    region_id
  FROM region
  WHERE region_name = 'Europe';

INSERT INTO country (country_code, country_name, region_id)
  SELECT
    'FR',
    'France',
    region_id
  FROM region
  WHERE region_name = 'Europe';

INSERT INTO country (country_code, country_name, region_id)
  SELECT
    'DE',
    'Germany',
    region_id
  FROM region
  WHERE region_name = 'Europe';

INSERT INTO country (country_code, country_name, region_id)
  SELECT
    'JP',
    'Japan',
    region_id
  FROM region
  WHERE region_name = 'Asia';

INSERT INTO country (country_code, country_name, region_id)
  SELECT
    'US',
    'United States of America',
    region_id
  FROM region
  WHERE region_name = 'Americas';

INSERT INTO country (country_code, country_name, region_id)
  SELECT
    'CA',
    'Canada',
    region_id
  FROM region
  WHERE region_name = 'Americas';

INSERT INTO country (country_code, country_name, region_id)
  SELECT
    'IN',
    'India',
    region_id
  FROM region
  WHERE region_name = 'Asia';

INSERT INTO country (country_code, country_name, region_id)
  SELECT
    'CN',
    'China',
    region_id
  FROM region
  WHERE region_name = 'Asia';

INSERT INTO country (country_code, country_name, region_id)
  SELECT
    'ZM',
    'Zambia',
    region_id
  FROM region
  WHERE region_name = 'Middle East and Africa';

INSERT INTO country (country_code, country_name, region_id)
  SELECT
    'EG',
    'Egypt',
    region_id
  FROM region
  WHERE region_name = 'Middle East and Africa';

-- Insert values into location
INSERT INTO location (street_address, postal_code, city, state_province, country_id)
  SELECT
    '1297 Via Cola di Rie',
    '00989',
    'Roma',
    null,
    country_id
  FROM country
  WHERE country_code = 'IT';

INSERT INTO location (street_address, postal_code, city, state_province, country_id)
  SELECT
    '2017 Shinjuku-ku',
    '1689',
    'Tokyo',
    'Tokyo Prefecture',
    country_id
  FROM country
  WHERE country_code = 'JP';

INSERT INTO location (street_address, postal_code, city, state_province, country_id)
  SELECT
    '2014 Jabberwocky Rd',
    '26192',
    'Southlake',
    'Texas',
    country_id
  FROM country
  WHERE country_code = 'US';

INSERT INTO location (street_address, postal_code, city, state_province, country_id)
  SELECT
    '2011 Interiors Blvd',
    '99236',
    'South San Francisco',
    'California',
    country_id
  FROM country
  WHERE country_code = 'US';

INSERT INTO location (street_address, postal_code, city, state_province, country_id)
  SELECT
    'Schwanthalerstr. 7031',
    '80925',
    'Munich',
    'Bavaria',
    country_id
  FROM country
  WHERE country_code = 'DE';

--insert values into department table

INSERT INTO department (department_name, location_id)
  SELECT
    'Administration',
    location_id
  FROM location
  WHERE city = 'South San Francisco';

INSERT INTO department (department_name, location_id)
  SELECT
    'Marketing',
    location_id
  FROM location
  WHERE city = 'Southlake';

INSERT INTO department (department_name, location_id)
  SELECT
    'Purchasing',
    location_id
  FROM location
  WHERE city = 'Tokyo';

INSERT INTO department (department_name, location_id)
  SELECT
    'Human Resources',
    location_id
  FROM location
  WHERE city = 'Munich';

INSERT INTO department (department_name, location_id)
  SELECT
    'Sales',
    location_id
  FROM location
  WHERE city = 'Roma';

INSERT INTO department (department_name, location_id)
  SELECT
    'Finance',
    location_id
  FROM location
  WHERE city = 'South San Francisco';

-- insert values into employee

INSERT INTO employee (first_name, last_name, email, department_id)
  SELECT
    'Steven',
    'King',
    'sking@test.com',
    department_id
  FROM department
  WHERE department_name = 'Administration';

INSERT INTO employee (first_name, last_name, email, department_id)
  SELECT
    'Ram',
    'Lakshman',
    'lakshman@test.com',
    department_id
  FROM department
  WHERE department_name = 'Administration';

INSERT INTO employee (first_name, last_name, email, department_id)
  SELECT
    'Edward',
    'Romiro',
    'redward@test.com',
    department_id
  FROM department
  WHERE department_name = 'Administration';

INSERT INTO employee (first_name, last_name, email, department_id)
  SELECT
    'Swapna',
    'Desai',
    'sdesai@test.com',
    department_id
  FROM department
  WHERE department_name = 'Marketing';

INSERT INTO employee (first_name, last_name, email, department_id)
  SELECT
    'Xiao',
    'Chen',
    'xchen@test.com',
    department_id
  FROM department
  WHERE department_name = 'Marketing';

INSERT INTO employee (first_name, last_name, email, department_id)
  SELECT
    'Pearson',
    'Lord',
    'plord@test.com',
    department_id
  FROM department
  WHERE department_name = 'Purchasing';

INSERT INTO employee (first_name, last_name, email, department_id)
  SELECT
    'Emily',
    'Jennifer',
    'ejenny@test.com',
    department_id
  FROM department
  WHERE department_name = 'Purchasing';

INSERT INTO employee (first_name, last_name, email, department_id)
  SELECT
    'Priya',
    'Menon',
    'pmenon@test.com',
    department_id
  FROM department
  WHERE department_name = 'Human Resources';

INSERT INTO employee (first_name, last_name, email, department_id)
  SELECT
    'Jeff',
    'Robert',
    'rjeff@test.com',
    department_id
  FROM department
  WHERE department_name = 'Human Resources';

INSERT INTO employee (first_name, last_name, email, department_id)
  SELECT
    'Amartya',
    'Patel',
    'apatel@test.com',
    department_id
  FROM department
  WHERE department_name = 'Sales';

INSERT INTO employee (first_name, last_name, email, department_id)
  SELECT
    'Jason',
    'Emilio',
    'jemilio@test.com',
    department_id
  FROM department
  WHERE department_name = 'Sales';

INSERT INTO employee (first_name, last_name, email, department_id)
  SELECT
    'Venkata Rao',
    'Tati',
    'vtati@test.com',
    department_id
  FROM department
  WHERE department_name = 'Finance';

INSERT INTO employee (first_name, last_name, email, department_id)
  SELECT
    'Shyam',
    'Saxena',
    'ssaxena@test.com',
    department_id
  FROM department
  WHERE department_name = 'Finance';

INSERT INTO employee (first_name, last_name, email, department_id)
  SELECT
    'Lakshmi',
    'Bhavani',
    'lbhavani@test.com',
    department_id
  FROM department
  WHERE department_name = 'Finance';
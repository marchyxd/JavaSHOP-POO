create database shop;

use shop ;
CREATE TABLE employee (
  employeeId INT,
  name VARCHAR(255),
  password VARCHAR(255)
);

INSERT INTO employee (employeeId, name, password) VALUES
  (111, 'Jiahao', 'stucom'),
  (222, 'test', 'stucom'),
  (333, 'admin', 'stucom');

CREATE TABLE inventory (
  id INT(11) AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45),
  price DOUBLE,
  available TINYINT(1),
  stock INT(11)
);

INSERT INTO inventory (name, price, available, stock) VALUES
  ('Apple', 10, 1, 50),
  ('Strawberry', 5, 1, 50),
  ('Pear', 20, 1, 50),
  ('Hamburger', 30, 1, 50);

CREATE TABLE historical_inventory (
  id INT(11) AUTO_INCREMENT PRIMARY KEY,
  id_product INT(11),
  name VARCHAR(255),
  available INT(11),
  stock INT(11),
  created_at DATETIME,
  price DOUBLE
);

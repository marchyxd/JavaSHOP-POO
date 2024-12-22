create table inventory(
  id int primary key auto_increment,
  name varchar(45),
  wholesalerPrice double,
  available boolean,
  stock int
);
CREATE TABLE historical_inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_product INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    wholesalerPrice DECIMAL(10, 2) NOT NULL,
    available INT NOT NULL,
    stock INT NOT NULL,
    created_at DATETIME NOT NULL
);

INSERT INTO inventory (id, name, wholesalerPrice, available, stock) VALUES
(1, 'Apple', 10.00, true, 50),
(2, 'Strawberry', 5.00, true, 50),
(3, 'Pear', 20.00, true, 50),
(4, 'Hamburger', 30.00, true, 50);

select * from historical_inventory;
select * from inventory;

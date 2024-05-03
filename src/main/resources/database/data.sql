CREATE DATABASE test;

USE test;

CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT,
    email VARCHAR(256) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    CONSTRAINT account_pk PRIMARY KEY (id)
);

CREATE INDEX email_idx ON accounts (email);

CREATE TABLE products (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    CONSTRAINT product_pk PRIMARY KEY (id)
);

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    account_id BIGINT NOT NULL,
    CONSTRAINT order_pk PRIMARY KEY (id)
);

ALTER TABLE orders ADD CONSTRAINT account_fk
    FOREIGN KEY (account_id)
    REFERENCES accounts(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

CREATE TABLE items (
    id BIGINT AUTO_INCREMENT,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    product_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    CONSTRAINT item_pk PRIMARY KEY (id)
);

ALTER TABLE items ADD CONSTRAINT product_fk
    FOREIGN KEY (product_id)
    REFERENCES products(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

ALTER TABLE Items ADD CONSTRAINT order_fk
    FOREIGN KEY (order_id)
    REFERENCES orders(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE;

DELIMITER //
CREATE PROCEDURE orderTotalPrice(IN orderId BIGINT)
BEGIN
    SELECT SUM(i.unit_price * i.quantity)
    AS order_total_price
    FROM items i
    WHERE i.order_id = orderId;
END //
DELIMITER ;
CREATE TABLE order_table
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id         BIGINT,
    registration_date TIMESTAMP,
    value_total       DECIMAL(10, 2),
    FOREIGN KEY (client_id) REFERENCES client (id)
);

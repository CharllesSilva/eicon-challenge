CREATE TABLE order_item
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id   BIGINT,
    product_id BIGINT,
    quantity   INT,
    FOREIGN KEY (order_id) REFERENCES order_table (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

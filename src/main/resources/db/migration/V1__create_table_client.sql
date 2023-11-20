CREATE TABLE client
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    number_control INT,
    name           VARCHAR(255),
    cpf            VARCHAR(14),
    address        VARCHAR(255)
);

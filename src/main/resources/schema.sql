CREATE TABLE pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    total DECIMAL(15, 2),
    status VARCHAR(20),
    codigo int,
    versao int
);

CREATE TABLE produto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT,
    nome VARCHAR(255),
    preco DECIMAL(15, 2),
    quantidade INT,
    versao INT,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id)
);
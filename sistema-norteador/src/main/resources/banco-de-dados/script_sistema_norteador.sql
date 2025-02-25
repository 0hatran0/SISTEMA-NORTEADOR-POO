CREATE DATABASE IF NOT EXISTS db_lavacao;
USE db_lavacao;

/*
TABELA PARA CRIAR O BANCO DO SISTEMA NORTEADOR
*/

CREATE TABLE IF NOT EXISTS marca(
    id int NOT NULL auto_increment,
    nome varchar(50) NOT NULL,
    CONSTRAINT pk_marca PRIMARY KEY(id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS cor(
    id int NOT NULL auto_increment,
    nome varchar(50) NOT NULL,
    CONSTRAINT pk_cor PRIMARY KEY(id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS servico(
    id int NOT NULL auto_increment,
    descricao varchar(150) NOT NULL,
    valor decimal(10,2) NOT NULL,
    pontos int NOT NULL DEFAULT 0,
    categoria ENUM('PEQUENO', 'MEDIO', 'GRANDE', 'MOTO', 'PADRAO') NOT NULL DEFAULT 'PADRAO',
    CONSTRAINT pk_servico PRIMARY KEY(id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS modelo(
    id int NOT NULL auto_increment,
    descricao varchar(150) NOT NULL,
    id_marca int NOT NULL,
    categoria ENUM('PEQUENO', 'MEDIO', 'GRANDE', 'MOTO', 'PADRAO') NOT NULL DEFAULT 'PADRAO',
    CONSTRAINT pk_modelo
      PRIMARY KEY(id),
    CONSTRAINT fk_modelo_marca
      FOREIGN KEY(id_marca)
      REFERENCES marca(id)
) engine=InnoDB;

/*TABELA MOTOR COM RELACIONAMENTO 1:1 PARA MODELO - COMPOSIÇÃO*/
CREATE TABLE IF NOT EXISTS motor(
    id_modelo int NOT NULL,
    potencia int NOT NULL DEFAULT 0,
    tipo_combustivel ENUM('GASOLINA', 'ETANOL', 'FLEX', 'DIESEL', 'GNV', 'OUTRO') 
    NOT NULL DEFAULT 'GASOLINA',
    CONSTRAINT pk_motor 
        PRIMARY KEY (id_modelo),
    CONSTRAINT fk_motor_modelo 
        FOREIGN KEY (id_modelo)
        REFERENCES modelo(id)
        ON DELETE CASCADE
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS cliente(
    id int NOT NULL auto_increment,
    nome varchar(150) NOT NULL,
    celular varchar(20) NOT NULL,
    email varchar(150) NOT NULL,
    data_cadastro date NOT NULL,
    CONSTRAINT pk_cliente
        PRIMARY KEY(id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS pessoa_fisica(
    id_cliente int NOT NULL,
    cpf varchar(150) NOT NULL,
    data_nascimento date NOT NULL,
    CONSTRAINT pk_cliente
        PRIMARY KEY(id_cliente),
    CONSTRAINT fk_pf_cliente
        FOREIGN KEY(id_cliente)
        REFERENCES cliente(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS pessoa_juridica(
    id_cliente int NOT NULL,
    cnpj varchar(150) NOT NULL,
    inscricao_estadual varchar(150) NOT NULL,
    CONSTRAINT pk_cliente
        PRIMARY KEY(id_cliente),
    CONSTRAINT fk_pj_cliente
        FOREIGN KEY(id_cliente)
        REFERENCES cliente(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS veiculo(
    id int NOT NULL auto_increment,
    placa varchar(10) NOT NULL,
    observacoes varchar(350) NOT NULL DEFAULT 'Não informado',
    id_cliente int NOT NULL,
    id_modelo int NOT NULL,
    id_cor int NOT NULL,
    CONSTRAINT pk_veiculo
      PRIMARY KEY(id),
    CONSTRAINT fk_veiculo_cliente
      FOREIGN KEY(id_cliente)
      REFERENCES cliente(id)
      ON DELETE CASCADE,
    CONSTRAINT fk_veiculo_modelo
      FOREIGN KEY(id_modelo)
      REFERENCES modelo(id),
    CONSTRAINT fk_veiculo_cor
      FOREIGN KEY(id_cor)
      REFERENCES cor(id)
) engine=InnoDB;

CREATE TABLE ordem_de_servico(
    numero int NOT NULL auto_increment,
    total decimal(10,2) NOT NULL,
    agenda date NOT NULL,
    desconto double NOT NULL,
    situacao ENUM('ABERTA', 'FECHADA', 'CANCELADA') NOT NULL DEFAULT 'ABERTA',
    id_veiculo int NOT NULL,
    CONSTRAINT pk_os
        PRIMARY KEY (numero),
    CONSTRAINT fk_os_veiculo
        FOREIGN KEY(id_veiculo)
        REFERENCES veiculo(id)
) engine=InnoDB;

CREATE TABLE item_da_ordem(
    id int NOT NULL auto_increment,
    valor_do_servico decimal(10,2) NOT NULL,
    observacao VARCHAR(300) NOT NULL DEFAULT 'Não informado',
    id_servico int NOT NULL,
    id_os int NOT NULL,
    CONSTRAINT pk_item_da_ordem
        PRIMARY KEY(id),
    CONSTRAINT fk_item_servico
        FOREIGN KEY(id_servico)
        REFERENCES servico(id),
    CONSTRAINT fk_item_os
        FOREIGN KEY(id_os)
        REFERENCES ordem_de_servico(numero)
        ON DELETE CASCADE
) engine=InnoDB;

INSERT INTO marca(nome) VALUES('BMW');
INSERT INTO marca(nome) VALUES('Mercedes-Benz');
INSERT INTO marca(nome) VALUES('Porsche');

INSERT INTO cor(nome) VALUES('Azul');
INSERT INTO cor(nome) VALUES('Verde');
INSERT INTO cor(nome) VALUES('Branco');

INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação por Fora','50','10','PADRAO');
INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação Completa','70','15','PADRAO');
INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação Completa + Cera','90','20','PADRAO');

INSERT INTO modelo(descricao, id_marca, categoria) VALUES('Z4','1','PADRAO');
INSERT INTO motor(id_modelo) (SELECT max(id) FROM modelo);
INSERT INTO modelo(descricao, id_marca, categoria) VALUES('CLS','2','PADRAO');
INSERT INTO motor(id_modelo) (SELECT max(id) FROM modelo);
INSERT INTO modelo(descricao, id_marca, categoria) VALUES('781','3','PADRAO');
INSERT INTO motor(id_modelo) (SELECT max(id) FROM modelo);

UPDATE motor SET potencia=200, tipo_combustivel='GASOLINA' WHERE id_modelo = 1;
UPDATE motor SET potencia=250, tipo_combustivel='DIESEL' WHERE id_modelo = 2;
UPDATE motor SET potencia=300, tipo_combustivel='GASOLINA' WHERE id_modelo = 3;

INSERT INTO cliente(nome, celular,  email, data_cadastro) VALUES('Luiz', '(11) 91111-1111', 'luiz@gmail.com', '2024-11-01');
INSERT INTO pessoa_fisica(id_cliente, cpf, data_nascimento) VALUES((SELECT max(id) FROM cliente), '111.111.111-11', '1970-01-10');
INSERT INTO cliente(nome, celular,  email, data_cadastro) VALUES('Bruna', '(22) 92222-2222', 'bruna@gmail.com', '2024-11-02');
INSERT INTO pessoa_juridica(id_cliente, cnpj, inscricao_estadual) VALUES((SELECT max(id) FROM cliente), '22.222.222/0002-22', '123456789');
INSERT INTO cliente(nome, celular,  email, data_cadastro) VALUES('Robson', '(33) 93333-3333', 'robson@gmail.com', '2024-11-03');
INSERT INTO pessoa_fisica(id_cliente, cpf, data_nascimento) VALUES((SELECT max(id) FROM cliente), '333.333.333-33', '1980-03-20');

INSERT INTO veiculo(placa, observacoes, id_cliente, id_modelo, id_cor) VALUES('AAA-111','Não informado','1','1','1');
INSERT INTO veiculo(placa, observacoes, id_cliente, id_modelo, id_cor) VALUES('BBB-222','Não informado','2','2','2');
INSERT INTO veiculo(placa, observacoes, id_cliente, id_modelo, id_cor) VALUES('CCC-333','Não informado','3','3','3');

INSERT INTO ordem_de_servico(total, agenda, desconto, situacao, id_veiculo) VALUES('120', '2025-02-01', '0', 'ABERTA', '1');
INSERT INTO item_da_ordem(valor_do_servico, observacao, id_servico, id_os) VALUES('60', 'Não informado', '1', '1');
INSERT INTO item_da_ordem(valor_do_servico, observacao, id_servico, id_os) VALUES('60', 'Não informado', '1', '1');



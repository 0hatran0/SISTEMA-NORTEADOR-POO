/*Inserir dados no banco*/
    INSERT INTO veiculo(placa, observacoes, id_modelo, id_cor) VALUES(?,?,?,?);
/*Utualizar dados no banco de acordo com ID*/
    UPDATE veiculo SET placa=?, observacoes=?, id_modelo=?, id_cor=? WHERE id=?;
/*Deletar dados do banco de acordo com o ID*/
    DELETE FROM veiculo WHERE id=?
/*Retornar os dados do banco*/
    SELECT * FROM veiculo
/*Retornar os dados do banco de acordo com o ID*/
    SELECT * FROM veiculo WHERE id=?

INSERT INTO cliente(nome, celular,  email, data_cadastro) VALUES('Luiz', '(11) 91111-1111', 'luiz@gmail.com', '2024-11-01');
INSERT INTO pessoa_fisica(id_cliente, cpf, data_nascimento) VALUES((SELECT max(id) FROM cliente), '111.111.111-11', '1970-01-10');
INSERT INTO cliente(nome, celular,  email, data_cadastro) VALUES('Bruna', '(22) 92222-2222', 'bruna@gmail.com', '2024-11-02');
INSERT INTO pessoa_jurica(id_cliente, cnpj, inscricao_estadual) VALUES((SELECT max(id) FROM cliente), '22.222.222/0002-22', '123456789');
INSERT INTO cliente(nome, celular,  email, data_cadastro) VALUES('Robson', '(33) 93333-3333', 'robson@gmail.com', '2024-11-03');
INSERT INTO pessoa_fisica(id_cliente, cpf, data_nascimento) VALUES((SELECT max(id) FROM cliente), '333.333.333-33', '1980-03-20');
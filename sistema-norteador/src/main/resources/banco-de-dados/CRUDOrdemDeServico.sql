/*Inserir dados no banco*/
    INSERT INTO cliente(nome, celular,  email, data_cadastro) VALUES(?,?,?,?);
    INSERT INTO pessoa_fisica(id_cliente, cpf, data_nascimento) VALUES((SELECT max(id) FROM cliente),?,?);
    INSERT INTO pessoa_juridica(id_cliente, cnpj, inscricao_estadual) VALUES((SELECT max(id) FROM cliente),?,?);
/*Utualizar dados no banco de acordo com ID*/
    UPDATE cliente SET nome=?, celular=?, email=?, data_cadastro=? WHERE id=?;
    UPDATE pessoa_fisica SET cpf=?, data_nascimento=? WHERE id_cliente = ?;
    UPDATE pessoa_juridica SET cnpj=?, inscricao_estadual=? WHERE id_cliente = ?;
/*Deletar dados do banco de acordo com o ID*/
    DELETE FROM cliente WHERE id=?
/*Listar cadastros no banco*/
    SELECT * FROM cliente c LEFT JOIN pessoa_fisica pf on pf.id_cliente = c.id LEFT JOIN pessoa_juridica pj on pj.id_cliente = c.id;
/*Retornar os dados do banco*/
    SELECT * FROM cliente c LEFT JOIN pessoa_fisica pf on pf.id_cliente = c.id LEFT JOIN pessoa_juridica pj on pj.id_cliente = c.id WHERE id=?;
/*Retornar os dados do banco de acordo com o ID*/
    SELECT * FROM cliente c LEFT JOIN pessoa_fisica pf on pf.id_cliente = c.id LEFT JOIN pessoa_juridica pj on pj.id_cliente = c.id WHERE id=?;

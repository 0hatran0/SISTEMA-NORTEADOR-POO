/*Inserir dados no banco*/
    INSERT INTO servico(descricao, valor, pontos, categoria) VALUES(?,?,?,?);
/*Utualizar dados no banco de acordo com ID*/
    UPDATE servico SET descricao=?, valor=?, pontos=?, categoria=? WHERE id=?
/*Deletar dados do banco de acordo com o ID*/
    DELETE FROM servico WHERE id=?
/*Retornar os dados do banco*/
    SELECT * FROM servico
/*Retornar os dados do banco de acordo com o ID*/
    SELECT * FROM servico WHERE id=?
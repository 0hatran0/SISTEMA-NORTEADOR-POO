/*Inserir dados no banco*/
    INSERT INTO cor(nome) VALUES(?)
/*Utualizar dados no banco de acordo com ID*/
    UPDATE cor SET nome=? WHERE id=?
/*Deletar dados do banco de acordo com o ID*/
    DELETE FROM cor WHERE id=?
/*Retornar os dados do banco*/
    SELECT * FROM cor
/*Retornar os dados do banco de acordo com o ID*/
    SELECT * FROM cor WHERE id=?
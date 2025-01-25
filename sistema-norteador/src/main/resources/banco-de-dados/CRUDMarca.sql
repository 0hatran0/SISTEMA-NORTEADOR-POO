/*Inserir dados no banco*/
    INSERT INTO marca(nome) VALUES(?)
/*Utualizar dados no banco de acordo com ID*/
    UPDATE marca SET nome=? WHERE id=?
/*Deletar dados do banco de acordo com o ID*/
    DELETE FROM marca WHERE id=?
/*Retornar os dados do banco*/
    SELECT * FROM marca
/*Retornar os dados do banco de acordo com o ID*/
    SELECT * FROM marca WHERE id=?
/*Inserir dados no banco*/
    INSERT INTO modelo(descricao, id_marca, categoria) VALUES(?,?,?);
    INSERT INTO motor(id_modelo, potencia, tipo_combustivel) VALUES(SELECT max(id) FROM modelo, ?, ?);
/*Utualizar dados no banco de acordo com ID*/
    UPDATE modelo SET descricao=?, id_marca=?, categoria=? WHERE id=?;
    UPDATE motor SET potencia=?, tipo_combustivel=? WHERE id_modelo=?;
/*Deletar dados do banco de acordo com o ID*/
    DELETE FROM modelo WHERE id=?
/*Retornar os dados do banco*/
    SELECT * FROM modelo md INNER JOIN motor mt ON md.id = mt.id_modelo;
/*Retornar os dados do banco de acordo com o ID*/
    SELECT * FROM modelo md INNER JOIN motor mt ON md.id = mt.id_modelo WHERE md.id = ?;


/*Inserir dados no banco*/
    INSERT INTO motor(id_modelo, potencia, tipo_combustivel) VALUES(SELECT max(id) FROM modelo, ?, ?);
/*Utualizar dados no banco de acordo com ID*/
    UPDATE motor SET potencia=?, tipo_combustivel=? WHERE id_modelo=?;
/*Deletar dados do banco de acordo com o ID*/
    DELETE FROM modelo WHERE id=?;
    /* motor carrega uma chave estrangeira, não pode ser deletada...
    on delete cascade */
/*Retornar os dados do banco*/
    SELECT * FROM motor ;
/*Retornar os dados do banco de acordo com o ID*/
    SELECT * FROM motor WHERE id_modelo = ?;

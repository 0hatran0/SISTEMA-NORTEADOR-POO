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
package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Motor;
import br.edu.ifsc.fln.model.domain.Modelo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MotorDAO{

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean atualizar(Motor motor) {
        String sql = "UPDATE motor SET potencia=?, tipo_combustivel=? WHERE id_modelo=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, motor.getPotencia());
            stmt.setString(2, motor.getTipoCombustivel().name());
            stmt.setInt(3, motor.getModelo().getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(MotorDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Modelo buscar(int id) {
        String sql = "SELECT * FROM motor WHERE id_modelo=?";
        Modelo retorno = new Modelo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno.getMotor().setPotencia(resultado.getInt("potencia"));
                retorno.getMotor().setTipoCombustivel(Enum.valueOf(ETipoCombustivel.class, resultado.getString("tipo_combustivel")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MotorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;        
    }
    

}

package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModeloDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Modelo modelo) {
        final String sql = "INSERT INTO modelo(descricao, id_marca, categoria) VALUES(?,?,?);";
        final String sqlMotor = "INSERT INTO motor(id_modelo, potencia, tipo_combustivel) "
                + "VALUES(SELECT max(id) FROM modelo, ?, ?);";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            //registra o modelo
            stmt.setString(1, modelo.getDescricao());
            stmt.setInt(2, modelo.getMarca().getId());
            stmt.setString(3, modelo.getECategoria().name());
            stmt.execute();
            //registrar motor
            stmt = connection.prepareStatement(sqlMotor);
            stmt.setInt(1, modelo.getMotor().getPotencia());
            stmt.setString(2, modelo.getMotor().getTipoCombustivel().name());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Modelo modelo) {
        String sql = "UPDATE modelo SET descricao=?, id_marca=?, categoria=? WHERE id=?;"
                + "UPDATE motor SET potencia=?, tipo_combustivel=? WHERE id_modelo=?;";    
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modelo.getDescricao());
            stmt.setInt(2, modelo.getMarca().getId());
            stmt.setString(3, modelo.getECategoria().name());
            stmt.setInt(4, modelo.getId());
            stmt.setInt(5, modelo.getMotor().getPotencia());
            stmt.setString(6, modelo.getMotor().getTipoCombustivel().name());
            stmt.setInt(7, modelo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Modelo modelo) {
        String sql = "DELETE FROM modelo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Modelo> listar() {
        String sql = "SELECT * FROM modelo md INNER JOIN motor mt ON md.id = mt.id_modelo;";
        List<Modelo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Modelo modelo = populateVO(resultado);
                retorno.add(modelo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Modelo buscar(Modelo modelo) {
        String sql = "SELECT * FROM modelo md INNER JOIN motor mt ON md.id = mt.id_modelo WHERE md.id = ?;";
        Modelo retorno = new Modelo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

//    public Cliente buscar(int id){return retorno;}

    private Modelo populateVO(ResultSet rs) throws SQLException {
        Modelo modelo = new Modelo();
        //modelo.setMarca(marca);

        modelo.setId(rs.getInt("id"));
        modelo.setDescricao(rs.getString("descricao"));
        modelo.setECategoria(Enum.valueOf(ECategoria.class, rs.getString("categoria")));
        Marca marca = new Marca();
        marca.setId(rs.getInt("id_marca"));
        MarcaDAO marcaDAO = new MarcaDAO();
        marcaDAO.setConnection(connection);
        marca = marcaDAO.buscar(marca);
        modelo.setMarca(marca);
        modelo.getMotor().setPotencia(rs.getInt("potencia"));
        modelo.getMotor().setTipoCombustivel(Enum.valueOf(ETipoCombustivel.class, rs.getString("tipo_combustivel")));
       
        return modelo;
    }

}

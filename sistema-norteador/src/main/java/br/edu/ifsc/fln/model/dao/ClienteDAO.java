package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Cliente cliente) {
        final String sql = "INSERT INTO cliente(placa, observacoes, id_modelo, id_cor) VALUES(?,?,?,?);";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            //registra o modelo
            stmt.setString(1, cliente.getPlaca());
            stmt.setString(2, cliente.getObservacoes());
            stmt.setInt(3, cliente.getModelo().getId());
            stmt.setInt(4, cliente.getCor().getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Cliente cliente) {
        String sql = "UPDATE cliente SET placa=?, observacoes=?, id_modelo=?, id_cor=? WHERE id=?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getPlaca());
            stmt.setString(2, cliente.getObservacoes());
            stmt.setInt(3, cliente.getModelo().getId());
            stmt.setInt(4, cliente.getCor().getId());
            stmt.setInt(5, cliente.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Cliente cliente) {
        String sql = "DELETE FROM cliente WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Cliente> listar() {
        String sql = "SELECT * FROM cliente;";
        List<Cliente> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Cliente cliente = populateVO(resultado);
                retorno.add(cliente);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Cliente buscar(Cliente cliente) {
        String sql = "SELECT * FROM cliente WHERE md.id = ?;";
        Cliente retorno = new Cliente();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cliente.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    /**
     * Método para mapear os dados de um modelo (relacional) para objeto, contemplando ou não a marca.
     * @param rs
     * @param comMarca
     * @return
     * @throws SQLException 
     */
//    private Modelo populateVO(ResultSet rs, boolean comMarca) throws SQLException {
//        Modelo modelo = new Modelo();
//        //modelo.setMarca(marca);
//
//        modelo.setId(rs.getInt("id"));
//        modelo.setDescricao(rs.getString("descricao"));
//        if (comMarca) {
//            Marca marca = new Marca();
//            marca.setId(rs.getInt("id_marca"));
//            MarcaDAO marcaDAO = new MarcaDAO();
//            marcaDAO.setConnection(connection);
//            marca = marcaDAO.buscar(marca);
//            cliente.setMarca(marca);
//        }
//        cliente.setECategoria(Enum.valueOf(ECategoria.class, rs.getString("categoria")));
//        
//        return modelo;
//    }

    private Cliente populateVO(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setPlaca(rs.getString("placa"));
        cliente.setObservacoes(rs.getString("observacoes"));
        Modelo modelo = new Modelo();
        modelo.setId(rs.getInt("id_modelo"));
        ModeloDAO modeloDAO = new ModeloDAO();
        modeloDAO.setConnection(connection);
        modelo = modeloDAO.buscar(modelo);
        cliente.setModelo(modelo);
        Cor cor = new Cor();
        cor.setId(rs.getInt("id_cor"));
        CorDAO corDAO = new CorDAO();
        corDAO.setConnection(connection);
        cor = corDAO.buscar(cor);
        cliente.setCor(cor);
        return cliente;
    }

}

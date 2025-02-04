package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdemServicoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(OrdemServico ordemServico) {
        String sql = "INSERT INTO ordem_de_servico(numero, total, agenda, desconto, situacao, id_veiculo) VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            stmt.setLong(1, ordemServico.getNumero());
            stmt.setDouble(2, ordemServico.calcularServico());
            stmt.setDate(3, Date.valueOf(ordemServico.getAgenda()));
            stmt.setDouble(4, ordemServico.getDesconto());
            if  (ordemServico.getStatus().name() != null) {
                stmt.setString(5, ordemServico.getStatus().name());
            } else {
                //TODO apresentar situação clara de inconsistência de dados
                //tratamento de exceções e a necessidade de uso de commit e rollback
                //stmt.setString(6, "teste");
                stmt.setString(5, EStatus.ABERTA.name());
            }
            stmt.setInt(6, ordemServico.getVeiculo().getId());
            stmt.execute();
            ItemOSDAO itemDaOrdemServicoDAO = new ItemOSDAO();
            itemDaOrdemServicoDAO.setConnection(connection);
            ServicoDAO servicoDAO = new ServicoDAO();
            servicoDAO.setConnection(connection);
//            for (ItemOS itemDaOrdemServico: ordemServico.getItemOS()) {
//                Servico servico = itemDaOrdemServico.getServico();
//                itemDaOrdemServico.setOrdemServico(this.buscarUltimaOrdemServico());
//                itemDaOrdemServicoDAO.inserir(itemDaOrdemServico);
//            }
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {

        }
    }

    public boolean alterar(OrdemServico ordemServico) {
        String sql = "UPDATE ordem_de_servico SET total=?, agenda=?, desconto=?, situacao=?, id_veiculo=? WHERE numero=?";
        try {
            connection.setAutoCommit(false);
            ItemOSDAO itemOSDAO = new ItemOSDAO();
            itemOSDAO.setConnection(connection);
            ServicoDAO servicoDAO = new ServicoDAO();
            servicoDAO.setConnection(connection);

            OrdemServico ordemServicoAnterior = buscar(ordemServico);
            List<ItemOS> itensDaOrdemServico = itemOSDAO.listarPorOrdem(ordemServicoAnterior);

            //atualiza os dados da ordemServico
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDouble(1, ordemServico.calcularServico());
            stmt.setDate(2, Date.valueOf(ordemServico.getAgenda()));
            stmt.setDouble(3, ordemServico.getDesconto());
            if  (ordemServico.getStatus().name() != null) {
                stmt.setString(4, ordemServico.getStatus().name());
            } else {
                //TODO apresentar situação clara de inconsistência de dados
                //tratamento de exceções e a necessidade de uso de commit e rollback
                //stmt.setString(6, "teste");
                stmt.setString(4, EStatus.ABERTA.name());
            }
            stmt.setInt(5, ordemServico.getVeiculo().getId());
            stmt.setLong(6,ordemServico.getNumero());
            stmt.execute();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException exc1) {
                Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, exc1);
            }
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(OrdemServico ordemServico) {
        String sql = "DELETE FROM ordem_de_servico WHERE numero=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            try {
                connection.setAutoCommit(false);
                ItemOSDAO itemOSDAO = new ItemOSDAO();
                itemOSDAO.setConnection(connection);
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                stmt.setLong(1, ordemServico.getNumero());
                stmt.execute();
                connection.commit();
            } catch (SQLException exc) {
                try {
                    connection.rollback();
                } catch (SQLException exc1) {
                    Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, exc1);
                }
                Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, exc);
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (Exception ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<OrdemServico> listar() {
        String sql = "SELECT * FROM ordem_de_servico";
        List<OrdemServico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                OrdemServico ordemServico = new OrdemServico();
                Veiculo veiculo = new Veiculo();
                List<ItemOS> itensOS = new ArrayList();

                ordemServico.setNumero(resultado.getInt("numero"));
//                ordemServico.calcularServico();
                ordemServico.setAgenda(resultado.getDate("agenda").toLocalDate());
                ordemServico.setDesconto(resultado.getDouble("desconto"));
                ordemServico.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
                veiculo.setId(resultado.getInt("id_veiculo"));

                //Obtendo os dados completos do Cliente associado à OrdemServico
                VeiculoDAO veiculoDAO = new VeiculoDAO();
                veiculoDAO.setConnection(connection);
                veiculo = veiculoDAO.buscar(veiculo);

                //Obtendo os dados completos dos Itens de OrdemServico associados à OrdemServico
                ItemOSDAO itemOSDAO = new ItemOSDAO();
                itemOSDAO.setConnection(connection);
                itensOS = itemOSDAO.listarPorOrdem(ordemServico);

                ordemServico.setVeiculo(veiculo);
                ordemServico.add(itensOS);
                retorno.add(ordemServico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public OrdemServico buscar(OrdemServico ordemServico) {
        String sql = "SELECT * FROM ordem_de_servico WHERE id=?";
        OrdemServico ordemServicoRetorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, ordemServico.getNumero());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Veiculo veiculo = new Veiculo();
                ordemServicoRetorno.setNumero(resultado.getLong("numero"));
//                ordemServicoRetorno.calcularServico();
                ordemServicoRetorno.setAgenda(resultado.getDate("agenda").toLocalDate());
                ordemServicoRetorno.setDesconto(resultado.getDouble("desconto"));
                ordemServicoRetorno.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
                veiculo.setId(resultado.getInt("id_veiculo"));
                ordemServicoRetorno.setVeiculo(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ordemServicoRetorno;
    }

    public OrdemServico buscar(int id) {
        String sql = "SELECT * FROM ordemServico WHERE id=?";
        OrdemServico ordemServicoRetorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Veiculo veiculo = new Veiculo();
                ordemServicoRetorno.setNumero(resultado.getLong("numero"));
//                ordemServicoRetorno.calcularServico();
                ordemServicoRetorno.setAgenda(resultado.getDate("agenda").toLocalDate());
                ordemServicoRetorno.setDesconto(resultado.getDouble("desconto"));
                ordemServicoRetorno.setStatus(Enum.valueOf(EStatus.class, resultado.getString("status")));
                veiculo.setId(resultado.getInt("id_veiculo"));
                ordemServicoRetorno.setVeiculo(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ordemServicoRetorno;
    }

    public OrdemServico buscarUltimaOrdemServico() {
        String sql = "SELECT max(id) as max FROM ordemServico";

        OrdemServico retorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            if (resultado.next()) {
                retorno.setNumero(resultado.getInt("max"));
                return retorno;
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

//    private Cliente populateVO(ResultSet rs) throws SQLException {
//        Cliente cliente;
//        if (rs.getString("cnpj") == null || rs.getString("cnpj").length() <= 0) {
//            //é um fornecedor nacional
//            cliente = new PessoaFisica();
//            ((PessoaFisica)cliente).setCpf(rs.getString("cpf"));
//            ((PessoaFisica)cliente).setDataNascimento(rs.getString("data_nascimento"));
//        } else {
//            //é um fornecedor internacional
//            cliente = new PessoaJuridica();
//            ((PessoaJuridica)cliente).setCnpj(rs.getString("cnpj"));
//            ((PessoaJuridica)cliente).setInscricaoEstadual(rs.getString("inscricao_estadual"));
//        }
//        cliente.setId(rs.getInt("id"));
//        cliente.setNome(rs.getString("nome"));
//        cliente.setCelular(rs.getString("celular"));
//        cliente.setEmail(rs.getString("email"));
//        cliente.setDataCadastro(rs.getString("data_cadastro"));
//        return cliente;
//    }

}

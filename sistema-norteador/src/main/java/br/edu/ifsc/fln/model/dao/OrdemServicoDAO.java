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
        String sql = "INSERT INTO ordem_de_servico(numero, total, agenda, desconto, status, id_veiculo) VALUES(?,?,?,?,?,?)";
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
            for (ItemOS itemDaOrdemServico: ordemServico.getItemOS()) {
                Servico servico = itemDaOrdemServico.getServico();
                itemDaOrdemServico.setOrdemServico(this.buscarUltimaOrdemServico());
                itemDaOrdemServicoDAO.inserir(itemDaOrdemServico);
            }
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
        String sql = "UPDATE ordemServico SET numero=?, total=?, agenda=?, desconto=?, status=?, id_veiculo=? WHERE id=?";
        try {
            //antes de atualizar a nova ordemServico, a anterior terá seus itens de ordemServico removidos
            // e o estoque dos produtos da ordemServico sofrerão um estorno
            connection.setAutoCommit(false);
            ItemDaOrdemServicoDAO itemDaOrdemServicoDAO = new ItemDaOrdemServicoDAO();
            itemDaOrdemServicoDAO.setConnection(connection);
            ProdutoDAO produtoDAO = new ProdutoDAO();
            produtoDAO.setConnection(connection);
            EstoqueDAO estoqueDAO = new EstoqueDAO();
            estoqueDAO.setConnection(connection);

            //OrdemServico ordemServicoAnterior = buscar(ordemServico.getCdOrdemServico());
            OrdemServico ordemServicoAnterior = buscar(ordemServico);
            List<ItemDeOrdemServico> itensDeOrdemServico = itemDaOrdemServicoDAO.listarPorOrdemServico(ordemServicoAnterior);
            for (ItemDeOrdemServico iv : itensDeOrdemServico) {
                //Produto p = iv.getProduto(); //isto não da certo ...
                Produto p = estoqueDAO.getEstoque(iv.getProduto());
                p.getEstoque().repor(iv.getQuantidade());
                estoqueDAO.atualizar(p.getEstoque());
                itemDaOrdemServicoDAO.remover(iv);
            }
            //atualiza os dados da ordemServico
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(ordemServico.getData()));
            stmt.setBigDecimal(2, ordemServico.getTotal());
            stmt.setBoolean(3, ordemServico.isPago());
            stmt.setDouble(4, ordemServico.getTaxaDesconto());
            stmt.setString(5, OrdemServico.getEmpresa());
            if  (ordemServico.getStatusOrdemServico() != null) {
                stmt.setString(6, ordemServico.getStatusOrdemServico().name());
            } else {
                stmt.setString(6, EStatusOrdemServico.ABERTA.name());
            }
            stmt.setInt(7, ordemServico.getCliente().getId());
            stmt.setInt(8, ordemServico.getId());
            stmt.execute();
            for (ItemDeOrdemServico iv: ordemServico.getItensDeOrdemServico()) {
                //Produto p = iv.getProduto(); //isto não da certo ...
                Produto p = estoqueDAO.getEstoque(iv.getProduto());
                p.getEstoque().retirar(iv.getQuantidade());
                estoqueDAO.atualizar(p.getEstoque());
                itemDaOrdemServicoDAO.inserir(iv);
            }
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

    public boolean remover(Cliente cliente) {
        String sql = "DELETE FROM ordemServico WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            try {
                connection.setAutoCommit(false);
                ItemDaOrdemServicoDAO itemDaOrdemServicoDAO = new ItemDaOrdemServicoDAO();
                itemDaOrdemServicoDAO.setConnection(connection);
                ProdutoDAO produtoDAO = new ProdutoDAO();
                produtoDAO.setConnection(connection);
                EstoqueDAO estoqueDAO = new EstoqueDAO();
                estoqueDAO.setConnection(connection);
                for (ItemDeOrdemServico itemDeOrdemServico : ordemServico.getItensDeOrdemServico()) {
                    Produto produto = itemDeOrdemServico.getProduto();
                    produto.getEstoque().repor(itemDeOrdemServico.getQuantidade());
                    estoqueDAO.atualizar(produto.getEstoque());
                    itemDaOrdemServicoDAO.remover(itemDeOrdemServico);
                }
                stmt.setInt(1, ordemServico.getId());
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

    public List<Cliente> listar() {
        String sql = "SELECT * FROM ordemServico";
        List<OrdemServico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                OrdemServico ordemServico = new OrdemServico();
                Cliente cliente = new Cliente();
                List<ItemDeOrdemServico> itensDeOrdemServico = new ArrayList();

                ordemServico.setId(resultado.getInt("id"));
                ordemServico.setData(resultado.getDate("data").toLocalDate());
                //ordemServico.setTotal(resultado.getBigDecimal("total"));
                ordemServico.setPago(resultado.getBoolean("pago"));
                ordemServico.setTaxaDesconto(resultado.getDouble("taxa_desconto"));
                ordemServico.setStatusOrdemServico(Enum.valueOf(EStatusOrdemServico.class, resultado.getString("situacao")));
                OrdemServico.setEmpresa(resultado.getString("empresa"));
                cliente.setId(resultado.getInt("id_cliente"));

                //Obtendo os dados completos do Cliente associado à OrdemServico
                ClienteDAO clienteDAO = new ClienteDAO();
                clienteDAO.setConnection(connection);
                cliente = clienteDAO.buscar(cliente);

                //Obtendo os dados completos dos Itens de OrdemServico associados à OrdemServico
                ItemDaOrdemServicoDAO itemDaOrdemServicoDAO = new ItemDaOrdemServicoDAO();
                itemDaOrdemServicoDAO.setConnection(connection);
                itensDeOrdemServico = itemDaOrdemServicoDAO.listarPorOrdemServico(ordemServico);

                ordemServico.setCliente(cliente);
                ordemServico.setItensDeOrdemServico(itensDeOrdemServico);
                retorno.add(ordemServico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public OrdemServico buscar(OrdemServico ordemServico) {
        String sql = "SELECT * FROM ordemServico WHERE id=?";
        OrdemServico ordemServicoRetorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, ordemServico.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Cliente cliente = new Cliente();
                ordemServicoRetorno.setId(resultado.getInt("id"));
                ordemServicoRetorno.setData(resultado.getDate("data").toLocalDate());
//                ordemServicoRetorno.setTotal(resultado.getBigDecimal("total"));
                ordemServicoRetorno.setStatusOrdemServico(Enum.valueOf(EStatusOrdemServico.class, resultado.getString("situacao")));
                ordemServicoRetorno.setPago(resultado.getBoolean("pago"));
                ordemServicoRetorno.setTaxaDesconto(resultado.getDouble("taxa_desconto"));
                //ordemServicoRetorno.setEmpresa(resultado.getString("empresa"));
                cliente.setId(resultado.getInt("id_cliente"));
                ordemServicoRetorno.setCliente(cliente);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ordemServicoRetorno;
    }

    public Cliente buscar(int id) {
        String sql = "SELECT * FROM ordemServico WHERE id=?";
        OrdemServico ordemServicoRetorno = new OrdemServico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Cliente cliente = new Cliente();
                ordemServicoRetorno.setId(resultado.getInt("id"));
                ordemServicoRetorno.setData(resultado.getDate("data").toLocalDate());
//                ordemServicoRetorno.setTotal(resultado.getBigDecimal("total"));
                ordemServicoRetorno.setStatusOrdemServico(Enum.valueOf(EStatusOrdemServico.class, resultado.getString("situacao")));
                ordemServicoRetorno.setPago(resultado.getBoolean("pago"));
                ordemServicoRetorno.setTaxaDesconto(resultado.getDouble("taxa_desconto"));
                //ordemServicoRetorno.setEmpresa(resultado.getString("empresa"));
                cliente.setId(resultado.getInt("id_cliente"));
                ordemServicoRetorno.setCliente(cliente);
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
                retorno.setId(resultado.getInt("max"));
                return retorno;
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdemServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    private Cliente populateVO(ResultSet rs) throws SQLException {
        Cliente cliente;
        if (rs.getString("cnpj") == null || rs.getString("cnpj").length() <= 0) {
            //é um fornecedor nacional
            cliente = new PessoaFisica();
            ((PessoaFisica)cliente).setCpf(rs.getString("cpf"));
            ((PessoaFisica)cliente).setDataNascimento(rs.getString("data_nascimento"));
        } else {
            //é um fornecedor internacional
            cliente = new PessoaJuridica();
            ((PessoaJuridica)cliente).setCnpj(rs.getString("cnpj"));
            ((PessoaJuridica)cliente).setInscricaoEstadual(rs.getString("inscricao_estadual"));
        }
        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCelular(rs.getString("celular"));
        cliente.setEmail(rs.getString("email"));
        cliente.setDataCadastro(rs.getString("data_cadastro"));
        return cliente;
    }

}

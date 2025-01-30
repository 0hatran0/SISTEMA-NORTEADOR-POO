/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.OrdemServicoDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.utils.AlertDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroOrdemDeServicoController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbOSAgenda;

    @FXML
    private Label lbOSDesconto;

    @FXML
    private Label lbOSNumero;

    @FXML
    private Label lbOSStatus;

    @FXML
    private Label lbOSTotal;

    @FXML
    private Label lbOSVeiculo;

    @FXML
    private TableColumn<OrdemServico, String> tableColumnNumeroDaOrdem;

    @FXML
    private TableColumn<OrdemServico, String> tableColumnTotalDaOrdem;

    @FXML
    private TableColumn<OrdemServico, String> tableColumnVeiculoDaOrdem;

//    @FXML
//    private TableColumn<Veiculo, String> tableColumnVeiculoDaOrdem;

    @FXML
    private TableView<OrdemServico> tableViewOrdemDeServico;

    private List<OrdemServico> listaOrdemServicos;
    private ObservableList<OrdemServico> observableListOrdemServicos;

    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final OrdemServicoDAO vendaDAO = new OrdemServicoDAO();
    private final ItemOSDAO itemOSDAO = new ItemOSDAO();
    private final ServicoDAO produtoDAO = new ServicoDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vendaDAO.setConnection(connection);

        carregarTableView();

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));
    }

    public void carregarTableView() {
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        tableColumnOrdemServicoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        //tableColumnOrdemServicoData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableColumnOrdemServicoData.setCellFactory(column -> {
            return new TableCell<OrdemServico, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(myDateFormatter.format(item));
                    }
                }
            };
        });

        tableColumnOrdemServicoData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tableColumnOrdemServicoCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));

        listaOrdemServicos = vendaDAO.listar();

        observableListOrdemServicos = FXCollections.observableArrayList(listaOrdemServicos);
        tableView.setItems(observableListOrdemServicos);
    }

    public void selecionarItemTableView(OrdemServico venda) {
        if (venda != null) {
            labelOrdemServicoId.setText(Integer.toString(venda.getId()));
            labelOrdemServicoData.setText(String.valueOf(
                    venda.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            labelOrdemServicoTotal.setText(String.format("%.2f", venda.getTotal()));
            labelOrdemServicoDesconto.setText((String.format("%.2f", venda.getTaxaDesconto())) + "%");
            checkBoxOrdemServicoPago.setSelected(venda.isPago());
            labelOrdemServicoSituacao.setText(venda.getStatusOrdemServico().name());
            labelOrdemServicoCliente.setText(venda.getCliente().getNome());
        } else {
            labelOrdemServicoId.setText("");
            labelOrdemServicoData.setText("");
            labelOrdemServicoTotal.setText("");
            labelOrdemServicoDesconto.setText("");
            checkBoxOrdemServicoPago.setSelected(false);
            labelOrdemServicoSituacao.setText("");
            labelOrdemServicoCliente.setText("");
        }
    }

    @FXML
    private void handleButtonInserir(ActionEvent event) throws IOException, SQLException {
        OrdemServico venda = new OrdemServico();
        List<ItemOS> itensOS = new ArrayList<>();
        venda.setItensOS(itensOS);
        boolean buttonConfirmarClicked = showFXMLAnchorPaneProcessoOrdemServicoDialog(venda);
        if (buttonConfirmarClicked) {
            //O código comentado a seguir (bloco try..catch) evidencia uma má prática de programação, haja vista que o boa parte da lógica de negócio está implementada no controller
            //PROBLEMA: caso haja necessidade de levar esta aplicação para outro nível (uma aplicação web, por exemplo), todo esse código deverá ser repetido no controller, o que
            //de fato pode se tornar inconsistente caso uma nova lógica seja necessária, implicando na necessidade de rever todos os controllers das aplicações, mas, o que garante
            // que todas equipes farão isso?
            //SOLUÇÃO: levar a lógica de negócio para o OrdemServicoDAO, afinal, estamos tratando de uma venda. É ela que deve resolver o problema
//            try {
//                connection.setAutoCommit(false);
//                vendaDAO.setConnection(connection);
//                vendaDAO.inserir(venda);
//                itemOSDAO.setConnection(connection);
//                produtoDAO.setConnection(connection);
//                estoqueDAO.setConnection(connection);
//                for (ItemOS itemOS: venda.getItensOS()) {
//                    Servico produto = itemOS.getServico();
//                    itemOS.setOrdemServico(vendaDAO.buscarUltimaOrdemServico());
//                    itemOSDAO.inserir(itemOS);
//                    produto.getEstoque().setQuantidade(
//                            produto.getEstoque().getQuantidade() - itemOS.getQuantidade());
//                    estoqueDAO.atualizar(produto.getEstoque());
//                }
//                connection.commit();
//                carregarTableView();
//            } catch (SQLException exc) {
//                try {
//                    connection.rollback();
//                } catch (SQLException exc1) {
//                    Logger.getLogger(FXMLAnchorPaneProcessoOrdemServicoController.class.getName()).log(Level.SEVERE, null, exc1);
//                }
//                Logger.getLogger(FXMLAnchorPaneProcessoOrdemServicoController.class.getName()).log(Level.SEVERE, null, exc);
//            }   
//        }
            vendaDAO.setConnection(connection);
            vendaDAO.inserir(venda);
            carregarTableView();
        }
    }

    @FXML
    private void handleButtonAlterar(ActionEvent event) throws IOException {
        OrdemServico venda = tableView.getSelectionModel().getSelectedItem();
        if (venda != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneProcessoOrdemServicoDialog(venda);
            if (buttonConfirmarClicked) {
                vendaDAO.alterar(venda);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um venda na Tabela.");
            alert.show();
        }
    }

    @FXML
    private void handleButtonRemover(ActionEvent event) throws SQLException {
        OrdemServico venda = tableView.getSelectionModel().getSelectedItem();
        if (venda != null) {
            if (AlertDialog.confirmarExclusao("Tem certeza que deseja excluir a venda " + venda.getId())) {
                vendaDAO.setConnection(connection);
                vendaDAO.remover(venda);
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Por favor, escolha uma venda na tabela!");
            alert.show();
        }
    }

    public boolean showFXMLAnchorPaneProcessoOrdemServicoDialog(OrdemServico venda) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneProcessoOrdemServicoDialogController.class.getResource(
                "/view/FXMLAnchorPaneProcessoOrdemServicoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de vendas");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //Setando o venda ao controller
        FXMLAnchorPaneProcessoOrdemServicoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setOrdemServico(venda);

        //Mostra o diálogo e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }


}

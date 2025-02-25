/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import br.edu.ifsc.fln.utils.AlertDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroClienteController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbClienteCPFouCNPJ;

    @FXML
    private Label lbClienteCelular;

    @FXML
    private Label lbClienteDataCadastro;

    @FXML
    private Label lbClienteEIouDataNasc;

    @FXML
    private Label lbClienteEmail;

    @FXML
    private Label lbClienteId;

    @FXML
    private Label lbClienteNome;

    @FXML
    private Label lbClienteTipo;

    @FXML
    private TableColumn<Cliente, String> tableColumnClienteNome;

    @FXML
    private TableView<Cliente> tableViewClientes;

    private List<Cliente> listaClientes;
    private ObservableList<Cliente> observableListClientes;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        carregarTableViewCliente();

        tableViewClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewClientes(newValue));
    }

    public void carregarTableViewCliente() {
        tableColumnClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        listaClientes = clienteDAO.listar();

        observableListClientes = FXCollections.observableArrayList(listaClientes);
        tableViewClientes.setItems(observableListClientes);
    }

    public void selecionarItemTableViewClientes(Cliente cliente) {
        if (cliente != null) {
            lbClienteId.setText(String.valueOf(cliente.getId()));
            lbClienteNome.setText(cliente.getNome());
            lbClienteCelular.setText(cliente.getCelular());
            lbClienteEmail.setText(cliente.getEmail());
            lbClienteDataCadastro.setText(String.valueOf(cliente.getDataCadastro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            if (cliente instanceof PessoaFisica) {
                lbClienteTipo.setText("PessoaFisica");
                lbClienteCPFouCNPJ.setText(((PessoaFisica)cliente).getCpf());
                lbClienteEIouDataNasc.setText(((PessoaFisica)cliente).getDataNascimento());
            } else {
                lbClienteTipo.setText("PessoaJuridica");
                lbClienteCPFouCNPJ.setText(((PessoaJuridica)cliente).getCnpj());
                lbClienteEIouDataNasc.setText(((PessoaJuridica)cliente).getInscricaoEstadual());
            }
        } else {
            lbClienteId.setText("");
            lbClienteNome.setText("");
            lbClienteCelular.setText("");
            lbClienteEmail.setText("");
            lbClienteTipo.setText("");
            lbClienteCPFouCNPJ.setText("");
            lbClienteEIouDataNasc.setText("");
        }

    }

    @FXML
    public void handleBtInserir() throws IOException {
        Cliente cliente = getTipoCliente();
        if (cliente != null ) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
            if (btConfirmarClicked) {
                clienteDAO.inserir(cliente);
                carregarTableViewCliente();
            }
        }
    }

    private Cliente getTipoCliente() {
        List<String> opcoes = new ArrayList<>();
        opcoes.add("PessoaFisica");
        opcoes.add("PessoaJuridica");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("PessoaFisica", opcoes);
        dialog.setTitle("Dialogo de Opções");
        dialog.setHeaderText("Escolha o tipo de cliente");
        dialog.setContentText("Tipo de cliente: ");
        Optional<String> escolha = dialog.showAndWait();
        if (escolha.isPresent()) {
            if (escolha.get().equalsIgnoreCase("PessoaFisica"))
                return new PessoaFisica();
            else
                return new PessoaJuridica();
        } else {
            return null;
        }
    }

    @FXML
    public void handleBtAlterar() throws IOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
            if (btConfirmarClicked) {
                clienteDAO.alterar(cliente);
                carregarTableViewCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um Cliente na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    public void handleBtExcluir() throws IOException {
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            if (AlertDialog.confirmarExclusao("Tem certeza que deseja excluir o cliente " + cliente.getNome())) {
                clienteDAO.remover(cliente);
                carregarTableViewCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cliente na tabela ao lado");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneCadastroClienteDialog(Cliente cliente) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroClienteController.class.getResource("/view/FXMLAnchorPaneCadastroClienteDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Cliente");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //enviando o obejto cliente para o controller
        FXMLAnchorPaneCadastroClienteDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);

        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();

        return controller.isBtConfirmarClicked();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroOrdemDeServicoDialogController implements Initializable {

    @FXML
    private Button btAdicionar;

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ComboBox<Servico> cbServico;

    @FXML
    private ComboBox<EStatus> cbStatus;

    @FXML
    private ComboBox<Veiculo> cbVeiculo;

    @FXML
    private MenuItem contextMenuItemAtualizarQtd;

    @FXML
    private MenuItem contextMenuItemRemoverItem;

    @FXML
    private ContextMenu contextMenuTableView;

    @FXML
    private DatePicker dpData;

    @FXML
    private TableColumn<ItemOS, String> tableColumnObservacao;

    @FXML
    private TableColumn<ItemOS, Servico> tableColumnServico;

    @FXML
    private TableColumn<ItemOS, Double> tableColumnValor;

    @FXML
    private TableView<ItemOS> tableViewItensOS;

    @FXML
    private TextField tfDesconto;

    @FXML
    private TextField tfTotal;

    @FXML
    private TextField tfValor;

    private List<Veiculo> listaVeiculos;
    private List<Servico> listaServicos;
    private ObservableList<Veiculo> observableListVeiculos;
    private ObservableList<Servico> observableListServicos;
    private ObservableList<ItemOS> observableListItensOS;

    //atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private OrdemServico ordemServico;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        veiculoDAO.setConnection(connection);
        servicoDAO.setConnection(connection);
        carregarComboBoxVeiculos();
        carregarComboBoxServicos();
        carregarChoiceBoxSituacao();
        setFocusLostHandle();
        tableColumnServico.setCellValueFactory(new PropertyValueFactory<>("servico"));
        tableColumnObservacao.setCellValueFactory(new PropertyValueFactory<>("observacao"));
        tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
    }
    
    public void carregarComboBoxVeiculos() {
        listaVeiculos = veiculoDAO.listar();
        observableListVeiculos =
                FXCollections.observableArrayList(listaVeiculos);
        cbVeiculo.setItems(observableListVeiculos);
    }

    public void carregarComboBoxServicos() {
        listaServicos = servicoDAO.listar();
        observableListServicos =
                FXCollections.observableArrayList(listaServicos);
        cbServico.setItems(observableListServicos);
    }

    public void carregarChoiceBoxSituacao() {
            cbStatus.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
                    event.consume();
                }
            }
        });
        cbStatus.setItems( FXCollections.observableArrayList( EStatus.values()));
    }

    private void setFocusLostHandle() {
//        textFieldDesconto.focusedProperty().addListener((ov, oldV, newV) -> {
//            if (!newV) { // focus lost
//                if (textFieldDesconto.getText() != null && !textFieldDesconto.getText().isEmpty()) {
//                    //System.out.println("teste focus lost");
//                    venda.setTaxaDesconto(Double.parseDouble(textFieldDesconto.getText()));
//                    textFieldValor.setText(venda.getTotal().toString());
//
//                }
//            }
//        });
    }

    /**
     * @return the dialogStage
     */
    public Stage getDialogStage() {
        return dialogStage;
    }

    /**
     * @param dialogStage the dialogStage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * @return the buttonConfirmarClicked
     */
    public boolean isBtConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    /**
     * @param buttonConfirmarClicked the buttonConfirmarClicked to set
     */
    public void setBtConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    /**
     * @return the ordemServico
     */
    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    /**
     * @param ordemServico the ordemServico to set
     */
    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
        if (ordemServico.getNumero() != 0) {
            cbVeiculo.getSelectionModel().select(this.ordemServico.getVeiculo());
            dpData.setValue(this.ordemServico.getAgenda());
            observableListItensOS = FXCollections.observableArrayList(
                    this.ordemServico.getItemOS());
            tableViewItensOS.setItems(observableListItensOS);
            tfDesconto.setText(String.format("%.2f", this.ordemServico.getDesconto()));
            tfTotal.setText(String.format("%.2f", this.ordemServico.getTotal()));
            cbStatus.getSelectionModel().select(this.ordemServico.getStatus());
        }
    }

    @FXML
    void handleBtAdicionar() {
        Servico servico;
        ItemOS itemOS = new ItemOS();
        if (cbServico.getSelectionModel().getSelectedItem() != null) {
            //o comboBox possui dados sintetizados de Produto para evitar carga desnecessária de informação
            servico = cbServico.getSelectionModel().getSelectedItem();
            //a instrução a seguir busca detalhes do produto selecionado
            servico  = servicoDAO.buscar(servico);
            itemOS.setServico(servico);
            itemOS.setValorServico(servico.getValor());
            itemOS.setObservacoes(itemOS.getObservacoes());
            itemOS.setOrdemServico(ordemServico);
            itemDeVenda.setVenda(venda);
            venda.getItensDeVenda().add(itemDeVenda);
            observableListItensDeVenda = FXCollections.observableArrayList(venda.getItensDeVenda());
            tableViewItensDeVenda.setItems(observableListItensDeVenda);
            textFieldValor.setText(String.format("%.2f", venda.getTotal()));
        }
    }
    
    @FXML
    private void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            veiculo.setPlaca(tfPlaca.getText());
            veiculo.setObservacoes(tfObservacoes.getText());
            veiculo.setModelo(cbModelo.getSelectionModel().getSelectedItem());
            veiculo.setCor(cbCor.getSelectionModel().getSelectedItem());
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    private void handleBtCancelar() {
        dialogStage.close();
    }

    @FXML
    void handleTableViewMouseClicked(MouseEvent event) {
        ItemOS itemOS
                = tableViewItensOS.getSelectionModel().getSelectedItem();
        if (itemOS == null) {
            contextMenuItemAtualizarQtd.setDisable(true);
            contextMenuItemRemoverItem.setDisable(true);
        } else {
            contextMenuItemAtualizarQtd.setDisable(false);
            contextMenuItemRemoverItem.setDisable(false);
        }

    }

    @FXML
    private void handleContextMenuItemAtualizarQtd() {
        ItemDeVenda itemDeVenda
                = tableViewItensDeVenda.getSelectionModel().getSelectedItem();
        int index = tableViewItensDeVenda.getSelectionModel().getSelectedIndex();

        int qtdAtualizada = Integer.parseInt(inputDialog(itemDeVenda.getQuantidade()));
        if (itemDeVenda.getProduto().getEstoque().getQuantidade() >= qtdAtualizada) {
            itemDeVenda.setQuantidade(qtdAtualizada);
            //venda.getItensDeVenda().set(venda.getItensDeVenda().indexOf(itemDeVenda),itemDeVenda);
            venda.getItensDeVenda().set(index, itemDeVenda);
            itemDeVenda.setValor(itemDeVenda.getProduto().getPreco().multiply(BigDecimal.valueOf(itemDeVenda.getQuantidade())));
            tableViewItensDeVenda.refresh();
            textFieldValor.setText(String.format("%.2f", venda.getTotal()));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Erro no estoque");
            alert.setContentText("Não há quantidade suficiente de produtos para venda.");
            alert.show();
        }
    }

    private String inputDialog(int value) {
        TextInputDialog dialog = new TextInputDialog(Integer.toString(value));
        dialog.setTitle("Entrada de dados.");
        dialog.setHeaderText("Atualização da quantidade de produtos.");
        dialog.setContentText("Quantidade: ");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        return result.get();
    }

    @FXML
    private void handleContextMenuItemRemoverItem() {
        ItemDeVenda itemDeVenda
                = tableViewItensDeVenda.getSelectionModel().getSelectedItem();
        int index = tableViewItensDeVenda.getSelectionModel().getSelectedIndex();
        venda.getItensDeVenda().remove(index);
        observableListItensDeVenda = FXCollections.observableArrayList(venda.getItensDeVenda());
        tableViewItensDeVenda.setItems(observableListItensDeVenda);

        textFieldValor.setText(String.format("%.2f", venda.getTotal()));
    }
    
        //validar entrada de dados do cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        
        if (tfPlaca.getText() == null || this.tfPlaca.getText().length() == 0) {
            errorMessage += "Placa inválido.\n";
        }

        if (tfObservacoes.getText() == null || this.tfObservacoes.getText().length() == 0) {
            errorMessage += "Observação inválido.\n";
        }
        
        if (cbModelo.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione um modelo para veiculo!\n";
        }

        if (cbCor.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma cor para veiculo!\n";
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campo(s) inválido(s), por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
   
}

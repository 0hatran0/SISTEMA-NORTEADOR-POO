/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mpisching
 */
public class FXMLAnchorPaneCadastroModeloDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ChoiceBox<ECategoria> cbCategoria;

    @FXML
    private ChoiceBox<ETipoCombustivel> cbCombustivel;

    @FXML
    private ComboBox<Marca> cbMarca;

    @FXML
    private TextField tfDescricao;

    @FXML
    private TextField tfPotencia;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();
    
    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Modelo modelo;  
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marcaDAO.setConnection(connection);
        carregarChoiceBoxCategoria();
        carregarChoiceBoxCombustivela();
        carregarComboBoxMarcas();
    } 
    
    public void carregarChoiceBoxCategoria() {
        cbCategoria.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
                    event.consume();
                }
            }
        });
        cbCategoria.setItems( FXCollections.observableArrayList( ECategoria.values()));
        //cbCategoria.getSelectionModel().select(-1);
    }    
    
    public void carregarChoiceBoxCombustivela() {
        cbCombustivel.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
                    event.consume();
                }
            }
        });
        cbCombustivel.setItems( FXCollections.observableArrayList( ETipoCombustivel.values()));
        //cbCategoria.getSelectionModel().select(-1);
    }
    
    private List<Marca> listaMarcas;
    private ObservableList<Marca> observableListMarcas; 
    
    public void carregarComboBoxMarcas() {
        listaMarcas = marcaDAO.listar();
        observableListMarcas = 
                FXCollections.observableArrayList(listaMarcas);
        cbMarca.setItems(observableListMarcas);
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
     * @return the modelo
     */
    public Modelo getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
        this.tfDescricao.setText(this.modelo.getDescricao());
        this.cbMarca.getSelectionModel().select(this.modelo.getMarca());
        this.cbCategoria.getSelectionModel().select(this.modelo.getECategoria());
        this.tfPotencia.setText(Integer.toString(modelo.getMotor().getPotencia()));
        this.cbCombustivel.getSelectionModel().select(this.modelo.getMotor().getTipoCombustivel());
    }    
    
    @FXML
    private void handleBtConfirmar() {
        if (validarEntradaDeDados()) {
            modelo.setDescricao(tfDescricao.getText());
            modelo.setMarca(cbMarca.getSelectionModel().getSelectedItem());
            modelo.setECategoria(cbCategoria.getSelectionModel().getSelectedItem());
            modelo.getMotor().setPotencia(Integer.parseInt(tfPotencia.getText()));
            modelo.getMotor().setTipoCombustivel(cbCombustivel.getSelectionModel().getSelectedItem());
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    private void handleBtCancelar() {
        dialogStage.close();
    }
    
        //validar entrada de dados do cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        
        if (tfDescricao.getText() == null || this.tfDescricao.getText().length() == 0) {
            errorMessage += "Descricao inválido.\n";
        }
        
        if (cbMarca.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma marca para modelo!\n";
        }
        
        if (cbCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma categoria para o modelo!\n";
        }
        
        if (tfPotencia.getText() == null || this.tfPotencia.getText().length() == 0) {
            errorMessage += "Pontencia inválida.\n";
        }
        
        if (cbCombustivel.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione um combustivel para o motor!\n";
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.zafirodesktop.entity.Turn;
import com.zafirodesktop.util.ComboBoxChoices;
import com.zafirodesktop.util.FormValidation;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Digitall
 */
public class DialogController extends FormValidation implements Initializable {

    /*
     Form values
     */
    @FXML
    private AnchorPane mainPane;
    @FXML
    private HBox buttonsBox;
    @FXML
    private Label message;
    @FXML
    private Label warningMessage;
    @FXML
    private Label title;
    @FXML
    private Label expectedBoxAmount;
    @FXML
    private Label boxAmountDiference;
    @FXML
    private TextField txtInitialAmount;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private ComboBox<ComboBoxChoices> options;
    @FXML
    private GridPane boxClosePane;

    /*
     Controller values
     */
    private ResourceBundle bundle;
    private InvoiceController invoiceController;
    private ProductController productController;
    private PersonController personController;
    private BackupController backupController;
    private DepositController depositController;
    private MainController mainController;
    private LoginController loginController;
    private int type;
    private Turn turn;
    private double expectedAmount, realAmount, difference;
    private boolean closeTurn;
    private boolean isCloseBox;
    private boolean isBDRestore;
    private DecimalFormat format;
    static final int SERVICE_ORDER = 0;
    static final int INVOICE = 1;
    static final int ORDER = 2;
    static final int PRODUCT = 3;
    static final int PERSON = 4;
    static final int MOVEMENT = 5;
    static final int IMPORT = 6;
    static final int OUTPUT = 7;
    static final int QUOTATION = 8;
    static final int ALERT = 9;
    static final int DEPOSIT = 10;
    static final int LOG_OUT = 11;
    static final int CONTINUE_TURN = 12;
    static final int CHANGE_VIEW = 13;

    public InvoiceController getInvoiceController() {
        return invoiceController;
    }

    public void setInvoiceController(InvoiceController invoiceController) {
        this.invoiceController = invoiceController;
    }

    public ProductController getProductController() {
        return productController;
    }

    public void setProductController(ProductController productController) {
        this.productController = productController;
    }

    public void setPersonController(PersonController personController) {
        this.personController = personController;
    }

    public void setBackupController(BackupController backupController) {
        this.backupController = backupController;
    }

    public void setDepositController(DepositController depositController) {
        this.depositController = depositController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setIsBDRestore(boolean isBDRestore) {
        this.isBDRestore = isBDRestore;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
    }

    /*
     Método para inicializar los valores del formulario
     */
    public void initializeValues(String bundleMessage, int type) {
        this.type = type;
        message.setText(bundle.getString(bundleMessage));
    }

    /*
     Método para inicializar los valores del formulario de dinero inicial en caja
     */
    public void initializeValuesCashBox() {
        txtInitialAmount.setPromptText(bundle.getString("initialAmountDesc"));
    }

    /*
     Método para inicializar los valores del formulario para continuar o cerrar un turno activo
     */
    public void initializeValuesContinueTurn(Turn activeTurn) {
        turn = activeTurn;
        title.setText(bundle.getString("alertTitle"));
        okButton.setText(bundle.getString("continueTurn"));
        okButton.setPrefWidth(150);
        cancelButton.setText(bundle.getString("closeTurn"));
        cancelButton.setPrefWidth(150);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    loginController.closeTurn(turn);
                    closeModal();
                } catch (IOException ex) {
                    Logger.getLogger(DialogController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    /*
     Método para inicializar los valores del formulario para el cierre de turno
     */
    public void initializeValuesCloseSession(Turn activeTurn, String optionID) {
        turn = activeTurn;
        format = new DecimalFormat("###,###.##");
        realAmount = new Float("0");
        if (turn != null) {
            expectedAmount = turn.getExpectedAmount();
        } else {
            expectedAmount = mainController.getSessionUser().getExpectedAmount();
        }
        ObservableList<ComboBoxChoices> typesList = FXCollections.observableArrayList(
                new ComboBoxChoices("0", bundle.getString("closeSessionOption1")),
                new ComboBoxChoices("1", bundle.getString("closeSessionOption2"))
        );
        options.getItems().addAll(typesList);
        if (turn != null) {
            options.getSelectionModel().selectLast();
            options.setDisable(true);
        }else{
            options.getSelectionModel().selectFirst();
        }
        //Se verifica si es cierre de caja y se lleva a cabo lo correspondiente
        if(optionID!=null){
            isCloseBox = true;
            closeTurn = true;
            options.getSelectionModel().selectLast();
            options.setDisable(true);
            message.setVisible(true);
            message.setText(bundle.getString("warningMessageCB"));
            title.setText(bundle.getString("finalAmountTitle"));
        }
        expectedBoxAmount.setText(format.format(expectedAmount));
    }

    /*
     Método para inicializar los valores para alertas
     */
    public void initializeAlert(int type, String msg) {
        this.type = type;
        title.setText(bundle.getString("alertTitle"));
        buttonsBox.getChildren().remove(cancelButton);
        message.setText(msg);
    }

    /*
     Método para ejecutar la acción predeterminada una vez se haya aceptado
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void performAction(ActionEvent event) {
        if (type == INVOICE) {
            invoiceController.invalidateInvoice();
            closeModal();
        } else {
            if (type == ORDER) {
                invoiceController.deleteInvoice();
                closeModal();
            } else if (type == QUOTATION) {
                invoiceController.deleteQuotation();
                closeModal();
            } else if (type == PRODUCT) {
                productController.deleteProduct();
                closeModal();
            } else if (type == PERSON) {
                personController.deletePerson();
                closeModal();
            } else if (type == OUTPUT) {
                depositController.deleteOutput();
                closeModal();
            } else if (type == DEPOSIT) {
                depositController.deleteDeposit();
                closeModal();
            } else if (type == MOVEMENT || type == SERVICE_ORDER) {
                invoiceController.activeInactiveMovement();
                closeModal();
            } else if (type == CHANGE_VIEW) {
                try {
                    mainController.loadView("main");
                } catch (Exception ex) {
                    Logger.getLogger(DialogController.class.getName()).log(Level.SEVERE, null, ex);
                }
                closeModal();
            }else if (type == ALERT) {
                closeModal();
            }else if (type == CONTINUE_TURN) {
                try {
                    loginController.loginAction(null, turn);
                } catch (Exception ex) {
                    Logger.getLogger(DialogController.class.getName()).log(Level.SEVERE, null, ex);
                }
                closeModal();
            } else if (type == LOG_OUT) {
                try {
                    mainController.logOut(event, 0.0, true, false, isBDRestore);
                } catch (Exception ex) {
                    Logger.getLogger(DialogController.class.getName()).log(Level.SEVERE, null, ex);
                }
                closeModal();
            } else {                
                backupController.importBackup();
                closeModal();
            }
        }
    }

    /*
     Método para iniciar la aplicación luego de registrar el dinero inicial en caja
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void setTurnInitialAmount(ActionEvent event) throws Exception {
        double initialAmount = Double.parseDouble(txtInitialAmount.getText());
        loginController.loginAction(initialAmount, null);
        closeModal();
    }

    /*
     Método para actualizar la diferencia entre los valores de dinero en caja
     */
    private void setBoxAmountsDifference() {
        realAmount = Float.parseFloat(txtInitialAmount.getText());
        if(expectedAmount>0){
            difference = realAmount - expectedAmount;
        }else{
            difference = realAmount + expectedAmount;
        }
        boxAmountDiference.setText(bundle.getString("moneyNotation") + " " + format.format(difference));
        if (difference >= 0) {
            if(boxAmountDiference.getStyleClass().contains("lblValidateError")){
                boxAmountDiference.getStyleClass().remove("lblValidateError");
            }
            if(!boxAmountDiference.getStyleClass().contains("lblValidateSucces")){
                boxAmountDiference.getStyleClass().add("lblValidateSucces");
            }
        } else {
            if(boxAmountDiference.getStyleClass().contains("lblValidateSucces")){
                boxAmountDiference.getStyleClass().remove("lblValidateSucces");
            }
            if(!boxAmountDiference.getStyleClass().contains("lblValidateError")){
                boxAmountDiference.getStyleClass().add("lblValidateError");
            }
        }

    }

    /*
     Método para cerrar la sesión de acuerdo a la acción seleccionada
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void closeSession(ActionEvent event) throws Exception {
        if(turn!=null){
            loginController.closeTurnAction(turn, realAmount);
        }else{
            mainController.logOut(event, realAmount, closeTurn, isCloseBox, isBDRestore);
        }
        closeModal();
    }

    /*
     Método para validar el campo initialAmount
     */
    public void validateIA(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        String text = t.getText();
        if (notNull(txtInitialAmount, message, length, bundle, bundle.getString("cashBoxinitialAmountTltp"))) {
            if (isNumeric(txtInitialAmount, message, text, bundle)) {
                maxLenght(txtInitialAmount, message, length, 23, bundle);
            }
        }
        validForm();
    }

    /*
     Método para cambiar los valores de acuerdo al tipo de cierre de sesión seleccionado
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void updateCloseType(ActionEvent event) {
        ComboBoxChoices selectedAction = options.getSelectionModel().getSelectedItem();
        switch (selectedAction.getItemValue()) {
            case "0":
                if (boxClosePane.isVisible()) {
                    boxClosePane.setVisible(false);
                }
                if (okButton.isDisable()) {
                    okButton.setDisable(false);
                }
                closeTurn = false;
                break;
            default:
                if (!boxClosePane.isVisible()) {
                    boxClosePane.setVisible(true);
                }
                closeTurn = true;
                validForm();
                break;
        }
    }

    /*
     Método para validar que el formulario ya esta listo para ser enviado
     */
    void validForm() {
        if (!message.isVisible() && !txtInitialAmount.getText().isEmpty()) {
            okButton.setDisable(false);
            if (options != null) {
                setBoxAmountsDifference();
            }
        } else {
            okButton.setDisable(true);
        }
    }

    /*
     Método para activar/desactivar el botón de acción del formulario
     @param: bandera de activar o desactivar boolean 
     */
    public void setDisableActionButton(boolean deactivate) {
        okButton.setDisable(deactivate);
    }

    /*
     Método para cerrar el modal iniciado sin guardar ninguna cambio, desde el boto cancelar
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void closeButtonAction(ActionEvent event) {
        closeModal();
    }
    
    private void closeModal() {
        Scene scene = mainPane.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

}

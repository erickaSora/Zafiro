/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.itextpdf.text.DocumentException;
import com.zafirodesktop.entity.Concept;
import com.zafirodesktop.entity.Deposit;
import com.zafirodesktop.entity.Remission;
import com.zafirodesktop.entity.Tranzaction;
import com.zafirodesktop.entity.Turn;
import com.zafirodesktop.entity.Warehouse;
import com.zafirodesktop.model.AbstractFacade;
import com.zafirodesktop.model.ConceptModel;
import com.zafirodesktop.model.DepositModel;
import com.zafirodesktop.model.RemissionModel;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.util.ComboBoxChoices;
import com.zafirodesktop.util.FormValidation;
import com.zafirodesktop.util.LogActions;
import com.zafirodesktop.util.PrintPDF;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.awt.HeadlessException;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Digitall
 */
public class DepositController extends FormValidation implements Initializable {

    /*
     Especificación de los componentes asociados en el FXML
     */
    @FXML
    private AnchorPane mainPane;
    @FXML
    private HBox buttonsBox;
    @FXML
    private HBox datePickerBox;
    @FXML
    private TextField deposit;
    @FXML
    private TextArea obs;
    @FXML
    private ComboBox<ComboBoxChoices> type;
    @FXML
    private Label lblSaldo;
    @FXML
    private Label title;
    @FXML
    private Label vldDeposit;
    @FXML
    private Label vldOutputDate;
    @FXML
    private Label lblAlvailableCash;
    @FXML
    private HBox buttonBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button printButton;
    @FXML
    private Button rePrintButton;
    @FXML
    private Button helpButton;
    @FXML
    private TableView actualDeposits;
    @FXML
    private TableColumn depositId;
    @FXML
    private TableColumn depositDate;
    @FXML
    private TableColumn depositValue;
    @FXML
    private Tooltip tlpHelp;
    @FXML
    private CheckBox orderPayment;

    /*
     Objetos y variables del controlador 
     */
    public final static int DEPOSIT = 1;
    public final static int DEPOSIT_ORDER = 0;
    private MainController mainController;
    private ResourceBundle bundle;
    private AbstractFacade abs;
    private Remission actualRemission;
    private Tranzaction tranzaction;
    public DatePicker outputDate;
    private boolean invoiceExists;
    private Turn user;
    private Double total;
    private Double totalDeposit;
    private Double leftAmount;
    private Double oldInvoicedValue;
    private int modalType;
    private int creditType;

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
    }

    /*
     Método para inicializar los valores del formulario
     */
    public void initializeValues(Remission remision, Turn sessionUser, int creditType) {
        this.creditType = creditType;
        actualRemission = remision;
        user = sessionUser;
        modalType = DialogController.DEPOSIT;
        leftAmount = actualRemission.getLeftAmount();
        DecimalFormat format = new DecimalFormat("###,###.##");
        lblSaldo.setText(format.format(leftAmount));
        RemissionModel rm = new RemissionModel();
        depositId = new TableColumn(bundle.getString("idDepositDatatable"));
        depositId.prefWidthProperty().bind(((actualDeposits.widthProperty().divide(3))));
        depositDate = new TableColumn(bundle.getString("dateDepositDatatable"));
        depositDate.prefWidthProperty().bind(((actualDeposits.widthProperty().divide(3))));
        depositValue = new TableColumn(bundle.getString("depositDepositDatatable"));
        depositValue.prefWidthProperty().bind(((actualDeposits.widthProperty().divide(2.9))));
        depositId.setCellValueFactory(new PropertyValueFactory<Remission, Integer>("idDepositPk"));
        depositDate.setCellValueFactory(new PropertyValueFactory<Remission, String>("date"));
        depositValue.setCellValueFactory(new PropertyValueFactory<Remission, String>("tranzactionDeposit"));
        actualDeposits.getColumns().addAll(depositId, depositDate, depositValue);
        Image img = new Image("/com/zafirodesktop/ui/img/ico/pointer_black.png");
        ImageView imgvw = new ImageView(img);
        imgvw.setFitHeight(15);
        imgvw.setFitWidth(15);
        tlpHelp.setGraphic(imgvw);
        Collection<Deposit> allAsociatedDeposits = rm.findAllByIdRemission("Deposit", actualRemission.getIdRemissionPk());
        actualDeposits.getItems().addAll(allAsociatedDeposits);
        if (leftAmount == 0) {
            deposit.setDisable(true);
            obs.setDisable(true);
        }
        //Se oculta el botón de impresión en caso de que sea una cuenta por pagar
        if(creditType == DEPOSIT_ORDER)
            rePrintButton.setVisible(false);
        if (!actualDeposits.getItems().isEmpty()) {
            deleteButton.setDisable(false);
            //Se comprueba que sea una abono de cliente para poder hacer la impresión
            if(creditType == DEPOSIT)
                rePrintButton.setDisable(false);
        }
        if (user.getIdUserFk().getCreatedOn() < 1) {
            deleteButton.setVisible(false);
        }
    }

    /*
     Método para inicializar los valores del formulario de Egresos
     @param: Usuario que esta ejecutando la acción Users sessionUser
     */
    public void initializeValuesOutput(Turn sessionUser) {
        user = sessionUser;
        outputDate = new DatePicker(Locale.getDefault());
        outputDate.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        outputDate.getCalendarView().todayButtonTextProperty().set("Hoy");
        outputDate.getCalendarView().setShowWeeks(false);
        outputDate.getStylesheets().add("com/zafirodesktop/ui/css/DatePicker.css");
        datePickerBox.getChildren().add(outputDate);
        title.setText(bundle.getString("outputTitle"));
        if(mainController.isUseCashBox()){
            DecimalFormat format = new DecimalFormat("###,###.##");
            lblAlvailableCash.setText(bundle.getString("lblAlvailableCash")+" "+bundle.getString("moneyNotation")+format.format(sessionUser.getExpectedAmount()));
            orderPayment.setVisible(true);
        }
        deposit.setPromptText(bundle.getString("lblOutputValue"));
        obs.setPromptText(bundle.getString("lblInvoiceObs"));
        ConceptModel cm = new ConceptModel();
        Collection<Concept> conceptsList = cm.findAllByType("E");
        ObservableList<ComboBoxChoices> typesList = FXCollections.observableArrayList();
        for (Concept concepts : conceptsList) {
            typesList.add(new ComboBoxChoices(concepts.getIdConceptPk().toString(), concepts.getConceptName()));
        }
        type.getItems().addAll(typesList);
        type.getSelectionModel().selectFirst();
        saveButton.setDisable(true);
        buttonsBox.getChildren().remove(deleteButton);
    }

    /*
     Método para inicializar los valores del formulario de Egresos ya existentes
     @param: Remision actual (Remission remission)
     */
    public void initializeValuesOutputExits(Remission remi) {
        actualRemission = remi;
        invoiceExists = true;
        modalType = DialogController.OUTPUT;
        double price = 0;
        ComboBoxChoices selectedConcept = new ComboBoxChoices(remi.getIdConceptFk().getIdConceptPk().toString(), remi.getIdConceptFk().getConceptName());
        type.getSelectionModel().select(selectedConcept);
        for (Tranzaction tranza : remi.getTranzactionCollection()) {
            tranzaction = tranza;
            price = tranza.getTransactionPrice();
        }
        oldInvoicedValue = price;
        lblAlvailableCash.setText("");
        deposit.setText(String.valueOf(price));
        obs.setText(remi.getObs());
        orderPayment.setDisable(true);
        if(tranzaction.getCashBoxChecked()!=null)
            orderPayment.setSelected(true);
        else
            orderPayment.setSelected(false);
        saveButton.setDisable(false);
        buttonsBox.getChildren().add(1, deleteButton);
    }

    public void saveDeposit(ActionEvent event) {
        try {
            int tipe = 3;
            PrintPDF pdf = new PrintPDF();
            SessionBD.persistenceCreate();
            DepositModel dm = new DepositModel();
            abs = new AbstractFacade();
            Deposit actualDeposit = new Deposit();
            actualDeposit.setDeposit(Double.parseDouble(deposit.getText()));
            actualDeposit.setIdRemissionFk(actualRemission);
            actualDeposit.setIdSell(user.getIdTurnPk().toString());
            actualDeposit.setObs(obs.getText());
            //abs.saveIntermediate(actualDeposit);
            totalDeposit = Double.parseDouble(deposit.getText());
            leftAmount = (leftAmount - totalDeposit);
            actualRemission.setLeftAmount(leftAmount);
            actualRemission.addDeposit(actualDeposit);
            //abs.updateFinal(actualRemission);
            dm.saveDeposit(actualDeposit, actualRemission);
            //Se actualiza el valor esperado en caja
            updateBoxExpectedMoney(user, totalDeposit, true);
            //Se ratifican los cambios de la transacción
            dm.executeCommit();
            try {
                Remission remissionPrint = actualRemission;
                remissionPrint.getDepositCollection().clear();
                remissionPrint.addDeposit(actualDeposit);
                //Se comprueba que sea un abono de cliente para imprimir el recibo
                if(creditType == 1)
                    pdf.printInvoice(null, remissionPrint, null, null, null, tipe, bundle, true);
                mainController.setMessage(bundle.getString("depositSuccess"), false);
                mainController.setDataTableView();
                closeModal();
            } catch (Exception e) {
                getMainController().setDataTableView();
                getMainController().setMessage(bundle.getString("createPrintError"), true);
                closeModal();
                e.printStackTrace();
            }
        } catch (Exception e) {
            abs.executeRollback();
            mainController.setMessage(bundle.getString("depositError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Registrar depósito", e.getMessage(), sw.toString(), user.toString());
            closeModal();
            //e.printStackTrace();
        }
    }

    /*
     Método para guardar los valores del formulario de Egresos
     @param: Acción que ejecuta el evento (ActionEvent onAction)
    */
    public void saveOutput(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            ComboBoxChoices selectedConcept = type.getSelectionModel().getSelectedItem();
            Concept concept = (Concept) abs.findByIdInt("Concept", Integer.valueOf(selectedConcept.getItemValue()));
            if (!invoiceExists) {
                Warehouse warehouse = (Warehouse) abs.findByIdInt("Warehouse", 1);
                actualRemission = new Remission();
                actualRemission.setStatus(new Short("1"));
                actualRemission.setInvoiced(new Short("0"));
                actualRemission.setIdTurnFk(user);
                tranzaction = new Tranzaction();
                tranzaction.setIdRemissionFk(actualRemission);
                tranzaction.setIdWarehouseFk(warehouse);
                //Se comprueba que se haya seleccionado pago con caja
                if(orderPayment.isSelected())
                    tranzaction.setCashBoxChecked(new Short("1"));
            }
            actualRemission.setIdConceptFk(concept);
            if (outputDate.getSelectedDate() != null) {
                actualRemission.setRemissionDate(outputDate.getSelectedDate());
                tranzaction.setTransactionDate(outputDate.getSelectedDate());
            }
            actualRemission.setObs(obs.getText());
            tranzaction.setTransactionPrice(Double.parseDouble(deposit.getText()));
            if (invoiceExists) {
                abs.updateIntermediate(actualRemission);
                abs.updateIntermediate(tranzaction);
                //Se actualiza el valor esperado en caja si estaba activado
                double transactionTotal = tranzaction.getTransactionPrice();
                if(tranzaction.getCashBoxChecked()!=null){    
                    transactionTotal -= oldInvoicedValue;
                    updateBoxExpectedMoney(user, transactionTotal, false);
                }
                //Se ratifican los cambios de la transacción
                abs.executeCommit();
                getMainController().setMessage(bundle.getString("updateSuccess"), false);
            } else {
                abs.saveIntermediate(actualRemission);
                abs.saveIntermediate(tranzaction);
                actualRemission.getTranzactionCollection().add(tranzaction);
                abs.updateIntermediate(actualRemission);
                //Se actualiza el valor esperado en caja en caso de que se haya especificado así
                if(tranzaction.getCashBoxChecked()!=null)
                    updateBoxExpectedMoney(user, Float.parseFloat(deposit.getText()), false);
                //Se ratifican los cambios de la transacción
                abs.executeCommit();
                mainController.setMessage(bundle.getString("createSuccess"), false);
            }
            mainController.setDataTableView();
            closeModal();
        } catch (Exception e) {
            abs.executeRollback();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Registrar salida", e.getMessage(), sw.toString(), user.toString());
            closeModal();
            mainController.setMessage(bundle.getString("createError"), true);
            //e.printStackTrace();
        }
    }

    /*
     Método por el cual se borra el abono seleccionado
     */
    public void deleteDeposit() {
        try {
            //Remission depositRemission = (Remission) actualDeposits.getSelectionModel().getSelectedItem();
            //TranzactionModel tm = new TranzactionModel();
            if(actualDeposits.getSelectionModel().getSelectedItem()==null)
                actualDeposits.getSelectionModel().selectFirst();
            Deposit actualDeposit = (Deposit) actualDeposits.getSelectionModel().getSelectedItem();
            //Tranzaction actualTranzaction = tm.findByRemission(depositRemission.getIdRemissionPk());
            actualRemission.setLeftAmount(leftAmount + actualDeposit.getDeposit());
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            abs.deleteIntIntermediate("Deposit", actualDeposit.getIdDepositPk());
            abs.updateIntermediate(actualRemission);
            //Se actualiza el valor esperado en caja
            updateBoxExpectedMoney(user, actualDeposit.getDeposit(), false);
            //Se ratifican los cambios de la transacción
            abs.executeCommit();
            mainController.setMessage(bundle.getString("deleteDepositSuccess"), false);
            mainController.setDataTableView();
            closeModal();
        } catch (Exception e) {
            abs.executeRollback();
            mainController.setMessage(bundle.getString("deleteDepositError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Borrar depósito", e.getMessage(), sw.toString(), user.toString());
            closeModal();
            //e.printStackTrace();
        }
    }

    /*
     Método para eliminar el egreso actual
     */
    public void deleteOutput() {
        try {
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            abs.deleteIntIntermediate("Tranzaction", tranzaction.getIdTransactionPk());
            abs.deleteIntIntermediate("Remission", actualRemission.getIdRemissionPk());
            //Se actualiza el valor esperado en caja en caso de que hubiera sido pagado con caja
            if(tranzaction.getCashBoxChecked()!=null)
                updateBoxExpectedMoney(user, Float.parseFloat(deposit.getText()), true);
            //Se ratifican los cambios de la transacción
            abs.executeCommit();
            mainController.setDataTableView();
            mainController.setMessage(bundle.getString("deleteSuccess"), false);
            closeModal();
        } catch (Exception e) {
            abs.executeRollback();
            mainController.setMessage(bundle.getString("deleteError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Borrar salida", e.getMessage(), sw.toString(), user.toString());
            closeModal();
            //e.printStackTrace();
        }
    }
    
    /*
     Método para imprimir el deposito actual en PDF
     @param: evento que ejecuta la acción(onAction)
     */
    @FXML
    public void depositPrint(ActionEvent event) {
        try {
            int tipe = 3;
            PrintPDF pdf = new PrintPDF();
            Deposit actualDeposit;
            if(actualDeposits.getSelectionModel().getSelectedItem()==null)
                actualDeposits.getSelectionModel().selectFirst();
            actualDeposit = (Deposit) actualDeposits.getSelectionModel().getSelectedItem();
            Remission remissionPrint = actualDeposit.getIdRemissionFk();
            remissionPrint.getDepositCollection().clear();
            remissionPrint.addDeposit(actualDeposit);
            pdf.printInvoice(null, remissionPrint, null, null, null, tipe, bundle, true);
            mainController.setMessage(bundle.getString("generateReportSucces"), false);
            closeModal();
        } catch (DocumentException | IOException | HeadlessException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Generar pdf depósito", e.getMessage(), sw.toString(), user.toString());
            closeModal();
            mainController.setMessage(bundle.getString("pdfError"), true);
            e.getMessage();
        } catch (PrinterException pe) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pe.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(pe.hashCode()), "Imprimir pdf depósito", pe.getMessage(), sw.toString(), user.toString());
            closeModal();
            mainController.setMessage(bundle.getString("printerError"), true);
            //pe.getMessage();
        }
    }

    /*
     Método para cargar el reporte de movimientos en PDF
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    public void creditsReport(ActionEvent event) throws DocumentException, IOException {
        try {
            PrintPDF pdf = new PrintPDF();
            String tipe = "personCredits";
            String reportTilte = bundle.getString("creditReportTilte")+" ";
            RemissionModel rm = new RemissionModel();
            Collection<Remission> credits;
            if(creditType == DEPOSIT){
                reportTilte+= actualRemission.getIdClientFk().getTotalName();
                credits = rm.findAllCreditsByClient(actualRemission.getIdClientFk().getIdPersonPk());
            }else{
                reportTilte += actualRemission.getIdSupplierFk().getTotalName();
                credits = rm.findAllCreditsBySupplier(actualRemission.getIdSupplierFk().getIdPersonPk());
            }
            String[] columnNames = {bundle.getString("lblReportMovementId"), bundle.getString("dateInvoiceDatatable"), bundle.getString("lblReportObs"), bundle.getString("lblReportMovementConcept"), bundle.getString("valueInvoiceDatatable")};
            int[] columnWidths = new int[]{10, 10, 50, 20, 10};
            pdf.printReport(tipe, reportTilte, columnNames, columnWidths, credits, null, null, bundle);
        } catch (IOException | HeadlessException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Reportar historial de crédito", e.getMessage(), sw.toString(), user.toString());
            getMainController().setMessage(bundle.getString("pdfError"), true);
        }
        closeModal();
    }

    /*
     Método para cargar el diálogo de confirmación 
     @param: evento que ejecute la acción (onAction)
     */
    public void showConfirmationDialog(ActionEvent event) throws IOException {
        String bundleMessage = "deleteMessage";
        Stage modal = new Stage(StageStyle.DECORATED);
        Locale es = new Locale("es", "ES");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/dialog.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
        Parent rootModal = (Parent) fxmlLoader.load();
        modal.setScene(new Scene(rootModal));
        modal.setTitle(bundle.getString("dialogTitle"));
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(
                ((Node) event.getSource()).getScene().getWindow());
        modal.show();
        DialogController dialogController = fxmlLoader.<DialogController>getController();
        dialogController.setDepositController(this);
        dialogController.initializeValues(bundleMessage, modalType);
    }
    
    /*
        Método para actualizar el dinero esperado en caja, luego de haber realizado un movimiento
        @param: Turno a actualizar: Turn turn, Valor a incrementar o decrementar: Float amount, Bandera que indica si aumenta o disminuye el valor: boolean increment 
     */
    void updateBoxExpectedMoney(Turn turn, double amount, boolean increment) throws Exception {
        if (abs == null) {
            abs = new AbstractFacade();
        } 
        double totalAmount;
        if(increment){
            totalAmount = turn.getExpectedAmount()+amount;
        }else{
            totalAmount = turn.getExpectedAmount()-amount;
        }
        turn.setExpectedAmount(totalAmount);
        abs.updateIntermediate(turn);
    }

    /*
     Método para validar el campo id
     */
    public void validateID(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        if (isNumeric(deposit, vldDeposit, t.getText(), bundle)) {
            isLowerLeftAmount(deposit, vldDeposit, t.getText(), leftAmount, bundle);
        }
        validForm();
    }

    /*
     Método para validar el campo valor
     */
    public void validateVA(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        if (isNumeric(deposit, vldDeposit, t.getText(), bundle)) {
            maxLenght(deposit, vldDeposit, t.getLength(), 23, bundle);
        }
        validForm();
    }

    /*
     Método para validar que el formulario ya esta listo para ser enviado
     */
    void validForm() {
        if (!vldDeposit.isVisible() && !deposit.getText().isEmpty()) {
            saveButton.setDisable(false);
        } else {
            saveButton.setDisable(true);
        }
    }
    
    /*
        Método para ocultar o mostrar la ayuda a través de la acción de un Botón
        @param: Acción que ejecuta la acción: Action onPress
    */
    @FXML
    private void showHideTooltip(ActionEvent event){
        showHideTooltips(tlpHelp, helpButton);
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
        mainController.showHideMask(false);
        Scene scene = mainPane.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

}

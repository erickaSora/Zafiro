/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.zafirodesktop.entity.Concept;
import com.zafirodesktop.entity.Discount;
import com.zafirodesktop.entity.Invoice;
import com.zafirodesktop.entity.Payment;
import com.zafirodesktop.entity.Person;
import com.zafirodesktop.entity.Product;
import com.zafirodesktop.entity.Remission;
import com.zafirodesktop.entity.Settings;
import com.zafirodesktop.entity.Tax;
import com.zafirodesktop.entity.TransactionDetail;
import com.zafirodesktop.entity.TransactionDetailPK;
import com.zafirodesktop.entity.Tranzaction;
import com.zafirodesktop.entity.Turn;
import com.zafirodesktop.entity.Warehouse;
import com.zafirodesktop.model.AbstractFacade;
import com.zafirodesktop.model.RemissionModel;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.model.TransactionDetailModel;
import com.zafirodesktop.model.TranzactionModel;
import com.zafirodesktop.util.ComboBoxChoices;
import com.zafirodesktop.util.FormValidation;
import com.zafirodesktop.util.LogActions;
import com.zafirodesktop.util.PrintPDF;
import com.zafirodesktop.util.ProductConverter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Digitall
 */
public class PaytypeController extends FormValidation implements Initializable {

    /*
     Especificación de los componentes asociados en el FXML
     */
    @FXML
    private AnchorPane mainPane;
    @FXML
    private TabPane checkerPane;
    @FXML
    private GridPane normalPane;
    @FXML
    private HBox entityReference;
    @FXML
    private HBox aprovNumber;
    @FXML
    private TextField receivedCash;
    @FXML
    private TextField getPerson;
    @FXML
    private TextField aprobationNo;
    @FXML
    private TextField aprobationNo2;
    @FXML
    private Label returnCash;
    @FXML
    private Label actualAmount;
    @FXML
    private Label total;
    @FXML
    private Label lblError;
    @FXML
    private Label lblError2;
    @FXML
    private Label lblError3;
    @FXML
    private ComboBox<ComboBoxChoices> type;
    @FXML
    private ComboBox<ComboBoxChoices> payType;
    @FXML
    private ComboBox<ComboBoxChoices> cardReference;
    @FXML
    private Button saveButton;

    /*
     Objetos y variables del controlador 
     */
    private ResourceBundle bundle;
    private MainController mainController;
    private AbstractFacade abs;
    private DecimalFormat format;
    private Person client;
    private Turn user;
    private Discount invoiceDiscount;
    private ObservableList<ProductConverter> selectedProducts;
    private List<ProductConverter> productValuesBftaxes;
    private Collection<ProductConverter> tableviewProductValues;
    private double totalInvoice;
    private String obs;
    private boolean isChecker;
    private boolean isCredit;
    private boolean isCreditCard;

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setClient(Person client) {
        this.client = client;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
    }

    /*
     Método para inicializar los valores del formulario, de acuerdo a si se utiliza o no caja
     */
    public void initializeValues(boolean isCheckBox, double transactionPrice, ObservableList<ProductConverter> products, Person person, Turn user, String observations, List<ProductConverter> valuesBfTaxes, Discount invoiceDiscount) {
        isChecker = isCheckBox;
        format = new DecimalFormat("###,###.00");
        abs = new AbstractFacade();
        totalInvoice = transactionPrice;
        client = person;
        selectedProducts = products;
        this.invoiceDiscount = invoiceDiscount;
        tableviewProductValues = new ArrayList<>();
        for (ProductConverter pr : selectedProducts) {
            tableviewProductValues.add(pr);
        }
        total.setText(bundle.getString("moneyNotation") + format.format(totalInvoice));
        productValuesBftaxes = valuesBfTaxes;
        obs = observations;
        this.user = user;
        if (isChecker) {
            normalPane.setVisible(false);
            checkerPane.setVisible(true);
            returnCash.setText(bundle.getString("moneyNotation") + "0");
            getPerson.setPromptText(bundle.getString("lblInvoiceIdPerson"));
            aprobationNo.setPromptText(bundle.getString("lblAprobationNo"));
            receivedCash.setPromptText(bundle.getString("lblReceivedCash"));
            ObservableList<ComboBoxChoices> typesList = FXCollections.observableArrayList(
                    new ComboBoxChoices("0", "Crédito"),
                    new ComboBoxChoices("1", "Débito")
            );
            type.getItems().addAll(typesList);
            type.getSelectionModel().selectFirst();
            if (client != null) {
                getPerson.setText(client.toString());
                getPerson.setEditable(false);
                RemissionModel rm = new RemissionModel();
                Remission creditRemission = null;
                for (Remission rem : rm.findByIdSell(client.getIdPersonPk().toString())) {
                    creditRemission = rem;
                }
                double leftAmount = 0;
                if (creditRemission != null) {
                    leftAmount = creditRemission.getLeftAmount();
                }
                actualAmount.setText(format.format(leftAmount));
            } else {
                actualAmount.setText(bundle.getString("moneyNotation") + "0");
            }
            //Se activan los eventos para cuando se cambie de tipo de pago
            checkerPane.getSelectionModel().selectedItemProperty().addListener(
                    new ChangeListener<Tab>() {
                        @Override
                        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                            switch (t1.getId()) {
                                case "2":
                                    isCredit = true;
                                    isCreditCard = false;
                                    break;
                                case "3":
                                    isCredit = false;
                                    isCreditCard = true;
                                    break;
                                default:
                                    isCredit = false;
                                    isCreditCard = false;
                                    break;
                            }
                            validForm();
                        }
                    }
            );
            //
        } else {
            saveButton.setDisable(false);
            isChecker = false;
            aprobationNo2.setPromptText(bundle.getString("lblAprobationNo"));
            Collection<Payment> paymentList = abs.findAll("Payment");
            ObservableList<ComboBoxChoices> comboList = FXCollections.observableArrayList();
            for (Payment pay : paymentList) {
                comboList.add(new ComboBoxChoices(pay.getIdPaymentPk().toString(), pay.getPaymentName()));
            }
            payType.getItems().addAll(comboList);
            payType.getSelectionModel().selectFirst();
            ObservableList<ComboBoxChoices> referencesList = FXCollections.observableArrayList(
                    new ComboBoxChoices("0", "Crédito"),
                    new ComboBoxChoices("1", "Débito")
            );
            cardReference.getItems().addAll(referencesList);
            cardReference.getSelectionModel().selectFirst();
        }
        mainPane.setFocusTraversable(true);
        mainPane.setOnKeyPressed(keyListener);
    }

    private EventHandler<KeyEvent> keyListener = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ESCAPE) {
                closeModal();
            } else if (event.getCode() == KeyCode.F10) {
                try {
                    saveInvoice();
                } catch (Exception ex) {
                    Logger.getLogger(PaytypeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

    /*
     Método para guardar la factura
     */
    private void saveInvoice() throws IOException, Exception {
        try {
            saveButton.setDisable(true);
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            PrintPDF pdf = new PrintPDF();
            InvoiceController invoiceController = new InvoiceController();
            ComboBoxChoices typeChoiced = (ComboBoxChoices) payType.getSelectionModel().getSelectedItem();
            Concept concept = (Concept) abs.findByIdInt("Concept", 2);
            Warehouse warehouse = (Warehouse) abs.findByIdInt("Warehouse", 1);
            Remission remission = new Remission();
            Tranzaction tranzaction = new Tranzaction();
            remission.setStatus(new Short("1"));
            remission.setIdConceptFk(concept);
            remission.setIdTurnFk(user);
            tranzaction.setIdWarehouseFk(warehouse);
            tranzaction.setTransactionPrice(totalInvoice);
            Invoice invoice = new Invoice();
            invoice.setStatus(new Short("1"));
            Settings settings = (Settings) abs.findByIdInt("Settings", 1);
            Integer newInvoice = Integer.parseInt(settings.getLastInvoice()) + 1;
            invoice.setIdInvoicePk(newInvoice.toString());
            invoice.setNoteHeader(settings.getNoteHeader());
            invoice.setNoteFooter(settings.getNoteFooter());
            settings.setLastInvoice(newInvoice.toString());
            abs.updateIntermediate(settings);
            //Se especifica el id del tipo de pago para cada caso
            int idPayment;
            if (isChecker) {
                if (isCredit) {
                    idPayment = 2;
                } else if (isCreditCard) {
                    idPayment = 3;
                } else {
                    idPayment = 1;
                }
            } else {
                idPayment = Integer.valueOf(typeChoiced.getItemValue());
            }
            Payment payment = (Payment) abs.findByIdInt("Payment", idPayment);
            invoice.setIdPaymentFk(payment);
            invoice.setIdDiscountFk(invoiceDiscount);
            invoice.setObs(obs);
            if (isCreditCard) {
                ComboBoxChoices selectedCard;
                String cardAprobationNo;
                if (isChecker) {
                    selectedCard = type.getSelectionModel().getSelectedItem();
                    cardAprobationNo = aprobationNo.getText();
                } else {
                    selectedCard = cardReference.getSelectionModel().getSelectedItem();
                    cardAprobationNo = aprobationNo2.getText();
                }
                invoice.setCardType(selectedCard.getItemLabel());
                invoice.setNoReference(cardAprobationNo);
            } else {
                invoice.setNoReference("");
                invoice.setCardType("");
            }
            abs.saveIntermediate(invoice);
            remission.setInvoiced(new Short("1"));
            remission.setIdClientFk(client);
            remission.setIdInvoiceFk(invoice);
            if (isCredit) {
                Remission creditRemission = new Remission();
                RemissionModel rm = new RemissionModel();
                double actualLeftAmount = 0;
                boolean exists = false;
                if (!rm.findByIdSell(client.getIdPersonPk().toString()).isEmpty()) {
                    for (Remission rem : rm.findByIdSell(client.getIdPersonPk().toString())) {
                        creditRemission = rem;
                    }
                    actualLeftAmount = creditRemission.getLeftAmount();
                } else {
                    Concept creditConcept = (Concept) abs.findByIdInt("Concept", 3);
                    creditRemission.setIdClientFk(client);
                    creditRemission.setIdConceptFk(creditConcept);
                    creditRemission.setStatus(new Short("1"));
                    creditRemission.setNoBuyReference(client.getIdPersonPk().toString());
                }
                remission.setDeposit(0.0);
                actualLeftAmount += totalInvoice;
                creditRemission.setLeftAmount(actualLeftAmount);
                rm.saveIntermediate(creditRemission);
            }
            abs.saveIntermediate(remission);
            tranzaction.setIdRemissionFk(remission);
            remission.addTrazaction(tranzaction);
            TranzactionModel tm = new TranzactionModel();
            tm.saveTranzaction(tranzaction, remission);
            if (!selectedProducts.isEmpty()) {
                TransactionDetailModel tdm = new TransactionDetailModel();
                TransactionDetail tansactionDetail;
                for (ProductConverter sldPrd : selectedProducts) {
                    Product product1 = (Product) abs.findByIdInt("Product", sldPrd.getIdProductPk());
                    TransactionDetailPK tPK = new TransactionDetailPK(product1.getIdProductPk(), tranzaction.getIdTransactionPk());
                    tansactionDetail = new TransactionDetail(tPK);
                    tansactionDetail.setProduct(product1);
                    tansactionDetail.setTranzaction(tranzaction);
                    tansactionDetail.setAmount(sldPrd.getAmount());
                    tansactionDetail.setUnitPrice(sldPrd.getActualPrice());
                    tansactionDetail.setDescription(sldPrd.getProductDescription());
                    //Se guardan los impuestos asociados si los tiene
                    if (!sldPrd.getTaxesCollection().isEmpty()) {
                        String allAsociatedTaxes = "";
                        for (Tax tax : sldPrd.getTaxesCollection()) {
                            allAsociatedTaxes += tax.getIdTaxPk() + ";" + tax.getTaxName() + ";" + tax.getTaxPct() + ";;";
                        }
                        tansactionDetail.setTaxes(allAsociatedTaxes);
                    }
                    //Se guardan los impuestos asociados si los tiene
                    if (!sldPrd.getDiscountCollection().isEmpty()) {
                        String allAsociatedDiscounts = "";
                        for (Discount discount : sldPrd.getDiscountCollection()) {
                            allAsociatedDiscounts += discount.getIdDiscountPk()+ ";" + discount.getDiscountDescrption() + ";" + discount.getDiscountPct() + ";;";
                        }
                        tansactionDetail.setDiscounts(allAsociatedDiscounts);
                    }
                    tranzaction.addTrazactionDetail(tansactionDetail);
                    tdm.saveTransactionDetail(tansactionDetail, tranzaction);
                    //abs.save(tansactionDetail);
                    invoiceController.updateStock(sldPrd.getAmount(), product1, false);
                }
            }
            //Se actualiza el valor esperado en caja
            if (!isCredit && !isCreditCard) {
                updateBoxExpectedMoney(user, totalInvoice, true);
            }
            //Se ratifican los cambios de la transacción
            abs.executeCommit();
            try {
                pdf.printInvoice(tranzaction, null, null, selectedProducts, productValuesBftaxes, 2, bundle, true);
                getMainController().reinitializeFormValues();
                getMainController().setInvoiceDataTableView();
                getMainController().setModuleID("credit");
                getMainController().setDataTableView();
                getMainController().setMessage(bundle.getString("createSuccess"), false);
                //se comprueba si es cajero para mantener el especificado de devoluciòn en caja
                if(isChecker && !isCredit && !isCreditCard){
                    checkerPane.setDisable(true);
                }else
                    closeModal();
            } catch (Exception e) {
                getMainController().reinitializeFormValues();
                getMainController().setInvoiceDataTableView();
                getMainController().setModuleID("credit");
                getMainController().setDataTableView();
                getMainController().setMessage(bundle.getString("createPrintError"), true);
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                LogActions log = new LogActions();
                log.createEntry(String.valueOf(e.hashCode()), "Imprimir factura vista cajero", e.getMessage(), sw.toString(), user.toString());
                closeModal();
            }
        } catch (Exception e) {
            abs.executeRollback();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar factura desde vista cajero", e.getMessage(), sw.toString(), user.toString());
            getMainController().setMessage(bundle.getString("createError"), true);
            //updateProductOriginalVales();
            closeModal();
            //e.printStackTrace();
        }
        mainController.checkMinimumStock(tableviewProductValues);
    }

    /*
     Método para ejecutar el guardado de la Facurta al presiona el botón
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void executeSaveInvoice(ActionEvent event) throws IOException, Exception {
        saveInvoice();
    }

    /*
     Método para cambiar los valores de acuerdo al tipo de pago seleccionado
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void updatePaymentType(ActionEvent event) {
        ComboBoxChoices selectedPayment = payType.getSelectionModel().getSelectedItem();
        switch (selectedPayment.getItemValue()) {
            case "2":
                isCredit = true;
                isCreditCard = false;
                if (entityReference.isVisible()) {
                    entityReference.setVisible(false);
                    cardReference.setVisible(false);
                    aprovNumber.setVisible(false);
                    aprobationNo2.setVisible(false);
                }
                if(lblError3.isVisible())
                    lblError3.setVisible(false);
                break;
            case "3":
                isCredit = false;
                isCreditCard = true;
                entityReference.setVisible(true);
                cardReference.setVisible(true);
                aprovNumber.setVisible(true);
                aprobationNo2.setVisible(true);
                break;
            default:
                isCredit = false;
                isCreditCard = false;
                if (entityReference.isVisible()) {
                    entityReference.setVisible(false);
                    cardReference.setVisible(false);
                    aprovNumber.setVisible(false);
                    aprobationNo2.setVisible(false);
                }
                if(lblError3.isVisible())
                    lblError3.setVisible(false);
                break;
        }
        validForm();
    }

    /*public void updateProductOriginalVales() throws Exception {
        for (Product prod : actualProductValues) {
            for (Product pro : tableviewProductValues) {
                if (prod.getIdProductPk().compareTo(pro.getIdProductPk()) == 0) {
                    pro.setActualPrice(prod.getActualPrice());
                    pro.setCantidadSeleccionada(1);
                    pro.setProductDescription(prod.getProductDescription());
                    abs.updateFinal(pro);
                }
            }
        }
    }

    
     Método para actualizar un producto a sus valores originales, luego de 
     ser removido de un movimiento que se está actualizando. 
    
    public void resetProductOriginalVales(Product pro) throws Exception {
        for (Product product : tableviewProductValues) {
            if (product.getIdProductPk().compareTo(pro.getIdProductPk()) == 0) {
                pro.setActualPrice(product.getActualPrice());
                pro.setCantidadSeleccionada(1);
                pro.setProductDescription(product.getProductDescription());
                abs.updateIntermediate(pro);
            }
        }
    }*/

    /*
     Método para actualizar el dinero esperado en caja, luego de haber realizado un movimiento
     @param: Turno a actualizar: Turn turn, Valor a incrementar o decrementar: Float amount, Bandera que indica si aumenta o disminuye el valor: boolean increment 
     */
    void updateBoxExpectedMoney(Turn turn, double amount, boolean increment) throws Exception {
        if (abs == null) {
            abs = new AbstractFacade();
        }
        double totalAmount;
        if (increment) {
            totalAmount = turn.getExpectedAmount() + amount;
        } else {
            totalAmount = turn.getExpectedAmount() - amount;
        }
        turn.setExpectedAmount(totalAmount);
        abs.updateIntermediate(turn);
    }

    /*
     Método para actualizar el valor a retornar de la caja
     */
    private void setReturnCash() {
        double receivedAmount = Float.parseFloat(receivedCash.getText());
        double difference = receivedAmount - totalInvoice;
        returnCash.setText(bundle.getString("moneyNotation") + " " + format.format(difference));
        if (difference >= 0) {
            if (returnCash.getStyleClass().contains("lblValidateError")) {
                returnCash.getStyleClass().remove("lblValidateError");
            }
            if (!returnCash.getStyleClass().contains("lblValidateSucces")) {
                returnCash.getStyleClass().add("lblValidateSucces");
            }
        } else {
            if (returnCash.getStyleClass().contains("lblValidateSucces")) {
                returnCash.getStyleClass().remove("lblValidateSucces");
            }
            if (!returnCash.getStyleClass().contains("lblValidateError")) {
                returnCash.getStyleClass().add("lblValidateError");
            }
        }

    }

    /*
     Método para validar el campo receivedCash
     */
    public void validateRC(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        String text = t.getText();
        if (notNull(receivedCash, lblError, length, bundle, bundle.getString("notNull"))) {
            if (isNumeric(receivedCash, lblError, text, bundle)) {
                maxLenght(receivedCash, lblError, length, 23, bundle);
            }
        }
        validForm();
    }

    /*
     Método para validar el campo aprobationNumber
     */
    public void validateAN(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        if (notNull(aprobationNo, lblError2, length, bundle, bundle.getString("notNull"))) {
            maxLenght(aprobationNo, lblError2, length, 50, bundle);
        }
        validForm();
    }

    /*
     Método para validar el campo aprobationNumber2
     */
    public void validateAN2(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        if (notNull(aprobationNo2, lblError3, length, bundle, bundle.getString("notNull"))) {
            maxLenght(aprobationNo2, lblError3, length, 50, bundle);
        }
        validForm();
    }

    /*
     Método para validar que el formulario ya esta listo para ser enviado
     */
    void validForm() {
        //Se indica el método de validación dependiendo de si es o no cajero
        if (isChecker) {
            //Se especifican las funciones a validar de acuerdo al tipo de pago seleccionado
            if (isCredit) {
                if (getPerson.getText().isEmpty()) {
                    saveButton.setDisable(true);
                } else {
                    saveButton.setDisable(false);
                }
            } else if (isCreditCard) {
                if (!lblError2.isVisible() && !aprobationNo.getText().isEmpty()) {
                    saveButton.setDisable(false);
                } else {
                    saveButton.setDisable(true);
                }
            } else {
                if (!lblError.isVisible() && !receivedCash.getText().isEmpty()) {
                    saveButton.setDisable(false);
                    setReturnCash();
                } else {
                    saveButton.setDisable(true);
                }
            }
        } else {
            if (isCreditCard) {
                if (!lblError3.isVisible() && !aprobationNo2.getText().isEmpty()) {
                    saveButton.setDisable(false);
                } else {
                    saveButton.setDisable(true);
                }
            } else {
                saveButton.setDisable(false);
            }
        }
    }

    /*
     Método para cerrar el modal iniciado sin guardar ninguna cambio, desde el boto cancelar
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void closeButtonAction(ActionEvent event
    ) {
        closeModal();
    }

    private void closeModal() {
        mainController.showHideMask(false);
        Scene scene = mainPane.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

}

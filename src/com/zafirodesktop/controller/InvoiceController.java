/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.itextpdf.text.DocumentException;
import com.zafirodesktop.entity.Annotation;
import com.zafirodesktop.entity.Concept;
import com.zafirodesktop.entity.Discount;
import com.zafirodesktop.entity.Invoice;
import com.zafirodesktop.entity.Payment;
import com.zafirodesktop.entity.Person;
import com.zafirodesktop.entity.Place;
import com.zafirodesktop.entity.Product;
import com.zafirodesktop.entity.ProductCategory;
import com.zafirodesktop.entity.ProductDiscount;
import com.zafirodesktop.entity.ProductTaxes;
import com.zafirodesktop.entity.Quotation;
import com.zafirodesktop.entity.QuotationDetail;
import com.zafirodesktop.entity.QuotationDetailPK;
import com.zafirodesktop.entity.Remission;
import com.zafirodesktop.entity.Settings;
import com.zafirodesktop.entity.Stock;
import com.zafirodesktop.entity.Tax;
import com.zafirodesktop.entity.TransactionDetail;
import com.zafirodesktop.entity.TransactionDetailPK;
import com.zafirodesktop.entity.Tranzaction;
import com.zafirodesktop.entity.Turn;
import com.zafirodesktop.entity.Warehouse;
import com.zafirodesktop.model.AbstractFacade;
import com.zafirodesktop.model.ConceptModel;
import com.zafirodesktop.model.ProductModel;
import com.zafirodesktop.model.RemissionModel;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.model.TransactionDetailModel;
import com.zafirodesktop.model.TranzactionModel;
import com.zafirodesktop.util.ComboBoxChoices;
import com.zafirodesktop.util.EditingCell;
import com.zafirodesktop.util.EditingCellFloat;
import com.zafirodesktop.util.FormValidation;
import com.zafirodesktop.util.LogActions;
import com.zafirodesktop.util.PrintPDF;
import com.zafirodesktop.util.ProductConverter;
import java.awt.HeadlessException;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 *
 * @author Digitall
 */
public class InvoiceController extends FormValidation implements Initializable {

    /*
     Especificación de los componentes asociados en el FXML
     */
    @FXML
    private AnchorPane mainPane;
    @FXML
    private HBox buttonsBox;
    @FXML
    private HBox pdtbButtonsBox;
    @FXML
    private HBox getProductBox;
    @FXML
    private HBox annotationsBox;
    @FXML
    private HBox entityReference;
    @FXML
    private HBox aprovNumber;
    @FXML
    private VBox remissionProductBox;
    @FXML
    private GridPane lblTotalsGrid;
    @FXML
    private Label title;
    @FXML
    private Label invNumber;
    @FXML
    private Label invDate;
    @FXML
    private Label vldMessage;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblTotalTitle;
    @FXML
    private Label lblSubTotal;
    @FXML
    private Label lblSubTotalTitle;
    @FXML
    private Label vldInfo;
    @FXML
    private TextField idPerson;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField phone;
    @FXML
    private TextField addres;
    @FXML
    private TextField city;
    @FXML
    private TextField getProduct;
    @FXML
    private TextArea serviceReference;
    @FXML
    private TextField obs;
    @FXML
    private TextField email;
    @FXML
    private TextField aprobationNo;
    @FXML
    private ComboBox<ComboBoxChoices> payType;
    @FXML
    private ComboBox<ComboBoxChoices> discountOpt;
    @FXML
    private ComboBox<ComboBoxChoices> conceptType;
    @FXML
    private ComboBox<ComboBoxChoices> cardReference;
    @FXML
    private CheckBox orderPayment;
    @FXML
    private TableView productDataTable;
    @FXML
    private TableView annotationDataTable;
    @FXML
    private TableColumn tcIdProduct;
    @FXML
    private TableColumn tcProductDescription;
    @FXML
    private TableColumn tcProductPrice;
    @FXML
    private TableColumn tcProductTotalPrice;
    @FXML
    private TableColumn tcProductTax;
    @FXML
    private TableColumn tcAmount;
    @FXML
    private TableColumn tcAnnotationDate;
    @FXML
    private TableColumn tcAnnotationObs;
    @FXML
    private ListView personList;
    @FXML
    private ListView productList;
    @FXML
    private ListView placesList;
    @FXML
    private Button addProductButton;
    @FXML
    private Button addAnnotationButton;
    @FXML
    private Button rmvProductButton;
    @FXML
    private Button rmvAnnotationButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button stateButton;
    @FXML
    private Button printButton;
    @FXML
    private Button pdfButton;
    @FXML
    private Button invoiceButton;
    @FXML
    private Button helpButton;
    @FXML
    private Tooltip tlpHelp;

    /*
     Objetos y variables del controlador 
     */
    static final int INVOICE = 2;
    static final int ORDER = 1;
    static final int MOVEMENT = 4;
    static final int ADD_INVENTORY = 5;
    static final int SERVICE_ORDER = 0;
    static final int QUOTATION = 20;
    static final int INITIALIZE_CREDIT = 21;
    static final int INITIALIZE_CREDIT_ORDER = 22;
    private String personType;
    DecimalFormat format;
    private int type;
    private int movementType;
    private double totalInvoice;
    private double oldInvoiceTotal;
    private double subTotalInvoice;
    private double totalNoDiscount;
    private double oldValueChecked;
    private boolean personExists;
    private boolean invoiceExists;
    private boolean isCredit;
    private boolean isCreditCard;
    private boolean invoiceRemission;
    private boolean quotationRemission;
    private ObservableList<ProductConverter> selectedProducts;
    private Collection<Tax> taxes;
    private Collection<Discount> discounts;
    private AbstractFacade abs;
    private Turn user;
    private Invoice invoice;
    private Concept concept;
    private Person person;
    private Place initPlace;
    private Discount invoiceDiscount;
    private Product toAssociateProduct;
    private Collection<TransactionDetail> oldProducts;
    private Collection<QuotationDetail> quotationOldProducts;
    //private List<Product> actualProductValues;
    private List<ProductConverter> productValuesBfTaxes;
    private Remission remission;
    private Remission creditRemission;
    private Remission initializeCreditRem;
    private Tranzaction tranzaction;
    private TransactionDetail tansactionDetail;
    private Warehouse warehouse;
    private TransactionDetailModel dtm;
    private Quotation quotation;
    private MainController mainController;
    private PrintPDF pdf;
    private ResourceBundle bundle;

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        Image img = new Image("/com/zafirodesktop/ui/img/ico/pointer_black.png");
        ImageView imgvw = new ImageView(img);
        imgvw.setFitHeight(15);
        imgvw.setFitWidth(15);
        tlpHelp.setGraphic(imgvw);
    }

    /*
     Método para inicializar los valores del formulario
     */
    public void initializeCredit(int type, Turn sessionUser) {
        this.type = type;
        user = sessionUser;
        idPerson.setPromptText(bundle.getString("lblInvoiceIdPerson"));
        firstName.setPromptText(bundle.getString("lblInvoiceFirstName"));
        lastName.setPromptText(bundle.getString("lblInvoiceLastName"));
        addres.setPromptText(bundle.getString("lblInvoiceAddres"));
        phone.setPromptText(bundle.getString("lblInvoicePhone"));
        city.setPromptText(bundle.getString("lblInvoiceCity"));
        getProduct.setPromptText(bundle.getString("initializeCreditValue"));
        abs = new AbstractFacade();
        initPlace = (Place) abs.findByIdInt("Place", 15001);
        city.setText(initPlace.getPlaceName() + " " + initPlace.getIdPlaceFk().getPlaceName());
    }

    /*
     Método para inicializar los valores del formulario
     */
    public void initializeValues(int invoiceType, Turn sessionUser) {
        user = sessionUser;
        type = invoiceType;
        abs = new AbstractFacade();
        if (type != ORDER && type != MOVEMENT) {
            getProduct.setPromptText(bundle.getString("lblInvoiceGetProductService"));
        } else {
            getProduct.setPromptText(bundle.getString("lblInvoiceGetProduct"));
        }
        if (type != ADD_INVENTORY) {
            idPerson.setPromptText(bundle.getString("lblInvoiceIdPerson"));
            firstName.setPromptText(bundle.getString("lblInvoiceFirstName"));
            lastName.setPromptText(bundle.getString("lblInvoiceLastName"));
            addres.setPromptText(bundle.getString("lblInvoiceAddres"));
            phone.setPromptText(bundle.getString("lblInvoicePhone"));
            city.setPromptText(bundle.getString("lblInvoiceCity"));
            email.setPromptText(bundle.getString("lblPersonEmail"));
        }
        if (type != SERVICE_ORDER) {
            obs.setPromptText(bundle.getString("lblInvoiceObs"));
        }
        if(type == ORDER && !mainController.isUseCashBox()){
            orderPayment.setVisible(false);
        }
        selectedProducts = FXCollections.observableArrayList();
        //actualProductValues = new ArrayList();
        productValuesBfTaxes = new ArrayList();
        taxes = new ArrayList();
        discounts = new ArrayList();

        if (type == INVOICE) {
            title.setText(bundle.getString("invoiceTitle"));
        } else if (type == ORDER) {
            title.setText(bundle.getString("orderTitle"));
        } else if (type == QUOTATION) {
            title.setText(bundle.getString("quotationTitle"));
        } else if (type == ADD_INVENTORY) {
            title.setText(bundle.getString("addInventoryTitle"));
        } else if (type == SERVICE_ORDER) {
            title.setText(bundle.getString("serviceOrderTitle"));
            annotationDataTable.setPlaceholder(new Label(bundle.getString("lblServiceOrderAnnNoItems")));
            serviceReference.setPromptText(bundle.getString("lblserviceOrderRef"));
            annotationsBox.setVisible(false);
            remissionProductBox.getChildren().remove(getProductBox);
            remissionProductBox.getChildren().remove(productDataTable);
            rmvProductButton.setVisible(false);
        } else {
            personType = "S";
            title.setText(bundle.getString("movementTitle"));
        }
        totalInvoice = new Float("0");
        format = new DecimalFormat("###,###.00");

        productDataTable.setPlaceholder(new Label(bundle.getString("lblInvoiceTableNoItems")));
        productDataTable.setEditable(
                true);
        //Callback para la edición del campo cantidad 
        Callback<TableColumn, TableCell> cellFactory
                = new Callback<TableColumn, TableCell>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCell();
                    }
                };
        //Callback para la edición del campo precio
        Callback<TableColumn, TableCell> cellFactoryFloat
                = new Callback<TableColumn, TableCell>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new EditingCellFloat();
                    }
                };
        tcIdProduct = new TableColumn(bundle.getString("productIdInvoiceDatatable"));

        tcIdProduct.prefWidthProperty()
                .bind((productDataTable.widthProperty().divide(7.05)));
        tcIdProduct.setCellValueFactory(
                new PropertyValueFactory<ProductConverter, String>("skuProduct"));
        tcProductDescription = new TableColumn(bundle.getString("productDescriptionInvoiceDatatable"));

        tcProductDescription.prefWidthProperty()
                .bind(productDataTable.widthProperty().divide(2.1));
        tcProductDescription.setCellValueFactory(
                new PropertyValueFactory<ProductConverter, String>("productDescription"));
        tcProductDescription.setCellFactory(TextFieldTableCell.forTableColumn());
        tcProductDescription.setOnEditCommit(
                new EventHandler<CellEditEvent<ProductConverter, String>>() {
                    @Override
                    public void handle(CellEditEvent<ProductConverter, String> t) {
                        /*Product temp = (Product) t.getTableView().getItems().get(
                         t.getTablePosition().getRow());
                         temp.setProductDescription(t.getNewValue());*/
                        ((ProductConverter) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setProductDescription(t.getNewValue());
                        refreshProductDataTable();
                    }
                }
        );

        tcProductPrice = new TableColumn(bundle.getString("productPriceInvoiceDatatable"));

        tcProductPrice.prefWidthProperty()
                .bind(productDataTable.widthProperty().divide(8));
        tcProductPrice.setCellValueFactory(
                new PropertyValueFactory<ProductConverter, Integer>("actualPrice"));
        tcProductPrice.setCellFactory(cellFactoryFloat);

        tcProductPrice.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<ProductConverter, Double>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<ProductConverter, Double> t
                    ) {
                        ProductConverter temp = (ProductConverter) t.getTableView().getItems().get(
                                t.getTablePosition().getRow());
                        double newValue = t.getNewValue();
                        //Se trae el producto con los valores reales
                        ProductConverter toUpdate = new ProductConverter();
                        for(ProductConverter cnvrt:productValuesBfTaxes){
                            if(temp.getIdProductPk().equals(cnvrt.getIdProductPk()))
                                toUpdate = cnvrt;
                        }
                        //Si es factura o cotización, para cargar los valores reales por si tiene descuentos
                        if (type == INVOICE || type == QUOTATION) {
                            int index = productValuesBfTaxes.indexOf(toUpdate);
                            productValuesBfTaxes.get(index).setActualPrice(newValue);
                        }
                        /// Si es factura o compra y si tiene impuestos
                        if (type == INVOICE || type == QUOTATION || type == SERVICE_ORDER) {
                            Collection<Tax> pTaxes = temp.getTaxesCollection();
                            float pcts = 0;
                            if (!pTaxes.isEmpty()) {
                                ///Se actualiza el valor antes de impuestos del producto
                                productValuesBfTaxes.remove(toUpdate);
                                ProductConverter p = new ProductConverter(temp.getIdProductPk(), newValue, temp.getProductDescription());
                                productValuesBfTaxes.add(p);
                                //
                                for (Tax newTax : pTaxes) {
                                    pcts = pcts + newTax.getTaxPct();
                                    if (!taxes.contains(newTax)) {
                                        taxes.add(newTax);
                                    }
                                }
                                newValue = newValue / ((pcts / 100) + 1);
                                //Se redondea a 2 cifras el valor escrito
                                newValue = (double)Math.round(newValue*100)/100;
                            }
                        }
                        //
                        temp.setActualPrice(newValue);
                        temp.setTotalPrice(format.format(newValue*temp.getAmount()));
                        if (type != SERVICE_ORDER && type != ADD_INVENTORY) {
                            updatePriceValues();
                        }
                        refreshProductDataTable();
                        validForm();
                    }
                }
        );
        tcAmount = new TableColumn(bundle.getString("productAmountInvoiceDatatable"));

        tcAmount.prefWidthProperty()
                .bind(productDataTable.widthProperty().divide(8));
        tcAmount.setCellValueFactory(
                new PropertyValueFactory<ProductConverter, Integer>("amount"));
        tcAmount.setCellFactory(cellFactory);

        tcAmount.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<ProductConverter, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<ProductConverter, Integer> t
                    ) {
                        ProductConverter temp = (ProductConverter) t.getTableView().getItems().get(
                                t.getTablePosition().getRow());
                        int newValue = t.getNewValue();
                        temp.setAmount(newValue);
                        temp.setTotalPrice(format.format(newValue*temp.getActualPrice()));
                        if (type != SERVICE_ORDER && type != ADD_INVENTORY) {
                            updatePriceValues();
                        }
                        refreshProductDataTable();
                        validForm();
                    }
                }
        );
        
        tcProductTotalPrice = new TableColumn(bundle.getString("productTotalPriceInvoiceDatatable"));

        tcProductTotalPrice.prefWidthProperty()
                .bind(productDataTable.widthProperty().divide(8));
        tcProductTotalPrice.setCellValueFactory(
                new PropertyValueFactory<ProductConverter, String>("totalPrice"));
        
        //Se agrega la columna de impuesto en el caso que sea una compra
        if(type == ORDER){
            ObservableList<ComboBoxChoices> comboTaxList = FXCollections.observableArrayList();
            comboTaxList.add(new ComboBoxChoices("-1", bundle.getString("lblInvoiceNotax")));
            Collection<Tax> comboTaxesList = abs.findAll("Tax");
            for (Tax tx : comboTaxesList) {
                comboTaxList.add(new ComboBoxChoices(tx.getIdTaxPk().toString(), tx.toString()));
            }
        
            tcProductTax = new TableColumn(bundle.getString("productTaxInvoiceDatatable"));
        
            tcProductTax.setCellValueFactory(
                new PropertyValueFactory<ProductConverter, String>("taxSelected")
            );
        
            tcProductTax.setCellFactory(ComboBoxTableCell.forTableColumn(comboTaxList));
            tcProductTax.setOnEditCommit(
               new EventHandler<CellEditEvent<ProductConverter, ComboBoxChoices>>() {
                    @Override
                    public void handle(CellEditEvent<ProductConverter, ComboBoxChoices> t) {
                        List<ProductConverter> tempList = productDataTable.getItems();
                        ProductConverter temp = (ProductConverter) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow());
                        temp.getTaxesCollection().clear();
                        //Se comprueba que existiese un valor antiguo para capturarlo
                        if(!temp.getByDefault().getItemValue().equals("-1")){
                            Tax oldTax = (Tax)abs.findByIdInt("Tax", Integer.parseInt(temp.getByDefault().getItemValue()));
                            int i=0;
                            for(ProductConverter actual:tempList){
                                if(actual.getTaxesCollection().contains(oldTax)){
                                    i++;
                                }
                            }
                            if(i==0 && taxes.contains(oldTax)){
                                taxes.remove(oldTax);
                            }
                        }
                        ComboBoxChoices newChoice = new ComboBoxChoices(t.getNewValue().getItemValue(), t.getNewValue().getItemLabel());
                        temp.setTaxSelected(t.getNewValue().getItemLabel());
                        temp.setByDefault(newChoice);
                        //Se carga el nuevo impuesto en caso de que exista
                        if(!newChoice.getItemValue().equals("-1")){
                            Tax actualTax = (Tax)abs.findByIdInt("Tax", Integer.parseInt(newChoice.getItemValue()));
                            temp.getTaxesCollection().add(actualTax);
                            if(!taxes.contains(actualTax)){
                                taxes.add(actualTax);
                            }
                        }
                        updatePriceValues();
                    };
                }
            );
            tcIdProduct.prefWidthProperty()
                .bind((productDataTable.widthProperty().divide(8)));
            tcAmount.prefWidthProperty()
                .bind(productDataTable.widthProperty().divide(16));
            productDataTable.getColumns()
                .addAll(tcIdProduct, tcProductDescription, tcProductTax, tcProductPrice, tcAmount, tcProductTotalPrice);
        }else{
            productDataTable.getColumns()
                .addAll(tcIdProduct, tcProductDescription, tcProductPrice, tcAmount, tcProductTotalPrice);
        }
        
        
        rmvProductButton.setDisable(
                true);
        //SessionBD.persistenceCreate();
        //abs = new AbstractFacade();
        if (type != ADD_INVENTORY) {
            initPlace = (Place) abs.findByIdInt("Place", 15001);
            city.setText(initPlace.getPlaceName() + " " + initPlace.getIdPlaceFk().getPlaceName());
        }
        if (type != MOVEMENT && type != SERVICE_ORDER && type != ADD_INVENTORY && type != QUOTATION) {
            aprobationNo.setPromptText(bundle.getString("lblAprobationNo"));
            //Carga de tipos de pago de acuerdo al movimiento actual
            ObservableList<ComboBoxChoices> comboList = FXCollections.observableArrayList();
            if(type==INVOICE){
                Collection<Payment> paymentList = abs.findAll("Payment");
                for (Payment pay : paymentList) {
                    comboList.add(new ComboBoxChoices(pay.getIdPaymentPk().toString(), pay.getPaymentName()));
                }
            }else{
                comboList.add(new ComboBoxChoices("0", "De contado"));
                comboList.add(new ComboBoxChoices("1", "A crédito"));
            }
            payType.getItems().addAll(comboList);
            payType.getSelectionModel().selectFirst();
            ObservableList<ComboBoxChoices> referencesList = FXCollections.observableArrayList(
                    new ComboBoxChoices("0", "Crédito"),
                    new ComboBoxChoices("1", "Débito")
            );
            cardReference.getItems().addAll(referencesList);
            cardReference.getSelectionModel().selectFirst();
            //Carga de descuentos disponibles
            Collection<Discount> discountList = abs.findAll("Discount");
            ObservableList<ComboBoxChoices> comboDiscList = FXCollections.observableArrayList();
            comboDiscList.add(new ComboBoxChoices("-1", bundle.getString("lblInvoiceNoDiscount")));
            for (Discount dis : discountList) {
                comboDiscList.add(new ComboBoxChoices(dis.getIdDiscountPk().toString(), dis.toString()));
            }
            discountOpt.getItems().addAll(comboDiscList);
            discountOpt.getSelectionModel().selectFirst();

        } else if (type == MOVEMENT) {
            ConceptModel conm = new ConceptModel();
            Collection<Concept> movementTypeList = conm.findAllByType("M");
            ObservableList<ComboBoxChoices> comboList = FXCollections.observableArrayList();
            for (Concept con : movementTypeList) {
                comboList.add(new ComboBoxChoices(con.getIdConceptPk().toString(), con.getConceptName()));
            }
            conceptType.getItems().addAll(comboList);
            conceptType.getSelectionModel().selectFirst();
        }

        saveButton.setDisable(
                true);
        
        buttonsBox.getChildren()
                .remove(deleteButton);
        buttonsBox.getChildren()
                .remove(printButton);
        buttonsBox.getChildren()
                .remove(pdfButton);
        if (type == INVOICE || type == ORDER) {
            lblTotalsGrid.getChildren().clear();
        } else if (type == MOVEMENT) {
            buttonsBox.getChildren().remove(stateButton);
        } else {
            buttonsBox.getChildren().remove(invoiceButton);
        }
    }

    /*
     Método para inicializar los valores del formulario para actualización
     */
    public void initializeValuesExists(Remission remi, Collection<ProductConverter> productList, Quotation quot) {
        remission = remi;
        personExists = true;
        invoiceExists = true;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        if (type == INVOICE) {
            //Se comprueba que no corresponda a una nueva factura de cotización
            if (remission.getIdInvoiceFk() != null) {
                invNumber.setText(bundle.getString("lblIvoiceNumber") + remission.getIdInvoiceFk().getIdInvoicePk());
                invDate.setText(bundle.getString("lblInvoiceDate") + df.format(remission.getIdInvoiceFk().getInvoiceDate()));
            }
            person = remission.getIdClientFk();
            if (remission.getIdConceptFk() != null) {
                title.setText(bundle.getString("invoiceUpdateTitle"));
                invoice = remission.getIdInvoiceFk();
                ComboBoxChoices selectedPayType = new ComboBoxChoices(remi.getIdInvoiceFk().getIdPaymentFk().getIdPaymentPk().toString(), remi.getIdInvoiceFk().getIdPaymentFk().getPaymentName());
                payType.getSelectionModel().select(selectedPayType);
                ComboBoxChoices selectedDiscountOpt;
                if (remi.getIdInvoiceFk().getIdDiscountFk() != null) {
                    selectedDiscountOpt = new ComboBoxChoices(remi.getIdInvoiceFk().getIdDiscountFk().getIdDiscountPk().toString(), remi.getIdInvoiceFk().getIdDiscountFk().toString());
                } else {
                    selectedDiscountOpt = new ComboBoxChoices("-1", bundle.getString("lblInvoiceNoDiscount"));
                }
                discountOpt.getSelectionModel().select(selectedDiscountOpt);
                buttonsBox.getChildren().add(1, printButton);
                buttonsBox.getChildren().add(2, pdfButton);
                if (user.getIdUserFk().getCreatedOn() > 0 && remission.getStatus() > 0) {
                    buttonsBox.getChildren().add(3, deleteButton);
                    rmvProductButton.setDisable(false);
                }
                //Se deshabilitan las funciones de edición de factura para que máximo se pueda anular
                //else {
                buttonsBox.getChildren().remove(saveButton);
                idPerson.setEditable(false);
                firstName.setEditable(false);
                lastName.setEditable(false);
                phone.setEditable(false);
                addres.setEditable(false);
                email.setEditable(false);
                city.setEditable(false);
                getProduct.setDisable(true);
                //obs.setEditable(false);
                rmvProductButton.setDisable(true);
                productDataTable.setEditable(false);
                //}
            } else {
                if (remission.getObs() != null) {
                    title.setText(bundle.getString("invoiceTitle"));
                    selectedProducts = FXCollections.observableArrayList();
                    oldProducts = new ArrayList<>();
                    invoiceRemission = true;
                } else {
                    title.setText(bundle.getString("invoiceTitle"));
                    invoiceExists = false;
                    quotation = quot;
                }
            }
        } else if (type == ORDER) {
            //Se carga la parte de información de tipo de pago
            ComboBoxChoices selectedPayType; 
            if(remi.getDeposit()!=null)
                selectedPayType = new ComboBoxChoices("1", "A crédito");
            else
                selectedPayType = new ComboBoxChoices("0", "De contado");
            payType.getSelectionModel().select(selectedPayType);
            payType.setDisable(true);
            //
            orderPayment.setDisable(true);
            orderPayment.setSelected(false);
            invNumber.setText(bundle.getString("lblIvoiceNumber") + remission.getIdRemissionPk());
            invDate.setText(bundle.getString("lblInvoiceDate") + df.format(remission.getRemissionDate()));
            title.setText(bundle.getString("orderUpdateTitle"));
            person = remission.getIdSupplierFk();
            buttonsBox.getChildren().add(1, deleteButton);
            rmvProductButton.setDisable(false);
        } else if (type == ADD_INVENTORY) {
            invNumber.setText(bundle.getString("lblIvoiceNumber") + remission.getIdRemissionPk());
            invDate.setText(bundle.getString("lblInvoiceDate") + df.format(remission.getRemissionDate()));
            title.setText(bundle.getString("addInventoryUpdateTitle"));
            buttonsBox.getChildren().add(1, deleteButton);
            rmvProductButton.setDisable(false);
        } else if (type == MOVEMENT) {
            invNumber.setText(bundle.getString("lblIvoiceNumber") + remission.getIdRemissionPk());
            invDate.setText(bundle.getString("lblInvoiceDate") + df.format(remission.getRemissionDate()));
            title.setText(bundle.getString("movementUpdateTitle"));
            ComboBoxChoices selectedType = new ComboBoxChoices(String.valueOf(remission.getIdConceptFk().getIdConceptPk()), remission.getIdConceptFk().getConceptName());
            conceptType.getSelectionModel().select(selectedType);
            conceptType.setDisable(true);
            rmvProductButton.setDisable(false);
            if (remission.getIdSupplierFk() != null) {
                person = remission.getIdSupplierFk();
            } else {
                person = remission.getIdClientFk();
            }
            //Si el movimiento corresponde a una garantía, activa los items correspondientes
            if (remission.getIdConceptFk().getIdConceptPk() == 4) {
                buttonsBox.getChildren().add(1, stateButton);
                buttonsBox.getChildren().add(2, deleteButton);
                //Si la garantía está marcada como finalizada, deshabilita la edición
                if (remission.getStatus() == 2) {
                    buttonsBox.getChildren().remove(saveButton);
                    idPerson.setEditable(false);
                    firstName.setEditable(false);
                    lastName.setEditable(false);
                    phone.setEditable(false);
                    addres.setEditable(false);
                    email.setEditable(false);
                    city.setEditable(false);
                    getProduct.setEditable(false);
                    obs.setEditable(false);
                    rmvProductButton.setDisable(true);
                }
            } else {
                buttonsBox.getChildren().add(1, deleteButton);
            }
        } else {
            invNumber.setText(bundle.getString("lblIvoiceNumber") + remission.getIdRemissionPk());
            invDate.setText(bundle.getString("lblInvoiceDate") + df.format(remission.getRemissionDate()));
            RemissionModel rm = new RemissionModel();
            title.setText(bundle.getString("serviceOrderUpdateTitle"));
            person = remission.getIdClientFk();
            serviceReference.setText(remission.getObs());
            annotationsBox.setVisible(true);
            tcAnnotationDate = new TableColumn(bundle.getString("dateInvoiceDatatable"));
            tcAnnotationDate.prefWidthProperty().bind(annotationDataTable.widthProperty().divide(5));
            tcAnnotationDate.setCellValueFactory(new PropertyValueFactory<ProductCategory, String>("date"));
            tcAnnotationObs = new TableColumn(bundle.getString("annotationObsDatatable"));
            tcAnnotationObs.prefWidthProperty().bind(annotationDataTable.widthProperty().divide(new Float("1.25")));
            tcAnnotationObs.setCellValueFactory(new PropertyValueFactory<ProductCategory, String>("description"));
            annotationDataTable.getColumns().addAll(tcAnnotationDate, tcAnnotationObs);
            annotationDataTable.getItems().addAll(rm.findAllByIdRemission("Annotation", remission.getIdRemissionPk()));
            if (annotationDataTable.getItems().isEmpty()) {
                rmvAnnotationButton.setDisable(true);
            }
            remissionProductBox.getChildren().add(getProductBox);
            remissionProductBox.getChildren().add(productDataTable);
            rmvProductButton.setVisible(true);
            if (remission.getStatus() > 0 && remission.getStatus() < 3) {
                buttonsBox.getChildren().add(1, printButton);
                buttonsBox.getChildren().add(2, pdfButton);
                buttonsBox.getChildren().add(3, deleteButton);
                invoiceButton.setText(bundle.getString("invoiceButton"));
                buttonsBox.getChildren().add(4, invoiceButton);
            } else {
                buttonsBox.getChildren().remove(saveButton);
                invoiceButton.setText(bundle.getString("seeInvoiceButton"));
                buttonsBox.getChildren().add(0, invoiceButton);
                buttonsBox.getChildren().add(1, printButton);
                buttonsBox.getChildren().add(2, pdfButton);
                buttonsBox.getChildren().remove(saveButton);
                idPerson.setEditable(false);
                firstName.setEditable(false);
                lastName.setEditable(false);
                phone.setEditable(false);
                addres.setEditable(false);
                email.setEditable(false);
                city.setEditable(false);
                getProduct.setEditable(false);
                serviceReference.setEditable(false);
                rmvAnnotationButton.setVisible(false);
                addAnnotationButton.setVisible(false);
                rmvProductButton.setVisible(false);
            }
        }
        if (quotation == null) {
            if (!remission.getTranzactionCollection().isEmpty()) {
                TransactionDetailModel tdm = new TransactionDetailModel();
                oldProducts = new ArrayList<>();
                for (Tranzaction tranz : remission.getTranzactionCollection()) {
                    tranzaction = tranz;
                }
                ///Se define si se seleccionó pago por caja en caso de ser compra o pago
                if(tranzaction.getCashBoxChecked()!= null && (type == ORDER || type == ADD_INVENTORY)){
                    orderPayment.setSelected(true);
                    oldValueChecked = tranzaction.getTransactionPrice();
                }
                //
                oldProducts = tdm.findAllById("TransactionDetail", tranzaction.getIdTransactionPk());
                for (TransactionDetail transactionDetail : oldProducts) {
                    //Se comprueba que no tenga relacionados impuestos y descuentos al mismo tiempo
                    double realVl;
                    String[] taxs;
                    //En el caso en el que sea una remisión a facturar, se inicializa temporalmente una factura y se cargan los impuestos asociados
                    if(type == INVOICE && invoice==null)
                        invoice = new Invoice();
                    if ((type == INVOICE && transactionDetail.getDiscounts() != null && transactionDetail.getTaxes() != null) || 
                            (type == INVOICE && invoice.getIdDiscountFk()!= null && transactionDetail.getTaxes() != null)) {
                        float taxsPct = 0;
                        taxs = transactionDetail.getTaxes().split(";;");
                        for (String tx : taxs) {
                            String[] tempTx = tx.split(";");
                            taxsPct += new Float(tempTx[2]);
                        }
                        realVl = (taxsPct / 100) * transactionDetail.getUnitPrice();
                        realVl += transactionDetail.getUnitPrice();
                    } else {
                        realVl = transactionDetail.getUnitPrice();
                    }
                    productValuesBfTaxes.add(new ProductConverter(transactionDetail.getProduct().getIdProductPk(), realVl, transactionDetail.getDescription()));
                    //Se crea un nuevo producto para agregarlo a la tabla a mostrar
                    ProductConverter tempProduct = new ProductConverter(transactionDetail.getProduct().getIdProductPk(), transactionDetail.getProduct().getSkuProduct(), 
                            transactionDetail.getUnitPrice(), transactionDetail.getProduct().getProductDescription());
                    tempProduct.setAmount(transactionDetail.getAmount());
                    tempProduct.setTaxesCollection(new ArrayList<Tax>());
                    tempProduct.setDiscountCollection(new ArrayList<Discount>());
                    //Se trae la descripción asociada al producto en el movimiento
                    if (transactionDetail.getDescription() != null) {
                        tempProduct.setProductDescription(transactionDetail.getDescription());
                    } else {
                        tempProduct.setProductDescription(transactionDetail.getProduct().getProductDescription());
                    }
                    //Si tiene impuestos asociados
                    if (transactionDetail.getTaxes() != null) {
                        tempProduct.getTaxesCollection().clear();
                        String[] allTaxes = transactionDetail.getTaxes().split(";;");
                        for (String allTaxe : allTaxes) {
                            String[] asociatedTaxes = allTaxe.split(";");
                            Tax tax = new Tax(Integer.parseInt(asociatedTaxes[0]), asociatedTaxes[1], new Float(asociatedTaxes[2]));
                            if (!taxes.contains(tax)) {
                                taxes.add(tax);
                            }
                            tempProduct.getTaxesCollection().add(tax);
                            //Se relaciona el impuesto por defecto al producto
                            tempProduct.setByDefault(new ComboBoxChoices(tax.getIdTaxPk().toString(), tax.toString()));
                            tempProduct.setTaxSelected(tax.toString());
                        }
                    }
                    //Si tiene descuentos asociados
                    if (transactionDetail.getDiscounts() != null) {
                        tempProduct.getDiscountCollection().clear();
                        String[] allDiscounts = transactionDetail.getDiscounts().split(";;");
                        for (String allDisco : allDiscounts) {
                            String[] asociatedDisco = allDisco.split(";");
                            Discount discount = new Discount(Integer.parseInt(asociatedDisco[0]), new Float(asociatedDisco[2]));
                            if (!discounts.contains(discount)) {
                                discounts.add(discount);
                            }
                            tempProduct.getDiscountCollection().add(discount);
                        }
                    }
                    //Se redondea a 2 cifras el valor escrito
                    double roundedValue = tempProduct.getActualPrice();
                    roundedValue = (double)Math.round(roundedValue*100)/100;
                    tempProduct.setActualPrice(roundedValue);
                    tempProduct.setTotalPrice(format.format(roundedValue*tempProduct.getAmount()));
                    productDataTable.getItems().add(tempProduct);
                    totalInvoice += (transactionDetail.getUnitPrice() * transactionDetail.getAmount());
                }
                if (type == SERVICE_ORDER && !oldProducts.isEmpty()) {
                    rmvProductButton.setDisable(false);
                    invoiceButton.setDisable(false);
                }
            }
        } else {
            TransactionDetailModel tdm = new TransactionDetailModel();
            Collection<QuotationDetail> qd = tdm.findAllById("QuotationDetail", quotation.getIdQuotationPk());
            double realVl;
            String[] taxs;
            for (QuotationDetail quotationDetail : qd) {
                //Se comprueba que no tenga relacionados impuestos y descuentos al mismo tiempo
                if (quotationDetail.getDiscounts() != null && quotationDetail.getTaxes() != null) {
                    float taxsPct = 0;
                    taxs = quotationDetail.getTaxes().split(";;");
                    for (String tx : taxs) {
                        String[] tempTx = tx.split(";");
                        taxsPct += new Float(tempTx[2]);
                    }
                    realVl = (taxsPct / 100) * quotationDetail.getUnitPrice();
                    realVl += quotationDetail.getUnitPrice();
                } else {
                    realVl = quotationDetail.getUnitPrice();
                }
                productValuesBfTaxes.add(new ProductConverter(quotationDetail.getProduct().getIdProductPk(), realVl, quotationDetail.getDescription()));
                //Se crea el nuevo producto a asociar a la tabla
                ProductConverter tempProduct = new ProductConverter(quotationDetail.getProduct().getIdProductPk(), quotationDetail.getProduct().getSkuProduct(), 
                        quotationDetail.getUnitPrice(), quotationDetail.getProduct().getProductDescription());
                tempProduct.setAmount(quotationDetail.getAmount());
                tempProduct.setTaxesCollection(new ArrayList<Tax>());
                tempProduct.setDiscountCollection(new ArrayList<Discount>());
                //Se trae la descripción asociada al producto en el movimiento
                if (quotationDetail.getDescription() != null) {
                    tempProduct.setProductDescription(quotationDetail.getDescription());
                } else {
                    tempProduct.setProductDescription(quotationDetail.getProduct().getProductDescription());
                }
                //Si tiene impuestos asociados
                if (quotationDetail.getTaxes() != null) {
                    tempProduct.getTaxesCollection().clear();
                    String[] allTaxes = quotationDetail.getTaxes().split(";;");
                    for (String allTaxe : allTaxes) {
                        String[] asociatedTaxes = allTaxe.split(";");
                        Tax tax = new Tax(Integer.parseInt(asociatedTaxes[0]), asociatedTaxes[1], new Float(asociatedTaxes[2]));
                        if (!taxes.contains(tax)) {
                            taxes.add(tax);
                        }
                        tempProduct.getTaxesCollection().add(tax);
                    }
                }
                //Si tiene descuentos asociados
                if (quotationDetail.getDiscounts() != null) {
                    tempProduct.getDiscountCollection().clear();
                    String[] allDiscounts = quotationDetail.getDiscounts().split(";;");
                    for (String allDisco : allDiscounts) {
                        String[] asociatedDisco = allDisco.split(";");
                        Discount discount = new Discount(Integer.parseInt(asociatedDisco[0]), new Float(asociatedDisco[2]));
                        if (!discounts.contains(discount)) {
                            discounts.add(discount);
                        }
                        tempProduct.getDiscountCollection().add(discount);
                    }
                }
                //Se redondea a 2 cifras el valor escrito
                double roundedValue = tempProduct.getActualPrice();
                roundedValue = (double)Math.round(roundedValue*100)/100;
                tempProduct.setActualPrice(roundedValue);
                tempProduct.setTotalPrice(format.format(roundedValue*tempProduct.getAmount()));
                productDataTable.getItems().add(tempProduct);
                totalInvoice += (quotationDetail.getUnitPrice() * quotationDetail.getAmount());
            }
        }
        
        if (type != ADD_INVENTORY) {
            idPerson.setText(person.getNit());
            firstName.setText(person.getPersonFirstname());
            lastName.setText(person.getPersonLastname());
            phone.setText(person.getPersonPhoneNo());
            addres.setText(person.getPersonAddress());
            email.setText(person.getEmail());
            initPlace = person.getIdPlaceFk();
            city.setText(initPlace.toString());
        }
        if (type != SERVICE_ORDER && type != INVOICE) {
            obs.setText(remission.getObs());
        } else if (type == INVOICE) {
            if (remission.getIdConceptFk() != null) {
                obs.setText(invoice.getObs());
                payType.setDisable(true);
                discountOpt.setDisable(true);
                if (invoice.getCardType() != null || !invoice.getCardType().isEmpty()) {
                    String savedCardId;
                    if (invoice.getCardType().startsWith("Cr")) {
                        savedCardId = "0";
                    } else {
                        savedCardId = "1";
                    }
                    ComboBoxChoices savedCard = new ComboBoxChoices(savedCardId, invoice.getCardType());
                    cardReference.getSelectionModel().select(savedCard);
                    aprobationNo.setText(invoice.getNoReference());
                }
            } else if (remission.getObs() != null) {
                obs.setText(bundle.getString("lblRemissionNo") + " " + remission.getIdRemissionPk());
            } else {
                obs.setText(bundle.getString("lblQuotationNo") + " " + quotation.getIdQuotationPk());
            }
        }

        saveButton.setDisable(
                false);
        if (type < MOVEMENT && type > SERVICE_ORDER) {
            //payType.setDisable(true);
            updatePriceValues();
        }
        oldInvoiceTotal = totalInvoice;
    }

    /*
     Método para inicializar los valores del formulario para actualización
     */
    public void initializeValuesQuotation(Quotation quot) {
        quotation = quot;
        personExists = true;
        invoiceExists = true;
        if (!quotation.getQuotationDetailCollection().isEmpty()) {
            quotationOldProducts = new ArrayList<>();
            TransactionDetailModel tdm = new TransactionDetailModel();
            Collection<QuotationDetail> td = tdm.findAllById("QuotationDetail", quotation.getIdQuotationPk());
            quotationOldProducts = td;
            if (quotationOldProducts
                    != null) {
                for (QuotationDetail quotdt : quotationOldProducts) {
                    //Se comprueba que no tenga relacionados impuestos y descuentos al mismo tiempo
                    double realVl;
                    if (quotdt.getDiscounts() != null && quotdt.getTaxes() != null) {
                        float taxsPct = 0;
                        String[] taxs = quotdt.getTaxes().split(";;");
                        for (String tx : taxs) {
                            String[] tempTx = tx.split(";");
                            taxsPct += new Float(tempTx[2]);
                        }
                        realVl = (taxsPct / 100) * quotdt.getUnitPrice();
                        realVl += quotdt.getUnitPrice();
                    } else {
                        realVl = quotdt.getUnitPrice();
                    }
                    productValuesBfTaxes.add(new ProductConverter(quotdt.getProduct().getIdProductPk(), realVl, quotdt.getDescription()));
                    //Se crea el nuevo producto para asociar a la tabla
                    ProductConverter tempProduct = new ProductConverter(quotdt.getProduct().getIdProductPk(), quotdt.getProduct().getSkuProduct(), quotdt.getUnitPrice(),
                            quotdt.getProduct().getProductDescription());
                    tempProduct.setAmount(quotdt.getAmount());
                    tempProduct.setTaxesCollection(new ArrayList<Tax>());
                    tempProduct.setDiscountCollection(new ArrayList<Discount>());
                    //Se trae la descripción asociada al producto en el movimiento
                    if (quotdt.getDescription() != null) {
                        tempProduct.setProductDescription(quotdt.getDescription());
                    } else {
                        tempProduct.setProductDescription(quotdt.getProduct().getProductDescription());
                    }
                    //Si tiene impuestos asociados
                    if (quotdt.getTaxes() != null) {
                        tempProduct.getTaxesCollection().clear();
                        String[] allTaxes = quotdt.getTaxes().split(";;");
                        for (String allTaxe : allTaxes) {
                            String[] asociatedTaxes = allTaxe.split(";");
                            Tax tax = new Tax(Integer.parseInt(asociatedTaxes[0]), asociatedTaxes[1], new Float(asociatedTaxes[2]));
                            if (!taxes.contains(tax)) {
                                taxes.add(tax);
                            }
                            tempProduct.getTaxesCollection().add(tax);
                        }
                    }
                    //Si tiene descuentos asociados
                    if (quotdt.getDiscounts() != null) {
                        tempProduct.getDiscountCollection().clear();
                        String[] allDiscounts = quotdt.getDiscounts().split(";;");
                        for (String allDisco : allDiscounts) {
                            String[] asociatedDisco = allDisco.split(";");
                            Discount discount = new Discount(Integer.parseInt(asociatedDisco[0]), new Float(asociatedDisco[2]));
                            if (!discounts.contains(discount)) {
                                discounts.add(discount);
                            }
                            tempProduct.getDiscountCollection().add(discount);
                        }
                    }
                    //Se redondea a 2 cifras el valor escrito
                    double roundedValue = tempProduct.getActualPrice();
                    roundedValue = (double)Math.round(roundedValue*100)/100;
                    tempProduct.setActualPrice(roundedValue);
                    tempProduct.setTotalPrice(format.format(roundedValue*tempProduct.getAmount()));
                    productDataTable.getItems().add(tempProduct);
                    totalInvoice += (quotdt.getUnitPrice() * quotdt.getAmount());
                }
            }
        }
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        invNumber.setText(bundle.getString("lblIvoiceNumber") + quotation.getIdQuotationPk());
        invDate.setText(bundle.getString("lblInvoiceDate") + df.format(quotation.getQuotationDate()));
        person = quotation.getIdClientFk();
        title.setText(bundle.getString("quotationUpdateTitle"));
        rmvProductButton.setDisable(false);
        idPerson.setText(person.getNit());
        firstName.setText(person.getPersonFirstname());
        lastName.setText(person.getPersonLastname());
        phone.setText(person.getPersonPhoneNo());
        addres.setText(person.getPersonAddress());
        email.setText(person.getEmail());
        obs.setText(quotation.getObs());
        initPlace = person.getIdPlaceFk();
        city.setText(initPlace.toString());
        if (quotation.getStatus() == 0) {
            saveButton.setDisable(false);
            buttonsBox.getChildren().add(1, printButton);
            buttonsBox.getChildren().add(2, pdfButton);
            buttonsBox.getChildren().add(3, deleteButton);
            invoiceButton.setText(bundle.getString("invoiceButton"));
            buttonsBox.getChildren().add(4, invoiceButton);
        } else {
            buttonsBox.getChildren().remove(saveButton);
            invoiceButton.setText(bundle.getString("seeInvoiceButton"));
            buttonsBox.getChildren().add(0, invoiceButton);
            buttonsBox.getChildren().remove(saveButton);
            idPerson.setEditable(false);
            firstName.setEditable(false);
            lastName.setEditable(false);
            phone.setEditable(false);
            addres.setEditable(false);
            email.setEditable(false);
            obs.setEditable(false);
            city.setEditable(false);
            getProduct.setEditable(false);
            rmvProductButton.setDisable(true);
        }
        updatePriceValues();
    }

    @FXML
    private void saveInvoice(ActionEvent event) throws IOException {
        try {
            saveButton.setDisable(true);
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            pdf = new PrintPDF();
            ComboBoxChoices typeChoiced = (ComboBoxChoices) payType.getSelectionModel().getSelectedItem();
            selectedProducts = productDataTable.getItems();
            if (!personExists) {
                person = new Person();
            }
            person.setNit(idPerson.getText());
            person.setPersonFirstname(firstName.getText());
            person.setPersonLastname(lastName.getText());
            person.setPersonPhoneNo(phone.getText());
            person.setPersonAddress(addres.getText());
            person.setEmail(email.getText());
            person.setIdPlaceFk(initPlace);
            if (movementType == 0) {
                concept = (Concept) abs.findByIdInt("Concept", type);
            } else {
                concept = (Concept) abs.findByIdInt("Concept", movementType);
            }
            warehouse = (Warehouse) abs.findByIdInt("Warehouse", 1);
            if (!invoiceExists) {
                remission = new Remission();
                tranzaction = new Tranzaction();
                remission.setStatus(new Short("1"));
            }
            remission.setIdConceptFk(concept);
            if (type != INVOICE) {
                remission.setObs(obs.getText());
            }
            remission.setIdTurnFk(user);
            tranzaction.setIdWarehouseFk(warehouse);
            tranzaction.setTransactionPrice(totalInvoice);
            if (type == INVOICE) {
                if (!invoiceExists || invoiceRemission) {
                    invoice = new Invoice();
                    invoice.setStatus(new Short("1"));
                    Settings settings = (Settings) abs.findByIdInt("Settings", 1);
                    Integer newInvoice = Integer.parseInt(settings.getLastInvoice()) + 1;
                    invoice.setIdInvoicePk(newInvoice.toString());
                    invoice.setNoteHeader(settings.getNoteHeader());
                    invoice.setNoteFooter(settings.getNoteFooter());
                    settings.setLastInvoice(newInvoice.toString());
                    abs.updateIntermediate(settings);
                }
                Payment payment = (Payment) abs.findByIdInt("Payment", Integer.valueOf(typeChoiced.getItemValue()));
                invoice.setIdPaymentFk(payment);
                invoice.setObs(obs.getText());
                invoice.setIdDiscountFk(invoiceDiscount);
                if (isCreditCard) {
                    ComboBoxChoices selectedCard = cardReference.getSelectionModel().getSelectedItem();
                    invoice.setCardType(selectedCard.getItemLabel());
                    invoice.setNoReference(aprobationNo.getText());
                } else {
                    invoice.setNoReference("");
                    invoice.setCardType("");
                }
                if (!invoiceExists) {
                    abs.saveIntermediate(invoice);
                } else {
                    abs.updateIntermediate(invoice);
                }
                remission.setInvoiced(new Short("1"));
                if (personExists) {
                    abs.updateIntermediate(person);
                } else {
                    abs.saveIntermediate(person);
                }
                remission.setIdClientFk(person);
                remission.setIdInvoiceFk(invoice);
                if (isCredit) {
                    RemissionModel rm = new RemissionModel();
                    double actualLeftAmount = 0;
                    boolean exists = false;
                    if (!rm.findByIdSell(person.getIdPersonPk().toString()).isEmpty()) {
                        for (Remission rem : rm.findByIdSell(person.getIdPersonPk().toString())) {
                            creditRemission = rem;
                        }
                        if (!invoiceExists) {
                            actualLeftAmount = creditRemission.getLeftAmount();
                        } else {
                            actualLeftAmount = creditRemission.getLeftAmount() - oldInvoiceTotal;
                        }
                        exists = true;
                    } else {
                        creditRemission = new Remission();
                        Concept creditConcept = (Concept) abs.findByIdInt("Concept", 3);
                        creditRemission.setIdClientFk(person);
                        creditRemission.setIdConceptFk(creditConcept);
                        creditRemission.setStatus(new Short("1"));
                        creditRemission.setIdTurnFk(user);
                        creditRemission.setNoBuyReference(person.getIdPersonPk().toString());
                    }
                    remission.setDeposit(0.0);
                    actualLeftAmount += totalInvoice;
                    creditRemission.setLeftAmount(actualLeftAmount);
                    if (exists) {
                        rm.updateIntermediate(creditRemission);
                    } else {
                        rm.saveIntermediate(creditRemission);
                    }
                }
                if (invoiceRemission) {
                    remission.setStatus(new Short("3"));
                }
                if (quotation != null) {
                    quotation.setStatus(new Short("1"));
                    quotation.setIdInvoiceFk(invoice.getIdInvoicePk());
                    abs.updateIntermediate(quotation);
                }
                if (!invoiceExists) {
                    abs.saveIntermediate(remission);
                } else {
                    abs.updateIntermediate(remission);
                }
                tranzaction.setIdRemissionFk(remission);
                if (!invoiceExists) {
                    remission.addTrazaction(tranzaction);
                    TranzactionModel tm = new TranzactionModel();
                    tm.saveTranzaction(tranzaction, remission);
                } else {
                    abs.updateIntermediate(tranzaction);
                }
                if (!selectedProducts.isEmpty()) {
                    TransactionDetailModel tdm = new TransactionDetailModel();
                    List < ProductConverter > updated = new ArrayList<>();
                    if (invoiceExists) {
                        int changeInQuantity;
                        boolean add;
                        //Se recorren los productos antiguos y se comparan con los nuevos para así ratificar los cambios
                        for (TransactionDetail oldPrd : oldProducts) {
                            //Se trae el producto asociado a la tabla
                            ProductConverter tempProduct = new ProductConverter();
                            for(ProductConverter cnvrtr:selectedProducts){
                                if(oldPrd.getProduct().getIdProductPk().equals(cnvrtr.getIdProductPk())){
                                    tempProduct = cnvrtr;
                                }
                            }
                            //Se compara si hace parte de los nuevos 
                            if (selectedProducts.contains(tempProduct)) {
                                int index = selectedProducts.indexOf(tempProduct);
                                ProductConverter sameProduct = selectedProducts.get(index);
                                //Se verifica que no sea una remisión a facturar para no hacer la comparación de inventario
                                if(!invoiceRemission){
                                    changeInQuantity = sameProduct.getAmount() - oldPrd.getAmount();
                                    oldPrd.setAmount(oldPrd.getAmount() + changeInQuantity);
                                }else
                                    changeInQuantity = oldPrd.getAmount();
                                oldPrd.setUnitPrice(sameProduct.getActualPrice());
                                oldPrd.setDescription(sameProduct.getProductDescription());
                                updated.add(tempProduct);
                                abs.updateIntermediate(oldPrd);
                                //Se especifica si es aumento o deceremento
                                add = true;
                                if (changeInQuantity > 0) {
                                    add = false;
                                }
                                //En caso de que se este facturando en 
                                updateStock(Math.abs(changeInQuantity), oldPrd.getProduct(), add);
                            } else {
                                changeInQuantity = oldPrd.getAmount();
                                Product toUpdate = oldPrd.getProduct();
                                tdm.deleteTemp(oldPrd.getTranzaction().getIdTransactionPk(), oldPrd.getProduct().getIdProductPk(), tranzaction);
                                updateStock(changeInQuantity, toUpdate, false);
                                //resetProductOriginalVales(toUpdate);
                            }
                        }
                    }
                    //Se guardan los nuevos datos
                    for (ProductConverter slPrd : selectedProducts) {
                        if (!updated.contains(slPrd)) {
                            //Se trae el verdadero producto para asociarlo a la transacción 
                            Product product1 = (Product)tdm.findByIdInt("Product", slPrd.getIdProductPk());
                            TransactionDetailPK tPK = new TransactionDetailPK(product1.getIdProductPk(), tranzaction.getIdTransactionPk());
                            tansactionDetail = new TransactionDetail(tPK);
                            tansactionDetail.setProduct(product1);
                            tansactionDetail.setTranzaction(tranzaction);
                            tansactionDetail.setAmount(slPrd.getAmount());
                            tansactionDetail.setUnitPrice(slPrd.getActualPrice());
                            tansactionDetail.setDescription(slPrd.getProductDescription());
                            //Se guardan los impuestos asociados si los tiene
                            if (!slPrd.getTaxesCollection().isEmpty()) {
                                String allAsociatedTaxes = "";
                                for (Tax tax : slPrd.getTaxesCollection()) {
                                    allAsociatedTaxes += tax.getIdTaxPk() + ";" + tax.getTaxName() + ";" + tax.getTaxPct() + ";;";
                                }
                                tansactionDetail.setTaxes(allAsociatedTaxes);
                            }
                            //Se guardan los descuentos asociados si los tiene
                            if (!slPrd.getDiscountCollection().isEmpty()) {
                                String allAsociatedDiscounts = "";
                                for (Discount discount : slPrd.getDiscountCollection()) {
                                    allAsociatedDiscounts += discount.getIdDiscountPk() + ";" + discount.getDiscountDescrption() + ";" + discount.getDiscountPct() + ";;";
                                }
                                tansactionDetail.setDiscounts(allAsociatedDiscounts);
                            }
                            tdm.saveTransactionDetail(tansactionDetail, tranzaction);
                            updateStock(slPrd.getAmount(), product1, false);
                        }
                    }
                }
            } else if (type == ORDER) {
                if (personExists) {
                    abs.updateIntermediate(person);
                } else {
                    person.setPersonType("S");
                    abs.saveIntermediate(person);
                }
                remission.setInvoiced(new Short("0"));
                remission.setIdSupplierFk(person);
                if (isCredit) {
                    RemissionModel rm = new RemissionModel();
                    double actualLeftAmount = 0;
                    boolean exists = false;
                    if (!rm.findByIdSell(person.getIdPersonPk().toString()).isEmpty()) {
                        for (Remission rem : rm.findByIdSell(person.getIdPersonPk().toString())) {
                            creditRemission = rem;
                        }
                        if (!invoiceExists) {
                            actualLeftAmount = creditRemission.getLeftAmount();
                        } else {
                            actualLeftAmount = creditRemission.getLeftAmount() - oldInvoiceTotal;
                        }
                        exists = true;
                    } else {
                        creditRemission = new Remission();
                        Concept creditConcept = (Concept) abs.findByIdInt("Concept", 12);
                        creditRemission.setIdSupplierFk(person);
                        creditRemission.setIdConceptFk(creditConcept);
                        creditRemission.setStatus(new Short("1"));
                        creditRemission.setIdTurnFk(user);
                        creditRemission.setNoBuyReference(person.getIdPersonPk().toString());
                    }
                    remission.setDeposit(0.0);
                    actualLeftAmount += totalInvoice;
                    creditRemission.setLeftAmount(actualLeftAmount);
                    if (exists) {
                        rm.updateIntermediate(creditRemission);
                    } else {
                        rm.saveIntermediate(creditRemission);
                    }
                }
                if (!invoiceExists) {
                    abs.saveIntermediate(remission);
                } else {
                    abs.updateIntermediate(remission);
                }
                tranzaction.setIdRemissionFk(remission);
                if (!invoiceExists) {
                    //Se agrega la opción de pago seleccionada
                    if(orderPayment.isSelected())
                        tranzaction.setCashBoxChecked(new Short("1"));
                    remission.addTrazaction(tranzaction);
                    TranzactionModel tm = new TranzactionModel();
                    tm.saveTranzaction(tranzaction, remission);
                } else {
                    abs.updateIntermediate(tranzaction);
                }
                if (!selectedProducts.isEmpty()) {
                    TransactionDetailModel tdm = new TransactionDetailModel();
                    List<ProductConverter> updated = new ArrayList<>();
                    if (invoiceExists) {
                        int changeInQuantity;
                        boolean add;
                        //Se recorren los productos antiguos y se comparan con los nuevos para así ratificar los cambios
                        for (TransactionDetail oldPrd : oldProducts) {
                            //Se trae el producto asociado a la tabla
                            ProductConverter tempProduct = new ProductConverter();
                            for(ProductConverter cnvrtr:selectedProducts){
                                if(oldPrd.getProduct().getIdProductPk().equals(cnvrtr.getIdProductPk())){
                                    tempProduct = cnvrtr;
                                }
                            }
                            //Se compara si hace parte de los nuevos 
                            if (selectedProducts.contains(tempProduct)) {
                                int index = selectedProducts.indexOf(tempProduct);
                                ProductConverter sameProduct = selectedProducts.get(index);
                                changeInQuantity = sameProduct.getAmount() - oldPrd.getAmount();
                                oldPrd.setAmount(oldPrd.getAmount() + changeInQuantity);
                                oldPrd.setUnitPrice(sameProduct.getActualPrice());
                                oldPrd.setDescription(sameProduct.getProductDescription());
                                //Se actualizan los impuestos asociados si los tiene
                                if (!sameProduct.getTaxesCollection().isEmpty()) {
                                    String allAsociatedTaxes = "";
                                    for (Tax tax : sameProduct.getTaxesCollection()) {
                                        allAsociatedTaxes += tax.getIdTaxPk() + ";" + tax.getTaxName() + ";" + tax.getTaxPct() + ";;";
                                    }
                                    oldPrd.setTaxes(allAsociatedTaxes);
                                }else{
                                    oldPrd.setTaxes(null);
                                }
                                updated.add(tempProduct);
                                abs.updateIntermediate(oldPrd);
                                //Se especifica si es aumento o deceremento
                                add = false;
                                if (changeInQuantity > 0) {
                                    add = true;
                                }
                                updateStock(Math.abs(changeInQuantity), oldPrd.getProduct(), add);
                            } else {
                                changeInQuantity = oldPrd.getAmount();
                                Product toUpdate = oldPrd.getProduct();
                                tdm.deleteTemp(oldPrd.getTranzaction().getIdTransactionPk(), oldPrd.getProduct().getIdProductPk(), tranzaction);
                                updateStock(changeInQuantity, toUpdate, false);
                                //resetProductOriginalVales(toUpdate);
                            }
                        }
                    }
                    //Se guardan los nuevos datos
                    for (ProductConverter sldPrd : selectedProducts) {
                        if (!updated.contains(sldPrd)) {
                            //Se trae el verdadero producto para asociarlo a la transacción 
                            Product product1 = (Product)tdm.findByIdInt("Product", sldPrd.getIdProductPk());
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
                            /*Se guardan los descuentos asociados si los tiene
                             if (!product1.getProductDiscountCollection().isEmpty()) {
                             String allAsociatedDiscounts = "";
                             for (ProductDiscount discount : product1.getProductDiscountCollection()) {
                             allAsociatedDiscounts += discount.getDiscount().getIdDiscountPk() + ";" + discount.getDiscount().getDiscountDescrption() + ";" + discount.getDiscount().getDiscountPct() + ";;";
                             }
                             tansactionDetail.setDiscounts(allAsociatedDiscounts);
                             }*/
                            tdm.saveTransactionDetail(tansactionDetail, tranzaction);  
                            updateStock(sldPrd.getAmount(), product1, true);
                        }
                    }
                }
            } else {
                if (personExists) {
                    abs.updateIntermediate(person);
                } else {
                    person.setPersonType(personType);
                    abs.saveIntermediate(person);
                }
                remission.setInvoiced(new Short("0"));
                if (movementType > 4) {
                    remission.setStatus(new Short("2"));
                }
                if (personType.equals("S")) {
                    remission.setIdSupplierFk(person);
                } else {
                    remission.setIdClientFk(person);
                }
                if (!invoiceExists) {
                    abs.saveIntermediate(remission);
                } else {
                    abs.updateIntermediate(remission);
                }
                tranzaction.setIdRemissionFk(remission);
                if (!invoiceExists) {
                    remission.addTrazaction(tranzaction);
                    TranzactionModel tm = new TranzactionModel();
                    tm.saveTranzaction(tranzaction, remission);
                } else {
                    abs.updateIntermediate(tranzaction);
                }
                if (!selectedProducts.isEmpty()) {
                    TransactionDetailModel tdm = new TransactionDetailModel();
                    List<ProductConverter> updated = new ArrayList<>();
                    if (invoiceExists) {
                        int changeInQuantity;
                        boolean add;
                        //Se recorren los productos antiguos y se comparan con los nuevos para así ratificar los cambios
                        for (TransactionDetail oldPrd : oldProducts) {
                            //Se trae el producto asociado a la tabla
                            ProductConverter tempProduct = new ProductConverter();
                            for(ProductConverter cnvrtr:selectedProducts){
                                if(oldPrd.getProduct().getIdProductPk().equals(cnvrtr.getIdProductPk())){
                                    tempProduct = cnvrtr;
                                }
                            }
                            //Se compara si hace parte de los nuevos 
                            if (selectedProducts.contains(tempProduct)) {
                                int index = selectedProducts.indexOf(tempProduct);
                                ProductConverter sameProduct = selectedProducts.get(index);
                                changeInQuantity = sameProduct.getAmount() - oldPrd.getAmount();
                                oldPrd.setAmount(oldPrd.getAmount() + changeInQuantity);
                                oldPrd.setUnitPrice(sameProduct.getActualPrice());
                                oldPrd.setDescription(sameProduct.getProductDescription());
                                updated.add(tempProduct);
                                abs.updateIntermediate(oldPrd);
                                //Se especifica si es aumento o deceremento
                                add = true;
                                if (changeInQuantity > 0) {
                                    add = false;
                                }
                                updateStock(Math.abs(changeInQuantity), oldPrd.getProduct(), add);
                            } else {
                                changeInQuantity = oldPrd.getAmount();
                                Product toUpdate = oldPrd.getProduct();
                                tdm.deleteTemp(oldPrd.getTranzaction().getIdTransactionPk(), oldPrd.getProduct().getIdProductPk(), tranzaction);
                                updateStock(changeInQuantity, toUpdate, true);
                            }
                        }
                    }
                    //Se guardan los nuevos datos
                    for (ProductConverter sldPrd : selectedProducts) {
                        if (!updated.contains(sldPrd)) {
                            //Se trae el verdadero producto para asociarlo a la transacción 
                            Product product1 = (Product)tdm.findByIdInt("Product", sldPrd.getIdProductPk());
                            TransactionDetailPK tPK = new TransactionDetailPK(product1.getIdProductPk(), tranzaction.getIdTransactionPk());
                            tansactionDetail = new TransactionDetail(tPK);
                            tansactionDetail.setProduct(product1);
                            tansactionDetail.setTranzaction(tranzaction);
                            tansactionDetail.setAmount(sldPrd.getAmount());
                            tansactionDetail.setUnitPrice(sldPrd.getActualPrice());
                            tansactionDetail.setDescription(sldPrd.getProductDescription());
                            tdm.saveTransactionDetail(tansactionDetail, tranzaction);
                            updateStock(sldPrd.getAmount(), product1, false);
                        }
                    }
                }
            }
            //Se actualiza el valor esperado en caja en caso que haya sido seleccionado
            if (type == ORDER) {
                if(tranzaction.getCashBoxChecked()!=null){
                    double transactionTotal = totalInvoice;
                    if(invoiceExists){
                        transactionTotal -= oldInvoiceTotal;
                    }
                    updateBoxExpectedMoney(user, transactionTotal, false);
                }
            } else if (type == INVOICE && payType.getSelectionModel().getSelectedItem().getItemValue().equals("1")) {
                updateBoxExpectedMoney(user, totalInvoice, true);
            }
            //Se ratifican los cambios de la transacción
            abs.executeCommit();
            if (type == INVOICE) {
                pdf.printInvoice(tranzaction, null, null, selectedProducts, productValuesBfTaxes, type, bundle, true);
            }
            //updateProductsOriginalVales();
            getMainController().setDataTableView();
            if (invoiceExists) {
                getMainController().setMessage(bundle.getString("updateSuccess"), false);
            } else {
                getMainController().setMessage(bundle.getString("createSuccess"), false);
            }
            closeModal();
            if (type != ORDER) {
                mainController.checkMinimumStock(selectedProducts);
            }
        } catch (IOException | HeadlessException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Imprimir factura", e.getMessage(), sw.toString(), user.toString());
            closeModal();
            getMainController().setMessage(bundle.getString("createPrintError"), true);
            mainController.setDataTableView();
            //e.printStackTrace();
        } catch (Exception e) {
            abs.executeRollback();
            //Especificando el movimiento actual
            String mov = "Factura";
            if(type==ORDER)
                mov = "Compra a proveedor";
            else if(type == MOVEMENT)
                mov = "Movimiento de inventario";
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar "+mov, e.getMessage(), e.getLocalizedMessage(), user.toString());
            closeModal();
            getMainController().setMessage(bundle.getString("createError"), true);
            e.printStackTrace();
        }
    }

    /*
     Método para registrar una remisión
     @Param: Evento que ejecuta la acción onAction (ActionEvent event)
     */
    @FXML
    private void saveQuotation(ActionEvent event) {
        try {
            saveButton.setDisable(true);
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            pdf = new PrintPDF();
            selectedProducts = productDataTable.getItems();
            if (!personExists) {
                person = new Person();
            }
            person.setNit(idPerson.getText());
            person.setPersonFirstname(firstName.getText());
            person.setPersonLastname(lastName.getText());
            person.setPersonPhoneNo(phone.getText());
            person.setPersonAddress(addres.getText());
            person.setEmail(email.getText());
            person.setIdPlaceFk(initPlace);
            if (!invoiceExists) {
                quotation = new Quotation();
                quotation.setStatus(new Short("0"));
            }
            if (personExists) {
                abs.updateIntermediate(person);
            } else {
                abs.saveIntermediate(person);
            }
            quotation.setIdClientFk(person);
            quotation.setIdSell(user.getIdTurnPk().toString());
            quotation.setObs(obs.getText());
            if (!invoiceExists) {
                abs.saveIntermediate(quotation);
            } else {
                abs.updateIntermediate(quotation);
            }
            if (!selectedProducts.isEmpty()) {
                TransactionDetailModel tdm = new TransactionDetailModel();
                List<ProductConverter> updated = new ArrayList<>();
                if (invoiceExists) {
                    int changeInQuantity;
                    //Se recorren los productos antiguos y se comparan con los nuevos para así ratificar los cambios
                    for (QuotationDetail qtd : quotationOldProducts) {
                        //Se trae el producto asociado a la tabla
                            ProductConverter tempProduct = new ProductConverter();
                            for(ProductConverter cnvrtr:selectedProducts){
                                if(qtd.getProduct().getIdProductPk().equals(cnvrtr.getIdProductPk())){
                                    tempProduct = cnvrtr;
                                }
                            }
                        //Se compara si hace parte de los nuevos 
                        if (selectedProducts.contains(tempProduct)) {
                            int index = selectedProducts.indexOf(tempProduct);
                            ProductConverter sameProduct = selectedProducts.get(index);
                            changeInQuantity = sameProduct.getAmount()- qtd.getAmount();
                            qtd.setAmount(qtd.getAmount() + changeInQuantity);
                            qtd.setUnitPrice(sameProduct.getActualPrice());
                            qtd.setDescription(sameProduct.getProductDescription());
                            updated.add(tempProduct);
                            abs.updateIntermediate(qtd);
                        } else {
                            quotation.removeQuotationDetail(qtd);
                            tdm.deleteQuotationDetails(qtd.getQuotationDetailPK(), quotation);
                            //resetProductOriginalVales(toUpdate);
                        }
                    }
                }
                //Se guardan los nuevos datos
                for (ProductConverter sldPrd : selectedProducts) {
                    if (!updated.contains(sldPrd)) {
                        //Se trae el verdadero producto para asociarlo a la transacción 
                        Product product1 = (Product)tdm.findByIdInt("Product", sldPrd.getIdProductPk());
                        QuotationDetailPK quotPK = new QuotationDetailPK(quotation.getIdQuotationPk(), product1.getIdProductPk());
                        QuotationDetail quotationDetail = new QuotationDetail(quotPK);
                        quotationDetail.setProduct(product1);
                        quotationDetail.setQuotation(quotation);
                        quotationDetail.setAmount(sldPrd.getAmount());
                        quotationDetail.setUnitPrice(sldPrd.getActualPrice());
                        quotationDetail.setDescription(sldPrd.getProductDescription());
                        //Se guardan los impuestos asociados si los tiene
                        if (!sldPrd.getTaxesCollection().isEmpty()) {
                            String allAsociatedTaxes = "";
                            for (Tax tax : sldPrd.getTaxesCollection()) {
                                allAsociatedTaxes += tax.getIdTaxPk() + ";" + tax.getTaxName() + ";" + tax.getTaxPct() + ";;";
                            }
                            quotationDetail.setTaxes(allAsociatedTaxes);
                        }
                        //Se guardan los descuentos asociados si los tiene
                        if (!sldPrd.getDiscountCollection().isEmpty()) {
                            String allAsociatedDiscounts = "";
                            for (Discount discount : sldPrd.getDiscountCollection()) {
                                allAsociatedDiscounts += discount.getIdDiscountPk() + ";" + discount.getDiscountDescrption() + ";" + discount.getDiscountPct() + ";;";
                            }
                            quotationDetail.setDiscounts(allAsociatedDiscounts);
                        }
                        quotation.addQuotationDetail(quotationDetail);
                        tdm.saveQuotationDetail(quotationDetail, quotation);
                    }
                }
            }
            abs.executeCommit();
            //Se comprueba si esta en vista cajero o administrador
            if(mainController.isAdminView)
                mainController.setDataTableView();
            else
                mainController.setInvoiceDataTableView();
            pdf.printInvoice(tranzaction, null, quotation, selectedProducts, productValuesBfTaxes, type, bundle, true);
            if (invoiceExists) {
                //pdf.printInvoice(tranzaction, null, quotation, selectedProducts, productValuesBfTaxes, type, bundle, true);
                mainController.setMessage(bundle.getString("updateSuccess"), false);
            } else {
                //pdf.printInvoice(tranzaction, null, quotation, selectedProducts, productValuesBfTaxes, type, bundle, true);
                mainController.setMessage(bundle.getString("createSuccess"), false);
            }
            closeModal();
            //updateProductOriginalVales();
        } catch (IOException | HeadlessException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Imprimir cotización", e.getMessage(), sw.toString(), user.toString());
            closeModal();
            mainController.setMessage(bundle.getString("createPrintError"), true);
            //Se comprueba si esta en vista cajero o administrador
            if(mainController.isAdminView)
                mainController.setDataTableView();
            else
                mainController.setInvoiceDataTableView();
            //e.printStackTrace();
        } catch (Exception e) {
            abs.executeRollback();
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar cotización", e.getMessage(), e.getLocalizedMessage(), user.toString());
            closeModal();
            //Se comprueba si esta en vista cajero o administrador
            if(mainController.isAdminView)
                mainController.setDataTableView();
            else
                mainController.setInvoiceDataTableView();
            e.printStackTrace();
        }
    }

    /*
     Método para registrar una remisión
     @Param: Evento que ejecuta la acción onAction (ActionEvent event)
     */
    @FXML
    private void saveRemission(ActionEvent event) {
        try {
            saveButton.setDisable(true);
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            pdf = new PrintPDF();
            if (!personExists) {
                person = new Person();
            }
            person.setNit(idPerson.getText());
            person.setPersonFirstname(firstName.getText());
            person.setPersonLastname(lastName.getText());
            person.setPersonPhoneNo(phone.getText());
            person.setPersonAddress(addres.getText());
            person.setEmail(email.getText());
            person.setIdPlaceFk(initPlace);
            if (!invoiceExists) {
                remission = new Remission();
                remission.setStatus(new Short("1"));
            }
            remission.setObs(serviceReference.getText());
            remission.setIdTurnFk(user);
            if (personExists) {
                abs.updateIntermediate(person);
            } else {
                abs.saveIntermediate(person);
            }
            remission.setIdClientFk(person);
            if (invoiceExists) {
                warehouse = (Warehouse) abs.findByIdInt("Warehouse", 1);
                if (tranzaction == null) {
                    tranzaction = new Tranzaction();
                }
                tranzaction.setIdWarehouseFk(warehouse);
                tranzaction.setIdRemissionFk(remission);
                if (!remission.getTranzactionCollection().contains(tranzaction)) {
                    remission.addTrazaction(tranzaction);
                    TranzactionModel tm = new TranzactionModel();
                    tm.saveTranzaction(tranzaction, remission);
                } else {
                    abs.updateIntermediate(tranzaction);
                }
                selectedProducts = productDataTable.getItems();
                TransactionDetailModel tdm = new TransactionDetailModel();
                if (!tranzaction.getTransactionDetailCollection().isEmpty()) {
                    for (TransactionDetail dt : oldProducts) {
                        tranzaction.getTransactionDetailCollection().remove(dt);
                        tdm.deleteTemp(tranzaction.getIdTransactionPk(), dt.getProduct().getIdProductPk(), tranzaction);
                    }
                }
                for (ProductConverter sldPrd : selectedProducts) {
                    //Se trae el verdadero producto para asociarlo a la transacción 
                    Product product1 = (Product)tdm.findByIdInt("Product", sldPrd.getIdProductPk());
                    TransactionDetailPK tPK = new TransactionDetailPK(product1.getIdProductPk(), tranzaction.getIdTransactionPk());
                    tansactionDetail = new TransactionDetail(tPK);
                    tansactionDetail.setProduct(product1);
                    tansactionDetail.setTranzaction(tranzaction);
                    tansactionDetail.setAmount(sldPrd.getAmount());
                    tansactionDetail.setUnitPrice(sldPrd.getActualPrice());
                    tansactionDetail.setDescription(sldPrd.getProductDescription());
                    //tranzaction.addTrazactionDetail(tansactionDetail);
                    //Se guardan los impuestos asociados si los tiene
                    if (!sldPrd.getTaxesCollection().isEmpty()) {
                        String allAsociatedTaxes = "";
                        for (Tax tax : sldPrd.getTaxesCollection()) {
                            allAsociatedTaxes += tax.getIdTaxPk() + ";" + tax.getTaxName() + ";" + tax.getTaxPct() + ";;";
                        }
                        tansactionDetail.setTaxes(allAsociatedTaxes);
                    }
                    tranzaction.getTransactionDetailCollection().add(tansactionDetail);
                    tdm.saveTransactionDetail(tansactionDetail, tranzaction);
                }
            }
            if (!invoiceExists) {
                abs.saveIntermediate(remission);
            } else {
                abs.updateIntermediate(remission);
            }
            abs.executeCommit();
            mainController.setDataTableView();
            if (invoiceExists) {
                mainController.setMessage(bundle.getString("updateSuccess"), false);
            } else {
                pdf.printInvoice(tranzaction, remission, null, null, null, type, bundle, true);
                mainController.setMessage(bundle.getString("createSuccess"), false);
            }
            closeModal();
            //updateProductsOriginalVales();
        } catch (Exception e) {
            abs.executeRollback();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar remisión", e.getMessage(), sw.toString(), user.toString());
            closeModal();
            mainController.setMessage(bundle.getString("createError"), true);
            //e.printStackTrace();
        }
    }

    /*
     Método para registrar una remisión
     @Param: Evento que ejecuta la acción onAction (ActionEvent event)
     */
    @FXML
    private void saveAddInventory(ActionEvent event) {
        try {
            saveButton.setDisable(true);
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            if (!invoiceExists) {
                concept = (Concept) abs.findByIdInt("Concept", 8);
                warehouse = (Warehouse) abs.findByIdInt("Warehouse", 1);
                remission = new Remission();
                remission.setStatus(new Short("1"));
                remission.setInvoiced(new Short("0"));
                remission.setIdConceptFk(concept);
                remission.setIdTurnFk(user);
                remission.setObs(obs.getText());
                abs.saveIntermediate(remission);
                tranzaction = new Tranzaction();
                tranzaction.setIdRemissionFk(remission);
                tranzaction.setIdWarehouseFk(warehouse);
                tranzaction.setTransactionPrice(0.0);
                abs.saveIntermediate(tranzaction);
                remission.getTranzactionCollection().add(tranzaction);
            } else {
                remission.setObs(obs.getText());
                abs.updateIntermediate(remission);
            }
            selectedProducts = productDataTable.getItems();
            if (!selectedProducts.isEmpty()) {
                TransactionDetailModel tdm = new TransactionDetailModel();
                List<ProductConverter> updated = new ArrayList<>();
                if (invoiceExists) {
                    int changeInQuantity;
                    boolean add;
                    //Se recorren los productos antiguos y se comparan con los nuevos para así ratificar los cambios
                    for (TransactionDetail oldPrd : oldProducts) {
                        //Se trae el producto asociado a la tabla
                            ProductConverter tempProduct = new ProductConverter();
                            for(ProductConverter cnvrtr:selectedProducts){
                                if(oldPrd.getProduct().getIdProductPk().equals(cnvrtr.getIdProductPk())){
                                    tempProduct = cnvrtr;
                                }
                            }
                        //Se compara si hace parte de los nuevos 
                        if (selectedProducts.contains(tempProduct)) {
                            int index = selectedProducts.indexOf(tempProduct);
                            ProductConverter sameProduct = selectedProducts.get(index);
                            changeInQuantity = sameProduct.getAmount()- oldPrd.getAmount();
                            oldPrd.setAmount(oldPrd.getAmount() + changeInQuantity);
                            oldPrd.setUnitPrice(sameProduct.getActualPrice());
                            oldPrd.setDescription(sameProduct.getProductDescription());
                            updated.add(tempProduct);
                            abs.updateIntermediate(oldPrd);
                            //Se especifica si es aumento o deceremento
                            add = false;
                            if (changeInQuantity > 0) {
                                add = true;
                            }
                            updateStock(Math.abs(changeInQuantity), oldPrd.getProduct(), add);
                        } else {
                            changeInQuantity = oldPrd.getAmount();
                            Product toUpdate = oldPrd.getProduct();
                            tdm.deleteTemp(oldPrd.getTranzaction().getIdTransactionPk(), oldPrd.getProduct().getIdProductPk(), tranzaction);
                            updateStock(changeInQuantity, toUpdate, false);
                            //resetProductOriginalVales(toUpdate);
                        }
                    }
                }
                //Se guardan los nuevos datos
                for (ProductConverter sldPrd : selectedProducts) {
                    if (!updated.contains(sldPrd)) {
                        //Se trae el verdadero producto para asociarlo a la transacción 
                        Product product1 = (Product)tdm.findByIdInt("Product", sldPrd.getIdProductPk());
                        TransactionDetailPK tPK = new TransactionDetailPK(product1.getIdProductPk(), tranzaction.getIdTransactionPk());
                        tansactionDetail = new TransactionDetail(tPK);
                        tansactionDetail.setProduct(product1);
                        tansactionDetail.setTranzaction(tranzaction);
                        tansactionDetail.setAmount(sldPrd.getAmount());
                        tansactionDetail.setUnitPrice(sldPrd.getActualPrice());
                        tansactionDetail.setDescription(sldPrd.getProductDescription());
                        tdm.saveTransactionDetail(tansactionDetail, tranzaction);
                        updateStock(sldPrd.getAmount(), product1, true);
                    }
                }
            }
            abs.executeCommit();
            getMainController().setDataTableView();
            if (invoiceExists) {
                getMainController().setMessage(bundle.getString("updateSuccess"), false);
            } else {
                getMainController().setMessage(bundle.getString("createSuccess"), false);
            }
            closeModal();
            //updateProductsOriginalVales();
        } catch (Exception e) {
            abs.executeRollback();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar carga de inventario", e.getMessage(), sw.toString(), user.toString());
            closeModal();
            getMainController().setMessage(bundle.getString("createError"), true);
            //e.printStackTrace();
        }
    }
    /*
     Método para borrar el dato actual
     */

    public void deleteInvoice() {
        try {
            abs = new AbstractFacade();
            //selectedProducts = productDataTable.getItems();
            boolean load = true;
            if (type == ORDER || type == ADD_INVENTORY) {
                load = false;
            }
            //Se comprueba si es una compra y si el pago fue a crédito
            if (type == ORDER && isCredit) {
                RemissionModel rm = new RemissionModel();
                for (Remission rem : rm.findByIdSell(person.getIdPersonPk().toString())) {
                    creditRemission = rem;
                }
                double actualLeftAmount = creditRemission.getLeftAmount();
                actualLeftAmount -= tranzaction.getTransactionPrice();
                creditRemission.setLeftAmount(actualLeftAmount);
                rm.updateIntermediate(creditRemission);
            }
            //
            TransactionDetailModel tdm = new TransactionDetailModel();
            for (TransactionDetail dt : oldProducts) {
                tdm.delete(dt.getTranzaction().getIdTransactionPk(), dt.getProduct().getIdProductPk());
                //Verifica que no sea una garantía marcada como finalizada para realizar el cargue al stock
                if (remission.getIdConceptFk().getIdConceptPk() == 4 && remission.getStatus() == 2) {
                    // do nothing
                } else {
                    updateStock(dt.getAmount(), dt.getProduct(), load);
                }
            }
            abs.deleteIntIntermediate("Tranzaction", tranzaction.getIdTransactionPk());
            abs.deleteIntIntermediate("Remission", remission.getIdRemissionPk());
            //Se actualiza el valor esperado en caja
            if (type == ORDER) {
                if(tranzaction.getCashBoxChecked()!=null)
                    updateBoxExpectedMoney(user, totalInvoice, true);
            }
            //Se ratifican los cambios de la transacción
            abs.executeCommit();
            getMainController().setDataTableView();
            getMainController().setMessage(bundle.getString("deleteSuccess"), false);
            closeModal();
            //updateProductsOriginalVales();
        } catch (Exception e) {
            abs.executeRollback();
            //Definiendo el tipo de movimiento a borrar
            String mov = "Compra a proveedor";
            if(type == MOVEMENT)
                mov = "Movimiento de inventario";
            else if(type == ADD_INVENTORY)
                mov = "Carga de inventario";
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Eliminar "+mov, e.getMessage(), sw.toString(), user.toString());
            getMainController().setMessage(bundle.getString("deleteError"), true);
            closeModal();
            //e.printStackTrace();
        }
    }

    /*
     Método para borrar el dato actual
     */
    public void deleteQuotation() {
        try {
            abs = new AbstractFacade();
            TransactionDetailModel tdm = new TransactionDetailModel();
            for (QuotationDetail qt : quotationOldProducts) {
                tdm.deleteQuotationDetail(qt.getQuotationDetailPK());
            }
            tdm.deleteIntFinal("Quotation", quotation.getIdQuotationPk());
            getMainController().setDataTableView();
            getMainController().setMessage(bundle.getString("deleteSuccess"), false);
            closeModal();
            //updateProductsOriginalVales();
        } catch (Exception e) {
            getMainController().setMessage(bundle.getString("deleteError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Eliminar cotización", e.getMessage(), sw.toString(), user.toString());
            closeModal();
            //e.printStackTrace();
        }
    }

    /*
     Método para anular la factura actual
     */
    public void invalidateInvoice() {
        try {
            boolean load = true;
            remission.setStatus(new Short("0"));
            invoice.setStatus(new Short("0"));
            invoice.setObs(obs.getText());
            SessionBD.persistenceCreate();
            RemissionModel rm = new RemissionModel();
            abs = new AbstractFacade();
            rm.updateIntermediate(invoice);
            rm.updateIntermediate(remission);
            selectedProducts = productDataTable.getItems();
            //Se comprueba si el pago fue a crédito
            if (remission.getIdInvoiceFk().getIdPaymentFk().getIdPaymentPk() == 2) {
                for (Remission rem : rm.findByIdSell(person.getIdPersonPk().toString())) {
                    creditRemission = rem;
                }
                double actualLeftAmount = creditRemission.getLeftAmount();
                actualLeftAmount -= tranzaction.getTransactionPrice();
                creditRemission.setLeftAmount(actualLeftAmount);
                rm.updateIntermediate(creditRemission);
            }
            for (ProductConverter prods : selectedProducts) {
                //Se trae el producto verdadero asociado a la transacción
                Product product1 = (Product)rm.findByIdInt("Product", prods.getIdProductPk());
                updateStock(prods.getAmount(), product1, load);
            }
            //Se actualiza el valor esperado en caja
            updateBoxExpectedMoney(user, totalInvoice, false);
            //Se ratifican los cambios de la transacción
            rm.executeCommit();
            //updateProductsOriginalVales();
            getMainController().setDataTableView();
            getMainController().setMessage(bundle.getString("invalidateSuccess"), false);
            closeModal();
        } catch (Exception e) {
            abs.executeRollback();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Anular factura", e.getMessage(), sw.toString(), user.toString());
            getMainController().setMessage(bundle.getString("invalidateError"), true);
            closeModal();
            //e.printStackTrace();
        }
    }

    /*
     Método para activar/desactivar movimiento actual
     */
    public void activeInactiveMovement() {
        try {
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            if (type != SERVICE_ORDER) {
                boolean load;
                if (remission.getStatus() == 1) {
                    remission.setStatus(new Short("2"));
                    load = true;
                } else {
                    remission.setStatus(new Short("1"));
                    load = false;
                }
                abs.updateIntermediate(remission);
                for (TransactionDetail dt : oldProducts) {
                    updateStock(dt.getAmount(), dt.getProduct(), load);
                }
            } else {
                if (remission.getStatus() == 1) {
                    remission.setStatus(new Short("2"));
                } else {
                    remission.setStatus(new Short("1"));
                }
                remission.setObs(serviceReference.getText());
                abs.updateIntermediate(remission);
            }
            abs.executeCommit();
            getMainController().setDataTableView();
            getMainController().setMessage(bundle.getString("updateSuccess"), false);
            closeModal();
            //updateProductsOriginalVales();
        } catch (Exception e) {
            abs.executeRollback();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Cambio de estado movimiento de inventario", e.getMessage(), sw.toString(), user.toString());
            getMainController().setMessage(bundle.getString("updateError"), true);
            closeModal();
            //e.printStackTrace();
        }
    }

    /*
     Método para borrar el dato actual
     */
    public void deleteAnnotation() {
        try {
            if (!annotationDataTable.getSelectionModel().isEmpty()) {
                abs = new AbstractFacade();
                Annotation annotation = (Annotation) annotationDataTable.getSelectionModel().getSelectedItem();
                abs.deleteIntFinal("Annotation", annotation.getIdAnnotationPk());
                remission.getAnnotationCollection().remove(annotation);
                annotationDataTableRefresh();
                if (annotationDataTable.getItems().isEmpty()) {
                    rmvAnnotationButton.setDisable(true);
                } else {
                    rmvAnnotationButton.setDisable(false);
                }
            }
        } catch (Exception e) {
            annotationDataTableRefresh();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Eliminar anotación", e.getMessage(), sw.toString(), user.toString());
            //e.printStackTrace();
        }
    }

    @FXML
    private void saveInitializeCredit(ActionEvent event) {
        try {
            if (!personExists) {
                person = new Person();
                //Se comprueba que no sea proveedor
                if(type==INITIALIZE_CREDIT_ORDER)
                    person.setPersonType("S");
            }
            person.setNit(idPerson.getText());
            person.setPersonFirstname(firstName.getText());
            person.setPersonLastname(lastName.getText());
            person.setPersonPhoneNo(phone.getText());
            person.setPersonAddress(addres.getText());
            person.setIdPlaceFk(initPlace);
            SessionBD.persistenceCreate();
            RemissionModel rm = new RemissionModel();
            if (personExists) {
                rm.updateIntermediate(person);
            } else {
                rm.saveIntermediate(person);
            }
            double actualLeftAmount = 0;
            boolean exists = false;
            if (invoiceExists) {
                for (Remission rem : rm.findByIdSell(person.getIdPersonPk().toString())) {
                    creditRemission = rem;
                }
                actualLeftAmount = creditRemission.getLeftAmount() - initializeCreditRem.getDeposit();
                exists = true;
            } else {
                initializeCreditRem = new Remission();
                Concept initializeCreditConcept = (Concept) abs.findByIdInt("Concept", 7);
                //Se comprueba que sea de cliente
                if(type == INITIALIZE_CREDIT)
                    initializeCreditRem.setIdClientFk(person);
                else
                    initializeCreditRem.setIdSupplierFk(person);
                initializeCreditRem.setIdConceptFk(initializeCreditConcept);    
                initializeCreditRem.setStatus(new Short("1"));
                if (rm.findByIdSell(person.getIdPersonPk().toString()).isEmpty()) {
                    creditRemission = new Remission();
                    Concept creditConcept;
                    //Se comprueba que sea de cliente
                    if(type == INITIALIZE_CREDIT){
                        creditConcept = (Concept) abs.findByIdInt("Concept", 3);
                        creditRemission.setIdClientFk(person);
                    }else {
                        creditConcept = (Concept) abs.findByIdInt("Concept", 12);
                        creditRemission.setIdSupplierFk(person);
                    }
                    
                    creditRemission.setIdConceptFk(creditConcept);
                    creditRemission.setStatus(new Short("1"));
                    creditRemission.setNoBuyReference(person.getIdPersonPk().toString());
                    creditRemission.setIdTurnFk(user);
                }
            }
            totalInvoice = Float.valueOf(getProduct.getText());
            initializeCreditRem.setDeposit(totalInvoice);
            actualLeftAmount += totalInvoice;
            creditRemission.setLeftAmount(actualLeftAmount);
            if (exists) {
                rm.updateIntermediate(creditRemission);
                rm.updateIntermediate(initializeCreditRem);
                mainController.setMessage(bundle.getString("updateSuccess"), false);
            } else {
                rm.saveIntermediate(creditRemission);
                rm.saveIntermediate(initializeCreditRem);
                Tranzaction remTran = new Tranzaction();
                remTran.setIdRemissionFk(initializeCreditRem);
                initializeCreditRem.addTrazaction(remTran);
                TranzactionModel tm = new TranzactionModel();
                tm.saveTranzaction(remTran, initializeCreditRem);
                mainController.setMessage(bundle.getString("createSuccess"), false);
            }
            rm.executeCommit();
            //Se comprueba que sea de cliente
            if(type == INITIALIZE_CREDIT)
                mainController.setModuleID("credit");
            else
                mainController.setModuleID("creditOrder");
            mainController.setDataTableView();
            closeModal();
        } catch (Exception e) {
            abs.executeRollback();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Registrar inicialización de crédito", e.getMessage(), sw.toString(), user.toString());
            mainController.setMessage(bundle.getString("createError"), true);
            closeModal();
            //e.printStackTrace();
        }

    }

    /*
     Método para facturar una remisión/cotización
     @param: Evento que ejecuta la acción ActionEvent onAction
     */
    @FXML
    private void invoiceRemission(ActionEvent event) throws IOException {
        closeModal();
        if (type != QUOTATION) {
            mainController.loadInvoiceView(remission, INVOICE, null, null);
        } else {
            if (quotation.getStatus() == 0) {
                selectedProducts = productDataTable.getItems();
                remission = new Remission();
                remission.setIdClientFk(person);
                remission.setStatus(new Short("1"));
                remission.setIdTurnFk(user);
                mainController.loadInvoiceView(remission, INVOICE, selectedProducts, quotation);
            } else {
                RemissionModel rm = new RemissionModel();
                remission = rm.findByIdInvoice(quotation.getIdInvoiceFk());
                mainController.loadInvoiceView(remission, INVOICE, null, null);
            }
        }
    }

    /*
     Método buscar una persona a través de un ListView
     @param: evento que ejecute la acción (onKeyPress)
     */
    @FXML
    private void searchAction(KeyEvent event) {
        TextField tfActual = (TextField) event.getSource();
        String moduleID = tfActual.getId();
        ObservableList data = FXCollections.observableArrayList();
        abs = new AbstractFacade();
        switch (moduleID) {
            case "idPerson": {
                String classToFind;
                if (type == INVOICE || type == SERVICE_ORDER || type == QUOTATION || type == INITIALIZE_CREDIT) {
                    classToFind = "Client";
                } else if (type == ORDER || type == INITIALIZE_CREDIT_ORDER) {
                    classToFind = "Supplier";
                } else {
                    classToFind = "Person";
                }
                Collection<Person> list = abs.findByLike(classToFind, idPerson.getText(), idPerson.getText(), idPerson.getText());
                for (Person person1 : list) {
                    data.add(person1);
                }
                personList.setVisible(true);
                personList.setItems(data);
                personList.getSelectionModel().selectFirst();
                break;
            }
            case "getProduct": {
                if (type != ADD_INVENTORY) {
                    if (placesList.isVisible()) {
                        placesList.setVisible(false);
                    }
                }
                vldMessage.setVisible(false);
                Collection<Product> list;
                if (type == INVOICE || type == SERVICE_ORDER || type == QUOTATION || type == ORDER) {
                    list = abs.findByLike("ServiceProduct", getProduct.getText(), getProduct.getText(), getProduct.getText());
                } else {
                    list = abs.findByLike("Product", getProduct.getText(), getProduct.getText(), getProduct.getText());
                }
                for (Product product1 : list) {
                    data.add(product1);
                }
                productList.setVisible(true);
                productList.setItems(data);
                productList.getSelectionModel().selectFirst();
                break;
            }
            default: {
                vldMessage.setVisible(false);
                Collection<Place> list = abs.findByLike1("Place", city.getText().toLowerCase());
                for (Place place1 : list) {
                    data.add(place1);
                }
                placesList.setVisible(true);
                placesList.setItems(data);
                placesList.getSelectionModel().selectFirst();
                break;
            }
        }

    }

    /*
     Método para cargar los datos del formulario de acuerdo al dato seleccionado 
     */
    private void loadSelectedData(String moduleID) {
        switch (moduleID) {
            case "idPerson":
            case "personList":
                if (personList.getSelectionModel().getSelectedItem() != null) {
                    person = (Person) personList.getSelectionModel().getSelectedItem();
                    idPerson.setText(person.getNit());
                    firstName.setText(person.getPersonFirstname());
                    lastName.setText(person.getPersonLastname());
                    phone.setText(person.getPersonPhoneNo());
                    addres.setText(person.getPersonAddress());
                    city.setText(person.getIdPlaceFk().getPlaceName() + " " + person.getIdPlaceFk().getIdPlaceFk().getPlaceName());
                    if (type != INITIALIZE_CREDIT && type != INITIALIZE_CREDIT_ORDER) {
                        email.setText(person.getEmail());
                    }
                    initPlace = person.getIdPlaceFk();
                    personExists = true;
                    validateNIT(null);
                    if (type == INITIALIZE_CREDIT || type == INITIALIZE_CREDIT_ORDER) {
                        RemissionModel rm = new RemissionModel();
                        if (!rm.findByIdSell(person.getIdPersonPk().toString()).isEmpty()) {
                            try {
                                initializeCreditRem = rm.findInitializeCredit(person.getIdPersonPk());
                                getProduct.setText(String.valueOf(Math.round(initializeCreditRem.getDeposit())));
                                getProduct.setDisable(false);
                                invoiceExists = true;
                            } catch (Exception e) {
                                getProduct.setDisable(true);
                                vldMessage.setVisible(true);
                                vldMessage.setText(bundle.getString("initializeCreditExist"));
                            }
                        }
                    }
                } else {
                    firstName.setText("");
                    lastName.setText("");
                    phone.setText("");
                    addres.setText("");
                    personExists = false;
                    vldMessage.setVisible(false);
                    if (type == INITIALIZE_CREDIT || type == INITIALIZE_CREDIT_ORDER) {
                        getProduct.setDisable(false);
                    }
                }
                personList.setVisible(false);
                break;
            case "getProduct":
            case "productList":
                if (productList.getSelectionModel().getSelectedItem() != null) {
                    Product slPrd = (Product) productList.getSelectionModel().getSelectedItem();
                    //Se crea un nuevo producto para asociarlo a la factura
                    ProductConverter tempProduct = new ProductConverter(slPrd.getIdProductPk(), slPrd.getSkuProduct(), slPrd.getActualPrice(), slPrd.getProductDescription());
                    tempProduct.setTaxesCollection(new ArrayList<Tax>());
                    tempProduct.setDiscountCollection(new ArrayList<Discount>());
                    //Se asocian los impuestos y descuentos, en caso de que los tenga y que no sea una compra
                    if(!slPrd.getProductTaxesCollection().isEmpty() && type != ORDER){
                        for(ProductTaxes pTx:slPrd.getProductTaxesCollection()){
                            tempProduct.getTaxesCollection().add(pTx.getTax());
                        }
                    }
                    if(!slPrd.getProductDiscountCollection().isEmpty()){
                        for(ProductDiscount pDc:slPrd.getProductDiscountCollection()){
                            tempProduct.getDiscountCollection().add(pDc.getDiscount());
                        }
                    }
                    ///Agregar producto a la lista de valores actuales y a la lista de valores antes de impuestos, en caso de que no esté agregado ya
                    int i=0;
                    ProductConverter p = new ProductConverter(tempProduct.getIdProductPk(), tempProduct.getActualPrice(), tempProduct.getProductDescription());
                    for(ProductConverter cnvrtr:productValuesBfTaxes){
                        if(cnvrtr.getIdProductPk().equals(tempProduct.getIdProductPk())){
                            i++;
                        }
                    }
                    if(i==0)
                        productValuesBfTaxes.add(p);
                    //
                    ///Se comprueba que la tabla no tenga asociado ya el producto
                    ObservableList<ProductConverter> currentItems = productDataTable.getItems();
                    for(ProductConverter pcnvrtr:currentItems){
                        if(pcnvrtr.getIdProductPk().equals(tempProduct.getIdProductPk())){
                            i=-1;
                            p = pcnvrtr;
                        }
                    }
                    //
                    if (i>=0) {
                        /// Agregar Descuentos en caso de que los tenga a la lista y si es factura o compra
                        if (type == INVOICE || type == QUOTATION || type == SERVICE_ORDER) {
                            float dcts = 0, pcts = 0;
                            //Se comprueba que sea una factura o una cotización para agregar el descuento
                            if (!tempProduct.getDiscountCollection().isEmpty() && type != ORDER) {
                                Collection<Discount> pDiscount = tempProduct.getDiscountCollection();
                                for (Discount actualDiscount : pDiscount) {
                                    dcts += actualDiscount.getDiscountPct();
                                    if (!discounts.contains(actualDiscount)) {
                                        discounts.add(actualDiscount);
                                    }
                                }
                                //Se agrega el descuento a la descripción del producto
                                tempProduct.setProductDescription(tempProduct.getProductDescription() + " (" + dcts + "% " + bundle.getString("lblDiscountInfo")
                                        + ")");
                            }//
                            /// Agregar Impuestos en caso de que los tenga a la lista
                            if (!tempProduct.getTaxesCollection().isEmpty()) {
                                Collection<Tax> pTaxes = tempProduct.getTaxesCollection();
                                for (Tax actualTaxes : pTaxes) {
                                    pcts = pcts + actualTaxes.getTaxPct();
                                    if (!taxes.contains(actualTaxes)) {
                                        taxes.add(actualTaxes);
                                    }
                                }
                                double newValue = tempProduct.getActualPrice() / ((pcts / 100) + 1);
                                //Se redondea a 2 cifras el valor escrito
                                newValue = (double)Math.round(newValue*100)/100;
                                tempProduct.setActualPrice(newValue);
                                tempProduct.setTotalPrice(format.format(newValue*tempProduct.getAmount()));
                            }
                        }
                        //
                        productDataTable.getItems().add(0, tempProduct);
                        if (type != SERVICE_ORDER && type != ADD_INVENTORY) {
                            updatePriceValues();
                        }
                        getProduct.setText("");
                    } else {
                        productDataTable.getSelectionModel().select(p);
                        ProductConverter tempProduct2 = (ProductConverter) productDataTable.getSelectionModel().getSelectedItem();
                        productDataTable.getItems().remove(tempProduct2);
                        int newAmount = tempProduct2.getAmount() + 1;
                        tempProduct2.setAmount(newAmount);
                        tempProduct2.setTotalPrice(format.format(tempProduct2.getActualPrice()*newAmount));
                        productDataTable.getItems().add(0, tempProduct2);
                        if (type != SERVICE_ORDER && type != ADD_INVENTORY) {
                            updatePriceValues();
                        }
                        refreshProductDataTable();
                        getProduct.setText("");
                    }
                } else {
                    vldMessage.setVisible(true);
                    vldMessage.setText(bundle.getString("notMatches"));
                }
                productList.setVisible(false);
                if (!productDataTable.getItems().isEmpty()) {
                    rmvProductButton.setDisable(false);
                } else {
                    rmvProductButton.setDisable(true);
                }
                break;
            default:
                if (placesList.getSelectionModel().getSelectedItem() != null) {
                    initPlace = (Place) placesList.getSelectionModel().getSelectedItem();
                    city.setText(initPlace.getPlaceName() + " " + initPlace.getIdPlaceFk().getPlaceName());
                } else {
                    vldMessage.setVisible(true);
                    vldMessage.setText(bundle.getString("notMatches"));
                }
                placesList.setVisible(false);
                break;
        }
        if (type != INITIALIZE_CREDIT && type != INITIALIZE_CREDIT_ORDER) {
            validForm();
        }
    }

    /*
     Método quitar un producto de los agregados en la lista
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void removeProductTableView(ActionEvent event) {
        ObservableList<ProductConverter> tempList = productDataTable.getItems();
        ProductConverter tempProduct;
        if (productDataTable.getSelectionModel().getSelectedItem() != null) {
            tempProduct = (ProductConverter) productDataTable.getSelectionModel().getSelectedItem();
        } else {
            productDataTable.getSelectionModel().selectFirst();
            tempProduct = (ProductConverter) productDataTable.getSelectionModel().getSelectedItem();
        }
        //Se saca el producto de las listas de valores especificados
        ProductConverter toRemove = new ProductConverter();
        for(ProductConverter cnvrt:productValuesBfTaxes){
            if(tempProduct.getIdProductPk().equals(cnvrt.getIdProductPk()))
                toRemove = cnvrt;
        }
        productValuesBfTaxes.remove(toRemove);
        productDataTable.getItems().remove(tempProduct);
        if (type != SERVICE_ORDER && type != ADD_INVENTORY) {
            Collection<Tax> removedTaxes = new ArrayList<>();
            Collection<Discount> removedDiscounts = new ArrayList<>();
            //Quita los descuentos asociados al producto en caso de que no estén en mas productos
            for (Discount disc : discounts) {
                int i = 0;
                for (ProductConverter prod : tempList) {
                    for (Discount prodDiscount : prod.getDiscountCollection()) {
                        if (prodDiscount.equals(disc)) {
                            i++;
                        }
                    }
                }
                //Se comprueba que el descuento no este asociado a ningún prodcuto ni a la factura
                if (i == 0 && !disc.equals(invoiceDiscount)) {
                    removedDiscounts.add(disc);
                }
            }
            discounts.removeAll(removedDiscounts);
            //
            //Quita los impuestos asociados al producto en caso de que no estén en mas productos
            for (Tax tax : taxes) {
                int i = 0;
                for (ProductConverter prod : tempList) {
                    for (Tax pordTax : prod.getTaxesCollection()) {
                        if (pordTax.equals(tax)) {
                            i++;
                        }
                    }
                }
                if (i == 0) {
                    removedTaxes.add(tax);
                }
            }
            taxes.removeAll(removedTaxes);
            //
            updatePriceValues();
        }
        if (!productDataTable.getItems().isEmpty()) {
            rmvProductButton.setDisable(false);
            if (type == SERVICE_ORDER) {
                invoiceButton.setDisable(false);
            }
        } else {
            rmvProductButton.setDisable(true);
            if (type == SERVICE_ORDER) {
                invoiceButton.setDisable(true);
            }
        }
        validForm();
    }

    /*
     Método para imprimir el invoice actual en PDF
     @param: evento que ejecuta la acción(onAction)
     */
    public void invoicePrint() {
        try {
            pdf = new PrintPDF();
            pdf.printInvoice(tranzaction, remission, quotation, productDataTable.getItems(), productValuesBfTaxes, type, bundle, true);
            //updateProductsOriginalVales();
            closeModal();
            mainController.setDataTableView();
        } catch (DocumentException | IOException | HeadlessException e) {
            closeModal();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Generar PDF factura", e.getMessage(), sw.toString(), user.toString());
            getMainController().setMessage(bundle.getString("pdfError"), true);
            //e.printStackTrace();
        } catch (PrinterException pe) {
            closeModal();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pe.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(pe.hashCode()), "Generar PDF factura", pe.getMessage(), sw.toString(), user.toString());
            getMainController().setMessage(bundle.getString("printerError"), true);
            //pe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     Método para imprimir el invoice actual en la impresora por defecto
     @param: evento que ejecuta la acción(onAction)
     */
    public void print() {
        try {
            pdf = new PrintPDF();
            pdf.printInvoice(tranzaction, remission, quotation, productDataTable.getItems(), productValuesBfTaxes, type, bundle, false);
            //updateProductsOriginalVales();
            closeModal();
            mainController.setDataTableView();
        } catch (DocumentException | IOException | HeadlessException e) {
            closeModal();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Imprimir factura", e.getMessage(), sw.toString(), user.toString());
            getMainController().setMessage(bundle.getString("printError"), true);
            //e.getMessage();
        } catch (PrinterException pe) {
            closeModal();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pe.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(pe.hashCode()), "Imprimir factura", pe.getMessage(), sw.toString(), user.toString());
            getMainController().setMessage(bundle.getString("printerError"), true);
            //pe.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     Método para mostrar las opciones del listado de acuerdo al texto ingresado
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void selectedAction(ActionEvent event) {
        TextField tfActual = (TextField) event.getSource();
        String moduleID = tfActual.getId();
        loadSelectedData(moduleID);
    }

    /*
     Método para cargar los datos del formulario de acuerdo al formulario seleccionado
     @param: evento que ejecute la acción (onMouseClicked)
     */
    @FXML
    private void selectedActionList(MouseEvent event) {
        ListView lvActual = (ListView) event.getSource();
        String moduleID = lvActual.getId();
        loadSelectedData(moduleID);
    }

    /*
     Método para cargar el modal de registro de nuevo producto
     @param: evento que ejecute la acción (onAction)
    */
    public void insertNewItemModal(ActionEvent event) throws IOException {
        String fxml="product";
        if(type==INVOICE || type == QUOTATION)
            fxml="service";
        Stage modal = new Stage(StageStyle.DECORATED);
        Locale es = new Locale("es", "ES");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/"+fxml+".fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
        Parent rootModal = (Parent) fxmlLoader.load();
        modal.setScene(new Scene(rootModal));
        modal.setTitle(bundle.getString("productTitle"));
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(
                ((Node) event.getSource()).getScene().getWindow());
        modal.show();
        ProductController produController = fxmlLoader.<ProductController>getController();
        produController.initializeValuesToInvoice();
        produController.setInvoiceController(this);
    }

    /*
     Método para cargar el modal de registro de nuevo producto
     @param: evento que ejecute la acción (onAction)
     */
    public void insertAnnotation(ActionEvent event) throws IOException {
        Stage modal = new Stage(StageStyle.DECORATED);
        Locale es = new Locale("es", "ES");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/annotation.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
        Parent rootModal = (Parent) fxmlLoader.load();
        modal.setScene(new Scene(rootModal));
        modal.setTitle(bundle.getString("annotationTitle"));
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(
                ((Node) event.getSource()).getScene().getWindow());
        modal.show();
        TaxController taxController = fxmlLoader.<TaxController>getController();
        taxController.setInvoiceController(this);
        taxController.initializeAnnotation(remission);
    }

    /*
     Método para agregar el producto recien creado a la tabla de productos
     @param: Producto a agregar a la tabla (Product newProduct)
     */
    public void productDataTableNewItem(Product newProduct) {
        //Se asocia el nuevo producto para la tabla
        ProductConverter product1 = new ProductConverter(newProduct.getIdProductPk(), newProduct.getProductReference(), newProduct.getActualPrice(), 
                newProduct.getProductDescription());
        product1.setDiscountCollection(new ArrayList<Discount>());
        product1.setTaxesCollection(new ArrayList<Tax>());
        if (type != ADD_INVENTORY && type != ORDER) {
            //Se asocian los impuestos y descuentos, en caso de que los tenga
            float pcts = 0;
            if(!newProduct.getProductTaxesCollection().isEmpty()){
                for(ProductTaxes pTx:newProduct.getProductTaxesCollection()){
                    product1.getTaxesCollection().add(pTx.getTax());
                    pcts = pcts + pTx.getTax().getTaxPct();
                    if(!taxes.contains(pTx.getTax())){
                        taxes.add(pTx.getTax());
                    }
                }
                double newValue = product1.getActualPrice() / ((pcts / 100) + 1);
                //Se redondea a 2 cifras el valor escrito
                newValue = (double)Math.round(newValue*100)/100;
                product1.setActualPrice(newValue);
                product1.setTotalPrice(format.format(newValue*product1.getAmount()));
            }
            if(!newProduct.getProductDiscountCollection().isEmpty()){
                for(ProductDiscount pDc:newProduct.getProductDiscountCollection()){
                    product1.getDiscountCollection().add(pDc.getDiscount());
                }
            }
        }
        //Se agrega a las listas, con el fin de mantener los valores reales
        if (!productValuesBfTaxes.contains(product1)) {
            ProductConverter p = new ProductConverter(product1.getIdProductPk(), product1.getActualPrice(), product1.getProductDescription());
            productValuesBfTaxes.add(p);
        }
        if (!productDataTable.getItems().contains(product1)) {
            //productDataTable.getItems().add(newProduct);
            productDataTable.getItems().add(0, product1);
        }
        vldMessage.setVisible(false);
        getProduct.setText("");
        updatePriceValues();
        validForm();
    }

    /*
     Método para redibujar la tabla de productos
     */
    public void refreshProductDataTable() {
        productDataTable.getColumns().clear();
        if(type == ORDER){
            productDataTable.getColumns()
                    .addAll(tcIdProduct, tcProductDescription, tcProductTax, tcProductPrice, tcAmount, tcProductTotalPrice);
        }else{
            productDataTable.getColumns()
                    .addAll(tcIdProduct, tcProductDescription, tcProductPrice, tcAmount, tcProductTotalPrice);
        }
    }


    /*
     Método para actualizar los valores del Inventario respecto del procedimiento realizado
     */
    public void updateStock(int amount, Product product, boolean load) throws Exception {
        ProductModel pdm = new ProductModel();
        Stock stock = (Stock) pdm.findByIdInt("Stock", product.getIdProductPk());
        if (!load) {
            stock.setAmount(stock.getAmount() - amount);
        } else {
            stock.setAmount(stock.getAmount() + amount);
        }
        product.refreshStockCollection(stock);
        pdm.updateStock(stock, product);
    }

    /*
     Método para esconder la lista de selección cuando se esta trabajando en otro campo
     */
    public void updatePriceValues() {
        if (type != MOVEMENT && type != ADD_INVENTORY) {
            //Se traen los productos en la tabla
            ObservableList<ProductConverter> tempList = productDataTable.getItems();
            totalInvoice = 0;
            subTotalInvoice = 0;
            double totalProducts = 0;
            lblTotalsGrid.getChildren().clear();
            for (ProductConverter product1 : tempList) {
                subTotalInvoice += product1.getActualPrice() * product1.getAmount();
                //Se cargan los valores reales del producto actual para los descuentos de factura
                for(ProductConverter convrt:productValuesBfTaxes){
                    if(product1.getIdProductPk().equals(convrt.getIdProductPk())){
                        totalProducts += convrt.getActualPrice() * product1.getAmount();
                    }
                }
            }
            //Se verifica que el producto tenga asociados impuestos o descuentos
            if (!discounts.isEmpty() || !taxes.isEmpty()) {
                double tempSubtotal = 0, tempDescs = 0;
                double tempTax;
                double tempTaxValue;
                int i = 1;
                if (!discounts.isEmpty()) {
                    lblSubTotalTitle.setText(bundle.getString("lblInvoiceSubTotal"));
                    lblSubTotal.setText(bundle.getString("moneyNotation") + format.format(subTotalInvoice));
                    lblTotalsGrid.add(lblSubTotalTitle, 0, 0);
                    lblTotalsGrid.add(lblSubTotal, 1, 0);
                    for (Discount disc : discounts) {
                        tempTax = disc.getDiscountPct() / 100;
                        //Se compara con el descuento de la factura, si lo tiene
                        if (invoiceDiscount != null && invoiceDiscount.equals(disc)) {
                            tempTaxValue = totalProducts * tempTax;
                        } else {
                            tempTaxValue = 0;
                        }
                        for (ProductConverter prod : tempList) {
                            for (Discount prodDisc : prod.getDiscountCollection()) {
                                if (prodDisc.equals(disc)) {
                                    //A fin de que el descuento lo haga sobre el valor sin impuestos
                                    for(ProductConverter convrt:productValuesBfTaxes){
                                        if(prod.getIdProductPk().equals(convrt.getIdProductPk())){
                                            tempTaxValue += (convrt.getActualPrice() * prod.getAmount()) * tempTax;
                                        }
                                    }
                                    /*int index = productValuesBfTaxes.indexOf(prod);
                                    ProductConverter tempProduct = productValuesBfTaxes.get(index);
                                    tempTaxValue += (tempProduct.getActualPrice() * prod.getAmount()) * tempTax;*/
                                }
                            }
                        }
                        //Se comprueba que el descuento tenga asociado al menos un producto
                        tempSubtotal += tempTaxValue;
                        Label tempTaxTitle = new Label(bundle.getString("lblInvoiceDiscount") + "(" + disc.getPercentaje() + ")");
                        tempTaxTitle.getStyleClass().add("totalValueslabel");
                        tempTaxTitle.setAlignment(Pos.CENTER_RIGHT);
                        tempTaxTitle.setPrefWidth(164);
                        Label tempTaxLabel = new Label(bundle.getString("moneyNotation") + format.format(tempTaxValue));
                        tempTaxLabel.getStyleClass().add("totalValueslabel");
                        tempTaxLabel.setAlignment(Pos.CENTER_LEFT);
                        tempTaxLabel.setPrefWidth(136);
                        lblTotalsGrid.add(tempTaxTitle, 0, i);
                        lblTotalsGrid.add(tempTaxLabel, 1, i);
                        i++;
                    }
                    lblSubTotal.setText(bundle.getString("moneyNotation") + format.format(subTotalInvoice));
                    totalInvoice = Math.round(subTotalInvoice - tempSubtotal);
                    tempDescs = tempSubtotal;
                    lblTotalTitle.setText(bundle.getString("lblInvoiceTotal"));
                    lblTotalTitle.getStyleClass().add("totalLabel");
                    lblTotal.setText(bundle.getString("moneyNotation") + format.format(totalInvoice));
                    lblTotal.getStyleClass().add("totalLabel");
                    lblTotalsGrid.add(lblTotalTitle, 0, i);
                    lblTotalsGrid.add(lblTotal, 1, i);
                }
                if (!taxes.isEmpty()) {
                    //Se comprueba que no hayan descuentos en los totales
                    boolean hasDiscounts = false;
                    if (i > 1) {
                        hasDiscounts = true;
                        tempSubtotal = 0;
                        subTotalInvoice -= tempDescs;
                        lblSubTotalTitle.setText(bundle.getString("lblInvoiceTotalValue"));
                        lblTotalTitle.setText(bundle.getString("lblInvoiceSubTotal"));
                        lblTotalTitle.getStyleClass().remove("totalLabel");
                        lblTotalTitle.getStyleClass().add("totalValueslabel");
                        lblTotal.getStyleClass().remove("totalLabel");
                        lblTotal.getStyleClass().add("totalValueslabel");
                        lblTotal.setText(bundle.getString("moneyNotation") + format.format(subTotalInvoice));
                        i++;
                    } else {
                        lblSubTotalTitle.setText(bundle.getString("lblInvoiceSubTotal"));
                        lblSubTotal.setText(bundle.getString("moneyNotation") + format.format(subTotalInvoice));
                        lblTotalsGrid.add(lblSubTotalTitle, 0, 0);
                        lblTotalsGrid.add(lblSubTotal, 1, 0);
                    }
                    for (Tax tax : taxes) {
                        //
                        tempTaxValue = 0;
                        tempTax = tax.getTaxPct() / 100;
                        for (ProductConverter prod : tempList) {
                            for (Tax pordTax : prod.getTaxesCollection()) {
                                if (pordTax.equals(tax)) {
                                    tempTaxValue = tempTaxValue + (prod.getActualPrice() * prod.getAmount()) * tempTax;
                                }
                            }
                        }
                        tempSubtotal += tempTaxValue;
                        Label tempTaxTitle = new Label(tax.toString());
                        tempTaxTitle.getStyleClass().add("totalValueslabel");
                        tempTaxTitle.setAlignment(Pos.CENTER_RIGHT);
                        tempTaxTitle.setPrefWidth(164);
                        Label tempTaxLabel = new Label(bundle.getString("moneyNotation") + format.format(tempTaxValue));
                        tempTaxLabel.getStyleClass().add("totalValueslabel");
                        tempTaxLabel.setAlignment(Pos.CENTER_LEFT);
                        tempTaxLabel.setPrefWidth(136);
                        lblTotalsGrid.add(tempTaxTitle, 0, i);
                        lblTotalsGrid.add(tempTaxLabel, 1, i);
                        i++;
                    }
                    totalInvoice = Math.round(tempSubtotal + subTotalInvoice);
                    if (hasDiscounts) {
                        Label tempTotalsTitle = new Label(bundle.getString("lblInvoiceTotal"));
                        Label tempTotalsValue = new Label(bundle.getString("moneyNotation") + format.format(totalInvoice));
                        tempTotalsTitle.getStyleClass().add("totalLabel");
                        tempTotalsValue.getStyleClass().add("totalLabel");
                        tempTotalsTitle.setAlignment(Pos.CENTER_RIGHT);
                        tempTotalsValue.setAlignment(Pos.CENTER_LEFT);
                        tempTotalsTitle.setPrefWidth(164);
                        tempTotalsValue.setPrefWidth(136);
                        lblTotalsGrid.add(tempTotalsTitle, 0, i);
                        lblTotalsGrid.add(tempTotalsValue, 1, i);
                    } else {
                        lblTotalTitle.setText(bundle.getString("lblInvoiceTotal"));
                        lblTotalTitle.getStyleClass().add("totalLabel");
                        lblTotal.setText(bundle.getString("moneyNotation") + format.format(totalInvoice));
                        lblTotal.getStyleClass().add("totalLabel");
                        lblTotalsGrid.add(lblTotalTitle, 0, i);
                        lblTotalsGrid.add(lblTotal, 1, i);
                    }
                }
            } else {
                //Se comprueba si existe algún descuento de factura
                int i = 0;
                totalInvoice = subTotalInvoice;
                if (invoiceDiscount != null) {
                    double desc = subTotalInvoice * (invoiceDiscount.getDiscountPct() / 100);
                    totalInvoice = subTotalInvoice - desc;
                    //Se adicionan los label del subtotal
                    Label tempSubtotalTitle = new Label(bundle.getString("lblInvoiceSubTotal"));
                    tempSubtotalTitle.getStyleClass().add("totalValueslabel");
                    tempSubtotalTitle.setAlignment(Pos.CENTER_RIGHT);
                    tempSubtotalTitle.setPrefWidth(164);
                    Label tempValueLabel = new Label(bundle.getString("moneyNotation") + format.format(subTotalInvoice));
                    tempValueLabel.getStyleClass().add("totalValueslabel");
                    tempValueLabel.setAlignment(Pos.CENTER_LEFT);
                    tempValueLabel.setPrefWidth(136);
                    lblTotalsGrid.add(tempSubtotalTitle, 0, i);
                    lblTotalsGrid.add(tempValueLabel, 1, i);
                    i++;
                    //Se adicionan los label del descuento
                    Label tempTaxTitle = new Label(bundle.getString("lblInvoiceDiscount") + "(" + invoiceDiscount.getPercentaje() + ")");
                    tempTaxTitle.getStyleClass().add("totalValueslabel");
                    tempTaxTitle.setAlignment(Pos.CENTER_RIGHT);
                    tempTaxTitle.setPrefWidth(164);
                    Label tempTaxLabel = new Label(bundle.getString("moneyNotation") + format.format(desc));
                    tempTaxLabel.getStyleClass().add("totalValueslabel");
                    tempTaxLabel.setAlignment(Pos.CENTER_LEFT);
                    tempTaxLabel.setPrefWidth(136);
                    lblTotalsGrid.add(tempTaxTitle, 0, i);
                    lblTotalsGrid.add(tempTaxLabel, 1, i);
                    i++;
                }
                totalInvoice = Math.round(totalInvoice);
                lblTotalTitle.setText(bundle.getString("lblInvoiceTotal"));
                lblTotalTitle.getStyleClass().add("totalLabel");
                lblTotal.setText(bundle.getString("moneyNotation") + format.format(totalInvoice));
                lblTotal.getStyleClass().add("totalLabel");
                lblTotalsGrid.add(lblTotalTitle, 0, i);
                lblTotalsGrid.add(lblTotal, 1, i);
            }
        }
    }

    /*
     Método para cambiar los valores de acuerdo al tipo de pago seleccionado
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void updatePaymentType(ActionEvent event) {
        ComboBoxChoices selectedPayment = payType.getSelectionModel().getSelectedItem();
        //Se especifica el cambio respecto del movimiento
        if(type==INVOICE){
            switch (selectedPayment.getItemValue()) {
            case "2":
                isCredit = true;
                isCreditCard = false;
                if (entityReference.isVisible()) {
                    entityReference.setVisible(false);
                    aprovNumber.setVisible(false);
                }
                break;
            case "3":
                isCredit = false;
                isCreditCard = true;
                entityReference.setVisible(true);
                aprovNumber.setVisible(true);
                break;
            default:
                isCredit = false;
                isCreditCard = false;
                if (entityReference.isVisible()) {
                    entityReference.setVisible(false);
                    aprovNumber.setVisible(false);
                }
                break;
            }
            validForm();
        }else{
            switch (selectedPayment.getItemValue()) {
                case "1":
                    isCredit = true;
                    orderPayment.setSelected(false);
                    orderPayment.setDisable(true);
                    break;
                default:
                    isCredit = false;
                    orderPayment.setSelected(true);
                    orderPayment.setDisable(false);
                    break;
            }
        }
    }

    /*
     Método para cambiar los valores de acuerdo al descuento seleccionado
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void updateDiscountOpt(ActionEvent event) {
        ComboBoxChoices selectedOpt = discountOpt.getSelectionModel().getSelectedItem();
        //Se recorre la lista de productos asociados para definir si tienen el descuento a remover
        boolean discount = false;
        Collection<ProductConverter> tempList = productDataTable.getItems();
        for (ProductConverter tempProduct : tempList) {
            for (Discount prodDisc : tempProduct.getDiscountCollection()) {
                if (prodDisc.equals(invoiceDiscount)) {
                    discount = true;
                }
            }
        }
        if (!discount && discounts.contains(invoiceDiscount)) {
            discounts.remove(invoiceDiscount);
        }
        switch (selectedOpt.getItemValue()) {
            case "-1":
                invoiceDiscount = null;
                break;
            default:
                invoiceDiscount = (Discount) abs.findByIdInt("Discount", Integer.parseInt(selectedOpt.getItemValue()));
                if (!discounts.contains(invoiceDiscount)) {
                    discounts.add(invoiceDiscount);
                }
        }
        updatePriceValues();
    }

    /*
     Método para cambiar los valores de acuerdo al tipo de movimiento seleccionado
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void updateConceptType(ActionEvent event) {
        ComboBoxChoices selectedType = conceptType.getSelectionModel().getSelectedItem();
        movementType = Integer.valueOf(selectedType.getItemValue());
        if (movementType == 4) {
            personType = "C";
        } else {
            personType = "S";
        }
    }

    /*
     Método para cargar el diálogo de confirmación 
     @param: evento que ejecute la acción (onAction)
     */
    public void showConfirmationDialog(ActionEvent event) throws IOException {
        int invoiceType;
        String bundleMessage;
        if (type == INVOICE) {
            invoiceType = 1;
            bundleMessage = "invalidateMessage";
        } else if (type == SERVICE_ORDER) {
            invoiceType = 0;
            bundleMessage = "completeServiceMessage";
        } else if (type == QUOTATION) {
            invoiceType = 8;
            bundleMessage = "deleteMessage";
        } else {
            Button actualButton = (Button) event.getSource();
            //Si el movimiento es garantía y esta ejecutando la acción de cambiar el estado del movimiento
            if (remission.getIdConceptFk().getIdConceptPk() == 4 && actualButton.getId().equals("stateButton")) {
                invoiceType = 5;
                if (remission.getStatus() == 2) {
                    bundleMessage = "activateMovement";
                } else {
                    bundleMessage = "deactivateMovement";
                }
            } else {
                invoiceType = 2;
                bundleMessage = "deleteMessage";
            }
        }
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
        dialogController.setInvoiceController(this);
        dialogController.initializeValues(bundleMessage, invoiceType);
    }

    /*
     Método para actualizar el datatable 
     */
    public void annotationDataTableRefresh() {
        RemissionModel rm = new RemissionModel();
        ObservableList<String> init = FXCollections.observableArrayList();
        annotationDataTable.setItems(init);
        annotationDataTable.getColumns().removeAll(tcAnnotationDate, tcAnnotationObs);
        annotationDataTable.getColumns().addAll(tcAnnotationDate, tcAnnotationObs);
        annotationDataTable.getItems().addAll(rm.findAllByIdRemission("Annotation", remission.getIdRemissionPk()));
        if (annotationDataTable.getItems().isEmpty()) {
            rmvAnnotationButton.setDisable(true);
        } else {
            rmvAnnotationButton.setDisable(false);
        }
    }

    /*
     Método para actualizar todos los productos a sus valores originales, luego de 
     registrar o actualizar un movimiento. 
     
    public void updateProductsOriginalVales() throws Exception {
        for (Product product : actualProductValues) {
            for (Product pro : selectedProducts) {
                if (product.getIdProductPk().equals(pro.getIdProductPk())) {
                    pro.setActualPrice(product.getActualPrice());
                    pro.setCantidadSeleccionada(1);
                    pro.setProductDescription(product.getProductDescription());
                    if (invoiceExists) {
                        //Se comprueba que sea un movimiento al que apliquen impuestos y/o descuentos
                        if (type == INVOICE || type == ORDER || type == QUOTATION) {
                            //Se revisa si contiene algún impuesto temporal, en el caso en el que se pueda actualizar la factura
                            if (product.getProductTaxesCollection() != null) {
                                pro.setProductTaxesCollection(product.getProductTaxesCollection());
                            }
                            //Se revisa si contiene algún descuento temporal para los movimientos en que aplica
                            if(type == INVOICE || type == QUOTATION){
                                if (product.getProductDiscountCollection() != null) {
                                    pro.setProductDiscountCollection(product.getProductDiscountCollection());
                                }
                            }
                        }
                    }
                    abs.updateFinal(pro);
                }
            }
        }
    }

    
     Método para actualizar un producto a sus valores originales, luego de 
     ser removido de un movimiento que se está actualizando. 
    
    public void resetProductOriginalVales(Product pro) throws Exception {
        for (Product product : actualProductValues) {
            if (product.getIdProductPk().compareTo(pro.getIdProductPk()) == 0) {
                pro.setActualPrice(product.getActualPrice());
                pro.setCantidadSeleccionada(1);
                pro.setProductDescription(product.getProductDescription());
                if (invoiceExists) {
                    //Se comprueba que sea un movimiento al que apliquen impuestos y/o descuentos
                    if (type == INVOICE || type == ORDER || type == QUOTATION) {
                        //Se revisa si contiene algún impuesto temporal, en el caso en el que se pueda actualizar la factura
                        if (product.getProductTaxesCollection() != null) {
                            pro.setProductTaxesCollection(product.getProductTaxesCollection());
                        }
                        //Se revisa si contiene algún descuento temporal para los movimientos en que aplica
                        if(type == INVOICE || type == QUOTATION){
                            if (product.getProductDiscountCollection() != null) {
                                pro.setProductDiscountCollection(product.getProductDiscountCollection());
                            }
                        }
                    }
                }
                abs.updateIntermediate(pro);
            }
        }
    } */

    /*
     Método para ocultar las vistas de buscar personas, municipios y productos
     cuadno se presiona una tecla sobre
     @param: Evento que ejecuta la acción "onPress" (KeyEvent arg)
     */
    public void hideListsViewOnPress(KeyEvent arg) {
        hideListsView();
    }

    /*
     Método para ocultar las vistas de buscar personas, municipios y productos
     cuadno se presiona una tecla sobre
     @param: Evento que ejecuta la acción "onPress" (KeyEvent arg)
     */
    public void hideListsViewOnClick(MouseEvent arg) {
        hideListsView();
    }

    /*
     Método para ocultar las vistas de buscar personas, municipios y productos
     */
    public void hideListsView() {
        if (placesList.isVisible()) {
            placesList.setVisible(false);
        }
        if (personList.isVisible()) {
            personList.setVisible(false);
        }
        if (productList != null) {
            if (productList.isVisible()) {
                productList.setVisible(false);
            }
        }
    }

    /*
     Método para ocultar la vista de buscar productos
     */
    public void hideProductListView(MouseEvent event) {
        if (productList.isVisible()) {
            productList.setVisible(false);
        }
    }

    /*
     Método para esconder la lista de selección cuando se esta trabajando en otro campo
     @param: evento que ejecute la acción (onKeyPress)
    
     @FXML
     private void hidePersonListViewOnPress(KeyEvent event) {
     if (personList.isVisible()) {
     personList.setVisible(false);
     }
     }
    
     /*
     Método para esconder la lista de selección cuando se esta trabajando en otro campo
     @param: evento que ejecute la acción (onKeyPress)
    
     @FXML
     private void hidePersonListViewOnClick(MouseEvent event) {
     if (personList.isVisible()) {
     personList.setVisible(false);
     }
     }*/

    /*
     Método para validar el campo reference para el formulario de remisión
     */
    public void validateRF(KeyEvent arg) {
        TextArea t = (TextArea) arg.getSource();
        int length = t.getText().length();
        notNullTextArea(serviceReference, vldInfo, length, bundle, bundle.getString("serviceOrderEspTltp"));
        validForm();
        hideListsView();
    }

    /*
     Método para validar el campo NIT
     */
    public void validateNIT(KeyEvent arg) {
        //En caso que sea nulo (Seleccionado con el mouse)
        TextField t;
        if (arg != null) {
            t = (TextField) arg.getSource();
        } else {
            t = idPerson;
        }
        int length = t.getText().length();
        if (notNull(idPerson, vldMessage, length, bundle, bundle.getString("notNull"))) {
            maxLenght(idPerson, vldMessage, length, 50, bundle);
        }
        if (type == INITIALIZE_CREDIT  || type == INITIALIZE_CREDIT_ORDER) {
            validateInitializeCreditForm();
        } else {
            validForm();
        }
    }

    /*
     Método para validar el campo firstName
     */
    public void validateFN(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        if (notNull(firstName, vldMessage, length, bundle, bundle.getString("notNull"))) {
            maxLenght(firstName, vldMessage, length, 96, bundle);
        }
        if (type == INITIALIZE_CREDIT) {
            validateInitializeCreditForm();
        } else {
            validForm();
        }
    }

    /*
     Método para validar el campo lastName
     */
    public void validateLN(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        maxLenght(lastName, vldMessage, length, 96, bundle);
        if (type == INITIALIZE_CREDIT) {
            validateInitializeCreditForm();
        } else {
            validForm();
        }
    }

    /*
     Método para validar el campo address
     */
    public void validateAD(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        maxLenght(addres, vldMessage, length, 96, bundle);
        if (type == INITIALIZE_CREDIT) {
            validateInitializeCreditForm();
        } else {
            validForm();
        }
    }

    /*
     Método para validar el campo phone
     */
    public void validatePN(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        maxLenght(phone, vldMessage, length, 96, bundle);
        if (type == INITIALIZE_CREDIT) {
            validateInitializeCreditForm();
        } else {
            validForm();
        }
    }

    /*
     Método para validar el campo email
     */
    public void validateEM(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        maxLenght(email, vldMessage, length, 76, bundle);
        validForm();
    }

    /*
     Método para validar el campo No. de aprobación
     */
    public void validateAN(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        if (notNull(aprobationNo, vldMessage, length, bundle, t.getPromptText())) {
            maxLenght(aprobationNo, vldMessage, length, 50, bundle);
        }
        validForm();
    }

    /*
     Método para validar el campo de valor de crédito a inicializar
     */
    public void validateCI(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        if (isNumeric(getProduct, vldMessage, t.getText(), bundle)) {
            maxLenght(getProduct, vldMessage, t.getLength(), 23, bundle);
        }
        validateInitializeCreditForm();
    }

    /*
     Método para validar que el formulario ya esta listo para ser enviado
     */
    void validateInitializeCreditForm() {
        if (!vldMessage.isVisible() && !getProduct.getText().isEmpty() && !idPerson.getText().isEmpty()) {
            saveButton.setDisable(false);
        } else {
            saveButton.setDisable(true);
        }
    }

    /*
     Método para validar que el formulario ya esta listo para ser enviado
     */
    void validForm() {
        if (type != SERVICE_ORDER && type != ADD_INVENTORY && type != INVOICE) {
            if (!vldMessage.isVisible() && idPerson != null && !productDataTable.getItems().isEmpty()
                    && !idPerson.getText().isEmpty()) {
                saveButton.setDisable(false);
            } else {
                saveButton.setDisable(true);
            }
        } else if (type == INVOICE) {
            if (isCreditCard) {
                if (!vldMessage.isVisible() && idPerson != null && !productDataTable.getItems().isEmpty()
                        && !idPerson.getText().isEmpty() && !aprobationNo.getText().isEmpty()) {
                    saveButton.setDisable(false);
                } else {
                    saveButton.setDisable(true);
                }
            } else {
                if (!vldMessage.isVisible() && idPerson != null && !productDataTable.getItems().isEmpty()
                        && !idPerson.getText().isEmpty()) {
                    saveButton.setDisable(false);
                } else {
                    saveButton.setDisable(true);
                }
            }
        } else if (type == ADD_INVENTORY) {
            if (!vldMessage.isVisible() && !productDataTable.getItems().isEmpty()) {
                saveButton.setDisable(false);
            } else {
                saveButton.setDisable(true);
            }
        } else {
            if (!vldMessage.isVisible() && idPerson.getText() != null && !vldInfo.isVisible()
                    && !serviceReference.getText().isEmpty()) {
                saveButton.setDisable(false);
            } else {
                saveButton.setDisable(true);
            }
        }
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
        if (increment) {
            totalAmount = turn.getExpectedAmount() + amount;
        } else {
            totalAmount = turn.getExpectedAmount() - amount;
        }
        turn.setExpectedAmount(totalAmount);
        abs.updateIntermediate(turn);
    }

    /*
     Método para ocultar o mostrar la ayuda a través de la acción de un Botón
     @param: Acción que ejecuta la acción: Action onPress
     */
    @FXML
    private void showHideTooltip(ActionEvent event) {
        showHideTooltips(tlpHelp, helpButton);
    }

    /*
     Método para cerrar el modal iniciado sin guardar ninguna cambio, desde el boto cancelar
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void closeButtonAction(ActionEvent event
    ) {
        /*if (type == INVOICE && mainController.productDataTable == null) {
        mainController.setDataTableView();
        }*/
        closeModal();
    }

    private void closeModal() {
        mainController.showHideMask(false);
        Scene scene = title.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

}

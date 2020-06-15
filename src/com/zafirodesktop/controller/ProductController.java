/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.itextpdf.text.DocumentException;
import com.zafirodesktop.entity.MovimientosProducto;
import com.zafirodesktop.entity.Product;
import com.zafirodesktop.entity.ProductCategory;
import com.zafirodesktop.entity.ProductDiscount;
import com.zafirodesktop.entity.ProductTaxes;
import com.zafirodesktop.entity.ProductTaxesPK;
import com.zafirodesktop.entity.TransactionDetail;
import com.zafirodesktop.entity.Turn;
import com.zafirodesktop.model.ProductModel;
import com.zafirodesktop.model.ReportsModel;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.util.ComboBoxChoices;
import com.zafirodesktop.util.FormValidation;
import com.zafirodesktop.util.LogActions;
import com.zafirodesktop.util.PrintPDF;
import java.awt.HeadlessException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Digitall
 */
public class ProductController extends FormValidation implements Initializable {

    /*
     Especificación de los componentes asociados en el FXML
     */
    @FXML
    private AnchorPane mainPane;
    @FXML
    private HBox buttonsBox;
    @FXML
    private HBox idBox;
    @FXML
    private HBox categoriesBox;
    @FXML
    private HBox taxexBox;
    @FXML
    private HBox discountBox;
    @FXML
    private Label title;
    @FXML
    private TextField productID;
    @FXML
    private TextField reference;
    @FXML
    private TextArea description;
    @FXML
    private TextField price;
    @FXML
    private TextField minimumStock;
    @FXML
    private ComboBox<ComboBoxChoices> departments;
    @FXML
    private TableView categories;
    @FXML
    private TableColumn item;
    @FXML
    private TableView taxes;
    @FXML
    private TableColumn taxItem;
    @FXML
    private TableView discounts;
    @FXML
    private TableColumn discountItem;
    @FXML
    private Label vldId;
    @FXML
    private Label vldRefrence;
    @FXML
    private Label vldPrice;
    @FXML
    private Label vldDescription;
    @FXML
    private Label vldMinimumStock;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button itemCategroy;
    @FXML
    private Button itemTax;
    @FXML
    private Button itemDiscount;
    @FXML
    private Button codebarButton;
    @FXML
    private Button movementButton;
    @FXML
    private Button ordersButton;
    @FXML
    private Button helpButton;
    @FXML
    private Tooltip tlpHelp;
    @FXML
    private Stage stage;

    /*
     Objetos y variables del controlador 
     */
    private boolean exists;
    private boolean alreadyInstanciated;
    private short type;
    private ProductModel pdm;
    public  Product product;
    private Turn user;
    private MainController mainController;
    private InvoiceController invoiceController;
    private ResourceBundle bundle;
    private Collection<ProductTaxes> productTaxes;

    public MainController getMainController() {
        return mainController;
    }

    public void setInvoiceController(InvoiceController invoiceController) {
        this.invoiceController = invoiceController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        productID.setPromptText(bundle.getString("lblProductId"));
        reference.setPromptText(bundle.getString("lblProductReference"));
        //Se comprueba que el campo exista en el formulario 
        if(price!=null){
            price.setPromptText(bundle.getString("lblProductPrice"));
        }
        if (minimumStock != null) {
            minimumStock.setPromptText(bundle.getString("lblMinimumStock"));
        }
        description.setPromptText(bundle.getString("lblProductDescription"));
        Image img = new Image("/com/zafirodesktop/ui/img/ico/pointer_black.png");
        ImageView imgvw = new ImageView(img);
        imgvw.setFitHeight(15);
        imgvw.setFitWidth(15);
        tlpHelp.setGraphic(imgvw);
        initializeValues();
    }

    /*
     Método para inicializar los valores del formulario
     */
    public void initializeValues() {
        String typeTitle;
        saveButton.setDisable(true);
        if (itemCategroy != null) {
            itemCategroy.setDisable(true);
        }
        itemTax.setDisable(true);
        buttonsBox.getChildren().remove(deleteButton);
        if (buttonsBox.getChildren().contains(movementButton)) {
            buttonsBox.getChildren().remove(movementButton);
            buttonsBox.getChildren().remove(ordersButton);
            idBox.getChildren().remove(codebarButton);
            //codebarButton.setVisible(false);
            typeTitle = bundle.getString("productTitle");
            type = 1;
        } else if (itemCategroy == null) {
            typeTitle = bundle.getString("departmentTitle");
            type = 2;
        } else {
            typeTitle = bundle.getString("serviceTitle");
            type = 0;
        }
        title.setText(typeTitle);
        //Se inicializa el valor del combo de departamentos si hace parte del formulario
        if(departments!=null){
            pdm = new ProductModel();
            Collection<Product> departmentsList = pdm.findAll("Department");
            ObservableList<ComboBoxChoices> comboList = FXCollections.observableArrayList();
            //Se agrega una entrada inicial básica para cuando no le quieren asociar ningún departamento
            comboList.add(new ComboBoxChoices("-1", bundle.getString("lblProductDepartment")));
            for (Product department : departmentsList) {
                comboList.add(new ComboBoxChoices(department.getIdProductPk().toString(), department.getProductReference()));
            }
            departments.getItems().addAll(comboList);
            departments.getSelectionModel().selectFirst();
            alreadyInstanciated = false;
        }
    }

    /*
        Método para inicializar los valores de impuestos cuando se crea desde un movimiento
    */
    public void initializeValuesToInvoice(){
        //Se activan los datatable y botón de adición de impuestos
        taxexBox.setVisible(true);
        taxes.setPlaceholder(new Label(bundle.getString("emptyTaxesDataTable")));
        itemTax.setVisible(true);
        taxItem = new TableColumn(bundle.getString("asociatedTaxesDatatable"));
        taxItem.prefWidthProperty().bind(taxes.widthProperty().divide(1));
        taxItem.setCellValueFactory(new PropertyValueFactory<ProductTaxes, String>("taxInformation"));
        taxes.getColumns().addAll(taxItem);
        itemTax.setDisable(false);
    }
    
    /*
     Método para inicializar los valores del formulario, en caso de que ya existan datos
     */
    public void initializeValuesExists(Product actualProduct, Turn sessionUser) {
        user = sessionUser;
        product = actualProduct;
        SessionBD.persistenceCreate();
        pdm = new ProductModel();
        exists = true;
        type = product.getType();
        //Se inicializan los valores de títulos de acuerdo al tipo de ingreso
        if (type == 1) {
            title.setText(bundle.getString("productTitleUpdate"));
        } else if (type == 2) {
            title.setText(bundle.getString("departmentTitleUpdate"));
        } else {
            title.setText(bundle.getString("serviceTitleUpdate"));
        }
        productID.setText(product.getSkuProduct());
        reference.setText(product.getProductReference());
        description.setText(product.getProductDescription());
        //Se comprueba que el campo exista en el formulario 
        if(price!=null){
            int productPrice = product.getActualPrice().intValue();
            price.setText(String.valueOf(productPrice));
        }
        if (minimumStock != null) {
            minimumStock.setText(String.valueOf(product.getMinimunStock()));
        }
        //Se comprueba que la tabla de categorias haga parte del formulario
        if (categoriesBox != null) {
            categoriesBox.setVisible(true);
            itemCategroy.setVisible(true);
        }
        //Se activan los datatable y botón de adición de impuestos y descuentos
        taxexBox.setVisible(true);
        itemTax.setVisible(true);
        discountBox.setVisible(true);
        itemDiscount.setVisible(true);
        //De acuerdo al tipo de usuario que acceda se permitirán ciertas acciones
        if (user != null && user.getIdUserFk().getCreatedOn() > 0) {
            saveButton.setDisable(false);
            if(itemCategroy!=null){
                itemCategroy.setDisable(false);
            }
            itemTax.setDisable(false);
            if (type == 1) {
                buttonsBox.getChildren().add(1, movementButton);
                buttonsBox.getChildren().add(2, ordersButton);
                buttonsBox.getChildren().add(3, deleteButton);
            } else {
                buttonsBox.getChildren().add(1, deleteButton);
            }
        } else {
            buttonsBox.getChildren().remove(saveButton);
            productID.setEditable(false);
            reference.setEditable(false);
            description.setEditable(false);
            //Se comprueba que el campo exista en el formulario 
            if(price!=null){
                price.setEditable(false);
            }
        }
        //En caso de que el campo departamento este activo
        if(departments!=null&&product.getIdProductFk()!=null){
            ComboBoxChoices selectedChoice = new ComboBoxChoices(product.getIdProductFk().getIdProductPk().toString(), product.getIdProductFk().getProductReference());
            departments.getSelectionModel().select(selectedChoice);
        }
        //Si el tipo pertenece a producto se habilitan las opciones propias de producto
        if (type == 1) {
            codebarButton.setVisible(true);
            if (!idBox.getChildren().contains(codebarButton)) {
                idBox.getChildren().add(0, codebarButton);
            }
        }
        if (categoriesBox != null) {
            item = new TableColumn(bundle.getString("asociatedItemsCategoryDatatable"));
            item.prefWidthProperty().bind(categories.widthProperty().divide(1));
            item.setCellValueFactory(new PropertyValueFactory<ProductCategory, String>("itemName"));
            categories.getColumns().addAll(item);
            categories.getItems().addAll(pdm.findByIdProduct("ProductCategory", product.getIdProductPk()));
        }
        //Se carga el datatable de taxes
        taxItem = new TableColumn(bundle.getString("asociatedTaxesDatatable"));
        taxItem.prefWidthProperty().bind(taxes.widthProperty().divide(1));
        taxItem.setCellValueFactory(new PropertyValueFactory<ProductTaxes, String>("taxInformation"));
        taxes.getColumns().addAll(taxItem);
        taxes.getItems().addAll(pdm.findByIdProduct("ProductTaxes", product.getIdProductPk()));
        //Se carga el datatable de discounts
        discountItem = new TableColumn(bundle.getString("asociatedDiscoubtsDatatable"));
        discountItem.prefWidthProperty().bind(taxes.widthProperty().divide(1));
        discountItem.setCellValueFactory(new PropertyValueFactory<ProductDiscount, String>("DiscountInformation"));
        discounts.getColumns().addAll(discountItem);
        discounts.getItems().addAll(pdm.findByIdProduct("ProductDiscount", product.getIdProductPk()));
    }

    /*
     Método para actualizar el datatable de categories
     */
    public void itemDataTableRefresh() {
        ObservableList<String> init = FXCollections.observableArrayList();
        categories.setItems(init);
        categories.getColumns().removeAll(item);
        categories.getColumns().addAll(item);
        categories.getItems().addAll(pdm.findByIdProduct("ProductCategory", product.getIdProductPk()));
    }

    /*
     Método para actualizar el datatable de taxes
     */
    public void taxesDataTableRefresh() {
        ObservableList<String> init = FXCollections.observableArrayList();
        taxes.setItems(init);
        taxes.getColumns().removeAll(taxItem);
        taxes.getColumns().addAll(taxItem);
        //Se cargan los valores registrados, de acuerdo al origen de la petición
        if(invoiceController!=null)
            taxes.getItems().addAll(product.getProductTaxesCollection());
        else
            taxes.getItems().addAll(pdm.findByIdProduct("ProductTaxes", product.getIdProductPk()));
    }
    
    /*
     Método para actualizar el datatable de discounts
     */
    public void discountsDataTableRefresh() {
        ObservableList<String> init = FXCollections.observableArrayList();
        discounts.setItems(init);
        discounts.getColumns().removeAll(discountItem);
        discounts.getColumns().addAll(discountItem);
        discounts.getItems().addAll(pdm.findByIdProduct("ProductDiscount", product.getIdProductPk()));
    }

    /*
     Método para insertar o actualizar los datos del formulario
     @param: acción que genera la petición(onAction)
     */
    @FXML
    private void saveProduct(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            pdm = new ProductModel();
            if (!exists && !alreadyInstanciated) {
                product = new Product();
                product.setType(type);
            }
            product.setProductReference(reference.getText());
            //Si el campo existe en el formulario se toma el valor, en caso contrario
            //se especifica un valor por defecto
            if(price!=null)
                product.setActualPrice(Double.parseDouble(price.getText()));
            else
                product.setActualPrice(0.0);
            if (minimumStock != null)
                product.setMinimunStock(Integer.parseInt(minimumStock.getText()));
            else
                product.setMinimunStock(0);
            //En caso de que el campo departamento este activo
            if(departments!=null){
                ComboBoxChoices selectedDepartment = departments.getSelectionModel().getSelectedItem();
                //Se verifica que se haya seleccionado algún departamento
                if(Integer.parseInt(selectedDepartment.getItemValue())>0){
                    Product department = (Product)pdm.findByIdInt("Product", Integer.parseInt(selectedDepartment.getItemValue()));
                    product.setIdProductFk(department);
                    //department.getProductCollection().add(product);
                }else{
                    product.setIdProductFk(null);
                }
            }
            product.setProductDescription(description.getText());
            product.setSkuProduct(productID.getText());
            if (exists) {
                pdm.updateFinal(product);
                getMainController().setMessage(bundle.getString("updateSuccess"), false);
                closeModal();
            } else {
                //Se comprueba que se hayan asociado impuestos al producto
                if(invoiceController!=null && product.getProductTaxesCollection()!=null){
                    productTaxes = new ArrayList<>();
                    productTaxes.addAll(product.getProductTaxesCollection());
                    product.getProductTaxesCollection().clear();
                }
                pdm.saveFinal(product);
                //Se guardan los impuestos asociados en caso de que existan
                if(productTaxes!=null){
                    for(ProductTaxes prodTax:productTaxes){
                        ProductTaxesPK pTaxesPK = new ProductTaxesPK(product.getIdProductPk(), prodTax.getTax().getIdTaxPk());
                        prodTax.setProductTaxesPK(pTaxesPK);
                        prodTax.setProduct(product);
                        prodTax.setId(product.getIdProductPk().toString() + prodTax.getTax().getIdTaxPk());
                        pdm.saveFinal(prodTax);
                    }
                }
                if (mainController != null) {
                    getMainController().setMessage(bundle.getString("createSuccess"), false);
                    initializeValuesExists(product, mainController.getSessionUser());
                } else {
                    closeModal();
                    invoiceController.productDataTableNewItem(product);
                }
            }
            if (mainController != null) {
                getMainController().setDataTableView();
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar producto", e.getMessage(), sw.toString(), user.toString());
            closeModal();
            if (mainController != null) {
                getMainController().setMessage(bundle.getString("deleteError"), true);
            }
            //e.printStackTrace();
        }
    }

    /*
     Método para borrar el dato actual
     */
    @FXML
    public void deleteProduct() {
        try {
            SessionBD.persistenceCreate();
            pdm = new ProductModel();
            if (pdm.findByIdProduct("TransactionDetail", product.getIdProductPk()).isEmpty()) {
                pdm.deleteFromStock(product.getIdProductPk());
                pdm.deleteIntFinal("Product", product.getIdProductPk());
                getMainController().setDataTableView();
                getMainController().setMessage(bundle.getString("deleteSuccess"), false);
            } else {
                getMainController().setMessage(bundle.getString("deleteProductError"), true);
            }
            closeModal();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Eliminar producto", e.getMessage(), sw.toString(), user.toString());
            closeModal();
            getMainController().setMessage(bundle.getString("deleteError"), true);
            //e.printStackTrace();
        }
    }

    /*
     Método para cargar el modal de registro de nuevo item
     @param: evento que ejecute la acción (onAction)
     */
    public void insertNewItemModal(ActionEvent event) throws IOException {
        Button tempButton = (Button) event.getSource();
        String id = tempButton.getId();
        Stage modal = new Stage(StageStyle.DECORATED);
        Locale es = new Locale("es", "ES");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/" + id + ".fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
        Parent rootModal = (Parent) fxmlLoader.load();
        modal.setScene(new Scene(rootModal));
        modal.setTitle("Item");
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(
                ((Node) event.getSource()).getScene().getWindow());
        modal.show();
        switch (id) {
            case "itemCategory":
                ProductCategoryController itemController = fxmlLoader.<ProductCategoryController>getController();
                itemController.initializeValues(product);
                itemController.setProductController(this);
                break;
            case "itemDiscount":
                ProductDiscountController discountController = fxmlLoader.<ProductDiscountController>getController();
                discountController.initializeValues(product);
                discountController.setProductController(this);
                break;
            default:
                //Se cargan los valores del producto a crear en caso de que sea desde un movimiento
                if(invoiceController!=null){
                    //Se verifica que el producto ya haya sido inicializado
                    if(product==null){
                        pdm = new ProductModel();
                        //Se verifica que ya existan productos
                        try{
                            product = new Product(pdm.findLast()+1);
                        }catch(Exception e){
                            product = new Product(1);
                        }
                        alreadyInstanciated = true;
                    }
                    product.setType(type);
                    product.setProductReference(reference.getText());
                    if(!price.getText().isEmpty())
                        product.setActualPrice(Double.parseDouble(price.getText()));
                    if (minimumStock != null){
                        if(!minimumStock.getText().isEmpty())
                            product.setMinimunStock(Integer.parseInt(minimumStock.getText()));
                    }else
                        product.setMinimunStock(0);
                    product.setProductDescription(description.getText());
                    product.setSkuProduct(productID.getText());
                    //Se verifica que tenga inicializada la colección de descuentos
                    if(product.getProductTaxesCollection()==null)
                        product.setProductTaxesCollection(new ArrayList<ProductTaxes>());
                }
                ProductTaxesController taxesController = fxmlLoader.<ProductTaxesController>getController();
                taxesController.initializeValues(product);
                taxesController.setProductController(this);
                taxesController.setInvoiceController(invoiceController);
                break;
        }
    }

    /*
     Método para cargar el modal de registro de nuevo item
     @param: evento que ejecute la acción (onMouseClicked)
     */
    public void loadSelectedItemModal(MouseEvent event) throws IOException {
        try {
            if(categories==null){
                categories = new TableView();
            }
            if (categories.getSelectionModel().getSelectedItem() != null
                    || taxes.getSelectionModel().getSelectedItem() != null
                    || discounts.getSelectionModel().getSelectedItem() != null) {
                TableView tempTable = (TableView) event.getSource();
                String id = tempTable.getId();
                String fxml;
                switch (id) {
                    case "categories":
                        fxml = "itemCategory";
                        break;
                    case "discounts":
                        fxml = "itemDiscount";
                        break;
                    default:
                        fxml = "itemTax";
                        break;
                }
                Stage modal = new Stage(StageStyle.DECORATED);
                Locale es = new Locale("es", "ES");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/" + fxml + ".fxml"));
                fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
                Parent rootModal = (Parent) fxmlLoader.load();
                modal.setScene(new Scene(rootModal));
                modal.setTitle("Item");
                modal.initModality(Modality.APPLICATION_MODAL);
                modal.initOwner(
                        ((Node) event.getSource()).getScene().getWindow());
                modal.show();
                switch (id){
                    case "categories":
                        ProductCategory actualItem = (ProductCategory) categories.getSelectionModel().getSelectedItem();
                        ProductCategoryController itemController = fxmlLoader.<ProductCategoryController>getController();
                        itemController.setProductController(this);
                        itemController.initializeValues(product);
                        itemController.initializeValuesExists(actualItem);
                        break;
                    case "discounts":
                        ProductDiscount actualDiscount = (ProductDiscount) discounts.getSelectionModel().getSelectedItem();
                        ProductDiscountController discountController = fxmlLoader.<ProductDiscountController>getController();
                        discountController.initializeValues(product);
                        discountController.setProductController(this);
                        discountController.initializeValuesExists(actualDiscount);
                        break;
                    default:
                        ProductTaxes actualTax = (ProductTaxes) taxes.getSelectionModel().getSelectedItem();
                        ProductTaxesController taxesController = fxmlLoader.<ProductTaxesController>getController();
                        taxesController.initializeValues(product);
                        taxesController.setProductController(this);
                        taxesController.setInvoiceController(invoiceController);
                        taxesController.initializeValuesExists(actualTax);
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Cargar formulario de creación", e.getMessage(), sw.toString(), user.toString());
            //e.printStackTrace();
        }
    }

    /*
     Método para crear el código de barras en PDF
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void generateBarcode(ActionEvent event) {
        try {
            PrintPDF pdf = new PrintPDF();
            pdf.printBarcode(product.getSkuProduct(), product.getProductReference(), product.getActualPrice());
            closeModal();
        } catch (Exception e) {
            mainController.setMessage(bundle.getString("barcodeGenerateError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Generar código de barras", e.getMessage(), sw.toString(), user.toString());
            closeModal();
            //e.printStackTrace();
        }
    }

    /*
     Método para cargar el reporte de movimientos en PDF
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    public void movementReport(ActionEvent event) throws DocumentException, IOException {
        try {
            PrintPDF pdf = new PrintPDF();
            String tipe = "productMovements";
            String reportTilte = bundle.getString("productMovementsReportTitle")+" " + product.getProductReference();
            String[] columnNames = {bundle.getString("lblReportMovementId"), bundle.getString("lblReportDate"), bundle.getString("lblReportObs"), bundle.getString("lblReportMovementConcept"), bundle.getString("lblReportAmout")};
            ReportsModel rm = new ReportsModel();
            Collection<MovimientosProducto> movimientos = rm.productMovement(product.getIdProductPk());
            int[] columnWidths = new int[]{10, 10, 50, 20, 10};
            pdf.printReport(tipe, reportTilte, columnNames, columnWidths, movimientos, null, null, bundle);
        } catch (IOException | HeadlessException e) {
            getMainController().setMessage(bundle.getString("pdfError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Generar reporte de movimientos", e.getMessage(), sw.toString(), user.toString());
        }
        closeModal();
    }
    
    /*
     Método para cargar el reporte de movimientos en PDF
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    public void ordersReport(ActionEvent event) throws DocumentException, IOException {
        try {
            PrintPDF pdf = new PrintPDF();
            String tipe = "productOrders";
            String reportTilte = bundle.getString("productOrdersReportTitle")+" " + product.getProductReference();
            String[] columnNames = {bundle.getString("lblReportMovementId"), bundle.getString("lblReportDate"), bundle.getString("lblReportObs"), bundle.getString("lblReportSupplier"), bundle.getString("lblReportSinglePrice"), bundle.getString("lblReportAmout")};
            ReportsModel rm = new ReportsModel();
            
            Collection<TransactionDetail> orders = rm.productOrders(product.getIdProductPk());
            int[] columnWidths = new int[]{10, 10, 40, 20, 10, 10};
            pdf.printReport(tipe, reportTilte, columnNames, columnWidths, orders, null, null, bundle);
        } catch (IOException | HeadlessException e) {
            getMainController().setMessage(bundle.getString("pdfError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Generar reporte de compras", e.getMessage(), sw.toString(), user.toString());
        }
        closeModal();
    }

    /*
     Método para cargar el diálogo de confirmación 
     @param: evento que ejecute la acción (onAction)
     */
    public void showConfirmationDialog(ActionEvent event) throws IOException {
        int invoiceType = 3;
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
        dialogController.setProductController(this);
        dialogController.initializeValues(bundleMessage, invoiceType);
    }

    /*
     Método para validar el campo id
     */
    public void validateID(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        if (notNull(productID, vldId, length, bundle, bundle.getString("productIdTltp"))) {
            if (nameExists(productID, vldId, "Product", productID.getText(), bundle)) {
                maxLenght(productID, vldId, length, 50, bundle);
            }
        }
        if(type!=2)
            validForm();
        else
            departmentFormValidation();
    }

    /*
     Método para validar el campo reference
     */
    public void validateRF(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        if (notNull(reference, vldRefrence, length, bundle, bundle.getString("productReferenceTltp"))) {
            maxLenght(reference, vldRefrence, length, 50, bundle);
        }
        if(type!=2)
            validForm();
        else
            departmentFormValidation();
    }

    /*
     Método para validar el campo price
     */
    public void validatePR(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        String text = t.getText();
        if (notNull(price, vldPrice, length, bundle, bundle.getString("productPriceTltp"))) {
            if (isNumeric(price, vldPrice, text, bundle)) {
                maxLenght(price, vldPrice, length, 23, bundle);
            }
        }
        validForm();
    }

    /*
     Método para validar el campo minimumStock
     */
    public void validateMS(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        String text = t.getText();
        if (notNull(minimumStock, vldMinimumStock, length, bundle, bundle.getString("minimumStockTltp"))) {
            if (isNumeric(minimumStock, vldMinimumStock, text, bundle)) {
                maxLenght(minimumStock, vldMinimumStock, length, 4, bundle);
            }
        }
        validForm();
    }

    /*
     Método para validar el campo description
     */
    public void validatePD(KeyEvent arg) {
        TextArea t = (TextArea) arg.getSource();
        int length = t.getText().length();
        if (notNullTextArea(description, vldDescription, length, bundle, bundle.getString("productDescriptionTltp"))) {
            maxLenghtTextArea(description, vldDescription, length, 100, bundle);
        }
        if(type!=2)
            validForm();
        else
            departmentFormValidation();
    }

    /*
     Método para validar que el formulario ya esta listo para ser enviado
     */
    void validForm() {
        boolean minStock = false;
        if (minimumStock != null) {
            if (!vldMinimumStock.isVisible() && !minimumStock.getText().isEmpty()) {
                minStock = true;
            }
        } else {
            minStock = true;
        }
        if (!vldId.isVisible() && !vldRefrence.isVisible() && !vldPrice.isVisible()
                && !vldDescription.isVisible() && !productID.getText().isEmpty()
                && !reference.getText().isEmpty() && !price.getText().isEmpty()
                && !description.getText().isEmpty() && minStock) {
            saveButton.setDisable(false);
            if (!idBox.getChildren().contains(codebarButton) && type > 0 && exists) {
                idBox.getChildren().add(0, codebarButton);
            }
        } else {
            saveButton.setDisable(true);
            idBox.getChildren().remove(codebarButton);
        }
    }
    
    /*
     Método para validar que el formulario de departamento
     */
    void departmentFormValidation() {
        if(!vldId.isVisible() && !vldRefrence.isVisible() && !vldDescription.isVisible() 
                && !productID.getText().isEmpty() && !reference.getText().isEmpty() 
                && !description.getText().isEmpty()){
            saveButton.setDisable(false);
        }else{
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
        if (mainController != null) {
            mainController.showHideMask(false);
        }
        Scene scene = mainPane.getScene();
        stage = (Stage) scene.getWindow();
        stage.close();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.controller;

import com.zafirodesktop.entity.Product;
import com.zafirodesktop.entity.ProductTaxes;
import com.zafirodesktop.entity.ProductTaxesPK;
import com.zafirodesktop.entity.Tax;
import com.zafirodesktop.model.ProductModel;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.util.ComboBoxChoices;
import com.zafirodesktop.util.FormValidation;
import com.zafirodesktop.util.LogActions;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
public class ProductTaxesController extends FormValidation implements Initializable{

    @FXML
    private AnchorPane mainPane;
    @FXML
    private HBox buttonsBox;
    @FXML
    private Label title;
    @FXML
    private ComboBox<ComboBoxChoices> items;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button helpButton;
    @FXML
    private Tooltip tlpHelp;
    
    private boolean exists;
    private ResourceBundle bundle;
    private ProductController productController;
    private InvoiceController invoiceController;
    private ProductModel pdm;
    private Product actualProduct;
    private ProductTaxes actualProductTax;
    private ProductTaxesPK actualProductTaxPX;
    private Tax actualTax;

    public ProductController getProductController() {
        return productController;
    }

    public void setProductController(ProductController productController) {
        this.productController = productController;
    }

    public InvoiceController getInvoiceController() {
        return invoiceController;
    }

    public void setInvoiceController(InvoiceController invoiceController) {
        this.invoiceController = invoiceController;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle=rb;
        title.setText(bundle.getString("asociatedTaxesDatatable"));
        Image img = new Image("/com/zafirodesktop/ui/img/ico/pointer_black.png");
        ImageView imgvw = new ImageView(img);
        imgvw.setFitHeight(15);
        imgvw.setFitWidth(15);
        tlpHelp.setGraphic(imgvw);
        items.setPromptText(bundle.getString("lblDefaultTax"));
    }
    
    /*
     Método para inicializar los valores del formulario
     */
    public void initializeValues(Product product) {
        pdm = new ProductModel();
        actualProduct = product;
        Collection<Tax> categoriesList = pdm.findAll("Tax");
        ObservableList<ComboBoxChoices> comboList = FXCollections.observableArrayList();
        for (Tax tax : categoriesList) {
            comboList.add(new ComboBoxChoices(tax.getIdTaxPk().toString(), tax.toString()));
        }
        items.getItems().addAll(comboList);
        items.getSelectionModel().selectFirst();
        saveButton.setDisable(true);
        buttonsBox.getChildren().remove(deleteButton);
    }
    
    /*
     Método para inicializar los valores del formulario, en caso de que ya existan datos
     @param: usuario a editar(Users)
     */
    public void initializeValuesExists(ProductTaxes actualItem) {
        actualProductTax = actualItem;
        actualProductTaxPX = actualItem.getProductTaxesPK();
        actualTax = actualItem.getTax();
        exists = true;
        ComboBoxChoices selectedItem = new ComboBoxChoices(actualTax.getIdTaxPk().toString(), actualTax.toString());
        items.getSelectionModel().select(selectedItem);
        buttonsBox.getChildren().add(1, deleteButton);
    }
    
    /*
     Método para insertar o actualizar item
     @param: acción que genera la petición(onAction)
     */
    @FXML
    private void insertItem(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            pdm = new ProductModel();
            ComboBoxChoices itemChoiced = (ComboBoxChoices) items.getSelectionModel().getSelectedItem();
            actualTax = (Tax) pdm.findByIdInt("Tax", Integer.valueOf(itemChoiced.getItemValue()));
            if (exists) {
                actualProduct.removeProductTaxes(actualProductTax);
                pdm.deleteTax(actualProductTax.getId(), actualProduct);
            }
            actualProductTaxPX = new ProductTaxesPK();
            actualProductTax = new ProductTaxes();
            actualProductTaxPX.setIdProductFk(actualProduct.getIdProductPk());
            actualProductTaxPX.setIdTaxFk(actualTax.getIdTaxPk());
            actualProductTax.setProductTaxesPK(actualProductTaxPX);
            actualProductTax.setProduct(actualProduct);
            actualProductTax.setTax(actualTax);
            actualProductTax.setId(actualProduct.getIdProductPk().toString()+actualTax.getIdTaxPk());
            actualProduct.addProductTaxes(actualProductTax);
            if(invoiceController==null){
                pdm.saveTax(actualProductTax, actualProduct);
            }
            productController.taxesDataTableRefresh();
            closeModal();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar impuesto de producto", e.getMessage(), sw.toString(), "");
            closeModal();
            //e.printStackTrace();
        }
    }
    
    /*
     Método para borrar el dato actual
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void deleteItem(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            pdm = new ProductModel();
            actualProduct.removeProductTaxes(actualProductTax);
            //Se verifica que no se haga desde una solicitud de un movimiento
            if(invoiceController==null)
                pdm.deleteTax(actualProductTax.getId(), actualProduct);
            productController.taxesDataTableRefresh();
            closeModal();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Eliminar impuesto de producto", e.getMessage(), sw.toString(), "");
            closeModal();
            //e.printStackTrace();
        }
    }
    
    /*
     Método para cargar el modal de registro de nuevo impuesto
     @param: evento que ejecute la acción (onAction)
     */
    public void insertNewItemModal(ActionEvent event) throws IOException {
        Stage modal = new Stage(StageStyle.DECORATED);
        Locale es = new Locale("es", "ES");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/tax.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
        Parent rootModal = (Parent) fxmlLoader.load();
        modal.setScene(new Scene(rootModal));
        modal.setTitle("Item");
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(
                ((Node) event.getSource()).getScene().getWindow());
        modal.show();
        TaxController taxController = fxmlLoader.<TaxController>getController();
        taxController.initializeTax();
        taxController.setPtController(this);
    }
    
    /*
     Método para refrescar el listado de Impuestos disponibles 
    */
    public void refreshDataList(){
        items.getItems().clear();
        Collection<Tax> categoriesList = pdm.findAll("Tax");
        ObservableList<ComboBoxChoices> comboList = FXCollections.observableArrayList();
        for (Tax tax : categoriesList) {
            comboList.add(new ComboBoxChoices(tax.getIdTaxPk().toString(), tax.getTaxName()));
        }
        items.getItems().addAll(comboList);
        items.getSelectionModel().selectLast();
    }
    
    /*
     Método para recargar los valores del combobox
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void itemSelected(MouseEvent event) {
        if(items.getSelectionModel().getSelectedItem()!=null){
            saveButton.setDisable(false);
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
        Scene scene = mainPane.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }
    
}

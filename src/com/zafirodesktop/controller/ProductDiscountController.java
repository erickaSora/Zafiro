/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.zafirodesktop.entity.Discount;
import com.zafirodesktop.entity.Product;
import com.zafirodesktop.entity.ProductDiscount;
import com.zafirodesktop.entity.ProductDiscountPK;
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
public class ProductDiscountController extends FormValidation implements Initializable{

    //FXML variables
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
    
    //Controller variables
    private boolean exists;
    private ResourceBundle bundle;
    private ProductController productController;
    private ProductModel pdm;
    private Product actualProduct;
    
    private ProductDiscount actualProductDiscount;
    private ProductDiscountPK actualProductDiscountPK;
    private Discount actualDiscount;

    public void setProductController(ProductController productController) {
        this.productController = productController;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle=rb;
        Image img = new Image("/com/zafirodesktop/ui/img/ico/pointer_black.png");
        ImageView imgvw = new ImageView(img);
        imgvw.setFitHeight(15);
        imgvw.setFitWidth(15);
        tlpHelp.setGraphic(imgvw);
    }
        
    /*
     Método para inicializar los valores del formulario
     */
    public void initializeValues(Product product) {
        title.setText(bundle.getString("asociatedDiscoubtsDatatable"));
        items.setPromptText(bundle.getString("lblDefaultDiscount"));
        pdm = new ProductModel();
        actualProduct = product;
        Collection<Discount> categoriesList = pdm.findAll("Discount");
        ObservableList<ComboBoxChoices> comboList = FXCollections.observableArrayList();
        for (Discount dis : categoriesList) {
            comboList.add(new ComboBoxChoices(dis.getIdDiscountPk().toString(), dis.getDiscountDescrption()+" | "+dis.getPercentaje()));
        }
        items.getItems().addAll(comboList);
        items.getSelectionModel().selectFirst();
        saveButton.setDisable(true);
        buttonsBox.getChildren().remove(deleteButton);
    }
    
    /*
     Método para inicializar los valores del formulario, en caso de que ya existan datos
     @param: productDiscount a editar(actualItem)
     */
    public void initializeValuesExists(ProductDiscount actualItem) {
        actualProductDiscount = actualItem;
        actualProductDiscountPK = actualItem.getProductDiscountPK();
        actualDiscount = actualItem.getDiscount();
        exists = true;
        ComboBoxChoices selectedItem = new ComboBoxChoices(actualDiscount.getIdDiscountPk().toString(), actualDiscount.getDiscountDescrption()+" | "+actualDiscount.getPercentaje());
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
            if (exists) {
                actualProduct.removeProductDiscount(actualProductDiscount);
                pdm.deleteDiscount(actualProductDiscount.getId(), actualProduct);
            }
            actualDiscount = (Discount) pdm.findByIdInt("Discount", Integer.valueOf(itemChoiced.getItemValue()));
            actualProductDiscountPK = new ProductDiscountPK();
            actualProductDiscount = new ProductDiscount();
            actualProductDiscountPK.setIdProductFk(actualProduct.getIdProductPk());
            actualProductDiscountPK.setIdDiscountFk(actualDiscount.getIdDiscountPk());
            actualProductDiscount.setProductDiscountPK(actualProductDiscountPK);
            actualProductDiscount.setProduct(actualProduct);
            actualProductDiscount.setDiscount(actualDiscount);
            actualProductDiscount.setId(actualProduct.getIdProductPk().toString()+actualDiscount.getIdDiscountPk());
            actualProduct.addProductDiscount(actualProductDiscount);
            pdm.saveDiscount(actualProductDiscount, actualProduct);
            productController.discountsDataTableRefresh();
            closeModal();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar descuento de producto", e.getMessage(), sw.toString(), "");
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
            actualProduct.removeProductDiscount(actualProductDiscount);
            pdm.deleteDiscount(actualProductDiscount.getId(), actualProduct);
            productController.discountsDataTableRefresh();
            closeModal();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Eliminar descuento de producto", e.getMessage(), sw.toString(), "");
            closeModal();
            //e.printStackTrace();
        }
    }
    
    /*
     Método para cargar el modal de registro de un nuevo descuento
     @param: evento que ejecute la acción (onAction)
     */
    public void insertNewItemModal(ActionEvent event) throws IOException {
        Stage modal = new Stage(StageStyle.DECORATED);
        Locale es = new Locale("es", "ES");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/discount.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
        Parent rootModal = (Parent) fxmlLoader.load();
        modal.setScene(new Scene(rootModal));
        modal.setTitle("Item");
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(
                ((Node) event.getSource()).getScene().getWindow());
        modal.show();
        DiscountsController discountController = fxmlLoader.<DiscountsController>getController();
        discountController.initializeValues();
        discountController.setPdController(this);
    }
    
    /*
     Método para refrescar el listado de Descuentos disponibles 
    */
    public void refreshDataList(){
        items.getItems().clear();
        Collection<Discount> categoriesList = pdm.findAll("Discount");
        ObservableList<ComboBoxChoices> comboList = FXCollections.observableArrayList();
        for (Discount dis : categoriesList) {
            comboList.add(new ComboBoxChoices(dis.getIdDiscountPk().toString(), dis.getDiscountDescrption()+" | "+dis.getPercentaje()));
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

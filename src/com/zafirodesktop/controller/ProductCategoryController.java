/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.zafirodesktop.entity.Category;
import com.zafirodesktop.entity.ItemCategory;
import com.zafirodesktop.entity.Product;
import com.zafirodesktop.entity.ProductCategory;
import com.zafirodesktop.entity.ProductCategoryPK;
import com.zafirodesktop.model.CategoryModel;
import com.zafirodesktop.model.ProductModel;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.util.ComboBoxChoices;
import com.zafirodesktop.util.FormValidation;
import com.zafirodesktop.util.LogActions;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import javafx.stage.Stage;

/**
 *
 * @author Digitall
 */
public class ProductCategoryController extends FormValidation implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private HBox buttonsBox;
    @FXML
    private Label title;
    @FXML
    private ComboBox<ComboBoxChoices> categories;
    @FXML
    private ComboBox<ComboBoxChoices> items;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button helpButton;
    @FXML
    private Tooltip tlpHelp;
    @FXML
    private Stage stage;

    private boolean exists;
    private ProductController productController;
    private ProductModel pdm;
    private CategoryModel cm;
    private ProductCategory productCategory;
    private ProductCategoryPK productCategoryPK;
    private Category category;
    private ItemCategory itemCategory;
    private Product product;
    private ResourceBundle bundle;

    public ProductController getProductController() {
        return productController;
    }

    public void setProductController(ProductController productController) {
        this.productController = productController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        Image img = new Image("/com/zafirodesktop/ui/img/ico/pointer_black.png");
        ImageView imgvw = new ImageView(img);
        imgvw.setFitHeight(15);
        imgvw.setFitWidth(15);
        tlpHelp.setGraphic(imgvw);
        categories.setPromptText(bundle.getString("lblDefaultCategory"));
        items.setPromptText(bundle.getString("lblDefaultSubCategory"));
    }

    /*
     Método para inicializar los valores del formulario
     */
    public void initializeValues(Product actualProduct) {
        SessionBD.persistenceCreate();
        pdm = new ProductModel();
        product = actualProduct;
        Collection<Category> categoriesList = pdm.findAll("Category");
        ObservableList<ComboBoxChoices> comboList = FXCollections.observableArrayList();
        for (Category cat : categoriesList) {
            comboList.add(new ComboBoxChoices(cat.getIdCategoryPk().toString(), cat.getCategoryName()));
        }
        categories.getItems().addAll(comboList);
        categories.getSelectionModel().selectFirst();
        saveButton.setDisable(true);
        buttonsBox.getChildren().remove(deleteButton);
    }

    /*
     Método para inicializar los valores del formulario, en caso de que ya existan datos
     @param: usuario a editar(Users)
     */
    public void initializeValuesExists(ProductCategory actualItem) {
        productCategory = actualItem;
        productCategoryPK = actualItem.getProductCategoryPK();
        category = actualItem.getItemCategory().getIdCategoryFk();
        itemCategory = actualItem.getItemCategory();
        exists = true;
        /*Collection<ItemCategory> itemsList = cm.findByIdCategory(category);
         ObservableList<ComboBoxChoices> comboList = FXCollections.observableArrayList();
         for (ItemCategory itm : itemsList) {
         comboList.add(new ComboBoxChoices(itm.getIdItemCategoryPk().toString(), itm.getItemCategoryName()));
         }*/
        ComboBoxChoices selectedCategory = new ComboBoxChoices(category.getIdCategoryPk().toString(), category.getCategoryName());
        ComboBoxChoices selectedItem = new ComboBoxChoices(itemCategory.getIdItemCategoryPk().toString(), itemCategory.getItemCategoryName());
        categories.getSelectionModel().select(selectedCategory);
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
            itemCategory = (ItemCategory) pdm.findByIdInt("ItemCategory", Integer.valueOf(itemChoiced.getItemValue()));
            if (exists) {
                product.removeProductItems(productCategory);
                pdm.deleteCategory(productCategory.getItem(), product);
            }
            productCategoryPK = new ProductCategoryPK();
            productCategory = new ProductCategory();
            productCategoryPK.setIdProductFk(product.getIdProductPk());
            productCategoryPK.setIdItemCategoryFk(itemCategory.getIdItemCategoryPk());
            productCategory.setProductCategoryPK(productCategoryPK);
            productCategory.setProduct(product);
            productCategory.setItemCategory(itemCategory);
            productCategory.setItem(product.getIdProductPk().toString() + itemCategory.getIdItemCategoryPk());
            product.addProductItems(productCategory);
            pdm.saveCategory(productCategory, product);
            productController.itemDataTableRefresh();
            closeModal();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar categoría de producto", e.getMessage(), sw.toString(), "");
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
            product.removeProductItems(productCategory);
            pdm.deleteCategory(productCategory.getItem(), product);
            productController.itemDataTableRefresh();
            closeModal();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Eliminar persona", e.getMessage(), sw.toString(), "");
            closeModal();
            //e.printStackTrace();
        }
    }

    /*
     Método para recargar los valores del combobox
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void selectedCategoryUpdateList(ActionEvent event) {
        try {
            cm = new CategoryModel();
            items.getItems().clear();
            ComboBoxChoices selectedCategory = categories.getSelectionModel().getSelectedItem();
            category = (Category) cm.findByIdInt("Category", Integer.parseInt(selectedCategory.getItemValue()));
            Collection<ItemCategory> itemsList = cm.findByIdCategory(category);
            ObservableList<ComboBoxChoices> comboList = FXCollections.observableArrayList();
            for (ItemCategory itm : itemsList) {
                comboList.add(new ComboBoxChoices(itm.getIdItemCategoryPk().toString(), itm.getItemCategoryName()));
            }
            items.getItems().addAll(comboList);
            items.getSelectionModel().selectFirst();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Actualizar listado de categorías", e.getMessage(), sw.toString(), "");
            //e.printStackTrace();
        }
    }

    /*
     Método para recargar los valores del combobox
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void itemSelected(MouseEvent event) {
        if (items.getSelectionModel().getSelectedItem() != null) {
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
        stage = (Stage) scene.getWindow();
        stage.close();
    }

}

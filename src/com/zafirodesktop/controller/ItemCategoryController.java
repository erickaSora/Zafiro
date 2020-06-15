/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.zafirodesktop.entity.Category;
import com.zafirodesktop.entity.ItemCategory;
import com.zafirodesktop.model.AbstractFacade;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.util.FormValidation;
import com.zafirodesktop.util.LogActions;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Digitall
 */
public class ItemCategoryController extends FormValidation implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private HBox buttonsBox;
    @FXML
    private Label title;
    @FXML
    private TextField itemCategoryName;
    @FXML
    private Label vldName;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Stage stage;

    private boolean exists;
    private AbstractFacade abs;
    private CategoryController categoryController;
    private ItemCategory itemCategory;
    private Category actualCategory;
    private ResourceBundle bundle;

    public CategoryController getCategoryController() {
        return categoryController;
    }

    public void setCategoryController(CategoryController categoryController) {
        this.categoryController = categoryController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
    }

    /*
     Método para inicializar los valores del formulario
     */
    public void initializeValues(Category category) {
        actualCategory = category;
        saveButton.setDisable(true);
        buttonsBox.getChildren().remove(deleteButton);
        title.setText(bundle.getString("itemCategoryTitle"));
    }

    /*
     Método para inicializar los valores del formulario, en caso de que ya existan datos
     @param: usuario a editar(Users)
     */
    public void initializeValuesExists(ItemCategory actualItem) {
        itemCategory = actualItem;
        actualCategory = itemCategory.getIdCategoryFk();
        exists = true;
        itemCategoryName.setText(itemCategory.getItemCategoryName());
        saveButton.setDisable(false);
        title.setText(bundle.getString("itemCategoryUpdateTitle"));
    }

    /*
     Método para insertar o actualizar item
     @param: acción que genera la petición(onAction)
     */
    @FXML
    private void insertItem(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            if(!exists)
                itemCategory = new ItemCategory();
            itemCategory.setItemCategoryName(itemCategoryName.getText());
            itemCategory.setIdCategoryFk(actualCategory);
            if (exists) 
                abs.updateFinal(itemCategory);
            else{ 
                actualCategory.getItemCategoryCollection().add(itemCategory);
                abs.saveFinal(itemCategory);
            }
            categoryController.itemDataTableRefresh();
            closeModal();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Insertar item de categoría", e.getMessage(), sw.toString(), "");
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
            abs = new AbstractFacade();
            actualCategory.getItemCategoryCollection().remove(itemCategory);
            abs.deleteIntFinal("ItemCategory", itemCategory.getIdItemCategoryPk());
            categoryController.itemDataTableRefresh();
            closeModal();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Eliminar item de categoría", e.getMessage(), sw.toString(), "");
            closeModal();
            //e.printStackTrace();
        }
    }

    /*
     Método para validar el campo categoryname
     */
    public void validateCN(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        if (notNull(itemCategoryName, vldName, t.getText().length(), bundle, bundle.getString("categoryItemNameTltp"))) {
            if(nameExists(itemCategoryName, vldName, "ItemCategory", t.getText(), bundle)){
                maxLenght(itemCategoryName, vldName, t.getLength(), 50, bundle);
            }
        }
        validForm();
    }

    /*
     Método para validar que el formulario ya esta listo para ser enviado
     */
    void validForm() {
        if (!itemCategoryName.getText().isEmpty() && !vldName.isVisible()) {
            saveButton.setDisable(false);
        } else {
            saveButton.setDisable(true);
        }
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

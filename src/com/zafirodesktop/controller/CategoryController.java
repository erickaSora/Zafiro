/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.zafirodesktop.entity.Category;
import com.zafirodesktop.entity.ItemCategory;
import com.zafirodesktop.model.CategoryModel;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.util.FormValidation;
import com.zafirodesktop.util.LogActions;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
public class CategoryController extends FormValidation implements Initializable {

    /*
     Especificación de los componentes asociados en el FXML
     */
    @FXML
    private AnchorPane mainPane;
    @FXML
    private HBox buttonsBox;
    @FXML
    private Label title;
    @FXML
    private TextField categoryName;
    @FXML
    private Label vldCategoryName;
    @FXML
    private TableView itemsDataTable;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addButton;
    @FXML
    private Button helpButton;
    @FXML
    private Tooltip tlpHelp;
    @FXML
    private Stage stage;
    @FXML
    private TableView asociatedItems;
    @FXML
    private TableColumn item;

    /*
     Objetos y variables del controlador 
     */
    private boolean exists;
    private CategoryModel cm;
    private Category category;
    private ItemCategory itemCategory;
    private MainController mainController;
    private ResourceBundle bundle;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        title.setText(bundle.getString("categoryTitle"));
        categoryName.setPromptText(bundle.getString("lblCategory"));
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
        buttonsBox.getChildren().remove(deleteButton);
    }

    /*
     Método para inicializar los valores del formulario, en caso de que ya existan datos
     @param: usuario a editar(Users)
     */
    public void initializeValuesExists(Category actualCategory) {
        cm = new CategoryModel();
        category = actualCategory;
        setExists(true);
        title.setText(bundle.getString("categoryUpdateTitle"));
        categoryName.setText(category.getCategoryName());
        saveButton.setDisable(false);
        itemsDataTable.setVisible(true);
        addButton.setVisible(true);
        addButton.setDisable(false);
        buttonsBox.getChildren().add(1, deleteButton);
        item = new TableColumn(bundle.getString("asociatedItemsCategoryDatatable"));
        item.setMinWidth(596);
        item.setCellValueFactory(new PropertyValueFactory<ItemCategory, String>("itemCategoryName"));
        itemsDataTable.getColumns().addAll(item);
        itemsDataTable.getItems().addAll(cm.findByIdCategory(category));
    }
    
    /*
     Método para actualizar el datatable 
    */
    public void itemDataTableRefresh(){
        ObservableList<String> init = FXCollections.observableArrayList();
        itemsDataTable.setItems(init);
        itemsDataTable.getColumns().removeAll(item);
        itemsDataTable.getColumns().addAll(item);
        itemsDataTable.getItems().addAll(cm.findByIdCategory(category));
    }

    /*
     Método para insertar o actualizar los datos del formulario
     @param: acción que genera la petición(onAction)
     */
    @FXML
    private void insertCategory(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            cm = new CategoryModel();
            if(!isExists())
                category = new Category();
            category.setCategoryName(categoryName.getText());
            if (isExists()) {
                cm.updateFinal(category);
                getMainController().setMessage(bundle.getString("updateSuccess"), false);
                closeModal();
            } else {
                cm.saveFinal(category);
                getMainController().setMessage(bundle.getString("createSuccess"), false);
                initializeValuesExists(category);
            }
            getMainController().setDataTableView();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar categoría", e.getMessage(), sw.toString(), "");
            closeModal();
            getMainController().setMessage(bundle.getString("createError"), true);
            //e.printStackTrace();
        }
    }

    /*
     Método para borrar el dato actual
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void deleteCategory(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            cm = new CategoryModel();
            cm.deleteIntFinal("Category", category.getIdCategoryPk());
            closeModal();
            getMainController().setDataTableView();
            getMainController().setMessage(bundle.getString("deleteSuccess"), false);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Borrar categoría", e.getMessage(), sw.toString(), "");
            closeModal();
            getMainController().setMessage(bundle.getString("deleteError"), true);
            //e.printStackTrace();
        }
    }

    /*
     Método para validar el campo categoryname
     */
    public void validateCN(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        if (notNull(categoryName, vldCategoryName, t.getText().length(), bundle, bundle.getString("categoryNameTltp"))) {
            if(nameExists(categoryName, vldCategoryName, "Category", t.getText(), bundle))
                maxLenght(categoryName, vldCategoryName, t.getLength(), 50, bundle);
        }
        validForm();
    }

    /*
     Método para validar que el formulario ya esta listo para ser enviado
     */
    void validForm() {
        if (!categoryName.getText().isEmpty() && !vldCategoryName.isVisible()) {
            saveButton.setDisable(false);
        } else {
            saveButton.setDisable(true);
        }
    }

    /*
     Método para cargar el modal de registro de nuevo item
     @param: evento que ejecute la acción (onAction)
     */
    public void insertNewItemModal(ActionEvent event) throws IOException {
        Stage modal = new Stage(StageStyle.DECORATED);
        Locale es = new Locale("es", "ES");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/item.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
        Parent rootModal = (Parent) fxmlLoader.load();
        modal.setScene(new Scene(rootModal));
        modal.setTitle("Item");
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(
                ((Node) event.getSource()).getScene().getWindow());
        modal.show();
        ItemCategoryController itemController = fxmlLoader.<ItemCategoryController>getController();
        itemController.initializeValues(category);
        itemController.setCategoryController(this);
    }

    /*
     Método para cargar el modal de registro de nuevo item
     @param: evento que ejecute la acción (onMouseClicked)
     */
    public void loadSelectedItemModal(MouseEvent event) throws IOException {
        try{
        if (itemsDataTable.getSelectionModel().getSelectedItem() != null) {
            Stage modal = new Stage(StageStyle.DECORATED);
            Locale es = new Locale("es", "ES");
            ItemCategory actualItem = (ItemCategory) itemsDataTable.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/item.fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
            Parent rootModal = (Parent) fxmlLoader.load();
            modal.setScene(new Scene(rootModal));
            modal.setTitle("Item");
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            modal.show();
            ItemCategoryController itemController = fxmlLoader.<ItemCategoryController>getController();
            itemController.setCategoryController(this);
            itemController.initializeValuesExists(actualItem);
        }
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Registrar nueva categoría", e.getMessage(), sw.toString(), "");
            //e.printStackTrace();
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
        stage = (Stage) scene.getWindow();
        stage.close();
    }

}

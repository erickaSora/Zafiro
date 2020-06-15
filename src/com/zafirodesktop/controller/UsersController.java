/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.controller;

import com.zafirodesktop.entity.Users;
import com.zafirodesktop.model.AbstractFacade;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.util.ComboBoxChoices;
import com.zafirodesktop.util.FormValidation;
import com.zafirodesktop.util.LogActions;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Digitall
 */
public class UsersController extends FormValidation implements Initializable{

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
    private TextField username;
    @FXML
    private PasswordField passwd;
    @FXML
    private PasswordField retypePasswd;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField phone;
    @FXML
    private ComboBox<ComboBoxChoices> type;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button helpButton;
    @FXML
    private Label vldUserName;
    @FXML
    private Label vldPasswd;
    @FXML
    private Label vldRetypePasswd;
    @FXML
    private Label vldFirstName;
    @FXML
    private Label vldPhoneNumber;
    @FXML
    private Label vldLastName;
    @FXML
    private Tooltip tlpId;
    @FXML
    private Tooltip tlpPasswd;
    @FXML
    private Tooltip tlpPasswdConfirm;
    @FXML
    private Tooltip tlpFirsName;
    @FXML
    private Tooltip tlpLastName;
    @FXML
    private Tooltip tlpPhone;
    @FXML
    private Tooltip tlpRol;
    @FXML
    private Tooltip tlpHelp;
    @FXML
    private Stage stage;
    
    /*
    Objetos y variables del controlador 
    */
    private boolean exists;
    private AbstractFacade abs;
    private Users user;
    private MainController mainController;
    private ResourceBundle bundle;

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle=rb;
        title.setText(bundle.getString("userTitle"));
        username.setPromptText(bundle.getString("lblUserId"));
        passwd.setPromptText(bundle.getString("lblUserPasswd"));
        retypePasswd.setPromptText(bundle.getString("configAppPasswdVld"));
        firstName.setPromptText(bundle.getString("lblUserName"));
        lastName.setPromptText(bundle.getString("lblUserLastName"));
        phone.setPromptText(bundle.getString("lblUserPhoneNumber"));
        type.setPromptText(bundle.getString("lblUserType"));
        cancelButton.setText(bundle.getString("cancelButton"));
        saveButton.setText(bundle.getString("saveButton"));
        deleteButton.setText(bundle.getString("deleteButton"));
        initializeValues();
    }
    
    /*
    Método para inicializar los valores del formulario
    */
    public void initializeValues(){
        ObservableList<ComboBoxChoices> typesList = FXCollections.observableArrayList(
                new ComboBoxChoices("0", bundle.getString("lblUserTypeOption1")),
                new ComboBoxChoices("1", bundle.getString("lblUserTypeOption2"))
        );
        type.getItems().addAll(typesList);
        type.getSelectionModel().selectFirst();
        saveButton.setDisable(true);
        buttonsBox.getChildren().remove(deleteButton);  
        Image img = new Image("/com/zafirodesktop/ui/img/ico/pointer_black.png");
        ImageView imgvw = new ImageView(img);
        imgvw.setFitHeight(15);
        imgvw.setFitWidth(15);
        tlpHelp.setGraphic(imgvw);
    }
    
    /*
    Método para inicializar los valores del formulario, en caso de que ya existan datos
    @param: usuario a editar(Users)
    */
    public void initializeValuesExists(Users actualUser){
        this.user = actualUser;
        exists=true;
        title.setText(bundle.getString("userTitleUpdate"));
        username.setText(actualUser.getUsername());
        passwd.setText(actualUser.getPassword());
        retypePasswd.setText(actualUser.getPassword());
        firstName.setText(actualUser.getFirstName());
        lastName.setText(actualUser.getLastName());
        phone.setText(actualUser.getPhone());
        type.getSelectionModel().select(actualUser.getCreatedOn());
        saveButton.setDisable(false);
        type.setDisable(true);
        if(actualUser.getId()!=1){
            buttonsBox.getChildren().add(1, deleteButton);
            type.setDisable(false);
        }
    }
    
    /*
    Método para insertar o actualizar los datos del formulario
    @param: acción que genera la petición(onAction)
    */
    @FXML
    private void insertUser(ActionEvent event){
        try {
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            ComboBoxChoices typeChoiced = (ComboBoxChoices) type.getSelectionModel().getSelectedItem();
            if(!exists){
                user = new Users();
            }
            user.setUsername(username.getText());
            user.setPassword(passwd.getText());
            user.setActive(new Short("1"));
            user.setFirstName(firstName.getText());
            user.setLastName(lastName.getText());
            user.setPhone(phone.getText());
            user.setCreatedOn(Integer.valueOf(typeChoiced.getItemValue()));
            if(exists){
                abs.updateFinal(user);
                getMainController().setMessage(bundle.getString("updateSuccess"), false);
            }else{
                abs.saveFinal(user);
                getMainController().setMessage(bundle.getString("createSuccess"), false);
            }
            getMainController().setDataTableView();
            closeModal();
        }catch(Exception e){
            getMainController().setMessage(bundle.getString("createError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar usuario", e.getMessage(), sw.toString(), this.user.toString());
            closeModal();
        }
    }
    
    /*
    Método para borrar el dato actual
    @param: evento que ejecute la acción (onAction)
    */
    @FXML
    private void deleteUser(ActionEvent event) {
        try{
        abs = new AbstractFacade();
        abs.deleteIntFinal("Users",user.getId());
        closeModal();
        getMainController().setDataTableView();
        getMainController().setMessage(bundle.getString("deleteSuccess"), false);
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Eliminar usuario", e.getMessage(), sw.toString(), this.user.toString());
            closeModal();
            getMainController().setMessage(bundle.getString("deleteError"), true);
            //e.printStackTrace();
        }
    }
    
    /*
    Método para validar el campo username
    */
    public void validateUN(KeyEvent arg){
        TextField t =(TextField)arg.getSource();
        if(validEmail(username, vldUserName, t.getText(), bundle)){
            if(nameExists(username, vldUserName, "Users", t.getText(), bundle))
                maxLenght(username, vldUserName, t.getLength(), 100, bundle);
        }
        validForm();
    }
    
    /*
    Método para validar el campo passwd
    */
    public void validatePW(KeyEvent arg){
        if(!retypePasswd.getText().isEmpty()){
            retypePasswd.setText("");
        }
        TextField t =(TextField)arg.getSource();
        int length = t.getText().length();
        notNull(passwd, vldPasswd, length, bundle, bundle.getString("passwdTltp"));
        validForm();
    }
    
    /*
     Método para validar la contraseña por segunda vez
     */
    public void validatePasswdRetype(KeyEvent arg){
        TextField t = (TextField) arg.getSource();
        match(retypePasswd, vldRetypePasswd, t.getText(), passwd.getText(), bundle);
        validForm();
    }
    
    /*
    Método para validar el campo firstname
    */
    public void validateFN(KeyEvent arg){
        TextField t =(TextField)arg.getSource();
        int length = t.getText().length();
        if(notNull(firstName, vldFirstName, length, bundle, bundle.getString("firstNameTltp")))
            maxLenght(firstName, vldFirstName, length, 50, bundle);
        validForm();
    }
    
    /*
    Método para validar el campo lastName
    */
    public void validateLN(KeyEvent arg){
        TextField t =(TextField)arg.getSource();
        int length = t.getText().length();
        maxLenght(lastName, vldLastName, length, 50, bundle);
        validForm();
    }
    
    /*
    Método para validar el campo phoneNumber
    */
    public void validatePN(KeyEvent arg){
        TextField t =(TextField)arg.getSource();
        int length = t.getText().length();
        maxLenght(phone, vldPhoneNumber, length, 20, bundle);
        validForm();
    }
    
    /*
    Método para validar que el formulario ya esta listo para ser enviado
    */
    void validForm(){
        if(!username.getText().isEmpty()&&!passwd.getText().isEmpty()&&!firstName.getText().isEmpty()&&!retypePasswd.getText().isEmpty()
                &&!vldFirstName.isVisible()&&!vldPasswd.isVisible()&&!vldUserName.isVisible()&&!vldLastName.isVisible()
                &&!vldPhoneNumber.isVisible()&&!vldRetypePasswd.isVisible())
            saveButton.setDisable(false);
        else
            saveButton.setDisable(true);
    }
    
    /*
     Método para cerrar el modal iniciado sin guardar ninguna cambio, desde el boto cancelar
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void closeButtonAction(ActionEvent event) {
        closeModal();
    }
    
    /*
        Método para ocultar o mostrar los tooltip de ayuda
    */
    public void showHideTooltips(){
        if(!tlpHelp.isShowing()){
            if(!helpButton.getStyleClass().contains("helpPressed")){
                helpButton.getStyleClass().add("helpPressed");
            }
            tlpHelp.show(helpButton, 
                helpButton.getScene().getWindow().getX() +helpButton.getLayoutX()+helpButton.getWidth()-10,
                helpButton.getScene().getWindow().getY() +helpButton.getLayoutY()+helpButton.getHeight());
        /*tlpId.show(username, 
                username.getScene().getWindow().getX() +username.getLayoutX()+username.getWidth()+10,
                username.getScene().getWindow().getY() +username.getLayoutY()+username.getHeight()-10);
        tlpFirsName.show(firstName, 
                firstName.getScene().getWindow().getX() +firstName.getLayoutX()+firstName.getWidth()+10,
                firstName.getScene().getWindow().getY() +firstName.getLayoutY()+firstName.getHeight()+240);
        tlpLastName.show(lastName, 
                lastName.getScene().getWindow().getX() +lastName.getLayoutX()+lastName.getWidth()+10,
                lastName.getScene().getWindow().getY() +lastName.getLayoutY()+lastName.getHeight()+320);
        tlpPasswd.show(passwd, 
                passwd.getScene().getWindow().getX() +passwd.getLayoutX()+passwd.getWidth()+10,
                passwd.getScene().getWindow().getY() +passwd.getLayoutY()+passwd.getHeight()+80);
        tlpPasswdConfirm.show(retypePasswd, 
                retypePasswd.getScene().getWindow().getX() +retypePasswd.getLayoutX()+retypePasswd.getWidth()+10,
                retypePasswd.getScene().getWindow().getY() +retypePasswd.getLayoutY()+retypePasswd.getHeight()+160);
        tlpPhone.show(phone, 
                phone.getScene().getWindow().getX() +phone.getLayoutX()+phone.getWidth()+10,
                phone.getScene().getWindow().getY() +phone.getLayoutY()+phone.getHeight()+400);
        tlpRol.show(type, 
                type.getScene().getWindow().getX() +type.getLayoutX()+type.getWidth()+10,
                type.getScene().getWindow().getY() +type.getLayoutY()+type.getHeight()+480);*/
        }else{
            if(helpButton.getStyleClass().contains("helpPressed")){
                helpButton.getStyleClass().remove("helpPressed");
            }
            tlpHelp.hide();
            /*tlpId.hide();
            tlpFirsName.hide();
            tlpLastName.hide();
            tlpPasswd.hide();
            tlpPasswdConfirm.hide();
            tlpPhone.hide();
            tlpRol.hide();*/
        }
        
    }

    private void closeModal() {
        mainController.showHideMask(false);
        Scene scene = mainPane.getScene();
        stage = (Stage) scene.getWindow();
        stage.close();
    }
}

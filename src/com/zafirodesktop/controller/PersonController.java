/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.zafirodesktop.entity.Person;
import com.zafirodesktop.entity.Place;
import com.zafirodesktop.model.AbstractFacade;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Digitall
 */
public class PersonController extends FormValidation implements Initializable {

    /*
     Especificación de los componentes asociados en el FXML
     */
    @FXML
    private AnchorPane mainPane;
    @FXML
    private HBox buttonsBox;
    @FXML
    private HBox bankBox;
    @FXML
    private HBox accountBox;
    @FXML
    private HBox personTypeBox;
    @FXML
    private Pane bankImagePane;
    @FXML
    private Pane accountImagePane;
    @FXML
    private ImageView bankImage;
    @FXML
    private ImageView accountImage;
    @FXML
    private Label title;
    @FXML
    private TextField idPerson;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField address;
    @FXML
    private TextField email;
    @FXML
    private TextField place;
    @FXML
    private TextField bankName;
    @FXML
    private TextField accountNumber;
    @FXML
    private ComboBox<ComboBoxChoices> type;
    @FXML
    private ListView placesList;
    @FXML
    private GridPane gridPane;
    @FXML
    private Tooltip tlpHelp;
    @FXML
    private Stage stage;

    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button helpButton;

    @FXML
    private Label vldIdPerson;
    @FXML
    private Label vldFirstName;
    @FXML
    private Label vldLastName;
    @FXML
    private Label vldAddress;
    @FXML
    private Label vldPhone;
    @FXML
    private Label vldEmail;
    @FXML
    private Label vldPlace;

    /*
     Objetos y variables del controlador 
     */
    private boolean exists;
    private boolean shownBank;
    private boolean isChecker;
    private AbstractFacade abs;
    private Person person;
    private Place initPlace;
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
        bundle = rb;
        title.setText(bundle.getString("personTitle"));
        idPerson.setPromptText(bundle.getString("lblPersonId"));
        firstName.setPromptText(bundle.getString("lblPersonName"));
        lastName.setPromptText(bundle.getString("lblPersonLastName"));
        phoneNumber.setPromptText(bundle.getString("lblPersonPhoneNumber"));
        address.setPromptText(bundle.getString("lblPersonAddress"));
        email.setPromptText(bundle.getString("lblPersonEmail"));
        place.setPromptText(bundle.getString("lblPersonPlace"));
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
        ObservableList<ComboBoxChoices> typesList = FXCollections.observableArrayList(
                new ComboBoxChoices("C", bundle.getString("personTypeClient")),
                new ComboBoxChoices("S", bundle.getString("personTypeSupplier"))
        );
        type.getItems().addAll(typesList);
        type.getSelectionModel().selectFirst();
        SessionBD.persistenceCreate();
        abs = new AbstractFacade();
        initPlace = (Place) abs.findByIdInt("Place", 15001);
        place.setText(initPlace.getPlaceName() + " " + initPlace.getIdPlaceFk().getPlaceName());
        saveButton.setDisable(true);
        buttonsBox.getChildren().remove(deleteButton);
    }

    /*
     Método para inicializar los valores del formulario para cajero
     */
    public void initializeValuesChecker() {
        gridPane.getChildren().remove(personTypeBox);
        isChecker = true;
    }

    /*
     Método para inicializar los valores del formulario, en caso de que ya existan datos
     */
    public void initializeValuesExists(Person actualPerson) {
        this.person = actualPerson;
        String typeName = bundle.getString("personTypeClient");
        if (person.getPersonType().equals("S")) {
            typeName = bundle.getString("personTypeSupplier");
            shownBank = true;
        }
        ComboBoxChoices selectedType = new ComboBoxChoices(person.getPersonType(), typeName);
        type.getSelectionModel().select(selectedType);
        if (shownBank) {
            bankName.setText(person.getBank());
            accountNumber.setText(person.getAccountNumber());
        }
        exists = true;
        initPlace = actualPerson.getIdPlaceFk();
        title.setText(bundle.getString("personUpdate"));
        idPerson.setText(actualPerson.getNit());
        firstName.setText(actualPerson.getPersonFirstname());
        lastName.setText(actualPerson.getPersonLastname());
        phoneNumber.setText(actualPerson.getPersonPhoneNo());
        address.setText(actualPerson.getPersonAddress());
        email.setText(actualPerson.getEmail());
        place.setText(initPlace.toString());
        saveButton.setDisable(false);
        buttonsBox.getChildren().add(1, deleteButton);
    }

    /*
     Método para insertar o actualizar los datos del formulario
     @param: acción que genera la petición(onAction)
     */
    @FXML
    public void savePerson(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            ComboBoxChoices typeChoiced = (ComboBoxChoices) type.getSelectionModel().getSelectedItem();
            //Place actualPlace = 
            if (!exists) {
                person = new Person();
            }
            person.setNit(idPerson.getText());
            person.setPersonFirstname(firstName.getText());
            person.setPersonLastname(lastName.getText());
            person.setPersonPhoneNo(phoneNumber.getText());
            person.setPersonAddress(address.getText());
            person.setEmail(email.getText());
            person.setIdPlaceFk(initPlace);
            person.setPersonType(typeChoiced.getItemValue());
            if (!isChecker) {
                if (typeChoiced.getItemValue().equals("S")) {
                    person.setAccountNumber(accountNumber.getText());
                    person.setBank(bankName.getText());
                }
            }
            if (exists) {
                abs.updateFinal(person);
                mainController.setMessage(bundle.getString("updateSuccess"), false);
            } else {
                abs.saveFinal(person);
                mainController.setMessage(bundle.getString("createSuccess"), false);
            }
            if (isChecker) {
                mainController.setActualClient(person);
                mainController.listsViewPanel.setVisible(false);
                mainController.messagesPanel.setVisible(false);
                mainController.validForm();
            }else{
                mainController.setDataTableView();
            }
            closeModal();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar persona", e.getMessage(), sw.toString(), "");
            closeModal();
            mainController.setMessage(bundle.getString("createError"), true);
            //e.printStackTrace();
        }
    }

    /*
     Método para borrar el dato actual
     */
    @FXML
    public void deletePerson() {
        try {
            abs.deleteStringFinal("Person", person.getNit());
            getMainController().setDataTableView();
            getMainController().setMessage(bundle.getString("deleteSuccess"), false);
            closeModal();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Elimianr persona", e.getMessage(), sw.toString(), "");
            closeModal();
            getMainController().setMessage(bundle.getString("deleteError"), true);
            //e.printStackTrace();
        }
    }

    /*
     Método buscar un lugar a través de un ListView
     @param: evento que ejecute la acción (onKeyPress)
     */
    @FXML
    private void searchAction(KeyEvent event) {
        TextField tfActual = (TextField) event.getSource();
        ObservableList data = FXCollections.observableArrayList();
        SessionBD.persistenceCreate();
        abs = new AbstractFacade();
        vldPlace.setVisible(false);
        Collection<Place> list = abs.findByLike1("Place", place.getText().toLowerCase());
        for (Place place1 : list) {
            data.add(place1);
        }
        placesList.setVisible(true);
        placesList.setItems(data);
        placesList.getSelectionModel().selectFirst();
    }

    /*
     Método para mostrar las opciones del listado de acuerdo al texto ingresado
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void selectedAction(ActionEvent event) {
        loadSelectedData();
    }

    /*
     Método para cargar los datos del formulario de acuerdo al formulario seleccionado
     @param: evento que ejecute la acción (onMouseClicked)
     */
    @FXML
    private void selectedActionList(MouseEvent event) {
        loadSelectedData();
    }

    /*
     Método para cargar los datos del formulario de acuerdo al dato seleccionado 
     */
    private void loadSelectedData() {
        if (placesList.getSelectionModel().getSelectedItem() != null) {
            initPlace = (Place) placesList.getSelectionModel().getSelectedItem();
            place.setText(initPlace.getPlaceName() + " " + initPlace.getIdPlaceFk().getPlaceName());
            placesList.setVisible(false);
        } else {
            vldPlace.setVisible(true);
            vldPlace.setText(bundle.getString("notMatches"));
        }
        validForm();
    }

    /*
     Método para cargar el diálogo de confirmación 
     @param: evento que ejecute la acción (onAction)
     */
    public void showConfirmationDialog(ActionEvent event) throws IOException {
        int invoiceType = 4;
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
        dialogController.setPersonController(this);
        dialogController.initializeValues(bundleMessage, invoiceType);
    }

    /*
     Método buscar un lugar a través de un ListView
     @param: evento que ejecute la acción (onKeyPress)
     */
    @FXML
    public void personTypeAction(ActionEvent event) {
        ComboBoxChoices selectedChoice = type.getSelectionModel().getSelectedItem();
        if(selectedChoice.getItemValue().equals("S")) {
            shownBank = true;
        } else {
            shownBank = false;
        }
        showHideBankAttributes("", "");
    }

    public void showHideBankAttributes(String txtBankName, String txtAcountNumber) {
        if (shownBank && !gridPane.getChildren().contains(bankBox)) {
            Image imgBank = new Image("/com/zafirodesktop/ui/img/ico/doalr_blac.png");
            Image imgAccount = new Image("/com/zafirodesktop/ui/img/ico/account_black.png");
            bankImage = new ImageView(imgBank);
            bankImage.setFitHeight(20);
            bankImage.setFitWidth(20);
            bankImage.setLayoutX(8);
            bankImage.setLayoutY(6);
            accountImage = new ImageView(imgAccount);
            accountImage.setFitHeight(20);
            accountImage.setFitWidth(20);
            accountImage.setLayoutX(8);
            accountImage.setLayoutY(6);
            bankImagePane = new Pane();
            bankImagePane.getStyleClass().add("iconContent");
            bankImagePane.setPrefSize(36, 33);
            bankImagePane.getChildren().add(bankImage);
            accountImagePane = new Pane();
            accountImagePane.getStyleClass().add("iconContent");
            accountImagePane.setPrefSize(36, 33);
            accountImagePane.getChildren().add(accountImage);
            bankName = new TextField(txtBankName);
            bankName.setPrefHeight(35);
            bankName.setPrefWidth(367);
            bankName.setPromptText(bundle.getString("lblPersonBank"));
            accountNumber = new TextField(txtAcountNumber);
            accountNumber.setPrefHeight(35);
            accountNumber.setPrefWidth(367);
            accountNumber.setPromptText(bundle.getString("lblPersonAccountNumber"));
            bankBox = new HBox();
            bankBox.getChildren().add(bankImagePane);
            bankBox.getChildren().add(bankName);
            accountBox = new HBox();
            accountBox.getChildren().add(accountImagePane);
            accountBox.getChildren().add(accountNumber);
            gridPane.add(bankBox, 0, 8);
            gridPane.add(accountBox, 0, 9);
        } else {
            if (gridPane.getChildren().contains(bankBox)) {
                gridPane.getChildren().remove(bankBox);
                gridPane.getChildren().remove(accountBox);
            }
        }
    }
    
    /*
     Método para ocultar la vista de buscar lugares
     */
    public void hidePlacesListView(MouseEvent event) {
        if (placesList.isVisible()) {
            placesList.setVisible(false);
        }
    }

    /*
     Método para validar el campo id
     */
    public void validateID(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        if (notNull(idPerson, vldIdPerson, length, bundle, bundle.getString("personIdTltp"))) {
            if(idExists(idPerson, vldIdPerson, "Person", idPerson.getText(), bundle)){
                maxLenght(idPerson, vldIdPerson, length, 50, bundle);
            }
        }
        validForm();
    }

    /*
     Método para validar el campo firstName
     */
    public void validateFN(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        if(notNull(firstName, vldFirstName, length, bundle, bundle.getString("personNameTltp")))
            maxLenght(firstName, vldFirstName, length, 100, bundle);
        validForm();
    }
    
    /*
     Método para validar el campo lastName
     */
    public void validateLN(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        maxLenght(lastName, vldLastName, length, 100, bundle);
        validForm();
    }
    
    /*
     Método para validar el campo address
     */
    public void validateAD(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        maxLenght(address, vldAddress, length, 100, bundle);
        validForm();
    }
    
    /*
     Método para validar el campo phone
     */
    public void validatePN(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        maxLenght(phoneNumber, vldPhone, length, 100, bundle);
        validForm();
    }
    
    /*
     Método para validar el campo email
     */
    public void validateEM(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        if(validEmail(email, vldEmail, t.getText(), bundle)){
            maxLenght(email, vldEmail, length, 80, bundle);
        }
        validForm();
    }
    
    

    /*
     Método para validar que el formulario ya esta listo para ser enviado
     */
    void validForm() {
        if (!vldIdPerson.isVisible() && !vldFirstName.isVisible() && !vldPlace.isVisible() && !vldLastName.isVisible()
                && !vldAddress.isVisible() && !vldPhone.isVisible() && !vldEmail.isVisible()
                && !place.getText().isEmpty() && !idPerson.getText().isEmpty() && !firstName.getText().isEmpty() ) {
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
        stage = (Stage) scene.getWindow();
        stage.close();
    }

}

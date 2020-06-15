/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.zafirodesktop.entity.Settings;
import com.zafirodesktop.entity.Users;
import com.zafirodesktop.model.AbstractFacade;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.model.UsersModel;
import com.zafirodesktop.util.ComboBoxChoices;
import com.zafirodesktop.util.FormValidation;
import com.zafirodesktop.util.LogActions;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author andresforero
 */
public class InitialConfigController extends FormValidation implements Initializable {

    //Welcome
    @FXML
    public AnchorPane mainPane;

    //BussinesInfo
    @FXML
    public Label lblValidate1;
    @FXML
    public Label lblValidateSr;
    @FXML
    public Label lblValidate2;
    @FXML
    public Label lblValidate3;
    @FXML
    public Label lblValidate4;
    @FXML
    public Label lblValidate5;
    @FXML
    public Label lblValidatePassword;
    @FXML
    private ImageView actualPage;
    @FXML
    private ImageView nextPage;
    @FXML
    private ImageView nextPage2;
    @FXML
    private ImageView nextPage3;
    @FXML
    private HBox actualState;
    @FXML
    public TextField txtBussinesName;
    @FXML
    public TextField txtSerial;
    @FXML
    public TextField txtNit;
    @FXML
    public TextField txtPhoneNumber;
    @FXML
    public TextField txtAddress;
    @FXML
    public TextField txtCity;
    @FXML
    public Button saveButton;
    @FXML
    public Stage stage;

    //BasicConfig
    @FXML
    public TextField txtAdminId;
    @FXML
    public PasswordField txtPasswd;
    @FXML
    public PasswordField txtValidatePasswd;
    @FXML
    public TextField txtLastInvoice;
    @FXML
    private ComboBox<ComboBoxChoices> type;

    //Personalization
    @FXML
    public TextField txtLogo;
    @FXML
    private ComboBox<ComboBoxChoices> printSize;
    @FXML
    private ComboBox<ComboBoxChoices> language;

    //Variables
    public ResourceBundle bundle;
    private AbstractFacade abs;
    private Settings settings;
    private Users admin;
    private boolean userExists;
    private int formId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        Image actual = new Image("com/zafirodesktop/ui/img/actualPage.png");
        Image next = new Image("com/zafirodesktop/ui/img/nextPage.png");
        actualPage = new ImageView(actual);
        actualPage.setFitWidth(15);
        actualPage.setFitHeight(15);
        nextPage = new ImageView(next);
        nextPage.setFitWidth(15);
        nextPage.setFitHeight(15);
        nextPage2 = new ImageView(next);
        nextPage2.setFitWidth(15);
        nextPage2.setFitHeight(15);
        nextPage3 = new ImageView(next);
        nextPage3.setFitWidth(15);
        nextPage3.setFitHeight(15);
    }

    /*
     Método para iniciar la vista de información del negocio
     */
    public void startInicialConfig() {
        abs = new AbstractFacade();
        settings = (Settings) abs.findByIdInt("Settings", 1);
        txtBussinesName.setPromptText(bundle.getString("lblSettingsBussinesName"));
        txtBussinesName.setText(settings.getBussinesName());
        txtSerial.setPromptText(bundle.getString("lblSettingsSerial"));
        txtNit.setPromptText(bundle.getString("lblSettingsNit"));
        txtNit.setText(settings.getNit());
        txtPhoneNumber.setPromptText(bundle.getString("lblSettingsPhoneNumber"));
        txtPhoneNumber.setText(settings.getPhoneNumber());
        txtAddress.setPromptText(bundle.getString("lblSettingsAddress"));
        txtAddress.setText(settings.getAddress());
        txtCity.setPromptText(bundle.getString("lblSettingsCity"));
        txtCity.setText(settings.getCity());
        refreshStates(actualPage, nextPage, nextPage2, nextPage3);
        if (settings.getBussinesName().isEmpty()) {
            lblValidate1.setText(bundle.getString("lblConfigBussinessName"));
            lblValidateSr.setText(bundle.getString("lblSettingsSerial"));
            lblValidate2.setText(bundle.getString("configBussinessNIT"));
            lblValidate3.setText(bundle.getString("configBussinessPhone"));
            lblValidate4.setText(bundle.getString("configBussinessAddress"));
            lblValidate5.setText(bundle.getString("configBussinessCity"));
            lblValidate1.setVisible(true);
            lblValidate2.setVisible(true);
            lblValidate3.setVisible(true);
            lblValidate4.setVisible(true);
            lblValidate5.setVisible(true);
            lblValidateSr.setVisible(true);
            saveButton.setDisable(true);
        }else{
            txtSerial.setText(getSerial(settings.getBussinesName()));
        }
        formId = 1;
    }

    /*
     Método para iniciar la vista de configuración inicial
     */
    public void startAppConfig() {
        abs = new AbstractFacade();
        settings = (Settings) abs.findByIdInt("Settings", 1);
        UsersModel um = new UsersModel();
        if (!um.findAll("Users").isEmpty()) {
            admin = um.findByCreatedOn(1);
            userExists = true;
        } else {
            admin = new Users("", "", new Short("1"));
        }
        txtAdminId.setPromptText(bundle.getString("lblConfigAppAdminId"));
        txtAdminId.setText(admin.getUsername());
        txtPasswd.setPromptText(bundle.getString("lblUserPasswd"));
        txtPasswd.setText(admin.getPassword());
        txtValidatePasswd.setPromptText(bundle.getString("lblUserPasswd"));
        txtValidatePasswd.setText(admin.getPassword());
        txtLastInvoice.setPromptText(bundle.getString("lblSettingsLastInvoice"));
        txtLastInvoice.setText(settings.getLastInvoice());
        ObservableList<ComboBoxChoices> typesList = FXCollections.observableArrayList(
                new ComboBoxChoices("0", "No, no utilizo caja"),
                new ComboBoxChoices("1", "Si, utilizo caja")
        );
        type.getItems().addAll(typesList);
        type.getSelectionModel().selectFirst();
        refreshStates(nextPage, actualPage, nextPage2, nextPage3);
        if (!userExists) {
            lblValidate1.setText(bundle.getString("lblConfigAppAdminId"));
            lblValidate2.setText(bundle.getString("configAppPasswd"));
            lblValidatePassword.setText(bundle.getString("configAppPasswdVld"));
            lblValidate3.setText(bundle.getString("configAppLastInvoice"));
            lblValidate4.setText(bundle.getString("configAppUseMoneyBox"));
            lblValidate1.setVisible(true);
            lblValidate2.setVisible(true);
            lblValidate3.setVisible(true);
            lblValidate4.setVisible(true);
            saveButton.setDisable(true);
        }
        formId = 2;
    }

    /*
     Método para iniciar la vista de información del negocio
     */
    public void startPersonalization() {
        abs = new AbstractFacade();
        settings = (Settings) abs.findByIdInt("Settings", 1);
        txtLogo.setPromptText(bundle.getString("lblSettingsLogo"));
        txtLogo.setText(settings.getLogo());
        ObservableList<ComboBoxChoices> typesList = FXCollections.observableArrayList(
                new ComboBoxChoices("0", "Tema Naranja")
        );
        type.getItems().addAll(typesList);
        type.getSelectionModel().selectFirst();
        ObservableList<ComboBoxChoices> printSizesList = FXCollections.observableArrayList(
                new ComboBoxChoices("TK", bundle.getString("lblSettingsPrintTk")),
                new ComboBoxChoices("HL", bundle.getString("lblSettingsPrintHF"))
        );
        printSize.getItems().addAll(printSizesList);
        printSize.getSelectionModel().selectFirst();
        ObservableList<ComboBoxChoices> languageList = FXCollections.observableArrayList(
                new ComboBoxChoices("es", "Español")
        );
        language.getItems().addAll(languageList);
        language.getSelectionModel().selectFirst();
        refreshStates(nextPage, nextPage2, actualPage, nextPage3);
        if (settings.getPrintSize() == null) {
            lblValidate2.setText(bundle.getString("lblPersonalizePrintSize"));
            lblValidate2.setVisible(true);
            saveButton.setDisable(true);
        }
        formId = 3;
    }

    /*
     Método para iniciar la vista de finalización de configuración
     */
    public void finishPersonalization() {
        refreshStates(nextPage, nextPage2, nextPage3, actualPage);
        formId = 4;
    }

    /*
     Método para insertar o actualizar los datos del formulario
     @param: acción que genera la petición(onAction)
     */
    @FXML
    public void saveBussinesInfo(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            settings.setBussinesName(txtBussinesName.getText());
            settings.setNit(txtNit.getText());
            settings.setPhoneNumber(txtPhoneNumber.getText());
            settings.setAddress(txtAddress.getText());
            settings.setCity(txtCity.getText());
            abs.updateFinal(settings);
            loadAppConfig("initialConfig2.fxml", formId);
            closeModal();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar información del negocio", e.getMessage(), sw.toString(), "");
        }
    }

    /*
     Método para insertar o actualizar los datos del formulario
     @param: acción que genera la petición(onAction)
     */
    @FXML
    public void loadPreviousView(ActionEvent event) {
        if (formId == 2) {
            loadAppConfig("initialConfig.fxml", 0);
        } else if (formId == 3) {
            loadAppConfig("initialConfig2.fxml", 1);
        } else {
            loadAppConfig("initialConfig3.fxml", 2);
        }
    }

    /*
     Método para insertar o actualizar los datos del formulario
     @param: acción que genera la petición(onAction)
     */
    @FXML
    public void saveConfigInfo(ActionEvent event) {
        try {
            ComboBoxChoices selectedBox = type.getSelectionModel().getSelectedItem();
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            settings.setLastInvoice(txtLastInvoice.getText());
            settings.setRememberedUser(txtAdminId.getText());
            settings.setCashBox(new Short(selectedBox.getItemValue()));
            abs.updateIntermediate(settings);
            admin.setUsername(txtAdminId.getText());
            admin.setEmail(txtAdminId.getText());
            admin.setPassword(txtPasswd.getText());
            if (userExists) {
                abs.updateFinal(admin);
            } else {
                //admin.setId(12345);
                admin.setFirstName("Administrador");
                admin.setCreatedOn(1);
                abs.saveFinal(admin);
            }
            loadAppConfig("initialConfig3.fxml", formId);
            closeModal();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar información de configuración", e.getMessage(), sw.toString(), "");
        }
    }

    /*
     Método para insertar o actualizar los datos del formulario
     @param: acción que genera la petición(onAction)
     */
    @FXML
    public void savePersonalization(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            ComboBoxChoices selectedPrintSize = printSize.getSelectionModel().getSelectedItem();
            ComboBoxChoices selectedLanguage = language.getSelectionModel().getSelectedItem();
            settings.setLogo(txtLogo.getText());
            settings.setPrintSize(selectedPrintSize.getItemValue());
            settings.setLanguage(selectedLanguage.getItemValue());
            abs.updateFinal(settings);
            loadAppConfig("initialConfig4.fxml", formId);
            closeModal();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar información de personalización", e.getMessage(), sw.toString(), "");
        }
    }

    /*
     Método para insertar o actualizar los datos del formulario
     @param: acción que genera la petición(onAction)
     */
    @FXML
    public void initiateApp(ActionEvent event) {
        loadAppConfig("login.fxml", formId);
        closeModal();
    }

    /*
     Método para inicializar el segundo formulario
     */
    public void loadAppConfig(String fxml, int form) {
        try {
            Locale es = new Locale("es", "ES");
            String title = "Configuración Inicial";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/" + fxml));
            fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
            Parent rootModal = (Parent) fxmlLoader.load();
            if (form == 0) {
                InitialConfigController conifgController = fxmlLoader.<InitialConfigController>getController();
                conifgController.startInicialConfig();
            } else if (form == 1) {
                InitialConfigController conifgController = fxmlLoader.<InitialConfigController>getController();
                conifgController.startAppConfig();
            } else if (form == 2) {
                InitialConfigController conifgController = fxmlLoader.<InitialConfigController>getController();
                conifgController.startPersonalization();
            } else if (form == 3) {
                InitialConfigController conifgController = fxmlLoader.<InitialConfigController>getController();
                conifgController.finishPersonalization();
            } else {
                title = "Login";
            }
            Stage window = new Stage();
            window.getIcons().add(new Image("/com/zafirodesktop/ui/img/ico/scirebox.bmp"));
            window.initStyle(StageStyle.UNDECORATED);
            window.setScene(new Scene(rootModal));
            window.setTitle(title);
            window.initModality(Modality.NONE);
            window.initOwner(mainPane.getScene().getWindow());
            closeModal();
            window.show();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Cargar formulario de configuración", e.getMessage(), sw.toString(), "");
        }
    }

    /*
     Método para ejecutar la acción de limpiar la ayuda en el combobox seleccionado
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void cleanComboBoxHelp(MouseEvent event) {
        ComboBox actualCombo = (ComboBox) event.getSource();
        switch (actualCombo.getId()) {
            case "type":
                lblValidate4.setVisible(false);
                break;
            case "printSize":
                lblValidate2.setVisible(false);
                saveButton.setDisable(false);
                break;
            default:
                lblValidate3.setVisible(false);
                break;
        }
    }

    /*
     Método para cargar el nuevo logo para la aplicación
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void loadImage(ActionEvent event) throws IOException {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecciona una imagen");
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
            File choosenFile = fileChooser.showOpenDialog(stage);
            BufferedImage bufferedImage = ImageIO.read(choosenFile);
            SwingFXUtils.toFXImage(bufferedImage, null);
            txtLogo.setText(choosenFile.getAbsolutePath());
            String path = System.getProperty("user.dir");
            path += "\\web-files\\logo3.jpg";
            File newLogo = new File(choosenFile.getAbsolutePath());
            if (newLogo.exists()) {
                File logoPath = new File(path);
                InputStream in = new FileInputStream(newLogo);
                OutputStream out = new FileOutputStream(logoPath);
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Cargar imagen", e.getMessage(), sw.toString(), "");
        }
    }

    /*
     Método para actualizar la vista en la que se encuentra actualmente
     */
    public void refreshStates(ImageView iv, ImageView im, ImageView ip, ImageView ig) {
        actualState.getChildren().clear();
        actualState.getChildren().addAll(iv, im, ip, ig);
    }
    
    /*
     Método para validar la contraseña por segunda vez
     */
    public void validatePasswdRetype(KeyEvent arg){
        TextField t = (TextField) arg.getSource();
        match(txtValidatePasswd, lblValidatePassword, t.getText(), txtPasswd.getText(), bundle);
        validateConfigForm();
    }

    /*
     Método para validar el campo bussines name
     */
    public void validateBN(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        if(showInformation(t, lblValidate1, length, "lblConfigBussinessName")){
            if(!txtSerial.getText().isEmpty())
                validateSerial(t, lblValidate1, t.getText(), "lblSerialBNError");
        }
        validateBussinesForm();
    }
    
    /*
     Método para validar que el serial escrito sea el entregado
     */
    public void validateSR(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        if(showInformation(t, lblValidateSr, length, "lblSettingsSerial")){
            validateSerial(t, lblValidateSr, txtBussinesName.getText(), "lblSerialError");
        }    
        validateBussinesForm();
    }

    /*
     Método para validar el campo firstName
     */
    public void validateNT(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        showInformation(txtNit, lblValidate2, length, "configBussinessNIT");
        validateBussinesForm();
    }

    /*
     Método para validar el campo firstName
     */
    public void validatePN(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        showInformation(txtPhoneNumber, lblValidate3, length, "configBussinessPhone");
        validateBussinesForm();
    }

    /*
     Método para validar el campo firstName
     */
    public void validateAD(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        showInformation(txtAddress, lblValidate4, length, "configBussinessAddress");
        validateBussinesForm();
    }

    /*
     Método para validar el campo firstName
     */
    public void validateCT(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        showInformation(txtCity, lblValidate5, length, "configBussinessCity");
        validateBussinesForm();
    }

    /*
     Método para validar el campo firstName
     */
    public void validateIA(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        if (showInformation(txtAdminId, lblValidate1, length, "lblConfigAppAdminId")) {
            validEmail(txtAdminId, lblValidate1, t.getText(), bundle);
        }
        validateConfigForm();
    }

    /*
     Método para validar el campo firstName
     */
    public void validateAP(KeyEvent arg) {
        if(!txtValidatePasswd.getText().isEmpty()){
            txtValidatePasswd.setText("");
        }
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        showInformation(txtPasswd, lblValidate2, length, "configAppPasswd");
        validateConfigForm();
    }

    /*
     Método para validar el campo firstName
     */
    public void validateLI(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        if (showInformation(txtLastInvoice, lblValidate3, length, "configAppLastInvoice")) {
            isNumeric(txtLastInvoice, lblValidate3, t.getText(), bundle);
        }
        validateConfigForm();
    }

    /*
     Método para validar que un campo no sea nulo
     @param: Campo a valida(TextField), label del mensaje(Label), 
     longitud del texto (int), bundle del idioma(ResourceBundle)
     */
    public boolean showInformation(TextField txtField, Label lbl, int length, String bundleMessage) {
        boolean valid;
        if (length < 1) {
            lbl.setVisible(true);
            lbl.setText(bundle.getString(bundleMessage));
            txtField.getStyleClass().add("txfValidateError");
            valid = false;
        } else {
            lbl.setVisible(false);
            txtField.getStyleClass().remove("txfValidateError");
            valid = true;
        }
        return valid;
    }
    
    /*
     Método para validar que el serial escrito es el asociado
     @param: Campo a valida(TextField), label del mensaje(Label), 
     longitud del texto (int), bundle del idioma(ResourceBundle)
     */
    public boolean validateSerial(TextField txtField, Label lbl, String bn, String bundleMessage) {
        boolean valid;
        String cifred = getSerial(bn.replaceAll("\\s+","").toLowerCase());
        String writed = txtField.getText().replace("-", "").toUpperCase();
        if (cifred.equals(writed)) {
            lbl.setVisible(false);
            txtField.getStyleClass().remove("txfValidateError");
            valid = true;
        } else {
            lbl.setVisible(true);
            lbl.setText(bundle.getString(bundleMessage));
            txtField.getStyleClass().add("txfValidateError");
            valid = false;
        }
        return valid;
    }
    
    /*
        Método para devolver la cadena del serial
    */
    private String getSerial(String bn){
        return DigestUtils.sha1Hex(bn).substring(0, 20).toUpperCase();
    }

    /*
     Método para validar que el formulario ya esta listo para ser enviado
     */
    void validateBussinesForm() {
        if (!lblValidate1.isVisible() && !lblValidate2.isVisible() && !lblValidate3.isVisible() && !lblValidate4.isVisible() && !lblValidate5.isVisible() && !lblValidateSr.isVisible()
                && !txtBussinesName.getText().isEmpty() && !txtNit.getText().isEmpty() && !txtPhoneNumber.getText().isEmpty()
                && !txtAddress.getText().isEmpty() && !txtCity.getText().isEmpty() && !txtSerial.getText().isEmpty()) {
            saveButton.setDisable(false);
        } else {
            saveButton.setDisable(true);
        }
    }

    /*
     Método para validar que el formulario ya esta listo para ser enviado
     */
    void validateConfigForm() {
        if (!lblValidate1.isVisible() && !lblValidate2.isVisible() && !lblValidate3.isVisible() && !lblValidatePassword.isVisible()
                && !txtAdminId.getText().isEmpty() && !txtPasswd.getText().isEmpty() && !txtLastInvoice.getText().isEmpty() && !txtValidatePasswd.getText().isEmpty()) {
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
    public void closeButtonAction(ActionEvent event) {
        closeModal();
    }

    /*
     Método para minimizar el modal iniciado sin guardar ninguna cambio, desde el boto cancelar
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    public void minimizeButtonAction(ActionEvent event) {
        Scene scene = mainPane.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.setIconified(true);
    }

    public void closeModal() {
        Scene scene = mainPane.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

}

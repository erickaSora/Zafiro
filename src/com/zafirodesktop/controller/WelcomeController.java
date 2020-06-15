/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.controller;

import com.zafirodesktop.entity.Settings;
import com.zafirodesktop.model.AbstractFacade;
import com.zafirodesktop.util.LogActions;
import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author andresforero
 */
public class WelcomeController implements Initializable{

    //Welcome
    @FXML
    public AnchorPane mainPane;
    @FXML
    public Button actionBt;
    @FXML
    public Label lblMessage;
    @FXML
    public ImageView warningIMG;
    
    //About
    @FXML
    private Button aboutLicense;
    @FXML
    private Button aboutPolicy;
    @FXML
    private Button aboutSupport;
    @FXML
    public Label lblRegisteredTo;
    @FXML
    public Label lblLicenseId;
    @FXML
    public CheckBox licenseAcept;
    
    
    
    public ResourceBundle bundle;
    private MainController mainController;
    private boolean isInitialConfig;
    private boolean isLicenseContract;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setIsInitialConfig(boolean isInitialConfig) {
        this.isInitialConfig = isInitialConfig;
    }

    public void setIsLicenseContract(boolean isLicenseContract) {
        this.isLicenseContract = isLicenseContract;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle=rb;
    }
    
    /*
        Incializar valores del formulario de acerca de
    */
    public void initializeAboutValues(){
        Settings settings = getCurrentSettings();
        lblRegisteredTo.setText(lblRegisteredTo.getText().concat(settings.getBussinesName()));
        String licenseId = settings.getBussinesName().trim().toUpperCase();
        licenseId = DigestUtils.md5Hex(licenseId).substring(0, 9);
        String labeled[] = {licenseId.substring(0,4),"-",licenseId.substring(4,7),"-",licenseId.substring(7,9)};
        lblLicenseId.setText(lblLicenseId.getText().concat(labeled[0]+labeled[1]+labeled[2]+labeled[3]+labeled[4]));
    }
    
    /*
        Método para inicializar los valores de configuración inicial, para la parte de contrato de licencia
    */
    public void initializeValuesLicense(){
        aboutLicense.setVisible(true);
        licenseAcept.setVisible(true);
        isInitialConfig = true;
        isLicenseContract = true;
    }
    
    /*
        Método para iniciar el módulo de configuración Inicial
    */
    public void startInicialConfig(){
        lblMessage.setText(bundle.getString("initialConfigMessage"));
        actionBt.setText(bundle.getString("startButton"));
    }
    
    /*
    Método para mostrar la notificación de error de conexión
    */
    public void displayConectionError(){
        lblMessage.setText(bundle.getString("bdConectionError"));
        actionBt.setVisible(false);
        warningIMG.setVisible(true);
        aboutSupport.setVisible(true);
    }
    
    /*
     Método para cargar el paso 1 de la configuración inicial
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    public void initiateConfig(ActionEvent event) throws IOException {
        try{
        Locale es = new Locale("es", "ES");
            String view = "licenseContract";
            String title = bundle.getString("licenseTitle");
            if(isInitialConfig && isLicenseContract){
                view = "initialConfig";
                title = bundle.getString("configBussinessInfoTitle");
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/"+view+".fxml"));
            fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
            Parent rootModal = (Parent) fxmlLoader.load();
            Stage window = new Stage();
            window.initStyle(StageStyle.UNDECORATED);
            window.getIcons().add(new Image("/com/zafirodesktop/ui/img/ico/scirebox.bmp"));
            window.setScene(new Scene(rootModal));
            window.setTitle(title);
            window.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            //Se comprueba se hasta ahora se está instalanado la app
            if(isInitialConfig){
                //Se comprueba si es la parte de bienvenida o si es el contrato de licencia
                if(isLicenseContract){
                    InitialConfigController conifgController = fxmlLoader.<InitialConfigController>getController();
                    conifgController.startInicialConfig();
                }else{
                    WelcomeController licenseController = fxmlLoader.<WelcomeController>getController();
                    licenseController.initializeValuesLicense();
                }
                window.initModality(Modality.NONE);
                closeModal();
            }else
                window.initModality(Modality.APPLICATION_MODAL);
            window.show();
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Iniciar configuración", e.getMessage(), sw.toString(), "");
            //e.printStackTrace();
        }     
    }
    
    /*
        Método para traer los valores actuales de la información del negocio
    */
    public Settings getCurrentSettings(){
        Settings settings = null;
        try{
            AbstractFacade abs = new AbstractFacade();
            settings = (Settings)abs.findByIdInt("Settings", 1);
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Iniciar acerca de", e.getMessage(), sw.toString(), "");
        }
        return settings;
    }
    
    /*
        Método para iniciar el cliente de correo electr{onico
    */
    public void openMailClient(){
        aboutSupport.setDisable(false);
        try {
            aboutSupport.setDisable(true);
            if(Desktop.isDesktopSupported()){
                Desktop desktop = Desktop.getDesktop();
                String message = "mailto:support@scirebox.com?subject=Solicitud%20de%20soporte";
                URI uri = URI.create(message);
                desktop.mail(uri);
            }
        } catch (IOException ex ) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(ex.hashCode()), "Contactar con el soporte", ex.getMessage(), sw.toString(), "");
        }
    }
    
    /*
        Método para gestionar las acciones del contrato de licencia
        @param: Evento que ejecuta la acción (onAction)
    */
    @FXML
    private void updateLicenseChoosed(ActionEvent event){
        if(licenseAcept.isSelected())
            aboutLicense.setDisable(false);
        else
            aboutLicense.setDisable(true);
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
        //Se oculta el tapiz del modal en caso de que aplique
        if(mainController!=null)
            mainController.showHideMask(false);
    }
    
}

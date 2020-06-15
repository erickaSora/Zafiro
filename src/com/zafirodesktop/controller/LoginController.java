/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.zafirodesktop.entity.Settings;
import com.zafirodesktop.entity.Turn;
import com.zafirodesktop.entity.Users;
import com.zafirodesktop.model.AbstractFacade;
import com.zafirodesktop.model.LoginModel;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.model.TurnModel;
import com.zafirodesktop.util.LogActions;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Digitall
 */
public class LoginController implements Initializable {

    @FXML
    public AnchorPane pane;
    @FXML
    public TextField username;
    @FXML
    public TextField passwd;
    @FXML
    public Button loginBt;
    @FXML
    public Label lblMessage;
    @FXML
    public CheckBox chkRemember;

    public ResourceBundle bundle;
    public Users user;
    public Settings settings;
    public boolean isCheckBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        user = new Users();
        username.setPromptText(bundle.getString("txtLoginUser"));
        passwd.setPromptText(bundle.getString("txtLoginPasswd"));
        AbstractFacade abs = new AbstractFacade();
        settings = (Settings) abs.findByIdInt("Settings", 1);
        if (settings != null) {
            username.setText(settings.getRememberedUser());
        }
        isCheckBox = false;
        /*try {
         SessionBD.persistenceCreate();
         } catch (Exception e) {
         setMessage("notConexion");
         }*/
    }

    public void login(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            LoginModel lm = new LoginModel();
            // Se busca el usuario de acuerdo a los datos ingresados
            user = lm.findByUserPasswd(username.getText(), passwd.getText());
            double initialAmount = 0.0;
            if (settings.getCashBox() > 0) {
                isCheckBox = true;
                String modalXML = "boxInitialCash";
                String modalTitle = bundle.getString("initialAmountTitle");
                //Parte para revisar que el usuario no tenga un turno activo
                TurnModel tm = new TurnModel();
                Collection<Turn> tc = tm.findByUserId(user.getId());
                Turn lastTurn = null;
                for (Turn turn : tc) {
                    lastTurn = turn;
                }
                if (lastTurn != null) {
                    modalXML = "dialog";
                    modalTitle = bundle.getString("alertTitle");
                }
                Stage modal = new Stage(StageStyle.DECORATED);
                Locale es = new Locale("es", "ES");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/" + modalXML + ".fxml"));
                fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
                Parent rootModal = (Parent) fxmlLoader.load();
                modal.setScene(new Scene(rootModal));
                modal.setTitle(modalTitle);
                modal.initModality(Modality.APPLICATION_MODAL);
                if (event != null) {
                    modal.initOwner(
                            ((Node) event.getSource()).getScene().getWindow());
                }
                modal.show();
                DialogController dialogController = fxmlLoader.<DialogController>getController();
                dialogController.setLoginController(this);
                if (lastTurn != null) {
                    dialogController.initializeValues("turnContinueMsg", DialogController.CONTINUE_TURN);
                    dialogController.initializeValuesContinueTurn(lastTurn);
                } else {
                    dialogController.setDisableActionButton(true);
                    dialogController.initializeValuesCashBox();
                }
            } else {
                loginAction(initialAmount, null);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Login", e.getMessage(), sw.toString(), "");
            e.printStackTrace();
            setMessage("loginInvalidUserMessage");
        }
    }
    /*
     Método para ejecutar la acción de logeo de acuerdo a las caractarísitas del usuario 
     */

    public void loginAction(Double initialAmount, Turn activeTurn) throws Exception {
        AbstractFacade abs = new AbstractFacade();
        Turn turn;
        //Se verifica que se vaya a continuar o a iniciar un turno
        if (initialAmount != null) {
            //Se crea un nuevo turno para el usuario que ha ingresado 
            turn = new Turn();
            turn.setInitialAmount(initialAmount);
            turn.setExpectedAmount(initialAmount);
            turn.setIdUserFk(user);
            abs.saveFinal(turn);
        } else {
            turn = activeTurn;
        }
        //Se redirecciona el usuario de acuerdo a los privilegios
        String viewType;
        if (user.getCreatedOn() > 0) {
            viewType = "main";
        } else {
            viewType = "mainChecker";
        }
        Locale es = new Locale("es", "ES");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/" + viewType + ".fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
        Parent rootModal = (Parent) fxmlLoader.load();
        MainController mainController = fxmlLoader.<MainController>getController();
        mainController.initializeValuesUser(turn);
        //Se especifica la vista a mostrar
        if (viewType.equals("mainChecker")) {
            mainController.initializeValuesChecker();
            mainController.setIsAdminView(false);
        } else {
            mainController.loadCharts();
            mainController.setIsAdminView(true);
        }
        //Se inicializa la variable que indica si se esta utilizando caja
        mainController.setUseCashBox(isCheckBox);
        mainController.hideRemoveCloseBox();
        mainController.hideRemoveChangeView();
        Stage window = new Stage();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        window.getIcons().add(new Image("/com/zafirodesktop/ui/img/ico/scirebox.bmp"));
        window.setX(bounds.getMinX());
        window.setY(bounds.getMinY());
        window.setWidth(bounds.getWidth());
        window.setHeight(bounds.getHeight());
        window.setScene(new Scene(rootModal));
        window.setTitle("ZafiroDesktop");
        window.initStyle(StageStyle.UNDECORATED);
        window.initModality(Modality.NONE);
        /*window.setFullScreen(true);
         window.initOwner(
         ((Node) event.getSource()).getScene().getWindow());*/
        closeModal();
        window.show();
        if (chkRemember.isSelected()) {
            settings.setRememberedUser(username.getText());
            abs.updateFinal(settings);
        } else {
            settings.setRememberedUser("");
            abs.updateFinal(settings);
        }
    }

    /*
     Método para cargar la vista principal luego de continuar un turno activo
     */
    public void closeTurn(Turn turn) throws IOException {
        Stage modal = new Stage(StageStyle.DECORATED);
        Locale es = new Locale("es", "ES");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/boxFinalCash.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
        Parent rootModal = (Parent) fxmlLoader.load();
        modal.setScene(new Scene(rootModal));
        modal.setTitle(bundle.getString("closeSessionTitle"));
        modal.initModality(Modality.APPLICATION_MODAL);
        DialogController dialogController = fxmlLoader.<DialogController>getController();
        dialogController.setLoginController(this);
        dialogController.initializeValuesCloseSession(turn, null);
        modal.show();
    }
    
    /*
     Método para cargar la vista para cambio de contraseña
     @param: Evento que ejecuta la acción (onAction event)
     */
    @FXML
    public void loadResetPassword(ActionEvent event) throws IOException {
        Stage modal = new Stage(StageStyle.DECORATED);
        Locale es = new Locale("es", "ES");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/resetPassword.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
        Parent rootModal = (Parent) fxmlLoader.load();
        modal.setScene(new Scene(rootModal));
        modal.setTitle(bundle.getString("ressetPasswordTitle"));
        modal.initModality(Modality.APPLICATION_MODAL);
        APIController apiController = fxmlLoader.<APIController>getController();
        apiController.initializeValuesRP();
        modal.show();
    }

    /*
     Método para cargar el modal de cerrar turno
     */
    public void closeTurnAction(Turn turn, Double realAmount) {
        try {
            AbstractFacade abs = new AbstractFacade();
            turn.setRealAmount(realAmount);
            turn.setEndDate(Calendar.getInstance().getTime());
            abs.updateFinal(turn);
            setMessage("closeTurnSuccess");
        } catch (Exception e) {
            setMessage("closeTurnError");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Cerrar turno", e.getMessage(), sw.toString(), turn.toString());
            e.printStackTrace();
        }
    }

    /*
     Método para iniciar la aplicación al darle enter
     */
    @FXML
    public void validMessage(KeyEvent event) {
        lblMessage.setVisible(false);
        if (event.getCode().equals(KeyCode.ENTER)) {
            login(null);
        }
    }

    public void setMessage(String bundleMessage) {
        lblMessage.setVisible(true);
        lblMessage.setText(bundle.getString(bundleMessage));
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
        Scene scene = pane.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.setIconified(true);
    }

    public void closeModal() {
        Scene scene = pane.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

}

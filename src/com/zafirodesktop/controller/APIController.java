/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.google.gson.Gson;
import com.zafirodesktop.entity.Settings;
import com.zafirodesktop.entity.Users;
import com.zafirodesktop.model.AbstractFacade;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.util.FormValidation;
import com.zafirodesktop.util.JSONResetPassword;
import com.zafirodesktop.util.LogActions;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Digitall
 */
public class APIController extends FormValidation implements Initializable {

    //Variables interfaz gráfica
    @FXML
    private Pane mainPane;
    @FXML
    private HBox boxEmail;
    @FXML
    private HBox boxCode;
    @FXML
    private HBox boxPasswd;
    @FXML
    private HBox boxNewPassw;
    @FXML
    private Button actionButon;
    @FXML
    private Label lblInfo;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtCode;
    @FXML
    private PasswordField txtPasswd;
    @FXML
    private PasswordField txtPasswdConfirm;
    @FXML
    private Label vldEmail;
    @FXML
    private Label vldCode;
    @FXML
    private Label vldPasswd;
    @FXML
    private Label vldPasswdConfirm;

    //Variables controlador
    private ResourceBundle bundle;
    private boolean hasCode;
    private AbstractFacade abs;
    private Settings settings;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
    }

    /*
     Método para inicializar los valores del formulario resetPassword
     */
    public void initializeValuesRP() {
        lblInfo.setText(bundle.getString("ressetPasswordInfo"));
        txtEmail.setPromptText(bundle.getString("lblResetPaswdEmail"));
        txtCode.setPromptText(bundle.getString("lblResetPaswdCode"));
        txtPasswd.setPromptText(bundle.getString("lblResetPaswdNew"));
        txtPasswdConfirm.setPromptText(bundle.getString("lblResetPaswdNewConf"));
    }

    /*
     Método solicitar al API el cambio de contraseña
     @param: evento que ejecuta la acción (onAction event)
     */
    public void resetPasswordAction(ActionEvent event) {
        if (!hasCode) {
            //Se realiza la petición a la API de recuperar contraseña
            try {
                SessionBD.persistenceCreate();
                abs = new AbstractFacade();
                settings = (Settings) abs.findByIdInt("Settings", 1);
                String url = "http://api.scirebox.com/public/forgotpasswd?subdomain=" + settings.getSubdomain() + "&email=" + txtEmail.getText();
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Gson gson = new Gson();
                JSONResetPassword rp = gson.fromJson(response.toString(), JSONResetPassword.class);
                if (rp.getStatus().equals("error")) {
                    //System.out.println("Error: "+rp.getErrors());
                    lblInfo.setText(bundle.getString("resetPasswordError"));
                } else {
                    lblInfo.setText(bundle.getString("resetPasswordSucces"));
                    hasCode = true;
                    txtEmail.setEditable(false);
                    boxCode.setVisible(true);
                    boxPasswd.setVisible(true);
                    boxNewPassw.setVisible(true);
                }
                actionButon.setDisable(true);
            } catch (Exception e) {
                lblInfo.setText(bundle.getString("resetPasswordError"));
                actionButon.setDisable(true);
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                LogActions log = new LogActions();
                log.createEntry(String.valueOf(e.hashCode()), "Enviar código al email para reseteo", e.getMessage(), sw.toString(), txtEmail.getText());
                e.printStackTrace();
            }
        } else {
            //Se envía el Hash recibido y la nueva contraseña a la API
            try {
                String url = "http://api.scirebox.com/public/changepasswd?hash=" + txtCode.getText() + "&subdomain=" + settings.getSubdomain() + "&passwd=" + txtPasswd.getText();
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Gson gson = new Gson();
                JSONResetPassword rp = gson.fromJson(response.toString(), JSONResetPassword.class);
                if (rp.getStatus().equals("error")) {
                    System.out.println("Error: "+rp.getErrors());
                    lblInfo.setText(bundle.getString("resetPasswordError2"));
                    actionButon.setDisable(true);
                } else {
                    Users user = (Users) abs.findByName("Users", txtEmail.getText());
                    user.setPassword(txtPasswd.getText());
                    abs.updateFinal(user);
                    lblInfo.setText(bundle.getString("resetPasswordSucces2"));
                    boxEmail.setVisible(false);
                    boxCode.setVisible(false);
                    boxPasswd.setVisible(false);
                    boxNewPassw.setVisible(false);
                    actionButon.setDisable(true);
                }
            } catch (Exception e) {
                lblInfo.setText(bundle.getString("resetPasswordError2"));
                actionButon.setDisable(true);
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                LogActions log = new LogActions();
                log.createEntry(String.valueOf(e.hashCode()), "Aplicando cambio de contraseña", e.getMessage(), sw.toString(), txtEmail.getText());
                e.printStackTrace();
            }
        }

    }

    /*
     Método para ocultar o 
     */

    /*
     Método para validar el campo email
     */
    public void validateEM(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        if (notNull(txtEmail, vldEmail, length, bundle, bundle.getString("personEmailTltp"))) {
            validEmail(txtEmail, vldEmail, t.getText(), bundle);
        }
        validForm();
    }

    /*
     Método para validar el código
     */
    public void validateCO(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        notNull(txtCode, vldCode, length, bundle, bundle.getString("codeCheckerTltp"));
        validForm();
    }

    /*
     Método para validar el campo passwd
     */
    public void validatePW(KeyEvent arg) {
        if (!txtPasswdConfirm.getText().isEmpty()) {
            txtPasswdConfirm.setText("");
        }
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        notNull(txtPasswd, vldPasswd, length, bundle, bundle.getString("passwdTltp"));
        validForm();
    }

    /*
     Método para validar la contraseña por segunda vez
     */
    public void validatePasswdRetype(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        match(txtPasswdConfirm, vldPasswdConfirm, t.getText(), txtPasswd.getText(), bundle);
        validForm();
    }

    /*
     Método para validar que el formulario ya esta listo para ser enviado
     */
    void validForm() {
        if (!hasCode) {
            if (!vldEmail.isVisible() && !txtEmail.getText().isEmpty()) {
                actionButon.setDisable(false);
            } else {
                actionButon.setDisable(true);
            }
        } else {
            if (!vldCode.isVisible() && !txtCode.getText().isEmpty() && !vldPasswd.isVisible() && !txtPasswd.getText().isEmpty()
                    && !vldPasswdConfirm.isVisible() && !txtPasswdConfirm.getText().isEmpty()) {
                actionButon.setDisable(false);
            } else {
                actionButon.setDisable(true);
            }
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
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

}

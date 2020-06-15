/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.util;

import com.zafirodesktop.model.AbstractFacade;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.model.SettingsModel;
import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.regex.Pattern;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;

/**
 *
 * @author Digitall
 */
public class FormValidation {
    
    private boolean valid;
    private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String DECIMAL_PATTERN = "[-+]?(\\d+)|(\\d*\\.\\d+)";
    
    /*
    Método para validar que un campo no sea nulo
    @param: Campo a valida(TextField), label del mensaje(Label), 
    longitud del texto (int), bundle del idioma(ResourceBundle)
    */
    public boolean notNull(TextField txtField, Label lbl, int length, ResourceBundle bundle, String message){
        if(length<1){
            lbl.setVisible(true);
            lbl.setText(message);
            txtField.getStyleClass().add("txfValidateError");
            valid = false;
        }else{
            lbl.setVisible(false);
            txtField.getStyleClass().remove("txfValidateError");
            valid = true;
        }
        return valid;
    }
    
    /*
    Método para validar que un campo no exceda cierto número de caractéres
    @param: Campo a valida(TextField), label del mensaje(Label), 
    longitud del texto (int), bundle del idioma(ResourceBundle)
    */
    public boolean maxLenght(TextField txtField, Label lbl, int length, int maxLenght, ResourceBundle bundle){
        if(length>maxLenght){
            lbl.setVisible(true);
            lbl.setText(bundle.getString("maxLenght"));
            txtField.getStyleClass().add("txfValidateError");
            valid = false;
        }else{
            lbl.setVisible(false);
            txtField.getStyleClass().remove("txfValidateError");
            valid = true;
        }
        return valid;
    }
    
    /*
    Método para validar que un campo no exceda cierto rango de números
    @param: Campo a validar(TextField), label del mensaje(Label),  
    número mínimo (int), número máximo (int), bundle del idioma(ResourceBundle)
    */
    public boolean numericRange(TextField txtField, Label lbl, float min, float max, ResourceBundle bundle){
        float num = Float.parseFloat(txtField.getText());
        if(num<=min || num>max){
            lbl.setVisible(true);
            lbl.setText(bundle.getString("numericRange"));
            txtField.getStyleClass().add("txfValidateError");
            valid = false;
        }else{
            lbl.setVisible(false);
            txtField.getStyleClass().remove("txfValidateError");
            valid = true;
        }
        return valid;
    }
    
    /*
    Método para validar que un campo no exceda cierto número de caractéres de un TextArea
    @param: Campo a validar(TextArea), label del mensaje(Label), 
    longitud del texto (int), bundle del idioma(ResourceBundle)
    */
    public boolean maxLenghtTextArea(TextArea txtField, Label lbl, int length, int maxLenght, ResourceBundle bundle){
        if(length>maxLenght){
            lbl.setVisible(true);
            lbl.setText(bundle.getString("maxLenght"));
            txtField.getStyleClass().add("txfValidateError");
            valid = false;
        }else{
            lbl.setVisible(false);
            txtField.getStyleClass().remove("txfValidateError");
            valid = true;
        }
        return valid;
    }
    
    /*
    Método para validar que un campo text area no sea nulo
    @param: Campo a validar(TextArea), label del mensaje(Label), 
    longitud del texto (int), bundle del idioma(ResourceBundle)
    */
    public boolean notNullTextArea(TextArea txtField, Label lbl, int length, ResourceBundle bundle, String message){
        if(length<1){
            lbl.setVisible(true);
            lbl.setText(message);
            txtField.getStyleClass().add("txfValidateError");
            valid = false;
        }else{
            lbl.setVisible(false);
            txtField.getStyleClass().remove("txfValidateError");
            valid = true;
        }
        return valid;
    }
    
    /*
    Método para validar que un campo sea numerico
    @param: Campo a validar(TextField), label del mensaje(Label), submit del formulario(Button), 
    Texto del campo (String), bundle del idioma(ResourceBundle)
    */
    public boolean isNumeric(TextField txtField, Label lbl, String text, ResourceBundle bundle){
        if(!Pattern.matches("\\d+", text)){
            lbl.setVisible(true);
            lbl.setText(bundle.getString("numeric"));
            txtField.getStyleClass().add("txfValidateError");
            valid = false;
        }else{
            lbl.setVisible(false);
            txtField.getStyleClass().remove("txfValidateError");
            valid = true;
        }
        return valid;
    }
    
    /*
    Método para validar que un campo sea decimal
    @param: Campo a validar(TextField), label del mensaje(Label), submit del formulario(Button), 
    Texto del campo (String), bundle del idioma(ResourceBundle)
    */
    public boolean isDecimal(TextField txtField, Label lbl, String text, ResourceBundle bundle){
        if(!Pattern.matches(DECIMAL_PATTERN, text)){
            lbl.setVisible(true);
            lbl.setText(bundle.getString("decimal"));
            txtField.getStyleClass().add("txfValidateError");
            valid = false;
        }else{
            lbl.setVisible(false);
            txtField.getStyleClass().remove("txfValidateError");
            valid = true;
        }
        return valid;
    }
    
    /*
    Método para validar que un campo de abono no sea mayor al valor faltante
    @param: Campo a validar(TextField), label del mensaje(Label), submit del formulario(Button), 
    Texto del campo (String), valor restante (Float), bundle del idioma(ResourceBundle)
    */
    public boolean isLowerLeftAmount(TextField txtField, Label lbl, String text, Double leftAmount, ResourceBundle bundle){
        double value = Double.parseDouble(text);
        if(value>leftAmount){
            lbl.setVisible(true);
            lbl.setText(bundle.getString("lowerLeftAmount"));
            txtField.getStyleClass().add("txfValidateError");
            valid = false;
        }else{
            lbl.setVisible(false);
            txtField.getStyleClass().remove("txfValidateError");
            valid = true;
        }
        return valid;
    }
    
    /*
    Método para validar que un id a registrar no exista ya en la base de datos
    @param: Campo a validar(TextField), label del mensaje(Label), clase a buscar (String), 
    Texto del campo (String), bundle del idioma(ResourceBundle)
    */
    public boolean idExists(TextField txtField, Label lbl, String clas, String text, ResourceBundle bundle){
        SessionBD.persistenceCreate();
        AbstractFacade abs = new AbstractFacade();
        Object ob = abs.findByIdString(clas, text);
        if(ob!=null){
        lbl.setVisible(true);
        lbl.setText(bundle.getString("idExits"));
        txtField.getStyleClass().add("txfValidateError");
        return false;
        } else {
            lbl.setVisible(false);
            txtField.getStyleClass().remove("txfValidateError");
            return true;
        }
    }
    
    
    
    /*
    Método para validar que un id entero a registrar no exista ya en la base de datos
    @param: Campo a validar(TextField), label del mensaje(Label), clase a buscar (String), 
    Texto del campo (String), bundle del idioma(ResourceBundle)
    */
    public boolean idExistsInt(TextField txtField, Label lbl, String clas, String text, ResourceBundle bundle){
        SessionBD.persistenceCreate();
        AbstractFacade abs = new AbstractFacade();
        try {
            abs.findByIdInt(clas, Integer.parseInt(text));
            lbl.setVisible(true);
            lbl.setText(bundle.getString("idExits"));
            txtField.getStyleClass().add("txfValidateError");
            return false;
        } catch (Exception e) {
            lbl.setVisible(false);
            txtField.getStyleClass().remove("txfValidateError");
            return true;
        }
    }
    
    /*
    Método para validar que un id a registrar no exista ya en la base de datos
    @param: Campo a validar(TextField), label del mensaje(Label), clase a buscar (String), 
    Texto del campo (String), bundle del idioma(ResourceBundle)
    */
    public boolean nameExists(TextField txtField, Label lbl, String clas, String text, ResourceBundle bundle){
        SessionBD.persistenceCreate();
        AbstractFacade abs = new AbstractFacade();
        try {
            abs.findByName(clas, text);
            lbl.setVisible(true);
            lbl.setText(bundle.getString("idExits"));
            txtField.getStyleClass().add("txfValidateError");
            return false;
        } catch (Exception e) {
            lbl.setVisible(false);
            txtField.getStyleClass().remove("txfValidateError");
            return true;
        }
    }
    
    /*
    Método para validar que un id a registrar no exista ya en la base de datos
    @param: Campo a validar(TextField), label del mensaje(Label), clase a buscar (String), 
    Texto del campo (String), bundle del idioma(ResourceBundle)
    */
    public boolean lastInvoiceExists(TextField txtField, Label lbl, String text, ResourceBundle bundle){
        SessionBD.persistenceCreate();
        SettingsModel sm = new SettingsModel();
        if(sm.findLastInvoice(text)!=null){
            lbl.setVisible(true);
            lbl.setText(bundle.getString("idExits"));
            txtField.getStyleClass().add("txfValidateError");
            valid = false;
        }else{
            lbl.setVisible(false);
            txtField.getStyleClass().remove("txfValidateError");
            valid = true;
        }
        return valid;
    }
    
    /*
    Método para validar que el texto de un campo sea igual al de otro campo
    @param: Campo a validar(TextField), label del mensaje(Label), clase a buscar (String), 
    Texto del campo (String), bundle del idioma(ResourceBundle)
    */
    public boolean match(TextField txtField, Label vldLabel, String txt1, String txt2, ResourceBundle bundle){
        if(txt1.equals(txt2)){
            vldLabel.setVisible(false);
            txtField.getStyleClass().remove("txfValidateError");
            valid = true;
        }else{
            vldLabel.setVisible(true);
            vldLabel.setText(bundle.getString("notMatch"));
            txtField.getStyleClass().add("txfValidateError");
            valid = false;
        }
        
        return valid;
    }
    
    /*
        Método para ocultar o mostrar los tooltip de ayuda
        @param: tooltip a mostrar/ocultar: Tooltip helpTltp, nodo sobre el cual mostrar la ayuda: Node node
    */
    public void showHideTooltips(Tooltip tlpHelp, Button helpButton){
        if(!tlpHelp.isShowing()){
            if(!helpButton.getStyleClass().contains("helpPressed")){
                helpButton.getStyleClass().add("helpPressed");
            }
            tlpHelp.show(helpButton, 
                helpButton.getScene().getWindow().getX() +helpButton.getLayoutX()+helpButton.getWidth()-10,
                helpButton.getScene().getWindow().getY() +helpButton.getLayoutY()+helpButton.getHeight());
        }else{
            if(helpButton.getStyleClass().contains("helpPressed")){
                helpButton.getStyleClass().remove("helpPressed");
            }
            tlpHelp.hide();
        }
        
    }
    
    /*
    Método para validar que un email sea correcto
    @param: Campo a validar(TextField), label del mensaje(Label)
    Texto del campo (String), bundle del idioma(ResourceBundle)
    */
    public boolean validEmail(TextField txtField, Label lbl, String text, ResourceBundle bundle){
        if(!Pattern.matches(EMAIL_PATTERN, text)){
            lbl.setVisible(true);
            lbl.setText(bundle.getString("validEmail"));
            txtField.getStyleClass().add("txfValidateError");
            valid = false;
        }else{
            lbl.setVisible(false);
            txtField.getStyleClass().remove("txfValidateError");
            valid = true;
        }
        return valid;
    }
    
}

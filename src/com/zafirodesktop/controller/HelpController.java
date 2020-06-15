/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.zafirodesktop.util.LogActions;
import com.zafirodesktop.util.PrintPDF;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Digitall
 */
public class HelpController implements Initializable {

    /*
     Especificación de los componentes asociados en el FXML
     */
    //Inicio
    @FXML
    private Button toNextPage;
    @FXML
    private Button toBackPage;
    @FXML
    private Button manual;
    @FXML
    private Label loginT;
    @FXML
    private Label initialBoxAmountT;
    @FXML
    private Label dashboardT;
    @FXML
    private Label menuT;
    @FXML
    private Label configurationT;
    @FXML
    private Label importInventoryT;
    @FXML
    private Label itemDataTable1T;
    @FXML
    private Label itemDataTable2T;
    @FXML
    private Label itemDataTable3T;
    @FXML
    private Label itemDataTable4T;
    @FXML
    private Label newItemT;
    @FXML
    private Label reportT;
    @FXML
    private Label newInvoiceT;
    @FXML
    private Label mainCheckerT;
    @FXML
    private Label closeBoxT;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private HBox actualState;
    @FXML
    private Stage stage;
    @FXML
    private ImageView page1;
    @FXML
    private ImageView principalImage;
    @FXML
    private ImageView page2;
    @FXML
    private ImageView page3;
    @FXML
    private ImageView page4;
    @FXML
    private ImageView page5;
    @FXML
    private ImageView page6;
    @FXML
    private ImageView page7;
    @FXML
    private ImageView page8;
    @FXML
    private ImageView page9;
    @FXML
    private ImageView page10;
    @FXML
    private ImageView page11;
    @FXML
    private ImageView page12;

    /*
     *Especificación variables del controlador
     */
    private ResourceBundle bundle;
    private String moduleID;
    private MainController mainController;
    private int state;
    Image actual;
    Image next;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        state = 1;
        actual = new Image("com/zafirodesktop/ui/img/actualPage.png");
        next = new Image("com/zafirodesktop/ui/img/nextPage.png");
    }

    /*
     Para actualizar el estado actual y mostrar la imagen y el texto correspondiente
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void menuButtonAction(ActionEvent event) {
        Button actualButton = (Button) event.getSource();
        moduleID = actualButton.getId();
        if(moduleID.equals("toNextPage")){
            state +=1;
        }else{
            state -=1;
        }
        refreshData();
        if(state==1){
            loginT.setVisible(true);
            principalImage.setImage(new Image("com/zafirodesktop/ui/img/loginT.png"));
            page1.setImage(new Image("com/zafirodesktop/ui/img/actualPage.png"));
            toBackPage.setVisible(false);
        }else if(state==2){
            initialBoxAmountT.setVisible(true);
            principalImage.setImage(new Image("com/zafirodesktop/ui/img/boxInitialAmountT.png"));
            page2.setImage(new Image("com/zafirodesktop/ui/img/actualPage.png"));
            toBackPage.setVisible(true);
        }else if(state==3){
            dashboardT.setVisible(true);
            principalImage.setImage(new Image("com/zafirodesktop/ui/img/dashboardT.png"));
            page3.setImage(new Image("com/zafirodesktop/ui/img/actualPage.png"));
        }else if(state==4){
            menuT.setVisible(true);
            principalImage.setImage(new Image("com/zafirodesktop/ui/img/menuT.png"));
            page4.setImage(new Image("com/zafirodesktop/ui/img/actualPage.png"));
        }else if(state==5){
            configurationT.setVisible(true);
            principalImage.setImage(new Image("com/zafirodesktop/ui/img/configurationT.png"));
            page5.setImage(new Image("com/zafirodesktop/ui/img/actualPage.png"));
        }else if(state==6){
            importInventoryT.setVisible(true);
            principalImage.setImage(new Image("com/zafirodesktop/ui/img/importInventoryT.png"));
            page6.setImage(new Image("com/zafirodesktop/ui/img/actualPage.png"));
        }else if(state==7){
            itemDataTable1T.setVisible(true);
            itemDataTable2T.setVisible(true);
            itemDataTable3T.setVisible(true);
            itemDataTable4T.setVisible(true);
            principalImage.setImage(new Image("com/zafirodesktop/ui/img/itemDataTableT.png"));
            page7.setImage(new Image("com/zafirodesktop/ui/img/actualPage.png"));
        }else if(state==8){
            newItemT.setVisible(true);
            principalImage.setImage(new Image("com/zafirodesktop/ui/img/newItemT.png"));
            page8.setImage(new Image("com/zafirodesktop/ui/img/actualPage.png"));
        }else if(state==9){
            newInvoiceT.setVisible(true);
            principalImage.setImage(new Image("com/zafirodesktop/ui/img/newInvoiceT.png"));
            page9.setImage(new Image("com/zafirodesktop/ui/img/actualPage.png"));
        }else if(state==10){
            reportT.setVisible(true);
            principalImage.setImage(new Image("com/zafirodesktop/ui/img/reportT.png"));
            page10.setImage(new Image("com/zafirodesktop/ui/img/actualPage.png"));
        }else if(state==11){
            mainCheckerT.setVisible(true);
            principalImage.setImage(new Image("com/zafirodesktop/ui/img/mainCheckerT.png"));
            page11.setImage(new Image("com/zafirodesktop/ui/img/actualPage.png"));
            toNextPage.setVisible(true);
        }else{
            closeBoxT.setVisible(true);
            principalImage.setImage(new Image("com/zafirodesktop/ui/img/closeBoxT.png"));
            page12.setImage(new Image("com/zafirodesktop/ui/img/actualPage.png"));
            toNextPage.setVisible(false);
        } 
    }

    /*
     Método para ocultar todos los labels y actualizar todos los state 
     */
    @FXML
    private void refreshData() {
        //Textos
        loginT.setVisible(false);
        initialBoxAmountT.setVisible(false);
        dashboardT.setVisible(false);
        menuT.setVisible(false);
        configurationT.setVisible(false);
        importInventoryT.setVisible(false);
        itemDataTable1T.setVisible(false);
        itemDataTable2T.setVisible(false);
        itemDataTable3T.setVisible(false);
        itemDataTable4T.setVisible(false);
        newItemT.setVisible(false);
        newInvoiceT.setVisible(false);
        reportT.setVisible(false);
        mainCheckerT.setVisible(false);
        closeBoxT.setVisible(false);
        //Estados
        page1.setImage(next);
        page2.setImage(next);
        page3.setImage(next);
        page4.setImage(next);
        page5.setImage(next);
        page6.setImage(next);
        page7.setImage(next);
        page8.setImage(next);
        page9.setImage(next);
        page10.setImage(next);
        page11.setImage(next);
        page12.setImage(next);
    }
    
    /*
     Método para abrir el manual del usuario en PDF
     @param: evento que ejecuta la acción(onAction)
     */
    public void openManual() {
        try {
            PrintPDF pdf = new PrintPDF();
            pdf.openPDF("manual.pdf");
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Abrir manual", e.getMessage(), sw.toString(), "");
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
        mainController.showHideMask(false);
        Scene scene = mainPane.getScene();
        stage = (Stage) scene.getWindow();
        stage.close();
    }

}

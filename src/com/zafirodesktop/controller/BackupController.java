/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.zafirodesktop.entity.Concept;
import com.zafirodesktop.entity.Product;
import com.zafirodesktop.entity.Remission;
import com.zafirodesktop.entity.Stock;
import com.zafirodesktop.entity.TransactionDetail;
import com.zafirodesktop.entity.TransactionDetailPK;
import com.zafirodesktop.entity.Tranzaction;
import com.zafirodesktop.entity.Turn;
import com.zafirodesktop.entity.Warehouse;
import com.zafirodesktop.model.Conexion;
import com.zafirodesktop.model.ProductModel;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.model.SettingsModel;
import com.zafirodesktop.model.TransactionDetailModel;
import com.zafirodesktop.model.TranzactionModel;
import com.zafirodesktop.util.FormValidation;
import com.zafirodesktop.util.LogActions;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Digitall
 */
public class BackupController extends FormValidation implements Initializable {
    /*
     Especificación de los componentes asociados en el FXML
     */

    @FXML
    private Pane mainPane;
    @FXML
    private TextField backupPath;
    @FXML
    private Label description;
    @FXML
    private Button generateButton;
    @FXML
    private Button importButton;
    @FXML
    private Button closeButton;
    @FXML
    private Button helpButton;
    @FXML
    private ImageView loading;
    @FXML
    private Tooltip tlpHelp;
    @FXML
    private Stage stage;

    /*
     *Especificación variables del controlador
     */
    private MainController mainController;
    private ResourceBundle bundle;
    private Turn sessionUser;

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        backupPath.setText(bundle.getString("lblFileSelection"));
        Image img = new Image("/com/zafirodesktop/ui/img/ico/pointer_black.png");
        ImageView imgvw = new ImageView(img);
        imgvw.setFitHeight(15);
        imgvw.setFitWidth(15);
        tlpHelp.setGraphic(imgvw);
    }
    
    /*
        Método para inicializar los valores del formulario de importe de inventario
        @param: Turno del usuario que realiza la acción: Turn user
    */
    public void initializeValues(Turn user){
        sessionUser = user;
    }

    /*
     Método para ejecutar la creación del Backup
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void generateBackup(ActionEvent event) {
        description.setText(bundle.getString("backupDescription"));
        generateButton.setDisable(true);
        importButton.setDisable(true);
        closeButton.setDisable(true);
        loading.setVisible(true);
        backUpDatabase(backupPath.getText());
    }

    /*
     Método para ejecutar la importación de un archivo csv
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void importData(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            SettingsModel sm = new SettingsModel();
            int lastProductID = sm.maxProductId();
            String path = backupPath.getText();
            sm.importData(path);
            Collection<Product> col = sm.findAllImportedProducts(lastProductID);
            if (col.isEmpty()) {
                mainController.setMessage(bundle.getString("importDataError"), true);
                closeModal();
            } else {
                Concept concept = (Concept) sm.findByIdInt("Concept", 8);
                Warehouse warehouse = (Warehouse) sm.findByIdInt("Warehouse", 1);
                Remission remission = new Remission();
                remission.setStatus(new Short("1"));
                remission.setInvoiced(new Short("0"));
                remission.setIdConceptFk(concept);
                remission.setObs(bundle.getString("inventoryImportObs"));
                remission.setIdTurnFk(sessionUser);
                sm.saveIntermediate(remission);
                Tranzaction tranzaction = new Tranzaction();
                tranzaction.setIdRemissionFk(remission);
                tranzaction.setIdWarehouseFk(warehouse);
                TranzactionModel tm = new TranzactionModel();
                tm.saveTranzaction(tranzaction, remission);
                TransactionDetailModel tdm = new TransactionDetailModel();
                ProductModel pm = new ProductModel();
                for (Product product : col) {
                    TransactionDetail td = new TransactionDetail(new TransactionDetailPK(product.getIdProductPk(), tranzaction.getIdTransactionPk()));
                    td.setProduct(product);
                    td.setTranzaction(tranzaction);
                    td.setAmount(product.getCantidadSeleccionada());
                    td.setUnitPrice(0.0);
                    tdm.saveTransactionDetail(td, tranzaction);
                    Stock stock = (Stock) tdm.findByIdInt("Stock", product.getIdProductPk());
                    stock.setAmount(stock.getAmount() + product.getCantidadSeleccionada());
                    pm.updateStock(stock, product);
                    product.setCantidadSeleccionada(1);
                    pm.updateIntermediate(product);
                }
                pm.executeCommit();
                mainController.setMessage(bundle.getString("importDataSucces"), false);
                closeModal();
            }
        } catch (Exception e) {
            mainController.setMessage(bundle.getString("importDataError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Importar productos", e.getMessage(), sw.toString(), sessionUser.toString());
            closeModal();
            e.printStackTrace();
        }
    }

    /*
     Método para ejecutar la importación del Backup
     */
    @FXML
    public void importBackup() {
        description.setText(bundle.getString("backupImport"));
        generateButton.setDisable(true);
        importButton.setDisable(true);
        closeButton.setDisable(true);
        loading.setVisible(true);
        String path = backupPath.getText();
        //Se comprueba que se haya seleccioanda la carpeta de la BD, en caso contrario se le agrega
        if(!path.contains("\\zafiro"))
            path+="\\zafiro";
        restoreDatabase(path);
    }

    /*
     Método para crear un backup online de la BD
     @param: ruta de la carpeta en donde se va a guardar el backup (String path)
     */
    public void backUpDatabase(String path) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy hh_mm aaa");
            String backupdirectory = path + "/scireboxBK_" + sdf.format(new Date());
            SessionBD.persistenceCreate();
            SettingsModel sm = new SettingsModel();
            sm.backupBD(backupdirectory);
            mainController.setMessage(bundle.getString("generateSucces"), false);
            closeModal();
        } catch (Exception e) {
            mainController.setMessage(bundle.getString("generateError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Generar backup", e.getMessage(), sw.toString(), sessionUser.toString());
            closeModal();
            //e.printStackTrace();
        }
    }

    /*
    
     */
    public void restoreDatabase(String path) {
        try {
            Conexion con = new Conexion();
            con.shutdownBD();
            boolean resp = con.restoreBD(path);
            closeModal();
            mainController.logOut(null, 0.0, false, false, true);
        } catch (Exception e) {
            mainController.setMessage(bundle.getString("importError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Restaurar base de datos", e.getMessage(), sw.toString(), sessionUser.toString());
            closeModal();
            //e.printStackTrace();
        }
    }

    /*
     Método para cargar la ruta de backup
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void selectBackupPath(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(bundle.getString("directorySelection"));
        File chosenDirectory = directoryChooser.showDialog(stage);
        backupPath.setText(chosenDirectory.getPath());
        generateButton.setDisable(false);
        importButton.setDisable(false);
    }

    /*
     Método para cargar la ruta de backup
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void selectImportPath(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(bundle.getString("csvSelection"));
        File chosenFile = fileChooser.showOpenDialog(new Stage());
        backupPath.setText(chosenFile.getPath());
        generateButton.setDisable(false);
    }

    /*
     Método para cargar el diálogo de confirmación 
     @param: evento que ejecute la acción (onAction)
     */
    public void showConfirmationDialog(ActionEvent event) throws IOException {
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
        dialogController.setBackupController(this);
        dialogController.initializeValues("importDatabaseMessage", DialogController.IMPORT);
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

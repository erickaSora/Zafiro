/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.itextpdf.text.DocumentException;
import com.zafirodesktop.entity.Invoice;
import com.zafirodesktop.entity.Payment;
import com.zafirodesktop.entity.Person;
import com.zafirodesktop.entity.Place;
import com.zafirodesktop.entity.Remission;
import com.zafirodesktop.entity.Settings;
import com.zafirodesktop.model.AbstractFacade;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.util.ComboBoxChoices;
import com.zafirodesktop.util.FormValidation;
import com.zafirodesktop.util.LogActions;
import com.zafirodesktop.util.PrintPDF;
import com.zafirodesktop.util.ProductConverter;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author Digitall
 */
public class SettingsController extends FormValidation implements Initializable {

    /*
     Especificación de los componentes asociados en el FXML
     */
    @FXML
    private AnchorPane mainPane;
    @FXML
    private TextField bussinesName;
    @FXML
    private TextField logo;
    @FXML
    private TextArea header;
    @FXML
    private TextArea footer;
    @FXML
    private TextField nit;
    @FXML
    private TextField lastInvoice;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField address;
    @FXML
    private TextField city;
    @FXML
    private TextField country;
    @FXML
    private ComboBox language;
    @FXML
    private ComboBox<ComboBoxChoices> printSize;
    @FXML
    private ComboBox<ComboBoxChoices> type;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button loadButton;
    @FXML
    private Button previewButton;
    @FXML
    private Button helpButton;
    @FXML
    private Label vldLastInvoice;
    @FXML
    private Tooltip tlpHelp;
    @FXML
    private Stage stage;

    private boolean exists;
    private boolean isPreview;
    private AbstractFacade abs;
    private Settings setting;
    private MainController mainController;
    private ResourceBundle bundle;

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /*
     Metodo inicial obligatorio del controlador, por medio del cual se inicializan
     todos los valores que hacen parte del FXML
     @param: ruta(URL), bundle de idioma (ResourceBundle)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        nit.setPromptText(bundle.getString("lblSettingsNit"));
        phoneNumber.setPromptText(bundle.getString("lblSettingsPhoneNumber"));
        address.setPromptText(bundle.getString("lblSettingsAddress"));
        city.setPromptText(bundle.getString("lblSettingsCity"));
        country.setPromptText(bundle.getString("lblSettingsCountry"));
        lastInvoice.setPromptText(bundle.getString("lblSettingsLastInvoice"));
        header.setPromptText(bundle.getString("lblSettingsHeader"));
        footer.setPromptText(bundle.getString("lblSettingsFooter"));
        logo.setPromptText(bundle.getString("lblSettingsLogo"));
        ObservableList<ComboBoxChoices> printSizesList = FXCollections.observableArrayList(
                new ComboBoxChoices("TK", bundle.getString("lblSettingsPrintTk")),
                new ComboBoxChoices("HL", bundle.getString("lblSettingsPrintHF"))
        );
        ObservableList<ComboBoxChoices> languageList = FXCollections.observableArrayList(
                new ComboBoxChoices("es", "Español")
        );
        printSize.getItems().addAll(printSizesList);
        printSize.getSelectionModel().selectFirst();
        ObservableList<ComboBoxChoices> typesList = FXCollections.observableArrayList(
                new ComboBoxChoices("0", "No, no utilizo caja"),
                new ComboBoxChoices("1", "Si, utilizo caja")
        );
        type.getItems().addAll(typesList);
        type.getSelectionModel().selectFirst();
        language.getItems().addAll(languageList);
        language.getSelectionModel().selectFirst();
        Image img = new Image("/com/zafirodesktop/ui/img/ico/pointer_black.png");
        ImageView imgvw = new ImageView(img);
        imgvw.setFitHeight(15);
        imgvw.setFitWidth(15);
        tlpHelp.setGraphic(imgvw);
        initializeValues();
    }

    /*
     Método para inicializar los valores del formulario, en caso de que ya existan datos
     */
    public void initializeValues() {
        setting = new Settings();
        int id = 1;
        try {
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            Collection<Settings> lista = abs.findAll("Settings");
            if (!lista.isEmpty()) {
                setting = (Settings) abs.findByIdInt("Settings", id);
                bussinesName.setText(setting.getBussinesName());
                logo.setText(setting.getLogo());
                header.setText(setting.getNoteHeader());
                footer.setText(setting.getNoteFooter());
                nit.setText(setting.getNit());
                lastInvoice.setText(setting.getLastInvoice());
                phoneNumber.setText(setting.getPhoneNumber());
                address.setText(setting.getAddress());
                city.setText(setting.getCity());
                country.setText(setting.getCountry());
                String printSizeName = bundle.getString("lblSettingsPrintHF");
                if (setting.getPrintSize().equals("TK")) {
                    printSizeName = bundle.getString("lblSettingsPrintTk");
                }
                ComboBoxChoices selectedType = new ComboBoxChoices(setting.getPrintSize(), printSizeName);
                printSize.getSelectionModel().select(selectedType);
                String cashBoxTypeName = "Si, utilizo caja";
                if(setting.getCashBox()==0){
                    cashBoxTypeName = "No, no utilizo caja";
                }
                ComboBoxChoices selectedBoxType = new ComboBoxChoices(setting.getCashBox().toString(), cashBoxTypeName);
                type.getSelectionModel().select(selectedBoxType);
                //language.getSelectionModel().select(setting.getLanguage());
                exists = true;
            } else {
                exists = false;
                saveButton.setDisable(true);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Inicializar configuración", e.getMessage(), sw.toString(), "");
            closeModal();
            mainController.setMessage(bundle.getString("loadModuleError"), true);
        }
    }

    /*
     Método para cerrar el modal iniciado sin guardar ninguna cambio
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void insertSettings(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            ComboBoxChoices languageChoiced = (ComboBoxChoices) language.getSelectionModel().getSelectedItem();
            ComboBoxChoices printSizeChoiced = (ComboBoxChoices) printSize.getSelectionModel().getSelectedItem();
            ComboBoxChoices cashBoxChoiced = (ComboBoxChoices) type.getSelectionModel().getSelectedItem();
            setting.setBussinesName(bussinesName.getText());
            if(logo!=null||!logo.getText().isEmpty())
                setting.setLogo(logo.getText());
            setting.setNoteHeader(header.getText());
            setting.setNoteFooter(footer.getText());
            setting.setNit(nit.getText());
            setting.setLastInvoice(lastInvoice.getText());
            setting.setPhoneNumber(phoneNumber.getText());
            setting.setAddress(address.getText());
            setting.setCity(city.getText());
            setting.setCountry(country.getText());
            setting.setLanguage(languageChoiced.getItemValue());
            setting.setPrintSize(printSizeChoiced.getItemValue());
            setting.setCashBox(new Short(cashBoxChoiced.getItemValue()));
            if (!exists) {
                int id = 1;
                setting.setId(id);
                abs.saveFinal(setting);
                //Se comprueba que no se esta generando un preview
                if(!isPreview){
                    closeModal();
                    getMainController().setMessage(bundle.getString("createSuccess"), false);
                }
            } else {
                abs.updateFinal(setting);
                //Se comprueba que no se esta generando un preview
                if(!isPreview){
                    closeModal();
                    getMainController().setMessage(bundle.getString("updateSuccess"), false);
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar configuración", e.getMessage(), sw.toString(), "");
            closeModal();
            getMainController().setMessage(bundle.getString("createError"), true);
            e.printStackTrace();
        }
    }

    /*
     Método para validar el campo lastInvoice
     */
    public void validateLI(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        String text = t.getText();
        if (isNumeric(lastInvoice, vldLastInvoice, text, bundle)) {
            int nextInvoice = Integer.parseInt(text) + 1;
            idExists(lastInvoice, vldLastInvoice, "Invoice", String.valueOf(nextInvoice), bundle);
        }
        validForm();
    }

    /*
     Método para validar que el formulario ya esta listo para ser enviado
     */
    void validForm() {
        if (!vldLastInvoice.isVisible() && !lastInvoice.getText().isEmpty()) {
            saveButton.setDisable(false);
        } else {
            saveButton.setDisable(true);
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
            logo.setText(choosenFile.getAbsolutePath());
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
            log.createEntry(String.valueOf(e.hashCode()), "Cargar logo", e.getMessage(), sw.toString(), "");
        }
    }

    /*
     Método para cargar en PDF la plantilla de la facutra 
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void printPreview(ActionEvent event) throws FileNotFoundException, DocumentException, IOException {
        isPreview = true;
        try {
            insertSettings(event);
            PrintPDF printPDF = new PrintPDF();
            Payment pay = new Payment(1, "Efectivo", 0);
            Invoice invoice = new Invoice("X");
            invoice.setIdPaymentFk(pay);
            invoice.setObs("XXXXXXXXXXXXXX");
            Remission remission = new Remission(1);
            Place place = new Place(1, "XXXXXX", "M");
            Person person = new Person(1, "XXXXXXX", "C");
            person.setNit("XXXXXXXXXX");
            person.setPersonAddress("XXXXXXXXXX");
            person.setPersonPhoneNo("XXXXXXXXXX");
            person.setIdPlaceFk(place);
            remission.setIdClientFk(person);
            remission.setIdInvoiceFk(invoice);
            remission.setIdTurnFk(mainController.getSessionUser());
            ProductConverter product = new ProductConverter(1, 0.0,"XXXXXXX");
            product.setAmount(1);
            product.setTaxesCollection(null);
            List<ProductConverter> list = new ArrayList<>();
            list.add(product);

            printPDF.printInvoice(null, remission, null, list, null, 2, bundle, true);
            isPreview = false;
        } catch (DocumentException | PrinterException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Imprimir vista previa", e.getMessage(), sw.toString(), "");
            closeModal();
            getMainController().setMessage(bundle.getString("printerError"), true);
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.zafirodesktop.entity.Annotation;
import com.zafirodesktop.entity.ItemCategory;
import com.zafirodesktop.entity.Product;
import com.zafirodesktop.entity.ProductCategory;
import com.zafirodesktop.entity.ProductTaxes;
import com.zafirodesktop.entity.ProductTaxesPK;
import com.zafirodesktop.entity.Remission;
import com.zafirodesktop.entity.Tax;
import com.zafirodesktop.model.AbstractFacade;
import com.zafirodesktop.model.ProductModel;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.util.ComboBoxChoices;
import com.zafirodesktop.util.FormValidation;
import com.zafirodesktop.util.LogActions;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Digitall
 */
public class TaxController extends FormValidation implements Initializable {

    /*
     Especificación de los componentes asociados en el FXML
     */
    @FXML
    private AnchorPane mainPane;
    @FXML
    private TextField name;
    @FXML
    private TextField pct;
    @FXML
    private TextArea annotationObs;
    @FXML
    private Label vldName;
    @FXML
    private Label title;
    @FXML
    private Label vldPct;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button associateButton;
    @FXML
    private ComboBox<ComboBoxChoices> group;
    @FXML
    private ComboBox<ComboBoxChoices> category;
    @FXML
    private HBox buttonBox;
    @FXML
    private HBox categoryBox;
    @FXML
    private Button helpButton;
    @FXML
    private Tooltip tlpHelp;

    /*
     Objetos y variables del controlador 
     */
    private static final int ALL = 0;
    private static final int SUBCATEGORY = 1;
    private static final int DEPARTMENT = 2;
    private ResourceBundle bundle;
    private MainController mainController;
    private InvoiceController invoiceController;
    private ProductTaxesController ptController;
    private TaxController dadController;
    private AbstractFacade abs;
    private Tax actualTax;
    private Remission actualRemission;
    private boolean exists;

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setInvoiceController(InvoiceController invoiceController) {
        this.invoiceController = invoiceController;
    }

    public void setPtController(ProductTaxesController ptController) {
        this.ptController = ptController;
    }

    public void setDadController(TaxController dadController) {
        this.dadController = dadController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        Image img = new Image("/com/zafirodesktop/ui/img/ico/pointer_black.png");
        ImageView imgvw = new ImageView(img);
        imgvw.setFitHeight(15);
        imgvw.setFitWidth(15);
        tlpHelp.setGraphic(imgvw);
    }

    /*
     Método para inicializar los valores del formulario Tax
     */
    public void initializeTax() {
        title.setText(bundle.getString("taxTitle"));
        name.setPromptText(bundle.getString("lblTaxName"));
        pct.setPromptText(bundle.getString("lblTaxPercent"));
        buttonBox.getChildren().remove(associateButton);
        buttonBox.getChildren().remove(deleteButton);
    }

    /*
     Método para inicializar los valores del formulario ApplyTax
     @param: impuesto a asociar (Tax selectedTax)
     */
    public void initializeApplyTax(Tax selectedTax) {
        actualTax = selectedTax;
        title.setText(actualTax.toString());
        ObservableList<ComboBoxChoices> typesList = FXCollections.observableArrayList(
                new ComboBoxChoices("0", bundle.getString("lblTaxGroupOption1")),
                new ComboBoxChoices("1", bundle.getString("lblTaxGroupOption2")),
                new ComboBoxChoices("2", bundle.getString("lblTaxGroupOption3"))
        );
        group.getItems().addAll(typesList);
        group.getSelectionModel().selectFirst();
        category.setPromptText(bundle.getString("lblNoSubCategory"));
    }

    /*
     Método para inicializar los valores del formulario Annotation
     */
    public void initializeAnnotation(Remission remission) {
        annotationObs.setPromptText(bundle.getString("lblAnnotationObs"));
        actualRemission = remission;
    }

    /*
     Método para inicializar los valores de acuerdo al Item Seleccionado
     */
    public void initializeExists(Tax tax) {
        actualTax = tax;
        exists = true;
        title.setText(bundle.getString("taxUpdateTitle"));
        name.setText(actualTax.getTaxName());
        pct.setText(String.valueOf(actualTax.getTaxPct()));
        saveButton.setDisable(false);
        buttonBox.getChildren().add(1, associateButton);
        buttonBox.getChildren().add(2, deleteButton);
    }

    /*
     Método para insertar o actualizar los datos del formulario
     @param: acción que genera la petición(onAction)
     */
    @FXML
    private void saveTax(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            if (!exists) {
                actualTax = new Tax();
            }
            actualTax.setTaxName(name.getText());
            actualTax.setTaxPct(new Float(pct.getText()));
            if (exists) {
                abs.updateFinal(actualTax);
                if (mainController != null) {
                    getMainController().setMessage(bundle.getString("updateSuccess"), false);
                } else {
                    ptController.refreshDataList();
                }
            } else {
                abs.saveFinal(actualTax);
                if (mainController != null) {
                    getMainController().setMessage(bundle.getString("createSuccess"), false);
                } else {
                    ptController.refreshDataList();
                }
            }
            if (mainController != null) {
                getMainController().setDataTableView();
            }
            closeModal();
        } catch (Exception e) {
            getMainController().setMessage(bundle.getString("createError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar impuesto", e.getMessage(), sw.toString(), "");
            closeModal();
        }
    }

    /*
     Método para insertar o actualizar los datos del formulario
     @param: acción que genera la petición(onAction)
     */
    @FXML
    private void saveAnnotation(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            Annotation annotation = new Annotation();
            annotation.setDescription(annotationObs.getText());
            annotation.setIdRemissionFk(actualRemission);
            abs.saveFinal(annotation);
            actualRemission.getAnnotationCollection().add(annotation);
            invoiceController.annotationDataTableRefresh();
            closeModal();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Guardar anotación", e.getMessage(), sw.toString(), "");
            closeModal();
            e.printStackTrace();
        }
    }

    /*
     Método para insertar o actualizar los datos del formulario
     @param: acción que genera la petición(onAction)
     */
    @FXML
    private void applyTax(ActionEvent event) {
        SessionBD.persistenceCreate();
        ProductModel pdm = new ProductModel();
        try {
            ComboBoxChoices selectedGroup = group.getSelectionModel().getSelectedItem();
            //De acuerdo a la opción seleccionada, trae el grupo de productos correspondiente
            int choiced = Integer.parseInt(selectedGroup.getItemValue());
            Collection<Product> data;
            if (choiced == DEPARTMENT) {
                ComboBoxChoices categoryChoiced = category.getSelectionModel().getSelectedItem();
                data = pdm.findByDepartment(Integer.parseInt(categoryChoiced.getItemValue()));
            } else if (choiced == SUBCATEGORY) {
                ComboBoxChoices categoryChoiced = category.getSelectionModel().getSelectedItem();
                Collection<ProductCategory> prodCategory = pdm.findByItemCategory(Integer.parseInt(categoryChoiced.getItemValue()));
                //Se buscan las subcategorías y se agregan los productos asociados
                data = new ArrayList<>();
                for (ProductCategory prodCategory1 : prodCategory) {
                    data.add(prodCategory1.getProduct());
                }
            } else {
                data = pdm.findAll("Product");
            }
            for (Product data1 : data) {
                //Se comprueba que el impuesto no este asociado ya al producto
                ProductTaxes temp = (ProductTaxes) pdm.findByIdString("ProductTaxes", data1.getIdProductPk().toString() + actualTax.getIdTaxPk());
                if (temp==null) {
                    ProductTaxesPK actualProductTaxPX = new ProductTaxesPK();
                    ProductTaxes actualProductTax = new ProductTaxes();
                    actualProductTaxPX.setIdProductFk(data1.getIdProductPk());
                    actualProductTaxPX.setIdTaxFk(actualTax.getIdTaxPk());
                    actualProductTax.setProductTaxesPK(actualProductTaxPX);
                    actualProductTax.setProduct(data1);
                    actualProductTax.setTax(actualTax);
                    actualProductTax.setId(data1.getIdProductPk().toString() + actualTax.getIdTaxPk());
                    data1.addProductTaxes(actualProductTax);
                    pdm.saveTax(actualProductTax, data1);
                }
            }
            pdm.executeCommit();
            closeModal();
            dadController.closeModal();
            dadController.getMainController().setMessage(bundle.getString("applyTaxesSuccess"), false);
        } catch (Exception e) {
            vldName.setText(bundle.getString("applyTaxesError"));
            vldName.setVisible(true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Aplicar impuesto a grupo de productos", e.getMessage(), sw.toString(), "");
            e.printStackTrace();
        }

    }

    /*
     Método para borrar el dato actual
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void deleteTax(ActionEvent event) {
        try {
            abs = new AbstractFacade();
            abs.deleteIntFinal("Tax", actualTax.getIdTaxPk());
            getMainController().setDataTableView();
            getMainController().setMessage(bundle.getString("deleteSuccess"), false);
            closeModal();
        } catch (Exception e) {
            getMainController().setMessage(bundle.getString("deleteError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Eliminar impuesto", e.getMessage(), sw.toString(), "");
            closeModal();
        }
    }
    
    /*
     Método para desasociar el impuesto de los productos en los que esté
     @param: evento que ejecute la acción (onAction)
    */
    @FXML
    private void deassociateTax(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            ProductModel pdm = new ProductModel();
            Collection<ProductTaxes> pt = pdm.findByTaxes(actualTax.getIdTaxPk());
            if(!pt.isEmpty()){
                for (ProductTaxes pt1 : pt) {
                    Product product = pt1.getProduct();
                    product.removeProductTaxes(pt1);
                    pdm.deleteTax(pt1.getId(), product);
                }                
            }
            closeModal();
            dadController.closeModal();
            dadController.getMainController().setMessage(bundle.getString("deassociateTaxesSuccess"), false);
        } catch (Exception e) {
            vldName.setText(bundle.getString("deassociateTaxesError"));
            vldName.setVisible(true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Desasociar impuesto de grupo de productos", e.getMessage(), sw.toString(), "");
            e.printStackTrace();
        }
    }

    /*
     Método para cargar el modal de selección de un 
     @param: evento que ejecute la acción (onAction)
     */
    public void insertNewItemModal(ActionEvent event) throws IOException {
        Stage modal = new Stage(StageStyle.DECORATED);
        Locale es = new Locale("es", "ES");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/applyTax.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
        Parent rootModal = (Parent) fxmlLoader.load();
        modal.setScene(new Scene(rootModal));
        modal.setTitle("Item");
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(
                ((Node) event.getSource()).getScene().getWindow());
        modal.show();
        TaxController taxController = fxmlLoader.<TaxController>getController();
        taxController.initializeApplyTax(actualTax);
        taxController.setDadController(this);
    }

    /*
     Método para recargar los valores del combobox group
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void itemSelected(ActionEvent event) {
        vldName.setVisible(false);
        ComboBoxChoices selectedGroup = group.getSelectionModel().getSelectedItem();
        if (selectedGroup.getItemValue().equals("0")) {
            categoryBox.setVisible(false);
            saveButton.setDisable(false);
        } else {
            categoryBox.setVisible(true);
            categorySelected(Integer.parseInt(selectedGroup.getItemValue()));
        }
    }

    /*
     Método para recargar los valores del combobox cateogry
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void categorySelected(int choiced) {
        category.getItems().clear();
        String toFind = "Department";
        if (choiced == SUBCATEGORY) {
            toFind = "ItemCategory";
        }
        Collection data;
        abs = new AbstractFacade();
        data = abs.findAll(toFind);
        if (data.isEmpty()) {
            saveButton.setDisable(true);
        } else {
            ObservableList<ComboBoxChoices> comboList = FXCollections.observableArrayList();
            if (toFind.equals("Department")) {
                for (Object dep : data) {
                    Product prod = (Product)dep;
                    comboList.add(new ComboBoxChoices(prod.getIdProductPk().toString(), prod.getProductReference()));
                }
                category.getItems().addAll(comboList);
                category.getSelectionModel().selectFirst();
                saveButton.setDisable(false);
            }else{
                for (Object dep : data) {
                    ItemCategory item = (ItemCategory)dep;
                    comboList.add(new ComboBoxChoices(item.getIdItemCategoryPk().toString(), item.toString()));
                }
                category.getItems().addAll(comboList);
                category.getSelectionModel().selectFirst();
                saveButton.setDisable(false);
            }
        }

    }

    /*
     Método para validar el campo name
     */
    public void validateNM(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        int length = t.getText().length();
        if (notNull(name, vldName, length, bundle, bundle.getString("taxNameTltp"))) {
            if (nameExists(name, vldName, "Tax", t.getText(), bundle)) {
                maxLenght(name, vldName, length, 50, bundle);
            }
        }
        validForm();
    }

    /*
     Método para validar el campo pct
     */
    public void validatePCT(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        if (isDecimal(pct, vldPct, t.getText(), bundle)) {
            if(maxLenght(pct, vldPct, t.getLength(), 10, bundle)){
                numericRange(pct, vldPct, 0, new Float("99.9"), bundle);
            }
        }
        validForm();
    }

    /*
     Método para validar el campo name
     */
    public void validateOBS(KeyEvent arg) {
        TextArea t = (TextArea) arg.getSource();
        int length = t.getText().length();
        notNullTextArea(annotationObs, vldName, length, bundle, bundle.getString("lblAnnotationObs"));
        validateAnnotationForm();
    }

    /*
     Método para validar que el formulario ya esta listo para ser enviado
     */
    void validForm() {
        if (!name.getText().isEmpty() && !pct.getText().isEmpty() && !vldName.isVisible()
                && !vldPct.isVisible()) {
            saveButton.setDisable(false);
        } else {
            saveButton.setDisable(true);
        }
    }

    /*
     Método para validar que el formulario ya esta listo para ser enviado
     */
    void validateAnnotationForm() {
        if (!annotationObs.getText().isEmpty() && !vldName.isVisible()) {
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
    private void closeButtonAction(ActionEvent event) {
        if (mainController != null) {
            mainController.setDataTableView();
        }
        closeModal();
    }

    /*
     Método para ocultar o mostrar la ayuda a través de la acción de un Botón
     @param: Acción que ejecuta la acción: Action onPress
     */
    @FXML
    private void showHideTooltip(ActionEvent event) {
        showHideTooltips(tlpHelp, helpButton);
    }

    private void closeModal() {
        if (mainController != null) {
            mainController.showHideMask(false);
        }
        Scene scene = mainPane.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

}

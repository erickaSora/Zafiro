/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.zafirodesktop.entity.Discount;
import com.zafirodesktop.entity.ItemCategory;
import com.zafirodesktop.entity.Product;
import com.zafirodesktop.entity.ProductCategory;
import com.zafirodesktop.entity.ProductDiscount;
import com.zafirodesktop.entity.ProductDiscountPK;
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
public class DiscountsController extends FormValidation implements Initializable{

    //FXML attributes
    @FXML
    private AnchorPane mainPane;
    @FXML
    private TextField pct;
    @FXML
    private TextArea obs;
    @FXML
    private Label title;
    @FXML
    private Label vldPct;
    @FXML
    private Label vldObs;
    @FXML
    private Button helpButton;
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
    private HBox categoryBox;
    @FXML
    private HBox buttonBox;
    @FXML
    private Tooltip tlpHelp;
    
    //Controller attributes
    private static final int ALL = 0;
    private static final int SUBCATEGORY = 1;
    private static final int DEPARTMENT = 2;
    private ResourceBundle bundle;
    private MainController mainController;
    private ProductDiscountController pdController;
    private DiscountsController dadController;
    private AbstractFacade abs;
    private Discount actualDiscount;
    private boolean exists;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setPdController(ProductDiscountController pdController) {
        this.pdController = pdController;
    }

    public void setDadController(DiscountsController dadController) {
        this.dadController = dadController;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle=rb;
        Image img = new Image("/com/zafirodesktop/ui/img/ico/pointer_black.png");
        ImageView imgvw = new ImageView(img);
        imgvw.setFitHeight(15);
        imgvw.setFitWidth(15);
        tlpHelp.setGraphic(imgvw);
    }
    
    /*
     Método para inicializar los valores del formulario Discount
     */
    public void initializeValues() {
        title.setText(bundle.getString("discountTitle"));
        pct.setPromptText(bundle.getString("lblDiscountPercent"));
        obs.setPromptText(bundle.getString("lblDiscountObs"));
        buttonBox.getChildren().remove(associateButton);
        buttonBox.getChildren().remove(deleteButton);
        saveButton.setDisable(true);
    }
    
    /*
     Método para inicializar los valores de acuerdo al Item Seleccionado
     */
    public void initializeValuesExists(Discount discount) {
        actualDiscount = discount;
        exists = true;
        title.setText(bundle.getString("discountUpdateTitle"));
        obs.setText(actualDiscount.getDiscountDescrption());
        pct.setText(String.valueOf(actualDiscount.getDiscountPct()));
        saveButton.setDisable(false);
        buttonBox.getChildren().add(1, associateButton);
        buttonBox.getChildren().add(2, deleteButton);
    }
    
    /*
     Método para inicializar los valores del formulario ApplyDiscount
     @param: descuento a asociar (Discount selectedDiscount)
     */
    public void initializeApplyDiscount(Discount selectedDiscount) {
        actualDiscount = selectedDiscount;
        title.setText(actualDiscount.toString());
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
     Método para insertar o actualizar los datos del formulario
     @param: acción que genera la petición(onAction)
     */
    @FXML
    private void saveDiscount(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            abs = new AbstractFacade();
            if (!exists) {
                actualDiscount = new Discount();
            }
            actualDiscount.setDiscountDescrption(obs.getText());
            actualDiscount.setDiscountPct(new Float(pct.getText()));
            if (exists) {
                abs.updateFinal(actualDiscount);
                if (mainController != null) {
                    mainController.setMessage(bundle.getString("updateSuccess"), false);
                } else {
                    //nothing to do
                }
            } else {
                abs.saveFinal(actualDiscount);
                if (mainController != null) {
                    mainController.setMessage(bundle.getString("createSuccess"), false);
                } else {
                    pdController.refreshDataList();
                }
            }
            if (mainController != null) {
                mainController.setDataTableView();
            }
            closeModal();
        } catch (Exception e) {
            mainController.setMessage(bundle.getString("createError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Registrar descuento", e.getMessage(), sw.toString(), "");
            closeModal();
        }
    }
    
    /*
     Método para insertar o actualizar los datos del formulario
     @param: acción que genera la petición(onAction)
     */
    @FXML
    private void applyDiscount(ActionEvent event) {
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
                ProductDiscount temp = (ProductDiscount) pdm.findByIdString("ProductDiscount", data1.getIdProductPk().toString() + actualDiscount.getIdDiscountPk());
                if (temp==null) {
                    ProductDiscountPK actualProductDiscountPX = new ProductDiscountPK();
                    ProductDiscount actualProductDiscount = new ProductDiscount();
                    actualProductDiscountPX.setIdProductFk(data1.getIdProductPk());
                    actualProductDiscountPX.setIdDiscountFk(actualDiscount.getIdDiscountPk());
                    actualProductDiscount.setProductDiscountPK(actualProductDiscountPX);
                    actualProductDiscount.setProduct(data1);
                    actualProductDiscount.setDiscount(actualDiscount);
                    actualProductDiscount.setId(data1.getIdProductPk().toString() + actualDiscount.getIdDiscountPk());
                    data1.addProductDiscount(actualProductDiscount);
                    pdm.saveDiscount(actualProductDiscount, data1);
                }
            }
            pdm.executeCommit();
            closeModal();
            dadController.closeModal();
            dadController.mainController.setMessage(bundle.getString("applyDiscountSuccess"), false);
        } catch (Exception e) {
            vldObs.setText(bundle.getString("applyDiscountError"));
            vldObs.setVisible(true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Aplicar descuentos a grupo de productos", e.getMessage(), sw.toString(), "");
            //e.printStackTrace();
        }

    }
    
    /*
     Método para borrar el dato actual
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void deleteDiscount(ActionEvent event) {
        try {
            abs = new AbstractFacade();
            abs.deleteIntFinal("Discount", actualDiscount.getIdDiscountPk());
            mainController.setDataTableView();
            mainController.setMessage(bundle.getString("deleteSuccess"), false);
            closeModal();
        } catch (Exception e) {
            mainController.setMessage(bundle.getString("deleteError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Borrar descuento", e.getMessage(), sw.toString(), "");
            closeModal();
        }
    }
    
    /*
     Método para desasociar el descuento de los productos en los que esté
     @param: evento que ejecute la acción (onAction)
    */
    @FXML
    private void deassociateDiscount(ActionEvent event) {
        try {
            SessionBD.persistenceCreate();
            ProductModel pdm = new ProductModel();
            Collection<ProductDiscount> pt = pdm.findByDiscount(actualDiscount.getIdDiscountPk());
            if(!pt.isEmpty()){
                for (ProductDiscount pt1 : pt) {
                    Product product = pt1.getProduct();
                    product.removeProductDiscount(pt1);
                    pdm.deleteDiscount(pt1.getId(), product);
                }                
            }
            closeModal();
            dadController.closeModal();
            dadController.mainController.setMessage(bundle.getString("deassociateDiscountSuccess"), false);
        } catch (Exception e) {
            vldObs.setText(bundle.getString("applyTaxesError"));
            vldObs.setVisible(true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Desasociar descuento de grupo de productos", e.getMessage(), sw.toString(), "");
            //e.printStackTrace();
        }
    }
    
    /*
     Método para cargar el modal de selección de un 
     @param: evento que ejecute la acción (onAction)
     */
    public void insertNewItemModal(ActionEvent event) throws IOException {
        Stage modal = new Stage(StageStyle.DECORATED);
        Locale es = new Locale("es", "ES");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zafirodesktop/ui/applyDiscount.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("com.zafirodesktop.util.Bundle", es));
        Parent rootModal = (Parent) fxmlLoader.load();
        modal.setScene(new Scene(rootModal));
        modal.setTitle("Item");
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(
                ((Node) event.getSource()).getScene().getWindow());
        modal.show();
        DiscountsController discountController = fxmlLoader.<DiscountsController>getController();
        discountController.initializeApplyDiscount(actualDiscount);
        discountController.setDadController(this);
    }
    
    /*
     Método para recargar los valores del combobox group
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void itemSelected(ActionEvent event) {
        vldPct.setVisible(false);
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
     Método para validar el campo pct
     */
    public void validatePCT(KeyEvent arg) {
        TextField t = (TextField) arg.getSource();
        if (isDecimal(pct, vldPct, t.getText(), bundle)) {
            if(maxLenght(pct, vldPct, t.getLength(), 23, bundle)){
                numericRange(t, vldPct, 0, new Float("99.9"), bundle);
            }
        }
        validForm();
    }
    
    /*
     Método para validar el campo obs
     */
    public void validateNM(KeyEvent arg) {
        TextArea t = (TextArea) arg.getSource();
        int length = t.getText().length();
        if(notNullTextArea(obs, vldObs, length, bundle, bundle.getString("discountObsTltp")))
            maxLenghtTextArea(obs, vldObs, length, 300, bundle);
        validForm();
    }
    
    /*
     Método para validar que el formulario ya esta listo para ser enviado
     */
    void validForm() {
        if (!pct.getText().isEmpty() && !obs.getText().isEmpty() && !vldObs.isVisible()
                && !vldPct.isVisible()) {
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

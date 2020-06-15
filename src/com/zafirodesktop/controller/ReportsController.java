/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.controller;

import com.itextpdf.text.DocumentException;
import com.zafirodesktop.entity.FacturasAnuladas;
import com.zafirodesktop.entity.Remission;
import com.zafirodesktop.entity.TotalesEgresos;
import com.zafirodesktop.entity.TotalesIngresos;
import com.zafirodesktop.entity.Tranzaction;
import com.zafirodesktop.model.AbstractFacade;
import com.zafirodesktop.model.ReportsModel;
import com.zafirodesktop.model.TranzactionModel;
import com.zafirodesktop.util.FormValidation;
import com.zafirodesktop.util.LogActions;
import com.zafirodesktop.util.PrintPDF;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import static java.util.Calendar.HOUR;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author Digitall
 */
public class ReportsController extends FormValidation implements Initializable {

    public static final long HOUR = 3600*1000;
    
    @FXML
    private AnchorPane mainPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private Button helpButton;
    @FXML
    private Tooltip tlpHelp;
    private DatePicker startDate;
    private DatePicker endDate;
    private int reportType;

    public static final int ENTRIES_REPORT = 1;
    public static final int EXPENSES_REPORT = 2;
    public static final int CLOSE_BOX_REPORT = 6;
    public static final int INVOICES_REPORT = 7;
    public static final int ORDERS_REPORT = 8;
    public static final int EARNINGS_REPORT = 9;
    private MainController mainController;
    private boolean hasDiscounts;
    private boolean hasTaxes;
    private AbstractFacade abs;
    private PrintPDF pdf;
    private ResourceBundle bundle;

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public boolean isHasDiscounts() {
        return hasDiscounts;
    }

    public void setHasDiscounts(boolean hasDiscounts) {
        this.hasDiscounts = hasDiscounts;
    }

    public boolean doHasTaxes() {
        return hasTaxes;
    }

    public void setHasTaxes(boolean hasTaxes) {
        this.hasTaxes = hasTaxes;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
    }

    public void initializeValues(int type) {
        reportType = type;
        startDate = new DatePicker(Locale.getDefault());
        startDate.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        startDate.getCalendarView().todayButtonTextProperty().set("Hoy");
        startDate.getCalendarView().setShowWeeks(false);
        startDate.getStylesheets().add("com/zafirodesktop/ui/css/DatePicker.css");
        gridPane.add(startDate, 0, 1);
        //Se inicializa solamente una fecha en caso de que el tipo sea CLOSE_BOX_REPORT
        if (type != CLOSE_BOX_REPORT) {
            endDate = new DatePicker(Locale.getDefault());
            endDate.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            endDate.getCalendarView().todayButtonTextProperty().set("Hoy");
            endDate.getCalendarView().setShowWeeks(false);
            endDate.getStylesheets().add("com/zafirodesktop/ui/css/DatePicker.css");
            gridPane.add(endDate, 1, 1);
        }
    }

    /*
     Método para iniciar los valores del formulario de 
     */
    public void initializeHelp() {
        Image img = new Image("/com/zafirodesktop/ui/img/ico/pointer_black.png");
        ImageView imgvw = new ImageView(img);
        imgvw.setFitHeight(15);
        imgvw.setFitWidth(15);
        tlpHelp.setGraphic(imgvw);
    }
    
    /*
     Método para cargar el pdf seleccionado en caso que existan datos
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void generateReport(ActionEvent event) throws DocumentException, IOException {
        try {
            Date finishDate = null;
            if (reportType != CLOSE_BOX_REPORT) {
                finishDate = new Date(endDate.getSelectedDate().getTime() + 23 * HOUR);
            }
            if (reportType == 1) {
                entriesReport(startDate.getSelectedDate(), finishDate);
            }else if (reportType == INVOICES_REPORT) {
                invoicesReport(startDate.getSelectedDate(), finishDate);
            }else if (reportType == ORDERS_REPORT) {
                ordersReport(startDate.getSelectedDate(), finishDate);
            }else if (reportType == 2) {
                expensesReport(startDate.getSelectedDate(), finishDate);
            }else if (reportType == EARNINGS_REPORT) {
                earningsReport(startDate.getSelectedDate(), finishDate);
            } else if (reportType == 4) {
                productsMovements(startDate.getSelectedDate(), finishDate);
            } else if (reportType == 5) {
                topSelled(startDate.getSelectedDate(), finishDate);
            } else if (reportType == CLOSE_BOX_REPORT) {
                closeBox(startDate.getSelectedDate());
            } else {
                invalidatedInvoicesReport(startDate.getSelectedDate(), finishDate);
            }
            closeModal();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Generar reporte de fechas", e.getMessage(), sw.toString(), "");
            //e.printStackTrace();
            mainController.setMessage(bundle.getString("reportDateError"), true);
            closeModal();
        }
    }
    
    /*
     Método para cargar el reporte de factura
     @param: fechaInicio(String), fechaFin(String)
     */
    public void earningsReport(Date dateFrom, Date dateTo) throws DocumentException, IOException {
        try {
            pdf = new PrintPDF();
            String type = "totalEarnings";
            String reportTilte = bundle.getString("earningsPdfTitle");
            String[] columnNames = {bundle.getString("lblReportId"), bundle.getString("lblReportDate"), bundle.getString("lblReportProduct"),bundle.getString("lblReportProductBuyPrice")+bundle.getString("moneyNotation"), bundle.getString("lblReportProductSellPrice")+bundle.getString("moneyNotation"), bundle.getString("lblReportProductEarning")+bundle.getString("moneyNotation")};
            abs = new AbstractFacade();
            Collection<Remission> totalesGanancias = abs.findAllDate("Earnings", dateFrom, dateTo);
            int[] columnWidths = new int[]{10, 15, 30, 15, 15, 15};
            pdf.printReport(type, reportTilte, columnNames, columnWidths, totalesGanancias, dateFrom, dateTo, bundle);
        } catch (IOException | HeadlessException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Generar reporte de ganancias", e.getMessage(), sw.toString(), "");
            getMainController().setMessage(bundle.getString("pdfError"), true);
            //e.printStackTrace();
        }
    }

    /*
     Método para cargar el reporte de ingresos
     @param: fechaInicio(String), fechaFin(String)
     */
    public void entriesReport(Date dateFrom, Date dateTo) throws DocumentException, IOException {
        try {
            pdf = new PrintPDF();
            String type = "totalEntries";
            String reportTilte = bundle.getString("entriesPdfTitle");
            String[] columnNames = {bundle.getString("lblReportId"), bundle.getString("lblReportDate"), bundle.getString("lblReportClient"), bundle.getString("lblReportMovementConcept"), bundle.getString("lblReportPayType"), bundle.getString("lblReportTotal")+"("+bundle.getString("moneyNotation")+")"};
            abs = new AbstractFacade();
            Collection<TotalesIngresos> totalesIngresos = abs.findAllDate("TotalesIngresos", dateFrom, dateTo);
            int[] columnWidths = new int[]{10, 10, 40, 10, 20, 10};
            pdf.printReport(type, reportTilte, columnNames, columnWidths, totalesIngresos, dateFrom, dateTo, bundle);
        } catch (IOException | HeadlessException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Generar reporte de ingresos", e.getMessage(), sw.toString(), "");
            getMainController().setMessage(bundle.getString("pdfError"), true);
            //e.printStackTrace();
        }
    }
    
    /*
     Método para cargar el reporte de facturación (Impuestos)
     @param: fechaInicio(String), fechaFin(String)
     */
    public void invoicesReport(Date dateFrom, Date dateTo) throws DocumentException, IOException {
        try {
            pdf = new PrintPDF();
            String type = "totalInvoices";
            String reportTilte = bundle.getString("invoicesPdfTitle");
            abs = new AbstractFacade();
            Collection<Tranzaction> totalesIngresos = abs.findAllDate("Tranzaction", dateFrom, dateTo);
            String[] columnNames = {bundle.getString("lblReportId"), bundle.getString("lblReportDate"), bundle.getString("lblReportClient"),  bundle.getString("lblReportPayType"), bundle.getString("lblReportSubTotal")+"("+bundle.getString("moneyNotation")+")", bundle.getString("lblReportDiscounts")+"("+bundle.getString("moneyNotation")+")", bundle.getString("lblReportTaxes")+"("+bundle.getString("moneyNotation")+")", bundle.getString("lblReportTotal")+"("+bundle.getString("moneyNotation")+")"};
            int[] columnWidths = new int[]{5, 10, 25, 10, 10, 15, 15, 10};
            pdf.printReport(type, reportTilte, columnNames, columnWidths, totalesIngresos, dateFrom, dateTo, bundle);
        } catch (IOException | HeadlessException e) {
            getMainController().setMessage(bundle.getString("pdfError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Generar reporte de facturación", e.getMessage(), sw.toString(), "");
            //e.printStackTrace();
        }
    }
    
    /*
     Método para cargar el reporte de compras a proveedor (Impuestos)
     @param: fechaInicio(String), fechaFin(String)
     */
    public void ordersReport(Date dateFrom, Date dateTo) throws DocumentException, IOException {
        try {
            pdf = new PrintPDF();
            String type = "totalOrders";
            String reportTilte = bundle.getString("ordersPdfTitle");
            TranzactionModel tm = new TranzactionModel();
            Collection<Tranzaction> totalesIngresos = tm.findAllOrdersByDate(dateFrom, dateTo);
            String[] columnNames = {bundle.getString("lblReportId"), bundle.getString("lblReportDate"), bundle.getString("lblReportSupplier"),  bundle.getString("lblReportPayType"), bundle.getString("lblReportSubTotal")+"("+bundle.getString("moneyNotation")+")", bundle.getString("lblReportTaxes")+"("+bundle.getString("moneyNotation")+")", bundle.getString("lblReportTotal")+"("+bundle.getString("moneyNotation")+")"};
            int[] columnWidths = new int[]{10, 15, 30, 10, 10, 15, 10};
            pdf.printReport(type, reportTilte, columnNames, columnWidths, totalesIngresos, dateFrom, dateTo, bundle);
        } catch (IOException | HeadlessException e) {
            getMainController().setMessage(bundle.getString("pdfError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Generar reporte de facturación", e.getMessage(), sw.toString(), "");
            //e.printStackTrace();
        }
    }

    /*
     Método para cargar el reporte de factura
     @param: fechaInicio(String), fechaFin(String)
     */
    public void expensesReport(Date dateFrom, Date dateTo) throws DocumentException, IOException {
        try {
            pdf = new PrintPDF();
            String type = "totalExpenses";
            String reportTilte = "Egresos";
            String[] columnNames = {"No.", "Fecha", "Proveedor", "Concepto", "Total $"};
            abs = new AbstractFacade();
            Collection<TotalesEgresos> totalesEgresos = abs.findAllDate("TotalesEgresos", dateFrom, dateTo);
            int[] columnWidths = new int[]{10, 10, 50, 20, 10};
            pdf.printReport(type, reportTilte, columnNames, columnWidths, totalesEgresos, dateFrom, dateTo, bundle);
        } catch (IOException | HeadlessException e) {
            getMainController().setMessage(bundle.getString("pdfError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Generar reporte de egresos", e.getMessage(), sw.toString(), "");
            //e.printStackTrace();
        }
    }

    /*
     Método para cargar el reporte de factura
     @param: fechaInicio(String), fechaFin(String)
     */
    public void invalidatedInvoicesReport(Date dateFrom, Date dateTo) throws DocumentException, IOException {
        try {
            pdf = new PrintPDF();
            String type = "invalidateInvoices";
            String reportTilte = "Facturas anuladas";
            String[] columnNames = {"No.", "Fecha", "Observaciones", "Cliente", "Valor $"};
            abs = new AbstractFacade();
            Collection<FacturasAnuladas> facturasAnuladas = abs.findAllDate("FacturasAnuladas", dateFrom, dateTo);
            int[] columnWidths = new int[]{10, 10, 50, 20, 10};
            pdf.printReport(type, reportTilte, columnNames, columnWidths, facturasAnuladas, dateFrom, dateTo, bundle);
        } catch (IOException | HeadlessException e) {
            getMainController().setMessage(bundle.getString("pdfError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Generar reporte de facturas anuladas", e.getMessage(), sw.toString(), "");
            //e.printStackTrace();
        }
    }

    /*
     Método para cargar el reporte de movimientos de todos los productos
     @param: fechaInicio(String), fechaFin(String)
     */
    public void productsMovements(Date dateFrom, Date dateTo) throws DocumentException, IOException {
        try {
            pdf = new PrintPDF();
            String type = "productsMovements";
            String reportTilte = "Movimientos de los productos";
            String[] columnNames = {"Código", "Nombre", "Entradas", "Salidas", "Cantidad Disponible"};
            abs = new AbstractFacade();
            Collection<FacturasAnuladas> facturasAnuladas = abs.findAll("Product");
            int[] columnWidths = new int[]{20, 20, 15, 30, 15};
            pdf.printReport(type, reportTilte, columnNames, columnWidths, facturasAnuladas, dateFrom, dateTo, bundle);
        } catch (IOException | HeadlessException e) {
            getMainController().setMessage(bundle.getString("pdfError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Generar reporte de movimientos de productos", e.getMessage(), sw.toString(), "");
            //e.printStackTrace();
        }
    }

    /*
     Método para cargar el reporte de productos más vendidos en un rango definido
     @param: fechaInicio(String), fechaFin(String)
     */
    public void topSelled(Date dateFrom, Date dateTo) throws DocumentException, IOException {
        try {
            pdf = new PrintPDF();
            String type = "topSelled";
            String reportTilte = "Productos más vendidos";
            String[] columnNames = {"Código", "Nombre", "Descripción", "Cantidad vendida"};
            ReportsModel rm = new ReportsModel();
            Collection<Object[]> tops = rm.topSelledProducts(dateFrom, dateTo);
            int[] columnWidths = new int[]{20, 20, 50, 10};
            pdf.printReport(type, reportTilte, columnNames, columnWidths, tops, dateFrom, dateTo, bundle);
        } catch (IOException | HeadlessException e) {
            getMainController().setMessage(bundle.getString("pdfError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Generar reporte de productos mas vendidos", e.getMessage(), sw.toString(), "");
            //e.printStackTrace();
        }
    }

    /*
     Método para cargar el reporte de cierre de caja de un día seleccionado
     @param: dia a reportar (Date date)
     */
    public void closeBox(Date date) throws ParseException, FileNotFoundException, DocumentException {
        try {
            pdf = new PrintPDF();
            pdf.printCloseBoxReport(date, bundle);
        } catch (IOException | HeadlessException e) {
            getMainController().setMessage(bundle.getString("pdfError"), true);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogActions log = new LogActions();
            log.createEntry(String.valueOf(e.hashCode()), "Generar reporte de cierre de caja", e.getMessage(), sw.toString(), "");
            //e.printStackTrace();
        }
    }

    /*
     Método para cargar el reporte de stock
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void inventoryReport(ActionEvent event) throws DocumentException, IOException {
        mainController.inventoryReport();
        closeModal();
    }

    /*
     Método para cargar el reporte de stock
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void minimumStockReport(ActionEvent event) throws DocumentException, IOException {
        mainController.minimunStockReport();
        closeModal();
    }

    /*
     Método para cargar el de movimientos de todos los productos a través de fechas
     @param: evento que ejecute la acción (onAction)
     */
    @FXML
    private void loadProductsMovements(ActionEvent event) throws DocumentException, IOException {
        closeModal();
        mainController.loadSettingsModal(event);
    }

    /*
     Método para ocultar o mostrar la ayuda a través de la acción de un Botón
     @param: Acción que ejecuta la acción: Action onPress
     */
    @FXML
    private void showHideTooltip(ActionEvent event) {
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
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

}

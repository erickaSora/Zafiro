/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.zafirodesktop.entity.Annotation;
import com.zafirodesktop.entity.Deposit;
import com.zafirodesktop.entity.Discount;
import com.zafirodesktop.entity.FacturasAnuladas;
import com.zafirodesktop.entity.Inventory;
import com.zafirodesktop.entity.Invoice;
import com.zafirodesktop.entity.MinimumStock;
import com.zafirodesktop.entity.MovimientosProducto;
import com.zafirodesktop.entity.Payment;
import com.zafirodesktop.entity.Person;
import com.zafirodesktop.entity.Product;
import com.zafirodesktop.entity.Quotation;
import com.zafirodesktop.entity.Remission;
import com.zafirodesktop.entity.Settings;
import com.zafirodesktop.entity.Tax;
import com.zafirodesktop.entity.TotalesEgresos;
import com.zafirodesktop.entity.TotalesIngresos;
import com.zafirodesktop.entity.TransactionDetail;
import com.zafirodesktop.entity.Tranzaction;
import com.zafirodesktop.entity.Turn;
import com.zafirodesktop.model.AbstractFacade;
import com.zafirodesktop.model.RemissionModel;
import com.zafirodesktop.model.ReportsModel;
import com.zafirodesktop.model.TransactionDetailModel;
import com.zafirodesktop.model.TurnModel;
import java.awt.Desktop;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 *
 * @author Digitall
 */
public class PrintPDF {

    private AbstractFacade abs;
    private ResourceBundle bundle;
    public static final long HOUR = 3600 * 1000;
    List<Tax> totalTaxes;
    
    /*
     Método para imprimir las facturas en PDF
     */
    public void pdfInvoice(Tranzaction tranzaction, Remission remi, Quotation quot, List<ProductConverter> proudcts, List<ProductConverter> actualValues, int type, ResourceBundle bundle, boolean isPdf) throws DocumentException, FileNotFoundException, IOException {
        double total = 0;
        double totalProducts = 0;
        String receiptNo = "00";
        String receiptLabel;
        String cardType = null;
        Payment payType = new Payment();
        Discount invoiceDiscount = null;
        Collection<Tax> taxes = new ArrayList<>();
        Collection<Discount> discounts = new ArrayList<>();
        Remission remission = new Remission();
        Quotation quotation = new Quotation();
        Deposit depo = new Deposit();
        Person person;
        Turn user;
        abs = new AbstractFacade();
        Settings settings = (Settings) abs.findByIdInt("Settings", 1);
        int fontSizeProductTable;
        int fontSizeLabels;
        Rectangle pagesize;
        if (settings.getPrintSize().equals("TK") && type > 0 && type < 20) {
            fontSizeProductTable = 7;
            fontSizeLabels = 8;
            pagesize = new Rectangle(215, 327);
        } else {
            fontSizeProductTable = 8;
            fontSizeLabels = 9;
            pagesize = PageSize.LETTER;
        }
        if (quot == null) {
            if (remi == null) {
                remission = tranzaction.getIdRemissionFk();
            } else {
                remission = remi;
            }
            user = remission.getIdTurnFk();
            person = remission.getIdClientFk();
            if (type == 2) {
                Invoice invoice = remission.getIdInvoiceFk();
                payType = invoice.getIdPaymentFk();
                invoiceDiscount = invoice.getIdDiscountFk();
                cardType = invoice.getCardType();
                receiptLabel = bundle.getString("invoiceNumber");
                receiptNo += invoice.getIdInvoicePk();
            } else if (type == 3) {
                for (Deposit deposit : remission.getDepositCollection()) {
                    depo = deposit;
                }
                user = (Turn) abs.findByIdInt("Turn", Integer.valueOf(depo.getIdSell()));
                receiptLabel = bundle.getString("casReceiptNumber");
                receiptNo += depo.getIdDepositPk().toString();
                remission.setRemissionDate(depo.getDepositDate());
            } else {
                receiptLabel = bundle.getString("remissionNumber");
                receiptNo += remission.getIdRemissionPk().toString();
            }
        } else {
            quotation = quot;
            receiptLabel = bundle.getString("quotationNumber");
            receiptNo += quot.getIdQuotationPk().toString();
            person = quot.getIdClientFk();
            user = (Turn) abs.findByIdInt("Turn", Integer.valueOf(quotation.getIdSell()));
        }
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat format = new DecimalFormat("###,###.00");
        Document document = new Document(pagesize, 15, 5, 5, 5);
        FileOutputStream pdfFile;
        if (isPdf) {
            pdfFile = new FileOutputStream("invoice.pdf");
        } else {
            pdfFile = new FileOutputStream("print.pdf");
        }
        PdfWriter.getInstance(document, pdfFile);
        document.open();
        PdfPTable headerTable;
        PdfPCell logoCell, invoiceInfoCell, tableInfoCell = null;
        //Header
        Paragraph companyName = new Paragraph(settings.getBussinesName(), FontFactory.getFont("Times New Roman", fontSizeLabels, Font.BOLD));
        Paragraph companyInfo = new Paragraph(bundle.getString("lblSettingsNit") + ": " + settings.getNit()
                + "\n" + settings.getAddress() + " - " + settings.getCity() + "\n" + settings.getPhoneNumber(), 
                FontFactory.getFont("Times New Roman", fontSizeProductTable));
        //
        //Se comrpueba que sea factura para asociar el note header
        Phrase noteHeader;
        if(type==2 || type == 20)
            noteHeader = new Phrase("\n"+settings.getNoteHeader(), FontFactory.getFont("Times New Roman", fontSizeProductTable));
        else
            noteHeader = new Phrase("\n", FontFactory.getFont("Times New Roman", fontSizeProductTable));
        PdfPCell noteHeaderCel = new PdfPCell();
        noteHeaderCel.setPhrase(noteHeader);
        noteHeaderCel.setBorder(0);
        noteHeaderCel.setHorizontalAlignment(Element.ALIGN_CENTER);
        noteHeaderCel.setVerticalAlignment(Element.ALIGN_TOP);
        //
        PdfPCell headerCel = new PdfPCell();
        headerCel.setVerticalAlignment(Element.ALIGN_TOP);
        headerCel.setBorder(0);

        Paragraph lblInvoiceId = new Paragraph(receiptLabel, FontFactory.getFont("Times New Roman", fontSizeLabels, Font.BOLD));
        lblInvoiceId.setAlignment(Element.ALIGN_CENTER);

        Paragraph valInvoiceId = new Paragraph("No. "+receiptNo, FontFactory.getFont("Times New Roman", fontSizeLabels, Font.BOLD));
        valInvoiceId.setAlignment(Element.ALIGN_CENTER);

        Paragraph lblDate; 
        Paragraph valDate;
        if (type != 20) {
            if (type != 2) {
                lblDate = new Paragraph(bundle.getString("invoiceDate"), FontFactory.getFont("Times New Roman", fontSizeLabels, Font.BOLD));
                valDate = new Paragraph(dt.format(remission.getRemissionDate()), FontFactory.getFont("Times New Roman", fontSizeLabels, Font.BOLD));
                
            } else {
                lblDate = new Paragraph(bundle.getString("invoiceDate"), FontFactory.getFont("Times New Roman", fontSizeLabels, Font.BOLD));
                valDate = new Paragraph(dt.format(remission.getIdInvoiceFk().getInvoiceDate()), FontFactory.getFont("Times New Roman", fontSizeLabels, Font.BOLD));                
            }
        } else {
            lblDate = new Paragraph(bundle.getString("invoiceDate"), FontFactory.getFont("Times New Roman", fontSizeLabels, Font.BOLD));
            valDate = new Paragraph(dt.format(quotation.getQuotationDate()), FontFactory.getFont("Times New Roman", fontSizeLabels, Font.BOLD));          
        }
        //Parte de carga de logo
        Image img;
        String path = System.getProperty("user.dir");
        path += "\\web-files\\logo3.jpg";
        File logo = new File(path);
        if (logo.exists()) {
            img = Image.getInstance("web-files/logo3.jpg");
        } else {
            img = Image.getInstance("web-files/logozafiro.png");
        }
        img.setAlignment(Image.ALIGN_CENTER);
        logoCell = new PdfPCell(img);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoCell.setVerticalAlignment(Element.ALIGN_TOP);
        logoCell.setBorder(0);
        //
        if (settings.getPrintSize().equals("TK") && type > 0 && type < 20) {
            companyName.setAlignment(Element.ALIGN_CENTER);
            Paragraph completeCoInfo = new Paragraph(companyInfo.getContent()+"\n"+settings.getNoteHeader(),FontFactory.getFont("Times New Roman", fontSizeProductTable));
            completeCoInfo.setAlignment(Element.ALIGN_CENTER);
            companyInfo.set(0, completeCoInfo);
            headerCel.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCel.addElement(companyName);
            headerCel.addElement(companyInfo); 
        } else {
            tableInfoCell = new PdfPCell();
            tableInfoCell.setBorder(0);
            headerTable = new PdfPTable(4);
            headerTable.getDefaultCell().setBorder(0);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new int[]{18, 37, 25, 20});
            headerTable.setSpacingAfter(5);
            /*Parte de carga de logo
            Image img;
            String path = System.getProperty("user.dir");
            path += "\\web-files\\logo3.jpg";
            File logo = new File(path);
            if (logo.exists()) {
                img = Image.getInstance("web-files/logo3.jpg");
            } else {
                img = Image.getInstance("web-files/logozafiro.png");
            }
            img.setAlignment(Image.ALIGN_CENTER);
            logoCell = new PdfPCell(img);
            logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoCell.setVerticalAlignment(Element.ALIGN_TOP);
            logoCell.setBorder(0);
            */
            headerCel.addElement(companyName);
            headerCel.addElement(companyInfo);
            headerCel.setPaddingLeft(15);
        
            invoiceInfoCell = new PdfPCell();
            invoiceInfoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            invoiceInfoCell.setVerticalAlignment(Element.ALIGN_TOP);
            invoiceInfoCell.setBorder(0);
            invoiceInfoCell.addElement(lblInvoiceId);
            invoiceInfoCell.addElement(valInvoiceId);
            Paragraph completeDate = new Paragraph(lblDate.getContent()+" "+valDate.getContent(),FontFactory.getFont("Times New Roman", fontSizeLabels, Font.BOLD));
            completeDate.setAlignment(Element.ALIGN_CENTER);
            lblDate.set(0, completeDate);
            invoiceInfoCell.addElement(lblDate);
            
            headerTable.addCell(logoCell);
            headerTable.addCell(headerCel);
            headerTable.addCell(noteHeaderCel);
            headerTable.addCell(invoiceInfoCell);
            tableInfoCell.addElement(headerTable);
        }

        //Content
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.getDefaultCell().setBorder(0);
        Paragraph lblNit = new Paragraph(bundle.getString("invoiceClientId"), FontFactory.getFont("Times New Roman", fontSizeLabels));
        Paragraph valNit = new Paragraph(person.getNit(), FontFactory.getFont("Times New Roman", fontSizeLabels));
        Paragraph lblClientName = new Paragraph(bundle.getString("invoiceClientName"), FontFactory.getFont("Times New Roman", fontSizeLabels));
        Paragraph valClientName = new Paragraph(person.getTotalName(), FontFactory.getFont("Times New Roman", fontSizeLabels));
        if (settings.getPrintSize().equals("TK") && type > 0 && type < 20) {
            infoTable.setWidthPercentage(50);
            infoTable.addCell(lblInvoiceId);
            infoTable.addCell(valInvoiceId);
            infoTable.addCell(lblDate);
            infoTable.addCell(valDate);
            infoTable.addCell(lblNit);
            infoTable.addCell(valNit);
            infoTable.addCell(lblClientName);
            infoTable.addCell(valClientName);
        } else {
            infoTable.setWidthPercentage(90);
            infoTable.setWidths(new int[]{60, 40});
            infoTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            /*Phrase nitPhrase = new Phrase(lblNit.getContent() + " " + valNit.getContent());
            Phrase namePhrase = new Phrase(lblClientName.getContent() + " " + valClientName.getContent());*/
            Paragraph nitPhrase = new Paragraph(lblNit.getContent() + " " + valNit.getContent(), FontFactory.getFont("Times New Roman", fontSizeLabels));
            Paragraph namePhrase = new Paragraph(lblClientName.getContent() + " " + valClientName.getContent(), FontFactory.getFont("Times New Roman", fontSizeLabels));
            Paragraph clientPhone = new Paragraph(bundle.getString("invoiceClientPhone") + " " + person.getPersonPhoneNo(), FontFactory.getFont("Times New Roman", fontSizeLabels));
            Paragraph clientAddres = new Paragraph(bundle.getString("invoiceClientAddres") + " " + person.getPersonAddress(), FontFactory.getFont("Times New Roman", fontSizeLabels));
            Paragraph clientCity = new Paragraph(bundle.getString("invoiceClientCity") + " " + person.getIdPlaceFk().getPlaceName(), FontFactory.getFont("Times New Roman", fontSizeLabels));
            infoTable.addCell(nitPhrase);
            infoTable.addCell(clientPhone);
            infoTable.addCell(namePhrase);
            infoTable.addCell(clientAddres);
            infoTable.addCell(clientCity);
            infoTable.addCell(new Phrase(""));
        }

        //Products
        PdfPTable productsTable;
        PdfPCell cell = new PdfPCell();
        PdfPCell cell2 = new PdfPCell();
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setBorder(0);
        PdfPCell productTableCell = new PdfPCell();
        productTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        productTableCell.setBorder(0);
        //
        if (type == 2 || type == 20) {
            productsTable = new PdfPTable(4);
            productsTable.setWidthPercentage(100);
            productsTable.setWidths(new int[]{18, 32, 20, 25});
            productsTable.getDefaultCell().setBorder(0);
            Paragraph amount = new Paragraph(bundle.getString("amountProductTable"), FontFactory.getFont("Times New Roman", fontSizeProductTable));
            PdfPCell amountCell = new PdfPCell();
            amountCell.setPhrase(amount);
            amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            amountCell.setBorderColor(BaseColor.LIGHT_GRAY);
            amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            if (!settings.getPrintSize().equals("TK")) {
                amountCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            }

            //
            Paragraph description = new Paragraph(bundle.getString("descriptionProductTable"), FontFactory.getFont("Times New Roman", fontSizeProductTable));
            PdfPCell descriptionCell = new PdfPCell();
            descriptionCell.setPhrase(description);
            descriptionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            descriptionCell.setBorderColor(BaseColor.LIGHT_GRAY);
            descriptionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            if (!settings.getPrintSize().equals("TK")) {
                descriptionCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            }
            //
            Paragraph unitPrice = new Paragraph(bundle.getString("unitPriceProductTable"), FontFactory.getFont("Times New Roman", fontSizeProductTable));
            PdfPCell unitPriceCell = new PdfPCell();
            unitPriceCell.setPhrase(unitPrice);
            unitPriceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            unitPriceCell.setBorderColor(BaseColor.LIGHT_GRAY);
            unitPriceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            if (!settings.getPrintSize().equals("TK")) {
                unitPriceCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            }
            //
            Paragraph totalPrice = new Paragraph(bundle.getString("totalPriceProductTable"), FontFactory.getFont("Times New Roman", fontSizeProductTable));
            PdfPCell totalPriceCell = new PdfPCell();
            totalPriceCell.setPhrase(totalPrice);
            totalPriceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalPriceCell.setBorderColor(BaseColor.LIGHT_GRAY);
            totalPriceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            if (!settings.getPrintSize().equals("TK")) {
                totalPriceCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            }
            //
            productsTable.addCell(amountCell);
            productsTable.addCell(descriptionCell);
            productsTable.addCell(unitPriceCell);
            productsTable.addCell(totalPriceCell);
            for (ProductConverter product : proudcts) {
                cell.setPhrase(new Paragraph(String.valueOf(product.getAmount()), FontFactory.getFont("Times New Roman", fontSizeProductTable)));
                productsTable.addCell(cell);
                cell.setPhrase(new Paragraph(product.getProductDescription(), FontFactory.getFont("Times New Roman", fontSizeProductTable)));
                productsTable.addCell(cell);
                cell.setPhrase(new Paragraph(format.format(product.getActualPrice()), FontFactory.getFont("Times New Roman", fontSizeProductTable)));
                productsTable.addCell(cell);
                cell.setPhrase(new Paragraph(format.format(product.getActualPrice() * product.getAmount()), FontFactory.getFont("Times New Roman", fontSizeProductTable)));
                productsTable.addCell(cell);
                total += product.getActualPrice() * product.getAmount();
                //Se cargan los valores reales del producto actual para los descuentos de factura, en caso de no ser nulo
                if(actualValues!=null){
                    for(ProductConverter convrt:actualValues){
                        if(product.getIdProductPk().equals(convrt.getIdProductPk())){
                            totalProducts += convrt.getActualPrice() * product.getAmount();
                        }
                    }
                }
                //Si tiene descuentos
                if (product.getDiscountCollection() != null) {
                    for (Discount pordDisc : product.getDiscountCollection()) {
                        if (!discounts.contains(pordDisc)) {
                            discounts.add(pordDisc);
                        }
                    }
                }
                //Si tiene impuestos
                if (product.getTaxesCollection() != null) {
                    for (Tax pordTax : product.getTaxesCollection()) {
                        if (!taxes.contains(pordTax)) {
                            taxes.add(pordTax);
                        }
                    }
                }
                //Si la factura tiene descuentos
                if(invoiceDiscount!=null){
                    if(!discounts.contains(invoiceDiscount)){
                        discounts.add(invoiceDiscount);
                    }
                }
            }
            productsTable.addCell(new Paragraph(""));
            productsTable.addCell(new Paragraph(""));
            if (!taxes.isEmpty() || !discounts.isEmpty()) {
                int i=0;
                double tempSubtotal = 0, tempDescs = 0;
                double tempTax;
                double tempTaxValue;
                Paragraph lblSubtotal;
                Paragraph subtotalValue;
                if (!discounts.isEmpty()) {
                    i++;
                    if(taxes.isEmpty()){
                        lblSubtotal = new Paragraph(bundle.getString("lblInvoiceSubTotal"), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                    }else{
                        lblSubtotal = new Paragraph(bundle.getString("lblInvoiceTotalValue"), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                    }
                    cell2.setPhrase(lblSubtotal);
                    cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    productsTable.addCell(cell2);
                    subtotalValue = new Paragraph(bundle.getString("moneyNotation") + format.format(total), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                    cell2.setPhrase(subtotalValue);
                    productsTable.addCell(cell2);
                    productsTable.addCell(new Paragraph(""));
                    productsTable.addCell(new Paragraph(""));
                    for (Discount disc : discounts) {
                        //
                        tempTax = disc.getDiscountPct() / 100;
                        //Se compara con el descuento de la factura, si lo tiene
                        if(invoiceDiscount!=null && invoiceDiscount.equals(disc)){
                            tempTaxValue = totalProducts * tempTax;
                        }else{
                            tempTaxValue = 0;
                        }
                        for (ProductConverter prod : proudcts) {
                            for (Discount pordDisc : prod.getDiscountCollection()) {
                                if (pordDisc.equals(disc)) {
                                    //A fin de que el descuento lo haga sobre el valor sin impuestos
                                    for(ProductConverter convrt:actualValues){
                                        if(prod.getIdProductPk().equals(convrt.getIdProductPk())){
                                            tempTaxValue += (convrt.getActualPrice() * prod.getAmount()) * tempTax;
                                        }
                                    }
                                    /*int index = actualValues.indexOf(prod);
                                    ProductConverter tempProduct = actualValues.get(index);
                                    tempTaxValue += (tempProduct.getActualPrice() * prod.getAmount()) * tempTax;*/
                                }
                            }
                        }
                        tempSubtotal += tempTaxValue;
                        tempDescs = tempSubtotal;
                        Paragraph lblTax = new Paragraph(bundle.getString("lblInvoiceDiscount") + "(" + disc.getPercentaje() + ")", FontFactory.getFont("Times New Roman", fontSizeProductTable));
                        cell2.setPhrase(lblTax);
                        productsTable.addCell(cell2);
                        Paragraph taxValue = new Paragraph(bundle.getString("moneyNotation") + format.format(tempTaxValue), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                        cell2.setPhrase(taxValue);
                        productsTable.addCell(cell2);
                        productsTable.addCell(new Paragraph(""));
                        productsTable.addCell(new Paragraph(""));
                    }
                    total-=tempDescs;
                }
                if (!taxes.isEmpty()) {
                    //Se comprueba que no hayan descuentos en los totales
                    if(i>0){
                        tempSubtotal = 0;
                        Paragraph lblTotalTitle = new Paragraph(bundle.getString("lblInvoiceSubTotal"), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                        cell2.setPhrase(lblTotalTitle);
                        productsTable.addCell(cell2);
                        Paragraph lblTotalTValue = new Paragraph(bundle.getString("moneyNotation") + format.format(total), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                        cell2.setPhrase(lblTotalTValue);
                        productsTable.addCell(cell2);
                        productsTable.addCell(new Paragraph(""));
                        productsTable.addCell(new Paragraph(""));
                    }else{
                        lblSubtotal = new Paragraph(bundle.getString("lblInvoiceSubTotal"), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                        cell2.setPhrase(lblSubtotal);
                        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        productsTable.addCell(cell2);
                        subtotalValue = new Paragraph(bundle.getString("moneyNotation") + format.format(total), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                        cell2.setPhrase(subtotalValue);
                        productsTable.addCell(cell2);
                        productsTable.addCell(new Paragraph(""));
                        productsTable.addCell(new Paragraph(""));
                    }
                    for (Tax tax : taxes) {
                        //
                        tempTaxValue = 0;
                        tempTax = tax.getTaxPct() / 100;
                        for (ProductConverter prod : proudcts) {
                            for (Tax pordTax : prod.getTaxesCollection()) {
                                if (pordTax.equals(tax)) {
                                    tempTaxValue = tempTaxValue + (prod.getActualPrice() * prod.getAmount()) * tempTax;
                                }
                            }
                        }
                        tempSubtotal += tempTaxValue;
                        Paragraph lblTax = new Paragraph(tax.toString(), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                        cell2.setPhrase(lblTax);
                        productsTable.addCell(cell2);
                        Paragraph taxValue = new Paragraph(bundle.getString("moneyNotation") + format.format(tempTaxValue), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                        cell2.setPhrase(taxValue);
                        productsTable.addCell(cell2);
                        productsTable.addCell(new Paragraph(""));
                        productsTable.addCell(new Paragraph(""));
                    }
                    total = Math.round(total + tempSubtotal);
                    /*if(hasDiscounts){
                        
                    }*/
                }
                
            }
            if(invoiceDiscount!=null && discounts.isEmpty()){
                double subTotalInvoice = total;
                double desc = subTotalInvoice * (invoiceDiscount.getDiscountPct()/100);
                total = Math.round(subTotalInvoice - desc);
                //Parte subtotal
                Paragraph lblSubtotal = new Paragraph(bundle.getString("lblInvoiceSubTotal"), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                cell2.setPhrase(lblSubtotal);
                productsTable.addCell(cell2);
                Paragraph subtotalValue = new Paragraph(bundle.getString("moneyNotation") + format.format(subTotalInvoice), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                cell2.setPhrase(subtotalValue);
                productsTable.addCell(cell2);
                productsTable.addCell(new Paragraph(""));
                productsTable.addCell(new Paragraph(""));
                //Parte descuecto
                Paragraph lblTax = new Paragraph(bundle.getString("lblInvoiceDiscount") + "(" + invoiceDiscount.getPercentaje() + ")", FontFactory.getFont("Times New Roman", fontSizeProductTable));
                cell2.setPhrase(lblTax);
                productsTable.addCell(cell2);
                Paragraph taxValue = new Paragraph(bundle.getString("moneyNotation") + format.format(desc), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                cell2.setPhrase(taxValue);
                productsTable.addCell(cell2);
                productsTable.addCell(new Paragraph(""));
                productsTable.addCell(new Paragraph(""));
            }
            Paragraph lblTotal = new Paragraph(bundle.getString("invoiceTotalPrice"), FontFactory.getFont("Times New Roman", fontSizeProductTable, Font.BOLD));
            cell2.setPhrase(lblTotal);
            productsTable.addCell(cell2);
            Paragraph totalValue = new Paragraph(bundle.getString("moneyNotation") + format.format(total), FontFactory.getFont("Times New Roman", fontSizeProductTable, Font.BOLD));
            cell2.setPhrase(totalValue);
            productsTable.addCell(cell2);

            productTableCell.addElement(productsTable);
            Paragraph footerInfo;
            PdfPTable sellerInfo = null;
            if (type != 20) {
                if(settings.getPrintSize().equals("TK")){
                    footerInfo = new Paragraph("\n" + bundle.getString("invoiceCashType") + " " + payType.getPaymentName() + " " + cardType + "\n"
                        + bundle.getString("invoiceSeller") + " " + user.getIdUserFk().getTotalName() + "\n"
                        + bundle.getString("lblInvoiceObs") + ": " + remission.getIdInvoiceFk().getObs(), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                }else{
                    footerInfo = new Paragraph("\n" + bundle.getString("invoiceCashType") + " " + payType.getPaymentName() + " " + cardType + "\n"
                        + bundle.getString("lblInvoiceObs") + ": " + remission.getIdInvoiceFk().getObs(), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                    //Parte de informaciòn de vendedor y cliente
                    sellerInfo = new PdfPTable(2);
                    sellerInfo.setWidthPercentage(100);
                    sellerInfo.setWidths(new int[]{50, 50});
                    PdfPCell infoCell = new PdfPCell();
                    infoCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    infoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    infoCell.setVerticalAlignment(Element.ALIGN_TOP);
                    infoCell.setPhrase(new Paragraph("\n"));
                    sellerInfo.addCell(infoCell);
                    infoCell.setPhrase(new Paragraph(user.getIdUserFk().getTotalName(), FontFactory.getFont("Times New Roman", fontSizeProductTable)));
                    sellerInfo.addCell(infoCell);
                    infoCell.setPhrase(new Paragraph(bundle.getString("invoiceClient"), FontFactory.getFont("Times New Roman", fontSizeProductTable)));
                    sellerInfo.addCell(infoCell);
                    infoCell.setPhrase(new Paragraph(bundle.getString("invoiceSeller"), FontFactory.getFont("Times New Roman", fontSizeProductTable)));
                    sellerInfo.addCell(infoCell);
                    //
                }
            } else {
                if(settings.getPrintSize().equals("TK")){
                   footerInfo = new Paragraph("\n" + bundle.getString("invoiceSeller") + " " + user.getIdUserFk().getTotalName() + "\n"
                        + bundle.getString("lblInvoiceObs") + ": " + quotation.getObs(), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                }else{
                    footerInfo = new Paragraph("\n" + bundle.getString("lblInvoiceObs") + ": " + quotation.getObs(), FontFactory.getFont("Times New Roman", fontSizeProductTable));
                    //Parte de informaciòn de vendedor y cliente
                    sellerInfo = new PdfPTable(2);
                    sellerInfo.setWidthPercentage(100);
                    sellerInfo.setWidths(new int[]{50, 50});
                    PdfPCell infoCell = new PdfPCell();
                    infoCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    infoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    infoCell.setVerticalAlignment(Element.ALIGN_TOP);
                    infoCell.setPhrase(new Paragraph("\n"));
                    sellerInfo.addCell(infoCell);
                    infoCell.setPhrase(new Paragraph(user.getIdUserFk().getTotalName(), FontFactory.getFont("Times New Roman", fontSizeProductTable)));
                    sellerInfo.addCell(infoCell);
                    infoCell.setPhrase(new Paragraph(bundle.getString("invoiceClient"), FontFactory.getFont("Times New Roman", fontSizeProductTable)));
                    sellerInfo.addCell(infoCell);
                    infoCell.setPhrase(new Paragraph(bundle.getString("invoiceSeller"), FontFactory.getFont("Times New Roman", fontSizeProductTable)));
                    sellerInfo.addCell(infoCell);
                    //
                }
            }
            productTableCell.addElement(footerInfo);
            if(sellerInfo != null){
                productTableCell.addElement(new Paragraph("\n"));
                productTableCell.addElement(sellerInfo);
            }
        } else if (type == 0) {
            productsTable = new PdfPTable(2);
            productsTable.setWidthPercentage(100);
            productsTable.setWidths(new int[]{25, 75});
            productsTable.getDefaultCell().setBorder(0);

            productsTable.addCell(new Paragraph(bundle.getString("lblserviceOrderRef") + ": ", FontFactory.getFont("Times New Roman", fontSizeLabels)));
            productsTable.addCell(new Paragraph(remission.getObs(), FontFactory.getFont("Times New Roman", fontSizeLabels)));
            productsTable.addCell(new Paragraph("\n"));
            productsTable.addCell(new Paragraph("\n"));
            for (Annotation annotation : remission.getAnnotationCollection()) {
                productsTable.addCell(new Paragraph(annotation.getDate(), FontFactory.getFont("Times New Roman", fontSizeLabels)));
                productsTable.addCell(new Paragraph(annotation.getDescription(), FontFactory.getFont("Times New Roman", fontSizeLabels)));
                productsTable.addCell(new Paragraph("\n"));
                productsTable.addCell(new Paragraph("\n"));
            }
            productTableCell.addElement(productsTable);
            //Parte de informaciòn de vendedor y cliente
            PdfPTable sellerInfo = new PdfPTable(2);
            sellerInfo.setWidthPercentage(100);
            sellerInfo.setWidths(new int[]{50, 50});
            PdfPCell infoCell = new PdfPCell();
            infoCell.setBorderColor(BaseColor.LIGHT_GRAY);
            infoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            infoCell.setVerticalAlignment(Element.ALIGN_TOP);
            infoCell.setPhrase(new Paragraph("\n"));
            sellerInfo.addCell(infoCell);
            infoCell.setPhrase(new Paragraph(user.getIdUserFk().getTotalName(), FontFactory.getFont("Times New Roman", fontSizeProductTable)));
            sellerInfo.addCell(infoCell);
            infoCell.setPhrase(new Paragraph(bundle.getString("invoiceClient"), FontFactory.getFont("Times New Roman", fontSizeProductTable)));
            sellerInfo.addCell(infoCell);
            infoCell.setPhrase(new Paragraph(bundle.getString("invoiceSeller"), FontFactory.getFont("Times New Roman", fontSizeProductTable)));
            sellerInfo.addCell(infoCell);
            productTableCell.addElement(new Paragraph("\n"));
            productTableCell.addElement(sellerInfo);
            //
        } else {
            productsTable = new PdfPTable(2);
            productsTable.setWidthPercentage(100);
            productsTable.setWidths(new int[]{25, 75});
            productsTable.getDefaultCell().setBorder(0);
            //Remission principalRemission = (Remission) abs.findByIdInt("Remission", Integer.valueOf(remission.getNoBuyReference()));
            //Remission prinRemission abs.findByIdInt(cardType, type)
            productsTable.addCell(new Paragraph(bundle.getString("invoiceDeposit") + " ", FontFactory.getFont("Times New Roman", fontSizeLabels, Font.BOLD)));
            productsTable.addCell(new Paragraph(bundle.getString("moneyNotation") + format.format(depo.getDeposit()), FontFactory.getFont("Times New Roman", fontSizeLabels, Font.BOLD)));
            productsTable.addCell(new Paragraph("\n"));
            productsTable.addCell(new Paragraph("\n"));
            productsTable.addCell(new Paragraph(bundle.getString("invoiceLeftAmount") + " ", FontFactory.getFont("Times New Roman", fontSizeLabels, Font.BOLD)));
            productsTable.addCell(new Paragraph(bundle.getString("moneyNotation") + format.format(remission.getLeftAmount()), FontFactory.getFont("Times New Roman", fontSizeLabels, Font.BOLD)));
            productsTable.addCell(new Paragraph("\n"));
            productsTable.addCell(new Paragraph("\n"));
            productsTable.addCell(new Paragraph(bundle.getString("lblInvoiceObs") + ": ", FontFactory.getFont("Times New Roman", fontSizeLabels, Font.BOLD)));
            productsTable.addCell(new Paragraph(depo.getObs(), FontFactory.getFont("Times New Roman", fontSizeLabels, Font.BOLD)));
            productTableCell.addElement(productsTable);
            //Parte de informaciòn de vendedor y cliente
            PdfPTable sellerInfo = new PdfPTable(2);
            sellerInfo.setWidthPercentage(100);
            sellerInfo.setWidths(new int[]{50, 50});
            PdfPCell infoCell = new PdfPCell();
            infoCell.setBorderColor(BaseColor.LIGHT_GRAY);
            infoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            infoCell.setVerticalAlignment(Element.ALIGN_TOP);
            infoCell.setPhrase(new Paragraph("\n"));
            sellerInfo.addCell(infoCell);
            infoCell.setPhrase(new Paragraph(user.getIdUserFk().getTotalName(), FontFactory.getFont("Times New Roman", fontSizeProductTable)));
            sellerInfo.addCell(infoCell);
            infoCell.setPhrase(new Paragraph(bundle.getString("invoiceClient"), FontFactory.getFont("Times New Roman", fontSizeProductTable)));
            sellerInfo.addCell(infoCell);
            infoCell.setPhrase(new Paragraph(bundle.getString("invoiceSeller"), FontFactory.getFont("Times New Roman", fontSizeProductTable)));
            sellerInfo.addCell(infoCell);
            productTableCell.addElement(new Paragraph("\n"));
            productTableCell.addElement(sellerInfo);
            /*Paragraph sellerInfo = new Paragraph("\n" + bundle.getString("invoiceSeller") + " " + user.getIdUserFk().getTotalName(), FontFactory.getFont("Times New Roman", fontSizeLabels));
            productTableCell.addElement(sellerInfo);*/
        }
        //Footer
        Paragraph footerText = new Paragraph(settings.getNoteFooter(), FontFactory.getFont("Times New Roman", fontSizeProductTable));
        PdfPCell footerCell = new PdfPCell();
        footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footerCell.setPhrase(footerText);
        footerCell.setBorder(0);
        //Scirebox publicity
        Paragraph publicityText = new Paragraph("Powered by scirebox.com", FontFactory.getFont("Times New Roman", fontSizeProductTable));
        PdfPCell publicityCell = new PdfPCell();
        publicityCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        publicityCell.setPhrase(publicityText);
        publicityCell.setBorder(0);
        //Spaces
        Paragraph emptyParagraph = new Paragraph(" ");
        PdfPCell spaceCell = new PdfPCell();
        spaceCell.setPhrase(emptyParagraph);
        spaceCell.setBorder(0);

        //Table Estructure
        PdfPTable table = new PdfPTable(1);
        table.getDefaultCell().setBorder(0);
        table.setWidthPercentage(100);
        if (settings.getPrintSize().equals("TK") && type > 0 && type < 20) {
            table.addCell(logoCell);
            table.addCell(headerCel);
        } else {
            table.addCell(tableInfoCell);
        }
        table.addCell(spaceCell);
        table.addCell(infoTable);
        //table.addCell(infoCel);
        table.addCell(spaceCell);
        table.addCell(productTableCell);
        table.addCell(spaceCell);
        table.addCell(footerCell);
        table.addCell(publicityCell);

        document.add(table);
        document.close();
    }

    public void printInvoice(Tranzaction tranzaction, Remission remi, Quotation quot, List<ProductConverter> proudcts, List<ProductConverter> actualValues, int type, ResourceBundle bundle, boolean isPdf) throws DocumentException, IOException, PrinterException {
        pdfInvoice(tranzaction, remi, quot, proudcts, actualValues, type, bundle, isPdf);
        if (isPdf) {
            openPDF("invoice.pdf");
        } else {
            PrintFiles print = new PrintFiles();
            String path = System.getProperty("user.dir");
            path += "\\print.pdf";
            print.silentPrintPDF(path);
        }
    }

    public void printReport(String type, String reportTilte, String[] columns, int[] columnWidths, Collection data, Date startDate, Date endDate, ResourceBundle rb) throws DocumentException, FileNotFoundException, IOException {
        bundle = rb;
        abs = new AbstractFacade();
        Remission remission = new Remission();
        Remission principalRemission = new Remission();
        int idProduct = 0, rows=1;
        CMYKColor row2 = new CMYKColor(17, 13, 13, 0);
        Settings settings = (Settings) abs.findByIdInt("Settings", 1);
        DateFormat mediumDf = DateFormat.getDateInstance(DateFormat.MEDIUM);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM yyyy, hh:mm aaa");
        DecimalFormat format = new DecimalFormat("###,###.00");
        Date defaultDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
        double subtotal = 0, iva = 0, total = 0;
        Rectangle pageSize = PageSize.LETTER;
        if (type.equals("inventory") || type.equals("productsMovements") || type.equals("totalInvoices") || type.equals("totalOrders")) {
            pageSize = PageSize.LETTER.rotate();
        }
        Document documento = new Document(pageSize, 25, 25, 20, 20);
        FileOutputStream ficheroPdf = new FileOutputStream("reporte.pdf");
        PdfWriter.getInstance(documento, ficheroPdf);
        documento.open();

        //header
        Paragraph titulo = new Paragraph("Reporte de " + reportTilte + " - " + settings.getBussinesName(), FontFactory.getFont("Times New Roman", 14, Font.BOLD));
        titulo.setAlignment(Element.ALIGN_CENTER);
        Paragraph fecharealizado = new Paragraph("Realizado el: " + sdf.format(defaultDate), FontFactory.getFont("Times New Roman", 12));
        fecharealizado.setAlignment(Element.ALIGN_CENTER);
        Paragraph fechaDesdeHasta = null;
        if (!type.equals("inventory") && !type.equals("personCredits") && !type.equals("productMovements") && !type.equals("productOrders") && !type.equals("minimumStock") && !type.equals("activeCredit")) {
            fechaDesdeHasta = new Paragraph("Desde: " + mediumDf.format(startDate) + " Hasta: " + mediumDf.format(endDate), FontFactory.getFont("Times New Roman", 10));
            fechaDesdeHasta.setAlignment(Element.ALIGN_CENTER);
        }
        Paragraph item;
        PdfPCell cell;
        PdfPTable header = new PdfPTable(columns.length);
        header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
        header.getDefaultCell().setPaddingTop(7);
        header.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        //Se especifica un tamaño personalizado para los tamaños de las columnas
        header.setWidths(columnWidths);
        header.setWidthPercentage(100);
        for (String column : columns) {
            //Se cuadra manualmente el header de la tabla en el caso de reportes de movimientos de todos los productos 
            if (type.equals("productsMovements") && column.equals("Entradas")) {
                PdfPTable entryTable = new PdfPTable(3);
                entryTable.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
                PdfPCell entryCell = new PdfPCell(new Paragraph(column, FontFactory.getFont("Times New Roman", 10)));
                entryCell.setColspan(3);
                entryCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                entryTable.addCell(entryCell);
                entryCell = new PdfPCell(new Paragraph("Compra", FontFactory.getFont("Times New Roman", 8)));
                entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                entryCell.setBackgroundColor(new CMYKColor(80, 19, 0, 0));
                entryTable.addCell(entryCell);
                entryCell = new PdfPCell(new Paragraph("Cargue", FontFactory.getFont("Times New Roman", 8)));
                entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                entryCell.setBackgroundColor(new CMYKColor(4, 16, 62, 0));
                entryTable.addCell(entryCell);
                entryCell = new PdfPCell(new Paragraph("Total", FontFactory.getFont("Times New Roman", 8)));
                entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                entryTable.addCell(entryCell);
                cell = new PdfPCell(entryTable);
                header.addCell(cell);
            } else if (type.equals("productsMovements") && column.equals("Salidas")) {
                PdfPTable entryTable = new PdfPTable(5);
                entryTable.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
                PdfPCell entryCell = new PdfPCell(new Paragraph(column, FontFactory.getFont("Times New Roman", 10)));
                entryCell.setColspan(5);
                entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                entryCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                entryTable.addCell(entryCell);
                entryCell = new PdfPCell(new Paragraph("Factura", FontFactory.getFont("Times New Roman", 8)));
                entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                entryCell.setBackgroundColor(new CMYKColor(42, 0, 57, 0));
                entryTable.addCell(entryCell);
                entryCell = new PdfPCell(new Paragraph("Garantía", FontFactory.getFont("Times New Roman", 8)));
                entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                entryCell.setBackgroundColor(new CMYKColor(0, 59, 32, 0));
                entryTable.addCell(entryCell);
                entryCell = new PdfPCell(new Paragraph("Obsequio", FontFactory.getFont("Times New Roman", 8)));
                entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                entryCell.setBackgroundColor(new CMYKColor(37, 38, 46, 0));
                entryTable.addCell(entryCell);
                entryCell = new PdfPCell(new Paragraph("Deterioro", FontFactory.getFont("Times New Roman", 8)));
                entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                entryCell.setBackgroundColor(new CMYKColor(0, 29, 47, 0));
                entryTable.addCell(entryCell);
                entryCell = new PdfPCell(new Paragraph("Total", FontFactory.getFont("Times New Roman", 8)));
                entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                entryTable.addCell(entryCell);
                cell = new PdfPCell(entryTable);
                header.addCell(cell);
            } else {
                item = new Paragraph(column, FontFactory.getFont("Times New Roman", 10));
                cell = new PdfPCell(item);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.addCell(cell);
            }
        }
        switch (type) {
            case "totalExpenses":
                Collection<TotalesEgresos> expenses = data;
                String conceptName;
                for (TotalesEgresos expense : expenses) {
                    conceptName = expense.getConcept();
                    if(rows%2==0){
                        header.getDefaultCell().setBackgroundColor(row2);
                    }else{
                        header.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    }
                    rows++;
                    header.addCell(new Paragraph("0" + String.valueOf(expense.getId()), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(mediumDf.format(expense.getFecha()), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(expense.getClient(), FontFactory.getFont("Times New Roman", 10)));
                    //Para el caso en que el concepto sea un abono 
                    if(conceptName.equals("ab"))
                        conceptName = "Abono a proveedor";
                    header.addCell(new Paragraph(conceptName, FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(format.format(expense.getTotal()), FontFactory.getFont("Times New Roman", 10)));
                    total += expense.getTotal();
                }
                break;
            case "totalEntries":
                Collection<TotalesIngresos> entries = data;
                String paymentLabel;
                for (TotalesIngresos entry : entries) {
                    if(rows%2==0){
                        header.getDefaultCell().setBackgroundColor(row2);
                    }else{
                        header.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    }
                    rows++;
                    header.addCell(new Paragraph("0" + String.valueOf(entry.getId()), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(mediumDf.format(entry.getFecha()), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(entry.getClient(), FontFactory.getFont("Times New Roman", 10)));
                    /// Parte de especificación del tipo de pago y sumatoria de Totales
                    if (entry.getIdPaymentFk() == null || entry.getIdPaymentFk().equals(1)) {
                        paymentLabel = "Efectivo";
                        subtotal += entry.getTotal();
                    } else if (entry.getIdPaymentFk().equals(2)) {
                        paymentLabel = "Efectivo";
                        entry.setConcept("Abono");
                        subtotal += entry.getTotal();
                    } else {
                        paymentLabel = "Tarjeta " + entry.getCardReference();
                        iva += entry.getTotal();
                    }
                    //
                    header.addCell(new Paragraph(entry.getConcept(), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(paymentLabel, FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(format.format(entry.getTotal()), FontFactory.getFont("Times New Roman", 10)));
                    total += entry.getTotal();
                }
                break;
            case "totalEarnings":
                Collection<Remission> earnings = data;
                TransactionDetailModel tdm = new TransactionDetailModel();
                Tranzaction trzcton=null;
                TransactionDetail buyLote;
                double sell, buy, rest;
                for (Remission entry : earnings) {
                    //Se trae ea valor estimado de compra del producto
                    for(Tranzaction tranz : entry.getTranzactionCollection()){
                        trzcton = tranz;
                    }
                    for(TransactionDetail td:trzcton.getTransactionDetailCollection()){
                        if(rows%2==0){
                        header.getDefaultCell().setBackgroundColor(row2);
                        }else{
                            header.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                        }
                        rows++;
                        buy=0;
                        header.addCell(new Paragraph("0" + String.valueOf(entry.getIdInvoiceFk().getIdInvoicePk()), FontFactory.getFont("Times New Roman", 10)));
                        header.addCell(new Paragraph(mediumDf.format(entry.getIdInvoiceFk().getInvoiceDate()), FontFactory.getFont("Times New Roman", 10)));
                        header.addCell(new Paragraph(td.getProduct().getProductDescription(), FontFactory.getFont("Times New Roman", 10)));
                        sell=td.getUnitPrice()*td.getAmount();
                        buyLote = tdm.findStockBuy(td.getProduct().getIdProductPk());
                        if(buyLote!=null)
                            buy = buyLote.getUnitPrice()*td.getAmount();
                        rest = sell - buy;
                        header.addCell(new Paragraph(format.format(buy), FontFactory.getFont("Times New Roman", 10)));
                        header.addCell(new Paragraph(format.format(sell), FontFactory.getFont("Times New Roman", 10)));
                        header.addCell(new Paragraph(format.format(rest), FontFactory.getFont("Times New Roman", 10)));
                        total+=rest;
                    }
                }
                break;    
            case "totalInvoices":
                Collection<Tranzaction> ingresos = data;
                totalTaxes = new ArrayList<>();
                List<Tax> taxes;
                List<Discount> discounts;
                Discount invoiceDiscount = null;
                String payLabel, taxLabel, discountLabel;
                for (Tranzaction entry : ingresos) {
                    if(rows%2==0){
                        header.getDefaultCell().setBackgroundColor(row2);
                    }else{
                        header.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    }
                    rows++;
                    discountLabel = bundle.getString("lblInvoiceNotax");
                    taxLabel = bundle.getString("lblInvoiceNotax");
                    int contDesc = 0;
                    taxes = new ArrayList<>();
                    discounts = new ArrayList<>();
                    invoiceDiscount = entry.getIdRemissionFk().getIdInvoiceFk().getIdDiscountFk();
                    double totalTax=0, totalDesc=0, totalFact=0, tempSubtotal=0, sumTax=0;
                    header.addCell(new Paragraph("0" + String.valueOf(entry.getIdRemissionFk().getIdInvoiceFk().getIdInvoicePk()), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(mediumDf.format(entry.getIdRemissionFk().getIdInvoiceFk().getInvoiceDate()), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(entry.getIdRemissionFk().getIdClientFk().getTotalName(), FontFactory.getFont("Times New Roman", 10)));
                    /// Parte de especificación del tipo de pago
                    if (entry.getIdRemissionFk().getIdInvoiceFk().getIdPaymentFk().getIdPaymentPk().equals(1)) {
                        payLabel = bundle.getString("lblReportCash");
                    }else if (entry.getIdRemissionFk().getIdInvoiceFk().getIdPaymentFk().getIdPaymentPk().equals(2)) {
                        payLabel = bundle.getString("lblReportCredit");
                    }else {
                        payLabel = bundle.getString("lblReportCard") + entry.getIdRemissionFk().getIdInvoiceFk().getCardType()+" "+entry.getIdRemissionFk().getIdInvoiceFk().getNoReference();
                    }
                    //
                    header.addCell(new Paragraph(payLabel, FontFactory.getFont("Times New Roman", 10)));
                    //Se recorre el listado de los productos asociados a fin de mostrar la información de descuentos y/o impuestos en caso de que los tenga
                    for(TransactionDetail td : entry.getTransactionDetailCollection()){
                        //Se trae el valor correspondiente al subtotal de la factura sin efectuar descuentos
                        tempSubtotal += td.getUnitPrice() * td.getAmount();
                        //Se guarda el valor total real del movimiento 
                        if(td.getDiscounts()!=null||td.getTaxes()!=null){
                            totalFact = 0;
                        }else{
                            totalFact = td.getTranzaction().getTransactionPrice();
                            if(invoiceDiscount!=null){
                                totalFact += Math.round(totalFact*(invoiceDiscount.getDiscountPct()/100));
                            }
                        }
                        //Si tiene impuestos o descuentos asociados al producto
                        if(td.getDiscounts()!=null||td.getTaxes()!=null){
                            //Si tiene descuentos por producto
                            if(td.getDiscounts()!=null){
                                String[] allDiscounts = td.getDiscounts().split(";;");
                                for (String allDisco : allDiscounts) {
                                    String[] asociatedDisco = allDisco.split(";");
                                    Discount discount = new Discount(Integer.parseInt(asociatedDisco[0]), new Float(asociatedDisco[2]), asociatedDisco[1]);
                                    //Ecuación para saber el valor total del producto antes del impuesto, en el caso en el que también tenga impuestos asociados
                                    double realVl;
                                    if(td.getTaxes()!=null){
                                        float taxsPct = 0;
                                        String[] taxs = td.getTaxes().split(";;");
                                        for (String tx : taxs) {
                                            String[] tempTx = tx.split(";");
                                            taxsPct += new Float(tempTx[2]);
                                        }
                                        realVl = (taxsPct/100)*td.getUnitPrice();
                                        realVl += td.getUnitPrice();
                                    }else{
                                        realVl = td.getUnitPrice();
                                    }
                                    //Valor para llevar el total de la factura fuera de impuestos y descuentos
                                    totalFact += realVl*td.getAmount();
                                    totalDesc = (realVl * (discount.getDiscountPct() /100))*td.getAmount();
                                    if(!discounts.contains(discount)){
                                        discount.setDiscountSum(totalDesc);
                                        discounts.add(discount);
                                    }else{
                                        int index = discounts.indexOf(discount);
                                        totalDesc += discounts.get(index).getDiscountSum();
                                        discounts.get(index).setDiscountSum(totalDesc);
                                    }
                                }
                            }
                            //Si tiene impuestos
                            if(td.getTaxes()!=null){
                                String[] allTaxes = td.getTaxes().split(";;");
                                for (String allTax : allTaxes) {
                                    String[] asociatedTax = allTax.split(";");
                                    Tax tax = new Tax(Integer.parseInt(asociatedTax[0]), asociatedTax[1], new Float(asociatedTax[2]));
                                    totalTax = (td.getUnitPrice() * (tax.getTaxPct() /100))*td.getAmount();
                                    if(!taxes.contains(tax)){
                                        tax.setTaxSum(totalTax);
                                        taxes.add(tax);
                                    }else{
                                        int index = taxes.indexOf(tax);
                                        totalTax += taxes.get(index).getTaxSum();
                                        taxes.get(index).setTaxSum(totalTax);
                                    }
                                    //Se adiciona el total del impuesto actual
                                    sumTax += totalTax;
                                }
                            }
                        }
                    }
                    //Para el caso en el que incluya descuento por factura
                        if(invoiceDiscount!=null){
                            //Se calcula el valor total de la factura
                            totalFact = sumTax+tempSubtotal;
                            if(discounts.contains(invoiceDiscount)){
                                totalDesc = (totalFact*(invoiceDiscount.getDiscountPct()/100));
                                int index = discounts.indexOf(invoiceDiscount);
                                totalDesc += discounts.get(index).getDiscountSum();
                                discounts.get(index).setDiscountSum(totalDesc);
                            }else{
                                totalDesc = (totalFact*(invoiceDiscount.getDiscountPct()/100));
                                invoiceDiscount.setDiscountSum(totalDesc);
                                discounts.add(invoiceDiscount);
                            }
                        }
                    //Se imprime la tupla de subtotal de la factura de acuerdo a los impuestos y descuentos asociados
                    header.addCell(new Paragraph(format.format(tempSubtotal), FontFactory.getFont("Times New Roman", 10)));
                    //Se imprime la tupla de descuentos, en caso de tenerlos
                    if(!discounts.isEmpty()){
                        PdfPTable discTable = new PdfPTable(2);
                        discTable.getDefaultCell().setBorder(0);
                        for(Discount actualDiscount:discounts){
                            discTable.addCell(new Paragraph(actualDiscount.getPercentaje(), FontFactory.getFont("Times New Roman", 10)));
                            discTable.addCell(new Paragraph(format.format(actualDiscount.getDiscountSum()), FontFactory.getFont("Times New Roman", 10)));
                        }
                        header.addCell(discTable);
                    }else{
                        header.addCell(new Paragraph(discountLabel, FontFactory.getFont("Times New Roman", 10)));
                    }
                    //Se imprime la tupla de impuestos, en caso de tenerlos
                    if(!taxes.isEmpty()){
                        PdfPTable taxTable = new PdfPTable(2);
                        taxTable.getDefaultCell().setBorder(0);
                        double taxTotal=0;
                        for(Tax actualTax:taxes){
                            taxTable.addCell(new Paragraph(actualTax.toString(), FontFactory.getFont("Times New Roman", 10)));
                            taxTable.addCell(new Paragraph(format.format(actualTax.getTaxSum()), FontFactory.getFont("Times New Roman", 10)));
                            //Se suman los valores al listado general de impuestos
                            if(!totalTaxes.contains(actualTax)){
                                totalTaxes.add(actualTax);
                            }else{
                                int index = totalTaxes.indexOf(actualTax);
                                taxTotal = totalTaxes.get(index).getTaxSum();
                                taxTotal += actualTax.getTaxSum();
                                totalTaxes.get(index).setTaxSum(taxTotal);
                            }
                            subtotal+= actualTax.getTaxSum();
                        }
                        header.addCell(taxTable);
                    }else{
                        header.addCell(new Paragraph(taxLabel, FontFactory.getFont("Times New Roman", 10)));
                    }
                    header.addCell(new Paragraph(format.format(entry.getTransactionPrice()), FontFactory.getFont("Times New Roman", 10)));
                    total += entry.getTransactionPrice();
                }
                break;
            case "totalOrders":
                Collection<Tranzaction> supplierOrders = data;
                totalTaxes = new ArrayList<>();
                List<Tax> impuestos;
                String payTypeLabel, taxAssocLabel;
                for (Tranzaction entry : supplierOrders) {
                    if(rows%2==0){
                        header.getDefaultCell().setBackgroundColor(row2);
                    }else{
                        header.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    }
                    rows++;
                    taxLabel = bundle.getString("lblInvoiceNotax");
                    taxes = new ArrayList<>();
                    double totalTax=0, totalFact=0, tempSubtotal=0, sumTax=0;
                    header.addCell(new Paragraph("0" + String.valueOf(entry.getIdRemissionFk().getIdRemissionPk()), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(mediumDf.format(entry.getIdRemissionFk().getRemissionDate()), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(entry.getIdRemissionFk().getIdSupplierFk().getTotalName(), FontFactory.getFont("Times New Roman", 10)));
                    /// Parte de especificación del tipo de pago
                    if (entry.getIdRemissionFk().getDeposit()!=null) {
                        payLabel = bundle.getString("lblReportCredit");
                    }else {
                        payLabel = bundle.getString("lblReportCash");
                    }
                    //
                    header.addCell(new Paragraph(payLabel, FontFactory.getFont("Times New Roman", 10)));
                    //Se recorre el listado de los productos asociados a fin de mostrar la información de impuestos en caso de que los tenga
                    for(TransactionDetail td : entry.getTransactionDetailCollection()){
                        //Se trae el valor correspondiente al subtotal de la factura sin efectuar descuentos
                        tempSubtotal += td.getUnitPrice() * td.getAmount();
                        //Se guarda el valor total real del movimiento 
                        if(td.getTaxes()!=null){
                            totalFact = 0;
                        }else{
                            totalFact = td.getTranzaction().getTransactionPrice();
                        }
                        //Si tiene impuestos asociados al producto
                        if(td.getTaxes()!=null){
                            String[] allTaxes = td.getTaxes().split(";;");
                            for (String allTax : allTaxes) {
                                String[] asociatedTax = allTax.split(";");
                                Tax tax = new Tax(Integer.parseInt(asociatedTax[0]), asociatedTax[1], new Float(asociatedTax[2]));
                                totalTax = (td.getUnitPrice() * (tax.getTaxPct() /100))*td.getAmount();
                                if(!taxes.contains(tax)){
                                    tax.setTaxSum(totalTax);
                                    taxes.add(tax);
                                }else{
                                    int index = taxes.indexOf(tax);
                                    totalTax += taxes.get(index).getTaxSum();
                                    taxes.get(index).setTaxSum(totalTax);
                                }
                                //Se adiciona el total del impuesto actual
                                sumTax += totalTax;
                            }
                        }
                    }
                    //Se imprime la tupla de subtotal de la factura de acuerdo a los impuestos y descuentos asociados
                    header.addCell(new Paragraph(format.format(tempSubtotal), FontFactory.getFont("Times New Roman", 10)));
                    //Se imprime la tupla de impuestos, en caso de tenerlos
                    if(!taxes.isEmpty()){
                        PdfPTable taxTable = new PdfPTable(2);
                        taxTable.getDefaultCell().setBorder(0);
                        double taxTotal=0;
                        for(Tax actualTax:taxes){
                            taxTable.addCell(new Paragraph(actualTax.toString(), FontFactory.getFont("Times New Roman", 10)));
                            taxTable.addCell(new Paragraph(format.format(actualTax.getTaxSum()), FontFactory.getFont("Times New Roman", 10)));
                            //Se suman los valores al listado general de impuestos
                            if(!totalTaxes.contains(actualTax)){
                                totalTaxes.add(actualTax);
                            }else{
                                int index = totalTaxes.indexOf(actualTax);
                                taxTotal = totalTaxes.get(index).getTaxSum();
                                taxTotal += actualTax.getTaxSum();
                                totalTaxes.get(index).setTaxSum(taxTotal);
                            }
                            subtotal+= actualTax.getTaxSum();
                        }
                        header.addCell(taxTable);
                    }else{
                        header.addCell(new Paragraph(taxLabel, FontFactory.getFont("Times New Roman", 10)));
                    }
                    header.addCell(new Paragraph(format.format(entry.getTransactionPrice()), FontFactory.getFont("Times New Roman", 10)));
                    total += entry.getTransactionPrice();
                }
                break;
            case "productMovements":
                Collection<MovimientosProducto> movements = data;
                for (MovimientosProducto mov : movements) {
                    if(rows%2==0){
                        header.getDefaultCell().setBackgroundColor(row2);
                    }else{
                        header.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    }
                    rows++;
                    header.addCell(new Paragraph("0" + String.valueOf(mov.getIdRemissionPk()), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(mediumDf.format(mov.getRemissionDate()), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(mov.getObs(), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(mov.getConceptName(), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(String.valueOf(mov.getAmount()), FontFactory.getFont("Times New Roman", 10)));
                    ///Parte de sumatoria de totales
                    switch (mov.getIdConceptPk()) {
                        case 4:
                            iva = iva + mov.getAmount();
                            break;
                        case 2:
                        case 6:
                        case 5:
                            total = total + mov.getAmount();
                            break;
                        default:
                            subtotal = subtotal + mov.getAmount();
                            break;
                    }
                    //
                    idProduct = mov.getIdProductPk();
                }
                break;
            case "productOrders":
                Collection<TransactionDetail> orders = data;
                for (TransactionDetail mov : orders) {
                    if(rows%2==0){
                        header.getDefaultCell().setBackgroundColor(row2);
                    }else{
                        header.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    }
                    rows++;
                    header.addCell(new Paragraph("0" + String.valueOf(mov.getTranzaction().getIdRemissionFk().getIdRemissionPk()), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(mediumDf.format(mov.getTranzaction().getIdRemissionFk().getRemissionDate()), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(mov.getTranzaction().getIdRemissionFk().getObs(), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(mov.getTranzaction().getIdRemissionFk().getIdSupplierFk().getTotalName(), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(format.format(mov.getUnitPrice()), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(String.valueOf(mov.getAmount()), FontFactory.getFont("Times New Roman", 10)));
                }
                break;
            case "inventory":
                Collection<Inventory> inventory = data;
                for (Inventory inv : inventory) {
                    if(rows%2==0){
                        header.getDefaultCell().setBackgroundColor(row2);
                    }else{
                        header.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    }
                    rows++;
                    header.addCell(new Paragraph(inv.getIdProductPk(), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(inv.getProductReference(), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(inv.getProductDescription(), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(format.format(inv.getActualPrice()), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(inv.getMinimum().toString(), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(inv.getAmount().toString(), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(format.format(inv.getTotal()), FontFactory.getFont("Times New Roman", 10)));
                    subtotal += 1;
                    total += inv.getTotal();
                }
                break;
            case "topSelled":
                Collection<Object[]> tops = data;
                for (Object[] obj : tops) {
                    if(rows%2==0){
                        header.getDefaultCell().setBackgroundColor(row2);
                    }else{
                        header.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    }
                    rows++;
                    header.addCell(new Paragraph((String) obj[0], FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph((String) obj[1], FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph((String) obj[2], FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(obj[3].toString(), FontFactory.getFont("Times New Roman", 10)));
                }
                break;
            case "activeCredit":
                Collection<Remission> remissions = data;
                RemissionModel rn = new RemissionModel();
                Remission lastCredit = new Remission();
                String personName;
                for (Remission rem : remissions) {
                    if(rows%2==0){
                        header.getDefaultCell().setBackgroundColor(row2);
                    }else{
                        header.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    }
                    rows++;
                    //Se comprueba si es de cliente o de proveedor
                    Collection<Remission> credits;
                    if(rem.getIdClientFk()!=null)
                        credits = rn.findAllCreditsByClient(rem.getIdClientFk().getIdPersonPk());
                    else
                        credits = rn.findAllCreditsBySupplier(rem.getIdSupplierFk().getIdPersonPk());
                    for (Remission remission1 : credits) {
                        lastCredit = remission1;
                    }
                    header.addCell(new Paragraph(lastCredit.getIdRemissionPk().toString(), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(lastCredit.getDate(), FontFactory.getFont("Times New Roman", 10)));
                    //Se comprueba si es de cliente o de proveedor
                    if(lastCredit.getIdClientFk()!=null)
                        personName = lastCredit.getIdClientFk().getTotalName();
                    else
                        personName = lastCredit.getIdSupplierFk().getTotalName();
                    header.addCell(new Paragraph(personName, FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(format.format(rem.getLeftAmount()), FontFactory.getFont("Times New Roman", 10)));
                    total += rem.getLeftAmount();
                }
                break;
            case "minimumStock":
                Collection<MinimumStock> minimumStocks = data;
                for (MinimumStock min : minimumStocks) {
                    if(rows%2==0){
                        header.getDefaultCell().setBackgroundColor(row2);
                    }else{
                        header.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    }
                    rows++;
                    header.addCell(new Paragraph(min.getSkuProduct(), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(min.getProductReference(), FontFactory.getFont("Times New Roman", 10)));
                    PdfPCell minimimCell = new PdfPCell(new Paragraph(String.valueOf(min.getCantidadMinima()), FontFactory.getFont("Times New Roman", 10)));
                    minimimCell.setBackgroundColor(new CMYKColor(4, 16, 62, 0));
                    minimimCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    minimimCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    header.addCell(minimimCell);
                    PdfPCell alvailableCell = new PdfPCell(new Paragraph(String.valueOf(min.getCantidadDisponible()), FontFactory.getFont("Times New Roman", 10)));
                    alvailableCell.setBackgroundColor(new CMYKColor(42, 0, 57, 0));
                    alvailableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    alvailableCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    header.addCell(alvailableCell);
                    PdfPCell diferenceCell = new PdfPCell(new Paragraph(String.valueOf(min.getDiferencia()), FontFactory.getFont("Times New Roman", 10)));
                    diferenceCell.setBackgroundColor(new CMYKColor(80, 19, 0, 0));
                    diferenceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    diferenceCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    header.addCell(diferenceCell);
                }
                break;
            case "productsMovements":
                Collection<Product> products = data;
                ReportsModel rm = new ReportsModel();
                for (Product product : products) {
                    int entradas = 0,
                            ventas = 0,
                            obsequios = 0,
                            deterioro = 0,
                            salidas = 0,
                            compras = 0,
                            cargues = 0,
                            garantias = 0;
                    Collection<MovimientosProducto> movList = rm.productMovementDate(product.getIdProductPk(), startDate, endDate);
                    for (MovimientosProducto movimientosProducto : movList) {
                        switch (movimientosProducto.getIdConceptPk()) {
                            case 4:
                                garantias += movimientosProducto.getAmount();
                                break;
                            case 2:
                                ventas += movimientosProducto.getAmount();
                                salidas += movimientosProducto.getAmount();
                                break;
                            case 6:
                                deterioro += movimientosProducto.getAmount();
                                salidas += movimientosProducto.getAmount();
                                break;
                            case 5:
                                obsequios += movimientosProducto.getAmount();
                                salidas += movimientosProducto.getAmount();
                                break;
                            case 1:
                                compras += movimientosProducto.getAmount();
                                entradas += movimientosProducto.getAmount();
                                break;
                            default:
                                cargues += movimientosProducto.getAmount();
                                entradas += movimientosProducto.getAmount();
                                break;
                        }
                    }
                    if(rows%2==0){
                        header.getDefaultCell().setBackgroundColor(row2);
                    }else{
                        header.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    }
                    header.addCell(new Paragraph(product.getSkuProduct(), FontFactory.getFont("Times New Roman", 8)));
                    header.addCell(new Paragraph(product.getProductReference(), FontFactory.getFont("Times New Roman", 8)));
                    //Se ingresan los datos para la tabla interna que desglosa las entradas
                    PdfPTable entryTable = new PdfPTable(3);
                    PdfPCell entryCell = new PdfPCell(new Paragraph(String.valueOf(compras), FontFactory.getFont("Times New Roman", 8)));
                    entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    entryCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    entryCell.setBackgroundColor(new CMYKColor(80, 19, 0, 0));
                    entryTable.addCell(entryCell);
                    entryCell = new PdfPCell(new Paragraph(String.valueOf(cargues), FontFactory.getFont("Times New Roman", 8)));
                    entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    entryCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    entryCell.setBackgroundColor(new CMYKColor(4, 16, 62, 0));
                    entryTable.addCell(entryCell);
                    entryCell = new PdfPCell(new Paragraph(String.valueOf(entradas), FontFactory.getFont("Times New Roman", 8)));
                    entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    entryCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    if(rows%2==0){
                        entryCell.setBackgroundColor(row2);
                    }else{
                        entryCell.setBackgroundColor(BaseColor.WHITE);
                    }
                    entryTable.addCell(entryCell);
                    cell = new PdfPCell(entryTable);
                    header.addCell(cell);
                    //header.addCell(new Paragraph(String.valueOf(entradas), FontFactory.getFont("Times New Roman", 10)));
                    //Se ingresan los datos para la tabla interna que desglosa las salidas
                    entryTable = new PdfPTable(5);
                    entryCell = new PdfPCell(new Paragraph(String.valueOf(ventas), FontFactory.getFont("Times New Roman", 8)));
                    entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    entryCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    entryCell.setBackgroundColor(new CMYKColor(42, 0, 57, 0));
                    entryTable.addCell(entryCell);
                    entryCell = new PdfPCell(new Paragraph(String.valueOf(garantias), FontFactory.getFont("Times New Roman", 8)));
                    entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    entryCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    entryCell.setBackgroundColor(new CMYKColor(0, 59, 32, 0));
                    entryTable.addCell(entryCell);
                    entryCell = new PdfPCell(new Paragraph(String.valueOf(obsequios), FontFactory.getFont("Times New Roman", 8)));
                    entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    entryCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    entryCell.setBackgroundColor(new CMYKColor(37, 38, 46, 0));
                    entryTable.addCell(entryCell);
                    entryCell = new PdfPCell(new Paragraph(String.valueOf(deterioro), FontFactory.getFont("Times New Roman", 8)));
                    entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    entryCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    entryCell.setBackgroundColor(new CMYKColor(0, 29, 47, 0));
                    entryTable.addCell(entryCell);
                    entryCell = new PdfPCell(new Paragraph(String.valueOf(salidas), FontFactory.getFont("Times New Roman", 8)));
                    entryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    entryCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    if(rows%2==0){
                        entryCell.setBackgroundColor(row2);
                    }else{
                        entryCell.setBackgroundColor(BaseColor.WHITE);
                    }
                    entryTable.addCell(entryCell);
                    cell = new PdfPCell(entryTable);
                    header.addCell(cell);
                    //header.addCell(new Paragraph(String.valueOf(salidas), FontFactory.getFont("Times New Roman", 10)));
                    cell = new PdfPCell(new Paragraph(product.getCantidadDisponible(), FontFactory.getFont("Times New Roman", 8)));
                    cell.setBorderColor(BaseColor.LIGHT_GRAY);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    if(rows%2==0){
                        cell.setBackgroundColor(row2);
                    }else{
                        cell.setBackgroundColor(BaseColor.WHITE);
                    }
                    rows++;
                    header.addCell(cell);
                }
                break;
            case "personCredits":
                Collection<Remission> credits = data;
                Tranzaction tran = new Tranzaction();
                for (Remission rem : credits) {
                    if (rem.getInvoiced() == 1 || rem.getIdConceptFk().getConceptType().equals("C") || rem.getIdConceptFk().getIdConceptPk() == 1) {
                        if(rows%2==0){
                            header.getDefaultCell().setBackgroundColor(row2);
                        }else{
                            header.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                        }
                        rows++;
                        for (Tranzaction tra : rem.getTranzactionCollection()) {
                            tran = tra;
                        }
                        if (rem.getIdInvoiceFk() != null) {
                            header.addCell(new Paragraph("0" + String.valueOf(rem.getIdInvoiceFk().getIdInvoicePk()), FontFactory.getFont("Times New Roman", 10)));
                        } else {
                            header.addCell(new Paragraph("0" + String.valueOf(rem.getIdRemissionPk()), FontFactory.getFont("Times New Roman", 10)));
                        }
                        header.addCell(new Paragraph(mediumDf.format(rem.getRemissionDate()), FontFactory.getFont("Times New Roman", 10)));
                        if (rem.getInvoiced() == 1) {
                            header.addCell(new Paragraph(rem.getIdInvoiceFk().getObs(), FontFactory.getFont("Times New Roman", 10)));
                        } else {
                            header.addCell(new Paragraph(rem.getObs(), FontFactory.getFont("Times New Roman", 10)));
                        }
                        header.addCell(new Paragraph(rem.getIdConceptFk().getConceptName(), FontFactory.getFont("Times New Roman", 10)));
                        if (rem.getInvoiced() == 1 || rem.getIdConceptFk().getIdConceptPk() == 1) {
                            header.addCell(new Paragraph(format.format(tran.getTransactionPrice()), FontFactory.getFont("Times New Roman", 10)));
                            total += tran.getTransactionPrice();
                        } else {
                            header.addCell(new Paragraph(format.format(rem.getDeposit()), FontFactory.getFont("Times New Roman", 10)));
                            total += rem.getDeposit();
                        }
                    } else {
                        principalRemission = rem;
                        for (Deposit deposit : rem.getDepositCollection()) {
                            if(rows%2==0){
                                header.getDefaultCell().setBackgroundColor(row2);
                            }else{
                                header.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                            }
                            rows++;
                            header.addCell(new Paragraph("0" + String.valueOf(deposit.getIdDepositPk()), FontFactory.getFont("Times New Roman", 10)));
                            header.addCell(new Paragraph(mediumDf.format(deposit.getDepositDate()), FontFactory.getFont("Times New Roman", 10)));
                            header.addCell(new Paragraph(deposit.getObs(), FontFactory.getFont("Times New Roman", 10)));
                            header.addCell(new Paragraph("Abono", FontFactory.getFont("Times New Roman", 10)));
                            header.addCell(new Paragraph(format.format(deposit.getDeposit()), FontFactory.getFont("Times New Roman", 10)));
                            subtotal += deposit.getDeposit();
                        }
                        /*header.addCell(new Paragraph("0" + String.valueOf(rem.getIdRemissionPk()), FontFactory.getFont("Times New Roman", 10)));
                         header.addCell(new Paragraph(mediumDf.format(rem.getRemissionDate()), FontFactory.getFont("Times New Roman", 10)));
                         header.addCell(new Paragraph(rem.getObs(), FontFactory.getFont("Times New Roman", 10)));
                         header.addCell(new Paragraph(rem.getIdConceptFk().getConceptName(), FontFactory.getFont("Times New Roman", 10)));
                         header.addCell(new Paragraph(format.format(rem.getDeposit()), FontFactory.getFont("Times New Roman", 10)));
                         subtotal += rem.getDeposit();*/
                    }
                }
                if (tran != null) {
                    remission = tran.getIdRemissionFk();
                }
                break;
            default:
                Collection<FacturasAnuladas> invalidatedInvoices = data;
                for (FacturasAnuladas factan : invalidatedInvoices) {
                    if(rows%2==0){
                        header.getDefaultCell().setBackgroundColor(row2);
                    }else{
                        header.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    }
                    rows++;
                    header.addCell(new Paragraph("0" + String.valueOf(factan.getIdInvoicePk()), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(mediumDf.format(factan.getInvoiceDate()), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(factan.getObs(), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(factan.getClient(), FontFactory.getFont("Times New Roman", 10)));
                    header.addCell(new Paragraph(format.format(factan.getTransactionPrice()), FontFactory.getFont("Times New Roman", 10)));
                }
                break;
        }
        //footer
        String txtTotales = "";
        if (!type.equals("productsMovements") && !type.equals("minimumStock") && !type.equals("productOrders")) {
            txtTotales = "Totales\n";
        }
        Paragraph totales = new Paragraph(txtTotales, FontFactory.getFont("Times New Roman", 12, Font.BOLD));
        totales.setAlignment(Element.ALIGN_RIGHT);
        switch (type) {
            case "totalInvoices":
                if(!totalTaxes.isEmpty()){
                    subtotal = total-subtotal;
                    totales.add(bundle.getString("lblReportSubTotal") +" "+ bundle.getString("moneyNotation") + format.format(subtotal)+"\n");
                    for(Tax tax:totalTaxes){
                        totales.add(tax.toString() + bundle.getString("moneyNotation")+" " + format.format(tax.getTaxSum())+"\n");
                    }
                    totales.add(bundle.getString("lblReportTotal") + bundle.getString("moneyNotation")+" " + format.format(total));
                }else{
                    totales.add(bundle.getString("lblReportTotal") + bundle.getString("moneyNotation")+" " + format.format(total));
                }
                break;
            case "totalOrders":
                if(!totalTaxes.isEmpty()){
                    subtotal = total-subtotal;
                    totales.add(bundle.getString("lblReportSubTotal") +" "+ bundle.getString("moneyNotation") + format.format(subtotal)+"\n");
                    for(Tax tax:totalTaxes){
                        totales.add(tax.toString() + bundle.getString("moneyNotation")+" " + format.format(tax.getTaxSum())+"\n");
                    }
                    totales.add(bundle.getString("lblReportTotal") + bundle.getString("moneyNotation")+" " + format.format(total));
                }else{
                    totales.add(bundle.getString("lblReportTotal") + bundle.getString("moneyNotation")+" " + format.format(total));
                }
                break;
            case "totalEarnings":
                totales.add("\nTotal ganancias estimadas: "+bundle.getString("moneyNotation")+format.format(total));
                break;
            case "inventory":
                totales.add("\nProductos totales: " + Math.round(subtotal) + "\nValor total estimado: $" + format.format(total));
                break;
            case "productsMovements":
            case "minimumStock":
            case "topSelled":
            case "productOrders":
                //nothing to do
                break;
            case "productMovements":
                ReportsModel rpm = new ReportsModel();
                totales.add("\nIngresos totales: " + Math.round(subtotal) + "\nSalidas totales: " + Math.round(total)
                        + "\nGarantías totales: " + Math.round(iva) + "\nCantidad disponible: " + rpm.productTotalStock(idProduct));
                break;
            case "personCredits":
                RemissionModel rm = new RemissionModel();
                if (principalRemission.getIdClientFk() == null && principalRemission.getIdSupplierFk() == null) {
                    //Se determina si es crédito de cliente o de proveedor
                    try{
                        principalRemission = rm.findByIdCredit(remission.getIdClientFk().getIdPersonPk().toString());
                    }catch(Exception e){
                        principalRemission = rm.findByIdCredit(remission.getIdSupplierFk().getIdPersonPk().toString());
                    }
                }
                totales.add("\nTotal: $" + format.format(total) + "\nAbono: $" + format.format(subtotal) + "\nSaldo: $" + format.format(principalRemission.getLeftAmount()));
                break;
            case "totalEntries":
                totales.add("\nTotal en efectivo: $" + format.format(subtotal) + "\nTotal por tarjeta: $" + format.format(iva) + "\nTotal: $" + format.format(total));
                break;
            case "activeCredit":
                totales.add("\nTotal: $" + format.format(total));
                break;
            default:
                totales.add("\nTotal: $" + format.format(total));
                break;
        }
        documento.add(titulo);
        documento.add(fecharealizado);
        if (!type.equals("inventory") && !type.equals("productMovements") && !type.equals("productOrders") && !type.equals("personCredits") && !type.equals("minimumStock") && !type.equals("activeCredit")) {
            documento.add(fechaDesdeHasta);
        }
        documento.add(new Paragraph(" "));
        documento.add(header);
        documento.add(new Paragraph(" "));
        documento.add(totales);

        documento.close();
        openPDF("reporte.pdf");
    }

    /*
     Método para generar el reporte de cierre de caja
     @Param: dia a reportar (Date day), bundle del idioma actual para los mensajes
     */
    public void printCloseBoxReport(Date day, ResourceBundle bundle) throws ParseException, FileNotFoundException, DocumentException, IOException {
        //Se especifica el día para realizar la búsqueda de turnos
        Date dayToReport;
        int cont = 1;
        double totalEfectivo = 0, totalTarjeta = 0, ingresosTotales = 0;
        double totalCompras = 0, totalPagos = 0, egresosTotales = 0;
        double totalEsperado = 0, totalReal = 0, totalDiferencia = 0;
        SimpleDateFormat mediumDf = new SimpleDateFormat("EEEE d MMMM yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM yyyy, hh:mm aaa");
        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm aaa");
        DecimalFormat format = new DecimalFormat("###,###.00");
        Date defaultDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
        if (day != null) {
            dayToReport = day;
        } else {
            SimpleDateFormat sdfComplete = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            SimpleDateFormat sdfDay = new SimpleDateFormat("dd-M-yyyy");
            String today = sdfDay.format(Calendar.getInstance().getTime());
            today += " 00:00:00";
            dayToReport = sdfComplete.parse(today);
        }
        Date dayToReportNigth = new Date(dayToReport.getTime() + 23 * HOUR);
        //Se inicia la configuración del pdf a generar
        Rectangle pageSize = PageSize.LETTER;
        Document documento = new Document(pageSize, 25, 25, 20, 20);
        FileOutputStream ficheroPdf = new FileOutputStream("cierre.pdf");
        PdfWriter.getInstance(documento, ficheroPdf);
        documento.open();
        TurnModel tm = new TurnModel();
        Settings settings = (Settings) tm.findByIdInt("Settings", 1);

        //header
        Paragraph titulo = new Paragraph("Cierre de caja " + mediumDf.format(dayToReport) + " - " + settings.getBussinesName(), FontFactory.getFont("Times New Roman", 14, Font.BOLD));
        titulo.setAlignment(Element.ALIGN_CENTER);
        Paragraph fecharealizado = new Paragraph("Realizado el: " + sdf.format(defaultDate), FontFactory.getFont("Times New Roman", 10));
        fecharealizado.setAlignment(Element.ALIGN_CENTER);
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(90);
        header.getDefaultCell().setBorderColor(BaseColor.WHITE);
        PdfPCell principalCell;
        PdfPCell valuesCell;
        PdfPTable valuesTable;
        //Se cargan los turnos asociados al día seleccionado
        Collection<Turn> tc = tm.findAllDate("Turn", dayToReport, dayToReportNigth);
        for (Turn turn : tc) {
            double efectivoFacturas = 0, efectivoAbonos = 0, ingresosTarjeta = 0, totalIngresos = 0;
            double compras = 0, pagos = 0, totalEgresos = 0;
            //Se especifica un mensaje en caso de que algún turno no haya sido finalizado
            String finalDate;
            if (turn.getEndDate() != null) {
                finalDate = sdf.format(turn.getEndDate());
            } else {
                finalDate = "(aún no ha sido finalizado)";
            }
            principalCell = new PdfPCell(new Paragraph("Turno #" + cont + ": " + turn.getIdUserFk().getTotalName() + ", iniciado a las " + sdfTime.format(turn.getStartDate()) + ", finalizado el " + finalDate + "\n\n", FontFactory.getFont("Times New Roman", 10, Font.BOLD)));
            principalCell.setBorderColor(BaseColor.WHITE);
            principalCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            header.addCell(principalCell);
            //Parte de ingresos
            valuesCell = new PdfPCell(new Paragraph("Ingresos", FontFactory.getFont("Times New Roman", 10)));
            valuesCell.setBackgroundColor(new CMYKColor(42, 0, 57, 0));
            valuesCell.setBorderColor(new CMYKColor(42, 0, 57, 0));
            header.addCell(valuesCell);
            Collection<TotalesIngresos> entries = tm.findByTurn("TotalesIngresos", turn.getIdTurnPk());
            valuesTable = new PdfPTable(4);
            valuesTable.setWidthPercentage(100);
            valuesTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            valuesTable.getDefaultCell().setBorderColor(new CMYKColor(42, 0, 57, 0));
            valuesTable.addCell(new Paragraph("Facturas en efectivo", FontFactory.getFont("Times New Roman", 10)));
            valuesTable.addCell(new Paragraph("Abonos en efectivo", FontFactory.getFont("Times New Roman", 10)));
            valuesTable.addCell(new Paragraph("Tarjeta", FontFactory.getFont("Times New Roman", 10)));
            valuesTable.addCell(new Paragraph("Total", FontFactory.getFont("Times New Roman", 10, Font.BOLD)));
            /// Parte de especificación del tipo de pago y sumatoria de Totales
            for (TotalesIngresos entry : entries) {
                if (entry.getIdPaymentFk() == null || entry.getIdPaymentFk().equals(1)) {
                    efectivoFacturas += entry.getTotal();
                    totalEfectivo += entry.getTotal();
                    totalIngresos += entry.getTotal();
                } else if (entry.getIdPaymentFk().equals(2)) {
                    efectivoAbonos += entry.getTotal();
                    totalEfectivo += entry.getTotal();
                    totalIngresos += entry.getTotal();
                } else {
                    ingresosTarjeta += entry.getTotal();
                    totalTarjeta += entry.getTotal();
                    totalIngresos += entry.getTotal();
                }
            }
            valuesTable.addCell(new Paragraph(format.format(efectivoFacturas), FontFactory.getFont("Times New Roman", 10)));
            valuesTable.addCell(new Paragraph(format.format(efectivoAbonos), FontFactory.getFont("Times New Roman", 10)));
            valuesTable.addCell(new Paragraph(format.format(ingresosTarjeta), FontFactory.getFont("Times New Roman", 10)));
            valuesTable.addCell(new Paragraph(format.format(totalIngresos), FontFactory.getFont("Times New Roman", 10, Font.BOLD)));
            ingresosTotales += totalIngresos;
            valuesCell = new PdfPCell(valuesTable);
            header.addCell(valuesCell);
            header.addCell(new Paragraph(" "));
            //Parte de Egresos
            valuesCell = new PdfPCell(new Paragraph("Egresos", FontFactory.getFont("Times New Roman", 10)));
            valuesCell.setBackgroundColor(new CMYKColor(0, 59, 32, 0));
            valuesCell.setBorderColor(new CMYKColor(0, 59, 32, 0));
            header.addCell(valuesCell);
            Collection<TotalesEgresos> outs = tm.findByTurn("TotalesEgresos", turn.getIdTurnPk());
            valuesTable = new PdfPTable(3);
            valuesTable.setWidthPercentage(100);
            valuesTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            valuesTable.getDefaultCell().setBorderColor(new CMYKColor(0, 59, 32, 0));
            valuesTable.addCell(new Paragraph("Compras a proveedor", FontFactory.getFont("Times New Roman", 10)));
            valuesTable.addCell(new Paragraph("Pagos", FontFactory.getFont("Times New Roman", 10)));
            valuesTable.addCell(new Paragraph("Total", FontFactory.getFont("Times New Roman", 10, Font.BOLD)));
            /// Parte de especificación del tipo de pago y sumatoria de Totales
            for (TotalesEgresos out : outs) {
                if (out.getConcept().equals("Compra a proveedor")) {
                    compras += out.getTotal();
                    totalCompras += out.getTotal();
                } else {
                    pagos += out.getTotal();
                    totalCompras += out.getTotal();
                }
            }
            valuesTable.addCell(new Paragraph(format.format(compras), FontFactory.getFont("Times New Roman", 10)));
            valuesTable.addCell(new Paragraph(format.format(pagos), FontFactory.getFont("Times New Roman", 10)));
            valuesTable.addCell(new Paragraph(format.format(totalCompras), FontFactory.getFont("Times New Roman", 10, Font.BOLD)));
            valuesCell = new PdfPCell(valuesTable);
            header.addCell(valuesCell);
            header.addCell(new Paragraph(" "));
            //Parte de Caja
            valuesCell = new PdfPCell(new Paragraph("Caja", FontFactory.getFont("Times New Roman", 10)));
            //valuesCell.setBackgroundColor(new CMYKColor(0, 29, 47, 0)); --------------------------------------------- Color crema
            valuesCell.setBackgroundColor(new CMYKColor(80, 19, 0, 0));
            valuesCell.setBorderColor(new CMYKColor(80, 19, 0, 0));
            header.addCell(valuesCell);
            if (turn.getRealAmount() != null) {
                valuesTable = new PdfPTable(4);
                valuesTable.setWidthPercentage(100);
                valuesTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                valuesTable.getDefaultCell().setBorderColor(new CMYKColor(80, 19, 0, 0));
                valuesTable.addCell(new Paragraph("Dinero inicial en caja", FontFactory.getFont("Times New Roman", 10)));
                valuesTable.addCell(new Paragraph("Dinero esperado en caja", FontFactory.getFont("Times New Roman", 10)));
                valuesTable.addCell(new Paragraph("Dinero real en caja", FontFactory.getFont("Times New Roman", 10)));
                valuesTable.addCell(new Paragraph("Diferencia", FontFactory.getFont("Times New Roman", 10, Font.BOLD)));
                valuesTable.addCell(new Paragraph(format.format(turn.getInitialAmount()), FontFactory.getFont("Times New Roman", 10)));
                valuesTable.addCell(new Paragraph(format.format(turn.getExpectedAmount()), FontFactory.getFont("Times New Roman", 10)));
                valuesTable.addCell(new Paragraph(format.format(turn.getRealAmount()), FontFactory.getFont("Times New Roman", 10)));
                //Se adapta para en caso de que el resultado sea negativo, la diferencia no aumente
                double diference;
                if(turn.getExpectedAmount()>0){
                    diference = turn.getRealAmount() - turn.getExpectedAmount();
                }else{
                    diference = turn.getRealAmount() + turn.getExpectedAmount();
                }
                valuesTable.addCell(new Paragraph(format.format(diference), FontFactory.getFont("Times New Roman", 10, Font.BOLD)));
                totalEsperado += turn.getExpectedAmount();
                totalReal += turn.getRealAmount();
                totalDiferencia += diference;
                valuesCell = new PdfPCell(valuesTable);
                header.addCell(valuesCell);
            } else {
                valuesCell = new PdfPCell(new Paragraph("No se muestran los valores de caja debido a que no se ha cerrado el turno aún", FontFactory.getFont("Times New Roman", 10)));
                header.addCell(valuesCell);
            }
            header.addCell(new Paragraph(" "));
            header.addCell(new Paragraph(" "));
            header.addCell(new Paragraph(" "));
            cont++;
        }
        //Parte de totales
        Paragraph totales = new Paragraph("Totales", FontFactory.getFont("Times New Roman", 12, Font.BOLD));
        totales.setAlignment(Element.ALIGN_RIGHT);
        totales.add("\nIngresos totales efectivo: $" + format.format(totalEfectivo) + "\nIngresos totales tarjeta: $" + format.format(totalTarjeta)
                + "\nTotal: $" + format.format(ingresosTotales) + "\n\n Dinero esperado en caja: $" + format.format(totalEsperado)
                + "\n Dinero real en caja: $" + format.format(totalReal) + "\n Diferencia: $" + format.format(totalDiferencia));
        //Parte de armado del PDF
        documento.add(titulo);
        documento.add(fecharealizado);
        documento.add(new Paragraph(" "));
        documento.add(header);
        documento.add(new Paragraph(" "));
        documento.add(totales);

        documento.close();
        openPDF("cierre.pdf");
    }

    /*
     Método para generar el código de barras en un archivo PDF
     @param: código a generar
     */
    public void printBarcode(String code, String productName, Double prodPrice) throws FileNotFoundException, DocumentException, IOException {
        Rectangle pagesize = new Rectangle(215, 327);
        Document documento = new Document(pagesize, 5, 5, 5, 5);
        PdfWriter docWriter = PdfWriter.getInstance(documento, new FileOutputStream("barcode.pdf"));
        DecimalFormat format = new DecimalFormat("###,###.00");

        documento.open();
        PdfPTable table = new PdfPTable(1);
        table.getDefaultCell().setBorder(1);
        PdfPCell cell = new PdfPCell();
        Font bold = new Font();
        bold.setStyle(Font.BOLD);
        Paragraph prodName = new Paragraph(productName + "\n$" + format.format(prodPrice), bold);
        prodName.setAlignment(Element.ALIGN_CENTER);
        PdfContentByte cb = docWriter.getDirectContent();
        Barcode128 code128 = new Barcode128();
        code128.setCodeType(Barcode128.CODE128);
        code128.setCode(code);
        Image barcode = code128.createImageWithBarcode(cb, null, null);
        barcode.scalePercent(190);
        barcode.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(prodName);
        cell.addElement(barcode);
        table.addCell(cell);
        documento.add(table);
        documento.close();
        openPDF("barcode.pdf");
    }

    /*
     Método para generar el código de barras de todos los productos en un archivo PDF
     @param: código a generar
     */
    public void printAllProductsBarcode(Collection<Product> list) throws FileNotFoundException, DocumentException, IOException {
        Document documento = new Document(PageSize.LETTER.rotate(), 10, 10, 20, 10);
        PdfWriter docWriter = PdfWriter.getInstance(documento, new FileOutputStream("barcode.pdf"));
        DecimalFormat format = new DecimalFormat("###,###.00");

        documento.open();
        PdfPTable table = new PdfPTable(4);
        table.setWidths(new int[]{25, 25, 20, 20});
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(1);
        Font bold = new Font();
        bold.setStyle(Font.BOLD);
        for (Product product : list) {
            Paragraph prodName = new Paragraph(product.getProductReference() + "\n$" + format.format(product.getActualPrice()), bold);
            prodName.setAlignment(Element.ALIGN_CENTER);
            PdfContentByte cb = docWriter.getDirectContent();
            Barcode128 code128 = new Barcode128();
            code128.setCodeType(Barcode128.CODE128);
            code128.setCode(product.getSkuProduct());
            Image barcode = code128.createImageWithBarcode(cb, null, null);
            barcode.scalePercent(190);
            barcode.setAlignment(Element.ALIGN_CENTER);
            PdfPCell cell = new PdfPCell();
            cell.addElement(prodName);
            cell.addElement(barcode);
            table.addCell(cell);
        }
        documento.add(table);
        documento.close();
        openPDF("barcode.pdf");
    }

    public void openPDF(String url) throws IOException {
        if(Desktop.isDesktopSupported()){
            String path = System.getProperty("user.dir");
            path += "\\" + url;
            Desktop desktop = Desktop.getDesktop();
            desktop.open(new File(path));
        }else{
            try {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } catch (Exception e) {
                Runtime.getRuntime().exec(new String[]{"open", url});
            }
        }
    }
}

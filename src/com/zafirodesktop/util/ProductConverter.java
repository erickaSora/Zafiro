/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.util;

import com.zafirodesktop.entity.Discount;
import com.zafirodesktop.entity.Tax;
import java.text.DecimalFormat;
import java.util.Collection;

/**
 *
 * @author Digitall
 */
public class ProductConverter {
    
    private Integer idProductPk;
    private String skuProduct;
    private Double actualPrice;
    private String productDescription;
    private String totalPrice; 
    private int amount = 1;
    private String taxSelected;
    private ComboBoxChoices byDefault;
    private Collection<Tax> taxesCollection;
    private Collection<Discount> discountCollection;
    private DecimalFormat format;
    
    public ProductConverter(){
    }

    public ProductConverter(Integer idProductPk, String skuProduct, Double actualPrice, String productDescription) {
        this.idProductPk = idProductPk;
        this.skuProduct = skuProduct;
        this.actualPrice = actualPrice;
        this.productDescription = productDescription;
        format = new DecimalFormat("###,###.00");
        totalPrice = format.format(actualPrice);
        byDefault = new ComboBoxChoices("-1", "N/A");
        taxSelected = byDefault.getItemLabel();
    }

    public ProductConverter(Integer idProductPk, Double actualPrice, String productDescription) {
        this.idProductPk = idProductPk;
        this.actualPrice = actualPrice;
        this.productDescription = productDescription;
        format = new DecimalFormat("###,###.00");
        totalPrice = format.format(actualPrice);
        byDefault = new ComboBoxChoices("-1", "N/A");
        taxSelected = byDefault.getItemLabel();
    }

    public Integer getIdProductPk() {
        return idProductPk;
    }

    public void setIdProductPk(Integer idProductPk) {
        this.idProductPk = idProductPk;
    }

    public String getSkuProduct() {
        return skuProduct;
    }

    public void setSkuProduct(String skuProduct) {
        this.skuProduct = skuProduct;
    }

    public Double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setTotalPrice(String totalPrice){
        this.totalPrice = totalPrice;
    }
    
    public String getTotalPrice() {
        return totalPrice;
    }

    public String getTaxSelected() {
        return taxSelected;
    }

    public void setTaxSelected(String taxSelected) {
        this.taxSelected = taxSelected;
    }

    public ComboBoxChoices getByDefault() {
        return byDefault;
    }

    public void setByDefault(ComboBoxChoices byDefault) {
        this.byDefault = byDefault;
    }
    
    public Collection<Tax> getTaxesCollection() {
        return taxesCollection;
    }

    public void setTaxesCollection(Collection<Tax> taxesCollection) {
        this.taxesCollection = taxesCollection;
    }

    public Collection<Discount> getDiscountCollection() {
        return discountCollection;
    }

    public void setDiscountCollection(Collection<Discount> discountCollection) {
        this.discountCollection = discountCollection;
    }
    
}

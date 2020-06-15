/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "TOP_SELLED")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TopSelled.findAll", query = "SELECT t FROM TopSelled t"),
    @NamedQuery(name = "TopSelled.findByRemissionDate", query = "SELECT t FROM TopSelled t WHERE t.remissionDate = :remissionDate"),
    @NamedQuery(name = "TopSelled.findBySkuProduct", query = "SELECT t FROM TopSelled t WHERE t.skuProduct = :skuProduct"),
    @NamedQuery(name = "TopSelled.findByProductReference", query = "SELECT t FROM TopSelled t WHERE t.productReference = :productReference"),
    @NamedQuery(name = "TopSelled.findByProductDescription", query = "SELECT t FROM TopSelled t WHERE t.productDescription = :productDescription"),
    @NamedQuery(name = "TopSelled.findByAmount", query = "SELECT t FROM TopSelled t WHERE t.amount = :amount")})
public class TopSelled implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "REMISSION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date remissionDate;
    @Column(name = "SKU_PRODUCT")
    private String skuProduct;
    @Id
    @Basic(optional = false)
    @Column(name = "PRODUCT_REFERENCE")
    private String productReference;
    @Column(name = "PRODUCT_DESCRIPTION")
    private String productDescription;
    @Column(name = "AMOUNT")
    private Integer amount;

    public TopSelled() {
    }

    public Date getRemissionDate() {
        return remissionDate;
    }

    public void setRemissionDate(Date remissionDate) {
        this.remissionDate = remissionDate;
    }

    public String getSkuProduct() {
        return skuProduct;
    }

    public void setSkuProduct(String skuProduct) {
        this.skuProduct = skuProduct;
    }

    public String getProductReference() {
        return productReference;
    }

    public void setProductReference(String productReference) {
        this.productReference = productReference;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
}

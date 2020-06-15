/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "INVENTORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inventory.findAll", query = "SELECT i FROM Inventory i"),
    @NamedQuery(name = "Inventory.findByIdProductPk", query = "SELECT i FROM Inventory i WHERE i.idProductPk = :idProductPk"),
    @NamedQuery(name = "Inventory.findByProductReference", query = "SELECT i FROM Inventory i WHERE i.productReference = :productReference"),
    @NamedQuery(name = "Inventory.findByProductDescription", query = "SELECT i FROM Inventory i WHERE i.productDescription = :productDescription"),
    @NamedQuery(name = "Inventory.findByActualPrice", query = "SELECT i FROM Inventory i WHERE i.actualPrice = :actualPrice"),
    @NamedQuery(name = "Inventory.findByAmount", query = "SELECT i FROM Inventory i WHERE i.amount = :amount")})
public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "ID_PRODUCT_PK")
    @Id
    private String idProductPk;
    @Basic(optional = false)
    @Column(name = "PRODUCT_REFERENCE")
    private String productReference;
    @Column(name = "PRODUCT_DESCRIPTION")
    private String productDescription;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ACTUAL_PRICE")
    private Float actualPrice;
    @Column(name = "TOTAL")
    private Float total;
    @Column(name = "MINIMUM")
    private Integer minimum;
    @Column(name = "AMOUNT")
    private Integer amount;

    public Inventory() {
    }

    public String getIdProductPk() {
        return idProductPk;
    }

    public void setIdProductPk(String idProductPk) {
        this.idProductPk = idProductPk;
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

    public Float getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Float actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }
    
}

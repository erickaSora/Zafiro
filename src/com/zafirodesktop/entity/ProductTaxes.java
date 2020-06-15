/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "PRODUCT_TAXES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductTaxes.findAll", query = "SELECT p FROM ProductTaxes p"),
    @NamedQuery(name = "ProductTaxes.findByIdProductFk", query = "SELECT p FROM ProductTaxes p WHERE p.productTaxesPK.idProductFk = :idProductFk"),
    @NamedQuery(name = "ProductTaxes.findByIdTaxFk", query = "SELECT p FROM ProductTaxes p WHERE p.productTaxesPK.idTaxFk = :idTaxFk"),
    @NamedQuery(name = "ProductTaxes.findById", query = "SELECT p FROM ProductTaxes p WHERE p.id = :id")})
public class ProductTaxes implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductTaxesPK productTaxesPK;
    @Column(name = "ID")
    private String id;
    @JoinColumn(name = "ID_TAX_FK", referencedColumnName = "ID_TAX_PK", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Tax tax;
    @JoinColumn(name = "ID_PRODUCT_FK", referencedColumnName = "ID_PRODUCT_PK", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Product product;

    public ProductTaxes() {
    }

    public ProductTaxes(ProductTaxesPK productTaxesPK) {
        this.productTaxesPK = productTaxesPK;
    }

    public ProductTaxes(Integer idProductFk, int idTaxFk) {
        this.productTaxesPK = new ProductTaxesPK(idProductFk, idTaxFk);
    }

    public ProductTaxes(Tax tax, Product product) {
        this.tax = tax;
        this.product = product;
    }

    public ProductTaxesPK getProductTaxesPK() {
        return productTaxesPK;
    }

    public void setProductTaxesPK(ProductTaxesPK productTaxesPK) {
        this.productTaxesPK = productTaxesPK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        if(!product.getProductTaxesCollection().contains(this)){
            product.getProductTaxesCollection().add(this);
        }
    }
    
    public String getTaxInformation(){
        return tax.getTaxName()+" | "+tax.getPercentage();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productTaxesPK != null ? productTaxesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductTaxes)) {
            return false;
        }
        ProductTaxes other = (ProductTaxes) object;
        if ((this.productTaxesPK == null && other.productTaxesPK != null) || (this.productTaxesPK != null && !this.productTaxesPK.equals(other.productTaxesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.ProductTaxes[ productTaxesPK=" + productTaxesPK + " ]";
    }
    
}

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
@Table(name = "PRODUCT_DISCOUNT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductDiscount.findAll", query = "SELECT p FROM ProductDiscount p"),
    @NamedQuery(name = "ProductDiscount.findByIdDiscountFk", query = "SELECT p FROM ProductDiscount p WHERE p.productDiscountPK.idDiscountFk = :idDiscountFk"),
    @NamedQuery(name = "ProductDiscount.findByIdProductFk", query = "SELECT p FROM ProductDiscount p WHERE p.productDiscountPK.idProductFk = :idProductFk"),
    @NamedQuery(name = "ProductDiscount.findById", query = "SELECT p FROM ProductDiscount p WHERE p.id = :id")})
public class ProductDiscount implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductDiscountPK productDiscountPK;
    @Column(name = "ID")
    private String id;
    @JoinColumn(name = "ID_PRODUCT_FK", referencedColumnName = "ID_PRODUCT_PK", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Product product;
    @JoinColumn(name = "ID_DISCOUNT_FK", referencedColumnName = "ID_DISCOUNT_PK", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Discount discount;

    public ProductDiscount() {
    }

    public ProductDiscount(ProductDiscountPK productDiscountPK) {
        this.productDiscountPK = productDiscountPK;
    }

    public ProductDiscount(int idDiscountFk, int idProductFk) {
        this.productDiscountPK = new ProductDiscountPK(idDiscountFk, idProductFk);
    }

    public ProductDiscount(Product product, Discount discount) {
        this.product = product;
        this.discount = discount;
    }

    public ProductDiscountPK getProductDiscountPK() {
        return productDiscountPK;
    }

    public void setProductDiscountPK(ProductDiscountPK productDiscountPK) {
        this.productDiscountPK = productDiscountPK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
    
    public String getDiscountInformation(){
        return discount.getDiscountDescrption()+" | "+discount.getPercentaje();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productDiscountPK != null ? productDiscountPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductDiscount)) {
            return false;
        }
        ProductDiscount other = (ProductDiscount) object;
        if ((this.productDiscountPK == null && other.productDiscountPK != null) || (this.productDiscountPK != null && !this.productDiscountPK.equals(other.productDiscountPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.ProductDiscount[ productDiscountPK=" + productDiscountPK + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "DISCOUNT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Discount.findAll", query = "SELECT d FROM Discount d"),
    @NamedQuery(name = "Discount.findById", query = "SELECT d FROM Discount d WHERE d.idDiscountPk = :id"),
    @NamedQuery(name = "Discount.findByDiscountPct", query = "SELECT d FROM Discount d WHERE d.discountPct = :discountPct"),
    @NamedQuery(name = "Discount.findByDiscountDescrption", query = "SELECT d FROM Discount d WHERE d.discountDescrption = :discountDescrption"),
    @NamedQuery(name = "Discount.findByLike", query = "SELECT d FROM Discount d WHERE LOWER(d.discountDescrption) LIKE :id OR LOWER(d.discountDescrption) LIKE :txt2 OR LOWER(d.discountDescrption) LIKE :txt3")})
public class Discount implements Serializable {
    @OneToMany(mappedBy = "discount")
    private Collection<ProductDiscount> productDiscountCollection;
    @OneToMany(mappedBy = "idDiscountFk")
    private Collection<Invoice> invoiceCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_DISCOUNT_PK")
    private Integer idDiscountPk;
    @Basic(optional = false)
    @Column(name = "DISCOUNT_PCT")
    private float discountPct;
    @Column(name = "DISCOUNT_DESCRPTION")
    private String discountDescrption;
    //Variable temporal
    @Transient
    private Double discountSum;

    public Discount() {
    }

    public Discount(Integer idDiscountPk) {
        this.idDiscountPk = idDiscountPk;
    }

    public Discount(Integer idDiscountPk, float discountPct) {
        this.idDiscountPk = idDiscountPk;
        this.discountPct = discountPct;
    }
    
    public Discount(Integer idDiscountPk, float discountPct, String discountDescription) {
        this.idDiscountPk = idDiscountPk;
        this.discountPct = discountPct;
        this.discountDescrption = discountDescription;
    }

    public Integer getIdDiscountPk() {
        return idDiscountPk;
    }

    public void setIdDiscountPk(Integer idDiscountPk) {
        this.idDiscountPk = idDiscountPk;
    }

    public float getDiscountPct() {
        return discountPct;
    }

    public void setDiscountPct(float discountPct) {
        this.discountPct = discountPct;
    }

    public String getDiscountDescrption() {
        return discountDescrption;
    }

    public void setDiscountDescrption(String discountDescrption) {
        this.discountDescrption = discountDescrption;
    }

    public Double getDiscountSum() {
        return discountSum;
    }

    public void setDiscountSum(Double discountSum) {
        this.discountSum = discountSum;
    }
    
    public String getPercentaje(){
        return discountPct+"%";
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDiscountPk != null ? idDiscountPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Discount)) {
            return false;
        }
        Discount other = (Discount) object;
        if ((this.idDiscountPk == null && other.idDiscountPk != null) || (this.idDiscountPk != null && !this.idDiscountPk.equals(other.idDiscountPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return discountDescrption+" | "+getPercentaje();
    }

    @XmlTransient
    public Collection<Invoice> getInvoiceCollection() {
        return invoiceCollection;
    }

    public void setInvoiceCollection(Collection<Invoice> invoiceCollection) {
        this.invoiceCollection = invoiceCollection;
    }

    @XmlTransient
    public Collection<ProductDiscount> getProductDiscountCollection() {
        return productDiscountCollection;
    }

    public void setProductDiscountCollection(Collection<ProductDiscount> productDiscountCollection) {
        this.productDiscountCollection = productDiscountCollection;
    }
}

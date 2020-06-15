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
@Table(name = "TAX")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tax.findAll", query = "SELECT t FROM Tax t"),
    @NamedQuery(name = "Tax.findById", query = "SELECT t FROM Tax t WHERE t.idTaxPk = :id"),
    @NamedQuery(name = "Tax.findByName", query = "SELECT t FROM Tax t WHERE LOWER(t.taxName) = :name"),
    @NamedQuery(name = "Tax.findByLike", query = "SELECT t FROM Tax t WHERE LOWER(t.taxName) LIKE :id OR "
            + "LOWER(t.taxName) LIKE :txt2 OR LOWER(t.taxName) LIKE :txt3")})
public class Tax implements Serializable {
    @OneToMany(mappedBy = "tax")
    private Collection<ProductTaxes> productTaxesCollection;
    @OneToMany(mappedBy = "idTaxFk")
    private Collection<Invoice> invoiceCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_TAX_PK")
    private Integer idTaxPk;
    @Basic(optional = false)
    @Column(name = "TAX_NAME")
    private String taxName;
    @Basic(optional = false)
    @Column(name = "TAX_PCT")
    private float taxPct;
    //Variable temporal
    @Transient
    private Double taxSum;

    public Tax() {
    }

    public Tax(Integer idTaxPk) {
        this.idTaxPk = idTaxPk;
    }

    public Tax(Integer idTaxPk, String taxName, float taxPct) {
        this.idTaxPk = idTaxPk;
        this.taxName = taxName;
        this.taxPct = taxPct;
    }

    public Integer getIdTaxPk() {
        return idTaxPk;
    }

    public void setIdTaxPk(Integer idTaxPk) {
        this.idTaxPk = idTaxPk;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public float getTaxPct() {
        return taxPct;
    }

    public void setTaxPct(float taxPct) {
        this.taxPct = taxPct;
    }

    public Double getTaxSum() {
        return taxSum;
    }

    public void setTaxSum(Double taxSum) {
        this.taxSum = taxSum;
    }
    
    public String getPercentage(){
        return taxPct+" %";
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTaxPk != null ? idTaxPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tax)) {
            return false;
        }
        Tax other = (Tax) object;
        if ((this.idTaxPk == null && other.idTaxPk != null) || (this.idTaxPk != null && !this.idTaxPk.equals(other.idTaxPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return taxName+" ("+taxPct+"%)";
    }

    @XmlTransient
    public Collection<Invoice> getInvoiceCollection() {
        return invoiceCollection;
    }

    public void setInvoiceCollection(Collection<Invoice> invoiceCollection) {
        this.invoiceCollection = invoiceCollection;
    }

    @XmlTransient
    public Collection<ProductTaxes> getProductTaxesCollection() {
        return productTaxesCollection;
    }

    public void setProductTaxesCollection(Collection<ProductTaxes> productTaxesCollection) {
        this.productTaxesCollection = productTaxesCollection;
    }
    
}

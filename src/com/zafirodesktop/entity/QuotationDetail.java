/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import javax.persistence.Basic;
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
@Table(name = "QUOTATION_DETAIL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuotationDetail.findAll", query = "SELECT q FROM QuotationDetail q"),
    @NamedQuery(name = "QuotationDetail.findById", query = "SELECT q FROM QuotationDetail q WHERE q.quotationDetailPK = :id"),
    @NamedQuery(name = "QuotationDetail.findByIdTransactionFk", query = "SELECT q FROM QuotationDetail q WHERE q.quotation.idQuotationPk = :id"),
    @NamedQuery(name = "QuotationDetail.findByIdQuotationFk", query = "SELECT q FROM QuotationDetail q WHERE q.quotationDetailPK.idQuotationFk = :idQuotationFk"),
    @NamedQuery(name = "QuotationDetail.findByIdProductFk", query = "SELECT q FROM QuotationDetail q WHERE q.quotationDetailPK.idProductFk = :idProductFk"),
    @NamedQuery(name = "QuotationDetail.findByAmount", query = "SELECT q FROM QuotationDetail q WHERE q.amount = :amount"),
    @NamedQuery(name = "QuotationDetail.findByUnitPrice", query = "SELECT q FROM QuotationDetail q WHERE q.unitPrice = :unitPrice")})
public class QuotationDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected QuotationDetailPK quotationDetailPK;
    @Basic(optional = false)
    @Column(name = "AMOUNT")
    private int amount;
    @Basic(optional = false)
    @Column(name = "UNIT_PRICE")
    private Double unitPrice;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "TAXES")
    private String taxes;
    @Column(name = "DISCOUNTS")
    private String discounts;
    @JoinColumn(name = "ID_QUOTATION_FK", referencedColumnName = "ID_QUOTATION_PK", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Quotation quotation;
    @JoinColumn(name = "ID_PRODUCT_FK", referencedColumnName = "ID_PRODUCT_PK", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Product product;

    public QuotationDetail() {
    }

    public QuotationDetail(QuotationDetailPK quotationDetailPK) {
        this.quotationDetailPK = quotationDetailPK;
    }

    public QuotationDetail(QuotationDetailPK quotationDetailPK, int amount, Double unitPrice) {
        this.quotationDetailPK = quotationDetailPK;
        this.amount = amount;
        this.unitPrice = unitPrice;
    }

    public QuotationDetail(int idQuotationFk, Integer idProductFk) {
        this.quotationDetailPK = new QuotationDetailPK(idQuotationFk, idProductFk);
    }

    public QuotationDetailPK getQuotationDetailPK() {
        return quotationDetailPK;
    }

    public void setQuotationDetailPK(QuotationDetailPK quotationDetailPK) {
        this.quotationDetailPK = quotationDetailPK;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Quotation getQuotation() {
        return quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaxes() {
        return taxes;
    }

    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (quotationDetailPK != null ? quotationDetailPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuotationDetail)) {
            return false;
        }
        QuotationDetail other = (QuotationDetail) object;
        if ((this.quotationDetailPK == null && other.quotationDetailPK != null) || (this.quotationDetailPK != null && !this.quotationDetailPK.equals(other.quotationDetailPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.QuotationDetail[ quotationDetailPK=" + quotationDetailPK + " ]";
    }
    
}

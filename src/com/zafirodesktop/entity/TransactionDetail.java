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
@Table(name = "TRANSACTION_DETAIL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TransactionDetail.findAll", query = "SELECT t FROM TransactionDetail t"),
    @NamedQuery(name = "TransactionDetail.findByIdProductFk", query = "SELECT t FROM TransactionDetail t WHERE t.transactionDetailPK.idProductFk = :idProductFk"),
    @NamedQuery(name = "TransactionDetail.findStockBuy", query = "SELECT t FROM TransactionDetail t WHERE t.tranzaction.idRemissionFk.idConceptFk.conceptType = 'S' AND t.transactionDetailPK.idProductFk = :idProduct order by t.tranzaction.idTransactionPk desc"),
    @NamedQuery(name = "TransactionDetail.findByIdTransactionFk", query = "SELECT t FROM TransactionDetail t WHERE t.transactionDetailPK.idTransactionFk = :id"),
    @NamedQuery(name = "TransactionDetail.findByProductConcept", query = "SELECT t FROM TransactionDetail t WHERE t.tranzaction.idRemissionFk.idConceptFk.idConceptPk = 1 "
            + "AND t.transactionDetailPK.idProductFk = :idProduct  order by t.transactionDetailPK.idTransactionFk desc"),
    @NamedQuery(name = "TransactionDetail.findByProductTransaction", query = "SELECT t FROM TransactionDetail t WHERE t.transactionDetailPK.idTransactionFk = :id AND t.product.idProductPk = :id2"),
    @NamedQuery(name = "TransactionDetail.findByAmount", query = "SELECT t FROM TransactionDetail t WHERE t.amount = :amount"),
    @NamedQuery(name = "TransactionDetail.findByUnitPrice", query = "SELECT t FROM TransactionDetail t WHERE t.unitPrice = :unitPrice")})
public class TransactionDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TransactionDetailPK transactionDetailPK;
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
    @JoinColumn(name = "ID_TRANSACTION_FK", referencedColumnName = "ID_TRANSACTION_PK", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Tranzaction tranzaction;
    @JoinColumn(name = "ID_PRODUCT_FK", referencedColumnName = "ID_PRODUCT_PK", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Product product;

    public TransactionDetail() {
    }

    public TransactionDetail(TransactionDetailPK transactionDetailPK) {
        this.transactionDetailPK = transactionDetailPK;
    }

    public TransactionDetail(TransactionDetailPK transactionDetailPK, int amount, Double unitPrice) {
        this.transactionDetailPK = transactionDetailPK;
        this.amount = amount;
        this.unitPrice = unitPrice;
    }

    public TransactionDetail(Integer idProductFk, int idTransactionFk) {
        this.transactionDetailPK = new TransactionDetailPK(idProductFk, idTransactionFk);
    }

    public TransactionDetailPK getTransactionDetailPK() {
        return transactionDetailPK;
    }

    public void setTransactionDetailPK(TransactionDetailPK transactionDetailPK) {
        this.transactionDetailPK = transactionDetailPK;
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

    public Tranzaction getTranzaction() {
        return tranzaction;
    }

    public void setTranzaction(Tranzaction tranzaction) {
        this.tranzaction = tranzaction;
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
        hash += (transactionDetailPK != null ? transactionDetailPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransactionDetail)) {
            return false;
        }
        TransactionDetail other = (TransactionDetail) object;
        if ((this.transactionDetailPK == null && other.transactionDetailPK != null) || (this.transactionDetailPK != null && !this.transactionDetailPK.equals(other.transactionDetailPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.TransactionDetail[ transactionDetailPK=" + transactionDetailPK + " ]";
    }
    
}

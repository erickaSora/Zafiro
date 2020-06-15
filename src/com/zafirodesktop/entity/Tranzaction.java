/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "TRANZACTION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tranzaction.findAll", query = "SELECT t FROM Tranzaction t"),
    @NamedQuery(name = "Tranzaction.findById", query = "SELECT t FROM Tranzaction t WHERE t.idTransactionPk = :id"),
    @NamedQuery(name = "Tranzaction.findByRemission", query = "SELECT t FROM Tranzaction t WHERE t.idRemissionFk.idRemissionPk = :id"),
    @NamedQuery(name = "Tranzaction.findAllDate", query = "SELECT t FROM Tranzaction t WHERE t.idRemissionFk.status > 0 AND t.idRemissionFk.invoiced = 1 AND t.transactionDate >= :startDate "
            + "and t.transactionDate <= :endDate order by t.idRemissionFk.idInvoiceFk.idInvoicePk desc"),
    @NamedQuery(name = "Tranzaction.findAllOrdersByDate", query = "SELECT t FROM Tranzaction t WHERE t.idRemissionFk.status > 0 AND t.idRemissionFk.idConceptFk.conceptType = 'S' AND t.transactionDate >= :startDate "
            + "and t.transactionDate <= :endDate order by t.idRemissionFk.idRemissionPk desc"),
    @NamedQuery(name = "Tranzaction.findByTransactionDate", query = "SELECT t FROM Tranzaction t WHERE t.transactionDate = :transactionDate"),
    @NamedQuery(name = "Tranzaction.findByTransactionPrice", query = "SELECT t FROM Tranzaction t WHERE t.transactionPrice = :transactionPrice")})
public class Tranzaction implements Serializable {

    @OneToMany(mappedBy = "tranzaction")
    private Collection<TransactionDetail> transactionDetailCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_TRANSACTION_PK")
    private Integer idTransactionPk;
    @Basic(optional = false)
    @Column(name = "TRANSACTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate = getDefaultDate();
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TRANSACTION_PRICE")
    private Double transactionPrice;
    @JoinColumn(name = "ID_WAREHOUSE_FK", referencedColumnName = "ID_WAREHOUSE_PK")
    @ManyToOne
    private Warehouse idWarehouseFk;
    @JoinColumn(name = "ID_REMISSION_FK", referencedColumnName = "ID_REMISSION_PK")
    @ManyToOne
    private Remission idRemissionFk;
    @Column(name = "CASH_BOX_CHECKED")
    private Short cashBoxChecked;

    public Tranzaction() {
    }

    public Tranzaction(Integer idTransactionPk) {
        this.idTransactionPk = idTransactionPk;
    }

    public Tranzaction(Integer idTransactionPk, Date transactionDate) {
        this.idTransactionPk = idTransactionPk;
        this.transactionDate = transactionDate;
    }

    public Integer getIdTransactionPk() {
        return idTransactionPk;
    }

    public void setIdTransactionPk(Integer idTransactionPk) {
        this.idTransactionPk = idTransactionPk;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getTransactionPrice() {
        return transactionPrice;
    }

    public void setTransactionPrice(Double transactionPrice) {
        this.transactionPrice = transactionPrice;
    }

    public Warehouse getIdWarehouseFk() {
        return idWarehouseFk;
    }

    public void setIdWarehouseFk(Warehouse idWarehouseFk) {
        this.idWarehouseFk = idWarehouseFk;
    }

    public Remission getIdRemissionFk() {
        return idRemissionFk;
    }

    public void setIdRemissionFk(Remission idRemissionFk) {
        this.idRemissionFk = idRemissionFk;
    }

    public Short getCashBoxChecked() {
        return cashBoxChecked;
    }

    public void setCashBoxChecked(Short cashBoxChecked) {
        this.cashBoxChecked = cashBoxChecked;
    }
    
    public static Date getDefaultDate() {
        return getDefaulTimezone(TimeZone.getDefault());
    }

    public static Date getDefaulTimezone(TimeZone timeZone) {
        return Calendar.getInstance(timeZone).getTime();
    }

    public String getTotalPrice() {
        DecimalFormat format = new DecimalFormat("###,###.##");
        return format.format(transactionPrice);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTransactionPk != null ? idTransactionPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tranzaction)) {
            return false;
        }
        Tranzaction other = (Tranzaction) object;
        if ((this.idTransactionPk == null && other.idTransactionPk != null) || (this.idTransactionPk != null && !this.idTransactionPk.equals(other.idTransactionPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.Tranzaction[ idTransactionPk=" + idTransactionPk + " ]";
    }

    @XmlTransient
    public Collection<TransactionDetail> getTransactionDetailCollection() {
        return transactionDetailCollection;
    }

    public void setTransactionDetailCollection(Collection<TransactionDetail> transactionDetailCollection) {
        this.transactionDetailCollection = transactionDetailCollection;
    }

    public void addTrazactionDetail(TransactionDetail td) {
        if (!transactionDetailCollection.contains(td)) {
            transactionDetailCollection.add(td);
            if (td.getTranzaction() != this) {
                td.setTranzaction(this);
            }
        }
    }

    public void removeTranzactionDetail(TransactionDetail td) {
        if (transactionDetailCollection.contains(td)) {
            transactionDetailCollection.remove(td);
        }
    }
}

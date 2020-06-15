/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "INVOICE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invoice.findAllInvoice", query = "SELECT i FROM Invoice i"),
    @NamedQuery(name = "Invoice.findAllDate", query = "SELECT t FROM Invoice t WHERE t.status > 0 AND t.invoiceDate >= :startDate "
            + "AND t.invoiceDate <= :endDate"),
    @NamedQuery(name = "Invoice.findById", query = "SELECT i FROM Invoice i WHERE i.idInvoicePk = :id"),
    @NamedQuery(name = "Invoice.findByInvoiceDate", query = "SELECT i FROM Invoice i WHERE i.invoiceDate = :invoiceDate"),
    @NamedQuery(name = "Invoice.findByNoteHeader", query = "SELECT i FROM Invoice i WHERE i.noteHeader = :noteHeader"),
    @NamedQuery(name = "Invoice.findByNoteFooter", query = "SELECT i FROM Invoice i WHERE i.noteFooter = :noteFooter"),
    @NamedQuery(name = "Invoice.findByStatus", query = "SELECT i FROM Invoice i WHERE i.status = :status")})
public class Invoice implements Serializable {
    @OneToMany(mappedBy = "idInvoiceFk")
    private Collection<Remission> remissionCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_INVOICE_PK")
    private String idInvoicePk;
    @Basic(optional = false)
    @Column(name = "INVOICE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invoiceDate = getDefaultDate();
    @Column(name = "NOTE_HEADER")
    private String noteHeader;
    @Column(name = "NOTE_FOOTER")
    private String noteFooter;
    @Column(name = "OBS")
    private String obs;
    @Column(name = "NO_REFERENCE")
    private String noReference;
    @Column(name = "CARD_TYPE")
    private String cardType;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private short status;
    @JoinColumn(name = "ID_TAX_FK", referencedColumnName = "ID_TAX_PK")
    @ManyToOne
    private Tax idTaxFk;
    @JoinColumn(name = "ID_PAYMENT_FK", referencedColumnName = "ID_PAYMENT_PK")
    @ManyToOne(optional = false)
    private Payment idPaymentFk;
    @JoinColumn(name = "ID_DISCOUNT_FK", referencedColumnName = "ID_DISCOUNT_PK")
    @ManyToOne
    private Discount idDiscountFk;

    public Invoice() {
    }

    public Invoice(String idInvoicePk) {
        this.idInvoicePk = idInvoicePk;
    }

    public Invoice(String idInvoicePk, Date invoiceDate, short status) {
        this.idInvoicePk = idInvoicePk;
        this.invoiceDate = invoiceDate;
        this.status = status;
    }

    public String getIdInvoicePk() {
        return idInvoicePk;
    }

    public void setIdInvoicePk(String idInvoicePk) {
        this.idInvoicePk = idInvoicePk;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getNoteHeader() {
        return noteHeader;
    }

    public void setNoteHeader(String noteHeader) {
        this.noteHeader = noteHeader;
    }

    public String getNoteFooter() {
        return noteFooter;
    }

    public void setNoteFooter(String noteFooter) {
        this.noteFooter = noteFooter;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }
    
    public Tax getIdTaxFk() {
        return idTaxFk;
    }

    public void setIdTaxFk(Tax idTaxFk) {
        this.idTaxFk = idTaxFk;
    }

    public Payment getIdPaymentFk() {
        return idPaymentFk;
    }

    public void setIdPaymentFk(Payment idPaymentFk) {
        this.idPaymentFk = idPaymentFk;
    }

    public Discount getIdDiscountFk() {
        return idDiscountFk;
    }

    public void setIdDiscountFk(Discount idDiscountFk) {
        this.idDiscountFk = idDiscountFk;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getNoReference() {
        return noReference;
    }

    public void setNoReference(String noReference) {
        this.noReference = noReference;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public static Date getDefaultDate() {
        return getDefaulTimezone(TimeZone.getDefault());
    }

    public static Date getDefaulTimezone(TimeZone timeZone) {
        return Calendar.getInstance(timeZone).getTime();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInvoicePk != null ? idInvoicePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invoice)) {
            return false;
        }
        Invoice other = (Invoice) object;
        if ((this.idInvoicePk == null && other.idInvoicePk != null) || (this.idInvoicePk != null && !this.idInvoicePk.equals(other.idInvoicePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.Invoice[ idInvoicePk=" + idInvoicePk + " ]";
    }

    @XmlTransient
    public Collection<Remission> getRemissionCollection() {
        return remissionCollection;
    }

    public void setRemissionCollection(Collection<Remission> remissionCollection) {
        this.remissionCollection = remissionCollection;
    }
}

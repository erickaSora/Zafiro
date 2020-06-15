/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
@Table(name = "QUOTATION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Quotation.findAll", query = "SELECT q FROM Quotation q ORDER BY q.idQuotationPk DESC"),
    @NamedQuery(name = "Quotation.findByLikeInt", query = "SELECT q FROM Quotation q WHERE q.idQuotationPk = :id OR LOWER(CONCAT(q.idClientFk.personFirstname,' ',q.idClientFk.personLastname)) "
            + "LIKE :txt2 OR LOWER(CONCAT(q.idClientFk.personFirstname,' ',q.idClientFk.personLastname)) LIKE :txt3"),
    @NamedQuery(name = "Quotation.findById", query = "SELECT q FROM Quotation q WHERE q.idQuotationPk = :id"),
    @NamedQuery(name = "Quotation.findByIdSell", query = "SELECT q FROM Quotation q WHERE q.idSell = :idSell"),
    @NamedQuery(name = "Quotation.findByQuotationDate", query = "SELECT q FROM Quotation q WHERE q.quotationDate = :quotationDate"),
    @NamedQuery(name = "Quotation.findByStatus", query = "SELECT q FROM Quotation q WHERE q.status = :status")})
public class Quotation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_QUOTATION_PK")
    private Integer idQuotationPk;
    @Column(name = "ID_SELL")
    private String idSell;
    @Column(name = "ID_INVOICE_FK")
    private String idInvoiceFk;
    @Basic(optional = false)
    @Column(name = "QUOTATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date quotationDate = getDefaultDate();
    @Basic(optional = false)
    @Column(name = "STATUS")
    private short status;
    @Column(name = "OBS")
    private String obs;
    @OneToMany(mappedBy = "quotation")
    private Collection<QuotationDetail> quotationDetailCollection;
    @JoinColumn(name = "ID_CLIENT_FK", referencedColumnName = "ID_PERSON_PK")
    @ManyToOne
    private Person idClientFk;
    @JoinColumn(name = "ID_SUPPLIER_FK", referencedColumnName = "ID_PERSON_PK")
    @ManyToOne
    private Person idSupplierFk;

    public Quotation() {
    }

    public Quotation(Integer idQuotationPk) {
        this.idQuotationPk = idQuotationPk;
    }

    public Quotation(Integer idQuotationPk, Date quotationDate, short status) {
        this.idQuotationPk = idQuotationPk;
        this.quotationDate = quotationDate;
        this.status = status;
    }

    public Integer getIdQuotationPk() {
        return idQuotationPk;
    }

    public void setIdQuotationPk(Integer idQuotationPk) {
        this.idQuotationPk = idQuotationPk;
    }

    public String getIdSell() {
        return idSell;
    }

    public void setIdSell(String idSell) {
        this.idSell = idSell;
    }

    public Date getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(Date quotationDate) {
        this.quotationDate = quotationDate;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getIdInvoiceFk() {
        return idInvoiceFk;
    }

    public void setIdInvoiceFk(String idInvoiceFk) {
        this.idInvoiceFk = idInvoiceFk;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
    
    public static Date getDefaultDate() {
        return getDefaulTimezone(TimeZone.getDefault());
    }

    public static Date getDefaulTimezone(TimeZone timeZone) {
        return Calendar.getInstance(timeZone).getTime();
    }
    
    public String getDate() {
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        return dt.format(quotationDate);
    }

    public String getTotalName() {
        return idClientFk.getTotalName();
    }

    public String getTranzactionPrice() {
        DecimalFormat format = new DecimalFormat("###,###.##");
        float price = 0;
        for (QuotationDetail qdetail : quotationDetailCollection) {
            price += qdetail.getUnitPrice()*qdetail.getAmount();
        }
        return format.format(price);
    }
    
    public String getQuotationStatus(){
        if(status==0)
            return "Pendiente";
        else 
            return "Facturada";
    }

    @XmlTransient
    public Collection<QuotationDetail> getQuotationDetailCollection() {
        return quotationDetailCollection;
    }

    public void setQuotationDetailCollection(Collection<QuotationDetail> quotationDetailCollection) {
        this.quotationDetailCollection = quotationDetailCollection;
    }

    public Person getIdClientFk() {
        return idClientFk;
    }

    public void setIdClientFk(Person idClientFk) {
        this.idClientFk = idClientFk;
    }

    public Person getIdSupplierFk() {
        return idSupplierFk;
    }

    public void setIdSupplierFk(Person idSupplierFk) {
        this.idSupplierFk = idSupplierFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idQuotationPk != null ? idQuotationPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Quotation)) {
            return false;
        }
        Quotation other = (Quotation) object;
        if ((this.idQuotationPk == null && other.idQuotationPk != null) || (this.idQuotationPk != null && !this.idQuotationPk.equals(other.idQuotationPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.Quotation[ idQuotationPk=" + idQuotationPk + " ]";
    }
    
    public void addQuotationDetail(QuotationDetail qt) {
        if (!quotationDetailCollection.contains(qt)) {
            quotationDetailCollection.add(qt);
            if (qt.getQuotation() != this) {
                qt.setQuotation(this);
            }
        }
    }

    public void removeQuotationDetail(QuotationDetail qt) {
        if (quotationDetailCollection.contains(qt)) {
            quotationDetailCollection.remove(qt);
        }
    }

}

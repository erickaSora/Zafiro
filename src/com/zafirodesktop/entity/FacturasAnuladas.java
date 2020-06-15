/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "FACTURAS_ANULADAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FacturasAnuladas.findAll", query = "SELECT f FROM FacturasAnuladas f"),
    @NamedQuery(name = "FacturasAnuladas.findAllDate", query = "SELECT t FROM FacturasAnuladas t WHERE t.invoiceDate >= :startDate "
            + "and t.invoiceDate <= :endDate"),
    @NamedQuery(name = "FacturasAnuladas.findByIdInvoicePk", query = "SELECT f FROM FacturasAnuladas f WHERE f.idInvoicePk = :idInvoicePk"),
    @NamedQuery(name = "FacturasAnuladas.findByInvoiceDate", query = "SELECT f FROM FacturasAnuladas f WHERE f.invoiceDate = :invoiceDate"),
    @NamedQuery(name = "FacturasAnuladas.findByObs", query = "SELECT f FROM FacturasAnuladas f WHERE f.obs = :obs"),
    @NamedQuery(name = "FacturasAnuladas.findByClient", query = "SELECT f FROM FacturasAnuladas f WHERE f.client = :client"),
    @NamedQuery(name = "FacturasAnuladas.findByTransactionPrice", query = "SELECT f FROM FacturasAnuladas f WHERE f.transactionPrice = :transactionPrice")})
public class FacturasAnuladas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "ID_INVOICE_PK")
    @Id
    private String idInvoicePk;
    @Basic(optional = false)
    @Column(name = "INVOICE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invoiceDate;
    @Column(name = "OBS")
    private String obs;
    @Column(name = "CLIENT")
    private String client;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TRANSACTION_PRICE")
    private Float transactionPrice;

    public FacturasAnuladas() {
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

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Float getTransactionPrice() {
        return transactionPrice;
    }

    public void setTransactionPrice(Float transactionPrice) {
        this.transactionPrice = transactionPrice;
    }
    
}

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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "PAYMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Payment.findAll", query = "SELECT p FROM Payment p"),
    @NamedQuery(name = "Payment.findById", query = "SELECT p FROM Payment p WHERE p.idPaymentPk = :id"),
    @NamedQuery(name = "Payment.findByPaymentName", query = "SELECT p FROM Payment p WHERE p.paymentName = :paymentName"),
    @NamedQuery(name = "Payment.findByPaymentPct", query = "SELECT p FROM Payment p WHERE p.paymentPct = :paymentPct")})
public class Payment implements Serializable {
    @OneToMany(mappedBy = "idPaymentFk")
    private Collection<Invoice> invoiceCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_PAYMENT_PK")
    private Integer idPaymentPk;
    @Basic(optional = false)
    @Column(name = "PAYMENT_NAME")
    private String paymentName;
    @Basic(optional = false)
    @Column(name = "PAYMENT_PCT")
    private int paymentPct;

    public Payment() {
    }

    public Payment(Integer idPaymentPk) {
        this.idPaymentPk = idPaymentPk;
    }

    public Payment(Integer idPaymentPk, String paymentName, int paymentPct) {
        this.idPaymentPk = idPaymentPk;
        this.paymentName = paymentName;
        this.paymentPct = paymentPct;
    }

    public Integer getIdPaymentPk() {
        return idPaymentPk;
    }

    public void setIdPaymentPk(Integer idPaymentPk) {
        this.idPaymentPk = idPaymentPk;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public int getPaymentPct() {
        return paymentPct;
    }

    public void setPaymentPct(int paymentPct) {
        this.paymentPct = paymentPct;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPaymentPk != null ? idPaymentPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        if ((this.idPaymentPk == null && other.idPaymentPk != null) || (this.idPaymentPk != null && !this.idPaymentPk.equals(other.idPaymentPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.Payment[ idPaymentPk=" + idPaymentPk + " ]";
    }

    @XmlTransient
    public Collection<Invoice> getInvoiceCollection() {
        return invoiceCollection;
    }

    public void setInvoiceCollection(Collection<Invoice> invoiceCollection) {
        this.invoiceCollection = invoiceCollection;
    }
    
}

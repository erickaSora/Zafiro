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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "DEPOSIT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Deposit.findAll", query = "SELECT d FROM Deposit d"),
    @NamedQuery(name = "Deposit.findById", query = "SELECT d FROM Deposit d WHERE d.idDepositPk = :id"),
    @NamedQuery(name = "Deposit.findByIdRemission", query = "SELECT d FROM Deposit d WHERE d.idRemissionFk.idRemissionPk = :id"),
    @NamedQuery(name = "Deposit.findByDeposit", query = "SELECT d FROM Deposit d WHERE d.deposit = :deposit")})
public class Deposit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_DEPOSIT_PK")
    private Integer idDepositPk;
    @Basic(optional = false)
    @Column(name = "DEPOSIT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date depositDate = getDefaultDate();
    @Basic(optional = false)
    @Column(name = "DEPOSIT")
    private Double deposit;
    @Column(name = "ID_SELL")
    private String idSell;
    @Column(name = "OBS")
    private String obs;
    @JoinColumn(name = "ID_REMISSION_FK", referencedColumnName = "ID_REMISSION_PK")
    @ManyToOne(optional = false)
    private Remission idRemissionFk;

    public Deposit() {
    }

    public Deposit(Integer idDepositPk) {
        this.idDepositPk = idDepositPk;
    }

    public Deposit(Integer idDepositPk, Date depositDate, Double deposit) {
        this.idDepositPk = idDepositPk;
        this.depositDate = depositDate;
        this.deposit = deposit;
    }

    public Integer getIdDepositPk() {
        return idDepositPk;
    }

    public void setIdDepositPk(Integer idDepositPk) {
        this.idDepositPk = idDepositPk;
    }

    public Date getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(Date depositDate) {
        this.depositDate = depositDate;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Remission getIdRemissionFk() {
        return idRemissionFk;
    }

    public void setIdRemissionFk(Remission idRemissionFk) {
        this.idRemissionFk = idRemissionFk;
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

    public String getIdSell() {
        return idSell;
    }

    public void setIdSell(String idSell) {
        this.idSell = idSell;
    }
    
    public String getTranzactionDeposit() {
        DecimalFormat format = new DecimalFormat("###,###.##");
        return format.format(deposit);
    }
    
    public String getDate() {
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        return dt.format(depositDate);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDepositPk != null ? idDepositPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Deposit)) {
            return false;
        }
        Deposit other = (Deposit) object;
        if ((this.idDepositPk == null && other.idDepositPk != null) || (this.idDepositPk != null && !this.idDepositPk.equals(other.idDepositPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.Deposit[ idDepositPk=" + idDepositPk + " ]";
    }
    
}

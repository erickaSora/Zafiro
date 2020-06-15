/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.entity;

import java.io.Serializable;
import java.math.BigInteger;
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

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "TURN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Turn.findAll", query = "SELECT t FROM Turn t"),
    @NamedQuery(name = "Turn.findByUserId", query = "SELECT t FROM Turn t WHERE t.idTurnPk = (SELECT MAX(t.idTurnPk) from Turn t where t.endDate is null and t.idUserFk.id = :id)"),
    @NamedQuery(name = "Turn.findByNonDate", query = "SELECT t FROM Turn t WHERE t.endDate is null "),
    @NamedQuery(name = "Turn.findAllDate", query = "SELECT t FROM Turn t WHERE t.startDate >= :startDate "
            + "and t.startDate <= :endDate"),
    @NamedQuery(name = "Turn.findById", query = "SELECT t FROM Turn t WHERE t.idTurnPk = :id")})
public class Turn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_TURN_PK")
    private BigInteger idTurnPk;
    @Basic(optional = false)
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate = getDefaultDate();
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @Column(name = "INITIAL_AMOUNT")
    private Double initialAmount;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "EXPECTED_AMOUNT")
    private Double expectedAmount;
    @Column(name = "REAL_AMOUNT")
    private Double realAmount;
    @JoinColumn(name = "ID_USER_FK", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Users idUserFk;
    @OneToMany(mappedBy = "idTurnFk")
    private Collection<Remission> remissionCollection;

    public Turn() {
    }

    public Turn(BigInteger idTurnPk) {
        this.idTurnPk = idTurnPk;
    }

    public Turn(BigInteger idTurnPk, Date startDate, Double initialAmount) {
        this.idTurnPk = idTurnPk;
        this.startDate = startDate;
        this.initialAmount = initialAmount;
    }

    public BigInteger getIdTurnPk() {
        return idTurnPk;
    }

    public void setIdTurnPk(BigInteger idTurnPk) {
        this.idTurnPk = idTurnPk;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(Double initialAmount) {
        this.initialAmount = initialAmount;
    }

    public Double getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(Double expectedAmount) {
        this.expectedAmount = expectedAmount;
    }

    public Double getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(Double realAmount) {
        this.realAmount = realAmount;
    }

    public Users getIdUserFk() {
        return idUserFk;
    }

    public void setIdUserFk(Users idUserFk) {
        this.idUserFk = idUserFk;
    }

    public Collection<Remission> getRemissionCollection() {
        return remissionCollection;
    }

    public void setRemissionCollection(Collection<Remission> remissionCollection) {
        this.remissionCollection = remissionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTurnPk != null ? idTurnPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Turn)) {
            return false;
        }
        Turn other = (Turn) object;
        if ((this.idTurnPk == null && other.idTurnPk != null) || (this.idTurnPk != null && !this.idTurnPk.equals(other.idTurnPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return idUserFk.getFirstName()+" "+idUserFk.getLastName();
    }

    public static Date getDefaultDate() {
        return getDefaulTimezone(TimeZone.getDefault());
    }

    public static Date getDefaulTimezone(TimeZone timeZone) {
        return Calendar.getInstance(timeZone).getTime();
    }

    public void addRemission(Remission rm) {
        if (!remissionCollection.contains(rm)) {
            remissionCollection.add(rm);
            if (rm.getIdTurnFk() != this) {
                rm.setIdTurnFk(this);
            }
        }
    }

    public void removeRemission(Remission rm) {
        if (remissionCollection.contains(rm)) {
            remissionCollection.remove(rm);
        }
    }

}

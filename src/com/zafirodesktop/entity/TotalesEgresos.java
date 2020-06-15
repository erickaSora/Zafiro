/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "TOTALES_EGRESOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TotalesEgresos.findAll", query = "SELECT t FROM TotalesEgresos t ORDER BY t.fecha DESC"),
    @NamedQuery(name = "TotalesEgresos.findAllDate", query = "SELECT t FROM TotalesEgresos t WHERE t.fecha >= :startDate "
            + "and t.fecha <= :endDate ORDER BY t.fecha DESC"),
    @NamedQuery(name = "TotalesEgresos.findById", query = "SELECT t FROM TotalesEgresos t WHERE t.id = :id"),
    @NamedQuery(name = "TotalesEgresos.findByTurn", query = "SELECT t FROM TotalesEgresos t WHERE t.idTurnFk = :turn")})
public class TotalesEgresos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "PK")
    @Id
    private String pk;
    @Basic(optional = false)
    @Column(name = "ID")
    private int id;
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "CONCEPT")
    private String concept;
    @Column(name = "CLIENT")
    private String client;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TOTAL")
    private Float total;
    @Column(name = "ID_TURN_FK")
    private BigInteger idTurnFk;

    public TotalesEgresos() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public BigInteger getIdTurnFk() {
        return idTurnFk;
    }

    public void setIdTurnFk(BigInteger idTurnFk) {
        this.idTurnFk = idTurnFk;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }
    
}

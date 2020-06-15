/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
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
@Table(name = "TOTALES_INGRESOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TotalesIngresos.findAll", query = "SELECT t FROM TotalesIngresos t ORDER BY t.fecha DESC"),
    @NamedQuery(name = "TotalesIngresos.findAllDate", query = "SELECT t FROM TotalesIngresos t WHERE t.fecha >= :startDate "
            + "and t.fecha <= :endDate ORDER BY t.fecha DESC"),
    @NamedQuery(name = "TotalesIngresos.findById", query = "SELECT t FROM TotalesIngresos t WHERE t.id = :id"),
    @NamedQuery(name = "TotalesIngresos.findByTurn", query = "SELECT t FROM TotalesIngresos t WHERE t.idTurnFk = :turn")})
public class TotalesIngresos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "PK")
    @Id
    private String pk;
    @Column(name = "ID")
    private String id;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "CONCEPT")
    private String concept;
    @Column(name = "ID_PAYMENT_FK")
    private Integer idPaymentFk;
    @Column(name = "CARD_REFERENCE")
    private String cardReference;
    @Column(name = "CLIENT")
    private String client;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TOTAL")
    private Float total;
    @Column(name = "ID_TURN_FK")
    private BigInteger idTurnFk;

    public TotalesIngresos() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Integer getIdPaymentFk() {
        return idPaymentFk;
    }

    public void setIdPaymentFk(Integer idPaymentFk) {
        this.idPaymentFk = idPaymentFk;
    }

    public String getCardReference() {
        return cardReference;
    }

    public void setCardReference(String cardReference) {
        this.cardReference = cardReference;
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

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public BigInteger getIdTurnFk() {
        return idTurnFk;
    }

    public void setIdTurnFk(BigInteger idTurnFk) {
        this.idTurnFk = idTurnFk;
    }
    
}

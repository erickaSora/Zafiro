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
@Table(name = "MOVIMIENTOS_PRODUCTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MovimientosProducto.findAll", query = "SELECT m FROM MovimientosProducto m"),
    @NamedQuery(name = "MovimientosProducto.findAllDate", query = "SELECT m FROM MovimientosProducto m WHERE m.idProductPk = :id AND m.remissionDate >= :startDate "
            + "and m.remissionDate <= :endDate"),
    @NamedQuery(name = "MovimientosProducto.findById", query = "SELECT m FROM MovimientosProducto m WHERE m.idProductPk = :id")})
public class MovimientosProducto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "ID")
    @Id
    private String id;
    @Basic(optional = false)
    @Column(name = "ID_PRODUCT_PK")
    private int idProductPk;
    @Column(name = "ID_REMISSION_PK")
    private String idRemissionPk;
    @Basic(optional = false)
    @Column(name = "REMISSION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date remissionDate;
    @Column(name = "OBS")
    private String obs;
    @Column(name = "CONCEPT_NAME")
    private String conceptName;
    @Basic(optional = false)
    @Column(name = "AMOUNT")
    private int amount;
    @Basic(optional = false)
    @Column(name = "ID_CONCEPT_PK")
    private int idConceptPk;

    public MovimientosProducto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdProductPk() {
        return idProductPk;
    }

    public void setIdProductPk(int idProductPk) {
        this.idProductPk = idProductPk;
    }

    public String getIdRemissionPk() {
        return idRemissionPk;
    }

    public void setIdRemissionPk(String idRemissionPk) {
        this.idRemissionPk = idRemissionPk;
    }

    public Date getRemissionDate() {
        return remissionDate;
    }

    public void setRemissionDate(Date remissionDate) {
        this.remissionDate = remissionDate;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getIdConceptPk() {
        return idConceptPk;
    }

    public void setIdConceptPk(int idConceptPk) {
        this.idConceptPk = idConceptPk;
    }
    
}

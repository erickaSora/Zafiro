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
@Table(name = "WAREHOUSE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Warehouse.findAll", query = "SELECT w FROM Warehouse w"),
    @NamedQuery(name = "Warehouse.findById", query = "SELECT w FROM Warehouse w WHERE w.idWarehousePk = :id"),
    @NamedQuery(name = "Warehouse.findByWarehouseName", query = "SELECT w FROM Warehouse w WHERE w.warehouseName = :warehouseName"),
    @NamedQuery(name = "Warehouse.findByWarehouseAddress", query = "SELECT w FROM Warehouse w WHERE w.warehouseAddress = :warehouseAddress"),
    @NamedQuery(name = "Warehouse.findByWarehousePhoneNo", query = "SELECT w FROM Warehouse w WHERE w.warehousePhoneNo = :warehousePhoneNo"),
    @NamedQuery(name = "Warehouse.findByWarehouseDescription", query = "SELECT w FROM Warehouse w WHERE w.warehouseDescription = :warehouseDescription"),
    @NamedQuery(name = "Warehouse.findByIdPlaceFk", query = "SELECT w FROM Warehouse w WHERE w.idPlaceFk = :idPlaceFk")})
public class Warehouse implements Serializable {
    @OneToMany(mappedBy = "idWarehouseFk")
    private Collection<Tranzaction> tranzactionCollection;
    @OneToMany(mappedBy = "warehouse")
    private Collection<Stock> stockCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_WAREHOUSE_PK")
    private Integer idWarehousePk;
    @Basic(optional = false)
    @Column(name = "WAREHOUSE_NAME")
    private String warehouseName;
    @Column(name = "WAREHOUSE_ADDRESS")
    private String warehouseAddress;
    @Column(name = "WAREHOUSE_PHONE_NO")
    private String warehousePhoneNo;
    @Column(name = "WAREHOUSE_DESCRIPTION")
    private String warehouseDescription;
    @Column(name = "ID_PLACE_FK")
    private Integer idPlaceFk;

    public Warehouse() {
    }

    public Warehouse(Integer idWarehousePk) {
        this.idWarehousePk = idWarehousePk;
    }

    public Warehouse(Integer idWarehousePk, String warehouseName) {
        this.idWarehousePk = idWarehousePk;
        this.warehouseName = warehouseName;
    }

    public Integer getIdWarehousePk() {
        return idWarehousePk;
    }

    public void setIdWarehousePk(Integer idWarehousePk) {
        this.idWarehousePk = idWarehousePk;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    public String getWarehousePhoneNo() {
        return warehousePhoneNo;
    }

    public void setWarehousePhoneNo(String warehousePhoneNo) {
        this.warehousePhoneNo = warehousePhoneNo;
    }

    public String getWarehouseDescription() {
        return warehouseDescription;
    }

    public void setWarehouseDescription(String warehouseDescription) {
        this.warehouseDescription = warehouseDescription;
    }

    public Integer getIdPlaceFk() {
        return idPlaceFk;
    }

    public void setIdPlaceFk(Integer idPlaceFk) {
        this.idPlaceFk = idPlaceFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idWarehousePk != null ? idWarehousePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Warehouse)) {
            return false;
        }
        Warehouse other = (Warehouse) object;
        if ((this.idWarehousePk == null && other.idWarehousePk != null) || (this.idWarehousePk != null && !this.idWarehousePk.equals(other.idWarehousePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.Warehouse[ idWarehousePk=" + idWarehousePk + " ]";
    }

    @XmlTransient
    public Collection<Stock> getStockCollection() {
        return stockCollection;
    }

    public void setStockCollection(Collection<Stock> stockCollection) {
        this.stockCollection = stockCollection;
    }

    @XmlTransient
    public Collection<Tranzaction> getTranzactionCollection() {
        return tranzactionCollection;
    }

    public void setTranzactionCollection(Collection<Tranzaction> tranzactionCollection) {
        this.tranzactionCollection = tranzactionCollection;
    }
    
}

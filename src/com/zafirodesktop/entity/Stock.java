/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "STOCK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Stock.findAll", query = "SELECT s FROM Stock s"),
    @NamedQuery(name = "Stock.findById", query = "SELECT s FROM Stock s WHERE s.stockPK.idProductFk = :id"),
    @NamedQuery(name = "Stock.findTotalAmount", query = "SELECT SUM(s.amount) FROM Stock s WHERE s.product.idProductPk = :id"),
    @NamedQuery(name = "Stock.findByIdWarehouseFk", query = "SELECT s FROM Stock s WHERE s.stockPK.idWarehouseFk = :idWarehouseFk"),
    @NamedQuery(name = "Stock.findByAmount", query = "SELECT s FROM Stock s WHERE s.amount = :amount")})
public class Stock implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected StockPK stockPK;
    @Basic(optional = false)
    @Column(name = "AMOUNT")
    private int amount;
    @JoinColumn(name = "ID_WAREHOUSE_FK", referencedColumnName = "ID_WAREHOUSE_PK", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Warehouse warehouse;
    @JoinColumn(name = "ID_PRODUCT_FK", referencedColumnName = "ID_PRODUCT_PK", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Product product;

    public Stock() {
    }

    public Stock(StockPK stockPK) {
        this.stockPK = stockPK;
    }

    public Stock(StockPK stockPK, int amount) {
        this.stockPK = stockPK;
        this.amount = amount;
    }

    public Stock(Integer idProductFk, int idWarehouseFk) {
        this.stockPK = new StockPK(idProductFk, idWarehouseFk);
    }

    public StockPK getStockPK() {
        return stockPK;
    }

    public void setStockPK(StockPK stockPK) {
        this.stockPK = stockPK;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stockPK != null ? stockPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stock)) {
            return false;
        }
        Stock other = (Stock) object;
        if ((this.stockPK == null && other.stockPK != null) || (this.stockPK != null && !this.stockPK.equals(other.stockPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.Stock[ stockPK=" + stockPK + " ]";
    }
    
}

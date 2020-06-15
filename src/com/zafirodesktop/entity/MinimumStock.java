/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "MINIMUM_STOCK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MinimumStock.findAll", query = "SELECT m FROM MinimumStock m"),
    @NamedQuery(name = "MinimumStock.findBySkuProduct", query = "SELECT m FROM MinimumStock m WHERE m.skuProduct = :skuProduct")})
public class MinimumStock implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "SKU_PRODUCT")
    @Id
    private String skuProduct;
    @Basic(optional = false)
    @Column(name = "PRODUCT_REFERENCE")
    private String productReference;
    @Basic(optional = false)
    @Column(name = "CANTIDAD_MINIMA")
    private int cantidadMinima;
    @Basic(optional = false)
    @Column(name = "CANTIDAD_DISPONIBLE")
    private int cantidadDisponible;
    @Basic(optional = false)
    @Column(name = "DIFERENCIA")
    private int diferencia;

    public MinimumStock() {
    }

    public String getSkuProduct() {
        return skuProduct;
    }

    public void setSkuProduct(String skuProduct) {
        this.skuProduct = skuProduct;
    }

    public String getProductReference() {
        return productReference;
    }

    public void setProductReference(String productReference) {
        this.productReference = productReference;
    }

    public int getCantidadMinima() {
        return cantidadMinima;
    }

    public void setCantidadMinima(int cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public int getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(int diferencia) {
        this.diferencia = diferencia;
    }
    
}

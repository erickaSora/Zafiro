/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "PRODUCT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p WHERE p.type = 1"),
    @NamedQuery(name = "Service.findAll", query = "SELECT p FROM Product p WHERE p.type = 0"),
    @NamedQuery(name = "Department.findAll", query = "SELECT p FROM Product p WHERE p.type = 2"),
    @NamedQuery(name = "Product.findById", query = "SELECT p FROM Product p WHERE p.idProductPk = :id"),
    @NamedQuery(name = "Product.findByName", query = "SELECT p FROM Product p WHERE LOWER(p.skuProduct) = :name"),
    @NamedQuery(name = "Product.findLast", query = "SELECT MAX(p.idProductPk) FROM Product p"),
    @NamedQuery(name = "ServiceProduct.findByLike", query = "SELECT p FROM Product p WHERE LOWER(p.skuProduct) LIKE :id OR LOWER(p.productReference) LIKE :txt2 "
            + "OR LOWER(p.productDescription) LIKE :txt3"),
    @NamedQuery(name = "Product.findByLike", query = "SELECT p FROM Product p WHERE p.type = 1 AND p IN (SELECT p FROM Product p WHERE LOWER(p.skuProduct) LIKE :id OR LOWER(p.productReference) LIKE :txt2 "
            + "OR LOWER(p.productDescription) LIKE :txt3)"),
    @NamedQuery(name = "Service.findByLike", query = "SELECT p FROM Product p WHERE p.type = 0 AND p IN (SELECT p FROM Product p WHERE LOWER(p.skuProduct) LIKE :id OR LOWER(p.productReference) LIKE :txt2 "
            + "OR LOWER(p.productDescription) LIKE :txt3)"),
    @NamedQuery(name = "Department.findByLike", query = "SELECT p FROM Product p WHERE p.type = 2 AND p IN (SELECT p FROM Product p WHERE LOWER(p.skuProduct) LIKE :id OR LOWER(p.productReference) LIKE :txt2 "
            + "OR LOWER(p.productDescription) LIKE :txt3)"),
    @NamedQuery(name = "Product.findByProductReference", query = "SELECT p FROM Product p WHERE p.productReference = :productReference"),
    @NamedQuery(name = "Product.findByProductDescription", query = "SELECT p FROM Product p WHERE p.productDescription = :productDescription"),
    @NamedQuery(name = "Product.findByCantidadSeleccionada", query = "SELECT p FROM Product p WHERE p.cantidadSeleccionada = :cantidadSeleccionada"),
    @NamedQuery(name = "Product.findByActualPrice", query = "SELECT p FROM Product p WHERE p.actualPrice = :actualPrice"),
    @NamedQuery(name = "Product.findByType", query = "SELECT p FROM Product p WHERE p.type = :type"),
    @NamedQuery(name = "Product.findByDepartment", query = "SELECT p FROM Product p WHERE p.idProductFk.idProductPk = :id"),
    @NamedQuery(name = "Product.findAllImported", query = "SELECT p FROM Product p WHERE p.idProductPk > :id"),
    @NamedQuery(name = "Product.findMaxId", query = "SELECT MAX(p.idProductPk) FROM Product p")})
public class Product implements Serializable {
    @OneToMany(mappedBy = "product")
    private Collection<ProductDiscount> productDiscountCollection;
    @Basic(optional = false)
    @Column(name = "MINIMUN_STOCK")
    private int minimunStock;
    @OneToMany(mappedBy = "idProductFk")
    private Collection<Product> productCollection;
    @JoinColumn(name = "ID_PRODUCT_FK", referencedColumnName = "ID_PRODUCT_PK")
    @ManyToOne
    private Product idProductFk;
    @OneToMany(mappedBy = "product")
    private Collection<QuotationDetail> quotationDetailCollection;
    @OneToMany(mappedBy = "product")
    private Collection<ProductTaxes> productTaxesCollection;
    @OneToMany(mappedBy = "product")
    private Collection<TransactionDetail> transactionDetailCollection;
    @OneToMany(mappedBy = "product")
    private Collection<ProductCategory> productCategoryCollection;
    @ManyToMany(mappedBy = "productCollection")
    private Collection<ItemCategory> itemCategoryCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_PRODUCT_PK")
    private Integer idProductPk;
    @Column(name = "SKU_PRODUCT")
    private String skuProduct;
    @Basic(optional = false)
    @Column(name = "PRODUCT_REFERENCE")
    private String productReference;
    @Column(name = "PRODUCT_DESCRIPTION")
    private String productDescription;
    @Column(name = "CANTIDAD_SELECCIONADA")
    private Integer cantidadSeleccionada = 1;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ACTUAL_PRICE")
    private Double actualPrice;
    @Basic(optional = false)
    @Column(name = "TYPE")
    private short type;
    @OneToMany(mappedBy = "product")
    private Collection<Stock> stockCollection;

    public Product() {
    }

    public Product(Integer idProductPk) {
        this.idProductPk = idProductPk;
    }

    public Product(Integer idProductPk, String productReference, short type) {
        this.idProductPk = idProductPk;
        this.productReference = productReference;
        this.type = type;
    }

    public Product(Integer idProductPk, Double actualPrice, String realDescription) {
        this.idProductPk = idProductPk;
        this.actualPrice = actualPrice;
        this.productDescription = realDescription;
    }
    
    public Product(Integer idProductPk, String skuProduct, Double actualPrice, String realDescription, Collection<ProductTaxes> pTaxesCollection, Collection<ProductDiscount> pDiscountCollection) {
        this.idProductPk = idProductPk;
        this.skuProduct = skuProduct;
        this.actualPrice = actualPrice;
        this.productDescription = realDescription;
        this.productTaxesCollection = pTaxesCollection;
        this.productDiscountCollection = pDiscountCollection;
    }

    public Integer getIdProductPk() {
        return idProductPk;
    }

    public void setIdProductPk(Integer idProductPk) {
        this.idProductPk = idProductPk;
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

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Integer getCantidadSeleccionada() {
        return cantidadSeleccionada;
    }

    public void setCantidadSeleccionada(Integer cantidadSeleccionada) {
        this.cantidadSeleccionada = cantidadSeleccionada;
    }

    public Double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getPrice() {
        DecimalFormat format = new DecimalFormat("###,###.##");
        return format.format(actualPrice);
    }

    @XmlTransient
    public Collection<Stock> getStockCollection() {
        return stockCollection;
    }

    public void setStockCollection(Collection<Stock> stockCollection) {
        this.stockCollection = stockCollection;
    }

    public String getCantidadDisponible() {
        int cantidadDisponible = 0;
        for (Stock stock : stockCollection) {
            cantidadDisponible += stock.getAmount();
        }
        return String.valueOf(cantidadDisponible);
    }
    
    public String getAssociatedProducts(){
        return String.valueOf(productCollection.size());
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProductPk != null ? idProductPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.idProductPk == null && other.idProductPk != null) || (this.idProductPk != null && !this.idProductPk.equals(other.idProductPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String ret = productDescription + " | " + productReference + " | $"+getPrice();
        if(type==1){
            ret = productDescription + " | " + productReference + " | $"+getPrice()+ " | Cantidad disponible: "+getCantidadDisponible();
        }
        return ret;
    }

    @XmlTransient
    public Collection<ItemCategory> getItemCategoryCollection() {
        return itemCategoryCollection;
    }

    public void setItemCategoryCollection(Collection<ItemCategory> itemCategoryCollection) {
        this.itemCategoryCollection = itemCategoryCollection;
    }

    @XmlTransient
    public Collection<ProductCategory> getProductCategoryCollection() {
        return productCategoryCollection;
    }

    public void setProductCategoryCollection(Collection<ProductCategory> productCategoryCollection) {
        this.productCategoryCollection = productCategoryCollection;
    }

    @XmlTransient
    public Collection<TransactionDetail> getTransactionDetailCollection() {
        return transactionDetailCollection;
    }

    public void setTransactionDetailCollection(Collection<TransactionDetail> transactionDetailCollection) {
        this.transactionDetailCollection = transactionDetailCollection;
    }

    @XmlTransient
    public Collection<ProductTaxes> getProductTaxesCollection() {
        if (productTaxesCollection == null) {
            productTaxesCollection = new ArrayList<>();
        }
        return productTaxesCollection;
    }

    public void setProductTaxesCollection(Collection<ProductTaxes> productTaxesCollection) {
        this.productTaxesCollection = productTaxesCollection;
    }

    @XmlTransient
    public Collection<QuotationDetail> getQuotationDetailCollection() {
        return quotationDetailCollection;
    }

    public void setQuotationDetailCollection(Collection<QuotationDetail> quotationDetailCollection) {
        this.quotationDetailCollection = quotationDetailCollection;
    }

    public void addProductTaxes(ProductTaxes pt) {
        if (!productTaxesCollection.contains(pt)) {
            productTaxesCollection.add(pt);
            if (pt.getProduct() != this) {
                pt.setProduct(this);
            }
        }
    }

    public void removeProductTaxes(ProductTaxes pt) {
        if (productTaxesCollection.contains(pt)) {
            productTaxesCollection.remove(pt);
        }
    }
    
    public void addProductDiscount(ProductDiscount pt) {
        if (!productDiscountCollection.contains(pt)) {
            productDiscountCollection.add(pt);
            if (pt.getProduct() != this) {
                pt.setProduct(this);
            }
        }
    }

    public void removeProductDiscount(ProductDiscount pt) {
        if (productDiscountCollection.contains(pt)) {
            productDiscountCollection.remove(pt);
        }
    }
    
    public void addProductItems(ProductCategory pc) {
        if (!productCategoryCollection.contains(pc)) {
            productCategoryCollection.add(pc);
            if (pc.getProduct() != this) {
                pc.setProduct(this);
            }
        }
    }

    public void removeProductItems(ProductCategory pi) {
        if (productCategoryCollection.contains(pi)) {
            productCategoryCollection.remove(pi);
        }
    }
    
    public void refreshStockCollection(Stock s) {
        if (stockCollection.contains(s)) {
            stockCollection.remove(s);
            stockCollection.add(s);
        }else{
            stockCollection.add(s);
            if (s.getProduct() != this) {
                s.setProduct(this);
            }
        }
    }

    public int getMinimunStock() {
        return minimunStock;
    }

    public void setMinimunStock(int minimunStock) {
        this.minimunStock = minimunStock;
    }

    @XmlTransient
    public Collection<Product> getProductCollection() {
        return productCollection;
    }

    public void setProductCollection(Collection<Product> productCollection) {
        this.productCollection = productCollection;
    }

    public Product getIdProductFk() {
        return idProductFk;
    }

    public void setIdProductFk(Product idProductFk) {
        this.idProductFk = idProductFk;
    }

    @XmlTransient
    public Collection<ProductDiscount> getProductDiscountCollection() {
        return productDiscountCollection;
    }

    public void setProductDiscountCollection(Collection<ProductDiscount> productDiscountCollection) {
        this.productDiscountCollection = productDiscountCollection;
    } 
}

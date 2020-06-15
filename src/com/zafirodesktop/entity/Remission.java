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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "REMISSION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiceOrder.findAll", query = "SELECT r FROM Remission r WHERE r.idConceptFk IS NULL OR r.status = 3 OR r.status = 0 AND r.obs IS NOT NULL ORDER BY r.idRemissionPk DESC"),
    @NamedQuery(name = "AddInventory.findAll", query = "SELECT r FROM Remission r WHERE r.idConceptFk.conceptType = 'L' ORDER BY r.idRemissionPk DESC"),
    @NamedQuery(name = "Output.findAll", query = "SELECT r FROM Remission r WHERE r.idConceptFk.conceptType = 'E' AND r.idConceptFk.idConceptPk != 12 ORDER BY r.idRemissionPk DESC"),
    @NamedQuery(name = "Order.findAll", query = "SELECT r FROM Remission r WHERE r.idConceptFk.idConceptPk = 1 ORDER BY r.idRemissionPk DESC"),
    @NamedQuery(name = "Movement.findAll", query = "SELECT r FROM Remission r WHERE r.idConceptFk.conceptType = 'M' ORDER BY r.idRemissionPk DESC"),
    @NamedQuery(name = "Credit.findAll", query = "SELECT r FROM Remission r WHERE r.leftAmount IS NOT NULL AND r.idClientFk IS NOT NULL"),
    @NamedQuery(name = "CreditOrder.findAll", query = "SELECT r FROM Remission r WHERE r.leftAmount IS NOT NULL AND r.idSupplierFk IS NOT NULL"),
    @NamedQuery(name = "Invoice.findAll", query = "SELECT r FROM Remission r WHERE r.idConceptFk.idConceptPk = 2 AND r.status > 0 ORDER BY r.idRemissionPk DESC"),
    @NamedQuery(name = "Remission.findById", query = "SELECT r FROM Remission r WHERE r.idRemissionPk = :id"),
    @NamedQuery(name = "Remission.findInitializeCredit", query = "SELECT r FROM Remission r WHERE r.idConceptFk.conceptType = 'C' AND r.idClientFk.idPersonPk = :idPerson OR r.idConceptFk.conceptType = 'C' AND r.idSupplierFk.idPersonPk = :idPerson"),
    @NamedQuery(name = "Movement.findByStatus", query = "SELECT r FROM Remission r WHERE r.idConceptFk.conceptType = 'M' AND r.status = :status OR r.status = 3 AND r.status = :status OR r.status = 0 AND r.status = :status AND r.obs IS NOT NULL"),
    @NamedQuery(name = "ServiceOrder.findByStatus", query = "SELECT r FROM Remission r WHERE r.idConceptFk IS NULL AND r.status = :status OR r.obs is not null and r.status = :status AND r.status > 2 OR r.obs is not null and r.status = :status AND r.status = 0"),
    @NamedQuery(name = "Remission.findByIdInvoice", query = "SELECT r FROM Remission r WHERE r.idInvoiceFk.idInvoicePk = :id"),
    @NamedQuery(name = "Remission.findByIdCredit", query = "SELECT r FROM Remission r WHERE r.noBuyReference = :noBuyReference"),
    @NamedQuery(name = "Remission.findAllActiveCredit", query = "SELECT r FROM Remission r WHERE r.leftAmount > 0 AND r.idClientFk IS NOT NULL order by r.remissionDate asc"),
    @NamedQuery(name = "Remission.findAllActiveCreditOrder", query = "SELECT r FROM Remission r WHERE r.leftAmount > 0 AND r.idSupplierFk IS NOT NULL order by r.remissionDate asc"),
    @NamedQuery(name = "Remission.findActiveCreditsByClient", query = "SELECT r FROM Remission r WHERE r.idInvoiceFk.idPaymentFk.idPaymentPk = 2 AND r.idClientFk.idPersonPk = :idClient AND r.status>0 order by r.remissionDate asc"),
    @NamedQuery(name = "Earnings.findAllDate", query = "SELECT r FROM Remission r WHERE r.invoiced = 1 AND r.status > 0 AND r.remissionDate >= :startDate "
            + "and r.remissionDate <= :endDate ORDER BY r.remissionDate DESC"),
    @NamedQuery(name = "Remission.findAllCreditsByClient", query = "SELECT r FROM Remission r WHERE r.idInvoiceFk.idPaymentFk.idPaymentPk = 2 "
            + "AND r.idClientFk.idPersonPk = :idClient AND r.status>0 UNION SELECT r FROM Remission r WHERE r.idClientFk.idPersonPk = :idClient "
            + "AND r.idConceptFk.conceptType = 'C' UNION SELECT r FROM Remission r WHERE r.idClientFk.idPersonPk = :idClient AND r.leftAmount IS NOT NULL"),
    @NamedQuery(name = "Remission.findAllCreditsBySupplier", query = "SELECT r FROM Remission r WHERE r.deposit IS NOT NULL "
            + "AND r.idSupplierFk.idPersonPk = :idClient AND r.status > 0 UNION SELECT r FROM Remission r WHERE r.idSupplierFk.idPersonPk = :idClient "
            + "AND r.idConceptFk.conceptType = 'C' UNION SELECT r FROM Remission r WHERE r.idSupplierFk.idPersonPk = :idClient AND r.leftAmount IS NOT NULL"),
    @NamedQuery(name = "Remission.findByNoBuyReference", query = "SELECT r FROM Remission r WHERE r.noBuyReference = :noBuyReference"),
    @NamedQuery(name = "Credit.findByLikeInt", query = "SELECT r FROM Remission r WHERE r.leftAmount IS NOT NULL AND r.idClientFk IS NOT NULL AND r IN "
            + "(SELECT r FROM Remission r WHERE r.idRemissionPk = :id OR LOWER(CONCAT(r.idClientFk.personFirstname,' ',r.idClientFk.personLastname)) "
            + "LIKE :txt2 OR r.noBuyReference = :txt3)"),
    @NamedQuery(name = "CreditOrder.findByLikeInt", query = "SELECT r FROM Remission r WHERE r.leftAmount IS NOT NULL AND r.idSupplierFk IS NOT NULL AND r IN "
            + "(SELECT r FROM Remission r WHERE r.idRemissionPk = :id OR LOWER(CONCAT(r.idSupplierFk.personFirstname,' ',r.idSupplierFk.personLastname)) "
            + "LIKE :txt2 OR r.noBuyReference = :txt3)"),
    @NamedQuery(name = "AddInventory.findByLikeInt", query = "SELECT r FROM Remission r WHERE r.idConceptFk.conceptType = 'L' AND r IN "
            + "(SELECT r FROM Remission r WHERE r.idRemissionPk = :id OR LOWER(r.idTurnFk.idUserFk.firstName) LIKE :txt2 OR r.noBuyReference LIKE :txt3)"),
    @NamedQuery(name = "Movement.findByLikeInt", query = "SELECT r FROM Remission r WHERE r.idConceptFk.conceptType = 'M' AND r IN "
            + "(SELECT r FROM Remission r WHERE r.idRemissionPk = :id OR LOWER(r.idConceptFk.conceptName) LIKE :txt2 OR r.idConceptFk.conceptName LIKE :txt3)"),
    @NamedQuery(name = "Output.findByLikeInt", query = "SELECT r FROM Remission r WHERE r.idConceptFk.conceptType = 'E' AND r IN "
            + "(SELECT r FROM Remission r WHERE r.idRemissionPk = :id OR LOWER(r.idConceptFk.conceptName) LIKE :txt2 OR r.idConceptFk.conceptName LIKE :txt3)"),
    @NamedQuery(name = "Order.findByLikeInt", query = "SELECT r FROM Remission r WHERE r.idConceptFk.idConceptPk = 1 AND r IN "
            + "(SELECT r FROM Remission r WHERE r.idRemissionPk = :id OR LOWER(CONCAT(r.idSupplierFk.personFirstname,' ',r.idSupplierFk.personLastname)) "
            + "LIKE :txt2 OR r.noBuyReference LIKE :txt3)"),
    @NamedQuery(name = "ServiceOrder.findByLikeInt", query = "SELECT r FROM Remission r WHERE r.idConceptFk IS NULL AND r IN "
            + "(SELECT r FROM Remission r WHERE r.idRemissionPk = :id OR LOWER(CONCAT(r.idClientFk.personFirstname,' ',r.idClientFk.personLastname)) "
            + "LIKE :txt2 OR r.noBuyReference LIKE :txt3) OR r.status = 3 AND r IN "
            + "(SELECT r FROM Remission r WHERE r.idRemissionPk = :id OR LOWER(CONCAT(r.idClientFk.personFirstname,' ',r.idClientFk.personLastname)) "
            + "LIKE :txt2 OR r.noBuyReference LIKE :txt3) OR r.status = 0 AND r.obs IS NOT NULL AND r IN"
            + "(SELECT r FROM Remission r WHERE r.idRemissionPk = :id OR LOWER(CONCAT(r.idClientFk.personFirstname,' ',r.idClientFk.personLastname)) "
            + "LIKE :txt2 OR r.noBuyReference LIKE :txt3)"),
    @NamedQuery(name = "Invoice.findByLike", query = "SELECT r FROM Remission r WHERE r.idConceptFk.idConceptPk = 2 AND r.status > 0 AND r IN "
            + "(SELECT r FROM Remission r WHERE LOWER(r.idInvoiceFk.idInvoicePk) LIKE :id OR LOWER(CONCAT(r.idClientFk.personFirstname,' ',r.idClientFk.personLastname)) "
            + "LIKE :txt2 OR r.noBuyReference = :txt3)")})
public class Remission implements Serializable {
    @OneToMany(mappedBy = "idRemissionFk")
    private Collection<Deposit> depositCollection;
    @OneToMany(mappedBy = "idRemissionFk")
    private Collection<Annotation> annotationCollection;

    private static final int INVALIDATED = 0;
    private static final int ACTIVE = 1;
    private static final int COMPLETED = 2;
    private static final int INVOICED = 3;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_REMISSION_PK")
    private Integer idRemissionPk;
    @Basic(optional = false)
    @Column(name = "STATUS")
    private short status;
    @Basic(optional = false)
    @Column(name = "INVOICED")
    private short invoiced;
    @Basic(optional = false)
    @Column(name = "REMISSION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date remissionDate = getDefaultDate();
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "NO_BUY_REFERENCE")
    private String noBuyReference;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "DEPOSIT")
    private Double deposit;
    @Column(name = "OBS")
    private String obs;
    @Column(name = "LEFT_AMOUNT")
    private Double leftAmount;
    @Column(name = "ID_SELL")
    private String idSell;
    @OneToMany(mappedBy = "idRemissionFk")
    private Collection<Tranzaction> tranzactionCollection;
    @JoinColumn(name = "ID_CLIENT_FK", referencedColumnName = "ID_PERSON_PK")
    @ManyToOne
    private Person idClientFk;
    @JoinColumn(name = "ID_SUPPLIER_FK", referencedColumnName = "ID_PERSON_PK")
    @ManyToOne
    private Person idSupplierFk;
    @JoinColumn(name = "ID_INVOICE_FK", referencedColumnName = "ID_INVOICE_PK")
    @ManyToOne
    private Invoice idInvoiceFk;
    @JoinColumn(name = "ID_CONCEPT_FK", referencedColumnName = "ID_CONCEPT_PK")
    @ManyToOne
    private Concept idConceptFk;
    @JoinColumn(name = "ID_TURN_FK", referencedColumnName = "ID_TURN_PK")
    @ManyToOne
    private Turn idTurnFk;

    public Remission() {
    }

    public Remission(Integer idRemissionPk) {
        this.idRemissionPk = idRemissionPk;
    }

    public Remission(Integer idRemissionPk, short status, short invoiced, Date remissionDate) {
        this.idRemissionPk = idRemissionPk;
        this.status = status;
        this.invoiced = invoiced;
        this.remissionDate = remissionDate;
    }

    public Remission(Date remissionDate, Double leftAmount, Person idClientFk) {
        this.remissionDate = remissionDate;
        this.leftAmount = leftAmount;
        this.idClientFk = idClientFk;
    }

    public Integer getIdRemissionPk() {
        return idRemissionPk;
    }

    public void setIdRemissionPk(Integer idRemissionPk) {
        this.idRemissionPk = idRemissionPk;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public short getInvoiced() {
        return invoiced;
    }

    public void setInvoiced(short invoiced) {
        this.invoiced = invoiced;
    }

    public Date getRemissionDate() {
        return remissionDate;
    }

    public void setRemissionDate(Date remissionDate) {
        this.remissionDate = remissionDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getNoBuyReference() {
        return noBuyReference;
    }

    public void setNoBuyReference(String noBuyReference) {
        this.noBuyReference = noBuyReference;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Double getLeftAmount() {
        return leftAmount;
    }

    public void setLeftAmount(Double leftAmount) {
        this.leftAmount = leftAmount;
    }

    public String getIdSell() {
        return idSell;
    }

    public void setIdSell(String idSell) {
        this.idSell = idSell;
    }

    public String getTotalName() {
        return idClientFk.getTotalName();
    }

    public String getSupplierName() {
        return idSupplierFk.getTotalName();
    }

    public String getDate() {
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        return dt.format(remissionDate);
    }
    
    public String getInvoiceDate() {
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        return dt.format(idInvoiceFk.getInvoiceDate());
    }

    public String getInvoiceId() {
        return idInvoiceFk.getIdInvoicePk();
    }

    public String getTranzactionPrice() {
        DecimalFormat format = new DecimalFormat("###,###.##");
        float price = 0;
        for (Tranzaction tranzaction : tranzactionCollection) {
            price += tranzaction.getTransactionPrice();
        }
        return format.format(price);
    }

    public String getTranzactionLeftAmount() {
        DecimalFormat format = new DecimalFormat("###,###.##");
        return format.format(leftAmount);
    }

    public String getTranzactionDeposit() {
        DecimalFormat format = new DecimalFormat("###,###.##");
        return format.format(deposit);
    }
    
    public String getMovementStatus(){
        if(status==INVALIDATED)
            return "Anulado";
        else if(status==ACTIVE)
            return "Pendiente";
        else if(status==COMPLETED)
            return "Finalizado";
        else
            return "Facturado";
    }
    
    public String getMovementType(){
        return idConceptFk.getConceptName();
    }
    
    public String getUserName(){
        return idTurnFk.getIdUserFk().getTotalName();
    }

    public static Date getDefaultDate() {
        return getDefaulTimezone(TimeZone.getDefault());
    }

    public static Date getDefaulTimezone(TimeZone timeZone) {
        return Calendar.getInstance(timeZone).getTime();
    }

    @XmlTransient
    public Collection<Tranzaction> getTranzactionCollection() {
        return tranzactionCollection;
    }

    public void setTranzactionCollection(Collection<Tranzaction> tranzactionCollection) {
        this.tranzactionCollection = tranzactionCollection;
    }

    public Person getIdClientFk() {
        return idClientFk;
    }

    public void setIdClientFk(Person idClientFk) {
        this.idClientFk = idClientFk;
    }

    public Person getIdSupplierFk() {
        return idSupplierFk;
    }

    public void setIdSupplierFk(Person idSupplierFk) {
        this.idSupplierFk = idSupplierFk;
    }

    public Invoice getIdInvoiceFk() {
        return idInvoiceFk;
    }

    public void setIdInvoiceFk(Invoice idInvoiceFk) {
        this.idInvoiceFk = idInvoiceFk;
    }

    public Concept getIdConceptFk() {
        return idConceptFk;
    }

    public void setIdConceptFk(Concept idConceptFk) {
        this.idConceptFk = idConceptFk;
    }

    public Turn getIdTurnFk() {
        return idTurnFk;
    }

    public void setIdTurnFk(Turn idTurnFk) {
        this.idTurnFk = idTurnFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRemissionPk != null ? idRemissionPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Remission)) {
            return false;
        }
        Remission other = (Remission) object;
        if ((this.idRemissionPk == null && other.idRemissionPk != null) || (this.idRemissionPk != null && !this.idRemissionPk.equals(other.idRemissionPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.Remission[ idRemissionPk=" + idRemissionPk + " ]";
    }

    @XmlTransient
    public Collection<Annotation> getAnnotationCollection() {
        return annotationCollection;
    }

    public void setAnnotationCollection(Collection<Annotation> annotationCollection) {
        this.annotationCollection = annotationCollection;
    }
    
    public void addTrazaction(Tranzaction tt) {
        if (!tranzactionCollection.contains(tt)) {
            tranzactionCollection.add(tt);
            if (tt.getIdRemissionFk() != this) {
                tt.setIdRemissionFk(this);
            }
        }
    }

    public void removeTranzaction(Tranzaction tt) {
        if (tranzactionCollection.contains(tt)) {
            tranzactionCollection.remove(tt);
        }
    }
    
    public void addDeposit(Deposit dp) {
        if (!depositCollection.contains(dp)) {
            depositCollection.add(dp);
            if (dp.getIdRemissionFk() != this) {
                dp.setIdRemissionFk(this);
            }
        }
    }

    public void removeDeposit(Deposit dp) {
        if (depositCollection.contains(dp)) {
            depositCollection.remove(dp);
        }
    }

    @XmlTransient
    public Collection<Deposit> getDepositCollection() {
        return depositCollection;
    }

    public void setDepositCollection(Collection<Deposit> depositCollection) {
        this.depositCollection = depositCollection;
    }

}

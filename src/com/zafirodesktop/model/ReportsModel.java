/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.model;

import com.zafirodesktop.entity.TotalesEgresos;
import com.zafirodesktop.entity.TotalesIngresos;
import java.text.SimpleDateFormat;
import static java.util.Calendar.HOUR;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javax.persistence.Query;

/**
 *
 * @author Digitall
 */
public class ReportsModel extends AbstractFacade {

    public Collection productMovement(int id) {
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Query query = entityManager.createNamedQuery("MovimientosProducto.findById").setParameter("id", id);
        list = query.getResultList();

        return list;
    }
    
    public Collection productOrders(int idProduct) {
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Query query = entityManager.createNamedQuery("TransactionDetail.findByProductConcept").setParameter("idProduct", idProduct);
        list = query.getResultList();
        return list;
    }

    public Collection productMovementDate(int id, Date dateFrom, Date dateTo) {
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Query query = entityManager.createNamedQuery("MovimientosProducto.findAllDate").setParameter("id", id)
                .setParameter("startDate", dateFrom)
                .setParameter("endDate", dateTo);
        list = query.getResultList();

        return list;
    }

    public Collection topSelledProducts(Date dateFrom, Date dateTo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTo.setTime(dateTo.getTime() + 23 * HOUR);
        Collection<Object[]> results;
        Query query = entityManager.createNativeQuery("select SKU_PRODUCT, PRODUCT_REFERENCE, PRODUCT_DESCRIPTION, SUM(AMOUNT) as amount "
                + "from TOP_SELLED "
                + "where REMISSION_DATE >= '" + sdf.format(dateFrom)
                + "' and REMISSION_DATE <= '" + sdf.format(dateTo)
                + "' GROUP BY SKU_PRODUCT, PRODUCT_REFERENCE, PRODUCT_DESCRIPTION ORDER BY amount DESC");
        results = query.getResultList();
        return results;
    }

    /*
     Método para retornar el producto con más ventas en el mes
     @param: fecha de inicio Date, fecha final Date
     */
    public Object[] topSelledProduct(Date dateFrom, Date dateTo) {
        Object[] ob = {"", "", "", 0};
        try {
            int cont = 0;
            list = topSelledProducts(dateFrom, dateTo);
            Iterator it = list.iterator();
            while (it.hasNext() && cont == 0) {
                ob = (Object[]) it.next();
                it.remove();
                cont++;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return ob;
    }
        
    /*
     Método para retornar la sumatoria de facturas realizadas en el mes
     @param: fecha de inicio Date, fecha final Date
     */
    public int monthInvoicesTotal(Date dateFrom, Date dateTo) {
        int cont = 0;
        try {
            list = findAllDate("Invoice", dateFrom, dateTo);
            for (Object object : list) {
                cont++;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return cont;
    }
    
    /*
     Método para retornar la sumatoria de ventas del mes
     @param: fecha de inicio Date, fecha final Date
     */
    public float monthSellesTotal(Date dateFrom, Date dateTo) {
        float total = 0;
        try {
            list = findAllDate("TotalesIngresos", dateFrom, dateTo);
            for (Object object : list) {
                TotalesIngresos totalesIngresos = (TotalesIngresos) object;
                total+=totalesIngresos.getTotal();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return total;
    }
    
    /*
     Método para retornar la sumatoria de egresos del mes
     @param: fecha de inicio Date, fecha final Date
     */
    public float monthTotalExpenses(Date dateFrom, Date dateTo) {
        float total = 0;
        try {
            list = findAllDate("TotalesEgresos", dateFrom, dateTo);
            for (Object object : list) {
                TotalesEgresos totalesEgresos = (TotalesEgresos) object;
                total+=totalesEgresos.getTotal();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return total;
    }

    /*public int productTotalExpenses(String id){
     int resp=0;
     Query query = entityManager.createNamedQuery("MovimientosProducto.findByAllExpenses").setParameter("id", id);
     if(query.getSingleResult()!=null)
     resp = Integer.parseInt(query.getSingleResult().toString());
     return resp;
     }
    
     public int productTotalEntries(String id){
     int resp=0;
     Query query = entityManager.createNamedQuery("MovimientosProducto.findByAllEntries").setParameter("id", id);
     if(query.getSingleResult()!=null)
     resp = Integer.parseInt(query.getSingleResult().toString());
     return resp;
     }
    
     public int productTotalMovements(String id){
     int resp=0;
     Query query = entityManager.createNamedQuery("MovimientosProducto.findByAllMovements").setParameter("id", id);
     if(query.getSingleResult()!=null)
     resp = Integer.parseInt(query.getSingleResult().toString());
     return resp;
     }*/
    public int productTotalStock(int id) {
        int resp = 0;
        Query query = entityManager.createNamedQuery("Stock.findTotalAmount").setParameter("id", id);
        if (query.getSingleResult() != null) {
            resp = Integer.parseInt(query.getSingleResult().toString());
        }
        return resp;
    }
}

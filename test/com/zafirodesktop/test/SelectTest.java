/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.test;

import com.zafirodesktop.entity.MovimientosProducto;
import com.zafirodesktop.entity.Product;
import com.zafirodesktop.entity.TotalesIngresos;
import com.zafirodesktop.entity.Turn;
import com.zafirodesktop.model.ReportsModel;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.model.TurnModel;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Digitall
 */
public class SelectTest {

    public static final long HOUR = 3600 * 1000;
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-M-yyyy");
        String day = sdf1.format(Calendar.getInstance().getTime());
        day+=" 00:00:00";
        Date today = sdf.parse(day);
        Date todayNigth = new Date(today.getTime() + 23 * HOUR);
        SessionBD.persistenceCreate();
        TurnModel tm = new TurnModel();
        /*Collection<Turn> tc = tm.findAllDate("Turn", today, todayNigth);
        for (Turn turn : tc) {
            System.out.println(turn.getStartDate());
        }*/
        Collection<TotalesIngresos> entries = tm.findByTurn("TotalesIngresos", new BigInteger("21"));
        for (TotalesIngresos totalesIngresos : entries) {
            System.out.println(totalesIngresos.getTotal());
        }
        
        /*ReportsModel rm = new ReportsModel();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        SimpleDateFormat toCompareDF = new SimpleDateFormat("M-yyyy");
        Calendar cal = Calendar.getInstance();
        String date1 = "01-08-2014 00:00:00";
        String date2 = "31-08-2014 23:59:00";
        String date3 = "01-"+toCompareDF.format(cal.getTime())+" 00:00:00";
        String date4 = "31-"+toCompareDF.format(cal.getTime())+" 23:59:00";
        Date dateInit = sdf.parse(date1);
        Date dateFinish = sdf.parse(date2);
        Object[] obj = rm.topSelledProduct(dateInit, dateFinish);
        System.out.println((String) obj[0] + " - " + (String) obj[1] + " - " + (String) obj[2] + " - "+obj[3]);
        System.out.println(date3+" - "+date4);*/
        /*Collection<Product> productsList = rm.findAll("Product");
         for (Product product : productsList) {
         int entradas = 0, ventas = 0, obsequios = 0, deterioro = 0, salidas = 0, garantias = 0;
         Collection<MovimientosProducto> movList = rm.productMovement(product.getIdProductPk());
         for (MovimientosProducto movimientosProducto : movList) {
         switch (movimientosProducto.getIdConceptPk()) {
         case 4:
         garantias+= movimientosProducto.getAmount();
         break;
         case 2:
         ventas += movimientosProducto.getAmount(); 
         salidas += movimientosProducto.getAmount();
         break;
         case 6:
         deterioro += movimientosProducto.getAmount(); 
         salidas += movimientosProducto.getAmount();
         break;
         case 5:
         obsequios += movimientosProducto.getAmount();
         break;
         default:
         entradas += movimientosProducto.getAmount();
         break;
         }
         }
         System.out.println("Producto: "+product.getProductReference()+"\nEntradas totales: "+entradas+"\nVentas: "+ventas+"\nObsequios: "+obsequios+"\nDeterioro: "+deterioro+"\nSalidas totales: "+salidas
         +"\nGarantías totales: "+garantias+"\nCantidad Disponible: "+product.getCantidadDisponible()+"\n\n");
         }*/

    }
}

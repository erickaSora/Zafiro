/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zafirodesktop.entity.Concept;
import com.zafirodesktop.entity.Invoice;
import com.zafirodesktop.entity.Payment;
import com.zafirodesktop.entity.Product;
import com.zafirodesktop.entity.Remission;
import com.zafirodesktop.entity.Tax;
import com.zafirodesktop.entity.TransactionDetail;
import com.zafirodesktop.entity.Tranzaction;
import com.zafirodesktop.util.JSONAccess;
import com.zafirodesktop.util.JSONObs;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Digitall
 */
public class ConexionAPI {

    private final String USER_AGENT = "Mozilla/5.0";
    String cookie;
    String cookieName;
    String cookieValue;

    public static void main(String[] args){

        ConexionAPI http = new ConexionAPI();

        System.out.println("Testing 1 - Send Http GET request");
        http.sendGet();

        /*System.out.println("\nTesting 2 - Send Http POST request");
         http.sendPost();*/
    }

    // HTTP GET request
    private void sendGet() {

        try {
            String url = "http://api.scirebox.com/oauth/code?username=digitall&passwd=123&subdomain=digitall";
            
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            // optional default is GET
            con.setRequestMethod("GET");
            
            //add request header
            //con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            Gson gson = new Gson();
            JSONObs obs = gson.fromJson(response.toString(), JSONObs.class);
            
            /**
             * Parte del phpsessid
             */
            String headerName = null;
            //String cookie="";
            for (int i = 1; (headerName = con.getHeaderFieldKey(i)) != null; i++) {
                if (headerName.equals("Set-Cookie")) {
                    cookie = con.getHeaderField(i);
                }
            }
            cookie = cookie.substring(0, cookie.indexOf(";"));
            cookieName = cookie.substring(0, cookie.indexOf("="));
            cookieValue = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
            //System.out.println(response.toString()+"\n"+cookieName+" "+cookieValue);
            
            /**
             * Fin Primera petición, código de autenticación almacenado en obs
             * Inicio segunda petición
             *
             */
            url = "http://api.scirebox.com/oauth/token?code=" + obs.getData().getAuthcode() + "&client_id=1&client_secret=abcdefg12345";
            URL obj2 = new URL(url);
            HttpURLConnection con2 = (HttpURLConnection) obj2.openConnection();
            con2.setRequestMethod("GET");
            con2.setRequestProperty("Cookie", cookieName + "=" + cookieValue);
            responseCode = con2.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url + "\n" + cookieName + " " + cookieValue);
            System.out.println("Response Code : " + responseCode);
            
            BufferedReader in2 = new BufferedReader(
                    new InputStreamReader(con2.getInputStream()));
            StringBuffer response2 = new StringBuffer();
            String inputLine2;
            while ((inputLine2 = in2.readLine()) != null) {
                response2.append(inputLine2);
            }
            in2.close();
            System.out.println(response2.toString());
            JSONAccess obs2 = gson.fromJson(response2.toString(), JSONAccess.class);
            
            /* Fin Segunda petición, token de acceso almacenado en obs2 Inicio
            // petición de objeto (Cliente)
            
            url =  "http://api.scirebox.com/tax?tax_name=mauricio'sgays&tax_pct=19&access_token="+obs2.getData().getAccess_token();
            URL obj3 = new URL(url); HttpURLConnection con3 = (HttpURLConnection)
            obj3.openConnection(); con3.setRequestMethod("GET");
            con3.setRequestProperty("Cookie", cookieName+"="+cookieValue);
            responseCode = con3.getResponseCode();
            System.out.println("\nSending'GET' request to URL : " + url + "\n"+cookieName+"="+cookieValue);
            System.out.println("Response Code : " + responseCode); BufferedReader
            in3 = new BufferedReader( new
            InputStreamReader(con3.getInputStream())); StringBuffer response3 =
            new StringBuffer(); String inputLine3; while ((inputLine3 =
            in3.readLine()) != null) { response3.append(inputLine3); }
            in3.close(); System.out.println(response3.toString());
            }*/
            //Peticíon (POST)
            //Data initialize Taxes
            Tax tax = new Tax(1, "Charles Xavier's tax", 15);
            
            //Data initialize Invoice
            Payment pay = new Payment(1);
            Concept concpt = new Concept(1);
            Invoice invoice = new Invoice("16", new Date(), new Short("1"));
            Remission remission = new Remission(1, new Short("1"), new Short("1"), new Date());
            Tranzaction tranzaction = new Tranzaction(1, new Date());
            Product product = new Product(1);
            TransactionDetail td = new TransactionDetail();
            td.setProduct(product);
            td.setAmount(1);
            td.setUnitPrice(10000.0);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            url = "http://api.scirebox.com/invoice";
            String parameters = "id=" + invoice.getIdInvoicePk() + "&invoice_date=" + format.format(invoice.getInvoiceDate()) + "&payment_id=" + pay.getIdPaymentPk() + "&status=" + invoice.getStatus()
            + "&remission_status=" + remission.getStatus() + "&invoiced=" + remission.getInvoiced() + "&remission_date=" + format.format(remission.getRemissionDate())
            + "&transaction_date=" + format.format(tranzaction.getTransactionDate())+"&person_id=1&nit=1057&person_firstname=AndresF&person_type=C";
            /*url = "http://api.scirebox.com/tax";
            String parameters = "tax_name=myTax&tax_pct=8";*/
            //Parseo de transaction detail a JSON
            JsonObject json = new JsonObject();
            json.addProperty("product_id", "1");
            json.addProperty("unit_price", "10000");
            json.addProperty("amount", "2");
            json.addProperty("description", "bla bla bla");
            //parameters += "&transaction_details="+json;
            
            byte[] postData = parameters.getBytes(Charset.forName("UTF-8"));
            int postDataLength = postData.length;
            URL request = new URL(url);
            con2 = (HttpURLConnection) request.openConnection();
            con2.setDoInput(true);
            con2.setDoOutput(true);
            con2.setRequestMethod("POST");
            con2.setRequestProperty("Cookie", cookieName + "=" + cookieValue);
            con2.setRequestProperty("Authorization", obs2.getData().getAccess_token());
            con2.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            con2.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(con2.getOutputStream());
            wr.write(postData);
            wr.flush();
            wr.close();
            
            responseCode = con2.getResponseCode();
            System.out.println("Sending POST petition to: " + url);
            System.out.println("Parameters: " + parameters);
            System.out.println("HTTP Post Response code: " + responseCode);
            in2 = new BufferedReader(
                    new InputStreamReader(con2.getErrorStream()));
            response2 = new StringBuffer();
            
            while ((inputLine2 = in2.readLine()) != null) {
                response2.append(inputLine2);
            }
            in2.close();
            
            //print result
            System.out.println(response2.toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConexionAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConexionAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

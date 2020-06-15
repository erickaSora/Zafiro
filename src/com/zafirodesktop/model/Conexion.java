/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Digitall
 */
public class Conexion {
    Connection con;

    public boolean shutdownBD() throws Exception {
        boolean resp = false;
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        try {
            DriverManager.getConnection("jdbc:derby://localhost:1527/zafiro;shutdown=true", "zafiro", "$764 %e2 w{~Y~b");
        } catch (SQLException se) {
            if (se.getSQLState().equals("XJ015")) {
                resp = true;
                return resp;
            }
        }
        return resp;
    }
    
    public boolean restoreBD(String path) throws Exception {
        boolean resp = false;
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        con = DriverManager.getConnection("jdbc:derby://localhost:1527/zafiro;restoreFrom="+ path, "zafiro", "$764 %e2 w{~Y~b");
        resp = true;
        closeConection();
        return resp;
    }
    
    public void closeConection() throws SQLException{
       con.close();
   }

}

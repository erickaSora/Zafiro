/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.util;

/**
 *
 * @author Digitall
 */
public class JSONAccess {
    
    private String status;
    private JSONAccesToken data;
    private String response_time;

    public JSONAccess(String status, JSONAccesToken data, String response_time) {
        this.status = status;
        this.data = data;
        this.response_time = response_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JSONAccesToken getData() {
        return data;
    }

    public void setData(JSONAccesToken data) {
        this.data = data;
    }

    public String getResponse_time() {
        return response_time;
    }

    public void setResponse_time(String response_time) {
        this.response_time = response_time;
    } 
}

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
public class JSONResetPassword {
    private String status;
    private String errors;
    private String response_time;

    public JSONResetPassword(String status, String errors, String response_time) {
        this.status = status;
        this.errors = errors;
        this.response_time = response_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getResponse_time() {
        return response_time;
    }

    public void setResponse_time(String response_time) {
        this.response_time = response_time;
    }
    
    
}



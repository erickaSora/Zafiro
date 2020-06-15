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
public class JSONObs {
    private String status;
    private JSONData data;
    private String response_time;

    public JSONObs(String status, JSONData data, String responseTime) {
        this.status = status;
        this.data = data;
        this.response_time = responseTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JSONData getData() {
        return data;
    }

    public void setData(JSONData data) {
        this.data = data;
    }

    public String getResponseTime() {
        return response_time;
    }

    public void setResponseTime(String responseTime) {
        this.response_time = responseTime;
    }
}

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
public class JSONData {
    private String authcode;

    public JSONData(String authcode) {
        this.authcode = authcode;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }
}

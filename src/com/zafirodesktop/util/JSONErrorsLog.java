/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Digitall
 */
public class JSONErrorsLog {
    private String code;
    private String currentOperation;
    private final Date errorDatetime = getDefaultDate();
    private String errorMessage;
    private String errorCause;
    private String currentUser;

    public JSONErrorsLog(String code, String currentOperation, String errorMessage, String errorCause, String currentUser) {
        this.code = code;
        this.currentOperation = currentOperation;
        this.errorMessage = errorMessage;
        this.errorCause = errorCause;
        this.currentUser = currentUser;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrentOperation() {
        return currentOperation;
    }

    public void setCurrentOperation(String currentOperation) {
        this.currentOperation = currentOperation;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCause() {
        return errorCause;
    }

    public void setErrorCause(String errorCause) {
        this.errorCause = errorCause;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
    
    public static Date getDefaultDate() {
        return getDefaulTimezone(TimeZone.getDefault());
    }

    public static Date getDefaulTimezone(TimeZone timeZone) {
        return Calendar.getInstance(timeZone).getTime();
    }

    @Override
    public String toString() {
        return "JSONErrorsLog{" + "code=" + code + ", currentOperation=" + currentOperation + ", errorDatetime=" + errorDatetime + ", errorMessage=" + errorMessage + ", errorCause=" + errorCause + ", currentUser=" + currentUser + '}';
    } 
}

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
public class ComboBoxChoices {
    
    private String itemValue;
    private String itemLabel;

    public ComboBoxChoices(String itemValue, String itemLabel) {
        this.itemValue = itemValue;
        this.itemLabel = itemLabel;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getItemLabel() {
        return itemLabel;
    }

    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }

    @Override
    public String toString() {
        return itemLabel;
    }
    
}

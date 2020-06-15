/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.test;

import com.google.gson.Gson;
import com.zafirodesktop.util.JSONObs;

/**
 *
 * @author Digitall
 */
public class GsonTest {
    public static void main(String[] args) {
        /*String json = "{\"reference\":\""+"portatil toshiba s/n: 1212121"+"\",\"description\":\""+"NO prende aparentemente por problemas físicos"+
                    "\",\"obs\":\""+"dejaron el pc con la Batería"+"\"}";
        JSONObs initialObs = new JSONObs("Portatil Dell inspiron 945 s/n: 43436546", "Prende pero no da video, probablemente chip de video","");
        Gson gson = new Gson();
        String json = gson.toJson(initialObs);
        System.out.println(json);
        
        JSONObs obs = gson.fromJson(json, JSONObs.class);*/
        //System.out.print(obs.getReference()+"\n"+obs.getDescription());
    }
}

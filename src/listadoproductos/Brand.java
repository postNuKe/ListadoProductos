/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listadoproductos;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Brand {
    private String name = "";
    private ArrayList<String> equals = new ArrayList<String>();
    
    public void Brand(){
        this.name = "";
        this.equals = new ArrayList<String>();
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void addEquals(String equals){
        this.equals.add(equals);
    }
    
    public ArrayList<String> getEquals(){
        return this.equals;
    }
    
}

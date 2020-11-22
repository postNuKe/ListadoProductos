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
    private String uri = "";
    private String percent = "";
    
    public void Brand(){
        this.name = "";
        this.equals = new ArrayList<String>();
        this.uri = "";
        this.percent = "";
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
    
    public String getUri(){
        return this.uri;
    }
    public void setUri(String uri){
        this.uri = uri;
    }
    
    public String getPercent(){
        return this.percent;
    }
    public void setPercent(String percent){
        this.percent = percent;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listadoproductos.info;

import java.util.ArrayList;

/**
 * Productos que hay en una marca, con sus porcentajes y youtube reviews
 * @author user
 */
public class BrandProduct{
    private String name = "";
    private String percent = "";
    private ArrayList<String> youtubeReviews = new ArrayList<>();
    
    public BrandProduct() {
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setPercent(String percent){
        this.percent = percent;
    }

    public String getPercent(){
        return this.percent;
    }

    public void addYoutubeReviews(String youtubeReview){
        this.youtubeReviews.add(youtubeReview);
    }

    public ArrayList<String> getYoutubeReviews(){
        return this.youtubeReviews;
    }
    
    public String toString(){
        final StringBuilder sb = new StringBuilder("");
        sb.append(this.name).append(" | ");
        sb.append(this.percent).append(" | ");
        sb.append(this.youtubeReviews.toString()).append(" | ");
        
        //System.out.println(sb.toString());
        return sb.toString();
    }
} 

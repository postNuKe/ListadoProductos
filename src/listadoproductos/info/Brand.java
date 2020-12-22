/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listadoproductos.info;

import java.util.ArrayList;
import listadoproductos.lib.Text;

/**
 *
 * @author user
 */
public class Brand {
    private String name = "";
    private ArrayList<String> equals = new ArrayList<>();
    private String uri = "";
    private String percent = "";
    private ArrayList<BrandProduct> products = new ArrayList<>();
    
    public void Brand(){
        this.name = "";
        this.equals = new ArrayList<>();
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
    /**
     * Busca del listado de productos de esta marca, si hay producto con ese nombre y si
     * tiene porcentaje específico
     * @param str Nombre del producto
     * @return 
     */
    public String getPercent(String str){
        for (BrandProduct product : this.products) {
            if(str.contains(Text.toHTML(product.getName()))){
                if(!product.getPercent().isEmpty()){
                    System.out.println(product.getPercent());
                    return product.getPercent();
                }
            }  
            /*
            for (String youtubeReview : product.getYoutubeReviews()) {
                if(str.contains(youtubeReview)){
                    System.out.println(youtubeReview);
                    return product.getPercent();
                }
            } 
*/
        }        
        return this.percent;
    }
    public void setPercent(String percent){
        this.percent = percent;
    }   
    
    public void setProducts(ArrayList<BrandProduct> products){
        this.products = products;
    }
    public void addProduct(BrandProduct product){
        this.products.add(product);
    }
    public ArrayList<BrandProduct> getProducts(){
        return this.products;
    }
}
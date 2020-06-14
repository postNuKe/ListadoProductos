/*
 * Clase para el manejo de los datos de un producto que está en oferta usado mas tarde en la
 * creacion del xml de ofertas
 * https://attacomsian.com/blog/java-read-write-xml#
 * http://zetcode.com/java/jaxb/
 */
package listadoproductos.info;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/*
    <product id="uri_product">
        <shop_name></shop_name>
        <shopCountry></shopCountry>
        <shop_uri></shop_uri>
        <name></name>
        <brand></brand>
        <price></price>
        <uri></uri>
    </product>

*/
/**
 *
 * @author user
 */
@XmlRootElement(name = "product")
@XmlType(propOrder = { "name", "brand", "uri", "price", "shopName", "shopCountry", "shopUri" })
public class ProductSpecial {
    @XmlAttribute
    private String id;
    
    private String shopName;
    private String shopUri;
    private String shopCountry;
    private String name;
    private String brand;
    private String uri;
    private String price;
    
    public ProductSpecial() {
    }
    
    public ProductSpecial(String shopName, String shopCountry, String shopUri, String name, String brand,
            String uri, String price){
        this.id = uri;
        this.shopName = shopName;
        this.shopCountry = shopCountry;
        this.shopUri = shopUri;
        this.name = name;
        this.brand = brand;
        this.uri = uri;
        this.price = price;
    }
    
    public String getId() {
            return id;
    }    
    public void setId() {
            this.id = this.uri;
    }    
    
    public String getShopName(){
        return this.shopName;
    }   
    
    @XmlElement
    public void setShopName(String name){
        this.shopName = name;
    }
    
    public String getShopCountry(){
        return this.shopCountry;
    }   
    
    @XmlElement
    public void setShopCountry(String country){
        this.shopCountry = country;
    }
    
    public String getShopUri(){
        return this.shopUri;
    }   
    
    @XmlElement
    public void setShopUri(String uri){
        this.shopUri = uri;
    }
    
    public String getName(){
        return this.name;
    }   
    
    @XmlElement
    public void setName(String name){
        this.name = name;
    }
    
    public String getBrand(){
        return this.brand;
    }   
    
    @XmlElement
    public void setBrand(String brand){
        this.brand = brand;
    }
    
    public String getUri(){
        return this.uri;
    }   
    
    @XmlElement
    public void setUri(String uri){
        this.uri = uri;
    }
    
    public String getPrice(){
        return this.price;
    }   
    
    @XmlElement
    public void setPrice(String price){
        this.price = price;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("shopName='").append(this.shopName).append('\'');
        sb.append(", shopCountry='").append(this.shopCountry).append('\'');
        sb.append(", shopUri='").append(this.shopUri).append('\'');
        sb.append(", name='").append(this.name).append('\'');
        sb.append(", brand='").append(this.brand).append('\'');
        sb.append(", uri='").append(this.uri).append('\'');
        sb.append(", price='").append(this.price).append('\'');
        sb.append('}');
        return sb.toString();
    }
    
}

/*
 * Clase para el manejo de los datos de un producto usado mas tarde en la
 * creacion del xml
 * https://attacomsian.com/blog/java-read-write-xml#
 * http://zetcode.com/java/jaxb/
 */
package listadoproductos.info;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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
        <is_special></is_special>
    </product>

*/
/**
 *
 * @author user
 */
@XmlRootElement(name = "product")
@XmlType(propOrder = { "name", "brand", "uri", "price", "isSpecial"
        , "shopName", "shopCountry", "shopUri", "brandUri", "percent"
        , "reviews", "stock", "youtubeReviews" })
public class Product {
    @XmlAttribute
    private String id;
    
    private String shopName;
    private String shopUri;
    private String shopCountry;
    private String name;
    private String brand;
    private String uri;
    private String price;
    private boolean isSpecial;
    private boolean stock;
    private String brandUri;
    private String percent;
    private List<String> reviews = new ArrayList<>();
    private List<String> youtubeReviews = new ArrayList<>();
    
    public Product() {
    }
    
    public Product(String shopName, String shopCountry, String shopUri, String name, String brand,
            String uri, String price, boolean isSpecial, String brandUri,
            String percent, List<String> reviews, boolean stock,
            List<String> youtubeReviews){
        this.id = uri;
        this.shopName = shopName;
        this.shopCountry = shopCountry;
        this.shopUri = shopUri;
        this.name = name;
        this.brand = brand;
        this.uri = uri;
        this.price = price;
        this.isSpecial = isSpecial;
        this.stock = stock;
        this.brandUri = brandUri;
        this.percent = percent;
        this.reviews = reviews;
        this.youtubeReviews = youtubeReviews;
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
    
    public boolean getIsSpecial(){
        return this.isSpecial;
    }   
    
    @XmlElement
    public void setIsSpecial(boolean isSpecial){
        this.isSpecial = isSpecial;
    }
    
    public boolean getStock(){
        return this.stock;
    }   
    
    @XmlElement
    public void setStock(boolean stock){
        this.stock = stock;
    }
    
    public String getBrandUri(){
        return this.brandUri;
    }   
    
    @XmlElement
    public void setBrandUri(String brandUri){
        this.brandUri = brandUri;
    }
    
    public String getPercent(){
        return this.percent;
    }   
    
    @XmlElement
    public void setPercent(String percent){
        this.percent = percent;
    }
    
    public List<String> getReviews(){
        return this.reviews;
    }   
    
    public void addReview(String review){
        this.reviews.add(review);
    }
    
    @XmlElementWrapper(name = "reviews")
    @XmlElement(name = "review")
    public void setReviews(List<String> reviews){
        this.reviews = reviews;
    }
    
    public List<String> getYoutubeReviews(){
        return this.youtubeReviews;
    }   
    
    public void addYoutubeReview(String youtubeReview){
        this.youtubeReviews.add(youtubeReview);
    }
    
    @XmlElementWrapper(name = "youtubeReviews")
    @XmlElement(name = "youtubeReview")
    public void setYoutubeReviews(List<String> youtubeReviews){
        this.youtubeReviews = youtubeReviews;
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
        sb.append(", isSpecial='").append(this.isSpecial).append('\'');
        sb.append(", brandUri='").append(this.brandUri).append('\'');
        sb.append(", percent='").append(this.percent).append('\'');
        sb.append(", reviews='").append(this.reviews.toString()).append('\'');
        sb.append(", youtubeReviews='").append(this.youtubeReviews.toString()).append('\'');
        sb.append('}');
        return sb.toString();
    }
    
    public String toStringEmail() {    
        final StringBuilder sb = new StringBuilder("");
        sb.append(this.shopName).append(" | ");
        sb.append(this.brand).append(" | ");
        sb.append(this.name).append(" | ");
        sb.append(this.price).append(" | ");
        sb.append(this.uri).append(" | ");
        
        //System.out.println(sb.toString());
        return sb.toString();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listadoproductos;

import listadoproductos.info.Brand;
import listadoproductos.info.Product;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.*;  
import listadoproductos.info.BrandProduct;
import listadoproductos.lib.Text;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.text.WordUtils;
import org.jsoup.Jsoup;

/**
 *
 * @author user
 */
public class LoadPage {
    //array con todos los productos de la tienda
    private static ArrayList<Product> productList = new ArrayList<>();
    //array con todas las marcas y sus equivalentes en texto
    private static ArrayList<Brand> brands = new ArrayList<Brand>();
    //numero de productos en la uri actualmente que se esta leyendo
    private static Integer productUriSize = 0;
    
    private static String baseUri = "";
    private static String baseName = "";
    private static String country = "";
    private static String currency = "EUR";
    private static String uri = "";
    private static String uriStringRemoveAfter = "";
    private static String name = "";
    
    private static String findProduct = "";
    
    private static String findName = "";
    
    private static String findPrice = "";
    
    private static String findUri = "";
    
    private static String findSpecial = "";
    private static String findSpecialPrice = "";
    
    private static String findNoStock = "";
    
    private static String findHasReviews = "";
    
    //Product Page
    private static String pPComments = "";
    
    private static Double pricePlus = 1.0;
    
    /** Remover cadenas a buscar en precio y nombre */
    private static List<String> rStrings = new ArrayList<String>();
    /** Remover entre valores en las cadenas a buscar en precio y nombre */
    private static List<String> rRegexStrings = new ArrayList<String>();
    
    private Currency currencies;

    public LoadPage() {
        
        baseUri = "";
        baseName = "";
        country = "";
        currency = "EUR";
        uri = "";
        uriStringRemoveAfter = "";
        name = "";

        findProduct = "";
        findName = "";
        findPrice = "";
        findUri = "";
        findSpecial = "";  
        findSpecialPrice = "";
        findNoStock = "";
        findHasReviews = "";
        
        //Product page
        pPComments = "";
        
        pricePlus = 1.0;
        
        productList = new ArrayList<>();
        brands = new ArrayList<>();
        
    }
    
    public void setBaseUri(String tBaseUri){
        baseUri = tBaseUri;  
    }
    public void setBaseName(String tBaseName){
        baseName = tBaseName;  
    }
    public void setCountry(String country){
        this.country = country;  
    }
    public void setCurrency(String currency){
        this.currency = currency;
    }
    
    public void setCurrencies(Currency cur){
        this.currencies = cur;
    }
    
    public void setPricePlus(String tPricePlus){
        pricePlus = Double.valueOf(tPricePlus);  
    }
    
    public void setUri(String tUri){
        uri = tUri;  
    }
    
    public void setUriRemoveStringAfter(String str){
        uriStringRemoveAfter = str;  
    }
    
    public void setName(String tName){
        name = tName;
    }
    public void setFindProduct(String tFindProduct){
        findProduct = tFindProduct;
    }    
    public void setFindName(String tFindName){
        findName = tFindName;
    }    
    public void setFindPrice(String tFindPrice){
        findPrice = tFindPrice;
    }    
    public void setFindUri(String tFindUri){
        findUri = tFindUri;
    }    
    public void setFindSpecial(String tFindSpecial){
        findSpecial = tFindSpecial;
    }         
    public void setFindSpecialPrice(String tFindSpecialPrice){
        findSpecialPrice = tFindSpecialPrice;
    }         
    public void setFindNoStock(String tFindNoStock){
        findNoStock = tFindNoStock;
    }         
    public void setFindHasReviews(String tFindHasReviews){
        findHasReviews = tFindHasReviews;
    }         

    public void setRStrings(List<String> array){
        rStrings = array;
    }
    /**
     * Establece las cadenas a eliminar entre valores. Debe de contener el 
     * asterisco entre las cadenas a buscar.
     * Ejemplo: (*)
     * @param array 
     */
    public void setRRegexStrings(List<String> array){
        rRegexStrings = array;
    }
    
    public void setBrands(ArrayList<Brand> brands){
        this.brands = brands;
    }
    
    public ArrayList<Product> getProductList(){
        return productList;
    }
    
    //Product Page
    public void setPPComments(String pPComments){
        this.pPComments = pPComments;
    }    
    
    /**
     * Devuelve el numero de productos en la uri actual que se esta leyendo
     * @return 
     */
    public Integer getProductUriSize(){
        return productUriSize;
    }
    
    public void load() throws IOException {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        //System.out.println(name + " " + baseURI);
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setDownloadImages(false);    
            webClient.addRequestHeader("Accept-Language" , "es-ES");
            //try { //tenia el try catch puesto para que no parara la ejecucion de la app cuando daba algun error en las webs, pero me di cuenta que entonces la siguiente ejecucion correcta enviaba emails con novedades cuando no lo son
                HtmlPage page = webClient.getPage(uri);
                //webClient.waitForBackgroundJavaScript(60 * 1000);
                System.out.println(page.getBaseURI());

                List<DomElement> products = page.getByXPath(findProduct);
                System.out.println("Articulos en la pagina " + products.size());
                productUriSize = products.size();
                for (DomElement product : products) {
                    //brand
                    System.out.print(getBrand(getProductName(product.getFirstByXPath(findName))).getName());

                    var myProduct = new Product();
                    myProduct.setShopName(baseName);
                    myProduct.setShopUri(baseUri);
                    myProduct.setShopCountry(country);
                    myProduct.setName(writeName(product.getFirstByXPath(findName)));
                    
                    Brand brand = getBrand(getProductName(product.getFirstByXPath(findName)));
                    myProduct.setBrand(brand.getName());
                    myProduct.setBrandUri(brand.getUri());
                    myProduct.setPercent(brand.getPercent(myProduct.getName()));
                    
                    myProduct.setUri(writeUri(product.getFirstByXPath(findUri)));
                    myProduct.setId();
                    myProduct.setIsSpecial(writeSpecial(product.getFirstByXPath(findSpecial)));
                    //System.out.println(myProduct.getIsSpecial());
                    if(myProduct.getIsSpecial()){
                        myProduct.setPrice(writePrice(product.getFirstByXPath(findSpecialPrice)));
                    }else{
                        myProduct.setPrice(writePrice(product.getFirstByXPath(findPrice)));
                    }
                    //System.out.println(myProduct.toString());
                    
                    myProduct.setStock(writeStock(product.getFirstByXPath(findNoStock)));
 
                    //comentarios
                    if(product.getFirstByXPath(findHasReviews) != null){
                        System.out.println("¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡tiene comentarios");
                        //cargamos la pagina del producto para ver los comentarios
                        //producto Page
                        try (final WebClient webClient2 = new WebClient(BrowserVersion.CHROME)) {
                            webClient2.getOptions().setThrowExceptionOnScriptError(false);
                            webClient2.getOptions().setThrowExceptionOnFailingStatusCode(false);
                            webClient2.getOptions().setCssEnabled(false);
                            webClient2.getOptions().setJavaScriptEnabled(false);
                            webClient2.getOptions().setDownloadImages(false);            
                            try {
                                HtmlPage productPage = webClient2.getPage(myProduct.getUri());
                                //webClient.waitForBackgroundJavaScript(60 * 1000);
                                System.out.println("¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡" + productPage.getBaseURI());

                                List<HtmlDivision> comments = productPage.getByXPath(pPComments);
                                System.out.println("¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡Comentarios en la pagina " + comments.size());  
                                //comentarios de la pagina del producto
                                for (HtmlDivision comment : comments) {
                                    //System.out.println(comment.getTextContent());
                                    myProduct.addReview(Text.toHTML(Text.html2text(comment.getTextContent()).trim()));
                                }
                            }catch (IOException e) {
                                /*me dio un error de conexion ssl en una web asi que mejor 
                                esto para no interrumpir la ejecucion completa de la app
                                */
                                System.out.println("Error: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                    
                    //youtubeReviews
                    for (BrandProduct brandProduct : brand.getProducts()) {
                        //miramos que el nombre del producto este dentro de
                        //algun producto de la marca
                        if(myProduct.getName().contains(Text.toHTML(brandProduct.getName()))){
                            if(!brandProduct.getPercent().isEmpty()){
                                myProduct.setPercent(brandProduct.getPercent());
                            }
                            //anadimos las reviews de youtube
                            myProduct.setYoutubeReviews(brandProduct.getYoutubeReviews());                        
                        }  
                    }  
                    /*
                    //miramos si este producto ya esta añadido antes por si 
                    //en la web han repetido el producto con la misma url
                    boolean productExists = false;
                    for (Product productInList : productList){
                        if (productInList.equals(myProduct)){
                            productExists = true;
                            break;
                        }
                    }   
                    if(!productExists){
                        productList.add(myProduct);
                    }
                    //if(!productList.contains(myProduct))*/ 
                    productList.add(myProduct);

                    System.out.println(""); 
                }     
            //} catch (IOException e) {
                /*me dio un error de conexion ssl en una web asi que mejor 
                esto para no interrumpir la ejecucion completa de la app
                */
            //    System.out.println("Error: " + e.getMessage());
            //    e.printStackTrace();
            //}
                   
 
        }           
    }
    
    /**
     * Obtiene el nombre del producto sin realizar ningun cambio en el texto
     * @param eName
     * @return 
     */
    public String getProductName(HtmlElement eName){
        String var = "";
        if (eName != null){
            String cellText = "";
            if(eName.getVisibleText().equals("")){
                cellText = eName.getTextContent();
            }else{
                cellText = eName.getVisibleText();
            }
            var = cellText.trim();
        }
        return var;        
    }
    
    /**
     * Busca el nombre y le quita las palabras a borrar y lo formatea a html
     * @param eName
     * @return 
     */
    public String writeName(HtmlElement eName){ 
        String var = "";
        if (eName != null){
            String cellText = "";
            if(eName.getVisibleText().equals("")){
                cellText = eName.getTextContent();
            }else{
                cellText = eName.getVisibleText();
            }
            var = Text.toHTML(Text.html2text(removeStrings(WordUtils.capitalizeFully(cellText))).trim());
            System.out.print(" || " + var); 
        }
        return var;
    }    
    /**
     * Dejamos el precio correctamente escrito, simbolo del punto y del euro
     * mas espacios en blancos etc...
     * @param ePrice
     * @return 
     */
    public String writePrice(DomElement ePrice){  
        String var = "";
        if(ePrice != null){
            var = Text.html2text(ePrice.getTextContent()).replace(",", ".").trim();

            //Si la moneda no es el Euro, eliminar la moneda, calcular el precio
            //en euros, y añadirle el simbolo del euro
            if(var.contains("£")){
                var = var.replace("£", "");
                Float floatPrice = Float.valueOf(var);
                //System.out.println(floatPrice);
                Double doublePricePlus = floatPrice * currencies.get(currency);
                //System.out.println(doublePricePlus);
                DecimalFormat df2 = new DecimalFormat("#.##");
                doublePricePlus = Double.valueOf(df2.format(doublePricePlus));   
                var = doublePricePlus.toString() + " €";
            }

            //buscamos que el precio este pegado al euro para separarlos
            String regex = "\\d\\p{Sc}"; //cualquier moneda, \p{Sc} euro
            //System.out.print(" precio:" + var);
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(var);
            int indexEuro = 1;
            while (matcher.find())
            {
                /*
                System.out.println("+++++++++++ : " + var);
                System.out.print("Start index: " + matcher.start());
                System.out.print(" End index: " + matcher.end() + " ");
                System.out.println(" : " + matcher.group());
                */

                StringBuffer buf = new StringBuffer(var);
                buf.replace(matcher.start() + indexEuro, matcher.end() + indexEuro - 1, " €"); 
                var = buf.toString();
                //System.out.println(buf);
                indexEuro++;
            }        
            //si hay que añadir un plus al precio
            System.out.print("|| price normal: " + var + " pricePlus:" + pricePlus);
            if(pricePlus > 1.0){
                regex = "\\d+.\\d+";
                //System.out.print(" precio:" + var);
                pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(var);
                String finalVar = "";
                while (matcher.find())
                {
                    /*
                    System.out.println("+++++++++++ : " + var);
                    System.out.print("Start index: " + matcher.start());
                    System.out.print(" End index: " + matcher.end() + " ");
                    System.out.println(" : " + matcher.group());
                    */
                    String strPrice = var.substring(matcher.start(), matcher.end());
                    //System.out.println(strPrice);
                    Float floatPrice = Float.valueOf(strPrice);
                    //System.out.println(floatPrice);
                    Double doublePricePlus = floatPrice * 1.21;
                    //System.out.println(doublePricePlus);
                    DecimalFormat df2 = new DecimalFormat("#.##");
                    doublePricePlus = Double.valueOf(df2.format(doublePricePlus));
                    //System.out.println("double : " + doublePricePlus);

                    finalVar += doublePricePlus.toString() + " € ";
                }          
                var = finalVar;
            }
            //System.out.print(" || " + ePrice.getVisibleText());    
        }
        System.out.println(" || " + var);
        return var;
    }

    public String writeUri(HtmlElement eUri){
        String var = eUri.getAttribute("href");
        if(!uriStringRemoveAfter.equals(""))
            var = var.split(uriStringRemoveAfter)[0];
        
        System.out.print(" || " + var);   
        return var;
    }

    public boolean writeSpecial(HtmlElement e){
        boolean var = false;
        if (e != null && !e.getTextContent().equals("")){
            System.out.print(" || OFERTA ");
            var = true;
        }
        return var;
    }   
    
    /**
     * Si el elemento existe o contiene algo entonces devuelve false de que
     * no hay stock de este producto
     * @param e
     * @return 
     */
    public boolean writeStock(HtmlElement e){
        boolean var = true;
        if (e != null && !e.getTextContent().equals("")){
            System.out.print(" || NO STOCK ");
            var = false;
        }
        return var;
    }    
    

    /**
     * Elimina de una cadena las cadenas del tag <remove> en cada page y tambien
     * elimina las marcas
     * @param str
     * @return 
     */
    public String removeStrings(String str){     
        //Eliminamos tambien las marcas
        for (Brand brand : this.brands) {
            //System.out.print(brand.getName() + " : ");
            for (String equals : brand.getEquals()) {
                if(str.toLowerCase().contains(equals.toLowerCase())){
                    //str = str.replace(equals, "");
                    str = Pattern.compile(
                                equals, 
                                Pattern.LITERAL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)
                                .matcher(str)
                                .replaceAll(Matcher.quoteReplacement(""));                      
                }
                //System.out.print(" @@ " + equals);
            }
            //System.out.println("");
        }         
        //cadenas remove de la tienda
        for( String oneItem : rStrings ) {
            //str = str.replace(oneItem, "");
            str = Pattern.compile(
                        oneItem, 
                        Pattern.LITERAL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)
                        .matcher(str)
                        .replaceAll(Matcher.quoteReplacement(""));             
        }
        //eliminamos cadenas con expresiones regulares
        for( String oneItem : rRegexStrings ) {
            //str = str.replace(oneItem, "");
            str = str.replaceAll(oneItem, "");
        }
          
        
        return str;
    }
    
    /**
     * Obtenemos la marca del producto si lo contiene en el nombre, si no,
     * establece por defecto el nombre que hay en el tag xml de la pagina
     * @param name
     * @return 
     */
    public Brand getBrand(String productName){
        for (Brand brand : this.brands) {
            //System.out.print(brand.getName() + " : ");
            for (String equals : brand.getEquals()) {
                if(productName.toLowerCase().contains(equals.toLowerCase())){
                    //System.out.print(" @@ Marca: " + equals);
                    return brand;
                }
                //System.out.print(" @@ " + equals);
            }
            //System.out.println("");
        }      
        Brand brand = new Brand();
        brand.setName(this.name);
        return brand;
    }
}

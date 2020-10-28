/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listadoproductos;

import listadoproductos.info.Product;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.*;  
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;

/**
 *
 * @author user
 */
public class LoadPage {
    //array con todos los productos de la pagina
    private static ArrayList<Product> productList = new ArrayList<>();
    //array con todas las marcas y sus equivalentes en texto
    private static ArrayList<Brand> brands = new ArrayList<Brand>();
    
    private static String baseUri = "";
    private static String baseName = "";
    private static String country = "";
    private static String uri = "";
    private static String name = "";
    
    private static String findProduct = "";
    
    private static String findName = "";
    
    private static String findPrice = "";
    
    private static String findUri = "";
    
    private static String findSpecial = "";
    private static String findSpecialPrice = "";
    
    private static Double pricePlus = 1.0;
    
    /** Remover cadenas a buscar en precio y nombre */
    private static List<String> rStrings = new ArrayList<String>();;

    public LoadPage() {
        
        baseUri = "";
        baseName = "";
        country = "";
        uri = "";
        name = "";

        findProduct = "";
        findName = "";
        findPrice = "";
        findUri = "";
        findSpecial = "";  
        findSpecialPrice = "";
        
        pricePlus = 1.0;
        
        productList = new ArrayList<Product>();
        brands = new ArrayList<Brand>();
        
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
    public void setPricePlus(String tPricePlus){
        pricePlus = Double.valueOf(tPricePlus);  
    }
    
    public void setUri(String tUri){
        uri = tUri;  
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

    public void setRStrings(List<String> array){
        rStrings = array;
    }
    
    public void setBrands(ArrayList<Brand> brands){
        this.brands = brands;
    }
    
    public ArrayList<Product> getProductList(){
        return productList;
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
            try {
                HtmlPage page = webClient.getPage(uri);
                //webClient.waitForBackgroundJavaScript(60 * 1000);
                System.out.println(page.getBaseURI());

                List<HtmlDivision> products = page.getByXPath(findProduct);
                System.out.println("Articulos en la pagina " + products.size());
                for (HtmlDivision product : products) {
                    //brand
                    System.out.print(getBrand(getProductName(product.getFirstByXPath(findName))));

                    var myProduct = new Product();
                    myProduct.setShopName(baseName);
                    myProduct.setShopUri(baseUri);
                    myProduct.setShopCountry(country);
                    myProduct.setName(writeName(product.getFirstByXPath(findName)));
                    myProduct.setBrand(getBrand(getProductName(product.getFirstByXPath(findName))));
                    myProduct.setUri(writeUri(product.getFirstByXPath(findUri)));
                    myProduct.setId();
                    myProduct.setIsSpecial(writeSpecial(product.getFirstByXPath(findSpecial)));
                    if(myProduct.getIsSpecial()){
                        myProduct.setPrice(writePrice(product.getFirstByXPath(findSpecialPrice)));
                    }else{
                        myProduct.setPrice(writePrice(product.getFirstByXPath(findPrice)));
                    }
                    //System.out.println(myProduct.toString());

                    productList.add(myProduct);

                    System.out.println(""); 
                }     
            } catch (IOException e) {
                /*me dio un error de conexion ssl en una web asi que mejor 
                esto para no interrumpir la ejecucion completa de la app
                */
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
                   
 
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
            var = toHTML(html2text(removeStrings(cellText)).trim());
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
    public String writePrice(HtmlElement ePrice){  
        String var = html2text(ePrice.getTextContent()).replace(",", ".").trim();
        //buscamos que el precio este pegado al euro para separarlos
        String regex = "\\d\\p{Sc}";
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
        System.out.println(" || " + var);
        return var;
    }
    public String writeUri(HtmlElement eUri){        
        String var = eUri.getAttribute("href");
        System.out.print(" || " + var);   
        return var;
    }

    public boolean writeSpecial(HtmlElement e){
        boolean var = false;
        if (e != null){
            System.out.print(" || OFERTA ");
            var = true;
        }
        return var;
    }    
    
    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    } 
    /**
     * parsea caracteres especiales
     * @param str
     * @return 
     */
    public String toHTML(String str) {
        String out = "";
        for (char c: str.toCharArray()) {
            if(!Character.isLetterOrDigit(c))
                out += String.format("&#x%x;", (int)c);
            else
                out += String.format("%s", c);

        }
        return out;
    }
    /**
     * Elimina de una cadena las cadenas del tag <remove> en cada page y tambien
     * elimina las marcas
     * @param str
     * @return 
     */
    public String removeStrings(String str){     
        
        for( String oneItem : rStrings ) {
            //str = str.replace(oneItem, "");
            str = Pattern.compile(
                        oneItem, 
                        Pattern.LITERAL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)
                        .matcher(str)
                        .replaceAll(Matcher.quoteReplacement(""));             
        }
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
        
        return str;
    }
    
    /**
     * Obtenemos la marca del producto si lo contiene en el nombre, si no,
     * establece por defecto el nombre que hay en el tag xml de la pagina
     * @param name
     * @return 
     */
    public String getBrand(String productName){
        for (Brand brand : this.brands) {
            //System.out.print(brand.getName() + " : ");
            for (String equals : brand.getEquals()) {
                if(productName.toLowerCase().contains(equals.toLowerCase())){
                    //System.out.print(" @@ Marca: " + equals);
                    return brand.getName();
                }
                //System.out.print(" @@ " + equals);
            }
            //System.out.println("");
        }        
        return this.name;
    }
}

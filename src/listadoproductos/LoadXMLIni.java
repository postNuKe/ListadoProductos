/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listadoproductos;

import listadoproductos.info.Brand;
import listadoproductos.info.Product;
import listadoproductos.info.Listado;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import com.sun.xml.bind.marshaller.*;
import java.io.IOException;
import java.io.Writer;
import listadoproductos.info.BrandProduct;

/**
 *
 * @author user
 */
public final class LoadXMLIni {
    private static String LISTADO_PRODUCTOS_XML = "./listado.xml";
    private static Date date = new Date();
    private static ArrayList<String> listPages = new ArrayList<String>();
    private static ArrayList<Brand> brands = new ArrayList<Brand>();
    
    public LoadXMLIni(String pathListado){
        try {       
            //cargamos las monedas
            Currency currencies = new Currency();
            setPathListado(pathListado);
            ArrayList<Product> productList = new ArrayList<>();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            //PrintStream fileOut = new PrintStream("./ofertas/" + formatter.format(date) + ".txt");
            //System.setOut(fileOut);        
            formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");          
            System.out.println(formatter.format(date)); 
            
            //Path path = Paths.get(pathListado); //path.getParent()
            //String path = new File("").getAbsolutePath();
            String path = new File(ListadoProductos.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
            File inputFile = new File( path + "/config.xml");
            if(!inputFile.exists()){//si no encuentra el config puede pues que lo coja del local
                inputFile = new File("./config.xml");
            }
            System.out.println("ruta config: " + inputFile.getPath());
            
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            
            //MARCAS
            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nBrands = doc.getElementsByTagName("brand");
            for (int temp = 0; temp < nBrands.getLength(); temp++) {
                Node nBrand = nBrands.item(temp);
                if (nBrand.getNodeType() == Node.ELEMENT_NODE) {
                    Element eBrand = (Element) nBrand;
                    Brand brand = new Brand();
                    brand.setName(eBrand.getElementsByTagName("name").item(0).getTextContent());
                    brand.setUri(eBrand.getElementsByTagName("website").item(0).getTextContent());
                    brand.setPercent(eBrand.getElementsByTagName("percent").item(0).getTextContent());
                    //System.out.println("BRAND: " + brand.getName());
                    
                    NodeList nEquals = eBrand.getElementsByTagName("equals");
                    for (int temp2 = 0; temp2 < nEquals.getLength(); temp2++) {
                        Node nEqual = nEquals.item(temp2);
                        if (nEqual.getNodeType() == Node.ELEMENT_NODE) {
                            Element eEqual = (Element) nEqual;
                            brand.addEquals(eEqual.getTextContent());
                            //System.out.print("||" + eEqual.getTextContent());
                        }
                    }
                    
                    //collection, productos marcados con porcentaje concreto,
                    //con youtubes etc...
                    Node nCollection = eBrand.getElementsByTagName("collection").item(0);                    
                    if(nCollection != null && nCollection.getNodeType() == Node.ELEMENT_NODE){
                        Element eProduct = (Element) nCollection;
                        NodeList nProducts = eProduct.getElementsByTagName("product");
                        for (int i = 0; i < nProducts.getLength(); i++) {
                            Node nProduct = nProducts.item(i);
                            if (nProduct.getNodeType() == Node.ELEMENT_NODE) {
                                BrandProduct brandProduct = new BrandProduct();
                                eProduct = (Element) nProduct;
                                
                                brandProduct.setName(eProduct.getAttribute("name"));
                                Node nPercent = eProduct.getElementsByTagName("percent").item(0);
                                if(nPercent != null){
                                    if (nPercent.getNodeType() == Node.ELEMENT_NODE) {
                                        brandProduct.setPercent(nPercent.getTextContent());
                                    }
                                }                                
                                NodeList nReviews = eProduct.getElementsByTagName("youtubeReview");
                                for (int j = 0; j < nReviews.getLength(); j++) {
                                    Node nReview = nReviews.item(j);
                                    if (nReview.getNodeType() == Node.ELEMENT_NODE) {
                                        Element eReview = (Element) nReview;
                                        brandProduct.addYoutubeReviews(eReview.getTextContent());
                                        //System.out.print("||" + eEqual.getTextContent());
                                    }
                                }
                                brand.addProduct(brandProduct);
                                System.out.println(brandProduct.toString());
                            }
                        }
                    }                 
                    
                    brands.add(brand);
                    //System.out.println("");
                }
            } 
            /*
            System.out.println("++++++++++++++");
            
            for (Brand brand : brands) {
                System.out.print(brand.getName() + " : ");
                for (String equals : brand.getEquals()) {
                    System.out.print(" @@ " + equals);
                }
                System.out.println("");
            }
            */
            
            
            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nShops = doc.getElementsByTagName("shop");

            for (int temp = 0; temp < nShops.getLength(); temp++) {
                Node nShop = nShops.item(temp);
                //System.out.println("\nCurrent Element :" + nShop.getNodeName());

                if (nShop.getNodeType() == Node.ELEMENT_NODE) {
                   Element eShop = (Element) nShop;
                   /*
                   System.out.println("Shop name : " 
                      + eShop.getAttribute("name"));
                   System.out.println("BaseUri : " 
                      + eShop
                      .getElementsByTagName("baseUri")
                      .item(0)
                      .getTextContent());
                   */
                    LoadPage lPage = new LoadPage();
                    lPage.setBrands(brands);
                    lPage.setBaseName(eShop.getAttribute("name"));
                    lPage.setCountry(eShop.getAttribute("country"));
                    lPage.setCurrency(eShop.getAttribute("currency"));
                    lPage.setCurrencies(currencies);
                    lPage.setBaseUri(eShop.getElementsByTagName("baseUri").item(0).getTextContent());
                    lPage.setUriRemoveStringAfter(eShop.getElementsByTagName("uriRemoveStringAfter").item(0).getTextContent());
                    Node pricePlus = eShop.getElementsByTagName("pricePlus").item(0);
                    if(pricePlus != null){
                        if (pricePlus.getNodeType() == Node.ELEMENT_NODE) {
                            lPage.setPricePlus(pricePlus.getTextContent());
                        }
                    }else lPage.setPricePlus("1.0");

                    //FIND PRODUCTOS
                    //definimos los tags a encontrar en la web para cada pagina
                    Node nFind = eShop.getElementsByTagName("find").item(0);
                    if(nFind.getNodeType() == Node.ELEMENT_NODE){
                        Element eFind = (Element) nFind;

                        lPage.setFindProduct(eFind.getElementsByTagName("product").item(0).getTextContent());                    
                        lPage.setFindName(eFind.getElementsByTagName("name").item(0).getTextContent());                
                        lPage.setFindPrice(eFind.getElementsByTagName("price").item(0).getTextContent());        
                        lPage.setFindUri(eFind.getElementsByTagName("uri").item(0).getTextContent());                  
                        lPage.setFindSpecial(eFind.getElementsByTagName("special").item(0).getTextContent());
                        lPage.setFindSpecialPrice(eFind.getElementsByTagName("specialPrice").item(0).getTextContent());
                        lPage.setFindNoStock(eFind.getElementsByTagName("noStock").item(0).getTextContent());
                        lPage.setFindHasReviews(eFind.getElementsByTagName("hasReviews").item(0).getTextContent());
                        //Product Page
                        lPage.setPPComments(eFind.getElementsByTagName("pPComments").item(0).getTextContent());
                        
                        Node nProduct = eFind.getElementsByTagName("special").item(0);
                        Element eProduct = (Element) nProduct;
                    }

                    //remove cadenas de texto
                    NodeList nRemoves = eShop.getElementsByTagName("remove");
                    List<String> rStrings = new ArrayList<>();
                    for (int temp2 = 0; temp2 < nRemoves.getLength(); temp2++) {
                        Node nRemove = nRemoves.item(temp2); 
                        rStrings.add(nRemove.getTextContent());
                    }
                    lPage.setRStrings(rStrings);
                    
                    //remove Between cadenas de texto
                    nRemoves = eShop.getElementsByTagName("removeRegex");
                    rStrings = new ArrayList<>();
                    for (int temp2 = 0; temp2 < nRemoves.getLength(); temp2++) {
                        Node nRemove = nRemoves.item(temp2); 
                        rStrings.add(nRemove.getTextContent());
                    }
                    lPage.setRRegexStrings(rStrings);

                    //PAGES DENTRO DE ESTA SHOP
                    NodeList nPages = eShop.getElementsByTagName("page");
                    for (int temp2 = 0; temp2 < nPages.getLength(); temp2++) {
                        Node nPage = nPages.item(temp2);
                        //System.out.println("\nCurrent Element :" + nPage.getNodeName()); 
                        if (nShop.getNodeType() == Node.ELEMENT_NODE) {
                            Element ePage = (Element) nPage;
                            lPage.setName(ePage.getAttribute("name"));
                            //miramos si la pagina es autoincrement
                            if(ePage.getAttribute("autoincrement") != null){
                                //System.out.println(ePage.getAttribute("name"));
                                boolean repeat = true;
                                Integer increment = 1;
                                while(repeat){
                                    String tempUri = ePage.getTextContent()
                                            .concat(
                                                    ePage.getAttribute("autoincrement")
                                            ).concat(String.valueOf(increment));
                                    lPage.setUri(tempUri);
                                    //System.out.println(ePage.getTextContent());
                                    lPage.load(); 
                                    //si no ha encontrado mas productos pues
                                    //paramos el while                                    
                                    if(lPage.getProductUriSize() == 0){
                                        repeat = false;
                                    }else{
                                        listPages.add(tempUri);  
                                        increment++;
                                    }                                    
                                }
                            }else{
                                //System.out.println(ePage.getAttribute("name"));
                                lPage.setUri(ePage.getTextContent());
                                //System.out.println(ePage.getTextContent());
                                lPage.load(); 
                                listPages.add(ePage.getTextContent());
                            }
                        }
                    }
                    productList.addAll(lPage.getProductList());
                   
                }
            }
            // create productList, assign products
            var listado = new Listado();
            listado.setDate(formatter.format(date));
            listado.setProductList(productList);   
            listado.setListPages(listPages);
            
            // create JAXB context and instantiate marshaller
            var context = JAXBContext.newInstance(Listado.class);
            var m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(CharacterEscapeHandler.class.getName(),
                new CharacterEscapeHandler() {
                    @Override
                    public void escape(char[] ac, int i, int j, boolean flag,
                            Writer writer) throws IOException {
                        writer.write(ac, i, j);
                    }
                });            

            // Write to System.out
            //m.marshal(listado, System.out);

            // Write to File
            m.marshal(listado, new File(LISTADO_PRODUCTOS_XML));  
            
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
    
    public void setPathListado(String path){
        this.LISTADO_PRODUCTOS_XML = path;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listadoproductos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import listadoproductos.info.Product;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Compara 2 archivos xml con productos, para ver cuales son nuevos productos
 * y cuales son nuevas ofertas
 * @author user
 */
public class Compare2XML {
    
    
    private static ArrayList<Product> newProducts = new ArrayList<Product>();
    private static ArrayList<Product> specialProducts = new ArrayList<Product>();
    
    
    public Compare2XML(File newXML, File oldXML) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        
        newProducts = new ArrayList<>();
        specialProducts = new ArrayList<>();
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        
        //nuevo documento
        XPath xPath = XPathFactory.newInstance().newXPath();
        Document docNewXML = dBuilder.parse(newXML);
        String expression = "/listado/productList//product";
        NodeList nDocNewProductsList = 
                (NodeList) xPath.compile(expression)
                        .evaluate(docNewXML, XPathConstants.NODESET);
        
        //viejo documento
        Document docOldXML = dBuilder.parse(oldXML);
        
        //revisamos todos los productos del nuevo documento a ver si hay cambios
        //con el viejo
        for (int i = 0; i < nDocNewProductsList.getLength(); i++) {
            Node nDocNewProduct = nDocNewProductsList.item(i);
            if (nDocNewProduct.getNodeType() == Node.ELEMENT_NODE) {
                Element eDocNewProduct = (Element) nDocNewProduct;
                expression = "/listado/productList/product[@id=" + "'" + eDocNewProduct.getAttribute("id") + "'" + "][1]";
                //System.out.println(eDocNewProduct.getAttribute("id"));
                Element nDocOldProduct = (Element) xPath.compile(expression).evaluate(docOldXML, XPathConstants.NODE);
                //existe el producto en el viejo
                if(nDocOldProduct != null){
                    //System.out.print(" + SI");
                    boolean newIsSpecial = Boolean.parseBoolean(eDocNewProduct.getElementsByTagName("isSpecial").item(0).getTextContent());
                    //System.out.println(eDocNewProduct.getElementsByTagName("isSpecial").item(0).getTextContent());
                    //miramos que este en oferta y que en el viejo no lo estuviera
                    if(newIsSpecial){
                        //System.out.println(" + NUEVO EN OFERTA");
                        Element eDocOldProduct = (Element) nDocOldProduct;
                        boolean oldIsSpecial = Boolean.parseBoolean(eDocOldProduct.getElementsByTagName("isSpecial").item(0).getTextContent());
                        //si en el nuevo esta en oferta y en el viejo no
                        if(newIsSpecial != oldIsSpecial){
                            specialProducts.add(addProductToArray(eDocNewProduct));                          
                        }
                    }                  

                }else{//nuevo producto
                    newProducts.add(addProductToArray(eDocNewProduct));
                    //System.out.println(" ---------------- NO");
                }      
            }            
        }
        
    }
    
    private Product addProductToArray(Element e){
        Product p = new Product();
        p.setBrand(e
                  .getElementsByTagName("brand")
                  .item(0)
                  .getTextContent());
        p.setIsSpecial(Boolean.parseBoolean(e
                  .getElementsByTagName("isSpecial")
                  .item(0)
                  .getTextContent()));
        p.setName(e
                  .getElementsByTagName("name")
                  .item(0)
                  .getTextContent());
        p.setPrice(e
                  .getElementsByTagName("price")
                  .item(0)
                  .getTextContent());
        p.setShopCountry(e
                  .getElementsByTagName("shopCountry")
                  .item(0)
                  .getTextContent());
        p.setShopName(e
                  .getElementsByTagName("shopName")
                  .item(0)
                  .getTextContent());
        p.setShopUri(e
                  .getElementsByTagName("shopUri")
                  .item(0)
                  .getTextContent());
        p.setUri(e
                  .getElementsByTagName("uri")
                  .item(0)
                  .getTextContent());
        p.setId();
        return p;       
    }
    
    /**
     * Devuelve todos los nuevos productos
     * @return 
     */
    public ArrayList<Product> getNewProducts(){
        return newProducts;
    }
    
    /**
     * Devuelve todos las nuevas ofertas
     * @return 
     */
    public ArrayList<Product> getSpecialProducts(){
        return specialProducts;
    }
    
    
    
    
}

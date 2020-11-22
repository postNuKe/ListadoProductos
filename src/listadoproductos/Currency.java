/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listadoproductos;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Obtiene en euros el valor de diferentes monedas de la web http://www.floatrates.com/feeds.html
 * @author user
 */
public class Currency {
    
    private static Map<String, String> initCurrencies;
    static {
        initCurrencies = new HashMap<>();
        initCurrencies.put("GBP", "http://www.floatrates.com/daily/gbp.xml");
        //initCurrencies.put("USD", "http://www.floatrates.com/daily/usd.xml");
    }
    
    private Map<String, Double> currencies = new HashMap<>();
    
    
    public Currency() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        
        //String expression = "//*[1][targetCurrency='EUR']/parent::item";
        //String expression = "/channel/item/targetCurrency='EUR'";
        String expression = "//*[targetCurrency='EUR'][1]";
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        XPath xPath = XPathFactory.newInstance().newXPath();  
        
        for (String cur : initCurrencies.keySet()) {
            Document docXML = dBuilder.parse(new URL(initCurrencies.get(cur)).openStream());

            NodeList nDocXML = 
                    (NodeList) xPath.compile(expression)
                            .evaluate(docXML, XPathConstants.NODESET);    

            for (int i = 0; i < nDocXML.getLength(); i++) {
                //System.out.println("entra bucle");
                Node nDocItem = nDocXML.item(i);
                if (nDocItem.getNodeType() == Node.ELEMENT_NODE) {
                    Element eDocEmail = (Element) nDocItem;
                    //System.out.println(eDocEmail.getElementsByTagName("exchangeRate").item(0).getTextContent());  
                    currencies.put(cur, Double.valueOf(eDocEmail.getElementsByTagName("exchangeRate").item(0).getTextContent()));
                    System.out.println(cur + " : " + currencies.get(cur));
                    
                }            
            } 
        }
        currencies.put("EUR", 1.00);
        //printCurrencies();

    }
    
    /**
     * Imprime en pantalla las diferentes monedas y sus equivalentes en euro
     */
    public void printCurrencies(){
        System.out.println("------- CURRENCIES --------");
        currencies.entrySet().forEach((entry) -> {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + " : " + value);
        });        
    }
    
    /**
     * Obtiene el Map de monedas
     * @return 
     */
    public Map getCurrencies(){
        return currencies;
    }
    
    /**
     * Devuelve el valor en euros de una moneda dada
     * @param key
     * @return 
     */
    public Double get(String key){
        if(currencies.get(key) != null)
            return currencies.get(key);
        return 0.0;
    }
    
}

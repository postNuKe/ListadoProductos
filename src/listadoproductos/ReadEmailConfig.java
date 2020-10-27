/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listadoproductos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
 * Obtiene los emails del archivo emails.xml
 * @author user
 */
public class ReadEmailConfig {
    private static final String XML_EMAILS = "emails.xml";
    private static ArrayList<String> aEmails = new ArrayList<String>();
    
    public ReadEmailConfig(String path) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        XPath xPath = XPathFactory.newInstance().newXPath();
        File xmlFile = new File(path.concat(XML_EMAILS));
        if(!xmlFile.exists()){//si no encuentra el config puede pues que lo coja del local
            xmlFile = new File("./" + XML_EMAILS);
        }        
        Document docXML = dBuilder.parse(xmlFile);
        String expression = "/root/email";
        NodeList nDocXML = 
                (NodeList) xPath.compile(expression)
                        .evaluate(docXML, XPathConstants.NODESET);    
        
        for (int i = 0; i < nDocXML.getLength(); i++) {
            Node nDocEmail = nDocXML.item(i);
            if (nDocEmail.getNodeType() == Node.ELEMENT_NODE) {
                Element eDocEmail = (Element) nDocEmail;
                System.out.println(eDocEmail.getAttribute("id"));  
                aEmails.add(eDocEmail.getAttribute("id"));
            }            
        }
        
    }
    
    public ArrayList<String> getEmails(){
        return aEmails;
    }
    
    public String getEmailsToString(){
        StringBuilder sb = new StringBuilder();
        for (String s : aEmails)
        {
            sb.append(s);
            sb.append(",");
        }

        System.out.println(sb.toString()); 
        return sb.toString();
    }
    
}

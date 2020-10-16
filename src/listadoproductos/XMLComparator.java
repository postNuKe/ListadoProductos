/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listadoproductos;

import java.io.BufferedReader; 
import java.io.FileInputStream; 
import java.io.FileNotFoundException; 
import java.io.IOException; 
import java.io.InputStreamReader; 
import java.io.Reader; 
import java.util.List; 
import org.custommonkey.xmlunit.DetailedDiff; 
import org.custommonkey.xmlunit.Diff; 
import org.custommonkey.xmlunit.Difference; 
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;


/**
 *
 * @author user
 */
public class XMLComparator {
    public XMLComparator(String file1, String file2) throws FileNotFoundException, SAXException, IOException { 
        // reading two xml file to compare in Java program 
        FileInputStream fis1 = new FileInputStream(file1); 
        FileInputStream fis2 = new FileInputStream(file2); 

        // using BufferedReader for improved performance 
        BufferedReader source = new BufferedReader(new InputStreamReader(fis1)); 
        BufferedReader target = new BufferedReader(new InputStreamReader(fis2)); 

        //configuring XMLUnit to ignore white spaces 
        XMLUnit.setIgnoreWhitespace(true); 
        //comparing two XML using XMLUnit in Java 
        List differences = compareXML(source, target); 
        //showing differences found in two xml files 
        printDifferences(differences); 
    } 
    
    public static List compareXML(Reader source, Reader target) throws SAXException, IOException{ 
        //creating Diff instance to compare two XML files 
        Diff xmlDiff = new Diff(source, target); 
        
        //for getting detailed differences between two xml files 
        DetailedDiff detailXmlDiff = new DetailedDiff(xmlDiff); 
        return detailXmlDiff.getAllDifferences(); 
    } 
    
    public static void printDifferences(List differences){ 
        int totalDifferences = differences.size(); 
        System.out.println("==============================="); 
        System.out.println("Total differences : " + totalDifferences); 
        System.out.println("================================"); 
        for(Object difference : differences){ 
            System.out.println(difference); 
        } 
    }

//Read more: https://javarevisited.blogspot.com/2017/04/how-to-compare-two-xml-files-in-java.html#ixzz6adqRIGAp    
}

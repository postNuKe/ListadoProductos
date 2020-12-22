/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listadoproductos.lib;

import org.jsoup.Jsoup;

/**
 *
 * @author user
 */
public class Text {
    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    } 
    /**
     * parsea caracteres especiales
     * @param str
     * @return 
     */
    public static String toHTML(String str) {
        String out = "";
        for (char c: str.toCharArray()) {
            if(!Character.isLetterOrDigit(c))
                out += String.format("&#x%x;", (int)c);
            else
                out += String.format("%s", c);

        }
        return out;
    }    
}

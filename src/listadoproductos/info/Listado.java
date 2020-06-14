/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listadoproductos.info;

/*
<listado>
    <productList>
        <product></product>
        ...
    </productList>
    <date></date>
</listado>

*/


import java.util.ArrayList;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import listadoproductos.AdapterCDATA;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Listado {

    // XmLElementWrapper generates a wrapper element around XML representation
    @XmlElementWrapper(name = "productList")
    // XmlElement sets the name of the entities
    @XmlElement(name = "product")
    private ArrayList<Product> productList;
    // XmLElementWrapper generates a wrapper element around XML representation
    @XmlElementWrapper(name = "pages")
    // XmlElement sets the name of the entities
    @XmlElement(name = "page")    
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private ArrayList<String> listPages;
    private String date;

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    public ArrayList<Product> getProductsList() {
        return productList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setListPages(ArrayList<String> list) {
        this.listPages = list;
    }  
    
    public ArrayList<String> getListsPages() {
        return listPages;
    }    
}

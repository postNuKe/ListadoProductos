/*
 * Listado productos en oferta para el envio de emails
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

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ListadoSpecials {

    // XmLElementWrapper generates a wrapper element around XML representation
    @XmlElementWrapper(name = "productList")
    // XmlElement sets the name of the entities
    @XmlElement(name = "product")
    private ArrayList<ProductSpecial> productList;

    private String date;

    public void setProductList(ArrayList<ProductSpecial> productList) {
        this.productList = productList;
    }

    public ArrayList<ProductSpecial> getProductsList() {
        return productList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    } 
}

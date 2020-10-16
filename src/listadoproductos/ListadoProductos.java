/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listadoproductos;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import listadoproductos.info.Listado;
import org.springframework.util.FileCopyUtils;

/**
 *
 * @author user
 */
public class ListadoProductos {
    private static String LISTADO_PRODUCTOS_NEW = "./listado_new_products.xml";
    private static String LISTADO_PRODUCTOS_NEW_SPECIAL = "./listado_new_products_special.xml";
    
    /** nombre del fListado anterior para comparar con el nuevo */
    private static String LISTADO_PRODUCTOS_PREVIOUS_XML = "listado_previous.xml";
    
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {   
        
        System.out.println("Number of Command Line Argument = "+args.length);

        for(int i = 0; i< args.length; i++) {
            System.out.println(String.format("Command Line Argument %d is %s", i, args[i]));
        }
        /*
        if(args.length < 5){
            System.out.println("Error: Insuficientes parámetros. \"$java -jar \'ListadoProductos.jar\' fListado.xml /remote_path/listado.xml server username password\"");
            System.exit(0);
        }
        */
        //String localListado = "./listado.xml";
        //String remoteListado = "/public_html/data/listado.xml";
        
        String localListado = args[0];
        
        
        //new LoadXMLIni(localListado);
        
        //averiguamos el path local para empezar a comparar los archivos
        Path path = Paths.get(localListado);
        File fListado = new File(localListado);
        String localListadoPrevious = path.getParent().toString().concat("/").concat(LISTADO_PRODUCTOS_PREVIOUS_XML);
        File fListadoPrevious = new File(localListadoPrevious);
        //System.out.println(fListadoPrevious.toString());
        if(fListadoPrevious.exists() && !fListadoPrevious.isDirectory()) { 
            //existe el listado antiguo
            System.out.println("existe el antiguo");
            //new XMLComparator(localListado, localListadoPrevious);
            Compare2XML compareXML = new Compare2XML(fListado, fListadoPrevious);
            
            
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

            //CREAMOS LOS XML DE NUEVOS PRODUCTOS Y NUEVAS OFERTAS
            // create productList, assign products
            var listado = new Listado();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            //PrintStream fileOut = new PrintStream("./ofertas/" + formatter.format(date) + ".txt");
            //System.setOut(fileOut);        
            Date date = new Date();
            formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");   
            //System.out.println(formatter.format(date));             
            listado.setDate(formatter.format(date));
            listado.setProductList(compareXML.getNewProducts());   
            //listado.setListPages(listPages);
            // Write to File
            m.marshal(listado, new File(LISTADO_PRODUCTOS_NEW));    
            
            listado = new Listado();
            listado.setDate(formatter.format(date));
            listado.setProductList(compareXML.getSpecialProducts());   
            //listado.setListPages(listPages);
            // Write to File
            m.marshal(listado, new File(LISTADO_PRODUCTOS_NEW_SPECIAL));
            
            //mandamos el correo
            SendEmail sendEmail = new SendEmail(compareXML.getNewProducts(), compareXML.getSpecialProducts()); //
            
        }else{
            //al no existir el anterior listado, pues copiamos el actual
            FileCopyUtils.copy(fListado, fListadoPrevious);
        }
        
        if(args.length > 1){//si solo ponemos la ruta del fListado en local que no de error
            String remoteListado = args[1];
            /*
            String ftp = "ftp.server";
            String username = "user";
            String password = "1234567890";   
            */
            String ftp = args[2];
            String username = args[3];
            String password = args[4];

            new FtpFileUpload(
                localListado
                , remoteListado
                , ftp
                , username
                , password);
        }
         

    }    
}

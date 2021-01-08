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
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import listadoproductos.info.Listado;
import org.springframework.util.FileCopyUtils;

/**
 *
 * @author user
 */
public class ListadoProductos {
    private static String LISTADO_PRODUCTOS_NEW = "listado_new_products.xml";
    private static String LISTADO_PRODUCTOS_NEW_SPECIAL = "listado_new_products_special.xml";
    
    /** nombre del fListado anterior para comparar con el nuevo */
    private static String LISTADO_PRODUCTOS_PREVIOUS_XML = "listado_previous.xml";

    private static Map<String, String> arguments;
    static {
        arguments = new HashMap<>();
        arguments.put("localListado", ""); //args[0]
        arguments.put("emailUser", ""); //args[1]
        arguments.put("emailPass", ""); //args[2]
        arguments.put("XMLEmails", ""); //args[3]
        arguments.put("remoteListado", ""); //args[4]
        arguments.put("FTPServer", ""); //args[5]
        arguments.put("FTPUser", ""); //args[6]
        arguments.put("FTPPass", ""); //args[7]
    }    
    
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
        arguments.put("localListado", args[0]);
        arguments.put("emailUser", args[1]);
        arguments.put("emailPass", args[2]);
        if(args.length > 3){//si esta en produccion
            arguments.put("XMLEmails", args[3]);
            arguments.put("remoteListado", args[4]); //args[4]
            arguments.put("FTPServer", args[5]); //args[5]
            arguments.put("FTPUser", args[6]); //args[6]
            arguments.put("FTPPass", args[7]); //args[7]            
        }
        
        
        LoadXMLIni loadXMLIni = new LoadXMLIni(arguments.get("localListado"));
        
        //averiguamos el path local para empezar a comparar los archivos
        Path path = Paths.get(arguments.get("localListado"));
        String localPath = path.getParent().toString().concat("/");
        File fListado = new File(arguments.get("localListado"));
        String localListadoPrevious = localPath.concat(LISTADO_PRODUCTOS_PREVIOUS_XML);
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
            m.marshal(listado, new File(localPath.concat(LISTADO_PRODUCTOS_NEW)));    
            
            listado = new Listado();
            listado.setDate(formatter.format(date));
            listado.setProductList(compareXML.getSpecialProducts());   
            //listado.setListPages(listPages);
            // Write to File
            m.marshal(listado, new File(localPath.concat(LISTADO_PRODUCTOS_NEW_SPECIAL)));
            
            //mandamos el correo
            if((compareXML.getNewProducts().size() > 0 
                    || compareXML.getSpecialProducts().size() > 0)
                    && !arguments.get("XMLEmails").equals("")){
                ReadEmailConfig email = new ReadEmailConfig(arguments.get("XMLEmails"));        
                SendEmailNewsByGmail sendEmail = new SendEmailNewsByGmail(
                        arguments.get("emailUser") //gmail_user_without_@gmail
                        , arguments.get("emailPass") //gmail_password
                        , email.getEmailsToString()
                        , compareXML.getNewProducts()
                        , compareXML.getSpecialProducts()
                ); //
            }
            
        }
        //copiamos el nuevo en el viejo
        FileCopyUtils.copy(fListado, fListadoPrevious);
        
        
        if(args.length > 4){//si solo ponemos la ruta del fListado en local que no de error
            new FtpFileUpload(
                arguments.get("localListado")
                , arguments.get("remoteListado")
                , arguments.get("FTPServer")
                , arguments.get("FTPUser")
                , arguments.get("FTPPass")
            );
        }
         

    }    
}

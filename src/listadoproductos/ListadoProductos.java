/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listadoproductos;

/**
 *
 * @author user
 */
public class ListadoProductos {
    
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
            System.out.println("Error: Insuficientes parámetros. \"$java -jar \'ListadoProductos.jar\' listado.xml /remote_path/listado.xml server username password\"");
            System.exit(0);
        }
        */
        //String localListado = "./listado.xml";
        //String remoteListado = "/public_html/data/listado.xml";
        
        String localListado = args[0];
        
        
        new LoadXMLIni(localListado);
        
        if(args.length > 1){//si solo ponemos la ruta del listado en local que no de error
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

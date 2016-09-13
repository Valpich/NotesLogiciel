package eseo.parser;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

@SuppressWarnings( "deprecation" )
public class RequestTools {
    private final static String USER_AGENT = "Mozilla/5.0";

    @SuppressWarnings( "resource" )
    public static StringBuffer sendGet( String cookie, String ID ) throws Exception {
        /*String url = "https://portail.eseo.fr/+CSCO+10756767633A2F2F726672622D617267++/OpDotNet/Eplug/FPC/Process/Annuaire/Parcours/pDetailParcours.aspx?idProcess=13739&idUser="
                + ID + "&idIns=249461&idProcessUC=5772&typeRef=module'";*/
        String certificatesTrustStorePath = "/Library/Java/JavaVirtualMachines/jdk1.8.0_91.jdk/Contents/Home/jre/lib/security/cacerts";
        System.setProperty( "javax.net.ssl.trustStore", certificatesTrustStorePath );
        String urlBDN = null;
        for(int i = 16310; i<16311; i++){
          urlBDN = "https://portail.eseo.fr/+CSCO+10756767633A2F2F726672622D617267++/OpDOtNet/modules/publipostage/Commun/downloadFile.aspx?strFilePath=E%3a%5cOpenportal_production%5cSites_OPSW%5cOpenportal_Production%5cDataOp%5c2%5cOutils%5cFPC%5cRapports_BDN%5c"+i+"%5c2%5cExport_BDN_27379.pdf&strFileName=BDN_UE.pdf";
          RequestTools.openWebpage(new URL(urlBDN));
          Thread.sleep(1000);
        }
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet( urlBDN );
        
        request.addHeader( "Cookie", cookie );
        request.addHeader( "User-Agent", USER_AGENT );
        HttpResponse response = null;
        try {
            response = httpclient.execute( request );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        System.out.println( "\nSending 'GET' request to URL : " + urlBDN );
        for ( Header tmp : request.getHeaders( "Cookie" ) ) {
          System.out.println( tmp );
        }
        System.out.println( "Response Code : "+response.getStatusLine().getStatusCode() );
        for(Header tmp : response.getAllHeaders()){
          System.out.println(tmp.getName()+" "+tmp.getValue());
        }
        /*
        BufferedReader rd = new BufferedReader(
                new InputStreamReader( response.getEntity().getContent() ) );

        StringBuffer resultat = new StringBuffer();
        String line = "";
        while ( ( line = rd.readLine() ) != null ) {
            resultat.append( line );
        }
         */
        return null;
    }
    
    public static void openWebpage(URI uri) {
      Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
      if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
          try {
              desktop.browse(uri);
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
  }

  public static void openWebpage(URL url) {
      try {
          openWebpage(url.toURI());
      } catch (URISyntaxException e) {
          e.printStackTrace();
      }
  }

}

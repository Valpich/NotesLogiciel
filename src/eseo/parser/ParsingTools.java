package eseo.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.TreeMap;

public class ParsingTools {

    public static float recupererNote( StringBuffer resultat, String nomUE ) {
        float valeur = -1;
        String tmp;
        String tmp2;
        String tmp3;
        String tmp4;
        tmp = resultat.toString().substring( resultat.toString().indexOf( nomUE ) );
        tmp2 = tmp.toString().substring( tmp.toString().indexOf( "</td>" ) + 4 );
        tmp3 = tmp2.toString().substring( tmp2.toString().indexOf( "</td>" ) + 4 );
        tmp4 = tmp3.toString().substring( tmp3.toString().indexOf( "border-right-width:0px;\" >" ) + 26,
                tmp3.toString().indexOf( "</td>" ) );
        try {
            valeur = NumberFormat.getNumberInstance( Locale.FRANCE ).parse( tmp4 ).floatValue();
        } catch ( ParseException e ) {
            // e.printStackTrace();
        }
        return valeur;
    }

    public static float recupererCoefficient( StringBuffer resultat, String nomUE ) {
        float valeur = -1;
        String tmp;
        String tmp2;
        String tmp3;
        tmp = resultat.toString().substring( resultat.toString().indexOf( nomUE ) );
        tmp2 = tmp.toString().substring( tmp.toString().indexOf( "</td>" ) + 4 );
        tmp3 = tmp2.toString().substring( tmp2.toString().indexOf( "border-right-width:0px;\" >" ) + 26,
                tmp2.toString().indexOf( "</td>" ) );
        try {
            valeur = NumberFormat.getNumberInstance( Locale.FRANCE ).parse( tmp3 ).floatValue();
        } catch ( ParseException e ) {
            // e.printStackTrace();
        }
        return valeur;
    }

    public static TreeMap<String, Integer> creerMap() {
        String current = null;
        try {
            current = new java.io.File( "." ).getCanonicalPath();
        } catch ( IOException e1 ) {
            e1.printStackTrace();
        }
        String resultat = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = Class.class.getClassLoader();
        }
        InputStream input = classLoader.getResourceAsStream("test.txt");
        StringBuilder sb=new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        
        resultat = parseFile( br );

        TreeMap<String, Integer> valeurs = new TreeMap<String, Integer>();
        String tmp = null;
        String tmp2 = null;
        String tmp3 = null;
        try {
            while ( true ) {
                try {
                    tmp = resultat.toString().substring( resultat.toString().indexOf( "VisUser(" ) + 8 );
                    tmp2 = tmp.toString().substring( tmp.toString().indexOf( "\"#000000\">" ) + 10 );
                    tmp3 = tmp2.toString().substring( tmp2.toString().indexOf( "&nbsp;" ) + 6 );
                } catch ( Exception e ) {

                } finally {
                    valeurs.put(
                            tmp2.substring( 0, tmp2.indexOf( "&nbsp;" ) )
                                    + " " + tmp3.substring( 0, tmp3.indexOf( "</font>" ) ),
                            Integer.parseInt( tmp.substring( 0, 5 ) ) );
                }
                resultat = tmp3;
            }
        } catch ( Exception e ) {
            // e.printStackTrace( );
        }
        return valeurs;
    }

    public static String parseFile(BufferedReader br) {
        String everything = null;
        try {
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                line = br.readLine();
            } catch ( IOException e ) {
                e.printStackTrace();
            }

            while ( line != null ) {
                sb.append( line );
                sb.append( System.lineSeparator() );
                try {
                    line = br.readLine();
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
            everything = sb.toString();

        } finally {
            try {
                br.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
        return everything;
    }
}

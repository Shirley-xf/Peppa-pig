package utils;

import app.App;
import app.datatype.Language;

import java.io.*;
import java.util.*;

/**
 * This class can parse the attributes of properties.
 */
public class PropertiesInfoParser {
    private static final String DEFAULT_PROPERTIES_PATH = "data" + App.FS.getSeparator() + "properties";
    private static String sPropPath;

    /**
     *
     * get the list of language according to the properties.
     *
     * @param path path to properties
     * @return the languages available, as linked list form
     *
     */
    public static List<Language> getLanguagesList(String path) {
        List lan_list = new LinkedList();
        sPropPath = (path.length() > 0) ? path : DEFAULT_PROPERTIES_PATH;
        File pro_dir = new File(sPropPath);
        File[] properties = pro_dir.listFiles(f -> f.getName().lastIndexOf(".properties") > 0);
        for (File f : properties) {
            Language lan = new Language();
            lan.setName(f.getName().substring(0, f.getName().lastIndexOf('.')));
            try {
                lan.setProperties_url(f.toURI().toURL().toExternalForm());
            } catch (Exception e) {
                e.printStackTrace();
            }
            lan_list.add(lan);
        }
        return lan_list;
    }


    /**
     *
     * Gets the property when specified the path to it.
     *
     * @param lan the language
     * @param path the path to property
     * @return
     */
    public static PropertyResourceBundle getProperty(String lan, String path) {

        String propertyFile = (path.length() > 0) ? path : DEFAULT_PROPERTIES_PATH + File.separator + lan + ".properties";
        PropertyResourceBundle property = null;
        try {
            BufferedReader conf = new BufferedReader(new InputStreamReader(new FileInputStream(propertyFile),"UTF-8"));
            property = new PropertyResourceBundle(conf);
        } catch (IOException e) {
            System.err.println("No properties file found.");
            System.exit(1);
        }
        return property;
    }
}

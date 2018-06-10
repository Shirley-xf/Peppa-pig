package utils;

import app.Main;
import app.datatype.Language;

import java.io.*;
import java.util.*;

public class PropertiesInfoParser {
    private static final String DEFAULT_PROPERTIES_PATH = "data" + Main.FS.getSeparator() + "properties";
    private static String sPropPath;
    public static List<Language> getPropertiesList(String path) throws Exception {
        List lan_list = new LinkedList();
        sPropPath = (path.length() > 0) ? path : DEFAULT_PROPERTIES_PATH;
        File pro_dir = new File(sPropPath);
        File[] properties = pro_dir.listFiles(f -> f.getName().lastIndexOf(".properties") > 0);
        for (File f : properties) {
            Language lan = new Language();
            lan.setName(f.getName().substring(0, f.getName().lastIndexOf('.')));
            lan.setProperties_url(f.toURI().toURL().toExternalForm());
            lan_list.add(lan);
        }
        return lan_list;
    }

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

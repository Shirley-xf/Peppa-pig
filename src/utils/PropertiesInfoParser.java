package utils;

import app.Language;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;

public class PropertiesInfoParser {
    private static final String DEFAULT_PROPERTIES_PATH = "data/properties";
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
}

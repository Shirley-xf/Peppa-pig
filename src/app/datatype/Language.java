package app.datatype;

import app.App;

public class Language {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getProperties_url() {
        return properties_url;
    }

    public void setProperties_url(String properties_url) {
        this.properties_url = properties_url;
    }


    private String properties_url;


    @Override
    public String toString() {
        String p_name;
        try {
            p_name = App.property.getString(name);
        } catch (Exception e) {
            p_name = name;
        }
        return p_name;
    }
}

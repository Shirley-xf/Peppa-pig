package app.datatype;

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
        return name;
    }
}

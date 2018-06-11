package custom;

import java.util.LinkedList;
import java.util.List;


/**
 * This class offers custom settings, which can be fetched by the boot class and run.
 */
public class CustomUtils {
    private CustomUtils() {
    }

    private static List<Customizable> sInitCustomList = new LinkedList<>();
    private static List<Customizable> sDurableCustomList = new LinkedList<>();

    private static Customizable prevCustom;

    /**
     * Add custom to initialize.
     *<p>
     *     The custom only runs once. So customs like initialize database should be use
     *     this function to add.
     *</p>
     * @param c the c
     */
    public static void addCustomToInit(Customizable c) {
        sInitCustomList.add(c);
    }

    /**
     * Sets previously run custom.
     * <p>
     *     This method runs at first since it runs. Stuff must be init here is the introductions url.
     *     Because Although we offers method to get introduction, we don't have auto translate method.
     *     client.client should specify the translated element here.
     *     Note that only one custom can be write at prev. Or will cause override.
     * </p>
     *
     * @param c the Customizable type
     */
    public static void addCustomAtPrev(Customizable c) {
        prevCustom = c;
    }

    /**
     * Add custom that runs after start() of the application.
     *
     * @param c the c
     */
    public static void addCustomAtPost(Customizable c) {
        sDurableCustomList.add(c);
    }

    /**
     * Deletes custom in init custom list.
     *
     * @param c the c
     */
    public static void delCustomInPrev(Customizable c) {
        sInitCustomList.remove(c);
    }

    /**
     * Deletes custom at post run customs.
     *
     * @param c the c
     */
    public static void delCustomInPost(Customizable c) {
        sDurableCustomList.remove(c);
    }

    /**
     * Gets init custom list.
     *
     * @return the init custom list
     */
    public static List getInitCustomList() {
        return sInitCustomList;
    }

    /**
     * Gets the list of customs that runs at the end of starts.
     *
     * @return the custom list
     */
    public static List getDurableCustomList() {
        return sDurableCustomList;
    }

    /**
     * Gets custom that runs at first when the app starts. It runs before init, according to App.
     *
     * @return the custom runs before.
     */
    public static Customizable getCustomAtPrev() {
        return prevCustom;
    }
}

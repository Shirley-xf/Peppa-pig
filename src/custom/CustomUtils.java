package custom;

import java.util.LinkedList;
import java.util.List;


public class CustomUtils {
    private CustomUtils() {}
    private static List<Customizable> sInitCustomList = new LinkedList<>();
    private static List<Customizable> sDurableCustomList = new LinkedList<>();
    public static void addCustomToInit(Customizable c) {
        sInitCustomList.add(c);
    }

    public static void addCustom(Customizable c) {
        sDurableCustomList.add(c);
    }

    public static void delCustomInPrev(Customizable c) {
        sInitCustomList.remove(c);
    }

    public static void delCustomInPost(Customizable c) {
        sDurableCustomList.remove(c);
    }

    public static List getPrevCustomList() {
        return sInitCustomList;
    }

    public static List getPostCustomList() {
        return sDurableCustomList;
    }
}

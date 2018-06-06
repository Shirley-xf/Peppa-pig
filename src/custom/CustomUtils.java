package custom;

import java.util.LinkedList;
import java.util.List;


public class CustomUtils {
    private CustomUtils() {}
    private static List<Customizable> sPrevCustomList = new LinkedList<>();
    private static List<Customizable> sPostCustomList = new LinkedList<>();
    public static void addCustomToPrev(Customizable c) {
        sPrevCustomList.add(c);
    }

    public static void addCustomToPost(Customizable c) {
        sPostCustomList.add(c);
    }

    public static void delCustomInPrev(Customizable c) {
        sPrevCustomList.remove(c);
    }

    public static void delCustomInPost(Customizable c) {
        sPostCustomList.remove(c);
    }

    public static List getPrevCustomList() {
        return sPrevCustomList;
    }

    public static List getPostCustomList() {
        return sPostCustomList;
    }
}

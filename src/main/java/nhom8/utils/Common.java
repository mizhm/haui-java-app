package nhom8.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Common {
    // Get system timestamp
    public static Long getTimeStamp() {
        return System.currentTimeMillis();
    }

    // Check null or empty
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof String) {
            return obj.toString().isEmpty();
        }

        if (obj instanceof List) {
            return ((List) obj).isEmpty();
        }

        if (obj instanceof Set) {
            return ((Set) obj).isEmpty();
        }

        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        return false;
    }

    // Kiểm tra là số nguyên
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}

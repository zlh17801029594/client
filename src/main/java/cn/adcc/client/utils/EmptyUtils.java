package cn.adcc.client.utils;

import java.util.List;
import java.util.Map;

public class EmptyUtils {
    public static <T> boolean isNotEmpty(T t) {
        if (t == null) {
            return false;
        }
        if (t instanceof List) {
            return !((List) t).isEmpty();
        } else if (t instanceof Map) {
            return !((Map) t).isEmpty();
        }
        return false;
    }
}

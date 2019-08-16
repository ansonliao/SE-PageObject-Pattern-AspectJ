package example.utils;


public class StrUtils {

    public synchronized static String firstCapitalize(String s) {
        if (com.google.common.base.Strings.isNullOrEmpty(s)) {
            return s;
        }
        if (s.length() == 1) {
            return s.toUpperCase();
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public synchronized static String withBoldHTML(String s) {
        return !s.trim().isEmpty() ? "<b>" + s + "</b>" : "";
    }
}

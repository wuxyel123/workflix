package utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ResourceValueChecker {

    /**
     * Check if the value is null or empty Integer
     * @param value The value to check
     * @return The value if it is not null or empty, null otherwise
     */
    public static Integer getValidInteger(Object value) {

        if(value==null || value.toString().isEmpty()){
            return null;
        }
        return Integer.parseInt(value.toString());
    }

    /**
     * Check if the value is null or empty string
     * @param value The value to check
     * @return The value if it is not null or empty, null otherwise
     */
    public static String getValidString(Object value) {

        if(value==null || value.toString().isEmpty()){
            return null;
        }
        return value.toString();
    }

    /**
     * Check if the value is null or empty date
     * @param value The value to check
     * @return The value if it is not null or empty, null otherwise
     */
    public static Date getValidDate(Object value) {

        Date date = null;
        String dateStr;
        if(value==null || value.toString().isEmpty()){
            return date;
        } else {
            try {
                date = Date.from(LocalDateTime.parse(value.toString()).atZone(ZoneId.systemDefault()).toInstant());
            } catch (Exception e) {}
        }

        return date;
    }

    /**
     * Check if the value is null or empty
     * @param value The value to check
     * @return The value if it is not null or empty, null otherwise
     */
    public static Boolean getValidBoolean(Object value) {

        if (value == null || value.toString().isEmpty()) {
            return false;
        }
        return Boolean.parseBoolean(value.toString());
    }

}

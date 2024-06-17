package utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Logger;

/**
 * @author nguyenlm Contains helper functions
 */
public class Utils {

    public static DateFormat DATE_FORMATER = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static Logger LOGGER = getLogger(Utils.class.getName());

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-4s] [%1$tF %1$tT] [%2$-7s] %5$s %n");
    }


    /**
     * @param className
     * @return Logger
     */
    public static Logger getLogger(String className) {
        return Logger.getLogger(className);
    }


    /**
     * @param num
     * @return String
     */
    public static String getCurrencyFormat(int num) {
        Locale vietname = new Locale("vi", "VN");
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(vietname);
        return defaultFormat.format(num * 1000);
    }

    /**
     * Return a {@link java.lang.String String} that represents the current time in the format of yyyy-MM-dd HH:mm:ss.
     *
     * @return the current time as {@link java.lang.String String}.
     * @author hieudm
     */
    public static String getToday() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String formatDateTime(Date date, String format) {
        if(date == null) return "";
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);

    }

}
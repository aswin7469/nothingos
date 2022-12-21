package libcore.net.http;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class HttpDate {
    private static final String[] BROWSER_COMPATIBLE_DATE_FORMATS = {"EEEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z", "EEE MMM d yyyy HH:mm:ss z"};
    private static final ThreadLocal<DateFormat> STANDARD_DATE_FORMAT = new ThreadLocal<DateFormat>() {
        /* access modifiers changed from: protected */
        public DateFormat initialValue() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.f700US);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpleDateFormat;
        }
    };

    public static Date parse(String str) {
        try {
            return STANDARD_DATE_FORMAT.get().parse(str);
        } catch (ParseException unused) {
            String[] strArr = BROWSER_COMPATIBLE_DATE_FORMATS;
            int length = strArr.length;
            int i = 0;
            while (i < length) {
                try {
                    return new SimpleDateFormat(strArr[i], Locale.f700US).parse(str);
                } catch (ParseException unused2) {
                    i++;
                }
            }
            return null;
        }
    }

    public static String format(Date date) {
        return STANDARD_DATE_FORMAT.get().format(date);
    }
}

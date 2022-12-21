package java.util.logging;

import android.net.wifi.WifiEnterpriseConfig;
import java.p026io.PrintWriter;
import java.p026io.StringWriter;
import java.p026io.Writer;
import java.util.Date;
import sun.util.logging.LoggingSupport;

public class SimpleFormatter extends Formatter {
    private static final String format = LoggingSupport.getSimpleFormat();
    private final Date dat = new Date();

    public synchronized String format(LogRecord logRecord) {
        String str;
        String formatMessage;
        String str2;
        this.dat.setTime(logRecord.getMillis());
        if (logRecord.getSourceClassName() != null) {
            str = logRecord.getSourceClassName();
            if (logRecord.getSourceMethodName() != null) {
                str = str + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + logRecord.getSourceMethodName();
            }
        } else {
            str = logRecord.getLoggerName();
        }
        formatMessage = formatMessage(logRecord);
        str2 = "";
        if (logRecord.getThrown() != null) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter((Writer) stringWriter);
            printWriter.println();
            logRecord.getThrown().printStackTrace(printWriter);
            printWriter.close();
            str2 = stringWriter.toString();
        }
        return String.format(format, this.dat, str, logRecord.getLoggerName(), logRecord.getLevel().getLocalizedLevelName(), formatMessage, str2);
    }
}

package java.util.logging;

import com.android.settingslib.accessibility.AccessibilityUtils;
import java.nio.charset.Charset;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

public class XMLFormatter extends Formatter {
    private LogManager manager = LogManager.getLogManager();

    public String getTail(Handler handler) {
        return "</log>\n";
    }

    /* renamed from: a2 */
    private void m1771a2(StringBuilder sb, int i) {
        if (i < 10) {
            sb.append('0');
        }
        sb.append(i);
    }

    private void appendISO8601(StringBuilder sb, long j) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(j);
        sb.append(gregorianCalendar.get(1));
        sb.append('-');
        m1771a2(sb, gregorianCalendar.get(2) + 1);
        sb.append('-');
        m1771a2(sb, gregorianCalendar.get(5));
        sb.append('T');
        m1771a2(sb, gregorianCalendar.get(11));
        sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        m1771a2(sb, gregorianCalendar.get(12));
        sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        m1771a2(sb, gregorianCalendar.get(13));
    }

    private void escape(StringBuilder sb, String str) {
        if (str == null) {
            str = "<null>";
        }
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt == '<') {
                sb.append("&lt;");
            } else if (charAt == '>') {
                sb.append("&gt;");
            } else if (charAt == '&') {
                sb.append("&amp;");
            } else {
                sb.append(charAt);
            }
        }
    }

    public String format(LogRecord logRecord) {
        StringBuilder sb = new StringBuilder(500);
        sb.append("<record>\n  <date>");
        appendISO8601(sb, logRecord.getMillis());
        sb.append("</date>\n");
        sb.append("  <millis>");
        sb.append(logRecord.getMillis());
        sb.append("</millis>\n");
        sb.append("  <sequence>");
        sb.append(logRecord.getSequenceNumber());
        sb.append("</sequence>\n");
        String loggerName = logRecord.getLoggerName();
        if (loggerName != null) {
            sb.append("  <logger>");
            escape(sb, loggerName);
            sb.append("</logger>\n");
        }
        sb.append("  <level>");
        escape(sb, logRecord.getLevel().toString());
        sb.append("</level>\n");
        if (logRecord.getSourceClassName() != null) {
            sb.append("  <class>");
            escape(sb, logRecord.getSourceClassName());
            sb.append("</class>\n");
        }
        if (logRecord.getSourceMethodName() != null) {
            sb.append("  <method>");
            escape(sb, logRecord.getSourceMethodName());
            sb.append("</method>\n");
        }
        sb.append("  <thread>");
        sb.append(logRecord.getThreadID());
        sb.append("</thread>\n");
        if (logRecord.getMessage() != null) {
            String formatMessage = formatMessage(logRecord);
            sb.append("  <message>");
            escape(sb, formatMessage);
            sb.append("</message>");
            sb.append("\n");
        } else {
            sb.append("<message/>");
            sb.append("\n");
        }
        ResourceBundle resourceBundle = logRecord.getResourceBundle();
        if (resourceBundle != null) {
            try {
                if (resourceBundle.getString(logRecord.getMessage()) != null) {
                    sb.append("  <key>");
                    escape(sb, logRecord.getMessage());
                    sb.append("</key>\n");
                    sb.append("  <catalog>");
                    escape(sb, logRecord.getResourceBundleName());
                    sb.append("</catalog>\n");
                }
            } catch (Exception unused) {
            }
        }
        Object[] parameters = logRecord.getParameters();
        if (!(parameters == null || parameters.length == 0 || logRecord.getMessage().indexOf("{") != -1)) {
            for (int i = 0; i < parameters.length; i++) {
                sb.append("  <param>");
                try {
                    escape(sb, parameters[i].toString());
                } catch (Exception unused2) {
                    sb.append("???");
                }
                sb.append("</param>\n");
            }
        }
        if (logRecord.getThrown() != null) {
            Throwable thrown = logRecord.getThrown();
            sb.append("  <exception>\n");
            sb.append("    <message>");
            escape(sb, thrown.toString());
            sb.append("</message>\n");
            StackTraceElement[] stackTrace = thrown.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                sb.append("    <frame>\n");
                sb.append("      <class>");
                escape(sb, stackTraceElement.getClassName());
                sb.append("</class>\n");
                sb.append("      <method>");
                escape(sb, stackTraceElement.getMethodName());
                sb.append("</method>\n");
                if (stackTraceElement.getLineNumber() >= 0) {
                    sb.append("      <line>");
                    sb.append(stackTraceElement.getLineNumber());
                    sb.append("</line>\n");
                }
                sb.append("    </frame>\n");
            }
            sb.append("  </exception>\n");
        }
        sb.append("</record>\n");
        return sb.toString();
    }

    public String getHead(Handler handler) {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"");
        String encoding = handler != null ? handler.getEncoding() : null;
        if (encoding == null) {
            encoding = Charset.defaultCharset().name();
        }
        try {
            encoding = Charset.forName(encoding).name();
        } catch (Exception unused) {
        }
        sb.append(encoding);
        sb.append("\" standalone=\"no\"?>\n<!DOCTYPE log SYSTEM \"logger.dtd\">\n<log>\n");
        return sb.toString();
    }
}

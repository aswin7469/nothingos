package sun.nio.p035fs;

import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;
import java.p026io.IOException;
import java.util.Locale;

/* renamed from: sun.nio.fs.AbstractFileTypeDetector */
public abstract class AbstractFileTypeDetector extends FileTypeDetector {
    private static final String TSPECIALS = "()<>@,;:/[]?=\\\"";

    /* access modifiers changed from: protected */
    public abstract String implProbeContentType(Path path) throws IOException;

    protected AbstractFileTypeDetector() {
    }

    public final String probeContentType(Path path) throws IOException {
        if (path != null) {
            String implProbeContentType = implProbeContentType(path);
            if (implProbeContentType == null) {
                return null;
            }
            return parse(implProbeContentType);
        }
        throw new NullPointerException("'file' is null");
    }

    private static String parse(String str) {
        String str2;
        int indexOf = str.indexOf(47);
        int indexOf2 = str.indexOf(59);
        if (indexOf < 0) {
            return null;
        }
        String lowerCase = str.substring(0, indexOf).trim().toLowerCase(Locale.ENGLISH);
        if (!isValidToken(lowerCase)) {
            return null;
        }
        if (indexOf2 < 0) {
            str2 = str.substring(indexOf + 1);
        } else {
            str2 = str.substring(indexOf + 1, indexOf2);
        }
        String lowerCase2 = str2.trim().toLowerCase(Locale.ENGLISH);
        if (!isValidToken(lowerCase2)) {
            return null;
        }
        StringBuilder sb = new StringBuilder(lowerCase.length() + lowerCase2.length() + 1);
        sb.append(lowerCase);
        sb.append('/');
        sb.append(lowerCase2);
        return sb.toString();
    }

    private static boolean isTokenChar(char c) {
        return c > ' ' && c < 127 && TSPECIALS.indexOf((int) c) < 0;
    }

    private static boolean isValidToken(String str) {
        int length = str.length();
        if (length == 0) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (!isTokenChar(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}

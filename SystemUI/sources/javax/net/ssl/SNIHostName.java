package javax.net.ssl;

import com.android.launcher3.icons.cache.BaseIconCache;
import java.net.IDN;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Pattern;

public final class SNIHostName extends SNIServerName {
    private final String hostname;

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public SNIHostName(java.lang.String r3) {
        /*
            r2 = this;
            java.lang.String r0 = "Server name value of host_name cannot be null"
            java.lang.Object r3 = java.util.Objects.requireNonNull(r3, (java.lang.String) r0)
            java.lang.String r3 = (java.lang.String) r3
            r0 = 2
            java.lang.String r3 = java.net.IDN.toASCII(r3, r0)
            java.nio.charset.Charset r0 = java.nio.charset.StandardCharsets.US_ASCII
            byte[] r0 = r3.getBytes((java.nio.charset.Charset) r0)
            r1 = 0
            r2.<init>(r1, r0)
            r2.hostname = r3
            r2.checkHostName()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.net.ssl.SNIHostName.<init>(java.lang.String):void");
    }

    public SNIHostName(byte[] bArr) {
        super(0, bArr);
        try {
            this.hostname = IDN.toASCII(StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT).decode(ByteBuffer.wrap(bArr)).toString());
            checkHostName();
        } catch (RuntimeException | CharacterCodingException e) {
            throw new IllegalArgumentException("The encoded server name value is invalid", e);
        }
    }

    public String getAsciiName() {
        return this.hostname;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SNIHostName) {
            return this.hostname.equalsIgnoreCase(((SNIHostName) obj).hostname);
        }
        return false;
    }

    public int hashCode() {
        return 527 + this.hostname.toUpperCase(Locale.ENGLISH).hashCode();
    }

    public String toString() {
        return "type=host_name (0), value=" + this.hostname;
    }

    public static SNIMatcher createSNIMatcher(String str) {
        if (str != null) {
            return new SNIHostNameMatcher(str);
        }
        throw new NullPointerException("The regular expression cannot be null");
    }

    private void checkHostName() {
        if (this.hostname.isEmpty()) {
            throw new IllegalArgumentException("Server name value of host_name cannot be empty");
        } else if (this.hostname.endsWith(BaseIconCache.EMPTY_CLASS_NAME)) {
            throw new IllegalArgumentException("Server name value of host_name cannot have the trailing dot");
        }
    }

    private static final class SNIHostNameMatcher extends SNIMatcher {
        private final Pattern pattern;

        SNIHostNameMatcher(String str) {
            super(0);
            this.pattern = Pattern.compile(str, 2);
        }

        public boolean matches(SNIServerName sNIServerName) {
            SNIHostName sNIHostName;
            if (sNIServerName != null) {
                if (sNIServerName instanceof SNIHostName) {
                    sNIHostName = (SNIHostName) sNIServerName;
                } else if (sNIServerName.getType() == 0) {
                    try {
                        sNIHostName = new SNIHostName(sNIServerName.getEncoded());
                    } catch (IllegalArgumentException | NullPointerException unused) {
                        return false;
                    }
                } else {
                    throw new IllegalArgumentException("The server name type is not host_name");
                }
                String asciiName = sNIHostName.getAsciiName();
                if (this.pattern.matcher(asciiName).matches()) {
                    return true;
                }
                return this.pattern.matcher(IDN.toUnicode(asciiName)).matches();
            }
            throw new NullPointerException("The SNIServerName argument cannot be null");
        }
    }
}

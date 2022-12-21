package javax.net.ssl;

import com.android.settingslib.accessibility.AccessibilityUtils;
import java.util.Arrays;

public abstract class SNIServerName {
    private static final char[] HEXES = "0123456789ABCDEF".toCharArray();
    private final byte[] encoded;
    private final int type;

    protected SNIServerName(int i, byte[] bArr) {
        if (i < 0) {
            throw new IllegalArgumentException("Server name type cannot be less than zero");
        } else if (i <= 255) {
            this.type = i;
            if (bArr != null) {
                this.encoded = (byte[]) bArr.clone();
                return;
            }
            throw new NullPointerException("Server name encoded value cannot be null");
        } else {
            throw new IllegalArgumentException("Server name type cannot be greater than 255");
        }
    }

    public final int getType() {
        return this.type;
    }

    public final byte[] getEncoded() {
        return (byte[]) this.encoded.clone();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SNIServerName sNIServerName = (SNIServerName) obj;
        if (this.type != sNIServerName.type || !Arrays.equals(this.encoded, sNIServerName.encoded)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ((527 + this.type) * 31) + Arrays.hashCode(this.encoded);
    }

    public String toString() {
        if (this.type == 0) {
            return "type=host_name (0), value=" + toHexString(this.encoded);
        }
        return "type=(" + this.type + "), value=" + toHexString(this.encoded);
    }

    private static String toHexString(byte[] bArr) {
        if (bArr.length == 0) {
            return "(empty)";
        }
        boolean z = true;
        StringBuilder sb = new StringBuilder((bArr.length * 3) - 1);
        for (byte b : bArr) {
            if (z) {
                z = false;
            } else {
                sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            }
            byte b2 = b & 255;
            char[] cArr = HEXES;
            sb.append(cArr[b2 >>> 4]);
            sb.append(cArr[b2 & 15]);
        }
        return sb.toString();
    }
}

package java.net;

import android.net.ProxyInfo;
import android.system.OsConstants;
import com.android.launcher3.icons.cache.BaseIconCache;
import java.p026io.ObjectStreamException;

public final class Inet4Address extends InetAddress {
    public static final InetAddress ALL = new Inet4Address((String) null, new byte[]{-1, -1, -1, -1});
    public static final InetAddress ANY = new Inet4Address((String) null, new byte[]{0, 0, 0, 0});
    static final int INADDRSZ = 4;
    public static final InetAddress LOOPBACK = new Inet4Address(ProxyInfo.LOCAL_HOST, new byte[]{Byte.MAX_VALUE, 0, 0, 1});
    private static final long serialVersionUID = 3286316764910316507L;

    public boolean isMCNodeLocal() {
        return false;
    }

    Inet4Address() {
        holder().hostName = null;
        holder().address = 0;
        holder().family = OsConstants.AF_INET;
    }

    Inet4Address(String str, byte[] bArr) {
        holder().hostName = str;
        holder().family = OsConstants.AF_INET;
        if (bArr != null && bArr.length == 4) {
            holder().address = ((bArr[0] << 24) & -16777216) | (bArr[3] & 255) | ((bArr[2] << 8) & 65280) | ((bArr[1] << 16) & 16711680);
        }
        holder().originalHostName = str;
    }

    Inet4Address(String str, int i) {
        holder().hostName = str;
        holder().family = OsConstants.AF_INET;
        holder().address = i;
        holder().originalHostName = str;
    }

    private Object writeReplace() throws ObjectStreamException {
        InetAddress inetAddress = new InetAddress();
        inetAddress.holder().hostName = holder().getHostName();
        inetAddress.holder().address = holder().getAddress();
        inetAddress.holder().family = 2;
        return inetAddress;
    }

    public boolean isMulticastAddress() {
        return (holder().getAddress() & -268435456) == -536870912;
    }

    public boolean isAnyLocalAddress() {
        return holder().getAddress() == 0;
    }

    public boolean isLoopbackAddress() {
        if (getAddress()[0] == Byte.MAX_VALUE) {
            return true;
        }
        return false;
    }

    public boolean isLinkLocalAddress() {
        int address = holder().getAddress();
        return ((address >>> 24) & 255) == 169 && ((address >>> 16) & 255) == 254;
    }

    public boolean isSiteLocalAddress() {
        int address = holder().getAddress();
        int i = (address >>> 24) & 255;
        return i == 10 || (i == 172 && ((address >>> 16) & 240) == 16) || (i == 192 && ((address >>> 16) & 255) == 168);
    }

    public boolean isMCGlobal() {
        byte[] address = getAddress();
        byte b = address[0];
        if ((b & 255) < 224 || (b & 255) > 238) {
            return false;
        }
        if ((b & 255) == 224 && address[1] == 0 && address[2] == 0) {
            return false;
        }
        return true;
    }

    public boolean isMCLinkLocal() {
        int address = holder().getAddress();
        return ((address >>> 24) & 255) == 224 && ((address >>> 16) & 255) == 0 && ((address >>> 8) & 255) == 0;
    }

    public boolean isMCSiteLocal() {
        int address = holder().getAddress();
        return ((address >>> 24) & 255) == 239 && ((address >>> 16) & 255) == 255;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0010, code lost:
        r2 = (r2 >>> 16) & 255;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isMCOrgLocal() {
        /*
            r2 = this;
            java.net.InetAddress$InetAddressHolder r2 = r2.holder()
            int r2 = r2.getAddress()
            int r0 = r2 >>> 24
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 239(0xef, float:3.35E-43)
            if (r0 != r1) goto L_0x001e
            int r2 = r2 >>> 16
            r2 = r2 & 255(0xff, float:3.57E-43)
            r0 = 192(0xc0, float:2.69E-43)
            if (r2 < r0) goto L_0x001e
            r0 = 195(0xc3, float:2.73E-43)
            if (r2 > r0) goto L_0x001e
            r2 = 1
            goto L_0x001f
        L_0x001e:
            r2 = 0
        L_0x001f:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.Inet4Address.isMCOrgLocal():boolean");
    }

    public byte[] getAddress() {
        int address = holder().getAddress();
        return new byte[]{(byte) ((address >>> 24) & 255), (byte) ((address >>> 16) & 255), (byte) ((address >>> 8) & 255), (byte) (address & 255)};
    }

    public String getHostAddress() {
        return numericToTextFormat(getAddress());
    }

    public int hashCode() {
        return holder().getAddress();
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof Inet4Address) && ((InetAddress) obj).holder().getAddress() == holder().getAddress();
    }

    static String numericToTextFormat(byte[] bArr) {
        return (bArr[0] & 255) + BaseIconCache.EMPTY_CLASS_NAME + (bArr[1] & 255) + BaseIconCache.EMPTY_CLASS_NAME + (bArr[2] & 255) + BaseIconCache.EMPTY_CLASS_NAME + (bArr[3] & 255);
    }
}

package android.net.connectivity.com.android.net.module.util;

import java.net.Inet6Address;
import java.net.InetAddress;

public final class ConnectivityUtils {
    public static int saturatedCast(long j) {
        if (j > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        if (j < -2147483648L) {
            return Integer.MIN_VALUE;
        }
        return (int) j;
    }

    private ConnectivityUtils() {
    }

    public static String addressAndPortToString(InetAddress inetAddress, int i) {
        return String.format(inetAddress instanceof Inet6Address ? "[%s]:%d" : "%s:%d", inetAddress.getHostAddress(), Integer.valueOf(i));
    }

    public static boolean isIPv6ULA(InetAddress inetAddress) {
        if (!(inetAddress instanceof Inet6Address) || (inetAddress.getAddress()[0] & 254) != 252) {
            return false;
        }
        return true;
    }
}

package android.net;

import android.net.DnsResolver;
import android.net.connectivity.com.android.net.module.util.Inet4AddressUtils;
import android.system.ErrnoException;
import android.util.Log;
import android.util.Pair;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.p026io.FileDescriptor;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;

public class NetworkUtils {
    private static final String TAG = "NetworkUtils";

    public static native void attachDropAllBPFFilter(FileDescriptor fileDescriptor) throws SocketException;

    @Deprecated
    public static native boolean bindProcessToNetworkForHostResolution(int i);

    private static native boolean bindProcessToNetworkHandle(long j);

    private static native int bindSocketToNetworkHandle(FileDescriptor fileDescriptor, long j);

    public static native void detachBPFFilter(FileDescriptor fileDescriptor) throws SocketException;

    private static native long getBoundNetworkHandleForProcess();

    public static native Network getDnsNetwork() throws ErrnoException;

    public static native TcpRepairWindow getTcpRepairWindow(FileDescriptor fileDescriptor) throws ErrnoException;

    public static boolean queryUserAccess(int i, int i2) {
        return false;
    }

    public static native void resNetworkCancel(FileDescriptor fileDescriptor);

    private static native FileDescriptor resNetworkQuery(long j, String str, int i, int i2, int i3) throws ErrnoException;

    public static native DnsResolver.DnsResponse resNetworkResult(FileDescriptor fileDescriptor) throws ErrnoException;

    private static native FileDescriptor resNetworkSend(long j, byte[] bArr, int i, int i2) throws ErrnoException;

    static {
        System.loadLibrary("framework-connectivity-jni");
    }

    public static boolean bindProcessToNetwork(int i) {
        return bindProcessToNetworkHandle(new Network(i).getNetworkHandle());
    }

    public static int getBoundNetworkForProcess() {
        long boundNetworkHandleForProcess = getBoundNetworkHandleForProcess();
        if (boundNetworkHandleForProcess == 0) {
            return 0;
        }
        return Network.fromNetworkHandle(boundNetworkHandleForProcess).getNetId();
    }

    public static int bindSocketToNetwork(FileDescriptor fileDescriptor, int i) {
        return bindSocketToNetworkHandle(fileDescriptor, new Network(i).getNetworkHandle());
    }

    public static FileDescriptor resNetworkSend(int i, byte[] bArr, int i2, int i3) throws ErrnoException {
        return resNetworkSend(new Network(i).getNetworkHandle(), bArr, i2, i3);
    }

    public static FileDescriptor resNetworkQuery(int i, String str, int i2, int i3, int i4) throws ErrnoException {
        return resNetworkQuery(new Network(i).getNetworkHandle(), str, i2, i3, i4);
    }

    @Deprecated
    public static InetAddress intToInetAddress(int i) {
        return Inet4AddressUtils.intToInet4AddressHTL(i);
    }

    @Deprecated
    public static int inetAddressToInt(Inet4Address inet4Address) throws IllegalArgumentException {
        return Inet4AddressUtils.inet4AddressToIntHTL(inet4Address);
    }

    @Deprecated
    public static int prefixLengthToNetmaskInt(int i) throws IllegalArgumentException {
        return Inet4AddressUtils.prefixLengthToV4NetmaskIntHTL(i);
    }

    public static int netmaskIntToPrefixLength(int i) {
        return Integer.bitCount(i);
    }

    @Deprecated
    public static int netmaskToPrefixLength(Inet4Address inet4Address) {
        return Inet4AddressUtils.netmaskToPrefixLength(inet4Address);
    }

    @Deprecated
    public static InetAddress numericToInetAddress(String str) throws IllegalArgumentException {
        return InetAddresses.parseNumericAddress(str);
    }

    public static int getImplicitNetmask(Inet4Address inet4Address) {
        return Inet4AddressUtils.getImplicitNetmask(inet4Address);
    }

    public static Pair<InetAddress, Integer> parseIpAndMask(String str) {
        int i;
        InetAddress inetAddress = null;
        try {
            String[] split = str.split("/", 2);
            i = Integer.parseInt(split[1]);
            try {
                inetAddress = InetAddresses.parseNumericAddress(split[0]);
            } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | NullPointerException | NumberFormatException unused) {
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | NullPointerException | NumberFormatException unused2) {
            i = -1;
        }
        if (inetAddress != null && i != -1) {
            return new Pair<>(inetAddress, Integer.valueOf(i));
        }
        throw new IllegalArgumentException("Invalid IP address and mask " + str);
    }

    @Deprecated
    public static Pair<InetAddress, Integer> legacyParseIpAndMask(String str) {
        int i;
        InetAddress inetAddress = null;
        try {
            String[] split = str.split("/", 2);
            i = Integer.parseInt(split[1]);
            try {
                String str2 = split[0];
                if (str2 != null) {
                    if (!str2.isEmpty()) {
                        if (split[0].startsWith(NavigationBarInflaterView.SIZE_MOD_START) && split[0].endsWith(NavigationBarInflaterView.SIZE_MOD_END) && split[0].indexOf(58) != -1) {
                            String str3 = split[0];
                            split[0] = str3.substring(1, str3.length() - 1);
                        }
                        inetAddress = InetAddresses.parseNumericAddress(split[0]);
                        if (inetAddress != null && i != -1) {
                            return new Pair<>(inetAddress, Integer.valueOf(i));
                        }
                        throw new IllegalArgumentException("Invalid IP address and mask " + str);
                    }
                }
                byte[] bArr = new byte[16];
                bArr[15] = 1;
                return new Pair<>(Inet6Address.getByAddress("ip6-localhost", bArr, 0), Integer.valueOf(i));
            } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | NullPointerException | NumberFormatException | UnknownHostException unused) {
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | NullPointerException | NumberFormatException | UnknownHostException unused2) {
            i = -1;
        }
    }

    public static InetAddress hexToInet6Address(String str) throws IllegalArgumentException {
        try {
            return numericToInetAddress(String.format(Locale.f700US, "%s:%s:%s:%s:%s:%s:%s:%s", str.substring(0, 4), str.substring(4, 8), str.substring(8, 12), str.substring(12, 16), str.substring(16, 20), str.substring(20, 24), str.substring(24, 28), str.substring(28, 32)));
        } catch (Exception e) {
            Log.e(TAG, "error in hexToInet6Address(" + str + "): " + e);
            throw new IllegalArgumentException((Throwable) e);
        }
    }

    public static String trimV4AddrZeros(String str) {
        return Inet4AddressUtils.trimAddressZeros(str);
    }

    private static TreeSet<IpPrefix> deduplicatePrefixSet(TreeSet<IpPrefix> treeSet) {
        TreeSet<IpPrefix> treeSet2 = new TreeSet<>(treeSet.comparator());
        Iterator<IpPrefix> it = treeSet.iterator();
        while (it.hasNext()) {
            IpPrefix next = it.next();
            Iterator<IpPrefix> it2 = treeSet2.iterator();
            while (true) {
                if (it2.hasNext()) {
                    if (it2.next().containsPrefix(next)) {
                        break;
                    }
                } else {
                    treeSet2.add(next);
                    break;
                }
            }
        }
        return treeSet2;
    }

    public static long routedIPv4AddressCount(TreeSet<IpPrefix> treeSet) {
        Iterator<IpPrefix> it = deduplicatePrefixSet(treeSet).iterator();
        long j = 0;
        while (it.hasNext()) {
            IpPrefix next = it.next();
            if (!next.isIPv4()) {
                Log.wtf(TAG, "Non-IPv4 prefix in routedIPv4AddressCount");
            }
            j += 1 << (32 - next.getPrefixLength());
        }
        return j;
    }

    public static BigInteger routedIPv6AddressCount(TreeSet<IpPrefix> treeSet) {
        BigInteger bigInteger = BigInteger.ZERO;
        Iterator<IpPrefix> it = deduplicatePrefixSet(treeSet).iterator();
        while (it.hasNext()) {
            IpPrefix next = it.next();
            if (!next.isIPv6()) {
                Log.wtf(TAG, "Non-IPv6 prefix in routedIPv6AddressCount");
            }
            bigInteger = bigInteger.add(BigInteger.ONE.shiftLeft(128 - next.getPrefixLength()));
        }
        return bigInteger;
    }
}

package android.net.util;

import android.net.InetAddresses;
import android.net.Network;
import android.system.C0308Os;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.util.Log;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import libcore.p030io.IoUtils;

public class DnsUtils {
    private static final int CHAR_BIT = 8;
    public static final int IPV6_ADDR_SCOPE_GLOBAL = 14;
    public static final int IPV6_ADDR_SCOPE_LINKLOCAL = 2;
    public static final int IPV6_ADDR_SCOPE_NODELOCAL = 1;
    public static final int IPV6_ADDR_SCOPE_SITELOCAL = 5;
    private static final String TAG = "DnsUtils";
    private static final Comparator<SortableAddress> sRfc6724Comparator = new Rfc6724Comparator();

    public static class Rfc6724Comparator implements Comparator<SortableAddress> {
        public int compare(SortableAddress sortableAddress, SortableAddress sortableAddress2) {
            int i;
            int i2;
            if (sortableAddress.hasSrcAddr != sortableAddress2.hasSrcAddr) {
                i = sortableAddress2.hasSrcAddr;
                i2 = sortableAddress.hasSrcAddr;
            } else if (sortableAddress.scopeMatch != sortableAddress2.scopeMatch) {
                i = sortableAddress2.scopeMatch;
                i2 = sortableAddress.scopeMatch;
            } else if (sortableAddress.labelMatch != sortableAddress2.labelMatch) {
                i = sortableAddress2.labelMatch;
                i2 = sortableAddress.labelMatch;
            } else if (sortableAddress.precedence != sortableAddress2.precedence) {
                i = sortableAddress2.precedence;
                i2 = sortableAddress.precedence;
            } else if (sortableAddress.scope != sortableAddress2.scope) {
                i = sortableAddress.scope;
                i2 = sortableAddress2.scope;
            } else if (sortableAddress.prefixMatchLen == sortableAddress2.prefixMatchLen) {
                return 0;
            } else {
                i = sortableAddress2.prefixMatchLen;
                i2 = sortableAddress.prefixMatchLen;
            }
            return i - i2;
        }
    }

    public static class SortableAddress {
        public final InetAddress address;
        public final int hasSrcAddr;
        public final int label;
        public final int labelMatch;
        public final int precedence;
        public final int prefixMatchLen;
        public final int scope;
        public final int scopeMatch;

        public SortableAddress(InetAddress inetAddress, InetAddress inetAddress2) {
            this.address = inetAddress;
            int i = 1;
            this.hasSrcAddr = inetAddress2 != null ? 1 : 0;
            int r2 = DnsUtils.findLabel(inetAddress);
            this.label = r2;
            int r3 = DnsUtils.findScope(inetAddress);
            this.scope = r3;
            this.precedence = DnsUtils.findPrecedence(inetAddress);
            this.labelMatch = (inetAddress2 == null || r2 != DnsUtils.findLabel(inetAddress2)) ? 0 : 1;
            this.scopeMatch = (inetAddress2 == null || r3 != DnsUtils.findScope(inetAddress2)) ? 0 : i;
            if (!DnsUtils.isIpv6Address(inetAddress) || !DnsUtils.isIpv6Address(inetAddress2)) {
                this.prefixMatchLen = 0;
            } else {
                this.prefixMatchLen = DnsUtils.compareIpv6PrefixMatchLen(inetAddress2, inetAddress);
            }
        }
    }

    public static List<InetAddress> rfc6724Sort(Network network, List<InetAddress> list) {
        ArrayList arrayList = new ArrayList();
        for (InetAddress next : list) {
            arrayList.add(new SortableAddress(next, findSrcAddress(network, next)));
        }
        Collections.sort(arrayList, sRfc6724Comparator);
        ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(((SortableAddress) it.next()).address);
        }
        return arrayList2;
    }

    private static InetAddress findSrcAddress(Network network, InetAddress inetAddress) {
        int i;
        if (isIpv4Address(inetAddress)) {
            i = OsConstants.AF_INET;
        } else {
            if (isIpv6Address(inetAddress)) {
                i = OsConstants.AF_INET6;
            }
            return null;
        }
        try {
            FileDescriptor socket = C0308Os.socket(i, OsConstants.SOCK_DGRAM, OsConstants.IPPROTO_UDP);
            if (network != null) {
                try {
                    network.bindSocket(socket);
                } catch (ErrnoException | IOException unused) {
                    IoUtils.closeQuietly(socket);
                    return null;
                } catch (Throwable th) {
                    IoUtils.closeQuietly(socket);
                    throw th;
                }
            }
            C0308Os.connect(socket, new InetSocketAddress(inetAddress, 0));
            InetAddress address = ((InetSocketAddress) C0308Os.getsockname(socket)).getAddress();
            IoUtils.closeQuietly(socket);
            return address;
        } catch (ErrnoException e) {
            Log.e(TAG, "findSrcAddress:" + e.toString());
        }
    }

    /* access modifiers changed from: private */
    public static int findLabel(InetAddress inetAddress) {
        if (isIpv4Address(inetAddress)) {
            return 4;
        }
        if (isIpv6Address(inetAddress)) {
            if (inetAddress.isLoopbackAddress()) {
                return 0;
            }
            if (isIpv6Address6To4(inetAddress)) {
                return 2;
            }
            if (isIpv6AddressTeredo(inetAddress)) {
                return 5;
            }
            if (isIpv6AddressULA(inetAddress)) {
                return 13;
            }
            if (((Inet6Address) inetAddress).isIPv4CompatibleAddress()) {
                return 3;
            }
            if (inetAddress.isSiteLocalAddress()) {
                return 11;
            }
            if (isIpv6Address6Bone(inetAddress)) {
                return 12;
            }
        }
        return 1;
    }

    /* access modifiers changed from: private */
    public static boolean isIpv6Address(InetAddress inetAddress) {
        return inetAddress instanceof Inet6Address;
    }

    private static boolean isIpv4Address(InetAddress inetAddress) {
        return inetAddress instanceof Inet4Address;
    }

    private static boolean isIpv6Address6To4(InetAddress inetAddress) {
        if (!isIpv6Address(inetAddress)) {
            return false;
        }
        byte[] address = inetAddress.getAddress();
        if (address[0] == 32 && address[1] == 2) {
            return true;
        }
        return false;
    }

    private static boolean isIpv6AddressTeredo(InetAddress inetAddress) {
        if (!isIpv6Address(inetAddress)) {
            return false;
        }
        byte[] address = inetAddress.getAddress();
        if (address[0] == 32 && address[1] == 1 && address[2] == 0 && address[3] == 0) {
            return true;
        }
        return false;
    }

    private static boolean isIpv6AddressULA(InetAddress inetAddress) {
        return isIpv6Address(inetAddress) && (inetAddress.getAddress()[0] & 254) == 252;
    }

    private static boolean isIpv6Address6Bone(InetAddress inetAddress) {
        if (!isIpv6Address(inetAddress)) {
            return false;
        }
        byte[] address = inetAddress.getAddress();
        if (address[0] == 63 && address[1] == -2) {
            return true;
        }
        return false;
    }

    private static int getIpv6MulticastScope(InetAddress inetAddress) {
        if (!isIpv6Address(inetAddress)) {
            return 0;
        }
        return inetAddress.getAddress()[1] & 15;
    }

    /* access modifiers changed from: private */
    public static int findScope(InetAddress inetAddress) {
        if (isIpv6Address(inetAddress)) {
            if (inetAddress.isMulticastAddress()) {
                return getIpv6MulticastScope(inetAddress);
            }
            if (inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress()) {
                return 2;
            }
            if (inetAddress.isSiteLocalAddress()) {
                return 5;
            }
            return 14;
        } else if (isIpv4Address(inetAddress)) {
            return (inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress()) ? 2 : 14;
        } else {
            return 1;
        }
    }

    /* access modifiers changed from: private */
    public static int findPrecedence(InetAddress inetAddress) {
        if (isIpv4Address(inetAddress)) {
            return 35;
        }
        if (isIpv6Address(inetAddress)) {
            if (inetAddress.isLoopbackAddress()) {
                return 50;
            }
            if (isIpv6Address6To4(inetAddress)) {
                return 30;
            }
            if (isIpv6AddressTeredo(inetAddress)) {
                return 5;
            }
            if (isIpv6AddressULA(inetAddress)) {
                return 3;
            }
            if (((Inet6Address) inetAddress).isIPv4CompatibleAddress() || inetAddress.isSiteLocalAddress() || isIpv6Address6Bone(inetAddress)) {
                return 1;
            }
            return 40;
        }
        return 1;
    }

    /* access modifiers changed from: private */
    public static int compareIpv6PrefixMatchLen(InetAddress inetAddress, InetAddress inetAddress2) {
        byte[] address = inetAddress.getAddress();
        byte[] address2 = inetAddress2.getAddress();
        if (address.length != address2.length) {
            return 0;
        }
        for (int i = 0; i < address2.length; i++) {
            byte b = address[i];
            byte b2 = address2[i];
            if (b != b2) {
                return (i * 8) + (Integer.numberOfLeadingZeros((b & 255) ^ (b2 & 255)) - 24);
            }
        }
        return address2.length * 8;
    }

    public static boolean haveIpv4(Network network) {
        return checkConnectivity(network, OsConstants.AF_INET, new InetSocketAddress(InetAddresses.parseNumericAddress("8.8.8.8"), 0));
    }

    public static boolean haveIpv6(Network network) {
        return checkConnectivity(network, OsConstants.AF_INET6, new InetSocketAddress(InetAddresses.parseNumericAddress("2000::"), 0));
    }

    private static boolean checkConnectivity(Network network, int i, SocketAddress socketAddress) {
        try {
            FileDescriptor socket = C0308Os.socket(i, OsConstants.SOCK_DGRAM, OsConstants.IPPROTO_UDP);
            if (network != null) {
                try {
                    network.bindSocket(socket);
                } catch (ErrnoException | IOException unused) {
                    IoUtils.closeQuietly(socket);
                    return false;
                } catch (Throwable th) {
                    IoUtils.closeQuietly(socket);
                    throw th;
                }
            }
            C0308Os.connect(socket, socketAddress);
            IoUtils.closeQuietly(socket);
            return true;
        } catch (ErrnoException unused2) {
            return false;
        }
    }
}

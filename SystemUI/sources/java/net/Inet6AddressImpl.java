package java.net;

import android.system.ErrnoException;
import android.system.GaiException;
import android.system.OsConstants;
import android.system.StructAddrinfo;
import dalvik.system.BlockGuard;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.util.Enumeration;
import libcore.net.InetAddressUtils;
import libcore.p030io.IoBridge;
import libcore.p030io.Libcore;

class Inet6AddressImpl implements InetAddressImpl {
    private static final AddressCache addressCache = new AddressCache();
    private static InetAddress anyLocalAddress;
    private static InetAddress[] loopbackAddresses;

    Inet6AddressImpl() {
    }

    public InetAddress[] lookupAllHostAddr(String str, int i) throws UnknownHostException {
        if (str == null || str.isEmpty()) {
            return loopbackAddresses();
        }
        InetAddress parseNumericAddressNoThrowStripOptionalBrackets = InetAddressUtils.parseNumericAddressNoThrowStripOptionalBrackets(str);
        if (parseNumericAddressNoThrowStripOptionalBrackets == null) {
            return lookupHostByName(str, i);
        }
        return new InetAddress[]{parseNumericAddressNoThrowStripOptionalBrackets};
    }

    private static InetAddress[] lookupHostByName(String str, int i) throws UnknownHostException {
        int i2;
        BlockGuard.getThreadPolicy().onNetwork();
        Object obj = addressCache.get(str, i);
        if (obj == null) {
            try {
                StructAddrinfo structAddrinfo = new StructAddrinfo();
                structAddrinfo.ai_flags = OsConstants.AI_ADDRCONFIG;
                structAddrinfo.ai_family = OsConstants.AF_UNSPEC;
                structAddrinfo.ai_socktype = OsConstants.SOCK_STREAM;
                InetAddress[] android_getaddrinfo = Libcore.f857os.android_getaddrinfo(str, structAddrinfo, i);
                for (InetAddress inetAddress : android_getaddrinfo) {
                    inetAddress.holder().hostName = str;
                    inetAddress.holder().originalHostName = str;
                }
                addressCache.put(str, i, android_getaddrinfo);
                return android_getaddrinfo;
            } catch (GaiException e) {
                if (!(e.getCause() instanceof ErrnoException) || !((i2 = ((ErrnoException) e.getCause()).errno) == OsConstants.EACCES || i2 == OsConstants.EPERM)) {
                    String str2 = "Unable to resolve host \"" + str + "\": " + Libcore.f857os.gai_strerror(e.error);
                    addressCache.putUnknownHost(str, i, str2);
                    throw e.rethrowAsUnknownHostException(str2);
                }
                throw new SecurityException("Permission denied (missing INTERNET permission?)", e);
            }
        } else if (obj instanceof InetAddress[]) {
            return (InetAddress[]) obj;
        } else {
            throw new UnknownHostException((String) obj);
        }
    }

    public String getHostByAddr(byte[] bArr) throws UnknownHostException {
        BlockGuard.getThreadPolicy().onNetwork();
        return getHostByAddr0(bArr);
    }

    public void clearAddressCache() {
        addressCache.clear();
    }

    public boolean isReachable(InetAddress inetAddress, int i, NetworkInterface networkInterface, int i2) throws IOException {
        InetAddress inetAddress2 = null;
        if (networkInterface != null) {
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (true) {
                if (!inetAddresses.hasMoreElements()) {
                    break;
                }
                InetAddress nextElement = inetAddresses.nextElement();
                if (nextElement.getClass().isInstance(inetAddress)) {
                    inetAddress2 = nextElement;
                    break;
                }
            }
            if (inetAddress2 == null) {
                return false;
            }
        }
        if (icmpEcho(inetAddress, i, inetAddress2, i2)) {
            return true;
        }
        return tcpEcho(inetAddress, i, inetAddress2, i2);
    }

    private boolean tcpEcho(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        boolean z = true;
        FileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = IoBridge.socket(OsConstants.AF_INET6, OsConstants.SOCK_STREAM, 0);
            if (i2 > 0) {
                IoBridge.setSocketOption(fileDescriptor, 25, Integer.valueOf(i2));
            }
            if (inetAddress2 != null) {
                IoBridge.bind(fileDescriptor, inetAddress2, 0);
            }
            IoBridge.connect(fileDescriptor, inetAddress, 7, i);
            return true;
        } catch (IOException e) {
            Throwable cause = e.getCause();
            if (!(cause instanceof ErrnoException) || ((ErrnoException) cause).errno != OsConstants.ECONNREFUSED) {
                z = false;
            }
            return z;
        } finally {
            IoBridge.closeAndSignalBlockedThreads(fileDescriptor);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00ce, code lost:
        if (r2 != null) goto L_0x00d0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
        libcore.p030io.Libcore.f857os.close(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00df, code lost:
        if (r2 != null) goto L_0x00d0;
     */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00d9 A[SYNTHETIC, Splitter:B:58:0x00d9] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean icmpEcho(java.net.InetAddress r22, int r23, java.net.InetAddress r24, int r25) throws java.p026io.IOException {
        /*
            r21 = this;
            r0 = r22
            r1 = r24
            r8 = 0
            r2 = 0
            boolean r9 = r0 instanceof java.net.Inet4Address     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            if (r9 == 0) goto L_0x000d
            int r3 = android.system.OsConstants.AF_INET     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            goto L_0x000f
        L_0x000d:
            int r3 = android.system.OsConstants.AF_INET6     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
        L_0x000f:
            if (r9 == 0) goto L_0x0014
            int r4 = android.system.OsConstants.IPPROTO_ICMP     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            goto L_0x0016
        L_0x0014:
            int r4 = android.system.OsConstants.IPPROTO_ICMPV6     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
        L_0x0016:
            int r5 = android.system.OsConstants.SOCK_DGRAM     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            java.io.FileDescriptor r15 = libcore.p030io.IoBridge.socket(r3, r5, r4)     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            if (r25 <= 0) goto L_0x002f
            java.lang.Integer r2 = java.lang.Integer.valueOf((int) r25)     // Catch:{ IOException -> 0x002c, all -> 0x0028 }
            r3 = 25
            libcore.p030io.IoBridge.setSocketOption(r15, r3, r2)     // Catch:{ IOException -> 0x002c, all -> 0x0028 }
            goto L_0x002f
        L_0x0028:
            r0 = move-exception
            r2 = r15
            goto L_0x00d7
        L_0x002c:
            r2 = r15
            goto L_0x00df
        L_0x002f:
            if (r1 == 0) goto L_0x0034
            libcore.p030io.IoBridge.bind(r15, r1, r8)     // Catch:{ IOException -> 0x002c, all -> 0x0028 }
        L_0x0034:
            r18 = 1
            r14 = r23
            r13 = r18
        L_0x003a:
            if (r14 <= 0) goto L_0x00cd
            r1 = 1000(0x3e8, float:1.401E-42)
            if (r14 < r1) goto L_0x0043
            r19 = r1
            goto L_0x0045
        L_0x0043:
            r19 = r14
        L_0x0045:
            java.lang.Integer r1 = java.lang.Integer.valueOf((int) r19)     // Catch:{ IOException -> 0x002c, all -> 0x0028 }
            r2 = 4102(0x1006, float:5.748E-42)
            libcore.p030io.IoBridge.setSocketOption(r15, r2, r1)     // Catch:{ IOException -> 0x002c, all -> 0x0028 }
            byte[] r12 = android.system.IcmpHeaders.createIcmpEchoHdr(r9, r13)     // Catch:{ IOException -> 0x002c, all -> 0x0028 }
            r3 = 0
            int r4 = r12.length     // Catch:{ IOException -> 0x002c, all -> 0x0028 }
            r5 = 0
            r7 = 0
            r1 = r15
            r2 = r12
            r6 = r22
            libcore.p030io.IoBridge.sendto(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ IOException -> 0x002c, all -> 0x0028 }
            java.net.InetSocketAddress r1 = libcore.p030io.IoBridge.getLocalInetSocketAddress(r15)     // Catch:{ IOException -> 0x002c, all -> 0x0028 }
            int r1 = r1.getPort()     // Catch:{ IOException -> 0x002c, all -> 0x0028 }
            int r2 = r12.length     // Catch:{ IOException -> 0x002c, all -> 0x0028 }
            byte[] r3 = new byte[r2]     // Catch:{ IOException -> 0x002c, all -> 0x0028 }
            java.net.DatagramPacket r4 = new java.net.DatagramPacket     // Catch:{ IOException -> 0x002c, all -> 0x0028 }
            int r5 = r12.length     // Catch:{ IOException -> 0x002c, all -> 0x0028 }
            r4.<init>(r3, r5)     // Catch:{ IOException -> 0x002c, all -> 0x0028 }
            r10 = 1
            r5 = 0
            r6 = 0
            r17 = 0
            r11 = r15
            r7 = r12
            r12 = r3
            r20 = r13
            r13 = r5
            r5 = r14
            r14 = r2
            r2 = r15
            r15 = r6
            r16 = r4
            int r6 = libcore.p030io.IoBridge.recvfrom(r10, r11, r12, r13, r14, r15, r16, r17)     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            int r7 = r7.length     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            if (r6 != r7) goto L_0x00c4
            if (r9 == 0) goto L_0x008c
            int r6 = android.system.OsConstants.ICMP_ECHOREPLY     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
        L_0x008a:
            byte r6 = (byte) r6     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            goto L_0x008f
        L_0x008c:
            int r6 = android.system.OsConstants.ICMP6_ECHO_REPLY     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            goto L_0x008a
        L_0x008f:
            java.net.InetAddress r4 = r4.getAddress()     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            boolean r4 = r4.equals(r0)     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            if (r4 == 0) goto L_0x00c4
            byte r4 = r3[r8]     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            if (r4 != r6) goto L_0x00c4
            r4 = 4
            byte r4 = r3[r4]     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            int r6 = r1 >> 8
            byte r6 = (byte) r6     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            if (r4 != r6) goto L_0x00c4
            r4 = 5
            byte r4 = r3[r4]     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            byte r1 = (byte) r1     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            if (r4 != r1) goto L_0x00c4
            r1 = 6
            byte r1 = r3[r1]     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            r1 = r1 & 255(0xff, float:3.57E-43)
            int r1 = r1 << 8
            r4 = 7
            byte r3 = r3[r4]     // Catch:{ IOException -> 0x00df, all -> 0x00d6 }
            r3 = r3 & 255(0xff, float:3.57E-43)
            int r1 = r1 + r3
            r3 = r20
            if (r1 > r3) goto L_0x00c6
            if (r2 == 0) goto L_0x00c3
            libcore.io.Os r0 = libcore.p030io.Libcore.f857os     // Catch:{ ErrnoException -> 0x00c3 }
            r0.close(r2)     // Catch:{ ErrnoException -> 0x00c3 }
        L_0x00c3:
            return r18
        L_0x00c4:
            r3 = r20
        L_0x00c6:
            int r14 = r5 - r19
            int r13 = r3 + 1
            r15 = r2
            goto L_0x003a
        L_0x00cd:
            r2 = r15
            if (r2 == 0) goto L_0x00e2
        L_0x00d0:
            libcore.io.Os r0 = libcore.p030io.Libcore.f857os     // Catch:{ ErrnoException -> 0x00e2 }
            r0.close(r2)     // Catch:{ ErrnoException -> 0x00e2 }
            goto L_0x00e2
        L_0x00d6:
            r0 = move-exception
        L_0x00d7:
            if (r2 == 0) goto L_0x00de
            libcore.io.Os r1 = libcore.p030io.Libcore.f857os     // Catch:{ ErrnoException -> 0x00de }
            r1.close(r2)     // Catch:{ ErrnoException -> 0x00de }
        L_0x00de:
            throw r0
        L_0x00df:
            if (r2 == 0) goto L_0x00e2
            goto L_0x00d0
        L_0x00e2:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.Inet6AddressImpl.icmpEcho(java.net.InetAddress, int, java.net.InetAddress, int):boolean");
    }

    public InetAddress anyLocalAddress() {
        InetAddress inetAddress;
        synchronized (Inet6AddressImpl.class) {
            if (anyLocalAddress == null) {
                Inet6Address inet6Address = new Inet6Address();
                inet6Address.holder().hostName = "::";
                anyLocalAddress = inet6Address;
            }
            inetAddress = anyLocalAddress;
        }
        return inetAddress;
    }

    public InetAddress[] loopbackAddresses() {
        InetAddress[] inetAddressArr;
        synchronized (Inet6AddressImpl.class) {
            if (loopbackAddresses == null) {
                loopbackAddresses = new InetAddress[]{Inet6Address.LOOPBACK, Inet4Address.LOOPBACK};
            }
            inetAddressArr = loopbackAddresses;
        }
        return inetAddressArr;
    }

    private String getHostByAddr0(byte[] bArr) throws UnknownHostException {
        InetAddress byAddress = InetAddress.getByAddress(bArr);
        try {
            return Libcore.f857os.getnameinfo(byAddress, OsConstants.NI_NAMEREQD);
        } catch (GaiException e) {
            UnknownHostException unknownHostException = new UnknownHostException(byAddress.toString());
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }
}

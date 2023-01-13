package java.net;

import android.system.OsConstants;
import java.p026io.IOException;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamField;
import java.util.Arrays;
import java.util.Enumeration;
import libcore.p030io.Libcore;
import sun.misc.Unsafe;
import sun.security.util.DerValue;

public final class Inet6Address extends InetAddress {
    public static final InetAddress ANY = new Inet6Address("::", new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 0);
    private static final long FIELDS_OFFSET;
    static final int INADDRSZ = 16;
    private static final int INT16SZ = 2;
    public static final InetAddress LOOPBACK = new Inet6Address("ip6-localhost", new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 0);
    private static final Unsafe UNSAFE;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("ipaddress", byte[].class), new ObjectStreamField("scope_id", Integer.TYPE), new ObjectStreamField("scope_id_set", Boolean.TYPE), new ObjectStreamField("scope_ifname_set", Boolean.TYPE), new ObjectStreamField("ifname", String.class)};
    private static final long serialVersionUID = 6880410070516793377L;
    private final transient Inet6AddressHolder holder6;

    static {
        try {
            Unsafe unsafe = Unsafe.getUnsafe();
            FIELDS_OFFSET = unsafe.objectFieldOffset(Inet6Address.class.getDeclaredField("holder6"));
            UNSAFE = unsafe;
        } catch (ReflectiveOperationException e) {
            throw new Error((Throwable) e);
        }
    }

    private class Inet6AddressHolder {
        byte[] ipaddress;
        int scope_id;
        boolean scope_id_set;
        NetworkInterface scope_ifname;
        boolean scope_ifname_set;

        private Inet6AddressHolder() {
            this.ipaddress = new byte[16];
        }

        private Inet6AddressHolder(byte[] bArr, int i, boolean z, NetworkInterface networkInterface, boolean z2) {
            this.ipaddress = bArr;
            this.scope_id = i;
            this.scope_id_set = z;
            this.scope_ifname_set = z2;
            this.scope_ifname = networkInterface;
        }

        /* access modifiers changed from: package-private */
        public void setAddr(byte[] bArr) {
            if (bArr.length == 16) {
                System.arraycopy((Object) bArr, 0, (Object) this.ipaddress, 0, 16);
            }
        }

        /* access modifiers changed from: package-private */
        public void init(byte[] bArr, int i) {
            setAddr(bArr);
            if (i > 0) {
                this.scope_id = i;
                this.scope_id_set = true;
            }
        }

        /* access modifiers changed from: package-private */
        public void init(byte[] bArr, NetworkInterface networkInterface) throws UnknownHostException {
            setAddr(bArr);
            if (networkInterface != null) {
                this.scope_id = Inet6Address.deriveNumericScope(this.ipaddress, networkInterface);
                this.scope_id_set = true;
                this.scope_ifname = networkInterface;
                this.scope_ifname_set = true;
            }
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Inet6AddressHolder)) {
                return false;
            }
            return Arrays.equals(this.ipaddress, ((Inet6AddressHolder) obj).ipaddress);
        }

        public int hashCode() {
            if (this.ipaddress == null) {
                return 0;
            }
            int i = 0;
            int i2 = 0;
            while (i < 16) {
                int i3 = 0;
                int i4 = 0;
                while (i3 < 4 && i < 16) {
                    i4 = (i4 << 8) + this.ipaddress[i];
                    i3++;
                    i++;
                }
                i2 += i4;
            }
            return i2;
        }

        /* access modifiers changed from: package-private */
        public boolean isIPv4CompatibleAddress() {
            byte[] bArr = this.ipaddress;
            return bArr[0] == 0 && bArr[1] == 0 && bArr[2] == 0 && bArr[3] == 0 && bArr[4] == 0 && bArr[5] == 0 && bArr[6] == 0 && bArr[7] == 0 && bArr[8] == 0 && bArr[9] == 0 && bArr[10] == 0 && bArr[11] == 0;
        }

        /* access modifiers changed from: package-private */
        public boolean isMulticastAddress() {
            return (this.ipaddress[0] & 255) == 255;
        }

        /* access modifiers changed from: package-private */
        public boolean isAnyLocalAddress() {
            byte b = 0;
            for (int i = 0; i < 16; i++) {
                b = (byte) (b | this.ipaddress[i]);
            }
            return b == 0;
        }

        /* access modifiers changed from: package-private */
        public boolean isLoopbackAddress() {
            byte b = 0;
            for (int i = 0; i < 15; i++) {
                b = (byte) (b | this.ipaddress[i]);
            }
            if (b == 0 && this.ipaddress[15] == 1) {
                return true;
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public boolean isLinkLocalAddress() {
            byte[] bArr = this.ipaddress;
            return (bArr[0] & 255) == 254 && (bArr[1] & DerValue.TAG_PRIVATE) == 128;
        }

        /* access modifiers changed from: package-private */
        public boolean isSiteLocalAddress() {
            byte[] bArr = this.ipaddress;
            return (bArr[0] & 255) == 254 && (bArr[1] & DerValue.TAG_PRIVATE) == 192;
        }

        /* access modifiers changed from: package-private */
        public boolean isMCGlobal() {
            byte[] bArr = this.ipaddress;
            return (bArr[0] & 255) == 255 && (bArr[1] & 15) == 14;
        }

        /* access modifiers changed from: package-private */
        public boolean isMCNodeLocal() {
            byte[] bArr = this.ipaddress;
            return (bArr[0] & 255) == 255 && (bArr[1] & 15) == 1;
        }

        /* access modifiers changed from: package-private */
        public boolean isMCLinkLocal() {
            byte[] bArr = this.ipaddress;
            return (bArr[0] & 255) == 255 && (bArr[1] & 15) == 2;
        }

        /* access modifiers changed from: package-private */
        public boolean isMCSiteLocal() {
            byte[] bArr = this.ipaddress;
            return (bArr[0] & 255) == 255 && (bArr[1] & 15) == 5;
        }

        /* access modifiers changed from: package-private */
        public boolean isMCOrgLocal() {
            byte[] bArr = this.ipaddress;
            return (bArr[0] & 255) == 255 && (bArr[1] & 15) == 8;
        }
    }

    Inet6Address() {
        this.holder.init((String) null, OsConstants.AF_INET6);
        this.holder6 = new Inet6AddressHolder();
    }

    Inet6Address(String str, byte[] bArr, int i) {
        this.holder.init(str, OsConstants.AF_INET6);
        Inet6AddressHolder inet6AddressHolder = new Inet6AddressHolder();
        this.holder6 = inet6AddressHolder;
        inet6AddressHolder.init(bArr, i);
    }

    Inet6Address(String str, byte[] bArr) {
        this.holder6 = new Inet6AddressHolder();
        try {
            initif(str, bArr, (NetworkInterface) null);
        } catch (UnknownHostException unused) {
        }
    }

    Inet6Address(String str, byte[] bArr, NetworkInterface networkInterface) throws UnknownHostException {
        this.holder6 = new Inet6AddressHolder();
        initif(str, bArr, networkInterface);
    }

    Inet6Address(String str, byte[] bArr, String str2) throws UnknownHostException {
        this.holder6 = new Inet6AddressHolder();
        initstr(str, bArr, str2);
    }

    public static Inet6Address getByAddress(String str, byte[] bArr, NetworkInterface networkInterface) throws UnknownHostException {
        if (str != null && str.length() > 0 && str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']') {
            str = str.substring(1, str.length() - 1);
        }
        if (bArr != null && bArr.length == 16) {
            return new Inet6Address(str, bArr, networkInterface);
        }
        throw new UnknownHostException("addr is of illegal length");
    }

    public static Inet6Address getByAddress(String str, byte[] bArr, int i) throws UnknownHostException {
        if (str != null && str.length() > 0 && str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']') {
            str = str.substring(1, str.length() - 1);
        }
        if (bArr != null && bArr.length == 16) {
            return new Inet6Address(str, bArr, i);
        }
        throw new UnknownHostException("addr is of illegal length");
    }

    private void initstr(String str, byte[] bArr, String str2) throws UnknownHostException {
        try {
            NetworkInterface byName = NetworkInterface.getByName(str2);
            if (byName != null) {
                initif(str, bArr, byName);
                return;
            }
            throw new UnknownHostException("no such interface " + str2);
        } catch (SocketException unused) {
            throw new UnknownHostException("SocketException thrown" + str2);
        }
    }

    private void initif(String str, byte[] bArr, NetworkInterface networkInterface) throws UnknownHostException {
        this.holder6.init(bArr, networkInterface);
        this.holder.init(str, bArr.length == 16 ? OsConstants.AF_INET6 : -1);
    }

    private static boolean isDifferentLocalAddressType(byte[] bArr, byte[] bArr2) {
        if (isLinkLocalAddress(bArr) && !isLinkLocalAddress(bArr2)) {
            return false;
        }
        if (!isSiteLocalAddress(bArr) || isSiteLocalAddress(bArr2)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public static int deriveNumericScope(byte[] bArr, NetworkInterface networkInterface) throws UnknownHostException {
        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
            InetAddress nextElement = inetAddresses.nextElement();
            if (nextElement instanceof Inet6Address) {
                Inet6Address inet6Address = (Inet6Address) nextElement;
                if (isDifferentLocalAddressType(bArr, inet6Address.getAddress())) {
                    return inet6Address.getScopeId();
                }
            }
        }
        throw new UnknownHostException("no scope_id found");
    }

    private int deriveNumericScope(String str) throws UnknownHostException {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface nextElement = networkInterfaces.nextElement();
                if (nextElement.getName().equals(str)) {
                    return deriveNumericScope(this.holder6.ipaddress, nextElement);
                }
            }
            throw new UnknownHostException("No matching address found for interface : " + str);
        } catch (SocketException unused) {
            throw new UnknownHostException("could not enumerate local network interfaces");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x006c  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0090  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readObject(java.p026io.ObjectInputStream r15) throws java.p026io.IOException, java.lang.ClassNotFoundException {
        /*
            r14 = this;
            java.lang.Class r0 = r14.getClass()
            java.lang.ClassLoader r0 = r0.getClassLoader()
            java.lang.Class<java.lang.Class> r1 = java.lang.Class.class
            java.lang.ClassLoader r1 = r1.getClassLoader()
            if (r0 != r1) goto L_0x00a5
            java.io.ObjectInputStream$GetField r15 = r15.readFields()
            java.lang.String r0 = "ipaddress"
            r1 = 0
            java.lang.Object r0 = r15.get((java.lang.String) r0, (java.lang.Object) r1)
            byte[] r0 = (byte[]) r0
            java.lang.String r2 = "scope_id"
            r3 = -1
            int r2 = r15.get((java.lang.String) r2, (int) r3)
            java.lang.String r3 = "scope_id_set"
            r4 = 0
            boolean r3 = r15.get((java.lang.String) r3, (boolean) r4)
            java.lang.String r5 = "scope_ifname_set"
            boolean r5 = r15.get((java.lang.String) r5, (boolean) r4)
            java.lang.String r6 = "ifname"
            java.lang.Object r15 = r15.get((java.lang.String) r6, (java.lang.Object) r1)
            java.lang.String r15 = (java.lang.String) r15
            if (r15 == 0) goto L_0x005c
            java.lang.String r6 = ""
            boolean r6 = r6.equals(r15)
            if (r6 != 0) goto L_0x005c
            java.net.NetworkInterface r1 = java.net.NetworkInterface.getByName(r15)     // Catch:{ SocketException -> 0x005c }
            if (r1 != 0) goto L_0x004c
            r15 = r4
            r2 = r15
            goto L_0x0057
        L_0x004c:
            r4 = 1
            int r15 = deriveNumericScope(r0, r1)     // Catch:{ UnknownHostException -> 0x0052, SocketException -> 0x0055 }
            r2 = r15
        L_0x0052:
            r15 = r4
            r4 = r3
            goto L_0x0057
        L_0x0055:
            r5 = r4
            goto L_0x005c
        L_0x0057:
            r12 = r15
            r11 = r1
            r9 = r2
            r10 = r4
            goto L_0x0060
        L_0x005c:
            r11 = r1
            r9 = r2
            r10 = r3
            r12 = r5
        L_0x0060:
            java.lang.Object r15 = r0.clone()
            r8 = r15
            byte[] r8 = (byte[]) r8
            int r15 = r8.length
            r0 = 16
            if (r15 != r0) goto L_0x0090
            java.net.InetAddress$InetAddressHolder r15 = r14.holder()
            int r15 = r15.getFamily()
            int r0 = android.system.OsConstants.AF_INET6
            if (r15 != r0) goto L_0x0088
            java.net.Inet6Address$Inet6AddressHolder r15 = new java.net.Inet6Address$Inet6AddressHolder
            r13 = 0
            r6 = r15
            r7 = r14
            r6.<init>(r8, r9, r10, r11, r12)
            sun.misc.Unsafe r0 = UNSAFE
            long r1 = FIELDS_OFFSET
            r0.putObject(r14, r1, r15)
            return
        L_0x0088:
            java.io.InvalidObjectException r14 = new java.io.InvalidObjectException
            java.lang.String r15 = "invalid address family type"
            r14.<init>(r15)
            throw r14
        L_0x0090:
            java.io.InvalidObjectException r14 = new java.io.InvalidObjectException
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            java.lang.String r0 = "invalid address length: "
            r15.<init>((java.lang.String) r0)
            int r0 = r8.length
            r15.append((int) r0)
            java.lang.String r15 = r15.toString()
            r14.<init>(r15)
            throw r14
        L_0x00a5:
            java.lang.SecurityException r14 = new java.lang.SecurityException
            java.lang.String r15 = "invalid address type"
            r14.<init>((java.lang.String) r15)
            throw r14
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.Inet6Address.readObject(java.io.ObjectInputStream):void");
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        String str;
        if (this.holder6.scope_ifname != null) {
            str = this.holder6.scope_ifname.getName();
            this.holder6.scope_ifname_set = true;
        } else {
            str = null;
        }
        ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
        putFields.put("ipaddress", (Object) this.holder6.ipaddress);
        putFields.put("scope_id", this.holder6.scope_id);
        putFields.put("scope_id_set", this.holder6.scope_id_set);
        putFields.put("scope_ifname_set", this.holder6.scope_ifname_set);
        putFields.put("ifname", (Object) str);
        objectOutputStream.writeFields();
    }

    public boolean isMulticastAddress() {
        return this.holder6.isMulticastAddress();
    }

    public boolean isAnyLocalAddress() {
        return this.holder6.isAnyLocalAddress();
    }

    public boolean isLoopbackAddress() {
        return this.holder6.isLoopbackAddress();
    }

    public boolean isLinkLocalAddress() {
        return this.holder6.isLinkLocalAddress();
    }

    static boolean isLinkLocalAddress(byte[] bArr) {
        return (bArr[0] & 255) == 254 && (bArr[1] & DerValue.TAG_PRIVATE) == 128;
    }

    public boolean isSiteLocalAddress() {
        return this.holder6.isSiteLocalAddress();
    }

    static boolean isSiteLocalAddress(byte[] bArr) {
        return (bArr[0] & 255) == 254 && (bArr[1] & DerValue.TAG_PRIVATE) == 192;
    }

    public boolean isMCGlobal() {
        return this.holder6.isMCGlobal();
    }

    public boolean isMCNodeLocal() {
        return this.holder6.isMCNodeLocal();
    }

    public boolean isMCLinkLocal() {
        return this.holder6.isMCLinkLocal();
    }

    public boolean isMCSiteLocal() {
        return this.holder6.isMCSiteLocal();
    }

    public boolean isMCOrgLocal() {
        return this.holder6.isMCOrgLocal();
    }

    public byte[] getAddress() {
        return (byte[]) this.holder6.ipaddress.clone();
    }

    public int getScopeId() {
        return this.holder6.scope_id;
    }

    public NetworkInterface getScopedInterface() {
        return this.holder6.scope_ifname;
    }

    public String getHostAddress() {
        return Libcore.f855os.getnameinfo(this, OsConstants.NI_NUMERICHOST);
    }

    public int hashCode() {
        return this.holder6.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Inet6Address)) {
            return false;
        }
        return this.holder6.equals(((Inet6Address) obj).holder6);
    }

    public boolean isIPv4CompatibleAddress() {
        return this.holder6.isIPv4CompatibleAddress();
    }

    static String numericToTextFormat(byte[] bArr) {
        StringBuilder sb = new StringBuilder(39);
        for (int i = 0; i < 8; i++) {
            int i2 = i << 1;
            sb.append(Integer.toHexString(((bArr[i2] << 8) & 65280) | (bArr[i2 + 1] & 255)));
            if (i < 7) {
                sb.append(":");
            }
        }
        return sb.toString();
    }
}

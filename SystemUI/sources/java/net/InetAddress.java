package java.net;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamException;
import java.p026io.ObjectStreamField;
import java.p026io.Serializable;
import libcore.net.InetAddressUtils;
import libcore.p030io.Libcore;
import sun.net.spi.nameservice.NameService;
import sun.net.util.IPAddressUtil;

public class InetAddress implements Serializable {
    private static final ClassLoader BOOT_CLASSLOADER = Object.class.getClassLoader();
    static final int NETID_UNSET = 0;
    static final InetAddressImpl impl = new Inet6AddressImpl();
    private static final NameService nameService = new NameService() {
        public InetAddress[] lookupAllHostAddr(String str, int i) throws UnknownHostException {
            return InetAddress.impl.lookupAllHostAddr(str, i);
        }

        public String getHostByAddr(byte[] bArr) throws UnknownHostException {
            return InetAddress.impl.getHostByAddr(bArr);
        }
    };
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("hostName", String.class), new ObjectStreamField("address", Integer.TYPE), new ObjectStreamField("family", Integer.TYPE)};
    private static final long serialVersionUID = 3286316764910316507L;
    private transient String canonicalHostName = null;
    transient InetAddressHolder holder = new InetAddressHolder();

    public boolean equals(Object obj) {
        return false;
    }

    public byte[] getAddress() {
        return null;
    }

    public String getHostAddress() {
        return null;
    }

    public int hashCode() {
        return -1;
    }

    public boolean isAnyLocalAddress() {
        return false;
    }

    public boolean isLinkLocalAddress() {
        return false;
    }

    public boolean isLoopbackAddress() {
        return false;
    }

    public boolean isMCGlobal() {
        return false;
    }

    public boolean isMCLinkLocal() {
        return false;
    }

    public boolean isMCNodeLocal() {
        return false;
    }

    public boolean isMCOrgLocal() {
        return false;
    }

    public boolean isMCSiteLocal() {
        return false;
    }

    public boolean isMulticastAddress() {
        return false;
    }

    public boolean isSiteLocalAddress() {
        return false;
    }

    static class InetAddressHolder {
        int address;
        int family;
        String hostName;
        String originalHostName;

        InetAddressHolder() {
        }

        InetAddressHolder(String str, int i, int i2) {
            this.originalHostName = str;
            this.hostName = str;
            this.address = i;
            this.family = i2;
        }

        /* access modifiers changed from: package-private */
        public void init(String str, int i) {
            this.originalHostName = str;
            this.hostName = str;
            if (i != -1) {
                this.family = i;
            }
        }

        /* access modifiers changed from: package-private */
        public String getHostName() {
            return this.hostName;
        }

        /* access modifiers changed from: package-private */
        public String getOriginalHostName() {
            return this.originalHostName;
        }

        /* access modifiers changed from: package-private */
        public int getAddress() {
            return this.address;
        }

        /* access modifiers changed from: package-private */
        public int getFamily() {
            return this.family;
        }
    }

    /* access modifiers changed from: package-private */
    public InetAddressHolder holder() {
        return this.holder;
    }

    InetAddress() {
    }

    private Object readResolve() throws ObjectStreamException {
        return new Inet4Address(holder().getHostName(), holder().getAddress());
    }

    public boolean isReachable(int i) throws IOException {
        return isReachable((NetworkInterface) null, 0, i);
    }

    public boolean isReachable(NetworkInterface networkInterface, int i, int i2) throws IOException {
        if (i < 0) {
            throw new IllegalArgumentException("ttl can't be negative");
        } else if (i2 >= 0) {
            return impl.isReachable(this, i2, networkInterface, i);
        } else {
            throw new IllegalArgumentException("timeout can't be negative");
        }
    }

    public boolean isReachableByICMP(int i) throws IOException {
        return ((Inet6AddressImpl) impl).icmpEcho(this, i, (InetAddress) null, 0);
    }

    public String getHostName() {
        if (holder().getHostName() == null) {
            holder().hostName = getHostFromNameService(this);
        }
        return holder().getHostName();
    }

    public String getCanonicalHostName() {
        if (this.canonicalHostName == null) {
            this.canonicalHostName = getHostFromNameService(this);
        }
        return this.canonicalHostName;
    }

    private static String getHostFromNameService(InetAddress inetAddress) {
        try {
            NameService nameService2 = nameService;
            String hostByAddr = nameService2.getHostByAddr(inetAddress.getAddress());
            boolean z = false;
            InetAddress[] lookupAllHostAddr = nameService2.lookupAllHostAddr(hostByAddr, 0);
            if (lookupAllHostAddr != null) {
                int i = 0;
                while (!z && i < lookupAllHostAddr.length) {
                    z = inetAddress.equals(lookupAllHostAddr[i]);
                    i++;
                }
            }
            if (!z) {
                return inetAddress.getHostAddress();
            }
            return hostByAddr;
        } catch (UnknownHostException unused) {
            return inetAddress.getHostAddress();
        }
    }

    public String toString() {
        String hostName = holder().getHostName();
        StringBuilder sb = new StringBuilder();
        if (hostName == null) {
            hostName = "";
        }
        sb.append(hostName);
        sb.append("/");
        sb.append(getHostAddress());
        return sb.toString();
    }

    public static InetAddress getByAddress(String str, byte[] bArr) throws UnknownHostException {
        return getByAddress(str, bArr, -1);
    }

    private static InetAddress getByAddress(String str, byte[] bArr, int i) throws UnknownHostException {
        if (str != null && str.length() > 0 && str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']') {
            str = str.substring(1, str.length() - 1);
        }
        if (bArr != null) {
            if (bArr.length == 4) {
                return new Inet4Address(str, bArr);
            }
            if (bArr.length == 16) {
                byte[] convertFromIPv4MappedAddress = IPAddressUtil.convertFromIPv4MappedAddress(bArr);
                if (convertFromIPv4MappedAddress != null) {
                    return new Inet4Address(str, convertFromIPv4MappedAddress);
                }
                return new Inet6Address(str, bArr, i);
            }
        }
        throw new UnknownHostException("addr is of illegal length");
    }

    public static InetAddress getByName(String str) throws UnknownHostException {
        return impl.lookupAllHostAddr(str, 0)[0];
    }

    public static InetAddress[] getAllByName(String str) throws UnknownHostException {
        return (InetAddress[]) impl.lookupAllHostAddr(str, 0).clone();
    }

    public static InetAddress getLoopbackAddress() {
        return impl.loopbackAddresses()[0];
    }

    public static InetAddress getByAddress(byte[] bArr) throws UnknownHostException {
        return getByAddress((String) null, bArr);
    }

    public static InetAddress getLocalHost() throws UnknownHostException {
        return impl.lookupAllHostAddr(Libcore.f855os.uname().nodename, 0)[0];
    }

    static InetAddress anyLocalAddress() {
        return impl.anyLocalAddress();
    }

    private void readObjectNoData(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (getClass().getClassLoader() != BOOT_CLASSLOADER) {
            throw new SecurityException("invalid address type");
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (getClass().getClassLoader() == BOOT_CLASSLOADER) {
            ObjectInputStream.GetField readFields = objectInputStream.readFields();
            this.holder = new InetAddressHolder((String) readFields.get("hostName", (Object) null), readFields.get("address", 0), readFields.get("family", 0));
            return;
        }
        throw new SecurityException("invalid address type");
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (getClass().getClassLoader() == BOOT_CLASSLOADER) {
            ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
            putFields.put("hostName", (Object) holder().hostName);
            putFields.put("address", holder().address);
            putFields.put("family", holder().family);
            objectOutputStream.writeFields();
            objectOutputStream.flush();
            return;
        }
        throw new SecurityException("invalid address type");
    }

    @Deprecated
    public static boolean isNumeric(String str) {
        return InetAddressUtils.parseNumericAddressNoThrowStripOptionalBrackets(str) != null;
    }

    @Deprecated
    public static InetAddress parseNumericAddress(String str) {
        if (str == null || str.isEmpty()) {
            return Inet6Address.LOOPBACK;
        }
        InetAddress parseNumericAddressNoThrowStripOptionalBrackets = InetAddressUtils.parseNumericAddressNoThrowStripOptionalBrackets(str);
        if (parseNumericAddressNoThrowStripOptionalBrackets != null) {
            return parseNumericAddressNoThrowStripOptionalBrackets;
        }
        throw new IllegalArgumentException("Not a numeric address: " + str);
    }

    public static void clearDnsCache() {
        impl.clearAddressCache();
    }

    public static InetAddress getByNameOnNet(String str, int i) throws UnknownHostException {
        return impl.lookupAllHostAddr(str, i)[0];
    }

    public static InetAddress[] getAllByNameOnNet(String str, int i) throws UnknownHostException {
        return (InetAddress[]) impl.lookupAllHostAddr(str, i).clone();
    }
}

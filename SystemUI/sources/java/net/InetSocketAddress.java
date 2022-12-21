package java.net;

import android.net.connectivity.com.android.net.module.util.ConnectivitySettingsUtils;
import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamException;
import java.p026io.ObjectStreamField;
import jdk.internal.misc.Unsafe;

public class InetSocketAddress extends SocketAddress {
    private static final long FIELDS_OFFSET;
    private static final Unsafe UNSAFE;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField(ConnectivitySettingsUtils.PRIVATE_DNS_MODE_PROVIDER_HOSTNAME_STRING, String.class), new ObjectStreamField("addr", InetAddress.class), new ObjectStreamField("port", Integer.TYPE)};
    private static final long serialVersionUID = 5076001401234631237L;
    private final transient InetSocketAddressHolder holder;

    private static class InetSocketAddressHolder {
        /* access modifiers changed from: private */
        public InetAddress addr;
        /* access modifiers changed from: private */
        public String hostname;
        /* access modifiers changed from: private */
        public int port;

        private InetSocketAddressHolder(String str, InetAddress inetAddress, int i) {
            this.hostname = str;
            this.addr = inetAddress;
            this.port = i;
        }

        /* access modifiers changed from: private */
        public int getPort() {
            return this.port;
        }

        /* access modifiers changed from: private */
        public InetAddress getAddress() {
            return this.addr;
        }

        /* access modifiers changed from: private */
        public String getHostName() {
            String str = this.hostname;
            if (str != null) {
                return str;
            }
            InetAddress inetAddress = this.addr;
            if (inetAddress != null) {
                return inetAddress.getHostName();
            }
            return null;
        }

        /* access modifiers changed from: private */
        public String getHostString() {
            String str = this.hostname;
            if (str != null) {
                return str;
            }
            InetAddress inetAddress = this.addr;
            if (inetAddress == null) {
                return null;
            }
            if (inetAddress.holder().getHostName() != null) {
                return this.addr.holder().getHostName();
            }
            return this.addr.getHostAddress();
        }

        /* access modifiers changed from: private */
        public boolean isUnresolved() {
            return this.addr == null;
        }

        public String toString() {
            if (isUnresolved()) {
                return this.hostname + ":" + this.port;
            }
            return this.addr.toString() + ":" + this.port;
        }

        public final boolean equals(Object obj) {
            boolean z;
            if (obj == null || !(obj instanceof InetSocketAddressHolder)) {
                return false;
            }
            InetSocketAddressHolder inetSocketAddressHolder = (InetSocketAddressHolder) obj;
            InetAddress inetAddress = this.addr;
            if (inetAddress != null) {
                z = inetAddress.equals(inetSocketAddressHolder.addr);
            } else {
                String str = this.hostname;
                z = str == null ? inetSocketAddressHolder.addr == null && inetSocketAddressHolder.hostname == null : !(inetSocketAddressHolder.addr != null || !str.equalsIgnoreCase(inetSocketAddressHolder.hostname));
            }
            if (!z || this.port != inetSocketAddressHolder.port) {
                return false;
            }
            return true;
        }

        public final int hashCode() {
            int hashCode;
            int i;
            InetAddress inetAddress = this.addr;
            if (inetAddress != null) {
                hashCode = inetAddress.hashCode();
                i = this.port;
            } else {
                String str = this.hostname;
                if (str == null) {
                    return this.port;
                }
                hashCode = str.toLowerCase().hashCode();
                i = this.port;
            }
            return hashCode + i;
        }
    }

    private static int checkPort(int i) {
        if (i >= 0 && i <= 65535) {
            return i;
        }
        throw new IllegalArgumentException("port out of range:" + i);
    }

    private static String checkHost(String str) {
        if (str != null) {
            return str;
        }
        throw new IllegalArgumentException("hostname can't be null");
    }

    public InetSocketAddress() {
        this.holder = new InetSocketAddressHolder((String) null, (InetAddress) null, 0);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public InetSocketAddress(int i) {
        this((InetAddress) null, i);
        InetAddress inetAddress = null;
    }

    public InetSocketAddress(InetAddress inetAddress, int i) {
        this.holder = new InetSocketAddressHolder((String) null, inetAddress == null ? Inet6Address.ANY : inetAddress, checkPort(i));
    }

    public InetSocketAddress(String str, int i) {
        InetAddress inetAddress;
        checkHost(str);
        try {
            inetAddress = InetAddress.getByName(str);
            str = null;
        } catch (UnknownHostException unused) {
            inetAddress = null;
        }
        this.holder = new InetSocketAddressHolder(str, inetAddress, checkPort(i));
    }

    private InetSocketAddress(int i, String str) {
        this.holder = new InetSocketAddressHolder(str, (InetAddress) null, i);
    }

    public static InetSocketAddress createUnresolved(String str, int i) {
        return new InetSocketAddress(checkPort(i), checkHost(str));
    }

    static {
        Unsafe unsafe = Unsafe.getUnsafe();
        UNSAFE = unsafe;
        FIELDS_OFFSET = unsafe.objectFieldOffset(InetSocketAddress.class, "holder");
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
        putFields.put(ConnectivitySettingsUtils.PRIVATE_DNS_MODE_PROVIDER_HOSTNAME_STRING, (Object) this.holder.hostname);
        putFields.put("addr", (Object) this.holder.addr);
        putFields.put("port", this.holder.port);
        objectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField readFields = objectInputStream.readFields();
        String str = (String) readFields.get(ConnectivitySettingsUtils.PRIVATE_DNS_MODE_PROVIDER_HOSTNAME_STRING, (Object) null);
        InetAddress inetAddress = (InetAddress) readFields.get("addr", (Object) null);
        int i = readFields.get("port", -1);
        checkPort(i);
        if (str == null && inetAddress == null) {
            throw new InvalidObjectException("hostname and addr can't both be null");
        }
        UNSAFE.putObject(this, FIELDS_OFFSET, new InetSocketAddressHolder(str, inetAddress, i));
    }

    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("Stream data required");
    }

    public final int getPort() {
        return this.holder.getPort();
    }

    public final InetAddress getAddress() {
        return this.holder.getAddress();
    }

    public final String getHostName() {
        return this.holder.getHostName();
    }

    public final String getHostString() {
        return this.holder.getHostString();
    }

    public final boolean isUnresolved() {
        return this.holder.isUnresolved();
    }

    public String toString() {
        return this.holder.toString();
    }

    public final boolean equals(Object obj) {
        if (obj == null || !(obj instanceof InetSocketAddress)) {
            return false;
        }
        return this.holder.equals(((InetSocketAddress) obj).holder);
    }

    public final int hashCode() {
        return this.holder.hashCode();
    }
}

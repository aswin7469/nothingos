package java.net;

import android.compat.Compatibility;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.system.StructIfaddrs;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.FileDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import libcore.p030io.IoUtils;
import libcore.p030io.Libcore;

public final class NetworkInterface {
    private static final byte[] DEFAULT_MAC_ADDRESS = {2, 0, 0, 0, 0, 0};
    public static final long RETURN_NULL_HARDWARE_ADDRESS = 170188668;
    private static final int defaultIndex;
    private static final NetworkInterface defaultInterface;
    /* access modifiers changed from: private */
    public InetAddress[] addrs;
    private InterfaceAddress[] bindings;
    private List<NetworkInterface> childs;
    private String displayName;
    private byte[] hardwareAddr;
    private int index;
    private String name;
    private NetworkInterface parent = null;
    private boolean virtual = false;

    static {
        NetworkInterface networkInterface = DefaultInterface.getDefault();
        defaultInterface = networkInterface;
        if (networkInterface != null) {
            defaultIndex = networkInterface.getIndex();
        } else {
            defaultIndex = 0;
        }
    }

    NetworkInterface() {
    }

    NetworkInterface(String str, int i, InetAddress[] inetAddressArr) {
        this.name = str;
        this.index = i;
        this.addrs = inetAddressArr;
    }

    public String getName() {
        return this.name;
    }

    public Enumeration<InetAddress> getInetAddresses() {
        return new Enumeration<InetAddress>() {
            private int count = 0;

            /* renamed from: i */
            private int f555i = 0;
            private InetAddress[] local_addrs;

            {
                boolean z;
                this.local_addrs = new InetAddress[NetworkInterface.this.addrs.length];
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    try {
                        securityManager.checkPermission(new NetPermission("getNetworkInformation"));
                    } catch (SecurityException unused) {
                        z = false;
                    }
                }
                z = true;
                for (int i = 0; i < NetworkInterface.this.addrs.length; i++) {
                    if (securityManager != null && !z) {
                        try {
                            securityManager.checkConnect(NetworkInterface.this.addrs[i].getHostAddress(), -1);
                        } catch (SecurityException unused2) {
                        }
                    }
                    InetAddress[] inetAddressArr = this.local_addrs;
                    int i2 = this.count;
                    this.count = i2 + 1;
                    inetAddressArr[i2] = NetworkInterface.this.addrs[i];
                }
            }

            public InetAddress nextElement() {
                int i = this.f555i;
                if (i < this.count) {
                    InetAddress[] inetAddressArr = this.local_addrs;
                    this.f555i = i + 1;
                    return inetAddressArr[i];
                }
                throw new NoSuchElementException();
            }

            public boolean hasMoreElements() {
                return this.f555i < this.count;
            }
        };
    }

    public List<InterfaceAddress> getInterfaceAddresses() {
        ArrayList arrayList = new ArrayList(1);
        if (this.bindings != null) {
            SecurityManager securityManager = System.getSecurityManager();
            int i = 0;
            while (true) {
                InterfaceAddress[] interfaceAddressArr = this.bindings;
                if (i >= interfaceAddressArr.length) {
                    break;
                }
                if (securityManager != null) {
                    try {
                        securityManager.checkConnect(interfaceAddressArr[i].getAddress().getHostAddress(), -1);
                    } catch (SecurityException unused) {
                    }
                }
                arrayList.add(this.bindings[i]);
                i++;
            }
        }
        return arrayList;
    }

    public Enumeration<NetworkInterface> getSubInterfaces() {
        return Collections.enumeration(this.childs);
    }

    public NetworkInterface getParent() {
        return this.parent;
    }

    public int getIndex() {
        return this.index;
    }

    public String getDisplayName() {
        if ("".equals(this.displayName)) {
            return null;
        }
        return this.displayName;
    }

    public static NetworkInterface getByName(String str) throws SocketException {
        str.getClass();
        for (NetworkInterface networkInterface : getAll()) {
            if (networkInterface.getName().equals(str)) {
                return networkInterface;
            }
        }
        return null;
    }

    public static NetworkInterface getByIndex(int i) throws SocketException {
        if (i >= 0) {
            for (NetworkInterface networkInterface : getAll()) {
                if (networkInterface.getIndex() == i) {
                    return networkInterface;
                }
            }
            return null;
        }
        throw new IllegalArgumentException("Interface index can't be negative");
    }

    public static NetworkInterface getByInetAddress(InetAddress inetAddress) throws SocketException {
        inetAddress.getClass();
        if ((inetAddress instanceof Inet4Address) || (inetAddress instanceof Inet6Address)) {
            for (NetworkInterface networkInterface : getAll()) {
                Iterator<T> it = Collections.list(networkInterface.getInetAddresses()).iterator();
                while (it.hasNext()) {
                    if (((InetAddress) it.next()).equals(inetAddress)) {
                        return networkInterface;
                    }
                }
            }
            return null;
        }
        throw new IllegalArgumentException("invalid address type");
    }

    public static Enumeration<NetworkInterface> getNetworkInterfaces() throws SocketException {
        NetworkInterface[] all = getAll();
        if (all.length == 0) {
            return null;
        }
        return Collections.enumeration(Arrays.asList(all));
    }

    private static NetworkInterface[] getAll() throws SocketException {
        HashMap hashMap = new HashMap();
        try {
            StructIfaddrs[] structIfaddrsArr = Libcore.f855os.getifaddrs();
            if (structIfaddrsArr != null) {
                for (StructIfaddrs structIfaddrs : structIfaddrsArr) {
                    String str = structIfaddrs.ifa_name;
                    List list = (List) hashMap.get(str);
                    if (list == null) {
                        list = new ArrayList();
                        hashMap.put(str, list);
                    }
                    list.add(structIfaddrs);
                }
                HashMap hashMap2 = new HashMap(hashMap.size());
                for (Map.Entry entry : hashMap.entrySet()) {
                    String str2 = (String) entry.getKey();
                    int if_nametoindex = Libcore.f855os.if_nametoindex((String) entry.getKey());
                    if (if_nametoindex != 0) {
                        NetworkInterface networkInterface = new NetworkInterface(str2, if_nametoindex, (InetAddress[]) null);
                        networkInterface.displayName = str2;
                        ArrayList arrayList = new ArrayList();
                        ArrayList arrayList2 = new ArrayList();
                        for (StructIfaddrs structIfaddrs2 : (List) entry.getValue()) {
                            if (structIfaddrs2.ifa_addr != null) {
                                arrayList.add(structIfaddrs2.ifa_addr);
                                arrayList2.add(new InterfaceAddress(structIfaddrs2.ifa_addr, (Inet4Address) structIfaddrs2.ifa_broadaddr, structIfaddrs2.ifa_netmask));
                            }
                            if (structIfaddrs2.hwaddr != null) {
                                networkInterface.hardwareAddr = structIfaddrs2.hwaddr;
                            }
                        }
                        networkInterface.addrs = (InetAddress[]) arrayList.toArray(new InetAddress[arrayList.size()]);
                        networkInterface.bindings = (InterfaceAddress[]) arrayList2.toArray(new InterfaceAddress[arrayList2.size()]);
                        networkInterface.childs = new ArrayList(0);
                        hashMap2.put(str2, networkInterface);
                    }
                }
                for (Map.Entry value : hashMap2.entrySet()) {
                    NetworkInterface networkInterface2 = (NetworkInterface) value.getValue();
                    String name2 = networkInterface2.getName();
                    int indexOf = name2.indexOf(58);
                    if (indexOf != -1) {
                        NetworkInterface networkInterface3 = (NetworkInterface) hashMap2.get(name2.substring(0, indexOf));
                        networkInterface2.virtual = true;
                        if (networkInterface3 != null) {
                            networkInterface2.parent = networkInterface3;
                            networkInterface3.childs.add(networkInterface2);
                        }
                    }
                }
                return (NetworkInterface[]) hashMap2.values().toArray((T[]) new NetworkInterface[hashMap2.size()]);
            }
            throw new SocketException("Failed to query network interfaces.");
        } catch (ErrnoException e) {
            throw e.rethrowAsSocketException();
        }
    }

    public boolean isUp() throws SocketException {
        int i = OsConstants.IFF_UP | OsConstants.IFF_RUNNING;
        return (getFlags() & i) == i;
    }

    public boolean isLoopback() throws SocketException {
        return (getFlags() & OsConstants.IFF_LOOPBACK) != 0;
    }

    public boolean isPointToPoint() throws SocketException {
        return (getFlags() & OsConstants.IFF_POINTOPOINT) != 0;
    }

    public boolean supportsMulticast() throws SocketException {
        return (getFlags() & OsConstants.IFF_MULTICAST) != 0;
    }

    public byte[] getHardwareAddress() throws SocketException {
        NetworkInterface byName = getByName(this.name);
        if (byName == null) {
            throw new SocketException("NetworkInterface doesn't exist anymore");
        } else if (byName.hardwareAddr != null || "lo".equals(this.name) || Compatibility.isChangeEnabled(RETURN_NULL_HARDWARE_ADDRESS)) {
            return byName.hardwareAddr;
        } else {
            return (byte[]) DEFAULT_MAC_ADDRESS.clone();
        }
    }

    public int getMTU() throws SocketException {
        try {
            FileDescriptor socket = Libcore.rawOs.socket(OsConstants.AF_INET, OsConstants.SOCK_DGRAM, 0);
            int ioctlMTU = Libcore.rawOs.ioctlMTU(socket, this.name);
            IoUtils.closeQuietly(socket);
            return ioctlMTU;
        } catch (ErrnoException e) {
            throw e.rethrowAsSocketException();
        } catch (Exception e2) {
            throw new SocketException((Throwable) e2);
        } catch (Throwable th) {
            IoUtils.closeQuietly((FileDescriptor) null);
            throw th;
        }
    }

    public boolean isVirtual() {
        return this.virtual;
    }

    private int getFlags() throws SocketException {
        try {
            FileDescriptor socket = Libcore.rawOs.socket(OsConstants.AF_INET, OsConstants.SOCK_DGRAM, 0);
            int ioctlFlags = Libcore.rawOs.ioctlFlags(socket, this.name);
            IoUtils.closeQuietly(socket);
            return ioctlFlags;
        } catch (ErrnoException e) {
            throw e.rethrowAsSocketException();
        } catch (Exception e2) {
            throw new SocketException((Throwable) e2);
        } catch (Throwable th) {
            IoUtils.closeQuietly((FileDescriptor) null);
            throw th;
        }
    }

    public boolean equals(Object obj) {
        boolean z;
        if (!(obj instanceof NetworkInterface)) {
            return false;
        }
        NetworkInterface networkInterface = (NetworkInterface) obj;
        String str = this.name;
        if (str != null) {
            if (!str.equals(networkInterface.name)) {
                return false;
            }
        } else if (networkInterface.name != null) {
            return false;
        }
        InetAddress[] inetAddressArr = this.addrs;
        if (inetAddressArr != null) {
            InetAddress[] inetAddressArr2 = networkInterface.addrs;
            if (inetAddressArr2 == null || inetAddressArr.length != inetAddressArr2.length) {
                return false;
            }
            int length = inetAddressArr2.length;
            for (int i = 0; i < length; i++) {
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        z = false;
                        break;
                    } else if (this.addrs[i].equals(inetAddressArr2[i2])) {
                        z = true;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (!z) {
                    return false;
                }
            }
            return true;
        } else if (networkInterface.addrs == null) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        String str = this.name;
        if (str == null) {
            return 0;
        }
        return str.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("name:");
        String str = this.name;
        if (str == null) {
            str = "null";
        }
        sb.append(str);
        String sb2 = sb.toString();
        if (this.displayName == null) {
            return sb2;
        }
        return sb2 + " (" + this.displayName + NavigationBarInflaterView.KEY_CODE_END;
    }

    static NetworkInterface getDefault() {
        return defaultInterface;
    }
}

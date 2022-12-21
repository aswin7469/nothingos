package java.net;

import com.android.systemui.navigationbar.NavigationBarInflaterView;

public class InterfaceAddress {
    private InetAddress address;
    private Inet4Address broadcast;
    private short maskLength;

    InterfaceAddress() {
        this.address = null;
        this.broadcast = null;
        this.maskLength = 0;
    }

    InterfaceAddress(InetAddress inetAddress, Inet4Address inet4Address, InetAddress inetAddress2) {
        this.maskLength = 0;
        this.address = inetAddress;
        this.broadcast = inet4Address;
        this.maskLength = countPrefixLength(inetAddress2);
    }

    private short countPrefixLength(InetAddress inetAddress) {
        short s = 0;
        for (byte b : inetAddress.getAddress()) {
            while (b != 0) {
                b = (byte) (b << 1);
                s = (short) (s + 1);
            }
        }
        return s;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public InetAddress getBroadcast() {
        return this.broadcast;
    }

    public short getNetworkPrefixLength() {
        return this.maskLength;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof InterfaceAddress)) {
            return false;
        }
        InterfaceAddress interfaceAddress = (InterfaceAddress) obj;
        InetAddress inetAddress = this.address;
        if (inetAddress != null ? !inetAddress.equals(interfaceAddress.address) : interfaceAddress.address != null) {
            return false;
        }
        Inet4Address inet4Address = this.broadcast;
        if (inet4Address != null ? !inet4Address.equals(interfaceAddress.broadcast) : interfaceAddress.broadcast != null) {
            return false;
        }
        if (this.maskLength != interfaceAddress.maskLength) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hashCode = this.address.hashCode();
        Inet4Address inet4Address = this.broadcast;
        return hashCode + (inet4Address != null ? inet4Address.hashCode() : 0) + this.maskLength;
    }

    public String toString() {
        return this.address + "/" + this.maskLength + " [" + this.broadcast + NavigationBarInflaterView.SIZE_MOD_END;
    }
}

package android.net.connectivity.com.android.net.module.util;

import android.net.MacAddress;
import android.text.TextUtils;
import java.net.NetworkInterface;
import java.net.SocketException;

public class InterfaceParams {
    private static final int ETHER_MTU = 1500;
    private static final int IPV6_MIN_MTU = 1280;
    public final int defaultMtu;
    public final boolean hasMacAddress;
    public final int index;
    public final MacAddress macAddr;
    public final String name;

    public static InterfaceParams getByName(String str) {
        NetworkInterface networkInterfaceByName = getNetworkInterfaceByName(str);
        if (networkInterfaceByName == null) {
            return null;
        }
        try {
            return new InterfaceParams(str, networkInterfaceByName.getIndex(), getMacAddress(networkInterfaceByName), networkInterfaceByName.getMTU());
        } catch (IllegalArgumentException | SocketException unused) {
            return null;
        }
    }

    public InterfaceParams(String str, int i, MacAddress macAddress) {
        this(str, i, macAddress, 1500);
    }

    public InterfaceParams(String str, int i, MacAddress macAddress, int i2) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("impossible interface name");
        } else if (i > 0) {
            this.name = str;
            this.index = i;
            boolean z = macAddress != null;
            this.hasMacAddress = z;
            this.macAddr = !z ? MacAddress.fromBytes(new byte[]{2, 0, 0, 0, 0, 0}) : macAddress;
            this.defaultMtu = i2 <= 1280 ? 1280 : i2;
        } else {
            throw new IllegalArgumentException("invalid interface index");
        }
    }

    public String toString() {
        return String.format("%s/%d/%s/%d", this.name, Integer.valueOf(this.index), this.macAddr, Integer.valueOf(this.defaultMtu));
    }

    private static NetworkInterface getNetworkInterfaceByName(String str) {
        try {
            return NetworkInterface.getByName(str);
        } catch (NullPointerException | SocketException unused) {
            return null;
        }
    }

    private static MacAddress getMacAddress(NetworkInterface networkInterface) {
        try {
            return MacAddress.fromBytes(networkInterface.getHardwareAddress());
        } catch (IllegalArgumentException | NullPointerException | SocketException unused) {
            return null;
        }
    }
}

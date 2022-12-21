package android.net;

import android.net.connectivity.com.android.net.module.util.MacAddressUtils;
import android.net.wifi.WifiInfo;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;

public final class MacAddress implements Parcelable {
    public static final MacAddress ALL_ZEROS_ADDRESS = new MacAddress(0);
    private static final MacAddress BASE_GOOGLE_MAC = fromString("da:a1:19:0:0:0");
    public static final MacAddress BROADCAST_ADDRESS;
    public static final Parcelable.Creator<MacAddress> CREATOR = new Parcelable.Creator<MacAddress>() {
        public MacAddress createFromParcel(Parcel parcel) {
            return new MacAddress(parcel.readLong());
        }

        public MacAddress[] newArray(int i) {
            return new MacAddress[i];
        }
    };
    private static final MacAddress DEFAULT_MAC_ADDRESS = fromString(WifiInfo.DEFAULT_MAC_ADDRESS);
    private static final byte[] ETHER_ADDR_BROADCAST;
    private static final int ETHER_ADDR_LEN = 6;
    private static final long LOCALLY_ASSIGNED_MASK = fromString("2:0:0:0:0:0").mAddr;
    private static final long MULTICAST_MASK = fromString("1:0:0:0:0:0").mAddr;
    private static final long NIC_MASK = fromString("0:0:0:ff:ff:ff").mAddr;
    private static final long OUI_MASK = fromString("ff:ff:ff:0:0:0").mAddr;
    public static final int TYPE_BROADCAST = 3;
    public static final int TYPE_MULTICAST = 2;
    public static final int TYPE_UNICAST = 1;
    public static final int TYPE_UNKNOWN = 0;
    private static final long VALID_LONG_MASK = 281474976710655L;
    private final long mAddr;

    @Retention(RetentionPolicy.SOURCE)
    public @interface MacAddressType {
    }

    public int describeContents() {
        return 0;
    }

    static {
        byte[] addr = addr(255, 255, 255, 255, 255, 255);
        ETHER_ADDR_BROADCAST = addr;
        BROADCAST_ADDRESS = fromBytes(addr);
    }

    private MacAddress(long j) {
        this.mAddr = j & VALID_LONG_MASK;
    }

    public int getAddressType() {
        if (equals(BROADCAST_ADDRESS)) {
            return 3;
        }
        return (this.mAddr & MULTICAST_MASK) != 0 ? 2 : 1;
    }

    public boolean isLocallyAssigned() {
        return (this.mAddr & LOCALLY_ASSIGNED_MASK) != 0;
    }

    public byte[] toByteArray() {
        return byteAddrFromLongAddr(this.mAddr);
    }

    public String toString() {
        return stringAddrFromLongAddr(this.mAddr);
    }

    public String toOuiString() {
        return String.format("%02x:%02x:%02x", Long.valueOf((this.mAddr >> 40) & 255), Long.valueOf((this.mAddr >> 32) & 255), Long.valueOf((this.mAddr >> 24) & 255));
    }

    public int hashCode() {
        long j = this.mAddr;
        return (int) (j ^ (j >> 32));
    }

    public boolean equals(Object obj) {
        return (obj instanceof MacAddress) && ((MacAddress) obj).mAddr == this.mAddr;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mAddr);
    }

    public static boolean isMacAddress(byte[] bArr) {
        return MacAddressUtils.isMacAddress(bArr);
    }

    public static int macAddressType(byte[] bArr) {
        if (!isMacAddress(bArr)) {
            return 0;
        }
        return fromBytes(bArr).getAddressType();
    }

    public static byte[] byteAddrFromStringAddr(String str) {
        Objects.requireNonNull(str);
        String[] split = str.split(":");
        if (split.length == 6) {
            byte[] bArr = new byte[6];
            for (int i = 0; i < 6; i++) {
                int intValue = Integer.valueOf(split[i], 16).intValue();
                if (intValue < 0 || 255 < intValue) {
                    throw new IllegalArgumentException(str + "was not a valid MAC address");
                }
                bArr[i] = (byte) intValue;
            }
            return bArr;
        }
        throw new IllegalArgumentException(str + " was not a valid MAC address");
    }

    public static String stringAddrFromByteAddr(byte[] bArr) {
        if (!isMacAddress(bArr)) {
            return null;
        }
        return String.format("%02x:%02x:%02x:%02x:%02x:%02x", Byte.valueOf(bArr[0]), Byte.valueOf(bArr[1]), Byte.valueOf(bArr[2]), Byte.valueOf(bArr[3]), Byte.valueOf(bArr[4]), Byte.valueOf(bArr[5]));
    }

    private static byte[] byteAddrFromLongAddr(long j) {
        return MacAddressUtils.byteAddrFromLongAddr(j);
    }

    private static long longAddrFromByteAddr(byte[] bArr) {
        return MacAddressUtils.longAddrFromByteAddr(bArr);
    }

    private static long longAddrFromStringAddr(String str) {
        Objects.requireNonNull(str);
        String[] split = str.split(":");
        if (split.length == 6) {
            long j = 0;
            for (String valueOf : split) {
                int intValue = Integer.valueOf(valueOf, 16).intValue();
                if (intValue < 0 || 255 < intValue) {
                    throw new IllegalArgumentException(str + "was not a valid MAC address");
                }
                j = (j << 8) + ((long) intValue);
            }
            return j;
        }
        throw new IllegalArgumentException(str + " was not a valid MAC address");
    }

    private static String stringAddrFromLongAddr(long j) {
        return String.format("%02x:%02x:%02x:%02x:%02x:%02x", Long.valueOf((j >> 40) & 255), Long.valueOf((j >> 32) & 255), Long.valueOf((j >> 24) & 255), Long.valueOf((j >> 16) & 255), Long.valueOf((j >> 8) & 255), Long.valueOf(j & 255));
    }

    public static MacAddress fromString(String str) {
        return new MacAddress(longAddrFromStringAddr(str));
    }

    public static MacAddress fromBytes(byte[] bArr) {
        return new MacAddress(longAddrFromByteAddr(bArr));
    }

    public static MacAddress createRandomUnicastAddressWithGoogleBase() {
        return MacAddressUtils.createRandomUnicastAddress(BASE_GOOGLE_MAC, new SecureRandom());
    }

    private static byte[] addr(int... iArr) {
        if (iArr.length == 6) {
            byte[] bArr = new byte[6];
            for (int i = 0; i < 6; i++) {
                bArr[i] = (byte) iArr[i];
            }
            return bArr;
        }
        throw new IllegalArgumentException(Arrays.toString(iArr) + " was not an array with length equal to 6");
    }

    public boolean matches(MacAddress macAddress, MacAddress macAddress2) {
        Objects.requireNonNull(macAddress);
        Objects.requireNonNull(macAddress2);
        long j = this.mAddr;
        long j2 = macAddress2.mAddr;
        return (j & j2) == (macAddress.mAddr & j2);
    }

    public Inet6Address getLinkLocalIpv6FromEui48Mac() {
        byte[] byteArray = toByteArray();
        byte[] bArr = new byte[16];
        bArr[0] = -2;
        bArr[1] = Byte.MIN_VALUE;
        bArr[8] = (byte) (byteArray[0] ^ 2);
        bArr[9] = byteArray[1];
        bArr[10] = byteArray[2];
        bArr[11] = -1;
        bArr[12] = -2;
        bArr[13] = byteArray[3];
        bArr[14] = byteArray[4];
        bArr[15] = byteArray[5];
        try {
            return Inet6Address.getByAddress((String) null, bArr, 0);
        } catch (UnknownHostException unused) {
            return null;
        }
    }
}

package android.net;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.net.module.util.NetUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Comparator;

public final class IpPrefix implements Parcelable {
    public static final Parcelable.Creator<IpPrefix> CREATOR = new Parcelable.Creator<IpPrefix>() {
        public IpPrefix createFromParcel(Parcel parcel) {
            return new IpPrefix(parcel.createByteArray(), parcel.readInt());
        }

        public IpPrefix[] newArray(int i) {
            return new IpPrefix[i];
        }
    };
    /* access modifiers changed from: private */
    public final byte[] address;
    private final int prefixLength;

    public int describeContents() {
        return 0;
    }

    private void checkAndMaskAddressAndPrefixLength() {
        byte[] bArr = this.address;
        if (bArr.length == 4 || bArr.length == 16) {
            NetUtils.maskRawAddress(bArr, this.prefixLength);
            return;
        }
        throw new IllegalArgumentException("IpPrefix has " + this.address.length + " bytes which is neither 4 nor 16");
    }

    public IpPrefix(byte[] bArr, int i) {
        this.address = (byte[]) bArr.clone();
        this.prefixLength = i;
        checkAndMaskAddressAndPrefixLength();
    }

    public IpPrefix(InetAddress inetAddress, int i) {
        this.address = inetAddress.getAddress();
        this.prefixLength = i;
        checkAndMaskAddressAndPrefixLength();
    }

    @SystemApi
    public IpPrefix(String str) {
        Pair<InetAddress, Integer> legacyParseIpAndMask = NetworkUtils.legacyParseIpAndMask(str);
        this.address = ((InetAddress) legacyParseIpAndMask.first).getAddress();
        this.prefixLength = ((Integer) legacyParseIpAndMask.second).intValue();
        checkAndMaskAddressAndPrefixLength();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof IpPrefix)) {
            return false;
        }
        IpPrefix ipPrefix = (IpPrefix) obj;
        if (!Arrays.equals(this.address, ipPrefix.address) || this.prefixLength != ipPrefix.prefixLength) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Arrays.hashCode(this.address) + (this.prefixLength * 11);
    }

    public InetAddress getAddress() {
        try {
            return InetAddress.getByAddress(this.address);
        } catch (UnknownHostException unused) {
            throw new IllegalArgumentException("Address is invalid");
        }
    }

    public byte[] getRawAddress() {
        return (byte[]) this.address.clone();
    }

    public int getPrefixLength() {
        return this.prefixLength;
    }

    public boolean contains(InetAddress inetAddress) {
        byte[] address2 = inetAddress.getAddress();
        if (address2 == null || address2.length != this.address.length) {
            return false;
        }
        NetUtils.maskRawAddress(address2, this.prefixLength);
        return Arrays.equals(this.address, address2);
    }

    public boolean containsPrefix(IpPrefix ipPrefix) {
        if (ipPrefix.getPrefixLength() < this.prefixLength) {
            return false;
        }
        byte[] rawAddress = ipPrefix.getRawAddress();
        NetUtils.maskRawAddress(rawAddress, this.prefixLength);
        return Arrays.equals(rawAddress, this.address);
    }

    public boolean isIPv6() {
        return getAddress() instanceof Inet6Address;
    }

    public boolean isIPv4() {
        return getAddress() instanceof Inet4Address;
    }

    public String toString() {
        try {
            return InetAddress.getByAddress(this.address).getHostAddress() + "/" + this.prefixLength;
        } catch (UnknownHostException e) {
            throw new IllegalStateException("IpPrefix with invalid address! Shouldn't happen.", e);
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByteArray(this.address);
        parcel.writeInt(this.prefixLength);
    }

    public static Comparator<IpPrefix> lengthComparator() {
        return new Comparator<IpPrefix>() {
            public int compare(IpPrefix ipPrefix, IpPrefix ipPrefix2) {
                if (ipPrefix.isIPv4()) {
                    if (ipPrefix2.isIPv6()) {
                        return -1;
                    }
                } else if (ipPrefix2.isIPv4()) {
                    return 1;
                }
                int prefixLength = ipPrefix.getPrefixLength();
                int prefixLength2 = ipPrefix2.getPrefixLength();
                if (prefixLength < prefixLength2) {
                    return -1;
                }
                if (prefixLength2 < prefixLength) {
                    return 1;
                }
                byte[] r6 = ipPrefix.address;
                byte[] r7 = ipPrefix2.address;
                int length = r6.length < r7.length ? r6.length : r7.length;
                for (int i = 0; i < length; i++) {
                    byte b = r6[i];
                    byte b2 = r7[i];
                    if (b < b2) {
                        return -1;
                    }
                    if (b > b2) {
                        return 1;
                    }
                }
                if (r7.length < length) {
                    return 1;
                }
                if (r6.length < length) {
                    return -1;
                }
                return 0;
            }
        };
    }
}

package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.system.OsConstants;
import android.util.Pair;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class LinkAddress implements Parcelable {
    public static final Parcelable.Creator<LinkAddress> CREATOR = new Parcelable.Creator<LinkAddress>() {
        public LinkAddress createFromParcel(Parcel parcel) {
            InetAddress inetAddress;
            try {
                inetAddress = InetAddress.getByAddress(parcel.createByteArray());
            } catch (UnknownHostException unused) {
                inetAddress = null;
            }
            return new LinkAddress(inetAddress, parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readLong(), parcel.readLong());
        }

        public LinkAddress[] newArray(int i) {
            return new LinkAddress[i];
        }
    };
    @SystemApi
    public static final long LIFETIME_PERMANENT = Long.MAX_VALUE;
    @SystemApi
    public static final long LIFETIME_UNKNOWN = -1;
    private InetAddress address;
    private long deprecationTime;
    private long expirationTime;
    private int flags;
    private int prefixLength;
    private int scope;

    public int describeContents() {
        return 0;
    }

    private static int scopeForUnicastAddress(InetAddress inetAddress) {
        if (inetAddress.isAnyLocalAddress()) {
            return OsConstants.RT_SCOPE_HOST;
        }
        if (inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress()) {
            return OsConstants.RT_SCOPE_LINK;
        }
        if ((inetAddress instanceof Inet4Address) || !inetAddress.isSiteLocalAddress()) {
            return OsConstants.RT_SCOPE_UNIVERSE;
        }
        return OsConstants.RT_SCOPE_SITE;
    }

    private boolean isIpv6ULA() {
        if (!isIpv6() || (this.address.getAddress()[0] & -2) != -4) {
            return false;
        }
        return true;
    }

    @SystemApi
    public boolean isIpv6() {
        return this.address instanceof Inet6Address;
    }

    public boolean isIPv6() {
        return isIpv6();
    }

    @SystemApi
    public boolean isIpv4() {
        return this.address instanceof Inet4Address;
    }

    private void init(InetAddress inetAddress, int i, int i2, int i3, long j, long j2) {
        if (inetAddress == null || inetAddress.isMulticastAddress() || i < 0 || (((inetAddress instanceof Inet4Address) && i > 32) || i > 128)) {
            throw new IllegalArgumentException("Bad LinkAddress params " + inetAddress + "/" + i);
        }
        int i4 = (j > -1 ? 1 : (j == -1 ? 0 : -1));
        boolean z = true;
        boolean z2 = i4 == 0;
        int i5 = (j2 > -1 ? 1 : (j2 == -1 ? 0 : -1));
        if (i5 != 0) {
            z = false;
        }
        if (z2 != z) {
            throw new IllegalArgumentException("Must not specify only one of deprecation time and expiration time");
        } else if (i4 != 0 && j < 0) {
            throw new IllegalArgumentException("invalid deprecation time " + j);
        } else if (i5 != 0 && j2 < 0) {
            throw new IllegalArgumentException("invalid expiration time " + j2);
        } else if (i4 == 0 || i5 == 0 || j2 >= j) {
            this.address = inetAddress;
            this.prefixLength = i;
            this.flags = i2;
            this.scope = i3;
            this.deprecationTime = j;
            this.expirationTime = j2;
        } else {
            throw new IllegalArgumentException("expiration earlier than deprecation (" + j + ", " + j2 + NavigationBarInflaterView.KEY_CODE_END);
        }
    }

    @SystemApi
    public LinkAddress(InetAddress inetAddress, int i, int i2, int i3) {
        init(inetAddress, i, i2, i3, -1, -1);
    }

    @SystemApi
    public LinkAddress(InetAddress inetAddress, int i, int i2, int i3, long j, long j2) {
        init(inetAddress, i, i2, i3, j, j2);
    }

    @SystemApi
    public LinkAddress(InetAddress inetAddress, int i) {
        this(inetAddress, i, 0, 0);
        this.scope = scopeForUnicastAddress(inetAddress);
    }

    public LinkAddress(InterfaceAddress interfaceAddress) {
        this(interfaceAddress.getAddress(), interfaceAddress.getNetworkPrefixLength());
    }

    @SystemApi
    public LinkAddress(String str) {
        this(str, 0, 0);
        this.scope = scopeForUnicastAddress(this.address);
    }

    @SystemApi
    public LinkAddress(String str, int i, int i2) {
        Pair<InetAddress, Integer> legacyParseIpAndMask = NetworkUtils.legacyParseIpAndMask(str);
        init((InetAddress) legacyParseIpAndMask.first, ((Integer) legacyParseIpAndMask.second).intValue(), i, i2, -1, -1);
    }

    public String toString() {
        return this.address.getHostAddress() + "/" + this.prefixLength;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof LinkAddress)) {
            return false;
        }
        LinkAddress linkAddress = (LinkAddress) obj;
        if (this.address.equals(linkAddress.address) && this.prefixLength == linkAddress.prefixLength && this.flags == linkAddress.flags && this.scope == linkAddress.scope && this.deprecationTime == linkAddress.deprecationTime && this.expirationTime == linkAddress.expirationTime) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.address, Integer.valueOf(this.prefixLength), Integer.valueOf(this.flags), Integer.valueOf(this.scope), Long.valueOf(this.deprecationTime), Long.valueOf(this.expirationTime));
    }

    @SystemApi
    public boolean isSameAddressAs(LinkAddress linkAddress) {
        return linkAddress != null && this.address.equals(linkAddress.address) && this.prefixLength == linkAddress.prefixLength;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public int getPrefixLength() {
        return this.prefixLength;
    }

    public int getNetworkPrefixLength() {
        return getPrefixLength();
    }

    public int getFlags() {
        int i = this.flags;
        if (this.deprecationTime != -1) {
            if (SystemClock.elapsedRealtime() >= this.deprecationTime) {
                i |= OsConstants.IFA_F_DEPRECATED;
            } else {
                i &= ~OsConstants.IFA_F_DEPRECATED;
            }
        }
        long j = this.expirationTime;
        if (j == Long.MAX_VALUE) {
            return i | OsConstants.IFA_F_PERMANENT;
        }
        return j != -1 ? i & (~OsConstants.IFA_F_PERMANENT) : i;
    }

    public int getScope() {
        return this.scope;
    }

    @SystemApi
    public long getDeprecationTime() {
        return this.deprecationTime;
    }

    @SystemApi
    public long getExpirationTime() {
        return this.expirationTime;
    }

    @SystemApi
    public boolean isGlobalPreferred() {
        int flags2 = getFlags();
        return this.scope == OsConstants.RT_SCOPE_UNIVERSE && !isIpv6ULA() && ((long) ((OsConstants.IFA_F_DADFAILED | OsConstants.IFA_F_DEPRECATED) & flags2)) == 0 && (((long) (OsConstants.IFA_F_TENTATIVE & flags2)) == 0 || ((long) (OsConstants.IFA_F_OPTIMISTIC & flags2)) != 0);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByteArray(this.address.getAddress());
        parcel.writeInt(this.prefixLength);
        parcel.writeInt(this.flags);
        parcel.writeInt(this.scope);
        parcel.writeLong(this.deprecationTime);
        parcel.writeLong(this.expirationTime);
    }
}

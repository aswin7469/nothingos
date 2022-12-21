package android.net;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.net.module.util.NetUtils;
import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Objects;

public final class RouteInfo implements Parcelable {
    public static final Parcelable.Creator<RouteInfo> CREATOR = new Parcelable.Creator<RouteInfo>() {
        public RouteInfo createFromParcel(Parcel parcel) {
            InetAddress inetAddress = null;
            IpPrefix ipPrefix = (IpPrefix) parcel.readParcelable((ClassLoader) null);
            try {
                inetAddress = InetAddress.getByAddress(parcel.createByteArray());
            } catch (UnknownHostException unused) {
            }
            return new RouteInfo(ipPrefix, inetAddress, parcel.readString(), parcel.readInt(), parcel.readInt());
        }

        public RouteInfo[] newArray(int i) {
            return new RouteInfo[i];
        }
    };
    public static final int RTN_THROW = 9;
    public static final int RTN_UNICAST = 1;
    public static final int RTN_UNREACHABLE = 7;
    private final IpPrefix mDestination;
    private final InetAddress mGateway;
    private final boolean mHasGateway;
    private final String mInterface;
    private final boolean mIsHost;
    private final int mMtu;
    private final int mType;

    @Retention(RetentionPolicy.SOURCE)
    public @interface RouteType {
    }

    public int describeContents() {
        return 0;
    }

    @SystemApi
    public RouteInfo(IpPrefix ipPrefix, InetAddress inetAddress, String str, int i) {
        this(ipPrefix, inetAddress, str, i, 0);
    }

    @SystemApi
    public RouteInfo(IpPrefix ipPrefix, InetAddress inetAddress, String str, int i, int i2) {
        if (i == 1 || i == 7 || i == 9) {
            if (ipPrefix == null) {
                if (inetAddress == null) {
                    throw new IllegalArgumentException("Invalid arguments passed in: " + inetAddress + NavigationBarInflaterView.BUTTON_SEPARATOR + ipPrefix);
                } else if (inetAddress instanceof Inet4Address) {
                    ipPrefix = new IpPrefix((InetAddress) NetworkStackConstants.IPV4_ADDR_ANY, 0);
                } else {
                    ipPrefix = new IpPrefix((InetAddress) NetworkStackConstants.IPV6_ADDR_ANY, 0);
                }
            }
            if (inetAddress == null) {
                if (ipPrefix.getAddress() instanceof Inet4Address) {
                    inetAddress = NetworkStackConstants.IPV4_ADDR_ANY;
                } else {
                    inetAddress = NetworkStackConstants.IPV6_ADDR_ANY;
                }
            }
            this.mHasGateway = true ^ inetAddress.isAnyLocalAddress();
            if ((!(ipPrefix.getAddress() instanceof Inet4Address) || (inetAddress instanceof Inet4Address)) && (!(ipPrefix.getAddress() instanceof Inet6Address) || (inetAddress instanceof Inet6Address))) {
                this.mDestination = ipPrefix;
                this.mGateway = inetAddress;
                this.mInterface = str;
                this.mType = i;
                this.mIsHost = isHost();
                this.mMtu = i2;
                return;
            }
            throw new IllegalArgumentException("address family mismatch in RouteInfo constructor");
        }
        throw new IllegalArgumentException("Unknown route type " + i);
    }

    public RouteInfo(IpPrefix ipPrefix, InetAddress inetAddress, String str) {
        this(ipPrefix, inetAddress, str, 1);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public RouteInfo(LinkAddress linkAddress, InetAddress inetAddress, String str) {
        this(linkAddress == null ? null : new IpPrefix(linkAddress.getAddress(), linkAddress.getPrefixLength()), inetAddress, str);
    }

    public RouteInfo(IpPrefix ipPrefix, InetAddress inetAddress) {
        this(ipPrefix, inetAddress, (String) null);
    }

    public RouteInfo(LinkAddress linkAddress, InetAddress inetAddress) {
        this(linkAddress, inetAddress, (String) null);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public RouteInfo(InetAddress inetAddress) {
        this((IpPrefix) null, inetAddress, (String) null);
        IpPrefix ipPrefix = null;
    }

    public RouteInfo(IpPrefix ipPrefix) {
        this(ipPrefix, (InetAddress) null, (String) null);
    }

    public RouteInfo(LinkAddress linkAddress) {
        this(linkAddress, (InetAddress) null, (String) null);
    }

    public RouteInfo(IpPrefix ipPrefix, int i) {
        this(ipPrefix, (InetAddress) null, (String) null, i);
    }

    public static RouteInfo makeHostRoute(InetAddress inetAddress, String str) {
        return makeHostRoute(inetAddress, (InetAddress) null, str);
    }

    public static RouteInfo makeHostRoute(InetAddress inetAddress, InetAddress inetAddress2, String str) {
        if (inetAddress == null) {
            return null;
        }
        if (inetAddress instanceof Inet4Address) {
            return new RouteInfo(new IpPrefix(inetAddress, 32), inetAddress2, str);
        }
        return new RouteInfo(new IpPrefix(inetAddress, 128), inetAddress2, str);
    }

    private boolean isHost() {
        return ((this.mDestination.getAddress() instanceof Inet4Address) && this.mDestination.getPrefixLength() == 32) || ((this.mDestination.getAddress() instanceof Inet6Address) && this.mDestination.getPrefixLength() == 128);
    }

    public IpPrefix getDestination() {
        return this.mDestination;
    }

    public LinkAddress getDestinationLinkAddress() {
        return new LinkAddress(this.mDestination.getAddress(), this.mDestination.getPrefixLength());
    }

    public InetAddress getGateway() {
        return this.mGateway;
    }

    public String getInterface() {
        return this.mInterface;
    }

    public int getType() {
        return this.mType;
    }

    @SystemApi
    public int getMtu() {
        return this.mMtu;
    }

    public boolean isDefaultRoute() {
        return this.mType == 1 && this.mDestination.getPrefixLength() == 0;
    }

    private boolean isUnreachableDefaultRoute() {
        return this.mType == 7 && this.mDestination.getPrefixLength() == 0;
    }

    public boolean isIPv4Default() {
        return isDefaultRoute() && (this.mDestination.getAddress() instanceof Inet4Address);
    }

    public boolean isIPv4UnreachableDefault() {
        return isUnreachableDefaultRoute() && (this.mDestination.getAddress() instanceof Inet4Address);
    }

    public boolean isIPv6Default() {
        return isDefaultRoute() && (this.mDestination.getAddress() instanceof Inet6Address);
    }

    public boolean isIPv6UnreachableDefault() {
        return isUnreachableDefaultRoute() && (this.mDestination.getAddress() instanceof Inet6Address);
    }

    public boolean isHostRoute() {
        return this.mIsHost;
    }

    public boolean hasGateway() {
        return this.mHasGateway;
    }

    public boolean matches(InetAddress inetAddress) {
        return this.mDestination.contains(inetAddress);
    }

    public static RouteInfo selectBestRoute(Collection<RouteInfo> collection, InetAddress inetAddress) {
        return NetUtils.selectBestRoute(collection, inetAddress);
    }

    public String toString() {
        String str;
        IpPrefix ipPrefix = this.mDestination;
        String ipPrefix2 = ipPrefix != null ? ipPrefix.toString() : "";
        int i = this.mType;
        if (i == 7) {
            str = ipPrefix2 + " unreachable";
        } else if (i == 9) {
            str = ipPrefix2 + " throw";
        } else {
            str = ipPrefix2 + " ->";
            if (this.mGateway != null) {
                str = str + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.mGateway.getHostAddress();
            }
            if (this.mInterface != null) {
                str = str + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.mInterface;
            }
            if (this.mType != 1) {
                str = str + " unknown type " + this.mType;
            }
        }
        return str + " mtu " + this.mMtu;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RouteInfo)) {
            return false;
        }
        RouteInfo routeInfo = (RouteInfo) obj;
        if (!Objects.equals(this.mDestination, routeInfo.getDestination()) || !Objects.equals(this.mGateway, routeInfo.getGateway()) || !Objects.equals(this.mInterface, routeInfo.getInterface()) || this.mType != routeInfo.getType() || this.mMtu != routeInfo.getMtu()) {
            return false;
        }
        return true;
    }

    public static class RouteKey {
        private final IpPrefix mDestination;
        private final InetAddress mGateway;
        private final String mInterface;

        RouteKey(IpPrefix ipPrefix, InetAddress inetAddress, String str) {
            this.mDestination = ipPrefix;
            this.mGateway = inetAddress;
            this.mInterface = str;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof RouteKey)) {
                return false;
            }
            RouteKey routeKey = (RouteKey) obj;
            if (!Objects.equals(routeKey.mDestination, this.mDestination) || !Objects.equals(routeKey.mGateway, this.mGateway) || !Objects.equals(routeKey.mInterface, this.mInterface)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(this.mDestination, this.mGateway, this.mInterface);
        }
    }

    public RouteKey getRouteKey() {
        return new RouteKey(this.mDestination, this.mGateway, this.mInterface);
    }

    public int hashCode() {
        int hashCode = this.mDestination.hashCode() * 41;
        InetAddress inetAddress = this.mGateway;
        int i = 0;
        int hashCode2 = hashCode + (inetAddress == null ? 0 : inetAddress.hashCode() * 47);
        String str = this.mInterface;
        if (str != null) {
            i = str.hashCode() * 67;
        }
        return hashCode2 + i + (this.mType * 71) + (this.mMtu * 89);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mDestination, i);
        InetAddress inetAddress = this.mGateway;
        parcel.writeByteArray(inetAddress == null ? null : inetAddress.getAddress());
        parcel.writeString(this.mInterface);
        parcel.writeInt(this.mType);
        parcel.writeInt(this.mMtu);
    }
}

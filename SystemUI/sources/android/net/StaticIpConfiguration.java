package android.net;

import android.annotation.SystemApi;
import android.net.connectivity.android.net.mdns.aidl.IMDnsEventListener;
import android.net.connectivity.com.android.net.module.util.InetAddressUtils;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public final class StaticIpConfiguration implements Parcelable {
    public static final Parcelable.Creator<StaticIpConfiguration> CREATOR = new Parcelable.Creator<StaticIpConfiguration>() {
        public StaticIpConfiguration createFromParcel(Parcel parcel) {
            return StaticIpConfiguration.readFromParcel(parcel);
        }

        public StaticIpConfiguration[] newArray(int i) {
            return new StaticIpConfiguration[i];
        }
    };
    public final ArrayList<InetAddress> dnsServers;
    public String domains;
    public InetAddress gateway;
    public LinkAddress ipAddress;

    public int describeContents() {
        return 0;
    }

    @SystemApi
    public StaticIpConfiguration() {
        this.dnsServers = new ArrayList<>();
    }

    @SystemApi
    public StaticIpConfiguration(StaticIpConfiguration staticIpConfiguration) {
        this();
        if (staticIpConfiguration != null) {
            this.ipAddress = staticIpConfiguration.ipAddress;
            this.gateway = staticIpConfiguration.gateway;
            this.dnsServers.addAll(staticIpConfiguration.dnsServers);
            this.domains = staticIpConfiguration.domains;
        }
    }

    @SystemApi
    public void clear() {
        this.ipAddress = null;
        this.gateway = null;
        this.dnsServers.clear();
        this.domains = null;
    }

    public LinkAddress getIpAddress() {
        return this.ipAddress;
    }

    public InetAddress getGateway() {
        return this.gateway;
    }

    public List<InetAddress> getDnsServers() {
        return this.dnsServers;
    }

    public String getDomains() {
        return this.domains;
    }

    public static final class Builder {
        private Iterable<InetAddress> mDnsServers;
        private String mDomains;
        private InetAddress mGateway;
        private LinkAddress mIpAddress;

        public Builder setIpAddress(LinkAddress linkAddress) {
            if (linkAddress == null || (linkAddress.getAddress() instanceof Inet4Address)) {
                this.mIpAddress = linkAddress;
                return this;
            }
            throw new IllegalArgumentException("Only IPv4 addresses can be used for the IP configuration");
        }

        public Builder setGateway(InetAddress inetAddress) {
            if (inetAddress == null || (inetAddress instanceof Inet4Address)) {
                this.mGateway = inetAddress;
                return this;
            }
            throw new IllegalArgumentException("Only IPv4 addresses can be used for the gateway configuration");
        }

        public Builder setDnsServers(Iterable<InetAddress> iterable) {
            Objects.requireNonNull(iterable);
            for (InetAddress inetAddress : iterable) {
                if (!(inetAddress instanceof Inet4Address)) {
                    throw new IllegalArgumentException("Only IPv4 addresses can be used for the DNS server configuration");
                }
            }
            this.mDnsServers = iterable;
            return this;
        }

        public Builder setDomains(String str) {
            this.mDomains = str;
            return this;
        }

        public StaticIpConfiguration build() {
            StaticIpConfiguration staticIpConfiguration = new StaticIpConfiguration();
            staticIpConfiguration.ipAddress = this.mIpAddress;
            staticIpConfiguration.gateway = this.mGateway;
            Iterable<InetAddress> iterable = this.mDnsServers;
            if (iterable != null) {
                for (InetAddress add : iterable) {
                    staticIpConfiguration.dnsServers.add(add);
                }
            }
            staticIpConfiguration.domains = this.mDomains;
            return staticIpConfiguration;
        }
    }

    @SystemApi
    public void addDnsServer(InetAddress inetAddress) {
        this.dnsServers.add(inetAddress);
    }

    @SystemApi
    public List<RouteInfo> getRoutes(String str) {
        ArrayList arrayList = new ArrayList(3);
        if (this.ipAddress != null) {
            RouteInfo routeInfo = new RouteInfo(this.ipAddress, (InetAddress) null, str);
            arrayList.add(routeInfo);
            InetAddress inetAddress = this.gateway;
            if (inetAddress != null && !routeInfo.matches(inetAddress)) {
                arrayList.add(RouteInfo.makeHostRoute(this.gateway, str));
            }
        }
        if (this.gateway != null) {
            IpPrefix ipPrefix = null;
            arrayList.add(new RouteInfo((IpPrefix) null, this.gateway, str));
        }
        return arrayList;
    }

    public LinkProperties toLinkProperties(String str) {
        LinkProperties linkProperties = new LinkProperties();
        linkProperties.setInterfaceName(str);
        LinkAddress linkAddress = this.ipAddress;
        if (linkAddress != null) {
            linkProperties.addLinkAddress(linkAddress);
        }
        for (RouteInfo addRoute : getRoutes(str)) {
            linkProperties.addRoute(addRoute);
        }
        Iterator<InetAddress> it = this.dnsServers.iterator();
        while (it.hasNext()) {
            linkProperties.addDnsServer(it.next());
        }
        linkProperties.setDomains(this.domains);
        return linkProperties;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("IP address ");
        LinkAddress linkAddress = this.ipAddress;
        if (linkAddress != null) {
            stringBuffer.append((Object) linkAddress).append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        stringBuffer.append("Gateway ");
        InetAddress inetAddress = this.gateway;
        if (inetAddress != null) {
            stringBuffer.append(inetAddress.getHostAddress()).append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        stringBuffer.append(" DNS servers: [");
        Iterator<InetAddress> it = this.dnsServers.iterator();
        while (it.hasNext()) {
            stringBuffer.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER).append(it.next().getHostAddress());
        }
        stringBuffer.append(" ] Domains ");
        String str = this.domains;
        if (str != null) {
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    public int hashCode() {
        LinkAddress linkAddress = this.ipAddress;
        int i = 0;
        int hashCode = (IMDnsEventListener.SERVICE_GET_ADDR_FAILED + (linkAddress == null ? 0 : linkAddress.hashCode())) * 47;
        InetAddress inetAddress = this.gateway;
        int hashCode2 = (hashCode + (inetAddress == null ? 0 : inetAddress.hashCode())) * 47;
        String str = this.domains;
        if (str != null) {
            i = str.hashCode();
        }
        return ((hashCode2 + i) * 47) + this.dnsServers.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StaticIpConfiguration)) {
            return false;
        }
        StaticIpConfiguration staticIpConfiguration = (StaticIpConfiguration) obj;
        if (staticIpConfiguration == null || !Objects.equals(this.ipAddress, staticIpConfiguration.ipAddress) || !Objects.equals(this.gateway, staticIpConfiguration.gateway) || !this.dnsServers.equals(staticIpConfiguration.dnsServers) || !Objects.equals(this.domains, staticIpConfiguration.domains)) {
            return false;
        }
        return true;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.ipAddress, i);
        InetAddressUtils.parcelInetAddress(parcel, this.gateway, i);
        parcel.writeInt(this.dnsServers.size());
        Iterator<InetAddress> it = this.dnsServers.iterator();
        while (it.hasNext()) {
            InetAddressUtils.parcelInetAddress(parcel, it.next(), i);
        }
        parcel.writeString(this.domains);
    }

    public static StaticIpConfiguration readFromParcel(Parcel parcel) {
        StaticIpConfiguration staticIpConfiguration = new StaticIpConfiguration();
        staticIpConfiguration.ipAddress = (LinkAddress) parcel.readParcelable((ClassLoader) null);
        staticIpConfiguration.gateway = InetAddressUtils.unparcelInetAddress(parcel);
        staticIpConfiguration.dnsServers.clear();
        int readInt = parcel.readInt();
        for (int i = 0; i < readInt; i++) {
            staticIpConfiguration.dnsServers.add(InetAddressUtils.unparcelInetAddress(parcel));
        }
        staticIpConfiguration.domains = parcel.readString();
        return staticIpConfiguration;
    }
}

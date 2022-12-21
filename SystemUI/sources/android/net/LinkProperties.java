package android.net;

import android.annotation.SystemApi;
import android.app.compat.CompatChanges;
import android.net.connectivity.com.android.net.module.util.LinkPropertiesUtils;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public final class LinkProperties implements Parcelable {
    public static final Parcelable.Creator<LinkProperties> CREATOR = new Parcelable.Creator<LinkProperties>() {
        public LinkProperties createFromParcel(Parcel parcel) {
            LinkProperties linkProperties = new LinkProperties();
            String readString = parcel.readString();
            if (readString != null) {
                linkProperties.setInterfaceName(readString);
            }
            int readInt = parcel.readInt();
            for (int i = 0; i < readInt; i++) {
                linkProperties.addLinkAddress((LinkAddress) parcel.readParcelable((ClassLoader) null));
            }
            int readInt2 = parcel.readInt();
            for (int i2 = 0; i2 < readInt2; i2++) {
                try {
                    linkProperties.addDnsServer(LinkProperties.readAddress(parcel));
                } catch (UnknownHostException unused) {
                }
            }
            int readInt3 = parcel.readInt();
            for (int i3 = 0; i3 < readInt3; i3++) {
                try {
                    linkProperties.addValidatedPrivateDnsServer(LinkProperties.readAddress(parcel));
                } catch (UnknownHostException unused2) {
                }
            }
            linkProperties.setUsePrivateDns(parcel.readBoolean());
            linkProperties.setPrivateDnsServerName(parcel.readString());
            int readInt4 = parcel.readInt();
            for (int i4 = 0; i4 < readInt4; i4++) {
                try {
                    linkProperties.addPcscfServer(LinkProperties.readAddress(parcel));
                } catch (UnknownHostException unused3) {
                }
            }
            linkProperties.setDomains(parcel.readString());
            try {
                linkProperties.setDhcpServerAddress((Inet4Address) InetAddress.getByAddress(parcel.createByteArray()));
            } catch (UnknownHostException unused4) {
            }
            linkProperties.setMtu(parcel.readInt());
            linkProperties.setTcpBufferSizes(parcel.readString());
            int readInt5 = parcel.readInt();
            for (int i5 = 0; i5 < readInt5; i5++) {
                linkProperties.addRoute((RouteInfo) parcel.readParcelable((ClassLoader) null));
            }
            if (parcel.readByte() == 1) {
                linkProperties.setHttpProxy((ProxyInfo) parcel.readParcelable((ClassLoader) null));
            }
            linkProperties.setNat64Prefix((IpPrefix) parcel.readParcelable((ClassLoader) null));
            ArrayList arrayList = new ArrayList();
            parcel.readList(arrayList, LinkProperties.class.getClassLoader());
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                linkProperties.addStackedLink((LinkProperties) it.next());
            }
            linkProperties.setWakeOnLanSupported(parcel.readBoolean());
            linkProperties.setCaptivePortalApiUrl((Uri) parcel.readParcelable((ClassLoader) null));
            linkProperties.setCaptivePortalData((CaptivePortalData) parcel.readParcelable((ClassLoader) null));
            return linkProperties;
        }

        public LinkProperties[] newArray(int i) {
            return new LinkProperties[i];
        }
    };
    public static final long EXCLUDED_ROUTES = 186082280;
    private static final int INET6_ADDR_LENGTH = 16;
    private static final int MAX_MTU = 10000;
    private static final int MIN_MTU = 68;
    private static final int MIN_MTU_V6 = 1280;
    private Uri mCaptivePortalApiUrl;
    private CaptivePortalData mCaptivePortalData;
    private Inet4Address mDhcpServerAddress;
    private final ArrayList<InetAddress> mDnses;
    private String mDomains;
    private ProxyInfo mHttpProxy;
    private String mIfaceName;
    private final ArrayList<LinkAddress> mLinkAddresses;
    private int mMtu;
    private IpPrefix mNat64Prefix;
    private final transient boolean mParcelSensitiveFields;
    private final ArrayList<InetAddress> mPcscfs;
    private String mPrivateDnsServerName;
    private ArrayList<RouteInfo> mRoutes;
    private Hashtable<String, LinkProperties> mStackedLinks;
    private String mTcpBufferSizes;
    private boolean mUsePrivateDns;
    private final ArrayList<InetAddress> mValidatedPrivateDnses;
    private boolean mWakeOnLanSupported;

    public enum ProvisioningChange {
        STILL_NOT_PROVISIONED,
        LOST_PROVISIONING,
        GAINED_PROVISIONING,
        STILL_PROVISIONED
    }

    public static boolean isValidMtu(int i, boolean z) {
        return z ? i >= 1280 && i <= 10000 : i >= 68 && i <= 10000;
    }

    public int describeContents() {
        return 0;
    }

    public static ProvisioningChange compareProvisioning(LinkProperties linkProperties, LinkProperties linkProperties2) {
        if (!linkProperties.isProvisioned() || !linkProperties2.isProvisioned()) {
            if (linkProperties.isProvisioned() && !linkProperties2.isProvisioned()) {
                return ProvisioningChange.LOST_PROVISIONING;
            }
            if (linkProperties.isProvisioned() || !linkProperties2.isProvisioned()) {
                return ProvisioningChange.STILL_NOT_PROVISIONED;
            }
            return ProvisioningChange.GAINED_PROVISIONING;
        } else if ((!linkProperties.isIpv4Provisioned() || linkProperties2.isIpv4Provisioned()) && (!linkProperties.isIpv6Provisioned() || linkProperties2.isIpv6Provisioned())) {
            return ProvisioningChange.STILL_PROVISIONED;
        } else {
            return ProvisioningChange.LOST_PROVISIONING;
        }
    }

    public LinkProperties() {
        this.mLinkAddresses = new ArrayList<>();
        this.mDnses = new ArrayList<>();
        this.mPcscfs = new ArrayList<>();
        this.mValidatedPrivateDnses = new ArrayList<>();
        this.mRoutes = new ArrayList<>();
        this.mStackedLinks = new Hashtable<>();
        this.mParcelSensitiveFields = false;
    }

    @SystemApi
    public LinkProperties(LinkProperties linkProperties) {
        this(linkProperties, false);
    }

    @SystemApi
    public LinkProperties(LinkProperties linkProperties, boolean z) {
        ArrayList<LinkAddress> arrayList = new ArrayList<>();
        this.mLinkAddresses = arrayList;
        ArrayList<InetAddress> arrayList2 = new ArrayList<>();
        this.mDnses = arrayList2;
        ArrayList<InetAddress> arrayList3 = new ArrayList<>();
        this.mPcscfs = arrayList3;
        ArrayList<InetAddress> arrayList4 = new ArrayList<>();
        this.mValidatedPrivateDnses = arrayList4;
        this.mRoutes = new ArrayList<>();
        this.mStackedLinks = new Hashtable<>();
        this.mParcelSensitiveFields = z;
        if (linkProperties != null) {
            this.mIfaceName = linkProperties.mIfaceName;
            arrayList.addAll(linkProperties.mLinkAddresses);
            arrayList2.addAll(linkProperties.mDnses);
            arrayList4.addAll(linkProperties.mValidatedPrivateDnses);
            this.mUsePrivateDns = linkProperties.mUsePrivateDns;
            this.mPrivateDnsServerName = linkProperties.mPrivateDnsServerName;
            arrayList3.addAll(linkProperties.mPcscfs);
            this.mDomains = linkProperties.mDomains;
            this.mRoutes.addAll(linkProperties.mRoutes);
            this.mHttpProxy = linkProperties.mHttpProxy == null ? null : new ProxyInfo(linkProperties.mHttpProxy);
            for (LinkProperties addStackedLink : linkProperties.mStackedLinks.values()) {
                addStackedLink(addStackedLink);
            }
            setMtu(linkProperties.mMtu);
            setDhcpServerAddress(linkProperties.getDhcpServerAddress());
            this.mTcpBufferSizes = linkProperties.mTcpBufferSizes;
            this.mNat64Prefix = linkProperties.mNat64Prefix;
            this.mWakeOnLanSupported = linkProperties.mWakeOnLanSupported;
            this.mCaptivePortalApiUrl = linkProperties.mCaptivePortalApiUrl;
            this.mCaptivePortalData = linkProperties.mCaptivePortalData;
        }
    }

    public void setInterfaceName(String str) {
        this.mIfaceName = str;
        ArrayList<RouteInfo> arrayList = new ArrayList<>(this.mRoutes.size());
        Iterator<RouteInfo> it = this.mRoutes.iterator();
        while (it.hasNext()) {
            arrayList.add(routeWithInterface(it.next()));
        }
        this.mRoutes = arrayList;
    }

    public String getInterfaceName() {
        return this.mIfaceName;
    }

    @SystemApi
    public List<String> getAllInterfaceNames() {
        ArrayList arrayList = new ArrayList(this.mStackedLinks.size() + 1);
        String str = this.mIfaceName;
        if (str != null) {
            arrayList.add(str);
        }
        for (LinkProperties allInterfaceNames : this.mStackedLinks.values()) {
            arrayList.addAll(allInterfaceNames.getAllInterfaceNames());
        }
        return arrayList;
    }

    @SystemApi
    public List<InetAddress> getAddresses() {
        ArrayList arrayList = new ArrayList();
        Iterator<LinkAddress> it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().getAddress());
        }
        return Collections.unmodifiableList(arrayList);
    }

    public List<InetAddress> getAllAddresses() {
        ArrayList arrayList = new ArrayList();
        Iterator<LinkAddress> it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().getAddress());
        }
        for (LinkProperties allAddresses : this.mStackedLinks.values()) {
            arrayList.addAll(allAddresses.getAllAddresses());
        }
        return arrayList;
    }

    private int findLinkAddressIndex(LinkAddress linkAddress) {
        for (int i = 0; i < this.mLinkAddresses.size(); i++) {
            if (this.mLinkAddresses.get(i).isSameAddressAs(linkAddress)) {
                return i;
            }
        }
        return -1;
    }

    @SystemApi
    public boolean addLinkAddress(LinkAddress linkAddress) {
        if (linkAddress == null) {
            return false;
        }
        int findLinkAddressIndex = findLinkAddressIndex(linkAddress);
        if (findLinkAddressIndex < 0) {
            this.mLinkAddresses.add(linkAddress);
            return true;
        } else if (this.mLinkAddresses.get(findLinkAddressIndex).equals(linkAddress)) {
            return false;
        } else {
            this.mLinkAddresses.set(findLinkAddressIndex, linkAddress);
            return true;
        }
    }

    @SystemApi
    public boolean removeLinkAddress(LinkAddress linkAddress) {
        int findLinkAddressIndex = findLinkAddressIndex(linkAddress);
        if (findLinkAddressIndex < 0) {
            return false;
        }
        this.mLinkAddresses.remove(findLinkAddressIndex);
        return true;
    }

    public List<LinkAddress> getLinkAddresses() {
        return Collections.unmodifiableList(this.mLinkAddresses);
    }

    @SystemApi
    public List<LinkAddress> getAllLinkAddresses() {
        ArrayList arrayList = new ArrayList(this.mLinkAddresses);
        for (LinkProperties allLinkAddresses : this.mStackedLinks.values()) {
            arrayList.addAll(allLinkAddresses.getAllLinkAddresses());
        }
        return arrayList;
    }

    public void setLinkAddresses(Collection<LinkAddress> collection) {
        this.mLinkAddresses.clear();
        for (LinkAddress addLinkAddress : collection) {
            addLinkAddress(addLinkAddress);
        }
    }

    @SystemApi
    public boolean addDnsServer(InetAddress inetAddress) {
        if (inetAddress == null || this.mDnses.contains(inetAddress)) {
            return false;
        }
        this.mDnses.add(inetAddress);
        return true;
    }

    @SystemApi
    public boolean removeDnsServer(InetAddress inetAddress) {
        return this.mDnses.remove((Object) inetAddress);
    }

    public void setDnsServers(Collection<InetAddress> collection) {
        this.mDnses.clear();
        for (InetAddress addDnsServer : collection) {
            addDnsServer(addDnsServer);
        }
    }

    public List<InetAddress> getDnsServers() {
        return Collections.unmodifiableList(this.mDnses);
    }

    @SystemApi
    public void setUsePrivateDns(boolean z) {
        this.mUsePrivateDns = z;
    }

    public boolean isPrivateDnsActive() {
        return this.mUsePrivateDns;
    }

    @SystemApi
    public void setPrivateDnsServerName(String str) {
        this.mPrivateDnsServerName = str;
    }

    public void setDhcpServerAddress(Inet4Address inet4Address) {
        this.mDhcpServerAddress = inet4Address;
    }

    public Inet4Address getDhcpServerAddress() {
        return this.mDhcpServerAddress;
    }

    public String getPrivateDnsServerName() {
        return this.mPrivateDnsServerName;
    }

    public boolean addValidatedPrivateDnsServer(InetAddress inetAddress) {
        if (inetAddress == null || this.mValidatedPrivateDnses.contains(inetAddress)) {
            return false;
        }
        this.mValidatedPrivateDnses.add(inetAddress);
        return true;
    }

    public boolean removeValidatedPrivateDnsServer(InetAddress inetAddress) {
        return this.mValidatedPrivateDnses.remove((Object) inetAddress);
    }

    @SystemApi
    public void setValidatedPrivateDnsServers(Collection<InetAddress> collection) {
        this.mValidatedPrivateDnses.clear();
        for (InetAddress addValidatedPrivateDnsServer : collection) {
            addValidatedPrivateDnsServer(addValidatedPrivateDnsServer);
        }
    }

    @SystemApi
    public List<InetAddress> getValidatedPrivateDnsServers() {
        return Collections.unmodifiableList(this.mValidatedPrivateDnses);
    }

    @SystemApi
    public boolean addPcscfServer(InetAddress inetAddress) {
        if (inetAddress == null || this.mPcscfs.contains(inetAddress)) {
            return false;
        }
        this.mPcscfs.add(inetAddress);
        return true;
    }

    public boolean removePcscfServer(InetAddress inetAddress) {
        return this.mPcscfs.remove((Object) inetAddress);
    }

    @SystemApi
    public void setPcscfServers(Collection<InetAddress> collection) {
        this.mPcscfs.clear();
        for (InetAddress addPcscfServer : collection) {
            addPcscfServer(addPcscfServer);
        }
    }

    @SystemApi
    public List<InetAddress> getPcscfServers() {
        return Collections.unmodifiableList(this.mPcscfs);
    }

    public void setDomains(String str) {
        this.mDomains = str;
    }

    public String getDomains() {
        return this.mDomains;
    }

    public void setMtu(int i) {
        this.mMtu = i;
    }

    public int getMtu() {
        return this.mMtu;
    }

    @SystemApi
    public void setTcpBufferSizes(String str) {
        this.mTcpBufferSizes = str;
    }

    @SystemApi
    public String getTcpBufferSizes() {
        return this.mTcpBufferSizes;
    }

    private RouteInfo routeWithInterface(RouteInfo routeInfo) {
        return new RouteInfo(routeInfo.getDestination(), routeInfo.getGateway(), this.mIfaceName, routeInfo.getType(), routeInfo.getMtu());
    }

    private int findRouteIndexByRouteKey(RouteInfo routeInfo) {
        for (int i = 0; i < this.mRoutes.size(); i++) {
            if (this.mRoutes.get(i).getRouteKey().equals(routeInfo.getRouteKey())) {
                return i;
            }
        }
        return -1;
    }

    public boolean addRoute(RouteInfo routeInfo) {
        String str = routeInfo.getInterface();
        if (str == null || str.equals(this.mIfaceName)) {
            RouteInfo routeWithInterface = routeWithInterface(routeInfo);
            int findRouteIndexByRouteKey = findRouteIndexByRouteKey(routeWithInterface);
            if (findRouteIndexByRouteKey == -1) {
                this.mRoutes.add(routeWithInterface);
                return true;
            } else if (this.mRoutes.get(findRouteIndexByRouteKey).equals(routeWithInterface)) {
                return false;
            } else {
                this.mRoutes.set(findRouteIndexByRouteKey, routeWithInterface);
                return true;
            }
        } else {
            throw new IllegalArgumentException("Route added with non-matching interface: " + str + " vs. " + this.mIfaceName);
        }
    }

    @SystemApi
    public boolean removeRoute(RouteInfo routeInfo) {
        return Objects.equals(this.mIfaceName, routeInfo.getInterface()) && this.mRoutes.remove((Object) routeInfo);
    }

    public List<RouteInfo> getRoutes() {
        if (CompatChanges.isChangeEnabled(EXCLUDED_ROUTES)) {
            return Collections.unmodifiableList(this.mRoutes);
        }
        return Collections.unmodifiableList(getUnicastRoutes());
    }

    private List<RouteInfo> getUnicastRoutes() {
        return (List) this.mRoutes.stream().filter(new LinkProperties$$ExternalSyntheticLambda0()).collect(Collectors.toList());
    }

    static /* synthetic */ boolean lambda$getUnicastRoutes$0(RouteInfo routeInfo) {
        return routeInfo.getType() == 1;
    }

    public void ensureDirectlyConnectedRoutes() {
        Iterator<LinkAddress> it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            addRoute(new RouteInfo(it.next(), (InetAddress) null, this.mIfaceName));
        }
    }

    @SystemApi
    public List<RouteInfo> getAllRoutes() {
        ArrayList arrayList = new ArrayList(getRoutes());
        for (LinkProperties allRoutes : this.mStackedLinks.values()) {
            arrayList.addAll(allRoutes.getAllRoutes());
        }
        return arrayList;
    }

    public void setHttpProxy(ProxyInfo proxyInfo) {
        this.mHttpProxy = proxyInfo;
    }

    public ProxyInfo getHttpProxy() {
        return this.mHttpProxy;
    }

    public IpPrefix getNat64Prefix() {
        return this.mNat64Prefix;
    }

    public void setNat64Prefix(IpPrefix ipPrefix) {
        if (ipPrefix == null || ipPrefix.getPrefixLength() == 96) {
            this.mNat64Prefix = ipPrefix;
            return;
        }
        throw new IllegalArgumentException("Only 96-bit prefixes are supported: " + ipPrefix);
    }

    public boolean addStackedLink(LinkProperties linkProperties) {
        if (linkProperties.getInterfaceName() == null) {
            return false;
        }
        this.mStackedLinks.put(linkProperties.getInterfaceName(), linkProperties);
        return true;
    }

    public boolean removeStackedLink(String str) {
        return this.mStackedLinks.remove(str) != null;
    }

    public List<LinkProperties> getStackedLinks() {
        if (this.mStackedLinks.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (LinkProperties linkProperties : this.mStackedLinks.values()) {
            arrayList.add(new LinkProperties(linkProperties));
        }
        return Collections.unmodifiableList(arrayList);
    }

    public void clear() {
        if (!this.mParcelSensitiveFields) {
            this.mIfaceName = null;
            this.mLinkAddresses.clear();
            this.mDnses.clear();
            this.mUsePrivateDns = false;
            this.mPrivateDnsServerName = null;
            this.mPcscfs.clear();
            this.mDomains = null;
            this.mRoutes.clear();
            this.mHttpProxy = null;
            this.mStackedLinks.clear();
            this.mMtu = 0;
            this.mDhcpServerAddress = null;
            this.mTcpBufferSizes = null;
            this.mNat64Prefix = null;
            this.mWakeOnLanSupported = false;
            this.mCaptivePortalApiUrl = null;
            this.mCaptivePortalData = null;
            return;
        }
        throw new UnsupportedOperationException("Cannot clear LinkProperties when parcelSensitiveFields is set");
    }

    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, "{", "}");
        if (this.mIfaceName != null) {
            stringJoiner.add("InterfaceName:");
            stringJoiner.add(this.mIfaceName);
        }
        stringJoiner.add("LinkAddresses: [");
        if (!this.mLinkAddresses.isEmpty()) {
            stringJoiner.add(TextUtils.join(NavigationBarInflaterView.BUTTON_SEPARATOR, this.mLinkAddresses));
        }
        stringJoiner.add(NavigationBarInflaterView.SIZE_MOD_END);
        stringJoiner.add("DnsAddresses: [");
        if (!this.mDnses.isEmpty()) {
            stringJoiner.add(TextUtils.join(NavigationBarInflaterView.BUTTON_SEPARATOR, this.mDnses));
        }
        stringJoiner.add(NavigationBarInflaterView.SIZE_MOD_END);
        if (this.mUsePrivateDns) {
            stringJoiner.add("UsePrivateDns: true");
        }
        if (this.mPrivateDnsServerName != null) {
            stringJoiner.add("PrivateDnsServerName:");
            stringJoiner.add(this.mPrivateDnsServerName);
        }
        if (!this.mPcscfs.isEmpty()) {
            stringJoiner.add("PcscfAddresses: [");
            stringJoiner.add(TextUtils.join(NavigationBarInflaterView.BUTTON_SEPARATOR, this.mPcscfs));
            stringJoiner.add(NavigationBarInflaterView.SIZE_MOD_END);
        }
        if (!this.mValidatedPrivateDnses.isEmpty()) {
            StringJoiner stringJoiner2 = new StringJoiner(NavigationBarInflaterView.BUTTON_SEPARATOR, "ValidatedPrivateDnsAddresses: [", NavigationBarInflaterView.SIZE_MOD_END);
            Iterator<InetAddress> it = this.mValidatedPrivateDnses.iterator();
            while (it.hasNext()) {
                stringJoiner2.add(it.next().getHostAddress());
            }
            stringJoiner.add(stringJoiner2.toString());
        }
        stringJoiner.add("Domains:");
        stringJoiner.add(this.mDomains);
        stringJoiner.add("MTU:");
        stringJoiner.add(Integer.toString(this.mMtu));
        if (this.mWakeOnLanSupported) {
            stringJoiner.add("WakeOnLanSupported: true");
        }
        if (this.mDhcpServerAddress != null) {
            stringJoiner.add("ServerAddress:");
            stringJoiner.add(this.mDhcpServerAddress.toString());
        }
        if (this.mCaptivePortalApiUrl != null) {
            stringJoiner.add("CaptivePortalApiUrl: " + this.mCaptivePortalApiUrl);
        }
        if (this.mCaptivePortalData != null) {
            stringJoiner.add("CaptivePortalData: " + this.mCaptivePortalData);
        }
        if (this.mTcpBufferSizes != null) {
            stringJoiner.add("TcpBufferSizes:");
            stringJoiner.add(this.mTcpBufferSizes);
        }
        stringJoiner.add("Routes: [");
        if (!this.mRoutes.isEmpty()) {
            stringJoiner.add(TextUtils.join(NavigationBarInflaterView.BUTTON_SEPARATOR, this.mRoutes));
        }
        stringJoiner.add(NavigationBarInflaterView.SIZE_MOD_END);
        if (this.mHttpProxy != null) {
            stringJoiner.add("HttpProxy:");
            stringJoiner.add(this.mHttpProxy.toString());
        }
        if (this.mNat64Prefix != null) {
            stringJoiner.add("Nat64Prefix:");
            stringJoiner.add(this.mNat64Prefix.toString());
        }
        Collection<LinkProperties> values = this.mStackedLinks.values();
        if (!values.isEmpty()) {
            StringJoiner stringJoiner3 = new StringJoiner(NavigationBarInflaterView.BUTTON_SEPARATOR, "Stacked: [", NavigationBarInflaterView.SIZE_MOD_END);
            for (LinkProperties linkProperties : values) {
                stringJoiner3.add("[ " + linkProperties + " ]");
            }
            stringJoiner.add(stringJoiner3.toString());
        }
        return stringJoiner.toString();
    }

    @SystemApi
    public boolean hasIpv4Address() {
        Iterator<LinkAddress> it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            if (it.next().getAddress() instanceof Inet4Address) {
                return true;
            }
        }
        return false;
    }

    public boolean hasIPv4Address() {
        return hasIpv4Address();
    }

    private boolean hasIpv4AddressOnInterface(String str) {
        return (Objects.equals(str, this.mIfaceName) && hasIpv4Address()) || (str != null && this.mStackedLinks.containsKey(str) && this.mStackedLinks.get(str).hasIpv4Address());
    }

    @SystemApi
    public boolean hasGlobalIpv6Address() {
        Iterator<LinkAddress> it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            LinkAddress next = it.next();
            if ((next.getAddress() instanceof Inet6Address) && next.isGlobalPreferred()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasIpv4UnreachableDefaultRoute() {
        Iterator<RouteInfo> it = this.mRoutes.iterator();
        while (it.hasNext()) {
            if (it.next().isIPv4UnreachableDefault()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasGlobalIPv6Address() {
        return hasGlobalIpv6Address();
    }

    @SystemApi
    public boolean hasIpv4DefaultRoute() {
        Iterator<RouteInfo> it = this.mRoutes.iterator();
        while (it.hasNext()) {
            if (it.next().isIPv4Default()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasIpv6UnreachableDefaultRoute() {
        Iterator<RouteInfo> it = this.mRoutes.iterator();
        while (it.hasNext()) {
            if (it.next().isIPv6UnreachableDefault()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasIPv4DefaultRoute() {
        return hasIpv4DefaultRoute();
    }

    @SystemApi
    public boolean hasIpv6DefaultRoute() {
        Iterator<RouteInfo> it = this.mRoutes.iterator();
        while (it.hasNext()) {
            if (it.next().isIPv6Default()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasIPv6DefaultRoute() {
        return hasIpv6DefaultRoute();
    }

    @SystemApi
    public boolean hasIpv4DnsServer() {
        Iterator<InetAddress> it = this.mDnses.iterator();
        while (it.hasNext()) {
            if (it.next() instanceof Inet4Address) {
                return true;
            }
        }
        return false;
    }

    public boolean hasIPv4DnsServer() {
        return hasIpv4DnsServer();
    }

    @SystemApi
    public boolean hasIpv6DnsServer() {
        Iterator<InetAddress> it = this.mDnses.iterator();
        while (it.hasNext()) {
            if (it.next() instanceof Inet6Address) {
                return true;
            }
        }
        return false;
    }

    public boolean hasIPv6DnsServer() {
        return hasIpv6DnsServer();
    }

    public boolean hasIpv4PcscfServer() {
        Iterator<InetAddress> it = this.mPcscfs.iterator();
        while (it.hasNext()) {
            if (it.next() instanceof Inet4Address) {
                return true;
            }
        }
        return false;
    }

    public boolean hasIpv6PcscfServer() {
        Iterator<InetAddress> it = this.mPcscfs.iterator();
        while (it.hasNext()) {
            if (it.next() instanceof Inet6Address) {
                return true;
            }
        }
        return false;
    }

    @SystemApi
    public boolean isIpv4Provisioned() {
        return hasIpv4Address() && hasIpv4DefaultRoute() && hasIpv4DnsServer();
    }

    @SystemApi
    public boolean isIpv6Provisioned() {
        return hasGlobalIpv6Address() && hasIpv6DefaultRoute() && hasIpv6DnsServer();
    }

    public boolean isIPv6Provisioned() {
        return isIpv6Provisioned();
    }

    @SystemApi
    public boolean isProvisioned() {
        return isIpv4Provisioned() || isIpv6Provisioned();
    }

    @SystemApi
    public boolean isReachable(InetAddress inetAddress) {
        RouteInfo selectBestRoute = RouteInfo.selectBestRoute(getAllRoutes(), inetAddress);
        if (selectBestRoute == null) {
            return false;
        }
        if (inetAddress instanceof Inet4Address) {
            return hasIpv4AddressOnInterface(selectBestRoute.getInterface());
        }
        if (!(inetAddress instanceof Inet6Address)) {
            return false;
        }
        if (inetAddress.isLinkLocalAddress()) {
            if (((Inet6Address) inetAddress).getScopeId() != 0) {
                return true;
            }
            return false;
        } else if (!selectBestRoute.hasGateway() || hasGlobalIpv6Address()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasExcludeRoute() {
        Iterator<RouteInfo> it = this.mRoutes.iterator();
        while (it.hasNext()) {
            if (it.next().getType() == 9) {
                return true;
            }
        }
        return false;
    }

    public boolean isIdenticalInterfaceName(LinkProperties linkProperties) {
        return LinkPropertiesUtils.isIdenticalInterfaceName(linkProperties, this);
    }

    public boolean isIdenticalDhcpServerAddress(LinkProperties linkProperties) {
        return Objects.equals(this.mDhcpServerAddress, linkProperties.mDhcpServerAddress);
    }

    public boolean isIdenticalAddresses(LinkProperties linkProperties) {
        return LinkPropertiesUtils.isIdenticalAddresses(linkProperties, this);
    }

    public boolean isIdenticalDnses(LinkProperties linkProperties) {
        return LinkPropertiesUtils.isIdenticalDnses(linkProperties, this);
    }

    public boolean isIdenticalPrivateDns(LinkProperties linkProperties) {
        return isPrivateDnsActive() == linkProperties.isPrivateDnsActive() && TextUtils.equals(getPrivateDnsServerName(), linkProperties.getPrivateDnsServerName());
    }

    public boolean isIdenticalValidatedPrivateDnses(LinkProperties linkProperties) {
        List<InetAddress> validatedPrivateDnsServers = linkProperties.getValidatedPrivateDnsServers();
        if (this.mValidatedPrivateDnses.size() == validatedPrivateDnsServers.size()) {
            return this.mValidatedPrivateDnses.containsAll(validatedPrivateDnsServers);
        }
        return false;
    }

    public boolean isIdenticalPcscfs(LinkProperties linkProperties) {
        List<InetAddress> pcscfServers = linkProperties.getPcscfServers();
        if (this.mPcscfs.size() == pcscfServers.size()) {
            return this.mPcscfs.containsAll(pcscfServers);
        }
        return false;
    }

    public boolean isIdenticalRoutes(LinkProperties linkProperties) {
        return LinkPropertiesUtils.isIdenticalRoutes(linkProperties, this);
    }

    public boolean isIdenticalHttpProxy(LinkProperties linkProperties) {
        return LinkPropertiesUtils.isIdenticalHttpProxy(linkProperties, this);
    }

    public boolean isIdenticalStackedLinks(LinkProperties linkProperties) {
        if (!this.mStackedLinks.keySet().equals(linkProperties.mStackedLinks.keySet())) {
            return false;
        }
        for (LinkProperties next : this.mStackedLinks.values()) {
            if (!next.equals(linkProperties.mStackedLinks.get(next.getInterfaceName()))) {
                return false;
            }
        }
        return true;
    }

    public boolean isIdenticalMtu(LinkProperties linkProperties) {
        return getMtu() == linkProperties.getMtu();
    }

    public boolean isIdenticalTcpBufferSizes(LinkProperties linkProperties) {
        return Objects.equals(this.mTcpBufferSizes, linkProperties.mTcpBufferSizes);
    }

    public boolean isIdenticalNat64Prefix(LinkProperties linkProperties) {
        return Objects.equals(this.mNat64Prefix, linkProperties.mNat64Prefix);
    }

    public boolean isIdenticalWakeOnLan(LinkProperties linkProperties) {
        return isWakeOnLanSupported() == linkProperties.isWakeOnLanSupported();
    }

    public boolean isIdenticalCaptivePortalApiUrl(LinkProperties linkProperties) {
        return Objects.equals(this.mCaptivePortalApiUrl, linkProperties.mCaptivePortalApiUrl);
    }

    public boolean isIdenticalCaptivePortalData(LinkProperties linkProperties) {
        return Objects.equals(this.mCaptivePortalData, linkProperties.mCaptivePortalData);
    }

    public void setWakeOnLanSupported(boolean z) {
        this.mWakeOnLanSupported = z;
    }

    public boolean isWakeOnLanSupported() {
        return this.mWakeOnLanSupported;
    }

    @SystemApi
    public void setCaptivePortalApiUrl(Uri uri) {
        this.mCaptivePortalApiUrl = uri;
    }

    @SystemApi
    public Uri getCaptivePortalApiUrl() {
        return this.mCaptivePortalApiUrl;
    }

    @SystemApi
    public void setCaptivePortalData(CaptivePortalData captivePortalData) {
        this.mCaptivePortalData = captivePortalData;
    }

    @SystemApi
    public CaptivePortalData getCaptivePortalData() {
        return this.mCaptivePortalData;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LinkProperties)) {
            return false;
        }
        LinkProperties linkProperties = (LinkProperties) obj;
        if (!isIdenticalInterfaceName(linkProperties) || !isIdenticalAddresses(linkProperties) || !isIdenticalDhcpServerAddress(linkProperties) || !isIdenticalDnses(linkProperties) || !isIdenticalPrivateDns(linkProperties) || !isIdenticalValidatedPrivateDnses(linkProperties) || !isIdenticalPcscfs(linkProperties) || !isIdenticalRoutes(linkProperties) || !isIdenticalHttpProxy(linkProperties) || !isIdenticalStackedLinks(linkProperties) || !isIdenticalMtu(linkProperties) || !isIdenticalTcpBufferSizes(linkProperties) || !isIdenticalNat64Prefix(linkProperties) || !isIdenticalWakeOnLan(linkProperties) || !isIdenticalCaptivePortalApiUrl(linkProperties) || !isIdenticalCaptivePortalData(linkProperties)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        String str = this.mIfaceName;
        if (str == null) {
            i = 0;
        } else {
            int hashCode = str.hashCode() + (this.mLinkAddresses.size() * 31) + (this.mDnses.size() * 37) + (this.mValidatedPrivateDnses.size() * 61);
            String str2 = this.mDomains;
            int hashCode2 = hashCode + (str2 == null ? 0 : str2.hashCode()) + (this.mRoutes.size() * 41);
            ProxyInfo proxyInfo = this.mHttpProxy;
            i = hashCode2 + (proxyInfo == null ? 0 : proxyInfo.hashCode()) + (this.mStackedLinks.hashCode() * 47);
        }
        int i2 = i + (this.mMtu * 51);
        String str3 = this.mTcpBufferSizes;
        int hashCode3 = i2 + (str3 == null ? 0 : str3.hashCode()) + (this.mUsePrivateDns ? 57 : 0);
        Inet4Address inet4Address = this.mDhcpServerAddress;
        int hashCode4 = hashCode3 + (inet4Address == null ? 0 : inet4Address.hashCode()) + (this.mPcscfs.size() * 67);
        String str4 = this.mPrivateDnsServerName;
        return hashCode4 + (str4 == null ? 0 : str4.hashCode()) + Objects.hash(this.mNat64Prefix) + (this.mWakeOnLanSupported ? 71 : 0) + Objects.hash(this.mCaptivePortalApiUrl, this.mCaptivePortalData);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getInterfaceName());
        parcel.writeInt(this.mLinkAddresses.size());
        Iterator<LinkAddress> it = this.mLinkAddresses.iterator();
        while (it.hasNext()) {
            parcel.writeParcelable(it.next(), i);
        }
        writeAddresses(parcel, this.mDnses);
        writeAddresses(parcel, this.mValidatedPrivateDnses);
        parcel.writeBoolean(this.mUsePrivateDns);
        parcel.writeString(this.mPrivateDnsServerName);
        writeAddresses(parcel, this.mPcscfs);
        parcel.writeString(this.mDomains);
        writeAddress(parcel, this.mDhcpServerAddress);
        parcel.writeInt(this.mMtu);
        parcel.writeString(this.mTcpBufferSizes);
        parcel.writeInt(this.mRoutes.size());
        Iterator<RouteInfo> it2 = this.mRoutes.iterator();
        while (it2.hasNext()) {
            parcel.writeParcelable(it2.next(), i);
        }
        if (this.mHttpProxy != null) {
            parcel.writeByte((byte) 1);
            parcel.writeParcelable(this.mHttpProxy, i);
        } else {
            parcel.writeByte((byte) 0);
        }
        parcel.writeParcelable(this.mNat64Prefix, 0);
        parcel.writeList(new ArrayList(this.mStackedLinks.values()));
        parcel.writeBoolean(this.mWakeOnLanSupported);
        CaptivePortalData captivePortalData = null;
        parcel.writeParcelable(this.mParcelSensitiveFields ? this.mCaptivePortalApiUrl : null, 0);
        if (this.mParcelSensitiveFields) {
            captivePortalData = this.mCaptivePortalData;
        }
        parcel.writeParcelable(captivePortalData, 0);
    }

    private static void writeAddresses(Parcel parcel, List<InetAddress> list) {
        parcel.writeInt(list.size());
        for (InetAddress writeAddress : list) {
            writeAddress(parcel, writeAddress);
        }
    }

    private static void writeAddress(Parcel parcel, InetAddress inetAddress) {
        parcel.writeByteArray(inetAddress == null ? null : inetAddress.getAddress());
        if (inetAddress instanceof Inet6Address) {
            Inet6Address inet6Address = (Inet6Address) inetAddress;
            boolean z = inet6Address.getScopeId() != 0;
            parcel.writeBoolean(z);
            if (z) {
                parcel.writeInt(inet6Address.getScopeId());
            }
        }
    }

    /* access modifiers changed from: private */
    public static InetAddress readAddress(Parcel parcel) throws UnknownHostException {
        byte[] createByteArray = parcel.createByteArray();
        if (createByteArray == null) {
            return null;
        }
        if (createByteArray.length != 16) {
            return InetAddress.getByAddress(createByteArray);
        }
        return Inet6Address.getByAddress((String) null, createByteArray, parcel.readBoolean() ? parcel.readInt() : 0);
    }
}

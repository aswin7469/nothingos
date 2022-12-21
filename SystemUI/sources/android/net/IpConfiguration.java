package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

public final class IpConfiguration implements Parcelable {
    public static final Parcelable.Creator<IpConfiguration> CREATOR = new Parcelable.Creator<IpConfiguration>() {
        public IpConfiguration createFromParcel(Parcel parcel) {
            IpConfiguration ipConfiguration = new IpConfiguration();
            ipConfiguration.ipAssignment = IpAssignment.valueOf(parcel.readString());
            ipConfiguration.proxySettings = ProxySettings.valueOf(parcel.readString());
            ipConfiguration.staticIpConfiguration = (StaticIpConfiguration) parcel.readParcelable((ClassLoader) null);
            ipConfiguration.httpProxy = (ProxyInfo) parcel.readParcelable((ClassLoader) null);
            return ipConfiguration;
        }

        public IpConfiguration[] newArray(int i) {
            return new IpConfiguration[i];
        }
    };
    private static final String TAG = "IpConfiguration";
    public ProxyInfo httpProxy;
    public IpAssignment ipAssignment;
    public ProxySettings proxySettings;
    public StaticIpConfiguration staticIpConfiguration;

    @SystemApi
    public enum IpAssignment {
        STATIC,
        DHCP,
        UNASSIGNED
    }

    @SystemApi
    public enum ProxySettings {
        NONE,
        STATIC,
        UNASSIGNED,
        PAC
    }

    public int describeContents() {
        return 0;
    }

    private void init(IpAssignment ipAssignment2, ProxySettings proxySettings2, StaticIpConfiguration staticIpConfiguration2, ProxyInfo proxyInfo) {
        StaticIpConfiguration staticIpConfiguration3;
        this.ipAssignment = ipAssignment2;
        this.proxySettings = proxySettings2;
        ProxyInfo proxyInfo2 = null;
        if (staticIpConfiguration2 == null) {
            staticIpConfiguration3 = null;
        } else {
            staticIpConfiguration3 = new StaticIpConfiguration(staticIpConfiguration2);
        }
        this.staticIpConfiguration = staticIpConfiguration3;
        if (proxyInfo != null) {
            proxyInfo2 = new ProxyInfo(proxyInfo);
        }
        this.httpProxy = proxyInfo2;
    }

    @SystemApi
    public IpConfiguration() {
        init(IpAssignment.UNASSIGNED, ProxySettings.UNASSIGNED, (StaticIpConfiguration) null, (ProxyInfo) null);
    }

    public IpConfiguration(IpAssignment ipAssignment2, ProxySettings proxySettings2, StaticIpConfiguration staticIpConfiguration2, ProxyInfo proxyInfo) {
        init(ipAssignment2, proxySettings2, staticIpConfiguration2, proxyInfo);
    }

    @SystemApi
    public IpConfiguration(IpConfiguration ipConfiguration) {
        this();
        if (ipConfiguration != null) {
            init(ipConfiguration.ipAssignment, ipConfiguration.proxySettings, ipConfiguration.staticIpConfiguration, ipConfiguration.httpProxy);
        }
    }

    @SystemApi
    public IpAssignment getIpAssignment() {
        return this.ipAssignment;
    }

    @SystemApi
    public void setIpAssignment(IpAssignment ipAssignment2) {
        this.ipAssignment = ipAssignment2;
    }

    public StaticIpConfiguration getStaticIpConfiguration() {
        return this.staticIpConfiguration;
    }

    @SystemApi
    public void setStaticIpConfiguration(StaticIpConfiguration staticIpConfiguration2) {
        this.staticIpConfiguration = staticIpConfiguration2;
    }

    @SystemApi
    public ProxySettings getProxySettings() {
        return this.proxySettings;
    }

    @SystemApi
    public void setProxySettings(ProxySettings proxySettings2) {
        this.proxySettings = proxySettings2;
    }

    public ProxyInfo getHttpProxy() {
        return this.httpProxy;
    }

    @SystemApi
    public void setHttpProxy(ProxyInfo proxyInfo) {
        this.httpProxy = proxyInfo;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IP assignment: " + this.ipAssignment.toString());
        sb.append("\n");
        if (this.staticIpConfiguration != null) {
            sb.append("Static configuration: " + this.staticIpConfiguration.toString());
            sb.append("\n");
        }
        sb.append("Proxy settings: " + this.proxySettings.toString());
        sb.append("\n");
        if (this.httpProxy != null) {
            sb.append("HTTP proxy: " + this.httpProxy.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IpConfiguration)) {
            return false;
        }
        IpConfiguration ipConfiguration = (IpConfiguration) obj;
        if (this.ipAssignment != ipConfiguration.ipAssignment || this.proxySettings != ipConfiguration.proxySettings || !Objects.equals(this.staticIpConfiguration, ipConfiguration.staticIpConfiguration) || !Objects.equals(this.httpProxy, ipConfiguration.httpProxy)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        StaticIpConfiguration staticIpConfiguration2 = this.staticIpConfiguration;
        return (staticIpConfiguration2 != null ? staticIpConfiguration2.hashCode() : 0) + 13 + (this.ipAssignment.ordinal() * 17) + (this.proxySettings.ordinal() * 47) + (this.httpProxy.hashCode() * 83);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.ipAssignment.name());
        parcel.writeString(this.proxySettings.name());
        parcel.writeParcelable(this.staticIpConfiguration, i);
        parcel.writeParcelable(this.httpProxy, i);
    }

    public static final class Builder {
        private ProxyInfo mProxyInfo;
        private StaticIpConfiguration mStaticIpConfiguration;

        public Builder setStaticIpConfiguration(StaticIpConfiguration staticIpConfiguration) {
            this.mStaticIpConfiguration = staticIpConfiguration;
            return this;
        }

        public Builder setHttpProxy(ProxyInfo proxyInfo) {
            this.mProxyInfo = proxyInfo;
            return this;
        }

        public IpConfiguration build() {
            ProxySettings proxySettings;
            IpConfiguration ipConfiguration = new IpConfiguration();
            ipConfiguration.setStaticIpConfiguration(this.mStaticIpConfiguration);
            ipConfiguration.setIpAssignment(this.mStaticIpConfiguration == null ? IpAssignment.DHCP : IpAssignment.STATIC);
            ipConfiguration.setHttpProxy(this.mProxyInfo);
            ProxyInfo proxyInfo = this.mProxyInfo;
            if (proxyInfo == null) {
                ipConfiguration.setProxySettings(ProxySettings.NONE);
            } else {
                if (proxyInfo.getPacFileUrl() == null) {
                    proxySettings = ProxySettings.STATIC;
                } else {
                    proxySettings = ProxySettings.PAC;
                }
                ipConfiguration.setProxySettings(proxySettings);
            }
            return ipConfiguration;
        }
    }
}

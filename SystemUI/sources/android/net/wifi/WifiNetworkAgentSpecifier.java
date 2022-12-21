package android.net.wifi;

import android.net.MacAddress;
import android.net.MatchAllNetworkSpecifier;
import android.net.NetworkSpecifier;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.wifi.p018x.com.android.internal.util.Preconditions;
import java.util.Objects;

public final class WifiNetworkAgentSpecifier extends NetworkSpecifier implements Parcelable {
    public static final Parcelable.Creator<WifiNetworkAgentSpecifier> CREATOR = new Parcelable.Creator<WifiNetworkAgentSpecifier>() {
        public WifiNetworkAgentSpecifier createFromParcel(Parcel parcel) {
            return new WifiNetworkAgentSpecifier((WifiConfiguration) parcel.readParcelable((ClassLoader) null), parcel.readInt(), parcel.readBoolean());
        }

        public WifiNetworkAgentSpecifier[] newArray(int i) {
            return new WifiNetworkAgentSpecifier[i];
        }
    };
    private final int mBand;
    private final boolean mMatchLocalOnlySpecifiers;
    private final WifiConfiguration mWifiConfiguration;

    public int describeContents() {
        return 0;
    }

    public NetworkSpecifier redact() {
        return null;
    }

    public WifiNetworkAgentSpecifier(WifiConfiguration wifiConfiguration, int i, boolean z) {
        Preconditions.checkNotNull(wifiConfiguration);
        this.mWifiConfiguration = wifiConfiguration;
        this.mBand = i;
        this.mMatchLocalOnlySpecifiers = z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mWifiConfiguration, i);
        parcel.writeInt(this.mBand);
        parcel.writeBoolean(this.mMatchLocalOnlySpecifiers);
    }

    public boolean canBeSatisfiedBy(NetworkSpecifier networkSpecifier) {
        if (this == networkSpecifier || networkSpecifier == null || (networkSpecifier instanceof MatchAllNetworkSpecifier)) {
            return true;
        }
        if (networkSpecifier instanceof WifiNetworkSpecifier) {
            return satisfiesNetworkSpecifier((WifiNetworkSpecifier) networkSpecifier);
        }
        return equals(networkSpecifier);
    }

    public boolean satisfiesNetworkSpecifier(WifiNetworkSpecifier wifiNetworkSpecifier) {
        Preconditions.checkNotNull(wifiNetworkSpecifier);
        Preconditions.checkNotNull(wifiNetworkSpecifier.ssidPatternMatcher);
        Preconditions.checkNotNull(wifiNetworkSpecifier.bssidPatternMatcher);
        Preconditions.checkNotNull(wifiNetworkSpecifier.wifiConfiguration.allowedKeyManagement);
        Preconditions.checkNotNull(this.mWifiConfiguration.SSID);
        Preconditions.checkNotNull(this.mWifiConfiguration.BSSID);
        Preconditions.checkNotNull(this.mWifiConfiguration.allowedKeyManagement);
        if (!this.mMatchLocalOnlySpecifiers) {
            if (wifiNetworkSpecifier.getBand() == this.mBand) {
                return true;
            }
            return false;
        } else if (wifiNetworkSpecifier.getBand() != -1 && wifiNetworkSpecifier.getBand() != this.mBand) {
            return false;
        } else {
            String str = this.mWifiConfiguration.SSID;
            Preconditions.checkState(str.startsWith("\"") && str.endsWith("\""));
            return wifiNetworkSpecifier.ssidPatternMatcher.match(str.substring(1, str.length() - 1)) && MacAddress.fromString(this.mWifiConfiguration.BSSID).matches((MacAddress) wifiNetworkSpecifier.bssidPatternMatcher.first, (MacAddress) wifiNetworkSpecifier.bssidPatternMatcher.second) && wifiNetworkSpecifier.wifiConfiguration.allowedKeyManagement.equals(this.mWifiConfiguration.allowedKeyManagement);
        }
    }

    public int hashCode() {
        return Objects.hash(this.mWifiConfiguration.SSID, this.mWifiConfiguration.BSSID, this.mWifiConfiguration.allowedKeyManagement, Integer.valueOf(this.mBand), Boolean.valueOf(this.mMatchLocalOnlySpecifiers));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WifiNetworkAgentSpecifier)) {
            return false;
        }
        WifiNetworkAgentSpecifier wifiNetworkAgentSpecifier = (WifiNetworkAgentSpecifier) obj;
        if (!Objects.equals(this.mWifiConfiguration.SSID, wifiNetworkAgentSpecifier.mWifiConfiguration.SSID) || !Objects.equals(this.mWifiConfiguration.BSSID, wifiNetworkAgentSpecifier.mWifiConfiguration.BSSID) || !Objects.equals(this.mWifiConfiguration.allowedKeyManagement, wifiNetworkAgentSpecifier.mWifiConfiguration.allowedKeyManagement) || this.mBand != wifiNetworkAgentSpecifier.mBand || this.mMatchLocalOnlySpecifiers != wifiNetworkAgentSpecifier.mMatchLocalOnlySpecifiers) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "WifiNetworkAgentSpecifier [WifiConfiguration=, SSID=" + this.mWifiConfiguration.SSID + ", BSSID=" + this.mWifiConfiguration.BSSID + ", band=" + this.mBand + ", mMatchLocalOnlySpecifiers=" + this.mMatchLocalOnlySpecifiers + NavigationBarInflaterView.SIZE_MOD_END;
    }
}

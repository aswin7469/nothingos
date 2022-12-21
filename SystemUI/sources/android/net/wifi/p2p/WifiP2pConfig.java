package android.net.wifi.p2p;

import android.net.MacAddress;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.net.wifi.WpsInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.StandardCharsets;
import java.util.regex.PatternSyntaxException;

public class WifiP2pConfig implements Parcelable {
    public static final Parcelable.Creator<WifiP2pConfig> CREATOR = new Parcelable.Creator<WifiP2pConfig>() {
        public WifiP2pConfig createFromParcel(Parcel parcel) {
            WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
            wifiP2pConfig.deviceAddress = parcel.readString();
            wifiP2pConfig.wps = (WpsInfo) parcel.readParcelable((ClassLoader) null);
            wifiP2pConfig.groupOwnerIntent = parcel.readInt();
            wifiP2pConfig.netId = parcel.readInt();
            wifiP2pConfig.networkName = parcel.readString();
            wifiP2pConfig.passphrase = parcel.readString();
            wifiP2pConfig.groupOwnerBand = parcel.readInt();
            return wifiP2pConfig;
        }

        public WifiP2pConfig[] newArray(int i) {
            return new WifiP2pConfig[i];
        }
    };
    public static final int GROUP_OWNER_BAND_2GHZ = 1;
    public static final int GROUP_OWNER_BAND_5GHZ = 2;
    public static final int GROUP_OWNER_BAND_AUTO = 0;
    public static final int GROUP_OWNER_INTENT_AUTO = -1;
    public static final int GROUP_OWNER_INTENT_MAX = 15;
    public static final int GROUP_OWNER_INTENT_MIN = 0;
    public String deviceAddress = "";
    public int groupOwnerBand = 0;
    public int groupOwnerIntent = -1;
    public int netId = -2;
    public String networkName = "";
    public String passphrase = "";
    public WpsInfo wps;

    @Retention(RetentionPolicy.SOURCE)
    public @interface GroupOperatingBandType {
    }

    public int describeContents() {
        return 0;
    }

    public String getNetworkName() {
        return this.networkName;
    }

    public String getPassphrase() {
        return this.passphrase;
    }

    public int getGroupOwnerBand() {
        return this.groupOwnerBand;
    }

    public int getNetworkId() {
        return this.netId;
    }

    public WifiP2pConfig() {
        WpsInfo wpsInfo = new WpsInfo();
        this.wps = wpsInfo;
        wpsInfo.setup = 0;
    }

    public void invalidate() {
        this.deviceAddress = "";
    }

    public WifiP2pConfig(String str) throws IllegalArgumentException {
        int i;
        String[] split = str.split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        if (split.length < 2 || !split[0].equals("P2P-GO-NEG-REQUEST")) {
            throw new IllegalArgumentException("Malformed supplicant event");
        }
        this.deviceAddress = split[1];
        this.wps = new WpsInfo();
        if (split.length > 2) {
            try {
                i = Integer.parseInt(split[2].split("=")[1]);
            } catch (NumberFormatException unused) {
                i = 0;
            }
            if (i == 1) {
                this.wps.setup = 1;
            } else if (i == 4) {
                this.wps.setup = 0;
            } else if (i != 5) {
                this.wps.setup = 0;
            } else {
                this.wps.setup = 2;
            }
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("\n address: ");
        stringBuffer.append(this.deviceAddress);
        stringBuffer.append("\n wps: ").append((Object) this.wps);
        stringBuffer.append("\n groupOwnerIntent: ").append(this.groupOwnerIntent);
        stringBuffer.append("\n persist: ").append(this.netId);
        stringBuffer.append("\n networkName: ").append(this.networkName);
        stringBuffer.append("\n passphrase: ").append(TextUtils.isEmpty(this.passphrase) ? "<empty>" : "<non-empty>");
        stringBuffer.append("\n groupOwnerBand: ").append(this.groupOwnerBand);
        return stringBuffer.toString();
    }

    public WifiP2pConfig(WifiP2pConfig wifiP2pConfig) {
        if (wifiP2pConfig != null) {
            this.deviceAddress = wifiP2pConfig.deviceAddress;
            this.wps = new WpsInfo(wifiP2pConfig.wps);
            this.groupOwnerIntent = wifiP2pConfig.groupOwnerIntent;
            this.netId = wifiP2pConfig.netId;
            this.networkName = wifiP2pConfig.networkName;
            this.passphrase = wifiP2pConfig.passphrase;
            this.groupOwnerBand = wifiP2pConfig.groupOwnerBand;
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.deviceAddress);
        parcel.writeParcelable(this.wps, i);
        parcel.writeInt(this.groupOwnerIntent);
        parcel.writeInt(this.netId);
        parcel.writeString(this.networkName);
        parcel.writeString(this.passphrase);
        parcel.writeInt(this.groupOwnerBand);
    }

    public static final class Builder {
        private static final MacAddress MAC_ANY_ADDRESS = MacAddress.fromString(WifiInfo.DEFAULT_MAC_ADDRESS);
        private static final int MAX_SSID_BYTES = 32;
        private MacAddress mDeviceAddress = MAC_ANY_ADDRESS;
        private int mGroupOperatingBand = 0;
        private int mGroupOperatingFrequency = 0;
        private int mNetId = -1;
        private String mNetworkName = "";
        private String mPassphrase = "";

        public Builder setDeviceAddress(MacAddress macAddress) {
            if (macAddress == null) {
                this.mDeviceAddress = MAC_ANY_ADDRESS;
            } else {
                this.mDeviceAddress = macAddress;
            }
            return this;
        }

        public Builder setNetworkName(String str) {
            if (TextUtils.isEmpty(str)) {
                throw new IllegalArgumentException("network name must be non-empty.");
            } else if (str.getBytes(StandardCharsets.UTF_8).length <= 32) {
                try {
                    if (str.matches("^DIRECT-[a-zA-Z0-9]{2}.*")) {
                        this.mNetworkName = str;
                        return this;
                    }
                    throw new IllegalArgumentException("network name must starts with the prefix DIRECT-xy.");
                } catch (PatternSyntaxException unused) {
                }
            } else {
                throw new IllegalArgumentException("network name exceeds 32 bytes.");
            }
        }

        public Builder setPassphrase(String str) {
            if (TextUtils.isEmpty(str)) {
                throw new IllegalArgumentException("passphrase must be non-empty.");
            } else if (str.length() < 8 || str.length() > 63) {
                throw new IllegalArgumentException("The length of a passphrase must be between 8 and 63.");
            } else {
                this.mPassphrase = str;
                return this;
            }
        }

        public Builder setGroupOperatingBand(int i) {
            if (i == 0 || i == 1 || i == 2) {
                this.mGroupOperatingBand = i;
                return this;
            }
            throw new IllegalArgumentException("Invalid constant for the group operating band!");
        }

        public Builder setGroupOperatingFrequency(int i) {
            if (i >= 0) {
                this.mGroupOperatingFrequency = i;
                return this;
            }
            throw new IllegalArgumentException("Invalid group operating frequency!");
        }

        public Builder enablePersistentMode(boolean z) {
            if (z) {
                this.mNetId = -2;
            } else {
                this.mNetId = -1;
            }
            return this;
        }

        public WifiP2pConfig build() {
            if ((TextUtils.isEmpty(this.mNetworkName) && !TextUtils.isEmpty(this.mPassphrase)) || (!TextUtils.isEmpty(this.mNetworkName) && TextUtils.isEmpty(this.mPassphrase))) {
                throw new IllegalStateException("network name and passphrase must be non-empty or empty both.");
            } else if (TextUtils.isEmpty(this.mNetworkName) && this.mDeviceAddress.equals(MAC_ANY_ADDRESS)) {
                throw new IllegalStateException("peer address must be set if network name and pasphrase are not set.");
            } else if (this.mGroupOperatingFrequency <= 0 || this.mGroupOperatingBand <= 0) {
                WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
                wifiP2pConfig.deviceAddress = this.mDeviceAddress.toString();
                wifiP2pConfig.networkName = this.mNetworkName;
                wifiP2pConfig.passphrase = this.mPassphrase;
                wifiP2pConfig.groupOwnerBand = 0;
                int i = this.mGroupOperatingFrequency;
                if (i > 0) {
                    wifiP2pConfig.groupOwnerBand = i;
                } else {
                    int i2 = this.mGroupOperatingBand;
                    if (i2 > 0) {
                        wifiP2pConfig.groupOwnerBand = i2;
                    }
                }
                wifiP2pConfig.netId = this.mNetId;
                return wifiP2pConfig;
            } else {
                throw new IllegalStateException("Preferred frequency and band are mutually exclusive.");
            }
        }
    }
}

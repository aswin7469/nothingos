package android.net.wifi;

import android.annotation.SystemApi;
import android.net.MacAddress;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.wifi.p018x.com.android.internal.util.Preconditions;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.util.Objects;

@SystemApi
public final class SoftApInfo implements Parcelable {
    public static final int CHANNEL_WIDTH_160MHZ = 6;
    public static final int CHANNEL_WIDTH_20MHZ = 2;
    public static final int CHANNEL_WIDTH_20MHZ_NOHT = 1;
    public static final int CHANNEL_WIDTH_2160MHZ = 7;
    public static final int CHANNEL_WIDTH_320MHZ = 11;
    public static final int CHANNEL_WIDTH_40MHZ = 3;
    public static final int CHANNEL_WIDTH_4320MHZ = 8;
    public static final int CHANNEL_WIDTH_6480MHZ = 9;
    public static final int CHANNEL_WIDTH_80MHZ = 4;
    public static final int CHANNEL_WIDTH_80MHZ_PLUS_MHZ = 5;
    public static final int CHANNEL_WIDTH_8640MHZ = 10;
    public static final int CHANNEL_WIDTH_AUTO = -1;
    public static final int CHANNEL_WIDTH_INVALID = 0;
    public static final Parcelable.Creator<SoftApInfo> CREATOR = new Parcelable.Creator<SoftApInfo>() {
        public SoftApInfo createFromParcel(Parcel parcel) {
            SoftApInfo softApInfo = new SoftApInfo();
            softApInfo.mFrequency = parcel.readInt();
            softApInfo.mBandwidth = parcel.readInt();
            softApInfo.mBssid = (MacAddress) parcel.readParcelable(MacAddress.class.getClassLoader());
            softApInfo.mWifiStandard = parcel.readInt();
            softApInfo.mApInstanceIdentifier = parcel.readString();
            softApInfo.mIdleShutdownTimeoutMillis = parcel.readLong();
            return softApInfo;
        }

        public SoftApInfo[] newArray(int i) {
            return new SoftApInfo[i];
        }
    };
    /* access modifiers changed from: private */
    public String mApInstanceIdentifier;
    /* access modifiers changed from: private */
    public int mBandwidth = 0;
    /* access modifiers changed from: private */
    public MacAddress mBssid;
    /* access modifiers changed from: private */
    public int mFrequency = 0;
    /* access modifiers changed from: private */
    public long mIdleShutdownTimeoutMillis;
    /* access modifiers changed from: private */
    public int mWifiStandard = 0;

    public int describeContents() {
        return 0;
    }

    public int getFrequency() {
        return this.mFrequency;
    }

    public void setFrequency(int i) {
        this.mFrequency = i;
    }

    public int getBandwidth() {
        return this.mBandwidth;
    }

    public void setBandwidth(int i) {
        this.mBandwidth = i;
    }

    public MacAddress getBssid() {
        if (SdkLevel.isAtLeastS()) {
            return getBssidInternal();
        }
        throw new UnsupportedOperationException();
    }

    public MacAddress getBssidInternal() {
        return this.mBssid;
    }

    public void setBssid(MacAddress macAddress) {
        if (macAddress != null) {
            Preconditions.checkArgument(!macAddress.equals(WifiManager.ALL_ZEROS_MAC_ADDRESS));
            Preconditions.checkArgument(!macAddress.equals(MacAddress.BROADCAST_ADDRESS));
        }
        this.mBssid = macAddress;
    }

    public void setWifiStandard(int i) {
        this.mWifiStandard = i;
    }

    public int getWifiStandard() {
        if (SdkLevel.isAtLeastS()) {
            return getWifiStandardInternal();
        }
        throw new UnsupportedOperationException();
    }

    public int getWifiStandardInternal() {
        return this.mWifiStandard;
    }

    public void setApInstanceIdentifier(String str) {
        this.mApInstanceIdentifier = str;
    }

    public String getApInstanceIdentifier() {
        return this.mApInstanceIdentifier;
    }

    public void setAutoShutdownTimeoutMillis(long j) {
        this.mIdleShutdownTimeoutMillis = j;
    }

    public long getAutoShutdownTimeoutMillis() {
        return this.mIdleShutdownTimeoutMillis;
    }

    public SoftApInfo(SoftApInfo softApInfo) {
        if (softApInfo != null) {
            this.mFrequency = softApInfo.mFrequency;
            this.mBandwidth = softApInfo.mBandwidth;
            this.mBssid = softApInfo.mBssid;
            this.mWifiStandard = softApInfo.mWifiStandard;
            this.mApInstanceIdentifier = softApInfo.mApInstanceIdentifier;
            this.mIdleShutdownTimeoutMillis = softApInfo.mIdleShutdownTimeoutMillis;
        }
    }

    public SoftApInfo() {
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mFrequency);
        parcel.writeInt(this.mBandwidth);
        parcel.writeParcelable(this.mBssid, i);
        parcel.writeInt(this.mWifiStandard);
        parcel.writeString(this.mApInstanceIdentifier);
        parcel.writeLong(this.mIdleShutdownTimeoutMillis);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("SoftApInfo{bandwidth= ");
        sb.append(this.mBandwidth);
        sb.append(", frequency= ");
        sb.append(this.mFrequency);
        if (this.mBssid != null) {
            sb.append(",bssid=");
            sb.append(this.mBssid.toString());
        }
        sb.append(", wifiStandard= ");
        sb.append(this.mWifiStandard);
        sb.append(", mApInstanceIdentifier= ");
        sb.append(this.mApInstanceIdentifier);
        sb.append(", mIdleShutdownTimeoutMillis= ");
        sb.append(this.mIdleShutdownTimeoutMillis);
        sb.append("}");
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SoftApInfo)) {
            return false;
        }
        SoftApInfo softApInfo = (SoftApInfo) obj;
        if (this.mFrequency == softApInfo.mFrequency && this.mBandwidth == softApInfo.mBandwidth && Objects.equals(this.mBssid, softApInfo.mBssid) && this.mWifiStandard == softApInfo.mWifiStandard && Objects.equals(this.mApInstanceIdentifier, softApInfo.mApInstanceIdentifier) && this.mIdleShutdownTimeoutMillis == softApInfo.mIdleShutdownTimeoutMillis) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mFrequency), Integer.valueOf(this.mBandwidth), this.mBssid, Integer.valueOf(this.mWifiStandard), this.mApInstanceIdentifier, Long.valueOf(this.mIdleShutdownTimeoutMillis));
    }
}

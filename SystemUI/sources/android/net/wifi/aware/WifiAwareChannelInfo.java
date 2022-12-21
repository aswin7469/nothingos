package android.net.wifi.aware;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

public final class WifiAwareChannelInfo implements Parcelable {
    public static final Parcelable.Creator<WifiAwareChannelInfo> CREATOR = new Parcelable.Creator<WifiAwareChannelInfo>() {
        public WifiAwareChannelInfo createFromParcel(Parcel parcel) {
            return new WifiAwareChannelInfo(parcel);
        }

        public WifiAwareChannelInfo[] newArray(int i) {
            return new WifiAwareChannelInfo[i];
        }
    };
    private final int mChannelBandwidth;
    private final int mChannelFreq;
    private final int mNumSpatialStreams;

    public int describeContents() {
        return 0;
    }

    public WifiAwareChannelInfo(int i, int i2, int i3) {
        this.mChannelFreq = i;
        this.mChannelBandwidth = i2;
        this.mNumSpatialStreams = i3;
    }

    public int getChannelFrequencyMhz() {
        return this.mChannelFreq;
    }

    public int getChannelBandwidth() {
        return this.mChannelBandwidth;
    }

    public int getSpatialStreamCount() {
        return this.mNumSpatialStreams;
    }

    private WifiAwareChannelInfo(Parcel parcel) {
        this.mChannelFreq = parcel.readInt();
        this.mChannelBandwidth = parcel.readInt();
        this.mNumSpatialStreams = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mChannelFreq);
        parcel.writeInt(this.mChannelBandwidth);
        parcel.writeInt(this.mNumSpatialStreams);
    }

    public String toString() {
        return "{.channelFreq = " + this.mChannelFreq + ", .channelBandwidth = " + this.mChannelBandwidth + ", .numSpatialStreams = " + this.mNumSpatialStreams + "}";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WifiAwareChannelInfo)) {
            return false;
        }
        WifiAwareChannelInfo wifiAwareChannelInfo = (WifiAwareChannelInfo) obj;
        if (this.mChannelFreq == wifiAwareChannelInfo.mChannelFreq && this.mChannelBandwidth == wifiAwareChannelInfo.mChannelBandwidth && this.mNumSpatialStreams == wifiAwareChannelInfo.mNumSpatialStreams) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mChannelFreq), Integer.valueOf(this.mChannelBandwidth), Integer.valueOf(this.mNumSpatialStreams));
    }
}

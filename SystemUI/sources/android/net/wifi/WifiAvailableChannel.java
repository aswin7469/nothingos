package android.net.wifi;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@SystemApi
public final class WifiAvailableChannel implements Parcelable {
    public static final Parcelable.Creator<WifiAvailableChannel> CREATOR = new Parcelable.Creator<WifiAvailableChannel>() {
        public WifiAvailableChannel createFromParcel(Parcel parcel) {
            return new WifiAvailableChannel(parcel);
        }

        public WifiAvailableChannel[] newArray(int i) {
            return new WifiAvailableChannel[i];
        }
    };
    public static final int FILTER_CELLULAR_COEXISTENCE = 1;
    public static final int FILTER_CONCURRENCY = 2;
    public static final int FILTER_NAN_INSTANT_MODE = 4;
    public static final int FILTER_REGULATORY = 0;
    public static final int OP_MODE_SAP = 2;
    public static final int OP_MODE_STA = 1;
    public static final int OP_MODE_TDLS = 32;
    public static final int OP_MODE_WIFI_AWARE = 16;
    public static final int OP_MODE_WIFI_DIRECT_CLI = 4;
    public static final int OP_MODE_WIFI_DIRECT_GO = 8;
    private int mFrequency;
    private int mOpModes;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Filter {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OpMode {
    }

    public static int getUsableFilter() {
        return 3;
    }

    public int describeContents() {
        return 0;
    }

    public WifiAvailableChannel(int i, int i2) {
        this.mFrequency = i;
        this.mOpModes = i2;
    }

    private WifiAvailableChannel(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mFrequency = parcel.readInt();
        this.mOpModes = parcel.readInt();
    }

    public int getFrequencyMhz() {
        return this.mFrequency;
    }

    public int getOperationalModes() {
        return this.mOpModes;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        WifiAvailableChannel wifiAvailableChannel = (WifiAvailableChannel) obj;
        if (this.mFrequency == wifiAvailableChannel.mFrequency && this.mOpModes == wifiAvailableChannel.mOpModes) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mFrequency), Integer.valueOf(this.mOpModes));
    }

    public String toString() {
        return "mFrequency = " + this.mFrequency + ", mOpModes = " + String.format("%x", Integer.valueOf(this.mOpModes));
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mFrequency);
        parcel.writeInt(this.mOpModes);
    }
}

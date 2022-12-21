package android.net.wifi;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;

@SystemApi
@Deprecated
public class WifiNetworkConnectionStatistics implements Parcelable {
    public static final Parcelable.Creator<WifiNetworkConnectionStatistics> CREATOR = new Parcelable.Creator<WifiNetworkConnectionStatistics>() {
        public WifiNetworkConnectionStatistics createFromParcel(Parcel parcel) {
            return new WifiNetworkConnectionStatistics(parcel.readInt(), parcel.readInt());
        }

        public WifiNetworkConnectionStatistics[] newArray(int i) {
            return new WifiNetworkConnectionStatistics[i];
        }
    };
    private static final String TAG = "WifiNetworkConnnectionStatistics";
    public int numConnection;
    public int numUsage;

    public int describeContents() {
        return 0;
    }

    public WifiNetworkConnectionStatistics(int i, int i2) {
        this.numConnection = i;
        this.numUsage = i2;
    }

    public WifiNetworkConnectionStatistics() {
    }

    public String toString() {
        return "c=" + this.numConnection + " u=" + this.numUsage;
    }

    public WifiNetworkConnectionStatistics(WifiNetworkConnectionStatistics wifiNetworkConnectionStatistics) {
        this.numConnection = wifiNetworkConnectionStatistics.numConnection;
        this.numUsage = wifiNetworkConnectionStatistics.numUsage;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.numConnection);
        parcel.writeInt(this.numUsage);
    }
}

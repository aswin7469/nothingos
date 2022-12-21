package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;

public class WpsInfo implements Parcelable {
    public static final Parcelable.Creator<WpsInfo> CREATOR = new Parcelable.Creator<WpsInfo>() {
        public WpsInfo createFromParcel(Parcel parcel) {
            WpsInfo wpsInfo = new WpsInfo();
            wpsInfo.setup = parcel.readInt();
            wpsInfo.BSSID = parcel.readString();
            wpsInfo.pin = parcel.readString();
            return wpsInfo;
        }

        public WpsInfo[] newArray(int i) {
            return new WpsInfo[i];
        }
    };
    public static final int DISPLAY = 1;
    public static final int INVALID = 4;
    public static final int KEYPAD = 2;
    public static final int LABEL = 3;
    public static final int PBC = 0;
    public String BSSID;
    public String pin;
    public int setup;

    public int describeContents() {
        return 0;
    }

    public WpsInfo() {
        this.setup = 4;
        this.BSSID = null;
        this.pin = null;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(" setup: ");
        stringBuffer.append(this.setup);
        stringBuffer.append("\n BSSID: ");
        stringBuffer.append(this.BSSID);
        stringBuffer.append("\n pin: ");
        stringBuffer.append(this.pin);
        stringBuffer.append(10);
        return stringBuffer.toString();
    }

    public WpsInfo(WpsInfo wpsInfo) {
        if (wpsInfo != null) {
            this.setup = wpsInfo.setup;
            this.BSSID = wpsInfo.BSSID;
            this.pin = wpsInfo.pin;
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.setup);
        parcel.writeString(this.BSSID);
        parcel.writeString(this.pin);
    }
}

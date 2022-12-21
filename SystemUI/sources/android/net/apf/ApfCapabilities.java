package android.net.apf;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityResources;
import android.os.Parcel;
import android.os.Parcelable;

@SystemApi
public final class ApfCapabilities implements Parcelable {
    public static final Parcelable.Creator<ApfCapabilities> CREATOR = new Parcelable.Creator<ApfCapabilities>() {
        public ApfCapabilities createFromParcel(Parcel parcel) {
            return new ApfCapabilities(parcel);
        }

        public ApfCapabilities[] newArray(int i) {
            return new ApfCapabilities[i];
        }
    };
    private static ConnectivityResources sResources;
    public final int apfPacketFormat;
    public final int apfVersionSupported;
    public final int maximumApfProgramSize;

    public int describeContents() {
        return 0;
    }

    public ApfCapabilities(int i, int i2, int i3) {
        this.apfVersionSupported = i;
        this.maximumApfProgramSize = i2;
        this.apfPacketFormat = i3;
    }

    private ApfCapabilities(Parcel parcel) {
        this.apfVersionSupported = parcel.readInt();
        this.maximumApfProgramSize = parcel.readInt();
        this.apfPacketFormat = parcel.readInt();
    }

    private static synchronized ConnectivityResources getResources(Context context) {
        ConnectivityResources connectivityResources;
        synchronized (ApfCapabilities.class) {
            if (sResources == null) {
                sResources = new ConnectivityResources(context);
            }
            connectivityResources = sResources;
        }
        return connectivityResources;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.apfVersionSupported);
        parcel.writeInt(this.maximumApfProgramSize);
        parcel.writeInt(this.apfPacketFormat);
    }

    public String toString() {
        return String.format("%s{version: %d, maxSize: %d, format: %d}", getClass().getSimpleName(), Integer.valueOf(this.apfVersionSupported), Integer.valueOf(this.maximumApfProgramSize), Integer.valueOf(this.apfPacketFormat));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ApfCapabilities)) {
            return false;
        }
        ApfCapabilities apfCapabilities = (ApfCapabilities) obj;
        if (this.apfVersionSupported == apfCapabilities.apfVersionSupported && this.maximumApfProgramSize == apfCapabilities.maximumApfProgramSize && this.apfPacketFormat == apfCapabilities.apfPacketFormat) {
            return true;
        }
        return false;
    }

    public boolean hasDataAccess() {
        return this.apfVersionSupported >= 4;
    }

    public static boolean getApfDrop8023Frames() {
        Resources system = Resources.getSystem();
        return system.getBoolean(system.getIdentifier("config_apfDrop802_3Frames", "bool", "android"));
    }

    public static int[] getApfEtherTypeBlackList() {
        Resources system = Resources.getSystem();
        return system.getIntArray(system.getIdentifier("config_apfEthTypeBlackList", "array", "android"));
    }
}

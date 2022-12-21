package android.net.wifi.aware;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.navigationbar.NavigationBarInflaterView;

@SystemApi
public final class AwareParams implements Parcelable {
    public static final Parcelable.Creator<AwareParams> CREATOR = new Parcelable.Creator<AwareParams>() {
        public AwareParams createFromParcel(Parcel parcel) {
            return new AwareParams(parcel);
        }

        public AwareParams[] newArray(int i) {
            return new AwareParams[i];
        }
    };
    public static final int UNSET_PARAMETER = -1;
    private int mDiscoveryBeaconIntervalMs;
    private int mDw24Ghz;
    private int mDw5Ghz;
    private int mDw6Ghz;
    private boolean mIsDwEarlyTerminationEnabled;
    private int mMacRandomIntervalSec;
    private int mNumSpatialStreamsInDiscovery;

    public int describeContents() {
        return 0;
    }

    public AwareParams() {
        this.mDw24Ghz = -1;
        this.mDw5Ghz = -1;
        this.mDw6Ghz = -1;
        this.mDiscoveryBeaconIntervalMs = -1;
        this.mNumSpatialStreamsInDiscovery = -1;
        this.mIsDwEarlyTerminationEnabled = false;
        this.mMacRandomIntervalSec = -1;
    }

    private AwareParams(Parcel parcel) {
        this.mDw24Ghz = -1;
        this.mDw5Ghz = -1;
        this.mDw6Ghz = -1;
        this.mDiscoveryBeaconIntervalMs = -1;
        this.mNumSpatialStreamsInDiscovery = -1;
        this.mIsDwEarlyTerminationEnabled = false;
        this.mMacRandomIntervalSec = -1;
        this.mDw24Ghz = parcel.readInt();
        this.mDw5Ghz = parcel.readInt();
        this.mDw6Ghz = parcel.readInt();
        this.mDiscoveryBeaconIntervalMs = parcel.readInt();
        this.mNumSpatialStreamsInDiscovery = parcel.readInt();
        this.mIsDwEarlyTerminationEnabled = parcel.readBoolean();
        this.mMacRandomIntervalSec = parcel.readInt();
    }

    public void setDiscoveryWindowWakeInterval24Ghz(int i) {
        if (i > 5 || i < 1) {
            throw new IllegalArgumentException("DW value for 2.4Ghz must be 1 to 5");
        }
        this.mDw24Ghz = i;
    }

    public void setDiscoveryWindowWakeInterval5Ghz(int i) {
        if (i > 5 || i < 0) {
            throw new IllegalArgumentException("DW value for 5Ghz must be 0 to 5");
        }
        this.mDw5Ghz = i;
    }

    public void setDiscoveryWindow6Ghz(int i) {
        this.mDw6Ghz = i;
    }

    public void setDiscoveryBeaconIntervalMillis(int i) {
        if (i >= 1) {
            this.mDiscoveryBeaconIntervalMs = i;
            return;
        }
        throw new IllegalArgumentException("Discovery Beacon interval must >= 1");
    }

    public void setNumSpatialStreamsInDiscovery(int i) {
        if (i >= 1) {
            this.mNumSpatialStreamsInDiscovery = i;
            return;
        }
        throw new IllegalArgumentException("Number Spatial streams must >= 1");
    }

    public void setMacRandomizationIntervalSeconds(int i) {
        if (i > 1800 || i < 1) {
            throw new IllegalArgumentException("Mac Randomization Interval must be between 1 to 1800 seconds");
        }
        this.mMacRandomIntervalSec = i;
    }

    public void setDwEarlyTerminationEnabled(boolean z) {
        this.mIsDwEarlyTerminationEnabled = z;
    }

    public int getDiscoveryWindowWakeInterval24Ghz() {
        return this.mDw24Ghz;
    }

    public int getDiscoveryWindowWakeInterval5Ghz() {
        return this.mDw5Ghz;
    }

    public int getDiscoveryWindowWakeInterval6Ghz() {
        return this.mDw24Ghz;
    }

    public int getDiscoveryBeaconIntervalMillis() {
        return this.mDiscoveryBeaconIntervalMs;
    }

    public int getNumSpatialStreamsInDiscovery() {
        return this.mNumSpatialStreamsInDiscovery;
    }

    public boolean isDwEarlyTerminationEnabled() {
        return this.mIsDwEarlyTerminationEnabled;
    }

    public int getMacRandomizationIntervalSeconds() {
        return this.mMacRandomIntervalSec;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mDw24Ghz);
        parcel.writeInt(this.mDw5Ghz);
        parcel.writeInt(this.mDw6Ghz);
        parcel.writeInt(this.mDiscoveryBeaconIntervalMs);
        parcel.writeInt(this.mNumSpatialStreamsInDiscovery);
        parcel.writeBoolean(this.mIsDwEarlyTerminationEnabled);
        parcel.writeInt(this.mMacRandomIntervalSec);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("AwareParams [mDw24Ghz=");
        stringBuffer.append(this.mDw24Ghz).append(", mDw5Ghz=").append(this.mDw5Ghz).append(", mDw6Ghz=").append(this.mDw6Ghz).append(", mDiscoveryBeaconIntervalMs=").append(this.mDiscoveryBeaconIntervalMs).append(", mNumSpatialStreamsInDiscovery=").append(this.mNumSpatialStreamsInDiscovery).append(", mIsDwEarlyTerminationEnabled=").append(this.mIsDwEarlyTerminationEnabled).append(", mMacRandomIntervalSec=").append(this.mMacRandomIntervalSec).append(NavigationBarInflaterView.SIZE_MOD_END);
        return stringBuffer.toString();
    }
}

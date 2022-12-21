package android.net.wifi.aware;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.Arrays;

public final class ConfigRequest implements Parcelable {
    public static final int CLUSTER_ID_MAX = 65535;
    public static final int CLUSTER_ID_MIN = 0;
    public static final Parcelable.Creator<ConfigRequest> CREATOR = new Parcelable.Creator<ConfigRequest>() {
        public ConfigRequest[] newArray(int i) {
            return new ConfigRequest[i];
        }

        public ConfigRequest createFromParcel(Parcel parcel) {
            return new ConfigRequest(parcel.readInt() != 0, parcel.readInt() != 0, parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.createIntArray());
        }
    };
    public static final int DW_DISABLE = 0;
    public static final int DW_INTERVAL_NOT_INIT = -1;
    public static final int NAN_BAND_24GHZ = 0;
    public static final int NAN_BAND_5GHZ = 1;
    public static final int NAN_BAND_6GHZ = 2;
    public final int mClusterHigh;
    public final int mClusterLow;
    public final int[] mDiscoveryWindowInterval;
    public final int mMasterPreference;
    public final boolean mSupport5gBand;
    public final boolean mSupport6gBand;

    public int describeContents() {
        return 0;
    }

    private ConfigRequest(boolean z, boolean z2, int i, int i2, int i3, int[] iArr) {
        this.mSupport5gBand = z;
        this.mSupport6gBand = z2;
        this.mMasterPreference = i;
        this.mClusterLow = i2;
        this.mClusterHigh = i3;
        this.mDiscoveryWindowInterval = iArr;
    }

    public String toString() {
        return "ConfigRequest [mSupport5gBand=" + this.mSupport5gBand + ", mSupport6gBand=" + this.mSupport6gBand + ", mMasterPreference=" + this.mMasterPreference + ", mClusterLow=" + this.mClusterLow + ", mClusterHigh=" + this.mClusterHigh + ", mDiscoveryWindowInterval=" + Arrays.toString(this.mDiscoveryWindowInterval) + NavigationBarInflaterView.SIZE_MOD_END;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mSupport5gBand ? 1 : 0);
        parcel.writeInt(this.mSupport6gBand ? 1 : 0);
        parcel.writeInt(this.mMasterPreference);
        parcel.writeInt(this.mClusterLow);
        parcel.writeInt(this.mClusterHigh);
        parcel.writeIntArray(this.mDiscoveryWindowInterval);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConfigRequest)) {
            return false;
        }
        ConfigRequest configRequest = (ConfigRequest) obj;
        if (this.mSupport5gBand == configRequest.mSupport5gBand && this.mSupport6gBand == configRequest.mSupport6gBand && this.mMasterPreference == configRequest.mMasterPreference && this.mClusterLow == configRequest.mClusterLow && this.mClusterHigh == configRequest.mClusterHigh && Arrays.equals(this.mDiscoveryWindowInterval, configRequest.mDiscoveryWindowInterval)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((((((((((true + (this.mSupport5gBand ? 1 : 0)) * 31) + (this.mSupport6gBand ? 1 : 0)) * 31) + this.mMasterPreference) * 31) + this.mClusterLow) * 31) + this.mClusterHigh) * 31) + Arrays.hashCode(this.mDiscoveryWindowInterval);
    }

    public void validate() throws IllegalArgumentException {
        int i = this.mMasterPreference;
        if (i < 0) {
            throw new IllegalArgumentException("Master Preference specification must be non-negative");
        } else if (i == 1 || i == 255 || i > 255) {
            throw new IllegalArgumentException("Master Preference specification must not exceed 255 or use 1 or 255 (reserved values)");
        } else {
            int i2 = this.mClusterLow;
            if (i2 < 0) {
                throw new IllegalArgumentException("Cluster specification must be non-negative");
            } else if (i2 <= 65535) {
                int i3 = this.mClusterHigh;
                if (i3 < 0) {
                    throw new IllegalArgumentException("Cluster specification must be non-negative");
                } else if (i3 > 65535) {
                    throw new IllegalArgumentException("Cluster specification must not exceed 0xFFFF");
                } else if (i2 <= i3) {
                    int[] iArr = this.mDiscoveryWindowInterval;
                    if (iArr.length == 3) {
                        int i4 = iArr[0];
                        if (i4 == -1 || (i4 >= 1 && i4 <= 5)) {
                            int i5 = iArr[1];
                            if (i5 == -1 || (i5 >= 0 && i5 <= 5)) {
                                int i6 = iArr[2];
                                if (i6 == -1) {
                                    return;
                                }
                                if (i6 < 0 || i6 > 5) {
                                    throw new IllegalArgumentException("Invalid discovery window interval for 6GHz: valid is UNSET or [0,5]");
                                }
                                return;
                            }
                            throw new IllegalArgumentException("Invalid discovery window interval for 5GHz: valid is UNSET or [0,5]");
                        }
                        throw new IllegalArgumentException("Invalid discovery window interval for 2.4GHz: valid is UNSET or [1,5]");
                    }
                    throw new IllegalArgumentException("Invalid discovery window interval: must have 3 elements (2.4 & 5 & 6");
                } else {
                    throw new IllegalArgumentException("Invalid argument combination - must have Cluster Low <= Cluster High");
                }
            } else {
                throw new IllegalArgumentException("Cluster specification must not exceed 0xFFFF");
            }
        }
    }

    public static final class Builder {
        private int mClusterHigh = 65535;
        private int mClusterLow = 0;
        private int[] mDiscoveryWindowInterval = {-1, -1, -1};
        private int mMasterPreference = 0;
        private boolean mSupport5gBand = true;
        private boolean mSupport6gBand = false;

        public Builder setSupport5gBand(boolean z) {
            this.mSupport5gBand = z;
            return this;
        }

        public Builder setSupport6gBand(boolean z) {
            this.mSupport6gBand = z;
            return this;
        }

        public Builder setMasterPreference(int i) {
            if (i < 0) {
                throw new IllegalArgumentException("Master Preference specification must be non-negative");
            } else if (i == 1 || i == 255 || i > 255) {
                throw new IllegalArgumentException("Master Preference specification must not exceed 255 or use 1 or 255 (reserved values)");
            } else {
                this.mMasterPreference = i;
                return this;
            }
        }

        public Builder setClusterLow(int i) {
            if (i < 0) {
                throw new IllegalArgumentException("Cluster specification must be non-negative");
            } else if (i <= 65535) {
                this.mClusterLow = i;
                return this;
            } else {
                throw new IllegalArgumentException("Cluster specification must not exceed 0xFFFF");
            }
        }

        public Builder setClusterHigh(int i) {
            if (i < 0) {
                throw new IllegalArgumentException("Cluster specification must be non-negative");
            } else if (i <= 65535) {
                this.mClusterHigh = i;
                return this;
            } else {
                throw new IllegalArgumentException("Cluster specification must not exceed 0xFFFF");
            }
        }

        public Builder setDiscoveryWindowInterval(int i, int i2) {
            if (i != 0 && i != 1 && i != 2) {
                throw new IllegalArgumentException("Invalid band value");
            } else if ((i != 0 || (i2 >= 1 && i2 <= 5)) && ((i != 1 || (i2 >= 0 && i2 <= 5)) && (i != 2 || (i2 >= 0 && i2 <= 5)))) {
                this.mDiscoveryWindowInterval[i] = i2;
                return this;
            } else {
                throw new IllegalArgumentException("Invalid interval value: 2.4 GHz [1,5] or 5GHz/6GHz [0,5]");
            }
        }

        public ConfigRequest build() {
            if (this.mClusterLow <= this.mClusterHigh) {
                return new ConfigRequest(this.mSupport5gBand, this.mSupport6gBand, this.mMasterPreference, this.mClusterLow, this.mClusterHigh, this.mDiscoveryWindowInterval);
            }
            throw new IllegalArgumentException("Invalid argument combination - must have Cluster Low <= Cluster High");
        }
    }
}

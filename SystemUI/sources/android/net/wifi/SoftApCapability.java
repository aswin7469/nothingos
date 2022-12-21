package android.net.wifi;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Objects;

@SystemApi
public final class SoftApCapability implements Parcelable {
    public static final Parcelable.Creator<SoftApCapability> CREATOR = new Parcelable.Creator<SoftApCapability>() {
        public SoftApCapability createFromParcel(Parcel parcel) {
            SoftApCapability softApCapability = new SoftApCapability(parcel.readLong());
            softApCapability.mMaximumSupportedClientNumber = parcel.readInt();
            softApCapability.setSupportedChannelList(1, parcel.createIntArray());
            softApCapability.setSupportedChannelList(2, parcel.createIntArray());
            softApCapability.setSupportedChannelList(4, parcel.createIntArray());
            softApCapability.setSupportedChannelList(8, parcel.createIntArray());
            softApCapability.setCountryCode(parcel.readString());
            return softApCapability;
        }

        public SoftApCapability[] newArray(int i) {
            return new SoftApCapability[i];
        }
    };
    private static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final long SOFTAP_FEATURE_ACS_OFFLOAD = 1;
    public static final long SOFTAP_FEATURE_BAND_24G_SUPPORTED = 32;
    public static final long SOFTAP_FEATURE_BAND_5G_SUPPORTED = 64;
    public static final long SOFTAP_FEATURE_BAND_60G_SUPPORTED = 256;
    public static final long SOFTAP_FEATURE_BAND_6G_SUPPORTED = 128;
    public static final long SOFTAP_FEATURE_CLIENT_FORCE_DISCONNECT = 2;
    public static final long SOFTAP_FEATURE_IEEE80211_AX = 16;
    public static final long SOFTAP_FEATURE_IEEE80211_BE = 512;
    public static final long SOFTAP_FEATURE_MAC_ADDRESS_CUSTOMIZATION = 8;
    public static final long SOFTAP_FEATURE_WPA3_OWE = 2048;
    public static final long SOFTAP_FEATURE_WPA3_OWE_TRANSITION = 1024;
    public static final long SOFTAP_FEATURE_WPA3_SAE = 4;
    private static final String TAG = "SoftApCapability";
    private String mCountryCodeFromDriver;
    /* access modifiers changed from: private */
    public int mMaximumSupportedClientNumber;
    private int[] mSupportedChannelListIn24g;
    private int[] mSupportedChannelListIn5g;
    private int[] mSupportedChannelListIn60g;
    private int[] mSupportedChannelListIn6g;
    private long mSupportedFeatures;

    @Retention(RetentionPolicy.SOURCE)
    public @interface HotspotFeatures {
    }

    public int describeContents() {
        return 0;
    }

    public void setCountryCode(String str) {
        this.mCountryCodeFromDriver = str;
    }

    public String getCountryCode() {
        return this.mCountryCodeFromDriver;
    }

    public int getMaxSupportedClients() {
        return this.mMaximumSupportedClientNumber;
    }

    public void setMaxSupportedClients(int i) {
        this.mMaximumSupportedClientNumber = i;
    }

    public boolean areFeaturesSupported(long j) {
        return (this.mSupportedFeatures & j) == j;
    }

    public boolean setSupportedChannelList(int i, int[] iArr) {
        if (iArr == null) {
            return false;
        }
        if (i == 1) {
            this.mSupportedChannelListIn24g = iArr;
        } else if (i == 2) {
            this.mSupportedChannelListIn5g = iArr;
        } else if (i == 4) {
            this.mSupportedChannelListIn6g = iArr;
        } else if (i == 8) {
            this.mSupportedChannelListIn60g = iArr;
        } else {
            throw new IllegalArgumentException("Invalid band: " + i);
        }
        return true;
    }

    public int[] getSupportedChannelList(int i) {
        if (i == 1) {
            return this.mSupportedChannelListIn24g;
        }
        if (i == 2) {
            return this.mSupportedChannelListIn5g;
        }
        if (i == 4) {
            return this.mSupportedChannelListIn6g;
        }
        if (i == 8) {
            return this.mSupportedChannelListIn60g;
        }
        throw new IllegalArgumentException("Invalid band: " + i);
    }

    public SoftApCapability(SoftApCapability softApCapability) {
        this.mSupportedFeatures = 0;
        int[] iArr = EMPTY_INT_ARRAY;
        this.mSupportedChannelListIn24g = iArr;
        this.mSupportedChannelListIn5g = iArr;
        this.mSupportedChannelListIn6g = iArr;
        this.mSupportedChannelListIn60g = iArr;
        if (softApCapability != null) {
            this.mSupportedFeatures = softApCapability.mSupportedFeatures;
            this.mMaximumSupportedClientNumber = softApCapability.mMaximumSupportedClientNumber;
            this.mSupportedChannelListIn24g = softApCapability.mSupportedChannelListIn24g;
            this.mSupportedChannelListIn5g = softApCapability.mSupportedChannelListIn5g;
            this.mSupportedChannelListIn6g = softApCapability.mSupportedChannelListIn6g;
            this.mSupportedChannelListIn60g = softApCapability.mSupportedChannelListIn60g;
            this.mCountryCodeFromDriver = softApCapability.mCountryCodeFromDriver;
        }
    }

    public SoftApCapability(long j) {
        int[] iArr = EMPTY_INT_ARRAY;
        this.mSupportedChannelListIn24g = iArr;
        this.mSupportedChannelListIn5g = iArr;
        this.mSupportedChannelListIn6g = iArr;
        this.mSupportedChannelListIn60g = iArr;
        this.mSupportedFeatures = j;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mSupportedFeatures);
        parcel.writeInt(this.mMaximumSupportedClientNumber);
        parcel.writeIntArray(this.mSupportedChannelListIn24g);
        parcel.writeIntArray(this.mSupportedChannelListIn5g);
        parcel.writeIntArray(this.mSupportedChannelListIn6g);
        parcel.writeIntArray(this.mSupportedChannelListIn60g);
        parcel.writeString(this.mCountryCodeFromDriver);
    }

    public String toString() {
        return "SupportedFeatures=" + this.mSupportedFeatures + " MaximumSupportedClientNumber=" + this.mMaximumSupportedClientNumber + " SupportedChannelListIn24g" + Arrays.toString(this.mSupportedChannelListIn24g) + " SupportedChannelListIn5g" + Arrays.toString(this.mSupportedChannelListIn5g) + " SupportedChannelListIn6g" + Arrays.toString(this.mSupportedChannelListIn6g) + " SupportedChannelListIn60g" + Arrays.toString(this.mSupportedChannelListIn60g) + " mCountryCodeFromDriver" + this.mCountryCodeFromDriver;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SoftApCapability)) {
            return false;
        }
        SoftApCapability softApCapability = (SoftApCapability) obj;
        if (this.mSupportedFeatures != softApCapability.mSupportedFeatures || this.mMaximumSupportedClientNumber != softApCapability.mMaximumSupportedClientNumber || !Arrays.equals(this.mSupportedChannelListIn24g, softApCapability.mSupportedChannelListIn24g) || !Arrays.equals(this.mSupportedChannelListIn5g, softApCapability.mSupportedChannelListIn5g) || !Arrays.equals(this.mSupportedChannelListIn6g, softApCapability.mSupportedChannelListIn6g) || !Arrays.equals(this.mSupportedChannelListIn60g, softApCapability.mSupportedChannelListIn60g) || !Objects.equals(this.mCountryCodeFromDriver, softApCapability.mCountryCodeFromDriver)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.mSupportedFeatures), Integer.valueOf(this.mMaximumSupportedClientNumber), Integer.valueOf(Arrays.hashCode(this.mSupportedChannelListIn24g)), Integer.valueOf(Arrays.hashCode(this.mSupportedChannelListIn5g)), Integer.valueOf(Arrays.hashCode(this.mSupportedChannelListIn6g)), Integer.valueOf(Arrays.hashCode(this.mSupportedChannelListIn60g)), this.mCountryCodeFromDriver);
    }
}

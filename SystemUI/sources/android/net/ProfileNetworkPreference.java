package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
import java.util.Objects;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class ProfileNetworkPreference implements Parcelable {
    public static final Parcelable.Creator<ProfileNetworkPreference> CREATOR = new Parcelable.Creator<ProfileNetworkPreference>() {
        public ProfileNetworkPreference[] newArray(int i) {
            return new ProfileNetworkPreference[i];
        }

        public ProfileNetworkPreference createFromParcel(Parcel parcel) {
            return new ProfileNetworkPreference(parcel);
        }
    };
    private int[] mExcludedUids;
    private int[] mIncludedUids;
    private final int mPreference;
    private final int mPreferenceEnterpriseId;

    public int describeContents() {
        return 0;
    }

    private ProfileNetworkPreference(int i, int[] iArr, int[] iArr2, int i2) {
        this.mIncludedUids = new int[0];
        this.mExcludedUids = new int[0];
        this.mPreference = i;
        this.mPreferenceEnterpriseId = i2;
        if (iArr != null) {
            this.mIncludedUids = (int[]) iArr.clone();
        } else {
            this.mIncludedUids = new int[0];
        }
        if (iArr2 != null) {
            this.mExcludedUids = (int[]) iArr2.clone();
        } else {
            this.mExcludedUids = new int[0];
        }
    }

    private ProfileNetworkPreference(Parcel parcel) {
        this.mIncludedUids = new int[0];
        this.mExcludedUids = new int[0];
        this.mPreference = parcel.readInt();
        parcel.readIntArray(this.mIncludedUids);
        parcel.readIntArray(this.mExcludedUids);
        this.mPreferenceEnterpriseId = parcel.readInt();
    }

    public int getPreference() {
        return this.mPreference;
    }

    public int[] getIncludedUids() {
        return (int[]) this.mIncludedUids.clone();
    }

    public int[] getExcludedUids() {
        return (int[]) this.mExcludedUids.clone();
    }

    public int getPreferenceEnterpriseId() {
        return this.mPreferenceEnterpriseId;
    }

    public String toString() {
        return "ProfileNetworkPreference{mPreference=" + getPreference() + "mIncludedUids=" + Arrays.toString(this.mIncludedUids) + "mExcludedUids=" + Arrays.toString(this.mExcludedUids) + "mPreferenceEnterpriseId=" + this.mPreferenceEnterpriseId + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ProfileNetworkPreference profileNetworkPreference = (ProfileNetworkPreference) obj;
        if (this.mPreference != profileNetworkPreference.mPreference || !Arrays.equals(this.mIncludedUids, profileNetworkPreference.mIncludedUids) || !Arrays.equals(this.mExcludedUids, profileNetworkPreference.mExcludedUids) || this.mPreferenceEnterpriseId != profileNetworkPreference.mPreferenceEnterpriseId) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.mPreference + (this.mPreferenceEnterpriseId * 2) + (Arrays.hashCode(this.mIncludedUids) * 11) + (Arrays.hashCode(this.mExcludedUids) * 13);
    }

    public static final class Builder {
        private int[] mExcludedUids = new int[0];
        private int[] mIncludedUids = new int[0];
        private int mPreference = 0;
        private int mPreferenceEnterpriseId;

        private boolean isEnterpriseIdentifierValid(int i) {
            return i >= 1 && i <= 5;
        }

        public Builder setPreference(int i) {
            this.mPreference = i;
            return this;
        }

        public Builder setIncludedUids(int[] iArr) {
            Objects.requireNonNull(iArr);
            this.mIncludedUids = (int[]) iArr.clone();
            return this;
        }

        public Builder setExcludedUids(int[] iArr) {
            Objects.requireNonNull(iArr);
            this.mExcludedUids = (int[]) iArr.clone();
            return this;
        }

        public ProfileNetworkPreference build() {
            if (this.mIncludedUids.length > 0 && this.mExcludedUids.length > 0) {
                throw new IllegalArgumentException("Both includedUids and excludedUids cannot be nonempty");
            } else if ((this.mPreference == 0 || isEnterpriseIdentifierValid(this.mPreferenceEnterpriseId)) && (this.mPreference != 0 || this.mPreferenceEnterpriseId == 0)) {
                return new ProfileNetworkPreference(this.mPreference, this.mIncludedUids, this.mExcludedUids, this.mPreferenceEnterpriseId);
            } else {
                throw new IllegalStateException("Invalid preference enterprise identifier");
            }
        }

        public Builder setPreferenceEnterpriseId(int i) {
            this.mPreferenceEnterpriseId = i;
            return this;
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mPreference);
        parcel.writeIntArray(this.mIncludedUids);
        parcel.writeIntArray(this.mExcludedUids);
        parcel.writeInt(this.mPreferenceEnterpriseId);
    }
}

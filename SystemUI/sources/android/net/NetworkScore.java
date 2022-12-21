package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
public final class NetworkScore implements Parcelable {
    public static final Parcelable.Creator<NetworkScore> CREATOR = new Parcelable.Creator<NetworkScore>() {
        public NetworkScore createFromParcel(Parcel parcel) {
            return new NetworkScore(parcel);
        }

        public NetworkScore[] newArray(int i) {
            return new NetworkScore[i];
        }
    };
    public static final int KEEP_CONNECTED_FOR_HANDOVER = 1;
    public static final int KEEP_CONNECTED_NONE = 0;
    public static final int MAX_AGENT_MANAGED_POLICY = 3;
    public static final int MIN_AGENT_MANAGED_POLICY = 1;
    public static final int POLICY_EXITING = 3;
    public static final int POLICY_TRANSPORT_PRIMARY = 2;
    public static final int POLICY_YIELD_TO_BAD_WIFI = 1;
    private final int mKeepConnectedReason;
    private final int mLegacyInt;
    private final long mPolicies;

    @Retention(RetentionPolicy.SOURCE)
    public @interface KeepConnectedReason {
    }

    public int describeContents() {
        return 0;
    }

    NetworkScore(int i, long j, int i2) {
        this.mLegacyInt = i;
        this.mPolicies = j;
        this.mKeepConnectedReason = i2;
    }

    private NetworkScore(Parcel parcel) {
        this.mLegacyInt = parcel.readInt();
        this.mPolicies = parcel.readLong();
        this.mKeepConnectedReason = parcel.readInt();
    }

    public int getLegacyInt() {
        return this.mLegacyInt;
    }

    public int getKeepConnectedReason() {
        return this.mKeepConnectedReason;
    }

    public boolean hasPolicy(int i) {
        return 0 != ((1 << i) & this.mPolicies);
    }

    public long getPolicies() {
        return this.mPolicies;
    }

    public boolean shouldYieldToBadWifi() {
        return hasPolicy(1);
    }

    @SystemApi
    public boolean isTransportPrimary() {
        return hasPolicy(2);
    }

    @SystemApi
    public boolean isExiting() {
        return hasPolicy(3);
    }

    public String toString() {
        return "Score(" + this.mLegacyInt + " ; Policies : " + this.mPolicies + NavigationBarInflaterView.KEY_CODE_END;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mLegacyInt);
        parcel.writeLong(this.mPolicies);
        parcel.writeInt(this.mKeepConnectedReason);
    }

    public static final class Builder {
        private static final int INVALID_LEGACY_INT = Integer.MIN_VALUE;
        private static final long POLICY_NONE = 0;
        private int mKeepConnectedReason = 0;
        private int mLegacyInt = Integer.MIN_VALUE;
        private int mPolicies = 0;

        public Builder setLegacyInt(int i) {
            this.mLegacyInt = i;
            return this;
        }

        public Builder setShouldYieldToBadWifi(boolean z) {
            if (z) {
                this.mPolicies = (int) (((long) this.mPolicies) | 2);
            } else {
                this.mPolicies = (int) (((long) this.mPolicies) & -3);
            }
            return this;
        }

        @SystemApi
        public Builder setTransportPrimary(boolean z) {
            if (z) {
                this.mPolicies = (int) (((long) this.mPolicies) | 4);
            } else {
                this.mPolicies = (int) (((long) this.mPolicies) & -5);
            }
            return this;
        }

        @SystemApi
        public Builder setExiting(boolean z) {
            if (z) {
                this.mPolicies = (int) (((long) this.mPolicies) | 8);
            } else {
                this.mPolicies = (int) (((long) this.mPolicies) & -9);
            }
            return this;
        }

        public Builder setKeepConnectedReason(int i) {
            this.mKeepConnectedReason = i;
            return this;
        }

        public NetworkScore build() {
            return new NetworkScore(this.mLegacyInt, (long) this.mPolicies, this.mKeepConnectedReason);
        }
    }
}

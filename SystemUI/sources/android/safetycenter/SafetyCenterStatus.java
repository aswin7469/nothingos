package android.safetycenter;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@SystemApi
public final class SafetyCenterStatus implements Parcelable {
    public static final Parcelable.Creator<SafetyCenterStatus> CREATOR = new Parcelable.Creator<SafetyCenterStatus>() {
        public SafetyCenterStatus createFromParcel(Parcel parcel) {
            return new Builder((CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel), (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel)).setSeverityLevel(parcel.readInt()).setRefreshStatus(parcel.readInt()).build();
        }

        public SafetyCenterStatus[] newArray(int i) {
            return new SafetyCenterStatus[i];
        }
    };
    public static final int OVERALL_SEVERITY_LEVEL_CRITICAL_WARNING = 1300;
    public static final int OVERALL_SEVERITY_LEVEL_OK = 1100;
    public static final int OVERALL_SEVERITY_LEVEL_RECOMMENDATION = 1200;
    public static final int OVERALL_SEVERITY_LEVEL_UNKNOWN = 1000;
    public static final int REFRESH_STATUS_DATA_FETCH_IN_PROGRESS = 10100;
    public static final int REFRESH_STATUS_FULL_RESCAN_IN_PROGRESS = 10200;
    public static final int REFRESH_STATUS_NONE = 0;
    /* access modifiers changed from: private */
    public final int mRefreshStatus;
    /* access modifiers changed from: private */
    public final int mSeverityLevel;
    /* access modifiers changed from: private */
    public final CharSequence mSummary;
    /* access modifiers changed from: private */
    public final CharSequence mTitle;

    @Retention(RetentionPolicy.SOURCE)
    public @interface OverallSeverityLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RefreshStatus {
    }

    public int describeContents() {
        return 0;
    }

    private SafetyCenterStatus(CharSequence charSequence, CharSequence charSequence2, int i, int i2) {
        this.mTitle = charSequence;
        this.mSummary = charSequence2;
        this.mSeverityLevel = i;
        this.mRefreshStatus = i2;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getSummary() {
        return this.mSummary;
    }

    public int getSeverityLevel() {
        return this.mSeverityLevel;
    }

    public int getRefreshStatus() {
        return this.mRefreshStatus;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetyCenterStatus)) {
            return false;
        }
        SafetyCenterStatus safetyCenterStatus = (SafetyCenterStatus) obj;
        if (this.mSeverityLevel != safetyCenterStatus.mSeverityLevel || this.mRefreshStatus != safetyCenterStatus.mRefreshStatus || !TextUtils.equals(this.mTitle, safetyCenterStatus.mTitle) || !TextUtils.equals(this.mSummary, safetyCenterStatus.mSummary)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mTitle, this.mSummary, Integer.valueOf(this.mSeverityLevel), Integer.valueOf(this.mRefreshStatus));
    }

    public String toString() {
        return "SafetyCenterStatus{mTitle=" + this.mTitle + ", mSummary=" + this.mSummary + ", mSeverityLevel=" + this.mSeverityLevel + ", mRefreshStatus=" + this.mRefreshStatus + '}';
    }

    public void writeToParcel(Parcel parcel, int i) {
        TextUtils.writeToParcel(this.mTitle, parcel, i);
        TextUtils.writeToParcel(this.mSummary, parcel, i);
        parcel.writeInt(this.mSeverityLevel);
        parcel.writeInt(this.mRefreshStatus);
    }

    public static final class Builder {
        private int mRefreshStatus = 0;
        private int mSeverityLevel = 1000;
        private CharSequence mSummary;
        private CharSequence mTitle;

        public Builder(CharSequence charSequence, CharSequence charSequence2) {
            this.mTitle = (CharSequence) Objects.requireNonNull(charSequence);
            this.mSummary = (CharSequence) Objects.requireNonNull(charSequence2);
        }

        public Builder(SafetyCenterStatus safetyCenterStatus) {
            this.mTitle = safetyCenterStatus.mTitle;
            this.mSummary = safetyCenterStatus.mSummary;
            this.mSeverityLevel = safetyCenterStatus.mSeverityLevel;
            this.mRefreshStatus = safetyCenterStatus.mRefreshStatus;
        }

        public Builder setTitle(CharSequence charSequence) {
            this.mTitle = (CharSequence) Objects.requireNonNull(charSequence);
            return this;
        }

        public Builder setSummary(CharSequence charSequence) {
            this.mSummary = (CharSequence) Objects.requireNonNull(charSequence);
            return this;
        }

        public Builder setSeverityLevel(int i) {
            this.mSeverityLevel = SafetyCenterStatus.validateOverallSeverityLevel(i);
            return this;
        }

        public Builder setRefreshStatus(int i) {
            this.mRefreshStatus = SafetyCenterStatus.validateRefreshStatus(i);
            return this;
        }

        public SafetyCenterStatus build() {
            return new SafetyCenterStatus(this.mTitle, this.mSummary, this.mSeverityLevel, this.mRefreshStatus);
        }
    }

    /* access modifiers changed from: private */
    public static int validateOverallSeverityLevel(int i) {
        if (i == 1000 || i == 1100 || i == 1200 || i == 1300) {
            return i;
        }
        throw new IllegalArgumentException(String.format("Unexpected OverallSeverityLevel for SafetyCenterStatus: %s", Integer.valueOf(i)));
    }

    /* access modifiers changed from: private */
    public static int validateRefreshStatus(int i) {
        if (i == 0 || i == 10100 || i == 10200) {
            return i;
        }
        throw new IllegalArgumentException(String.format("Unexpected RefreshStatus for SafetyCenterStatus: %s", Integer.valueOf(i)));
    }
}

package android.safetycenter;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.permission.jarjar.com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SystemApi
public final class SafetySourceData implements Parcelable {
    public static final Parcelable.Creator<SafetySourceData> CREATOR = new Parcelable.Creator<SafetySourceData>() {
        public SafetySourceData createFromParcel(Parcel parcel) {
            List list = (List) Objects.requireNonNull(parcel.createTypedArrayList(SafetySourceIssue.CREATOR));
            Builder status = new Builder().setStatus((SafetySourceStatus) parcel.readTypedObject(SafetySourceStatus.CREATOR));
            for (int i = 0; i < list.size(); i++) {
                status.addIssue((SafetySourceIssue) list.get(i));
            }
            return status.build();
        }

        public SafetySourceData[] newArray(int i) {
            return new SafetySourceData[i];
        }
    };
    public static final int SEVERITY_LEVEL_CRITICAL_WARNING = 400;
    public static final int SEVERITY_LEVEL_INFORMATION = 200;
    public static final int SEVERITY_LEVEL_RECOMMENDATION = 300;
    public static final int SEVERITY_LEVEL_UNSPECIFIED = 100;
    private final List<SafetySourceIssue> mIssues;
    private final SafetySourceStatus mStatus;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SeverityLevel {
    }

    public int describeContents() {
        return 0;
    }

    private SafetySourceData(SafetySourceStatus safetySourceStatus, List<SafetySourceIssue> list) {
        this.mStatus = safetySourceStatus;
        this.mIssues = list;
    }

    public SafetySourceStatus getStatus() {
        return this.mStatus;
    }

    public List<SafetySourceIssue> getIssues() {
        return this.mIssues;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedObject(this.mStatus, i);
        parcel.writeTypedList(this.mIssues);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetySourceData)) {
            return false;
        }
        SafetySourceData safetySourceData = (SafetySourceData) obj;
        if (!Objects.equals(this.mStatus, safetySourceData.mStatus) || !this.mIssues.equals(safetySourceData.mIssues)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mStatus, this.mIssues);
    }

    public String toString() {
        return "SafetySourceData{, mStatus=" + this.mStatus + ", mIssues=" + this.mIssues + '}';
    }

    public static final class Builder {
        private final List<SafetySourceIssue> mIssues = new ArrayList();
        private SafetySourceStatus mStatus;

        public Builder setStatus(SafetySourceStatus safetySourceStatus) {
            this.mStatus = safetySourceStatus;
            return this;
        }

        public Builder addIssue(SafetySourceIssue safetySourceIssue) {
            this.mIssues.add((SafetySourceIssue) Objects.requireNonNull(safetySourceIssue));
            return this;
        }

        public Builder clearIssues() {
            this.mIssues.clear();
            return this;
        }

        public SafetySourceData build() {
            int issuesMaxSeverityLevel;
            List unmodifiableList = Collections.unmodifiableList(new ArrayList(this.mIssues));
            if (this.mStatus != null && (issuesMaxSeverityLevel = getIssuesMaxSeverityLevel(unmodifiableList)) > 200) {
                Preconditions.checkArgument(issuesMaxSeverityLevel <= this.mStatus.getSeverityLevel(), "Safety source data must not contain any issue with a severity level both greater than SEVERITY_LEVEL_INFORMATION and greater than the status severity level");
            }
            return new SafetySourceData(this.mStatus, unmodifiableList);
        }

        private static int getIssuesMaxSeverityLevel(List<SafetySourceIssue> list) {
            int i = Integer.MIN_VALUE;
            for (int i2 = 0; i2 < list.size(); i2++) {
                i = Math.max(i, list.get(i2).getSeverityLevel());
            }
            return i;
        }
    }
}

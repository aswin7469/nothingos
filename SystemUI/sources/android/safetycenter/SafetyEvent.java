package android.safetycenter;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@SystemApi
public final class SafetyEvent implements Parcelable {
    public static final Parcelable.Creator<SafetyEvent> CREATOR = new Parcelable.Creator<SafetyEvent>() {
        public SafetyEvent createFromParcel(Parcel parcel) {
            return new Builder(parcel.readInt()).setRefreshBroadcastId(parcel.readString()).setSafetySourceIssueId(parcel.readString()).setSafetySourceIssueActionId(parcel.readString()).build();
        }

        public SafetyEvent[] newArray(int i) {
            return new SafetyEvent[i];
        }
    };
    public static final int SAFETY_EVENT_TYPE_DEVICE_LOCALE_CHANGED = 500;
    public static final int SAFETY_EVENT_TYPE_DEVICE_REBOOTED = 600;
    public static final int SAFETY_EVENT_TYPE_REFRESH_REQUESTED = 200;
    public static final int SAFETY_EVENT_TYPE_RESOLVING_ACTION_FAILED = 400;
    public static final int SAFETY_EVENT_TYPE_RESOLVING_ACTION_SUCCEEDED = 300;
    public static final int SAFETY_EVENT_TYPE_SOURCE_STATE_CHANGED = 100;
    private final String mRefreshBroadcastId;
    private final String mSafetySourceIssueActionId;
    private final String mSafetySourceIssueId;
    private final int mType;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    public int describeContents() {
        return 0;
    }

    private SafetyEvent(int i, String str, String str2, String str3) {
        this.mType = i;
        this.mRefreshBroadcastId = str;
        this.mSafetySourceIssueId = str2;
        this.mSafetySourceIssueActionId = str3;
    }

    public int getType() {
        return this.mType;
    }

    public String getRefreshBroadcastId() {
        return this.mRefreshBroadcastId;
    }

    public String getSafetySourceIssueId() {
        return this.mSafetySourceIssueId;
    }

    public String getSafetySourceIssueActionId() {
        return this.mSafetySourceIssueActionId;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mType);
        parcel.writeString(this.mRefreshBroadcastId);
        parcel.writeString(this.mSafetySourceIssueId);
        parcel.writeString(this.mSafetySourceIssueActionId);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetyEvent)) {
            return false;
        }
        SafetyEvent safetyEvent = (SafetyEvent) obj;
        if (this.mType != safetyEvent.mType || !Objects.equals(this.mRefreshBroadcastId, safetyEvent.mRefreshBroadcastId) || !Objects.equals(this.mSafetySourceIssueId, safetyEvent.mSafetySourceIssueId) || !Objects.equals(this.mSafetySourceIssueActionId, safetyEvent.mSafetySourceIssueActionId)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mType), this.mRefreshBroadcastId, this.mSafetySourceIssueId, this.mSafetySourceIssueActionId);
    }

    public String toString() {
        return "SafetyEvent{mType=" + this.mType + ", mRefreshBroadcastId='" + this.mRefreshBroadcastId + "', mSafetySourceIssueId='" + this.mSafetySourceIssueId + "', mSafetySourceIssueActionId='" + this.mSafetySourceIssueActionId + "'}";
    }

    public static final class Builder {
        private String mRefreshBroadcastId;
        private String mSafetySourceIssueActionId;
        private String mSafetySourceIssueId;
        private final int mType;

        public Builder(int i) {
            this.mType = SafetyEvent.validateType(i);
        }

        public Builder setRefreshBroadcastId(String str) {
            this.mRefreshBroadcastId = str;
            return this;
        }

        public Builder setSafetySourceIssueId(String str) {
            this.mSafetySourceIssueId = str;
            return this;
        }

        public Builder setSafetySourceIssueActionId(String str) {
            this.mSafetySourceIssueActionId = str;
            return this;
        }

        public SafetyEvent build() {
            int i = this.mType;
            if (i != 200) {
                if (i == 300 || i == 400) {
                    if (this.mSafetySourceIssueId == null) {
                        throw new IllegalArgumentException(String.format("Missing issue id for resolving action safety event (%s)", Integer.valueOf(this.mType)));
                    } else if (this.mSafetySourceIssueActionId == null) {
                        throw new IllegalArgumentException(String.format("Missing issue action id for resolving action safety event (%s)", Integer.valueOf(this.mType)));
                    }
                }
            } else if (this.mRefreshBroadcastId == null) {
                throw new IllegalArgumentException("Missing refresh broadcast id for refresh requested safety event");
            }
            return new SafetyEvent(this.mType, this.mRefreshBroadcastId, this.mSafetySourceIssueId, this.mSafetySourceIssueActionId);
        }
    }

    /* access modifiers changed from: private */
    public static int validateType(int i) {
        if (i == 100 || i == 200 || i == 300 || i == 400 || i == 500 || i == 600) {
            return i;
        }
        throw new IllegalArgumentException(String.format("Unexpected Type for SafetyEvent: %s", Integer.valueOf(i)));
    }
}

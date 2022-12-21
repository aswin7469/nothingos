package android.safetycenter.config;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@SystemApi
public final class SafetySource implements Parcelable {
    public static final Parcelable.Creator<SafetySource> CREATOR = new Parcelable.Creator<SafetySource>() {
        public SafetySource createFromParcel(Parcel parcel) {
            return new Builder(parcel.readInt()).setId(parcel.readString()).setPackageName(parcel.readString()).setTitleResId(parcel.readInt()).setTitleForWorkResId(parcel.readInt()).setSummaryResId(parcel.readInt()).setIntentAction(parcel.readString()).setProfile(parcel.readInt()).setInitialDisplayState(parcel.readInt()).setMaxSeverityLevel(parcel.readInt()).setSearchTermsResId(parcel.readInt()).setLoggingAllowed(parcel.readBoolean()).setRefreshOnPageOpenAllowed(parcel.readBoolean()).build();
        }

        public SafetySource[] newArray(int i) {
            return new SafetySource[i];
        }
    };
    public static final int INITIAL_DISPLAY_STATE_DISABLED = 1;
    public static final int INITIAL_DISPLAY_STATE_ENABLED = 0;
    public static final int INITIAL_DISPLAY_STATE_HIDDEN = 2;
    public static final int PROFILE_ALL = 2;
    public static final int PROFILE_NONE = 0;
    public static final int PROFILE_PRIMARY = 1;
    public static final int SAFETY_SOURCE_TYPE_DYNAMIC = 2;
    public static final int SAFETY_SOURCE_TYPE_ISSUE_ONLY = 3;
    public static final int SAFETY_SOURCE_TYPE_STATIC = 1;
    private final String mId;
    private final int mInitialDisplayState;
    private final String mIntentAction;
    private final boolean mLoggingAllowed;
    private final int mMaxSeverityLevel;
    private final String mPackageName;
    private final int mProfile;
    private final boolean mRefreshOnPageOpenAllowed;
    private final int mSearchTermsResId;
    private final int mSummaryResId;
    private final int mTitleForWorkResId;
    private final int mTitleResId;
    private final int mType;

    @Retention(RetentionPolicy.SOURCE)
    public @interface InitialDisplayState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Profile {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SafetySourceType {
    }

    public int describeContents() {
        return 0;
    }

    private SafetySource(int i, String str, String str2, int i2, int i3, int i4, String str3, int i5, int i6, int i7, int i8, boolean z, boolean z2) {
        this.mType = i;
        this.mId = str;
        this.mPackageName = str2;
        this.mTitleResId = i2;
        this.mTitleForWorkResId = i3;
        this.mSummaryResId = i4;
        this.mIntentAction = str3;
        this.mProfile = i5;
        this.mInitialDisplayState = i6;
        this.mMaxSeverityLevel = i7;
        this.mSearchTermsResId = i8;
        this.mLoggingAllowed = z;
        this.mRefreshOnPageOpenAllowed = z2;
    }

    public int getType() {
        return this.mType;
    }

    public String getId() {
        return this.mId;
    }

    public String getPackageName() {
        if (this.mType != 1) {
            return this.mPackageName;
        }
        throw new UnsupportedOperationException("getPackageName unsupported for static safety source");
    }

    public int getTitleResId() {
        if (this.mType != 3) {
            return this.mTitleResId;
        }
        throw new UnsupportedOperationException("getTitleResId unsupported for issue-only safety source");
    }

    public int getTitleForWorkResId() {
        if (this.mType == 3) {
            throw new UnsupportedOperationException("getTitleForWorkResId unsupported for issue-only safety source");
        } else if (this.mProfile != 1) {
            return this.mTitleForWorkResId;
        } else {
            throw new UnsupportedOperationException("getTitleForWorkResId unsupported for primary profile safety source");
        }
    }

    public int getSummaryResId() {
        if (this.mType != 3) {
            return this.mSummaryResId;
        }
        throw new UnsupportedOperationException("getSummaryResId unsupported for issue-only safety source");
    }

    public String getIntentAction() {
        if (this.mType != 3) {
            return this.mIntentAction;
        }
        throw new UnsupportedOperationException("getIntentAction unsupported for issue-only safety source");
    }

    public int getProfile() {
        return this.mProfile;
    }

    public int getInitialDisplayState() {
        int i = this.mType;
        if (i == 1) {
            throw new UnsupportedOperationException("getInitialDisplayState unsupported for static safety source");
        } else if (i != 3) {
            return this.mInitialDisplayState;
        } else {
            throw new UnsupportedOperationException("getInitialDisplayState unsupported for issue-only safety source");
        }
    }

    public int getMaxSeverityLevel() {
        if (this.mType != 1) {
            return this.mMaxSeverityLevel;
        }
        throw new UnsupportedOperationException("getMaxSeverityLevel unsupported for static safety source");
    }

    public int getSearchTermsResId() {
        if (this.mType != 3) {
            return this.mSearchTermsResId;
        }
        throw new UnsupportedOperationException("getSearchTermsResId unsupported for issue-only safety source");
    }

    public boolean isLoggingAllowed() {
        if (this.mType != 1) {
            return this.mLoggingAllowed;
        }
        throw new UnsupportedOperationException("isLoggingAllowed unsupported for static safety source");
    }

    public boolean isRefreshOnPageOpenAllowed() {
        if (this.mType != 1) {
            return this.mRefreshOnPageOpenAllowed;
        }
        throw new UnsupportedOperationException("isRefreshOnPageOpenAllowed unsupported for static safety source");
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetySource)) {
            return false;
        }
        SafetySource safetySource = (SafetySource) obj;
        if (this.mType == safetySource.mType && Objects.equals(this.mId, safetySource.mId) && Objects.equals(this.mPackageName, safetySource.mPackageName) && this.mTitleResId == safetySource.mTitleResId && this.mTitleForWorkResId == safetySource.mTitleForWorkResId && this.mSummaryResId == safetySource.mSummaryResId && Objects.equals(this.mIntentAction, safetySource.mIntentAction) && this.mProfile == safetySource.mProfile && this.mInitialDisplayState == safetySource.mInitialDisplayState && this.mMaxSeverityLevel == safetySource.mMaxSeverityLevel && this.mSearchTermsResId == safetySource.mSearchTermsResId && this.mLoggingAllowed == safetySource.mLoggingAllowed && this.mRefreshOnPageOpenAllowed == safetySource.mRefreshOnPageOpenAllowed) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mType), this.mId, this.mPackageName, Integer.valueOf(this.mTitleResId), Integer.valueOf(this.mTitleForWorkResId), Integer.valueOf(this.mSummaryResId), this.mIntentAction, Integer.valueOf(this.mProfile), Integer.valueOf(this.mInitialDisplayState), Integer.valueOf(this.mMaxSeverityLevel), Integer.valueOf(this.mSearchTermsResId), Boolean.valueOf(this.mLoggingAllowed), Boolean.valueOf(this.mRefreshOnPageOpenAllowed));
    }

    public String toString() {
        return "SafetySource{mType=" + this.mType + ", mId='" + this.mId + "', mPackageName='" + this.mPackageName + "', mTitleResId=" + this.mTitleResId + ", mTitleForWorkResId=" + this.mTitleForWorkResId + ", mSummaryResId=" + this.mSummaryResId + ", mIntentAction='" + this.mIntentAction + "', mProfile=" + this.mProfile + ", mInitialDisplayState=" + this.mInitialDisplayState + ", mMaxSeverityLevel=" + this.mMaxSeverityLevel + ", mSearchTermsResId=" + this.mSearchTermsResId + ", mLoggingAllowed=" + this.mLoggingAllowed + ", mRefreshOnPageOpenAllowed=" + this.mRefreshOnPageOpenAllowed + '}';
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mType);
        parcel.writeString(this.mId);
        parcel.writeString(this.mPackageName);
        parcel.writeInt(this.mTitleResId);
        parcel.writeInt(this.mTitleForWorkResId);
        parcel.writeInt(this.mSummaryResId);
        parcel.writeString(this.mIntentAction);
        parcel.writeInt(this.mProfile);
        parcel.writeInt(this.mInitialDisplayState);
        parcel.writeInt(this.mMaxSeverityLevel);
        parcel.writeInt(this.mSearchTermsResId);
        parcel.writeBoolean(this.mLoggingAllowed);
        parcel.writeBoolean(this.mRefreshOnPageOpenAllowed);
    }

    public static final class Builder {
        private String mId;
        private Integer mInitialDisplayState;
        private String mIntentAction;
        private Boolean mLoggingAllowed;
        private Integer mMaxSeverityLevel;
        private String mPackageName;
        private Integer mProfile;
        private Boolean mRefreshOnPageOpenAllowed;
        private Integer mSearchTermsResId;
        private Integer mSummaryResId;
        private Integer mTitleForWorkResId;
        private Integer mTitleResId;
        private final int mType;

        public Builder(int i) {
            this.mType = i;
        }

        public Builder setId(String str) {
            this.mId = str;
            return this;
        }

        public Builder setPackageName(String str) {
            this.mPackageName = str;
            return this;
        }

        public Builder setTitleResId(int i) {
            this.mTitleResId = Integer.valueOf(i);
            return this;
        }

        public Builder setTitleForWorkResId(int i) {
            this.mTitleForWorkResId = Integer.valueOf(i);
            return this;
        }

        public Builder setSummaryResId(int i) {
            this.mSummaryResId = Integer.valueOf(i);
            return this;
        }

        public Builder setIntentAction(String str) {
            this.mIntentAction = str;
            return this;
        }

        public Builder setProfile(int i) {
            this.mProfile = Integer.valueOf(i);
            return this;
        }

        public Builder setInitialDisplayState(int i) {
            this.mInitialDisplayState = Integer.valueOf(i);
            return this;
        }

        public Builder setMaxSeverityLevel(int i) {
            this.mMaxSeverityLevel = Integer.valueOf(i);
            return this;
        }

        public Builder setSearchTermsResId(int i) {
            this.mSearchTermsResId = Integer.valueOf(i);
            return this;
        }

        public Builder setLoggingAllowed(boolean z) {
            this.mLoggingAllowed = Boolean.valueOf(z);
            return this;
        }

        public Builder setRefreshOnPageOpenAllowed(boolean z) {
            this.mRefreshOnPageOpenAllowed = Boolean.valueOf(z);
            return this;
        }

        public SafetySource build() {
            int i = this.mType;
            if (i == 1 || i == 2 || i == 3) {
                boolean z = i == 1;
                boolean z2 = i == 2;
                boolean z3 = i == 3;
                BuilderUtils.validateAttribute(this.mId, "id", true, false);
                BuilderUtils.validateAttribute(this.mPackageName, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME, z2 || z3, z);
                int validateIntDef = BuilderUtils.validateIntDef(this.mInitialDisplayState, "initialDisplayState", false, z || z3, 0, 0, 1, 2);
                boolean z4 = validateIntDef == 0;
                boolean z5 = validateIntDef == 2;
                boolean z6 = z2 && !z5;
                int validateIntDef2 = BuilderUtils.validateIntDef(this.mProfile, "profile", true, false, 0, 1, 2);
                boolean z7 = validateIntDef2 == 2;
                int validateResId = BuilderUtils.validateResId(this.mSearchTermsResId, "searchTerms", false, z3);
                boolean z8 = z6 || (z2 && z5 && validateResId != 0) || z;
                int validateResId2 = BuilderUtils.validateResId(this.mTitleResId, "title", z8, z3);
                int validateResId3 = BuilderUtils.validateResId(this.mTitleForWorkResId, "titleForWork", z7 && z8, !z7 || z3);
                int validateResId4 = BuilderUtils.validateResId(this.mSummaryResId, "summary", z6, z3);
                BuilderUtils.validateAttribute(this.mIntentAction, "intentAction", (z2 && z4) || z, z3);
                return new SafetySource(this.mType, this.mId, this.mPackageName, validateResId2, validateResId3, validateResId4, this.mIntentAction, validateIntDef2, validateIntDef, BuilderUtils.validateInteger(this.mMaxSeverityLevel, "maxSeverityLevel", false, z, Integer.MAX_VALUE), validateResId, BuilderUtils.validateBoolean(this.mLoggingAllowed, "loggingAllowed", false, z, true), BuilderUtils.validateBoolean(this.mRefreshOnPageOpenAllowed, "refreshOnPageOpenAllowed", false, z, false));
            }
            throw new IllegalStateException("Unexpected type");
        }
    }
}

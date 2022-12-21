package android.safetycenter;

import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SystemApi
public final class SafetyCenterIssue implements Parcelable {
    public static final Parcelable.Creator<SafetyCenterIssue> CREATOR = new Parcelable.Creator<SafetyCenterIssue>() {
        public SafetyCenterIssue createFromParcel(Parcel parcel) {
            return new Builder(parcel.readString(), (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel), (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel)).setSubtitle((CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel)).setSeverityLevel(parcel.readInt()).setDismissible(parcel.readBoolean()).setShouldConfirmDismissal(parcel.readBoolean()).setActions(parcel.createTypedArrayList(Action.CREATOR)).build();
        }

        public SafetyCenterIssue[] newArray(int i) {
            return new SafetyCenterIssue[i];
        }
    };
    public static final int ISSUE_SEVERITY_LEVEL_CRITICAL_WARNING = 2300;
    public static final int ISSUE_SEVERITY_LEVEL_OK = 2100;
    public static final int ISSUE_SEVERITY_LEVEL_RECOMMENDATION = 2200;
    /* access modifiers changed from: private */
    public final List<Action> mActions;
    /* access modifiers changed from: private */
    public final boolean mDismissible;
    /* access modifiers changed from: private */
    public final String mId;
    /* access modifiers changed from: private */
    public final int mSeverityLevel;
    /* access modifiers changed from: private */
    public final boolean mShouldConfirmDismissal;
    /* access modifiers changed from: private */
    public final CharSequence mSubtitle;
    /* access modifiers changed from: private */
    public final CharSequence mSummary;
    /* access modifiers changed from: private */
    public final CharSequence mTitle;

    @Retention(RetentionPolicy.SOURCE)
    public @interface IssueSeverityLevel {
    }

    public int describeContents() {
        return 0;
    }

    private SafetyCenterIssue(String str, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, boolean z, boolean z2, List<Action> list) {
        this.mId = str;
        this.mTitle = charSequence;
        this.mSubtitle = charSequence2;
        this.mSummary = charSequence3;
        this.mSeverityLevel = i;
        this.mDismissible = z;
        this.mShouldConfirmDismissal = z2;
        this.mActions = list;
    }

    public String getId() {
        return this.mId;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    public CharSequence getSummary() {
        return this.mSummary;
    }

    public int getSeverityLevel() {
        return this.mSeverityLevel;
    }

    public boolean isDismissible() {
        return this.mDismissible;
    }

    public boolean shouldConfirmDismissal() {
        return this.mShouldConfirmDismissal;
    }

    public List<Action> getActions() {
        return this.mActions;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetyCenterIssue)) {
            return false;
        }
        SafetyCenterIssue safetyCenterIssue = (SafetyCenterIssue) obj;
        if (this.mSeverityLevel != safetyCenterIssue.mSeverityLevel || this.mDismissible != safetyCenterIssue.mDismissible || this.mShouldConfirmDismissal != safetyCenterIssue.mShouldConfirmDismissal || !Objects.equals(this.mId, safetyCenterIssue.mId) || !TextUtils.equals(this.mTitle, safetyCenterIssue.mTitle) || !TextUtils.equals(this.mSubtitle, safetyCenterIssue.mSubtitle) || !TextUtils.equals(this.mSummary, safetyCenterIssue.mSummary) || !Objects.equals(this.mActions, safetyCenterIssue.mActions)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mId, this.mTitle, this.mSubtitle, this.mSummary, Integer.valueOf(this.mSeverityLevel), Boolean.valueOf(this.mDismissible), Boolean.valueOf(this.mShouldConfirmDismissal), this.mActions);
    }

    public String toString() {
        return "SafetyCenterIssue{mId='" + this.mId + "', mTitle=" + this.mTitle + ", mSubtitle=" + this.mSubtitle + ", mSummary=" + this.mSummary + ", mSeverityLevel=" + this.mSeverityLevel + ", mDismissible=" + this.mDismissible + ", mConfirmDismissal=" + this.mShouldConfirmDismissal + ", mActions=" + this.mActions + '}';
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mId);
        TextUtils.writeToParcel(this.mTitle, parcel, i);
        TextUtils.writeToParcel(this.mSubtitle, parcel, i);
        TextUtils.writeToParcel(this.mSummary, parcel, i);
        parcel.writeInt(this.mSeverityLevel);
        parcel.writeBoolean(this.mDismissible);
        parcel.writeBoolean(this.mShouldConfirmDismissal);
        parcel.writeTypedList(this.mActions);
    }

    public static final class Builder {
        private List<Action> mActions = new ArrayList();
        private boolean mDismissible = true;
        private String mId;
        private int mSeverityLevel = SafetyCenterIssue.ISSUE_SEVERITY_LEVEL_OK;
        private boolean mShouldConfirmDismissal = true;
        private CharSequence mSubtitle;
        private CharSequence mSummary;
        private CharSequence mTitle;

        public Builder(String str, CharSequence charSequence, CharSequence charSequence2) {
            this.mId = (String) Objects.requireNonNull(str);
            this.mTitle = (CharSequence) Objects.requireNonNull(charSequence);
            this.mSummary = (CharSequence) Objects.requireNonNull(charSequence2);
        }

        public Builder(SafetyCenterIssue safetyCenterIssue) {
            this.mId = safetyCenterIssue.mId;
            this.mTitle = safetyCenterIssue.mTitle;
            this.mSubtitle = safetyCenterIssue.mSubtitle;
            this.mSummary = safetyCenterIssue.mSummary;
            this.mSeverityLevel = safetyCenterIssue.mSeverityLevel;
            this.mDismissible = safetyCenterIssue.mDismissible;
            this.mShouldConfirmDismissal = safetyCenterIssue.mShouldConfirmDismissal;
            this.mActions = new ArrayList(safetyCenterIssue.mActions);
        }

        public Builder setId(String str) {
            this.mId = (String) Objects.requireNonNull(str);
            return this;
        }

        public Builder setTitle(CharSequence charSequence) {
            this.mTitle = (CharSequence) Objects.requireNonNull(charSequence);
            return this;
        }

        public Builder setSubtitle(CharSequence charSequence) {
            this.mSubtitle = charSequence;
            return this;
        }

        public Builder setSummary(CharSequence charSequence) {
            this.mSummary = (CharSequence) Objects.requireNonNull(charSequence);
            return this;
        }

        public Builder setSeverityLevel(int i) {
            this.mSeverityLevel = SafetyCenterIssue.validateIssueSeverityLevel(i);
            return this;
        }

        public Builder setDismissible(boolean z) {
            this.mDismissible = z;
            return this;
        }

        public Builder setShouldConfirmDismissal(boolean z) {
            this.mShouldConfirmDismissal = z;
            return this;
        }

        public Builder setActions(List<Action> list) {
            this.mActions = (List) Objects.requireNonNull(list);
            return this;
        }

        public SafetyCenterIssue build() {
            return new SafetyCenterIssue(this.mId, this.mTitle, this.mSubtitle, this.mSummary, this.mSeverityLevel, this.mDismissible, this.mShouldConfirmDismissal, Collections.unmodifiableList(new ArrayList(this.mActions)));
        }
    }

    @SystemApi
    public static final class Action implements Parcelable {
        public static final Parcelable.Creator<Action> CREATOR = new Parcelable.Creator<Action>() {
            public Action createFromParcel(Parcel parcel) {
                return new Builder(parcel.readString(), (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel), (PendingIntent) parcel.readTypedObject(PendingIntent.CREATOR)).setWillResolve(parcel.readBoolean()).setIsInFlight(parcel.readBoolean()).setSuccessMessage((CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel)).build();
            }

            public Action[] newArray(int i) {
                return new Action[i];
            }
        };
        private final String mId;
        private final boolean mInFlight;
        private final CharSequence mLabel;
        private final PendingIntent mPendingIntent;
        private final CharSequence mSuccessMessage;
        private final boolean mWillResolve;

        public int describeContents() {
            return 0;
        }

        private Action(String str, CharSequence charSequence, PendingIntent pendingIntent, boolean z, boolean z2, CharSequence charSequence2) {
            this.mId = str;
            this.mLabel = charSequence;
            this.mPendingIntent = pendingIntent;
            this.mWillResolve = z;
            this.mInFlight = z2;
            this.mSuccessMessage = charSequence2;
        }

        public String getId() {
            return this.mId;
        }

        public CharSequence getLabel() {
            return this.mLabel;
        }

        public PendingIntent getPendingIntent() {
            return this.mPendingIntent;
        }

        public boolean willResolve() {
            return this.mWillResolve;
        }

        public boolean isInFlight() {
            return this.mInFlight;
        }

        public CharSequence getSuccessMessage() {
            return this.mSuccessMessage;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Action)) {
                return false;
            }
            Action action = (Action) obj;
            if (!Objects.equals(this.mId, action.mId) || !TextUtils.equals(this.mLabel, action.mLabel) || !Objects.equals(this.mPendingIntent, action.mPendingIntent) || this.mWillResolve != action.mWillResolve || this.mInFlight != action.mInFlight || !TextUtils.equals(this.mSuccessMessage, action.mSuccessMessage)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(this.mId, this.mLabel, this.mSuccessMessage, Boolean.valueOf(this.mWillResolve), Boolean.valueOf(this.mInFlight), this.mPendingIntent);
        }

        public String toString() {
            return "Action{mId=" + this.mId + ", mLabel=" + this.mLabel + ", mPendingIntent=" + this.mPendingIntent + ", mWillResolve=" + this.mWillResolve + ", mInFlight=" + this.mInFlight + ", mSuccessMessage=" + this.mSuccessMessage + '}';
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.mId);
            TextUtils.writeToParcel(this.mLabel, parcel, i);
            parcel.writeTypedObject(this.mPendingIntent, i);
            parcel.writeBoolean(this.mWillResolve);
            parcel.writeBoolean(this.mInFlight);
            TextUtils.writeToParcel(this.mSuccessMessage, parcel, i);
        }

        public static final class Builder {
            private String mId;
            private boolean mInFlight;
            private CharSequence mLabel;
            private PendingIntent mPendingIntent;
            private CharSequence mSuccessMessage;
            private boolean mWillResolve;

            public Builder(String str, CharSequence charSequence, PendingIntent pendingIntent) {
                this.mId = (String) Objects.requireNonNull(str);
                this.mLabel = (CharSequence) Objects.requireNonNull(charSequence);
                this.mPendingIntent = (PendingIntent) Objects.requireNonNull(pendingIntent);
            }

            public Builder setId(String str) {
                this.mId = (String) Objects.requireNonNull(str);
                return this;
            }

            public Builder setLabel(CharSequence charSequence) {
                this.mLabel = (CharSequence) Objects.requireNonNull(charSequence);
                return this;
            }

            public Builder setPendingIntent(PendingIntent pendingIntent) {
                this.mPendingIntent = (PendingIntent) Objects.requireNonNull(pendingIntent);
                return this;
            }

            public Builder setWillResolve(boolean z) {
                this.mWillResolve = z;
                return this;
            }

            public Builder setIsInFlight(boolean z) {
                this.mInFlight = z;
                return this;
            }

            public Builder setSuccessMessage(CharSequence charSequence) {
                this.mSuccessMessage = charSequence;
                return this;
            }

            public Action build() {
                return new Action(this.mId, this.mLabel, this.mPendingIntent, this.mWillResolve, this.mInFlight, this.mSuccessMessage);
            }
        }
    }

    /* access modifiers changed from: private */
    public static int validateIssueSeverityLevel(int i) {
        if (i == 2100 || i == 2200 || i == 2300) {
            return i;
        }
        throw new IllegalArgumentException(String.format("Unexpected IssueSeverityLevel for SafetyCenterIssue: %s", Integer.valueOf(i)));
    }
}

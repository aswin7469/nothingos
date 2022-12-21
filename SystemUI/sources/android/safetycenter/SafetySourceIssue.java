package android.safetycenter;

import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.android.permission.jarjar.com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SystemApi
public final class SafetySourceIssue implements Parcelable {
    public static final Parcelable.Creator<SafetySourceIssue> CREATOR = new Parcelable.Creator<SafetySourceIssue>() {
        public SafetySourceIssue createFromParcel(Parcel parcel) {
            int readInt = parcel.readInt();
            int readInt2 = parcel.readInt();
            List list = (List) Objects.requireNonNull(parcel.createTypedArrayList(Action.CREATOR));
            Builder onDismissPendingIntent = new Builder(parcel.readString(), (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel), (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel), readInt, parcel.readString()).setSubtitle((CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel)).setIssueCategory(readInt2).setOnDismissPendingIntent((PendingIntent) parcel.readTypedObject(PendingIntent.CREATOR));
            for (int i = 0; i < list.size(); i++) {
                onDismissPendingIntent.addAction((Action) list.get(i));
            }
            return onDismissPendingIntent.build();
        }

        public SafetySourceIssue[] newArray(int i) {
            return new SafetySourceIssue[i];
        }
    };
    public static final int ISSUE_CATEGORY_ACCOUNT = 200;
    public static final int ISSUE_CATEGORY_DEVICE = 100;
    public static final int ISSUE_CATEGORY_GENERAL = 300;
    private final List<Action> mActions;
    private final String mId;
    private final int mIssueCategory;
    private final String mIssueTypeId;
    private final PendingIntent mOnDismissPendingIntent;
    private final int mSeverityLevel;
    private final CharSequence mSubtitle;
    private final CharSequence mSummary;
    private final CharSequence mTitle;

    @Retention(RetentionPolicy.SOURCE)
    public @interface IssueCategory {
    }

    public int describeContents() {
        return 0;
    }

    private SafetySourceIssue(String str, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, int i2, List<Action> list, PendingIntent pendingIntent, String str2) {
        this.mId = str;
        this.mTitle = charSequence;
        this.mSubtitle = charSequence2;
        this.mSummary = charSequence3;
        this.mSeverityLevel = i;
        this.mIssueCategory = i2;
        this.mActions = list;
        this.mOnDismissPendingIntent = pendingIntent;
        this.mIssueTypeId = str2;
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

    public int getIssueCategory() {
        return this.mIssueCategory;
    }

    public List<Action> getActions() {
        return this.mActions;
    }

    public PendingIntent getOnDismissPendingIntent() {
        return this.mOnDismissPendingIntent;
    }

    public String getIssueTypeId() {
        return this.mIssueTypeId;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mId);
        TextUtils.writeToParcel(this.mTitle, parcel, i);
        TextUtils.writeToParcel(this.mSubtitle, parcel, i);
        TextUtils.writeToParcel(this.mSummary, parcel, i);
        parcel.writeInt(this.mSeverityLevel);
        parcel.writeInt(this.mIssueCategory);
        parcel.writeTypedList(this.mActions);
        parcel.writeTypedObject(this.mOnDismissPendingIntent, i);
        parcel.writeString(this.mIssueTypeId);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetySourceIssue)) {
            return false;
        }
        SafetySourceIssue safetySourceIssue = (SafetySourceIssue) obj;
        if (this.mSeverityLevel != safetySourceIssue.mSeverityLevel || !TextUtils.equals(this.mId, safetySourceIssue.mId) || !TextUtils.equals(this.mTitle, safetySourceIssue.mTitle) || !TextUtils.equals(this.mSubtitle, safetySourceIssue.mSubtitle) || !TextUtils.equals(this.mSummary, safetySourceIssue.mSummary) || this.mIssueCategory != safetySourceIssue.mIssueCategory || !this.mActions.equals(safetySourceIssue.mActions) || !Objects.equals(this.mOnDismissPendingIntent, safetySourceIssue.mOnDismissPendingIntent) || !TextUtils.equals(this.mIssueTypeId, safetySourceIssue.mIssueTypeId)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mId, this.mTitle, this.mSubtitle, this.mSummary, Integer.valueOf(this.mSeverityLevel), Integer.valueOf(this.mIssueCategory), this.mActions, this.mOnDismissPendingIntent, this.mIssueTypeId);
    }

    public String toString() {
        return "SafetySourceIssue{mId=" + this.mId + "mTitle=" + this.mTitle + ", mSubtitle=" + this.mSubtitle + ", mSummary=" + this.mSummary + ", mSeverityLevel=" + this.mSeverityLevel + ", mIssueCategory=" + this.mIssueCategory + ", mActions=" + this.mActions + ", mOnDismissPendingIntent=" + this.mOnDismissPendingIntent + ", mIssueTypeId=" + this.mIssueTypeId + '}';
    }

    @SystemApi
    public static final class Action implements Parcelable {
        public static final Parcelable.Creator<Action> CREATOR = new Parcelable.Creator<Action>() {
            public Action createFromParcel(Parcel parcel) {
                return new Builder(parcel.readString(), (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel), (PendingIntent) parcel.readTypedObject(PendingIntent.CREATOR)).setWillResolve(parcel.readBoolean()).setSuccessMessage((CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel)).build();
            }

            public Action[] newArray(int i) {
                return new Action[i];
            }
        };
        private final String mId;
        private final CharSequence mLabel;
        private final PendingIntent mPendingIntent;
        private final CharSequence mSuccessMessage;
        private final boolean mWillResolve;

        public int describeContents() {
            return 0;
        }

        private Action(String str, CharSequence charSequence, PendingIntent pendingIntent, boolean z, CharSequence charSequence2) {
            this.mId = str;
            this.mLabel = charSequence;
            this.mPendingIntent = pendingIntent;
            this.mWillResolve = z;
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

        public CharSequence getSuccessMessage() {
            return this.mSuccessMessage;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.mId);
            TextUtils.writeToParcel(this.mLabel, parcel, i);
            parcel.writeTypedObject(this.mPendingIntent, i);
            parcel.writeBoolean(this.mWillResolve);
            TextUtils.writeToParcel(this.mSuccessMessage, parcel, i);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Action)) {
                return false;
            }
            Action action = (Action) obj;
            if (!this.mId.equals(action.mId) || !TextUtils.equals(this.mLabel, action.mLabel) || !this.mPendingIntent.equals(action.mPendingIntent) || this.mWillResolve != action.mWillResolve || !TextUtils.equals(this.mSuccessMessage, action.mSuccessMessage)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(this.mId, this.mLabel, this.mPendingIntent, Boolean.valueOf(this.mWillResolve), this.mSuccessMessage);
        }

        public String toString() {
            return "Action{mId=" + this.mId + ", mLabel=" + this.mLabel + ", mPendingIntent=" + this.mPendingIntent + ", mWillResolve=" + this.mWillResolve + ", mSuccessMessage=" + this.mSuccessMessage + '}';
        }

        public static final class Builder {
            private final String mId;
            private final CharSequence mLabel;
            private final PendingIntent mPendingIntent;
            private CharSequence mSuccessMessage;
            private boolean mWillResolve = false;

            public Builder(String str, CharSequence charSequence, PendingIntent pendingIntent) {
                this.mId = (String) Objects.requireNonNull(str);
                this.mLabel = (CharSequence) Objects.requireNonNull(charSequence);
                this.mPendingIntent = (PendingIntent) Objects.requireNonNull(pendingIntent);
            }

            public Builder setWillResolve(boolean z) {
                this.mWillResolve = z;
                return this;
            }

            public Builder setSuccessMessage(CharSequence charSequence) {
                this.mSuccessMessage = charSequence;
                return this;
            }

            public Action build() {
                return new Action(this.mId, this.mLabel, this.mPendingIntent, this.mWillResolve, this.mSuccessMessage);
            }
        }
    }

    public static final class Builder {
        private final List<Action> mActions = new ArrayList();
        private final String mId;
        private int mIssueCategory = 300;
        private final String mIssueTypeId;
        private PendingIntent mOnDismissPendingIntent;
        private final int mSeverityLevel;
        private CharSequence mSubtitle;
        private final CharSequence mSummary;
        private final CharSequence mTitle;

        public Builder(String str, CharSequence charSequence, CharSequence charSequence2, int i, String str2) {
            this.mId = (String) Objects.requireNonNull(str);
            this.mTitle = (CharSequence) Objects.requireNonNull(charSequence);
            this.mSummary = (CharSequence) Objects.requireNonNull(charSequence2);
            this.mSeverityLevel = SafetySourceIssue.validateSeverityLevel(i);
            this.mIssueTypeId = (String) Objects.requireNonNull(str2);
        }

        public Builder setSubtitle(CharSequence charSequence) {
            this.mSubtitle = charSequence;
            return this;
        }

        public Builder setIssueCategory(int i) {
            this.mIssueCategory = SafetySourceIssue.validateIssueCategory(i);
            return this;
        }

        public Builder addAction(Action action) {
            this.mActions.add((Action) Objects.requireNonNull(action));
            return this;
        }

        public Builder clearActions() {
            this.mActions.clear();
            return this;
        }

        public Builder setOnDismissPendingIntent(PendingIntent pendingIntent) {
            Preconditions.checkArgument(pendingIntent == null || !pendingIntent.isActivity(), "Safety source issue on dismiss pending intent must not start an activity");
            this.mOnDismissPendingIntent = pendingIntent;
            return this;
        }

        public SafetySourceIssue build() {
            List unmodifiableList = Collections.unmodifiableList(new ArrayList(this.mActions));
            boolean z = true;
            Preconditions.checkArgument(!unmodifiableList.isEmpty(), "Safety source issue must contain at least 1 action");
            if (unmodifiableList.size() > 2) {
                z = false;
            }
            Preconditions.checkArgument(z, "Safety source issue must not contain more than 2 actions");
            return new SafetySourceIssue(this.mId, this.mTitle, this.mSubtitle, this.mSummary, this.mSeverityLevel, this.mIssueCategory, unmodifiableList, this.mOnDismissPendingIntent, this.mIssueTypeId);
        }
    }

    /* access modifiers changed from: private */
    public static int validateSeverityLevel(int i) {
        if (i == 100) {
            throw new IllegalArgumentException("SeverityLevel for SafetySourceIssue must not be SEVERITY_LEVEL_UNSPECIFIED");
        } else if (i == 200 || i == 300 || i == 400) {
            return i;
        } else {
            throw new IllegalArgumentException(String.format("Unexpected SeverityLevel for SafetySourceIssue: %s", Integer.valueOf(i)));
        }
    }

    /* access modifiers changed from: private */
    public static int validateIssueCategory(int i) {
        if (i == 100 || i == 200 || i == 300) {
            return i;
        }
        throw new IllegalArgumentException(String.format("Unexpected IssueCategory for SafetySourceIssue: %s", Integer.valueOf(i)));
    }
}

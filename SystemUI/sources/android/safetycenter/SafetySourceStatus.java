package android.safetycenter;

import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.android.permission.jarjar.com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@SystemApi
public final class SafetySourceStatus implements Parcelable {
    public static final Parcelable.Creator<SafetySourceStatus> CREATOR = new Parcelable.Creator<SafetySourceStatus>() {
        public SafetySourceStatus createFromParcel(Parcel parcel) {
            return new Builder((CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel), (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel), parcel.readInt()).setPendingIntent((PendingIntent) parcel.readTypedObject(PendingIntent.CREATOR)).setIconAction((IconAction) parcel.readTypedObject(IconAction.CREATOR)).setEnabled(parcel.readBoolean()).build();
        }

        public SafetySourceStatus[] newArray(int i) {
            return new SafetySourceStatus[i];
        }
    };
    private final boolean mEnabled;
    private final IconAction mIconAction;
    private final PendingIntent mPendingIntent;
    private final int mSeverityLevel;
    private final CharSequence mSummary;
    private final CharSequence mTitle;

    public int describeContents() {
        return 0;
    }

    private SafetySourceStatus(CharSequence charSequence, CharSequence charSequence2, int i, PendingIntent pendingIntent, IconAction iconAction, boolean z) {
        this.mTitle = charSequence;
        this.mSummary = charSequence2;
        this.mSeverityLevel = i;
        this.mPendingIntent = pendingIntent;
        this.mIconAction = iconAction;
        this.mEnabled = z;
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

    public PendingIntent getPendingIntent() {
        return this.mPendingIntent;
    }

    public IconAction getIconAction() {
        return this.mIconAction;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void writeToParcel(Parcel parcel, int i) {
        TextUtils.writeToParcel(this.mTitle, parcel, i);
        TextUtils.writeToParcel(this.mSummary, parcel, i);
        parcel.writeInt(this.mSeverityLevel);
        parcel.writeTypedObject(this.mPendingIntent, i);
        parcel.writeTypedObject(this.mIconAction, i);
        parcel.writeBoolean(this.mEnabled);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetySourceStatus)) {
            return false;
        }
        SafetySourceStatus safetySourceStatus = (SafetySourceStatus) obj;
        if (this.mSeverityLevel != safetySourceStatus.mSeverityLevel || this.mEnabled != safetySourceStatus.mEnabled || !TextUtils.equals(this.mTitle, safetySourceStatus.mTitle) || !TextUtils.equals(this.mSummary, safetySourceStatus.mSummary) || !Objects.equals(this.mPendingIntent, safetySourceStatus.mPendingIntent) || !Objects.equals(this.mIconAction, safetySourceStatus.mIconAction)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mTitle, this.mSummary, Integer.valueOf(this.mSeverityLevel), this.mPendingIntent, this.mIconAction, Boolean.valueOf(this.mEnabled));
    }

    public String toString() {
        return "SafetySourceStatus{mTitle=" + this.mTitle + ", mSummary=" + this.mSummary + ", mSeverityLevel=" + this.mSeverityLevel + ", mPendingIntent=" + this.mPendingIntent + ", mIconAction=" + this.mIconAction + ", mEnabled=" + this.mEnabled + '}';
    }

    @SystemApi
    public static final class IconAction implements Parcelable {
        public static final Parcelable.Creator<IconAction> CREATOR = new Parcelable.Creator<IconAction>() {
            public IconAction createFromParcel(Parcel parcel) {
                return new IconAction(parcel.readInt(), (PendingIntent) parcel.readTypedObject(PendingIntent.CREATOR));
            }

            public IconAction[] newArray(int i) {
                return new IconAction[i];
            }
        };
        public static final int ICON_TYPE_GEAR = 100;
        public static final int ICON_TYPE_INFO = 200;
        private final int mIconType;
        private final PendingIntent mPendingIntent;

        @Retention(RetentionPolicy.SOURCE)
        public @interface IconType {
        }

        public int describeContents() {
            return 0;
        }

        public IconAction(int i, PendingIntent pendingIntent) {
            this.mIconType = validateIconType(i);
            this.mPendingIntent = (PendingIntent) Objects.requireNonNull(pendingIntent);
        }

        public int getIconType() {
            return this.mIconType;
        }

        public PendingIntent getPendingIntent() {
            return this.mPendingIntent;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.mIconType);
            parcel.writeTypedObject(this.mPendingIntent, i);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof IconAction)) {
                return false;
            }
            IconAction iconAction = (IconAction) obj;
            if (this.mIconType != iconAction.mIconType || !this.mPendingIntent.equals(iconAction.mPendingIntent)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.mIconType), this.mPendingIntent);
        }

        public String toString() {
            return "IconAction{mIconType=" + this.mIconType + ", mPendingIntent=" + this.mPendingIntent + '}';
        }

        private static int validateIconType(int i) {
            if (i == 100 || i == 200) {
                return i;
            }
            throw new IllegalArgumentException(String.format("Unexpected IconType for IconAction: %s", Integer.valueOf(i)));
        }
    }

    public static final class Builder {
        private boolean mEnabled = true;
        private IconAction mIconAction;
        private PendingIntent mPendingIntent;
        private final int mSeverityLevel;
        private final CharSequence mSummary;
        private final CharSequence mTitle;

        public Builder(CharSequence charSequence, CharSequence charSequence2, int i) {
            this.mTitle = (CharSequence) Objects.requireNonNull(charSequence);
            this.mSummary = (CharSequence) Objects.requireNonNull(charSequence2);
            this.mSeverityLevel = SafetySourceStatus.validateSeverityLevel(i);
        }

        public Builder setPendingIntent(PendingIntent pendingIntent) {
            Preconditions.checkArgument(pendingIntent == null || pendingIntent.isActivity(), "Safety source status pending intent must start an activity");
            this.mPendingIntent = pendingIntent;
            return this;
        }

        public Builder setIconAction(IconAction iconAction) {
            this.mIconAction = iconAction;
            return this;
        }

        public Builder setEnabled(boolean z) {
            Preconditions.checkArgument(z || this.mSeverityLevel == 100, "Safety source status must have a severity level of SEVERITY_LEVEL_UNSPECIFIED when disabled");
            this.mEnabled = z;
            return this;
        }

        public SafetySourceStatus build() {
            return new SafetySourceStatus(this.mTitle, this.mSummary, this.mSeverityLevel, this.mPendingIntent, this.mIconAction, this.mEnabled);
        }
    }

    /* access modifiers changed from: private */
    public static int validateSeverityLevel(int i) {
        if (i == 100 || i == 200 || i == 300 || i == 400) {
            return i;
        }
        throw new IllegalArgumentException(String.format("Unexpected SeverityLevel for SafetySourceStatus: %s", Integer.valueOf(i)));
    }
}

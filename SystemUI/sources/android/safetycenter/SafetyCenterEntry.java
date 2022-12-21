package android.safetycenter;

import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@SystemApi
public final class SafetyCenterEntry implements Parcelable {
    public static final Parcelable.Creator<SafetyCenterEntry> CREATOR = new Parcelable.Creator<SafetyCenterEntry>() {
        public SafetyCenterEntry createFromParcel(Parcel parcel) {
            return new Builder(parcel.readString(), (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel)).setSummary((CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel)).setSeverityLevel(parcel.readInt()).setSeverityUnspecifiedIconType(parcel.readInt()).setEnabled(parcel.readBoolean()).setPendingIntent((PendingIntent) parcel.readTypedObject(PendingIntent.CREATOR)).setIconAction((IconAction) parcel.readTypedObject(IconAction.CREATOR)).build();
        }

        public SafetyCenterEntry[] newArray(int i) {
            return new SafetyCenterEntry[i];
        }
    };
    public static final int ENTRY_SEVERITY_LEVEL_CRITICAL_WARNING = 3400;
    public static final int ENTRY_SEVERITY_LEVEL_OK = 3200;
    public static final int ENTRY_SEVERITY_LEVEL_RECOMMENDATION = 3300;
    public static final int ENTRY_SEVERITY_LEVEL_UNKNOWN = 3000;
    public static final int ENTRY_SEVERITY_LEVEL_UNSPECIFIED = 3100;
    public static final int SEVERITY_UNSPECIFIED_ICON_TYPE_NO_ICON = 0;
    public static final int SEVERITY_UNSPECIFIED_ICON_TYPE_NO_RECOMMENDATION = 2;
    public static final int SEVERITY_UNSPECIFIED_ICON_TYPE_PRIVACY = 1;
    /* access modifiers changed from: private */
    public final boolean mEnabled;
    /* access modifiers changed from: private */
    public final IconAction mIconAction;
    /* access modifiers changed from: private */
    public final String mId;
    /* access modifiers changed from: private */
    public final PendingIntent mPendingIntent;
    /* access modifiers changed from: private */
    public final int mSeverityLevel;
    /* access modifiers changed from: private */
    public final int mSeverityUnspecifiedIconType;
    /* access modifiers changed from: private */
    public final CharSequence mSummary;
    /* access modifiers changed from: private */
    public final CharSequence mTitle;

    @Retention(RetentionPolicy.SOURCE)
    public @interface EntrySeverityLevel {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SeverityUnspecifiedIconType {
    }

    public int describeContents() {
        return 0;
    }

    private SafetyCenterEntry(String str, CharSequence charSequence, CharSequence charSequence2, int i, int i2, boolean z, PendingIntent pendingIntent, IconAction iconAction) {
        this.mId = str;
        this.mTitle = charSequence;
        this.mSummary = charSequence2;
        this.mSeverityLevel = i;
        this.mSeverityUnspecifiedIconType = i2;
        this.mEnabled = z;
        this.mPendingIntent = pendingIntent;
        this.mIconAction = iconAction;
    }

    public String getId() {
        return this.mId;
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

    public int getSeverityUnspecifiedIconType() {
        return this.mSeverityUnspecifiedIconType;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public PendingIntent getPendingIntent() {
        return this.mPendingIntent;
    }

    public IconAction getIconAction() {
        return this.mIconAction;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetyCenterEntry)) {
            return false;
        }
        SafetyCenterEntry safetyCenterEntry = (SafetyCenterEntry) obj;
        if (this.mSeverityLevel != safetyCenterEntry.mSeverityLevel || this.mSeverityUnspecifiedIconType != safetyCenterEntry.mSeverityUnspecifiedIconType || this.mEnabled != safetyCenterEntry.mEnabled || !Objects.equals(this.mId, safetyCenterEntry.mId) || !TextUtils.equals(this.mTitle, safetyCenterEntry.mTitle) || !TextUtils.equals(this.mSummary, safetyCenterEntry.mSummary) || !Objects.equals(this.mPendingIntent, safetyCenterEntry.mPendingIntent) || !Objects.equals(this.mIconAction, safetyCenterEntry.mIconAction)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mId, this.mTitle, this.mSummary, Integer.valueOf(this.mSeverityLevel), Integer.valueOf(this.mSeverityUnspecifiedIconType), Boolean.valueOf(this.mEnabled), this.mPendingIntent, this.mIconAction);
    }

    public String toString() {
        return "SafetyCenterEntry{mId='" + this.mId + "', mTitle=" + this.mTitle + ", mSummary=" + this.mSummary + ", mSeverityLevel=" + this.mSeverityLevel + ", mSeverityUnspecifiedIconType=" + this.mSeverityUnspecifiedIconType + ", mEnabled=" + this.mEnabled + ", mPendingIntent=" + this.mPendingIntent + ", mIconAction=" + this.mIconAction + '}';
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mId);
        TextUtils.writeToParcel(this.mTitle, parcel, i);
        TextUtils.writeToParcel(this.mSummary, parcel, i);
        parcel.writeInt(this.mSeverityLevel);
        parcel.writeInt(this.mSeverityUnspecifiedIconType);
        parcel.writeBoolean(this.mEnabled);
        parcel.writeTypedObject(this.mPendingIntent, i);
        parcel.writeTypedObject(this.mIconAction, i);
    }

    public static final class Builder {
        private boolean mEnabled = true;
        private IconAction mIconAction;
        private String mId;
        private PendingIntent mPendingIntent;
        private int mSeverityLevel = 3000;
        private int mSeverityUnspecifiedIconType = 0;
        private CharSequence mSummary;
        private CharSequence mTitle;

        public Builder(String str, CharSequence charSequence) {
            this.mId = (String) Objects.requireNonNull(str);
            this.mTitle = (CharSequence) Objects.requireNonNull(charSequence);
        }

        public Builder(SafetyCenterEntry safetyCenterEntry) {
            this.mId = safetyCenterEntry.mId;
            this.mTitle = safetyCenterEntry.mTitle;
            this.mSummary = safetyCenterEntry.mSummary;
            this.mSeverityLevel = safetyCenterEntry.mSeverityLevel;
            this.mSeverityUnspecifiedIconType = safetyCenterEntry.mSeverityUnspecifiedIconType;
            this.mEnabled = safetyCenterEntry.mEnabled;
            this.mPendingIntent = safetyCenterEntry.mPendingIntent;
            this.mIconAction = safetyCenterEntry.mIconAction;
        }

        public Builder setId(String str) {
            this.mId = (String) Objects.requireNonNull(str);
            return this;
        }

        public Builder setTitle(CharSequence charSequence) {
            this.mTitle = (CharSequence) Objects.requireNonNull(charSequence);
            return this;
        }

        public Builder setSummary(CharSequence charSequence) {
            this.mSummary = charSequence;
            return this;
        }

        public Builder setSeverityLevel(int i) {
            this.mSeverityLevel = SafetyCenterEntry.validateEntrySeverityLevel(i);
            return this;
        }

        public Builder setSeverityUnspecifiedIconType(int i) {
            this.mSeverityUnspecifiedIconType = SafetyCenterEntry.validateSeverityUnspecifiedIconType(i);
            return this;
        }

        public Builder setEnabled(boolean z) {
            this.mEnabled = z;
            return this;
        }

        public Builder setPendingIntent(PendingIntent pendingIntent) {
            this.mPendingIntent = pendingIntent;
            return this;
        }

        public Builder setIconAction(IconAction iconAction) {
            this.mIconAction = iconAction;
            return this;
        }

        public Builder setIconAction(int i, PendingIntent pendingIntent) {
            this.mIconAction = new IconAction(i, pendingIntent);
            return this;
        }

        public SafetyCenterEntry build() {
            return new SafetyCenterEntry(this.mId, this.mTitle, this.mSummary, this.mSeverityLevel, this.mSeverityUnspecifiedIconType, this.mEnabled, this.mPendingIntent, this.mIconAction);
        }
    }

    public static final class IconAction implements Parcelable {
        public static final Parcelable.Creator<IconAction> CREATOR = new Parcelable.Creator<IconAction>() {
            public IconAction createFromParcel(Parcel parcel) {
                return new IconAction(parcel.readInt(), (PendingIntent) parcel.readTypedObject(PendingIntent.CREATOR));
            }

            public IconAction[] newArray(int i) {
                return new IconAction[i];
            }
        };
        public static final int ICON_ACTION_TYPE_GEAR = 30100;
        public static final int ICON_ACTION_TYPE_INFO = 30200;
        private final PendingIntent mPendingIntent;
        private final int mType;

        @Retention(RetentionPolicy.SOURCE)
        public @interface IconActionType {
        }

        public int describeContents() {
            return 0;
        }

        public IconAction(int i, PendingIntent pendingIntent) {
            this.mType = validateIconActionType(i);
            this.mPendingIntent = (PendingIntent) Objects.requireNonNull(pendingIntent);
        }

        public int getType() {
            return this.mType;
        }

        public PendingIntent getPendingIntent() {
            return this.mPendingIntent;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof IconAction)) {
                return false;
            }
            IconAction iconAction = (IconAction) obj;
            if (this.mType != iconAction.mType || !Objects.equals(this.mPendingIntent, iconAction.mPendingIntent)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.mType), this.mPendingIntent);
        }

        public String toString() {
            return "IconAction{mType=" + this.mType + ", mPendingIntent=" + this.mPendingIntent + '}';
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.mType);
            parcel.writeTypedObject(this.mPendingIntent, i);
        }

        private static int validateIconActionType(int i) {
            if (i == 30100 || i == 30200) {
                return i;
            }
            throw new IllegalArgumentException(String.format("Unexpected IconActionType for IconAction: %s", Integer.valueOf(i)));
        }
    }

    /* access modifiers changed from: private */
    public static int validateEntrySeverityLevel(int i) {
        if (i == 3000 || i == 3100 || i == 3200 || i == 3300 || i == 3400) {
            return i;
        }
        throw new IllegalArgumentException(String.format("Unexpected EntrySeverityLevel for SafetyCenterEntry: %s", Integer.valueOf(i)));
    }

    /* access modifiers changed from: private */
    public static int validateSeverityUnspecifiedIconType(int i) {
        if (i == 0 || i == 1 || i == 2) {
            return i;
        }
        throw new IllegalArgumentException(String.format("Unexpected SeverityUnspecifiedIconType for SafetyCenterEntry: %s", Integer.valueOf(i)));
    }
}

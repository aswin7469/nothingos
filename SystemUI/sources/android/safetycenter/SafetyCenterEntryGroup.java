package android.safetycenter;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SystemApi
public final class SafetyCenterEntryGroup implements Parcelable {
    public static final Parcelable.Creator<SafetyCenterEntryGroup> CREATOR = new Parcelable.Creator<SafetyCenterEntryGroup>() {
        public SafetyCenterEntryGroup createFromParcel(Parcel parcel) {
            return new Builder(parcel.readString(), (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel)).setSummary((CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel)).setSeverityLevel(parcel.readInt()).setSeverityUnspecifiedIconType(parcel.readInt()).setEntries(parcel.createTypedArrayList(SafetyCenterEntry.CREATOR)).build();
        }

        public SafetyCenterEntryGroup[] newArray(int i) {
            return new SafetyCenterEntryGroup[i];
        }
    };
    /* access modifiers changed from: private */
    public final List<SafetyCenterEntry> mEntries;
    /* access modifiers changed from: private */
    public final String mId;
    /* access modifiers changed from: private */
    public final int mSeverityLevel;
    /* access modifiers changed from: private */
    public final int mSeverityUnspecifiedIconType;
    /* access modifiers changed from: private */
    public final CharSequence mSummary;
    /* access modifiers changed from: private */
    public final CharSequence mTitle;

    public int describeContents() {
        return 0;
    }

    private SafetyCenterEntryGroup(String str, CharSequence charSequence, CharSequence charSequence2, int i, int i2, List<SafetyCenterEntry> list) {
        this.mId = str;
        this.mTitle = charSequence;
        this.mSummary = charSequence2;
        this.mSeverityLevel = i;
        this.mSeverityUnspecifiedIconType = i2;
        this.mEntries = list;
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

    public List<SafetyCenterEntry> getEntries() {
        return this.mEntries;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetyCenterEntryGroup)) {
            return false;
        }
        SafetyCenterEntryGroup safetyCenterEntryGroup = (SafetyCenterEntryGroup) obj;
        if (this.mSeverityLevel != safetyCenterEntryGroup.mSeverityLevel || this.mSeverityUnspecifiedIconType != safetyCenterEntryGroup.mSeverityUnspecifiedIconType || !Objects.equals(this.mId, safetyCenterEntryGroup.mId) || !TextUtils.equals(this.mTitle, safetyCenterEntryGroup.mTitle) || !TextUtils.equals(this.mSummary, safetyCenterEntryGroup.mSummary) || !Objects.equals(this.mEntries, safetyCenterEntryGroup.mEntries)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mId, this.mTitle, this.mSummary, Integer.valueOf(this.mSeverityLevel), Integer.valueOf(this.mSeverityUnspecifiedIconType), this.mEntries);
    }

    public String toString() {
        return "SafetyCenterEntryGroup{mId='" + this.mId + "', mTitle=" + this.mTitle + ", mSummary=" + this.mSummary + ", mSeverityLevel=" + this.mSeverityLevel + ", mSeverityUnspecifiedIconType=" + this.mSeverityUnspecifiedIconType + ", mEntries=" + this.mEntries + '}';
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mId);
        TextUtils.writeToParcel(this.mTitle, parcel, i);
        TextUtils.writeToParcel(this.mSummary, parcel, i);
        parcel.writeInt(this.mSeverityLevel);
        parcel.writeInt(this.mSeverityUnspecifiedIconType);
        parcel.writeTypedList(this.mEntries);
    }

    public static final class Builder {
        private List<SafetyCenterEntry> mEntries = new ArrayList();
        private String mId;
        private int mSeverityLevel = 3000;
        private int mSeverityUnspecifiedIconType = 0;
        private CharSequence mSummary;
        private CharSequence mTitle;

        public Builder(String str, CharSequence charSequence) {
            this.mId = (String) Objects.requireNonNull(str);
            this.mTitle = (CharSequence) Objects.requireNonNull(charSequence);
        }

        public Builder(SafetyCenterEntryGroup safetyCenterEntryGroup) {
            this.mId = safetyCenterEntryGroup.mId;
            this.mTitle = safetyCenterEntryGroup.mTitle;
            this.mSummary = safetyCenterEntryGroup.mSummary;
            this.mSeverityLevel = safetyCenterEntryGroup.mSeverityLevel;
            this.mSeverityUnspecifiedIconType = safetyCenterEntryGroup.mSeverityUnspecifiedIconType;
            this.mEntries = new ArrayList(safetyCenterEntryGroup.mEntries);
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
            this.mSeverityLevel = SafetyCenterEntryGroup.validateEntrySeverityLevel(i);
            return this;
        }

        public Builder setSeverityUnspecifiedIconType(int i) {
            this.mSeverityUnspecifiedIconType = SafetyCenterEntryGroup.validateSeverityUnspecifiedIconType(i);
            return this;
        }

        public Builder setEntries(List<SafetyCenterEntry> list) {
            this.mEntries = (List) Objects.requireNonNull(list);
            return this;
        }

        public SafetyCenterEntryGroup build() {
            return new SafetyCenterEntryGroup(this.mId, this.mTitle, this.mSummary, this.mSeverityLevel, this.mSeverityUnspecifiedIconType, Collections.unmodifiableList(new ArrayList(this.mEntries)));
        }
    }

    /* access modifiers changed from: private */
    public static int validateEntrySeverityLevel(int i) {
        if (i == 3000 || i == 3100 || i == 3200 || i == 3300 || i == 3400) {
            return i;
        }
        throw new IllegalArgumentException(String.format("Unexpected EntrySeverityLevel for SafetyCenterEntryGroup: %s", Integer.valueOf(i)));
    }

    /* access modifiers changed from: private */
    public static int validateSeverityUnspecifiedIconType(int i) {
        if (i == 0 || i == 1 || i == 2) {
            return i;
        }
        throw new IllegalArgumentException(String.format("Unexpected SeverityUnspecifiedIconType for SafetyCenterEntryGroup: %s", Integer.valueOf(i)));
    }
}

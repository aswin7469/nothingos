package android.safetycenter;

import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.Objects;

@SystemApi
public final class SafetyCenterStaticEntry implements Parcelable {
    public static final Parcelable.Creator<SafetyCenterStaticEntry> CREATOR = new Parcelable.Creator<SafetyCenterStaticEntry>() {
        public SafetyCenterStaticEntry createFromParcel(Parcel parcel) {
            return new Builder((CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel)).setSummary((CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel)).setPendingIntent((PendingIntent) parcel.readTypedObject(PendingIntent.CREATOR)).build();
        }

        public SafetyCenterStaticEntry[] newArray(int i) {
            return new SafetyCenterStaticEntry[i];
        }
    };
    /* access modifiers changed from: private */
    public final PendingIntent mPendingIntent;
    /* access modifiers changed from: private */
    public final CharSequence mSummary;
    /* access modifiers changed from: private */
    public final CharSequence mTitle;

    public int describeContents() {
        return 0;
    }

    private SafetyCenterStaticEntry(CharSequence charSequence, CharSequence charSequence2, PendingIntent pendingIntent) {
        this.mTitle = charSequence;
        this.mSummary = charSequence2;
        this.mPendingIntent = pendingIntent;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getSummary() {
        return this.mSummary;
    }

    public PendingIntent getPendingIntent() {
        return this.mPendingIntent;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetyCenterStaticEntry)) {
            return false;
        }
        SafetyCenterStaticEntry safetyCenterStaticEntry = (SafetyCenterStaticEntry) obj;
        if (!TextUtils.equals(this.mTitle, safetyCenterStaticEntry.mTitle) || !TextUtils.equals(this.mSummary, safetyCenterStaticEntry.mSummary) || !Objects.equals(this.mPendingIntent, safetyCenterStaticEntry.mPendingIntent)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mTitle, this.mSummary, this.mPendingIntent);
    }

    public String toString() {
        return "SafetyCenterStaticEntry{mTitle=" + this.mTitle + ", mSummary=" + this.mSummary + ", mPendingIntent=" + this.mPendingIntent + '}';
    }

    public void writeToParcel(Parcel parcel, int i) {
        TextUtils.writeToParcel(this.mTitle, parcel, i);
        TextUtils.writeToParcel(this.mSummary, parcel, i);
        parcel.writeTypedObject(this.mPendingIntent, i);
    }

    public static final class Builder {
        private PendingIntent mPendingIntent;
        private CharSequence mSummary;
        private CharSequence mTitle;

        public Builder(CharSequence charSequence) {
            this.mTitle = (CharSequence) Objects.requireNonNull(charSequence);
        }

        public Builder(SafetyCenterStaticEntry safetyCenterStaticEntry) {
            this.mTitle = safetyCenterStaticEntry.mTitle;
            this.mSummary = safetyCenterStaticEntry.mSummary;
            this.mPendingIntent = safetyCenterStaticEntry.mPendingIntent;
        }

        public Builder setTitle(CharSequence charSequence) {
            this.mTitle = (CharSequence) Objects.requireNonNull(charSequence);
            return this;
        }

        public Builder setSummary(CharSequence charSequence) {
            this.mSummary = charSequence;
            return this;
        }

        public Builder setPendingIntent(PendingIntent pendingIntent) {
            this.mPendingIntent = pendingIntent;
            return this;
        }

        public SafetyCenterStaticEntry build() {
            return new SafetyCenterStaticEntry(this.mTitle, this.mSummary, this.mPendingIntent);
        }
    }
}

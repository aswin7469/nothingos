package android.safetycenter;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SystemApi
public final class SafetyCenterStaticEntryGroup implements Parcelable {
    public static final Parcelable.Creator<SafetyCenterStaticEntryGroup> CREATOR = new Parcelable.Creator<SafetyCenterStaticEntryGroup>() {
        public SafetyCenterStaticEntryGroup createFromParcel(Parcel parcel) {
            return new SafetyCenterStaticEntryGroup((CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel), parcel.createTypedArrayList(SafetyCenterStaticEntry.CREATOR));
        }

        public SafetyCenterStaticEntryGroup[] newArray(int i) {
            return new SafetyCenterStaticEntryGroup[i];
        }
    };
    private final List<SafetyCenterStaticEntry> mStaticEntries;
    private final CharSequence mTitle;

    public int describeContents() {
        return 0;
    }

    public SafetyCenterStaticEntryGroup(CharSequence charSequence, List<SafetyCenterStaticEntry> list) {
        this.mTitle = (CharSequence) Objects.requireNonNull(charSequence);
        this.mStaticEntries = Collections.unmodifiableList(new ArrayList((Collection) Objects.requireNonNull(list)));
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public List<SafetyCenterStaticEntry> getStaticEntries() {
        return this.mStaticEntries;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetyCenterStaticEntryGroup)) {
            return false;
        }
        SafetyCenterStaticEntryGroup safetyCenterStaticEntryGroup = (SafetyCenterStaticEntryGroup) obj;
        if (!TextUtils.equals(this.mTitle, safetyCenterStaticEntryGroup.mTitle) || !Objects.equals(this.mStaticEntries, safetyCenterStaticEntryGroup.mStaticEntries)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mTitle, this.mStaticEntries);
    }

    public String toString() {
        return "SafetyCenterStaticEntryGroup{mTitle=" + this.mTitle + ", mStaticEntries=" + this.mStaticEntries + '}';
    }

    public void writeToParcel(Parcel parcel, int i) {
        TextUtils.writeToParcel(this.mTitle, parcel, i);
        parcel.writeTypedList(this.mStaticEntries);
    }
}

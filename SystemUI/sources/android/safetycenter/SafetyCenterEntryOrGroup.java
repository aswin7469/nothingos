package android.safetycenter;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

@SystemApi
public final class SafetyCenterEntryOrGroup implements Parcelable {
    public static final Parcelable.Creator<SafetyCenterEntryOrGroup> CREATOR = new Parcelable.Creator<SafetyCenterEntryOrGroup>() {
        public SafetyCenterEntryOrGroup createFromParcel(Parcel parcel) {
            SafetyCenterEntry safetyCenterEntry = (SafetyCenterEntry) parcel.readTypedObject(SafetyCenterEntry.CREATOR);
            SafetyCenterEntryGroup safetyCenterEntryGroup = (SafetyCenterEntryGroup) parcel.readTypedObject(SafetyCenterEntryGroup.CREATOR);
            if (safetyCenterEntry != null) {
                return new SafetyCenterEntryOrGroup(safetyCenterEntry);
            }
            return new SafetyCenterEntryOrGroup(safetyCenterEntryGroup);
        }

        public SafetyCenterEntryOrGroup[] newArray(int i) {
            return new SafetyCenterEntryOrGroup[i];
        }
    };
    private final SafetyCenterEntry mEntry;
    private final SafetyCenterEntryGroup mEntryGroup;

    public int describeContents() {
        return 0;
    }

    public SafetyCenterEntryOrGroup(SafetyCenterEntry safetyCenterEntry) {
        this.mEntry = (SafetyCenterEntry) Objects.requireNonNull(safetyCenterEntry);
        this.mEntryGroup = null;
    }

    public SafetyCenterEntryOrGroup(SafetyCenterEntryGroup safetyCenterEntryGroup) {
        this.mEntry = null;
        this.mEntryGroup = (SafetyCenterEntryGroup) Objects.requireNonNull(safetyCenterEntryGroup);
    }

    public SafetyCenterEntry getEntry() {
        return this.mEntry;
    }

    public SafetyCenterEntryGroup getEntryGroup() {
        return this.mEntryGroup;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetyCenterEntryOrGroup)) {
            return false;
        }
        SafetyCenterEntryOrGroup safetyCenterEntryOrGroup = (SafetyCenterEntryOrGroup) obj;
        if (!Objects.equals(this.mEntry, safetyCenterEntryOrGroup.mEntry) || !Objects.equals(this.mEntryGroup, safetyCenterEntryOrGroup.mEntryGroup)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mEntry, this.mEntryGroup);
    }

    public String toString() {
        return "SafetyCenterEntryOrGroup{mEntry=" + this.mEntry + ", mEntryGroup=" + this.mEntryGroup + '}';
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedObject(this.mEntry, i);
        parcel.writeTypedObject(this.mEntryGroup, i);
    }
}

package android.safetycenter;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SystemApi
public final class SafetyCenterData implements Parcelable {
    public static final Parcelable.Creator<SafetyCenterData> CREATOR = new Parcelable.Creator<SafetyCenterData>() {
        public SafetyCenterData createFromParcel(Parcel parcel) {
            return new SafetyCenterData((SafetyCenterStatus) parcel.readTypedObject(SafetyCenterStatus.CREATOR), parcel.createTypedArrayList(SafetyCenterIssue.CREATOR), parcel.createTypedArrayList(SafetyCenterEntryOrGroup.CREATOR), parcel.createTypedArrayList(SafetyCenterStaticEntryGroup.CREATOR));
        }

        public SafetyCenterData[] newArray(int i) {
            return new SafetyCenterData[i];
        }
    };
    private final List<SafetyCenterEntryOrGroup> mEntriesOrGroups;
    private final List<SafetyCenterIssue> mIssues;
    private final List<SafetyCenterStaticEntryGroup> mStaticEntryGroups;
    private final SafetyCenterStatus mStatus;

    public int describeContents() {
        return 0;
    }

    public SafetyCenterData(SafetyCenterStatus safetyCenterStatus, List<SafetyCenterIssue> list, List<SafetyCenterEntryOrGroup> list2, List<SafetyCenterStaticEntryGroup> list3) {
        this.mStatus = (SafetyCenterStatus) Objects.requireNonNull(safetyCenterStatus);
        this.mIssues = Collections.unmodifiableList(new ArrayList((Collection) Objects.requireNonNull(list)));
        this.mEntriesOrGroups = Collections.unmodifiableList(new ArrayList((Collection) Objects.requireNonNull(list2)));
        this.mStaticEntryGroups = Collections.unmodifiableList(new ArrayList((Collection) Objects.requireNonNull(list3)));
    }

    public SafetyCenterStatus getStatus() {
        return this.mStatus;
    }

    public List<SafetyCenterIssue> getIssues() {
        return this.mIssues;
    }

    public List<SafetyCenterEntryOrGroup> getEntriesOrGroups() {
        return this.mEntriesOrGroups;
    }

    public List<SafetyCenterStaticEntryGroup> getStaticEntryGroups() {
        return this.mStaticEntryGroups;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetyCenterData)) {
            return false;
        }
        SafetyCenterData safetyCenterData = (SafetyCenterData) obj;
        if (!Objects.equals(this.mStatus, safetyCenterData.mStatus) || !Objects.equals(this.mIssues, safetyCenterData.mIssues) || !Objects.equals(this.mEntriesOrGroups, safetyCenterData.mEntriesOrGroups) || !Objects.equals(this.mStaticEntryGroups, safetyCenterData.mStaticEntryGroups)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.mStatus, this.mIssues, this.mEntriesOrGroups, this.mStaticEntryGroups);
    }

    public String toString() {
        return "SafetyCenterData{mStatus=" + this.mStatus + ", mIssues=" + this.mIssues + ", mEntriesOrGroups=" + this.mEntriesOrGroups + ", mStaticEntryGroups=" + this.mStaticEntryGroups + '}';
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedObject(this.mStatus, i);
        parcel.writeTypedList(this.mIssues);
        parcel.writeTypedList(this.mEntriesOrGroups);
        parcel.writeTypedList(this.mStaticEntryGroups);
    }
}

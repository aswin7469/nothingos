package android.safetycenter;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

@SystemApi
public final class SafetySourceErrorDetails implements Parcelable {
    public static final Parcelable.Creator<SafetySourceErrorDetails> CREATOR = new Parcelable.Creator<SafetySourceErrorDetails>() {
        public SafetySourceErrorDetails[] newArray(int i) {
            return new SafetySourceErrorDetails[0];
        }

        public SafetySourceErrorDetails createFromParcel(Parcel parcel) {
            return new SafetySourceErrorDetails((SafetyEvent) parcel.readTypedObject(SafetyEvent.CREATOR));
        }
    };
    private final SafetyEvent mSafetyEvent;

    public int describeContents() {
        return 0;
    }

    public SafetySourceErrorDetails(SafetyEvent safetyEvent) {
        this.mSafetyEvent = (SafetyEvent) Objects.requireNonNull(safetyEvent);
    }

    public SafetyEvent getSafetyEvent() {
        return this.mSafetyEvent;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetySourceErrorDetails)) {
            return false;
        }
        return this.mSafetyEvent.equals(((SafetySourceErrorDetails) obj).mSafetyEvent);
    }

    public int hashCode() {
        return Objects.hash(this.mSafetyEvent);
    }

    public String toString() {
        return "SafetySourceErrorDetails{mSafetyEvent=" + this.mSafetyEvent + '}';
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedObject(this.mSafetyEvent, i);
    }
}

package android.safetycenter;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.Objects;

@SystemApi
public final class SafetyCenterErrorDetails implements Parcelable {
    public static final Parcelable.Creator<SafetyCenterErrorDetails> CREATOR = new Parcelable.Creator<SafetyCenterErrorDetails>() {
        public SafetyCenterErrorDetails[] newArray(int i) {
            return new SafetyCenterErrorDetails[0];
        }

        public SafetyCenterErrorDetails createFromParcel(Parcel parcel) {
            return new SafetyCenterErrorDetails((CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel));
        }
    };
    private final CharSequence mErrorMessage;

    public int describeContents() {
        return 0;
    }

    public SafetyCenterErrorDetails(CharSequence charSequence) {
        this.mErrorMessage = (CharSequence) Objects.requireNonNull(charSequence);
    }

    public CharSequence getErrorMessage() {
        return this.mErrorMessage;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SafetyCenterErrorDetails)) {
            return false;
        }
        return TextUtils.equals(this.mErrorMessage, ((SafetyCenterErrorDetails) obj).mErrorMessage);
    }

    public int hashCode() {
        return Objects.hash(this.mErrorMessage);
    }

    public String toString() {
        return "SafetyCenterErrorDetails{mErrorMessage=" + this.mErrorMessage + '}';
    }

    public void writeToParcel(Parcel parcel, int i) {
        TextUtils.writeToParcel(this.mErrorMessage, parcel, i);
    }
}

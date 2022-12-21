package android.nearby;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.internal.util.Preconditions;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
import java.util.Objects;

@SystemApi
public final class CredentialElement implements Parcelable {
    public static final Parcelable.Creator<CredentialElement> CREATOR = new Parcelable.Creator<CredentialElement>() {
        public CredentialElement createFromParcel(Parcel parcel) {
            String readString = parcel.readString();
            byte[] bArr = new byte[parcel.readInt()];
            parcel.readByteArray(bArr);
            return new CredentialElement(readString, bArr);
        }

        public CredentialElement[] newArray(int i) {
            return new CredentialElement[i];
        }
    };
    private final String mKey;
    private final byte[] mValue;

    public int describeContents() {
        return 0;
    }

    public CredentialElement(String str, byte[] bArr) {
        Preconditions.checkState((str == null || bArr == null) ? false : true, "neither key or value can be null");
        this.mKey = str;
        this.mValue = bArr;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mKey);
        parcel.writeInt(this.mValue.length);
        parcel.writeByteArray(this.mValue);
    }

    public String getKey() {
        return this.mKey;
    }

    public byte[] getValue() {
        return this.mValue;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CredentialElement)) {
            return false;
        }
        CredentialElement credentialElement = (CredentialElement) obj;
        if (!this.mKey.equals(credentialElement.mKey) || !Arrays.equals(this.mValue, credentialElement.mValue)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mKey.hashCode()), Integer.valueOf(Arrays.hashCode(this.mValue)));
    }
}

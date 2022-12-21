package android.nearby;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.internal.util.Preconditions;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

@SystemApi
public final class PrivateCredential extends PresenceCredential implements Parcelable {
    public static final Parcelable.Creator<PrivateCredential> CREATOR = new Parcelable.Creator<PrivateCredential>() {
        public PrivateCredential createFromParcel(Parcel parcel) {
            parcel.readInt();
            return PrivateCredential.createFromParcelBody(parcel);
        }

        public PrivateCredential[] newArray(int i) {
            return new PrivateCredential[i];
        }
    };
    private String mDeviceName;
    private byte[] mMetadataEncryptionKey;

    public int describeContents() {
        return 0;
    }

    private PrivateCredential(Parcel parcel) {
        super(0, parcel);
        byte[] bArr = new byte[parcel.readInt()];
        this.mMetadataEncryptionKey = bArr;
        parcel.readByteArray(bArr);
        this.mDeviceName = parcel.readString();
    }

    private PrivateCredential(int i, byte[] bArr, String str, byte[] bArr2, List<CredentialElement> list, byte[] bArr3) {
        super(0, i, bArr, bArr2, list);
        this.mDeviceName = str;
        this.mMetadataEncryptionKey = bArr3;
    }

    static PrivateCredential createFromParcelBody(Parcel parcel) {
        return new PrivateCredential(parcel);
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.mMetadataEncryptionKey.length);
        parcel.writeByteArray(this.mMetadataEncryptionKey);
        parcel.writeString(this.mDeviceName);
    }

    public byte[] getMetadataEncryptionKey() {
        return this.mMetadataEncryptionKey;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public static final class Builder {
        private final byte[] mAuthenticityKey;
        private final List<CredentialElement> mCredentialElements;
        private final String mDeviceName;
        private int mIdentityType;
        private final byte[] mMetadataEncryptionKey;
        private final byte[] mSecretId;

        public Builder(byte[] bArr, byte[] bArr2, byte[] bArr3, String str) {
            boolean z = true;
            Preconditions.checkState(bArr != null && bArr.length > 0, "secret id cannot be empty");
            Preconditions.checkState(bArr2 != null && bArr2.length > 0, "authenticity key cannot be empty");
            Preconditions.checkState(bArr3 != null && bArr3.length > 0, "metadataEncryptionKey cannot be empty");
            Preconditions.checkState((str == null || str.length() <= 0) ? false : z, "deviceName cannot be empty");
            this.mSecretId = bArr;
            this.mAuthenticityKey = bArr2;
            this.mMetadataEncryptionKey = bArr3;
            this.mDeviceName = str;
            this.mCredentialElements = new ArrayList();
        }

        public Builder setIdentityType(int i) {
            this.mIdentityType = i;
            return this;
        }

        public Builder addCredentialElement(CredentialElement credentialElement) {
            this.mCredentialElements.add(credentialElement);
            return this;
        }

        public PrivateCredential build() {
            return new PrivateCredential(this.mIdentityType, this.mSecretId, this.mDeviceName, this.mAuthenticityKey, this.mCredentialElements, this.mMetadataEncryptionKey);
        }
    }
}

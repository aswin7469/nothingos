package android.nearby;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.internal.util.Preconditions;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SystemApi
public final class PublicCredential extends PresenceCredential implements Parcelable {
    public static final Parcelable.Creator<PublicCredential> CREATOR = new Parcelable.Creator<PublicCredential>() {
        public PublicCredential createFromParcel(Parcel parcel) {
            parcel.readInt();
            return PublicCredential.createFromParcelBody(parcel);
        }

        public PublicCredential[] newArray(int i) {
            return new PublicCredential[i];
        }
    };
    private final byte[] mEncryptedMetadata;
    private final byte[] mEncryptedMetadataKeyTag;
    private final byte[] mPublicKey;

    public int describeContents() {
        return 0;
    }

    private PublicCredential(int i, byte[] bArr, byte[] bArr2, List<CredentialElement> list, byte[] bArr3, byte[] bArr4, byte[] bArr5) {
        super(1, i, bArr, bArr2, list);
        this.mPublicKey = bArr3;
        this.mEncryptedMetadata = bArr4;
        this.mEncryptedMetadataKeyTag = bArr5;
    }

    private PublicCredential(Parcel parcel) {
        super(1, parcel);
        byte[] bArr = new byte[parcel.readInt()];
        this.mPublicKey = bArr;
        parcel.readByteArray(bArr);
        byte[] bArr2 = new byte[parcel.readInt()];
        this.mEncryptedMetadata = bArr2;
        parcel.readByteArray(bArr2);
        byte[] bArr3 = new byte[parcel.readInt()];
        this.mEncryptedMetadataKeyTag = bArr3;
        parcel.readByteArray(bArr3);
    }

    static PublicCredential createFromParcelBody(Parcel parcel) {
        return new PublicCredential(parcel);
    }

    public byte[] getPublicKey() {
        return this.mPublicKey;
    }

    public byte[] getEncryptedMetadata() {
        return this.mEncryptedMetadata;
    }

    public byte[] getEncryptedMetadataKeyTag() {
        return this.mEncryptedMetadataKeyTag;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PublicCredential)) {
            return false;
        }
        PublicCredential publicCredential = (PublicCredential) obj;
        if (!super.equals(obj) || !Arrays.equals(this.mPublicKey, publicCredential.mPublicKey) || !Arrays.equals(this.mEncryptedMetadata, publicCredential.mEncryptedMetadata) || !Arrays.equals(this.mEncryptedMetadataKeyTag, publicCredential.mEncryptedMetadataKeyTag)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), Integer.valueOf(Arrays.hashCode(this.mPublicKey)), Integer.valueOf(Arrays.hashCode(this.mEncryptedMetadata)), Integer.valueOf(Arrays.hashCode(this.mEncryptedMetadataKeyTag)));
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.mPublicKey.length);
        parcel.writeByteArray(this.mPublicKey);
        parcel.writeInt(this.mEncryptedMetadata.length);
        parcel.writeByteArray(this.mEncryptedMetadata);
        parcel.writeInt(this.mEncryptedMetadataKeyTag.length);
        parcel.writeByteArray(this.mEncryptedMetadataKeyTag);
    }

    public static final class Builder {
        private final byte[] mAuthenticityKey;
        private final List<CredentialElement> mCredentialElements;
        private final byte[] mEncryptedMetadata;
        private final byte[] mEncryptedMetadataKeyTag;
        private int mIdentityType;
        private final byte[] mPublicKey;
        private final byte[] mSecretId;

        public Builder(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5) {
            boolean z = true;
            Preconditions.checkState(bArr != null && bArr.length > 0, "secret id cannot be empty");
            Preconditions.checkState(bArr2 != null && bArr2.length > 0, "authenticity key cannot be empty");
            Preconditions.checkState(bArr3 != null && bArr3.length > 0, "publicKey cannot be empty");
            Preconditions.checkState(bArr4 != null && bArr4.length > 0, "encryptedMetadata cannot be empty");
            Preconditions.checkState((bArr5 == null || bArr5.length <= 0) ? false : z, "encryptedMetadataKeyTag cannot be empty");
            this.mSecretId = bArr;
            this.mAuthenticityKey = bArr2;
            this.mPublicKey = bArr3;
            this.mEncryptedMetadata = bArr4;
            this.mEncryptedMetadataKeyTag = bArr5;
            this.mCredentialElements = new ArrayList();
        }

        public Builder setIdentityType(int i) {
            this.mIdentityType = i;
            return this;
        }

        public Builder addCredentialElement(CredentialElement credentialElement) {
            Objects.requireNonNull(credentialElement);
            this.mCredentialElements.add(credentialElement);
            return this;
        }

        public PublicCredential build() {
            return new PublicCredential(this.mIdentityType, this.mSecretId, this.mAuthenticityKey, this.mCredentialElements, this.mPublicKey, this.mEncryptedMetadata, this.mEncryptedMetadataKeyTag);
        }
    }
}

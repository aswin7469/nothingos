package android.nearby;

import android.annotation.SystemApi;
import android.os.Parcel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SystemApi
public abstract class PresenceCredential {
    public static final int CREDENTIAL_TYPE_PRIVATE = 0;
    public static final int CREDENTIAL_TYPE_PUBLIC = 1;
    public static final int IDENTITY_TYPE_PRIVATE = 1;
    public static final int IDENTITY_TYPE_PROVISIONED = 2;
    public static final int IDENTITY_TYPE_TRUSTED = 3;
    public static final int IDENTITY_TYPE_UNKNOWN = 0;
    private final byte[] mAuthenticityKey;
    private final List<CredentialElement> mCredentialElements;
    private final int mIdentityType;
    private final byte[] mSecretId;
    private final int mType;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CredentialType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface IdentityType {
    }

    PresenceCredential(int i, int i2, byte[] bArr, byte[] bArr2, List<CredentialElement> list) {
        this.mType = i;
        this.mIdentityType = i2;
        this.mSecretId = bArr;
        this.mAuthenticityKey = bArr2;
        this.mCredentialElements = list;
    }

    PresenceCredential(int i, Parcel parcel) {
        this.mType = i;
        this.mIdentityType = parcel.readInt();
        byte[] bArr = new byte[parcel.readInt()];
        this.mSecretId = bArr;
        parcel.readByteArray(bArr);
        byte[] bArr2 = new byte[parcel.readInt()];
        this.mAuthenticityKey = bArr2;
        parcel.readByteArray(bArr2);
        ArrayList arrayList = new ArrayList();
        this.mCredentialElements = arrayList;
        parcel.readList(arrayList, CredentialElement.class.getClassLoader(), CredentialElement.class);
    }

    public int getType() {
        return this.mType;
    }

    public int getIdentityType() {
        return this.mIdentityType;
    }

    public byte[] getSecretId() {
        return this.mSecretId;
    }

    public byte[] getAuthenticityKey() {
        return this.mAuthenticityKey;
    }

    public List<CredentialElement> getCredentialElements() {
        return this.mCredentialElements;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PresenceCredential)) {
            return false;
        }
        PresenceCredential presenceCredential = (PresenceCredential) obj;
        if (this.mType != presenceCredential.mType || this.mIdentityType != presenceCredential.mIdentityType || !Arrays.equals(this.mSecretId, presenceCredential.mSecretId) || !Arrays.equals(this.mAuthenticityKey, presenceCredential.mAuthenticityKey) || !this.mCredentialElements.equals(presenceCredential.mCredentialElements)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mType), Integer.valueOf(this.mIdentityType), Integer.valueOf(Arrays.hashCode(this.mSecretId)), Integer.valueOf(Arrays.hashCode(this.mAuthenticityKey)), Integer.valueOf(this.mCredentialElements.hashCode()));
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mType);
        parcel.writeInt(this.mIdentityType);
        parcel.writeInt(this.mSecretId.length);
        parcel.writeByteArray(this.mSecretId);
        parcel.writeInt(this.mAuthenticityKey.length);
        parcel.writeByteArray(this.mAuthenticityKey);
        parcel.writeList(this.mCredentialElements);
    }
}

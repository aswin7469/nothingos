package android.security.keystore.recovery;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
@SystemApi
/* loaded from: classes2.dex */
public final class WrappedApplicationKey implements Parcelable {
    public static final Parcelable.Creator<WrappedApplicationKey> CREATOR = new Parcelable.Creator<WrappedApplicationKey>() { // from class: android.security.keystore.recovery.WrappedApplicationKey.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public WrappedApplicationKey mo3559createFromParcel(Parcel in) {
            return new WrappedApplicationKey(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public WrappedApplicationKey[] mo3560newArray(int length) {
            return new WrappedApplicationKey[length];
        }
    };
    private String mAlias;
    private byte[] mEncryptedKeyMaterial;
    private byte[] mMetadata;

    /* loaded from: classes2.dex */
    public static class Builder {
        private WrappedApplicationKey mInstance = new WrappedApplicationKey();

        public Builder setAlias(String alias) {
            this.mInstance.mAlias = alias;
            return this;
        }

        public Builder setEncryptedKeyMaterial(byte[] encryptedKeyMaterial) {
            this.mInstance.mEncryptedKeyMaterial = encryptedKeyMaterial;
            return this;
        }

        public Builder setMetadata(byte[] metadata) {
            this.mInstance.mMetadata = metadata;
            return this;
        }

        public WrappedApplicationKey build() {
            Objects.requireNonNull(this.mInstance.mAlias);
            Objects.requireNonNull(this.mInstance.mEncryptedKeyMaterial);
            return this.mInstance;
        }
    }

    private WrappedApplicationKey() {
    }

    @Deprecated
    public WrappedApplicationKey(String alias, byte[] encryptedKeyMaterial) {
        Objects.requireNonNull(alias);
        this.mAlias = alias;
        Objects.requireNonNull(encryptedKeyMaterial);
        this.mEncryptedKeyMaterial = encryptedKeyMaterial;
    }

    public String getAlias() {
        return this.mAlias;
    }

    public byte[] getEncryptedKeyMaterial() {
        return this.mEncryptedKeyMaterial;
    }

    public byte[] getMetadata() {
        return this.mMetadata;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mAlias);
        out.writeByteArray(this.mEncryptedKeyMaterial);
        out.writeByteArray(this.mMetadata);
    }

    protected WrappedApplicationKey(Parcel in) {
        this.mAlias = in.readString();
        this.mEncryptedKeyMaterial = in.createByteArray();
        if (in.dataAvail() > 0) {
            this.mMetadata = in.createByteArray();
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}

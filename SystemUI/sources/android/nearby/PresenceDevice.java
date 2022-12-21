package android.nearby;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SystemApi
public final class PresenceDevice extends NearbyDevice implements Parcelable {
    public static final Parcelable.Creator<PresenceDevice> CREATOR = new Parcelable.Creator<PresenceDevice>() {
        public PresenceDevice createFromParcel(Parcel parcel) {
            Parcel parcel2 = parcel;
            String str = null;
            String readString = parcel.readInt() == 1 ? parcel.readString() : null;
            int readInt = parcel.readInt();
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < readInt; i++) {
                arrayList.add(Integer.valueOf(parcel.readInt()));
            }
            int readInt2 = parcel.readInt();
            byte[] bArr = new byte[parcel.readInt()];
            parcel2.readByteArray(bArr);
            byte[] bArr2 = new byte[parcel.readInt()];
            parcel2.readByteArray(bArr2);
            byte[] bArr3 = new byte[parcel.readInt()];
            parcel2.readByteArray(bArr3);
            String readString2 = parcel.readString();
            int readInt3 = parcel.readInt();
            if (parcel.readInt() == 1) {
                str = parcel.readString();
            }
            long readLong = parcel.readLong();
            ArrayList arrayList2 = new ArrayList();
            int i2 = 0;
            for (int readInt4 = parcel.readInt(); i2 < readInt4; readInt4 = readInt4) {
                arrayList2.add((DataElement) parcel2.readParcelable(DataElement.class.getClassLoader(), DataElement.class));
                i2++;
            }
            Builder discoveryTimestampMillis = new Builder(readString2, bArr, bArr2, bArr3).setName(readString).setRssi(readInt2).setDeviceType(readInt3).setDeviceImageUrl(str).setDiscoveryTimestampMillis(readLong);
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                discoveryTimestampMillis.addMedium(((Integer) arrayList.get(i3)).intValue());
            }
            for (int i4 = 0; i4 < arrayList2.size(); i4++) {
                discoveryTimestampMillis.addExtendedProperty((DataElement) arrayList2.get(i4));
            }
            return discoveryTimestampMillis.build();
        }

        public PresenceDevice[] newArray(int i) {
            return new PresenceDevice[i];
        }
    };
    private final String mDeviceId;
    private final String mDeviceImageUrl;
    private final int mDeviceType;
    private final long mDiscoveryTimestampMillis;
    private final byte[] mEncryptedIdentity;
    private final List<DataElement> mExtendedProperties;
    private final byte[] mSalt;
    private final byte[] mSecretId;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DeviceType {
        public static final int DISPLAY = 3;
        public static final int LAPTOP = 4;
        public static final int PHONE = 1;
        public static final int TABLET = 2;

        /* renamed from: TV */
        public static final int f38TV = 5;
        public static final int UNKNOWN = 0;
        public static final int WATCH = 6;
    }

    public int describeContents() {
        return 0;
    }

    public String getDeviceId() {
        return this.mDeviceId;
    }

    public byte[] getSalt() {
        return this.mSalt;
    }

    public byte[] getSecretId() {
        return this.mSecretId;
    }

    public byte[] getEncryptedIdentity() {
        return this.mEncryptedIdentity;
    }

    public int getDeviceType() {
        return this.mDeviceType;
    }

    public String getDeviceImageUrl() {
        return this.mDeviceImageUrl;
    }

    public long getDiscoveryTimestampMillis() {
        return this.mDiscoveryTimestampMillis;
    }

    public List<DataElement> getExtendedProperties() {
        return this.mExtendedProperties;
    }

    private PresenceDevice(String str, List<Integer> list, int i, String str2, byte[] bArr, byte[] bArr2, byte[] bArr3, int i2, String str3, long j, List<DataElement> list2) {
        super(str, list, i);
        this.mDeviceId = str2;
        this.mSalt = bArr;
        this.mSecretId = bArr2;
        this.mEncryptedIdentity = bArr3;
        this.mDeviceType = i2;
        this.mDeviceImageUrl = str3;
        this.mDiscoveryTimestampMillis = j;
        this.mExtendedProperties = list2;
    }

    public void writeToParcel(Parcel parcel, int i) {
        String name = getName();
        int i2 = 1;
        parcel.writeInt(name == null ? 0 : 1);
        if (name != null) {
            parcel.writeString(name);
        }
        List<Integer> mediums = getMediums();
        parcel.writeInt(mediums.size());
        for (Integer intValue : mediums) {
            parcel.writeInt(intValue.intValue());
        }
        parcel.writeInt(getRssi());
        parcel.writeInt(this.mSalt.length);
        parcel.writeByteArray(this.mSalt);
        parcel.writeInt(this.mSecretId.length);
        parcel.writeByteArray(this.mSecretId);
        parcel.writeInt(this.mEncryptedIdentity.length);
        parcel.writeByteArray(this.mEncryptedIdentity);
        parcel.writeString(this.mDeviceId);
        parcel.writeInt(this.mDeviceType);
        if (this.mDeviceImageUrl == null) {
            i2 = 0;
        }
        parcel.writeInt(i2);
        String str = this.mDeviceImageUrl;
        if (str != null) {
            parcel.writeString(str);
        }
        parcel.writeLong(this.mDiscoveryTimestampMillis);
        parcel.writeInt(this.mExtendedProperties.size());
        for (DataElement writeParcelable : this.mExtendedProperties) {
            parcel.writeParcelable(writeParcelable, 0);
        }
    }

    public static final class Builder {
        private final String mDeviceId;
        private String mDeviceImageUrl;
        private int mDeviceType;
        private long mDiscoveryTimestampMillis;
        private final byte[] mEncryptedIdentity;
        private final List<DataElement> mExtendedProperties = new ArrayList();
        private final List<Integer> mMediums = new ArrayList();
        private String mName;
        private int mRssi = -127;
        private final byte[] mSalt;
        private final byte[] mSecretId;

        public Builder(String str, byte[] bArr, byte[] bArr2, byte[] bArr3) {
            Objects.requireNonNull(str);
            Objects.requireNonNull(bArr);
            Objects.requireNonNull(bArr2);
            Objects.requireNonNull(bArr3);
            this.mDeviceId = str;
            this.mSalt = bArr;
            this.mSecretId = bArr2;
            this.mEncryptedIdentity = bArr3;
        }

        public Builder setName(String str) {
            this.mName = str;
            return this;
        }

        public Builder addMedium(int i) {
            this.mMediums.add(Integer.valueOf(i));
            return this;
        }

        public Builder setRssi(int i) {
            this.mRssi = i;
            return this;
        }

        public Builder setDeviceType(int i) {
            this.mDeviceType = i;
            return this;
        }

        public Builder setDeviceImageUrl(String str) {
            this.mDeviceImageUrl = str;
            return this;
        }

        public Builder setDiscoveryTimestampMillis(long j) {
            this.mDiscoveryTimestampMillis = j;
            return this;
        }

        public Builder addExtendedProperty(DataElement dataElement) {
            Objects.requireNonNull(dataElement);
            this.mExtendedProperties.add(dataElement);
            return this;
        }

        public PresenceDevice build() {
            return new PresenceDevice(this.mName, this.mMediums, this.mRssi, this.mDeviceId, this.mSalt, this.mSecretId, this.mEncryptedIdentity, this.mDeviceType, this.mDeviceImageUrl, this.mDiscoveryTimestampMillis, this.mExtendedProperties);
        }
    }
}

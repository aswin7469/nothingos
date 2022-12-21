package android.nearby;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.internal.util.Preconditions;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SystemApi
public final class PresenceBroadcastRequest extends BroadcastRequest implements Parcelable {
    public static final Parcelable.Creator<PresenceBroadcastRequest> CREATOR = new Parcelable.Creator<PresenceBroadcastRequest>() {
        public PresenceBroadcastRequest createFromParcel(Parcel parcel) {
            parcel.readInt();
            return PresenceBroadcastRequest.createFromParcelBody(parcel);
        }

        public PresenceBroadcastRequest[] newArray(int i) {
            return new PresenceBroadcastRequest[i];
        }
    };
    private final List<Integer> mActions;
    private final PrivateCredential mCredential;
    private final List<DataElement> mExtendedProperties;
    private final byte[] mSalt;

    public int describeContents() {
        return 0;
    }

    private PresenceBroadcastRequest(int i, int i2, List<Integer> list, byte[] bArr, List<Integer> list2, PrivateCredential privateCredential, List<DataElement> list3) {
        super(3, i, i2, list);
        this.mSalt = bArr;
        this.mActions = list2;
        this.mCredential = privateCredential;
        this.mExtendedProperties = list3;
    }

    private PresenceBroadcastRequest(Parcel parcel) {
        super(3, parcel);
        byte[] bArr = new byte[parcel.readInt()];
        this.mSalt = bArr;
        parcel.readByteArray(bArr);
        ArrayList arrayList = new ArrayList();
        this.mActions = arrayList;
        parcel.readList(arrayList, Integer.class.getClassLoader(), Integer.class);
        this.mCredential = (PrivateCredential) parcel.readParcelable(PrivateCredential.class.getClassLoader(), PrivateCredential.class);
        ArrayList arrayList2 = new ArrayList();
        this.mExtendedProperties = arrayList2;
        parcel.readList(arrayList2, DataElement.class.getClassLoader(), DataElement.class);
    }

    static PresenceBroadcastRequest createFromParcelBody(Parcel parcel) {
        return new PresenceBroadcastRequest(parcel);
    }

    public byte[] getSalt() {
        return this.mSalt;
    }

    public List<Integer> getActions() {
        return this.mActions;
    }

    public PrivateCredential getCredential() {
        return this.mCredential;
    }

    public List<DataElement> getExtendedProperties() {
        return this.mExtendedProperties;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.mSalt.length);
        parcel.writeByteArray(this.mSalt);
        parcel.writeList(this.mActions);
        parcel.writeParcelable(this.mCredential, 0);
        parcel.writeList(this.mExtendedProperties);
    }

    public static final class Builder {
        private final List<Integer> mActions;
        private final PrivateCredential mCredential;
        private final List<DataElement> mExtendedProperties;
        private final List<Integer> mMediums;
        private final byte[] mSalt;
        private int mTxPower;
        private int mVersion;

        public Builder(List<Integer> list, byte[] bArr, PrivateCredential privateCredential) {
            boolean z = true;
            Preconditions.checkState(!list.isEmpty(), "mediums cannot be empty");
            Preconditions.checkState((bArr == null || bArr.length <= 0) ? false : z, "salt cannot be empty");
            this.mVersion = 0;
            this.mTxPower = -127;
            this.mCredential = privateCredential;
            this.mActions = new ArrayList();
            this.mExtendedProperties = new ArrayList();
            this.mSalt = bArr;
            this.mMediums = list;
        }

        public Builder setVersion(int i) {
            this.mVersion = i;
            return this;
        }

        public Builder setTxPower(int i) {
            this.mTxPower = i;
            return this;
        }

        public Builder addAction(int i) {
            this.mActions.add(Integer.valueOf(i));
            return this;
        }

        public Builder addExtendedProperty(DataElement dataElement) {
            Objects.requireNonNull(dataElement);
            this.mExtendedProperties.add(dataElement);
            return this;
        }

        public PresenceBroadcastRequest build() {
            return new PresenceBroadcastRequest(this.mVersion, this.mTxPower, this.mMediums, this.mSalt, this.mActions, this.mCredential, this.mExtendedProperties);
        }
    }
}

package android.nearby;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.internal.util.Preconditions;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArraySet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@SystemApi
public final class PresenceScanFilter extends ScanFilter implements Parcelable {
    public static final Parcelable.Creator<PresenceScanFilter> CREATOR = new Parcelable.Creator<PresenceScanFilter>() {
        public PresenceScanFilter createFromParcel(Parcel parcel) {
            parcel.readInt();
            return PresenceScanFilter.createFromParcelBody(parcel);
        }

        public PresenceScanFilter[] newArray(int i) {
            return new PresenceScanFilter[i];
        }
    };
    private final List<PublicCredential> mCredentials;
    private final List<DataElement> mExtendedProperties;
    private final List<Integer> mPresenceActions;

    public int describeContents() {
        return 0;
    }

    public List<PublicCredential> getCredentials() {
        return this.mCredentials;
    }

    public List<Integer> getPresenceActions() {
        return this.mPresenceActions;
    }

    public List<DataElement> getExtendedProperties() {
        return this.mExtendedProperties;
    }

    private PresenceScanFilter(int i, List<PublicCredential> list, List<Integer> list2, List<DataElement> list3) {
        super(2, i);
        this.mCredentials = new ArrayList(list);
        this.mPresenceActions = new ArrayList(list2);
        this.mExtendedProperties = list3;
    }

    private PresenceScanFilter(Parcel parcel) {
        super(2, parcel);
        ArrayList arrayList = new ArrayList();
        this.mCredentials = arrayList;
        if (parcel.readInt() != 0) {
            parcel.readParcelableList(arrayList, PublicCredential.class.getClassLoader(), PublicCredential.class);
        }
        ArrayList arrayList2 = new ArrayList();
        this.mPresenceActions = arrayList2;
        if (parcel.readInt() != 0) {
            parcel.readList(arrayList2, Integer.class.getClassLoader(), Integer.class);
        }
        ArrayList arrayList3 = new ArrayList();
        this.mExtendedProperties = arrayList3;
        if (parcel.readInt() != 0) {
            parcel.readParcelableList(arrayList3, DataElement.class.getClassLoader(), DataElement.class);
        }
    }

    static PresenceScanFilter createFromParcelBody(Parcel parcel) {
        return new PresenceScanFilter(parcel);
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeInt(this.mCredentials.size());
        if (!this.mCredentials.isEmpty()) {
            parcel.writeParcelableList(this.mCredentials, 0);
        }
        parcel.writeInt(this.mPresenceActions.size());
        if (!this.mPresenceActions.isEmpty()) {
            parcel.writeList(this.mPresenceActions);
        }
        parcel.writeInt(this.mExtendedProperties.size());
        if (!this.mExtendedProperties.isEmpty()) {
            parcel.writeList(this.mExtendedProperties);
        }
    }

    public static final class Builder {
        private final Set<PublicCredential> mCredentials = new ArraySet();
        private final List<DataElement> mExtendedProperties = new ArrayList();
        private int mMaxPathLoss = 127;
        private final Set<Integer> mPresenceActions = new ArraySet();
        private final Set<Integer> mPresenceIdentities = new ArraySet();

        public Builder setMaxPathLoss(int i) {
            this.mMaxPathLoss = i;
            return this;
        }

        public Builder addCredential(PublicCredential publicCredential) {
            Objects.requireNonNull(publicCredential);
            this.mCredentials.add(publicCredential);
            return this;
        }

        public Builder addPresenceAction(int i) {
            this.mPresenceActions.add(Integer.valueOf(i));
            return this;
        }

        public Builder addExtendedProperty(DataElement dataElement) {
            Objects.requireNonNull(dataElement);
            this.mExtendedProperties.add(dataElement);
            return this;
        }

        public PresenceScanFilter build() {
            Preconditions.checkState(!this.mCredentials.isEmpty(), "credentials cannot be empty");
            return new PresenceScanFilter(this.mMaxPathLoss, new ArrayList(this.mCredentials), new ArrayList(this.mPresenceActions), this.mExtendedProperties);
        }
    }
}

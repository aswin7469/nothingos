package android.bluetooth;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class DeviceGroup implements Parcelable {
    public static final Parcelable.Creator<DeviceGroup> CREATOR = new Parcelable.Creator<DeviceGroup>() { // from class: android.bluetooth.DeviceGroup.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public DeviceGroup mo3559createFromParcel(Parcel in) {
            return new DeviceGroup(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public DeviceGroup[] mo3560newArray(int size) {
            return new DeviceGroup[size];
        }
    };
    private final boolean mExclusiveAccessSupport;
    private List<BluetoothDevice> mGroupDevices;
    private int mGroupId;
    private final ParcelUuid mIncludingSrvcUUID;
    private int mSize;

    public DeviceGroup(int groupId, int size, List<BluetoothDevice> groupDevices, ParcelUuid includingSrvcUUID, boolean exclusiveAccessSupport) {
        this.mGroupDevices = new ArrayList();
        this.mGroupId = groupId;
        this.mSize = size;
        this.mGroupDevices = groupDevices;
        this.mIncludingSrvcUUID = includingSrvcUUID;
        this.mExclusiveAccessSupport = exclusiveAccessSupport;
    }

    public DeviceGroup(Parcel in) {
        this.mGroupDevices = new ArrayList();
        this.mGroupId = in.readInt();
        this.mSize = in.readInt();
        in.readList(this.mGroupDevices, BluetoothDevice.class.getClassLoader());
        this.mIncludingSrvcUUID = (ParcelUuid) in.readParcelable(ParcelUuid.class.getClassLoader());
        this.mExclusiveAccessSupport = in.readBoolean();
    }

    public int getDeviceGroupId() {
        return this.mGroupId;
    }

    public int getDeviceGroupSize() {
        return this.mSize;
    }

    public int getTotalDiscoveredGroupDevices() {
        return this.mGroupDevices.size();
    }

    public List<BluetoothDevice> getDeviceGroupMembers() {
        return this.mGroupDevices;
    }

    public ParcelUuid getIncludingServiceUUID() {
        return this.mIncludingSrvcUUID;
    }

    public boolean isExclusiveAccessSupported() {
        return this.mExclusiveAccessSupport;
    }

    public boolean isGroupDiscoveredCompleted() {
        return this.mSize == getTotalDiscoveredGroupDevices();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mGroupId);
        dest.writeInt(this.mSize);
        dest.writeList(this.mGroupDevices);
        dest.writeParcelable(this.mIncludingSrvcUUID, 0);
        dest.writeBoolean(this.mExclusiveAccessSupport);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}

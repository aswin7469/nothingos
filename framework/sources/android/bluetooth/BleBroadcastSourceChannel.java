package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BleBroadcastSourceChannel implements Parcelable {
    public static final Parcelable.Creator<BleBroadcastSourceChannel> CREATOR = new Parcelable.Creator<BleBroadcastSourceChannel>() { // from class: android.bluetooth.BleBroadcastSourceChannel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public BleBroadcastSourceChannel mo3559createFromParcel(Parcel in) {
            byte[] metadata;
            BleBroadcastSourceChannel.log(BleBroadcastSourceChannel.TAG, "createFromParcel>");
            int index = in.readInt();
            String desc = in.readString();
            boolean status = in.readBoolean();
            int subGroupId = in.readInt();
            int metadataLength = in.readInt();
            if (metadataLength <= 0) {
                metadata = null;
            } else {
                byte[] metadata2 = new byte[metadataLength];
                in.readByteArray(metadata2);
                metadata = metadata2;
            }
            BleBroadcastSourceChannel srcChannel = new BleBroadcastSourceChannel(index, desc, status, subGroupId, metadata);
            BleBroadcastSourceChannel.log(BleBroadcastSourceChannel.TAG, "createFromParcel:" + srcChannel);
            return srcChannel;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public BleBroadcastSourceChannel[] mo3560newArray(int size) {
            return new BleBroadcastSourceChannel[size];
        }
    };
    private static final String TAG = "BleBroadcastSourceChannel";
    private String mDescription;
    private int mIndex;
    private byte[] mMetadata;
    private boolean mStatus;
    private int mSubGroupId;

    public BleBroadcastSourceChannel(int index, String description, boolean st, int aSubGroupId, byte[] aMetadata) {
        this.mIndex = index;
        this.mDescription = description;
        this.mStatus = st;
        this.mSubGroupId = aSubGroupId;
        if (aMetadata != null && aMetadata.length != 0) {
            byte[] bArr = new byte[aMetadata.length];
            this.mMetadata = bArr;
            System.arraycopy(aMetadata, 0, bArr, 0, aMetadata.length);
        }
    }

    public boolean equals(Object o) {
        if (o instanceof BleBroadcastSourceChannel) {
            BleBroadcastSourceChannel other = (BleBroadcastSourceChannel) o;
            return other.mIndex == this.mIndex && other.mDescription == this.mDescription && other.mStatus == this.mStatus;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mIndex), this.mDescription, Boolean.valueOf(this.mStatus));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return this.mDescription;
    }

    public int getIndex() {
        return this.mIndex;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public boolean getStatus() {
        return this.mStatus;
    }

    public byte[] getMetadata() {
        return this.mMetadata;
    }

    public int getSubGroupId() {
        return this.mSubGroupId;
    }

    public void setStatus(boolean status) {
        this.mStatus = status;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        log(TAG, "writeToParcel>");
        out.writeInt(this.mIndex);
        out.writeString(this.mDescription);
        out.writeBoolean(this.mStatus);
        out.writeInt(this.mSubGroupId);
        byte[] bArr = this.mMetadata;
        if (bArr != null) {
            out.writeInt(bArr.length);
            out.writeByteArray(this.mMetadata);
        } else {
            out.writeInt(0);
        }
        log(TAG, "writeToParcel:" + toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String TAG2, String msg) {
        BleBroadcastSourceInfo.BASS_Debug(TAG2, msg);
    }
}

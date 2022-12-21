package androidx.slice;

import androidx.slice.SliceItemHolder;
import androidx.versionedparcelable.VersionedParcel;

public final class SliceItemHolderParcelizer {
    private static SliceItemHolder.SliceItemPool sBuilder = new SliceItemHolder.SliceItemPool();

    public static SliceItemHolder read(VersionedParcel versionedParcel) {
        SliceItemHolder sliceItemHolder = sBuilder.get();
        sliceItemHolder.mVersionedParcelable = versionedParcel.readVersionedParcelable(sliceItemHolder.mVersionedParcelable, 1);
        sliceItemHolder.mParcelable = versionedParcel.readParcelable(sliceItemHolder.mParcelable, 2);
        sliceItemHolder.mStr = versionedParcel.readString(sliceItemHolder.mStr, 3);
        sliceItemHolder.mInt = versionedParcel.readInt(sliceItemHolder.mInt, 4);
        sliceItemHolder.mLong = versionedParcel.readLong(sliceItemHolder.mLong, 5);
        sliceItemHolder.mBundle = versionedParcel.readBundle(sliceItemHolder.mBundle, 6);
        return sliceItemHolder;
    }

    public static void write(SliceItemHolder sliceItemHolder, VersionedParcel versionedParcel) {
        versionedParcel.setSerializationFlags(true, true);
        if (sliceItemHolder.mVersionedParcelable != null) {
            versionedParcel.writeVersionedParcelable(sliceItemHolder.mVersionedParcelable, 1);
        }
        if (sliceItemHolder.mParcelable != null) {
            versionedParcel.writeParcelable(sliceItemHolder.mParcelable, 2);
        }
        if (sliceItemHolder.mStr != null) {
            versionedParcel.writeString(sliceItemHolder.mStr, 3);
        }
        if (sliceItemHolder.mInt != 0) {
            versionedParcel.writeInt(sliceItemHolder.mInt, 4);
        }
        if (0 != sliceItemHolder.mLong) {
            versionedParcel.writeLong(sliceItemHolder.mLong, 5);
        }
        if (sliceItemHolder.mBundle != null) {
            versionedParcel.writeBundle(sliceItemHolder.mBundle, 6);
        }
    }
}

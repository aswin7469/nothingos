package androidx.slice;

import androidx.versionedparcelable.VersionedParcel;
import java.util.Arrays;

public final class SliceItemParcelizer {
    public static SliceItem read(VersionedParcel versionedParcel) {
        SliceItem sliceItem = new SliceItem();
        sliceItem.mHints = (String[]) versionedParcel.readArray(sliceItem.mHints, 1);
        sliceItem.mFormat = versionedParcel.readString(sliceItem.mFormat, 2);
        sliceItem.mSubType = versionedParcel.readString(sliceItem.mSubType, 3);
        sliceItem.mHolder = (SliceItemHolder) versionedParcel.readVersionedParcelable(sliceItem.mHolder, 4);
        sliceItem.onPostParceling();
        return sliceItem;
    }

    public static void write(SliceItem sliceItem, VersionedParcel versionedParcel) {
        versionedParcel.setSerializationFlags(true, true);
        sliceItem.onPreParceling(versionedParcel.isStream());
        if (!Arrays.equals((Object[]) Slice.NO_HINTS, (Object[]) sliceItem.mHints)) {
            versionedParcel.writeArray(sliceItem.mHints, 1);
        }
        if (!"text".equals(sliceItem.mFormat)) {
            versionedParcel.writeString(sliceItem.mFormat, 2);
        }
        if (sliceItem.mSubType != null) {
            versionedParcel.writeString(sliceItem.mSubType, 3);
        }
        versionedParcel.writeVersionedParcelable(sliceItem.mHolder, 4);
    }
}

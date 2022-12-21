package androidx.slice;

import androidx.versionedparcelable.VersionedParcel;
import java.util.Arrays;

public final class SliceParcelizer {
    public static Slice read(VersionedParcel versionedParcel) {
        Slice slice = new Slice();
        slice.mSpec = (SliceSpec) versionedParcel.readVersionedParcelable(slice.mSpec, 1);
        slice.mItems = (SliceItem[]) versionedParcel.readArray(slice.mItems, 2);
        slice.mHints = (String[]) versionedParcel.readArray(slice.mHints, 3);
        slice.mUri = versionedParcel.readString(slice.mUri, 4);
        slice.onPostParceling();
        return slice;
    }

    public static void write(Slice slice, VersionedParcel versionedParcel) {
        versionedParcel.setSerializationFlags(true, false);
        slice.onPreParceling(versionedParcel.isStream());
        if (slice.mSpec != null) {
            versionedParcel.writeVersionedParcelable(slice.mSpec, 1);
        }
        if (!Arrays.equals((Object[]) Slice.NO_ITEMS, (Object[]) slice.mItems)) {
            versionedParcel.writeArray(slice.mItems, 2);
        }
        if (!Arrays.equals((Object[]) Slice.NO_HINTS, (Object[]) slice.mHints)) {
            versionedParcel.writeArray(slice.mHints, 3);
        }
        if (slice.mUri != null) {
            versionedParcel.writeString(slice.mUri, 4);
        }
    }
}

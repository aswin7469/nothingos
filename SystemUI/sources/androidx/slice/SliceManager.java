package androidx.slice;

import android.content.Context;
import android.net.Uri;
import java.util.List;
import java.util.Set;

public abstract class SliceManager {
    public abstract int checkSlicePermission(Uri uri, int i, int i2);

    public abstract List<Uri> getPinnedSlices();

    public abstract Set<SliceSpec> getPinnedSpecs(Uri uri);

    public abstract void grantSlicePermission(String str, Uri uri);

    public abstract void revokeSlicePermission(String str, Uri uri);

    public static SliceManager getInstance(Context context) {
        return new SliceManagerWrapper(context);
    }

    SliceManager() {
    }
}

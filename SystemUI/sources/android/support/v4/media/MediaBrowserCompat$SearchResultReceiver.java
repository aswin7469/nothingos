package android.support.v4.media;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.ResultReceiver;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
class MediaBrowserCompat$SearchResultReceiver extends ResultReceiver {
    @Override // android.support.v4.os.ResultReceiver
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultData != null) {
            resultData = MediaSessionCompat.unparcelWithClassLoader(resultData);
        }
        if (resultCode != 0 || resultData == null || !resultData.containsKey("search_results")) {
            throw null;
        }
        Parcelable[] parcelableArray = resultData.getParcelableArray("search_results");
        Objects.requireNonNull(parcelableArray);
        ArrayList arrayList = new ArrayList();
        for (Parcelable parcelable : parcelableArray) {
            arrayList.add((MediaBrowserCompat$MediaItem) parcelable);
        }
        throw null;
    }
}

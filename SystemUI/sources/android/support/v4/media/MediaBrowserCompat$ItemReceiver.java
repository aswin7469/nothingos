package android.support.v4.media;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.ResultReceiver;
/* loaded from: classes.dex */
class MediaBrowserCompat$ItemReceiver extends ResultReceiver {
    @Override // android.support.v4.os.ResultReceiver
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultData != null) {
            resultData = MediaSessionCompat.unparcelWithClassLoader(resultData);
        }
        if (resultCode != 0 || resultData == null || !resultData.containsKey("media_item")) {
            throw null;
        }
        Parcelable parcelable = resultData.getParcelable("media_item");
        if (parcelable == null || (parcelable instanceof MediaBrowserCompat$MediaItem)) {
            MediaBrowserCompat$MediaItem mediaBrowserCompat$MediaItem = (MediaBrowserCompat$MediaItem) parcelable;
            throw null;
        }
        throw null;
    }
}

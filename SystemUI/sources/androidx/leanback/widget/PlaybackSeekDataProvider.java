package androidx.leanback.widget;

import android.graphics.Bitmap;

public class PlaybackSeekDataProvider {

    public static class ResultCallback {
        public void onThumbnailLoaded(Bitmap bitmap, int i) {
        }
    }

    public long[] getSeekPositions() {
        return null;
    }

    public void getThumbnail(int i, ResultCallback resultCallback) {
    }

    public void reset() {
    }
}

package androidx.slice.widget;

import android.content.Context;
import android.net.Uri;

class SliceMetrics {
    /* access modifiers changed from: protected */
    public void logHidden() {
    }

    /* access modifiers changed from: protected */
    public void logTouch(int i, Uri uri) {
    }

    /* access modifiers changed from: protected */
    public void logVisible() {
    }

    SliceMetrics() {
    }

    public static SliceMetrics getInstance(Context context, Uri uri) {
        return new SliceMetricsWrapper(context, uri);
    }
}

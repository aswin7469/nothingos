package androidx.slice.widget;

import android.content.Context;
import android.net.Uri;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class SliceMetricsWrapper extends SliceMetrics {
    private final android.app.slice.SliceMetrics mSliceMetrics;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SliceMetricsWrapper(Context context, Uri uri) {
        this.mSliceMetrics = new android.app.slice.SliceMetrics(context, uri);
    }

    @Override // androidx.slice.widget.SliceMetrics
    protected void logVisible() {
        this.mSliceMetrics.logVisible();
    }

    @Override // androidx.slice.widget.SliceMetrics
    protected void logHidden() {
        this.mSliceMetrics.logHidden();
    }

    @Override // androidx.slice.widget.SliceMetrics
    protected void logTouch(int actionType, Uri subSlice) {
        this.mSliceMetrics.logTouch(actionType, subSlice);
    }
}

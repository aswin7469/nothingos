package com.android.settings.panel;

import android.net.Uri;
import androidx.slice.widget.SliceLiveData;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PanelFragment$$ExternalSyntheticLambda4 implements SliceLiveData.OnErrorListener {
    public final /* synthetic */ PanelFragment f$0;
    public final /* synthetic */ Uri f$1;

    public /* synthetic */ PanelFragment$$ExternalSyntheticLambda4(PanelFragment panelFragment, Uri uri) {
        this.f$0 = panelFragment;
        this.f$1 = uri;
    }

    public final void onSliceError(int i, Throwable th) {
        this.f$0.lambda$loadAllSlices$1(this.f$1, i, th);
    }
}

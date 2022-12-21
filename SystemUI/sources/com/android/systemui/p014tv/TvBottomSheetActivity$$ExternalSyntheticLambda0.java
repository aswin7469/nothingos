package com.android.systemui.p014tv;

import android.graphics.Rect;
import android.view.View;
import java.util.Collections;

/* renamed from: com.android.systemui.tv.TvBottomSheetActivity$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TvBottomSheetActivity$$ExternalSyntheticLambda0 implements View.OnLayoutChangeListener {
    public final /* synthetic */ View f$0;

    public /* synthetic */ TvBottomSheetActivity$$ExternalSyntheticLambda0(View view) {
        this.f$0 = view;
    }

    public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.f$0.setUnrestrictedPreferKeepClearRects(Collections.singletonList(new Rect(0, 0, i3 - i, i4 - i2)));
    }
}

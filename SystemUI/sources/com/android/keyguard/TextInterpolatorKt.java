package com.android.keyguard;

import android.text.Layout;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0002¨\u0006\u0005"}, mo64987d2 = {"getDrawOrigin", "", "Landroid/text/Layout;", "lineNo", "", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: TextInterpolator.kt */
public final class TextInterpolatorKt {
    /* access modifiers changed from: private */
    public static final float getDrawOrigin(Layout layout, int i) {
        if (layout.getParagraphDirection(i) == 1) {
            return layout.getLineLeft(i);
        }
        return layout.getLineRight(i);
    }
}

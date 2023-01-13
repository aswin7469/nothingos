package com.android.keyguard;

import android.text.Layout;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0002¨\u0006\u0005"}, mo65043d2 = {"getDrawOrigin", "", "Landroid/text/Layout;", "lineNo", "", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
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

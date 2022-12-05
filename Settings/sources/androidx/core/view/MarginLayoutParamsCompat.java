package androidx.core.view;

import android.os.Build;
import android.view.ViewGroup;
/* loaded from: classes.dex */
public final class MarginLayoutParamsCompat {
    public static int getMarginStart(ViewGroup.MarginLayoutParams lp) {
        if (Build.VERSION.SDK_INT >= 17) {
            return lp.getMarginStart();
        }
        return lp.leftMargin;
    }

    public static int getMarginEnd(ViewGroup.MarginLayoutParams lp) {
        if (Build.VERSION.SDK_INT >= 17) {
            return lp.getMarginEnd();
        }
        return lp.rightMargin;
    }

    public static void setMarginStart(ViewGroup.MarginLayoutParams lp, int marginStart) {
        if (Build.VERSION.SDK_INT >= 17) {
            lp.setMarginStart(marginStart);
        } else {
            lp.leftMargin = marginStart;
        }
    }

    public static void setMarginEnd(ViewGroup.MarginLayoutParams lp, int marginEnd) {
        if (Build.VERSION.SDK_INT >= 17) {
            lp.setMarginEnd(marginEnd);
        } else {
            lp.rightMargin = marginEnd;
        }
    }
}

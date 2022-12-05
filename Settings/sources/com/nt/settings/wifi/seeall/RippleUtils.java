package com.nt.settings.wifi.seeall;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.RippleDrawable;
import android.util.StateSet;
import android.view.View;
/* loaded from: classes2.dex */
public class RippleUtils {
    private static RippleUtils sRippleUtils;
    private ColorStateList mRippleColor;

    private RippleUtils() {
    }

    public static RippleUtils getInstance() {
        if (sRippleUtils == null) {
            sRippleUtils = new RippleUtils();
        }
        return sRippleUtils;
    }

    public void setRippleColor(int i) {
        this.mRippleColor = ColorStateList.valueOf(i);
    }

    public void setRippleEffect(View view) {
        setRippleEffect(view, this.mRippleColor);
    }

    public void setRippleEffect(View view, ColorStateList colorStateList) {
        if (colorStateList == null) {
            return;
        }
        view.setBackground(new RippleDrawable(new ColorStateList(new int[][]{new int[]{16842919}, StateSet.NOTHING}, new int[]{convertRgbToArgb(colorStateList.getDefaultColor(), 10.0f), 0}), view.getBackground(), null));
    }

    private int convertRgbToArgb(int i, float f) {
        return Color.argb((int) (f * 255.0f), Color.red(i), Color.green(i), Color.blue(i));
    }
}

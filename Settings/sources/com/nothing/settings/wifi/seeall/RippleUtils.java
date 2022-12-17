package com.nothing.settings.wifi.seeall;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.StateSet;
import android.view.View;

public class RippleUtils {
    private static RippleUtils mRippleUtils;
    private ColorStateList mRippleColor;
    private int[] pressedState = {16842919};

    private RippleUtils() {
    }

    public static RippleUtils getInstance() {
        if (mRippleUtils == null) {
            mRippleUtils = new RippleUtils();
        }
        return mRippleUtils;
    }

    public void setRippleColor(int i) {
        this.mRippleColor = ColorStateList.valueOf(i);
    }

    public void setRippleEffect(View view) {
        setRippleEffect(view, this.mRippleColor);
    }

    public void setRippleEffect(View view, ColorStateList colorStateList) {
        if (colorStateList != null) {
            view.setBackground(new RippleDrawable(new ColorStateList(new int[][]{this.pressedState, StateSet.NOTHING}, new int[]{convertRgbToArgb(colorStateList.getDefaultColor(), 10.0f), 0}), view.getBackground(), (Drawable) null));
        }
    }

    private int convertRgbToArgb(int i, float f) {
        return Color.argb((int) (f * 255.0f), Color.red(i), Color.green(i), Color.blue(i));
    }
}

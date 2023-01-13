package com.nothing.systemui.p024qs.tiles.settings.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import com.nothing.systemui.util.NTLogUtil;

/* renamed from: com.nothing.systemui.qs.tiles.settings.util.AnimUtils */
public class AnimUtils {
    private static final int EXPAND_DURATION = 300;
    private static final String TAG = "AnimUtils";

    public static Animation expand(final View view) {
        view.setVisibility(0);
        view.measure(-1, -2);
        final int measuredHeight = view.getMeasuredHeight();
        NTLogUtil.m1686d(TAG, "expand height = " + view.getHeight() + ", viewHeight:" + measuredHeight + ", bottom:" + view.getPaddingBottom());
        view.getLayoutParams().height = 0;
        C42311 r0 = new Animation() {
            /* access modifiers changed from: protected */
            public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
                return super.clone();
            }

            public void applyTransformation(float f, Transformation transformation) {
                if (f >= 1.0f) {
                    view.getLayoutParams().height = -2;
                } else {
                    view.getLayoutParams().height = (int) (((float) measuredHeight) * f);
                    view.setAlpha(f);
                }
                view.requestLayout();
            }
        };
        r0.setDuration(300);
        r0.setInterpolator(new LinearOutSlowInInterpolator());
        view.startAnimation(r0);
        return r0;
    }

    public static Animation collapse(final View view) {
        view.measure(-1, -2);
        final int measuredHeight = view.getMeasuredHeight();
        NTLogUtil.m1686d(TAG, "collapse height = " + view.getHeight() + ", viewHeight:" + measuredHeight + ", bottom:" + view.getPaddingBottom());
        view.getLayoutParams().height = measuredHeight;
        C42322 r1 = new Animation() {
            /* access modifiers changed from: protected */
            public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
                return super.clone();
            }

            public void applyTransformation(float f, Transformation transformation) {
                if (f >= 1.0f) {
                    view.getLayoutParams().height = -2;
                    view.setVisibility(8);
                } else {
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    int i = measuredHeight;
                    layoutParams.height = i - ((int) (((float) i) * f));
                    view.setAlpha(1.0f - f);
                }
                view.requestLayout();
            }
        };
        r1.setDuration(300);
        r1.setInterpolator(new FastOutLinearInInterpolator());
        view.startAnimation(r1);
        return r1;
    }
}

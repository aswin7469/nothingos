package com.google.android.material.transition.platform;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.RectF;
import android.transition.Transition;
import android.util.TypedValue;
import androidx.core.graphics.PathParser;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.google.android.material.resources.MaterialAttributes;
/* loaded from: classes2.dex */
class TransitionUtils {
    private static final RectF transformAlphaRectF = new RectF();

    static float lerp(float f, float f2, float f3) {
        return f + (f3 * (f2 - f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean maybeApplyThemeInterpolator(Transition transition, Context context, int i, TimeInterpolator timeInterpolator) {
        if (i == 0 || transition.getInterpolator() != null) {
            return false;
        }
        transition.setInterpolator(resolveThemeInterpolator(context, i, timeInterpolator));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean maybeApplyThemeDuration(Transition transition, Context context, int i) {
        int resolveInteger;
        if (i == 0 || transition.getDuration() != -1 || (resolveInteger = MaterialAttributes.resolveInteger(context, i, -1)) == -1) {
            return false;
        }
        transition.setDuration(resolveInteger);
        return true;
    }

    static TimeInterpolator resolveThemeInterpolator(Context context, int i, TimeInterpolator timeInterpolator) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(i, typedValue, true)) {
            if (typedValue.type != 3) {
                throw new IllegalArgumentException("Motion easing theme attribute must be a string");
            }
            String valueOf = String.valueOf(typedValue.string);
            if (isEasingType(valueOf, "cubic-bezier")) {
                String[] split = getEasingContent(valueOf, "cubic-bezier").split(",");
                if (split.length != 4) {
                    throw new IllegalArgumentException("Motion easing theme attribute must have 4 control points if using bezier curve format; instead got: " + split.length);
                }
                return PathInterpolatorCompat.create(getControlPoint(split, 0), getControlPoint(split, 1), getControlPoint(split, 2), getControlPoint(split, 3));
            } else if (isEasingType(valueOf, "path")) {
                return PathInterpolatorCompat.create(PathParser.createPathFromPathData(getEasingContent(valueOf, "path")));
            } else {
                throw new IllegalArgumentException("Invalid motion easing type: " + valueOf);
            }
        }
        return timeInterpolator;
    }

    private static boolean isEasingType(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append("(");
        return str.startsWith(sb.toString()) && str.endsWith(")");
    }

    private static String getEasingContent(String str, String str2) {
        return str.substring(str2.length() + 1, str.length() - 1);
    }

    private static float getControlPoint(String[] strArr, int i) {
        float parseFloat = Float.parseFloat(strArr[i]);
        if (parseFloat < 0.0f || parseFloat > 1.0f) {
            throw new IllegalArgumentException("Motion easing control point value must be between 0 and 1; instead got: " + parseFloat);
        }
        return parseFloat;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float lerp(float f, float f2, float f3, float f4, float f5) {
        return lerp(f, f2, f3, f4, f5, false);
    }

    static float lerp(float f, float f2, float f3, float f4, float f5, boolean z) {
        if (!z || (f5 >= 0.0f && f5 <= 1.0f)) {
            return f5 < f3 ? f : f5 > f4 ? f2 : lerp(f, f2, (f5 - f3) / (f4 - f3));
        }
        return lerp(f, f2, f5);
    }
}

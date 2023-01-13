package com.android.systemui.statusbar.notification;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import com.android.internal.util.ContrastColorUtil;
import com.android.systemui.C1894R;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;

public class NotificationUtils {
    private static final boolean INCLUDE_HASH_CODE_IN_LIST_ENTRY_LOG_KEY = false;
    private static final int[] sLocationBase = new int[2];
    private static final int[] sLocationOffset = new int[2];
    private static Boolean sUseNewInterruptionModel;

    public static float interpolate(float f, float f2, float f3) {
        return (f * (1.0f - f3)) + (f2 * f3);
    }

    public static boolean isGrayscale(ImageView imageView, ContrastColorUtil contrastColorUtil) {
        Object tag = imageView.getTag(C1894R.C1898id.icon_is_grayscale);
        if (tag != null) {
            return Boolean.TRUE.equals(tag);
        }
        boolean isGrayscaleIcon = contrastColorUtil.isGrayscaleIcon(imageView.getDrawable());
        imageView.setTag(C1894R.C1898id.icon_is_grayscale, Boolean.valueOf(isGrayscaleIcon));
        return isGrayscaleIcon;
    }

    public static int interpolateColors(int i, int i2, float f) {
        return Color.argb((int) interpolate((float) Color.alpha(i), (float) Color.alpha(i2), f), (int) interpolate((float) Color.red(i), (float) Color.red(i2), f), (int) interpolate((float) Color.green(i), (float) Color.green(i2), f), (int) interpolate((float) Color.blue(i), (float) Color.blue(i2), f));
    }

    public static float getRelativeYOffset(View view, View view2) {
        int[] iArr = sLocationBase;
        view2.getLocationOnScreen(iArr);
        int[] iArr2 = sLocationOffset;
        view.getLocationOnScreen(iArr2);
        return (float) (iArr2[1] - iArr[1]);
    }

    public static int getFontScaledHeight(Context context, int i) {
        return (int) (((float) context.getResources().getDimensionPixelSize(i)) * Math.max(1.0f, context.getResources().getDisplayMetrics().scaledDensity / context.getResources().getDisplayMetrics().density));
    }

    public static String logKey(ListEntry listEntry) {
        return listEntry == null ? "null" : logKey(listEntry.getKey());
    }

    public static String logKey(ExpandableNotificationRow expandableNotificationRow) {
        return expandableNotificationRow == null ? "null" : logKey((ListEntry) expandableNotificationRow.getEntry());
    }

    public static String logKey(String str) {
        return str == null ? "null" : str.replace((CharSequence) "\n", (CharSequence) "");
    }
}

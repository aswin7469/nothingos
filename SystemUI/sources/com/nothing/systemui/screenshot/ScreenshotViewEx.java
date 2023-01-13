package com.nothing.systemui.screenshot;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.systemui.C1894R;
import com.nothing.systemui.util.NTLogUtil;

public class ScreenshotViewEx {
    static String TAG = "ScreenshotViewEx";

    public static void updateScreenshotPreviewBorder(boolean z, Context context, ImageView imageView, View view, float f) {
        Drawable drawable = imageView.getDrawable();
        if (drawable == null) {
            NTLogUtil.m1687e(TAG, " updateScreenshotPreviewBorder drawable is null");
            return;
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int dimension = (int) (context.getResources().getDimension(C1894R.dimen.overlay_border_width) * 2.0f);
        if (z) {
            layoutParams.width = ((int) f) + dimension;
            layoutParams.height = ((int) (((float) intrinsicHeight) * (f / ((float) intrinsicWidth)))) + dimension;
        } else {
            layoutParams.width = ((int) (((float) intrinsicWidth) * (f / ((float) intrinsicHeight)))) + dimension;
            layoutParams.height = ((int) f) + dimension;
        }
        NTLogUtil.m1687e(TAG, " updateScreenshotPreviewBorder drawable h:" + intrinsicHeight + " w:" + intrinsicWidth + " Params.width:" + layoutParams.width + " bordeParams.height:" + layoutParams.height + " orientationPortrait:" + z);
        view.setLayoutParams(layoutParams);
    }
}

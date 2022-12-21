package com.android.systemui.p012qs.external;

import android.content.Context;
import android.content.res.ColorStateList;
import com.android.systemui.C1893R;

/* renamed from: com.android.systemui.qs.external.TileColorPicker */
public class TileColorPicker {
    static final int[] DISABLE_STATE_SET = {-16842910};
    static final int[] ENABLE_STATE_SET = {16842910, 16843518};
    static final int[] INACTIVE_STATE_SET = {-16843518};
    private static TileColorPicker sInstance;
    private ColorStateList mColorStateList;

    private TileColorPicker(Context context) {
        this.mColorStateList = context.getResources().getColorStateList(C1893R.C1894color.tint_color_selector, context.getTheme());
    }

    public static TileColorPicker getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TileColorPicker(context);
        }
        return sInstance;
    }

    public int getColor(int i) {
        if (i == 0) {
            return this.mColorStateList.getColorForState(DISABLE_STATE_SET, 0);
        }
        if (i == 1) {
            return this.mColorStateList.getColorForState(INACTIVE_STATE_SET, 0);
        }
        if (i != 2) {
            return this.mColorStateList.getColorForState(ENABLE_STATE_SET, 0);
        }
        return this.mColorStateList.getColorForState(ENABLE_STATE_SET, 0);
    }
}

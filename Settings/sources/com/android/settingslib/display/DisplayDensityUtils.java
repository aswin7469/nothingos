package com.android.settingslib.display;

import android.content.Context;
import android.content.res.Resources;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.MathUtils;
import android.view.WindowManagerGlobal;
import com.android.settingslib.R$string;
import java.util.Arrays;
/* loaded from: classes.dex */
public class DisplayDensityUtils {
    private final int mCurrentIndex;
    private final int mDefaultDensity;
    private final String[] mEntries;
    private final int[] mValues;
    public static final int SUMMARY_DEFAULT = R$string.screen_zoom_summary_default;
    private static final int SUMMARY_CUSTOM = R$string.screen_zoom_summary_custom;
    private static final int[] SUMMARIES_SMALLER = {R$string.screen_zoom_summary_small};
    private static final int[] SUMMARIES_LARGER = {R$string.screen_zoom_summary_very_large, R$string.screen_zoom_summary_extremely_large};

    public DisplayDensityUtils(Context context) {
        int i;
        int defaultDisplayDensity = getDefaultDisplayDensity(0);
        int i2 = -1;
        if (defaultDisplayDensity <= 0) {
            this.mEntries = null;
            this.mValues = null;
            this.mDefaultDensity = 0;
            this.mCurrentIndex = -1;
            return;
        }
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getDisplayNoVerify().getRealMetrics(displayMetrics);
        int i3 = displayMetrics.densityDpi;
        float f = defaultDisplayDensity;
        float min = Math.min(1.5f, ((Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels) * 160) / 320) / f) - 1.0f;
        int constrain = (int) MathUtils.constrain(min / 0.09f, 0.0f, SUMMARIES_LARGER.length);
        int constrain2 = (int) MathUtils.constrain(1.6666664f, 0.0f, SUMMARIES_SMALLER.length);
        int i4 = constrain2 + 1 + constrain;
        String[] strArr = new String[i4];
        int[] iArr = new int[i4];
        if (constrain2 > 0) {
            i = 0;
            for (int i5 = constrain2 - 1; i5 >= 0; i5--) {
                if (i3 == 375) {
                    i2 = i;
                }
                strArr[i] = resources.getString(SUMMARIES_SMALLER[i5]);
                iArr[i] = 375;
                i++;
            }
        } else {
            i = 0;
        }
        i2 = i3 == defaultDisplayDensity ? i : i2;
        iArr[i] = defaultDisplayDensity;
        strArr[i] = resources.getString(SUMMARY_DEFAULT);
        int i6 = i + 1;
        if (constrain > 0) {
            float f2 = min / constrain;
            int i7 = 0;
            while (i7 < constrain) {
                int i8 = i7 + 1;
                int i9 = ((int) (((i8 * f2) + 1.0f) * f)) & (-2);
                if (i3 == i9) {
                    i2 = i6;
                }
                iArr[i6] = i9;
                strArr[i6] = resources.getString(SUMMARIES_LARGER[i7]);
                i6++;
                i7 = i8;
            }
        }
        if (i2 < 0) {
            int i10 = i4 + 1;
            iArr = Arrays.copyOf(iArr, i10);
            iArr[i6] = i3;
            strArr = (String[]) Arrays.copyOf(strArr, i10);
            strArr[i6] = resources.getString(SUMMARY_CUSTOM, Integer.valueOf(i3));
            i2 = i6;
        }
        this.mDefaultDensity = defaultDisplayDensity;
        this.mCurrentIndex = i2;
        this.mEntries = strArr;
        this.mValues = iArr;
    }

    public String[] getEntries() {
        return this.mEntries;
    }

    public int[] getValues() {
        return this.mValues;
    }

    public int getCurrentIndex() {
        return this.mCurrentIndex;
    }

    public int getDefaultDensity() {
        return this.mDefaultDensity;
    }

    private static int getDefaultDisplayDensity(int i) {
        try {
            return WindowManagerGlobal.getWindowManagerService().getInitialDisplayDensity(i);
        } catch (RemoteException unused) {
            return -1;
        }
    }
}

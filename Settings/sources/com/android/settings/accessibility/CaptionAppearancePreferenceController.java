package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.view.accessibility.CaptioningManager;
import com.android.settings.R$array;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;

public class CaptionAppearancePreferenceController extends BasePreferenceController {
    private final CaptioningManager mCaptioningManager;

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public CaptionAppearancePreferenceController(Context context, String str) {
        super(context, str);
        this.mCaptioningManager = (CaptioningManager) context.getSystemService(CaptioningManager.class);
    }

    public CharSequence getSummary() {
        return this.mContext.getString(R$string.preference_summary_default_combination, new Object[]{geFontScaleSummary(), getPresetSummary()});
    }

    private float[] getFontScaleValuesArray() {
        String[] stringArray = this.mContext.getResources().getStringArray(R$array.captioning_font_size_selector_values);
        int length = stringArray.length;
        float[] fArr = new float[length];
        for (int i = 0; i < length; i++) {
            fArr[i] = Float.parseFloat(stringArray[i]);
        }
        return fArr;
    }

    private CharSequence geFontScaleSummary() {
        float[] fontScaleValuesArray = getFontScaleValuesArray();
        String[] stringArray = this.mContext.getResources().getStringArray(R$array.captioning_font_size_selector_titles);
        int indexOf = Floats.indexOf(fontScaleValuesArray, this.mCaptioningManager.getFontScale());
        if (indexOf == -1) {
            indexOf = 0;
        }
        return stringArray[indexOf];
    }

    private CharSequence getPresetSummary() {
        return this.mContext.getResources().getStringArray(R$array.captioning_preset_selector_titles)[Ints.indexOf(this.mContext.getResources().getIntArray(R$array.captioning_preset_selector_values), this.mCaptioningManager.getRawUserStyle())];
    }
}

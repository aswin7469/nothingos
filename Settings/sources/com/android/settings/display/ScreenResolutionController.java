package com.android.settings.display;

import android.content.Context;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.view.Display;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;

public class ScreenResolutionController extends BasePreferenceController {
    static final int FHD_WIDTH = 1080;
    static final int QHD_WIDTH = 1440;
    private Display mDisplay = ((DisplayManager) this.mContext.getSystemService(DisplayManager.class)).getDisplay(0);

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

    public ScreenResolutionController(Context context, String str) {
        super(context, str);
    }

    private boolean isSupportedMode(int i) {
        for (Display.Mode physicalWidth : getSupportedModes()) {
            if (physicalWidth.getPhysicalWidth() == i) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean checkSupportedResolutions() {
        return isSupportedMode(FHD_WIDTH) && isSupportedMode(QHD_WIDTH);
    }

    public int getAvailabilityStatus() {
        return checkSupportedResolutions() ? 0 : 3;
    }

    public CharSequence getSummary() {
        int displayWidth = getDisplayWidth();
        if (displayWidth == FHD_WIDTH) {
            return this.mContext.getString(R$string.screen_resolution_summary_high);
        }
        if (displayWidth != QHD_WIDTH) {
            return this.mContext.getString(R$string.screen_resolution_title);
        }
        return this.mContext.getString(R$string.screen_resolution_summary_highest);
    }

    public int getDisplayWidth() {
        return this.mDisplay.getMode().getPhysicalWidth();
    }

    public Display.Mode[] getSupportedModes() {
        return this.mDisplay.getSupportedModes();
    }
}

package com.android.settings.slices;

import android.content.IntentFilter;

public interface Sliceable {
    Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return null;
    }

    IntentFilter getIntentFilter() {
        return null;
    }

    int getSliceHighlightMenuRes() {
        return 0;
    }

    boolean hasAsyncUpdate() {
        return false;
    }

    boolean isPublicSlice() {
        return false;
    }

    boolean isSliceable() {
        return false;
    }

    boolean useDynamicSliceSummary() {
        return false;
    }
}

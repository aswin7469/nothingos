package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;
import java.util.Locale;
import javax.inject.Inject;

class PointerCountClassifier extends FalsingClassifier {
    private static final int MAX_ALLOWED_POINTERS = 1;
    private static final int MAX_ALLOWED_POINTERS_SWIPE_DOWN = 2;
    private int mMaxPointerCount;

    @Inject
    PointerCountClassifier(FalsingDataProvider falsingDataProvider) {
        super(falsingDataProvider);
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        int i = this.mMaxPointerCount;
        if (motionEvent.getActionMasked() == 0) {
            this.mMaxPointerCount = motionEvent.getPointerCount();
        } else {
            this.mMaxPointerCount = Math.max(this.mMaxPointerCount, motionEvent.getPointerCount());
        }
        if (i != this.mMaxPointerCount) {
            logDebug("Pointers observed:" + this.mMaxPointerCount);
        }
    }

    /* access modifiers changed from: package-private */
    public FalsingClassifier.Result calculateFalsingResult(int i, double d, double d2) {
        int i2 = 2;
        if (!(i == 0 || i == 2)) {
            i2 = 1;
        }
        return this.mMaxPointerCount > i2 ? falsed(1.0d, getReason(i2)) : FalsingClassifier.Result.passed(0.0d);
    }

    private String getReason(int i) {
        Locale locale = null;
        return String.format((Locale) null, "{pointersObserved=%d, threshold=%d}", Integer.valueOf(this.mMaxPointerCount), Integer.valueOf(i));
    }
}

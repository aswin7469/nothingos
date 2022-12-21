package com.nothing.systemui.p024qs;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.android.systemui.util.SafeMarqueeTextView;

/* renamed from: com.nothing.systemui.qs.CircleTileTextView */
public class CircleTileTextView extends SafeMarqueeTextView {
    private static final String TAG = "CircleTileTextView";
    private boolean mIsLabel;
    private boolean mIsSecondLabel;
    private float mPosition = 0.0f;

    public CircleTileTextView(Context context) {
        super(context);
    }

    public CircleTileTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0021, code lost:
        if (r0 >= 0.667f) goto L_0x0024;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0013, code lost:
        if (r0 <= 0.667f) goto L_0x0023;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setAlpha(float r5) {
        /*
            r4 = this;
            boolean r0 = r4.mIsLabel
            r1 = 1059766403(0x3f2ac083, float:0.667)
            r2 = 0
            if (r0 == 0) goto L_0x0015
            float r0 = r4.mPosition
            r3 = 1051361018(0x3eaa7efa, float:0.333)
            int r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r3 <= 0) goto L_0x0015
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L_0x0023
        L_0x0015:
            boolean r0 = r4.mIsSecondLabel
            if (r0 == 0) goto L_0x0024
            float r0 = r4.mPosition
            int r3 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r3 == 0) goto L_0x0023
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L_0x0024
        L_0x0023:
            r5 = r2
        L_0x0024:
            super.setAlpha(r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.p024qs.CircleTileTextView.setAlpha(float):void");
    }

    public void setPosition(float f) {
        this.mPosition = f;
    }

    public void setIsLabel(boolean z) {
        this.mIsLabel = z;
    }

    public void setIsSecondLabel(boolean z) {
        this.mIsSecondLabel = z;
    }

    public boolean canScrollHorizontally(int i) {
        if (!isSingleLine() || getEllipsize() != TextUtils.TruncateAt.MARQUEE) {
            return super.canScrollHorizontally(i);
        }
        return false;
    }
}

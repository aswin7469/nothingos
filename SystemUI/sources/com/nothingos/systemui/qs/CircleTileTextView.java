package com.nothingos.systemui.qs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
/* loaded from: classes2.dex */
public class CircleTileTextView extends TextView {
    private boolean mIsLabel;
    private boolean mIsSecondLabel;
    private float mPosition = 0.0f;

    public CircleTileTextView(Context context) {
        super(context);
    }

    public CircleTileTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0021, code lost:
        if (r0 >= 0.667f) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0013, code lost:
        if (r0 <= 0.667f) goto L7;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setAlpha(float f) {
        if (this.mIsLabel) {
            float f2 = this.mPosition;
            if (f2 > 0.333f) {
            }
        }
        if (this.mIsSecondLabel) {
            float f3 = this.mPosition;
            if (f3 != 0.0f) {
            }
            f = 0.0f;
        }
        super.setAlpha(f);
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
}

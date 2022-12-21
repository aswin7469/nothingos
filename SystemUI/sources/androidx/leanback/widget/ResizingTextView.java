package androidx.leanback.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.widget.TextView;
import androidx.core.widget.TextViewCompat;
import androidx.leanback.C0742R;

class ResizingTextView extends TextView {
    public static final int TRIGGER_MAX_LINES = 1;
    private float mDefaultLineSpacingExtra;
    private int mDefaultPaddingBottom;
    private int mDefaultPaddingTop;
    private int mDefaultTextSize;
    private boolean mDefaultsInitialized;
    private boolean mIsResized;
    private boolean mMaintainLineSpacing;
    private int mResizedPaddingAdjustmentBottom;
    private int mResizedPaddingAdjustmentTop;
    private int mResizedTextSize;
    private int mTriggerConditions;

    public ResizingTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i);
        this.mIsResized = false;
        this.mDefaultsInitialized = false;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0742R.styleable.lbResizingTextView, i, i2);
        try {
            this.mTriggerConditions = obtainStyledAttributes.getInt(C0742R.styleable.lbResizingTextView_resizeTrigger, 1);
            this.mResizedTextSize = obtainStyledAttributes.getDimensionPixelSize(C0742R.styleable.lbResizingTextView_resizedTextSize, -1);
            this.mMaintainLineSpacing = obtainStyledAttributes.getBoolean(C0742R.styleable.lbResizingTextView_maintainLineSpacing, false);
            this.mResizedPaddingAdjustmentTop = obtainStyledAttributes.getDimensionPixelOffset(C0742R.styleable.lbResizingTextView_resizedPaddingAdjustmentTop, 0);
            this.mResizedPaddingAdjustmentBottom = obtainStyledAttributes.getDimensionPixelOffset(C0742R.styleable.lbResizingTextView_resizedPaddingAdjustmentBottom, 0);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public ResizingTextView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ResizingTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842884);
    }

    public ResizingTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public int getTriggerConditions() {
        return this.mTriggerConditions;
    }

    public void setTriggerConditions(int i) {
        if (this.mTriggerConditions != i) {
            this.mTriggerConditions = i;
            requestLayout();
        }
    }

    public int getResizedTextSize() {
        return this.mResizedTextSize;
    }

    public void setResizedTextSize(int i) {
        if (this.mResizedTextSize != i) {
            this.mResizedTextSize = i;
            resizeParamsChanged();
        }
    }

    public boolean getMaintainLineSpacing() {
        return this.mMaintainLineSpacing;
    }

    public void setMaintainLineSpacing(boolean z) {
        if (this.mMaintainLineSpacing != z) {
            this.mMaintainLineSpacing = z;
            resizeParamsChanged();
        }
    }

    public int getResizedPaddingAdjustmentTop() {
        return this.mResizedPaddingAdjustmentTop;
    }

    public void setResizedPaddingAdjustmentTop(int i) {
        if (this.mResizedPaddingAdjustmentTop != i) {
            this.mResizedPaddingAdjustmentTop = i;
            resizeParamsChanged();
        }
    }

    public int getResizedPaddingAdjustmentBottom() {
        return this.mResizedPaddingAdjustmentBottom;
    }

    public void setResizedPaddingAdjustmentBottom(int i) {
        if (this.mResizedPaddingAdjustmentBottom != i) {
            this.mResizedPaddingAdjustmentBottom = i;
            resizeParamsChanged();
        }
    }

    private void resizeParamsChanged() {
        if (this.mIsResized) {
            requestLayout();
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:48:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMeasure(int r7, int r8) {
        /*
            r6 = this;
            boolean r0 = r6.mDefaultsInitialized
            r1 = 1
            if (r0 != 0) goto L_0x0020
            float r0 = r6.getTextSize()
            int r0 = (int) r0
            r6.mDefaultTextSize = r0
            float r0 = r6.getLineSpacingExtra()
            r6.mDefaultLineSpacingExtra = r0
            int r0 = r6.getPaddingTop()
            r6.mDefaultPaddingTop = r0
            int r0 = r6.getPaddingBottom()
            r6.mDefaultPaddingBottom = r0
            r6.mDefaultsInitialized = r1
        L_0x0020:
            int r0 = r6.mDefaultTextSize
            float r0 = (float) r0
            r2 = 0
            r6.setTextSize(r2, r0)
            float r0 = r6.mDefaultLineSpacingExtra
            float r3 = r6.getLineSpacingMultiplier()
            r6.setLineSpacing(r0, r3)
            int r0 = r6.mDefaultPaddingTop
            int r3 = r6.mDefaultPaddingBottom
            r6.setPaddingTopAndBottom(r0, r3)
            super.onMeasure(r7, r8)
            android.text.Layout r0 = r6.getLayout()
            if (r0 == 0) goto L_0x0053
            int r3 = r6.mTriggerConditions
            r3 = r3 & r1
            if (r3 <= 0) goto L_0x0053
            int r0 = r0.getLineCount()
            int r3 = r6.getMaxLines()
            if (r3 <= r1) goto L_0x0053
            if (r0 != r3) goto L_0x0053
            r0 = r1
            goto L_0x0054
        L_0x0053:
            r0 = r2
        L_0x0054:
            float r3 = r6.getTextSize()
            int r3 = (int) r3
            r4 = -1
            if (r0 == 0) goto L_0x009f
            int r5 = r6.mResizedTextSize
            if (r5 == r4) goto L_0x0067
            if (r3 == r5) goto L_0x0067
            float r3 = (float) r5
            r6.setTextSize(r2, r3)
            r2 = r1
        L_0x0067:
            float r3 = r6.mDefaultLineSpacingExtra
            int r4 = r6.mDefaultTextSize
            float r4 = (float) r4
            float r3 = r3 + r4
            int r4 = r6.mResizedTextSize
            float r4 = (float) r4
            float r3 = r3 - r4
            boolean r4 = r6.mMaintainLineSpacing
            if (r4 == 0) goto L_0x0085
            float r4 = r6.getLineSpacingExtra()
            int r4 = (r4 > r3 ? 1 : (r4 == r3 ? 0 : -1))
            if (r4 == 0) goto L_0x0085
            float r2 = r6.getLineSpacingMultiplier()
            r6.setLineSpacing(r3, r2)
            r2 = r1
        L_0x0085:
            int r3 = r6.mDefaultPaddingTop
            int r4 = r6.mResizedPaddingAdjustmentTop
            int r3 = r3 + r4
            int r4 = r6.mDefaultPaddingBottom
            int r5 = r6.mResizedPaddingAdjustmentBottom
            int r4 = r4 + r5
            int r5 = r6.getPaddingTop()
            if (r5 != r3) goto L_0x009b
            int r5 = r6.getPaddingBottom()
            if (r5 == r4) goto L_0x00d3
        L_0x009b:
            r6.setPaddingTopAndBottom(r3, r4)
            goto L_0x00dc
        L_0x009f:
            int r5 = r6.mResizedTextSize
            if (r5 == r4) goto L_0x00ac
            int r4 = r6.mDefaultTextSize
            if (r3 == r4) goto L_0x00ac
            float r3 = (float) r4
            r6.setTextSize(r2, r3)
            r2 = r1
        L_0x00ac:
            boolean r3 = r6.mMaintainLineSpacing
            if (r3 == 0) goto L_0x00c2
            float r3 = r6.getLineSpacingExtra()
            float r4 = r6.mDefaultLineSpacingExtra
            int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
            if (r3 == 0) goto L_0x00c2
            float r2 = r6.getLineSpacingMultiplier()
            r6.setLineSpacing(r4, r2)
            r2 = r1
        L_0x00c2:
            int r3 = r6.getPaddingTop()
            int r4 = r6.mDefaultPaddingTop
            if (r3 != r4) goto L_0x00d5
            int r3 = r6.getPaddingBottom()
            int r4 = r6.mDefaultPaddingBottom
            if (r3 == r4) goto L_0x00d3
            goto L_0x00d5
        L_0x00d3:
            r1 = r2
            goto L_0x00dc
        L_0x00d5:
            int r2 = r6.mDefaultPaddingTop
            int r3 = r6.mDefaultPaddingBottom
            r6.setPaddingTopAndBottom(r2, r3)
        L_0x00dc:
            r6.mIsResized = r0
            if (r1 == 0) goto L_0x00e3
            super.onMeasure(r7, r8)
        L_0x00e3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.ResizingTextView.onMeasure(int, int):void");
    }

    private void setPaddingTopAndBottom(int i, int i2) {
        if (isPaddingRelative()) {
            setPaddingRelative(getPaddingStart(), i, getPaddingEnd(), i2);
        } else {
            setPadding(getPaddingLeft(), i, getPaddingRight(), i2);
        }
    }

    public void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, callback));
    }
}

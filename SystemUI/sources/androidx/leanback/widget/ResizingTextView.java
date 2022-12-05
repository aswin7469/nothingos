package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.widget.TextView;
import androidx.core.widget.TextViewCompat;
import androidx.leanback.R$styleable;
@SuppressLint({"AppCompatCustomView"})
/* loaded from: classes.dex */
class ResizingTextView extends TextView {
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

    @SuppressLint({"CustomViewStyleable"})
    public ResizingTextView(Context ctx, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(ctx, attrs, defStyleAttr);
        this.mIsResized = false;
        this.mDefaultsInitialized = false;
        TypedArray obtainStyledAttributes = ctx.obtainStyledAttributes(attrs, R$styleable.lbResizingTextView, defStyleAttr, defStyleRes);
        try {
            this.mTriggerConditions = obtainStyledAttributes.getInt(R$styleable.lbResizingTextView_resizeTrigger, 1);
            this.mResizedTextSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.lbResizingTextView_resizedTextSize, -1);
            this.mMaintainLineSpacing = obtainStyledAttributes.getBoolean(R$styleable.lbResizingTextView_maintainLineSpacing, false);
            this.mResizedPaddingAdjustmentTop = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.lbResizingTextView_resizedPaddingAdjustmentTop, 0);
            this.mResizedPaddingAdjustmentBottom = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.lbResizingTextView_resizedPaddingAdjustmentBottom, 0);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public ResizingTextView(Context ctx, AttributeSet attrs, int defStyleAttr) {
        this(ctx, attrs, defStyleAttr, 0);
    }

    public ResizingTextView(Context ctx, AttributeSet attrs) {
        this(ctx, attrs, 16842884);
    }

    public ResizingTextView(Context ctx) {
        this(ctx, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x009f  */
    @Override // android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean z;
        int i;
        boolean z2 = true;
        if (!this.mDefaultsInitialized) {
            this.mDefaultTextSize = (int) getTextSize();
            this.mDefaultLineSpacingExtra = getLineSpacingExtra();
            this.mDefaultPaddingTop = getPaddingTop();
            this.mDefaultPaddingBottom = getPaddingBottom();
            this.mDefaultsInitialized = true;
        }
        boolean z3 = false;
        setTextSize(0, this.mDefaultTextSize);
        setLineSpacing(this.mDefaultLineSpacingExtra, getLineSpacingMultiplier());
        setPaddingTopAndBottom(this.mDefaultPaddingTop, this.mDefaultPaddingBottom);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Layout layout = getLayout();
        if (layout != null && (this.mTriggerConditions & 1) > 0) {
            int lineCount = layout.getLineCount();
            int maxLines = getMaxLines();
            if (maxLines > 1 && lineCount == maxLines) {
                z = true;
                int textSize = (int) getTextSize();
                if (!z) {
                    int i2 = this.mResizedTextSize;
                    if (i2 != -1 && textSize != i2) {
                        setTextSize(0, i2);
                        z3 = true;
                    }
                    float f = (this.mDefaultLineSpacingExtra + this.mDefaultTextSize) - this.mResizedTextSize;
                    if (this.mMaintainLineSpacing && getLineSpacingExtra() != f) {
                        setLineSpacing(f, getLineSpacingMultiplier());
                        z3 = true;
                    }
                    int i3 = this.mDefaultPaddingTop + this.mResizedPaddingAdjustmentTop;
                    int i4 = this.mDefaultPaddingBottom + this.mResizedPaddingAdjustmentBottom;
                    if (getPaddingTop() != i3 || getPaddingBottom() != i4) {
                        setPaddingTopAndBottom(i3, i4);
                    }
                    z2 = z3;
                } else {
                    if (this.mResizedTextSize != -1 && textSize != (i = this.mDefaultTextSize)) {
                        setTextSize(0, i);
                        z3 = true;
                    }
                    if (this.mMaintainLineSpacing) {
                        float lineSpacingExtra = getLineSpacingExtra();
                        float f2 = this.mDefaultLineSpacingExtra;
                        if (lineSpacingExtra != f2) {
                            setLineSpacing(f2, getLineSpacingMultiplier());
                            z3 = true;
                        }
                    }
                    if (getPaddingTop() != this.mDefaultPaddingTop || getPaddingBottom() != this.mDefaultPaddingBottom) {
                        setPaddingTopAndBottom(this.mDefaultPaddingTop, this.mDefaultPaddingBottom);
                    }
                    z2 = z3;
                }
                this.mIsResized = z;
                if (z2) {
                    return;
                }
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }
        }
        z = false;
        int textSize2 = (int) getTextSize();
        if (!z) {
        }
        this.mIsResized = z;
        if (z2) {
        }
    }

    private void setPaddingTopAndBottom(int paddingTop, int paddingBottom) {
        if (isPaddingRelative()) {
            setPaddingRelative(getPaddingStart(), paddingTop, getPaddingEnd(), paddingBottom);
        } else {
            setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), paddingBottom);
        }
    }

    @Override // android.widget.TextView
    public void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {
        super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, actionModeCallback));
    }
}

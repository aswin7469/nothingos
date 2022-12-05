package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.autofill.AutofillValue;
import android.widget.EditText;
import android.widget.TextView;
import androidx.core.widget.TextViewCompat;
@SuppressLint({"AppCompatCustomView"})
/* loaded from: classes.dex */
public class GuidedActionEditText extends EditText {
    private GuidedActionAutofillSupport$OnAutofillListener mAutofillListener;
    private ImeKeyMonitor$ImeKeyListener mKeyListener;
    private final Drawable mNoPaddingDrawable;
    private final Drawable mSavedBackground;

    @Override // android.widget.TextView, android.view.View
    public int getAutofillType() {
        return 1;
    }

    /* loaded from: classes.dex */
    static final class NoPaddingDrawable extends Drawable {
        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int alpha) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        NoPaddingDrawable() {
        }

        @Override // android.graphics.drawable.Drawable
        public boolean getPadding(Rect padding) {
            padding.set(0, 0, 0, 0);
            return true;
        }
    }

    public GuidedActionEditText(Context ctx) {
        this(ctx, null);
    }

    public GuidedActionEditText(Context ctx, AttributeSet attrs) {
        this(ctx, attrs, 16842862);
    }

    public GuidedActionEditText(Context ctx, AttributeSet attrs, int defStyleAttr) {
        super(ctx, attrs, defStyleAttr);
        this.mSavedBackground = getBackground();
        NoPaddingDrawable noPaddingDrawable = new NoPaddingDrawable();
        this.mNoPaddingDrawable = noPaddingDrawable;
        setBackground(noPaddingDrawable);
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        ImeKeyMonitor$ImeKeyListener imeKeyMonitor$ImeKeyListener = this.mKeyListener;
        boolean onKeyPreIme = imeKeyMonitor$ImeKeyListener != null ? imeKeyMonitor$ImeKeyListener.onKeyPreIme(this, keyCode, event) : false;
        return !onKeyPreIme ? super.onKeyPreIme(keyCode, event) : onKeyPreIme;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName((isFocused() ? EditText.class : TextView.class).getName());
    }

    @Override // android.widget.TextView, android.view.View
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            setBackground(this.mSavedBackground);
        } else {
            setBackground(this.mNoPaddingDrawable);
        }
        if (!focused) {
            setFocusable(false);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void autofill(AutofillValue values) {
        super.autofill(values);
        GuidedActionAutofillSupport$OnAutofillListener guidedActionAutofillSupport$OnAutofillListener = this.mAutofillListener;
        if (guidedActionAutofillSupport$OnAutofillListener != null) {
            guidedActionAutofillSupport$OnAutofillListener.onAutofill(this);
        }
    }

    @Override // android.widget.TextView
    public void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {
        super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, actionModeCallback));
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (!isInTouchMode() || isFocusableInTouchMode() || isTextSelectable()) {
            return super.onTouchEvent(event);
        }
        return false;
    }
}

package androidx.leanback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.leanback.R$id;
/* loaded from: classes.dex */
class GuidedActionsRelativeLayout extends RelativeLayout {
    private boolean mInOverride;
    private InterceptKeyEventListener mInterceptKeyEventListener;
    private float mKeyLinePercent;

    /* loaded from: classes.dex */
    interface InterceptKeyEventListener {
        boolean onInterceptKeyEvent(KeyEvent event);
    }

    public GuidedActionsRelativeLayout(Context context) {
        this(context, null);
    }

    public GuidedActionsRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuidedActionsRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mInOverride = false;
        this.mKeyLinePercent = GuidanceStylingRelativeLayout.getKeyLinePercent(context);
    }

    @Override // android.widget.RelativeLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View findViewById;
        int size = View.MeasureSpec.getSize(heightMeasureSpec);
        if (size > 0 && (findViewById = findViewById(R$id.guidedactions_sub_list)) != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) findViewById.getLayoutParams();
            if (marginLayoutParams.topMargin < 0 && !this.mInOverride) {
                this.mInOverride = true;
            }
            if (this.mInOverride) {
                marginLayoutParams.topMargin = (int) ((this.mKeyLinePercent * size) / 100.0f);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mInOverride = false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        InterceptKeyEventListener interceptKeyEventListener = this.mInterceptKeyEventListener;
        if (interceptKeyEventListener == null || !interceptKeyEventListener.onInterceptKeyEvent(event)) {
            return super.dispatchKeyEvent(event);
        }
        return true;
    }
}

package androidx.leanback.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.leanback.R$id;
import androidx.leanback.R$styleable;
/* loaded from: classes.dex */
class GuidanceStylingRelativeLayout extends RelativeLayout {
    private float mTitleKeylinePercent;

    public GuidanceStylingRelativeLayout(Context context) {
        this(context, null);
    }

    public GuidanceStylingRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuidanceStylingRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mTitleKeylinePercent = getKeyLinePercent(context);
    }

    public static float getKeyLinePercent(Context context) {
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(R$styleable.LeanbackGuidedStepTheme);
        float f = obtainStyledAttributes.getFloat(R$styleable.LeanbackGuidedStepTheme_guidedStepKeyline, 40.0f);
        obtainStyledAttributes.recycle();
        return f;
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        View findViewById = getRootView().findViewById(R$id.guidance_title);
        View findViewById2 = getRootView().findViewById(R$id.guidance_breadcrumb);
        View findViewById3 = getRootView().findViewById(R$id.guidance_description);
        ImageView imageView = (ImageView) getRootView().findViewById(R$id.guidance_icon);
        int measuredHeight = (int) ((getMeasuredHeight() * this.mTitleKeylinePercent) / 100.0f);
        if (findViewById != null && findViewById.getParent() == this) {
            int baseline = (((measuredHeight - findViewById.getBaseline()) - findViewById2.getMeasuredHeight()) - findViewById.getPaddingTop()) - findViewById2.getTop();
            if (findViewById2.getParent() == this) {
                findViewById2.offsetTopAndBottom(baseline);
            }
            findViewById.offsetTopAndBottom(baseline);
            if (findViewById3 != null && findViewById3.getParent() == this) {
                findViewById3.offsetTopAndBottom(baseline);
            }
        }
        if (imageView == null || imageView.getParent() != this || imageView.getDrawable() == null) {
            return;
        }
        imageView.offsetTopAndBottom(measuredHeight - (imageView.getMeasuredHeight() / 2));
    }
}

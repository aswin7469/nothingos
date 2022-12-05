package androidx.leanback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
/* loaded from: classes.dex */
public class BrowseRowsFrameLayout extends FrameLayout {
    public BrowseRowsFrameLayout(Context context) {
        this(context, null);
    }

    public BrowseRowsFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrowseRowsFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // android.view.ViewGroup
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        child.measure(FrameLayout.getChildMeasureSpec(parentWidthMeasureSpec, getPaddingLeft() + getPaddingRight() + widthUsed, marginLayoutParams.width), FrameLayout.getChildMeasureSpec(parentHeightMeasureSpec, getPaddingTop() + getPaddingBottom() + heightUsed, marginLayoutParams.height));
    }
}

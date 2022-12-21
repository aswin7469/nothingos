package com.android.systemui.statusbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.systemui.C1893R;

public class KeyboardShortcutAppItemLayout extends RelativeLayout {
    private static final double MAX_WIDTH_PERCENT_FOR_KEYWORDS = 0.7d;

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public KeyboardShortcutAppItemLayout(Context context) {
        super(context);
    }

    public KeyboardShortcutAppItemLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (View.MeasureSpec.getMode(i) == 1073741824) {
            ImageView imageView = (ImageView) findViewById(C1893R.C1897id.keyboard_shortcuts_icon);
            TextView textView = (TextView) findViewById(C1893R.C1897id.keyboard_shortcuts_keyword);
            int size = View.MeasureSpec.getSize(i) - (getPaddingLeft() + getPaddingRight());
            if (imageView.getVisibility() == 0) {
                size -= imageView.getMeasuredWidth();
            }
            textView.setMaxWidth((int) Math.round(((double) size) * MAX_WIDTH_PERCENT_FOR_KEYWORDS));
        }
        super.onMeasure(i, i2);
    }
}

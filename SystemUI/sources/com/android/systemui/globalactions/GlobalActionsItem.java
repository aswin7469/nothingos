package com.android.systemui.globalactions;

import android.content.Context;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GlobalActionsItem extends LinearLayout {
    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public GlobalActionsItem(Context context) {
        super(context);
    }

    public GlobalActionsItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public GlobalActionsItem(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    private TextView getTextView() {
        return (TextView) findViewById(16908299);
    }

    public void setMarquee(boolean z) {
        TextView textView = getTextView();
        textView.setSingleLine(z);
        textView.setEllipsize(z ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
    }

    public boolean isTruncated() {
        Layout layout;
        TextView textView = getTextView();
        if (textView == null || (layout = textView.getLayout()) == null || layout.getLineCount() <= 0 || layout.getEllipsisCount(layout.getLineCount() - 1) <= 0) {
            return false;
        }
        return true;
    }
}

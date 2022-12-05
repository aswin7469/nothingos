package com.nt.settings.widget;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
/* loaded from: classes2.dex */
public class CustomizePreference extends Preference {
    private TextView mTitleTextView;
    private int mTitleTextViewColor = -1;
    private int mTitleTextViewLayoutGravity = -1;

    public CustomizePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomizePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public CustomizePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // android.preference.Preference
    protected View onCreateView(ViewGroup viewGroup) {
        View onCreateView = super.onCreateView(viewGroup);
        this.mTitleTextView = (TextView) onCreateView.findViewById(16908310);
        return onCreateView;
    }

    @Override // android.preference.Preference
    protected void onBindView(View view) {
        super.onBindView(view);
        ((TextView) view.findViewById(16908304)).setMaxLines(Integer.MAX_VALUE);
        int i = this.mTitleTextViewColor;
        if (i != -1) {
            this.mTitleTextView.setTextColor(i);
        }
        if (this.mTitleTextViewLayoutGravity != -1) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mTitleTextView.getLayoutParams();
            layoutParams.gravity = this.mTitleTextViewLayoutGravity;
            this.mTitleTextView.setLayoutParams(layoutParams);
        }
    }
}

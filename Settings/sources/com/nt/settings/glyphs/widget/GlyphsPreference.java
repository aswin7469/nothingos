package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.R$styleable;
/* loaded from: classes2.dex */
public class GlyphsPreference extends Preference {
    private Drawable mBackgroundDrawable;
    private int mSummarySize;
    private int mTitleSize;

    public GlyphsPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public GlyphsPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public GlyphsPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public GlyphsPreference(Context context) {
        super(context);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        TextView textView;
        TextView textView2;
        super.onBindViewHolder(preferenceViewHolder);
        if (this.mBackgroundDrawable != null) {
            preferenceViewHolder.itemView.setBackgroundResource(R.drawable.bg_switch);
        }
        if (this.mTitleSize > 0 && (textView2 = (TextView) preferenceViewHolder.findViewById(16908310)) != null) {
            textView2.setTextSize(0, this.mTitleSize);
        }
        if (this.mSummarySize <= 0 || (textView = (TextView) preferenceViewHolder.findViewById(16908304)) == null) {
            return;
        }
        textView.setTextSize(0, this.mSummarySize);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.GlyphsPreference, 0, 0);
        this.mTitleSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.GlyphsPreference_title_font_size, 0);
        this.mBackgroundDrawable = obtainStyledAttributes.getDrawable(R$styleable.GlyphsPreference_pre_background);
        this.mSummarySize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.GlyphsPreference_summary_font_size, 0);
        obtainStyledAttributes.recycle();
    }
}

package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
/* loaded from: classes2.dex */
public class GlyphsBackgroundPreference extends Preference {
    private PreferenceViewHolder holder;
    private Drawable mBackgroundDrawable;
    private int mWidgetVisibility = 8;

    public GlyphsBackgroundPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R.layout.preference_glyphs_backgroud);
    }

    public GlyphsBackgroundPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(R.layout.preference_glyphs_backgroud);
    }

    public GlyphsBackgroundPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, TypedArrayUtils.getAttr(context, R.attr.preferenceStyle, 16842894));
        setLayoutResource(R.layout.preference_glyphs_backgroud);
    }

    public GlyphsBackgroundPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.preference_glyphs_backgroud);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.holder = preferenceViewHolder;
        Drawable drawable = this.mBackgroundDrawable;
        if (drawable != null) {
            preferenceViewHolder.itemView.setBackground(drawable);
        }
        LinearLayout linearLayout = (LinearLayout) preferenceViewHolder.findViewById(16908312);
        if (linearLayout == null || selectedViewResId() <= 0) {
            return;
        }
        linearLayout.setVisibility(this.mWidgetVisibility);
        linearLayout.removeAllViews();
        LayoutInflater.from(preferenceViewHolder.itemView.getContext()).inflate(selectedViewResId(), linearLayout);
    }

    public void setBackground(Drawable drawable) {
        this.mBackgroundDrawable = drawable;
        notifyChanged();
    }

    public void setWidgetFrameVisibility(int i) {
        this.mWidgetVisibility = i;
        notifyChanged();
    }

    protected int selectedViewResId() {
        return R.layout.preference_widget_confirm;
    }
}

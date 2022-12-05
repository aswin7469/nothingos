package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
/* loaded from: classes2.dex */
public class GlyphFooterButtonPreference extends Preference {
    private OnConfirmListener mOnConfirmListener;

    /* loaded from: classes2.dex */
    public interface OnConfirmListener {
        void onConfirm(View view);
    }

    public GlyphFooterButtonPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.preference_footer_button);
        setSelectable(false);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.findViewById(R.id.btn_set).setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.glyphs.widget.GlyphFooterButtonPreference.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (GlyphFooterButtonPreference.this.mOnConfirmListener != null) {
                    GlyphFooterButtonPreference.this.mOnConfirmListener.onConfirm(view);
                }
            }
        });
    }
}

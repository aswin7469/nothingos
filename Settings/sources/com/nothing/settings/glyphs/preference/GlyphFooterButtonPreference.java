package com.nothing.settings.glyphs.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$id;
import com.android.settings.R$layout;

public class GlyphFooterButtonPreference extends Preference {
    private OnConfirmListener mOnConfirmListener;

    public interface OnConfirmListener {
        void onConfirm(View view);
    }

    public GlyphFooterButtonPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R$layout.preference_footer_button);
        setSelectable(false);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.findViewById(R$id.btn_set).setOnClickListener(new GlyphFooterButtonPreference$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(View view) {
        OnConfirmListener onConfirmListener = this.mOnConfirmListener;
        if (onConfirmListener != null) {
            onConfirmListener.onConfirm(view);
        }
    }
}

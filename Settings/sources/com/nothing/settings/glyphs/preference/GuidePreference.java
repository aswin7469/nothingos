package com.nothing.settings.glyphs.preference;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.nothing.settings.glyphs.widget.GlyphsItemView;

public class GuidePreference extends Preference {
    /* access modifiers changed from: private */
    public String mAnimPath = "glyphs/beetle.csv";
    /* access modifiers changed from: private */
    public GlyphsItemView mGlyphsItemView;
    private OnConfirmListener mOnConfirmListener;

    public interface OnConfirmListener {
        void onConfirm(View view);
    }

    public GuidePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R$layout.preference_guide);
    }

    public GuidePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(R$layout.preference_guide);
    }

    public GuidePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R$layout.preference_guide);
        setSelectable(false);
        Log.d("GuidePreference", "GuidePreference CREATE");
    }

    public GuidePreference(Context context) {
        super(context);
        setLayoutResource(R$layout.preference_guide);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.findViewById(R$id.ibtn_enter).setOnClickListener(new GuidePreference$$ExternalSyntheticLambda0(this));
        ((ScrollView) preferenceViewHolder.findViewById(R$id.sv_root)).requestDisallowInterceptTouchEvent(false);
        GlyphsItemView glyphsItemView = (GlyphsItemView) preferenceViewHolder.findViewById(R$id.preview_glyphs);
        this.mGlyphsItemView = glyphsItemView;
        glyphsItemView.release();
        if (this.mGlyphsItemView != null && !TextUtils.isEmpty(this.mAnimPath)) {
            this.mGlyphsItemView.setOnAnimListener(new GlyphsItemView.onAnimatorChangedListener() {
                public void onStart(View view) {
                }

                public void onFinish(View view) {
                    GuidePreference.this.mGlyphsItemView.startAnim(GuidePreference.this.mAnimPath);
                }
            });
            this.mGlyphsItemView.startAnim(this.mAnimPath);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(View view) {
        OnConfirmListener onConfirmListener = this.mOnConfirmListener;
        if (onConfirmListener != null) {
            onConfirmListener.onConfirm(view);
        }
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.mOnConfirmListener = onConfirmListener;
    }

    public void play(String str) {
        this.mAnimPath = str;
        notifyChanged();
    }

    public void release() {
        GlyphsItemView glyphsItemView = this.mGlyphsItemView;
        if (glyphsItemView != null) {
            glyphsItemView.release();
            this.mAnimPath = null;
        }
    }
}

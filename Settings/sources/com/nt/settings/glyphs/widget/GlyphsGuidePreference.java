package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.nt.settings.glyphs.widget.GlyphsItemView;
/* loaded from: classes2.dex */
public class GlyphsGuidePreference extends Preference {
    private String mAnimPath = "glyphs/beetle.csv";
    private GlyphsItemView mGlyphsItemView;
    private OnConfirmListener mOnConfirmListener;

    /* loaded from: classes2.dex */
    public interface OnConfirmListener {
        void onConfirm(View view);
    }

    public GlyphsGuidePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R.layout.nt_preference_guide);
    }

    public GlyphsGuidePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(R.layout.nt_preference_guide);
    }

    public GlyphsGuidePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.nt_preference_guide);
        setSelectable(false);
        Log.d("GlyphsGuidePreference", "GlyphsGuidePreference CREATE");
    }

    public GlyphsGuidePreference(Context context) {
        super(context);
        setLayoutResource(R.layout.nt_preference_guide);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.findViewById(R.id.ibtn_enter).setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.glyphs.widget.GlyphsGuidePreference.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (GlyphsGuidePreference.this.mOnConfirmListener != null) {
                    GlyphsGuidePreference.this.mOnConfirmListener.onConfirm(view);
                }
            }
        });
        ((ScrollView) preferenceViewHolder.findViewById(R.id.sv_root)).requestDisallowInterceptTouchEvent(false);
        GlyphsItemView glyphsItemView = (GlyphsItemView) preferenceViewHolder.findViewById(R.id.preview_glyphs);
        this.mGlyphsItemView = glyphsItemView;
        glyphsItemView.release();
        if (this.mGlyphsItemView == null || TextUtils.isEmpty(this.mAnimPath)) {
            return;
        }
        this.mGlyphsItemView.setOnAnimListener(new GlyphsItemView.OnAnimListener() { // from class: com.nt.settings.glyphs.widget.GlyphsGuidePreference.2
            @Override // com.nt.settings.glyphs.widget.GlyphsItemView.OnAnimListener
            public void onStart(View view) {
            }

            @Override // com.nt.settings.glyphs.widget.GlyphsItemView.OnAnimListener
            public void onFinish(View view) {
                GlyphsGuidePreference.this.mGlyphsItemView.startAnim(GlyphsGuidePreference.this.mAnimPath);
            }
        });
        this.mGlyphsItemView.startAnim(this.mAnimPath);
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

package com.nothing.settings.glyphs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.airbnb.lottie.LottieAnimationView;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$styleable;

public class GlyphsAnimPreviewPreference extends Preference {
    private String mAnimDarkUrl;
    private String mAnimLightUrl;
    private LottieAnimationView mAnimPreview;

    public GlyphsAnimPreviewPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet, i, i2);
    }

    public GlyphsAnimPreviewPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public GlyphsAnimPreviewPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public GlyphsAnimPreviewPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        setLayoutResource(R$layout.nt_preference_glyphs_anim_preview);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.GlyphsAnimPreviewPreference, i, i2);
        this.mAnimDarkUrl = obtainStyledAttributes.getString(R$styleable.GlyphsAnimPreviewPreference_anim_dark_url);
        this.mAnimLightUrl = obtainStyledAttributes.getString(R$styleable.GlyphsAnimPreviewPreference_anim_light_url);
        setSelectable(false);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mAnimPreview = (LottieAnimationView) preferenceViewHolder.findViewById(R$id.lav_preview);
        setAnimUrl(this.mAnimDarkUrl, this.mAnimLightUrl);
    }

    public void setAnimUrl(String str, String str2) {
        if (this.mAnimPreview != null) {
            this.mAnimDarkUrl = str;
            this.mAnimLightUrl = str2;
            if (isNightMode()) {
                this.mAnimPreview.setAnimation(str2);
            } else {
                this.mAnimPreview.setAnimation(str);
            }
            this.mAnimPreview.playAnimation();
        }
    }

    private boolean isNightMode() {
        return (getContext().getResources().getConfiguration().uiMode & 32) != 0;
    }

    public void onDetached() {
        super.onDetached();
        LottieAnimationView lottieAnimationView = this.mAnimPreview;
        if (lottieAnimationView != null) {
            lottieAnimationView.cancelAnimation();
        }
    }
}

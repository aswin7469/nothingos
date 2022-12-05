package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.R$styleable;
import com.nt.settings.glyphs.utils.LedBrightnessUtils;
/* loaded from: classes2.dex */
public class GlyphsPreviewPreference extends Preference {
    private Drawable mBackgroundDrawable;
    private Drawable mImageDrawable;
    private int mLightValue;
    private ImageView mPreImageView;
    private Drawable mPreviewDrawable;

    public GlyphsPreviewPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mLightValue = 200;
        init(context, attributeSet, i, i2);
    }

    public GlyphsPreviewPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public GlyphsPreviewPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public GlyphsPreviewPreference(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        setLayoutResource(R.layout.preference_glyphs_preview);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ImagePreference, i, i2);
        this.mImageDrawable = obtainStyledAttributes.getDrawable(R$styleable.ImagePreference_image);
        this.mBackgroundDrawable = obtainStyledAttributes.getDrawable(R$styleable.ImagePreference_nt_background);
        this.mPreviewDrawable = obtainStyledAttributes.getDrawable(R$styleable.ImagePreference_nt_preview);
        setSelectable(false);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ImageView imageView = (ImageView) preferenceViewHolder.findViewById(R.id.image);
        this.mPreImageView = (ImageView) preferenceViewHolder.findViewById(R.id.image_cover);
        Drawable drawable = this.mImageDrawable;
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        }
        Drawable drawable2 = this.mBackgroundDrawable;
        if (drawable2 != null) {
            imageView.setBackground(drawable2);
        }
        Drawable drawable3 = this.mPreviewDrawable;
        if (drawable3 != null) {
            this.mPreImageView.setBackground(drawable3);
        }
        if (!isLedSwitchChecked(imageView.getContext())) {
            closeLed();
        } else {
            setLightness(this.mLightValue);
        }
    }

    private boolean isLedSwitchChecked(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "led_effect_enable", 0) == 1;
    }

    public void setLightness(int i) {
        this.mLightValue = i;
        ImageView imageView = this.mPreImageView;
        if (imageView == null) {
            return;
        }
        imageView.setAlpha(i / LedBrightnessUtils.getLedBrightnessMax());
    }

    public void closeLed() {
        ImageView imageView = this.mPreImageView;
        if (imageView == null) {
            return;
        }
        imageView.setAlpha(0.12f);
    }
}

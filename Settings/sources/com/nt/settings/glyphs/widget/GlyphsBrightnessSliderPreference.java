package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
/* loaded from: classes2.dex */
public class GlyphsBrightnessSliderPreference extends Preference implements SeekBar.OnSeekBarChangeListener {
    private OnSeekBarPreferenceChangeListener mListener;
    private int mProgress = 0;
    private int mMax = 0;
    private int mStart = 0;

    /* loaded from: classes2.dex */
    public interface OnSeekBarPreferenceChangeListener {
        void onProgressChanged(Preference preference, int i);

        void onStartTrackingTouch(Preference preference, int i);

        void onStopTrackingTouch(Preference preference);
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public GlyphsBrightnessSliderPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.layout_brightness_slider);
        setSelectable(false);
    }

    public GlyphsBrightnessSliderPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.layout_brightness_slider);
        setSelectable(false);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        int i;
        super.onBindViewHolder(preferenceViewHolder);
        SeekBar seekBar = (SeekBar) preferenceViewHolder.findViewById(R.id.slider);
        int i2 = this.mMax;
        if (i2 < 0 || i2 <= (i = this.mStart)) {
            return;
        }
        if (this.mProgress > i2) {
            this.mProgress = i2;
        }
        if (this.mProgress < i) {
            this.mProgress = i;
        }
        seekBar.setMax(i2 - i);
        seekBar.setProgress(this.mProgress - this.mStart);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setOnTouchListener(new View.OnTouchListener() { // from class: com.nt.settings.glyphs.widget.GlyphsBrightnessSliderPreference.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (GlyphsBrightnessSliderPreference.this.mListener == null) {
                        return false;
                    }
                    OnSeekBarPreferenceChangeListener onSeekBarPreferenceChangeListener = GlyphsBrightnessSliderPreference.this.mListener;
                    GlyphsBrightnessSliderPreference glyphsBrightnessSliderPreference = GlyphsBrightnessSliderPreference.this;
                    onSeekBarPreferenceChangeListener.onStartTrackingTouch(glyphsBrightnessSliderPreference, glyphsBrightnessSliderPreference.mProgress);
                    return false;
                } else if (motionEvent.getAction() != 3 || GlyphsBrightnessSliderPreference.this.mListener == null) {
                    return false;
                } else {
                    GlyphsBrightnessSliderPreference.this.mListener.onStopTrackingTouch(GlyphsBrightnessSliderPreference.this);
                    return false;
                }
            }
        });
    }

    public void setProgress(int i) {
        this.mProgress = i;
        notifyChanged();
    }

    public void setMaxAndMin(int i, int i2) {
        this.mMax = i2;
        this.mStart = i;
        notifyChanged();
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        int i2 = i + this.mStart;
        this.mProgress = i2;
        OnSeekBarPreferenceChangeListener onSeekBarPreferenceChangeListener = this.mListener;
        if (onSeekBarPreferenceChangeListener == null) {
            return;
        }
        onSeekBarPreferenceChangeListener.onProgressChanged(this, i2);
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        OnSeekBarPreferenceChangeListener onSeekBarPreferenceChangeListener = this.mListener;
        if (onSeekBarPreferenceChangeListener != null) {
            onSeekBarPreferenceChangeListener.onStopTrackingTouch(this);
        }
    }

    public void setOnSeekBarChangeListener(OnSeekBarPreferenceChangeListener onSeekBarPreferenceChangeListener) {
        this.mListener = onSeekBarPreferenceChangeListener;
    }
}

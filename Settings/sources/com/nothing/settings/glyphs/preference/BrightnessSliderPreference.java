package com.nothing.settings.glyphs.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$id;
import com.android.settings.R$layout;

public class BrightnessSliderPreference extends Preference implements SeekBar.OnSeekBarChangeListener {
    private OnSeekBarPreferenceChangeListener mListener;
    private int mMax = 0;
    private int mProgress = 0;
    private int mStart = 0;

    public interface OnSeekBarPreferenceChangeListener {
        void onProgressChanged(Preference preference, int i);

        void onStartTrackingTouch(Preference preference, int i);

        void onStopTrackingTouch(Preference preference);
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public BrightnessSliderPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R$layout.layout_brightness_slider);
        setSelectable(false);
    }

    public BrightnessSliderPreference(Context context) {
        super(context);
        setLayoutResource(R$layout.layout_brightness_slider);
        setSelectable(false);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        int i;
        super.onBindViewHolder(preferenceViewHolder);
        SeekBar seekBar = (SeekBar) preferenceViewHolder.findViewById(R$id.slider);
        int i2 = this.mMax;
        if (i2 >= 0 && i2 > (i = this.mStart)) {
            if (this.mProgress > i2) {
                this.mProgress = i2;
            }
            if (this.mProgress < i) {
                this.mProgress = i;
            }
            seekBar.setMax(i2 - i);
            seekBar.setProgress(this.mProgress - this.mStart);
            seekBar.setOnSeekBarChangeListener(this);
            seekBar.setOnTouchListener(new BrightnessSliderPreference$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onBindViewHolder$0(View view, MotionEvent motionEvent) {
        OnSeekBarPreferenceChangeListener onSeekBarPreferenceChangeListener;
        if (motionEvent.getAction() == 0) {
            OnSeekBarPreferenceChangeListener onSeekBarPreferenceChangeListener2 = this.mListener;
            if (onSeekBarPreferenceChangeListener2 == null) {
                return false;
            }
            onSeekBarPreferenceChangeListener2.onStartTrackingTouch(this, this.mProgress);
            return false;
        }
        if (motionEvent.getAction() == 3 && (onSeekBarPreferenceChangeListener = this.mListener) != null) {
            onSeekBarPreferenceChangeListener.onStopTrackingTouch(this);
        }
        return false;
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

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        int i2 = i + this.mStart;
        this.mProgress = i2;
        OnSeekBarPreferenceChangeListener onSeekBarPreferenceChangeListener = this.mListener;
        if (onSeekBarPreferenceChangeListener != null) {
            onSeekBarPreferenceChangeListener.onProgressChanged(this, i2);
        }
    }

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

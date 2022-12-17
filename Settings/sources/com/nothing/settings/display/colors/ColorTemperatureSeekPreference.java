package com.nothing.settings.display.colors;

import android.content.Context;
import android.provider.Settings;
import android.util.AttributeSet;
import android.widget.SeekBar;
import com.android.settings.widget.SeekBarPreference;

public class ColorTemperatureSeekPreference extends SeekBarPreference {
    private Context mContext;
    private int mMinColorTemperature;

    public ColorTemperatureSeekPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public ColorTemperatureSeekPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ColorTemperatureSeekPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "current_screen_color_temperature", this.mMinColorTemperature + seekBar.getProgress());
    }
}

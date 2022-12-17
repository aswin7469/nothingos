package com.nothing.settings.display.colors;

import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.hardware.display.ColorDisplayManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.widget.SeekBar;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$layout;
import com.android.settings.widget.SeekBarPreference;

public class ColorTemperaturePreference extends SeekBarPreference {
    private ColorDisplayManager mColorDisplayManager;
    private Context mContext;
    private int mDefaultColorTemperature;
    private int mMaxColorTemperature;
    private int mMinColorTemperature;
    private ColorTemperatureSeekBar mSeekBar;

    public ColorTemperaturePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public ColorTemperaturePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public ColorTemperaturePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ColorTemperaturePreference(Context context) {
        super(context);
        setLayoutResource(R$layout.color_temperature_preference);
        this.mColorDisplayManager = (ColorDisplayManager) context.getSystemService(ColorDisplayManager.class);
        this.mContext = context;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mSeekBar = (ColorTemperatureSeekBar) preferenceViewHolder.findViewById(16909465);
        init();
    }

    private void init() {
        Resources resources = this.mContext.getResources();
        this.mMaxColorTemperature = resources.getInteger(17694811);
        this.mMinColorTemperature = resources.getInteger(17694812);
        this.mDefaultColorTemperature = resources.getInteger(17694809);
        initializePreference();
    }

    /* access modifiers changed from: private */
    public void updateColorTemperatureByProgress(int i) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "current_screen_color_temperature", ColorTemperatureUtils.convertProgressToValue(i, this.mMinColorTemperature, this.mMaxColorTemperature, this.mDefaultColorTemperature));
    }

    private void initializePreference() {
        setContinuousUpdates(true);
        this.mSeekBar.setMin(0);
        this.mSeekBar.setMax(this.mMaxColorTemperature - this.mMinColorTemperature);
        this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                ColorTemperaturePreference.this.updateColorTemperatureByProgress(i);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                ColorTemperaturePreference.this.updateColorTemperatureByProgress(seekBar.getProgress());
            }
        });
        setHapticFeedbackMode(2);
        this.mSeekBar.setProgress(ColorTemperatureUtils.convertValueToProgress(Settings.Global.getInt(this.mContext.getContentResolver(), "current_screen_color_temperature", this.mDefaultColorTemperature), this.mMinColorTemperature, this.mMaxColorTemperature, this.mDefaultColorTemperature));
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("display_white_balance_enabled"), false, new ContentObserver(new Handler()) {
            public void onChange(boolean z, Uri uri) {
                super.onChange(z, uri);
                ColorTemperaturePreference colorTemperaturePreference = ColorTemperaturePreference.this;
                colorTemperaturePreference.setEnabled(!colorTemperaturePreference.isDisplayWhiteBalanceSettingEnabled());
            }
        }, -2);
        setEnabled(true ^ isDisplayWhiteBalanceSettingEnabled());
    }

    public boolean isDisplayWhiteBalanceSettingEnabled() {
        return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "display_white_balance_enabled", this.mContext.getResources().getBoolean(17891605) ? 1 : 0, -2) == 1;
    }
}

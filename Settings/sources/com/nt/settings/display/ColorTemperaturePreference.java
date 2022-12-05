package com.nt.settings.display;

import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.hardware.display.ColorDisplayManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.widget.SeekBar;
import androidx.preference.Preference;
import com.android.settings.widget.SeekBarPreference;
/* loaded from: classes2.dex */
public class ColorTemperaturePreference extends SeekBarPreference {
    private ColorDisplayManager mColorDisplayManager;
    private Context mContext;
    private int mDefaultColorTemperature;
    private int mMaxColorTemperature;
    private int mMinColorTemperature;

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
        this.mColorDisplayManager = (ColorDisplayManager) context.getSystemService(ColorDisplayManager.class);
        this.mContext = context;
        Resources resources = context.getResources();
        this.mMaxColorTemperature = resources.getInteger(17694803);
        this.mMinColorTemperature = resources.getInteger(17694804);
        this.mDefaultColorTemperature = resources.getInteger(17694801);
        initializePreference();
    }

    private void initializePreference() {
        setContinuousUpdates(true);
        setMax(this.mMaxColorTemperature - this.mMinColorTemperature);
        setMin(0);
        setHapticFeedbackMode(2);
        setProgress(Settings.Global.getInt(this.mContext.getContentResolver(), "current_screen_color_temperature", this.mDefaultColorTemperature) - this.mMinColorTemperature);
        setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() { // from class: com.nt.settings.display.ColorTemperaturePreference.1
            @Override // androidx.preference.Preference.OnPreferenceChangeListener
            public boolean onPreferenceChange(Preference preference, Object obj) {
                return ColorTemperaturePreference.this.mColorDisplayManager.setDisplayWhiteBalanceColorTemperature(ColorTemperaturePreference.this.mMinColorTemperature + ((Integer) obj).intValue());
            }
        });
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("display_white_balance_enabled"), false, new ContentObserver(new Handler()) { // from class: com.nt.settings.display.ColorTemperaturePreference.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                super.onChange(z, uri);
                ColorTemperaturePreference colorTemperaturePreference = ColorTemperaturePreference.this;
                colorTemperaturePreference.setEnabled(!colorTemperaturePreference.isDisplayWhiteBalanceSettingEnabled());
            }
        }, -2);
        setEnabled(true ^ isDisplayWhiteBalanceSettingEnabled());
    }

    @Override // com.android.settings.widget.SeekBarPreference, android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "current_screen_color_temperature", this.mMinColorTemperature + seekBar.getProgress());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDisplayWhiteBalanceSettingEnabled() {
        return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "display_white_balance_enabled", this.mContext.getResources().getBoolean(17891508) ? 1 : 0, -2) == 1;
    }
}

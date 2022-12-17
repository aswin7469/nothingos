package com.nothing.settings.gestures;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import androidx.lifecycle.LifecycleObserver;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$array;
import com.android.settings.R$string;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.SelectorWithWidgetPreference;
import java.util.HashMap;
import java.util.Map;

public class NavigationSettingsController extends AbstractPreferenceController implements LifecycleObserver, SelectorWithWidgetPreference.OnClickListener, PreferenceControllerMixin {
    private int mButtonNavKeyValue;
    private final Map<String, Integer> mButtonNavSettingsKeyToValueMap = new HashMap();
    private final ContentResolver mContentResolver;
    private Context mContext;
    private boolean mIsRTL;
    private OnChangeListener mOnChangeListener;
    private SelectorWithWidgetPreference mPreference;
    private final String mPreferenceKey;
    private final Resources mResources;

    public interface OnChangeListener {
        void onCheckedChanged(Preference preference);
    }

    public boolean isAvailable() {
        return true;
    }

    public NavigationSettingsController(Context context, Lifecycle lifecycle, String str) {
        super(context);
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mResources = context.getResources();
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
        this.mPreferenceKey = str;
        this.mIsRTL = context.getResources().getConfiguration().getLayoutDirection() != 1 ? false : true;
    }

    protected static int getSecureButtonNavValue(ContentResolver contentResolver, String str) {
        return Settings.Secure.getInt(contentResolver, str, 0);
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.mOnChangeListener = onChangeListener;
    }

    private Map<String, Integer> getButtonNavValueToKeyMap() {
        if (this.mButtonNavSettingsKeyToValueMap.size() == 0) {
            String[] stringArray = this.mResources.getStringArray(R$array.nt_button_nav_keys);
            int[] intArray = this.mResources.getIntArray(R$array.nt_button_nav_values);
            int length = intArray.length;
            for (int i = 0; i < length; i++) {
                this.mButtonNavSettingsKeyToValueMap.put(stringArray[i], Integer.valueOf(intArray[i]));
            }
        }
        return this.mButtonNavSettingsKeyToValueMap;
    }

    private void putSecureInt(String str, int i) {
        Settings.Secure.putInt(this.mContentResolver, str, i);
    }

    private void handlePreferenceChange(int i) {
        putSecureInt("settings_navi_bar_combination", i);
    }

    public String getPreferenceKey() {
        return this.mPreferenceKey;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (SelectorWithWidgetPreference) preferenceScreen.findPreference(getPreferenceKey());
        if ("nt_button_nav_left".equals(getPreferenceKey())) {
            this.mPreference.setTitle(this.mContext.getText(this.mIsRTL ? R$string.nt_navigation_back_button_right_side : R$string.nt_navigation_back_button_left_side));
        } else {
            this.mPreference.setTitle(this.mContext.getText(this.mIsRTL ? R$string.nt_navigation_back_button_left_side : R$string.nt_navigation_back_button_right_side));
        }
        this.mPreference.setOnClickListener(this);
    }

    public void onRadioButtonClicked(SelectorWithWidgetPreference selectorWithWidgetPreference) {
        handlePreferenceChange(getButtonNavValueToKeyMap().get(this.mPreferenceKey).intValue());
        OnChangeListener onChangeListener = this.mOnChangeListener;
        if (onChangeListener != null) {
            onChangeListener.onCheckedChanged(this.mPreference);
        }
    }

    private int getButtonNavKeyValue() {
        return getSecureButtonNavValue(this.mContentResolver, "settings_navi_bar_combination");
    }

    /* access modifiers changed from: protected */
    public void updatePreferenceCheckedState(int i) {
        if (this.mButtonNavKeyValue == i) {
            this.mPreference.setChecked(true);
        }
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        this.mButtonNavKeyValue = getButtonNavKeyValue();
        this.mPreference.setChecked(false);
        updatePreferenceCheckedState(getButtonNavValueToKeyMap().get(this.mPreference.getKey()).intValue());
    }
}

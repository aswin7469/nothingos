package com.android.systemui.p012qs;

import android.app.ActivityManager;
import android.database.ContentObserver;
import android.os.Handler;
import com.android.systemui.statusbar.policy.Listenable;
import com.android.systemui.util.settings.SettingsProxy;

/* renamed from: com.android.systemui.qs.SettingObserver */
public abstract class SettingObserver extends ContentObserver implements Listenable {
    private final int mDefaultValue;
    private boolean mListening;
    private int mObservedValue;
    private final String mSettingName;
    private final SettingsProxy mSettingsProxy;
    private int mUserId;

    /* access modifiers changed from: protected */
    public abstract void handleValueChanged(int i, boolean z);

    public SettingObserver(SettingsProxy settingsProxy, Handler handler, String str, int i) {
        this(settingsProxy, handler, str, i, 0);
    }

    public SettingObserver(SettingsProxy settingsProxy, Handler handler, String str) {
        this(settingsProxy, handler, str, ActivityManager.getCurrentUser());
    }

    public SettingObserver(SettingsProxy settingsProxy, Handler handler, String str, int i, int i2) {
        super(handler);
        this.mSettingsProxy = settingsProxy;
        this.mSettingName = str;
        this.mDefaultValue = i2;
        this.mObservedValue = i2;
        this.mUserId = i;
    }

    public int getValue() {
        return this.mListening ? this.mObservedValue : getValueFromProvider();
    }

    public void setValue(int i) {
        this.mSettingsProxy.putIntForUser(this.mSettingName, i, this.mUserId);
    }

    private int getValueFromProvider() {
        return this.mSettingsProxy.getIntForUser(this.mSettingName, this.mDefaultValue, this.mUserId);
    }

    public void setListening(boolean z) {
        if (z != this.mListening) {
            this.mListening = z;
            if (z) {
                this.mObservedValue = getValueFromProvider();
                SettingsProxy settingsProxy = this.mSettingsProxy;
                settingsProxy.registerContentObserverForUser(settingsProxy.getUriFor(this.mSettingName), false, (ContentObserver) this, this.mUserId);
                return;
            }
            this.mSettingsProxy.unregisterContentObserver(this);
            this.mObservedValue = this.mDefaultValue;
        }
    }

    public void onChange(boolean z) {
        int valueFromProvider = getValueFromProvider();
        boolean z2 = valueFromProvider != this.mObservedValue;
        this.mObservedValue = valueFromProvider;
        handleValueChanged(valueFromProvider, z2);
    }

    public void setUserId(int i) {
        this.mUserId = i;
        if (this.mListening) {
            setListening(false);
            setListening(true);
        }
    }

    public int getCurrentUser() {
        return this.mUserId;
    }

    public String getKey() {
        return this.mSettingName;
    }

    public boolean isListening() {
        return this.mListening;
    }
}

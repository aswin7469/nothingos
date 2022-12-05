package com.android.settings.development;

import android.content.Context;
import android.os.UserManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settingslib.Utils;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;
/* loaded from: classes.dex */
public class PreferVonrController extends DeveloperOptionsPreferenceController implements SubscriptionsChangeListener.SubscriptionsChangeListenerClient, LifecycleObserver {
    private SubscriptionsChangeListener mChangeListener;
    private Preference mPreference;
    private UserManager mUserManager;

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "prefer_vonr_mode";
    }

    public PreferVonrController(Context context, Lifecycle lifecycle) {
        super(context);
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        this.mChangeListener = new SubscriptionsChangeListener(context, this);
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        this.mChangeListener.start();
        update();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.mChangeListener.stop();
    }

    @Override // com.android.settingslib.development.DeveloperOptionsPreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    private void update() {
        Preference preference = this.mPreference;
        if (preference == null) {
            return;
        }
        preference.setEnabled(!this.mChangeListener.isAirplaneModeOn());
        if (!SubscriptionUtil.getAvailableSubscriptions(this.mContext).isEmpty()) {
            return;
        }
        this.mPreference.setEnabled(false);
    }

    @Override // com.android.settingslib.development.DeveloperOptionsPreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        return !Utils.isWifiOnly(this.mContext) && this.mUserManager.isAdminUser();
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onAirplaneModeChanged(boolean z) {
        update();
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onSubscriptionsChanged() {
        update();
    }
}

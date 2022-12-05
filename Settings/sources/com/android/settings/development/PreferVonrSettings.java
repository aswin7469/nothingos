package com.android.settings.development;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.ims.VolteQueryImsState;
import com.android.settingslib.RestrictedSwitchPreference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class PreferVonrSettings extends SettingsPreferenceFragment implements SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    private SubscriptionsChangeListener mChangeListener;
    private Context mContext;
    private final List<RestrictedSwitchPreference> mPreferenceList = new ArrayList();
    private PreferenceScreen mPreferenceScreen;
    private Map<Integer, RestrictedSwitchPreference> mPreferences;
    private SharedPreferences mSharedPreferences;
    private SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTelephonyManager;

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 39;
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onAirplaneModeChanged(boolean z) {
    }

    @Override // com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d("PreferVonrSettings", "onCreate");
        if (getPreferenceScreen() == null) {
            setPreferenceScreen(getPreferenceManager().createPreferenceScreen(getContext()));
        }
        this.mPreferenceScreen = getPreferenceScreen();
        addPreferencesFromResource(R.xml.prefer_vonr_list);
        Context prefContext = getPrefContext();
        this.mContext = prefContext;
        this.mSubscriptionManager = (SubscriptionManager) prefContext.getSystemService(SubscriptionManager.class);
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        this.mChangeListener = new SubscriptionsChangeListener(this.mContext, this);
        this.mPreferences = new ArrayMap();
        Context context = this.mContext;
        this.mSharedPreferences = context.getSharedPreferences(context.getPackageName(), 0);
    }

    @Override // com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Log.d("PreferVonrSettings", "onResume");
        super.onResume();
        this.mChangeListener.start();
        update();
    }

    @Override // com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        super.onPause();
        this.mChangeListener.stop();
    }

    private void update() {
        Log.d("PreferVonrSettings", "update");
        Map<Integer, RestrictedSwitchPreference> map = this.mPreferences;
        this.mPreferences = new ArrayMap();
        for (int i = 0; i < this.mTelephonyManager.getActiveModemCount(); i++) {
            SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = this.mSubscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(i);
            if (activeSubscriptionInfoForSimSlotIndex != null) {
                int subscriptionId = activeSubscriptionInfoForSimSlotIndex.getSubscriptionId();
                RestrictedSwitchPreference remove = map.remove(Integer.valueOf(subscriptionId));
                if (remove == null) {
                    remove = new RestrictedSwitchPreference(this.mContext);
                    this.mPreferenceScreen.addPreference(remove);
                }
                remove.setTitle(activeSubscriptionInfoForSimSlotIndex.getDisplayName());
                remove.setOrder(i);
                this.mPreferences.put(Integer.valueOf(subscriptionId), remove);
                this.mPreferenceList.add(remove);
                remove.setChecked(isVoNrSwitchChecked(i));
                remove.setEnabled(isVoNrSwitchEnabled(subscriptionId, i));
                maybeChangeNrCapability(i);
                Log.d("PreferVonrSettings", "add preference for slot: " + i + " subId: " + subscriptionId);
            } else {
                Log.d("PreferVonrSettings", "sub info is null, add null preference for slot: " + i);
                this.mPreferenceList.add(i, null);
            }
        }
        for (RestrictedSwitchPreference restrictedSwitchPreference : map.values()) {
            this.mPreferenceScreen.removePreference(restrictedSwitchPreference);
        }
    }

    private boolean isVoNrSwitchEnabled(int i, int i2) {
        boolean isImsOverNrEnabledByPlatform = ImsManager.getInstance(this.mContext, i2).isImsOverNrEnabledByPlatform();
        VolteQueryImsState volteQueryImsState = new VolteQueryImsState(this.mContext, i);
        return (volteQueryImsState.isVoImsOptInEnabled() || volteQueryImsState.isReadyToVoLte()) && !isImsOverNrEnabledByPlatform;
    }

    private boolean isVoNrSwitchChecked(int i) {
        ImsManager imsManager = ImsManager.getInstance(this.mContext, i);
        boolean isEnhanced4gLteModeSettingEnabledByUser = imsManager.isEnhanced4gLteModeSettingEnabledByUser();
        boolean z = false;
        if (!isEnhanced4gLteModeSettingEnabledByUser) {
            return false;
        }
        boolean isImsOverNrEnabledByPlatform = imsManager.isImsOverNrEnabledByPlatform();
        if (this.mSharedPreferences.getInt("vonr_mode_" + i, -1) == 1) {
            z = true;
        }
        Log.d("PreferVonrSettings", "enhanced 4g enabled: " + isEnhanced4gLteModeSettingEnabledByUser + ", vonr enabled by carrier: " + isImsOverNrEnabledByPlatform + ", vonr enabled by user: " + z + " for slot: " + i);
        return isImsOverNrEnabledByPlatform ? isEnhanced4gLteModeSettingEnabledByUser : z;
    }

    private void maybeChangeNrCapability(int i) {
        ImsManager imsManager = ImsManager.getInstance(this.mContext, i);
        boolean isEnhanced4gLteModeSettingEnabledByUser = imsManager.isEnhanced4gLteModeSettingEnabledByUser();
        boolean isImsOverNrEnabledByPlatform = imsManager.isImsOverNrEnabledByPlatform();
        boolean z = true;
        if (this.mSharedPreferences.getInt("vonr_mode_" + i, -1) != 1) {
            z = false;
        }
        if (isEnhanced4gLteModeSettingEnabledByUser || !z || isImsOverNrEnabledByPlatform) {
            return;
        }
        changeNrCapability(imsManager, false);
    }

    @Override // com.android.settings.core.InstrumentedPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
    public boolean onPreferenceTreeClick(Preference preference) {
        int indexOf = this.mPreferenceList.indexOf(preference);
        RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) preference;
        Log.d("PreferVonrSettings", "onPreferenceTreeClick, preference isChecked: " + restrictedSwitchPreference.isChecked() + " for slot: " + indexOf);
        ImsManager imsManager = ImsManager.getInstance(this.mContext, indexOf);
        if (!imsManager.isEnhanced4gLteModeSettingEnabledByUser()) {
            Log.d("PreferVonrSettings", "onPreferenceTreeClick, ims is disabled, ignore the request");
            return false;
        }
        changeNrCapability(imsManager, restrictedSwitchPreference.isChecked());
        savePreferenceForSlot(indexOf, restrictedSwitchPreference.isChecked());
        return super.onPreferenceTreeClick(preference);
    }

    private void changeNrCapability(ImsManager imsManager, boolean z) {
        try {
            imsManager.changeMmTelCapability(z, 1, new int[]{3});
        } catch (ImsException e) {
            Log.e("PreferVonrSettings", "Failed to change vonr mode to " + z + " since " + e);
        }
    }

    private void savePreferenceForSlot(int i, boolean z) {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences != null) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putInt("vonr_mode_" + i, z ? 1 : 0).apply();
        }
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onSubscriptionsChanged() {
        SubscriptionInfo next;
        Iterator<SubscriptionInfo> it = SubscriptionUtil.getAvailableSubscriptions(this.mContext).iterator();
        while (it.hasNext() && (next = it.next()) != null) {
            if (this.mPreferences.get(Integer.valueOf(next.getSubscriptionId())) == null) {
                Log.d("PreferVonrSettings", "sub changed, will update preference");
                update();
                return;
            }
        }
    }
}

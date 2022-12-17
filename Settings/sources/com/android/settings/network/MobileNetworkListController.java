package com.android.settings.network;

import android.content.Intent;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.ArrayMap;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R$string;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.Map;

@Deprecated
public class MobileNetworkListController extends AbstractPreferenceController implements LifecycleObserver, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    @VisibleForTesting
    static final String KEY_ADD_MORE = "add_more";
    private SubscriptionsChangeListener mChangeListener;
    private PreferenceScreen mPreferenceScreen;
    private Map<Integer, Preference> mPreferences;
    private SubscriptionManager mSubscriptionManager;

    public String getPreferenceKey() {
        return null;
    }

    public boolean isAvailable() {
        return true;
    }

    public void onAirplaneModeChanged(boolean z) {
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

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceScreen = preferenceScreen;
        preferenceScreen.findPreference(KEY_ADD_MORE).setVisible(MobileNetworkUtils.showEuiccSettings(this.mContext));
        update();
    }

    private void update() {
        if (this.mPreferenceScreen != null) {
            Map<Integer, Preference> map = this.mPreferences;
            this.mPreferences = new ArrayMap();
            for (SubscriptionInfo next : SubscriptionUtil.getAvailableSubscriptions(this.mContext)) {
                int subscriptionId = next.getSubscriptionId();
                Preference remove = map.remove(Integer.valueOf(subscriptionId));
                if (remove == null) {
                    remove = new Preference(this.mPreferenceScreen.getContext());
                    this.mPreferenceScreen.addPreference(remove);
                }
                CharSequence uniqueSubscriptionDisplayName = SubscriptionUtil.getUniqueSubscriptionDisplayName(next, this.mContext);
                remove.setTitle(uniqueSubscriptionDisplayName);
                if (!next.isEmbedded()) {
                    int phoneId = SubscriptionManager.getPhoneId(subscriptionId);
                    if (this.mSubscriptionManager.isActiveSubscriptionId(subscriptionId) && SubscriptionManager.getSimStateForSlotIndex(phoneId) != 6) {
                        remove.setSummary(R$string.mobile_network_active_sim);
                    } else if (SubscriptionUtil.showToggleForPhysicalSim(this.mSubscriptionManager)) {
                        remove.setSummary((CharSequence) this.mContext.getString(R$string.mobile_network_inactive_sim));
                    } else {
                        remove.setSummary((CharSequence) this.mContext.getString(R$string.mobile_network_tap_to_activate, new Object[]{uniqueSubscriptionDisplayName}));
                    }
                } else if (this.mSubscriptionManager.isActiveSubscriptionId(subscriptionId)) {
                    remove.setSummary(R$string.mobile_network_active_esim);
                } else {
                    remove.setSummary(R$string.mobile_network_inactive_esim);
                }
                remove.setOnPreferenceClickListener(new MobileNetworkListController$$ExternalSyntheticLambda0(this, next, subscriptionId));
                this.mPreferences.put(Integer.valueOf(subscriptionId), remove);
            }
            for (Preference removePreference : map.values()) {
                this.mPreferenceScreen.removePreference(removePreference);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$update$0(SubscriptionInfo subscriptionInfo, int i, Preference preference) {
        if (subscriptionInfo.isEmbedded() || this.mSubscriptionManager.isActiveSubscriptionId(i) || SubscriptionUtil.showToggleForPhysicalSim(this.mSubscriptionManager)) {
            Intent intent = new Intent("android.settings.NETWORK_OPERATOR_SETTINGS");
            intent.setPackage("com.android.settings");
            intent.putExtra("android.provider.extra.SUB_ID", subscriptionInfo.getSubscriptionId());
            this.mContext.startActivity(intent);
        } else {
            SubscriptionUtil.startToggleSubscriptionDialogActivity(this.mContext, i, true);
        }
        return true;
    }

    public void onSubscriptionsChanged() {
        update();
    }
}

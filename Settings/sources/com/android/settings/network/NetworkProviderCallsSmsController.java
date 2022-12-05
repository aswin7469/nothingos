package com.android.settings.network;

import android.os.UserManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.Utils;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.List;
/* loaded from: classes.dex */
public class NetworkProviderCallsSmsController extends AbstractPreferenceController implements SubscriptionsChangeListener.SubscriptionsChangeListenerClient, LifecycleObserver {
    private boolean mIsRtlMode;
    private RestrictedPreference mPreference;
    private SubscriptionManager mSubscriptionManager;
    private SubscriptionsChangeListener mSubscriptionsChangeListener;
    private TelephonyManager mTelephonyManager;
    private UserManager mUserManager;

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "calls_and_sms";
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        this.mSubscriptionsChangeListener.start();
        update();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.mSubscriptionsChangeListener.stop();
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (RestrictedPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        List<SubscriptionInfo> activeSubscriptions = SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager);
        if (activeSubscriptions.isEmpty()) {
            return setSummaryResId(R.string.calls_sms_no_sim);
        }
        StringBuilder sb = new StringBuilder();
        for (SubscriptionInfo subscriptionInfo : activeSubscriptions) {
            int size = activeSubscriptions.size();
            int subscriptionId = subscriptionInfo.getSubscriptionId();
            CharSequence uniqueSubscriptionDisplayName = MobileNetworkUtils.getUniqueSubscriptionDisplayName(subscriptionInfo, this.mContext);
            if (size == 1 && SubscriptionManager.isValidSubscriptionId(subscriptionId) && isInService(subscriptionId)) {
                return uniqueSubscriptionDisplayName;
            }
            sb.append(uniqueSubscriptionDisplayName);
            if (subscriptionInfo != activeSubscriptions.get(activeSubscriptions.size() - 1)) {
                sb.append(", ");
            }
            if (this.mIsRtlMode) {
                sb.insert(0, "\u200f").insert(sb.length(), "\u200f");
            }
        }
        return sb;
    }

    protected CharSequence getPreferredStatus(int i, int i2) {
        int i3;
        boolean z = false;
        boolean z2 = i2 == getDefaultVoiceSubscriptionId();
        if (i2 == getDefaultSmsSubscriptionId()) {
            z = true;
        }
        if (!SubscriptionManager.isValidSubscriptionId(i2) || !isInService(i2)) {
            if (i > 1) {
                i3 = R.string.calls_sms_unavailable;
            } else {
                i3 = R.string.calls_sms_temp_unavailable;
            }
            return setSummaryResId(i3);
        } else if (z2 && z) {
            return setSummaryResId(R.string.calls_sms_preferred);
        } else {
            if (z2) {
                return setSummaryResId(R.string.calls_sms_calls_preferred);
            }
            return z ? setSummaryResId(R.string.calls_sms_sms_preferred) : "";
        }
    }

    private String setSummaryResId(int i) {
        return this.mContext.getResources().getString(i);
    }

    protected int getDefaultVoiceSubscriptionId() {
        return SubscriptionManager.getDefaultVoiceSubscriptionId();
    }

    protected int getDefaultSmsSubscriptionId() {
        return SubscriptionManager.getDefaultSmsSubscriptionId();
    }

    private void update() {
        RestrictedPreference restrictedPreference = this.mPreference;
        if (restrictedPreference == null || restrictedPreference.isDisabledByAdmin()) {
            return;
        }
        refreshSummary(this.mPreference);
        this.mPreference.setOnPreferenceClickListener(null);
        this.mPreference.setFragment(null);
        if (SubscriptionUtil.getActiveSubscriptions(this.mSubscriptionManager).isEmpty()) {
            this.mPreference.setEnabled(false);
            return;
        }
        this.mPreference.setEnabled(true);
        this.mPreference.setFragment(NetworkProviderCallsSmsFragment.class.getCanonicalName());
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        return this.mUserManager.isAdminUser();
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onAirplaneModeChanged(boolean z) {
        update();
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        if (preference == null) {
            return;
        }
        refreshSummary(this.mPreference);
        update();
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onSubscriptionsChanged() {
        refreshSummary(this.mPreference);
        update();
    }

    protected boolean isInService(int i) {
        return Utils.isInService(this.mTelephonyManager.createForSubscriptionId(i).getServiceState());
    }
}

package com.android.settings.development;

import android.content.Context;
import android.content.Intent;
import android.os.UserManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.widget.AddPreference;
import com.android.settingslib.Utils;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import java.util.List;
/* loaded from: classes.dex */
public class Prefer5GNetworkSummaryController extends AbstractPreferenceController implements SubscriptionsChangeListener.SubscriptionsChangeListenerClient, LifecycleObserver, PreferenceControllerMixin {
    private SubscriptionsChangeListener mChangeListener;
    private final MetricsFeatureProvider mMetricsFeatureProvider = FeatureFactory.getFactory(this.mContext).getMetricsFeatureProvider();
    private AddPreference mPreference;
    private SubscriptionManager mSubscriptionManager;
    private UserManager mUserManager;

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "prefer_5G_nr_mode";
    }

    public Prefer5GNetworkSummaryController(Context context, Lifecycle lifecycle) {
        super(context);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        if (lifecycle != null) {
            this.mChangeListener = new SubscriptionsChangeListener(context, this);
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

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (AddPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        List<SubscriptionInfo> availableSubscriptions = SubscriptionUtil.getAvailableSubscriptions(this.mContext);
        if (availableSubscriptions.isEmpty()) {
            if (!MobileNetworkUtils.showEuiccSettings(this.mContext)) {
                return null;
            }
            return this.mContext.getResources().getString(R.string.mobile_network_summary_add_a_network);
        }
        int size = availableSubscriptions.size();
        return this.mContext.getResources().getQuantityString(R.plurals.mobile_network_summary_count, size, Integer.valueOf(size));
    }

    private void startAddSimFlow() {
        Intent intent = new Intent("android.telephony.euicc.action.PROVISION_EMBEDDED_SUBSCRIPTION");
        intent.putExtra("android.telephony.euicc.extra.FORCE_PROVISION", true);
        this.mContext.startActivity(intent);
    }

    private void update() {
        AddPreference addPreference = this.mPreference;
        if (addPreference == null || addPreference.isDisabledByAdmin()) {
            return;
        }
        refreshSummary(this.mPreference);
        this.mPreference.setOnPreferenceClickListener(null);
        this.mPreference.setOnAddClickListener(null);
        this.mPreference.setFragment(null);
        this.mPreference.setEnabled(!this.mChangeListener.isAirplaneModeOn());
        if (SubscriptionUtil.getAvailableSubscriptions(this.mContext).isEmpty()) {
            if (MobileNetworkUtils.showEuiccSettings(this.mContext)) {
                this.mPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.settings.development.Prefer5GNetworkSummaryController$$ExternalSyntheticLambda0
                    @Override // androidx.preference.Preference.OnPreferenceClickListener
                    public final boolean onPreferenceClick(Preference preference) {
                        boolean lambda$update$0;
                        lambda$update$0 = Prefer5GNetworkSummaryController.this.lambda$update$0(preference);
                        return lambda$update$0;
                    }
                });
                return;
            } else {
                this.mPreference.setEnabled(false);
                return;
            }
        }
        if (MobileNetworkUtils.showEuiccSettings(this.mContext)) {
            this.mPreference.setAddWidgetEnabled(!this.mChangeListener.isAirplaneModeOn());
            this.mPreference.setOnAddClickListener(new AddPreference.OnAddClickListener() { // from class: com.android.settings.development.Prefer5GNetworkSummaryController$$ExternalSyntheticLambda1
                @Override // com.android.settings.widget.AddPreference.OnAddClickListener
                public final void onAddClick(AddPreference addPreference2) {
                    Prefer5GNetworkSummaryController.this.lambda$update$1(addPreference2);
                }
            });
        }
        this.mPreference.setFragment(Prefer5GNetworkListFragment.class.getCanonicalName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$update$0(Preference preference) {
        this.mMetricsFeatureProvider.logClickedPreference(preference, preference.getExtras().getInt("category"));
        startAddSimFlow();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$1(AddPreference addPreference) {
        this.mMetricsFeatureProvider.logClickedPreference(addPreference, addPreference.getExtras().getInt("category"));
        startAddSimFlow();
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        return !Utils.isWifiOnly(this.mContext) && this.mUserManager.isAdminUser();
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onAirplaneModeChanged(boolean z) {
        update();
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onSubscriptionsChanged() {
        refreshSummary(this.mPreference);
        update();
    }
}

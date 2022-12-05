package com.android.settings.network.telephony.gsm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.network.AllowedNetworkTypesListener;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.Enhanced4gBasePreferenceController;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settings.network.telephony.NetworkSelectSettings;
import com.android.settings.network.telephony.TelephonyTogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class AutoSelectPreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver, Enhanced4gBasePreferenceController.On4gLteUpdateListener, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    private static final long MINIMUM_DIALOG_TIME_MILLIS = TimeUnit.SECONDS.toMillis(1);
    private AllowedNetworkTypesListener mAllowedNetworkTypesListener;
    private List<OnNetworkSelectModeListener> mListeners = new ArrayList();
    private boolean mOnlyAutoSelectInHome;
    private PreferenceScreen mPreferenceScreen;
    ProgressDialog mProgressDialog;
    private SubscriptionsChangeListener mSubscriptionsListener;
    SwitchPreference mSwitchPreference;
    private TelephonyManager mTelephonyManager;
    private final Handler mUiHandler;

    /* loaded from: classes.dex */
    public interface OnNetworkSelectModeListener {
        void onNetworkSelectModeChanged();
    }

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onAirplaneModeChanged(boolean z) {
    }

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AutoSelectPreferenceController(Context context, String str) {
        super(context, str);
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mSubId = -1;
        Handler handler = new Handler(Looper.getMainLooper());
        this.mUiHandler = handler;
        AllowedNetworkTypesListener allowedNetworkTypesListener = new AllowedNetworkTypesListener(new HandlerExecutor(handler));
        this.mAllowedNetworkTypesListener = allowedNetworkTypesListener;
        allowedNetworkTypesListener.setAllowedNetworkTypesListener(new AllowedNetworkTypesListener.OnAllowedNetworkTypesListener() { // from class: com.android.settings.network.telephony.gsm.AutoSelectPreferenceController$$ExternalSyntheticLambda0
            @Override // com.android.settings.network.AllowedNetworkTypesListener.OnAllowedNetworkTypesListener
            public final void onAllowedNetworkTypesChanged() {
                AutoSelectPreferenceController.this.lambda$new$0();
            }
        });
        this.mSubscriptionsListener = new SubscriptionsChangeListener(context, this);
    }

    @Override // com.android.settings.network.telephony.Enhanced4gBasePreferenceController.On4gLteUpdateListener
    public void on4gLteUpdated() {
        updateState(this.mSwitchPreference);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: updatePreference */
    public void lambda$new$0() {
        PreferenceScreen preferenceScreen = this.mPreferenceScreen;
        if (preferenceScreen != null) {
            displayPreference(preferenceScreen);
        }
        SwitchPreference switchPreference = this.mSwitchPreference;
        if (switchPreference != null) {
            updateState(switchPreference);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        this.mAllowedNetworkTypesListener.register(this.mContext, this.mSubId);
        this.mSubscriptionsListener.start();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        this.mAllowedNetworkTypesListener.unregister(this.mContext, this.mSubId);
        this.mSubscriptionsListener.stop();
    }

    @Override // com.android.settings.network.telephony.TelephonyTogglePreferenceController, com.android.settings.network.telephony.TelephonyAvailabilityCallback
    public int getAvailabilityStatus(int i) {
        return MobileNetworkUtils.shouldDisplayNetworkSelectOptions(this.mContext, i) ? 0 : 2;
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceScreen = preferenceScreen;
        this.mSwitchPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean isChecked() {
        return this.mTelephonyManager.getNetworkSelectionMode() == 1;
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        preference.setSummary((CharSequence) null);
        if (this.mTelephonyManager.getPhoneType() == 2) {
            preference.setEnabled(false);
            return;
        }
        ServiceState serviceState = this.mTelephonyManager.getServiceState();
        if (serviceState == null) {
            preference.setEnabled(false);
        } else if (serviceState.getRoaming()) {
            preference.setEnabled(true);
        } else {
            preference.setEnabled(!this.mOnlyAutoSelectInHome);
            if (!this.mOnlyAutoSelectInHome) {
                return;
            }
            preference.setSummary(this.mContext.getString(R.string.manual_mode_disallowed_summary, this.mTelephonyManager.getSimOperatorName()));
        }
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean setChecked(boolean z) {
        if (z) {
            setAutomaticSelectionMode();
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("android.provider.extra.SUB_ID", this.mSubId);
        new SubSettingLauncher(this.mContext).setDestination(NetworkSelectSettings.class.getName()).setSourceMetricsCategory(1581).setTitleRes(R.string.choose_network_title).setArguments(bundle).launch();
        return false;
    }

    Future setAutomaticSelectionMode() {
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        showAutoSelectProgressBar();
        this.mSwitchPreference.setEnabled(false);
        return ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.android.settings.network.telephony.gsm.AutoSelectPreferenceController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                AutoSelectPreferenceController.this.lambda$setAutomaticSelectionMode$2(elapsedRealtime);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setAutomaticSelectionMode$2(long j) {
        this.mTelephonyManager.setNetworkSelectionModeAutomatic();
        final int networkSelectionMode = this.mTelephonyManager.getNetworkSelectionMode();
        this.mUiHandler.postDelayed(new Runnable() { // from class: com.android.settings.network.telephony.gsm.AutoSelectPreferenceController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AutoSelectPreferenceController.this.lambda$setAutomaticSelectionMode$1(networkSelectionMode);
            }
        }, Math.max(MINIMUM_DIALOG_TIME_MILLIS - (SystemClock.elapsedRealtime() - j), 0L));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setAutomaticSelectionMode$1(int i) {
        boolean z = true;
        this.mSwitchPreference.setEnabled(true);
        SwitchPreference switchPreference = this.mSwitchPreference;
        if (i != 1) {
            z = false;
        }
        switchPreference.setChecked(z);
        for (OnNetworkSelectModeListener onNetworkSelectModeListener : this.mListeners) {
            onNetworkSelectModeListener.onNetworkSelectModeChanged();
        }
        dismissProgressBar();
    }

    public AutoSelectPreferenceController init(Lifecycle lifecycle, int i) {
        this.mSubId = i;
        this.mTelephonyManager = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
        PersistableBundle configForSubId = ((CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class)).getConfigForSubId(this.mSubId);
        this.mOnlyAutoSelectInHome = configForSubId != null ? configForSubId.getBoolean("only_auto_select_in_home_network") : false;
        lifecycle.addObserver(this);
        return this;
    }

    public AutoSelectPreferenceController addListener(OnNetworkSelectModeListener onNetworkSelectModeListener) {
        this.mListeners.add(onNetworkSelectModeListener);
        return this;
    }

    private void showAutoSelectProgressBar() {
        if (this.mProgressDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(this.mContext);
            this.mProgressDialog = progressDialog;
            progressDialog.setMessage(this.mContext.getResources().getString(R.string.register_automatically));
            this.mProgressDialog.setCanceledOnTouchOutside(false);
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.setIndeterminate(true);
        }
        this.mProgressDialog.show();
    }

    private void dismissProgressBar() {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        try {
            this.mProgressDialog.dismiss();
        } catch (IllegalArgumentException unused) {
        }
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onSubscriptionsChanged() {
        updateState(this.mSwitchPreference);
    }
}

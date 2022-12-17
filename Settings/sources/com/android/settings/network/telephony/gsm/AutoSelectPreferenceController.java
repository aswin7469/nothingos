package com.android.settings.network.telephony.gsm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R$string;
import com.android.settings.network.AllowedNetworkTypesListener;
import com.android.settings.network.CarrierConfigCache;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.Enhanced4gBasePreferenceController;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settings.network.telephony.TelephonyTogglePreferenceController;
import com.android.settings.network.telephony.gsm.SelectNetworkPreferenceController;
import com.android.settingslib.utils.ThreadUtils;
import com.qti.extphone.Client;
import com.qti.extphone.ExtPhoneCallbackBase;
import com.qti.extphone.ExtTelephonyManager;
import com.qti.extphone.IExtPhoneCallback;
import com.qti.extphone.NetworkSelectionMode;
import com.qti.extphone.ServiceCallback;
import com.qti.extphone.Status;
import com.qti.extphone.Token;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class AutoSelectPreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver, Enhanced4gBasePreferenceController.On4gLteUpdateListener, SubscriptionsChangeListener.SubscriptionsChangeListenerClient, SelectNetworkPreferenceController.OnNetworkScanTypeListener {
    private static final long MINIMUM_DIALOG_TIME_MILLIS = TimeUnit.SECONDS.toMillis(1);
    private static final String TAG = "AutoSelectPreferenceController";
    private AllowedNetworkTypesListener mAllowedNetworkTypesListener;
    /* access modifiers changed from: private */
    public int mCacheOfModeStatus;
    /* access modifiers changed from: private */
    public Client mClient;
    protected IExtPhoneCallback mExtPhoneCallback = new ExtPhoneCallbackBase() {
        public void getNetworkSelectionModeResponse(int i, Token token, Status status, NetworkSelectionMode networkSelectionMode) {
            Log.i(AutoSelectPreferenceController.TAG, "ExtPhoneCallback: getNetworkSelectionModeResponse");
            int i2 = status.get();
            int i3 = 1;
            if (i2 == 1) {
                try {
                    AutoSelectPreferenceController autoSelectPreferenceController = AutoSelectPreferenceController.this;
                    if (networkSelectionMode.getIsManual()) {
                        TelephonyManager unused = AutoSelectPreferenceController.this.mTelephonyManager;
                        i3 = 2;
                    } else {
                        TelephonyManager unused2 = AutoSelectPreferenceController.this.mTelephonyManager;
                    }
                    autoSelectPreferenceController.mCacheOfModeStatus = i3;
                    MobileNetworkUtils.setAccessMode(AutoSelectPreferenceController.this.mContext, i, networkSelectionMode.getAccessMode());
                } catch (Exception unused3) {
                }
            }
            synchronized (AutoSelectPreferenceController.this.mLock) {
                AutoSelectPreferenceController.this.mLock.notify();
            }
        }
    };
    private ServiceCallback mExtTelManagerServiceCallback = new ServiceCallback() {
        public void onConnected() {
            AutoSelectPreferenceController.this.mServiceConnected = true;
            AutoSelectPreferenceController autoSelectPreferenceController = AutoSelectPreferenceController.this;
            autoSelectPreferenceController.mClient = autoSelectPreferenceController.mExtTelephonyManager.registerCallback(AutoSelectPreferenceController.this.mContext.getPackageName(), AutoSelectPreferenceController.this.mExtPhoneCallback);
            Log.i(AutoSelectPreferenceController.TAG, "mExtTelManagerServiceCallback: service connected " + AutoSelectPreferenceController.this.mClient);
        }

        public void onDisconnected() {
            Log.i(AutoSelectPreferenceController.TAG, "mExtTelManagerServiceCallback: service disconnected");
            if (AutoSelectPreferenceController.this.mServiceConnected) {
                AutoSelectPreferenceController.this.mServiceConnected = false;
                AutoSelectPreferenceController.this.mClient = null;
            }
        }
    };
    /* access modifiers changed from: private */
    public ExtTelephonyManager mExtTelephonyManager;
    private List<OnNetworkSelectModeListener> mListeners;
    /* access modifiers changed from: private */
    public Object mLock = new Object();
    private boolean mOnlyAutoSelectInHome;
    private PreferenceScreen mPreferenceScreen;
    ProgressDialog mProgressDialog;
    private AtomicLong mRecursiveUpdate;
    /* access modifiers changed from: private */
    public boolean mServiceConnected;
    private SubscriptionsChangeListener mSubscriptionsListener;
    SwitchPreference mSwitchPreference;
    /* access modifiers changed from: private */
    public TelephonyManager mTelephonyManager;
    private final Handler mUiHandler;
    private AtomicBoolean mUpdatingConfig;

    public interface OnNetworkSelectModeListener {
        void onNetworkSelectModeUpdated(int i);
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public void onAirplaneModeChanged(boolean z) {
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AutoSelectPreferenceController(Context context, String str) {
        super(context, str);
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mSubId = -1;
        this.mRecursiveUpdate = new AtomicLong();
        this.mUpdatingConfig = new AtomicBoolean();
        this.mCacheOfModeStatus = 0;
        this.mListeners = new ArrayList();
        Handler handler = new Handler(Looper.getMainLooper());
        this.mUiHandler = handler;
        AllowedNetworkTypesListener allowedNetworkTypesListener = new AllowedNetworkTypesListener(new HandlerExecutor(handler));
        this.mAllowedNetworkTypesListener = allowedNetworkTypesListener;
        allowedNetworkTypesListener.setAllowedNetworkTypesListener(new AutoSelectPreferenceController$$ExternalSyntheticLambda2(this));
        this.mSubscriptionsListener = new SubscriptionsChangeListener(context, this);
    }

    public void on4gLteUpdated() {
        updateState(this.mSwitchPreference);
    }

    /* access modifiers changed from: private */
    /* renamed from: updatePreference */
    public void lambda$new$0() {
        PreferenceScreen preferenceScreen = this.mPreferenceScreen;
        if (preferenceScreen != null) {
            displayPreference(preferenceScreen);
        }
        if (this.mSwitchPreference != null) {
            this.mRecursiveUpdate.getAndIncrement();
            updateState(this.mSwitchPreference);
            this.mRecursiveUpdate.decrementAndGet();
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

    public int getAvailabilityStatus(int i) {
        return MobileNetworkUtils.shouldDisplayNetworkSelectOptions(this.mContext, i) ? 0 : 2;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceScreen = preferenceScreen;
        this.mSwitchPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public boolean isChecked() {
        if (!this.mUpdatingConfig.get()) {
            if (MobileNetworkUtils.isCagSnpnEnabled(this.mContext)) {
                synchronized (this.mLock) {
                    getNetworkSelectionMode();
                }
            } else {
                this.mCacheOfModeStatus = this.mTelephonyManager.getNetworkSelectionMode();
            }
            for (OnNetworkSelectModeListener onNetworkSelectModeUpdated : this.mListeners) {
                onNetworkSelectModeUpdated.onNetworkSelectModeUpdated(this.mCacheOfModeStatus);
            }
        }
        return this.mCacheOfModeStatus == 1;
    }

    private void getNetworkSelectionMode() {
        if (this.mServiceConnected && this.mClient != null) {
            try {
                this.mExtTelephonyManager.getNetworkSelectionMode(this.mTelephonyManager.getSlotIndex(), this.mClient);
            } catch (RuntimeException e) {
                Log.i(TAG, "Exception getNetworkSelectionMode " + e);
            }
            try {
                this.mLock.wait();
            } catch (Exception e2) {
                Log.i(TAG, "Exception :" + e2);
            }
        }
    }

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
            if (this.mOnlyAutoSelectInHome) {
                preference.setSummary((CharSequence) this.mContext.getString(R$string.manual_mode_disallowed_summary, new Object[]{this.mTelephonyManager.getSimOperatorName()}));
            }
        }
    }

    public boolean setChecked(boolean z) {
        Log.i(TAG, "isChecked = " + z);
        if (this.mRecursiveUpdate.get() != MINIMUM_DIALOG_TIME_MILLIS) {
            return true;
        }
        if (z) {
            setAutomaticSelectionMode();
            return false;
        } else if (this.mSwitchPreference == null) {
            return false;
        } else {
            Intent intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.Settings$NetworkSelectActivity");
            intent.putExtra("android.provider.extra.SUB_ID", this.mSubId);
            this.mSwitchPreference.setIntent(intent);
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public Future setAutomaticSelectionMode() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        showAutoSelectProgressBar();
        SwitchPreference switchPreference = this.mSwitchPreference;
        if (switchPreference != null) {
            switchPreference.setIntent((Intent) null);
            this.mSwitchPreference.setEnabled(false);
        }
        return ThreadUtils.postOnBackgroundThread((Runnable) new AutoSelectPreferenceController$$ExternalSyntheticLambda1(this, elapsedRealtime));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setAutomaticSelectionMode$2(long j) {
        this.mUpdatingConfig.set(true);
        this.mTelephonyManager.setNetworkSelectionModeAutomatic();
        this.mUpdatingConfig.set(false);
        this.mUiHandler.postDelayed(new AutoSelectPreferenceController$$ExternalSyntheticLambda0(this), Math.max(MINIMUM_DIALOG_TIME_MILLIS - (SystemClock.elapsedRealtime() - j), MINIMUM_DIALOG_TIME_MILLIS));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setAutomaticSelectionMode$1() {
        this.mRecursiveUpdate.getAndIncrement();
        this.mSwitchPreference.setEnabled(true);
        this.mSwitchPreference.setChecked(isChecked());
        this.mRecursiveUpdate.decrementAndGet();
        dismissProgressBar();
    }

    public AutoSelectPreferenceController init(int i) {
        this.mSubId = i;
        this.mTelephonyManager = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
        ExtTelephonyManager instance = ExtTelephonyManager.getInstance(this.mContext);
        this.mExtTelephonyManager = instance;
        instance.connectService(this.mExtTelManagerServiceCallback);
        PersistableBundle configForSubId = CarrierConfigCache.getInstance(this.mContext).getConfigForSubId(this.mSubId);
        this.mOnlyAutoSelectInHome = configForSubId != null ? configForSubId.getBoolean("only_auto_select_in_home_network") : false;
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
            progressDialog.setMessage(this.mContext.getResources().getString(R$string.register_automatically));
            this.mProgressDialog.setCanceledOnTouchOutside(false);
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.setIndeterminate(true);
        }
        this.mProgressDialog.show();
    }

    private void dismissProgressBar() {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                this.mProgressDialog.dismiss();
            } catch (IllegalArgumentException unused) {
            }
        }
    }

    public void onSubscriptionsChanged() {
        updateState(this.mSwitchPreference);
    }

    public void onNetworkScanTypeChanged(int i) {
        Log.i(TAG, "onNetworkScanTypeChanged type = " + i);
        this.mSwitchPreference.setChecked(true);
        setChecked(true);
    }
}

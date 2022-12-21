package com.android.settingslib.deviceinfo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.ims.ImsMmTelManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settingslib.C1757R;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public abstract class AbstractImsStatusPreferenceController extends AbstractConnectivityPreferenceController {
    private static final String[] CONNECTIVITY_INTENTS = {"android.bluetooth.adapter.action.STATE_CHANGED", ConnectivityManager.CONNECTIVITY_ACTION, "android.net.wifi.LINK_CONFIGURATION_CHANGED", WifiManager.NETWORK_STATE_CHANGED_ACTION};
    static final String KEY_IMS_REGISTRATION_STATE = "ims_reg_state";
    private static final String LOG_TAG = "AbstractImsPrefController";
    private static final long MAX_THREAD_BLOCKING_TIME_MS = 2000;
    private Preference mImsStatus;

    public String getPreferenceKey() {
        return KEY_IMS_REGISTRATION_STATE;
    }

    public AbstractImsStatusPreferenceController(Context context, Lifecycle lifecycle) {
        super(context, lifecycle);
    }

    public boolean isAvailable() {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
        PersistableBundle configForSubId = carrierConfigManager != null ? carrierConfigManager.getConfigForSubId(SubscriptionManager.getDefaultDataSubscriptionId()) : null;
        return configForSubId != null && configForSubId.getBoolean("show_ims_registration_status_bool");
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mImsStatus = preferenceScreen.findPreference(KEY_IMS_REGISTRATION_STATE);
        updateConnectivity();
    }

    /* access modifiers changed from: protected */
    public String[] getConnectivityIntents() {
        return CONNECTIVITY_INTENTS;
    }

    /* access modifiers changed from: protected */
    public void updateConnectivity() {
        if (this.mImsStatus != null) {
            int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
            if (!SubscriptionManager.isValidSubscriptionId(defaultDataSubscriptionId)) {
                this.mImsStatus.setSummary(C1757R.string.ims_reg_status_not_registered);
                return;
            }
            ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
            StateCallback stateCallback = new StateCallback();
            try {
                ImsMmTelManager.createForSubscriptionId(defaultDataSubscriptionId).getRegistrationState(newSingleThreadExecutor, stateCallback);
            } catch (Exception unused) {
            }
            this.mImsStatus.setSummary(stateCallback.waitUntilResult() ? C1757R.string.ims_reg_status_registered : C1757R.string.ims_reg_status_not_registered);
            try {
                newSingleThreadExecutor.shutdownNow();
            } catch (Exception unused2) {
            }
        }
    }

    private final class StateCallback extends AtomicBoolean implements Consumer<Integer> {
        private final Semaphore mSemaphore;

        private StateCallback() {
            super(false);
            this.mSemaphore = new Semaphore(0);
        }

        public void accept(Integer num) {
            set(num.intValue() == 2);
            try {
                this.mSemaphore.release();
            } catch (Exception unused) {
            }
        }

        public boolean waitUntilResult() {
            try {
                if (!this.mSemaphore.tryAcquire(2000, TimeUnit.MILLISECONDS)) {
                    Log.w(AbstractImsStatusPreferenceController.LOG_TAG, "IMS registration state query timeout");
                    return false;
                }
            } catch (Exception unused) {
            }
            return get();
        }
    }
}

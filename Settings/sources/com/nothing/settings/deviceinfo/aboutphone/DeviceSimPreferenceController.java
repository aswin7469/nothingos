package com.nothing.settings.deviceinfo.aboutphone;

import android.content.Context;
import android.content.IntentFilter;
import android.telephony.SubscriptionManager;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.deviceinfo.simstatus.SimStatusDialogFragment;
import com.nothing.settings.deviceinfo.aboutphone.DeviceSimPreference;

public class DeviceSimPreferenceController extends BasePreferenceController implements DeviceSimPreference.OnClickListener, LifecycleObserver {
    public static final String KEY = "nt_device_sims";
    public static final int SIM_0 = 0;
    public static final int SIM_0_PARENT = 21;
    public static final int SIM_1 = 1;
    public static final int SIM_1_PARENT = 22;
    public static final String TAG = "DeviceSimPreferenceController";
    private final Context mContext;
    private final Fragment mFragment;
    private final SubscriptionManager.OnSubscriptionsChangedListener mOnSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() {
        public void onSubscriptionsChanged() {
            Log.i(DeviceSimPreferenceController.TAG, "onSubscriptionsChanged");
            if (DeviceSimPreferenceController.this.mSimPreference != null) {
                DeviceSimPreferenceController.this.mSimPreference.updateState();
            }
        }
    };
    /* access modifiers changed from: private */
    public DeviceSimPreference mSimPreference;
    private final SubscriptionManager mSubscriptionManager;

    public int getAvailabilityStatus() {
        return 1;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public DeviceSimPreferenceController(Context context, Fragment fragment) {
        super(context, KEY);
        this.mContext = context;
        this.mFragment = fragment;
        if (!(fragment == null || fragment.getLifecycle() == null)) {
            fragment.getLifecycle().addObserver(this);
        }
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Log.d(TAG, "onResume");
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(this.mContext.getMainExecutor(), this.mOnSubscriptionsChangedListener);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Log.d(TAG, "onPause");
        this.mSubscriptionManager.removeOnSubscriptionsChangedListener(this.mOnSubscriptionsChangedListener);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        DeviceSimPreference deviceSimPreference = (DeviceSimPreference) preferenceScreen.findPreference(KEY);
        this.mSimPreference = deviceSimPreference;
        if (deviceSimPreference != null) {
            deviceSimPreference.setOnClickListener(this);
        }
    }

    private String getSimTitle(int i, int i2) {
        return this.mContext.getString(i, new Object[]{Integer.valueOf(i2)});
    }

    public void onClick(int i) {
        if (i == 21) {
            if (SubscriptionManager.from(this.mContext).getActiveSubscriptionInfoForSimSlotIndex(0) != null) {
                SimStatusDialogFragment.show(this.mFragment, 0, getSimTitle(R$string.sim_card_number_title, 1));
            }
        } else if (i == 22 && SubscriptionManager.from(this.mContext).getActiveSubscriptionInfoForSimSlotIndex(1) != null) {
            SimStatusDialogFragment.show(this.mFragment, 1, getSimTitle(R$string.sim_card_number_title, 2));
        }
    }
}

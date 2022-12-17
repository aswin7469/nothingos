package com.android.settings.network.telephony.gsm;

import android.content.Context;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settings.network.telephony.TelephonyTogglePreferenceController;
import com.qti.extphone.ExtTelephonyManager;
import java.util.ArrayList;
import java.util.List;

public class SelectNetworkPreferenceController extends TelephonyTogglePreferenceController implements LifecycleObserver {
    private static final String LOG_TAG = "SelectNetworkPreferenceController";
    private ExtTelephonyManager mExtTelephonyManager;
    private List<OnNetworkScanTypeListener> mListeners = new ArrayList();
    private PreferenceScreen mPreferenceScreen;
    SwitchPreference mSwitchPreference;
    private TelephonyManager mTelephonyManager;

    public interface OnNetworkScanTypeListener {
        void onNetworkScanTypeChanged(int i);
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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public SelectNetworkPreferenceController(Context context, String str) {
        super(context, str);
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mExtTelephonyManager = ExtTelephonyManager.getInstance(context);
        this.mSubId = -1;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        Log.i(LOG_TAG, "onStart");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        Log.i(LOG_TAG, "onStop");
    }

    public int getAvailabilityStatus(int i) {
        return MobileNetworkUtils.isCagSnpnEnabled(this.mContext) ? 0 : 2;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreferenceScreen = preferenceScreen;
        this.mSwitchPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public boolean isChecked() {
        return MobileNetworkUtils.getAccessMode(this.mContext, this.mTelephonyManager.getSlotIndex()) == 2;
    }

    public boolean setChecked(boolean z) {
        Log.i(LOG_TAG, "isChecked = " + z);
        int i = z ? 2 : 1;
        MobileNetworkUtils.setAccessMode(this.mContext, this.mTelephonyManager.getSlotIndex(), i);
        for (OnNetworkScanTypeListener onNetworkScanTypeChanged : this.mListeners) {
            onNetworkScanTypeChanged.onNetworkScanTypeChanged(i);
        }
        return true;
    }

    public SelectNetworkPreferenceController init(int i) {
        this.mSubId = i;
        this.mTelephonyManager = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
        return this;
    }

    public SelectNetworkPreferenceController addListener(OnNetworkScanTypeListener onNetworkScanTypeListener) {
        this.mListeners.add(onNetworkScanTypeListener);
        return this;
    }
}

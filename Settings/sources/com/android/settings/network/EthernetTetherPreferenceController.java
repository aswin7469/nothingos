package com.android.settings.network;

import android.content.Context;
import android.content.IntentFilter;
import android.net.EthernetManager;
import android.net.IpConfiguration;
import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.internal.annotations.VisibleForTesting;
import java.util.HashSet;

public final class EthernetTetherPreferenceController extends TetherBasePreferenceController {
    private final HashSet<String> mAvailableInterfaces = new HashSet<>();
    @VisibleForTesting
    EthernetManager.InterfaceStateListener mEthernetListener;
    private final EthernetManager mEthernetManager;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getTetherType() {
        return 5;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public EthernetTetherPreferenceController(Context context, String str) {
        super(context, str);
        this.mEthernetManager = (EthernetManager) context.getSystemService(EthernetManager.class);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        this.mEthernetListener = new EthernetTetherPreferenceController$$ExternalSyntheticLambda0(this);
        Handler handler = new Handler(Looper.getMainLooper());
        EthernetManager ethernetManager = this.mEthernetManager;
        if (ethernetManager != null) {
            ethernetManager.addInterfaceStateListener(new EthernetTetherPreferenceController$$ExternalSyntheticLambda1(handler), this.mEthernetListener);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onStart$0(String str, int i, int i2, IpConfiguration ipConfiguration) {
        if (i == 2) {
            this.mAvailableInterfaces.add(str);
        } else {
            this.mAvailableInterfaces.remove(str);
        }
        updateState(this.mPreference);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        EthernetManager ethernetManager = this.mEthernetManager;
        if (ethernetManager != null) {
            ethernetManager.removeInterfaceStateListener(this.mEthernetListener);
        }
    }

    public boolean shouldEnable() {
        ensureRunningOnMainLoopThread();
        for (String contains : this.mTm.getTetherableIfaces()) {
            if (this.mAvailableInterfaces.contains(contains)) {
                return true;
            }
        }
        return false;
    }

    public boolean shouldShow() {
        return this.mEthernetManager != null;
    }

    private void ensureRunningOnMainLoopThread() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Not running on main loop thread: " + Thread.currentThread().getName());
        }
    }
}

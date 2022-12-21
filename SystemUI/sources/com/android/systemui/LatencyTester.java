package com.android.systemui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Build;
import android.provider.DeviceConfig;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.p026io.PrintWriter;
import javax.inject.Inject;

@SysUISingleton
public class LatencyTester extends CoreStartable {
    private static final String ACTION_FACE_WAKE = "com.android.systemui.latency.ACTION_FACE_WAKE";
    private static final String ACTION_FINGERPRINT_WAKE = "com.android.systemui.latency.ACTION_FINGERPRINT_WAKE";
    private static final boolean DEFAULT_ENABLED = Build.IS_ENG;
    private final BiometricUnlockController mBiometricUnlockController;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (LatencyTester.ACTION_FINGERPRINT_WAKE.equals(action)) {
                LatencyTester.this.fakeWakeAndUnlock(BiometricSourceType.FINGERPRINT);
            } else if (LatencyTester.ACTION_FACE_WAKE.equals(action)) {
                LatencyTester.this.fakeWakeAndUnlock(BiometricSourceType.FACE);
            }
        }
    };
    private final DeviceConfigProxy mDeviceConfigProxy;
    private boolean mEnabled;

    @Inject
    public LatencyTester(Context context, BiometricUnlockController biometricUnlockController, BroadcastDispatcher broadcastDispatcher, DeviceConfigProxy deviceConfigProxy, @Main DelayableExecutor delayableExecutor) {
        super(context);
        this.mBiometricUnlockController = biometricUnlockController;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mDeviceConfigProxy = deviceConfigProxy;
        updateEnabled();
        deviceConfigProxy.addOnPropertiesChangedListener("latency_tracker", delayableExecutor, new LatencyTester$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-LatencyTester  reason: not valid java name */
    public /* synthetic */ void m2521lambda$new$0$comandroidsystemuiLatencyTester(DeviceConfig.Properties properties) {
        updateEnabled();
    }

    public void start() {
        registerForBroadcasts(this.mEnabled);
    }

    /* access modifiers changed from: private */
    public void fakeWakeAndUnlock(BiometricSourceType biometricSourceType) {
        if (this.mEnabled) {
            this.mBiometricUnlockController.onBiometricAcquired(biometricSourceType, 0);
            this.mBiometricUnlockController.onBiometricAuthenticated(KeyguardUpdateMonitor.getCurrentUser(), biometricSourceType, true);
        }
    }

    private void registerForBroadcasts(boolean z) {
        if (z) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_FINGERPRINT_WAKE);
            intentFilter.addAction(ACTION_FACE_WAKE);
            this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter);
            return;
        }
        this.mBroadcastDispatcher.unregisterReceiver(this.mBroadcastReceiver);
    }

    private void updateEnabled() {
        boolean z = this.mEnabled;
        boolean z2 = Build.IS_DEBUGGABLE && this.mDeviceConfigProxy.getBoolean("latency_tracker", "enabled", DEFAULT_ENABLED);
        this.mEnabled = z2;
        if (z2 != z) {
            registerForBroadcasts(z2);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("mEnabled=" + this.mEnabled);
    }
}

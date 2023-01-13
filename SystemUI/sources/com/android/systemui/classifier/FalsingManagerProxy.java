package com.android.systemui.classifier;

import android.content.Context;
import android.net.Uri;
import android.provider.DeviceConfig;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.FalsingPlugin;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.util.DeviceConfigProxy;
import java.p026io.PrintWriter;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Provider;

@SysUISingleton
public class FalsingManagerProxy implements FalsingManager, Dumpable {
    private static final String DUMPABLE_TAG = "FalsingManager";
    public static final String FALSING_REMAIN_LOCKED = "falsing_failure_after_attempts";
    public static final String FALSING_SUCCESS = "falsing_success_after_attempts";
    private final Provider<BrightLineFalsingManager> mBrightLineFalsingManagerProvider;
    private final DeviceConfigProxy mDeviceConfig;
    private final DeviceConfig.OnPropertiesChangedListener mDeviceConfigListener;
    private final DumpManager mDumpManager;
    /* access modifiers changed from: private */
    public FalsingManager mInternalFalsingManager;
    final PluginListener<FalsingPlugin> mPluginListener;
    private final PluginManager mPluginManager;

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-classifier-FalsingManagerProxy  reason: not valid java name */
    public /* synthetic */ void m2602lambda$new$0$comandroidsystemuiclassifierFalsingManagerProxy(DeviceConfig.Properties properties) {
        onDeviceConfigPropertiesChanged(properties.getNamespace());
    }

    @Inject
    FalsingManagerProxy(PluginManager pluginManager, @Main Executor executor, DeviceConfigProxy deviceConfigProxy, DumpManager dumpManager, Provider<BrightLineFalsingManager> provider) {
        FalsingManagerProxy$$ExternalSyntheticLambda0 falsingManagerProxy$$ExternalSyntheticLambda0 = new FalsingManagerProxy$$ExternalSyntheticLambda0(this);
        this.mDeviceConfigListener = falsingManagerProxy$$ExternalSyntheticLambda0;
        this.mPluginManager = pluginManager;
        this.mDumpManager = dumpManager;
        this.mDeviceConfig = deviceConfigProxy;
        this.mBrightLineFalsingManagerProvider = provider;
        setupFalsingManager();
        deviceConfigProxy.addOnPropertiesChangedListener("systemui", executor, falsingManagerProxy$$ExternalSyntheticLambda0);
        C20141 r3 = new PluginListener<FalsingPlugin>() {
            public void onPluginConnected(FalsingPlugin falsingPlugin, Context context) {
                FalsingManager falsingManager = falsingPlugin.getFalsingManager(context);
                if (falsingManager != null) {
                    FalsingManagerProxy.this.mInternalFalsingManager.cleanupInternal();
                    FalsingManager unused = FalsingManagerProxy.this.mInternalFalsingManager = falsingManager;
                }
            }

            public void onPluginDisconnected(FalsingPlugin falsingPlugin) {
                FalsingManagerProxy.this.setupFalsingManager();
            }
        };
        this.mPluginListener = r3;
        pluginManager.addPluginListener(r3, FalsingPlugin.class);
        dumpManager.registerDumpable(DUMPABLE_TAG, this);
    }

    private void onDeviceConfigPropertiesChanged(String str) {
        if ("systemui".equals(str)) {
            setupFalsingManager();
        }
    }

    /* access modifiers changed from: private */
    public void setupFalsingManager() {
        FalsingManager falsingManager = this.mInternalFalsingManager;
        if (falsingManager != null) {
            falsingManager.cleanupInternal();
        }
        this.mInternalFalsingManager = this.mBrightLineFalsingManagerProvider.get();
    }

    public void onSuccessfulUnlock() {
        this.mInternalFalsingManager.onSuccessfulUnlock();
    }

    public boolean isUnlockingDisabled() {
        return this.mInternalFalsingManager.isUnlockingDisabled();
    }

    public boolean isFalseTouch(int i) {
        return this.mInternalFalsingManager.isFalseTouch(i);
    }

    public boolean isSimpleTap() {
        return this.mInternalFalsingManager.isSimpleTap();
    }

    public boolean isFalseTap(int i) {
        return this.mInternalFalsingManager.isFalseTap(i);
    }

    public boolean isFalseDoubleTap() {
        return this.mInternalFalsingManager.isFalseDoubleTap();
    }

    public boolean isClassifierEnabled() {
        return this.mInternalFalsingManager.isClassifierEnabled();
    }

    public boolean shouldEnforceBouncer() {
        return this.mInternalFalsingManager.shouldEnforceBouncer();
    }

    public Uri reportRejectedTouch() {
        return this.mInternalFalsingManager.reportRejectedTouch();
    }

    public boolean isReportingEnabled() {
        return this.mInternalFalsingManager.isReportingEnabled();
    }

    public void addFalsingBeliefListener(FalsingManager.FalsingBeliefListener falsingBeliefListener) {
        this.mInternalFalsingManager.addFalsingBeliefListener(falsingBeliefListener);
    }

    public void removeFalsingBeliefListener(FalsingManager.FalsingBeliefListener falsingBeliefListener) {
        this.mInternalFalsingManager.removeFalsingBeliefListener(falsingBeliefListener);
    }

    public void addTapListener(FalsingManager.FalsingTapListener falsingTapListener) {
        this.mInternalFalsingManager.addTapListener(falsingTapListener);
    }

    public void removeTapListener(FalsingManager.FalsingTapListener falsingTapListener) {
        this.mInternalFalsingManager.removeTapListener(falsingTapListener);
    }

    public void onProximityEvent(FalsingManager.ProximityEvent proximityEvent) {
        this.mInternalFalsingManager.onProximityEvent(proximityEvent);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        this.mInternalFalsingManager.dump(printWriter, strArr);
    }

    public void cleanupInternal() {
        this.mDeviceConfig.removeOnPropertiesChangedListener(this.mDeviceConfigListener);
        this.mPluginManager.removePluginListener(this.mPluginListener);
        this.mDumpManager.unregisterDumpable(DUMPABLE_TAG);
        this.mInternalFalsingManager.cleanupInternal();
    }
}

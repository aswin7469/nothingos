package com.android.systemui.navigationbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.om.IOverlayManager;
import android.content.pm.PackageManager;
import android.content.res.ApkAssets;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class NavigationModeController implements Dumpable {
    private static final String TAG = "NavigationModeController";
    private final Context mContext;
    private Context mCurrentUserContext;
    private final DeviceProvisionedController.DeviceProvisionedListener mDeviceProvisionedCallback;
    private final Executor mUiBgExecutor;
    private ArrayList<ModeChangedListener> mListeners = new ArrayList<>();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.navigationbar.NavigationModeController.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Log.d(NavigationModeController.TAG, "ACTION_OVERLAY_CHANGED");
            NavigationModeController.this.updateCurrentInteractionMode(true);
        }
    };
    private final IOverlayManager mOverlayManager = IOverlayManager.Stub.asInterface(ServiceManager.getService("overlay"));

    /* loaded from: classes.dex */
    public interface ModeChangedListener {
        void onNavigationModeChanged(int i);
    }

    public NavigationModeController(Context context, DeviceProvisionedController deviceProvisionedController, ConfigurationController configurationController, Executor executor) {
        DeviceProvisionedController.DeviceProvisionedListener deviceProvisionedListener = new DeviceProvisionedController.DeviceProvisionedListener() { // from class: com.android.systemui.navigationbar.NavigationModeController.1
            @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
            public void onUserSwitched() {
                String str = NavigationModeController.TAG;
                Log.d(str, "onUserSwitched: " + ActivityManagerWrapper.getInstance().getCurrentUserId());
                NavigationModeController.this.reloadNavOverlay();
                NavigationModeController.this.updateCurrentInteractionMode(true);
            }
        };
        this.mDeviceProvisionedCallback = deviceProvisionedListener;
        this.mContext = context;
        this.mCurrentUserContext = context;
        this.mUiBgExecutor = executor;
        deviceProvisionedController.addCallback(deviceProvisionedListener);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.OVERLAY_CHANGED");
        intentFilter.addDataScheme("package");
        intentFilter.addDataSchemeSpecificPart("android", 0);
        context.registerReceiverAsUser(this.mReceiver, UserHandle.ALL, intentFilter, null, null);
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.navigationbar.NavigationModeController.3
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onOverlayChanged() {
                Log.d(NavigationModeController.TAG, "onOverlayChanged");
                NavigationModeController.this.updateCurrentInteractionMode(true);
            }
        });
        updateCurrentInteractionMode(false);
        reloadNavOverlay();
    }

    public void updateCurrentInteractionMode(boolean z) {
        Context currentUserContext = getCurrentUserContext();
        this.mCurrentUserContext = currentUserContext;
        final int currentInteractionMode = getCurrentInteractionMode(currentUserContext);
        this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.navigationbar.NavigationModeController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NavigationModeController.this.lambda$updateCurrentInteractionMode$0(currentInteractionMode);
            }
        });
        String str = TAG;
        Log.d(str, "updateCurrentInteractionMode: mode=" + currentInteractionMode);
        dumpAssetPaths(this.mCurrentUserContext);
        if (z) {
            for (int i = 0; i < this.mListeners.size(); i++) {
                this.mListeners.get(i).onNavigationModeChanged(currentInteractionMode);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateCurrentInteractionMode$0(int i) {
        Settings.Secure.putString(this.mCurrentUserContext.getContentResolver(), "navigation_mode", String.valueOf(i));
    }

    public int addListener(ModeChangedListener modeChangedListener) {
        this.mListeners.add(modeChangedListener);
        return getCurrentInteractionMode(this.mCurrentUserContext);
    }

    public void removeListener(ModeChangedListener modeChangedListener) {
        this.mListeners.remove(modeChangedListener);
    }

    private int getCurrentInteractionMode(Context context) {
        int integer = context.getResources().getInteger(17694868);
        String str = TAG;
        Log.d(str, "getCurrentInteractionMode: mode=" + integer + " contextUser=" + context.getUserId());
        return integer;
    }

    public Context getCurrentUserContext() {
        int currentUserId = ActivityManagerWrapper.getInstance().getCurrentUserId();
        String str = TAG;
        Log.d(str, "getCurrentUserContext: contextUser=" + this.mContext.getUserId() + " currentUser=" + currentUserId);
        if (this.mContext.getUserId() == currentUserId) {
            return this.mContext;
        }
        try {
            Context context = this.mContext;
            return context.createPackageContextAsUser(context.getPackageName(), 0, UserHandle.of(currentUserId));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to create package context", e);
            return null;
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        printWriter.println("NavigationModeController:");
        printWriter.println("  mode=" + getCurrentInteractionMode(this.mCurrentUserContext));
        try {
            str = String.join(", ", this.mOverlayManager.getDefaultOverlayPackages());
        } catch (RemoteException unused) {
            str = "failed_to_fetch";
        }
        printWriter.println("  defaultOverlays=" + str);
        dumpAssetPaths(this.mCurrentUserContext);
    }

    private void dumpAssetPaths(Context context) {
        ApkAssets[] apkAssets;
        String str = TAG;
        Log.d(str, "  contextUser=" + this.mCurrentUserContext.getUserId());
        Log.d(str, "  assetPaths=");
        for (ApkAssets apkAssets2 : context.getResources().getAssets().getApkAssets()) {
            Log.d(TAG, "    " + apkAssets2.getDebugName());
        }
    }

    public void reloadNavOverlay() {
        Context currentUserContext = getCurrentUserContext();
        this.mCurrentUserContext = currentUserContext;
        if (getCurrentInteractionMode(currentUserContext) == 2 && this.mOverlayManager != null) {
            int userId = this.mCurrentUserContext.getUserId();
            try {
                this.mOverlayManager.setEnabledExclusiveInCategory("com.android.internal.systemui.navbar.threebutton", userId);
                this.mOverlayManager.setEnabledExclusiveInCategory("com.android.internal.systemui.navbar.gestural", userId);
            } catch (RemoteException unused) {
                String str = TAG;
                Log.e(str, "Failed to reload nav overlay for user " + userId);
            }
        }
    }
}

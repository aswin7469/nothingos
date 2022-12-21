package com.android.systemui.navigationbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.om.IOverlayManager;
import android.content.pm.PackageManager;
import android.content.res.ApkAssets;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import androidx.slice.core.SliceHints;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.UiBackground;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.navigationbar.NavigationModeControllerEx;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@SysUISingleton
public class NavigationModeController implements Dumpable {
    private static final boolean DEBUG = true;
    /* access modifiers changed from: private */
    public static final String TAG = "NavigationModeController";
    private final Context mContext;
    /* access modifiers changed from: private */
    public Context mCurrentUserContext;
    private final DeviceProvisionedController.DeviceProvisionedListener mDeviceProvisionedCallback;
    /* access modifiers changed from: private */
    public NavigationModeControllerEx mEx;
    private ArrayList<ModeChangedListener> mListeners = new ArrayList<>();
    /* access modifiers changed from: private */
    public final IOverlayManager mOverlayManager;
    private BroadcastReceiver mReceiver;
    private final Executor mUiBgExecutor;

    public interface ModeChangedListener {
        void onNavigationModeChanged(int i);
    }

    @Inject
    public NavigationModeController(Context context, DeviceProvisionedController deviceProvisionedController, ConfigurationController configurationController, @UiBackground Executor executor, DumpManager dumpManager) {
        C22611 r0 = new DeviceProvisionedController.DeviceProvisionedListener() {
            public void onUserSwitched() {
                Log.d(NavigationModeController.TAG, "onUserSwitched: " + ActivityManagerWrapper.getInstance().getCurrentUserId());
                NavigationModeController.this.updateCurrentInteractionMode(true);
                NavigationModeControllerEx access$400 = NavigationModeController.this.mEx;
                Context access$100 = NavigationModeController.this.mCurrentUserContext;
                IOverlayManager access$200 = NavigationModeController.this.mOverlayManager;
                NavigationModeController navigationModeController = NavigationModeController.this;
                access$400.updateNavModeOverlay(access$100, access$200, navigationModeController.getCurrentInteractionMode(navigationModeController.mCurrentUserContext));
            }
        };
        this.mDeviceProvisionedCallback = r0;
        this.mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Log.d(NavigationModeController.TAG, "ACTION_OVERLAY_CHANGED");
                NavigationModeController.this.updateCurrentInteractionMode(true);
            }
        };
        this.mContext = context;
        this.mCurrentUserContext = context;
        IOverlayManager asInterface = IOverlayManager.Stub.asInterface(ServiceManager.getService(SliceHints.HINT_OVERLAY));
        this.mOverlayManager = asInterface;
        this.mUiBgExecutor = executor;
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
        deviceProvisionedController.addCallback(r0);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.OVERLAY_CHANGED");
        intentFilter.addDataScheme("package");
        intentFilter.addDataSchemeSpecificPart("android", 0);
        context.registerReceiverAsUser(this.mReceiver, UserHandle.ALL, intentFilter, (String) null, (Handler) null);
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() {
            public void onThemeChanged() {
                Log.d(NavigationModeController.TAG, "onOverlayChanged");
                NavigationModeController.this.updateCurrentInteractionMode(true);
            }
        });
        updateCurrentInteractionMode(false);
        NavigationModeControllerEx navigationModeControllerEx = (NavigationModeControllerEx) NTDependencyEx.get(NavigationModeControllerEx.class);
        this.mEx = navigationModeControllerEx;
        Context context2 = this.mCurrentUserContext;
        navigationModeControllerEx.updateNavModeOverlay(context2, asInterface, getCurrentInteractionMode(context2));
    }

    public void updateCurrentInteractionMode(boolean z) {
        Context currentUserContext = getCurrentUserContext();
        this.mCurrentUserContext = currentUserContext;
        int currentInteractionMode = getCurrentInteractionMode(currentUserContext);
        this.mUiBgExecutor.execute(new NavigationModeController$$ExternalSyntheticLambda0(this, currentInteractionMode));
        Log.d(TAG, "updateCurrentInteractionMode: mode=" + currentInteractionMode);
        dumpAssetPaths(this.mCurrentUserContext);
        if (z) {
            for (int i = 0; i < this.mListeners.size(); i++) {
                this.mListeners.get(i).onNavigationModeChanged(currentInteractionMode);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateCurrentInteractionMode$0$com-android-systemui-navigationbar-NavigationModeController */
    public /* synthetic */ void mo34846x24e40a4b(int i) {
        Settings.Secure.putString(this.mCurrentUserContext.getContentResolver(), "navigation_mode", String.valueOf(i));
    }

    public int addListener(ModeChangedListener modeChangedListener) {
        this.mListeners.add(modeChangedListener);
        return getCurrentInteractionMode(this.mCurrentUserContext);
    }

    public void removeListener(ModeChangedListener modeChangedListener) {
        this.mListeners.remove((Object) modeChangedListener);
    }

    public boolean getImeDrawsImeNavBar() {
        return this.mCurrentUserContext.getResources().getBoolean(17891683);
    }

    /* access modifiers changed from: private */
    public int getCurrentInteractionMode(Context context) {
        int integer = context.getResources().getInteger(17694882);
        Log.d(TAG, "getCurrentInteractionMode: mode=" + integer + " contextUser=" + context.getUserId());
        return integer;
    }

    public Context getCurrentUserContext() {
        int currentUserId = ActivityManagerWrapper.getInstance().getCurrentUserId();
        Log.d(TAG, "getCurrentUserContext: contextUser=" + this.mContext.getUserId() + " currentUser=" + currentUserId);
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

    public void dump(PrintWriter printWriter, String[] strArr) {
        String str;
        printWriter.println("NavigationModeController:");
        printWriter.println("  mode=" + getCurrentInteractionMode(this.mCurrentUserContext));
        try {
            str = String.join((CharSequence) ", ", (CharSequence[]) this.mOverlayManager.getDefaultOverlayPackages());
        } catch (RemoteException unused) {
            str = "failed_to_fetch";
        }
        printWriter.println("  defaultOverlays=" + str);
        dumpAssetPaths(this.mCurrentUserContext);
    }

    private void dumpAssetPaths(Context context) {
        String str = TAG;
        Log.d(str, "  contextUser=" + this.mCurrentUserContext.getUserId());
        Log.d(str, "  assetPaths=");
        ApkAssets[] apkAssets = context.getResources().getAssets().getApkAssets();
        int length = apkAssets.length;
        for (int i = 0; i < length; i++) {
            Log.d(TAG, "    " + apkAssets[i].getDebugName());
        }
    }
}

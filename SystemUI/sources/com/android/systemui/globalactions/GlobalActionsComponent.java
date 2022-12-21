package com.android.systemui.globalactions;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.CoreStartable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.plugins.GlobalActions;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.ExtensionController;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Provider;

@SysUISingleton
public class GlobalActionsComponent extends CoreStartable implements CommandQueue.Callbacks, GlobalActions.GlobalActionsManager {
    private IStatusBarService mBarService;
    private final CommandQueue mCommandQueue;
    private ExtensionController.Extension<GlobalActions> mExtension;
    private final ExtensionController mExtensionController;
    private final Provider<GlobalActions> mGlobalActionsProvider;
    private GlobalActions mPlugin;
    private StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;

    @Inject
    public GlobalActionsComponent(Context context, CommandQueue commandQueue, ExtensionController extensionController, Provider<GlobalActions> provider, StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        super(context);
        this.mCommandQueue = commandQueue;
        this.mExtensionController = extensionController;
        this.mGlobalActionsProvider = provider;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
    }

    public void start() {
        this.mBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
        ExtensionController.ExtensionBuilder<GlobalActions> withPlugin = this.mExtensionController.newExtension(GlobalActions.class).withPlugin(GlobalActions.class);
        Provider<GlobalActions> provider = this.mGlobalActionsProvider;
        Objects.requireNonNull(provider);
        ExtensionController.Extension<GlobalActions> build = withPlugin.withDefault(new GlobalActionsComponent$$ExternalSyntheticLambda0(provider)).withCallback(new GlobalActionsComponent$$ExternalSyntheticLambda1(this)).build();
        this.mExtension = build;
        this.mPlugin = build.get();
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
    }

    /* access modifiers changed from: private */
    public void onExtensionCallback(GlobalActions globalActions) {
        GlobalActions globalActions2 = this.mPlugin;
        if (globalActions2 != null) {
            globalActions2.destroy();
        }
        this.mPlugin = globalActions;
    }

    public void handleShowShutdownUi(boolean z, String str) {
        this.mExtension.get().showShutdownUi(z, str);
    }

    public void handleShowGlobalActionsMenu() {
        this.mStatusBarKeyguardViewManager.setGlobalActionsVisible(true);
        this.mExtension.get().showGlobalActions(this);
    }

    public void onGlobalActionsShown() {
        try {
            this.mBarService.onGlobalActionsShown();
        } catch (RemoteException unused) {
        }
    }

    public void onGlobalActionsHidden() {
        try {
            this.mStatusBarKeyguardViewManager.setGlobalActionsVisible(false);
            this.mBarService.onGlobalActionsHidden();
        } catch (RemoteException unused) {
        }
    }

    public void shutdown() {
        try {
            this.mBarService.shutdown();
        } catch (RemoteException unused) {
        }
    }

    public void reboot(boolean z) {
        try {
            this.mBarService.reboot(z);
        } catch (RemoteException unused) {
        }
    }
}

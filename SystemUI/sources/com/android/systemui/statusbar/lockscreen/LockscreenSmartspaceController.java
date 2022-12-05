package com.android.systemui.statusbar.lockscreen;

import android.app.PendingIntent;
import android.app.smartspace.SmartspaceConfig;
import android.app.smartspace.SmartspaceManager;
import android.app.smartspace.SmartspaceSession;
import android.app.smartspace.SmartspaceTarget;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;
import android.view.View;
import android.view.ViewGroup;
import com.android.settingslib.Utils;
import com.android.systemui.R$attr;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: LockscreenSmartspaceController.kt */
/* loaded from: classes.dex */
public final class LockscreenSmartspaceController {
    @NotNull
    private final ActivityStarter activityStarter;
    @NotNull
    private final ConfigurationController configurationController;
    @NotNull
    private final ContentResolver contentResolver;
    @NotNull
    private final Context context;
    @NotNull
    private final DeviceProvisionedController deviceProvisionedController;
    @NotNull
    private final LockscreenSmartspaceController$deviceProvisionedListener$1 deviceProvisionedListener;
    @NotNull
    private final Execution execution;
    @NotNull
    private final FalsingManager falsingManager;
    @NotNull
    private final FeatureFlags featureFlags;
    @NotNull
    private final Handler handler;
    @Nullable
    private UserHandle managedUserHandle;
    @Nullable
    private final BcSmartspaceDataPlugin plugin;
    @NotNull
    private final SecureSettings secureSettings;
    @Nullable
    private SmartspaceSession session;
    @NotNull
    private final LockscreenSmartspaceController$settingsObserver$1 settingsObserver;
    private boolean showSensitiveContentForCurrentUser;
    private boolean showSensitiveContentForManagedUser;
    @NotNull
    private final SmartspaceManager smartspaceManager;
    private BcSmartspaceDataPlugin.SmartspaceView smartspaceView;
    @NotNull
    private final StatusBarStateController statusBarStateController;
    @NotNull
    private final Executor uiExecutor;
    @NotNull
    private final UserTracker userTracker;
    private View view;
    @NotNull
    private final SmartspaceSession.OnTargetsAvailableListener sessionListener = new SmartspaceSession.OnTargetsAvailableListener() { // from class: com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$sessionListener$1
        public final void onTargetsAvailable(List<SmartspaceTarget> targets) {
            Execution execution;
            BcSmartspaceDataPlugin bcSmartspaceDataPlugin;
            boolean filterSmartspaceTarget;
            execution = LockscreenSmartspaceController.this.execution;
            execution.assertIsMainThread();
            Intrinsics.checkNotNullExpressionValue(targets, "targets");
            LockscreenSmartspaceController lockscreenSmartspaceController = LockscreenSmartspaceController.this;
            ArrayList arrayList = new ArrayList();
            for (Object obj : targets) {
                filterSmartspaceTarget = lockscreenSmartspaceController.filterSmartspaceTarget((SmartspaceTarget) obj);
                if (filterSmartspaceTarget) {
                    arrayList.add(obj);
                }
            }
            bcSmartspaceDataPlugin = LockscreenSmartspaceController.this.plugin;
            if (bcSmartspaceDataPlugin == null) {
                return;
            }
            bcSmartspaceDataPlugin.onTargetsAvailable(arrayList);
        }
    };
    @NotNull
    private final LockscreenSmartspaceController$userTrackerCallback$1 userTrackerCallback = new UserTracker.Callback() { // from class: com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$userTrackerCallback$1
        @Override // com.android.systemui.settings.UserTracker.Callback
        public void onUserChanged(int i, @NotNull Context userContext) {
            Execution execution;
            Intrinsics.checkNotNullParameter(userContext, "userContext");
            execution = LockscreenSmartspaceController.this.execution;
            execution.assertIsMainThread();
            LockscreenSmartspaceController.this.reloadSmartspace();
        }
    };
    @NotNull
    private final LockscreenSmartspaceController$configChangeListener$1 configChangeListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$configChangeListener$1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onThemeChanged() {
            Execution execution;
            execution = LockscreenSmartspaceController.this.execution;
            execution.assertIsMainThread();
            LockscreenSmartspaceController.this.updateTextColorFromWallpaper();
        }
    };
    @NotNull
    private final LockscreenSmartspaceController$statusBarStateListener$1 statusBarStateListener = new LockscreenSmartspaceController$statusBarStateListener$1(this);

    /* JADX WARN: Type inference failed for: r1v4, types: [java.lang.Object, com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$deviceProvisionedListener$1] */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$userTrackerCallback$1] */
    /* JADX WARN: Type inference failed for: r2v3, types: [com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$settingsObserver$1] */
    /* JADX WARN: Type inference failed for: r2v4, types: [com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$configChangeListener$1] */
    public LockscreenSmartspaceController(@NotNull Context context, @NotNull FeatureFlags featureFlags, @NotNull SmartspaceManager smartspaceManager, @NotNull ActivityStarter activityStarter, @NotNull FalsingManager falsingManager, @NotNull SecureSettings secureSettings, @NotNull UserTracker userTracker, @NotNull ContentResolver contentResolver, @NotNull ConfigurationController configurationController, @NotNull StatusBarStateController statusBarStateController, @NotNull DeviceProvisionedController deviceProvisionedController, @NotNull Execution execution, @NotNull Executor uiExecutor, @NotNull final Handler handler, @NotNull Optional<BcSmartspaceDataPlugin> optionalPlugin) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(featureFlags, "featureFlags");
        Intrinsics.checkNotNullParameter(smartspaceManager, "smartspaceManager");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(secureSettings, "secureSettings");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        Intrinsics.checkNotNullParameter(contentResolver, "contentResolver");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(deviceProvisionedController, "deviceProvisionedController");
        Intrinsics.checkNotNullParameter(execution, "execution");
        Intrinsics.checkNotNullParameter(uiExecutor, "uiExecutor");
        Intrinsics.checkNotNullParameter(handler, "handler");
        Intrinsics.checkNotNullParameter(optionalPlugin, "optionalPlugin");
        this.context = context;
        this.featureFlags = featureFlags;
        this.smartspaceManager = smartspaceManager;
        this.activityStarter = activityStarter;
        this.falsingManager = falsingManager;
        this.secureSettings = secureSettings;
        this.userTracker = userTracker;
        this.contentResolver = contentResolver;
        this.configurationController = configurationController;
        this.statusBarStateController = statusBarStateController;
        this.deviceProvisionedController = deviceProvisionedController;
        this.execution = execution;
        this.uiExecutor = uiExecutor;
        this.handler = handler;
        this.plugin = optionalPlugin.orElse(null);
        ?? r1 = new DeviceProvisionedController.DeviceProvisionedListener() { // from class: com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$deviceProvisionedListener$1
            @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
            public void onDeviceProvisionedChanged() {
                LockscreenSmartspaceController.this.connectSession();
            }

            @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
            public void onUserSetupChanged() {
                LockscreenSmartspaceController.this.connectSession();
            }
        };
        this.deviceProvisionedListener = r1;
        this.settingsObserver = new ContentObserver(handler) { // from class: com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$settingsObserver$1
            @Override // android.database.ContentObserver
            public void onChange(boolean z, @Nullable Uri uri) {
                Execution execution2;
                execution2 = LockscreenSmartspaceController.this.execution;
                execution2.assertIsMainThread();
                LockscreenSmartspaceController.this.reloadSmartspace();
            }
        };
        deviceProvisionedController.addCallback(r1);
    }

    @NotNull
    public final View getView() {
        View view = this.view;
        if (view != null) {
            return view;
        }
        Intrinsics.throwUninitializedPropertyAccessException("view");
        throw null;
    }

    public final boolean isEnabled() {
        this.execution.assertIsMainThread();
        return this.featureFlags.isSmartspaceEnabled() && this.plugin != null;
    }

    @NotNull
    public final View buildAndConnectView(@NotNull ViewGroup parent) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        this.execution.assertIsMainThread();
        if (!isEnabled()) {
            throw new RuntimeException("Cannot build view when not enabled");
        }
        buildView(parent);
        connectSession();
        return getView();
    }

    public final void requestSmartspaceUpdate() {
        SmartspaceSession smartspaceSession = this.session;
        if (smartspaceSession == null) {
            return;
        }
        smartspaceSession.requestSmartspaceUpdate();
    }

    private final void buildView(ViewGroup viewGroup) {
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
        if (bcSmartspaceDataPlugin == null) {
            return;
        }
        if (this.view != null) {
            ViewGroup viewGroup2 = (ViewGroup) getView().getParent();
            if (viewGroup2 == null) {
                return;
            }
            viewGroup2.removeView(getView());
            return;
        }
        BcSmartspaceDataPlugin.SmartspaceView ssView = bcSmartspaceDataPlugin.getView(viewGroup);
        ssView.registerDataProvider(this.plugin);
        ssView.setIntentStarter(new BcSmartspaceDataPlugin.IntentStarter() { // from class: com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$buildView$2
            @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.IntentStarter
            public void startIntent(@Nullable View view, @Nullable Intent intent) {
                ActivityStarter activityStarter;
                activityStarter = LockscreenSmartspaceController.this.activityStarter;
                activityStarter.startActivity(intent, true);
            }

            @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.IntentStarter
            public void startPendingIntent(@Nullable PendingIntent pendingIntent) {
                ActivityStarter activityStarter;
                activityStarter = LockscreenSmartspaceController.this.activityStarter;
                activityStarter.startPendingIntentDismissingKeyguard(pendingIntent);
            }
        });
        ssView.setFalsingManager(this.falsingManager);
        Intrinsics.checkNotNullExpressionValue(ssView, "ssView");
        this.smartspaceView = ssView;
        this.view = (View) ssView;
        updateTextColorFromWallpaper();
        this.statusBarStateListener.onDozeAmountChanged(0.0f, this.statusBarStateController.getDozeAmount());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void connectSession() {
        if (this.plugin == null || this.session != null || this.smartspaceView == null || !this.deviceProvisionedController.isDeviceProvisioned() || !this.deviceProvisionedController.isCurrentUserSetup()) {
            return;
        }
        SmartspaceSession createSmartspaceSession = this.smartspaceManager.createSmartspaceSession(new SmartspaceConfig.Builder(this.context, "lockscreen").build());
        createSmartspaceSession.addOnTargetsAvailableListener(this.uiExecutor, this.sessionListener);
        this.session = createSmartspaceSession;
        this.deviceProvisionedController.removeCallback(this.deviceProvisionedListener);
        this.userTracker.addCallback(this.userTrackerCallback, this.uiExecutor);
        this.contentResolver.registerContentObserver(this.secureSettings.getUriFor("lock_screen_allow_private_notifications"), true, this.settingsObserver, -1);
        this.configurationController.addCallback(this.configChangeListener);
        this.statusBarStateController.addCallback(this.statusBarStateListener);
        reloadSmartspace();
    }

    public final void disconnect() {
        List<SmartspaceTarget> emptyList;
        this.execution.assertIsMainThread();
        SmartspaceSession smartspaceSession = this.session;
        if (smartspaceSession == null) {
            return;
        }
        if (smartspaceSession != null) {
            smartspaceSession.removeOnTargetsAvailableListener(this.sessionListener);
            smartspaceSession.close();
        }
        this.userTracker.removeCallback(this.userTrackerCallback);
        this.contentResolver.unregisterContentObserver(this.settingsObserver);
        this.configurationController.removeCallback(this.configChangeListener);
        this.statusBarStateController.removeCallback(this.statusBarStateListener);
        this.session = null;
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
        if (bcSmartspaceDataPlugin == null) {
            return;
        }
        emptyList = CollectionsKt__CollectionsKt.emptyList();
        bcSmartspaceDataPlugin.onTargetsAvailable(emptyList);
    }

    public final void addListener(@NotNull BcSmartspaceDataPlugin.SmartspaceTargetListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.execution.assertIsMainThread();
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
        if (bcSmartspaceDataPlugin == null) {
            return;
        }
        bcSmartspaceDataPlugin.registerListener(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean filterSmartspaceTarget(SmartspaceTarget smartspaceTarget) {
        UserHandle userHandle = smartspaceTarget.getUserHandle();
        if (Intrinsics.areEqual(userHandle, this.userTracker.getUserHandle())) {
            if (!smartspaceTarget.isSensitive() || this.showSensitiveContentForCurrentUser) {
                return true;
            }
        } else if (Intrinsics.areEqual(userHandle, this.managedUserHandle) && this.userTracker.getUserHandle().getIdentifier() == 0 && (!smartspaceTarget.isSensitive() || this.showSensitiveContentForManagedUser)) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateTextColorFromWallpaper() {
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(this.context, R$attr.wallpaperTextColor);
        BcSmartspaceDataPlugin.SmartspaceView smartspaceView = this.smartspaceView;
        if (smartspaceView != null) {
            smartspaceView.setPrimaryTextColor(colorAttrDefaultColor);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("smartspaceView");
            throw null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void reloadSmartspace() {
        boolean z = false;
        this.showSensitiveContentForCurrentUser = this.secureSettings.getIntForUser("lock_screen_allow_private_notifications", 0, this.userTracker.getUserId()) == 1;
        UserHandle workProfileUser = getWorkProfileUser();
        this.managedUserHandle = workProfileUser;
        Integer valueOf = workProfileUser == null ? null : Integer.valueOf(workProfileUser.getIdentifier());
        if (valueOf != null) {
            if (this.secureSettings.getIntForUser("lock_screen_allow_private_notifications", 0, valueOf.intValue()) == 1) {
                z = true;
            }
            this.showSensitiveContentForManagedUser = z;
        }
        SmartspaceSession smartspaceSession = this.session;
        if (smartspaceSession == null) {
            return;
        }
        smartspaceSession.requestSmartspaceUpdate();
    }

    private final UserHandle getWorkProfileUser() {
        for (UserInfo userInfo : this.userTracker.getUserProfiles()) {
            if (userInfo.isManagedProfile()) {
                return userInfo.getUserHandle();
            }
        }
        return null;
    }
}

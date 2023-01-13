package com.android.systemui.statusbar.lockscreen;

import android.app.smartspace.SmartspaceConfig;
import android.app.smartspace.SmartspaceManager;
import android.app.smartspace.SmartspaceSession;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.UserInfo;
import android.os.Handler;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.flags.ResourceBooleanFlag;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000Õ\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b*\u0005#&0@C\b\u0007\u0018\u0000 Y2\u00020\u0001:\u0001YB\u0001\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\u0006\u0010\u0016\u001a\u00020\u0017\u0012\u0006\u0010\u0018\u001a\u00020\u0019\u0012\b\b\u0001\u0010\u001a\u001a\u00020\u001b\u0012\b\b\u0001\u0010\u001c\u001a\u00020\u001d\u0012\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020 0\u001f¢\u0006\u0002\u0010!J\u000e\u0010E\u001a\u00020F2\u0006\u0010G\u001a\u00020HJ\u0010\u0010I\u001a\u0004\u0018\u00010J2\u0006\u0010K\u001a\u00020LJ\u0012\u0010M\u001a\u0004\u0018\u00010J2\u0006\u0010K\u001a\u00020LH\u0002J\b\u0010N\u001a\u00020FH\u0002J\u0006\u0010O\u001a\u00020FJ\u0010\u0010P\u001a\u0002032\u0006\u0010Q\u001a\u00020RH\u0002J\n\u0010S\u001a\u0004\u0018\u00010)H\u0002J\u0006\u0010T\u001a\u000203J\b\u0010U\u001a\u00020FH\u0002J\u000e\u0010V\u001a\u00020F2\u0006\u0010G\u001a\u00020HJ\u0006\u0010W\u001a\u00020FJ\b\u0010X\u001a\u00020FH\u0002R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u00020#X\u0004¢\u0006\u0004\n\u0002\u0010$R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010%\u001a\u00020&X\u0004¢\u0006\u0004\n\u0002\u0010'R\u000e\u0010\u0018\u001a\u00020\u0019X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010(\u001a\u0004\u0018\u00010)X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010*\u001a\u0004\u0018\u00010 X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010+\u001a\u0004\u0018\u00010,X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020.X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010/\u001a\u000200X\u0004¢\u0006\u0004\n\u0002\u00101R\u000e\u00102\u001a\u000203X\u000e¢\u0006\u0002\n\u0000R\u000e\u00104\u001a\u000203X\u000e¢\u0006\u0002\n\u0000R\u000e\u00105\u001a\u000203X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0014\u00106\u001a\b\u0012\u0004\u0012\u00020807X\u000e¢\u0006\u0002\n\u0000R\u001a\u00109\u001a\u00020:X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b;\u0010<\"\u0004\b=\u0010>R\u000e\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010?\u001a\u00020@X\u0004¢\u0006\u0004\n\u0002\u0010AR\u000e\u0010\u001a\u001a\u00020\u001bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010B\u001a\u00020CX\u0004¢\u0006\u0004\n\u0002\u0010D¨\u0006Z"}, mo65043d2 = {"Lcom/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController;", "", "context", "Landroid/content/Context;", "featureFlags", "Lcom/android/systemui/flags/FeatureFlags;", "smartspaceManager", "Landroid/app/smartspace/SmartspaceManager;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "falsingManager", "Lcom/android/systemui/plugins/FalsingManager;", "secureSettings", "Lcom/android/systemui/util/settings/SecureSettings;", "userTracker", "Lcom/android/systemui/settings/UserTracker;", "contentResolver", "Landroid/content/ContentResolver;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "statusBarStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "deviceProvisionedController", "Lcom/android/systemui/statusbar/policy/DeviceProvisionedController;", "execution", "Lcom/android/systemui/util/concurrency/Execution;", "uiExecutor", "Ljava/util/concurrent/Executor;", "handler", "Landroid/os/Handler;", "optionalPlugin", "Ljava/util/Optional;", "Lcom/android/systemui/plugins/BcSmartspaceDataPlugin;", "(Landroid/content/Context;Lcom/android/systemui/flags/FeatureFlags;Landroid/app/smartspace/SmartspaceManager;Lcom/android/systemui/plugins/ActivityStarter;Lcom/android/systemui/plugins/FalsingManager;Lcom/android/systemui/util/settings/SecureSettings;Lcom/android/systemui/settings/UserTracker;Landroid/content/ContentResolver;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/plugins/statusbar/StatusBarStateController;Lcom/android/systemui/statusbar/policy/DeviceProvisionedController;Lcom/android/systemui/util/concurrency/Execution;Ljava/util/concurrent/Executor;Landroid/os/Handler;Ljava/util/Optional;)V", "configChangeListener", "com/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController$configChangeListener$1", "Lcom/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController$configChangeListener$1;", "deviceProvisionedListener", "com/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController$deviceProvisionedListener$1", "Lcom/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController$deviceProvisionedListener$1;", "managedUserHandle", "Landroid/os/UserHandle;", "plugin", "session", "Landroid/app/smartspace/SmartspaceSession;", "sessionListener", "Landroid/app/smartspace/SmartspaceSession$OnTargetsAvailableListener;", "settingsObserver", "com/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController$settingsObserver$1", "Lcom/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController$settingsObserver$1;", "showNotifications", "", "showSensitiveContentForCurrentUser", "showSensitiveContentForManagedUser", "smartspaceViews", "", "Lcom/android/systemui/plugins/BcSmartspaceDataPlugin$SmartspaceView;", "stateChangeListener", "Landroid/view/View$OnAttachStateChangeListener;", "getStateChangeListener", "()Landroid/view/View$OnAttachStateChangeListener;", "setStateChangeListener", "(Landroid/view/View$OnAttachStateChangeListener;)V", "statusBarStateListener", "com/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController$statusBarStateListener$1", "Lcom/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController$statusBarStateListener$1;", "userTrackerCallback", "com/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController$userTrackerCallback$1", "Lcom/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController$userTrackerCallback$1;", "addListener", "", "listener", "Lcom/android/systemui/plugins/BcSmartspaceDataPlugin$SmartspaceTargetListener;", "buildAndConnectView", "Landroid/view/View;", "parent", "Landroid/view/ViewGroup;", "buildView", "connectSession", "disconnect", "filterSmartspaceTarget", "t", "Landroid/app/smartspace/SmartspaceTarget;", "getWorkProfileUser", "isEnabled", "reloadSmartspace", "removeListener", "requestSmartspaceUpdate", "updateTextColorFromWallpaper", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LockscreenSmartspaceController.kt */
public final class LockscreenSmartspaceController {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "LockscreenSmartspaceController";
    /* access modifiers changed from: private */
    public final ActivityStarter activityStarter;
    private final LockscreenSmartspaceController$configChangeListener$1 configChangeListener;
    private final ConfigurationController configurationController;
    private final ContentResolver contentResolver;
    private final Context context;
    private final DeviceProvisionedController deviceProvisionedController;
    private final LockscreenSmartspaceController$deviceProvisionedListener$1 deviceProvisionedListener;
    /* access modifiers changed from: private */
    public final Execution execution;
    private final FalsingManager falsingManager;
    private final FeatureFlags featureFlags;
    private final Handler handler;
    private UserHandle managedUserHandle;
    private final BcSmartspaceDataPlugin plugin;
    private final SecureSettings secureSettings;
    private SmartspaceSession session;
    private final SmartspaceSession.OnTargetsAvailableListener sessionListener = new LockscreenSmartspaceController$$ExternalSyntheticLambda1(this);
    private final LockscreenSmartspaceController$settingsObserver$1 settingsObserver;
    private boolean showNotifications;
    private boolean showSensitiveContentForCurrentUser;
    private boolean showSensitiveContentForManagedUser;
    private final SmartspaceManager smartspaceManager;
    /* access modifiers changed from: private */
    public Set<BcSmartspaceDataPlugin.SmartspaceView> smartspaceViews = new LinkedHashSet();
    private View.OnAttachStateChangeListener stateChangeListener = new LockscreenSmartspaceController$stateChangeListener$1(this);
    /* access modifiers changed from: private */
    public final StatusBarStateController statusBarStateController;
    /* access modifiers changed from: private */
    public final LockscreenSmartspaceController$statusBarStateListener$1 statusBarStateListener;
    private final Executor uiExecutor;
    private final UserTracker userTracker;
    private final LockscreenSmartspaceController$userTrackerCallback$1 userTrackerCallback = new LockscreenSmartspaceController$userTrackerCallback$1(this);

    @Inject
    public LockscreenSmartspaceController(Context context2, FeatureFlags featureFlags2, SmartspaceManager smartspaceManager2, ActivityStarter activityStarter2, FalsingManager falsingManager2, SecureSettings secureSettings2, UserTracker userTracker2, ContentResolver contentResolver2, ConfigurationController configurationController2, StatusBarStateController statusBarStateController2, DeviceProvisionedController deviceProvisionedController2, Execution execution2, @Main Executor executor, @Main Handler handler2, Optional<BcSmartspaceDataPlugin> optional) {
        Context context3 = context2;
        FeatureFlags featureFlags3 = featureFlags2;
        SmartspaceManager smartspaceManager3 = smartspaceManager2;
        ActivityStarter activityStarter3 = activityStarter2;
        FalsingManager falsingManager3 = falsingManager2;
        SecureSettings secureSettings3 = secureSettings2;
        UserTracker userTracker3 = userTracker2;
        ContentResolver contentResolver3 = contentResolver2;
        ConfigurationController configurationController3 = configurationController2;
        StatusBarStateController statusBarStateController3 = statusBarStateController2;
        DeviceProvisionedController deviceProvisionedController3 = deviceProvisionedController2;
        Execution execution3 = execution2;
        Executor executor2 = executor;
        Handler handler3 = handler2;
        Optional<BcSmartspaceDataPlugin> optional2 = optional;
        Intrinsics.checkNotNullParameter(context3, "context");
        Intrinsics.checkNotNullParameter(featureFlags3, "featureFlags");
        Intrinsics.checkNotNullParameter(smartspaceManager3, "smartspaceManager");
        Intrinsics.checkNotNullParameter(activityStarter3, "activityStarter");
        Intrinsics.checkNotNullParameter(falsingManager3, "falsingManager");
        Intrinsics.checkNotNullParameter(secureSettings3, "secureSettings");
        Intrinsics.checkNotNullParameter(userTracker3, "userTracker");
        Intrinsics.checkNotNullParameter(contentResolver3, "contentResolver");
        Intrinsics.checkNotNullParameter(configurationController3, "configurationController");
        Intrinsics.checkNotNullParameter(statusBarStateController3, "statusBarStateController");
        Intrinsics.checkNotNullParameter(deviceProvisionedController3, "deviceProvisionedController");
        Intrinsics.checkNotNullParameter(execution3, "execution");
        Intrinsics.checkNotNullParameter(executor2, "uiExecutor");
        Intrinsics.checkNotNullParameter(handler3, "handler");
        Intrinsics.checkNotNullParameter(optional2, "optionalPlugin");
        this.context = context3;
        this.featureFlags = featureFlags3;
        this.smartspaceManager = smartspaceManager3;
        this.activityStarter = activityStarter3;
        this.falsingManager = falsingManager3;
        this.secureSettings = secureSettings3;
        this.userTracker = userTracker3;
        this.contentResolver = contentResolver3;
        this.configurationController = configurationController3;
        this.statusBarStateController = statusBarStateController3;
        this.deviceProvisionedController = deviceProvisionedController3;
        this.execution = execution3;
        this.uiExecutor = executor2;
        this.handler = handler3;
        this.plugin = optional2.orElse(null);
        this.settingsObserver = new LockscreenSmartspaceController$settingsObserver$1(this, handler3);
        this.configChangeListener = new LockscreenSmartspaceController$configChangeListener$1(this);
        this.statusBarStateListener = new LockscreenSmartspaceController$statusBarStateListener$1(this);
        LockscreenSmartspaceController$deviceProvisionedListener$1 lockscreenSmartspaceController$deviceProvisionedListener$1 = new LockscreenSmartspaceController$deviceProvisionedListener$1(this);
        this.deviceProvisionedListener = lockscreenSmartspaceController$deviceProvisionedListener$1;
        deviceProvisionedController3.addCallback(lockscreenSmartspaceController$deviceProvisionedListener$1);
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: LockscreenSmartspaceController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final View.OnAttachStateChangeListener getStateChangeListener() {
        return this.stateChangeListener;
    }

    public final void setStateChangeListener(View.OnAttachStateChangeListener onAttachStateChangeListener) {
        Intrinsics.checkNotNullParameter(onAttachStateChangeListener, "<set-?>");
        this.stateChangeListener = onAttachStateChangeListener;
    }

    /* access modifiers changed from: private */
    /* renamed from: sessionListener$lambda-0  reason: not valid java name */
    public static final void m3085sessionListener$lambda0(LockscreenSmartspaceController lockscreenSmartspaceController, List list) {
        Intrinsics.checkNotNullParameter(lockscreenSmartspaceController, "this$0");
        lockscreenSmartspaceController.execution.assertIsMainThread();
        Intrinsics.checkNotNullExpressionValue(list, "targets");
        Collection arrayList = new ArrayList();
        for (Object next : list) {
            if (lockscreenSmartspaceController.filterSmartspaceTarget((SmartspaceTarget) next)) {
                arrayList.add(next);
            }
        }
        List list2 = (List) arrayList;
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = lockscreenSmartspaceController.plugin;
        if (bcSmartspaceDataPlugin != null) {
            bcSmartspaceDataPlugin.onTargetsAvailable(list2);
        }
    }

    public final boolean isEnabled() {
        this.execution.assertIsMainThread();
        FeatureFlags featureFlags2 = this.featureFlags;
        ResourceBooleanFlag resourceBooleanFlag = Flags.SMARTSPACE;
        Intrinsics.checkNotNullExpressionValue(resourceBooleanFlag, "SMARTSPACE");
        return featureFlags2.isEnabled(resourceBooleanFlag) && this.plugin != null;
    }

    public final View buildAndConnectView(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        this.execution.assertIsMainThread();
        if (isEnabled()) {
            View buildView = buildView(viewGroup);
            connectSession();
            return buildView;
        }
        throw new RuntimeException("Cannot build view when not enabled");
    }

    public final void requestSmartspaceUpdate() {
        SmartspaceSession smartspaceSession = this.session;
        if (smartspaceSession != null) {
            smartspaceSession.requestSmartspaceUpdate();
        }
    }

    private final View buildView(ViewGroup viewGroup) {
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
        if (bcSmartspaceDataPlugin == null) {
            return null;
        }
        BcSmartspaceDataPlugin.SmartspaceView view = bcSmartspaceDataPlugin.getView(viewGroup);
        view.registerDataProvider(this.plugin);
        view.setIntentStarter(new LockscreenSmartspaceController$buildView$1(this));
        view.setFalsingManager(this.falsingManager);
        if (view != null) {
            View view2 = (View) view;
            view2.addOnAttachStateChangeListener(this.stateChangeListener);
            return view2;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.View");
    }

    /* access modifiers changed from: private */
    public final void connectSession() {
        if (this.plugin != null && this.session == null && !this.smartspaceViews.isEmpty() && this.deviceProvisionedController.isDeviceProvisioned() && this.deviceProvisionedController.isCurrentUserSetup()) {
            SmartspaceSession createSmartspaceSession = this.smartspaceManager.createSmartspaceSession(new SmartspaceConfig.Builder(this.context, "lockscreen").build());
            Log.d(TAG, "Starting smartspace session for lockscreen");
            createSmartspaceSession.addOnTargetsAvailableListener(this.uiExecutor, this.sessionListener);
            this.session = createSmartspaceSession;
            this.deviceProvisionedController.removeCallback(this.deviceProvisionedListener);
            this.userTracker.addCallback(this.userTrackerCallback, this.uiExecutor);
            this.contentResolver.registerContentObserver(this.secureSettings.getUriFor("lock_screen_allow_private_notifications"), true, this.settingsObserver, -1);
            this.contentResolver.registerContentObserver(this.secureSettings.getUriFor("lock_screen_show_notifications"), true, this.settingsObserver, -1);
            this.configurationController.addCallback(this.configChangeListener);
            this.statusBarStateController.addCallback(this.statusBarStateListener);
            this.plugin.registerSmartspaceEventNotifier(new LockscreenSmartspaceController$$ExternalSyntheticLambda0(this));
            reloadSmartspace();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: connectSession$lambda-2  reason: not valid java name */
    public static final void m3084connectSession$lambda2(LockscreenSmartspaceController lockscreenSmartspaceController, SmartspaceTargetEvent smartspaceTargetEvent) {
        Intrinsics.checkNotNullParameter(lockscreenSmartspaceController, "this$0");
        SmartspaceSession smartspaceSession = lockscreenSmartspaceController.session;
        if (smartspaceSession != null) {
            smartspaceSession.notifySmartspaceEvent(smartspaceTargetEvent);
        }
    }

    public final void disconnect() {
        if (this.smartspaceViews.isEmpty()) {
            this.execution.assertIsMainThread();
            SmartspaceSession smartspaceSession = this.session;
            if (smartspaceSession != null) {
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
                if (bcSmartspaceDataPlugin != null) {
                    bcSmartspaceDataPlugin.registerSmartspaceEventNotifier((BcSmartspaceDataPlugin.SmartspaceEventNotifier) null);
                }
                BcSmartspaceDataPlugin bcSmartspaceDataPlugin2 = this.plugin;
                if (bcSmartspaceDataPlugin2 != null) {
                    bcSmartspaceDataPlugin2.onTargetsAvailable(CollectionsKt.emptyList());
                }
                Log.d(TAG, "Ending smartspace session for lockscreen");
            }
        }
    }

    public final void addListener(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        Intrinsics.checkNotNullParameter(smartspaceTargetListener, "listener");
        this.execution.assertIsMainThread();
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
        if (bcSmartspaceDataPlugin != null) {
            bcSmartspaceDataPlugin.registerListener(smartspaceTargetListener);
        }
    }

    public final void removeListener(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        Intrinsics.checkNotNullParameter(smartspaceTargetListener, "listener");
        this.execution.assertIsMainThread();
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
        if (bcSmartspaceDataPlugin != null) {
            bcSmartspaceDataPlugin.unregisterListener(smartspaceTargetListener);
        }
    }

    private final boolean filterSmartspaceTarget(SmartspaceTarget smartspaceTarget) {
        if (this.showNotifications) {
            UserHandle userHandle = smartspaceTarget.getUserHandle();
            if (Intrinsics.areEqual((Object) userHandle, (Object) this.userTracker.getUserHandle())) {
                if (smartspaceTarget.isSensitive() && !this.showSensitiveContentForCurrentUser) {
                    return false;
                }
            } else if (!Intrinsics.areEqual((Object) userHandle, (Object) this.managedUserHandle) || this.userTracker.getUserHandle().getIdentifier() != 0) {
                return false;
            } else {
                if (smartspaceTarget.isSensitive() && !this.showSensitiveContentForManagedUser) {
                    return false;
                }
            }
            return true;
        } else if (smartspaceTarget.getFeatureType() == 1) {
            return true;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public final void updateTextColorFromWallpaper() {
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(this.context, C1894R.attr.wallpaperTextColor);
        for (BcSmartspaceDataPlugin.SmartspaceView primaryTextColor : this.smartspaceViews) {
            primaryTextColor.setPrimaryTextColor(colorAttrDefaultColor);
        }
    }

    /* access modifiers changed from: private */
    public final void reloadSmartspace() {
        boolean z = false;
        this.showNotifications = this.secureSettings.getIntForUser("lock_screen_show_notifications", 0, this.userTracker.getUserId()) == 1;
        this.showSensitiveContentForCurrentUser = this.secureSettings.getIntForUser("lock_screen_allow_private_notifications", 0, this.userTracker.getUserId()) == 1;
        UserHandle workProfileUser = getWorkProfileUser();
        this.managedUserHandle = workProfileUser;
        Integer valueOf = workProfileUser != null ? Integer.valueOf(workProfileUser.getIdentifier()) : null;
        if (valueOf != null) {
            if (this.secureSettings.getIntForUser("lock_screen_allow_private_notifications", 0, valueOf.intValue()) == 1) {
                z = true;
            }
            this.showSensitiveContentForManagedUser = z;
        }
        SmartspaceSession smartspaceSession = this.session;
        if (smartspaceSession != null) {
            smartspaceSession.requestSmartspaceUpdate();
        }
    }

    private final UserHandle getWorkProfileUser() {
        for (UserInfo next : this.userTracker.getUserProfiles()) {
            if (next.isManagedProfile()) {
                return next.getUserHandle();
            }
        }
        return null;
    }
}

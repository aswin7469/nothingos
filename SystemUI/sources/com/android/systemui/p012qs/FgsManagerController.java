package com.android.systemui.p012qs;

import android.app.IActivityManager;
import android.app.IForegroundServiceObserver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.text.format.DateUtils;
import android.util.ArrayMap;
import android.util.IndentingPrintWriter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.C1893R;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.time.SystemClock;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.ForegroundServiceManagerDialog;
import com.nothing.systemui.p024qs.QSFragmentEx;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000×\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010#\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\r*\u0001>\b\u0007\u0018\u0000 c2\u00020\u00012\u00020\u0002:\tabcdefghiBc\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0001\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0001\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\u0006\u0010\u0016\u001a\u00020\u0017¢\u0006\u0002\u0010\u0018J\u000e\u0010@\u001a\u00020A2\u0006\u0010B\u001a\u00020.J\u000e\u0010C\u001a\u00020A2\u0006\u0010B\u001a\u000202J%\u0010D\u001a\u00020A2\u0006\u0010E\u001a\u00020F2\u000e\u0010G\u001a\n\u0012\u0006\b\u0001\u0012\u00020I0HH\u0016¢\u0006\u0002\u0010JJ\u0006\u0010K\u001a\u00020\"J\b\u0010L\u001a\u00020\"H\u0002J\u0006\u0010M\u001a\u00020AJ(\u0010N\u001a\u00020A2\u0006\u0010O\u001a\u00020\u001c2\u0006\u0010P\u001a\u00020I2\u0006\u0010Q\u001a\u00020\"2\u0006\u0010R\u001a\u00020SH\u0002J(\u0010T\u001a\u00020A2\u0006\u0010U\u001a\u00020V2\u0006\u0010P\u001a\u00020I2\u0006\u0010Q\u001a\u00020\"2\u0006\u0010W\u001a\u00020\u001cH\u0016J\u000e\u0010X\u001a\u00020A2\u0006\u0010B\u001a\u00020.J\u000e\u0010Y\u001a\u00020A2\u0006\u0010B\u001a\u000202J\u0006\u0010Z\u001a\u00020\u001cJ\u0010\u0010[\u001a\u00020A2\b\u0010\\\u001a\u0004\u0018\u00010]J \u0010^\u001a\u00020A2\u0006\u0010Q\u001a\u00020\"2\u0006\u0010P\u001a\u00020I2\u0006\u0010R\u001a\u00020SH\u0002J\b\u0010_\u001a\u00020AH\u0003J\b\u0010`\u001a\u00020AH\u0002R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0019\u001a\u00060\u001aR\u00020\u00008\u0002X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001b\u001a\u00020\u001c@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0018\u0010 \u001a\b\u0012\u0004\u0012\u00020\"0!8\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010#\u001a\u0004\u0018\u00010$8\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010%\u001a\u00020\u001c8\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\u001f\"\u0004\b'\u0010(R\u001e\u0010)\u001a\u00020\u001c2\u0006\u0010\u001b\u001a\u00020\u001c@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001fR\u0012\u0010*\u001a\u00020\"8\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020,X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010-\u001a\b\u0012\u0004\u0012\u00020.0!8\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u001c\u00101\u001a\b\u0012\u0004\u0012\u0002020!8\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b3\u00100R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\"\u00104\u001a\u0012\u0012\b\u0012\u000606R\u00020\u0000\u0012\u0004\u0012\u000207058\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R \u00108\u001a\u0012\u0012\b\u0012\u000606R\u00020\u0000\u0012\u0004\u0012\u00020:098\u0002X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010;\u001a\u00020\u001c2\u0006\u0010\u001b\u001a\u00020\u001c@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b<\u0010\u001fR\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010=\u001a\u00020>X\u0004¢\u0006\u0004\n\u0002\u0010?¨\u0006j"}, mo64987d2 = {"Lcom/android/systemui/qs/FgsManagerController;", "Landroid/app/IForegroundServiceObserver$Stub;", "Lcom/android/systemui/Dumpable;", "context", "Landroid/content/Context;", "mainExecutor", "Ljava/util/concurrent/Executor;", "backgroundExecutor", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "activityManager", "Landroid/app/IActivityManager;", "packageManager", "Landroid/content/pm/PackageManager;", "userTracker", "Lcom/android/systemui/settings/UserTracker;", "deviceConfigProxy", "Lcom/android/systemui/util/DeviceConfigProxy;", "dialogLaunchAnimator", "Lcom/android/systemui/animation/DialogLaunchAnimator;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Landroid/content/Context;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Lcom/android/systemui/util/time/SystemClock;Landroid/app/IActivityManager;Landroid/content/pm/PackageManager;Lcom/android/systemui/settings/UserTracker;Lcom/android/systemui/util/DeviceConfigProxy;Lcom/android/systemui/animation/DialogLaunchAnimator;Lcom/android/systemui/broadcast/BroadcastDispatcher;Lcom/android/systemui/dump/DumpManager;)V", "appListAdapter", "Lcom/android/systemui/qs/FgsManagerController$AppListAdapter;", "<set-?>", "", "changesSinceDialog", "getChangesSinceDialog", "()Z", "currentProfileIds", "", "", "dialog", "Lcom/android/systemui/statusbar/phone/SystemUIDialog;", "initialized", "getInitialized", "setInitialized", "(Z)V", "isAvailable", "lastNumberOfVisiblePackages", "lock", "", "onDialogDismissedListeners", "Lcom/android/systemui/qs/FgsManagerController$OnDialogDismissedListener;", "getOnDialogDismissedListeners", "()Ljava/util/Set;", "onNumberOfPackagesChangedListeners", "Lcom/android/systemui/qs/FgsManagerController$OnNumberOfPackagesChangedListener;", "getOnNumberOfPackagesChangedListeners", "runningApps", "Landroid/util/ArrayMap;", "Lcom/android/systemui/qs/FgsManagerController$UserPackage;", "Lcom/android/systemui/qs/FgsManagerController$RunningApp;", "runningServiceTokens", "", "Lcom/android/systemui/qs/FgsManagerController$StartTimeAndTokens;", "showFooterDot", "getShowFooterDot", "userTrackerCallback", "com/android/systemui/qs/FgsManagerController$userTrackerCallback$1", "Lcom/android/systemui/qs/FgsManagerController$userTrackerCallback$1;", "addOnDialogDismissedListener", "", "listener", "addOnNumberOfPackagesChangedListener", "dump", "printwriter", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "getNumRunningPackages", "getNumVisiblePackagesLocked", "init", "logEvent", "stopped", "packageName", "userId", "timeStarted", "", "onForegroundStateChanged", "token", "Landroid/os/IBinder;", "isForeground", "removeOnDialogDismissedListener", "removeOnNumberOfPackagesChangedListener", "shouldUpdateFooterVisibility", "showDialog", "viewLaunchedFrom", "Landroid/view/View;", "stopPackage", "updateAppItemsLocked", "updateNumberOfVisibleRunningPackagesLocked", "AppItemViewHolder", "AppListAdapter", "Companion", "OnDialogDismissedListener", "OnNumberOfPackagesChangedListener", "RunningApp", "StartTimeAndTokens", "UIControl", "UserPackage", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.FgsManagerController */
/* compiled from: FgsManagerController.kt */
public final class FgsManagerController extends IForegroundServiceObserver.Stub implements Dumpable {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final boolean DEFAULT_TASK_MANAGER_ENABLED = true;
    private static final boolean DEFAULT_TASK_MANAGER_SHOW_FOOTER_DOT = false;
    private static final String LOG_TAG = "FgsManagerController";
    /* access modifiers changed from: private */
    public final IActivityManager activityManager;
    private final AppListAdapter appListAdapter = new AppListAdapter();
    private final Executor backgroundExecutor;
    private final BroadcastDispatcher broadcastDispatcher;
    private boolean changesSinceDialog;
    private final Context context;
    /* access modifiers changed from: private */
    public Set<Integer> currentProfileIds = new LinkedHashSet();
    private final DeviceConfigProxy deviceConfigProxy;
    private SystemUIDialog dialog;
    private final DialogLaunchAnimator dialogLaunchAnimator;
    private final DumpManager dumpManager;
    private boolean initialized;
    private boolean isAvailable;
    /* access modifiers changed from: private */
    public int lastNumberOfVisiblePackages;
    /* access modifiers changed from: private */
    public final Object lock = new Object();
    private final Executor mainExecutor;
    private final Set<OnDialogDismissedListener> onDialogDismissedListeners = new LinkedHashSet();
    private final Set<OnNumberOfPackagesChangedListener> onNumberOfPackagesChangedListeners = new LinkedHashSet();
    /* access modifiers changed from: private */
    public final PackageManager packageManager;
    private ArrayMap<UserPackage, RunningApp> runningApps = new ArrayMap<>();
    private final Map<UserPackage, StartTimeAndTokens> runningServiceTokens = new LinkedHashMap();
    private boolean showFooterDot;
    /* access modifiers changed from: private */
    public final SystemClock systemClock;
    private final UserTracker userTracker;
    private final FgsManagerController$userTrackerCallback$1 userTrackerCallback = new FgsManagerController$userTrackerCallback$1(this);

    @Metadata(mo64986d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0004À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/qs/FgsManagerController$OnDialogDismissedListener;", "", "onDialogDismissed", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.qs.FgsManagerController$OnDialogDismissedListener */
    /* compiled from: FgsManagerController.kt */
    public interface OnDialogDismissedListener {
        void onDialogDismissed();
    }

    @Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/qs/FgsManagerController$OnNumberOfPackagesChangedListener;", "", "onNumberOfPackagesChanged", "", "numPackages", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.qs.FgsManagerController$OnNumberOfPackagesChangedListener */
    /* compiled from: FgsManagerController.kt */
    public interface OnNumberOfPackagesChangedListener {
        void onNumberOfPackagesChanged(int i);
    }

    @Metadata(mo64986d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, mo64987d2 = {"Lcom/android/systemui/qs/FgsManagerController$UIControl;", "", "(Ljava/lang/String;I)V", "NORMAL", "HIDE_BUTTON", "HIDE_ENTRY", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.qs.FgsManagerController$UIControl */
    /* compiled from: FgsManagerController.kt */
    private enum UIControl {
        NORMAL,
        HIDE_BUTTON,
        HIDE_ENTRY
    }

    @Inject
    public FgsManagerController(Context context2, @Main Executor executor, @Background Executor executor2, SystemClock systemClock2, IActivityManager iActivityManager, PackageManager packageManager2, UserTracker userTracker2, DeviceConfigProxy deviceConfigProxy2, DialogLaunchAnimator dialogLaunchAnimator2, BroadcastDispatcher broadcastDispatcher2, DumpManager dumpManager2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(executor, "mainExecutor");
        Intrinsics.checkNotNullParameter(executor2, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(systemClock2, "systemClock");
        Intrinsics.checkNotNullParameter(iActivityManager, "activityManager");
        Intrinsics.checkNotNullParameter(packageManager2, "packageManager");
        Intrinsics.checkNotNullParameter(userTracker2, "userTracker");
        Intrinsics.checkNotNullParameter(deviceConfigProxy2, "deviceConfigProxy");
        Intrinsics.checkNotNullParameter(dialogLaunchAnimator2, "dialogLaunchAnimator");
        Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        this.context = context2;
        this.mainExecutor = executor;
        this.backgroundExecutor = executor2;
        this.systemClock = systemClock2;
        this.activityManager = iActivityManager;
        this.packageManager = packageManager2;
        this.userTracker = userTracker2;
        this.deviceConfigProxy = deviceConfigProxy2;
        this.dialogLaunchAnimator = dialogLaunchAnimator2;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.dumpManager = dumpManager2;
    }

    @Metadata(mo64986d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \b*\u0004\u0018\u00010\u00070\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, mo64987d2 = {"Lcom/android/systemui/qs/FgsManagerController$Companion;", "", "()V", "DEFAULT_TASK_MANAGER_ENABLED", "", "DEFAULT_TASK_MANAGER_SHOW_FOOTER_DOT", "LOG_TAG", "", "kotlin.jvm.PlatformType", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.qs.FgsManagerController$Companion */
    /* compiled from: FgsManagerController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final boolean getChangesSinceDialog() {
        return this.changesSinceDialog;
    }

    public final boolean isAvailable() {
        return this.isAvailable;
    }

    public final boolean getShowFooterDot() {
        return this.showFooterDot;
    }

    public final boolean getInitialized() {
        return this.initialized;
    }

    public final void setInitialized(boolean z) {
        this.initialized = z;
    }

    public final void init() {
        synchronized (this.lock) {
            if (!this.initialized) {
                try {
                    this.activityManager.registerForegroundServiceObserver((IForegroundServiceObserver) this);
                } catch (RemoteException e) {
                    e.rethrowFromSystemServer();
                }
                this.userTracker.addCallback(this.userTrackerCallback, this.backgroundExecutor);
                Set<Integer> set = this.currentProfileIds;
                Iterable<UserInfo> userProfiles = this.userTracker.getUserProfiles();
                Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(userProfiles, 10));
                for (UserInfo userInfo : userProfiles) {
                    arrayList.add(Integer.valueOf(userInfo.id));
                }
                set.addAll((List) arrayList);
                this.deviceConfigProxy.addOnPropertiesChangedListener("systemui", this.backgroundExecutor, new FgsManagerController$$ExternalSyntheticLambda5(this));
                this.isAvailable = this.deviceConfigProxy.getBoolean("systemui", "task_manager_enabled", true);
                this.showFooterDot = this.deviceConfigProxy.getBoolean("systemui", "task_manager_show_footer_dot", false);
                this.dumpManager.registerDumpable(this);
                BroadcastDispatcher.registerReceiver$default(this.broadcastDispatcher, new FgsManagerController$init$1$3(this), new IntentFilter("android.intent.action.SHOW_FOREGROUND_SERVICE_MANAGER"), this.mainExecutor, (UserHandle) null, 4, (String) null, 40, (Object) null);
                this.initialized = true;
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: init$lambda-2$lambda-1  reason: not valid java name */
    public static final void m2886init$lambda2$lambda1(FgsManagerController fgsManagerController, DeviceConfig.Properties properties) {
        Intrinsics.checkNotNullParameter(fgsManagerController, "this$0");
        fgsManagerController.isAvailable = properties.getBoolean("task_manager_enabled", fgsManagerController.isAvailable);
        fgsManagerController.showFooterDot = properties.getBoolean("task_manager_show_footer_dot", fgsManagerController.showFooterDot);
    }

    public void onForegroundStateChanged(IBinder iBinder, String str, int i, boolean z) {
        Intrinsics.checkNotNullParameter(iBinder, "token");
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        synchronized (this.lock) {
            UserPackage userPackage = new UserPackage(this, i, str);
            if (z) {
                Map<UserPackage, StartTimeAndTokens> map = this.runningServiceTokens;
                StartTimeAndTokens startTimeAndTokens = map.get(userPackage);
                if (startTimeAndTokens == null) {
                    startTimeAndTokens = new StartTimeAndTokens(this.systemClock);
                    map.put(userPackage, startTimeAndTokens);
                }
                startTimeAndTokens.addToken(iBinder);
            } else {
                StartTimeAndTokens startTimeAndTokens2 = this.runningServiceTokens.get(userPackage);
                boolean z2 = false;
                if (startTimeAndTokens2 != null) {
                    startTimeAndTokens2.removeToken(iBinder);
                    if (startTimeAndTokens2.isEmpty()) {
                        z2 = true;
                    }
                }
                if (z2) {
                    this.runningServiceTokens.remove(userPackage);
                }
            }
            updateNumberOfVisibleRunningPackagesLocked();
            updateAppItemsLocked();
            Unit unit = Unit.INSTANCE;
        }
    }

    public final Set<OnNumberOfPackagesChangedListener> getOnNumberOfPackagesChangedListeners() {
        return this.onNumberOfPackagesChangedListeners;
    }

    public final Set<OnDialogDismissedListener> getOnDialogDismissedListeners() {
        return this.onDialogDismissedListeners;
    }

    public final void addOnNumberOfPackagesChangedListener(OnNumberOfPackagesChangedListener onNumberOfPackagesChangedListener) {
        Intrinsics.checkNotNullParameter(onNumberOfPackagesChangedListener, "listener");
        synchronized (this.lock) {
            this.onNumberOfPackagesChangedListeners.add(onNumberOfPackagesChangedListener);
        }
    }

    public final void removeOnNumberOfPackagesChangedListener(OnNumberOfPackagesChangedListener onNumberOfPackagesChangedListener) {
        Intrinsics.checkNotNullParameter(onNumberOfPackagesChangedListener, "listener");
        synchronized (this.lock) {
            this.onNumberOfPackagesChangedListeners.remove(onNumberOfPackagesChangedListener);
        }
    }

    public final void addOnDialogDismissedListener(OnDialogDismissedListener onDialogDismissedListener) {
        Intrinsics.checkNotNullParameter(onDialogDismissedListener, "listener");
        synchronized (this.lock) {
            this.onDialogDismissedListeners.add(onDialogDismissedListener);
        }
    }

    public final void removeOnDialogDismissedListener(OnDialogDismissedListener onDialogDismissedListener) {
        Intrinsics.checkNotNullParameter(onDialogDismissedListener, "listener");
        synchronized (this.lock) {
            this.onDialogDismissedListeners.remove(onDialogDismissedListener);
        }
    }

    public final int getNumRunningPackages() {
        int numVisiblePackagesLocked;
        synchronized (this.lock) {
            numVisiblePackagesLocked = getNumVisiblePackagesLocked();
        }
        return numVisiblePackagesLocked;
    }

    private final int getNumVisiblePackagesLocked() {
        Iterable<UserPackage> keySet = this.runningServiceTokens.keySet();
        if ((keySet instanceof Collection) && ((Collection) keySet).isEmpty()) {
            return 0;
        }
        int i = 0;
        for (UserPackage userPackage : keySet) {
            if ((userPackage.getUiControl() != UIControl.HIDE_ENTRY && this.currentProfileIds.contains(Integer.valueOf(userPackage.getUserId()))) && (i = i + 1) < 0) {
                CollectionsKt.throwCountOverflow();
            }
        }
        return i;
    }

    /* access modifiers changed from: private */
    public final void updateNumberOfVisibleRunningPackagesLocked() {
        int numVisiblePackagesLocked = getNumVisiblePackagesLocked();
        if (numVisiblePackagesLocked != this.lastNumberOfVisiblePackages) {
            this.lastNumberOfVisiblePackages = numVisiblePackagesLocked;
            this.changesSinceDialog = true;
            for (OnNumberOfPackagesChangedListener fgsManagerController$$ExternalSyntheticLambda0 : this.onNumberOfPackagesChangedListeners) {
                this.backgroundExecutor.execute(new FgsManagerController$$ExternalSyntheticLambda0(fgsManagerController$$ExternalSyntheticLambda0, numVisiblePackagesLocked));
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: updateNumberOfVisibleRunningPackagesLocked$lambda-13$lambda-12  reason: not valid java name */
    public static final void m2892updateNumberOfVisibleRunningPackagesLocked$lambda13$lambda12(OnNumberOfPackagesChangedListener onNumberOfPackagesChangedListener, int i) {
        Intrinsics.checkNotNullParameter(onNumberOfPackagesChangedListener, "$it");
        onNumberOfPackagesChangedListener.onNumberOfPackagesChanged(i);
    }

    public final boolean shouldUpdateFooterVisibility() {
        return this.dialog == null;
    }

    public final void showDialog(View view) {
        synchronized (this.lock) {
            if (this.dialog == null) {
                for (UserPackage updateUiControl : this.runningServiceTokens.keySet()) {
                    updateUiControl.updateUiControl();
                }
                Executor executor = this.mainExecutor;
                int numRunningPackages = getNumRunningPackages();
                QSFragment qSFragment = ((QSFragmentEx) NTDependencyEx.get(QSFragmentEx.class)).getQSFragment();
                Intrinsics.checkNotNull(qSFragment);
                ForegroundServiceManagerDialog foregroundServiceManagerDialog = new ForegroundServiceManagerDialog(executor, numRunningPackages, qSFragment.getView().getContext());
                foregroundServiceManagerDialog.setTitle(C1893R.string.fgs_manager_dialog_title);
                foregroundServiceManagerDialog.setMessage(C1893R.string.fgs_manager_dialog_message);
                Context context2 = foregroundServiceManagerDialog.getContext();
                RecyclerView recyclerView = new RecyclerView(context2);
                recyclerView.setLayoutManager(new LinearLayoutManager(context2));
                recyclerView.setAdapter(this.appListAdapter);
                int dimensionPixelSize = context2.getResources().getDimensionPixelSize(C1893R.dimen.fgs_manager_list_top_spacing);
                foregroundServiceManagerDialog.setView(recyclerView, 0, dimensionPixelSize, 0, 0);
                this.dialog = foregroundServiceManagerDialog;
                foregroundServiceManagerDialog.setOnDismissListener(new FgsManagerController$$ExternalSyntheticLambda1(this));
                this.mainExecutor.execute(new FgsManagerController$$ExternalSyntheticLambda2(view, foregroundServiceManagerDialog, this));
                this.backgroundExecutor.execute(new FgsManagerController$$ExternalSyntheticLambda3(this));
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: showDialog$lambda-22$lambda-17  reason: not valid java name */
    public static final void m2888showDialog$lambda22$lambda17(FgsManagerController fgsManagerController, DialogInterface dialogInterface) {
        Intrinsics.checkNotNullParameter(fgsManagerController, "this$0");
        fgsManagerController.changesSinceDialog = false;
        synchronized (fgsManagerController.lock) {
            fgsManagerController.dialog = null;
            fgsManagerController.updateAppItemsLocked();
            Unit unit = Unit.INSTANCE;
        }
        for (OnDialogDismissedListener fgsManagerController$$ExternalSyntheticLambda6 : fgsManagerController.onDialogDismissedListeners) {
            fgsManagerController.mainExecutor.execute(new FgsManagerController$$ExternalSyntheticLambda6(fgsManagerController$$ExternalSyntheticLambda6));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: showDialog$lambda-22$lambda-19  reason: not valid java name */
    public static final void m2889showDialog$lambda22$lambda19(View view, ForegroundServiceManagerDialog foregroundServiceManagerDialog, FgsManagerController fgsManagerController) {
        Unit unit;
        Intrinsics.checkNotNullParameter(foregroundServiceManagerDialog, "$dialog");
        Intrinsics.checkNotNullParameter(fgsManagerController, "this$0");
        if (view != null) {
            DialogLaunchAnimator.showFromView$default(fgsManagerController.dialogLaunchAnimator, foregroundServiceManagerDialog, view, false, 4, (Object) null);
            unit = Unit.INSTANCE;
        } else {
            unit = null;
        }
        if (unit == null) {
            foregroundServiceManagerDialog.show();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: showDialog$lambda-22$lambda-21  reason: not valid java name */
    public static final void m2890showDialog$lambda22$lambda21(FgsManagerController fgsManagerController) {
        Intrinsics.checkNotNullParameter(fgsManagerController, "this$0");
        synchronized (fgsManagerController.lock) {
            fgsManagerController.updateAppItemsLocked();
            Unit unit = Unit.INSTANCE;
        }
    }

    private final void updateAppItemsLocked() {
        if (this.dialog == null) {
            this.runningApps.clear();
            return;
        }
        Collection arrayList = new ArrayList();
        Iterator it = this.runningServiceTokens.keySet().iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            UserPackage userPackage = (UserPackage) next;
            if (this.currentProfileIds.contains(Integer.valueOf(userPackage.getUserId())) && userPackage.getUiControl() != UIControl.HIDE_ENTRY) {
                RunningApp runningApp = this.runningApps.get(userPackage);
                if (!(runningApp != null && runningApp.getStopped())) {
                    z = true;
                }
            }
            if (z) {
                arrayList.add(next);
            }
        }
        List<UserPackage> list = (List) arrayList;
        Set<UserPackage> keySet = this.runningApps.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet, "runningApps.keys");
        Collection arrayList2 = new ArrayList();
        for (Object next2 : keySet) {
            if (!this.runningServiceTokens.containsKey((UserPackage) next2)) {
                arrayList2.add(next2);
            }
        }
        List<UserPackage> list2 = (List) arrayList2;
        for (UserPackage userPackage2 : list) {
            ApplicationInfo applicationInfoAsUser = this.packageManager.getApplicationInfoAsUser(userPackage2.getPackageName(), 0, userPackage2.getUserId());
            int userId = userPackage2.getUserId();
            String packageName = userPackage2.getPackageName();
            StartTimeAndTokens startTimeAndTokens = this.runningServiceTokens.get(userPackage2);
            Intrinsics.checkNotNull(startTimeAndTokens);
            long startTime = startTimeAndTokens.getStartTime();
            UIControl uiControl = userPackage2.getUiControl();
            CharSequence applicationLabel = this.packageManager.getApplicationLabel(applicationInfoAsUser);
            Intrinsics.checkNotNullExpressionValue(applicationLabel, "packageManager.getApplicationLabel(ai)");
            PackageManager packageManager2 = this.packageManager;
            Drawable userBadgedIcon = packageManager2.getUserBadgedIcon(packageManager2.getApplicationIcon(applicationInfoAsUser), UserHandle.of(userPackage2.getUserId()));
            Intrinsics.checkNotNullExpressionValue(userBadgedIcon, "packageManager.getUserBa…UserHandle.of(it.userId))");
            this.runningApps.put(userPackage2, new RunningApp(userId, packageName, startTime, uiControl, applicationLabel, userBadgedIcon));
            String packageName2 = userPackage2.getPackageName();
            int userId2 = userPackage2.getUserId();
            RunningApp runningApp2 = this.runningApps.get(userPackage2);
            Intrinsics.checkNotNull(runningApp2);
            logEvent(false, packageName2, userId2, runningApp2.getTimeStarted());
        }
        for (UserPackage userPackage3 : list2) {
            RunningApp runningApp3 = this.runningApps.get(userPackage3);
            Intrinsics.checkNotNull(runningApp3);
            RunningApp runningApp4 = runningApp3;
            RunningApp copy$default = RunningApp.copy$default(runningApp4, 0, (String) null, 0, (UIControl) null, 15, (Object) null);
            copy$default.setStopped(true);
            copy$default.setAppLabel(runningApp4.getAppLabel());
            copy$default.setIcon(runningApp4.getIcon());
            this.runningApps.put(userPackage3, copy$default);
        }
        this.mainExecutor.execute(new FgsManagerController$$ExternalSyntheticLambda7(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: updateAppItemsLocked$lambda-29  reason: not valid java name */
    public static final void m2891updateAppItemsLocked$lambda29(FgsManagerController fgsManagerController) {
        Intrinsics.checkNotNullParameter(fgsManagerController, "this$0");
        AppListAdapter appListAdapter2 = fgsManagerController.appListAdapter;
        Collection<RunningApp> values = fgsManagerController.runningApps.values();
        Intrinsics.checkNotNullExpressionValue(values, "runningApps.values");
        appListAdapter2.setData(CollectionsKt.sortedWith(CollectionsKt.toList(values), new C2319xc6d53fa()));
    }

    /* access modifiers changed from: private */
    public final void stopPackage(int i, String str, long j) {
        logEvent(true, str, i, j);
        this.activityManager.stopAppForUser(str, i);
    }

    private final void logEvent(boolean z, String str, int i, long j) {
        this.backgroundExecutor.execute(new FgsManagerController$$ExternalSyntheticLambda4(this, str, i, z ? 2 : 1, this.systemClock.elapsedRealtime(), j));
    }

    /* access modifiers changed from: private */
    /* renamed from: logEvent$lambda-30  reason: not valid java name */
    public static final void m2887logEvent$lambda30(FgsManagerController fgsManagerController, String str, int i, int i2, long j, long j2) {
        Intrinsics.checkNotNullParameter(fgsManagerController, "this$0");
        Intrinsics.checkNotNullParameter(str, "$packageName");
        SysUiStatsLog.write(450, fgsManagerController.packageManager.getPackageUidAsUser(str, i), i2, j - j2);
    }

    @Metadata(mo64986d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\b\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\nH\u0016J\u0018\u0010\u000f\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\nH\u0016J\u0014\u0010\u0013\u001a\u00020\f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005R\u0018\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00058\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, mo64987d2 = {"Lcom/android/systemui/qs/FgsManagerController$AppListAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/android/systemui/qs/FgsManagerController$AppItemViewHolder;", "(Lcom/android/systemui/qs/FgsManagerController;)V", "data", "", "Lcom/android/systemui/qs/FgsManagerController$RunningApp;", "lock", "", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "setData", "newData", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.qs.FgsManagerController$AppListAdapter */
    /* compiled from: FgsManagerController.kt */
    private final class AppListAdapter extends RecyclerView.Adapter<AppItemViewHolder> {
        private List<RunningApp> data = CollectionsKt.emptyList();
        private final Object lock = new Object();

        public AppListAdapter() {
        }

        public AppItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            Intrinsics.checkNotNullParameter(viewGroup, "parent");
            View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(C1893R.layout.fgs_manager_app_item, viewGroup, false);
            Intrinsics.checkNotNullExpressionValue(inflate, "from(parent.context)\n   …_app_item, parent, false)");
            return new AppItemViewHolder(inflate);
        }

        public void onBindViewHolder(AppItemViewHolder appItemViewHolder, int i) {
            Intrinsics.checkNotNullParameter(appItemViewHolder, "holder");
            Ref.ObjectRef objectRef = new Ref.ObjectRef();
            synchronized (this.lock) {
                objectRef.element = this.data.get(i);
                Unit unit = Unit.INSTANCE;
            }
            FgsManagerController fgsManagerController = FgsManagerController.this;
            appItemViewHolder.getIconView().setImageDrawable(((RunningApp) objectRef.element).getIcon());
            appItemViewHolder.getAppLabelView().setText(((RunningApp) objectRef.element).getAppLabel());
            appItemViewHolder.getDurationView().setText(DateUtils.formatDuration(Math.max(fgsManagerController.systemClock.elapsedRealtime() - ((RunningApp) objectRef.element).getTimeStarted(), 60000), 20));
            appItemViewHolder.getStopButton().setOnClickListener(new FgsManagerController$AppListAdapter$$ExternalSyntheticLambda0(appItemViewHolder, fgsManagerController, objectRef));
            if (((RunningApp) objectRef.element).getUiControl() == UIControl.HIDE_BUTTON) {
                appItemViewHolder.getStopButton().setVisibility(4);
            }
            if (((RunningApp) objectRef.element).getStopped()) {
                appItemViewHolder.getStopButton().setEnabled(false);
                appItemViewHolder.getStopButton().setText(C1893R.string.fgs_manager_app_item_stop_button_stopped_label);
                appItemViewHolder.getDurationView().setVisibility(8);
                return;
            }
            appItemViewHolder.getStopButton().setEnabled(true);
            appItemViewHolder.getStopButton().setText(C1893R.string.fgs_manager_app_item_stop_button_label);
            appItemViewHolder.getDurationView().setVisibility(0);
        }

        /* access modifiers changed from: private */
        /* renamed from: onBindViewHolder$lambda-2$lambda-1  reason: not valid java name */
        public static final void m2893onBindViewHolder$lambda2$lambda1(AppItemViewHolder appItemViewHolder, FgsManagerController fgsManagerController, Ref.ObjectRef objectRef, View view) {
            Intrinsics.checkNotNullParameter(appItemViewHolder, "$this_with");
            Intrinsics.checkNotNullParameter(fgsManagerController, "this$0");
            Intrinsics.checkNotNullParameter(objectRef, "$runningApp");
            appItemViewHolder.getStopButton().setText(C1893R.string.fgs_manager_app_item_stop_button_stopped_label);
            fgsManagerController.stopPackage(((RunningApp) objectRef.element).getUserId(), ((RunningApp) objectRef.element).getPackageName(), ((RunningApp) objectRef.element).getTimeStarted());
        }

        public int getItemCount() {
            return this.data.size();
        }

        public final void setData(List<RunningApp> list) {
            Intrinsics.checkNotNullParameter(list, "newData");
            Ref.ObjectRef objectRef = new Ref.ObjectRef();
            objectRef.element = this.data;
            this.data = list;
            DiffUtil.calculateDiff(new FgsManagerController$AppListAdapter$setData$1(objectRef, list)).dispatchUpdatesTo((RecyclerView.Adapter) this);
        }
    }

    @Metadata(mo64986d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0004\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dJ\u0013\u0010\u001e\u001a\u00020\u00142\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001H\u0002J\b\u0010 \u001a\u00020\u0003H\u0016J\u0006\u0010!\u001a\u00020\u001bR\u001a\u0010\u0007\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR \u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u000e\u001a\u00020\u000f8F@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\u0014X\u000e¢\u0006\u0002\n\u0000R\u001b\u0010\u0015\u001a\u00020\u00038FX\u0002¢\u0006\f\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0016\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\t¨\u0006\""}, mo64987d2 = {"Lcom/android/systemui/qs/FgsManagerController$UserPackage;", "", "userId", "", "packageName", "", "(Lcom/android/systemui/qs/FgsManagerController;ILjava/lang/String;)V", "backgroundRestrictionExemptionReason", "getBackgroundRestrictionExemptionReason", "()I", "setBackgroundRestrictionExemptionReason", "(I)V", "getPackageName", "()Ljava/lang/String;", "<set-?>", "Lcom/android/systemui/qs/FgsManagerController$UIControl;", "uiControl", "getUiControl", "()Lcom/android/systemui/qs/FgsManagerController$UIControl;", "uiControlInitialized", "", "uid", "getUid", "uid$delegate", "Lkotlin/Lazy;", "getUserId", "dump", "", "pw", "Ljava/io/PrintWriter;", "equals", "other", "hashCode", "updateUiControl", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.qs.FgsManagerController$UserPackage */
    /* compiled from: FgsManagerController.kt */
    private final class UserPackage {
        private int backgroundRestrictionExemptionReason = -1;
        private final String packageName;
        final /* synthetic */ FgsManagerController this$0;
        private UIControl uiControl = UIControl.NORMAL;
        private boolean uiControlInitialized;
        private final Lazy uid$delegate;
        private final int userId;

        public UserPackage(FgsManagerController fgsManagerController, int i, String str) {
            Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
            this.this$0 = fgsManagerController;
            this.userId = i;
            this.packageName = str;
            this.uid$delegate = LazyKt.lazy(new FgsManagerController$UserPackage$uid$2(fgsManagerController, this));
        }

        public final int getUserId() {
            return this.userId;
        }

        public final String getPackageName() {
            return this.packageName;
        }

        public final int getUid() {
            return ((Number) this.uid$delegate.getValue()).intValue();
        }

        public final int getBackgroundRestrictionExemptionReason() {
            return this.backgroundRestrictionExemptionReason;
        }

        public final void setBackgroundRestrictionExemptionReason(int i) {
            this.backgroundRestrictionExemptionReason = i;
        }

        public final UIControl getUiControl() {
            if (!this.uiControlInitialized) {
                updateUiControl();
            }
            return this.uiControl;
        }

        public final void updateUiControl() {
            UIControl uIControl;
            int backgroundRestrictionExemptionReason2 = this.this$0.activityManager.getBackgroundRestrictionExemptionReason(getUid());
            this.backgroundRestrictionExemptionReason = backgroundRestrictionExemptionReason2;
            if (!(backgroundRestrictionExemptionReason2 == 10 || backgroundRestrictionExemptionReason2 == 11)) {
                if (backgroundRestrictionExemptionReason2 != 51 && backgroundRestrictionExemptionReason2 != 63) {
                    if (!(backgroundRestrictionExemptionReason2 == 300 || backgroundRestrictionExemptionReason2 == 318 || backgroundRestrictionExemptionReason2 == 320 || backgroundRestrictionExemptionReason2 == 55 || backgroundRestrictionExemptionReason2 == 56)) {
                        switch (backgroundRestrictionExemptionReason2) {
                            case 322:
                            case 323:
                            case 324:
                                break;
                            default:
                                uIControl = UIControl.NORMAL;
                                break;
                        }
                    }
                } else {
                    uIControl = UIControl.HIDE_ENTRY;
                    this.uiControl = uIControl;
                    this.uiControlInitialized = true;
                }
            }
            uIControl = UIControl.HIDE_BUTTON;
            this.uiControl = uIControl;
            this.uiControlInitialized = true;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof UserPackage)) {
                return false;
            }
            UserPackage userPackage = (UserPackage) obj;
            if (!Intrinsics.areEqual((Object) userPackage.packageName, (Object) this.packageName) || userPackage.userId != this.userId) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.userId), this.packageName);
        }

        public final void dump(PrintWriter printWriter) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            printWriter.println("UserPackage: [");
            boolean z = printWriter instanceof IndentingPrintWriter;
            if (z) {
                ((IndentingPrintWriter) printWriter).increaseIndent();
            }
            printWriter.println("userId=" + this.userId);
            printWriter.println("packageName=" + this.packageName);
            printWriter.println("uiControl=" + getUiControl() + " (reason=" + this.backgroundRestrictionExemptionReason + ')');
            if (z) {
                ((IndentingPrintWriter) printWriter).decreaseIndent();
            }
            printWriter.println(NavigationBarInflaterView.SIZE_MOD_END);
        }
    }

    @Metadata(mo64986d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\rJ\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\u0014\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u000e\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u0017J\u0013\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001b\u001a\u00020\u001cHÖ\u0001J\u0006\u0010\u001d\u001a\u00020\u0019J\u000e\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\rJ\t\u0010\u001f\u001a\u00020 HÖ\u0001R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006!"}, mo64987d2 = {"Lcom/android/systemui/qs/FgsManagerController$StartTimeAndTokens;", "", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "(Lcom/android/systemui/util/time/SystemClock;)V", "startTime", "", "getStartTime", "()J", "getSystemClock", "()Lcom/android/systemui/util/time/SystemClock;", "tokens", "", "Landroid/os/IBinder;", "getTokens", "()Ljava/util/Set;", "addToken", "", "token", "component1", "copy", "dump", "pw", "Ljava/io/PrintWriter;", "equals", "", "other", "hashCode", "", "isEmpty", "removeToken", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.qs.FgsManagerController$StartTimeAndTokens */
    /* compiled from: FgsManagerController.kt */
    private static final class StartTimeAndTokens {
        private final long startTime;
        private final SystemClock systemClock;
        private final Set<IBinder> tokens = new LinkedHashSet();

        public static /* synthetic */ StartTimeAndTokens copy$default(StartTimeAndTokens startTimeAndTokens, SystemClock systemClock2, int i, Object obj) {
            if ((i & 1) != 0) {
                systemClock2 = startTimeAndTokens.systemClock;
            }
            return startTimeAndTokens.copy(systemClock2);
        }

        public final SystemClock component1() {
            return this.systemClock;
        }

        public final StartTimeAndTokens copy(SystemClock systemClock2) {
            Intrinsics.checkNotNullParameter(systemClock2, "systemClock");
            return new StartTimeAndTokens(systemClock2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return (obj instanceof StartTimeAndTokens) && Intrinsics.areEqual((Object) this.systemClock, (Object) ((StartTimeAndTokens) obj).systemClock);
        }

        public int hashCode() {
            return this.systemClock.hashCode();
        }

        public String toString() {
            return "StartTimeAndTokens(systemClock=" + this.systemClock + ')';
        }

        public StartTimeAndTokens(SystemClock systemClock2) {
            Intrinsics.checkNotNullParameter(systemClock2, "systemClock");
            this.systemClock = systemClock2;
            this.startTime = systemClock2.elapsedRealtime();
        }

        public final SystemClock getSystemClock() {
            return this.systemClock;
        }

        public final long getStartTime() {
            return this.startTime;
        }

        public final Set<IBinder> getTokens() {
            return this.tokens;
        }

        public final void addToken(IBinder iBinder) {
            Intrinsics.checkNotNullParameter(iBinder, "token");
            this.tokens.add(iBinder);
        }

        public final void removeToken(IBinder iBinder) {
            Intrinsics.checkNotNullParameter(iBinder, "token");
            this.tokens.remove(iBinder);
        }

        public final boolean isEmpty() {
            return this.tokens.isEmpty();
        }

        public final void dump(PrintWriter printWriter) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            printWriter.println("StartTimeAndTokens: [");
            boolean z = printWriter instanceof IndentingPrintWriter;
            if (z) {
                ((IndentingPrintWriter) printWriter).increaseIndent();
            }
            printWriter.println("startTime=" + this.startTime + " (time running = " + (this.systemClock.elapsedRealtime() - this.startTime) + "ms)");
            printWriter.println("tokens: [");
            if (z) {
                ((IndentingPrintWriter) printWriter).increaseIndent();
            }
            for (IBinder valueOf : this.tokens) {
                printWriter.println(String.valueOf((Object) valueOf));
            }
            if (z) {
                ((IndentingPrintWriter) printWriter).decreaseIndent();
            }
            printWriter.println(NavigationBarInflaterView.SIZE_MOD_END);
            if (z) {
                ((IndentingPrintWriter) printWriter).decreaseIndent();
            }
            printWriter.println(NavigationBarInflaterView.SIZE_MOD_END);
        }
    }

    @Metadata(mo64986d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\bR\u0011\u0010\u000b\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u0010¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006\u0013"}, mo64987d2 = {"Lcom/android/systemui/qs/FgsManagerController$AppItemViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "parent", "Landroid/view/View;", "(Landroid/view/View;)V", "appLabelView", "Landroid/widget/TextView;", "getAppLabelView", "()Landroid/widget/TextView;", "durationView", "getDurationView", "iconView", "Landroid/widget/ImageView;", "getIconView", "()Landroid/widget/ImageView;", "stopButton", "Landroid/widget/Button;", "getStopButton", "()Landroid/widget/Button;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.qs.FgsManagerController$AppItemViewHolder */
    /* compiled from: FgsManagerController.kt */
    private static final class AppItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView appLabelView;
        private final TextView durationView;
        private final ImageView iconView;
        private final Button stopButton;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public AppItemViewHolder(View view) {
            super(view);
            Intrinsics.checkNotNullParameter(view, "parent");
            View requireViewById = view.requireViewById(C1893R.C1897id.fgs_manager_app_item_label);
            Intrinsics.checkNotNullExpressionValue(requireViewById, "parent.requireViewById(R…s_manager_app_item_label)");
            this.appLabelView = (TextView) requireViewById;
            View requireViewById2 = view.requireViewById(C1893R.C1897id.fgs_manager_app_item_duration);
            Intrinsics.checkNotNullExpressionValue(requireViewById2, "parent.requireViewById(R…anager_app_item_duration)");
            this.durationView = (TextView) requireViewById2;
            View requireViewById3 = view.requireViewById(C1893R.C1897id.fgs_manager_app_item_icon);
            Intrinsics.checkNotNullExpressionValue(requireViewById3, "parent.requireViewById(R…gs_manager_app_item_icon)");
            this.iconView = (ImageView) requireViewById3;
            View requireViewById4 = view.requireViewById(C1893R.C1897id.fgs_manager_app_item_stop_button);
            Intrinsics.checkNotNullExpressionValue(requireViewById4, "parent.requireViewById(R…ger_app_item_stop_button)");
            this.stopButton = (Button) requireViewById4;
        }

        public final TextView getAppLabelView() {
            return this.appLabelView;
        }

        public final TextView getDurationView() {
            return this.durationView;
        }

        public final ImageView getIconView() {
            return this.iconView;
        }

        public final Button getStopButton() {
            return this.stopButton;
        }
    }

    @Metadata(mo64986d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0010\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\b\u0018\u00002\u00020\u0001B7\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eB%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\u000fJ\t\u0010&\u001a\u00020\u0003HÆ\u0003J\t\u0010'\u001a\u00020\u0005HÆ\u0003J\t\u0010(\u001a\u00020\u0007HÆ\u0003J\t\u0010)\u001a\u00020\tHÆ\u0003J1\u0010*\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\tHÆ\u0001J\u0016\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u000200J\u0013\u00101\u001a\u00020\u001b2\b\u00102\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u00103\u001a\u00020\u0003HÖ\u0001J\t\u00104\u001a\u00020\u0005HÖ\u0001R\u001a\u0010\n\u001a\u00020\u000bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001c\u0010\f\u001a\u0004\u0018\u00010\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u001a\u0010\u001a\u001a\u00020\u001bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b$\u0010%¨\u00065"}, mo64987d2 = {"Lcom/android/systemui/qs/FgsManagerController$RunningApp;", "", "userId", "", "packageName", "", "timeStarted", "", "uiControl", "Lcom/android/systemui/qs/FgsManagerController$UIControl;", "appLabel", "", "icon", "Landroid/graphics/drawable/Drawable;", "(ILjava/lang/String;JLcom/android/systemui/qs/FgsManagerController$UIControl;Ljava/lang/CharSequence;Landroid/graphics/drawable/Drawable;)V", "(ILjava/lang/String;JLcom/android/systemui/qs/FgsManagerController$UIControl;)V", "getAppLabel", "()Ljava/lang/CharSequence;", "setAppLabel", "(Ljava/lang/CharSequence;)V", "getIcon", "()Landroid/graphics/drawable/Drawable;", "setIcon", "(Landroid/graphics/drawable/Drawable;)V", "getPackageName", "()Ljava/lang/String;", "stopped", "", "getStopped", "()Z", "setStopped", "(Z)V", "getTimeStarted", "()J", "getUiControl", "()Lcom/android/systemui/qs/FgsManagerController$UIControl;", "getUserId", "()I", "component1", "component2", "component3", "component4", "copy", "dump", "", "pw", "Ljava/io/PrintWriter;", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "equals", "other", "hashCode", "toString", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.qs.FgsManagerController$RunningApp */
    /* compiled from: FgsManagerController.kt */
    private static final class RunningApp {
        private CharSequence appLabel;
        private Drawable icon;
        private final String packageName;
        private boolean stopped;
        private final long timeStarted;
        private final UIControl uiControl;
        private final int userId;

        public static /* synthetic */ RunningApp copy$default(RunningApp runningApp, int i, String str, long j, UIControl uIControl, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                i = runningApp.userId;
            }
            if ((i2 & 2) != 0) {
                str = runningApp.packageName;
            }
            String str2 = str;
            if ((i2 & 4) != 0) {
                j = runningApp.timeStarted;
            }
            long j2 = j;
            if ((i2 & 8) != 0) {
                uIControl = runningApp.uiControl;
            }
            return runningApp.copy(i, str2, j2, uIControl);
        }

        public final int component1() {
            return this.userId;
        }

        public final String component2() {
            return this.packageName;
        }

        public final long component3() {
            return this.timeStarted;
        }

        public final UIControl component4() {
            return this.uiControl;
        }

        public final RunningApp copy(int i, String str, long j, UIControl uIControl) {
            Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
            Intrinsics.checkNotNullParameter(uIControl, "uiControl");
            return new RunningApp(i, str, j, uIControl);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof RunningApp)) {
                return false;
            }
            RunningApp runningApp = (RunningApp) obj;
            return this.userId == runningApp.userId && Intrinsics.areEqual((Object) this.packageName, (Object) runningApp.packageName) && this.timeStarted == runningApp.timeStarted && this.uiControl == runningApp.uiControl;
        }

        public int hashCode() {
            return (((((Integer.hashCode(this.userId) * 31) + this.packageName.hashCode()) * 31) + Long.hashCode(this.timeStarted)) * 31) + this.uiControl.hashCode();
        }

        public String toString() {
            return "RunningApp(userId=" + this.userId + ", packageName=" + this.packageName + ", timeStarted=" + this.timeStarted + ", uiControl=" + this.uiControl + ')';
        }

        public RunningApp(int i, String str, long j, UIControl uIControl) {
            Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
            Intrinsics.checkNotNullParameter(uIControl, "uiControl");
            this.userId = i;
            this.packageName = str;
            this.timeStarted = j;
            this.uiControl = uIControl;
            this.appLabel = "";
        }

        public final int getUserId() {
            return this.userId;
        }

        public final String getPackageName() {
            return this.packageName;
        }

        public final long getTimeStarted() {
            return this.timeStarted;
        }

        public final UIControl getUiControl() {
            return this.uiControl;
        }

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public RunningApp(int i, String str, long j, UIControl uIControl, CharSequence charSequence, Drawable drawable) {
            this(i, str, j, uIControl);
            Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
            Intrinsics.checkNotNullParameter(uIControl, "uiControl");
            Intrinsics.checkNotNullParameter(charSequence, "appLabel");
            Intrinsics.checkNotNullParameter(drawable, BaseIconCache.IconDB.COLUMN_ICON);
            this.appLabel = charSequence;
            this.icon = drawable;
        }

        public final CharSequence getAppLabel() {
            return this.appLabel;
        }

        public final void setAppLabel(CharSequence charSequence) {
            Intrinsics.checkNotNullParameter(charSequence, "<set-?>");
            this.appLabel = charSequence;
        }

        public final Drawable getIcon() {
            return this.icon;
        }

        public final void setIcon(Drawable drawable) {
            this.icon = drawable;
        }

        public final boolean getStopped() {
            return this.stopped;
        }

        public final void setStopped(boolean z) {
            this.stopped = z;
        }

        public final void dump(PrintWriter printWriter, SystemClock systemClock) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            Intrinsics.checkNotNullParameter(systemClock, "systemClock");
            printWriter.println("RunningApp: [");
            boolean z = printWriter instanceof IndentingPrintWriter;
            if (z) {
                ((IndentingPrintWriter) printWriter).increaseIndent();
            }
            printWriter.println("userId=" + this.userId);
            printWriter.println("packageName=" + this.packageName);
            printWriter.println("timeStarted=" + this.timeStarted + " (time since start = " + (systemClock.elapsedRealtime() - this.timeStarted) + "ms)");
            printWriter.println("uiControl=" + this.uiControl);
            printWriter.println("appLabel=" + this.appLabel);
            printWriter.println("icon=" + this.icon);
            printWriter.println("stopped=" + this.stopped);
            if (z) {
                ((IndentingPrintWriter) printWriter).decreaseIndent();
            }
            printWriter.println(NavigationBarInflaterView.SIZE_MOD_END);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "printwriter");
        Intrinsics.checkNotNullParameter(strArr, "args");
        PrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        synchronized (this.lock) {
            indentingPrintWriter.println("current user profiles = " + this.currentProfileIds);
            indentingPrintWriter.println("changesSinceDialog=" + this.changesSinceDialog);
            indentingPrintWriter.println("Running service tokens: [");
            IndentingPrintWriter indentingPrintWriter2 = indentingPrintWriter;
            indentingPrintWriter2.increaseIndent();
            for (Map.Entry next : this.runningServiceTokens.entrySet()) {
                indentingPrintWriter.println("{");
                IndentingPrintWriter indentingPrintWriter3 = indentingPrintWriter;
                indentingPrintWriter3.increaseIndent();
                ((UserPackage) next.getKey()).dump(indentingPrintWriter);
                ((StartTimeAndTokens) next.getValue()).dump(indentingPrintWriter);
                indentingPrintWriter3.decreaseIndent();
                indentingPrintWriter.println("}");
            }
            indentingPrintWriter2.decreaseIndent();
            indentingPrintWriter.println(NavigationBarInflaterView.SIZE_MOD_END);
            indentingPrintWriter.println("Loaded package UI info: [");
            IndentingPrintWriter indentingPrintWriter4 = indentingPrintWriter;
            indentingPrintWriter4.increaseIndent();
            for (Map.Entry entry : this.runningApps.entrySet()) {
                indentingPrintWriter.println("{");
                IndentingPrintWriter indentingPrintWriter5 = indentingPrintWriter;
                indentingPrintWriter5.increaseIndent();
                ((UserPackage) entry.getKey()).dump(indentingPrintWriter);
                ((RunningApp) entry.getValue()).dump(indentingPrintWriter, this.systemClock);
                indentingPrintWriter5.decreaseIndent();
                indentingPrintWriter.println("}");
            }
            indentingPrintWriter4.decreaseIndent();
            indentingPrintWriter.println(NavigationBarInflaterView.SIZE_MOD_END);
            Unit unit = Unit.INSTANCE;
        }
    }
}

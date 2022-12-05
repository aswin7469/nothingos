package com.android.systemui.privacy;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import androidx.constraintlayout.widget.R$styleable;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.appops.AppOpItem;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysJvmKt;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: PrivacyItemController.kt */
/* loaded from: classes.dex */
public final class PrivacyItemController implements Dumpable {
    @NotNull
    public static final Companion Companion;
    @NotNull
    private static final int[] OPS;
    @NotNull
    private static final IntentFilter intentFilter;
    private boolean allIndicatorsAvailable;
    @NotNull
    private final AppOpsController appOpsController;
    @NotNull
    private final DelayableExecutor bgExecutor;
    @NotNull
    private final PrivacyItemController$cb$1 cb;
    @NotNull
    private final DeviceConfigProxy deviceConfigProxy;
    @NotNull
    private final PrivacyItemController$devicePropertiesChangedListener$1 devicePropertiesChangedListener;
    @Nullable
    private Runnable holdingRunnableCanceler;
    @NotNull
    private final MyExecutor internalUiExecutor;
    private boolean listening;
    private boolean locationAvailable;
    @NotNull
    private final PrivacyLogger logger;
    @NotNull
    private final ArrayList<String> privacyCameraWhiteList;
    @NotNull
    private final SystemClock systemClock;
    @NotNull
    private final Runnable updateListAndNotifyChanges;
    @NotNull
    private final UserTracker userTracker;
    @NotNull
    private UserTracker.Callback userTrackerCallback;
    @NotNull
    private static final int[] OPS_MIC_CAMERA = {26, R$styleable.Constraint_layout_goneMarginRight, 27, 100};
    @NotNull
    private static final int[] OPS_LOCATION = {0, 1};
    @NotNull
    private List<PrivacyItem> privacyList = CollectionsKt.emptyList();
    @NotNull
    private List<Integer> currentUserIds = CollectionsKt.emptyList();
    @NotNull
    private final List<WeakReference<Callback>> callbacks = new ArrayList();
    @NotNull
    private final Runnable notifyChanges = new Runnable() { // from class: com.android.systemui.privacy.PrivacyItemController$notifyChanges$1
        @Override // java.lang.Runnable
        public final void run() {
            List<WeakReference> list;
            List<PrivacyItem> privacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core = PrivacyItemController.this.getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
            list = PrivacyItemController.this.callbacks;
            for (WeakReference weakReference : list) {
                PrivacyItemController.Callback callback = (PrivacyItemController.Callback) weakReference.get();
                if (callback != null) {
                    callback.onPrivacyItemsChanged(privacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core);
                }
            }
        }
    };
    private boolean micCameraAvailable = isMicCameraEnabled();

    /* compiled from: PrivacyItemController.kt */
    /* loaded from: classes.dex */
    public interface Callback {
        default void onFlagLocationChanged(boolean z) {
        }

        default void onFlagMicCameraChanged(boolean z) {
        }

        void onPrivacyItemsChanged(@NotNull List<PrivacyItem> list);
    }

    @VisibleForTesting
    public static /* synthetic */ void getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getUserTrackerCallback$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v11, types: [android.provider.DeviceConfig$OnPropertiesChangedListener, com.android.systemui.privacy.PrivacyItemController$devicePropertiesChangedListener$1] */
    /* JADX WARN: Type inference failed for: r4v2, types: [com.android.systemui.privacy.PrivacyItemController$cb$1] */
    public PrivacyItemController(@NotNull AppOpsController appOpsController, @NotNull final DelayableExecutor uiExecutor, @NotNull DelayableExecutor bgExecutor, @NotNull DeviceConfigProxy deviceConfigProxy, @NotNull UserTracker userTracker, @NotNull PrivacyLogger logger, @NotNull SystemClock systemClock, @NotNull DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(appOpsController, "appOpsController");
        Intrinsics.checkNotNullParameter(uiExecutor, "uiExecutor");
        Intrinsics.checkNotNullParameter(bgExecutor, "bgExecutor");
        Intrinsics.checkNotNullParameter(deviceConfigProxy, "deviceConfigProxy");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        Intrinsics.checkNotNullParameter(logger, "logger");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.appOpsController = appOpsController;
        this.bgExecutor = bgExecutor;
        this.deviceConfigProxy = deviceConfigProxy;
        this.userTracker = userTracker;
        this.logger = logger;
        this.systemClock = systemClock;
        this.internalUiExecutor = new MyExecutor(this, uiExecutor);
        this.updateListAndNotifyChanges = new Runnable() { // from class: com.android.systemui.privacy.PrivacyItemController$updateListAndNotifyChanges$1
            @Override // java.lang.Runnable
            public final void run() {
                Runnable runnable;
                PrivacyItemController.this.updatePrivacyList();
                DelayableExecutor delayableExecutor = uiExecutor;
                runnable = PrivacyItemController.this.notifyChanges;
                delayableExecutor.execute(runnable);
            }
        };
        boolean isLocationEnabled = isLocationEnabled();
        this.locationAvailable = isLocationEnabled;
        this.allIndicatorsAvailable = this.micCameraAvailable && isLocationEnabled;
        ?? r2 = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.systemui.privacy.PrivacyItemController$devicePropertiesChangedListener$1
            public void onPropertiesChanged(@NotNull DeviceConfig.Properties properties) {
                PrivacyItemController.MyExecutor myExecutor;
                List<WeakReference> list;
                List<WeakReference> list2;
                Intrinsics.checkNotNullParameter(properties, "properties");
                if ("privacy".equals(properties.getNamespace())) {
                    if (!properties.getKeyset().contains("camera_mic_icons_enabled") && !properties.getKeyset().contains("location_indicators_enabled")) {
                        return;
                    }
                    boolean z = false;
                    if (properties.getKeyset().contains("camera_mic_icons_enabled")) {
                        PrivacyItemController.this.micCameraAvailable = properties.getBoolean("camera_mic_icons_enabled", true);
                        PrivacyItemController privacyItemController = PrivacyItemController.this;
                        privacyItemController.setAllIndicatorsAvailable(privacyItemController.getMicCameraAvailable() && PrivacyItemController.this.getLocationAvailable());
                        list2 = PrivacyItemController.this.callbacks;
                        PrivacyItemController privacyItemController2 = PrivacyItemController.this;
                        for (WeakReference weakReference : list2) {
                            PrivacyItemController.Callback callback = (PrivacyItemController.Callback) weakReference.get();
                            if (callback != null) {
                                callback.onFlagMicCameraChanged(privacyItemController2.getMicCameraAvailable());
                            }
                        }
                    }
                    if (properties.getKeyset().contains("location_indicators_enabled")) {
                        PrivacyItemController.this.setLocationAvailable(properties.getBoolean("location_indicators_enabled", false));
                        PrivacyItemController privacyItemController3 = PrivacyItemController.this;
                        if (privacyItemController3.getMicCameraAvailable() && PrivacyItemController.this.getLocationAvailable()) {
                            z = true;
                        }
                        privacyItemController3.setAllIndicatorsAvailable(z);
                        list = PrivacyItemController.this.callbacks;
                        PrivacyItemController privacyItemController4 = PrivacyItemController.this;
                        for (WeakReference weakReference2 : list) {
                            PrivacyItemController.Callback callback2 = (PrivacyItemController.Callback) weakReference2.get();
                            if (callback2 != null) {
                                callback2.onFlagLocationChanged(privacyItemController4.getLocationAvailable());
                            }
                        }
                    }
                    myExecutor = PrivacyItemController.this.internalUiExecutor;
                    myExecutor.updateListeningState();
                }
            }
        };
        this.devicePropertiesChangedListener = r2;
        this.cb = new AppOpsController.Callback() { // from class: com.android.systemui.privacy.PrivacyItemController$cb$1
            @Override // com.android.systemui.appops.AppOpsController.Callback
            public void onActiveStateChanged(int i, int i2, @NotNull String packageName, boolean z) {
                boolean contains;
                List list;
                PrivacyLogger privacyLogger;
                Intrinsics.checkNotNullParameter(packageName, "packageName");
                contains = ArraysKt___ArraysKt.contains(PrivacyItemController.Companion.getOPS_LOCATION(), i);
                if (!contains || PrivacyItemController.this.getLocationAvailable()) {
                    int userId = UserHandle.getUserId(i2);
                    list = PrivacyItemController.this.currentUserIds;
                    if (!list.contains(Integer.valueOf(userId)) && i != 100 && i != 101) {
                        return;
                    }
                    privacyLogger = PrivacyItemController.this.logger;
                    privacyLogger.logUpdatedItemFromAppOps(i, i2, packageName, z);
                    PrivacyItemController.this.update(false);
                }
            }
        };
        this.userTrackerCallback = new UserTracker.Callback() { // from class: com.android.systemui.privacy.PrivacyItemController$userTrackerCallback$1
            @Override // com.android.systemui.settings.UserTracker.Callback
            public void onUserChanged(int i, @NotNull Context userContext) {
                Intrinsics.checkNotNullParameter(userContext, "userContext");
                PrivacyItemController.this.update(true);
            }

            @Override // com.android.systemui.settings.UserTracker.Callback
            public void onProfilesChanged(@NotNull List<? extends UserInfo> profiles) {
                Intrinsics.checkNotNullParameter(profiles, "profiles");
                PrivacyItemController.this.update(true);
            }
        };
        deviceConfigProxy.addOnPropertiesChangedListener("privacy", uiExecutor, r2);
        dumpManager.registerDumpable("PrivacyItemController", this);
        this.privacyCameraWhiteList = new ArrayList<String>() { // from class: com.android.systemui.privacy.PrivacyItemController$privacyCameraWhiteList$1
            /* JADX INFO: Access modifiers changed from: package-private */
            {
                add("com.nt.facerecognition");
            }

            @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
            public final /* bridge */ boolean contains(Object obj) {
                if (!(obj == null ? true : obj instanceof String)) {
                    return false;
                }
                return contains((String) obj);
            }

            public /* bridge */ boolean contains(String str) {
                return super.contains((Object) str);
            }

            public /* bridge */ int getSize() {
                return super.size();
            }

            @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
            public final /* bridge */ int indexOf(Object obj) {
                if (!(obj == null ? true : obj instanceof String)) {
                    return -1;
                }
                return indexOf((String) obj);
            }

            public /* bridge */ int indexOf(String str) {
                return super.indexOf((Object) str);
            }

            @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
            public final /* bridge */ int lastIndexOf(Object obj) {
                if (!(obj == null ? true : obj instanceof String)) {
                    return -1;
                }
                return lastIndexOf((String) obj);
            }

            public /* bridge */ int lastIndexOf(String str) {
                return super.lastIndexOf((Object) str);
            }

            @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
            public final /* bridge */ boolean remove(Object obj) {
                if (!(obj == null ? true : obj instanceof String)) {
                    return false;
                }
                return remove((String) obj);
            }

            public /* bridge */ boolean remove(String str) {
                return super.remove((Object) str);
            }

            @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
            public final /* bridge */ int size() {
                return getSize();
            }
        };
    }

    /* compiled from: PrivacyItemController.kt */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @VisibleForTesting
        public static /* synthetic */ void getTIME_TO_HOLD_INDICATORS$annotations() {
        }

        private Companion() {
        }

        @NotNull
        public final int[] getOPS_MIC_CAMERA() {
            return PrivacyItemController.OPS_MIC_CAMERA;
        }

        @NotNull
        public final int[] getOPS_LOCATION() {
            return PrivacyItemController.OPS_LOCATION;
        }

        @NotNull
        public final int[] getOPS() {
            return PrivacyItemController.OPS;
        }
    }

    static {
        int[] plus;
        Companion companion = new Companion(null);
        Companion = companion;
        plus = ArraysKt___ArraysJvmKt.plus(companion.getOPS_MIC_CAMERA(), companion.getOPS_LOCATION());
        OPS = plus;
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.USER_SWITCHED");
        intentFilter2.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
        intentFilter2.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
        intentFilter = intentFilter2;
    }

    @NotNull
    public final synchronized List<PrivacyItem> getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return CollectionsKt.toList(this.privacyList);
    }

    private final boolean isMicCameraEnabled() {
        return this.deviceConfigProxy.getBoolean("privacy", "camera_mic_icons_enabled", true);
    }

    private final boolean isLocationEnabled() {
        return this.deviceConfigProxy.getBoolean("privacy", "location_indicators_enabled", false);
    }

    public final boolean getMicCameraAvailable() {
        return this.micCameraAvailable;
    }

    public final boolean getLocationAvailable() {
        return this.locationAvailable;
    }

    public final void setLocationAvailable(boolean z) {
        this.locationAvailable = z;
    }

    public final boolean getAllIndicatorsAvailable() {
        return this.allIndicatorsAvailable;
    }

    public final void setAllIndicatorsAvailable(boolean z) {
        this.allIndicatorsAvailable = z;
    }

    private final void unregisterListener() {
        this.userTracker.removeCallback(this.userTrackerCallback);
    }

    private final void registerReceiver() {
        this.userTracker.addCallback(this.userTrackerCallback, this.bgExecutor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void update(final boolean z) {
        this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.privacy.PrivacyItemController$update$1
            @Override // java.lang.Runnable
            public final void run() {
                Runnable runnable;
                UserTracker userTracker;
                int collectionSizeOrDefault;
                PrivacyLogger privacyLogger;
                List<Integer> list;
                if (z) {
                    PrivacyItemController privacyItemController = this;
                    userTracker = privacyItemController.userTracker;
                    List<UserInfo> userProfiles = userTracker.getUserProfiles();
                    collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(userProfiles, 10);
                    ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
                    for (UserInfo userInfo : userProfiles) {
                        arrayList.add(Integer.valueOf(userInfo.id));
                    }
                    privacyItemController.currentUserIds = arrayList;
                    privacyLogger = this.logger;
                    list = this.currentUserIds;
                    privacyLogger.logCurrentProfilesChanged(list);
                }
                runnable = this.updateListAndNotifyChanges;
                runnable.run();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setListeningState() {
        boolean z = (!this.callbacks.isEmpty()) & (this.micCameraAvailable || this.locationAvailable);
        if (this.listening == z) {
            return;
        }
        this.listening = z;
        if (z) {
            this.appOpsController.addCallback(Companion.getOPS(), this.cb);
            registerReceiver();
            update(true);
            return;
        }
        this.appOpsController.removeCallback(Companion.getOPS(), this.cb);
        unregisterListener();
        update(false);
    }

    private final void addCallback(WeakReference<Callback> weakReference) {
        this.callbacks.add(weakReference);
        if ((!this.callbacks.isEmpty()) && !this.listening) {
            this.internalUiExecutor.updateListeningState();
        } else if (!this.listening) {
        } else {
            this.internalUiExecutor.execute(new NotifyChangesToCallback(weakReference.get(), getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core()));
        }
    }

    private final void removeCallback(final WeakReference<Callback> weakReference) {
        this.callbacks.removeIf(new Predicate<WeakReference<Callback>>() { // from class: com.android.systemui.privacy.PrivacyItemController$removeCallback$1
            @Override // java.util.function.Predicate
            public final boolean test(@NotNull WeakReference<PrivacyItemController.Callback> it) {
                Intrinsics.checkNotNullParameter(it, "it");
                PrivacyItemController.Callback callback = it.get();
                if (callback == null) {
                    return true;
                }
                return callback.equals(weakReference.get());
            }
        });
        if (this.callbacks.isEmpty()) {
            this.internalUiExecutor.updateListeningState();
        }
    }

    public final void addCallback(@NotNull Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        addCallback(new WeakReference<>(callback));
    }

    public final void removeCallback(@NotNull Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        removeCallback(new WeakReference<>(callback));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updatePrivacyList() {
        Runnable runnable = this.holdingRunnableCanceler;
        if (runnable != null) {
            runnable.run();
            Unit unit = Unit.INSTANCE;
            this.holdingRunnableCanceler = null;
        }
        if (!this.listening) {
            this.privacyList = CollectionsKt.emptyList();
            return;
        }
        List<AppOpItem> activeAppOps = this.appOpsController.getActiveAppOps(true);
        Intrinsics.checkNotNullExpressionValue(activeAppOps, "appOpsController.getActiveAppOps(true)");
        ArrayList<AppOpItem> arrayList = new ArrayList();
        for (Object obj : activeAppOps) {
            AppOpItem appOpItem = (AppOpItem) obj;
            if (this.currentUserIds.contains(Integer.valueOf(UserHandle.getUserId(appOpItem.getUid()))) || appOpItem.getCode() == 100 || appOpItem.getCode() == 101) {
                arrayList.add(obj);
            }
        }
        ArrayList arrayList2 = new ArrayList();
        for (AppOpItem it : arrayList) {
            Intrinsics.checkNotNullExpressionValue(it, "it");
            PrivacyItem privacyItem = toPrivacyItem(it);
            if (privacyItem != null) {
                arrayList2.add(privacyItem);
            }
        }
        this.privacyList = processNewList(CollectionsKt.distinct(arrayList2));
    }

    private final List<PrivacyItem> processNewList(List<PrivacyItem> list) {
        Object obj;
        this.logger.logRetrievedPrivacyItemsList(list);
        long elapsedRealtime = this.systemClock.elapsedRealtime() - 5000;
        List<PrivacyItem> privacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core = getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
        ArrayList arrayList = new ArrayList();
        Iterator<T> it = privacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core.iterator();
        while (true) {
            boolean z = true;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            PrivacyItem privacyItem = (PrivacyItem) next;
            if (privacyItem.getTimeStampElapsed() <= elapsedRealtime || isIn(privacyItem, list)) {
                z = false;
            }
            if (z) {
                arrayList.add(next);
            }
        }
        if (!arrayList.isEmpty()) {
            this.logger.logPrivacyItemsToHold(arrayList);
            Iterator it2 = arrayList.iterator();
            if (!it2.hasNext()) {
                obj = null;
            } else {
                Object next2 = it2.next();
                if (!it2.hasNext()) {
                    obj = next2;
                } else {
                    long timeStampElapsed = ((PrivacyItem) next2).getTimeStampElapsed();
                    do {
                        Object next3 = it2.next();
                        long timeStampElapsed2 = ((PrivacyItem) next3).getTimeStampElapsed();
                        if (timeStampElapsed > timeStampElapsed2) {
                            next2 = next3;
                            timeStampElapsed = timeStampElapsed2;
                        }
                    } while (it2.hasNext());
                    obj = next2;
                }
            }
            PrivacyItem privacyItem2 = (PrivacyItem) obj;
            Intrinsics.checkNotNull(privacyItem2);
            long timeStampElapsed3 = privacyItem2.getTimeStampElapsed() - elapsedRealtime;
            this.logger.logPrivacyItemsUpdateScheduled(timeStampElapsed3);
            this.holdingRunnableCanceler = this.bgExecutor.executeDelayed(this.updateListAndNotifyChanges, timeStampElapsed3);
        }
        ArrayList arrayList2 = new ArrayList();
        for (Object obj2 : list) {
            if (!((PrivacyItem) obj2).getPaused()) {
                arrayList2.add(obj2);
            }
        }
        return CollectionsKt.plus((Collection) arrayList2, (Iterable) arrayList);
    }

    private final PrivacyItem toPrivacyItem(AppOpItem appOpItem) {
        PrivacyType privacyType;
        int code = appOpItem.getCode();
        if (code != 0 && code != 1) {
            if (code != 26) {
                if (code == 27 || code == 100) {
                    privacyType = PrivacyType.TYPE_MICROPHONE;
                } else if (code != 101) {
                    return null;
                }
            }
            privacyType = PrivacyType.TYPE_CAMERA;
        } else {
            privacyType = PrivacyType.TYPE_LOCATION;
        }
        PrivacyType privacyType2 = privacyType;
        if (privacyType2 != PrivacyType.TYPE_LOCATION || this.locationAvailable) {
            if (this.privacyCameraWhiteList.contains(appOpItem.getPackageName()) && privacyType2 == PrivacyType.TYPE_CAMERA) {
                return null;
            }
            String packageName = appOpItem.getPackageName();
            Intrinsics.checkNotNullExpressionValue(packageName, "appOpItem.packageName");
            return new PrivacyItem(privacyType2, new PrivacyApplication(packageName, appOpItem.getUid()), appOpItem.getTimeStartedElapsed(), appOpItem.isDisabled());
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PrivacyItemController.kt */
    /* loaded from: classes.dex */
    public static final class NotifyChangesToCallback implements Runnable {
        @Nullable
        private final Callback callback;
        @NotNull
        private final List<PrivacyItem> list;

        public NotifyChangesToCallback(@Nullable Callback callback, @NotNull List<PrivacyItem> list) {
            Intrinsics.checkNotNullParameter(list, "list");
            this.callback = callback;
            this.list = list;
        }

        @Override // java.lang.Runnable
        public void run() {
            Callback callback = this.callback;
            if (callback == null) {
                return;
            }
            callback.onPrivacyItemsChanged(this.list);
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        pw.println("PrivacyItemController state:");
        pw.println(Intrinsics.stringPlus("  Listening: ", Boolean.valueOf(this.listening)));
        pw.println(Intrinsics.stringPlus("  Current user ids: ", this.currentUserIds));
        pw.println("  Privacy Items:");
        for (PrivacyItem privacyItem : getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            pw.print("    ");
            pw.println(privacyItem.toString());
        }
        pw.println("  Callbacks:");
        Iterator<T> it = this.callbacks.iterator();
        while (it.hasNext()) {
            Callback callback = (Callback) ((WeakReference) it.next()).get();
            if (callback != null) {
                pw.print("    ");
                pw.println(callback.toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PrivacyItemController.kt */
    /* loaded from: classes.dex */
    public final class MyExecutor implements Executor {
        @NotNull
        private final DelayableExecutor delegate;
        @Nullable
        private Runnable listeningCanceller;
        final /* synthetic */ PrivacyItemController this$0;

        public MyExecutor(@NotNull PrivacyItemController this$0, DelayableExecutor delegate) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(delegate, "delegate");
            this.this$0 = this$0;
            this.delegate = delegate;
        }

        @Override // java.util.concurrent.Executor
        public void execute(@NotNull Runnable command) {
            Intrinsics.checkNotNullParameter(command, "command");
            this.delegate.execute(command);
        }

        public final void updateListeningState() {
            Runnable runnable = this.listeningCanceller;
            if (runnable != null) {
                runnable.run();
            }
            DelayableExecutor delayableExecutor = this.delegate;
            final PrivacyItemController privacyItemController = this.this$0;
            this.listeningCanceller = delayableExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.privacy.PrivacyItemController$MyExecutor$updateListeningState$1
                @Override // java.lang.Runnable
                public final void run() {
                    PrivacyItemController.this.setListeningState();
                }
            }, 0L);
        }
    }

    private final boolean isIn(PrivacyItem privacyItem, List<PrivacyItem> list) {
        boolean z;
        if (!(list instanceof Collection) || !list.isEmpty()) {
            for (PrivacyItem privacyItem2 : list) {
                if (privacyItem2.getPrivacyType() == privacyItem.getPrivacyType() && Intrinsics.areEqual(privacyItem2.getApplication(), privacyItem.getApplication()) && privacyItem2.getTimeStampElapsed() == privacyItem.getTimeStampElapsed()) {
                    z = true;
                    continue;
                } else {
                    z = false;
                    continue;
                }
                if (z) {
                    return true;
                }
            }
        }
        return false;
    }
}

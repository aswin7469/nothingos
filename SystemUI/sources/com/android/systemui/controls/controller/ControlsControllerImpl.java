package com.android.systemui.controls.controller;

import android.app.PendingIntent;
import android.app.backup.BackupManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.UserHandle;
import android.service.controls.Control;
import android.service.controls.actions.ControlAction;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.controller.ControlsBindingController;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt___SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ControlsControllerImpl.kt */
/* loaded from: classes.dex */
public final class ControlsControllerImpl implements Dumpable, ControlsController {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private AuxiliaryPersistenceWrapper auxiliaryPersistenceWrapper;
    @NotNull
    private final ControlsBindingController bindingController;
    @NotNull
    private final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    private final Context context;
    @NotNull
    private UserHandle currentUser;
    @NotNull
    private final DelayableExecutor executor;
    @NotNull
    private final ControlsControllerImpl$listingCallback$1 listingCallback;
    @NotNull
    private final ControlsListingController listingController;
    @NotNull
    private final ControlsFavoritePersistenceWrapper persistenceWrapper;
    @NotNull
    private final BroadcastReceiver restoreFinishedReceiver;
    private boolean seedingInProgress;
    @NotNull
    private final ControlsUiController uiController;
    private boolean userChanging;
    @NotNull
    private UserStructure userStructure;
    @NotNull
    private final ControlsControllerImpl$userSwitchReceiver$1 userSwitchReceiver;
    @NotNull
    private final List<Consumer<Boolean>> seedingCallbacks = new ArrayList();
    @NotNull
    private final ContentObserver settingObserver = new ContentObserver() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$settingObserver$1
        /* JADX INFO: Access modifiers changed from: package-private */
        {
            super(null);
        }

        public void onChange(boolean z, @NotNull Collection<? extends Uri> uris, int i, int i2) {
            boolean z2;
            Intrinsics.checkNotNullParameter(uris, "uris");
            z2 = ControlsControllerImpl.this.userChanging;
            if (z2 || i2 != ControlsControllerImpl.this.getCurrentUserId()) {
                return;
            }
            ControlsControllerImpl.this.resetFavorites();
        }
    };

    @VisibleForTesting
    public static /* synthetic */ void getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getRestoreFinishedReceiver$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getSettingObserver$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v8, types: [com.android.systemui.controls.controller.ControlsControllerImpl$userSwitchReceiver$1, android.content.BroadcastReceiver] */
    /* JADX WARN: Type inference failed for: r8v3, types: [java.lang.Object, com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1] */
    public ControlsControllerImpl(@NotNull Context context, @NotNull DelayableExecutor executor, @NotNull ControlsUiController uiController, @NotNull ControlsBindingController bindingController, @NotNull ControlsListingController listingController, @NotNull BroadcastDispatcher broadcastDispatcher, @NotNull Optional<ControlsFavoritePersistenceWrapper> optionalWrapper, @NotNull DumpManager dumpManager, @NotNull UserTracker userTracker) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(uiController, "uiController");
        Intrinsics.checkNotNullParameter(bindingController, "bindingController");
        Intrinsics.checkNotNullParameter(listingController, "listingController");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(optionalWrapper, "optionalWrapper");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        this.context = context;
        this.executor = executor;
        this.uiController = uiController;
        this.bindingController = bindingController;
        this.listingController = listingController;
        this.broadcastDispatcher = broadcastDispatcher;
        this.userChanging = true;
        UserHandle userHandle = userTracker.getUserHandle();
        this.currentUser = userHandle;
        this.userStructure = new UserStructure(context, userHandle);
        ControlsFavoritePersistenceWrapper orElseGet = optionalWrapper.orElseGet(new Supplier<ControlsFavoritePersistenceWrapper>() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.function.Supplier
            /* renamed from: get */
            public final ControlsFavoritePersistenceWrapper mo403get() {
                File file = ControlsControllerImpl.this.userStructure.getFile();
                Intrinsics.checkNotNullExpressionValue(file, "userStructure.file");
                return new ControlsFavoritePersistenceWrapper(file, ControlsControllerImpl.this.executor, new BackupManager(ControlsControllerImpl.this.userStructure.getUserContext()));
            }
        });
        Intrinsics.checkNotNullExpressionValue(orElseGet, "optionalWrapper.orElseGet {\n            ControlsFavoritePersistenceWrapper(\n                    userStructure.file,\n                    executor,\n                    BackupManager(userStructure.userContext)\n            )\n        }");
        this.persistenceWrapper = orElseGet;
        File auxiliaryFile = this.userStructure.getAuxiliaryFile();
        Intrinsics.checkNotNullExpressionValue(auxiliaryFile, "userStructure.auxiliaryFile");
        this.auxiliaryPersistenceWrapper = new AuxiliaryPersistenceWrapper(auxiliaryFile, executor);
        ?? r4 = new BroadcastReceiver() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$userSwitchReceiver$1
            @Override // android.content.BroadcastReceiver
            public void onReceive(@NotNull Context context2, @NotNull Intent intent) {
                UserHandle userHandle2;
                Intrinsics.checkNotNullParameter(context2, "context");
                Intrinsics.checkNotNullParameter(intent, "intent");
                if (Intrinsics.areEqual(intent.getAction(), "android.intent.action.USER_SWITCHED")) {
                    ControlsControllerImpl.this.userChanging = true;
                    UserHandle newUser = UserHandle.of(intent.getIntExtra("android.intent.extra.user_handle", getSendingUserId()));
                    userHandle2 = ControlsControllerImpl.this.currentUser;
                    if (Intrinsics.areEqual(userHandle2, newUser)) {
                        ControlsControllerImpl.this.userChanging = false;
                        return;
                    }
                    ControlsControllerImpl controlsControllerImpl = ControlsControllerImpl.this;
                    Intrinsics.checkNotNullExpressionValue(newUser, "newUser");
                    controlsControllerImpl.setValuesForUser(newUser);
                }
            }
        };
        this.userSwitchReceiver = r4;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1
            @Override // android.content.BroadcastReceiver
            public void onReceive(@NotNull Context context2, @NotNull Intent intent) {
                Intrinsics.checkNotNullParameter(context2, "context");
                Intrinsics.checkNotNullParameter(intent, "intent");
                if (intent.getIntExtra("android.intent.extra.USER_ID", -10000) == ControlsControllerImpl.this.getCurrentUserId()) {
                    DelayableExecutor delayableExecutor = ControlsControllerImpl.this.executor;
                    final ControlsControllerImpl controlsControllerImpl = ControlsControllerImpl.this;
                    delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1$onReceive$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper;
                            Log.d("ControlsControllerImpl", "Restore finished, storing auxiliary favorites");
                            ControlsControllerImpl.this.getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core().initialize();
                            controlsFavoritePersistenceWrapper = ControlsControllerImpl.this.persistenceWrapper;
                            controlsFavoritePersistenceWrapper.storeFavorites(ControlsControllerImpl.this.getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core().getFavorites());
                            ControlsControllerImpl.this.resetFavorites();
                        }
                    });
                }
            }
        };
        this.restoreFinishedReceiver = broadcastReceiver;
        ?? r8 = new ControlsListingController.ControlsListingCallback() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1
            @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
            public void onServicesUpdated(@NotNull final List<ControlsServiceInfo> serviceInfos) {
                Intrinsics.checkNotNullParameter(serviceInfos, "serviceInfos");
                DelayableExecutor delayableExecutor = ControlsControllerImpl.this.executor;
                final ControlsControllerImpl controlsControllerImpl = ControlsControllerImpl.this;
                delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1$onServicesUpdated$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        int collectionSizeOrDefault;
                        Set<ComponentName> set;
                        int collectionSizeOrDefault2;
                        Set set2;
                        int collectionSizeOrDefault3;
                        Set<String> intersect;
                        Set<ComponentName> subtract;
                        ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper;
                        Set<ComponentName> subtract2;
                        Set<ComponentName> intersect2;
                        ControlsBindingController controlsBindingController;
                        List<ControlsServiceInfo> list = serviceInfos;
                        collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10);
                        ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
                        for (ControlsServiceInfo controlsServiceInfo : list) {
                            arrayList.add(controlsServiceInfo.componentName);
                        }
                        set = CollectionsKt___CollectionsKt.toSet(arrayList);
                        List<StructureInfo> allStructures = Favorites.INSTANCE.getAllStructures();
                        collectionSizeOrDefault2 = CollectionsKt__IterablesKt.collectionSizeOrDefault(allStructures, 10);
                        ArrayList arrayList2 = new ArrayList(collectionSizeOrDefault2);
                        for (StructureInfo structureInfo : allStructures) {
                            arrayList2.add(structureInfo.getComponentName());
                        }
                        set2 = CollectionsKt___CollectionsKt.toSet(arrayList2);
                        boolean z = false;
                        SharedPreferences sharedPreferences = controlsControllerImpl.userStructure.getUserContext().getSharedPreferences("controls_prefs", 0);
                        Set<String> completedSeedingPackageSet = sharedPreferences.getStringSet("SeedingCompleted", new LinkedHashSet());
                        collectionSizeOrDefault3 = CollectionsKt__IterablesKt.collectionSizeOrDefault(set, 10);
                        ArrayList arrayList3 = new ArrayList(collectionSizeOrDefault3);
                        for (ComponentName componentName : set) {
                            arrayList3.add(componentName.getPackageName());
                        }
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        Intrinsics.checkNotNullExpressionValue(completedSeedingPackageSet, "completedSeedingPackageSet");
                        intersect = CollectionsKt___CollectionsKt.intersect(completedSeedingPackageSet, arrayList3);
                        edit.putStringSet("SeedingCompleted", intersect).apply();
                        subtract = CollectionsKt___CollectionsKt.subtract(set2, set);
                        ControlsControllerImpl controlsControllerImpl2 = controlsControllerImpl;
                        for (ComponentName it : subtract) {
                            Favorites favorites = Favorites.INSTANCE;
                            Intrinsics.checkNotNullExpressionValue(it, "it");
                            favorites.removeStructures(it);
                            controlsBindingController = controlsControllerImpl2.bindingController;
                            controlsBindingController.onComponentRemoved(it);
                            z = true;
                        }
                        if (!controlsControllerImpl.getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core().getFavorites().isEmpty()) {
                            subtract2 = CollectionsKt___CollectionsKt.subtract(set, set2);
                            ControlsControllerImpl controlsControllerImpl3 = controlsControllerImpl;
                            for (ComponentName it2 : subtract2) {
                                AuxiliaryPersistenceWrapper auxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core = controlsControllerImpl3.getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
                                Intrinsics.checkNotNullExpressionValue(it2, "it");
                                List<StructureInfo> cachedFavoritesAndRemoveFor = auxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core.getCachedFavoritesAndRemoveFor(it2);
                                if (!cachedFavoritesAndRemoveFor.isEmpty()) {
                                    for (StructureInfo structureInfo2 : cachedFavoritesAndRemoveFor) {
                                        Favorites.INSTANCE.replaceControls(structureInfo2);
                                    }
                                    z = true;
                                }
                            }
                            intersect2 = CollectionsKt___CollectionsKt.intersect(set, set2);
                            ControlsControllerImpl controlsControllerImpl4 = controlsControllerImpl;
                            for (ComponentName it3 : intersect2) {
                                AuxiliaryPersistenceWrapper auxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core2 = controlsControllerImpl4.getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
                                Intrinsics.checkNotNullExpressionValue(it3, "it");
                                auxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core2.getCachedFavoritesAndRemoveFor(it3);
                            }
                        }
                        if (z) {
                            Log.d("ControlsControllerImpl", "Detected change in available services, storing updated favorites");
                            controlsFavoritePersistenceWrapper = controlsControllerImpl.persistenceWrapper;
                            controlsFavoritePersistenceWrapper.storeFavorites(Favorites.INSTANCE.getAllStructures());
                        }
                    }
                });
            }
        };
        this.listingCallback = r8;
        String name = ControlsControllerImpl.class.getName();
        Intrinsics.checkNotNullExpressionValue(name, "javaClass.name");
        dumpManager.registerDumpable(name, this);
        resetFavorites();
        this.userChanging = false;
        broadcastDispatcher.registerReceiver(r4, new IntentFilter("android.intent.action.USER_SWITCHED"), executor, UserHandle.ALL);
        context.registerReceiver(broadcastReceiver, new IntentFilter("com.android.systemui.backup.RESTORE_FINISHED"), "com.android.systemui.permission.SELF", null);
        listingController.addCallback(r8);
    }

    /* compiled from: ControlsControllerImpl.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // com.android.systemui.util.UserAwareController
    public int getCurrentUserId() {
        return this.currentUser.getIdentifier();
    }

    @NotNull
    public final AuxiliaryPersistenceWrapper getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.auxiliaryPersistenceWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setValuesForUser(UserHandle userHandle) {
        Log.d("ControlsControllerImpl", Intrinsics.stringPlus("Changing to user: ", userHandle));
        this.currentUser = userHandle;
        UserStructure userStructure = new UserStructure(this.context, userHandle);
        this.userStructure = userStructure;
        ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper = this.persistenceWrapper;
        File file = userStructure.getFile();
        Intrinsics.checkNotNullExpressionValue(file, "userStructure.file");
        controlsFavoritePersistenceWrapper.changeFileAndBackupManager(file, new BackupManager(this.userStructure.getUserContext()));
        AuxiliaryPersistenceWrapper auxiliaryPersistenceWrapper = this.auxiliaryPersistenceWrapper;
        File auxiliaryFile = this.userStructure.getAuxiliaryFile();
        Intrinsics.checkNotNullExpressionValue(auxiliaryFile, "userStructure.auxiliaryFile");
        auxiliaryPersistenceWrapper.changeFile(auxiliaryFile);
        resetFavorites();
        this.bindingController.changeUser(userHandle);
        this.listingController.changeUser(userHandle);
        this.userChanging = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void resetFavorites() {
        Favorites favorites = Favorites.INSTANCE;
        favorites.clear();
        favorites.load(this.persistenceWrapper.readFavorites());
    }

    private final boolean confirmAvailability() {
        if (this.userChanging) {
            Log.w("ControlsControllerImpl", "Controls not available while user is changing");
            return false;
        }
        return true;
    }

    public void loadForComponent(@NotNull final ComponentName componentName, @NotNull final Consumer<ControlsController.LoadData> dataCallback, @NotNull final Consumer<Runnable> cancelWrapper) {
        List emptyList;
        List emptyList2;
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(dataCallback, "dataCallback");
        Intrinsics.checkNotNullParameter(cancelWrapper, "cancelWrapper");
        if (!confirmAvailability()) {
            if (this.userChanging) {
                this.executor.executeDelayed(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ControlsControllerImpl.this.loadForComponent(componentName, dataCallback, cancelWrapper);
                    }
                }, 500L, TimeUnit.MILLISECONDS);
            }
            emptyList = CollectionsKt__CollectionsKt.emptyList();
            emptyList2 = CollectionsKt__CollectionsKt.emptyList();
            dataCallback.accept(ControlsControllerKt.createLoadDataObject(emptyList, emptyList2, true));
        }
        cancelWrapper.accept(this.bindingController.bindAndLoad(componentName, new ControlsBindingController.LoadCallback() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2
            @Override // java.util.function.Consumer
            public /* bridge */ /* synthetic */ void accept(List<? extends Control> list) {
                accept2((List<Control>) list);
            }

            /* renamed from: accept  reason: avoid collision after fix types in other method */
            public void accept2(@NotNull final List<Control> controls) {
                Intrinsics.checkNotNullParameter(controls, "controls");
                DelayableExecutor delayableExecutor = ControlsControllerImpl.this.executor;
                final ComponentName componentName2 = componentName;
                final ControlsControllerImpl controlsControllerImpl = ControlsControllerImpl.this;
                final Consumer<ControlsController.LoadData> consumer = dataCallback;
                delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$accept$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        int collectionSizeOrDefault;
                        Set set;
                        Set findRemoved;
                        int collectionSizeOrDefault2;
                        List plus;
                        ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper;
                        List<ControlInfo> controlsForComponent = Favorites.INSTANCE.getControlsForComponent(componentName2);
                        collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(controlsForComponent, 10);
                        ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
                        for (ControlInfo controlInfo : controlsForComponent) {
                            arrayList.add(controlInfo.getControlId());
                        }
                        Favorites favorites = Favorites.INSTANCE;
                        if (favorites.updateControls(componentName2, controls)) {
                            controlsFavoritePersistenceWrapper = controlsControllerImpl.persistenceWrapper;
                            controlsFavoritePersistenceWrapper.storeFavorites(favorites.getAllStructures());
                        }
                        ControlsControllerImpl controlsControllerImpl2 = controlsControllerImpl;
                        set = CollectionsKt___CollectionsKt.toSet(arrayList);
                        findRemoved = controlsControllerImpl2.findRemoved(set, controls);
                        List<Control> list = controls;
                        ComponentName componentName3 = componentName2;
                        collectionSizeOrDefault2 = CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10);
                        ArrayList arrayList2 = new ArrayList(collectionSizeOrDefault2);
                        for (Control control : list) {
                            arrayList2.add(new ControlStatus(control, componentName3, arrayList.contains(control.getControlId()), false, 8, null));
                        }
                        ArrayList arrayList3 = new ArrayList();
                        List<StructureInfo> structuresForComponent = Favorites.INSTANCE.getStructuresForComponent(componentName2);
                        ControlsControllerImpl controlsControllerImpl3 = controlsControllerImpl;
                        ComponentName componentName4 = componentName2;
                        for (StructureInfo structureInfo : structuresForComponent) {
                            for (ControlInfo controlInfo2 : structureInfo.getControls()) {
                                if (findRemoved.contains(controlInfo2.getControlId())) {
                                    arrayList3.add(ControlsControllerImpl.createRemovedStatus$default(controlsControllerImpl3, componentName4, controlInfo2, structureInfo.getStructure(), false, 8, null));
                                }
                            }
                        }
                        plus = CollectionsKt___CollectionsKt.plus((Collection) arrayList3, (Iterable) arrayList2);
                        consumer.accept(ControlsControllerKt.createLoadDataObject$default(plus, arrayList, false, 4, null));
                    }
                });
            }

            @Override // com.android.systemui.controls.controller.ControlsBindingController.LoadCallback
            public void error(@NotNull String message) {
                Intrinsics.checkNotNullParameter(message, "message");
                DelayableExecutor delayableExecutor = ControlsControllerImpl.this.executor;
                final ComponentName componentName2 = componentName;
                final Consumer<ControlsController.LoadData> consumer = dataCallback;
                final ControlsControllerImpl controlsControllerImpl = ControlsControllerImpl.this;
                delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$error$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        int collectionSizeOrDefault;
                        int collectionSizeOrDefault2;
                        ControlStatus createRemovedStatus;
                        List<StructureInfo> structuresForComponent = Favorites.INSTANCE.getStructuresForComponent(componentName2);
                        ControlsControllerImpl controlsControllerImpl2 = controlsControllerImpl;
                        ComponentName componentName3 = componentName2;
                        ArrayList<ControlStatus> arrayList = new ArrayList();
                        for (StructureInfo structureInfo : structuresForComponent) {
                            List<ControlInfo> controls = structureInfo.getControls();
                            collectionSizeOrDefault2 = CollectionsKt__IterablesKt.collectionSizeOrDefault(controls, 10);
                            ArrayList arrayList2 = new ArrayList(collectionSizeOrDefault2);
                            for (ControlInfo controlInfo : controls) {
                                createRemovedStatus = controlsControllerImpl2.createRemovedStatus(componentName3, controlInfo, structureInfo.getStructure(), false);
                                arrayList2.add(createRemovedStatus);
                            }
                            CollectionsKt__MutableCollectionsKt.addAll(arrayList, arrayList2);
                        }
                        collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(arrayList, 10);
                        ArrayList arrayList3 = new ArrayList(collectionSizeOrDefault);
                        for (ControlStatus controlStatus : arrayList) {
                            arrayList3.add(controlStatus.getControl().getControlId());
                        }
                        consumer.accept(ControlsControllerKt.createLoadDataObject(arrayList, arrayList3, true));
                    }
                });
            }
        }));
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public boolean addSeedingFavoritesCallback(@NotNull final Consumer<Boolean> callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        if (!this.seedingInProgress) {
            return false;
        }
        this.executor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$addSeedingFavoritesCallback$1
            @Override // java.lang.Runnable
            public final void run() {
                boolean z;
                List list;
                z = ControlsControllerImpl.this.seedingInProgress;
                if (z) {
                    list = ControlsControllerImpl.this.seedingCallbacks;
                    list.add(callback);
                    return;
                }
                callback.accept(Boolean.FALSE);
            }
        });
        return true;
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public void seedFavoritesForComponents(@NotNull final List<ComponentName> componentNames, @NotNull final Consumer<SeedResponse> callback) {
        Intrinsics.checkNotNullParameter(componentNames, "componentNames");
        Intrinsics.checkNotNullParameter(callback, "callback");
        if (this.seedingInProgress || componentNames.isEmpty()) {
            return;
        }
        if (!confirmAvailability()) {
            if (this.userChanging) {
                this.executor.executeDelayed(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$seedFavoritesForComponents$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ControlsControllerImpl.this.seedFavoritesForComponents(componentNames, callback);
                    }
                }, 500L, TimeUnit.MILLISECONDS);
                return;
            }
            for (ComponentName componentName : componentNames) {
                String packageName = componentName.getPackageName();
                Intrinsics.checkNotNullExpressionValue(packageName, "it.packageName");
                callback.accept(new SeedResponse(packageName, false));
            }
            return;
        }
        this.seedingInProgress = true;
        startSeeding(componentNames, callback, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void startSeeding(List<ComponentName> list, final Consumer<SeedResponse> consumer, final boolean z) {
        final List drop;
        if (list.isEmpty()) {
            endSeedingCall(!z);
            return;
        }
        final ComponentName componentName = list.get(0);
        Log.d("ControlsControllerImpl", Intrinsics.stringPlus("Beginning request to seed favorites for: ", componentName));
        drop = CollectionsKt___CollectionsKt.drop(list, 1);
        this.bindingController.bindAndLoadSuggested(componentName, new ControlsBindingController.LoadCallback() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1
            @Override // java.util.function.Consumer
            public /* bridge */ /* synthetic */ void accept(List<? extends Control> list2) {
                accept2((List<Control>) list2);
            }

            /* renamed from: accept  reason: avoid collision after fix types in other method */
            public void accept2(@NotNull final List<Control> controls) {
                Intrinsics.checkNotNullParameter(controls, "controls");
                DelayableExecutor delayableExecutor = ControlsControllerImpl.this.executor;
                final ControlsControllerImpl controlsControllerImpl = ControlsControllerImpl.this;
                final Consumer<SeedResponse> consumer2 = consumer;
                final ComponentName componentName2 = componentName;
                final List<ComponentName> list2 = drop;
                final boolean z2 = z;
                delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1$accept$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper;
                        ArrayMap arrayMap = new ArrayMap();
                        for (Control control : controls) {
                            CharSequence structure = control.getStructure();
                            if (structure == null) {
                                structure = "";
                            }
                            List list3 = (List) arrayMap.get(structure);
                            if (list3 == null) {
                                list3 = new ArrayList();
                            }
                            if (list3.size() < 6) {
                                String controlId = control.getControlId();
                                Intrinsics.checkNotNullExpressionValue(controlId, "it.controlId");
                                CharSequence title = control.getTitle();
                                Intrinsics.checkNotNullExpressionValue(title, "it.title");
                                CharSequence subtitle = control.getSubtitle();
                                Intrinsics.checkNotNullExpressionValue(subtitle, "it.subtitle");
                                list3.add(new ControlInfo(controlId, title, subtitle, control.getDeviceType()));
                                arrayMap.put(structure, list3);
                            }
                        }
                        ComponentName componentName3 = componentName2;
                        for (Map.Entry entry : arrayMap.entrySet()) {
                            CharSequence s = (CharSequence) entry.getKey();
                            List cs = (List) entry.getValue();
                            Favorites favorites = Favorites.INSTANCE;
                            Intrinsics.checkNotNullExpressionValue(s, "s");
                            Intrinsics.checkNotNullExpressionValue(cs, "cs");
                            favorites.replaceControls(new StructureInfo(componentName3, s, cs));
                        }
                        controlsFavoritePersistenceWrapper = controlsControllerImpl.persistenceWrapper;
                        controlsFavoritePersistenceWrapper.storeFavorites(Favorites.INSTANCE.getAllStructures());
                        Consumer<SeedResponse> consumer3 = consumer2;
                        String packageName = componentName2.getPackageName();
                        Intrinsics.checkNotNullExpressionValue(packageName, "componentName.packageName");
                        consumer3.accept(new SeedResponse(packageName, true));
                        controlsControllerImpl.startSeeding(list2, consumer2, z2);
                    }
                });
            }

            @Override // com.android.systemui.controls.controller.ControlsBindingController.LoadCallback
            public void error(@NotNull String message) {
                Intrinsics.checkNotNullParameter(message, "message");
                Log.e("ControlsControllerImpl", Intrinsics.stringPlus("Unable to seed favorites: ", message));
                DelayableExecutor delayableExecutor = ControlsControllerImpl.this.executor;
                final Consumer<SeedResponse> consumer2 = consumer;
                final ComponentName componentName2 = componentName;
                final ControlsControllerImpl controlsControllerImpl = ControlsControllerImpl.this;
                final List<ComponentName> list2 = drop;
                delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1$error$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Consumer<SeedResponse> consumer3 = consumer2;
                        String packageName = componentName2.getPackageName();
                        Intrinsics.checkNotNullExpressionValue(packageName, "componentName.packageName");
                        consumer3.accept(new SeedResponse(packageName, false));
                        controlsControllerImpl.startSeeding(list2, consumer2, true);
                    }
                });
            }
        });
    }

    private final void endSeedingCall(boolean z) {
        this.seedingInProgress = false;
        Iterator<T> it = this.seedingCallbacks.iterator();
        while (it.hasNext()) {
            ((Consumer) it.next()).accept(Boolean.valueOf(z));
        }
        this.seedingCallbacks.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ ControlStatus createRemovedStatus$default(ControlsControllerImpl controlsControllerImpl, ComponentName componentName, ControlInfo controlInfo, CharSequence charSequence, boolean z, int i, Object obj) {
        if ((i & 8) != 0) {
            z = true;
        }
        return controlsControllerImpl.createRemovedStatus(componentName, controlInfo, charSequence, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ControlStatus createRemovedStatus(ComponentName componentName, ControlInfo controlInfo, CharSequence charSequence, boolean z) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setPackage(componentName.getPackageName());
        Control control = new Control.StatelessBuilder(controlInfo.getControlId(), PendingIntent.getActivity(this.context, componentName.hashCode(), intent, 67108864)).setTitle(controlInfo.getControlTitle()).setSubtitle(controlInfo.getControlSubtitle()).setStructure(charSequence).setDeviceType(controlInfo.getDeviceType()).build();
        Intrinsics.checkNotNullExpressionValue(control, "control");
        return new ControlStatus(control, componentName, true, z);
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public void subscribeToFavorites(@NotNull StructureInfo structureInfo) {
        Intrinsics.checkNotNullParameter(structureInfo, "structureInfo");
        if (!confirmAvailability()) {
            return;
        }
        this.bindingController.subscribe(structureInfo);
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public void unsubscribe() {
        if (!confirmAvailability()) {
            return;
        }
        this.bindingController.unsubscribe();
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public void addFavorite(@NotNull final ComponentName componentName, @NotNull final CharSequence structureName, @NotNull final ControlInfo controlInfo) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(structureName, "structureName");
        Intrinsics.checkNotNullParameter(controlInfo, "controlInfo");
        if (!confirmAvailability()) {
            return;
        }
        this.executor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$addFavorite$1
            @Override // java.lang.Runnable
            public final void run() {
                ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper;
                Favorites favorites = Favorites.INSTANCE;
                if (favorites.addFavorite(componentName, structureName, controlInfo)) {
                    controlsFavoritePersistenceWrapper = this.persistenceWrapper;
                    controlsFavoritePersistenceWrapper.storeFavorites(favorites.getAllStructures());
                }
            }
        });
    }

    public void replaceFavoritesForStructure(@NotNull final StructureInfo structureInfo) {
        Intrinsics.checkNotNullParameter(structureInfo, "structureInfo");
        if (!confirmAvailability()) {
            return;
        }
        this.executor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$replaceFavoritesForStructure$1
            @Override // java.lang.Runnable
            public final void run() {
                ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper;
                Favorites favorites = Favorites.INSTANCE;
                favorites.replaceControls(StructureInfo.this);
                controlsFavoritePersistenceWrapper = this.persistenceWrapper;
                controlsFavoritePersistenceWrapper.storeFavorites(favorites.getAllStructures());
            }
        });
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public void refreshStatus(@NotNull final ComponentName componentName, @NotNull final Control control) {
        List<Control> listOf;
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(control, "control");
        if (!confirmAvailability()) {
            Log.d("ControlsControllerImpl", "Controls not available");
            return;
        }
        if (control.getStatus() == 1) {
            this.executor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$refreshStatus$1
                @Override // java.lang.Runnable
                public final void run() {
                    List<Control> listOf2;
                    ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper;
                    Favorites favorites = Favorites.INSTANCE;
                    ComponentName componentName2 = componentName;
                    listOf2 = CollectionsKt__CollectionsJVMKt.listOf(control);
                    if (favorites.updateControls(componentName2, listOf2)) {
                        controlsFavoritePersistenceWrapper = this.persistenceWrapper;
                        controlsFavoritePersistenceWrapper.storeFavorites(favorites.getAllStructures());
                    }
                }
            });
        }
        ControlsUiController controlsUiController = this.uiController;
        listOf = CollectionsKt__CollectionsJVMKt.listOf(control);
        controlsUiController.onRefreshState(componentName, listOf);
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public void onActionResponse(@NotNull ComponentName componentName, @NotNull String controlId, int i) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(controlId, "controlId");
        if (!confirmAvailability()) {
            return;
        }
        this.uiController.onActionResponse(componentName, controlId, i);
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public void action(@NotNull ComponentName componentName, @NotNull ControlInfo controlInfo, @NotNull ControlAction action) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(controlInfo, "controlInfo");
        Intrinsics.checkNotNullParameter(action, "action");
        if (!confirmAvailability()) {
            return;
        }
        this.bindingController.action(componentName, controlInfo, action);
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    @NotNull
    public List<StructureInfo> getFavorites() {
        return Favorites.INSTANCE.getAllStructures();
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    public int countFavoritesForComponent(@NotNull ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        return Favorites.INSTANCE.getControlsForComponent(componentName).size();
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    @NotNull
    public List<StructureInfo> getFavoritesForComponent(@NotNull ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        return Favorites.INSTANCE.getStructuresForComponent(componentName);
    }

    @NotNull
    public List<ControlInfo> getFavoritesForStructure(@NotNull ComponentName componentName, @NotNull CharSequence structureName) {
        List emptyList;
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(structureName, "structureName");
        Favorites favorites = Favorites.INSTANCE;
        emptyList = CollectionsKt__CollectionsKt.emptyList();
        return favorites.getControlsForStructure(new StructureInfo(componentName, structureName, emptyList));
    }

    @Override // com.android.systemui.controls.controller.ControlsController
    @NotNull
    public StructureInfo getPreferredStructure() {
        return this.uiController.getPreferredStructure(getFavorites());
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        pw.println("ControlsController state:");
        pw.println(Intrinsics.stringPlus("  Changing users: ", Boolean.valueOf(this.userChanging)));
        pw.println(Intrinsics.stringPlus("  Current user: ", Integer.valueOf(this.currentUser.getIdentifier())));
        pw.println("  Favorites:");
        for (StructureInfo structureInfo : Favorites.INSTANCE.getAllStructures()) {
            pw.println(Intrinsics.stringPlus("    ", structureInfo));
            for (ControlInfo controlInfo : structureInfo.getControls()) {
                pw.println(Intrinsics.stringPlus("      ", controlInfo));
            }
        }
        pw.println(this.bindingController.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Set<String> findRemoved(Set<String> set, List<Control> list) {
        int collectionSizeOrDefault;
        Set<String> minus;
        collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10);
        ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
        for (Control control : list) {
            arrayList.add(control.getControlId());
        }
        minus = SetsKt___SetsKt.minus(set, arrayList);
        return minus;
    }
}

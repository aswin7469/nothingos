package com.android.systemui.controls.controller;

import android.app.PendingIntent;
import android.app.backup.BackupManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.UserHandle;
import android.service.controls.Control;
import android.service.controls.actions.ControlAction;
import android.util.Log;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.Dumpable;
import com.android.systemui.backup.BackupHelper;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.p010ui.ControlsUiController;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.p026io.File;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\t*\u0002*@\b\u0007\u0018\u0000 \u00012\u00020\u00012\u00020\u0002:\u0002\u0001BW\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0001\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0015¢\u0006\u0002\u0010\u0016J \u0010B\u001a\u00020C2\u0006\u0010D\u001a\u00020E2\u0006\u0010F\u001a\u00020G2\u0006\u0010B\u001a\u00020HH\u0016J \u0010I\u001a\u00020C2\u0006\u0010D\u001a\u00020E2\u0006\u0010J\u001a\u00020K2\u0006\u0010F\u001a\u00020GH\u0016J\u0016\u0010L\u001a\u0002052\f\u0010M\u001a\b\u0012\u0004\u0012\u00020504H\u0016J\b\u0010N\u001a\u000205H\u0002J\u0010\u0010O\u001a\u00020&2\u0006\u0010D\u001a\u00020EH\u0016J*\u0010P\u001a\u00020Q2\u0006\u0010D\u001a\u00020E2\u0006\u0010F\u001a\u00020G2\u0006\u0010R\u001a\u00020K2\b\b\u0002\u0010S\u001a\u000205H\u0002J\u0006\u0010T\u001a\u00020CJ%\u0010U\u001a\u00020C2\u0006\u0010V\u001a\u00020W2\u000e\u0010X\u001a\n\u0012\u0006\b\u0001\u0012\u00020Z0YH\u0016¢\u0006\u0002\u0010[J\u0010\u0010\\\u001a\u00020C2\u0006\u0010]\u001a\u000205H\u0002J*\u0010^\u001a\b\u0012\u0004\u0012\u00020Z0_2\f\u0010`\u001a\b\u0012\u0004\u0012\u00020Z0_2\f\u0010a\u001a\b\u0012\u0004\u0012\u00020c0bH\u0002J\u000e\u0010d\u001a\b\u0012\u0004\u0012\u00020e0bH\u0016J\u0016\u0010f\u001a\b\u0012\u0004\u0012\u00020e0b2\u0006\u0010D\u001a\u00020EH\u0016J\u001e\u0010g\u001a\b\u0012\u0004\u0012\u00020G0b2\u0006\u0010D\u001a\u00020E2\u0006\u0010J\u001a\u00020KH\u0016J\b\u0010h\u001a\u00020eH\u0016J,\u0010i\u001a\u00020C2\u0006\u0010D\u001a\u00020E2\f\u0010j\u001a\b\u0012\u0004\u0012\u00020k042\f\u0010l\u001a\b\u0012\u0004\u0012\u00020m04H\u0016J \u0010n\u001a\u00020C2\u0006\u0010D\u001a\u00020E2\u0006\u0010o\u001a\u00020Z2\u0006\u0010p\u001a\u00020&H\u0016J\u0018\u0010q\u001a\u00020C2\u0006\u0010D\u001a\u00020E2\u0006\u0010r\u001a\u00020cH\u0016J\u0010\u0010s\u001a\u00020C2\u0006\u0010t\u001a\u00020eH\u0016J\b\u0010u\u001a\u00020CH\u0002J$\u0010v\u001a\u00020C2\f\u0010w\u001a\b\u0012\u0004\u0012\u00020E0b2\f\u0010M\u001a\b\u0012\u0004\u0012\u00020x04H\u0016J\u0010\u0010y\u001a\u00020C2\u0006\u0010z\u001a\u00020$H\u0002J,\u0010{\u001a\u00020C2\f\u0010|\u001a\b\u0012\u0004\u0012\u00020E0b2\f\u0010M\u001a\b\u0012\u0004\u0012\u00020x042\u0006\u0010}\u001a\u000205H\u0002J\u0010\u0010~\u001a\u00020C2\u0006\u0010t\u001a\u00020eH\u0016J\b\u0010\u001a\u00020CH\u0016R$\u0010\u0017\u001a\u00020\u00188\u0000@\u0000X\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u0019\u0010\u001a\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u001f\u001a\u00020 8BX\u0004¢\u0006\u0006\u001a\u0004\b!\u0010\"R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010%\u001a\u00020&8VX\u0004¢\u0006\u0006\u001a\u0004\b'\u0010(R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u00020*X\u0004¢\u0006\u0004\n\u0002\u0010+R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010-\u001a\u00020.8\u0000X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b/\u0010\u001a\u001a\u0004\b0\u00101R\u001a\u00102\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002050403X\u0004¢\u0006\u0002\n\u0000R\u000e\u00106\u001a\u000205X\u000e¢\u0006\u0002\n\u0000R\u001c\u00107\u001a\u0002088\u0000X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b9\u0010\u001a\u001a\u0004\b:\u0010;R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u000205X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020>X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010?\u001a\u00020@X\u0004¢\u0006\u0004\n\u0002\u0010A¨\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsControllerImpl;", "Lcom/android/systemui/Dumpable;", "Lcom/android/systemui/controls/controller/ControlsController;", "context", "Landroid/content/Context;", "executor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "uiController", "Lcom/android/systemui/controls/ui/ControlsUiController;", "bindingController", "Lcom/android/systemui/controls/controller/ControlsBindingController;", "listingController", "Lcom/android/systemui/controls/management/ControlsListingController;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "optionalWrapper", "Ljava/util/Optional;", "Lcom/android/systemui/controls/controller/ControlsFavoritePersistenceWrapper;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "userTracker", "Lcom/android/systemui/settings/UserTracker;", "(Landroid/content/Context;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/controls/ui/ControlsUiController;Lcom/android/systemui/controls/controller/ControlsBindingController;Lcom/android/systemui/controls/management/ControlsListingController;Lcom/android/systemui/broadcast/BroadcastDispatcher;Ljava/util/Optional;Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/settings/UserTracker;)V", "auxiliaryPersistenceWrapper", "Lcom/android/systemui/controls/controller/AuxiliaryPersistenceWrapper;", "getAuxiliaryPersistenceWrapper$SystemUI_nothingRelease$annotations", "()V", "getAuxiliaryPersistenceWrapper$SystemUI_nothingRelease", "()Lcom/android/systemui/controls/controller/AuxiliaryPersistenceWrapper;", "setAuxiliaryPersistenceWrapper$SystemUI_nothingRelease", "(Lcom/android/systemui/controls/controller/AuxiliaryPersistenceWrapper;)V", "contentResolver", "Landroid/content/ContentResolver;", "getContentResolver", "()Landroid/content/ContentResolver;", "currentUser", "Landroid/os/UserHandle;", "currentUserId", "", "getCurrentUserId", "()I", "listingCallback", "com/android/systemui/controls/controller/ControlsControllerImpl$listingCallback$1", "Lcom/android/systemui/controls/controller/ControlsControllerImpl$listingCallback$1;", "persistenceWrapper", "restoreFinishedReceiver", "Landroid/content/BroadcastReceiver;", "getRestoreFinishedReceiver$SystemUI_nothingRelease$annotations", "getRestoreFinishedReceiver$SystemUI_nothingRelease", "()Landroid/content/BroadcastReceiver;", "seedingCallbacks", "", "Ljava/util/function/Consumer;", "", "seedingInProgress", "settingObserver", "Landroid/database/ContentObserver;", "getSettingObserver$SystemUI_nothingRelease$annotations", "getSettingObserver$SystemUI_nothingRelease", "()Landroid/database/ContentObserver;", "userChanging", "userStructure", "Lcom/android/systemui/controls/controller/UserStructure;", "userSwitchReceiver", "com/android/systemui/controls/controller/ControlsControllerImpl$userSwitchReceiver$1", "Lcom/android/systemui/controls/controller/ControlsControllerImpl$userSwitchReceiver$1;", "action", "", "componentName", "Landroid/content/ComponentName;", "controlInfo", "Lcom/android/systemui/controls/controller/ControlInfo;", "Landroid/service/controls/actions/ControlAction;", "addFavorite", "structureName", "", "addSeedingFavoritesCallback", "callback", "confirmAvailability", "countFavoritesForComponent", "createRemovedStatus", "Lcom/android/systemui/controls/ControlStatus;", "structure", "setRemoved", "destroy", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "endSeedingCall", "state", "findRemoved", "", "favoriteKeys", "list", "", "Landroid/service/controls/Control;", "getFavorites", "Lcom/android/systemui/controls/controller/StructureInfo;", "getFavoritesForComponent", "getFavoritesForStructure", "getPreferredStructure", "loadForComponent", "dataCallback", "Lcom/android/systemui/controls/controller/ControlsController$LoadData;", "cancelWrapper", "Ljava/lang/Runnable;", "onActionResponse", "controlId", "response", "refreshStatus", "control", "replaceFavoritesForStructure", "structureInfo", "resetFavorites", "seedFavoritesForComponents", "componentNames", "Lcom/android/systemui/controls/controller/SeedResponse;", "setValuesForUser", "newUser", "startSeeding", "remainingComponentNames", "didAnyFail", "subscribeToFavorites", "unsubscribe", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl implements Dumpable, ControlsController {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final int DEFAULT_ENABLED = 1;
    private static final String PERMISSION_SELF = "com.android.systemui.permission.SELF";
    public static final int SUGGESTED_CONTROLS_PER_STRUCTURE = 6;
    private static final String TAG = "ControlsControllerImpl";
    private static final long USER_CHANGE_RETRY_DELAY = 500;
    private AuxiliaryPersistenceWrapper auxiliaryPersistenceWrapper;
    /* access modifiers changed from: private */
    public final ControlsBindingController bindingController;
    private final BroadcastDispatcher broadcastDispatcher;
    private final Context context;
    /* access modifiers changed from: private */
    public UserHandle currentUser;
    /* access modifiers changed from: private */
    public final DelayableExecutor executor;
    private final ControlsControllerImpl$listingCallback$1 listingCallback;
    private final ControlsListingController listingController;
    /* access modifiers changed from: private */
    public final ControlsFavoritePersistenceWrapper persistenceWrapper;
    private final BroadcastReceiver restoreFinishedReceiver;
    private final List<Consumer<Boolean>> seedingCallbacks = new ArrayList();
    private boolean seedingInProgress;
    private final ContentObserver settingObserver;
    private final ControlsUiController uiController;
    /* access modifiers changed from: private */
    public boolean userChanging = true;
    /* access modifiers changed from: private */
    public UserStructure userStructure;
    private final ControlsControllerImpl$userSwitchReceiver$1 userSwitchReceiver;

    /* renamed from: getAuxiliaryPersistenceWrapper$SystemUI_nothingRelease$annotations */
    public static /* synthetic */ void m377x9fff8416() {
    }

    public static /* synthetic */ void getRestoreFinishedReceiver$SystemUI_nothingRelease$annotations() {
    }

    public static /* synthetic */ void getSettingObserver$SystemUI_nothingRelease$annotations() {
    }

    @Inject
    public ControlsControllerImpl(Context context2, @Background DelayableExecutor delayableExecutor, ControlsUiController controlsUiController, ControlsBindingController controlsBindingController, ControlsListingController controlsListingController, BroadcastDispatcher broadcastDispatcher2, Optional<ControlsFavoritePersistenceWrapper> optional, DumpManager dumpManager, UserTracker userTracker) {
        Context context3 = context2;
        DelayableExecutor delayableExecutor2 = delayableExecutor;
        ControlsUiController controlsUiController2 = controlsUiController;
        ControlsBindingController controlsBindingController2 = controlsBindingController;
        ControlsListingController controlsListingController2 = controlsListingController;
        BroadcastDispatcher broadcastDispatcher3 = broadcastDispatcher2;
        Optional<ControlsFavoritePersistenceWrapper> optional2 = optional;
        DumpManager dumpManager2 = dumpManager;
        Intrinsics.checkNotNullParameter(context3, "context");
        Intrinsics.checkNotNullParameter(delayableExecutor2, "executor");
        Intrinsics.checkNotNullParameter(controlsUiController2, "uiController");
        Intrinsics.checkNotNullParameter(controlsBindingController2, "bindingController");
        Intrinsics.checkNotNullParameter(controlsListingController2, "listingController");
        Intrinsics.checkNotNullParameter(broadcastDispatcher3, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(optional2, "optionalWrapper");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        this.context = context3;
        this.executor = delayableExecutor2;
        this.uiController = controlsUiController2;
        this.bindingController = controlsBindingController2;
        this.listingController = controlsListingController2;
        this.broadcastDispatcher = broadcastDispatcher3;
        this.currentUser = userTracker.getUserHandle();
        this.userStructure = new UserStructure(context3, this.currentUser);
        ControlsFavoritePersistenceWrapper orElseGet = optional2.orElseGet(new ControlsControllerImpl$$ExternalSyntheticLambda4(this));
        Intrinsics.checkNotNullExpressionValue(orElseGet, "optionalWrapper.orElseGe…)\n            )\n        }");
        this.persistenceWrapper = orElseGet;
        File auxiliaryFile = this.userStructure.getAuxiliaryFile();
        Intrinsics.checkNotNullExpressionValue(auxiliaryFile, "userStructure.auxiliaryFile");
        this.auxiliaryPersistenceWrapper = new AuxiliaryPersistenceWrapper(auxiliaryFile, delayableExecutor2);
        ControlsControllerImpl$userSwitchReceiver$1 controlsControllerImpl$userSwitchReceiver$1 = new ControlsControllerImpl$userSwitchReceiver$1(this);
        this.userSwitchReceiver = controlsControllerImpl$userSwitchReceiver$1;
        BroadcastReceiver controlsControllerImpl$restoreFinishedReceiver$1 = new ControlsControllerImpl$restoreFinishedReceiver$1(this);
        this.restoreFinishedReceiver = controlsControllerImpl$restoreFinishedReceiver$1;
        this.settingObserver = new ControlsControllerImpl$settingObserver$1(this);
        ControlsControllerImpl$listingCallback$1 controlsControllerImpl$listingCallback$1 = new ControlsControllerImpl$listingCallback$1(this);
        this.listingCallback = controlsControllerImpl$listingCallback$1;
        String name = getClass().getName();
        Intrinsics.checkNotNullExpressionValue(name, "javaClass.name");
        dumpManager2.registerDumpable(name, this);
        resetFavorites();
        this.userChanging = false;
        BroadcastDispatcher.registerReceiver$default(broadcastDispatcher2, controlsControllerImpl$userSwitchReceiver$1, new IntentFilter("android.intent.action.USER_SWITCHED"), delayableExecutor2, UserHandle.ALL, 0, (String) null, 48, (Object) null);
        context2.registerReceiver(controlsControllerImpl$restoreFinishedReceiver$1, new IntentFilter(BackupHelper.ACTION_RESTORE_FINISHED), "com.android.systemui.permission.SELF", (Handler) null, 4);
        controlsListingController2.addCallback(controlsControllerImpl$listingCallback$1);
    }

    @Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nXT¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsControllerImpl$Companion;", "", "()V", "DEFAULT_ENABLED", "", "PERMISSION_SELF", "", "SUGGESTED_CONTROLS_PER_STRUCTURE", "TAG", "USER_CHANGE_RETRY_DELAY", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsControllerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public int getCurrentUserId() {
        return this.currentUser.getIdentifier();
    }

    private final ContentResolver getContentResolver() {
        ContentResolver contentResolver = this.context.getContentResolver();
        Intrinsics.checkNotNullExpressionValue(contentResolver, "context.contentResolver");
        return contentResolver;
    }

    public final AuxiliaryPersistenceWrapper getAuxiliaryPersistenceWrapper$SystemUI_nothingRelease() {
        return this.auxiliaryPersistenceWrapper;
    }

    public final void setAuxiliaryPersistenceWrapper$SystemUI_nothingRelease(AuxiliaryPersistenceWrapper auxiliaryPersistenceWrapper2) {
        Intrinsics.checkNotNullParameter(auxiliaryPersistenceWrapper2, "<set-?>");
        this.auxiliaryPersistenceWrapper = auxiliaryPersistenceWrapper2;
    }

    /* access modifiers changed from: private */
    /* renamed from: _init_$lambda-0  reason: not valid java name */
    public static final ControlsFavoritePersistenceWrapper m2611_init_$lambda0(ControlsControllerImpl controlsControllerImpl) {
        Intrinsics.checkNotNullParameter(controlsControllerImpl, "this$0");
        File file = controlsControllerImpl.userStructure.getFile();
        Intrinsics.checkNotNullExpressionValue(file, "userStructure.file");
        return new ControlsFavoritePersistenceWrapper(file, controlsControllerImpl.executor, new BackupManager(controlsControllerImpl.userStructure.getUserContext()));
    }

    /* access modifiers changed from: private */
    public final void setValuesForUser(UserHandle userHandle) {
        Log.d(TAG, "Changing to user: " + userHandle);
        this.currentUser = userHandle;
        UserStructure userStructure2 = new UserStructure(this.context, this.currentUser);
        this.userStructure = userStructure2;
        ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper = this.persistenceWrapper;
        File file = userStructure2.getFile();
        Intrinsics.checkNotNullExpressionValue(file, "userStructure.file");
        controlsFavoritePersistenceWrapper.changeFileAndBackupManager(file, new BackupManager(this.userStructure.getUserContext()));
        AuxiliaryPersistenceWrapper auxiliaryPersistenceWrapper2 = this.auxiliaryPersistenceWrapper;
        File auxiliaryFile = this.userStructure.getAuxiliaryFile();
        Intrinsics.checkNotNullExpressionValue(auxiliaryFile, "userStructure.auxiliaryFile");
        auxiliaryPersistenceWrapper2.changeFile(auxiliaryFile);
        resetFavorites();
        this.bindingController.changeUser(userHandle);
        this.listingController.changeUser(userHandle);
        this.userChanging = false;
    }

    public final BroadcastReceiver getRestoreFinishedReceiver$SystemUI_nothingRelease() {
        return this.restoreFinishedReceiver;
    }

    public final ContentObserver getSettingObserver$SystemUI_nothingRelease() {
        return this.settingObserver;
    }

    public final void destroy() {
        this.broadcastDispatcher.unregisterReceiver(this.userSwitchReceiver);
        this.context.unregisterReceiver(this.restoreFinishedReceiver);
        this.listingController.removeCallback(this.listingCallback);
    }

    /* access modifiers changed from: private */
    public final void resetFavorites() {
        Favorites.INSTANCE.clear();
        Favorites.INSTANCE.load(this.persistenceWrapper.readFavorites());
    }

    private final boolean confirmAvailability() {
        if (!this.userChanging) {
            return true;
        }
        Log.w(TAG, "Controls not available while user is changing");
        return false;
    }

    public void loadForComponent(ComponentName componentName, Consumer<ControlsController.LoadData> consumer, Consumer<Runnable> consumer2) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(consumer, "dataCallback");
        Intrinsics.checkNotNullParameter(consumer2, "cancelWrapper");
        if (!confirmAvailability()) {
            if (this.userChanging) {
                this.executor.executeDelayed(new ControlsControllerImpl$$ExternalSyntheticLambda1(this, componentName, consumer, consumer2), 500, TimeUnit.MILLISECONDS);
            }
            consumer.accept(ControlsControllerKt.createLoadDataObject(CollectionsKt.emptyList(), CollectionsKt.emptyList(), true));
        }
        consumer2.accept(this.bindingController.bindAndLoad(componentName, new ControlsControllerImpl$loadForComponent$2(this, componentName, consumer)));
    }

    /* access modifiers changed from: private */
    /* renamed from: loadForComponent$lambda-1  reason: not valid java name */
    public static final void m2614loadForComponent$lambda1(ControlsControllerImpl controlsControllerImpl, ComponentName componentName, Consumer consumer, Consumer consumer2) {
        Intrinsics.checkNotNullParameter(controlsControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(componentName, "$componentName");
        Intrinsics.checkNotNullParameter(consumer, "$dataCallback");
        Intrinsics.checkNotNullParameter(consumer2, "$cancelWrapper");
        controlsControllerImpl.loadForComponent(componentName, consumer, consumer2);
    }

    public boolean addSeedingFavoritesCallback(Consumer<Boolean> consumer) {
        Intrinsics.checkNotNullParameter(consumer, "callback");
        if (!this.seedingInProgress) {
            return false;
        }
        this.executor.execute(new ControlsControllerImpl$$ExternalSyntheticLambda5(this, consumer));
        return true;
    }

    /* access modifiers changed from: private */
    /* renamed from: addSeedingFavoritesCallback$lambda-2  reason: not valid java name */
    public static final void m2613addSeedingFavoritesCallback$lambda2(ControlsControllerImpl controlsControllerImpl, Consumer consumer) {
        Intrinsics.checkNotNullParameter(controlsControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(consumer, "$callback");
        if (controlsControllerImpl.seedingInProgress) {
            controlsControllerImpl.seedingCallbacks.add(consumer);
        } else {
            consumer.accept(false);
        }
    }

    public void seedFavoritesForComponents(List<ComponentName> list, Consumer<SeedResponse> consumer) {
        Intrinsics.checkNotNullParameter(list, "componentNames");
        Intrinsics.checkNotNullParameter(consumer, "callback");
        if (!this.seedingInProgress && !list.isEmpty()) {
            if (confirmAvailability()) {
                this.seedingInProgress = true;
                startSeeding(list, consumer, false);
            } else if (this.userChanging) {
                this.executor.executeDelayed(new ControlsControllerImpl$$ExternalSyntheticLambda0(this, list, consumer), 500, TimeUnit.MILLISECONDS);
            } else {
                for (ComponentName packageName : list) {
                    String packageName2 = packageName.getPackageName();
                    Intrinsics.checkNotNullExpressionValue(packageName2, "it.packageName");
                    consumer.accept(new SeedResponse(packageName2, false));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: seedFavoritesForComponents$lambda-3  reason: not valid java name */
    public static final void m2617seedFavoritesForComponents$lambda3(ControlsControllerImpl controlsControllerImpl, List list, Consumer consumer) {
        Intrinsics.checkNotNullParameter(controlsControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(list, "$componentNames");
        Intrinsics.checkNotNullParameter(consumer, "$callback");
        controlsControllerImpl.seedFavoritesForComponents(list, consumer);
    }

    /* access modifiers changed from: private */
    public final void startSeeding(List<ComponentName> list, Consumer<SeedResponse> consumer, boolean z) {
        if (list.isEmpty()) {
            endSeedingCall(!z);
            return;
        }
        ComponentName componentName = list.get(0);
        Log.d(TAG, "Beginning request to seed favorites for: " + componentName);
        this.bindingController.bindAndLoadSuggested(componentName, new ControlsControllerImpl$startSeeding$1(this, consumer, componentName, CollectionsKt.drop(list, 1), z));
    }

    private final void endSeedingCall(boolean z) {
        this.seedingInProgress = false;
        for (Consumer accept : this.seedingCallbacks) {
            accept.accept(Boolean.valueOf(z));
        }
        this.seedingCallbacks.clear();
    }

    static /* synthetic */ ControlStatus createRemovedStatus$default(ControlsControllerImpl controlsControllerImpl, ComponentName componentName, ControlInfo controlInfo, CharSequence charSequence, boolean z, int i, Object obj) {
        if ((i & 8) != 0) {
            z = true;
        }
        return controlsControllerImpl.createRemovedStatus(componentName, controlInfo, charSequence, z);
    }

    /* access modifiers changed from: private */
    public final ControlStatus createRemovedStatus(ComponentName componentName, ControlInfo controlInfo, CharSequence charSequence, boolean z) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setPackage(componentName.getPackageName());
        Control build = new Control.StatelessBuilder(controlInfo.getControlId(), PendingIntent.getActivity(this.context, componentName.hashCode(), intent, 67108864)).setTitle(controlInfo.getControlTitle()).setSubtitle(controlInfo.getControlSubtitle()).setStructure(charSequence).setDeviceType(controlInfo.getDeviceType()).build();
        Intrinsics.checkNotNullExpressionValue(build, "control");
        return new ControlStatus(build, componentName, true, z);
    }

    /* access modifiers changed from: private */
    public final Set<String> findRemoved(Set<String> set, List<Control> list) {
        Iterable<Control> iterable = list;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (Control controlId : iterable) {
            arrayList.add(controlId.getControlId());
        }
        return SetsKt.minus(set, (List) arrayList);
    }

    public void subscribeToFavorites(StructureInfo structureInfo) {
        Intrinsics.checkNotNullParameter(structureInfo, "structureInfo");
        if (confirmAvailability()) {
            this.bindingController.subscribe(structureInfo);
        }
    }

    public void unsubscribe() {
        if (confirmAvailability()) {
            this.bindingController.unsubscribe();
        }
    }

    public void addFavorite(ComponentName componentName, CharSequence charSequence, ControlInfo controlInfo) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(charSequence, "structureName");
        Intrinsics.checkNotNullParameter(controlInfo, "controlInfo");
        if (confirmAvailability()) {
            this.executor.execute(new ControlsControllerImpl$$ExternalSyntheticLambda2(componentName, charSequence, controlInfo, this));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: addFavorite$lambda-8  reason: not valid java name */
    public static final void m2612addFavorite$lambda8(ComponentName componentName, CharSequence charSequence, ControlInfo controlInfo, ControlsControllerImpl controlsControllerImpl) {
        Intrinsics.checkNotNullParameter(componentName, "$componentName");
        Intrinsics.checkNotNullParameter(charSequence, "$structureName");
        Intrinsics.checkNotNullParameter(controlInfo, "$controlInfo");
        Intrinsics.checkNotNullParameter(controlsControllerImpl, "this$0");
        if (Favorites.INSTANCE.addFavorite(componentName, charSequence, controlInfo)) {
            controlsControllerImpl.persistenceWrapper.storeFavorites(Favorites.INSTANCE.getAllStructures());
        }
    }

    public void replaceFavoritesForStructure(StructureInfo structureInfo) {
        Intrinsics.checkNotNullParameter(structureInfo, "structureInfo");
        if (confirmAvailability()) {
            this.executor.execute(new ControlsControllerImpl$$ExternalSyntheticLambda6(structureInfo, this));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: replaceFavoritesForStructure$lambda-9  reason: not valid java name */
    public static final void m2616replaceFavoritesForStructure$lambda9(StructureInfo structureInfo, ControlsControllerImpl controlsControllerImpl) {
        Intrinsics.checkNotNullParameter(structureInfo, "$structureInfo");
        Intrinsics.checkNotNullParameter(controlsControllerImpl, "this$0");
        Favorites.INSTANCE.replaceControls(structureInfo);
        controlsControllerImpl.persistenceWrapper.storeFavorites(Favorites.INSTANCE.getAllStructures());
    }

    public void refreshStatus(ComponentName componentName, Control control) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(control, "control");
        if (!confirmAvailability()) {
            Log.d(TAG, "Controls not available");
            return;
        }
        if (control.getStatus() == 1) {
            this.executor.execute(new ControlsControllerImpl$$ExternalSyntheticLambda3(componentName, control, this));
        }
        this.uiController.onRefreshState(componentName, CollectionsKt.listOf(control));
    }

    /* access modifiers changed from: private */
    /* renamed from: refreshStatus$lambda-10  reason: not valid java name */
    public static final void m2615refreshStatus$lambda10(ComponentName componentName, Control control, ControlsControllerImpl controlsControllerImpl) {
        Intrinsics.checkNotNullParameter(componentName, "$componentName");
        Intrinsics.checkNotNullParameter(control, "$control");
        Intrinsics.checkNotNullParameter(controlsControllerImpl, "this$0");
        if (Favorites.INSTANCE.updateControls(componentName, CollectionsKt.listOf(control))) {
            controlsControllerImpl.persistenceWrapper.storeFavorites(Favorites.INSTANCE.getAllStructures());
        }
    }

    public void onActionResponse(ComponentName componentName, String str, int i) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(str, "controlId");
        if (confirmAvailability()) {
            this.uiController.onActionResponse(componentName, str, i);
        }
    }

    public void action(ComponentName componentName, ControlInfo controlInfo, ControlAction controlAction) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(controlInfo, "controlInfo");
        Intrinsics.checkNotNullParameter(controlAction, "action");
        if (confirmAvailability()) {
            this.bindingController.action(componentName, controlInfo, controlAction);
        }
    }

    public List<StructureInfo> getFavorites() {
        return Favorites.INSTANCE.getAllStructures();
    }

    public int countFavoritesForComponent(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        return Favorites.INSTANCE.getControlsForComponent(componentName).size();
    }

    public List<StructureInfo> getFavoritesForComponent(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        return Favorites.INSTANCE.getStructuresForComponent(componentName);
    }

    public List<ControlInfo> getFavoritesForStructure(ComponentName componentName, CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(charSequence, "structureName");
        return Favorites.INSTANCE.getControlsForStructure(new StructureInfo(componentName, charSequence, CollectionsKt.emptyList()));
    }

    public StructureInfo getPreferredStructure() {
        return this.uiController.getPreferredStructure(getFavorites());
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("ControlsController state:");
        printWriter.println("  Changing users: " + this.userChanging);
        printWriter.println("  Current user: " + this.currentUser.getIdentifier());
        printWriter.println("  Favorites:");
        for (StructureInfo structureInfo : Favorites.INSTANCE.getAllStructures()) {
            printWriter.println("    " + structureInfo);
            for (ControlInfo controlInfo : structureInfo.getControls()) {
                printWriter.println("      " + controlInfo);
            }
        }
        printWriter.println(this.bindingController.toString());
    }
}

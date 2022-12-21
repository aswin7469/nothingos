package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.content.Context;
import android.icu.text.DateFormat;
import android.os.IBinder;
import android.os.UserHandle;
import android.service.controls.Control;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import android.service.controls.actions.ControlAction;
import android.util.Log;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.controls.controller.ControlsBindingController;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000b*\u0001\r\b\u0017\u0018\u0000 72\u00020\u0001:\b6789:;<=B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ \u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010\u001b\u001a\u00020!H\u0016J\u0018\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020&H\u0016J\u0018\u0010'\u001a\u00020\u001c2\u0006\u0010$\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020&H\u0016J\u0010\u0010(\u001a\u00020\u001c2\u0006\u0010$\u001a\u00020\u001eH\u0016J\u0010\u0010)\u001a\u00020\u001c2\u0006\u0010*\u001a\u00020\u0012H\u0016J\u0015\u0010+\u001a\u00020\u00102\u0006\u0010$\u001a\u00020\u001eH\u0011¢\u0006\u0002\b,J\u0010\u0010-\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\u0010\u0010.\u001a\u00020\u00102\u0006\u0010$\u001a\u00020\u001eH\u0002J\u0010\u0010/\u001a\u00020\u001c2\u0006\u00100\u001a\u000201H\u0016J\b\u00102\u001a\u000203H\u0016J\b\u00104\u001a\u00020\u001cH\u0002J\b\u00105\u001a\u00020\u001cH\u0016R\u0010\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0004\n\u0002\u0010\u000eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\u00020\u00148VX\u0004¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\b\u0018\u00010\u0018R\u00020\u0000X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u000e¢\u0006\u0002\n\u0000¨\u0006>"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl;", "Lcom/android/systemui/controls/controller/ControlsBindingController;", "context", "Landroid/content/Context;", "backgroundExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "lazyController", "Ldagger/Lazy;", "Lcom/android/systemui/controls/controller/ControlsController;", "userTracker", "Lcom/android/systemui/settings/UserTracker;", "(Landroid/content/Context;Lcom/android/systemui/util/concurrency/DelayableExecutor;Ldagger/Lazy;Lcom/android/systemui/settings/UserTracker;)V", "actionCallbackService", "com/android/systemui/controls/controller/ControlsBindingControllerImpl$actionCallbackService$1", "Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl$actionCallbackService$1;", "currentProvider", "Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager;", "currentUser", "Landroid/os/UserHandle;", "currentUserId", "", "getCurrentUserId", "()I", "loadSubscriber", "Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl$LoadSubscriber;", "statefulControlSubscriber", "Lcom/android/systemui/controls/controller/StatefulControlSubscriber;", "action", "", "componentName", "Landroid/content/ComponentName;", "controlInfo", "Lcom/android/systemui/controls/controller/ControlInfo;", "Landroid/service/controls/actions/ControlAction;", "bindAndLoad", "Ljava/lang/Runnable;", "component", "callback", "Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;", "bindAndLoadSuggested", "bindService", "changeUser", "newUser", "createProviderManager", "createProviderManager$SystemUI_nothingRelease", "onComponentRemoved", "retrieveLifecycleManager", "subscribe", "structureInfo", "Lcom/android/systemui/controls/controller/StructureInfo;", "toString", "", "unbind", "unsubscribe", "CallbackRunnable", "Companion", "LoadSubscriber", "OnActionResponseRunnable", "OnCancelAndLoadRunnable", "OnLoadErrorRunnable", "OnLoadRunnable", "OnSubscribeRunnable", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsBindingControllerImpl.kt */
public class ControlsBindingControllerImpl implements ControlsBindingController {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final long MAX_CONTROLS_REQUEST = 100000;
    private static final long SUGGESTED_CONTROLS_REQUEST = 36;
    private static final long SUGGESTED_STRUCTURES = 6;
    private static final String TAG = "ControlsBindingControllerImpl";
    /* access modifiers changed from: private */
    public static final ControlsBindingControllerImpl$Companion$emptyCallback$1 emptyCallback = new ControlsBindingControllerImpl$Companion$emptyCallback$1();
    private final ControlsBindingControllerImpl$actionCallbackService$1 actionCallbackService = new ControlsBindingControllerImpl$actionCallbackService$1(this);
    /* access modifiers changed from: private */
    public final DelayableExecutor backgroundExecutor;
    private final Context context;
    /* access modifiers changed from: private */
    public ControlsProviderLifecycleManager currentProvider;
    /* access modifiers changed from: private */
    public UserHandle currentUser;
    /* access modifiers changed from: private */
    public final Lazy<ControlsController> lazyController;
    private LoadSubscriber loadSubscriber;
    private StatefulControlSubscriber statefulControlSubscriber;

    @Inject
    public ControlsBindingControllerImpl(Context context2, @Background DelayableExecutor delayableExecutor, Lazy<ControlsController> lazy, UserTracker userTracker) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(delayableExecutor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(lazy, "lazyController");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        this.context = context2;
        this.backgroundExecutor = delayableExecutor;
        this.lazyController = lazy;
        this.currentUser = userTracker.getUserHandle();
    }

    @Metadata(mo64986d1 = {"\u0000!\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\b\u0003*\u0001\n\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0004\n\u0002\u0010\u000b¨\u0006\f"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl$Companion;", "", "()V", "MAX_CONTROLS_REQUEST", "", "SUGGESTED_CONTROLS_REQUEST", "SUGGESTED_STRUCTURES", "TAG", "", "emptyCallback", "com/android/systemui/controls/controller/ControlsBindingControllerImpl$Companion$emptyCallback$1", "Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl$Companion$emptyCallback$1;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsBindingControllerImpl.kt */
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

    public ControlsProviderLifecycleManager createProviderManager$SystemUI_nothingRelease(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, "component");
        return new ControlsProviderLifecycleManager(this.context, this.backgroundExecutor, this.actionCallbackService, this.currentUser, componentName);
    }

    private final ControlsProviderLifecycleManager retrieveLifecycleManager(ComponentName componentName) {
        ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.currentProvider;
        if (controlsProviderLifecycleManager != null) {
            if (!Intrinsics.areEqual((Object) controlsProviderLifecycleManager != null ? controlsProviderLifecycleManager.getComponentName() : null, (Object) componentName)) {
                unbind();
            }
        }
        ControlsProviderLifecycleManager controlsProviderLifecycleManager2 = this.currentProvider;
        if (controlsProviderLifecycleManager2 == null) {
            controlsProviderLifecycleManager2 = createProviderManager$SystemUI_nothingRelease(componentName);
        }
        this.currentProvider = controlsProviderLifecycleManager2;
        return controlsProviderLifecycleManager2;
    }

    public Runnable bindAndLoad(ComponentName componentName, ControlsBindingController.LoadCallback loadCallback) {
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(loadCallback, "callback");
        LoadSubscriber loadSubscriber2 = this.loadSubscriber;
        if (loadSubscriber2 != null) {
            loadSubscriber2.loadCancel();
        }
        IControlsSubscriber.Stub loadSubscriber3 = new LoadSubscriber(this, loadCallback, MAX_CONTROLS_REQUEST);
        this.loadSubscriber = loadSubscriber3;
        retrieveLifecycleManager(componentName).maybeBindAndLoad(loadSubscriber3);
        return loadSubscriber3.loadCancel();
    }

    public void bindAndLoadSuggested(ComponentName componentName, ControlsBindingController.LoadCallback loadCallback) {
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(loadCallback, "callback");
        LoadSubscriber loadSubscriber2 = this.loadSubscriber;
        if (loadSubscriber2 != null) {
            loadSubscriber2.loadCancel();
        }
        IControlsSubscriber.Stub loadSubscriber3 = new LoadSubscriber(this, loadCallback, SUGGESTED_CONTROLS_REQUEST);
        this.loadSubscriber = loadSubscriber3;
        retrieveLifecycleManager(componentName).maybeBindAndLoadSuggested(loadSubscriber3);
    }

    public void subscribe(StructureInfo structureInfo) {
        Intrinsics.checkNotNullParameter(structureInfo, "structureInfo");
        unsubscribe();
        ControlsProviderLifecycleManager retrieveLifecycleManager = retrieveLifecycleManager(structureInfo.getComponentName());
        ControlsController controlsController = this.lazyController.get();
        Intrinsics.checkNotNullExpressionValue(controlsController, "lazyController.get()");
        IControlsSubscriber statefulControlSubscriber2 = new StatefulControlSubscriber(controlsController, retrieveLifecycleManager, this.backgroundExecutor, MAX_CONTROLS_REQUEST);
        this.statefulControlSubscriber = statefulControlSubscriber2;
        Iterable<ControlInfo> controls = structureInfo.getControls();
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(controls, 10));
        for (ControlInfo controlId : controls) {
            arrayList.add(controlId.getControlId());
        }
        retrieveLifecycleManager.maybeBindAndSubscribe((List) arrayList, statefulControlSubscriber2);
    }

    public void unsubscribe() {
        StatefulControlSubscriber statefulControlSubscriber2 = this.statefulControlSubscriber;
        if (statefulControlSubscriber2 != null) {
            statefulControlSubscriber2.cancel();
        }
        this.statefulControlSubscriber = null;
    }

    public void action(ComponentName componentName, ControlInfo controlInfo, ControlAction controlAction) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(controlInfo, "controlInfo");
        Intrinsics.checkNotNullParameter(controlAction, "action");
        if (this.statefulControlSubscriber == null) {
            Log.w(TAG, "No actions can occur outside of an active subscription. Ignoring.");
        } else {
            retrieveLifecycleManager(componentName).maybeBindAndSendAction(controlInfo.getControlId(), controlAction);
        }
    }

    public void bindService(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, "component");
        retrieveLifecycleManager(componentName).bindService();
    }

    public void changeUser(UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(userHandle, "newUser");
        if (!Intrinsics.areEqual((Object) userHandle, (Object) this.currentUser)) {
            unbind();
            this.currentUser = userHandle;
        }
    }

    private final void unbind() {
        unsubscribe();
        LoadSubscriber loadSubscriber2 = this.loadSubscriber;
        if (loadSubscriber2 != null) {
            loadSubscriber2.loadCancel();
        }
        this.loadSubscriber = null;
        ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.currentProvider;
        if (controlsProviderLifecycleManager != null) {
            controlsProviderLifecycleManager.unbindService();
        }
        this.currentProvider = null;
    }

    public void onComponentRemoved(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        this.backgroundExecutor.execute(new ControlsBindingControllerImpl$$ExternalSyntheticLambda0(this, componentName));
    }

    /* access modifiers changed from: private */
    /* renamed from: onComponentRemoved$lambda-2  reason: not valid java name */
    public static final void m2602onComponentRemoved$lambda2(ControlsBindingControllerImpl controlsBindingControllerImpl, ComponentName componentName) {
        Intrinsics.checkNotNullParameter(controlsBindingControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(componentName, "$componentName");
        ControlsProviderLifecycleManager controlsProviderLifecycleManager = controlsBindingControllerImpl.currentProvider;
        if (controlsProviderLifecycleManager != null && Intrinsics.areEqual((Object) controlsProviderLifecycleManager.getComponentName(), (Object) componentName)) {
            controlsBindingControllerImpl.unbind();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("  ControlsBindingController:\n");
        sb.append("    currentUser=" + this.currentUser + 10);
        sb.append("    StatefulControlSubscriber=" + this.statefulControlSubscriber);
        sb.append("    Providers=" + this.currentProvider + 10);
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "StringBuilder(\"  Control…\\n\")\n        }.toString()");
        return sb2;
    }

    @Metadata(mo64986d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\b¢\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u000b\u001a\u00020\fH&J\b\u0010\r\u001a\u00020\fH\u0016R\u0016\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u000e"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl$CallbackRunnable;", "Ljava/lang/Runnable;", "token", "Landroid/os/IBinder;", "(Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl;Landroid/os/IBinder;)V", "provider", "Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager;", "getProvider", "()Lcom/android/systemui/controls/controller/ControlsProviderLifecycleManager;", "getToken", "()Landroid/os/IBinder;", "doRun", "", "run", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsBindingControllerImpl.kt */
    private abstract class CallbackRunnable implements Runnable {
        private final ControlsProviderLifecycleManager provider;
        final /* synthetic */ ControlsBindingControllerImpl this$0;
        private final IBinder token;

        public abstract void doRun();

        public CallbackRunnable(ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder) {
            Intrinsics.checkNotNullParameter(iBinder, "token");
            this.this$0 = controlsBindingControllerImpl;
            this.token = iBinder;
            this.provider = controlsBindingControllerImpl.currentProvider;
        }

        public final IBinder getToken() {
            return this.token;
        }

        /* access modifiers changed from: protected */
        public final ControlsProviderLifecycleManager getProvider() {
            return this.provider;
        }

        public void run() {
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.provider;
            if (controlsProviderLifecycleManager == null) {
                Log.e(ControlsBindingControllerImpl.TAG, "No current provider set");
            } else if (!Intrinsics.areEqual((Object) controlsProviderLifecycleManager.getUser(), (Object) this.this$0.currentUser)) {
                Log.e(ControlsBindingControllerImpl.TAG, "User " + this.provider.getUser() + " is not current user");
            } else if (!Intrinsics.areEqual((Object) this.token, (Object) this.provider.getToken())) {
                Log.e(ControlsBindingControllerImpl.TAG, "Provider for token:" + this.token + " does not exist anymore");
            } else {
                doRun();
            }
        }
    }

    @Metadata(mo64986d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\b\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B#\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\b\u0010\u000f\u001a\u00020\u0010H\u0016R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u0011"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl$OnLoadRunnable;", "Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl$CallbackRunnable;", "Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl;", "token", "Landroid/os/IBinder;", "list", "", "Landroid/service/controls/Control;", "callback", "Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;", "(Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl;Landroid/os/IBinder;Ljava/util/List;Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;)V", "getCallback", "()Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;", "getList", "()Ljava/util/List;", "doRun", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsBindingControllerImpl.kt */
    private final class OnLoadRunnable extends CallbackRunnable {
        private final ControlsBindingController.LoadCallback callback;
        private final List<Control> list;
        final /* synthetic */ ControlsBindingControllerImpl this$0;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public OnLoadRunnable(ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder, List<Control> list2, ControlsBindingController.LoadCallback loadCallback) {
            super(controlsBindingControllerImpl, iBinder);
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(list2, "list");
            Intrinsics.checkNotNullParameter(loadCallback, "callback");
            this.this$0 = controlsBindingControllerImpl;
            this.list = list2;
            this.callback = loadCallback;
        }

        public final List<Control> getList() {
            return this.list;
        }

        public final ControlsBindingController.LoadCallback getCallback() {
            return this.callback;
        }

        public void doRun() {
            Log.d(ControlsBindingControllerImpl.TAG, "LoadSubscription: Complete and loading controls");
            this.callback.accept(this.list);
        }
    }

    @Metadata(mo64986d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0000\b\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B+\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\b\u0010\u0013\u001a\u00020\u0014H\u0016R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006\u0015"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl$OnCancelAndLoadRunnable;", "Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl$CallbackRunnable;", "Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl;", "token", "Landroid/os/IBinder;", "list", "", "Landroid/service/controls/Control;", "subscription", "Landroid/service/controls/IControlsSubscription;", "callback", "Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;", "(Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl;Landroid/os/IBinder;Ljava/util/List;Landroid/service/controls/IControlsSubscription;Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;)V", "getCallback", "()Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;", "getList", "()Ljava/util/List;", "getSubscription", "()Landroid/service/controls/IControlsSubscription;", "doRun", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsBindingControllerImpl.kt */
    private final class OnCancelAndLoadRunnable extends CallbackRunnable {
        private final ControlsBindingController.LoadCallback callback;
        private final List<Control> list;
        private final IControlsSubscription subscription;
        final /* synthetic */ ControlsBindingControllerImpl this$0;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public OnCancelAndLoadRunnable(ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder, List<Control> list2, IControlsSubscription iControlsSubscription, ControlsBindingController.LoadCallback loadCallback) {
            super(controlsBindingControllerImpl, iBinder);
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(list2, "list");
            Intrinsics.checkNotNullParameter(iControlsSubscription, "subscription");
            Intrinsics.checkNotNullParameter(loadCallback, "callback");
            this.this$0 = controlsBindingControllerImpl;
            this.list = list2;
            this.subscription = iControlsSubscription;
            this.callback = loadCallback;
        }

        public final List<Control> getList() {
            return this.list;
        }

        public final IControlsSubscription getSubscription() {
            return this.subscription;
        }

        public final ControlsBindingController.LoadCallback getCallback() {
            return this.callback;
        }

        public void doRun() {
            Log.d(ControlsBindingControllerImpl.TAG, "LoadSubscription: Canceling and loading controls");
            ControlsProviderLifecycleManager provider = getProvider();
            if (provider != null) {
                provider.cancelSubscription(this.subscription);
            }
            this.callback.accept(this.list);
        }
    }

    @Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\b\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\b\u0010\u000e\u001a\u00020\u000fH\u0016R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u0010"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl$OnSubscribeRunnable;", "Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl$CallbackRunnable;", "Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl;", "token", "Landroid/os/IBinder;", "subscription", "Landroid/service/controls/IControlsSubscription;", "requestLimit", "", "(Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl;Landroid/os/IBinder;Landroid/service/controls/IControlsSubscription;J)V", "getRequestLimit", "()J", "getSubscription", "()Landroid/service/controls/IControlsSubscription;", "doRun", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsBindingControllerImpl.kt */
    private final class OnSubscribeRunnable extends CallbackRunnable {
        private final long requestLimit;
        private final IControlsSubscription subscription;
        final /* synthetic */ ControlsBindingControllerImpl this$0;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public OnSubscribeRunnable(ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder, IControlsSubscription iControlsSubscription, long j) {
            super(controlsBindingControllerImpl, iBinder);
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(iControlsSubscription, "subscription");
            this.this$0 = controlsBindingControllerImpl;
            this.subscription = iControlsSubscription;
            this.requestLimit = j;
        }

        public final IControlsSubscription getSubscription() {
            return this.subscription;
        }

        public final long getRequestLimit() {
            return this.requestLimit;
        }

        public void doRun() {
            Log.d(ControlsBindingControllerImpl.TAG, "LoadSubscription: Starting subscription");
            ControlsProviderLifecycleManager provider = getProvider();
            if (provider != null) {
                provider.startSubscription(this.subscription, this.requestLimit);
            }
        }
    }

    @Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\b\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\b\u0010\u000e\u001a\u00020\u000fH\u0016R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u0010"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl$OnActionResponseRunnable;", "Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl$CallbackRunnable;", "Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl;", "token", "Landroid/os/IBinder;", "controlId", "", "response", "", "(Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl;Landroid/os/IBinder;Ljava/lang/String;I)V", "getControlId", "()Ljava/lang/String;", "getResponse", "()I", "doRun", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsBindingControllerImpl.kt */
    private final class OnActionResponseRunnable extends CallbackRunnable {
        private final String controlId;
        private final int response;
        final /* synthetic */ ControlsBindingControllerImpl this$0;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public OnActionResponseRunnable(ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder, String str, int i) {
            super(controlsBindingControllerImpl, iBinder);
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(str, "controlId");
            this.this$0 = controlsBindingControllerImpl;
            this.controlId = str;
            this.response = i;
        }

        public final String getControlId() {
            return this.controlId;
        }

        public final int getResponse() {
            return this.response;
        }

        public void doRun() {
            ControlsProviderLifecycleManager provider = getProvider();
            if (provider != null) {
                ((ControlsController) this.this$0.lazyController.get()).onActionResponse(provider.getComponentName(), this.controlId, this.response);
            }
        }
    }

    @Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\b\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B\u001d\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\b\u0010\u000e\u001a\u00020\u000fH\u0016R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u0010"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl$OnLoadErrorRunnable;", "Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl$CallbackRunnable;", "Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl;", "token", "Landroid/os/IBinder;", "error", "", "callback", "Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;", "(Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl;Landroid/os/IBinder;Ljava/lang/String;Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;)V", "getCallback", "()Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;", "getError", "()Ljava/lang/String;", "doRun", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsBindingControllerImpl.kt */
    private final class OnLoadErrorRunnable extends CallbackRunnable {
        private final ControlsBindingController.LoadCallback callback;
        private final String error;
        final /* synthetic */ ControlsBindingControllerImpl this$0;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public OnLoadErrorRunnable(ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder, String str, ControlsBindingController.LoadCallback loadCallback) {
            super(controlsBindingControllerImpl, iBinder);
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(str, "error");
            Intrinsics.checkNotNullParameter(loadCallback, "callback");
            this.this$0 = controlsBindingControllerImpl;
            this.error = str;
            this.callback = loadCallback;
        }

        public final String getError() {
            return this.error;
        }

        public final ControlsBindingController.LoadCallback getCallback() {
            return this.callback;
        }

        public void doRun() {
            this.callback.error(this.error);
            ControlsProviderLifecycleManager provider = getProvider();
            if (provider != null) {
                Log.e(ControlsBindingControllerImpl.TAG, "onError receive from '" + provider.getComponentName() + "': " + this.error);
            }
        }
    }

    @Metadata(mo64986d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0004\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010\u001a\u001a\u00020\u001bJ\u0010\u0010\u001c\u001a\u00020\t2\u0006\u0010\u001d\u001a\u00020\u001bH\u0002J\u0010\u0010\u001e\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020 H\u0016J\u0018\u0010!\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010\"\u001a\u00020#H\u0016J\u0018\u0010$\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010%\u001a\u00020\u0012H\u0016J\u0018\u0010&\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010'\u001a\u00020\u0019H\u0016R\u0016\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\bX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R!\u0010\u0010\u001a\u0012\u0012\u0004\u0012\u00020\u00120\u0011j\b\u0012\u0004\u0012\u00020\u0012`\u0013¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0018\u001a\u00020\u0019X.¢\u0006\u0002\n\u0000¨\u0006("}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl$LoadSubscriber;", "Landroid/service/controls/IControlsSubscriber$Stub;", "callback", "Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;", "requestLimit", "", "(Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl;Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;J)V", "_loadCancelInternal", "Lkotlin/Function0;", "", "getCallback", "()Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;", "setCallback", "(Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;)V", "isTerminated", "Ljava/util/concurrent/atomic/AtomicBoolean;", "loadedControls", "Ljava/util/ArrayList;", "Landroid/service/controls/Control;", "Lkotlin/collections/ArrayList;", "getLoadedControls", "()Ljava/util/ArrayList;", "getRequestLimit", "()J", "subscription", "Landroid/service/controls/IControlsSubscription;", "loadCancel", "Ljava/lang/Runnable;", "maybeTerminateAndRun", "postTerminateFn", "onComplete", "token", "Landroid/os/IBinder;", "onError", "s", "", "onNext", "c", "onSubscribe", "subs", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsBindingControllerImpl.kt */
    private final class LoadSubscriber extends IControlsSubscriber.Stub {
        private Function0<Unit> _loadCancelInternal;
        private ControlsBindingController.LoadCallback callback;
        private AtomicBoolean isTerminated = new AtomicBoolean(false);
        private final ArrayList<Control> loadedControls = new ArrayList<>();
        private final long requestLimit;
        /* access modifiers changed from: private */
        public IControlsSubscription subscription;
        final /* synthetic */ ControlsBindingControllerImpl this$0;

        public LoadSubscriber(ControlsBindingControllerImpl controlsBindingControllerImpl, ControlsBindingController.LoadCallback loadCallback, long j) {
            Intrinsics.checkNotNullParameter(loadCallback, "callback");
            this.this$0 = controlsBindingControllerImpl;
            this.callback = loadCallback;
            this.requestLimit = j;
        }

        public final ControlsBindingController.LoadCallback getCallback() {
            return this.callback;
        }

        public final void setCallback(ControlsBindingController.LoadCallback loadCallback) {
            Intrinsics.checkNotNullParameter(loadCallback, "<set-?>");
            this.callback = loadCallback;
        }

        public final long getRequestLimit() {
            return this.requestLimit;
        }

        public final ArrayList<Control> getLoadedControls() {
            return this.loadedControls;
        }

        public final Runnable loadCancel() {
            return new C2022x1b4f6956(this);
        }

        /* access modifiers changed from: private */
        /* renamed from: loadCancel$lambda-1  reason: not valid java name */
        public static final void m2605loadCancel$lambda1(LoadSubscriber loadSubscriber) {
            Intrinsics.checkNotNullParameter(loadSubscriber, "this$0");
            Function0<Unit> function0 = loadSubscriber._loadCancelInternal;
            if (function0 != null) {
                Log.d(ControlsBindingControllerImpl.TAG, "Canceling loadSubscribtion");
                function0.invoke();
            }
        }

        public void onSubscribe(IBinder iBinder, IControlsSubscription iControlsSubscription) {
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(iControlsSubscription, "subs");
            this.subscription = iControlsSubscription;
            this._loadCancelInternal = new ControlsBindingControllerImpl$LoadSubscriber$onSubscribe$1(this.this$0, this);
            this.this$0.backgroundExecutor.execute(new OnSubscribeRunnable(this.this$0, iBinder, iControlsSubscription, this.requestLimit));
        }

        public void onNext(IBinder iBinder, Control control) {
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(control, "c");
            this.this$0.backgroundExecutor.execute(new C2024x1b4f6958(this, control, this.this$0, iBinder));
        }

        /* access modifiers changed from: private */
        /* renamed from: onNext$lambda-2  reason: not valid java name */
        public static final void m2607onNext$lambda2(LoadSubscriber loadSubscriber, Control control, ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder) {
            Intrinsics.checkNotNullParameter(loadSubscriber, "this$0");
            Intrinsics.checkNotNullParameter(control, "$c");
            Intrinsics.checkNotNullParameter(controlsBindingControllerImpl, "this$1");
            Intrinsics.checkNotNullParameter(iBinder, "$token");
            if (!loadSubscriber.isTerminated.get()) {
                loadSubscriber.loadedControls.add(control);
                if (((long) loadSubscriber.loadedControls.size()) >= loadSubscriber.requestLimit) {
                    List list = loadSubscriber.loadedControls;
                    IControlsSubscription iControlsSubscription = loadSubscriber.subscription;
                    if (iControlsSubscription == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("subscription");
                        iControlsSubscription = null;
                    }
                    loadSubscriber.maybeTerminateAndRun(new OnCancelAndLoadRunnable(controlsBindingControllerImpl, iBinder, list, iControlsSubscription, loadSubscriber.callback));
                }
            }
        }

        public void onError(IBinder iBinder, String str) {
            Intrinsics.checkNotNullParameter(iBinder, "token");
            Intrinsics.checkNotNullParameter(str, DateFormat.SECOND);
            maybeTerminateAndRun(new OnLoadErrorRunnable(this.this$0, iBinder, str, this.callback));
        }

        public void onComplete(IBinder iBinder) {
            Intrinsics.checkNotNullParameter(iBinder, "token");
            maybeTerminateAndRun(new OnLoadRunnable(this.this$0, iBinder, this.loadedControls, this.callback));
        }

        private final void maybeTerminateAndRun(Runnable runnable) {
            if (!this.isTerminated.get()) {
                this._loadCancelInternal = C2025x5cb900b7.INSTANCE;
                this.callback = ControlsBindingControllerImpl.emptyCallback;
                ControlsProviderLifecycleManager access$getCurrentProvider$p = this.this$0.currentProvider;
                if (access$getCurrentProvider$p != null) {
                    access$getCurrentProvider$p.cancelLoadTimeout();
                }
                this.this$0.backgroundExecutor.execute(new C2023x1b4f6957(this, runnable));
            }
        }

        /* access modifiers changed from: private */
        /* renamed from: maybeTerminateAndRun$lambda-3  reason: not valid java name */
        public static final void m2606maybeTerminateAndRun$lambda3(LoadSubscriber loadSubscriber, Runnable runnable) {
            Intrinsics.checkNotNullParameter(loadSubscriber, "this$0");
            Intrinsics.checkNotNullParameter(runnable, "$postTerminateFn");
            loadSubscriber.isTerminated.compareAndSet(false, true);
            runnable.run();
        }
    }
}

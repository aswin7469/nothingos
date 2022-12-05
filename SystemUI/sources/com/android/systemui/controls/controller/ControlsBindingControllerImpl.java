package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.os.UserHandle;
import android.service.controls.Control;
import android.service.controls.IControlsActionCallback;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import android.service.controls.actions.ControlAction;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.controls.controller.ControlsBindingController;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsBindingControllerImpl.kt */
@VisibleForTesting
/* loaded from: classes.dex */
public class ControlsBindingControllerImpl implements ControlsBindingController {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final ControlsBindingControllerImpl$Companion$emptyCallback$1 emptyCallback = new ControlsBindingController.LoadCallback() { // from class: com.android.systemui.controls.controller.ControlsBindingControllerImpl$Companion$emptyCallback$1
        /* renamed from: accept  reason: avoid collision after fix types in other method */
        public void accept2(@NotNull List<Control> controls) {
            Intrinsics.checkNotNullParameter(controls, "controls");
        }

        @Override // com.android.systemui.controls.controller.ControlsBindingController.LoadCallback
        public void error(@NotNull String message) {
            Intrinsics.checkNotNullParameter(message, "message");
        }

        @Override // java.util.function.Consumer
        public /* bridge */ /* synthetic */ void accept(List<? extends Control> list) {
            accept2((List<Control>) list);
        }
    };
    @NotNull
    private final ControlsBindingControllerImpl$actionCallbackService$1 actionCallbackService = new IControlsActionCallback.Stub() { // from class: com.android.systemui.controls.controller.ControlsBindingControllerImpl$actionCallbackService$1
        public void accept(@NotNull IBinder token, @NotNull String controlId, int i) {
            Intrinsics.checkNotNullParameter(token, "token");
            Intrinsics.checkNotNullParameter(controlId, "controlId");
            ControlsBindingControllerImpl.this.backgroundExecutor.execute(new ControlsBindingControllerImpl.OnActionResponseRunnable(ControlsBindingControllerImpl.this, token, controlId, i));
        }
    };
    @NotNull
    private final DelayableExecutor backgroundExecutor;
    @NotNull
    private final Context context;
    @Nullable
    private ControlsProviderLifecycleManager currentProvider;
    @NotNull
    private UserHandle currentUser;
    @NotNull
    private final Lazy<ControlsController> lazyController;
    @Nullable
    private LoadSubscriber loadSubscriber;
    @Nullable
    private StatefulControlSubscriber statefulControlSubscriber;

    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.controls.controller.ControlsBindingControllerImpl$actionCallbackService$1] */
    public ControlsBindingControllerImpl(@NotNull Context context, @NotNull DelayableExecutor backgroundExecutor, @NotNull Lazy<ControlsController> lazyController, @NotNull UserTracker userTracker) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(backgroundExecutor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(lazyController, "lazyController");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        this.context = context;
        this.backgroundExecutor = backgroundExecutor;
        this.lazyController = lazyController;
        this.currentUser = userTracker.getUserHandle();
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @VisibleForTesting
    @NotNull
    public ControlsProviderLifecycleManager createProviderManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@NotNull ComponentName component) {
        Intrinsics.checkNotNullParameter(component, "component");
        return new ControlsProviderLifecycleManager(this.context, this.backgroundExecutor, this.actionCallbackService, this.currentUser, component);
    }

    private final ControlsProviderLifecycleManager retrieveLifecycleManager(ComponentName componentName) {
        ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.currentProvider;
        if (controlsProviderLifecycleManager != null) {
            if (!Intrinsics.areEqual(controlsProviderLifecycleManager == null ? null : controlsProviderLifecycleManager.getComponentName(), componentName)) {
                unbind();
            }
        }
        ControlsProviderLifecycleManager controlsProviderLifecycleManager2 = this.currentProvider;
        if (controlsProviderLifecycleManager2 == null) {
            controlsProviderLifecycleManager2 = createProviderManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core(componentName);
        }
        this.currentProvider = controlsProviderLifecycleManager2;
        return controlsProviderLifecycleManager2;
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    @NotNull
    public Runnable bindAndLoad(@NotNull ComponentName component, @NotNull ControlsBindingController.LoadCallback callback) {
        Intrinsics.checkNotNullParameter(component, "component");
        Intrinsics.checkNotNullParameter(callback, "callback");
        LoadSubscriber loadSubscriber = this.loadSubscriber;
        if (loadSubscriber != null) {
            loadSubscriber.loadCancel();
        }
        LoadSubscriber loadSubscriber2 = new LoadSubscriber(this, callback, 100000L);
        this.loadSubscriber = loadSubscriber2;
        retrieveLifecycleManager(component).maybeBindAndLoad(loadSubscriber2);
        return loadSubscriber2.loadCancel();
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public void bindAndLoadSuggested(@NotNull ComponentName component, @NotNull ControlsBindingController.LoadCallback callback) {
        Intrinsics.checkNotNullParameter(component, "component");
        Intrinsics.checkNotNullParameter(callback, "callback");
        LoadSubscriber loadSubscriber = this.loadSubscriber;
        if (loadSubscriber != null) {
            loadSubscriber.loadCancel();
        }
        LoadSubscriber loadSubscriber2 = new LoadSubscriber(this, callback, 36L);
        this.loadSubscriber = loadSubscriber2;
        retrieveLifecycleManager(component).maybeBindAndLoadSuggested(loadSubscriber2);
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public void subscribe(@NotNull StructureInfo structureInfo) {
        int collectionSizeOrDefault;
        Intrinsics.checkNotNullParameter(structureInfo, "structureInfo");
        unsubscribe();
        ControlsProviderLifecycleManager retrieveLifecycleManager = retrieveLifecycleManager(structureInfo.getComponentName());
        ControlsController controlsController = this.lazyController.get();
        Intrinsics.checkNotNullExpressionValue(controlsController, "lazyController.get()");
        StatefulControlSubscriber statefulControlSubscriber = new StatefulControlSubscriber(controlsController, retrieveLifecycleManager, this.backgroundExecutor, 100000L);
        this.statefulControlSubscriber = statefulControlSubscriber;
        List<ControlInfo> controls = structureInfo.getControls();
        collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(controls, 10);
        ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
        for (ControlInfo controlInfo : controls) {
            arrayList.add(controlInfo.getControlId());
        }
        retrieveLifecycleManager.maybeBindAndSubscribe(arrayList, statefulControlSubscriber);
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public void unsubscribe() {
        StatefulControlSubscriber statefulControlSubscriber = this.statefulControlSubscriber;
        if (statefulControlSubscriber != null) {
            statefulControlSubscriber.cancel();
        }
        this.statefulControlSubscriber = null;
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public void action(@NotNull ComponentName componentName, @NotNull ControlInfo controlInfo, @NotNull ControlAction action) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(controlInfo, "controlInfo");
        Intrinsics.checkNotNullParameter(action, "action");
        if (this.statefulControlSubscriber == null) {
            Log.w("ControlsBindingControllerImpl", "No actions can occur outside of an active subscription. Ignoring.");
        } else {
            retrieveLifecycleManager(componentName).maybeBindAndSendAction(controlInfo.getControlId(), action);
        }
    }

    @Override // com.android.systemui.util.UserAwareController
    public void changeUser(@NotNull UserHandle newUser) {
        Intrinsics.checkNotNullParameter(newUser, "newUser");
        if (Intrinsics.areEqual(newUser, this.currentUser)) {
            return;
        }
        unbind();
        this.currentUser = newUser;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void unbind() {
        unsubscribe();
        LoadSubscriber loadSubscriber = this.loadSubscriber;
        if (loadSubscriber != null) {
            loadSubscriber.loadCancel();
        }
        this.loadSubscriber = null;
        ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.currentProvider;
        if (controlsProviderLifecycleManager != null) {
            controlsProviderLifecycleManager.unbindService();
        }
        this.currentProvider = null;
    }

    @Override // com.android.systemui.controls.controller.ControlsBindingController
    public void onComponentRemoved(@NotNull final ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsBindingControllerImpl$onComponentRemoved$1
            @Override // java.lang.Runnable
            public final void run() {
                ControlsProviderLifecycleManager controlsProviderLifecycleManager = ControlsBindingControllerImpl.this.currentProvider;
                if (controlsProviderLifecycleManager == null) {
                    return;
                }
                ComponentName componentName2 = componentName;
                ControlsBindingControllerImpl controlsBindingControllerImpl = ControlsBindingControllerImpl.this;
                if (!Intrinsics.areEqual(controlsProviderLifecycleManager.getComponentName(), componentName2)) {
                    return;
                }
                controlsBindingControllerImpl.unbind();
            }
        });
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder("  ControlsBindingController:\n");
        sb.append("    currentUser=" + this.currentUser + '\n');
        sb.append(Intrinsics.stringPlus("    StatefulControlSubscriber=", this.statefulControlSubscriber));
        sb.append("    Providers=" + this.currentProvider + '\n');
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "StringBuilder(\"  ControlsBindingController:\\n\").apply {\n            append(\"    currentUser=$currentUser\\n\")\n            append(\"    StatefulControlSubscriber=$statefulControlSubscriber\")\n            append(\"    Providers=$currentProvider\\n\")\n        }.toString()");
        return sb2;
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    private abstract class CallbackRunnable implements Runnable {
        @Nullable
        private final ControlsProviderLifecycleManager provider;
        final /* synthetic */ ControlsBindingControllerImpl this$0;
        @NotNull
        private final IBinder token;

        public abstract void doRun();

        public CallbackRunnable(@NotNull ControlsBindingControllerImpl this$0, IBinder token) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(token, "token");
            this.this$0 = this$0;
            this.token = token;
            this.provider = this$0.currentProvider;
        }

        @Nullable
        protected final ControlsProviderLifecycleManager getProvider() {
            return this.provider;
        }

        @Override // java.lang.Runnable
        public void run() {
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.provider;
            if (controlsProviderLifecycleManager != null) {
                if (!Intrinsics.areEqual(controlsProviderLifecycleManager.getUser(), this.this$0.currentUser)) {
                    Log.e("ControlsBindingControllerImpl", "User " + this.provider.getUser() + " is not current user");
                    return;
                } else if (!Intrinsics.areEqual(this.token, this.provider.getToken())) {
                    Log.e("ControlsBindingControllerImpl", "Provider for token:" + this.token + " does not exist anymore");
                    return;
                } else {
                    doRun();
                    return;
                }
            }
            Log.e("ControlsBindingControllerImpl", "No current provider set");
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    private final class OnLoadRunnable extends CallbackRunnable {
        @NotNull
        private final ControlsBindingController.LoadCallback callback;
        @NotNull
        private final List<Control> list;
        final /* synthetic */ ControlsBindingControllerImpl this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public OnLoadRunnable(@NotNull ControlsBindingControllerImpl this$0, @NotNull IBinder token, @NotNull List<Control> list, ControlsBindingController.LoadCallback callback) {
            super(this$0, token);
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(token, "token");
            Intrinsics.checkNotNullParameter(list, "list");
            Intrinsics.checkNotNullParameter(callback, "callback");
            this.this$0 = this$0;
            this.list = list;
            this.callback = callback;
        }

        @Override // com.android.systemui.controls.controller.ControlsBindingControllerImpl.CallbackRunnable
        public void doRun() {
            Log.d("ControlsBindingControllerImpl", "LoadSubscription: Complete and loading controls");
            this.callback.accept(this.list);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    public final class OnCancelAndLoadRunnable extends CallbackRunnable {
        @NotNull
        private final ControlsBindingController.LoadCallback callback;
        @NotNull
        private final List<Control> list;
        @NotNull
        private final IControlsSubscription subscription;
        final /* synthetic */ ControlsBindingControllerImpl this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public OnCancelAndLoadRunnable(@NotNull ControlsBindingControllerImpl this$0, @NotNull IBinder token, @NotNull List<Control> list, @NotNull IControlsSubscription subscription, ControlsBindingController.LoadCallback callback) {
            super(this$0, token);
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(token, "token");
            Intrinsics.checkNotNullParameter(list, "list");
            Intrinsics.checkNotNullParameter(subscription, "subscription");
            Intrinsics.checkNotNullParameter(callback, "callback");
            this.this$0 = this$0;
            this.list = list;
            this.subscription = subscription;
            this.callback = callback;
        }

        @Override // com.android.systemui.controls.controller.ControlsBindingControllerImpl.CallbackRunnable
        public void doRun() {
            Log.d("ControlsBindingControllerImpl", "LoadSubscription: Canceling and loading controls");
            ControlsProviderLifecycleManager provider = getProvider();
            if (provider != null) {
                provider.cancelSubscription(this.subscription);
            }
            this.callback.accept(this.list);
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    private final class OnSubscribeRunnable extends CallbackRunnable {
        private final long requestLimit;
        @NotNull
        private final IControlsSubscription subscription;
        final /* synthetic */ ControlsBindingControllerImpl this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public OnSubscribeRunnable(@NotNull ControlsBindingControllerImpl this$0, @NotNull IBinder token, IControlsSubscription subscription, long j) {
            super(this$0, token);
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(token, "token");
            Intrinsics.checkNotNullParameter(subscription, "subscription");
            this.this$0 = this$0;
            this.subscription = subscription;
            this.requestLimit = j;
        }

        @Override // com.android.systemui.controls.controller.ControlsBindingControllerImpl.CallbackRunnable
        public void doRun() {
            Log.d("ControlsBindingControllerImpl", "LoadSubscription: Starting subscription");
            ControlsProviderLifecycleManager provider = getProvider();
            if (provider == null) {
                return;
            }
            provider.startSubscription(this.subscription, this.requestLimit);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    public final class OnActionResponseRunnable extends CallbackRunnable {
        @NotNull
        private final String controlId;
        private final int response;
        final /* synthetic */ ControlsBindingControllerImpl this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public OnActionResponseRunnable(@NotNull ControlsBindingControllerImpl this$0, @NotNull IBinder token, String controlId, int i) {
            super(this$0, token);
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(token, "token");
            Intrinsics.checkNotNullParameter(controlId, "controlId");
            this.this$0 = this$0;
            this.controlId = controlId;
            this.response = i;
        }

        @NotNull
        public final String getControlId() {
            return this.controlId;
        }

        public final int getResponse() {
            return this.response;
        }

        @Override // com.android.systemui.controls.controller.ControlsBindingControllerImpl.CallbackRunnable
        public void doRun() {
            ControlsProviderLifecycleManager provider = getProvider();
            if (provider == null) {
                return;
            }
            ((ControlsController) this.this$0.lazyController.get()).onActionResponse(provider.getComponentName(), getControlId(), getResponse());
        }
    }

    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    private final class OnLoadErrorRunnable extends CallbackRunnable {
        @NotNull
        private final ControlsBindingController.LoadCallback callback;
        @NotNull
        private final String error;
        final /* synthetic */ ControlsBindingControllerImpl this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public OnLoadErrorRunnable(@NotNull ControlsBindingControllerImpl this$0, @NotNull IBinder token, @NotNull String error, ControlsBindingController.LoadCallback callback) {
            super(this$0, token);
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(token, "token");
            Intrinsics.checkNotNullParameter(error, "error");
            Intrinsics.checkNotNullParameter(callback, "callback");
            this.this$0 = this$0;
            this.error = error;
            this.callback = callback;
        }

        @NotNull
        public final String getError() {
            return this.error;
        }

        @Override // com.android.systemui.controls.controller.ControlsBindingControllerImpl.CallbackRunnable
        public void doRun() {
            this.callback.error(this.error);
            ControlsProviderLifecycleManager provider = getProvider();
            if (provider == null) {
                return;
            }
            Log.e("ControlsBindingControllerImpl", "onError receive from '" + provider.getComponentName() + "': " + getError());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ControlsBindingControllerImpl.kt */
    /* loaded from: classes.dex */
    public final class LoadSubscriber extends IControlsSubscriber.Stub {
        @Nullable
        private Function0<Unit> _loadCancelInternal;
        @NotNull
        private ControlsBindingController.LoadCallback callback;
        private final long requestLimit;
        private IControlsSubscription subscription;
        final /* synthetic */ ControlsBindingControllerImpl this$0;
        @NotNull
        private final ArrayList<Control> loadedControls = new ArrayList<>();
        @NotNull
        private AtomicBoolean isTerminated = new AtomicBoolean(false);

        public LoadSubscriber(@NotNull ControlsBindingControllerImpl this$0, ControlsBindingController.LoadCallback callback, long j) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(callback, "callback");
            this.this$0 = this$0;
            this.callback = callback;
            this.requestLimit = j;
        }

        @NotNull
        public final ControlsBindingController.LoadCallback getCallback() {
            return this.callback;
        }

        public final long getRequestLimit() {
            return this.requestLimit;
        }

        @NotNull
        public final ArrayList<Control> getLoadedControls() {
            return this.loadedControls;
        }

        @NotNull
        public final Runnable loadCancel() {
            return new Runnable() { // from class: com.android.systemui.controls.controller.ControlsBindingControllerImpl$LoadSubscriber$loadCancel$1
                @Override // java.lang.Runnable
                public final void run() {
                    Function0 function0;
                    function0 = ControlsBindingControllerImpl.LoadSubscriber.this._loadCancelInternal;
                    if (function0 == null) {
                        return;
                    }
                    Log.d("ControlsBindingControllerImpl", "Canceling loadSubscribtion");
                    function0.mo1951invoke();
                }
            };
        }

        public void onSubscribe(@NotNull IBinder token, @NotNull IControlsSubscription subs) {
            Intrinsics.checkNotNullParameter(token, "token");
            Intrinsics.checkNotNullParameter(subs, "subs");
            this.subscription = subs;
            this._loadCancelInternal = new ControlsBindingControllerImpl$LoadSubscriber$onSubscribe$1(this.this$0, this);
            this.this$0.backgroundExecutor.execute(new OnSubscribeRunnable(this.this$0, token, subs, this.requestLimit));
        }

        public void onNext(@NotNull final IBinder token, @NotNull final Control c) {
            Intrinsics.checkNotNullParameter(token, "token");
            Intrinsics.checkNotNullParameter(c, "c");
            DelayableExecutor delayableExecutor = this.this$0.backgroundExecutor;
            final ControlsBindingControllerImpl controlsBindingControllerImpl = this.this$0;
            delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsBindingControllerImpl$LoadSubscriber$onNext$1
                @Override // java.lang.Runnable
                public final void run() {
                    AtomicBoolean atomicBoolean;
                    IControlsSubscription iControlsSubscription;
                    atomicBoolean = ControlsBindingControllerImpl.LoadSubscriber.this.isTerminated;
                    if (atomicBoolean.get()) {
                        return;
                    }
                    ControlsBindingControllerImpl.LoadSubscriber.this.getLoadedControls().add(c);
                    if (ControlsBindingControllerImpl.LoadSubscriber.this.getLoadedControls().size() < ControlsBindingControllerImpl.LoadSubscriber.this.getRequestLimit()) {
                        return;
                    }
                    ControlsBindingControllerImpl.LoadSubscriber loadSubscriber = ControlsBindingControllerImpl.LoadSubscriber.this;
                    ControlsBindingControllerImpl controlsBindingControllerImpl2 = controlsBindingControllerImpl;
                    IBinder iBinder = token;
                    ArrayList<Control> loadedControls = loadSubscriber.getLoadedControls();
                    iControlsSubscription = ControlsBindingControllerImpl.LoadSubscriber.this.subscription;
                    if (iControlsSubscription != null) {
                        loadSubscriber.maybeTerminateAndRun(new ControlsBindingControllerImpl.OnCancelAndLoadRunnable(controlsBindingControllerImpl2, iBinder, loadedControls, iControlsSubscription, ControlsBindingControllerImpl.LoadSubscriber.this.getCallback()));
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("subscription");
                        throw null;
                    }
                }
            });
        }

        public void onError(@NotNull IBinder token, @NotNull String s) {
            Intrinsics.checkNotNullParameter(token, "token");
            Intrinsics.checkNotNullParameter(s, "s");
            maybeTerminateAndRun(new OnLoadErrorRunnable(this.this$0, token, s, this.callback));
        }

        public void onComplete(@NotNull IBinder token) {
            Intrinsics.checkNotNullParameter(token, "token");
            maybeTerminateAndRun(new OnLoadRunnable(this.this$0, token, this.loadedControls, this.callback));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void maybeTerminateAndRun(final Runnable runnable) {
            if (this.isTerminated.get()) {
                return;
            }
            this._loadCancelInternal = ControlsBindingControllerImpl$LoadSubscriber$maybeTerminateAndRun$1.INSTANCE;
            this.callback = ControlsBindingControllerImpl.emptyCallback;
            ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.this$0.currentProvider;
            if (controlsProviderLifecycleManager != null) {
                controlsProviderLifecycleManager.cancelLoadTimeout();
            }
            this.this$0.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.ControlsBindingControllerImpl$LoadSubscriber$maybeTerminateAndRun$2
                @Override // java.lang.Runnable
                public final void run() {
                    AtomicBoolean atomicBoolean;
                    atomicBoolean = ControlsBindingControllerImpl.LoadSubscriber.this.isTerminated;
                    atomicBoolean.compareAndSet(false, true);
                    runnable.run();
                }
            });
        }
    }
}
